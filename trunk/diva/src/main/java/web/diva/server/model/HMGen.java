/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import org.tc33.jheatchart.HeatChart;
import web.diva.shared.beans.HeatMapImgResult;

/**
 *
 * @author Yehia Farag
 */
public class HMGen {

    private final String path;
    private final Dataset dataset;
    
    
    private final List<String> indexer;
    private final HeatMapImgResult results;
    private final List<String>colIndexer;

    public HMGen(String path, Dataset dataset,List<String> indexer,List<String>colIndexer) {
        this.dataset = dataset;
        this.path = path;
        this.indexer = indexer;
        this.colIndexer = colIndexer;
        this.results = new HeatMapImgResult();
        generateImage();
        

    }

    private String generateImage() {

        double[][] data = this.dataset.getData();// calcdistNoThread(false);
        double[][] sortedRowData = new double[data.length][];
        int [] reIndex = new int[indexer.size()];
        String[] geneNames = new String[indexer.size()];
        for (int x = 0; x < data.length; x++) {            
            double[] d = data[Integer.valueOf(indexer.get(x))]; 
            sortedRowData[x] = d;
            reIndex[x] = Integer.valueOf(indexer.get(x));
            geneNames[x] = this.dataset.getRowIds()[Integer.valueOf(indexer.get(x))];
        }
        
        double[][] sortedRowColData = new double[data.length][];
         int [] colReIndex = new int[colIndexer.size()];
        String[] colNames = new String[colIndexer.size()];
       
        for (int x = 0; x < data.length; x++) {            
            double[] d = sortedRowData[x]; 
            double[] sortedD = new double[d.length];
            for(int z=0;z<d.length;z++){
                sortedD[z] = d[Integer.valueOf(colIndexer.get(z))];
            }
            sortedRowColData[x] = sortedD;
        }
         for(int x=0;x<colIndexer.size();x++){
            colReIndex[x] = Integer.valueOf(colIndexer.get(x));
            colNames[x] = this.dataset.getColumnIds()[Integer.valueOf(colIndexer.get(x))];
            }
        
//        System.out.println("start heat map processing ");
// Step 1: Create our heat map chart using our data.
        HeatChart map = new HeatChart(sortedRowColData){

            @Override
            public void setHighValueColour(Color color) {
                super.setHighValueColour(color); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setLowValueColour(Color color) {
                super.setLowValueColour(color); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setColourScale(double d) {
                super.setColourScale(d); //To change body of generated methods, choose Tools | Templates.
            }
        };

// Step 2: Customise the chart.
        Object[] lab = this.dataset.getRowIds();
        Object[] col = this.dataset.getColumnIds();
        
        map.setYValues(lab);
         Object[] sortedCol = new Object[col.length];
         for(int x=0;x<col.length;x++)
         {
             sortedCol[x] = colNames[x];
         }
        map.setXValues(sortedCol);
        map.setShowYAxisValues(false);
        map.setShowXAxisValues(false);
        
        map.setHighValueColour(new Color(0,255,0));//(255,154,154));
        map.setColourScale(1.0);
        map.setLowValueColour(new Color(255,0,0));//(56,73,187));
        map.setBackgroundColour(Color.WHITE);
        map.setAxisThickness(0);
        map.setChartMargin(0);
       // Step 3: Output the chart to a file.
        results.setColNum(dataset.getDataWidth());
        results.setMaxColour("#00FF00");//"#FF0000");//("#FF9A9A");
        results.setMinColour("#FF0000");//"#3849BB");
        results.setMaxValue(map.getHighValue());
        results.setMinValue(map.getLowValue());
        results.setRowNum(data.length);
        results.setGeneReindex(reIndex);
        results.setGeneName(geneNames);
        results.setColNames(colNames);
        results.setColReindex(colReIndex);
        File img = new File(path, "java-heat-chart.png");
        String base64 = "";        
        try {
            img.createNewFile();
            map.saveToFile(img);
            // Reading a Image file from file system
            FileInputStream imageInFile = new FileInputStream(img);
            byte imageData[] = new byte[(int) img.length()];
            imageInFile.read(imageData);
            base64 = Base64.encode(imageData);
            base64 = "data:image/png;base64," + base64;
        } catch (Exception exp) {
            exp.printStackTrace();
        }        
      
        results.setImgString(base64);
        return base64;
    }



    public HeatMapImgResult getHeatMapResults() {
        return results;
    }
}
