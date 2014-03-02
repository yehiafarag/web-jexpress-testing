/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.pca.model;

import java.io.Serializable;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Kristoffer
 */
public class ArrayUtilsTest implements Serializable{

    /*
     * toIntArray(boolean array)
     */
    
    @Test 
    public void length_Of_Int_Array_Is_Zero_If_Boolean_Array_Has_Length_Of_Zero() {
        boolean[] array = {};
        int[] intArray = ArrayUtils.toIntArray(array);
        assertEquals(0, intArray.length);
    }
    
    @Test
    public void length_Of_Int_Array_Is_The_Same_As_Amount_Of_True_Values_In_Boolean_Array() {
        boolean[] array = {true, false, false, false, false, false, false};
        int[] intArray = ArrayUtils.toIntArray(array);
        assertEquals(1, intArray.length);
    }
    
    @Test
    public void boolean_Array_With_No_True_Values_Gives_Empty_Int_Array() {
        boolean[] array = {false, false, false};
        int[] intArray = ArrayUtils.toIntArray(array);
        assertEquals(0, intArray.length);
    }
    
    @Test
    public void int_Array_Has_Int_Values_That_Corresponds_To_Indices_With_True_Values_In_Boolean_Array() {
        boolean[] array = {true, false, true, false, true, false, false};
        int[] intArray = ArrayUtils.toIntArray(array);
        assertEquals(3, intArray.length);
        assertEquals(0, intArray[0]);
        assertEquals(2, intArray[1]);
        assertEquals(4, intArray[2]);
    }
    
    /*toBooleanArray(int[] array)*/
    
    @Test 
    public void to_Boolean_Array_Returns_Array_With_Correct_Flags_Being_True() {
        int[] array = {5, 10, 15};
        boolean[] boolArray = ArrayUtils.toBooleanArray(15 + 1, array);
        assertTrue(boolArray[5]);
        assertTrue(boolArray[10]);
        assertTrue(boolArray[15]);
    }
    
    @Test 
    public void to_Boolean_Array_Returns_Array_With_Correct_Flags_Being_False() {
        int[] array = {2, 4, 6};
        boolean[] boolArray = ArrayUtils.toBooleanArray(6 + 1, array);
        assertFalse(boolArray[0]);
        assertFalse(boolArray[1]);
        assertFalse(boolArray[3]);
        assertFalse(boolArray[5]);
    }
    
}