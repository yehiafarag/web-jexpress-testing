package no.uib.jexpress_modularized.core.dataset;

import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * @author pawels
 */
public class AnnotationLibrary implements Serializable{

    private String name;
    private Hashtable<String, String> annotations;

    public AnnotationLibrary(String name) {
        this.name = name;
        this.annotations = new Hashtable<String, String>();
    }

    public String getName() {
        return name;
    }

    public String getAnnotation(String id) {
        return annotations.get(id);
    }

    public void addAnnotation(String id, String value) {
        annotations.put(id, value);
    }

}
