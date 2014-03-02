/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.somclust.model;

import java.io.Serializable;

/**
 *
 * @author st08574
 */
public class ClusterResults implements Serializable {

    private Node rowDendrogramRootNode;
    private Node columnDendrogramRootNode;

    public void setRowDendrogramRoot(Node rowRoot) {
        rowDendrogramRootNode = rowRoot;
    }

    public void setColumnDendrogramRoot(Node columnRoot) {
        columnDendrogramRootNode = columnRoot;
    }

    public Node getRowDendrogramRootNode() {
        return this.rowDendrogramRootNode;
    }

    public Node getColumnDendrogramRootNode() {
        return this.columnDendrogramRootNode;
    }
}
