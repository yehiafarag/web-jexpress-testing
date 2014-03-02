/*
 * BackgroundFactory.java
 *
 * Created on 31. januar 2002, 14:35
 */
package no.uib.jexpress_modularized.core.visualization;

import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.*;
import java.awt.*;
import java.awt.image.*;
import java.io.Serializable;
import javax.swing.*;

/**
 *
 * @author bjarte
 */
public class BackgroundFactory extends JComponent implements Serializable {

    private DensityFactory dens;
    private int[] DensX;
    private int[] DensY;
    private boolean[] hide;
    public Color Single = Color.white;
    private int method = 0;
    public static int TOP_BOTTOM = 0;
    public static int UPPERLEFT_LOWERRIGHT = 1;
    public int GradientType = 0;
    public Color gradient1 = new Color(250, 100, 100);
    public Color gradient2 = new Color(250, 200, 200);
    private boolean[] available;
    private int mapping[];
    public boolean tileImages = false;
    public String externalImage = null;
    private ColorFactory factory;

    public void setColorFactory(ColorFactory factory) {
        this.factory = factory;
    }

    /**
     * Creates a new instance of BackgroundFactory
     */
    public BackgroundFactory() {
    }

    public BackgroundFactory(boolean[] available) {
        this.available = available;
        int counter = 0;
        mapping = new int[available.length];
        for (int i = 0; i < available.length; i++) {
            if (available[i]) {
                mapping[counter] = i;
                counter++;
            }
        }
    }

    public void setDensCords(int[] x, int[] y) {
        this.DensX = x;
        this.DensY = y;
    }

    public void setDensColors(Color col1, Color col2, Color col3, Color col4, Color col5) {
        if (dens == null) {
            dens = new DensityFactory();
            dens.setColorFactory(factory);
        }

        dens.setDensColors(col1, col2, col3, col4, col5);
    }

    public Color[] getDensColors() {
        //if(dens==null) dens = new DensityFactory();

        Color[] bg = new Color[5];
        bg[0] = dens.color1;
        bg[1] = dens.color2;
        bg[2] = dens.color3;
        bg[3] = dens.color4;
        bg[4] = dens.color5;

        return bg;

    }

    public void setDensArea(int densarea) {
        dens.dotheatarea = densarea;
    }

    public void setNumColors(int colors) {
        dens.numcolors = colors;
    }

    public void setDensTolerance(int percent) {
        dens.dotTolerance = percent;
    }

    public void setHide(boolean[] hide) {
        this.hide = hide;
    }

    public boolean[] getTolerated() {

        if (method == 0 && dens != null) {
            return dens.getTolerated();
        } else {
            return null;
        }
    }

    public void paintOneColor(Graphics g, Rectangle r) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Single);
        g2d.fill(r);

    }

    //Generate a gradient background
    public void paintGradient(Graphics g, Rectangle r) {
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gp = null;
        if (GradientType == TOP_BOTTOM) {
            gp = new GradientPaint(r.x, r.y, gradient1, r.x, r.height, gradient2);
        } else if (GradientType == UPPERLEFT_LOWERRIGHT) {
            gp = new GradientPaint(r.x, r.y, gradient1, r.width, r.height, gradient2);
        }
        g2d.setPaint(gp);
        g2d.fill(r);

    }

    //Generate a One picture background
    public void paintPicture(Graphics g, Rectangle r) {
        Graphics2D g2d = (Graphics2D) g;
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON );
        //GradientPaint gp = new GradientPaint(UR, new Color(155,155,255), LR, new Color(55,55,255)); 

        if (externalImage == null || externalImage == "") {
            paintOneColor(g, r);
            return;
        }

        ImageIcon iic = null;
        try {
            iic = new ImageIcon(externalImage);
        } catch (Exception e) {
        }

        if (iic == null) {
            paintOneColor(g, r);
            return;
        } //if(BGIMAGE==0) iic = new ImageIcon( getClass().getResource( "im/gf.jpg" ) );
        //else if(BGIMAGE==1) iic = new ImageIcon( getClass().getResource( "im/gf2.jpg" ) );
        //BufferedImage bim = new BufferedImage(im.getWidth(this),im.getHeight(this),BufferedImage.TYPE_INT_RGB);//(BufferedImage) iic.getImage(); 
        //Graphics gbim = bim.getGraphics();
        else {


            Image im = iic.getImage();
            if (im.getWidth(this) < 1 || im.getHeight(this) < 1) {
                paintOneColor(g, r);
                return;
            }

            BufferedImage bim = new BufferedImage(im.getWidth(this), im.getHeight(this), BufferedImage.TYPE_INT_RGB);//(BufferedImage) iic.getImage(); 
            Graphics gbim = bim.getGraphics();
            gbim.drawImage(im, 0, 0, this);

            if (!tileImages) {


                //gbim.drawImage(im,0,0,this);

                TexturePaint tp = new TexturePaint(bim, r);
                g2d.setPaint(tp);
                g2d.fill(r);
                //g2d.fill((Shape)Area);
            } else {

                Rectangle tile = new Rectangle(bim.getWidth(this), bim.getHeight(this));
                TexturePaint tp = new TexturePaint(bim, tile);
                g2d.setPaint(tp);
                g2d.fill(r);




            }



        }

    }

    //Generate a tiled picture background
    public void paintTiled(Graphics g, Rectangle r, int TILENUMBER) {
        Graphics2D g2d = (Graphics2D) g;
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON );
        //GradientPaint gp = new GradientPaint(UR, new Color(155,155,255), LR, new Color(55,55,255)); 

        ImageIcon iic = null;
        if (TILENUMBER == 0) {
            iic = new ImageIcon(getClass().getResource("im/tile1.jpg"));
        } else if (TILENUMBER == 1) {
            iic = new ImageIcon(getClass().getResource("im/tile2.jpg"));
        } else if (TILENUMBER == 2) {
            iic = new ImageIcon(getClass().getResource("im/tile3.gif"));
        } else if (TILENUMBER == 3) {
            iic = new ImageIcon(getClass().getResource("im/tile4.jpg"));
        } else if (TILENUMBER == 4) {
            iic = new ImageIcon(getClass().getResource("im/tile5.gif"));
        } else if (TILENUMBER == 5) {
            iic = new ImageIcon(getClass().getResource("im/tile6.jpg"));
        }

        Image im = iic.getImage();
        BufferedImage bim = new BufferedImage(im.getWidth(this), im.getHeight(this), BufferedImage.TYPE_INT_RGB);//(BufferedImage) iic.getImage(); 
        Graphics gbim = bim.getGraphics();
        gbim.setColor(Color.blue);
        gbim.fillRect(0, 0, r.width, r.height);
        //System.out.print("\nersef");
        gbim.drawImage(im, 0, 0, this);
        Rectangle tile = new Rectangle(im.getWidth(this), im.getHeight(this));
        TexturePaint tp = new TexturePaint(bim, tile);
        g2d.setPaint(tp);
        g2d.fill(r);
        //g2d.fill((Shape)Area);
    }

    public void paintDensity(Graphics g, Rectangle r, int[] x, int[] y, boolean[] hide) {
        if (dens == null) {
            dens = new DensityFactory();
        }
        dens.setColorFactory(factory);
        Image back = dens.createHeatMap(r, x, y, hide);
        g.drawImage(back, r.x, r.y, this);
    }

    public void paintBackground(Graphics g, Rectangle r, int met) {

        if (available != null) {
            method = mapping[met];
        } else {
            this.method = met;
        }

        switch (method) {
            case (0):
                if (DensX != null && DensY != null) {
                    paintDensity(g, r, DensX, DensY, hide);
                }
                break;
            case (1):
            default:
                paintOneColor(g, r);
                break;
            case (2):
                paintGradient(g, r);
                break;
            case (3):
                paintPicture(g, r);
                break;
            //case(4): paintPicture(g,r,1); break;
            case (4):
                paintTiled(g, r, 0);
                break;
            case (5):
                paintTiled(g, r, 1);
                break;
            case (6):
                paintTiled(g, r, 2);
                break;
            case (7):
                paintTiled(g, r, 3);
                break;
            case (8):
                paintTiled(g, r, 4);
                break;
            case (9):
                paintTiled(g, r, 5);
                break;


        }
    }

    public void fillCombo(JComboBox combo) {
        combo.removeAllItems();

        if (available == null || available[0]) {
            combo.addItem("Density Map");
        }
        if (available == null || available[1]) {
            combo.addItem("One Color");
        }
        if (available == null || available[2]) {
            combo.addItem("Gradient");
        }
        //if(available==null || available[3])combo.addItem("Picture 1");
        //if(available==null || available[4])combo.addItem("Picture 2");
        if (available == null || available[3]) {
            combo.addItem("External Picture");
        }
        if (available == null || available[4]) {
            combo.addItem("Tile1");
        }
        if (available == null || available[5]) {
            combo.addItem("Tile2");
        }
        if (available == null || available[6]) {
            combo.addItem("Tile3");
        }
        if (available == null || available[7]) {
            combo.addItem("Tile4");
        }
        if (available == null || available[8]) {
            combo.addItem("Tile5");
        }
        if (available == null || available[9]) {
            combo.addItem("Tile6");
        }

    }

    public void fillGradCombo(JComboBox combo) {
        combo.removeAllItems();
        combo.addItem("Top-Bottom");
        combo.addItem("Diagonal");
    }
}
