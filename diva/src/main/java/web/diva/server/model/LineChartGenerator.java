/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Yehia Farag
 */
public class LineChartGenerator {

    private final JFreeImgGenerator imgGenerator;
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo(new StandardEntityCollection());

    public LineChartGenerator() {

        imgGenerator = new JFreeImgGenerator();
    }

    /**
     *
     * @return dataset.
     */
    private XYDataset createDataset(Number[][] pointsData, String[] geneNames, int[] selection) {

        final XYSeriesCollection dataset = new XYSeriesCollection();
        if (selection == null) {
            for (int x = 0; x < pointsData.length; x++) {
                XYSeries series = new XYSeries(geneNames[x] + x);
                Number[] data = pointsData[x];
                for (int y = 0; y < data.length; y++) {
                    series.add(y, data[y]);
                }
                dataset.addSeries(series);

            }
        } else {

            for (int x = 0; x < selection.length; x++) {
                XYSeries series = new XYSeries(geneNames[selection[x]]);
                Number[] data = pointsData[selection[x]];
                for (int y = 0; y < data.length; y++) {
                    series.add(y, data[y]);
                }
                dataset.addSeries(series);

            }

        }

        return dataset;

    }

    /**
     * Creates a chart.
     *
     * @param dataset the data for the chart.
     * @param lcr the line chart result
     *
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset, String[] colors, String[] columnIds, int[] selection) {

        final JFreeChart chart = ChartFactory.createXYLineChart(
                "", // chart title
                "", // x axis label
                "", // y axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                false, // include legend
                false, // tooltips
                false // urls
        );
        

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
       
       
 
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        
        if (selection == null) {
            for (int x = 0; x < colors.length; x++) {

                renderer.setSeriesShapesVisible(x, false);
                renderer.setSeriesPaint(x, imgGenerator.hex2Rgb(colors[x]));

            }
        } else {
            for (int x = 0; x < selection.length; x++) {

                renderer.setSeriesShapesVisible(x, false);
                renderer.setSeriesPaint(x, imgGenerator.hex2Rgb(colors[selection[x]]));

            }

        }

        plot.setRenderer(renderer);
       

        SymbolAxis rangeAxis = new SymbolAxis("", columnIds);
        rangeAxis.setGridBandsVisible(false);
        rangeAxis.setVerticalTickLabels(true);
        rangeAxis.setVisible(true); 
//        rangeAxis.setLabelFont(new Font("Arial", Font.PLAIN, 1));
        rangeAxis.setFixedDimension(51.0);
       
        
        boolean auto = rangeAxis.getAutoRangeIncludesZero();
        rangeAxis.setAutoRangeIncludesZero(true ^ auto);
        rangeAxis.setTickUnit(new NumberTickUnit(1));
        rangeAxis.setRange(0, columnIds.length);
       
        
     
        plot.setDomainAxis(rangeAxis);
      
     

        return chart;

    }

    public String generateChart(String path, Number[][] pointsData, String[] rowIds, String[] colors, String[] columnIds, int[] selection, String imgName,double width,double height) {
         
        final XYDataset dataset = createDataset(pointsData, rowIds, selection);
        final JFreeChart chart = createChart(dataset, colors, columnIds, selection);
        chartRenderingInfo.clear();
        String imgurl = imgGenerator.saveToFile(chart, width, height, path, chartRenderingInfo, imgName);

        return imgurl;
    }

}
