/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.core.model;

import java.awt.Color;
import java.io.Serializable;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.dataset.DatasetGenerator;
import no.uib.jexpress_modularized.core.dataset.DatasetUtils;
import no.uib.jexpress_modularized.core.dataset.Group;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pawels
 */
public class DatasetUtilsTest implements Serializable{


    public DatasetUtilsTest() {}

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSubdatasetGeneration() {
        Dataset super_ds = DatasetGenerator.generateDummyDataset(100,20);
        Selection col_s = new Selection(Selection.TYPE.OF_COLUMNS, new int[] {1,2,4,19});
        Selection row_s = new Selection(Selection.TYPE.OF_ROWS, new int[] {1,11,22,33,44,55,66,77,88,99});
        
        Dataset sub_ds = DatasetUtils.getSubDatasetFromSelection(super_ds, col_s);
        assertEquals(sub_ds.getDataLength(), super_ds.getDataLength());
        assertEquals(sub_ds.getDataWidth(), col_s.size());        
        assertEquals(sub_ds.getColumnIds()[0], super_ds.getColumnIds()[1]);
        assertEquals(sub_ds.getColumnIds()[1], super_ds.getColumnIds()[2]);
        assertEquals(sub_ds.getColumnIds()[2], super_ds.getColumnIds()[4]);



        sub_ds = DatasetUtils.getSubDatasetFromSelection(super_ds, row_s);
        assertEquals(sub_ds.getDataWidth(), super_ds.getDataWidth());
        assertEquals(sub_ds.getDataLength(), row_s.size());
        assertEquals(sub_ds.getRowIds()[0], super_ds.getRowIds()[1]);
        assertEquals(sub_ds.getRowIds()[1], super_ds.getRowIds()[11]);
        assertEquals(sub_ds.getRowIds()[2], super_ds.getRowIds()[22]);
        assertEquals(sub_ds.getRowIds()[9], super_ds.getRowIds()[99]);

        sub_ds = DatasetUtils.getSubDatasetFromSelection(sub_ds, col_s);
        assertEquals(sub_ds.getDataWidth(), col_s.size());
        assertEquals(sub_ds.getDataLength(), row_s.size());
        assertEquals(sub_ds.getColumnIds()[0], super_ds.getColumnIds()[1]);
        assertEquals(sub_ds.getColumnIds()[1], super_ds.getColumnIds()[2]);
        assertEquals(sub_ds.getColumnIds()[2], super_ds.getColumnIds()[4]);
        assertEquals(sub_ds.getRowIds()[0], super_ds.getRowIds()[1]);
        assertEquals(sub_ds.getRowIds()[1], super_ds.getRowIds()[11]);
        assertEquals(sub_ds.getRowIds()[2], super_ds.getRowIds()[22]);
        assertEquals(sub_ds.getRowIds()[9], super_ds.getRowIds()[99]);

        // test subdataset made of group as well (test casting Group->Selection)
        Group g = new Group("group", Color.RED, col_s);
        sub_ds = DatasetUtils.getSubDatasetFromGroup(super_ds, g);
        assertEquals(sub_ds.getDataLength(), super_ds.getDataLength());
        assertEquals(sub_ds.getDataWidth(), col_s.size());
        assertEquals(sub_ds.getColumnIds()[0], super_ds.getColumnIds()[1]);
        assertEquals(sub_ds.getColumnIds()[1], super_ds.getColumnIds()[2]);
        assertEquals(sub_ds.getColumnIds()[2], super_ds.getColumnIds()[4]);

    }

    @Test
    public void testSubdatasetGenerationWithIllegalSelection() {
        Dataset super_ds = DatasetGenerator.generateDummyDataset(100,20);
        Selection col_s = new Selection(Selection.TYPE.OF_COLUMNS, new int[] {1,29});
        Selection row_s = new Selection(Selection.TYPE.OF_ROWS, new int[] {1,199});

        try {
            DatasetUtils.getSubDatasetFromSelection(super_ds, col_s);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(),"Selection/Group contains column index out of bounds of the dataset: 29");
        }

        try {
            DatasetUtils.getSubDatasetFromSelection(super_ds, row_s);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(),"Selection/Group contains row index out of bounds of the dataset: 199");
        }
    }

}
