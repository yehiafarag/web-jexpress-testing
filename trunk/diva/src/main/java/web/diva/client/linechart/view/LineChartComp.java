/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.linechart.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import web.diva.client.GreetingServiceAsync;
import web.diva.shared.ModularizedListener;
import web.diva.shared.Selection;
import web.diva.shared.SelectionManager;
import web.diva.shared.beans.LineChartResults;
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
//    private Chart chart;
    private VerticalPanel layout;
//    private HorizontalPanel buttonsLayout;
////    private LineChartResults results;
     private Image image;
     private final GreetingServiceAsync greetingService;
    
    public LineChartComp(LineChartResults results, SelectionManager selectionManager,GreetingServiceAsync greetingService) {
        this.greetingService =  greetingService;
        try {
            
            this.classtype = 3;
            this.datasetId = results.getDatasetId();
            this.components.add(LineChartComp.this);
            this.selectionManager = selectionManager;
            this.selectionManager.addSelectionChangeListener(datasetId, LineChartComp.this);
            
            layout = new VerticalPanel();
            layout.setHeight("300px");
            layout.setWidth("" + RootPanel.get("LineChartResults").getOffsetWidth() + "px");
            
//            buttonsLayout = new HorizontalPanel();
//            buttonsLayout.setWidth("" + RootPanel.get("LineChartResults").getOffsetWidth() + "px");
//            buttonsLayout.setHeight("50px");            
//            this.results = results;
            
//            chart = new Chart()
//                    .setType(Series.Type.LINE)
//                    .setChartTitleText(" ")
//                    .setMarginRight(5);            
//            chart.setHeight(300);
//            chart.setTitle("Line Chart");
//            chart.setWidth(RootPanel.get("LineChartResults").getOffsetWidth());
//            chart.getXAxis().setAxisTitleText("");
//            chart.getYAxis().setAxisTitleText("");
//            chart.setAnimation(false);
//            chart.setLegend(new Legend().setEnabled(false));
//            chart.setSeriesPlotOptions(new SeriesPlotOptions().setShowInLegend(false));
//            chart.setLinePlotOptions(new LinePlotOptions()
//                    .setLineWidth(1)
//                    .setShadow(false)
//                    .setAnimation(false)
//                    .setMarker(new Marker().setSymbol(Marker.Symbol.CIRCLE)
//                            .setEnabled(false).setRadius(0.001)));
//
//        for (int x = 0; x < results.getGeneNames().length; x++) {
//            Number[] points = results.getLineChartPoints()[x];
//            Series series = chart.createSeries().setPoints(points);
//            series.setPlotOptions(new LinePlotOptions()
//                    .setLineWidth(1).setShadow(false)
//                    .setMarker(new Marker().setSymbol(Marker.Symbol.CIRCLE).setFillColor("#BDBDBD")
//                    .setEnabled(false).setRadius(0.001)).setColor(results.getColours()[x]));
//            chart.addSeries(series);
//
//        }
//            chart.redraw();
            
           
//            layout.add(buttonsLayout);
//            buttonsLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//            buttonsLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//            layout.add(buttonsLayout);
            
            
            Selection sel = selectionManager.getSelectedRows(datasetId);            
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                this.updateSelection(selectedRows);
            }else{
                initImage(results.getImageString());
            }            
            
        } catch (Exception exp) {
            Window.alert(exp.getMessage());
        }
        
    }
    
    private void updateSelection(int[] selection) {
        if (selection != null && selection.length > 0) {            
             greetingService.updateLineChartSelection(datasetId, selection,new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(String result) {
                        initImage(result);
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
//            chart.removeAllSeries();
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
//            for (int x = 0; x < selection.length; x++) {
//                Number[] points = results.getLineChartPoints()[selection[x]];
//                Series series = chart.createSeries().setPoints(points);
//                series.setPlotOptions(new LinePlotOptions().setShadow(false)
//                        //                        .setLineWidth(1)
//                        //                        .setMarker(new Marker().setSymbol(Marker.Symbol.CIRCLE)
//                        //                        .setEnabled(false).setRadius(0.001))
//                        .setColor(results.getColours()[selection[x]]));
//                
//                chart.addSeries(series);
//                
//            }
            //chart.redraw();
        }
        
    }
    
    public VerticalPanel getLayout() {
        return layout;
    }
    
    private void initImage(String url){ 
        if(image != null ){
        layout.remove(image);
        image = null;
        }
        image = new Image(url);
        image.setHeight("300px");
        image.setWidth(RootPanel.get("LineChartResults").getOffsetWidth()+"px");
        layout.add(image);
        layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    
    }
}
