/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.linechart.view;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.plotOptions.LinePlotOptions;
import org.moxieapps.gwt.highcharts.client.plotOptions.Marker;
import org.moxieapps.gwt.highcharts.client.plotOptions.SeriesPlotOptions;
import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.model.core.model.ModularizedListener;
import web.jexpress.shared.model.core.model.Selection;
import web.jexpress.shared.model.core.model.SelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class LineChartComp  extends ModularizedListener  {

    private SelectionManager selectionManager;
    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
           Selection sel =selectionManager.getSelectedRows(datasetId);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                this.updateSelection(selectedRows);
            }
     }
    }

//    private Dataset dataset;
     private Chart chart;
     private LineChartResults results;
    public LineChartComp(LineChartResults results,SelectionManager selectionManager)
    {
         this.classtype=3;
        this.datasetId = results.getDatasetId();
        this.components.add(LineChartComp.this);
        this.selectionManager = selectionManager;
        this.selectionManager.addSelectionChangeListener(datasetId, LineChartComp.this);

        this.results = results;

        chart = new Chart()
                .setType(Series.Type.LINE)
                .setChartTitleText("Line Chart")
                .setMarginRight(10);  
        chart.setHeight(300);
        chart.setWidth(500);
        
        chart.setAnimation(false);
        chart.setLegend(new Legend().setEnabled(false));
        chart.setSeriesPlotOptions(new SeriesPlotOptions().setShowInLegend(false));
        chart.setLinePlotOptions(new LinePlotOptions()
                .setLineWidth(1)
                .setAnimation(false)
                .setMarker(new Marker().setSymbol(Marker.Symbol.CIRCLE)
                .setEnabled(false).setRadius(0.001)));

        //init data for line chart        
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

         
        
        
        Selection sel =selectionManager.getSelectedRows(datasetId);           
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                this.updateSelection(selectedRows);
            }         
                
    }
    public Chart getChart()
    {
        return chart;
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
}
