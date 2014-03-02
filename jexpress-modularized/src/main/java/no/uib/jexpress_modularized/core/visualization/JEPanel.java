/*
 * JEPanel.java
 *
 * Created on 12. september 2004, 00:36
 */
package no.uib.jexpress_modularized.core.visualization;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 *
 * @author bjarte dysvik
 */
public class JEPanel extends JPanel implements Serializable{

    private double TextPercentHeight = 30.0;
    private Color ShadowColor = new Color(190, 190, 210);
    private Color UColor = Color.white;
    private Color LColor = new Color(230, 230, 255);
    private String fontName = "Book Antiqua";
    private String text = "J-Express Modularized";
    private double percentStartGradient = 50.0;
    private boolean paintText = true;

    /**
     * Creates a new instance of JEPanel
     */
    public JEPanel() {
    }

    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        setOpaque(false);
        g2d.setPaint(new GradientPaint(0f, (float) ((percentStartGradient / 100.0) * getHeight()), UColor, 0f, (float) getHeight(), LColor));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (paintText) {
            paintText(g);
        }

        super.paint(g);
    }

    public void paintText(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Object textantialias = g2d.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


        int tsize = this.getTextSize(g);

        g2d.setFont(new Font(fontName, 1, tsize));



        //int x = g.getFontMetrics().getHeight();
        //int y = g.getFontMetrics().stringWidth("molmine");

        g2d.rotate(-Math.PI / 2);
        // g.setColor(new Color(170,170,190));
        //g2d.drawLine(getWidth()/2,getHeight()/2, getWidth(),getHeight());


        int descent = g.getFontMetrics().getDescent();

        g2d.translate(-(getHeight() - 9), (getWidth() - descent));


        //g2d.setPaint(new GradientPaint(20f,270f,Color.white,20f,360f,new Color(150,150,150)));
        g.setColor(ShadowColor);
        g2d.drawString(text, -2, -2);
        g.setColor(Color.white);
        g2d.drawString(text, -1, -1);


        g2d.translate((getHeight() - 9), -(getWidth() - descent));
        g2d.rotate(Math.PI / 2);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textantialias);

        // Font f = Font.getFont("Book Antiqua");
        // if(f==null) System.out.print("\nNo");


    }

    private int getTextSize(Graphics g) {

        Font pr = g.getFont();
        int psize = (int) ((TextPercentHeight / 100.0) * (double) getHeight());


        int asize = 1;
        int stepsize = 1;

        while (asize < psize) {
            g.setFont(new Font(fontName, 1, stepsize));
            asize = g.getFontMetrics().stringWidth(text);
            stepsize++;
        }

        g.setFont(pr);
        return stepsize;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("test");
        f.getContentPane().add(new JEPanel());
        f.setSize(400, 500);
        f.setVisible(true);
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
    }

    /**
     * Getter for property fontName.
     *
     * @return Value of property fontName.
     */
    public java.lang.String getFontName() {
        return fontName;
    }

    /**
     * Setter for property fontName.
     *
     * @param fontName New value of property fontName.
     */
    public void setFontName(java.lang.String fontName) {
        this.fontName = fontName;
    }

    /**
     * Getter for property LColor.
     *
     * @return Value of property LColor.
     */
    public java.awt.Color getLColor() {
        return LColor;
    }

    /**
     * Setter for property LColor.
     *
     * @param LColor New value of property LColor.
     */
    public void setLColor(java.awt.Color LColor) {
        this.LColor = LColor;
    }

    /**
     * Getter for property ShadowColor.
     *
     * @return Value of property ShadowColor.
     */
    public java.awt.Color getShadowColor() {
        return ShadowColor;
    }

    /**
     * Setter for property ShadowColor.
     *
     * @param ShadowColor New value of property ShadowColor.
     */
    public void setShadowColor(java.awt.Color ShadowColor) {
        this.ShadowColor = ShadowColor;
    }

    /**
     * Getter for property text.
     *
     * @return Value of property text.
     */
    public java.lang.String getText() {
        return text;
    }

    /**
     * Setter for property text.
     *
     * @param text New value of property text.
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }

    /**
     * Getter for property TextPercentHeight.
     *
     * @return Value of property TextPercentHeight.
     */
    public double getTextPercentHeight() {
        return TextPercentHeight;
    }

    /**
     * Setter for property TextPercentHeight.
     *
     * @param TextPercentHeight New value of property TextPercentHeight.
     */
    public void setTextPercentHeight(double TextPercentHeight) {
        this.TextPercentHeight = TextPercentHeight;
    }

    /**
     * Getter for property UColor.
     *
     * @return Value of property UColor.
     */
    public java.awt.Color getUColor() {
        return UColor;
    }

    /**
     * Setter for property UColor.
     *
     * @param UColor New value of property UColor.
     */
    public void setUColor(java.awt.Color UColor) {
        this.UColor = UColor;
    }

    /**
     * Getter for property paintText.
     *
     * @return Value of property paintText.
     */
    public boolean isPaintText() {
        return paintText;
    }

    /**
     * Setter for property paintText.
     *
     * @param paintText New value of property paintText.
     */
    public void setPaintText(boolean paintText) {
        this.paintText = paintText;
    }

    /**
     * Getter for property percentStartGradient.
     *
     * @return Value of property percentStartGradient.
     */
    public double getPercentStartGradient() {
        return percentStartGradient;
    }

    /**
     * Setter for property percentStartGradient.
     *
     * @param percentStartGradient New value of property percentStartGradient.
     */
    public void setPercentStartGradient(double percentStartGradient) {
        this.percentStartGradient = percentStartGradient;
    }
}
