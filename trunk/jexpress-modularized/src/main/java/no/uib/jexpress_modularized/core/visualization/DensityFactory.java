/*
 * DensityFactory.java
 *
 * Created on 31. januar 2002, 11:11
 */
package no.uib.jexpress_modularized.core.visualization;

import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.*;
import java.awt.image.*;
import java.awt.*;
import java.io.Serializable;

/**
 *
 * @author bjarte
 */
public class DensityFactory implements Serializable {

    private densdraw dens;
    public Color color1 = Color.white;
    /**
     * The second color in the density map
     */
    public Color color2 = new Color(200, 200, 250);
    /**
     * The third color in the density map
     */
    public Color color3 = Color.blue;
    /**
     * The fourth color in the density map
     */
    public Color color4 = Color.red;
    /**
     * The fifh color in the density map
     */
    public Color color5 = Color.yellow;
    public int numcolors = 300;
    private int[] heattable;
    public int dotTolerance = 101;//Draw dots within this heat area
    public int dotheatarea = 100;//the area around a dot that is heated
    //private boolean[] hide;
    private boolean[] tolerated;
    private Rectangle valueArea;
    private ColorFactory colors;

    /**
     * Creates a new instance of DensityFactory
     */
    public DensityFactory() {
    }

    public void setColorFactory(ColorFactory colors) {
        this.colors = colors;
    }

    public Image createHeatMap(Rectangle valueAreaIn, int[] x, int[] y, boolean[] hide) {
        int width = valueAreaIn.width;
        int height = valueAreaIn.height;
        int left = valueAreaIn.x;
        int top = valueAreaIn.y;

        // this.valueArea=valueArea;
        valueArea = new Rectangle(left - 10, top - 10, width + 20, height + 20);


        int maxheat = 0;
        double tmp = 0.0;
        tolerated = new boolean[x.length];


        double[][] nullv = new double[1][x.length];

        for (int i = 0; i < nullv[0].length; i++) {
            nullv[0][i] = 0.0;
        }
        double nullx = 0.0;
        double nully = 0.0;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = img.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        //g2d.getTransform().setToTranslation(valueAreaIn.x, valueAreaIn.y);


        g.setColor(Color.black);

        int pixx = 0;
        int pixy = 0;

        int[][] heat = new int[height][width];
        //  int[][] heat=new int[width][height];

        if (dotheatarea < 1) {
            dotheatarea = 1; //prevent nullpointer inferno...
        }
        boolean err = false;

        if (dens == null || dens.getmaxcirc() != dotheatarea) {
            dens = new densdraw(dotheatarea);
        }

        for (int i = 0; i < x.length; i++) {
            dens.drawCirc(heat, x[i] - left, y[i] - top);
        }

        for (int i = 0; i < heat.length; i++) {
            for (int j = 0; j < heat[0].length; j++) {
                if (heat[i][j] > maxheat) {
                    maxheat = heat[i][j];
                }
            }
        }

        if (colors != null) {
            Color[] cols = colors.getAllColors(numcolors);
            heattable = new int[numcolors];

            for (int i = 0; i < cols.length; i++) {
                heattable[i] = cols[i].getRGB();
            }

        } else {
            colorInterpolator temp = new colorInterpolator(color1, color2, color3, color4, color5);
            heattable = temp.getInterpolatedColors(numcolors);
        }


        int ht[] = new int[heat[0].length * heat.length];


        if (maxheat == 0) {
            maxheat = 1;
        }

        try {

            //   for(int i=0;i<heat.length;i++){
            //        heat[i][10]=10;
            //   }

            //Normalize the heat map    
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    tmp = ((double) heat[i][j] / (double) maxheat);
                    heat[i][j] = (int) (tmp * (double) (heattable.length - 1));
                    ht[(i * (width)) + j] = heattable[heat[i][j]];
                }
            }

        } catch (Exception e) {
        }


        try {


            for (int i = 0; i < x.length; i++) {
                if (valueArea.contains(x[i], y[i])) {
                    if (y[i] - top < heat.length && x[i] - left < heat[0].length && y[i] - top > 0 && x[i] - left > 0) {
                        if (heat[y[i] - top][x[i] - left] <= (((double) (heattable.length - 1) * this.dotTolerance) / 100) || (hide != null && !hide[i])) {
                            tolerated[i] = true;

                        }
                    } //Tolerate the points a few pixels outside the valueArea..
                    else if (hide != null && !hide[i]) {
                        tolerated[i] = true;
                    }

                } else {
                    // System.out.print("\n heatpixel: "+heat[y[i]-top][x[i]-left]+" tolerance: "+(((double)(heattable.length-1) * this.dotTolerance )/100 ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }






        //if(paintdensities){
        img.setRGB(0, 0, width, height, ht, 0, width);
        //}

        g.setClip(null);
        g.setColor(Color.red);

        g2d.setStroke(new BasicStroke(4.5f));
        //     g2d.draw(valueArea); 
        //g.drawLine(valueArea.x,valueArea.y,valueArea.width,valueArea.height);


        //     g.drawLine(0,0,heat[0].length,heat.length);



        return img;
    }

    public void setDensColors(Color col1, Color col2, Color col3, Color col4, Color col5) {
        color1 = col1;
        color2 = col2;
        color3 = col3;
        color4 = col4;
        color5 = col5;
    }

    public boolean[] getTolerated() {
        return tolerated;
    }

//The class the creates a integer array stamp of a heap that creates the heat areas..
    class densdraw {

        double[][] scales = null;
        int rad = 0;
        int maxcirc;

        public int getmaxcirc() {
            return maxcirc;
        }

        public densdraw(int maxcirc) {
            this.maxcirc = maxcirc;
            scales = new double[maxcirc][maxcirc];
            rad = maxcirc / 2;
            int center = maxcirc;
            double dist = 0;
            double max = rad;

            double x = 0;
            double y = 0;


            double maxgauss = rad * Math.exp(-1.0 * (max * max) / (2.0 * max * max));

            double theta = 1.0;
            for (int i = 0; i < scales.length; i++) {
                for (int j = 0; j < scales.length; j++) {

                    x = rad - (double) i;
                    y = rad - (double) j;

                    dist = Math.sqrt((x * x) + (y * y));
                    dist = Math.max(0, max - (dist));

                    scales[i][j] = rad - rad * Math.exp(-1.0 * (dist * dist) / (2.0 * max * max));
                }
            }
        }

        public void drawCirc(int[][] matrix, int y, int x) {

            int offsetx = 0;//x-rad;
            int offsety = 0;//-rad;

            if (x - rad < 0) {
                offsetx = -(x - rad);
            }
            if (y - rad < 0) {
                offsety = -(y - rad);
            }

            int startx = Math.max(0, x - rad);
            int starty = Math.max(0, y - rad);
            int endx = Math.min(matrix.length, x + rad);
            int endy = Math.min(matrix[0].length, y + rad);

            for (int i = startx; i < endx; i++) {
                for (int j = starty; j < endy; j++) {
                    matrix[i][j] += scales[offsetx + (i - startx)][offsety + (j - starty)];

                }
            }
        }
    }
}
