/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.TreeMap;

/**
 *
 * @author Y.M
 */
public class PCAResults implements IsSerializable{
    
    private  TreeMap<String,PCAPoint>  points ;
    private int datasetId;
    private int dataSize;
    private String header;
    private int pcai;
    private int pcaii;

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

    public TreeMap<String, PCAPoint> getPoints() {
        return points;
    }

    public void setPoints(TreeMap<String, PCAPoint> points) {
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
