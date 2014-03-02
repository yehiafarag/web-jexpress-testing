/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.model;

import java.io.Serializable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author NetBeans
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    no.uib.jexpress_modularized.core.model.DatasetUtilsTest.class, 
//    no.uib.jexpress_modularized.core.model.DatasetLibraryTest.class, 
    no.uib.jexpress_modularized.core.model.AnnotationLibraryTest.class, 
    no.uib.jexpress_modularized.core.model.SelectionManagerTest.class, 
    no.uib.jexpress_modularized.core.model.DatasetTest.class
})
public class TestSuite implements Serializable{
    
}