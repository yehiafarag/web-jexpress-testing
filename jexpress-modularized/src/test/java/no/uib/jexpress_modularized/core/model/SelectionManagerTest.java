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
 * @author pawels
 */
public class SelectionManagerTest implements Serializable{
    
    private String selectionCtrl;

    public SelectionManagerTest() {}

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

    @Test
    public void testSingleton() {
        SelectionManager sm1 = SelectionManager.getSelectionManager();
        SelectionManager sm2 = SelectionManager.getSelectionManager();
        assertEquals(sm1,sm2);
    }


    @Test
    public void testSelectionChange() {
        Dataset ds = DatasetGenerator.generateDummyDataset(10, 5);
        SelectionManager sm = SelectionManager.getSelectionManager();

        Selection s1 = new Selection(Selection.TYPE.OF_COLUMNS);
        Selection s2 = new Selection(Selection.TYPE.OF_ROWS);
        Selection s3 = new Selection(Selection.TYPE.OF_ROWS);

        sm.setSelectedColumns(ds, s1);
        sm.setSelectedRows(ds, s2);

        assertEquals(sm.getSelectedColumns(ds),s1);
        assertEquals(sm.getSelectedRows(ds),s2);
        sm.setSelectedColumns(ds, null);
        sm.setSelectedRows(ds, s3);
        assertEquals(sm.getSelectedColumns(ds),null);
        assertEquals(sm.getSelectedRows(ds),s3);

    }


    @Test
    public void testSelectionChangeListeners() {
        Dataset ds = DatasetGenerator.generateDummyDataset(10, 5);
        SelectionManager sm = SelectionManager.getSelectionManager();
        SelectionChangeListener l = new SelectionChangeListener() {
            @Override
            public void selectionChanged(Selection.TYPE type) {
                selectionCtrl = type.toString();
            }
        };

        Selection.TYPE t = Selection.TYPE.OF_ROWS;

        sm.addSelectionChangeListener(ds, l);
        sm.setSelectedRows(ds, new Selection(Selection.TYPE.OF_ROWS));
        assertEquals(t.toString(), selectionCtrl);
        t = Selection.TYPE.OF_COLUMNS;
        sm.setSelectedColumns(ds, new Selection(Selection.TYPE.OF_COLUMNS));
        assertEquals(t.toString(), selectionCtrl);

        sm.removeSelectionChangeListener(ds, l);
        sm.setSelectedRows(ds, new Selection(Selection.TYPE.OF_ROWS));
        assertEquals(t.toString(), selectionCtrl);
    }


}
