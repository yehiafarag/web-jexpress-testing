/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactory;

/**
 *
 * @author Bjarte
 */
public class SliderUI implements Serializable{

    private ColorFactory colorFactory;
    private Color lines = new Color(120, 120, 210);
    private ColorTable tab;
    private int margin = 4;

    public SliderUI(ColorFactory colorFactory) {

        this.colorFactory = colorFactory;
        tab = new ColorTable(colorFactory);
    }

    public void paintSlider(Graphics g, Rectangle area, boolean symmetric) {

        int W = area.width;
        int H = area.height;

        int CW = W / 2;
        int CH = H / 2;

        Rectangle ar = new Rectangle(area.x + 4, area.y + H - 20, W - 8, H);


        tab.paintColors(g, ar, symmetric);








    }
}
