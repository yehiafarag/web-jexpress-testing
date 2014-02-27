/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.pca.view;

import org.moxieapps.gwt.highcharts.client.Point;

/**
 *
 * @author Y.M
 */
public class CustomPoint extends Point{
    private String geneName;

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        super.setName(geneName);
        this.geneName = geneName;
    }

    public int getGeneIndex() {
        return geneIndex;
    }

    public void setGeneIndex(int geneIndex) {
        this.geneIndex = geneIndex;
    }

//    public String getColor() {
//        return color;
//    }

//    public void setColor(String color) {
//        this.color = color;
//    }

//    public boolean isSelected() {
//        return selected;
//    }
//
//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }
    private int geneIndex;
//    private String color;
//    private boolean selected;
    
    public CustomPoint(Number x,Number y)
    {
        super(x, y);
    }
    
}
