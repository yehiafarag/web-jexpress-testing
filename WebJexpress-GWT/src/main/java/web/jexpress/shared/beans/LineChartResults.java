/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Yehia Farag 
 */
public class LineChartResults implements IsSerializable{
    private Number[] lineChartPoints[];
    private int datasetId;
    private String[] colours;
    private String[] geneNames;

    public Number[][] getLineChartPoints() {
        return lineChartPoints;
    }

    public void setLineChartPoints(Number[] lineChartPoints[]) {
        this.lineChartPoints = lineChartPoints;
    }

    public int getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    public String[] getColours() {
        return colours;
    }

    public void setColours(String[] colours) {
        this.colours = colours;
    }

    public String[] getGeneNames() {
        return geneNames;
    }

    public void setGeneNames(String[] geneNames) {
        this.geneNames = geneNames;
    }
    
}
