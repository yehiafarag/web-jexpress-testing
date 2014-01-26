/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.server.model;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import no.uib.jexpress_modularized.core.computation.JMath;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import org.tc33.jheatchart.HeatChart;
import web.jexpress.shared.beans.ImgResult;

/**
 *
 * @author Yehia Farag
 */
public class HMGen {

    private String pass;
    private final Dataset dataset;
    private final JMath jmath = new JMath();
    private int distance = 2;   //the distance measure used (see expresscomponents.JMath.java)
    private double[][] tmpDist;
    private boolean distanceClustering = false;
    private boolean distanceMatrixOnly = false;
    private int mode = 0;
    private String heatMapString;
    private List<String> indexer;
    private ImgResult results;

    public HMGen(String pass, Dataset dataset,List<String> indexer) {
        this.dataset = dataset;
        this.pass = pass;
        this.indexer = indexer;
        this.results = new ImgResult();
        this.heatMapString = generateImage();
        

    }

    private String generateImage() {

        double[][] data = this.dataset.getData();// calcdistNoThread(false);
        double[][] sortedData = new double[data.length][];
        int [] reIndex = new int[indexer.size()];
        String[] geneNames = new String[indexer.size()];
        for (int x = 0; x < data.length; x++) {            
            double[] d = data[Integer.valueOf(indexer.get(x))]; 
            sortedData[x] = d;
            reIndex[x] = Integer.valueOf(indexer.get(x));
            geneNames[x] = this.dataset.getRowIds()[Integer.valueOf(indexer.get(x))];
        }
        
//        System.out.println("start heat map processing ");
// Step 1: Create our heat map chart using our data.
        HeatChart map = new HeatChart(sortedData){

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
             sortedCol[col.length-1-x] = col[x];
         }
        map.setXValues(sortedCol);
        map.setShowYAxisValues(false);
        map.setShowXAxisValues(false);
        
        map.setHighValueColour(new Color(255,154,154));
        map.setColourScale(1.0);
        map.setLowValueColour(new Color(56,73,187));
        map.setBackgroundColour(Color.WHITE);
        map.setAxisThickness(0);
        map.setChartMargin(0);
       // Step 3: Output the chart to a file.
        results.setColNum(dataset.getDataWidth());
        results.setMaxColour("#FF9A9A");
        results.setMinColour("#3849BB");
        results.setMaxValue(map.getHighValue());
        results.setMinValue(map.getLowValue());
        results.setRowNum(data.length);
        results.setGeneReindex(reIndex);
        results.setGeneName(geneNames);
        results.setColNames(dataset.getColumnIds());
        File img = new File(/*pass*/"D:\\files", "java-heat-chart.png");
        String base64 = "";        
        try {
            img.createNewFile();
            map.saveToFile(img);
            // Reading a Image file from file system
            FileInputStream imageInFile = new FileInputStream(img);
            byte imageData[] = new byte[(int) img.length()];
            imageInFile.read(imageData);
            base64 = Base64.encode(imageData);
//com.google.gwt.user.server.Base64Utils.toBase64(imageData); 
            base64 = "data:image/png;base64," + base64;
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        results.setImgString(base64);
        return base64;
    }

//    public double[][] calcdistNoThread(final boolean transposed) {
//
//        jmath.setMetric(distance);
//
//        double[][] dist = null;
//
//        try {
//            if (!transposed) {
//                dist = makematrix(dataset.getDataLength());
//            } else {
//                dist = makematrix(dataset.getDataWidth());
//            }
//
//            if (!transposed) { //This is not the transposed version. Proceed without transposing.
//                double[][] dst = dataset.getData();
//                boolean[][] nulls = dataset.getMissingMeasurements();
//                int n = dist.length;
//
//                if (nulls != null) {   // nulls present, use the getnulls matrix.
//                    for (int i = 0; i < n; i++) {
//                        for (int j = 0; j < i; j++) {
//                            dist[i][j] = (float) jmath.dist(dst[i], dst[j], nulls[i], nulls[j]);
//
//                        }
//                    }
//                } else {   //No nulls, do not use the getnulls matrix.
//                    for (int i = 0; i < n; i++) {
//                        for (int j = 0; j < i; j++) {
//                            dist[i][j] = (float) jmath.dist(dst[i], dst[j]);
//
//                        }
//                    }
//                }
//
//                if (distanceClustering) {
//                    tmpDist = dist;
//                }
//            } else { //This is the transposed version. Proceed without transposing.
//
//                double[][] trans = dataset.getData();
//                boolean[][] transnulls = dataset.getMissingMeasurements();
//
//                double[][] dst = new double[trans[0].length][trans.length];
//                boolean[][] nulls = null;
//
//                boolean nullsPresent = false;
//                if (transnulls != null) {
//                    nulls = new boolean[transnulls[0].length][transnulls.length];
//                    nullsPresent = true;
//                }
//
//                for (int i = 0; i < trans.length; i++) {
//                    for (int j = 0; j < trans[0].length; j++) {
//                        dst[j][i] = trans[i][j];
//
//                        if (nullsPresent) {
//                            nulls[j][i] = transnulls[i][j];
//                        }
//                    }
//                }
//
//                int n = dist.length;
//
//                if (nulls != null) {   // nulls present, use the getnulls matrix.
//                    for (int i = 0; i < n; i++) {
//                        for (int j = 0; j < i; j++) {
//                            dist[i][j] = (float) jmath.dist(dst[i], dst[j], nulls[i], nulls[j]);
//
//                        }
//                    }
//                } else {   //No nulls, do not use the getnulls matrix.
//                    for (int i = 0; i < n; i++) {
//                        for (int j = 0; j < i; j++) {
//                            dist[i][j] = (float) jmath.dist(dst[i], dst[j]);
//
//                        }
//                    }
//                }
//
//            }
//        } catch (OutOfMemoryError er) {
//            dist = null;
//            System.gc();
//        }
//
//        controlDistanceMatrix(dist);
//
//        if (mode == 3) {
////            if (!distanceMatrixOnly) {
////                toproot = makeclusters(true, true, dist);
////            }
////
////            gocluster();
////            return;
//        }
//
//        if (mode != 1) {
////            if (!distanceMatrixOnly) {
////                trunk = makeclusters(true, false, dist);
////            }
////            if (!ClusterColumns) {
////                gocluster();
////            } else {
////                mode = 3;
////                calcdistNoThread(true); //recursive call for the top component..
////            }
//        }
//        return dist;
//
//
//    }
//
//    private double[][] makematrix(int lengde) {
//        double[][] inmatrix = new double[lengde][];
//        for (int i = 0; i < lengde; i++) {
//            inmatrix[i] = new double[i];
//        }
//        return inmatrix;
//    }
//
//    private void controlDistanceMatrix(double[][] dist) {
//
//        double max = 0;
//        for (int i = 0; i < dist.length; i++) {
//            for (int j = 0; j < dist[0].length; j++) {
//                if (dist[i][j] > max) {
//                    max = dist[i][j];
//                }
//            }
//        }
//
//        for (int i = 0; i < dist.length; i++) {
//            for (int j = 0; j < dist[0].length; j++) {
//                if (Double.isNaN(dist[i][j])) {
//                    dist[i][j] = max;
//                }
//                if (Double.isInfinite(dist[i][j])) {
//                    dist[i][j] = max;
//                }
//            }
//        }
//
//    }
//
//    public Node clonetree(Node trunk) {
//
//        return (Node) trunk.clone();
//
//    }

    

    public ImgResult getHeatMapResults() {
        return results;
    }
}
