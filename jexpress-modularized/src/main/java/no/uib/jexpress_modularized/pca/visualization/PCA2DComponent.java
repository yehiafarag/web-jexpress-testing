/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.pca.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.border.Border;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.core.model.SelectionManager;
import no.uib.jexpress_modularized.core.model.ModularizedListener;
import no.uib.jexpress_modularized.core.visualization.JeToolTip2;
import no.uib.jexpress_modularized.core.visualization.Tools;
import no.uib.jexpress_modularized.core.visualization.charts.Axis;
import no.uib.jexpress_modularized.core.visualization.charts.DensScatterPlot;
import no.uib.jexpress_modularized.core.visualization.documentation.MetaInfoNode;
import no.uib.jexpress_modularized.pca.computation.PcaResults;
import no.uib.jexpress_modularized.pca.model.ArrayUtils;

/**
 *
 * @author Yehia Farag
 */
public class PCA2DComponent extends ModularizedListener implements Serializable {
    //For speeding lookup

    boolean antialias = true;
    private no.uib.jexpress_modularized.core.visualization.charts.DensScatterPlot plot;
    private no.uib.jexpress_modularized.pca.computation.PcaResults pcaResults;
    int mode3d = 1; //1=rotate, 2=zoom
    String title = null;
    int dotTolerance = 2;//Draw dots within this heat area
    int dotheatarea = 10;//the area around a dot that is heated
    int dotsize = 1;  //The size of each dot.
    boolean paintscale = false;   //Paint the scale?
    boolean paintstats = false;   //paint the stats?
    boolean paintdensities = true;
    public boolean zoom = false;
    public double[] zoomedRect;
    /**
     * The first color in the density map
     */
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
     * The fifth color in the density map
     */
    public Color color5 = Color.yellow;
    int numcolors = 299;
    private int pcax = 0;
    private int pcay = 1;
    private int pcaz = 2;
    boolean[] painted;    //If a point in the scatter plot is not selected (by the colorclasses) this index will
    //be false and it will not be sweeped.
    double[][][] neurons;   //the nodes in a SOM (if a som is assigned)
    int clickx = -1;  //If a node has been selected, these are the coordinates in the latice.
    int clicky = -1;  //and the node at clickx,clicky will be colored yellow
    boolean shadowUnselected = false;
    boolean isneurons = true;
    int zoomedpca = 0;
    public boolean quadratic = true;
    boolean showPCA = true;

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (shadowUnselected == false) {
            plot.setNotShaded(getSelectedIndexes());
            plot.forceFullRepaint();
        }
    }

    public boolean isShadowUnselected() {
        return shadowUnselected;
    }

    public void setShadowUnselected(boolean shadowUnselected) {
        this.shadowUnselected = shadowUnselected;
    }

    private void updateSelectionOnDataSet(boolean[] members) {
        //OBS
        int[] selectedIndices = ArrayUtils.toIntArray(members);
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        SelectionManager.getSelectionManager().setSelectedRows(data, selection);
    }

    public PCA2DComponent(PcaResults pcaResults, Dataset data) {

        this.pcaResults = pcaResults;
        this.data = data;
        this.plot = new DensScatterPlot();
        plot.setMaximumSize(new Dimension(32767, 32767));
        plot.setMinimumSize(new Dimension(12, 4));
        plot.setPreferredSize(new Dimension(300, 350));
        classtype = 9;
        isneurons = false;
        components.add(PCA2DComponent.this);
        plot.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 120, 120)));
        plot.setLayout(new java.awt.FlowLayout(0, 5, 1));
        updatePlot();
        if (SelectionManager.getSelectionManager().getSelectedRows(this.data) != null && shadowUnselected == false) //NOTE: should this method be used, or should this class implement the 
        {
            selectionChanged(Selection.TYPE.OF_ROWS);
        }
        SelectionManager.getSelectionManager().addSelectionChangeListener(data, PCA2DComponent.this);
    }

    public javax.swing.JToolTip createToolTip() {
        return (new JeToolTip2(plot)); //TODO: was previously JeToolTip
    }

    /**
     * Returns the dataset represented by this object
     *
     * @return The dataset represented by this object
     */
    @Override
    public Dataset getDataSet() {
        return data;
    }

    /**
     * If this is a som representation, light up the neuron at point p.
     *
     * @param p The neuron to be lit.
     */
    public void setSelected(java.awt.Point p) {
        plot.setHighLightedNeuron(p);

    }

    public void setframeType(int frameType) {
        plot.setframeType(frameType);
    }

    public void setFrameBG(Color color) {
        plot.setFrameBG(color);
    }

    public void forceFullRepaint() {
        plot.forceFullRepaint();
    }

    public boolean isHex() {
        return plot.isHex();
    }

    public void setHex(boolean hex) {
        plot.hex = hex;
    }

    public Axis getXaxis() {
        return plot.xaxis;
    }

    public Axis getYaxis() {
        return plot.yaxis;
    }

    public boolean[] getTolerated() {
        return plot.getTolerated();
    }

    public Vector getIndexesAtPoint(Point point, int radius) {
        return plot.getIndexesAtPoint(point, radius);
    }

    public boolean isZoompca() {
        return plot.zoompca;
    }

    public boolean isPaintNamesonClick() {

        return plot.paintNamesonClick;
    }

    public boolean[] getFramedIndexes() {
        return plot.getFramedIndexes();
    }

    public double[] getZoomedArea() {
        return plot.getZoomedArea();
    }

    public void setSpotNames(String[][] SpotNames) {
        plot.setSpotNames(SpotNames);
    }

    @Override
    public void setBorder(Border border) {
        plot.setBorder(border);

    }

    @Override
    public void setLayout(LayoutManager loutManager) {
        plot.setLayout(loutManager);
    }

    @Override
    public void addMouseMotionListener(MouseMotionListener mml) {
        plot.addMouseMotionListener(mml);
    }

    @Override
    public void addMouseListener(MouseListener ml) {
        plot.addMouseListener(ml);
    }

    public void setNotShaded(boolean[] notShaded) {
        plot.setNotShaded(notShaded);
    }

    /**
     * @return an array where the selected indexes are flagged as true. If there
     * are no indexes that are currently selected, or if the current Selection
     * from the SelectionManager is null, then an array of only false values are
     * returned.
     */
    public boolean[] getSelectedIndexes() {
        Selection selOfRows = SelectionManager.getSelectionManager().getSelectedRows(data);
        if (selOfRows != null) {
            int[] sel = selOfRows.getMembers();
            if (sel == null) {
                return new boolean[data.getDataLength()];
            }
            boolean[] ret = ArrayUtils.toBooleanArray(data.getDataLength(), sel);
            return ret;
        } else {
            return new boolean[data.getDataLength()];
        }
    }

    public void updatePlot() {
        if (pcaResults == null) {
            return;
        }

        double[][] points = new double[2][(int) pcaResults.nrPoints()];

        for (int i = 0; i < pcaResults.nrPoints(); i++) {
            points[0][i] = pcaResults.ElementAt(i, pcax);
            points[1][i] = pcaResults.ElementAt(i, pcay);
        }
        plot.data = data;
        if (zoom) {
            plot.setPropsAndData(points[0], points[1], zoomedRect);
        } else {
            plot.setPropsAndData(points[0], points[1]);
        }
        plot.setXaxisTitle("Principal Component " + (pcax + 1));
        plot.setYaxisTitle("Principal Component" + (pcay + 1));
        plot.FullRepaint = true;
        plot.repaint();
    }

    /**
     * Sweep the points within a square created by mouse dragging
     */
    public void sweep(boolean[] members) {
        System.out.println("PCAComponent: sweep()");
        updateSelectionOnDataSet(members);
        Tools t = new Tools();
        List<Integer> v = new ArrayList<Integer>();
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < members.length; i++) {
            if (members[i]) {
                b.append(i);
                b.append(",");
                v.add(i);
            }
        }

        if (b.length() > 1) {
            b.setLength(b.length() - 1);
        }

        MetaInfoNode metainfonode = new MetaInfoNode(MetaInfoNode.pca);

        if (v.size() > 0) {

            metainfonode.put("Plot_Size", plot.getPlotSize().width + "," + plot.getPlotSize().height);
            metainfonode.put("X-Component", String.valueOf(pcax + 1));
            metainfonode.put("Y-Component", String.valueOf(pcay + 1));

            Group Class = null;
            boolean activeSet = false;
            StringBuilder visibleGroups = new StringBuilder();
            if (data.getRowGroups().size() > 1) {
                for (int j = 0; j < data.getRowGroups().size(); j++) {
                    Class = (Group) data.getRowGroups().get(j);
                    activeSet = Class.isActive();//(Boolean)Class.elementAt(0);
                    if (activeSet) {
                        visibleGroups.append(Class.getName() + ",");
                    }
                }
                visibleGroups.setLength(visibleGroups.length() - 1);
                metainfonode.put("Groups_Visible", visibleGroups.toString());
            }

            metainfonode.put("Frame", plot.getFrameDescription());

            metainfonode.put("Row indices", b.toString());


            int i = ((Integer) plot.Layout.get("SbgSV")).intValue();
            int colors = ((Integer) plot.Layout.get("colorsSV")).intValue();
            int threshold = ((Integer) plot.Layout.get("tresholdSV")).intValue();

            if (i == 0) {
                metainfonode.put("Density Map", "On");
                metainfonode.put("Density Map Colors", String.valueOf(colors));
                metainfonode.put("Density Map Threshold", String.valueOf(threshold));
            } else {
                metainfonode.put("Density Map", "Off");
            }
        }
    }

    public String getMeta() {


        String meta = "";
        if (plot.getframeType() == 0) {
        } else if (plot.getframeType() == 1) {
        }

        meta += "Plot_Size_: (" + plot.getPlotSize().width + "," + plot.getPlotSize().height + ")\n";
        meta += "X-Component: " + (pcax + 1) + "\n";
        meta += "Y-Component: " + (pcay + 1) + "\n";

        Group group = null;
        boolean activeSet = false;

        if (data.getRowGroups().size() > 1) {
            meta += "Groups Visible: \n";

            for (int j = 0; j < data.getRowGroups().size(); j++) {

                group = (Group) data.getRowGroups().get(j);
                activeSet = group.isActive();

                if (activeSet) {
                    meta += group.getName() + " (n=" + group.size() + ")\n";
                }
            }
            meta += "Note: Members of theese groups are not known.\n";

        }

        meta += "Colors: " + numcolors + "\n";
        meta += "Dot plot density threshold: " + dotTolerance;

        return meta;
    }

    @Override
    public void finalize() {
        //This is just used for garbage collecting monitoring
        try {
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void ColumnSelectionHasChanged(Object source) {
    }

    public void zoomout() {
        plot.zoomout();
    }

    public void setZoomPca(boolean zoom) {
        plot.zoompca = zoom;
    }

    public void setPaintNamesonClick(boolean paint) {
        plot.paintNamesonClick = paint;
    }

    public Vector getComponentsVector() {
        return components;
    }

    public int getPcax() {
        return pcax;
    }

    public void setPcax(int pcax) {
        this.pcax = pcax;
    }

    public int getPcay() {
        return pcay;
    }

    public void setPcay(int pcay) {
        this.pcay = pcay;
    }

    public int getPcaz() {
        return pcaz;
    }

    public void setPcaz(int pcaz) {
        this.pcaz = pcaz;
    }

    public no.uib.jexpress_modularized.core.visualization.charts.DensScatterPlot getPlot() {
        return plot;
    }

    public no.uib.jexpress_modularized.pca.computation.PcaResults getPcaResults() {
        return pcaResults;
    }
}
