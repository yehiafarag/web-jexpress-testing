/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.core.dataset;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Set;

/**
 *
 * Singleton class containing annotation libraries for all datasets
 *
 * @author pawels
 */
public class AnnotationManager implements Serializable{

    private static AnnotationManager instance;

    private Hashtable<String, AnnotationLibrary> rowAnnotations;
    private Hashtable<String, AnnotationLibrary> columnAnnotations;


    private AnnotationManager() {
        rowAnnotations = new Hashtable<String, AnnotationLibrary>();
        columnAnnotations = new Hashtable<String, AnnotationLibrary>();
    }

    
    public static AnnotationManager getAnnotationManager() {
        if (instance == null) {
            instance = new AnnotationManager();
        }
        return instance;
    }

    public Set<String> getManagedRowAnnotationNames() {
        return rowAnnotations.keySet();
    }
    
    public Set<String> getManagedColumnAnnotationNames() {
         return columnAnnotations.keySet();
    }


    public void addRowAnnotations(String annotationName, String[] ids, String[] values) {
        if (!rowAnnotations.containsKey(annotationName)) {
            rowAnnotations.put(annotationName, new AnnotationLibrary(annotationName));
        }
        AnnotationLibrary lib = rowAnnotations.get(annotationName);
        for (int i = 0; i<ids.length; i++) {
            lib.addAnnotation(ids[i],values[i]);
        }
    }

    public void addColumnAnnotations(String annotationName, String[] ids, String[] values) {
        if (!columnAnnotations.containsKey(annotationName)) {
            columnAnnotations.put(annotationName, new AnnotationLibrary(annotationName));
        }
        AnnotationLibrary lib =  columnAnnotations.get(annotationName);
        for (int i = 0; i<ids.length; i++) {
            lib.addAnnotation(ids[i],values[i]);
        }
    }

    public void removeColumnAnnotation(String annotationName) {
        if (annotationName != null) { columnAnnotations.remove(annotationName); }
    }

    public void removeRowAnnotation(String annotationName) {
        if (annotationName != null) { rowAnnotations.remove(annotationName); }
    }

    public void clearAnnotationManager() {
        columnAnnotations.clear();
        rowAnnotations.clear();
    }

    public AnnotationLibrary getColumnAnnotations(String annotationName) {
        if (!columnAnnotations.containsKey(annotationName)) {
            return null;
        }
        return columnAnnotations.get(annotationName);
    }


    public AnnotationLibrary getRowAnnotations(String annotationName) {
        if (!rowAnnotations.containsKey(annotationName)) {
            return null;
        }
        return rowAnnotations.get(annotationName);
    }

}
