/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 *
 * @author Bjarte
 */
public class ColorPicker extends JComponent implements Serializable {

    private Color color = Color.black;

    public ColorPicker() {
        addMouseListener();
        setPreferredSize(new Dimension(24, 24));
    }

    private void addMouseListener() {

        this.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                color = JColorChooser.showDialog(ColorPicker.this, "Select Color", color);
                repaint();
                ColorPicker.this.firePropertyChange("ColorChange", 0, 1);
            }
        });

    }

    public void paintComponent(Graphics g) {

        this.paintBorder(g);

        g.setColor(getColor());
        g.fillRect(2, 2, getWidth() - 5, getHeight() - 5);

        g.setColor(Color.black);
        g.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }
}
