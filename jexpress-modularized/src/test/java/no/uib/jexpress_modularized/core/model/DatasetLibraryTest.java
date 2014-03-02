/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.core.model;

import java.io.Serializable;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.dataset.DatasetGenerator;
import no.uib.jexpress_modularized.core.dataset.DatasetLibrary;
import no.uib.jexpress_modularized.core.dataset.DatasetUtils;
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
//public class DatasetLibraryTest implements Serializable{

//    private Dataset dataset, childDs1, childDs2, childDs3;
//    private static String rootId, childDs1Id, childDs2Id, childDs3Id;
//
//    public DatasetLibraryTest() {}
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//    }
//
//    @AfterClass
//    public static void tearDownClass() throws Exception {
//    }
//
//    @Before
//    public void setUp() {
//        dataset = DatasetGenerator.generateSmallRealDataset();
//        dataset.setName("root");
//        Selection s1 = new Selection(Selection.TYPE.OF_ROWS, new int[] {1,2,3,4,5});
//        Selection s2 = new Selection(Selection.TYPE.OF_ROWS, new int[] {5,6,7,8,9});
//        Selection s3 = new Selection(Selection.TYPE.OF_ROWS, new int[] {8,9});
//        childDs1 = DatasetUtils.getSubDatasetFromSelection(dataset, s1);
//        childDs1.setName("child1-5");
//        childDs2 = DatasetUtils.getSubDatasetFromSelection(dataset, s2);
//        childDs2.setName("child5-9");
//        childDs3 = DatasetUtils.getSubDatasetFromSelection(dataset, s3);
//        childDs3.setName("child8-9");
//    }
//
//    @After
//    public void tearDown() {
//    }
//
//
//    /*
//     * TODO add tests of getting names
//     */
//    @Test
//    public void testAddDataset() {
//
//        DatasetLibrary dsl = DatasetLibrary.getDatasetLibrary();
//
//        rootId = dsl.addDataset(dataset, null);
//        assertEquals(dsl.getDataset(rootId), dataset);
//
//        childDs1Id = dsl.addDataset(childDs1, rootId);
//        childDs2Id = dsl.addDataset(childDs2, rootId);
//        assertEquals(dsl.getDataset(childDs1Id), childDs1);
//        assertEquals(dsl.getDataset(childDs2Id), childDs2);
//
//        childDs3Id = dsl.addDataset(childDs3, childDs2Id);
//        assertEquals(dsl.getDataset(childDs3Id), childDs3);
//
//    }
//
//
//    /**
//     * TODO add tests of getting names
//     */
//    @Test
//    public void testRemoveDataset() {
//
//        Selection s1 = new Selection(Selection.TYPE.OF_COLUMNS, new int[] {1,2});
//        Selection s2 = new Selection(Selection.TYPE.OF_COLUMNS, new int[] {2,3});
//        Dataset toRemove1 = DatasetUtils.getSubDatasetFromSelection(dataset, s1);
//        toRemove1.setName("toremove1");
//        Dataset toRemove2 = DatasetUtils.getSubDatasetFromSelection(dataset, s2);
//        toRemove2.setName("toremove2");
//
//
//        Selection s12 = new Selection(Selection.TYPE.OF_ROWS, new int[] {5,6,7,8});
//        Dataset toRemove12 = DatasetUtils.getSubDatasetFromSelection(toRemove1, s12);
//        toRemove12.setName("toremove12");
//
//
//        DatasetLibrary dsl = DatasetLibrary.getDatasetLibrary();
//        String toRemove1Id = dsl.addDataset(toRemove1, rootId);
//        String toRemove2Id = dsl.addDataset(toRemove2, rootId);
//
//        String toRemove12Id = dsl.addDataset(toRemove12, toRemove1Id);
//
//        assertEquals(dsl.getDataset(toRemove1Id), toRemove1);
//        assertEquals(dsl.getDataset(toRemove2Id), toRemove2);
//        assertEquals(dsl.getDataset(toRemove12Id), toRemove12);
//
//        dsl.removeDataset(toRemove1Id);
//        assertEquals(dsl.getDataset(toRemove1Id), null);
//        assertEquals(dsl.getDataset(toRemove12Id), null);
//
//        dsl.removeDataset(toRemove2Id);
//        assertEquals(dsl.getDataset(toRemove2Id), null);
//        
//    }
//
//    @Test
//    public void testRemoveRootDataset() {
//        DatasetLibrary dsl = DatasetLibrary.getDatasetLibrary();
//
//        dsl.removeDataset(rootId);
//        assertEquals(dsl.getDataset(rootId), null);
//        assertEquals(dsl.getDataset(childDs1Id), null);
//        assertEquals(dsl.getDataset(childDs2Id), null);
//        assertEquals(dsl.getDataset(childDs3Id), null);
//    }
//
//    @Test
//    public void testFaultyAddDataset() {
//
//        DatasetLibrary dsl = DatasetLibrary.getDatasetLibrary();
//
//        try {
//            dsl.addDataset(null, "parent");
//            fail("Null datasets are not allowed");
//        } catch (IllegalArgumentException exc) {
//            assertEquals(exc.getMessage(), "Null datasets are not allowed in the DatasetLibrary");
//        }
//
//        try {
//            dataset.setName(null);
//            dsl.addDataset(dataset, "parent");
//            fail("Null names are not allowed");
//        } catch (IllegalArgumentException exc) {
//            assertEquals(exc.getMessage(), "Datasets with null and empty names are not allowed in the DatasetLibrary");
//        }
//
//        try {
//            dataset.setName("");
//            dsl.addDataset(dataset, null);
//            fail("Null names are not allowed");
//        } catch (IllegalArgumentException exc) {
//            assertEquals(exc.getMessage(), "Datasets with null and empty names are not allowed in the DatasetLibrary");
//        }
//
//        try {
//            dataset.setName("root");
//            dsl.addDataset(dataset, "parent");
//            fail("Non-existent parents are not allowed");
//        } catch (IllegalArgumentException exc) {
//            assertEquals(exc.getMessage(), "Parent dataset not found in the library: parent");
//        }
//
//        try {
//            rootId = dsl.addDataset(dataset, null);
//            dsl.addDataset(dataset, null);
//            fail("multiple root datasets with the same name are not allowed");
//        } catch (IllegalArgumentException exc) {
//            assertEquals(exc.getMessage(), "Dataset with the same name already exists on this level: root");
//        }
//
//        try {
//            dataset.setName("rootChild");
//            dsl.addDataset(dataset, rootId);
//            dsl.addDataset(dataset, rootId);
//            fail("multiple children datasets with the same name are not allowed");
//        } catch (IllegalArgumentException exc) {
//            assertEquals(exc.getMessage(), "Dataset with the same name already exists on this level: rootChild");
//        }
//
//    }
//

//}
