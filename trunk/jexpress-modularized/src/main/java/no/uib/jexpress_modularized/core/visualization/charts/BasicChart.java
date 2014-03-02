/*

 * chart.java

 *

 * Created on 30. januar 2002, 11:33

 */
package no.uib.jexpress_modularized.core.visualization.charts;

import javax.swing.*;

import java.awt.*;

import java.io.Serializable;

import java.util.*;

/**
 *
 *
 *
 * @author bjarte
 *
 */
public class BasicChart extends JPanel implements Serializable{

    public Axis xaxis, 
                   yaxis;
    //double[][] values={{0.0003,0.0005},{0.00042,0.00035},{-0.000275,0.000334},{0.00034,0.00023},{0.00042,0.000126},{0.00079,0.00023},{0.00023,0.00057},{0.00026,0.00024}};
    // public double[] Rx = new double[]{4,3.6,12.1,10.0}; //Raw X
    // public double[] Ry = new double[]{4,3.6,-12.1,10.0}; //Raw Y
    //public double[] Rx = new double[]{0.4,0.6,0.1,0.0}; //Raw X
    //public double[] Ry = new double[]{0.122,0.6,-0.01,0.44}; //Raw Y
    // double[] Rx = new double[]{0.21, -0.04, -0.15, -0.04, 0.21, -0.14, -0.03, -0.07, -0.36, -0.14, -0.42, -0.34, -0.23, -0.17}; //Raw X
    // double[] Ry = new double[]{0.16, -0.34, -0.32, -0.34, -0.12, -0.34, -0.27, -0.15, -0.15, -0.51, -0.3, -0.25, -0.12, -0.4 }; //Raw Y
    public double[] Rx = new double[0];//new double[]{1, 5.35E9}; //Raw X
    public double[] Ry = new double[0];//new double[]{1, 5.35E9}; //Raw Y
    int[] Nx = new int[]{4, 3, 12, 10}; //Normalized X
    int[] Ny = new int[]{4, 36, -12, 10}; //Normalized Y
    String[] legend = new String[]{"test1", "testing2", "testing 3"};
    public int bottom = 45;
    public int left = 60;
    public int top = 10;
    public int right = 20;
    //public int topTitleHeight;
    public ChartLabel topText = new ChartLabel("");
    int LegendWidth;
    public boolean PaintLegend = false;
    public Hashtable props; //where the values for the chart can be stored.
    public int dotsize = 9;

    public void transferValues(int method, boolean X, double[][] source, int index) {



        if (method == 0) {

            Rx = new double[source.length];

            Ry = new double[source.length];



            if (X) {

                for (int i = 0; i < source.length; i++) {

                    Rx[i] = source[i][index];

                }

            } else {

                for (int i = 0; i < source.length; i++) {

                    Ry[i] = source[i][index];

                }

            }

        }

    }

    public void setXaxisLabel(String label) {

        xaxis.setTitleText(label);

    }

    public void setYaxisLabel(String label) {

        yaxis.setTitleText(label);

    }

    public BasicChart(double[] Rxn, double[] Ryn) {

        if (Rxn != null && Ryn != null) {
            this.Rx = Rxn;
            this.Ry = Ryn;
        }

        setPreferredSize(new Dimension(300, 350));

        topText = new ChartLabel("");

        xaxis = new Axis(Rx, 0, this);

        yaxis = new Axis(Ry, 1, this);

        yaxis.correctForCloseValues = false;



        xaxis.gridcolor = new Color(220, 220, 220);

        yaxis.gridcolor = new Color(220, 220, 220);



        yaxis.dropFirstGridLine = true;



        setXaxisLabel("X-Axis");

        setYaxisLabel("Y-Axis");

        xaxis.setTitleFont(new Font("TIMES NEW ROMAN", 1, 13));

        yaxis.setTitleFont(new Font("TIMES NEW ROMAN", 1, 13));

        topText.setFont(new Font("TIMES NEW ROMAN", 1, 15));

        this.setBackground(new Color(210, 204, 204));

    }

    public BasicChart() {

        setPreferredSize(new Dimension(300, 350));



        xaxis = new Axis(Rx, 0, this);

        yaxis = new Axis(Ry, 1, this);

        yaxis.correctForCloseValues = false;



        xaxis.gridcolor = new Color(220, 220, 220);

        yaxis.gridcolor = new Color(220, 220, 220);



        yaxis.dropFirstGridLine = true;



        setXaxisLabel("X-Axis");

        setYaxisLabel("Y-Axis");

        xaxis.setTitleFont(new Font("TIMES NEW ROMAN", 1, 13));

        yaxis.setTitleFont(new Font("TIMES NEW ROMAN", 1, 13));

        topText.setFont(new Font("TIMES NEW ROMAN", 1, 15));

        this.setBackground(new Color(210, 204, 204));

        /*
         *
         * this.addComponentListener(new ComponentAdapter(){
         *
         *
         *
         * public void componentResized(ComponentEvent e){
         *
         * repaint();
         *
         * }
         *
         *
         *
         * });
         *
         */



    }

    public int Height() {
        return getHeight();
    }

    public int Width() {
        return getWidth();
    }

    @Override
    public void paintComponent(Graphics g) {

        int TitleHeight = topText.getHeight(g);

        int HTitlePos = Width() / 2 - (topText.getWidth(g) / 2);





        g.setColor(getBackground());

        g.fillRect(0, 0, Width(), Height());



        g.setColor(Color.black);

        topText.draw(g, HTitlePos, top);





        if (PaintLegend) {
            LegendWidth = paintLegend(g);
        }



        xaxis.positionAxis(left, Width() - right - LegendWidth, Height() - bottom, Height() - bottom);

        yaxis.positionAxis(left, left, top + TitleHeight, Height() - bottom);



        xaxis.data_window.setSize(10, Height() - bottom - top - TitleHeight);

        yaxis.data_window.setSize(Width() - left - right - LegendWidth, 10);



        // if(xaxis!=null) xaxis.drawAxis(g);   

        // if(yaxis!=null) yaxis.drawAxis(g);   





        // int[] x = getXValues();

        // int[] y = getYValues();



        // g.setColor(new Color(255,255,255));

        // g.fillRect(left+1,top+1,Width()-left-right-LegendWidth-1,Height()-top-bottom-topText.getHeight(g)-1);



        g.setColor(Color.black);



        if (xaxis != null) {
            xaxis.drawAxis(g);
        }

        if (yaxis != null) {
            yaxis.drawAxis(g);
        }



        Nx = getXValues();

        Ny = getYValues();







        g.drawLine(left + 1, top + TitleHeight, Width() - right - LegendWidth, top + TitleHeight);

        g.drawLine(Width() - right - LegendWidth, top + TitleHeight, Width() - right - LegendWidth, Height() - bottom);





        g.setColor(Color.red);

        for (int i = 0; i < Nx.length; i++) {

            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(Color.red);



            g.fillOval(Nx[i] - (dotsize / 2), Ny[i] - (dotsize / 2), dotsize, dotsize);

            g.setColor(Color.black);

            g.drawOval(Nx[i] - (dotsize / 2), Ny[i] - (dotsize / 2), dotsize, dotsize);

        }











    }

    public int paintLegend(Graphics g) {



        Color bf = g.getColor();

        ChartLabel tf = new ChartLabel("", new Font("TIMES NEW ROMAN", 0, 12));

        tf.setColor(Color.black);

        int maxwidth = -1;

        int height = 0;





        for (int i = 0; i < legend.length; i++) {

            tf.setText(legend[i]);

            if (tf.getWidth(g) > maxwidth) {
                maxwidth = tf.getWidth(g);
            }

            height += tf.getHeight(g);

        }

        maxwidth += 8;



        int top = (Height() / 2) - (height / 2);



        for (int i = 0; i < legend.length; i++) {

            tf.setText(legend[i]);

            tf.draw(g, Width() - maxwidth + 3, top + 3 + (i * tf.getHeight(g)));

        }

        // g.setColor(Color.black);

        // g.drawRect(Width()-maxwidth,top-tf.getHeight(g),maxwidth-2,(legend.length*tf.getHeight(g)));



        g.setColor(bf);

        return maxwidth;



    }

    public int[] getXValues() {

        if (xaxis == null || Rx == null) {
            return null;
        }



        int[] ret = new int[Rx.length];

        for (int i = 0; i < Rx.length; i++) {

            ret[i] = xaxis.getInteger(Rx[i]);

        }

        return ret;

    }

    public int[] getYValues() {

        if (yaxis == null || Ry == null) {
            return null;
        }



        int[] ret = new int[Ry.length];

        for (int i = 0; i < Ry.length; i++) {

            ret[i] = yaxis.getInteger(Ry[i]);

        }

        return ret;

    }

    public static void main(String[] args) {


        BasicChart c = new BasicChart();

        JFrame f = new JFrame();

        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);



        f.getContentPane().add("Center", c);

        f.pack();

        f.setVisible(true);



    }
}
