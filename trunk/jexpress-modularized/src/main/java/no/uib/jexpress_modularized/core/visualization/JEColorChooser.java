/*

 * JEColorChooser.java

 *

 * Created on 6. mars 2002, 20:05

 */
package no.uib.jexpress_modularized.core.visualization;

import java.awt.event.ActionListener;

import java.awt.Color;

import javax.swing.*;

import java.awt.event.*;
import java.io.Serializable;

/**
 *
 *
 *
 * @author bjarte
 *
 */
public class JEColorChooser extends javax.swing.JColorChooser implements Serializable{

    ActionListener cancel = null;
    ActionListener ok = null;
    javax.swing.JDialog dial = null;
    //JEColorChooser self = null;
    //javax.swing.JWindow f = null;
    java.awt.Window f;
    javax.swing.JButton okb = new javax.swing.JButton("Ok");
    javax.swing.JButton cancelb = new javax.swing.JButton("Cancel");
    Color ret = null;
    javax.swing.JPanel p = null;
    private boolean hasAlpha = false;
    private int alphaValue = -1;

    public Color showNonStaticDialog(java.awt.Component component, String title, Color initialColor, java.awt.Window win) {

        if (hasAlpha) {
            return showNonStaticDialog(component, title, initialColor, win, true);
        }

        f = win;



        setColor(initialColor);



        this.ret = initialColor;



        if (cancel == null) {

            cancel = new ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {

                    dial.setVisible(false);

                    dial.dispose();

                }
            };



            ok = new ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {

                    JEColorChooser.this.ret = getColor();

                    dial.setVisible(false);

                    dial.dispose();

                }
            };



            cancelb.addActionListener(cancel);

            okb.addActionListener(ok);



            p = new javax.swing.JPanel();

            p.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));



            okb.setFont(new java.awt.Font("Dialog", 0, 12));

            cancelb.setFont(new java.awt.Font("Dialog", 0, 12));

            okb.setBackground(new Color(204, 204, 214));

            cancelb.setBackground(new Color(204, 204, 214));



            p.add(okb);

            p.add(cancelb);



        }



        JFrame tmp = null;



        if (f instanceof JFrame) {
            dial = new javax.swing.JDialog((JFrame) f, title, true);
        } else if (f instanceof JDialog) {
            dial = new javax.swing.JDialog((JDialog) f, title, true);
        } else {
            dial = new javax.swing.JDialog(tmp, title, true);
        }

        dial.getContentPane().add("South", p);

        dial.getContentPane().add("Center", this);

        dial.pack();



        if (f != null) {
            dial.setLocationRelativeTo(f);
        }



        //dial.setLocation(component.getLocationOnScreen().x,component.getLocationOnScreen().y);

        //dial.setModal(true);

        dial.setVisible(true);







        return ret;

    }

    public Color showNonStaticDialog(java.awt.Component component, String title, Color initialColor, java.awt.Window win, boolean hasAlpha) {

        this.hasAlpha = hasAlpha;

        if (hasAlpha) {
            alphaValue = 100;
        }

        f = win;

        this.setColor(initialColor);



        this.ret = initialColor;



        if (cancel == null) {

            cancel = new ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {

                    dial.setVisible(false);

                    dial.dispose();

                }
            };



            ok = new ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {

                    JEColorChooser.this.ret = getColor();

                    dial.setVisible(false);

                    dial.dispose();

                }
            };



            cancelb.addActionListener(cancel);

            okb.addActionListener(ok);



            p = new javax.swing.JPanel();

            p.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));



            okb.setFont(new java.awt.Font("Dialog", 0, 12));

            cancelb.setFont(new java.awt.Font("Dialog", 0, 12));

            okb.setBackground(new Color(204, 204, 214));

            cancelb.setBackground(new Color(204, 204, 214));



            p.add(okb);

            p.add(cancelb);

            if (hasAlpha) {

                p.add(getAlphaPanel());

            }

        }



        JFrame tmp = null;



        if (f instanceof JFrame) {
            dial = new javax.swing.JDialog((JFrame) f, title, true);
        } else if (f instanceof JDialog) {
            dial = new javax.swing.JDialog((JDialog) f, title, true);
        } else {
            dial = new javax.swing.JDialog(tmp, title, true);
        }

        dial.getContentPane().add("South", p);

        dial.getContentPane().add("Center", this);

        dial.pack();



        if (f != null) {
            dial.setLocationRelativeTo(f);
        }



        //dial.setLocation(component.getLocationOnScreen().x,component.getLocationOnScreen().y);

        //dial.setModal(true);

        dial.setVisible(true);



        if (hasAlpha && alphaValue > 0) {
            ret = new Color(ret.getRed(), ret.getGreen(), ret.getBlue(), alphaValue);
        }



        return ret;

    }

    public JEColorChooser() {

        final javax.swing.JLabel previewLabel = new javax.swing.JLabel("Selected: ");

        final javax.swing.JLabel previewLabel1 = new javax.swing.JLabel("        ");

        previewLabel1.setOpaque(true);

        final javax.swing.JPanel pan = new javax.swing.JPanel();



        pan.add(previewLabel);

        pan.add(previewLabel1);



        setPreviewPanel(pan);



        getSelectionModel().addChangeListener(
                new javax.swing.event.ChangeListener() {

                    public void stateChanged(javax.swing.event.ChangeEvent e) {

                        Color newColor = getColor();

                        previewLabel1.setBackground(newColor);

                        previewLabel1.repaint();



                    }
                });

    }

    public JEColorChooser(boolean hasAlpha) {

        final javax.swing.JLabel previewLabel = new javax.swing.JLabel("Selected: ");

        final javax.swing.JLabel previewLabel1 = new javax.swing.JLabel("        ");

        previewLabel1.setOpaque(true);

        final javax.swing.JPanel pan = new javax.swing.JPanel();



        pan.add(previewLabel);

        pan.add(previewLabel1);







        getSelectionModel().addChangeListener(
                new javax.swing.event.ChangeListener() {

                    public void stateChanged(javax.swing.event.ChangeEvent e) {

                        Color newColor = getColor();

                        previewLabel1.setBackground(newColor);

                        previewLabel1.repaint();



                    }
                });









    }

    public int getAlphaValue() {

        return alphaValue;

    }

    public JPanel getAlphaPanel() {



        JPanel p = new JPanel(new java.awt.BorderLayout(5, 5));

        final JSlider s = new javax.swing.JSlider();

        s.setMaximum(255);

        s.setValue(alphaValue);



        s.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent e) {

                alphaValue = s.getValue();

            }
        });

        p.add(s);

        p.add("West", new JLabel("Alpha"));

        return p;

    }

    /**
     *
     * Getter for property hasAlpha.
     *
     * @return Value of property hasAlpha.
     *
     */
    public boolean isHasAlpha() {

        return hasAlpha;

    }

    /**
     *
     * Setter for property hasAlpha.
     *
     * @param hasAlpha New value of property hasAlpha.
     *
     */
    public void setHasAlpha(boolean hasAlpha) {

        this.hasAlpha = hasAlpha;

    }
}
