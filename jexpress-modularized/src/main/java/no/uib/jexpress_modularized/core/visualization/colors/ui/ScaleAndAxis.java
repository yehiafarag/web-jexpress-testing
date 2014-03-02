/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import javax.swing.JComponent;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactory;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author Bjarte
 */
public class ScaleAndAxis extends JComponent implements Serializable{

    private ColorFactory factory = new ColorFactory();
    private NumberAxis axis;
    private ColorTable table;
    private SymbolAxis axis2;
    private boolean paintActive = false;
    private boolean previewMode = false;  //do not draw scale if previewMode and no automatic value range
    private boolean forceNoValues = false;

    public ScaleAndAxis() {
        this.setPreferredSize(new Dimension(240, 45));
        factory.initControlPoints(true);

        axis = new NumberAxis("");
        axis.setRange(-56.0, 156.0);

        axis2 = new SymbolAxis("", new String[]{""});
        axis2.setRange(-1.0, 1.0);
        axis2.setGridBandPaint(Color.green);
        axis2.setTickUnit(new NumberTickUnit(0.5));
        axis2.setGridBandsVisible(false);

        // axis.setTickUnit(new NumberTickUnit(10.0));

        table = new ColorTable(factory);

    }

    public ScaleAndAxis(ColorFactory factory) {
        this.factory = factory;
        this.setPreferredSize(new Dimension(240, 45));
        factory.initControlPoints(true);

        axis = new NumberAxis("");
        axis.setRange(-56.0, 156.0);

        axis2 = new SymbolAxis("", new String[]{""});
        axis2.setRange(-1.0, 1.0);
        axis2.setGridBandPaint(Color.green);
        axis2.setTickUnit(new NumberTickUnit(0.5));
        axis2.setGridBandsVisible(false);

        //axis2.setTickUnit(new NumberTickUnit(10.0));

        table = new ColorTable(factory);
    }

    public void setColorFactory(ColorFactory fac) {
        this.factory = fac;
        table = new ColorTable(factory);
        repaint();
    }

    public void paintComponent(Graphics g) {
        //Rectangle R = g.getClipBounds();

        //Rectangle R =getBounds();
        Rectangle R = new Rectangle(0, 0, getWidth(), getHeight());

        int valuemargin = 13;


        if (factory != null && !factory.getValueRange().isAutomatic()) {

            double ma = factory.getValueRange().getMax();
            double mi = factory.getValueRange().getMin();

            double mmx = Math.max(Math.abs(ma), Math.abs(mi));

            if (mmx >= 1000) {
                valuemargin = 15;
            }
            if (mmx >= 10000) {
                valuemargin = 18;
            }
            if (mmx >= 100000) {
                valuemargin = 25;
            }

        }

        int rightMargin = valuemargin * 2;

        this.paintBorder(g);

        Rectangle colors = new Rectangle(R.x + valuemargin, R.y + 4, R.width - rightMargin, R.height - 36);

        Rectangle2D.Double totalR = new Rectangle2D.Double(R.x + valuemargin, R.y + 100, R.width - rightMargin, R.height);
        Rectangle2D.Double axisR = new Rectangle2D.Double(R.x + valuemargin, R.y + R.height / 2, R.width - rightMargin, R.height / 2);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (forceNoValues || (previewMode && factory.getValueRange().isAutomatic())) {
            axis2.draw(g2d, R.y + R.height - 25, totalR, axisR, RectangleEdge.BOTTOM, null);
        } else if (!factory.getValueRange().isAutomatic()) {
            axis.setRange(factory.getValueRange().getMin(), factory.getValueRange().getMax());
            axis.draw(g2d, R.y + R.height - 25, totalR, axisR, RectangleEdge.BOTTOM, null);
        } else {

            //if(factory.getValueRange().isSymmetric()){

            //   axis.setRange(-factory.getValueRange().getABSDataMax(), factory.getValueRange().getABSDataMax());  
            //}
            //else{
            axis.setRange(factory.getValueRange().getDataMin(), factory.getValueRange().getDataMax());
            // }

            axis.draw(g2d, R.y + R.height - 25, totalR, axisR, RectangleEdge.BOTTOM, null);
        }

        table.paintColors(g, colors, factory.isSymmetric());

        if (isPaintActive()) {

            g2d.setColor(new Color(140, 190, 140));

            String act = "Default";
            int tm = g2d.getFontMetrics().stringWidth(act) / 2;
            int mid = this.getWidth() / 2;

            int dw = mid - tm;

            g2d.drawString(act, dw, R.height - 2);
        }
    }

    public boolean isPaintActive() {
        return paintActive;
    }

    public void setPaintActive(boolean paintActive) {
        this.paintActive = paintActive;
    }

    public void setPreviewMode(boolean previewMode) {
        this.previewMode = previewMode;
    }

    public boolean isForceNoValues() {
        return forceNoValues;
    }

    public void setForceNoValues(boolean forceNoValues) {
        this.forceNoValues = forceNoValues;
    }
}
