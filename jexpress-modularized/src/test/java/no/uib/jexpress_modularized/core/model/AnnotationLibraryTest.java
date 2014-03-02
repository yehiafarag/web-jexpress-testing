/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.model;

import java.io.Serializable;
import no.uib.jexpress_modularized.core.dataset.AnnotationLibrary;
import no.uib.jexpress_modularized.core.dataset.AnnotationManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bjarte, pawels
 */
public class AnnotationLibraryTest implements Serializable{

    String[] ids;
    String[] values;
    String annName;

    public AnnotationLibraryTest() {}

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        ids = new String[]{"KA","KB","KC","KD","KE"};
        values = new String[]{"VA","VB","VC","VD","VE"};
        annName = "myAnnotation";
        AnnotationManager.getAnnotationManager().clearAnnotationManager();
    }
    
    @After
    public void tearDown() {
        AnnotationManager.getAnnotationManager().clearAnnotationManager();
    }

    /**
     * Test of createAnnotationLibrary method, of class AnnotationLibrary.
     */
    @Test
    public void testAddingAnnotationsToLibrary() {
        //System.out.println("test adding annotations to a library");
        AnnotationLibrary lib = new AnnotationLibrary(annName);
        for (int i=0; i<ids.length; i++) {
              lib.addAnnotation(ids[i], values[i]);
        }
        
        for (int i=0; i<ids.length; i++) {
            assertEquals(lib.getAnnotation(ids[i]), values[i]);
        }
        assertEquals(annName, lib.getName());
    }

    /*
     * Test of the annotation manager
     *
     */
    @Test
    public void testAnnotationManager() {

        AnnotationManager m = AnnotationManager.getAnnotationManager();
        m.addColumnAnnotations(annName, ids, values);

        assertEquals(m.getManagedRowAnnotationNames().size(), 0);
        assertEquals(m.getRowAnnotations(annName), null);

        assertEquals(m.getManagedColumnAnnotationNames().size(), 1);
        AnnotationLibrary lib = m.getColumnAnnotations(annName);
        for (int i=0; i<ids.length; i++) {
            System.out.println(ids[i] + " --- " + values[i] );
            assertEquals(lib.getAnnotation(ids[i]), values[i]);
        }

        /******** test one more **********/
        
        String annName2 = "anotherAnnotation";
        m.addRowAnnotations(annName2, ids, values);

        assertEquals(m.getColumnAnnotations(annName2), null);
        assertEquals(m.getManagedColumnAnnotationNames().size(), 1);

        assertEquals(m.getManagedRowAnnotationNames().size(), 1);
        lib = m.getRowAnnotations(annName2);
        for (int i=0; i<ids.length; i++) {
            assertEquals(lib.getAnnotation(ids[i]), values[i]);
        }

    }
}
