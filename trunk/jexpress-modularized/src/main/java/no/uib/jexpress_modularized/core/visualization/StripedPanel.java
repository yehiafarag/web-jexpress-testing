/*

 * srtipedPanel.java

 *

 * Created on 29. april 2002, 16:28

 */
package no.uib.jexpress_modularized.core.visualization;

import java.awt.Color;

/**
 *
 *
 *
 * @author bjarte dysvik
 *
 */
public class StripedPanel extends javax.swing.JPanel implements java.io.Serializable {

//    private static final String PROP_SAMPLE_PROPERTY = "SampleProperty";
    //  private String sampleProperty;
    //  private java.beans.PropertyChangeSupport propertySupport;
    private Color color1 = Color.black;
    private Color color2 = Color.gray;

    /**
     * Creates new srtipedPanel
     */
    public StripedPanel() {

        //  propertySupport = new java.beans.PropertyChangeSupport( this );

        setOpaque(false);

    }

    public void setColor1(Color color) {

        color1 = color;

    }

    public Color getColor1() {

        return color1;

    }

    public void setColor2(Color color) {

        color2 = color;

    }

    public Color getColor2() {

        return color2;

    }

    public void paintComponent(java.awt.Graphics g) {



        g.setColor(getBackground());

        int width = getWidth();

        int height = getHeight();

        g.fillRect(0, 0, width, height);



        java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;



        g2d.setPaint(new java.awt.GradientPaint(0, 0, color1, width, 0, color2));

        for (int i = 0; i < height; i++) {
            if (i % 2 == 0) {
                g.drawLine(0, i, width, i);
            }
        }





        super.paintComponent(g);

    }
    /*
     * public String getSampleProperty() {
     *
     * return sampleProperty;
     *
     * }
     *
     *
     *
     * public void setSampleProperty(String value) {
     *
     * String oldValue = sampleProperty;
     *
     * sampleProperty = value;
     *
     * propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue,
     * sampleProperty);
     *
     * }
     *
     *
     *
     *
     *
     * public void addPropertyChangeListener(java.beans.PropertyChangeListener
     * listener) {
     *
     * propertySupport.addPropertyChangeListener(listener);
     *
     * }
     *
     *
     *
     * public void
     * removePropertyChangeListener(java.beans.PropertyChangeListener listener)
     * {
     *
     * propertySupport.removePropertyChangeListener(listener);
     *
     * }
     *
     */
}
