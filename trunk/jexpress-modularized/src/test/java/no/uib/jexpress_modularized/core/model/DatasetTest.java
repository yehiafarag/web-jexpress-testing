/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.model;

import java.io.Serializable;
import no.uib.jexpress_modularized.core.dataset.AnnotationManager;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.dataset.DatasetGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bjarte
 */
public class DatasetTest implements Serializable{
    
    public DatasetTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        AnnotationManager.getAnnotationManager().clearAnnotationManager();
    }
    
    @After
    public void tearDown() {
        AnnotationManager.getAnnotationManager().clearAnnotationManager();
    }


    /**
     * Test of addRowAnnotations method, of class NewDataSet.
     */
    @Test
    public void testAddRowAnnotations() {
        int length=50, width=10;
        Dataset ds = DatasetGenerator.generateDummyDataset(length, width);
        assertEquals(ds.getColumnIds().length, width);
        assertEquals(ds.getRowIds().length, length);

        String annName = "testAnnotation";
        String[] values = new String[ds.getDataLength()];
        for (int i=0; i<values.length; i++) {
            values[i] = "annotation: "+i;
        }

        AnnotationManager am = AnnotationManager.getAnnotationManager();
        am.addRowAnnotations(annName, ds.getRowIds(), values);
        ds.addRowAnnotationNameInUse(annName);

        assertEquals(ds.getRowAnnotationNamesInUse().size(),1);
        assertTrue(ds.getRowAnnotationNamesInUse().contains(annName));


        System.out.println(ds.getRowIds()[1] + " --- " + am.getRowAnnotations(annName).getAnnotation(ds.getRowIds()[1]));
        System.out.println(ds.getRowIds()[11] + " --- " + am.getRowAnnotations(annName).getAnnotation(ds.getRowIds()[11]));
        System.out.println(ds.getRowIds()[31] + " --- " + am.getRowAnnotations(annName).getAnnotation(ds.getRowIds()[31]));
    }

   
    /**
     * Test of creation of a new dataset
     */
    @Test
    public void testSetData() {
        System.out.println("setData");
        double[][] measurements = new double[10][5];
        boolean[][] nullMeasurements = new boolean[10][5];
        Dataset instance = Dataset.newDataSet(measurements, nullMeasurements);
        
        double[][] data = instance.getData();
        
        for(int i=0;i<measurements.length;i++){
            for(int j=0;j<measurements[i].length;j++){
                if(measurements[i][j] != data[i][j]) fail("Set and get data is different");
            }
        }
        
        if(instance.getDataLength()!=10) fail("Dataset length is not the same as the array data length");
        if(instance.getDataWidth()!=5) fail("Dataset width is not the same as the array data width");
    }

}
