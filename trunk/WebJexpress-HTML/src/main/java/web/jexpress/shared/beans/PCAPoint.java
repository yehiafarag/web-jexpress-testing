/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.shared.beans;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Y.M
 */
public class PCAPoint implements IsSerializable{
    private double x;
    private double y;
    private String geneName;
    private String color;
    private int geneIndex;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getGeneIndex() {
        return geneIndex;
    }

    public void setGeneIndex(int geneIndex) {
        this.geneIndex = geneIndex;
    }
    
}
