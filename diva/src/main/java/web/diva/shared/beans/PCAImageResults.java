/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.diva.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.HashMap;

/**
 *
 * @author Yehia Farag
 */
public class PCAImageResults implements IsSerializable{
    private String imgString;
    private int datasetId;
    
    private double dataAreaMaxX;
    private double dataAreaMaxY;
    private double dataAreaMinX;
    private double dataAreaMinY,MinX,MinY,MaxX,MaxY;
    
     private PCAPoint[] indexeMap ;
    
    
    
    private HashMap<String,String> xyName ;

    public String getImgString() {
        return imgString;
    }

    public void setImgString(String imgString) {
        this.imgString = imgString;
    }

    public int getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    public HashMap<String,String> getXyName() {
        return xyName;
    }

    public void setXyName(HashMap<String,String> xyName) {
        this.xyName = xyName;
    }

    public double getDataAreaMaxX() {
        return dataAreaMaxX;
    }

    public void setDataAreaMaxX(double dataAreaMaxX) {
        this.dataAreaMaxX = dataAreaMaxX;
    }

    public double getDataAreaMaxY() {
        return dataAreaMaxY;
    }

    public void setDataAreaMaxY(double dataAreaMaxY) {
        this.dataAreaMaxY = dataAreaMaxY;
    }

    public double getDataAreaMinX() {
        return dataAreaMinX;
    }

    public void setDataAreaMinX(double dataAreaMinX) {
        this.dataAreaMinX = dataAreaMinX;
    }

    public double getDataAreaMinY() {
        return dataAreaMinY;
    }

    public void setDataAreaMinY(double dataAreaMinY) {
        this.dataAreaMinY = dataAreaMinY;
    }

    public double getMinX() {
        return MinX;
    }

    public void setMinX(double MinX) {
        this.MinX = MinX;
    }

    public double getMinY() {
        return MinY;
    }

    public void setMinY(double MinY) {
        this.MinY = MinY;
    }

    public double getMaxX() {
        return MaxX;
    }

    public void setMaxX(double MaxX) {
        this.MaxX = MaxX;
    }

    public double getMaxY() {
        return MaxY;
    }

    public void setMaxY(double MaxY) {
        this.MaxY = MaxY;
    }

    public PCAPoint[] getIndexeMap() {
        return indexeMap;
    }

    public void setIndexeMap(PCAPoint[] indexeMap) {
        this.indexeMap = indexeMap;
    }
    
}
