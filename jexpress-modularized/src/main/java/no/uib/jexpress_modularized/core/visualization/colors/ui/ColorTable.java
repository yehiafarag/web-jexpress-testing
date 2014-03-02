/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactory;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ControlPoint;

/**
 *
 * @author Bjarte
 */
public class ColorTable implements Serializable{

    private ColorFactory colorFactory;
    private int res = 256;

    public ColorTable(ColorFactory colorFactory) {
        this.colorFactory = colorFactory;
    }

    public void paintColors(Graphics g, Rectangle area, boolean symmetric) {

        if (area.width < 0) {
            return;
        }

        if (symmetric) {
            Color[] neg = colorFactory.getNegativeColors(area.width / 2);
            Color[] pos = colorFactory.getPositiveColors(area.width / 2);


            int mid = (area.width / 2);

            int colorArea = mid;


            for (int i = 0; i < colorArea; i++) {

                double c = i / (double) colorArea;
                List<ControlPoint> colors = colorFactory.getControlPoints();

                int ind = (int) (colors.size() * c);
                if (i >= neg.length) {
                    g.setColor(neg[neg.length - 1]);
                } else {
                    g.setColor(neg[i]);
                }

                int L = mid - 1 - i;

                g.drawLine(area.x + L, area.y + 0, area.x + L, area.y + area.height);

                if (i >= pos.length) {
                    g.setColor(pos[pos.length - 1]);
                } else {
                    g.setColor(pos[i]);
                }

                L = mid + 1 + i;

                g.drawLine(area.x + L, area.y + 0, area.x + L, area.y + area.height);
            }
        } else {
            Color[] all = colorFactory.getAllColors(area.width);


            for (int i = 0; i < area.width; i++) {

                double c = i / (double) area.width;
                List<ControlPoint> colors = colorFactory.getControlPoints();

                int ind = (int) (colors.size() * c);

                g.setColor(all[i]);

                int L = i;

                g.drawLine(area.x + L, area.y + 0, area.x + L, area.y + area.height);


            }



        }


    }
}
