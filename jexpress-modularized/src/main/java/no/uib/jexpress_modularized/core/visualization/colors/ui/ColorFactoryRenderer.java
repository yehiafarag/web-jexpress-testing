/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactory;

/**
 *
 * @author Bjarte
 */
public class ColorFactoryRenderer extends JComponent implements ListCellRenderer , Serializable{

    //private Border selected = BorderFactory.createLineBorder(Color.black);
    private Border selected = BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(200, 200, 220));
    private JPanel p;
    private int selectedIndex = -1;
    private ScaleAndAxis renderer = new ScaleAndAxis();

    public ColorFactoryRenderer() {
        setPreferredSize(new Dimension(100, 30));
        renderer.setPreviewMode(true);
        p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        p.add(renderer);
        p.setOpaque(false);

    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        if (!(value instanceof ColorFactory)) {
            return this;
        }

        ColorFactory factory = (ColorFactory) value;

        if (index == selectedIndex) {
            renderer.setPaintActive(true);
        } else {
            renderer.setPaintActive(false);
        }

        renderer.setColorFactory(factory);

        if (isSelected) {
            renderer.setBorder(selected);
        } else {
            renderer.setBorder(null);
        }

        return p;

    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
        this.paintBorder(g);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
