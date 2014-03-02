/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.DefaultComboBoxModel;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactory;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactoryList;

/**
 *
 * @author bjarte
 */
public class ColorTableComboboxModel extends DefaultComboBoxModel implements Serializable{

    private ColorFactoryList factoryList;

    public ColorTableComboboxModel(ColorFactoryList List) {
        this.factoryList = List;
        reInit();


        factoryList.addPropertyChangeListener("ColorListChanged", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                reInit();
                //ColorTableComboboxModel.this.fireContentsChanged(factoryList, 0, factoryList.getAllFactories().size());
            }
        });
    }

    private void reInit() {
        this.removeAllElements();

        for (ColorFactory f : factoryList.getAllFactories()) {
            this.addElement(f);
        }
        this.setSelectedItem(factoryList.getActiveFactory());

    }
}
