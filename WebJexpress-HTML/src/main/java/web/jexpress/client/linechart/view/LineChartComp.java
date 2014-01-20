/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.linechart.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.plotOptions.LinePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.Marker;
import org.moxieapps.gwt.highcharts.client.plotOptions.SeriesPlotOptions;
import web.jexpress.client.core.model.SelectionManager;
import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.model.core.model.ModularizedListener;
import web.jexpress.shared.model.core.model.Selection;
//import web.jexpress.shared.model.core.model.SelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class LineChartComp extends ModularizedListener {
    
    private SelectionManager selectionManager;
    
    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows(datasetId);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                this.updateSelection(selectedRows);
            }
        }
    }

//    private Dataset dataset;
    private Chart chart;
    private VerticalPanel layout;
    private HorizontalPanel buttonsLayout;
    private LineChartResults results;
    
    public LineChartComp(LineChartResults results, SelectionManager selectionManager) {
        try {
            
            this.classtype = 3;
            this.datasetId = results.getDatasetId();
            this.components.add(LineChartComp.this);
            this.selectionManager = selectionManager;
            this.selectionManager.addSelectionChangeListener(datasetId, LineChartComp.this);
            
            layout = new VerticalPanel();
            layout.setHeight("300px");
            layout.setWidth("" + RootPanel.get("LineChartResults").getOffsetWidth() + "px");
            
            buttonsLayout = new HorizontalPanel();
            buttonsLayout.setWidth("" + RootPanel.get("LineChartResults").getOffsetWidth() + "px");
            buttonsLayout.setHeight("50px");            
            this.results = results;
            chart = new Chart()
                    .setType(Series.Type.LINE)
                    .setChartTitleText(" ")
                    .setMarginRight(5);            
            chart.setHeight(250);
            chart.setWidth(RootPanel.get("LineChartResults").getOffsetWidth());
            
            chart.setAnimation(false);
            chart.setLegend(new Legend().setEnabled(false));
            chart.setSeriesPlotOptions(new SeriesPlotOptions().setShowInLegend(false));
            chart.setLinePlotOptions(new LinePlotOptions()
                    .setLineWidth(1)
                    .setAnimation(false)
                    .setMarker(new Marker().setSymbol(Marker.Symbol.CIRCLE)
                            .setEnabled(false).setRadius(0.001)));

            //init data for line chart    
//            for (String color : results.getIndicesGroup().keySet()) {
//                Number[][] points = results.getIndicesGroup().get(color);
//                for (Number[] row : points) {
//                    Series series = chart.createSeries().setPoints(row);                    
//                    series.setPlotOptions(new LinePlotOptions()
//                            .setLineWidth(1)
//                            .setMarker(new Marker().setSymbol(Marker.Symbol.CIRCLE)
//                                    .setEnabled(false).setRadius(0.001)).setColor(color));
////            series.setName(""+x);
//                    chart.addSeries(series);
//                }
//                
//            }
        for (int x = 0; x < results.getGeneNames().length; x++) {
            Number[] points = results.getLineChartPoints()[x];
            Series series = chart.createSeries().setPoints(points);
            series.setPlotOptions(new LinePlotOptions()
                    .setLineWidth(1)
                    .setMarker(new Marker().setSymbol(Marker.Symbol.CIRCLE)
                    .setEnabled(false).setRadius(0.001)).setColor(results.getColours()[x]));
//            series.setName(""+x);
            chart.addSeries(series);

        }
            chart.redraw();
            
            layout.add(chart);
            layout.add(buttonsLayout);
            buttonsLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            buttonsLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            layout.add(buttonsLayout);
            layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            
            Selection sel = selectionManager.getSelectedRows(datasetId);            
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                this.updateSelection(selectedRows);
            }            
            
        } catch (Exception exp) {
            Window.alert(exp.getMessage());
        }
        
    }
    
    private void updateSelection(int[] selection) {
        if (selection != null && selection.length > 0) {
            chart.removeAllSeries();
//            for(Series ser:chart.getSeries())
//            {
//                ser.hide();
//                chart.addSeries(ser, true, false);
//            }
//            for (int x = 0; x < results.getGeneNames().length; x++) {
//                Series s = chart.getSeries("" + x);
//                s.hide();
//                chart.removeSeries(s);
//                chart.addSeries(s);
//            }
//            for (int x : selection) {
//                 Series s = chart.getSeries("" + x);
//                 s.show();
//                 chart.removeSeries(s);
//                 chart.addSeries(s);
//
//            }
//            chart.redraw();
//            selection = new int[dataset.getMemberMaps().size()];
//            int x = 0;
//            for (int key : dataset.getMemberMaps().keySet()) {
//                selection[x] = key;
//                x++;
//            }
//        }
//        chart.removeAllSeries();
            for (int x = 0; x < selection.length; x++) {
                Number[] points = results.getLineChartPoints()[selection[x]];
                Series series = chart.createSeries().setPoints(points);
                series.setPlotOptions(new LinePlotOptions()
                        //                        .setLineWidth(1)
                        //                        .setMarker(new Marker().setSymbol(Marker.Symbol.CIRCLE)
                        //                        .setEnabled(false).setRadius(0.001))
                        .setColor(results.getColours()[selection[x]]));
                
                chart.addSeries(series);
                
            }
            //chart.redraw();
        }
        
    }
    
    public VerticalPanel getLayout() {
        return layout;
    }
}
