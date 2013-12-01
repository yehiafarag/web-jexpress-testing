/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.server.model;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import no.uib.jexpress_modularized.core.computation.JMath;
import org.tc33.jheatchart.HeatChart;
import web.jexpress.shared.model.core.model.dataset.Dataset;

/**
 *
 * @author Y.M
 */
public class HeatMapGenerator {

    private String pass;
     private Dataset dataset;
     private JMath jmath = new JMath();
         private int distance = 2;   //the distance measure used (see expresscomponents.JMath.java)
 private double[][] tmpDist;
 
    private boolean distanceClustering = false;
    private boolean distanceMatrixOnly = false;
    int mode = 0;

    public String getPass() {
        return pass;
    }
    public HeatMapGenerator(String pass, Dataset dataset) {
        this.dataset= dataset;
        this.pass =pass;
        generateImage();
    }

    private void generateImage() {

        double[][] data = calcdistNoThread(false);
        for(int x =0;x<data.length;x++)
        {
            System.out.println("row "+x);
            System.out.println();
            double[] d = data[x];
            for(int y=0;y<d.length;y++ )
            {
                System.out.print(d[y]+" , ");
            }
            System.out.println("--------------------");
            if(x==3)
                break;
        }
                /*new double[][]{{3, 2, 3, 4, 5, 6},
            {2, 3, 4, 5, 6, 7},
            {3, 4, 5, 6, 7, 6},
            {4, 5, 6, 7, 6, 5}};*/
        System.out.println("start heat map processing ");
// Step 1: Create our heat map chart using our data.
        HeatChart map = new HeatChart(data);

// Step 2: Customise the chart.
        Object[] lab = dataset.getRowIds();
        Object[] col = dataset.getColumnIds();
        map.setYValues(lab);
        map.setXValues(col);
        int width = 50;//300/col.length;
        int hight = 30;//600/lab.length;
        map.setXValuesHorizontal(false);
       map.setCellSize(new Dimension(width, hight));
//        map.setAxisThickness(2);
////        map.setBackgroundColour(Color.BLACK);
//       map.set
     map.setShowYAxisValues(true);
//     map.setColourScale(5000);
       map.setHighValueColour(Color.RED);
////       map.setColourScale(1);
       map.setLowValueColour(Color.WHITE);
//        map.setTitle("This is my heat chart title");
//        map.setXAxisLabel("X Axis");
//        map.setYAxisLabel("Y Axis");

// Step 3: Output the chart to a file.
       
        try {
            File f =new File(pass,"java-heat-chart.png");
            f.createNewFile();
            pass = "java-heat-chart.png";
            
            map.saveToFile(f);
           // System.out.println("the pass is ---->> .> .>>> "+f.getAbsolutePath()+"  "+f.getCanonicalPath()+" "+f.getPath());
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
    
    public  double[][] calcdistNoThread(final boolean transposed) {

        jmath.setMetric(distance);

        double[][] dist = null;

        try {
            if (!transposed) {
                dist = makematrix(dataset.getDataLength());
            } else {
                dist = makematrix(dataset.getDataWidth());
            }

            if (!transposed) { //This is not the transposed version. Proceed without transposing.
                double[][] dst = dataset.getMeasurements();
//                boolean[][] nulls = dataset.getMissingMeasurements();
                int n = dist.length;

////                if (nulls != null) {   // nulls present, use the getnulls matrix.
////                    for (int i = 0; i < n; i++) {
////                        for (int j = 0; j < i; j++) {
////                            dist[i][j] = (float) jmath.dist(dst[i], dst[j], nulls[i], nulls[j]);
////
////                        }
////                    }
////                } else 
                {   //No nulls, do not use the getnulls matrix.
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < i; j++) {
                            dist[i][j] = (float) jmath.dist(dst[i], dst[j]);

                        }
                    }
                }

//                if (distanceClustering && clusterWay == ROWS) {
                    tmpDist = dist;
//                }
            } 
//            else 
//            { //This is the transposed version. Proceed without transposing.
//
//                double[][] trans = data.getData();
//                boolean[][] transnulls = data.getMissingMeasurements();
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
// for (int i = 0; i < n; i++) {
//                        for (int j = 0; j < i; j++) {
//                            dist[i][j] = (float) jmath.dist(dst[i], dst[j]);
//
//                        }
//                    }
//                }
//
//                if (distanceClustering && clusterWay == COLUMNS) {
//                    tmpDist = dist;
//                }
//
//            }
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
//                if (distanceClustering && clusterWay == COLUMNS) {
//                    tmpDist = dist;
//                }
//
//            }
        } catch (OutOfMemoryError er) {
            dist = null;
            System.gc();
        }

        controlDistanceMatrix(dist);
        

//        if (mode == 3) {
//            if (!distanceMatrixOnly) {
//                toproot = makeclusters(true, true, dist);
//            }
//
//            gocluster();
//            return;
//        }

//        if (mode != 1) {
//            if (!distanceMatrixOnly) {
//                trunk = makeclusters(true, false, dist);
//            }
//            if (!ClusterColumns) {
//                gocluster();
//            } else
//            {
//                mode = 3;
//                calcdistNoThread(true); //recursive call for the top component..
//            }
        
                return dist;

        }


    
    
//      public void drawScale(Graphics off, Point st, float width, float height) {
//
//        Rectangle r = new Rectangle(st.x, st.y, (int) width, (int) height);
//        if (off.getClipBounds() != null && !off.getClipBounds().intersects(r)) {
//            return;
//        }
//
//        if (width < 50 || height < 25) {
//            return;
//        }
//        ScaleAndAxis sc = new ScaleAndAxis();
//        sc.setColorFactory(ColorFactoryList.getInstance().getActiveColorFactory(data));
//        off.translate(st.x, st.y);
//        Rectangle bac = off.getClipBounds();
//        sc.setLocation(r.x, r.y);
//        sc.setSize(r.width, r.height + 10);
//        sc.paintComponent(off);
//        off.setClip(bac);
//        off.translate(-st.x, -st.y);
//
//    }
// public Image generateScale() {
//
//        Rectangle r = getScaleBounds();
//        float width = (float) r.width;
//        float height = (float) r.height;
//
//        BufferedImage buff = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
//        Graphics off = buff.getGraphics();
//        drawScale(off, new Point(0, 0), width, height);
//        return buff;
//    }
 public double[][] makematrix(int lengde) {
        double[][] inmatrix = new double[lengde][];
        for (int i = 0; i < lengde; i++) {
            inmatrix[i] = new double[i];
        }
        return inmatrix;
    }
 
 
    public void controlDistanceMatrix(double[][] dist) {

        double max = 0;
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[0].length; j++) {
                if (dist[i][j] > max) {
                    max = dist[i][j];
                }
            }
        }

        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[0].length; j++) {
                if (Double.isNaN(dist[i][j])) {
                    dist[i][j] = max;
                }
                if (Double.isInfinite(dist[i][j])) {
                    dist[i][j] = max;
                }
            }
        }

    }

}
