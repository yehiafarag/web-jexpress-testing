/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.pca.model;

import java.io.Serializable;

/**
 * Contains various methods for handling arrays. Also contains an inner class
 * <code>DataSet</code>, which contains static methods that mimic the behaviour
 * of old methods from the
 * <code>DataSet</code> class in JExpress that does not exist in the modularized
 * version of
 * <code>DataSet</code>. The purpose of this is to be able to get code that rely
 * on the particular data-formats\* that these methods force/encourage, without
 * having to change the whole call-stack. </p> \*For example using an array of
 * boolean instead of an array of int (see toIntArray(boolean[] array) method).
 *
 * @author Kristoffer
 */
/*
 * NOTE: the motivation for making this class was that I had wound up with all
 * of these methods copy-pasted into the classes that needed them as private
 * methods. Because I found this way to be unorganized and error-prone (problems
 * could occur if I needed to change one of the methods, but forgot to change
 * all of them), I decided to make a dedicated class for them. This way it will
 * also hopefully be easier to get rid them if someone wants to replace them
 * with something better in the future
 */
public class ArrayUtils implements Serializable{

    /*
     * Does not need to be initialized, as long as this class only contain
     * static methods.
     */
    private ArrayUtils() {
    }

    /**
     * Converts a given array of boolean to an array of int. This conversion
     * happens thus: </p> if a cell of index
     * <code>i</code> in
     * <code>array</code> is
     * <code>true</code>, then the int
     * <code>i</code> will be in the new int array.
     *
     * @param array an array of boolean
     * @return an array of int
     */
    //The motivation for making this method was that I was making methods 
    //that needed to take arrays of boolean and pass 
    //that data to other methods that demanded arrays of int.
    public static int[] toIntArray(boolean[] array) {
        int numberOfTrue = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                numberOfTrue++;
            }
        }
        int[] intArray = new int[numberOfTrue];
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                intArray[count++] = i;
            }
        }
        return intArray;
    }

    /**
     * Converts a given array of int to an array of boolean. This conversion
     * happens thus: </p>. For a given index <i>i</i> in the new array, <i>i</i>
     * is set to true if the int i exists in
     * <code>array</code>, false otherwise.
     *
     * @param length the length that the given array should get. length + 1 must
     * not be larger than the largest integer in the param array
     * @param array an array of non-negative int-values
     * @return
     */
    /*
     * NOTE: the length of the array could also be equal to the largest int in
     * the given int array + 1, but the length is instead given as its own
     * argument. This is because boolean-arrays that are meant as flag-arrays in
     * JExpress seems to be often made to have a length equal to the a DataSet
     * (incase its for example meant to represent which rows are in a
     * Selection), in other words its independent of any maximum value. This way
     * the array will function the same as boolean arrays in JExpress, and not
     * lead to some unexpected bugs (such as IndexOutOfBoundsException if a loop
     * iterates over another array, and implicitly assumes that the boolean
     * array has an equal length to it).
     */
    public static boolean[] toBooleanArray(int length, int[] array) {
        boolean[] boolArray = new boolean[length];
        for (int i = 0; i < array.length; i++) {
            boolArray[array[i]] = true;
        }
        return boolArray;
    }

   
}