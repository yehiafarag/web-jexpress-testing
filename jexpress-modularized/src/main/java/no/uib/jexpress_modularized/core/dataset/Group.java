package no.uib.jexpress_modularized.core.dataset;

import no.uib.jexpress_modularized.core.model.Selection;
import java.awt.Color;
import java.io.Serializable;

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

}
