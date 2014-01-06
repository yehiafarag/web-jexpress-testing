/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.TreeMap;
import web.jexpress.shared.Unit;

/**
 *
 * @author Yehia Farag
 */
public class SomClusteringResults implements IsSerializable{
    private int height;
    private int datasetId;
    private TreeMap<String,String>toolTips;
    private String[] geneNames;
    private String[] colsNames;    
    private Unit sideTree;
    private String imgString;

    public Unit getSideTree() {
        return sideTree;
    }

    public void setSideTree(Unit sideTree) {
        this.sideTree = sideTree;
    }

    public Unit getTopTree() {
        return topTree;
    }

    public void setTopTree(Unit topTree) {
        this.topTree = topTree;
    }
    private Unit topTree;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    public TreeMap<String,String> getToolTips() {
        return toolTips;
    }

    public void setToolTips(TreeMap<String,String> toolTips) {
        this.toolTips = toolTips;
    }

    public String[] getGeneNames() {
        return geneNames;
    }

    public void setGeneNames(String[] geneNames) {
        this.geneNames = geneNames;
    }

    public String[] getColsNames() {
        return colsNames;
    }

    public void setColsNames(String[] colsNames) {
        this.colsNames = colsNames;
    }
    public String getImgString() {
        return imgString;
    }

    public void setImgString(String imgString) {
        this.imgString = imgString;
    }
    
}
