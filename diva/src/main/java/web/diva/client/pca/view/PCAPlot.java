/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.pca.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.ToolTipData;
import org.moxieapps.gwt.highcharts.client.ToolTipFormatter;
import org.moxieapps.gwt.highcharts.client.events.ChartSelectionEvent;
import org.moxieapps.gwt.highcharts.client.events.ChartSelectionEventHandler;
import org.moxieapps.gwt.highcharts.client.plotOptions.Marker;
import org.moxieapps.gwt.highcharts.client.plotOptions.ScatterPlotOptions;
import web.diva.shared.ModularizedListener;
import web.diva.shared.Selection;
import web.diva.shared.SelectionManager;
import web.diva.shared.beans.PCAPoint;
import web.diva.shared.beans.PCAResults;
//import web.jexpress.shared.model.core.model.SelectionManager;

/**
 *
 * @author Y.M
 */
public class PCAPlot extends ModularizedListener implements IsSerializable {

    private SelectionManager selectionManager;
    private boolean zoom = false;
    private boolean selectAll = false;
    private int[] prevSelection;

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows(datasetId);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                if (selectedRows != null && selectedRows.length != 0) {
                    updateChartSelection(selectedRows);
                }
            }
        }
    }
    private VerticalPanel layout;
    private HorizontalPanel buttonsLayout;
    private Chart chart;
    private CustomPoint[] points;
    private PCAResults results;

    public PCAPlot(final PCAResults results, SelectionManager selectionManager) {
        try {
            this.classtype = 3;
            this.datasetId = results.getDatasetId();
            this.components.add(PCAPlot.this);
            this.selectionManager = selectionManager;
            this.results = results;
            int pcx = results.getPcai();
            int pcy = results.getPcaii();
            this.selectionManager.addSelectionChangeListener(results.getDatasetId(), PCAPlot.this);
            points = this.initPoints(results.getPoints());


            layout = new VerticalPanel();
            layout.setHeight("300px");
            layout.setWidth(""+RootPanel.get("PCAChartResults").getOffsetWidth()+"px");
            
            
            buttonsLayout = new HorizontalPanel();
            buttonsLayout.setWidth(""+RootPanel.get("PCAChartResults").getOffsetWidth()+"px");
            buttonsLayout.setHeight("25px");
            

            chart = new Chart().setHeight(275).setWidth(RootPanel.get("PCAChartResults").getOffsetWidth())
                    .setType(Series.Type.SCATTER).setZoomType(Chart.ZoomType.X_AND_Y).setReflow(false)
                    .setLegend(new Legend().setLayout(Legend.Layout.VERTICAL)
                    .setAlign(Legend.Align.LEFT).setVerticalAlign(Legend.VerticalAlign.TOP)
                    .setX(100).setY(70).setBorderWidth(1).setFloating(true)
                    .setBackgroundColor("#FFFFFF"))
                    .setToolTip(new ToolTip()
                    .setFormatter(new ToolTipFormatter() {
                @Override
                public String format(ToolTipData toolTipData) {
                    return results.getHeader() + " : " + toolTipData.getPointName();
                }
            }))
                    .setScatterPlotOptions(new ScatterPlotOptions().setShadow(false)
                    .setAllowPointSelect(false)
                    .setShowInLegend(false).setMarker(new Marker().setFillColor("#FFFFFF").setRadius(2).setSymbol(Marker.Symbol.CIRCLE) )
                    .setEnableMouseTracking(true));
            chart.getXAxis()
                    .setAxisTitleText("Principal Component " + (pcx + 1))
                    .setStartOnTick(true)
                    .setEndOnTick(true)
                    .setShowLastLabel(true);
            chart.getYAxis().setAxisTitleText("Principal Component " + (pcy + 1));
            chart.showLoading("loading...");
            chart.setSelectionEventHandler(new ChartSelectionEventHandler() {
                @Override
                public boolean onSelection(ChartSelectionEvent chartSelectionEvent) {
                    if (!zoom && !selectAll) {
                        int[] selectedPoints = getSelection(chartSelectionEvent.getXAxisMax(), chartSelectionEvent.getXAxisMin(), chartSelectionEvent.getYAxisMax(), chartSelectionEvent.getYAxisMin());
                        updateSelectedList(selectedPoints);
                    }
                    return zoom;
                }
            });

            Selection sel = selectionManager.getSelectedRows(datasetId);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                if (selectedRows != null && selectedRows.length != 0) {
                    updateChartSelection(selectedRows);
                }
            } else {
                selectAll(false);
//                selectAll = true;
            }
            chart.setChartTitleText(" ");

            // Make a new check box, and select it by default.
            layout.add(chart);
            CheckBox cb = new CheckBox("Enable Zoom");
            cb.setChecked(false);    // Hook up a handler to find out when it's clicked.
            cb.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    zoom = ((CheckBox) event.getSource()).isChecked();
                }
            });
            buttonsLayout.add(cb);

            CheckBox cb2 = new CheckBox("Show All");
            cb2.setChecked(selectAll);    // Hook up a handler to find out when it's clicked.
            cb2.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    selectAll = ((CheckBox) event.getSource()).isChecked();
                    selectAll(selectAll);
                }
            });
            buttonsLayout.add(cb2);
            buttonsLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            buttonsLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            layout.add(buttonsLayout);
            layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
            layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
            

        } catch (Exception e) {
//            RootPanel.get().add(new Label("error" + e.getLocalizedMessage()));
        }

    }

    private int[] getSelection(double maxX, double minX, double maxY, double minY) {
        List<Integer> selectedPoints = new ArrayList<Integer>();
        for (CustomPoint cp : points) {
            if (cp.getX().doubleValue() >= minX && cp.getX().doubleValue() <= maxX && cp.getY().doubleValue() >= minY && cp.getY().doubleValue() <= maxY) {
                selectedPoints.add(cp.getGeneIndex());
            }
        }
        if (selectedPoints.size() > 0) {
            int[] selectedIndexes = new int[selectedPoints.size()];
            int index = 0;
            for (int select : selectedPoints) {
                selectedIndexes[index++] = select;
            }
            return selectedIndexes;
        }
        return null;


    }

    private void updateSelectedList(int[] selIndex) {
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selIndex);
        selectionManager.setSelectedRows(results.getDatasetId(), selection,classtype);
    }

    private void selectAll(boolean selAll) {
        for (int x = 0; x < points.length; x++) {
            CustomPoint cp = points[x];
            cp.setSelected(selAll);
        }
        if (!selAll) {
            if (prevSelection != null && prevSelection.length != 0) {
                for (int index : prevSelection) {
                    CustomPoint cp = points[index];
                    cp.setSelected(true);
                }

            }
            selectAll = selAll;

        }
        chart.removeAllSeries();
        chart.addSeries(chart.createSeries().setName("1").setPlotOptions(new ScatterPlotOptions().setMarker(new Marker()./*setFillColor("#FFFFFF").*/setRadius(2).setSymbol(Marker.Symbol.CIRCLE).setSelectState(new Marker().setRadius(3)))).setPoints(points));

    }

    private void updateChartSelection(int[] selectedIndex) {
        if (prevSelection != null && prevSelection.length != 0) {
            for (int index : prevSelection) {
                CustomPoint cp = points[index];
                cp.setSelected(false);
            }

        }
        prevSelection = selectedIndex;
        if(selectAll)
            return;
        if (selectedIndex != null && selectedIndex.length != 0) {
            for (int index : selectedIndex) {
                CustomPoint cp = points[index];
                cp.setSelected(true);
            }

            chart.removeAllSeries();
            chart.addSeries(chart.createSeries().setName("1").setPlotOptions(new ScatterPlotOptions().setMarker(new Marker()./*setFillColor("#FFFFFF").setRadius(2).*/setSymbol(Marker.Symbol.CIRCLE).setSelectState(new Marker().setRadius(3)))).setPoints(points));

        }

    }

    private CustomPoint[] initPoints(TreeMap<String, PCAPoint> pointTree) {

        CustomPoint[] tempPoints = new CustomPoint[pointTree.size()];
        for (String str : pointTree.keySet()) {
            PCAPoint pcaP = pointTree.get(str);
            CustomPoint point = new CustomPoint(pcaP.getX(), pcaP.getY());
            point.setColor(pcaP.getColor());
            point.setMarker(new Marker().setSelectState(new Marker().setFillColor(pcaP.getColor())).setFillColor("#D8D8D8").setHoverState(new Marker().setFillColor(pcaP.getColor())));
            point.setSelected(false);
           
            point.setGeneIndex(pcaP.getGeneIndex());
            point.setGeneName(pcaP.getGeneName());
            tempPoints[point.getGeneIndex()] = point;
        }
        return tempPoints;
    }

    public VerticalPanel getScatterPlotLayout() {
        return layout;
    }
}
