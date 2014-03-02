
package no.uib.jexpress_modularized.somclust.computation;

import java.io.Serializable;
import no.uib.jexpress_modularized.somclust.model.Neuron;
import no.uib.jexpress_modularized.somclust.model.NeuronLattice;
import no.uib.jexpress_modularized.somclust.model.ClusterResults;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters;
import no.uib.jexpress_modularized.somclust.model.Node;
import no.uib.jexpress_modularized.core.computation.JMath;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters.LINKAGE;
import no.uib.jexpress_modularized.somclust.model.ClusteringCalculator;
import no.uib.jexpress_modularized.somclust.model.SOMClustUtil;


/**
 *
 * @author bjarte, pawels
 */
public class SOMClustCompute implements Serializable{

    private Node[] globalclustergroups;   
    private Node master, topmaster;
    private List<Neuron> neuronlist;
    private LINKAGE LINK;
    private int distance;
    private boolean clusterColumns;

    private List<double[][]> NoNullDataGroups;
    private List<int[]> NoNullDataIndices;
    
    private SOMClustUtil util = new SOMClustUtil();
    private ClusteringCalculator clust;
    
    /*
     * @param Dataset , ClusterParameters
     */

    public SOMClustCompute(Dataset dataSet, ClusterParameters pm) {

        //init the attribiutes 
        this.LINK = pm.getLink();
        this.distance = pm.getDistance();
        this.clusterColumns = pm.isClusterSamples();
        double[][] data = dataSet.getData();
        Random ran = new Random(234234l);
        JMath jmath = new JMath();
        jmath.setMetric(distance);
        NeuronLattice lattice = new NeuronLattice(7, 7, true, data, jmath, ran);
        int iteration = 0;
        int iterations = 200;
        double phi = 0.45;
        double momentum1 = 0.998;
        double theta = 0.9;
        double momentum2 = 0.998;
        int neigkernel = 0;
        neuronlist = util.calcNeurons(lattice,iteration,iterations,phi,momentum1,theta,momentum2,neigkernel,data,ran);
        List<Integer>[] sortedIndices = util.getSortedIndices(neuronlist, data, jmath);
        
        double[][][] dataGroups = new double[neuronlist.size()][][];
        int[][] dataGroupIndices = new int[neuronlist.size()][];

        for (int i = 0; i < neuronlist.size(); i++) {
            if (sortedIndices[i] != null) {
                dataGroups[i] = new double[sortedIndices[i].size()][];
                dataGroupIndices[i] = new int[sortedIndices[i].size()];
                for (int j = 0; j < sortedIndices[i].size(); j++) {
                    dataGroups[i][j] = data[sortedIndices[i].get(j)];
                    dataGroupIndices[i][j] = sortedIndices[i].get(j);
                }
            }
        }

        List<Neuron> NoNullneuronlist = new ArrayList<Neuron>();
        NoNullDataGroups = new ArrayList<double[][]>();
        NoNullDataIndices = new ArrayList<int[]>();

        for (int i = 0; i < neuronlist.size(); i++) {
            if (dataGroups[i] != null) {
                NoNullneuronlist.add(neuronlist.get(i));
                NoNullDataGroups.add(dataGroups[i]);
                NoNullDataIndices.add(dataGroupIndices[i]);
            }
        }
        neuronlist = NoNullneuronlist;
        globalclustergroups = new Node[neuronlist.size()];
        clust = new ClusteringCalculator(dataSet, neuronlist, NoNullDataGroups, NoNullDataIndices, globalclustergroups, master, topmaster, LINK, distance, clusterColumns);
        
    }

    /**
     * Executes the clustering algorithm. Takes time.
     * 
     * @return #ClusterResults which is an object representing clustering results
     */
    public ClusterResults runClustering() {
        
       ClusterResults clusterResults = clust.runClustering();
       return clusterResults;
    }

    /**
     * Monitor progress in percent of threads completed
     * @return progress in percent (i.e. 100 = done)
     */
    public int getProgress() {
      return clust.getProgress();
    }

    /**
     * Cancel the clustering when in progress
     */
    public void cancelClustering() {
        clust.setCancelled(true);
    }
   
}
