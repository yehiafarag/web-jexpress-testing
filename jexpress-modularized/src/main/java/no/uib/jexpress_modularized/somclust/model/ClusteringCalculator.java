/*
 * this class will be responsable for clustering calculations 
 */
package no.uib.jexpress_modularized.somclust.model;

import java.io.Serializable;
import java.util.List;
import no.uib.jexpress_modularized.core.computation.JMath;
import no.uib.jexpress_modularized.core.dataset.Dataset;

/**
 *
 * @author Yehia Farag
 */
public class ClusteringCalculator implements Serializable {

    private boolean cancelled = false;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    private Node[] globalclustergroups;
    private Dataset dataSet;
    private Node master, topmaster;
    private List<Neuron> neuronlist;
    private ClusterParameters.LINKAGE LINK;
    private int distance;
    private boolean clusterColumns;
    private List<double[][]> NoNullDataGroups;
    private List<int[]> NoNullDataIndices;

    public ClusteringCalculator(Dataset dataSet, List<Neuron> neuronlist, List<double[][]> NoNullDataGroups, List<int[]> NoNullDataIndices, Node[] globalclustergroups, Node master, Node topmaster, ClusterParameters.LINKAGE LINK, int distance, boolean clusterColumns) {
        this.dataSet = dataSet;
        this.neuronlist = neuronlist;
        this.NoNullDataGroups = NoNullDataGroups;
        this.NoNullDataIndices = NoNullDataIndices;
        this.globalclustergroups = globalclustergroups;
        this.master = master;
        this.topmaster = topmaster;
        this.LINK = LINK;
        this.distance = distance;
        this.clusterColumns = clusterColumns;


    }

    /**
     * Executes the clustering algorithm. Takes time.
     *
     * @return #ClusterResults which is an object representing clustering
     * results
     */
    public ClusterResults runClustering() {

        for (int i = 0; i < neuronlist.size(); i++) {
            Thread th = createDistanceThread(i, NoNullDataGroups.get(i), NoNullDataIndices.get(i));
            th.start();
            if (cancelled) {
                return null;
            }
        }


        while (getProgress() < 100 && !cancelled) {
            try {
                Thread.sleep(500);
            } catch (Exception ex) {
            }
        }
        if (cancelled) {
            return null;
        }

        return createResult();
    }

    public int getProgress() {
        int cnt = 0;
        for (int i = 0; i < globalclustergroups.length; i++) {
            if (globalclustergroups[i] != null) {
                cnt++;
            }
        }
        return (cnt * 100) / globalclustergroups.length;
    }

    /**
     * Cancel the clustering when in progress
     */
    public void cancelClustering() {
        cancelled = true;
    }

    private ClusterResults createResult() {

        int[] internalnodes = new int[neuronlist.size()];
        for (int i = 0; i < internalnodes.length; i++) {
            internalnodes[i] = i;
        }

        float[][] distanceM = new float[neuronlist.size()][neuronlist.size()];
        JMath jmath = new JMath();
        jmath.setMetric(distance);

        for (int i = 0; i < distanceM.length; i++) {
            for (int j = 0; j < i; j++) {
                distanceM[i][j] = (float) jmath.dist(neuronlist.get(i).vector, neuronlist.get(j).vector);
            }
        }

        //finally cluster the groups themselves..
        HClustFactory factory = new HClustFactory();
        master = factory.makeclusters(distanceM, internalnodes, LINK);

        //replaceNodes(master, clustergroups);
        replaceNodes(master, globalclustergroups);

        if (cancelled) {
            return null;
        }

        ClusterResults clusteringResults = new ClusterResults();
        if (clusterColumns) {

            int[] internaltopnodes = new int[dataSet.getDataWidth()];
            for (int i = 0; i < internaltopnodes.length; i++) {
                internaltopnodes[i] = i;
            }

            float[][] topdst = this.getTopDistances();
            topmaster = factory.makeclusters(topdst, internaltopnodes, LINK);

            clusteringResults.setColumnDendrogramRoot(topmaster);
        }

        clusteringResults.setRowDendrogramRoot(master);

        return clusteringResults;

    }

    private void replaceNodes(Node root, Node[] clustergroups) {

        if (!root.left.merged) {
            root.left = clustergroups[root.left.nme];
        } else {
            replaceNodes(root.left, clustergroups);
        }

        if (!root.right.merged) {
            root.right = clustergroups[root.right.nme];
        } else {
            replaceNodes(root.right, clustergroups);
        }

    }

    private Thread createDistanceThread(final int gen, final double[][] data, final int[] indices) {
        Thread th = new Thread() {
            @Override
            public void run() {
                HClustFactory factory = new HClustFactory();
                float[][] dst = getDistanceMatrix(data);
                globalclustergroups[gen] = factory.makeclusters(dst, indices, LINK);
            }
        };

        return th;
    }

    private float[][] getDistanceMatrix(double[][] data) {
        if (data == null) {
            return null;
        }
        float[][] distanceM = new float[data.length][];
        JMath jmath = new JMath();
        jmath.setMetric(this.distance);

        for (int i = 0; i < distanceM.length; i++) {
            distanceM[i] = new float[i];
            for (int j = 0; j < i; j++) {
                distanceM[i][j] = (float) jmath.euclid(data[i], data[j]);
            }
            if (cancelled) {
                return null;
            }
        }
        return distanceM;
    }

    private float[][] getTopDistances() {
        double[][] matrix = new double[dataSet.getDataWidth()][dataSet.getDataLength()];
        double[][] dat = dataSet.getData();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = (float) dat[j][i];
            }
        }
        float[][] dist = getDistanceMatrix(matrix);
        return dist;
    }
}
