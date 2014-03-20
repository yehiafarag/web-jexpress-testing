/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.pca.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import web.diva.client.GreetingServiceAsync;
import web.diva.shared.ModularizedListener;
import web.diva.shared.Selection;
import web.diva.shared.SelectionManager;
import web.diva.shared.beans.PCAImageResults;
import web.diva.shared.beans.PCAPoint;

/**
 *
 * @author Yehia Farag
 */
public class PCAPlot extends ModularizedListener implements IsSerializable {

    private final SelectionManager selectionManager;
    private boolean zoom = false;
    private boolean selectAll = false;
    private final GreetingServiceAsync greetingService;

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows(datasetId);
            if (sel != null && !zoom && !selectAll) {
                int[] selectedRows = sel.getMembers();
                if (selectedRows != null && selectedRows.length != 0) {
                    updateSelection(selectedRows);
                }
            }
        }
    }
    private final VerticalPanel layout;
    private final VerticalPanel imgLayout;
    private final HorizontalPanel buttonsLayout;
    private PCAPoint[] selectionIndexMap;
    private Image chart;
    private final HTML toolTip = new HTML();

    public PCAPlot(final PCAImageResults results, SelectionManager selectionManager, GreetingServiceAsync greetingService) {

        this.greetingService = greetingService;
        this.classtype = 3;
        this.datasetId = results.getDatasetId();
        this.components.add(PCAPlot.this);
        this.selectionManager = selectionManager;
        this.selectionManager.addSelectionChangeListener(results.getDatasetId(), PCAPlot.this);
        layout = new VerticalPanel();
        layout.setHeight("300px");
        layout.setWidth("" + RootPanel.get("PCAChartResults").getOffsetWidth() + "px");
        layout.setBorderWidth(1);
        RootPanel.get("tooltip").add(toolTip);
        imgLayout = new VerticalPanel();
        imgLayout.setHeight(275 + "px");
        imgLayout.setWidth(RootPanel.get("PCAChartResults").getOffsetWidth() + "px");

        buttonsLayout = new HorizontalPanel();
        buttonsLayout.setWidth("" + RootPanel.get("PCAChartResults").getOffsetWidth() + "px");
        buttonsLayout.setHeight("25px");

        // Make a new check box, and select it by default.
        layout.add(imgLayout);
        CheckBox cb = new CheckBox("Zoom");

        cb.setChecked(false);    // Hook up a handler to find out when it's clicked.
        cb.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                zoom = ((CheckBox) event.getSource()).isChecked();
                if (!zoom) {
                    updateWithSelection();
                }

            }
        });
        buttonsLayout.add(cb);

        CheckBox cb2 = new CheckBox("Show All");
        cb2.setChecked(selectAll);    // Hook up a handler to find out when it's clicked.
        cb2.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectAll = ((CheckBox) event.getSource()).isChecked();
                if (!selectAll) {
                    updateWithSelection();
                } else {
                    updateSelection(null);
                }

            }
        });
        buttonsLayout.setBorderWidth(1);
        buttonsLayout.add(cb2);
        buttonsLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        buttonsLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        layout.add(buttonsLayout);
        layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        updateWithSelection();

    }

    private void updateWithSelection() {
        Selection sel = selectionManager.getSelectedRows(datasetId);
        if (sel != null) {
            int[] selectedRows = sel.getMembers();
            this.updateSelection(selectedRows);
        } else {
            this.updateSelection(null);
        }

    }

    private int[] getSelection(double maxX, double minX, double maxY, double minY) {
        List<Integer> selectedPoints = new ArrayList<Integer>();
        for (PCAPoint cp : selectionIndexMap) {
            if (cp.getX() >= minX && cp.getX() <= maxX && cp.getY() >= minY && cp.getY() <= maxY) {
                selectedPoints.add(cp.getGeneIndex());
            }
        }
        String s = "";
        if (selectedPoints.size() > 0) {
            int[] selectedIndexes = new int[selectedPoints.size()];
            int index = 0;
            for (int select : selectedPoints) {
                selectedIndexes[index++] = select;
                s += select;
            }
            return selectedIndexes;
        }
        return null;

    }

    private void updateSelectedList(int[] selIndex) {
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selIndex);
        selectionManager.setSelectedRows(datasetId, selection, classtype);
    }

    public VerticalPanel getScatterPlotLayout() {
        return layout;
    }

    private void updateSelection(int[] selection) {
        greetingService.updatePCASelection(datasetId, selection, zoom, selectAll,RootPanel.get("PCAChartResults").getOffsetWidth() ,300.0, new AsyncCallback<PCAImageResults>() {
            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(PCAImageResults result) {
                if (chart != null) {
                    imgLayout.remove(chart);
                    chart = null;

                }
                chart = new Image(result.getImgString());
                chart.setHeight(275 + "px");
                chart.setWidth(RootPanel.get("PCAChartResults").getOffsetWidth() + "px");
                imgLayout.add(chart);
                selectionIndexMap = result.getIndexeMap();
                initChartImage(result.getXyName());
                RootPanel.get("loaderImage").setVisible(false);
            }
        });

    }

    private MouseMoveHandler mouseMoveHandler;
    private MouseOutHandler mouseOutHandler;
    private double startX;
    private double endX;
    private double startY;
    private double endY;

    private void initChartImage(final HashMap<String, String> tooltips) {

        mouseMoveHandler = new MouseMoveHandler() {
            @Override
            public void onMouseMove(MouseMoveEvent event) {
                double yi = event.getY();
                double xi = event.getX();

                int x = (int) xi;
                int y = (int) yi;
                String key = x + "," + y;
                String key1 = (x + 1) + "," + y;
                String key2 = (x - 1) + "," + y;
                String key3 = x + "," + (y + 1);
                String key4 = x + "," + (y - 1);
                if (tooltips.containsKey(key)) {
                    toolTip.setHTML("<p style='font-weight: bold; color:white;font-size: 15px;background: #819FF7; border-style:double;'>" + tooltips.get(key) + "</p>");
                    toolTip.setVisible(true);
                } else if (tooltips.containsKey(key1)) {
                    toolTip.setHTML("<p style='font-weight: bold; color:white;font-size: 15px;background: #819FF7; border-style:double;'>" + tooltips.get(key1) + "</p>");
                    toolTip.setVisible(true);
                } else if (tooltips.containsKey(key2)) {
                    toolTip.setHTML("<p style='font-weight: bold; color:white;font-size: 15px;background: #819FF7; border-style:double;'>" + tooltips.get(key2) + "</p>");
                    toolTip.setVisible(true);
                } else if (tooltips.containsKey(key3)) {
                    toolTip.setHTML("<p style='font-weight: bold; color:white;font-size: 15px;background: #819FF7; border-style:double;'>" + tooltips.get(key3) + "</p>");
                    toolTip.setVisible(true);
                } else if (tooltips.containsKey(key4)) {
                    toolTip.setHTML("<p style='font-weight: bold; color:white;font-size: 15px;background: #819FF7; border-style:double;'>" + tooltips.get(key4) + "</p>");
                    toolTip.setVisible(true);
                } else {
                    toolTip.setText("");
                    toolTip.setVisible(false);
                }
            }
        };

        mouseOutHandler = new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                toolTip.setText("");
                toolTip.setVisible(false);
            }
        };

        this.chart.addMouseMoveHandler(mouseMoveHandler);
        this.chart.addMouseOutHandler(mouseOutHandler);
        chart.addMouseUpHandler(new MouseUpHandler() {

            @Override
            public void onMouseUp(MouseUpEvent event) {
                endX = event.getX();
                endY = event.getY();
                //update 
                int[] selection = getSelection(endX, startX, endY, startY);
                if (selection != null && !zoom) {
                    updateSelectedList(selection);
                } else if (selection != null && zoom) {
                    updateSelection(selection);

                }
            }
        });

        chart.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                startX = event.getX();
                startY = event.getY();
                event.preventDefault();

            }
        });

    }
}
