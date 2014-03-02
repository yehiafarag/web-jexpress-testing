/*
 * ClusterParameters.java
 *
 * Created on 23. september 2007, 16:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.somclust.model;

import java.io.Serializable;

/**
 *
 * @author Bjarte Dysvik
 */
public class ClusterParameters implements Serializable {

    private LINKAGE link = LINKAGE.COMPLETE;
    private int distance = 2;
    private boolean clusterSamples = true;

    public enum LINKAGE {

        SINGLE, WPGMA, UPGMA, COMPLETE
    }

    /**
     * Creates a new instance of ClusterParameters
     */
    public ClusterParameters() {
    }

    public LINKAGE getLink() {
        return link;
    }

    public void setLink(LINKAGE link) {
        this.link = link;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isClusterSamples() {
        return clusterSamples;
    }

    public void setClusterSamples(boolean clusterSamples) {
        this.clusterSamples = clusterSamples;
    }
}
