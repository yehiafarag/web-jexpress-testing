package no.uib.jexpress_modularized.core.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.border.LineBorder;

public class ColorButton extends javax.swing.JButton implements java.awt.event.ActionListener, Serializable {

    javax.swing.JInternalFrame refocus = null;
    Color color = Color.black;
    javax.swing.JDialog d = null;
    JEColorChooser chooser = new JEColorChooser();
    javax.swing.JDialog dial = null;
    java.awt.Window owner = null;
    private boolean hasAlpha = false;
    private LineBorder br = new LineBorder(Color.GRAY);

    public void setChooser(JEColorChooser chooser) {
        this.chooser = chooser;
    }

    public void setOwner(javax.swing.JFrame owner) {
        this.owner = owner;
    }

    public ColorButton() {
        addActionListener(this);
        setBackground(color);
    }

    public Color getColor() {
        if (color == null) {
            return Color.black;
        } else if (hasAlpha) {
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        } else {
            return new Color(color.getRed(), color.getGreen(), color.getBlue());
        }
    }

    public void setColor(Color c) {
        color = c;
        setBackground(c);
    }

    public void actionPerformed(java.awt.event.ActionEvent p1) {

        //System.out.print("\nancestor: "+getRootPane().getTopLevelAncestor().getClass().getName());

        // @TODO: reimplement??

//        if(owner==null && getRootPane().getTopLevelAncestor() instanceof ImageAnalysis.ImageData.Suite.JProcessCreator){
//            ImageAnalysis.ImageData.Suite.JProcessCreator cr = (ImageAnalysis.ImageData.Suite.JProcessCreator)getRootPane().getTopLevelAncestor();
//            owner = (javax.swing.JFrame)cr.suite.getRootPane().getTopLevelAncestor();
//             
//        }
        if (owner == null && (getRootPane().getTopLevelAncestor() instanceof javax.swing.JFrame)) {
            owner = (javax.swing.JFrame) getRootPane().getTopLevelAncestor();
        }
        if (owner == null && (getRootPane().getTopLevelAncestor() instanceof javax.swing.JDialog)) {
            owner = (javax.swing.JDialog) (getRootPane().getTopLevelAncestor());
        }
        //  color.setOwner((javax.swing.JFrame)owner);

        if (this.hasAlpha) {
            this.setColor(chooser.showNonStaticDialog(this, "Choose Color", getColor(), owner, true));
        } else {
            this.setColor(chooser.showNonStaticDialog(this, "Choose Color", getColor(), owner));
        }
        this.firePropertyChange("Color changed", false, true);
    }

    /**
     * Getter for property hasAlpha.
     *
     * @return Value of property hasAlpha.
     */
    public boolean isHasAlpha() {
        return hasAlpha;
    }

    public void paintComponent(Graphics g) {
        g.setColor(this.getColor());
        g.fillRect(0, 0, getWidth(), getHeight());

        br.paintBorder(this, g, 0, 0, getWidth(), getHeight());
    }

    /**
     * Setter for property hasAlpha.
     *
     * @param hasAlpha New value of property hasAlpha.
     */
    public void setHasAlpha(boolean hasAlpha) {
        this.hasAlpha = hasAlpha;
    }
}
