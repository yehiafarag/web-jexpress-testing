/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.pca.view;

  
import com.smartgwt.client.data.Record;  
  
public class ScatterPlotChartData extends Record {  
  
    private ScatterPlotChartData(float time, float value, String animal){  
        setAttribute("Time", time);  
        setAttribute("value", value);  
        setAttribute("animal", animal);  
    }  
  
    public static ScatterPlotChartData[] getData() {  
        return new ScatterPlotChartData[] {  
            new ScatterPlotChartData(0.033f, 0.02f, "Moose"),  
            new ScatterPlotChartData(0.083f, 0.15f, "Moose"),  
            new ScatterPlotChartData(0.25f, 0.77f, "Moose"),  
            new ScatterPlotChartData(0.25f, 0.77f, "Moose"),  
  
            new ScatterPlotChartData(0.5f, 0.87f, "Moose"),  
            new ScatterPlotChartData(1f, 1.15f, "Moose"),  
            new ScatterPlotChartData(2f, 1.15f, "Moose"),  
            new ScatterPlotChartData(4f, 0.71f, "Moose"),  
  
            new ScatterPlotChartData(5f, 0.67f, "Moose"),  
            new ScatterPlotChartData(6f, 0.61f, "Moose"),  
            new ScatterPlotChartData(7f, 0.41f, "Moose"),  
            new ScatterPlotChartData(8f, 0.22f, "Moose"),  
  
            new ScatterPlotChartData(0.033f, 0.02f, "Platypus"),  
            new ScatterPlotChartData(0.083f, 0.28f, "Platypus"),  
            new ScatterPlotChartData(0.25f, 0.71f, "Platypus"),  
            new ScatterPlotChartData(0.5f, 0.81f, "Platypus"),  
  
            new ScatterPlotChartData(1f, 1.06f, "Platypus"),  
            new ScatterPlotChartData(2f, 1.06f, "Platypus"),  
            new ScatterPlotChartData(4f, 0.52f, "Platypus"),  
            new ScatterPlotChartData(8f, 0.10f, "Platypus")  
        };  
    }  
  
}  