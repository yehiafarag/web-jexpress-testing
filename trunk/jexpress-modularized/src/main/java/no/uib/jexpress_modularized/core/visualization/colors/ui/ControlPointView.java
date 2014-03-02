/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.io.Serializable;
import java.util.List;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ControlPoint;

/**
 *
 * @author Bjarte
 */
public class ControlPointView implements Serializable{

    private static int PointWidth = 3;
    private static Color fg = new Color(100, 100, 100);
    private ControlPoint selected;

    public void paint(Graphics g, Rectangle valueArea, List<ControlPoint> P) {

        for (ControlPoint p : P) {
            paint(g, valueArea, p);
        }

    }

    public ControlPoint getControlPointAt(List<ControlPoint> P, Rectangle valueArea, Point pt) {

        for (ControlPoint p : P) {

            double val = valueArea.x + ((p.getLocation() + 1.0) / 2.0) * (double) valueArea.width;
            int H = valueArea.height;

            Shape s = createPointShape((int) val, valueArea.y, valueArea.height);
            if (s.contains(pt)) {
                return p;
            }

        }
        return null;
    }

    public void paint(Graphics g, Rectangle valueArea, ControlPoint P) {

        double val = valueArea.x + ((P.getLocation() + 1.0) / 2.0) * (double) valueArea.width;

        int H = valueArea.height;


        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape s = createPointShape((int) val, valueArea.y, valueArea.height);

        g.setColor(P.getColor());
        g2d.fill(s);

        g.setColor(fg);

        if (selected != null && selected.equals(P)) {
            g.setColor(Color.blue);

            Color c = P.getColor();
            if (c.getRed() + c.getGreen() < 200 && +c.getBlue() > 150) {
                g.setColor(Color.GREEN);
            }


            g.fillRect((int) (val - PointWidth), valueArea.y, (int) (PointWidth * 2), 5);

        }

        g2d.draw(s);

    }

    private static Shape createPointShape(int mid, int y, int H) {

        Polygon p = new Polygon();
        p.addPoint(mid, y + H);
        p.addPoint(mid - PointWidth, y + H - 4);
        p.addPoint(mid - PointWidth, y);
        p.addPoint(mid + PointWidth, y);
        p.addPoint(mid + PointWidth, y + H - 4);
        return p;
    }

    public ControlPoint getSelected() {
        return selected;
    }

    public void setSelected(ControlPoint selected) {
        this.selected = selected;
    }
}
