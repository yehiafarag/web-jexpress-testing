/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactory;

/**
 *
 * @author bjarte
 */
public class ColorTableCellRenderer extends DefaultListCellRenderer implements Serializable {

    private ColorTable table;
    private ColorFactory factory;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        factory = (ColorFactory) value;
        table = new ColorTable(factory);

        if (isSelected) {
            setBorder(BorderFactory.createLineBorder(Color.blue));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.white));
        }

        this.setPreferredSize(new Dimension(100, 10));
        return this;
    }

    public void paintComponent(Graphics g) {
        if (table == null || factory == null) {
            return;
        }
        Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
        table.paintColors(g, rect, factory.isSymmetric());
        this.paintBorder(g);
    }
}