/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactory;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ControlPoint;

/**
 *
 * @author Bjarte
 */
public class ColorSliderComponent extends JComponent implements Serializable{

    private SliderUI UI;
    private ControlPointView cpv = new ControlPointView();
    private ControlPoint pressedPoint;
    private ColorFactory factory = new ColorFactory();
    private boolean mirror;

    public ColorSliderComponent() {

        this.setFocusable(true);

        addMouseListener();
        addKeyListener();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(500, 50));

        factory.initControlPoints(true);
        factory.setMirror(true);

        this.setFocusable(true);

        setFactory(factory);
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public void setFactory(ColorFactory factory) {
        this.factory = factory;
        UI = new SliderUI(factory);
        repaint();
    }

    public ControlPoint getSelected() {
        return pressedPoint;
    }

    public void setSymmetric(boolean symmetric) {
        int conf = JOptionPane.showConfirmDialog(this, "This will reset all color control points. Continue?");
        if (conf == JOptionPane.OK_OPTION) {
            factory.initControlPoints(symmetric);
        }
    }

    private void addKeyListener() {

        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == e.VK_DELETE) {

                    if (pressedPoint != null) {
                        factory.removeControlPoint(pressedPoint, true);
                        repaint();

                    }
                }

            }
        });

    }

    private void addMouseListener() {

        this.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                Rectangle r = getControlPointArea();

                ControlPoint cp = cpv.getControlPointAt(factory.getControlPoints(), r, e.getPoint());
                if (cp != null) {
                    cpv.setSelected(cp);


                    if (e.getClickCount() == 2) {
                        Color color = JColorChooser.showDialog(ColorSliderComponent.this, "Select Color", cp.getColor());

                        if (color != null) {
                            cp.setColor(color);
                        }
                        repaint();
                        factory.resetColorTable();
                        ColorSliderComponent.this.firePropertyChange("ColorsChanged", 0, 1);

                    }


                } else {

                    if (e.getClickCount() == 2) {

                        double value = e.getPoint().x / (double) getWidth();
                        double cpval = (2.0 * value) - 1.0;

                        ControlPoint ncp = new ControlPoint();
                        ncp.setColor(Color.gray);
                        ncp.setLocation(cpval);
                        factory.addControlPoint(ncp);
                        cpv.setSelected(ncp);
                        pressedPoint = ncp;

                        if (mirror) {

                            ControlPoint ncp2 = new ControlPoint();
                            ncp2.setColor(Color.gray);
                            ncp2.setLocation(-cpval);

                            factory.addControlPoint(ncp2);

                            ncp.setPartner(ncp2);
                            ncp2.setPartner(ncp);
                        }

                    }
                    factory.resetColorTable();
                    ColorSliderComponent.this.firePropertyChange("ColorsChanged", 0, 1);
                    ColorSliderComponent.this.requestFocus();
                }



                repaint();
            }

            public void mousePressed(MouseEvent e) {

                Rectangle r = getControlPointArea();
                ControlPoint cp = cpv.getControlPointAt(factory.getControlPoints(), r, e.getPoint());
                if (cp != null && !cp.isFixed()) {
                    pressedPoint = cp;
                    cpv.setSelected(cp);
                } else {
                    pressedPoint = null;
                }
            }
        });


        this.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                Rectangle r = getControlPointArea();

                Point np = new Point(e.getPoint().x - r.x, e.getPoint().y - r.y);


                if (pressedPoint != null) {

                    double value = np.x / (double) r.getWidth();

                    double cpval = (2.0 * value) - 1.0;

                    //if(cpval>0) cpval = cpval - (   );


                    pressedPoint.setLocation(cpval);

                    ControlPoint partner = pressedPoint.getPartner();
                    if (partner != null) {
                        partner.setLocation(-pressedPoint.getLocation());
                    }
                    repaint();
                    factory.resetColorTable();
                    ColorSliderComponent.this.firePropertyChange("ColorsChanged", 0, 1);
                }

            }
        });


    }

    private Rectangle getControlPointArea() {
        //Rectangle B = getBounds();
        Rectangle B = this.getVisibleRect();
        return new Rectangle(B.x + 4, B.y + B.height - 45, B.width - 8, 20);

    }

    public void paintComponent(Graphics g) {

        this.paintBorder(g);

        Rectangle R = this.getVisibleRect();

        Rectangle B = R;//getBounds();
        g.setClip(B);
//        g.setColor(Color.green);
//        
//        g.drawRect(R.x, R.y, R.width, R.height);
//        

        // g.fillRect(B.x, B.y, B.width, B.height);

        if (UI != null) {
            UI.paintSlider(g, B, factory.isSymmetric());

            Rectangle r = getControlPointArea();

            cpv.paint(g, r, factory.getControlPoints());
        }


    }

    public boolean isMirror() {
        return mirror;
    }
}
