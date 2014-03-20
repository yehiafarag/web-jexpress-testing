package no.uib.jexpress_modularized.core.dataset;

import no.uib.jexpress_modularized.core.model.Selection;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Set of rows or columns with name, description and color assigned
 *
 * @author pawels
 */
public class Group extends Selection implements Serializable{

    private String name;
    private String description;
    private Color color;
    private String hashColor;
    
    private List<Integer> indices;
    private List<String> geneList;

    /**
     * Create a group from selection
     *
     * @param name - group name
     * @param color - color the group will be associated/displayed with
     * @param type - type of group (column or row)
     * @param indices - indices of rows/columns to add to a group
     */
    public Group(String name, Color color, TYPE type, int[] indices) {
        super(type, indices);
        setName(name);
        setColor(color);
        setHashColor("#" + Integer.toHexString(color.getRGB()).substring(2));
        List<Integer> ind = new ArrayList<Integer>();
                for (int x : indices) {
                    ind.add(x);
                }
                this.setIndices(ind);
    }

    /**
     * Create a group from selection
     * 
     * @param name - group name
     * @param color - color the group will be associated/displayed with
     * @param selection - selection of rows/columns to add to a group
     */
    public Group(String name, Color color, Selection selection) {
        super(selection.getType(), selection.getMembers());
        setName(name);
        setColor(color);        
        setHashColor("#" + Integer.toHexString(color.getRGB()).substring(2));
        List<Integer> ind = new ArrayList<Integer>();
                for (int x : selection.getMembers()) {
                    ind.add(x);
                }
                this.setIndices(ind);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashColor() {
        return hashColor;
    }

    public void setHashColor(String hashColor) {
        this.hashColor = hashColor;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public List<String> getGeneList() {
        return geneList;
    }

    public void setGeneList(List<String> geneList) {
        this.geneList = geneList;
    }

}
