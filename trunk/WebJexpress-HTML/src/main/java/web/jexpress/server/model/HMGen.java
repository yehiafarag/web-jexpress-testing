/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.server.model;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import no.uib.jexpress_modularized.core.computation.JMath;
import no.uib.jexpress_modularized.core.dataset.DataSet;
import no.uib.jexpress_modularized.somclust.model.Node;
import org.tc33.jheatchart.HeatChart;

/**
 *
 * @author Y.M
 */
public class HMGen {

    private String pass;
    private DataSet data;
    private JMath jmath = new JMath();
    private int distance = 2;   //the distance measure used (see expresscomponents.JMath.java)
    private double[][] tmpDist;
    private boolean distanceClustering = false;
    private boolean distanceMatrixOnly = false;
    int mode = 0;

    public HMGen(String pass, DataSet dataset) {
        this.data = dataset;
        this.pass = pass;
        generateImage();

    }

    private void generateImage() {

        double[][] data = this.data.getData();// calcdistNoThread(false);
        for (int x = 0; x < data.length; x++) {
            System.out.println("row " + x);
            System.out.println();
            double[] d = data[x];
            for (int y = 0; y < d.length; y++) {
                System.out.print(d[y] + " , ");
            }
            System.out.println("--------------------");
            if (x == 3) {
                break;
            }
        }
        /*new double[][]{{3, 2, 3, 4, 5, 6},
         {2, 3, 4, 5, 6, 7},
         {3, 4, 5, 6, 7, 6},
         {4, 5, 6, 7, 6, 5}};*/
        System.out.println("start heat map processing ");
// Step 1: Create our heat map chart using our data.
        HeatChart map = new HeatChart(data){

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
        Object[] lab = this.data.getRowIds();
        Object[] col = this.data.getColumnIds();
        map.setYValues(lab);
        map.setXValues(col);
        int width = 10;//300/col.length;
        int hight = 10;//600/lab.length;
        map.setXValuesHorizontal(false);
        map.setCellSize(new Dimension(width, hight));
//        map.setAxisThickness(2);
////        map.setBackgroundColour(Color.BLACK);
//       map.set
        map.setShowYAxisValues(true);
//     map.setColourScale(5000);
        map.setHighValueColour(Color.GREEN);
       map.setColourScale(0.1);
        map.setLowValueColour(Color.BLUE);
     
        System.out.println("loma lomaa ---->>>>>>>>>>>>>>>>>>>>>> "+map.getColourScale());
//        map.setTitle("This is my heat chart title");
//        map.setXAxisLabel("X Axis");
//        map.setYAxisLabel("Y Axis");

// Step 3: Output the chart to a file.

        try {
            File f = new File(pass, "java-heat-chart.png");
            f.createNewFile();
            pass = "java-heat-chart.png";

            map.saveToFile(f);
            // System.out.println("the pass is ---->> .> .>>> "+f.getAbsolutePath()+"  "+f.getCanonicalPath()+" "+f.getPath());
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public double[][] calcdistNoThread(final boolean transposed) {

        jmath.setMetric(distance);

        double[][] dist = null;

        try {
            if (!transposed) {
                dist = makematrix(data.getDataLength());
            } else {
                dist = makematrix(data.getDataWidth());
            }

            if (!transposed) { //This is not the transposed version. Proceed without transposing.
                double[][] dst = data.getData();
                boolean[][] nulls = data.getMissingMeasurements();
                int n = dist.length;

                if (nulls != null) {   // nulls present, use the getnulls matrix.
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < i; j++) {
                            dist[i][j] = (float) jmath.dist(dst[i], dst[j], nulls[i], nulls[j]);

                        }
                    }
                } else {   //No nulls, do not use the getnulls matrix.
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < i; j++) {
                            dist[i][j] = (float) jmath.dist(dst[i], dst[j]);

                        }
                    }
                }

                if (distanceClustering) {
                    tmpDist = dist;
                }
            } else { //This is the transposed version. Proceed without transposing.

                double[][] trans = data.getData();
                boolean[][] transnulls = data.getMissingMeasurements();

                double[][] dst = new double[trans[0].length][trans.length];
                boolean[][] nulls = null;

                boolean nullsPresent = false;
                if (transnulls != null) {
                    nulls = new boolean[transnulls[0].length][transnulls.length];
                    nullsPresent = true;
                }

                for (int i = 0; i < trans.length; i++) {
                    for (int j = 0; j < trans[0].length; j++) {
                        dst[j][i] = trans[i][j];

                        if (nullsPresent) {
                            nulls[j][i] = transnulls[i][j];
                        }
                    }
                }

                int n = dist.length;

                if (nulls != null) {   // nulls present, use the getnulls matrix.
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < i; j++) {
                            dist[i][j] = (float) jmath.dist(dst[i], dst[j], nulls[i], nulls[j]);

                        }
                    }
                } else {   //No nulls, do not use the getnulls matrix.
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < i; j++) {
                            dist[i][j] = (float) jmath.dist(dst[i], dst[j]);

                        }
                    }
                }

            }
        } catch (OutOfMemoryError er) {
            dist = null;
            System.gc();
        }

        controlDistanceMatrix(dist);

        if (mode == 3) {
//            if (!distanceMatrixOnly) {
//                toproot = makeclusters(true, true, dist);
//            }
//
//            gocluster();
//            return;
        }

        if (mode != 1) {
//            if (!distanceMatrixOnly) {
//                trunk = makeclusters(true, false, dist);
//            }
//            if (!ClusterColumns) {
//                gocluster();
//            } else {
//                mode = 3;
//                calcdistNoThread(true); //recursive call for the top component..
//            }
        }
        return dist;


    }

    private double[][] makematrix(int lengde) {
        double[][] inmatrix = new double[lengde][];
        for (int i = 0; i < lengde; i++) {
            inmatrix[i] = new double[i];
        }
        return inmatrix;
    }

    private void controlDistanceMatrix(double[][] dist) {

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

    public Node clonetree(Node trunk) {

        return (Node) trunk.clone();

    }

    public String getPass() {
        return pass;
    }
}
