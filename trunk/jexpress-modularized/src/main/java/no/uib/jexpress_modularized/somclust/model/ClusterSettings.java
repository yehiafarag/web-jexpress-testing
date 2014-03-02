/*
 * ClusterSettings.java
 *
 * Created on 23. september 2007, 16:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.somclust.model;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author Bjarte Dysvik
 */
public class ClusterSettings implements Serializable {

    private Color markColor = Color.RED;
    private int leftTreeSize = 200;
    private int topTreeSize = 100;
    private int squareL = 2;
    private int squareW = 14;
    private boolean grid = true;

    /**
     * Creates a new instance of ClusterSettings
     */
    public ClusterSettings() {
    }

    public Color getMarkColor() {
        return markColor;
    }

    public void setMarkColor(Color markColor) {
        this.markColor = markColor;
    }

    public int getLeftTreeSize() {
        return leftTreeSize;
    }

    public void setLeftTreeSize(int leftTreeSize) {
        this.leftTreeSize = leftTreeSize;
    }

    public int getTopTreeSize() {
        return topTreeSize;
    }

    public void setTopTreeSize(int topTreeSize) {
        this.topTreeSize = topTreeSize;
    }

    public int getSquareL() {
        return squareL;
    }

    public void setSquareL(int squareL) {
        this.squareL = squareL;
    }

    public int getSquareW() {
        return squareW;
    }

    public void setSquareW(int squareW) {
        this.squareW = squareW;
    }

    public boolean paintGrid() {
        return grid;
    }

    public void setGrid(boolean grid) {
        this.grid = grid;
    }
}
