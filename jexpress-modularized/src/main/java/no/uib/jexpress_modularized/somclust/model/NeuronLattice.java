/*
 * NeuronLattice.java
 *
 * Created on 03 November 2006, 10:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.somclust.model;

import java.io.Serializable;
import no.uib.jexpress_modularized.core.computation.JMath;

/**
 *
 * @author Anne-Kristin
 */
public class NeuronLattice implements Serializable {

    double DEG2RAD = Math.PI / 180;
    double RAD2DEG = 180 / Math.PI;
    public boolean hex = true;
    public Neuron[][] lattice;
    public int length, width;
    int[] taken;
    int neuronsassigned = 0;
    java.util.Random ran;
    JMath jmath;

    public NeuronLattice(int width, int length, boolean hex, double[][] matrix, JMath jmath, java.util.Random ran) {
        this.length = width;
        this.width = length;
        this.hex = hex;
        this.jmath = jmath;
        this.ran = ran;
        lattice = new Neuron[this.width][this.length];
        double spor = (double) lattice.length * 2;

        if (hex) {

            double s = 10.0;
            double h = Math.sin(30 * DEG2RAD) * s;
            double r = Math.cos(30 * DEG2RAD) * s;
            double b = s + (2 * h);
            double a = 2 * r;

            for (int i = 0; i < lattice.length; i++) {
                for (int j = 0; j < lattice[0].length; j++) {

                    if (i % 2 == 0) {
                        lattice[i][j] = new Neuron(makeneuron(matrix), (b / 2) * i, j * a);
                    } else {
                        lattice[i][j] = new Neuron(makeneuron(matrix), (b / 2) * i, (j * a) + r);
                    }
                    if (lattice[i][j] == null) {
                    }
                }
            }
        } else {
            for (int i = 0; i < lattice.length; i++) {
                for (int j = 0; j < lattice[0].length; j++) {
                    lattice[i][j] = new Neuron(makeneuron(matrix), (double) i / (double) lattice.length, (double) j / (double) lattice[0].length);
                    if (lattice[i][j] == null) {
                    }
                }
            }
        }

        //Normalize the lattice
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        double maxy = Double.MIN_VALUE;
        double miny = Double.MAX_VALUE;

        for (int i = 0; i < lattice.length; i++) {
            for (int j = 0; j < lattice[0].length; j++) {
                if (max < lattice[i][j].xcor) {
                    max = lattice[i][j].xcor;
                }
                if (min > lattice[i][j].xcor) {
                    min = lattice[i][j].xcor;
                }
                if (maxy < lattice[i][j].ycor) {
                    maxy = lattice[i][j].ycor;
                }
                if (miny > lattice[i][j].ycor) {
                    miny = lattice[i][j].ycor;
                }
            }
        }

        for (int i = 0; i < lattice.length; i++) {
            for (int j = 0; j < lattice[0].length; j++) {
                lattice[i][j].xcor = (lattice[i][j].xcor - min) / (max - min);
                lattice[i][j].ycor = (lattice[i][j].ycor - miny) / (maxy - miny);
            }
        }

    }

    /**
     * Find closest node to point With Selected Distance metric (in jmath)
     *
     * @param point The point from where we want to find the closest Neuron
     * @return The closest Neuron to the input point
     */
    public Neuron findclosest(double[] point) {
        double smallest = Double.MAX_VALUE;
        Neuron ret = lattice[0][0];
        double calc = 0.0;
        for (int i = 0; i < lattice.length; i++) {
            for (int j = 0; j < lattice[0].length; j++) {

                calc = jmath.dist(lattice[i][j].vector, point);
                if (calc < smallest) {
                    smallest = calc;
                    ret = lattice[i][j];
                }
            }
        }
        return ret;
    }

    public void clear() {
        for (int i = 0; i < lattice.length; i++) {
            for (int j = 0; j < lattice[0].length; j++) {
                lattice[i][j].clear();
            }
        }
    }

    public double[][][] getReferenceVectors(int datawidth) {
        double[][][] ret = new double[width][length][datawidth];

        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                for (int k = 0; k < ret[0][0].length; k++) {
                    ret[i][j][k] = lattice[i][j].vector[k];
                }
            }
        }

        return ret;
    }

    /*
     * private double[] makeneuron(double min,double max,int numelements){
     * double temp=0.0; //int elem=0; double[] ret=new double[numelements];
     *
     *
     * for(int i=0;i<numelements;i++){ //temp=(Math.random()*(max-min))+min;
     * temp=(ran.nextDouble()*(max-min))+min;
     *
     *
     * //elem=(int)Math.round(temp); ret[i]=temp;} //System.out.print("\n svar:
     * "+String.valueOf(elem)); return ret; }
     */
    private double[] makeneuron(double[][] matrix) {
        //double temp=0.0;
        int temp = -1;
        boolean ok = false;
        boolean isthere = false;

        if (width * length < matrix.length) {

            if (taken == null) {
                taken = new int[width * length];
                for (int i = 0; i < taken.length; i++) {
                    taken[i] = -1;
                };
            }

            while (!ok) {
                temp = (int) Math.round(ran.nextDouble() * (matrix.length - 1));

                isthere = false;
                for (int i = 0; i < taken.length; i++) {
                    if (temp == taken[i]) {
                        isthere = true;
                    }
                }
                if (!isthere) {

                    ok = true;
                    taken[neuronsassigned] = temp;
                    neuronsassigned++;
                }

            }
        } else {
            temp = (int) Math.round(ran.nextDouble() * (matrix.length - 1));
        }


        double[] ret = new double[matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++) {
            ret[i] = matrix[temp][i];
        }
        return ret;
    }

    public void makeR(Neuron clos, double[] pkt, double phi, double theta, int neigkernel) {
        double r = 0.0;
        double ndist = 0.0;


        for (int i = 0; i < lattice.length; i++) {
            for (int j = 0; j < lattice[0].length; j++) {
                r = 0.0;

                ndist = (Math.pow((lattice[i][j].xcor - clos.xcor), 2) + Math.pow((lattice[i][j].ycor - clos.ycor), 2));
                ndist = Math.sqrt(ndist);

                if (neigkernel == 0) {
                    r = Math.exp(-1.0 * (ndist * ndist) / (2.0 * theta * theta));
                } else if (neigkernel == 1) {
                    r = ((1.0 - (ndist * ndist) / (theta * theta)));
                } else {
                    if (ndist < theta) {
                        r = 0.5;
                    }
                }

                if (r < 0) {
                    r = 0;
                }
                for (int k = 0; k < lattice[i][j].vector.length; k++) {
                    lattice[i][j].vector[k] += (phi * r * (pkt[k] - lattice[i][j].vector[k]));
                }

            }
        }
    }
}
