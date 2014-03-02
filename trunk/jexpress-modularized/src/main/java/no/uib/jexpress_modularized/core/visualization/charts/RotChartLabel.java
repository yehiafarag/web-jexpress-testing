package no.uib.jexpress_modularized.core.visualization.charts;

import java.awt.*;

import java.util.*;

import java.lang.*;

import java.awt.image.*;
import java.io.Serializable;

public class RotChartLabel extends ChartLabel implements Serializable{

    /**
     *
     * Rotation Angle of text in degrees
     *
     */
    protected int angle = 0;
    private double cos = 1.0;
    private double sin = 0.0;
    private Component component = null;
    boolean aalias = true;

    /**
     *
     * Instantiate the class
     *
     */
    public RotChartLabel() {
    }

    /**
     *
     * Instantiate the class.
     *
     * @param s String to parse.
     *
     */
    public RotChartLabel(String s) {

        super(s);

    }

    /**
     *
     * Instantiate the class.
     *
     * @param c the Component the text will be drawn into.
     *
     */
    public RotChartLabel(Component c) {

        setDrawingComponent(c);

    }

    /**
     *
     * Instantiate the class
     *
     * @param s String to parse.
     *
     * @param f Font to use.
     *
     */
    public RotChartLabel(String s, Font f) {

        super(s, f);

    }

    /**
     *
     * Instantiate the class
     *
     * @param s String to parse.
     *
     * @param f Font to use.
     *
     * @param c Color to use
     *
     * @param j Justification
     *
     */
    public RotChartLabel(String s, Font f, Color c, int j) {

        super(s, f, c, j);

    }

    /**
     *
     * Instantiate the class
     *
     * @param s String to parse.
     *
     * @param c Color to use
     *
     */
    public RotChartLabel(String s, Color c) {

        super(s, c);

    }

    /**
     *
     * Instantiate the class
     *
     * @param f Font to use.
     *
     * @param c Color to use
     *
     * @param j Justification
     *
     * @param a Rotation angle in degrees
     *
     */
    public RotChartLabel(Font f, Color c, int j, int a) {

        super(f, c, j);

        setRotation(a);

    }

    /**
     *
     * Instantiate the class
     *
     * @param f Font to use.
     *
     * @param c Color to use
     *
     * @param j Justification
     *
     */
    public RotChartLabel(Font f, Color c, int j) {

        super(f, c, j);

    }

    /**
     *
     * Copy the state of the parsed ChartLabel into the existing
     *
     * object.
     *
     * @param t The ChartLabel to get the state information from.
     *
     */
    public void copyState(RotChartLabel t) {

        if (t == null) {
            return;
        }



        font = t.getFont();

        color = t.getColor();

        justification = t.getJustification();

        setRotation(t.getRotation(), t.getComponent());



        if (font == null) {
            return;
        }

        fontname = font.getName();

        fontstyle = font.getStyle();

        fontsize = font.getSize();



        parse = true;

    }

    /**
     *
     * Set the text rotation angle. Only multiples of 90 degrees
     *
     * are accepted
     *
     * @param angle The angle to rotate the text
     *
     */
    public void setRotation(int angle) {



        this.angle = ((angle % 360) / 90) * 90;



        cos = Math.cos(angle * Math.PI / 180.0);

        sin = Math.sin(angle * Math.PI / 180.0);



    }

    /**
     *
     * Set the Component the text will be drawn into. This is required to rotate
     *
     * the text. if it is not set the text will not be rotated.
     *
     * @param c The drawing component
     *
     */
    public void setDrawingComponent(Component c) {

        component = c;



    }

    /**
     *
     * Set the Rotation and the Component that will be drawn into
     *
     * @param angle The angle to rotate the text
     *
     * @param c The drawing component
     *
     */
    public void setRotation(int angle, Component c) {

        setRotation(angle);

        setDrawingComponent(c);

    }

    /**
     *
     * @return the Rotation angle in degrees.
     *
     */
    public int getRotation() {

        return angle;

    }

    /**
     *
     * @return the Component that will receive the text.
     *
     */
    public Component getComponent() {

        return component;

    }

    /**
     *
     * The width of the text after it has been rotated.
     *
     * @param g Graphics context.
     *
     * @return the width of the parsed text after it has been rotated.
     *
     */
    public int getRWidth(Graphics g) {



        parseText(g);



        return (int) (Math.abs(cos * width + sin * height) + 0.5);

    }

    /**
     *
     * The height of the text after it has been rotated.
     *
     * @param g Graphics context.
     *
     * @return the height of the parsed text after it has been rotated.
     *
     */
    public int getRHeight(Graphics g) {



        parseText(g);



        return (int) (Math.abs(-sin * width + cos * height) + 0.5);



    }

    /**
     *
     * Return the left edge of the rotated text.      *
     * This is dependent on the justification of the text.
     *
     * @param g Graphics context.
     *
     * @return the Left edge of the rotated text
     *
     */
    public int getLeftEdge(Graphics g) {

        return getLeftEdge(g, justification);

    }

    /**
     *
     * Return the right edge of the rotated text.      *
     * This is dependent on the justification of the text.
     *
     * @param g Graphics context.
     *
     * @return the Right edge of the rotated text
     *
     */
    public int getRightEdge(Graphics g) {

        return getRightEdge(g, justification);

    }

    /**
     *
     * Return the top edge of the rotated text.      *
     * This is dependent on the justification of the text.
     *
     * @param g Graphics context.
     *
     * @return the Top edge of the rotated text
     *
     */
    public int getTopEdge(Graphics g) {

        return getTopEdge(g, justification);

    }

    /**
     *
     * Return the bottom edge of the rotated text.      *
     * This is dependent on the justification of the text.
     *
     * @param g Graphics context.
     *
     * @return the Bottom edge of the rotated text
     *
     */
    public int getBottomEdge(Graphics g) {

        return getBottomEdge(g, justification);

    }

    /**
     *
     * Return the left edge of the rotated text.      *
     * @param g Graphics context.
     *
     * @param j Text justification
     *
     * @return the Left edge of the rotated text
     *
     */
    public int getLeftEdge(Graphics g, int j) {



        parseText(g);



        switch (angle) {



            case 90:
            case -270:

                return -ascent;

            case 180:
            case -180:

                if (j == CENTER) {
                    return -width / 2;
                } else if (j == RIGHT) {
                    return 0;
                } else {
                    return -width;
                }

            case 270:
            case -90:

                return -descent - leading;

            default:

                if (j == CENTER) {
                    return -width / 2;
                } else if (j == RIGHT) {
                    return -width;
                } else {
                    return 0;
                }



        }





    }

    /**
     *
     * Return the right edge of the rotated text.      *
     * @param g Graphics context.
     *
     * @param j Text justification
     *
     * @return the Right edge of the rotated text
     *
     */
    public int getRightEdge(Graphics g, int j) {



        parseText(g);



        switch (angle) {



            case 90:
            case -270:

                return descent + leading;

            case 180:
            case -180:

                if (j == CENTER) {
                    return width / 2;
                } else if (j == RIGHT) {
                    return width;
                } else {
                    return 0;
                }

            case 270:
            case -90:

                return ascent;

            default:

                if (j == CENTER) {
                    return width / 2;
                } else if (j == RIGHT) {
                    return 0;
                } else {
                    return width;
                }



        }





    }

    /**
     *
     * Return the top edge of the rotated text.      *
     * @param g Graphics context.
     *
     * @param j Text justification
     *
     * @return the Top edge of the rotated text
     *
     */
    public int getTopEdge(Graphics g, int j) {



        parseText(g);



        switch (angle) {



            case 90:
            case -270:

                if (j == CENTER) {
                    return width / 2;
                } else if (j == RIGHT) {
                    return 0;
                } else {
                    return width;
                }

            case 180:
            case -180:

                return descent + leading;

            case 270:
            case -90:

                if (j == CENTER) {
                    return width / 2;
                } else if (j == RIGHT) {
                    return width;
                } else {
                    return 0;
                }

            default:

                return ascent;

        }

    }

    /**
     *
     * Return the bottom edge of the rotated text.      *
     * @param g Graphics context.
     *
     * @param j Text justification
     *
     * @return the Bottom edge of the rotated text
     *
     */
    public int getBottomEdge(Graphics g, int j) {



        parseText(g);



        switch (angle) {



            case 90:
            case -270:

                if (j == CENTER) {
                    return -width / 2;
                } else if (j == RIGHT) {
                    return -width;
                } else {
                    return 0;
                }

            case 180:
            case -180:

                return -ascent;

            case 270:
            case -90:

                if (j == CENTER) {
                    return -width / 2;
                } else if (j == RIGHT) {
                    return 0;
                } else {
                    return -width;
                }

            default:

                return -descent - leading;

        }

    }

    /**
     *
     * Parse the text then draw it.
     *
     * @param g Graphics context
     *
     * @param x pixel position of the text
     *
     * @param y pixel position of the text
     *
     */
    public void draw(Graphics g, int x, int y) {



        if (g == null) {
            return;
        }



        if (component == null) {
            angle = 0;
        }



        if (angle == 0) {
            super.draw(g, x, y);
        } else {
            draw(component, g, x, y);
        }

    }

    /**
     *
     * Parse the text then draw it.
     *
     * @param g Graphics context
     *
     * @param x pixel position of the text
     *
     * @param y pixel position of the text
     *
     * @param j justification of the text
     *
     */
    public void draw(Graphics g, int x, int y, int j) {



        justification = j;



        if (g == null) {
            return;
        }

        if (component == null) {
            angle = 0;
        }



        if (angle == 0) {
            super.draw(g, x, y);
        } else {
            draw(component, g, x, y);
        }

    }

    /**
     *
     * Parse the text, rotate it then draw it to the screen.
     *
     * @param g Graphics context
     *
     * @param x pixel position of the text
     *
     * @param y pixel position of the text
     *
     */
    public synchronized void draw(Component comp, Graphics g, int x, int y) {



        TextState ts;

        int xoffset = 0;

        int yoffset = 0;

        Image offsI = null;

        Graphics offsG = null;

        Image rotatedImage = null;

        int maxHeight = 0;





        if (text == null || comp == null) {
            return;
        }





        parseText(g);



        maxHeight = maxAscent + maxDescent;



        if (width < 1 || maxHeight < 1) {
            return;
        }





        /*
         *
         ** Calculate the offset of the rotated image so that it
         *
         ** will be positioned correctly. Remeber the image is calculated
         *
         ** on the Maximum Ascent and descent so that no character          *
         ** is truncated
         *
         */

        switch (angle) {



            case 90:
            case -270:

                xoffset = -maxAscent;

                if (justification == CENTER) {
                    yoffset = -width / 2;
                } else if (justification == RIGHT) {
                    yoffset = 0;
                } else {
                    yoffset = -width;
                }

                break;

            case 180:
            case -180:

                yoffset = -maxDescent;

                if (justification == CENTER) {
                    xoffset = -width / 2;
                } else if (justification == RIGHT) {
                    xoffset = 0;
                } else {
                    xoffset = -width;
                }

                break;

            case 270:
            case -90:

                xoffset = -maxDescent;

                if (justification == CENTER) {
                    yoffset = -width / 2;
                } else if (justification == RIGHT) {
                    yoffset = -width;
                } else {
                    yoffset = 0;
                }

                break;

            default:

                xoffset = 0;

                yoffset = 0;

                break;

        }

        /*
         *
         ** Create the offscreen image that the text will be written into
         *
         */

        //offsI = comp.createImage(width,maxHeight);



        //        offsI=new BufferedImage(width,maxHeight, BufferedImage.TYPE_INT_ARGB);





        //System.out.print("\nWIDTH: "+maxHeight);

        //        offsG = offsI.getGraphics();

        /*
         *
         ** Color the image with the background color
         *
         */

        if (background != null) {

            g.setColor(background);

        } else {

            g.setColor(comp.getBackground());

        }



        //       offsG.fillRect(0,0,width,maxHeight);

        /*
         *
         ** Set the image font and color
         *
         */

        //    g.setFont(g.getFont());

        //    g.setColor(g.getColor());





        Graphics2D g2D = (Graphics2D) g;





        //System.out.print(g2D.getRenderingHints().get(RenderingHints.KEY_TEXT_ANTIALIASING));





        if ((g2D.getRenderingHints().get(RenderingHints.KEY_TEXT_ANTIALIASING)).equals(RenderingHints.VALUE_TEXT_ANTIALIAS_ON)) {
            //g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 
            //  System.out.print("\nAntialias");
            // Graphics2D g2D2 = (Graphics2D) offsG;
            // g2D2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); 
        }



        if (font != null) {
            g.setFont(font);
        }

        //if(color != null) 

        g2D.setColor(comp.getForeground());



        /*
         *
         ** Write to the offscreen image
         *
         */

        //for(int i=0; i<list.size(); i++) {

        ts = ((TextState) (list.elementAt(0)));

        //    if(ts.f != null) g2D.setFont(ts.f);

        //    if(ts.s != null) 



        g2D.translate(x + xoffset + this.height - 2, y + yoffset + this.width);

        g2D.rotate(-Math.PI / 2);

        //g2D.translate(x+xoffset,y+yoffset);





        int w = g2D.getFontMetrics().stringWidth(ts.toString()) + 4;



        g2D.drawString(ts.toString(), 0, 0);



        g2D.scale(0.8, 0.8);



        if (list.size() > 1) {

            ts = ((TextState) (list.elementAt(1)));





            g2D.translate(w, 0);

            g2D.drawString(ts.toString(), 2, -4);

            g2D.translate(-w, 0);

            g2D.scale(1.0 / 0.8, 1.0 / 0.8);





            g2D.rotate(Math.PI / 2);

            g2D.translate(-(x + xoffset + this.height - 2), -(y + yoffset + this.width));



        }



        //g2D.rotate(Math.PI/3);



        // }



        if (background != null) {

            g.setColor(background);

        } else {

            g.setColor(comp.getBackground());

        }



        /*
         *
         ** Rotate the Offscreen image
         *
         */



        //      RotateTextFilter f = new RotateTextFilter(angle);

        //       ImageProducer producer = new FilteredImageSource(offsI.getSource(),f);



        //        rotatedImage = comp.createImage(producer);

        /*
         *
         ** Draw the rotated image to the Component. Do not notify any
         *
         ** image consumer especially the component, otherwise we will get a          *
         ** feedback loop starting up,
         *
         ** as this method is normally called from a paint method.
         *
         */

        //        g.drawImage(rotatedImage,x+xoffset,y+yoffset,null);





    }
}

/**
 *
 * This is an extension to the ImageFilter class that will rotate an image
 *
 * a multiple of 90 degrees. This filter is easily extended to allow arbitrary
 *
 * rotations
 *
 */
class RotateTextFilter extends ImageFilter {

    /*
     *
     ** The angle to rotate the image in degrees
     *
     */
    private int angle = 0;

    /*
     *
     ** The minimum and maximum points on the image after rotation.
     *
     ** More important if arbitrary rotation was allowed
     *
     */
    private int xmin = 0;
    private int xmax = 0;
    private int ymin = 0;
    private int ymax = 0;

    /*
     *
     ** The width and height of the New rotated image
     *
     */
    private int width;
    private int height;

    /*
     *
     ** the rotation cosines of the angle
     *
     */
    private double cos = 1.0;
    private double sin = 0.0;

    /*
     *
     ** The arrays that will store the new image. Only one is used depending
     *
     ** on the data type of the original image.
     *
     */
    private int ipixels[];
    private byte bpixels[];

    /*
     *
     ** the color model of the original image
     *
     */
    private ColorModel colorModel;

    /*
     *
     *****************
     *
     ** Constructors
     *
     ***************
     */
    /**
     *
     * Instantiate the class
     *
     * @param angle the angle to rotate the image. Only multiples of 90 degrees
     *
     * are allowed
     *
     */
    public RotateTextFilter(int angle) {



        this.angle = ((angle % 360) / 90) * 90;



        cos = Math.cos(angle * Math.PI / 180.0);

        sin = Math.sin(angle * Math.PI / 180.0);

    }

    /**
     *
     * Add to the properties table of the image that it has been rotated
     *
     * @param props The property table of the original image
     *
     */
    public void setProperties(Hashtable props) {

        props = (Hashtable) props.clone();

        props.put("rotAngle", new Integer(angle));

        super.setProperties(props);

    }

    /**
     *
     * Find the dimensions of the original image and pass onto
     *
     * the image consumer the dimensions of the new roated image
     *
     * @param w width of the original image
     *
     * @param h height of the original image
     *
     */
    public void setDimensions(int w, int h) {

        int x[] = {0, w - 1, w - 1, 0};

        int y[] = {0, 0, h - 1, h - 1};



        int xx;

        int yy;



        for (int i = 0; i < 4; i++) {

            xx = (int) Math.round(x[i] * cos + y[i] * sin);

            yy = (int) Math.round(-x[i] * sin + y[i] * cos);



            xmin = Math.min(xmin, xx);

            xmax = Math.max(xmax, xx);

            ymin = Math.min(ymin, yy);

            ymax = Math.max(ymax, yy);

        }



        width = xmax - xmin + 1;

        height = ymax - ymin + 1;



        consumer.setDimensions(width, height);

    }

    /**
     *
     * As the pixels of the original image are sent store them in memory
     *
     * as the rotated image.
     *
     *
     *
     * This method is called with a subset rectangle of the original image.
     *
     *
     *
     * @param x position of Left column in original image of this rectangle
     *
     * @param y poisition of Top row in original image of this rectangle
     *
     * @param w width of this rectangle
     *
     * @param h height of this rectangle
     *
     * @param model Colormodel associated with original image      *
     * @param pixels Array containing the image or part of it
     *
     * @param off The offset into the pixels array where the parsed      *
     * rectangle starts      *
     * @param scan The actual width of the image.
     *
     */
    public void setPixels(int x, int y, int w, int h,
            ColorModel model, byte pixels[], int off,
            int scan) {



        int i, j, k;



        int ir, jr;





        /*
         *
         ** If the byte array is null create it. Also remember the color
         *
         ** model so that we can pass it onto the image consumer
         *
         */



        if (bpixels == null) {

            colorModel = model;

            bpixels = new byte[width * height];

        }





        /*
         *
         ** place the rotated image into memory one pixel at a time
         *
         */



        j = y;

        for (int n = 0; n < h; n++, j++) {

            i = x;

            for (int m = 0; m < w; m++, i++) {



                ir = (int) Math.round(i * cos + j * sin) - xmin;

                jr = (int) Math.round(-i * sin + j * cos) - ymin;



                k = ir + jr * width;

                bpixels[k] = pixels[ (j - y) * scan + (i - x) + off];

            }

        }



    }

    /**
     *
     * As the pixels of the original image are sent store them in memory
     *
     * as the rotated image.
     *
     *
     *
     * This method is called with a subset rectangle of the original image.
     *
     *
     *
     * @param x position of Left column in original image of this rectangle
     *
     * @param y poisition of Top row in original image of this rectangle
     *
     * @param w width of this rectangle
     *
     * @param h height of this rectangle
     *
     * @param model Colormodel associated with original image      *
     * @param pixels Array containing the image or part of it
     *
     * @param off The offset into the pixels array where the parsed      *
     * rectangle starts      *
     * @param scan The actual width of the image.
     *
     */
    public void setPixels(int x, int y, int w, int h,
            ColorModel model, int pixels[], int off,
            int scan) {



        int i, j, k;



        int ir, jr;



        /*
         *
         ** If the integer array is null create it. Also remember the color
         *
         ** model so that we can pass it onto the image consumer
         *
         */

        if (ipixels == null) {

            colorModel = model;

            ipixels = new int[width * height];

        }



        /*
         *
         ** place the rotated image into memory one pixel at a time
         *
         */



        j = y;

        for (int n = 0; n < h; n++, j++) {

            i = x;

            for (int m = 0; m < w; m++, i++) {



                ir = (int) Math.round(i * cos + j * sin) - xmin;

                jr = (int) Math.round(-i * sin + j * cos) - ymin;



                k = ir + jr * width;

                ipixels[k] = pixels[ (j - y) * scan + (i - x) + off];

            }

        }



    }

    /**
     *
     * Called when the image is complete.
     *
     * When this is called by the image producer, we can then pass the rotated
     *
     * image onto the image consumer.
     *
     *
     *
     * @param status Status of the original image from the image producer.
     *
     */
    public void imageComplete(int status) {





        if (status == ImageConsumer.IMAGEABORTED
                || status == ImageConsumer.IMAGEERROR) {

            consumer.imageComplete(status);

            ipixels = null;

            bpixels = null;

            return;

        }

        /*
         *
         ** Send the rotated image to the image consumer. Not forgetting to
         * tell
         *
         ** it when the image is complete          *
         */

        if (bpixels != null) {



            for (int j = 0; j < height; j++) {
                consumer.setPixels(0, j, width, 1,
                        colorModel, bpixels, j * width, width);
            }



            consumer.imageComplete(status);

        } else if (ipixels != null) {



            for (int j = 0; j < height; j++) {
                consumer.setPixels(0, j, width, 1,
                        colorModel, ipixels, j * width, width);
            }

            consumer.imageComplete(status);

        } else {
            consumer.imageComplete(ImageConsumer.IMAGEABORTED);
        }



        ipixels = null;

        bpixels = null;



    }
}
