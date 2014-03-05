/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.TreeMap;

/**
 *
 * @author Y.M
 */
public class PCAResults implements IsSerializable{
    
    private  TreeMap<Integer,PCAPoint>  points ;
    private int datasetId;
    private int dataSize;
    private String header;
    private int pcai;
    private int pcaii;
    
    private TreeMap<String,String> xyName ;

    public TreeMap<String, String> getXyName() {
        return xyName;
    }

    public void setXyName(TreeMap<String, String> xyName) {
        this.xyName = xyName;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

 

    public int getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

   

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public TreeMap<Integer, PCAPoint> getPoints() {
        return points;
    }

    public void setPoints(TreeMap<Integer, PCAPoint> points) {
        this.points = points;
    }

    public int getPcai() {
        return pcai;
    }

    public void setPcai(int pcai) {
        this.pcai = pcai;
    }

    public int getPcaii() {
        return pcaii;
    }

    public void setPcaii(int pcaii) {
        this.pcaii = pcaii;
    }

    
}
