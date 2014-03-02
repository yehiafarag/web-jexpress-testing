/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.io.Serializable;
import javax.swing.JComboBox;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactoryList;

/**
 *
 * @author bjarte
 */
public class ColorTableCombobox extends JComboBox implements Serializable{

    ColorTableComboboxModel model;
    ColorTableCellRenderer renderer = new ColorTableCellRenderer();

    public ColorTableCombobox() {
        this.setRenderer(renderer);
    }

    public void setFactoryList(ColorFactoryList list) {
        model = new ColorTableComboboxModel(list);
        this.setModel(model);
    }
}
