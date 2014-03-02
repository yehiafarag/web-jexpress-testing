
/*--------------------------------------------------------------------------
 *
 * Copyright (c) 2001 MolMine AS.  All rights reserved.
 *
 * All paper, computer, digital, graphical, or other representations of the source code remain
 * the property of MolMine AS. All patents, ideas, inventions or novelties contained within the
 * source code are the exclusive intellectual property of MolMine AS. Surce code is provided 
 * for reference and support purposes only. Copies of the source code in any form, whether this
 * be digital, graphical or any other media, may not be distributed, discussed, or revealed to 
 * any person, computer or organisation not directly involved in support of a related product 
 * provided by the licensee or organisation not authorzed by MolMine AS to be directly involved 
 * in source code level support of J-Express.
 
 * The source code may not be modified except where specifically authorized by MolMine AS. No 
 * part of the source code may be used  within any product other than J-Express.
 *
 * You undertake that:
 *  The source code will not be distributed except where specifical authorized by MolMine AS.
 *  That you will ensure that all copies and representations of the source code can be identified.
 *
 * DISCLAIMER:
 * THIS SOFTWARE IS PROVIDED BY MOLMINE AS "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE  ARE DISCLAIMED.  IN NO EVENT SHALL MOLMINE OR ITS CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *---------------------------------------------------------------------------
 */
package no.uib.jexpress_modularized.core.visualization;

import java.awt.image.PixelGrabber;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.Hashtable;
import javax.swing.JInternalFrame;
import java.awt.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.dataset.Group;

public class Tools extends java.awt.Component implements Serializable{

    int longestimage;
    boolean debug = false;

    public static void main(String[] args) {
        Tools t = new Tools();


    }

    public Tools() {
    }

    public int intval(String s) {
        Integer i = new Integer(s);
        return i.intValue();
    }

    public static double log(int base, double number) {
        return Math.log(number) / Math.log(base);
    }

    public static double log10(double x) throws ArithmeticException {
        if (x == 0.0) {
            throw new ArithmeticException("range exception");
        }
        //return Math.log(x)/2.30258509299404568401;
        return Math.log(x) / Math.log(10);
    }

    public int intval(double d) {
        //int ret=(int)Math.ceil(d);


        Long rounded = new Long(Math.round(d));
        return rounded.intValue();
        //		return ret;
    }

    public double round(double d) {
        Long rounded = new Long(Math.round(d));
        return rounded.doubleValue();
    }

    public double divideint(int a, int b) {
        double val1 = (double) a;
        double val2 = (double) b;
        return (val1 / val2);
    }

    public int idivideint(int a, int b) {
        return intval(divideint(a, b));
    }

    public double trunkdist(double a, double b) {
        double temp = 0;
        if (a < b) {
            temp = a;
            a = b;
            b = temp;
        }
        return abs(a - b);
    }

    public double mid(double a, double b) {
        double temp = 0;
        temp = a + b;
        temp = temp / 2;
        return temp;
    }

    public int imid(double a, double b) {
        double temp = 0;
        temp = a + b;
        temp = temp / 2;
        return intval(temp);
    }

    public int abs(int a) {
        return Math.abs(a);
    }

    public double abs(double a) {
        return Math.abs(a);
    }

    public int norm(int value, int absspan, int span) {
        double dvalue = (double) value;
        double dspan = (double) span;
        double dabsspan = (double) absspan;
        return intval((dvalue / dspan) * dabsspan);
    }

    public double norm(double value, double absspan, double span) {

        return ((value / span) * absspan);
    }

    public double norm(double value, double absspan, double span, double rightend) {

        return ((value / rightend) * (absspan));
    }

    public int calcnextstep(int down, int gendist, int absspan, int span) {
        int ret = 0;

        double ddown = (double) down;
        double dgendist = (double) gendist;
        double dabsspan = (double) absspan;
        double dspan = (double) span;
        double divided = round(dgendist) / dspan;
        ret = intval(ddown + (divided * dabsspan));
        return ret;
    }

    public int calcnextdist(double trunkvalue, int divisor, int absspan, int span) {
        int ret = 0;
        double dtrunkvalue = (double) trunkvalue;
        double ddivisor = (double) divisor;
        double dabsspan = (double) absspan;
        double dspan = (double) span;
        double divided = round(dtrunkvalue / ddivisor);

        ret = intval((divided / dspan) * dabsspan);
        return ret;
    }

    public void print(String s) {
        System.out.print(s);
    }

    public void println(String s) {
        System.out.print(s + "\n");
    }

    public void print(int s) {
        System.out.print(String.valueOf(s));
    }

    public void println(int s) {
        System.out.print(String.valueOf(s) + "\n");
    }

    public void print(double s) {
        System.out.print(String.valueOf(s));
    }

    public void println(double s) {
        System.out.print(String.valueOf(s) + "\n");
    }

    /**
     * Filter the image so that it can fit in a 8 bit gif image..
     *
     * @return A 8 bit version of the input Image.
     */
    //Does not work and is not used...
    public Image filterImage(Image in) {

        BufferedImage im = (BufferedImage) in;
        BufferedImage img = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_BGR);



        //          BufferedImageFilter filter = new BufferedImageFilter();
        //  BufferedImageOp.filter(im,img);
        java.awt.image.ColorConvertOp conv = new java.awt.image.ColorConvertOp(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT));
        conv.filter(im, img);


        return (Image) img;
    }

    //Create a colored column..
    public Image createRotatedImage(String s, java.awt.Font font, int i, int topnamesheight, int squareW, boolean antialias) {

        java.awt.FontMetrics fm;
        Color backgroundColor = Color.white;
        Color foregroundColor = Color.black;
        double temp = (double) i;
        if ((int) Math.round(temp / 2) * 2 == i) {
            backgroundColor = new Color(210, 210, 210);
        } else {
            backgroundColor = new Color(230, 230, 230);
        }

        //int w = fm.stringWidth(s);// + 2;
        int w = topnamesheight;
        int h = squareW;
        //int h = fm.getMaxAscent() + fm.getMaxDescent();// + 2;

        //Image img = createImage(w, h);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics g = img.getGraphics();

        g.setColor(backgroundColor);
        g.fillRect(0, 0, w, h);
        g.setColor(foregroundColor);
        g.setFont(font);
        //g.setFont(new Font("ARIAL",0,h));

        if (antialias) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        fm = g.getFontMetrics();


        g.drawString(s, 2, fm.getMaxAscent() - 2);
        g.dispose();

        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException ie) {
        }
        int[] newPixels = new int[w * h];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                newPixels[x * h + y] = pixels[y * w + (w - x - 1)];
            }
        }
        MemoryImageSource imageSource = new MemoryImageSource(h, w, java.awt.image.ColorModel.getRGBdefault(), newPixels, 0, h);
        Image myImage = createImage(imageSource);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(myImage, 0);
        try {
            mt.waitForAll();
        } catch (InterruptedException ie) {
        }
        return myImage;
    }

    //Create a colored column..
    public Image createRotatedImage(String s, java.awt.Font font, int i, int imageheight, int topnamesheight, int fontsize, boolean antialias) {

        java.awt.FontMetrics fm;
        Color backgroundColor = Color.white;
        Color foregroundColor = Color.black;
        double temp = (double) i;
        if ((int) Math.round(temp / 2) * 2 == i) {
            backgroundColor = new Color(210, 210, 210);
        } else {
            backgroundColor = new Color(230, 230, 230);
        }

        //int w = fm.stringWidth(s);// + 2;
        int w = topnamesheight;
        int h = imageheight;
        //int h = fm.getMaxAscent() + fm.getMaxDescent();// + 2;

        //Image img = createImage(w, h);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics g = img.getGraphics();

        g.setColor(backgroundColor);
        g.fillRect(0, 0, w, h);
        g.setColor(foregroundColor);
        g.setFont(font);
        //g.setFont(new Font("ARIAL",0,fontsize));

        if (antialias) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        fm = g.getFontMetrics();
        int spaceLeft = h - fm.getMaxAscent();

        //g.drawString(s, 0, fm.getMaxAscent()-2);
        g.drawString(s, 2, fm.getMaxAscent() - 1 + (spaceLeft / 2));//+(imageheight/2)-(fontsize/2));
        g.dispose();

        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException ie) {
        }
        int[] newPixels = new int[w * h];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                newPixels[x * h + y] = pixels[y * w + (w - x - 1)];
            }
        }
        MemoryImageSource imageSource = new MemoryImageSource(h, w, java.awt.image.ColorModel.getRGBdefault(), newPixels, 0, h);
        Image myImage = createImage(imageSource);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(myImage, 0);
        try {
            mt.waitForAll();
        } catch (InterruptedException ie) {
        }
        return myImage;
    }

    //Create a colored column..
    public Image createRotatedImage(String[] s, boolean[] visibleClumns, java.awt.Font font, int i, int imageheight, int topnamesheight, int fontsize, boolean antialias, Color fg) {

        java.awt.FontMetrics fm;
        Color backgroundColor = Color.white;
        Color foregroundColor = Color.black;


        if (i != -1) {
            double temp = (double) i;
            if ((int) Math.round(temp / 2) * 2 == i) {
                backgroundColor = new Color(210, 210, 210);
            } else {
                backgroundColor = new Color(230, 230, 230);
            }
        }



        //int w = fm.stringWidth(s);// + 2;
        int w = topnamesheight;
        int h = imageheight;
        //int h = fm.getMaxAscent() + fm.getMaxDescent();// + 2;

        //Image img = createImage(w, h);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics g = img.getGraphics();

        g.setColor(backgroundColor);
        g.fillRect(0, 0, w, h);
        g.setColor(foregroundColor);
        g.setFont(font);
        //g.setFont(new Font("ARIAL",0,fontsize));



        int visibleItems = 0;

        if (visibleClumns != null) {
            for (int j = 1; j < visibleClumns.length - 1; j++) {
                if (visibleClumns[j]) {
                    visibleItems++;
                } else {
                    visibleItems = s.length;
                }
            }
        }



        if (antialias) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        fm = g.getFontMetrics();
        int space = 1;



        if (visibleItems > 1) {
            space = Math.min(visibleItems, h / ((visibleItems - 1) * fm.getMaxAscent()));
        }

        // System.out.print("\n"+space);

        int spaceLeft = h - (fm.getMaxAscent() * (space));
        g.setColor(fg);
        int counter = 0;
        for (int j = 0; j < s.length; j++) {
            //g.drawString(s[j], 0, fm.getMaxAscent()+(imageheight/2)-(fontsize/2)-2);
            if ((visibleClumns == null || visibleClumns[j + 1]) && (space >= counter)) {
                g.drawString(s[j], 2, fm.getMaxAscent() - 1 + (counter * fm.getMaxAscent()) + (spaceLeft / 2));
                counter++;
            }
        }

        g.dispose();

        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException ie) {
        }
        int[] newPixels = new int[w * h];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                newPixels[x * h + y] = pixels[y * w + (w - x - 1)];
            }
        }
        MemoryImageSource imageSource = new MemoryImageSource(h, w, java.awt.image.ColorModel.getRGBdefault(), newPixels, 0, h);
        Image myImage = createImage(imageSource);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(myImage, 0);
        try {
            mt.waitForAll();
        } catch (InterruptedException ie) {
        }
        return myImage;
    }

    //Create a colored column..
    public Image createRotatedImage(String[] s, boolean[] visibleClumns, java.awt.Font font, int i, int imageheight, int topnamesheight, int fontsize, boolean antialias, Color fg, boolean up) {

        java.awt.FontMetrics fm;
        Color backgroundColor = Color.black;
        Color foregroundColor = Color.white;


        if (i != -1) {
            double temp = (double) i;
            if ((int) Math.round(temp / 2) * 2 == i) {
                backgroundColor = new Color(210, 210, 210);
            } else {
                backgroundColor = new Color(230, 230, 230);
            }
        }



        //int w = fm.stringWidth(s);// + 2;
        int w = topnamesheight;
        int h = imageheight;
        //int h = fm.getMaxAscent() + fm.getMaxDescent();// + 2;

        //Image img = createImage(w, h);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics g = img.getGraphics();

        g.setColor(backgroundColor);
        g.fillRect(0, 0, w, h);
        g.setColor(foregroundColor);
        g.setFont(font);
        //g.setFont(new Font("ARIAL",0,fontsize));



        int visibleItems = 0;

        if (visibleClumns != null) {
            for (int j = 1; j < visibleClumns.length - 1; j++) {
                if (visibleClumns[j]) {
                    visibleItems++;
                } else {
                    visibleItems = s.length;
                }
            }
        }



        if (antialias) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        fm = g.getFontMetrics();
        int space = 1;

        //visibleItems=1;

        if (visibleItems > 1) {
            space = Math.min(visibleItems, h / ((visibleItems) * fm.getMaxAscent()));
        }

        // System.out.print("\n"+space);

        int spaceLeft = 0;
        //if(visibleItems>1) 

        spaceLeft = h - ((fm.getMaxAscent() - 1) * (visibleItems + 1));

        //spaceLeft =0;
        g.setColor(fg);
        int counter = 0;
        for (int j = 0; j < s.length; j++) {
            //g.drawString(s[j], 0, fm.getMaxAscent()+(imageheight/2)-(fontsize/2)-2);
            if ((visibleClumns == null || visibleClumns[j + 1]) && (space >= counter)) {
                g.drawString(s[j], 2, -1 + ((counter + 1) * fm.getMaxAscent()) + (spaceLeft / 2));
                counter++;
            }
        }

        g.dispose();

        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException ie) {
        }

        int[] newPixels = null;

        if (up) {
            newPixels = new int[w * h];
            for (int y = 0; y < h; ++y) {
                for (int x = 0; x < w; ++x) {
                    newPixels[x * h + y] = pixels[y * w + (w - x - 1)];
                }
            }
        } else {
            newPixels = new int[w * h];
            for (int y = 0; y < h; ++y) {
                for (int x = 0; x < w; ++x) {
                    newPixels[x * h + y] = pixels[pixels.length - (y * w + (w - x - 1)) - 1];
                }
            }
        }
        MemoryImageSource imageSource = new MemoryImageSource(h, w, java.awt.image.ColorModel.getRGBdefault(), newPixels, 0, h);
        Image myImage = createImage(imageSource);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(myImage, 0);
        try {
            mt.waitForAll();
        } catch (InterruptedException ie) {
        }
        return myImage;
    }

    public Image createRotatedImage(String s, java.awt.FontMetrics fm, java.awt.Font font, Color background, boolean antialias) {

        //Color backgroundColor=Color.white;
        Color foregroundColor = Color.black;
        //double temp=(double)i;
        //if ((int)Math.round(temp/2)*2==i)
        //backgroundColor=new Color(230,230,250);
        //else
        //background=new Color(250,250,250);

        int w = fm.stringWidth(s) + 2;

        if (w > longestimage) {
            longestimage = w;
        }


        //				int w=topnamesheight;
        //				int h=squareW;
        int h = fm.getMaxAscent() + fm.getMaxDescent();// + 2;
        //Image img = createImage(w, h);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);


        java.awt.Graphics g = img.getGraphics();

        //g.setColor(background);
        //g.setColor(Color.red);
        //g.fillRect(0, 0, w, h);
        g.setColor(foregroundColor);
        g.setFont(font);

        if (antialias) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        g.drawString(s, 1, fm.getMaxAscent() - 2);
        g.dispose();

        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException ie) {
        }
        int[] newPixels = new int[w * h];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                newPixels[x * h + y] = pixels[y * w + (w - x - 1)];
            }
        }
        MemoryImageSource imageSource = new MemoryImageSource(h, w, java.awt.image.ColorModel.getRGBdefault(), newPixels, 0, h);
        Image myImage = createImage(imageSource);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(myImage, 0);
        try {
            mt.waitForAll();
        } catch (InterruptedException ie) {
        }
        return myImage;
    }

    public int[][] flipmatrix(int[][] data) {

        int ret[][] = new int[data[0].length][data.length];

        for (int i = 0; i < data.length; i++) {

            for (int j = 0; j < data[0].length; j++) {

                ret[j][i] = data[i][j];

            }

        }

        return ret;
    }

    public static double[][] doubleToFloat(double[][] source) {
        if (source == null) {
            return null;
        }
        if (source.length == 0 || source[0].length == 0) {
            return new double[][]{};
        }

        double[][] ret = new double[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[0].length; j++) {
                ret[i][j] = (double) source[i][j];
            }
        }
        return ret;
    }

    public double[][] flipmatrix(double[][] data) {
        double ret[][] = new double[data[0].length][data.length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                ret[j][i] = data[i][j];
            }
        }
        return ret;
    }

    public String[][] flipmatrix(String[][] data) {
        String ret[][] = new String[data[0].length][data.length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                ret[j][i] = data[i][j];
            }
        }
        return ret;
    }

    public static void printMatrix(double[][] data) {
        System.out.print("\n");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                System.out.print(data[i][j] + "\t");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public double[][] itod(int[][] mat, double ammount) {
        double[][] ret = new double[mat.length][mat[0].length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {

                ret[i][j] = (((double) mat[i][j]) / ammount);
            }
        }
        return ret;
    }

    public int[][] dtoi(double[][] mat, double ammount) {
        int[][] ret = new int[mat.length][mat[0].length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {

                ret[i][j] = (int) ((mat[i][j]) / ammount);
            }
        }
        return ret;
    }

    //A stringreplace function
    public static String replaceStr(String main, String source, String dest) {
        //private String replaceStr(String sSource, , String sToRep) {
        String ret = main;

        int iSrtPos = main.indexOf(source);

        while (iSrtPos != -1) {

            String sBefore = ret.substring(0, iSrtPos);
            String sAfter = ret.substring(iSrtPos + source.length());
            ret = sBefore + dest + sAfter;
            iSrtPos = ret.indexOf(source);
        }

        return ret;
    }

    //Counts the max line occurence of char chr in the vector
    public int maxlength(java.util.Vector strings, byte chr) {
        int max = 0;
        int incount = 0;
        String tocount = "";
        byte[] b;
        //byte comp=new Byte(chr).byteValue();

        for (int i = 0; i < strings.size(); i++) {

            incount = 0;
            tocount = (String) strings.elementAt(i);
            b = tocount.getBytes();
            for (int j = 0; j < b.length; j++) {
                if (b[j] == chr) {
                    incount++;
                }
            }
            if (incount > max) {
                max = incount;
            }
        }
        return max;
    }

    public static boolean proptobool(String s) {

        if (s != null && s.equalsIgnoreCase("YES")) {
            return true;
        } else {
            return false;
        }
    }

    public static String booltoprop(boolean b) {
        if (b) {
            return "YES";
        } else {
            return "NO";
        }
    }

    public Color proptoColor(String s) {
        Color c = new Color(proptoint(s));
        return c;
    }

    public String inttoprop(int i) {
        return String.valueOf(i);
    }

    public String dbltoprop(double i) {
        return String.valueOf(i);
    }

    public String longtoprop(long i) {
        return String.valueOf(i);
    }

    public int proptoint(String s) {
        int ret = 0;
        try {
            ret = new Integer(s).intValue();
        } catch (Exception e) {
        }
        return ret;
    }

    public double proptodbl(String s) {
        double ret = 0;
        try {
            ret = new Double(s).doubleValue();
        } catch (Exception e) {
        }
        return ret;
    }

    public long proptolong(String s) {
        long ret = 0;
        try {
            ret = new Long(s).longValue();
        } catch (Exception e) {
        }
        return ret;
    }

    public static Hashtable readProps() {

        Hashtable props = new Hashtable();

        try {

            FileInputStream istream = new FileInputStream("jexpress.cfg");
            ObjectInputStream OI = new ObjectInputStream(istream);
            props = (Hashtable) OI.readObject();
            istream.close();
        } catch (Exception ioe) {
        }

        return props;
    }

    /*
     * public void writeProp(String propname, double value){ writeProp(propname,
     * dbltoprop(value));} public void writeProp(String propname, long value){
     * writeProp(propname, longtoprop(value));} public void writeProp(String
     * propname, int value){ writeProp(propname, inttoprop(value));} public void
     * writeProp(String propname, boolean value){ writeProp(propname,
     * booltoprop(value));} public void writeProp(String propname, Color value){
     * writeProp(propname, value.getRGB());}
     */
    /*
     * public void writeProp(String propname, String value){
     *
     * // Hashtable props=new Hashtable();
     *
     * try {
     *
     * FileInputStream istream = new FileInputStream("jexpress.cfg");
     * props.load(istream);
     *
     * FileOutputStream ostream = new FileOutputStream("jexpress.cfg");
     * props.put(propname,value);
     *
     * props.store(ostream,"External Links"); ostream.close();
     *
     * props=null; }
     *
     * catch (IOException ioe) {} }
     */
    //returns a gauss curve of the input
    public double[] gauss(double[] in, double top) {
        double[] ret = new double[in.length];
        double theta = 1.0;
        for (int i = 0; i < in.length; i++) {

            theta = in[in.length - 1] - in[i];
            if (theta != 0.0) {
                ret[i] = Math.exp(-1.0 * (in[i] * in[i]) / (2.0 * theta * theta));
            } else {
                ret[i] = 0;
            }
        }
        return ret;
    }

    public double[] gauss(double[] in) {
        double[] ret = new double[in.length];
        double theta = in.length * 1.2;
        for (int i = 0; i < in.length; i++) {

            theta = in[in.length - 1] - in[i];
            if (theta != 0.0) {
                ret[i] = Math.exp(-1 * (in[i] * in[i]) / (2 * theta * theta));
            } else {
                ret[i] = 0;
            }
        }
        return ret;
    }

    //returns a gauss curve of the input
    public int[] gauss(int[] in, double top) {
        int[] ret = new int[in.length];

        double theta = 1.0;
        for (int i = 0; i < in.length; i++) {
            theta = top - in[i];
            if (theta != 0.0) {
                ret[i] = (int) (10.0 * Math.exp(-1.0 * ((double) in[i] * (double) in[i]) / (2.0 * theta * theta)));
            } else {
                ret[i] = 0;
            }
        }
        return ret;
    }

    public void writedialogStatus(Object o, String formName, Hashtable p) {



        if (o != null) {

            java.lang.reflect.Field[] f = o.getClass().getDeclaredFields();

            for (int i = 0; i < f.length; i++) {

                if (f[i].getType().getName().equals("javax.swing.JSlider")) {
                    try {
                        javax.swing.JSlider js = (javax.swing.JSlider) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(formName + "-" + f[i].getName(), inttoprop(js.getValue()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }

                }
                if (f[i].getType().getName().equals("javax.swing.JComboBox")) {
                    try {
                        javax.swing.JComboBox js = (javax.swing.JComboBox) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(formName + "-" + f[i].getName(), inttoprop(js.getSelectedIndex()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }

                } else if (f[i].getType().getName().equals("javax.swing.JCheckBox")) {
                    try {
                        javax.swing.JCheckBox jc = (javax.swing.JCheckBox) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(formName + "-" + f[i].getName(), booltoprop(jc.isSelected()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (f[i].getType().getName().equals("javax.swing.JRadioButton")) {
                    try {
                        javax.swing.JRadioButton jc = (javax.swing.JRadioButton) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(formName + "-" + f[i].getName(), booltoprop(jc.isSelected()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                } else if (f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.WholeNumberField")) {
                    try {
                        WholeNumberField wn = (WholeNumberField) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(formName + "-" + f[i].getName(), inttoprop(wn.getValue()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                } else if (f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.DoubleField")) {
                    try {
                        DoubleField wn = (DoubleField) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(formName + "-" + f[i].getName(), dbltoprop(wn.getValue()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                } else if (f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.ColorButton")) {
                    try {
                        ColorButton wn = (ColorButton) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(formName + "-" + f[i].getName(), inttoprop(wn.getColor().getRGB()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                } else if (f[i].getType().getName().equals("javax.swing.JTextField")) {
                    try {
                        javax.swing.JTextField jt = (javax.swing.JTextField) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(formName + "-" + f[i].getName(), jt.getText());
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                }


            }


            //last.. Check if the source component is a JInternalframe and if it is save its size.
            try {
                JInternalFrame ji = (JInternalFrame) o;
                p.put(formName + "-" + "X", inttoprop(ji.getLocation().x));
                p.put(formName + "-" + "Y", inttoprop(ji.getLocation().y));
                p.put(formName + "-" + "W", inttoprop(ji.getSize().width));
                p.put(formName + "-" + "H", inttoprop(ji.getSize().height));
            } catch (Exception e) {
                if (debug) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void readialogStatus(Object o, String formName, Hashtable p) {

        java.lang.reflect.Field[] f = o.getClass().getDeclaredFields();

        for (int i = 0; i < f.length; i++) {
            if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("javax.swing.JSlider")) {
                try {

                    javax.swing.JSlider js = (javax.swing.JSlider) f[i].get(o);
                    int I = proptoint((String) p.get(formName + "-" + f[i].getName()));
                    js.setValue(I);
                    //System.out.print("\nset slider: "+f[i].getName()+" to value "+I);

                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            }

            if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("javax.swing.JComboBox")) {
                try {

                    javax.swing.JComboBox js = (javax.swing.JComboBox) f[i].get(o);
                    int I = proptoint((String) p.get(formName + "-" + f[i].getName()));
                    js.setSelectedIndex(I);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            } else if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("javax.swing.JCheckBox")) {
                try {
                    javax.swing.JCheckBox jc = (javax.swing.JCheckBox) f[i].get(o);
                    boolean b = proptobool((String) p.get(formName + "-" + f[i].getName()));
                    jc.setSelected(b);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            } else if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("javax.swing.JRadioButton")) {
                try {
                    javax.swing.JRadioButton jc = (javax.swing.JRadioButton) f[i].get(o);
                    boolean b = proptobool((String) p.get(formName + "-" + f[i].getName()));
                    jc.setSelected(b);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            } else if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.WholeNumberField")) {
                try {
                    WholeNumberField wn = (WholeNumberField) f[i].get(o);
                    int val = proptoint((String) p.get(formName + "-" + f[i].getName()));
                    wn.setValue(val);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            } else if (f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.DoubleField")) {
                try {
                    DoubleField wn = (DoubleField) f[i].get(o);
                    double val = proptodbl((String) p.get(formName + "-" + f[i].getName()));
                    wn.setValue(val);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }
            } else if (f[i].getName().endsWith("SV")) {
                if (f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.ColorButton")) {
                    try {
                        ColorButton wn = (ColorButton) f[i].get(o);
                        String str = (String) p.get(formName + "-" + f[i].getName());

                        if (str == null) {
                            wn.setColor(new Color(202, 202, 202));
                        } else {
                            int val = proptoint(str);
                            wn.setColor(new Color(val));
                        }
                    } catch (Exception ee) {
                        if (debug) {
                            ee.printStackTrace();
                        }
                    }
                }
            } else if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("javax.swing.JTextField")) {
                try {

                    javax.swing.JTextField jt = (javax.swing.JTextField) f[i].get(o);
                    String s = (String) p.get(formName + "-" + f[i].getName());
                    jt.setText(s);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            }



        }

        //Last.. check in the root component is a JInternalFrame and if it is read and set its size.
        try {
            JInternalFrame ji = (JInternalFrame) o;

            int W = proptoint((String) p.get(formName + "-" + "W"));
            int H = proptoint((String) p.get(formName + "-" + "H"));
            int X = proptoint((String) p.get(formName + "-" + "X"));
            int Y = proptoint((String) p.get(formName + "-" + "Y"));


            if (W == 0) {
                W = 200;
            }
            if (H == 0) {
                H = 200;
            }

            ji.setSize(new java.awt.Dimension(W, H));
            //ji.setLocation(X,Y);
        } catch (Exception e) {
            if (debug) {
                e.printStackTrace();
            }
        }
    }

    public static void ObjectFromHash(Object o, Hashtable p) {
        boolean debug = false;
        java.lang.reflect.Field[] f = o.getClass().getDeclaredFields();

        for (int i = 0; i < f.length; i++) {
            if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("javax.swing.JSlider")) {
                try {

                    javax.swing.JSlider js = (javax.swing.JSlider) f[i].get(o);
                    int I = ((Integer) p.get(f[i].getName())).intValue();
                    js.setValue(I);

                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            }

            if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("javax.swing.JComboBox")) {
                try {

                    javax.swing.JComboBox js = (javax.swing.JComboBox) f[i].get(o);
                    //int I = proptoint((String)p.get("-"+f[i].getName()));
                    int I = ((Integer) p.get(f[i].getName())).intValue();
                    js.setSelectedIndex(I);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            } else if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("javax.swing.JCheckBox")) {
                try {
                    javax.swing.JCheckBox jc = (javax.swing.JCheckBox) f[i].get(o);
                    boolean b = ((Boolean) p.get(f[i].getName())).booleanValue();
                    jc.setSelected(b);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            } else if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("javax.swing.JRadioButton")) {
                try {
                    javax.swing.JRadioButton jc = (javax.swing.JRadioButton) f[i].get(o);
                    //boolean b = proptobool((String)p.get("-"+f[i].getName()));
                    boolean b = ((Boolean) p.get(f[i].getName())).booleanValue();
                    jc.setSelected(b);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            } else if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.WholeNumberField")) {
                try {
                    WholeNumberField wn = (WholeNumberField) f[i].get(o);
                    //int val = proptoint((String)p.get("-"+f[i].getName()));
                    int I = ((Integer) p.get(f[i].getName())).intValue();
                    wn.setValue(I);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            } else if (f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.DoubleField")) {
                try {
                    DoubleField wn = (DoubleField) f[i].get(o);
                    double val = ((Double) p.get(f[i].getName())).doubleValue();
                    wn.setValue(val);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }
            } else if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.ColorButton")) {
                try {
                    ColorButton wn = (ColorButton) f[i].get(o);
                    //int val = proptoint((String)p.get("-"+f[i].getName()));
                    Color c = ((Color) p.get(f[i].getName()));
                    wn.setColor(c);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            } else if (f[i].getName().endsWith("SV") && f[i].getType().getName().equals("javax.swing.JTextField")) {
                try {

                    javax.swing.JTextField jt = (javax.swing.JTextField) f[i].get(o);
                    String s = (String) p.get(f[i].getName());
                    jt.setText(s);
                } catch (Exception e) {
                    if (debug) {
                        e.printStackTrace();
                    }
                }

            }

        }


    }

    public static Hashtable objectToHash(Object o) {
        boolean debug = false;
        Hashtable p = new Hashtable();
        if (o != null) {


            java.lang.reflect.Field[] f = o.getClass().getDeclaredFields();

            for (int i = 0; i < f.length; i++) {

                if (f[i].getType().getName().equals("javax.swing.JSlider")) {
                    try {
                        javax.swing.JSlider js = (javax.swing.JSlider) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(f[i].getName(), new Integer(js.getValue()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }

                }
                if (f[i].getType().getName().equals("javax.swing.JComboBox")) {
                    try {
                        javax.swing.JComboBox js = (javax.swing.JComboBox) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(f[i].getName(), new Integer(js.getSelectedIndex()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }

                } else if (f[i].getType().getName().equals("javax.swing.JCheckBox")) {
                    try {
                        javax.swing.JCheckBox jc = (javax.swing.JCheckBox) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(f[i].getName(), new Boolean(jc.isSelected()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (f[i].getType().getName().equals("javax.swing.JRadioButton")) {
                    try {
                        javax.swing.JRadioButton jc = (javax.swing.JRadioButton) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(f[i].getName(), new Boolean(jc.isSelected()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                } else if (f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.WholeNumberField")) {
                    try {
                        WholeNumberField wn = (WholeNumberField) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(f[i].getName(), new Integer(wn.getValue()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                } else if (f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.DoubleField")) {
                    try {
                        DoubleField wn = (DoubleField) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(f[i].getName(), new Double(wn.getValue()));
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                } else if (f[i].getType().getName().equals("no.uib.jexpress_modularized.visualization.basic.ColorButton")) {
                    try {
                        ColorButton wn = (ColorButton) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(f[i].getName(), wn.getColor());
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                } else if (f[i].getType().getName().equals("javax.swing.JTextField")) {
                    try {
                        javax.swing.JTextField jt = (javax.swing.JTextField) f[i].get(o);
                        if (f[i].getName().endsWith("SV")) {
                            p.put(f[i].getName(), jt.getText());
                        }
                    } catch (Exception e) {
                        if (debug) {
                            e.printStackTrace();
                        }
                    }
                }


            }


        }

        return p;
    }

    public static void writeProperties(Hashtable p) {
        boolean debug = false;
        try {
            java.io.FileOutputStream ostream = new java.io.FileOutputStream("jexpress.cfg");
            java.io.ObjectOutputStream obj = new java.io.ObjectOutputStream(ostream);
            //p.store(ostream,"Settings");
            obj.writeObject(p);
            ostream.close();
        } catch (java.io.IOException ioe) {
            if (debug) {
                ioe.printStackTrace();
            }
        }
    }

    public void arraySwap(Object[] source, int from, int to) {
        Object tmp = source[from];
        source[from] = source[to];
        source[to] = tmp;
    }

    public void arraySwap(boolean[] source, int from, int to) {
        boolean tmp = source[from];
        source[from] = source[to];
        source[to] = tmp;
    }

    //Swap a column in the Object array.
    public void arraySwap(Object[][] source, int from, int to) {
        for (int i = 0; i < source.length; i++) {
            Object tmp = source[i][from];
            source[i][from] = source[i][to];
            source[i][to] = tmp;
        }
    }

    public void sort(java.util.Vector v) {
        // precondition the Vector v exists
        // post condition elements are sorted in ascending order
        int k;  //index of next position
        int n = v.size();
        k = 0;
        while (k != n - 1) {
            int j = getSmallest(v, k);
            swap(v, j, k);
            k++;
        }
    }

    private void swap(java.util.Vector v, int j, int k) {
        // precondition:  0<=j<used and 0<=k<used
        // swaps elements at position j and k in Vector
        Object temp = v.elementAt(j);
        v.setElementAt(v.elementAt(k), j);
        v.setElementAt(temp, k);
    }

    private int getSmallest(java.util.Vector v, int k) {
        // returns the minimum value in the Vector item
        // or returns -1 if the Vector is empty

        if (v.size() == 0) {
            return -1;
        }
        int smallIndex = k;
        for (int i = k; i < v.size(); i++) {

            //This statement differs for every data type
            if (((Integer) v.elementAt(i)).intValue() < ((Integer) v.elementAt(smallIndex)).intValue()) {
                smallIndex = i;
            }
        }
        return smallIndex;
    }

    public static Color[] greateColorScale(double[] curve, Color col1, Color col2) {
        int length = curve.length;

        Color[] ret = new Color[length];

        //double[] gs = new double[ret.length];
        //for(int i=0;i<gs.length;i++) gs[i]=(double)i;
        //gs=gauss(gs);
        //for(int i=0;i<gs.length;i++) System.out.print("\n"+gs[i]);



        //paintvalue=Math.abs(60+(int)(((double)colorval[i]/(highest))*180));
        int r1 = col1.getRed();
        int g1 = col1.getGreen();
        int b1 = col1.getBlue();
        int tra1 = col1.getAlpha();

        int r2 = col2.getRed();
        int g2 = col2.getGreen();
        int b2 = col2.getBlue();
        int tra2 = col2.getAlpha();

        int tr = 0;
        int tg = 0;
        int tb = 0;
        int tra = 0;

        for (int i = 0; i < length; i++) {

            tr = r1 + (int) ((double) (r2 - r1) * (curve[i]));// (int)(((double)(((double)r2-(double)r1))/(double)length)*(i*gs[i]));
            tg = g1 + (int) ((double) (g2 - g1) * (curve[i]));//(int)(((double)(((double)g2-(double)g1))/(double)length)*(i*gs[i]));
            tb = b1 + (int) ((double) (b2 - b1) * (curve[i]));//(int)(((double)(((double)b2-(double)b1))/(double)length)*(i*gs[i]));
            tra = tra1 + (int) ((double) (tra2 - tra1) * (curve[i]));//(int)(((double)(((double)b2-(double)b1))/(double)length)*(i*gs[i]));

            ret[i] = new Color(tr, tg, tb, tra);
        }



        return ret;
    }

    public void writeFrameSetings(Hashtable p, JInternalFrame frame) {
        p.put(frame.getTitle() + "location", frame.getLocation());
        p.put(frame.getTitle() + "size", frame.getSize());
        p.put(frame.getTitle() + "visible", new Boolean(frame.isVisible()));
    }

    public void readFrameSetings(Hashtable p, JInternalFrame frame) {
        if (p.containsKey(frame.getTitle() + "location")) {
            frame.setLocation((java.awt.Point) p.get(frame.getTitle() + "location"));
        }
        if (p.containsKey(frame.getTitle() + "size")) {
            frame.setSize((java.awt.Dimension) p.get(frame.getTitle() + "size"));
        }
        if (p.containsKey(frame.getTitle() + "visible")) {
            frame.setVisible(((Boolean) p.get(frame.getTitle() + "visible")).booleanValue());
        }

    }

    public static ArrayList split(String arg, char separator) {
        ArrayList liste = new ArrayList();
        int index = 0;
        StringBuffer wort = new StringBuffer();
        while (index < arg.length()) {
            if (arg.charAt(index) == separator) {
                liste.add(wort.toString());
                wort = new StringBuffer();
            } else {
                wort.append(arg.charAt(index));
            }
            index++;
        }
        //if (!wort.toString().equals(""))
        liste.add(wort.toString());
        return liste;
    }

    public static double[][] transpose(double[][] trans) {
        double[][] ret = new double[trans[0].length][trans.length];
        for (int i = 0; i < trans.length; i++) {
            for (int j = 0; j < trans[0].length; j++) {
                ret[j][i] = trans[i][j];
            }
        }
        return ret;
    }

    public static String[][] transpose(String[][] trans) {
        String[][] ret = new String[trans[0].length][trans.length];
        for (int i = 0; i < trans.length; i++) {
            for (int j = 0; j < trans[0].length; j++) {
                ret[j][i] = trans[i][j];
            }
        }
        return ret;
    }

    public static double tStatisticOneSample(double[] obs, double my0) {
        double m = 0, ss = 0, n = (double) obs.length;
        for (int i = 0; i < obs.length; i++) {
            m += obs[i];
            ss += obs[i] * obs[i];
        }
        m /= n;
        double var = (ss - m * m * n) / (n - 1);
        return (m - my0) / Math.sqrt(var / n);
    }

    public static double tStatistic(double[] obs1, double[] obs2) {
        //two-sample t-statistic
        double var1, var2, ss1 = 0, ss2 = 0;
        double n1, n2, am, bm;
        n1 = obs1.length;
        n2 = obs2.length;
        am = 0;
        bm = 0;
        for (int i = 0; i < obs1.length; i++) {
            am += obs1[i];
        }
        am /= n1;
        for (int i = 0; i < obs2.length; i++) {
            bm += obs2[i];
        }
        bm /= n2;
        for (int i = 0; i < obs1.length; i++) {
            ss1 += obs1[i] * obs1[i];
        }
        ss1 -= am * am * n1;
        for (int i = 0; i < obs2.length; i++) {
            ss2 += obs2[i] * obs2[i];
        }
        ss2 -= bm * bm * n2;
        //System.out.println("am="+am+" bm="+bm+" var1="+var1+" var2="+var2);
        if (am == bm || (ss1 + ss2) == 0 || n1 == 0 || n2 == 0) {
            return 0;
        } else {
            return (am - bm) / (Math.sqrt((ss1 + ss2) / (n1 + n2 - 2))
                    * Math.sqrt((1 / n1) + (1 / n2)));
        }
    }

    public static double[] tScores(Dataset data, double mu) {

        double[] tvals = new double[data.getDataLength()];
        double[][] dat = data.getData();
        /*
         * double[] means = new double[data.getDataWidth()]; double[][] dat =
         * data.getData(); double tmp=0.0;
         *
         * for(int i=0;i<data.getDataLength();i++){ for(int
         * j=0;j<data.getDataWidth();j++){ means[j]+=dat[i][j]; } }
         *
         * for(int j=0;j<data.getDataWidth();j++){
         * means[j]=means[j]/data.getDataWidth(); }
         *
         * for(int i=0;i<data.getDataLength();i++){
         *
         * tmp=0.0; for(int j=0;j<data.getDataWidth();j++){
         * tmp+=Math.pow(dat[i][j]-means[j],2); }
         *
         * tvals[i]=Math.sqrt(tmp);
         *
         * }
         */
        for (int i = 0; i < data.getDataLength(); i++) {
            tvals[i] = tStatisticOneSample(dat[i], mu);
        }
        return tvals;
        // return expresscomponents.JDoubleSorter.quickSort(tvals);
    }

    public static double[] tScores(Dataset data, double[] mu) {

        double[] tvals = new double[data.getDataLength()];
        double[][] dat = data.getData();
        /*
         * double[] means = new double[data.getDataWidth()]; double[][] dat =
         * data.getData(); double tmp=0.0;
         *
         * for(int i=0;i<data.getDataLength();i++){ for(int
         * j=0;j<data.getDataWidth();j++){ means[j]+=dat[i][j]; } }
         *
         * for(int j=0;j<data.getDataWidth();j++){
         * means[j]=means[j]/data.getDataWidth(); }
         *
         * for(int i=0;i<data.getDataLength();i++){
         *
         * tmp=0.0; for(int j=0;j<data.getDataWidth();j++){
         * tmp+=Math.pow(dat[i][j]-means[j],2); }
         *
         * tvals[i]=Math.sqrt(tmp);
         *
         * }
         */
        for (int i = 0; i < data.getDataLength(); i++) {
            tvals[i] = tStatistic(dat[i], mu);
        }
        return tvals;
        // return expresscomponents.JDoubleSorter.quickSort(tvals);
    }

    public static Color[] getGroupColors(Dataset data, boolean columns) {

        Group group = null;
        boolean activeSet;
        boolean[] indexes;
        Color classColor;
        Color[] ret = null;

        // @TODO: reimplement me!!

//        if (data == null) {
//            return null;
//        }
//        if (!columns) {
//            ret = new Color[data.getDataLength()];
//        } else {
//            ret = new Color[data.getDataWidth()];
//        }
//
//        Vector groups = null;
//        if (columns) {
//            groups = data.getColumnGroups();
//        } else {
//            groups = data.getGroups();
//        }
//
//
//        for (int i = 0; i < groups.size(); i++) {
//            group = (Group) groups.elementAt(i);
//            if (!group.isActive()) {
//                continue;
//            }
//            indexes = group.getMembers();//(boolean[]) Class.elementAt(3);
//            for (int j = 0; j < ret.length; j++) {
//                if (ret[j] == null && indexes[j]) {
//                    ret[j] = group.getColor();
//                }
//            }
//        }
//
//        for (int j = 0; j < ret.length; j++) {
//            if (ret[j] == null) {
//                ret[j] = Color.gray;
//            }
//        }

        return ret;
    }

    public static boolean[] inActiveGroup(Dataset data) {

        //Vector Class=null;
        Group group = null;
        boolean activeSet;
        boolean[] indexes;
        Color classColor;
        boolean[] inActiveGroup = new boolean[data.getDataLength()];


        // @TODO: reimplement me!!

//        if (data.getGroups().size() != 0) {
//            for (int j = 0; j < data.getGroups().size(); j++) {
//                group = (Group) data.getGroups().elementAt(j);
//                //Class=(Vector)data.getGroups().elementAt(j);
//                activeSet = group.isActive();//(Boolean)Class.elementAt(0);
//                //classColor=(Color)Class.elementAt(2);
//                indexes = group.getMembers();//(boolean[]) Class.elementAt(3);
//
//                if (activeSet) {
//                    //col[i]=classColor;
//                    //painted[i]=true;
//                    addBoolArray(inActiveGroup, indexes);
//                }
//            }
//        }

        return inActiveGroup;
    }

    public static boolean[] inActiveColumnGroup(Dataset data) {
        Group group = null;
        //Vector Class=null;
        boolean activeSet;
        boolean[] indexes;
        Color classColor;
        boolean[] inActiveGroup = new boolean[data.getDataWidth()];

        // @TODO: reimplement me!!

//        if (data.getColumnGroups().size() != 0) {
//            for (int j = 0; j < data.getColumnGroups().size(); j++) {
//                group = (Group) data.getColumnGroups().elementAt(j);
//                //Class=(Vector)data.getColumnGroups().elementAt(j);
//                activeSet = group.isActive();//(Boolean)Class.elementAt(0);
//
//                //classColor=(Color)Class.elementAt(2);
//                indexes = group.getMembers();//(boolean[]) Class.elementAt(3);
//
//                if (activeSet) {
//                    //col[i]=classColor;
//                    //painted[i]=true;
//                    addBoolArray(inActiveGroup, indexes);
//                }
//            }
//        }

        return inActiveGroup;
    }

    public static void addBoolArray(boolean[] source, boolean[] newarray) {
        for (int i = 0; i < source.length; i++) {
            source[i] = source[i] | newarray[i];
        }
    }
}
