/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.rank.computation.util;

import java.io.Serializable;



/**

 *

 * @author  bjarte

 * @version 

 */

public class JDoubleSorter implements Serializable{



    /** Creates new Sorter */

    public JDoubleSorter() {

    }



    public static void main(String[] args){

     double[] test = new double[]{1.1,2.5,2.8,8.3,2.9,1.2,22.55,22.34,21.23,65.4,22.11,21.23,65.4,22.11,1.1,6.1,99.2,9.12};   

        JDoubleSorter dd = new JDoubleSorter();

     int[] ret = dd.quickSort(test);

     for(int i=0;i<ret.length;i++) System.out.print("\n"+test[ret[i]]);

        

    }

    

    

    

     //---------- The sorting methods..

    

    

    // helper method for QuickSort_M3_BI

    private  static int partition(double[] A, int left, int right,int[] arrangement) {

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

    

    

    private static void sort3(double[] A, int i, int j, int k,int[] arrangement){

        if (A[arrangement[i]]>(A[arrangement[j]]))

            exchange(arrangement,i,j);

        if (A[arrangement[i]]>(A[arrangement[k]]))

            exchange(arrangement,i,k);

        if (A[arrangement[j]]>(A[arrangement[k]]))

            exchange(arrangement,j,k);

    }

    

    public static void sort(double[] A) {

        quickSort(A);

    }

    

    

    

    

    public static int[] quickSort(double[] A) {

        int[] arrangement = new int[A.length];

        for(int i=0;i<A.length;i++) arrangement[i]=i;

        //quickSort(A, 0, A.length - 1,arrangement);

        presort(A, arrangement);
        
        return arrangement;

    }

    

    //A preallocated int array as input. This array will be re-valued..

     public static void quickSortI(double[] A, int[] arrangement) {

         if(A.length!=arrangement.length) throw new IllegalArgumentException("The Arrays are not of the same length");

        //int[] arrangement = new int[A.length];

        for(int i=0;i<A.length;i++) arrangement[i]=i;

        quickSort(A, 0, A.length - 1,arrangement);

        //return arrangement;

    }

    

    

    

    public static void quickSort(double[] A,int[] arrangement) {

        quickSort(A, 0, A.length - 1,arrangement);
         

    }

    

    public  static void quickSort(double[] A, int left, int right,int[] arrangement) {

        if ((right - left) > 15) {

            int p = partition(A, left, right,arrangement);

            quickSort(A, left, p - 1,arrangement);

            quickSort(A, p + 1, right,arrangement);

        }

        else

            binaryInsertionSort(A, left, right,arrangement);

    }

    

     public static void presort(double[] A, int[] arrangement) {

        presort(A, 0, A.length - 1,arrangement);

    }
    
    
    public static void presort(double[] A,  int left, int right,int[] arrangement) {

        boolean nan = false;

        for(int i=0;i<A.length;i++)if(Double.isNaN(A[i])){ nan=true; break;}

        if(!nan) quickSort(A, left, right, arrangement);

        

        else{

            int nannum = 0;
            int tmp=0;
            for(int i=0;i<A.length;i++){
                if(Double.isNaN(A[arrangement[i]])){

                    while(Double.isNaN(A[arrangement[nannum]]) && arrangement.length<nannum-1 && arrangement[nannum]<right-2)nannum++;

                    

                    tmp = arrangement[i];

                    arrangement[i]=arrangement[nannum];

                    arrangement[nannum]=tmp;

                    nannum++;

                }

            }

            

       //     System.out.print("\nnannum:"+nannum);

            quickSort(A, nannum, right, arrangement);

        }

        

        

        

    }

    

    

    

    //-------------- BINARY INSERTION SORT

    

    private static int binarySearch(double[] A, int left, int right, int value,int[] arrangement){

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

    

    public static void sort(double[] A, int[] arrangement){

        binaryInsertionSort(A,arrangement);

    }

    

    public static void binaryInsertionSort(double[] A,int[] arrangement){

        binaryInsertionSort(A, 0, A.length - 1,arrangement);

    }

    

    public static void binaryInsertionSort(double[] A, int left, int right,int[] arrangement){

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

