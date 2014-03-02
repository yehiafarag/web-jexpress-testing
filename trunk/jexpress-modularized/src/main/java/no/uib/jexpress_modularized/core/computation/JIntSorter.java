/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.computation;

import java.io.Serializable;

/*

 * Sorter.java

 *

 * Created on 12. desember 2001, 10:42

 */

/**

 *

 * @author  bjarte

 * @version 

 */

//NOTE: added because of use in DensScatterPlot
public class JIntSorter implements Serializable{



    /** Creates new Sorter */

    public JIntSorter() {

    }
     //---------- The sorting methods..
    // helper method for QuickSort_M3_BI

    private  static int partition(int[] A, int left, int right,int[] arrangement) {

        int i, j, middle;

        

        middle = (left + right) / 2;

        

        sort3(A, left, middle, right,arrangement);

        

        // now left and right will serve as sentinels

        // the partitioning element will be put in A[right - 1]

        right = right - 1;

        exchange(arrangement,right,middle);

        

        for (i = left, j = right; ; ){

            while (A[arrangement[++i]]<(A[arrangement[right]])) ;

            while (A[arrangement[--j]]>(A[arrangement[right]])) ;

            if (i >= j) break;

            exchange(arrangement,i,j);

        }

        exchange(arrangement,right,i);

        

        return i;

    }

    

    

    private  static void exchange(int[] A , int indexA,int indexB){

        int tmp = A[indexA];

        A[indexA]=A[indexB];

        A[indexB]=tmp;

    }

    

    

    private static void sort3(int[] A, int i, int j, int k,int[] arrangement){

        if (A[arrangement[i]]>(A[arrangement[j]]))

            exchange(arrangement,i,j);

        if (A[arrangement[i]]>(A[arrangement[k]]))

            exchange(arrangement,i,k);

        if (A[arrangement[j]]>(A[arrangement[k]]))

            exchange(arrangement,j,k);

    }

    

    public static void sort(int[] A) {

        quickSort(A);

    }

    

    

    

    

    public static int[] quickSort(int[] A) {

        int[] arrangement = new int[A.length];

        for(int i=0;i<A.length;i++) arrangement[i]=i;

        quickSort(A, 0, A.length - 1,arrangement);

        return arrangement;

    }

    

    

    

    public static void quickSort(int[] A,int[] arrangement) {

        quickSort(A, 0, A.length - 1,arrangement);

    }

    

    public  static void quickSort(int[] A, int left, int right,int[] arrangement) {

        if ((right - left) > 15) {

            int p = partition(A, left, right,arrangement);

            quickSort(A, left, p - 1,arrangement);

            quickSort(A, p + 1, right,arrangement);

        }

        else

            binaryInsertionSort(A, left, right,arrangement);

    }

    

    

    

    

    

    //-------------- BINARY INSERTION SORT

    

    private static int binarySearch(int[] A, int left, int right, int value,int[] arrangement){

        int middle; 

        double cmp;

        

        while (left <= right) {

            middle = (left + right) / 2;

            //cmp = A[value].compareToIgnoreCase(A[arrangement[middle]]);

            cmp = A[value]-A[arrangement[middle]];

            if (cmp == 0)

                return middle;

            else if (cmp < 0)

                right = middle - 1;

            else // if (cmp > 0)

                left = middle + 1;

        }

        

        return left;

    }

    

    public static void sort(int[] A, int[] arrangement){

        binaryInsertionSort(A,arrangement);

    }

    

    public static void binaryInsertionSort(int[] A,int[] arrangement){

        binaryInsertionSort(A, 0, A.length - 1,arrangement);

    }

    

    public static void binaryInsertionSort(int[] A, int left, int right,int[] arrangement){

        int i, j, pos;

        //String value = new String();

        int value = -1;

        

        for (i = left + 1; i <= right; i++){

            value=arrangement[i];

            

            pos = binarySearch(A, left, i - 1, value,arrangement);

            

            for (j = i; j > pos; j--)

                //A[j]=(A[j - 1]);

                arrangement[j]=arrangement[j-1];

            

            arrangement[j]=value;

        }

    }

    

    

}

