/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.rank.computation.util;

import java.io.Serializable;


/*
 * Shuffle.java
 *
 * Created on 10 July 2007, 13:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


/**
 *
 * @author Anne-Kristin
 */
public class Shuffle implements Serializable{
    
    /** Creates a new instance of Shuffle */
    public Shuffle() {
    }
    
      //int[] 1,2,3 is returned as permuted as for instance 2,3,1
    public static int[] permuteOrder(int[] original, java.util.Random ran) {
        int[] ret = new int[original.length];
        double w[]=new double[original.length];
        for(int i=0;i<ret.length;i++)
            w[i]=ran.nextDouble();        
        int neworder[]=JDoubleSorter.quickSort(w);
        for(int i=0;i<ret.length;i++)
            ret[i]=original[neworder[i]];
        
        return ret;
    }

    public static double[] permuteOrder(double[] original, java.util.Random ran) {
        
        // New return array
        double[] ret = new double[original.length];
        
        // Create an array of random numbers
        double w[]=new double[original.length];
        for(int i=0;i<ret.length;i++)
            w[i]=ran.nextDouble();
        
        // Get new order
        int neworder[]=JDoubleSorter.quickSort(w);
        for(int i=0;i<ret.length;i++)
            ret[i]=original[neworder[i]];
        
        return ret;
    }
    
    
}

