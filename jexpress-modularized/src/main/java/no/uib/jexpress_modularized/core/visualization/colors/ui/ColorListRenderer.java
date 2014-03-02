/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

/**
 *
 * @author Bjarte
 */
public class ColorListRenderer extends JComponent implements ListCellRenderer, Serializable {

    private Color color;
    private Border selected = BorderFactory.createLineBorder(Color.black);

    public ColorListRenderer() {
        setPreferredSize(new Dimension(10, 15));

    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        if (!(value instanceof Color)) {
            return this;
        }

        color = (Color) value;

        if (isSelected) {
            setBorder(selected);
        } else {
            setBorder(null);
        }

        return this;

    }

    public void paintComponent(Graphics g) {

        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(color);

        g.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
        this.paintBorder(g);
    }
}
