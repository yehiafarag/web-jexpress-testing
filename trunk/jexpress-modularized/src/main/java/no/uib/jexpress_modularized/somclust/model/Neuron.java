/*
 * Neuron.java
 *
 * Created on 03 November 2006, 10:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.somclust.model;

import java.io.Serializable;

/**
 *
 * @author Anne-Kristin
 */
public class Neuron implements Serializable {

    public double xcor, ycor;	//coordinates in node lattice
    public double[] vector;
    double[] addedvector;
    double additions = 1.0;
    public java.util.Vector members = new java.util.Vector();
    public double mapx, mapy, mapz;

    public Neuron(double[] vector) {
        this.vector = vector;
    }

    public Neuron(double[] vector, double xcor, double ycor) {
        this.xcor = xcor;
        this.ycor = ycor;
        addedvector = new double[vector.length];
        for (int i = 0; i < addedvector.length; i++) {
            addedvector[i] = 0.0;
        }
        this.vector = vector;
    }

    public double dist(Neuron n) {

        double ret = 0;

        for (int i = 0; i < vector.length; i++) {
            ret = ret + Math.pow(vector[i] - n.vector[i], 2);
        }
        return Math.sqrt(ret);
    }

    public void clear() {
        additions = 1.0;
        clearmembers();
        for (int i = 0; i < addedvector.length; i++) {
            addedvector[i] = 0.0;
        }
    }

    public void clearmembers() {
        members.clear();
    }

    public void addout() {
        for (int i = 0; i < addedvector.length; i++) {
            addedvector[i] = (addedvector[i] / (double) additions);
        }
    }

    public void newmember(int index) {
        members.add(new Integer(index));

    }

    public void removemember(int index) {

        for (int i = 0; i < members.size(); i++) {
            if (((Integer) members.elementAt(i)).intValue() == index) {

                members.removeElementAt(i);
                break;
            }
        }
    }
}
