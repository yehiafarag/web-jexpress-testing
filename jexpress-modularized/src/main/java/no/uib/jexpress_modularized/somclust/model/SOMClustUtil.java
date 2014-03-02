/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.somclust.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import no.uib.jexpress_modularized.core.computation.JMath;

/**
 *
 * @author Yehia Farag
 */
public class SOMClustUtil implements Serializable {

    public ArrayList<Neuron> calcNeurons(NeuronLattice lattice, int iteration, int iterations, double phi, double momentum1, double theta, double momentum2, int neigkernel, double[][] data, Random ran) {
        boolean please_stop = false;

        while (!please_stop) {
            if (iteration >= iterations) {
                please_stop = true;
            } else {
                iteration++;
                int pkt = (int) Math.floor(ran.nextDouble() * (data.length - 1));
                double[] pnt = data[pkt];
                Neuron clos = lattice.findclosest(pnt);
                lattice.makeR(clos, pnt, phi, theta, neigkernel);

                phi = phi * momentum1;
                theta = theta * momentum2;
            }
        }
        Neuron[][] neurons = lattice.lattice;
        ArrayList<Neuron> neuronlist = this.getNeuronList(neurons);
        return neuronlist;

    }

    private ArrayList<Neuron> getNeuronList(Neuron[][] neurons) {
        ArrayList<Neuron> neuronlist = new ArrayList<Neuron>();
        for (int i = 0; i < neurons.length; i++) {
            for (int j = 0; j < neurons[0].length; j++) {
                neuronlist.add(neurons[i][j]);
            }
        }
        return neuronlist;


    }

    public List<Integer>[] getSortedIndices(List<Neuron> neuronlist, double[][] data, JMath jmath) {
        List<Integer>[] sortedIndices = new ArrayList[neuronlist.size()];

        for (int i = 0; i < data.length; i++) {
            int min = getClosestNeuron(data[i], neuronlist, jmath);
            if (sortedIndices[min] == null) {
                sortedIndices[min] = new ArrayList<Integer>();
            }
            sortedIndices[min].add(i);
        }

        return sortedIndices;
    }

    private int getClosestNeuron(double[] vector, List<Neuron> neuronlist, JMath jmath) {
        double max = Double.MAX_VALUE;
        int maxIndex = -1;
        for (int i = 0; i < neuronlist.size(); i++) {
            double current = jmath.euclid(vector, neuronlist.get(i).vector);
            if (current < max) {
                max = current;
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
