/*

 * CLabel.java

 *

 * Created on 4. september 2003, 17:56

 */
package no.uib.jexpress_modularized.core.visualization.charts;

import java.awt.*;
import java.io.Serializable;



import javax.swing.*;

/**
 *
 *
 *
 * @author bjarte dysvik
 *
 */
public class CLabel extends JComponent implements Serializable{

    private String text = "";
    private String exponent = "";
    private Rectangle area = new Rectangle(0, 0, 100, 100);
    boolean rotated = false;
    private Font font = new Font("Times New Roman", 1, 10);
    //public static int Center = 0;
    //public static int Right = 1;
    public int alignment = SwingConstants.CENTER;

    /**
     * Creates a new instance of CLabel
     */
    public CLabel() {
    }

    public CLabel(String text) {

        this.text = text;

        this.setBorder(BorderFactory.createLineBorder(new Color(110, 110, 110)));

        setFont(new Font("ARIAL", 1, 45));

    }

    public void setText(String text) {

        this.text = text;

    }

    public String getText() {

        return text;

    }

    public static void main(String[] args) {



        JFrame f = new JFrame();

        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);



        CLabel l = new CLabel("x 10");

        l.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(160, 160, 160)));



        l.setFont(new Font("Times New Roman", 1, 10));

        f.getContentPane().add(l);
        f.pack();
        f.setVisible(true);
    }

    public Dimension getPreferredSize() {

        int w = 0;
        int h = 0;

        Font f = font;//this.getFont();


        if (f == null) {
            f = new Font("ARIAL", 1, 2);
        }

        FontMetrics fm = getFontMetrics(f);

        int expH = 0;

        if (exponent != null && exponent.length() > 0) {
            //expH= 5;//(int)(((double)fm.getAscent())/3.0);
        }


        if (rotated) {
            return new Dimension(fm.getHeight() + 4, fm.stringWidth(text + exponent) + 4 + expH);
        } else {
            return new Dimension(fm.stringWidth(text + exponent) + expH + 4, fm.getHeight() + 4);
        }
    }

    public void setExponent(String exponent) {
        this.exponent = exponent;
    }

    public void setArea(Rectangle area) {
        this.area = area;
    }

    public void paint(Graphics g) {

        int x = 0;

        int vh = 0;

        int vv = 0;

        int y = 0;



        int sx = 0;

        int sy = 0;



        g.setFont(font);


        FontMetrics fm = g.getFontMetrics();



        StringBuffer sb = new StringBuffer(text);



        if (!rotated) {

            if (fm.stringWidth(sb.toString()) > getWidth()) {



                while (sb.length() > 0 && fm.stringWidth(sb.toString() + "..") > getWidth()) {

                    sb.setLength(sb.length() - 1);

                }



                //sb.setLength(sb.length()-exponent.length());



                sb.append("..");

            }

        } else {

            if (fm.stringWidth(sb.toString()) > getHeight()) {



                while (sb.length() > 0 && fm.stringWidth(sb.toString() + "..") > getHeight()) {

                    sb.setLength(sb.length() - 1);

                }

                sb.append("..");

            }

        }



        int expH = 0;



        if (exponent != null && exponent.length() > 0) {
            //expH= 5;//(int)(((double)fm.getAscent()*0.8)/3.0);
        }



        int strW = fm.stringWidth(sb.toString() + exponent);

        int strH = fm.getHeight();



        int line = fm.getAscent();// +fm.getDescent();



        Graphics2D g2d = (Graphics2D) g;



        if (rotated) {

            x = (getWidth() / 2) + ((strH) / 4);



            if (alignment == SwingConstants.CENTER) {

                vh = (getHeight() / 2) + (strW / 2);

            } else if (alignment == SwingConstants.RIGHT) {

                vh = (strW);

            } else if (alignment == SwingConstants.LEFT) {

                vh = (getHeight()) - 1;

            }



            g2d.translate(area.x + 1 + x, area.y + 1 + vh - expH);



            g2d.rotate(-Math.PI / 2);

            g.drawString(sb.toString(), 0, 0);



            if (exponent != null && exponent.length() > 0) {

                int sh = fm.getHeight();

                int sw = fm.stringWidth(exponent);

                g2d.translate(fm.stringWidth(sb.toString()), -sh / 3);

                g2d.scale(0.8, 0.8);



                // FontMetrics fm2 = g2d.getFontMetrics();



                g.drawString(exponent, 0, 0);

                g2d.scale(1.0 / 0.8, 1.0 / 0.8);



                g2d.translate(-(fm.stringWidth(sb.toString())), sh / 3);

            }







            g2d.rotate(Math.PI / 2);



            g2d.translate(-area.x - x - 1, -area.y - vh - 1 + expH);

        } else {





            x = (getHeight() / 2) - (strH / 2);



            if (alignment == SwingConstants.CENTER) {

                vv = (getWidth() / 2) - (strW / 2);

            }



            g2d.translate(area.x + vv, area.y + 1 + x + line + expH);

            g.drawString(sb.toString(), 0, 0);







            if (exponent != null && exponent.length() > 0) {

                int sh = fm.getHeight();

                int sw = fm.stringWidth(exponent);



                g2d.translate(fm.stringWidth(sb.toString()), -sh / 3);

                g2d.scale(0.8, 0.8);



                //FontMetrics fm2 = g2d.getFontMetrics();





                g.drawString(exponent, 0, 0);

                g2d.scale(1.0 / 0.8, 1.0 / 0.8);

                g2d.translate(-(fm.stringWidth(sb.toString())), sh / 3);

            }

            g2d.translate(-area.x - vv, -area.y - 1 - x - line - expH);

        }



        //     g.setColor(Color.blue);

        //     g2d.draw(area);

        //   this.paintBorder(g);  



    }

    /**
     * Getter for property alignment.
     *
     * @return Value of property alignment.
     *
     *
     *
     */
    public int getAlignment() {

        return alignment;

    }

    /**
     * Setter for property alignment.
     *
     * @param alignment New value of property alignment.
     *
     *
     *
     */
    public void setAlignment(int alignment) {

        this.alignment = alignment;

    }

    /**
     * Getter for property font.
     *
     * @return Value of property font.
     *
     *
     *
     */
    public java.awt.Font getFont() {

        return font;

    }

    /**
     * Setter for property font.
     *
     * @param font New value of property font.
     *
     *
     *
     */
    public void setFont(java.awt.Font font) {

        this.font = font;

    }

    /**
     * Getter for property rotated.
     *
     * @return Value of property rotated.
     *
     *
     *
     */
    public boolean isRotated() {

        return rotated;

    }

    /**
     * Setter for property rotated.
     *
     * @param rotated New value of property rotated.
     *
     *
     *
     */
    public void setRotated(boolean rotated) {

        this.rotated = rotated;

    }

    /**
     * Getter for property exponent.
     *
     * @return Value of property exponent.
     *
     *
     *
     */
    public java.lang.String getExponent() {

        return exponent;

    }
}
