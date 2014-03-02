/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.colorcomponents;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Bjarte
 */
public class ColorFactory implements Serializable{

    private Color[] allColors;
    private Color[] pos;
    private Color[] neg;
    private int numColors = 256;
    private List<ControlPoint> ControlPoints = new ArrayList<ControlPoint>();
    private ValueRange range = new ValueRange(-1.0, 1.0);
    private Color missing = Color.GREEN;
    private int ID;
    protected ColorFactory parent = null;
    public long lastChangedStamp = 0;

    protected void setID(int ID) {
        this.ID = ID;
    }

    public ColorFactory getDerivedFactory() {
        ColorFactory ret = new ColorFactory();
        ret.setControlPoints(ControlPoints);
        ret.setMissing(missing);
        ret.setID(ID);
        ret.parent = this;
        ret.setValueRange(range.getCopy());
        return ret;
    }

    public ColorFactory() {
        ID = (int) (Math.random() * 1000000);
    }

    public int getID() {
        return ID;
    }

    public Color getColor(double value) {

        if (parent != null) {

            if (lastChangedStamp != parent.lastChangedStamp) {
                this.resetColorTable();
                lastChangedStamp = parent.lastChangedStamp;
            }
        }

        if (!isSymmetric()) {
            //  if (true) {
            if (range != null) {
                value = range.relativeValue(value);
            }

            if (allColors == null) {
                allColors = getAllColors(numColors);
            }

            if (value < 0.0) {
                return allColors[0];
            }
            if (value > 1.0) {
                return allColors[allColors.length - 1];
            }

            return allColors[(int) ((allColors.length - 1) * value)];

        } else {

            if (range != null) {
                value = range.relativeSymmetricValue(value);
            }

            if (allColors == null) {
                allColors = getAllColors(numColors);
            }

            if (value < 0.0) {
                return allColors[0];
            }
            if (value > 1.0) {
                return allColors[allColors.length - 1];
            }

            //System.out.println("S: "+value);

            //return allColors[(int) ((allColors.length - 1) * (value + 1.0) / 2.0)];
            return allColors[(int) ((allColors.length - 1) * value)];
        }
    }

    public void resetColorTable() {
        clearColorArrays();
        lastChangedStamp = Calendar.getInstance().getTimeInMillis();
    }

    public void initControlPoints(boolean symmetric) {

        range.setSymmetric(symmetric);

        ControlPoint cp = new ControlPoint();
        cp.setColor(Color.BLUE);
        cp.setLocation(-1.0);
        cp.setFixed(true);
        addControlPoint(cp);

        if (symmetric) {

            cp = new ControlPoint();
            cp.setColor(Color.WHITE);
            cp.setLocation(0.0);
            addControlPoint(cp);
            cp.setFixed(true);
        }

        cp = new ControlPoint();
        cp.setColor(Color.RED);
        cp.setLocation(1.0);
        addControlPoint(cp);
        cp.setFixed(true);

    }

    public Color[] getNegativeColors(int arrayWidth) {
        //if(parent!=null) return parent.getNegativeColors(arrayWidth);
        Color[] ret = new Color[arrayWidth];

        for (int i = 0; i < getControlPoints().size(); i++) {
            if (getControlPoints().get(i).getLocation() <= 0) {

                double v = -getControlPoints().get(i).getLocation();
                int ind = (int) (v * (arrayWidth - 1));

                ret[ind] = getControlPoints().get(i).getColor();
            }
        }
        interpolateColors(ret);

        return ret;
    }

    public Color[] getPositiveColors(int arrayWidth) {
        //if(parent!=null) return parent.getPositiveColors(arrayWidth);
        Color[] ret = new Color[arrayWidth];

        for (int i = 0; i < getControlPoints().size(); i++) {
            if (getControlPoints().get(i).getLocation() >= 0) {

                double v = getControlPoints().get(i).getLocation();
                int ind = (int) (v * ((arrayWidth) - 1));

                ret[ind] = getControlPoints().get(i).getColor();
            }
        }
        interpolateColors(ret);

        return ret;
    }

    public Color[] getAllColors(int arrayWidth) {
        //if(parent!=null) return parent.getAllColors(arrayWidth);
        if (parent == null) {
            lastChangedStamp = Calendar.getInstance().getTimeInMillis();
        }
        Color[] ret = new Color[arrayWidth];

        for (int i = 0; i < getControlPoints().size(); i++) {

            double v = (getControlPoints().get(i).getLocation() + 1.0) / 2.0;
            int ind = (int) (v * ((arrayWidth) - 1));

            ret[ind] = getControlPoints().get(i).getColor();
        }
        interpolateColors(ret);

        return ret;
    }

    private void interpolateColors(Color[] colors) {

        int toIndex = 1;
        int fromIndex = 0;

        while (fromIndex < colors.length - 1) {

            toIndex = findNextColor(colors, fromIndex);
            interpolateColors(colors, fromIndex, toIndex);

            fromIndex = toIndex;
        }
    }

    private int findNextColor(Color[] colors, int start) {
        for (int i = start + 1; i < colors.length; i++) {
            if (colors[i] != null) {
                return i;
            }
        }
        return colors.length - 1;
    }

    private void clearColorArrays() {
        pos = null;
        neg = null;
        allColors = null;

    }

    private void interpolateColors(Color[] colors, int from, int to) {
        Color c1 = colors[from];
        Color c2 = colors[to];

        if (c1 == null || c2 == null) {
            return;
        }

        for (int i = from; i < to; i++) {

            double R = c2.getRed() - c1.getRed();
            double G = c2.getGreen() - c1.getGreen();
            double B = c2.getBlue() - c1.getBlue();

            double fract = (i - from) / (double) (to - from);

            double RR = c1.getRed() + (double) ((R) * fract);
            double GG = c1.getGreen() + (double) ((G) * fract);
            double BB = c1.getBlue() + (double) ((B) * fract);

            colors[i] = new Color((int) RR, (int) GG, (int) BB);

        }


    }

    public void removeControlPoint(ControlPoint point, boolean removePartner) {
        clearColorArrays();
        getControlPoints().remove(point);
        if (removePartner && point.getPartner() != null) {
            getControlPoints().remove(point.getPartner());
        }
    }

    public void addControlPoint(ControlPoint point) {
        clearColorArrays();
        getControlPoints().add(point);
    }

    public void setMirror(boolean mirror) {
        clearColorArrays();
    }

    public boolean isSymmetric() {
        return range.isSymmetric();
    }

    public void setSymmetric(boolean symmetric) {
        clearColorArrays();
        range.setSymmetric(symmetric);
    }

    public Color[] getAllColors() {
        return allColors;
    }

    public Color[] getPos() {
        return pos;
    }

    public Color getMissing() {
        if (parent != null) {
            return parent.getMissing();
        }
        return missing;
    }

    public void setMissing(Color missing) {
        this.missing = missing;
    }

    public ValueRange getValueRange() {
        return range;
    }

    public void setValueRange(ValueRange range) {
        this.range = range;
    }

    public void setColors(Color[] pos, Color[] neg, Color nullc, Color miss) {

        ControlPoints.clear();
        range.setSymmetric(true);
        this.missing = miss;

        ControlPoint p = new ControlPoint();
        p.setLocation(0.0);
        p.setColor(nullc);
        addControlPoint(p);
        p.setFixed(true);

        p = new ControlPoint();
        p.setLocation(0.2);
        p.setColor(pos[(int) ((pos.length - 1) * 0.2)]);
        addControlPoint(p);

        ControlPoint p2 = new ControlPoint();
        p2.setLocation(-0.2);
        p2.setColor(pos[(int) ((neg.length - 1) * 0.2)]);
        addControlPoint(p2);
        p.setPartner(p2);
        p2.setPartner(p);

        p = new ControlPoint();
        p.setLocation(0.4);
        p.setColor(pos[(int) ((pos.length - 1) * 0.4)]);
        addControlPoint(p);

        p2 = new ControlPoint();
        p2.setLocation(-0.4);
        p2.setColor(neg[(int) ((neg.length - 1) * 0.4)]);
        addControlPoint(p2);
        p.setPartner(p2);
        p2.setPartner(p);

        p = new ControlPoint();
        p.setLocation(0.6);
        p.setColor(pos[(int) ((pos.length - 1) * 0.6)]);
        addControlPoint(p);

        p2 = new ControlPoint();
        p2.setLocation(-0.6);
        p2.setColor(neg[(int) ((neg.length - 1) * 0.6)]);
        addControlPoint(p2);
        p.setPartner(p2);
        p2.setPartner(p);

        p = new ControlPoint();
        p.setLocation(0.8);
        p.setColor(pos[(int) ((pos.length - 1) * 0.8)]);
        addControlPoint(p);

        p2 = new ControlPoint();
        p2.setLocation(-0.8);
        p2.setColor(neg[(int) ((neg.length - 1) * 0.8)]);
        addControlPoint(p2);
        p.setPartner(p2);
        p2.setPartner(p);

        p = new ControlPoint();
        p.setLocation(1.0);
        p.setColor(pos[(int) ((pos.length - 1) * 1.0)]);
        addControlPoint(p);
        p.setFixed(true);

        p2 = new ControlPoint();
        p2.setLocation(-1.0);
        p2.setColor(neg[(int) ((neg.length - 1) * 1.0)]);
        addControlPoint(p2);
        p.setPartner(p2);
        p2.setPartner(p);
        p2.setFixed(true);
    }

    public Color[] getNeg() {
        return neg;
    }

    public void setNeg(Color[] neg) {
        this.neg = neg;
    }

    public void setPos(Color[] pos) {
        this.pos = pos;
    }

    public void setAllColors(Color[] cols) {
        this.allColors = cols;
    }

    public int getNumColors() {
        return numColors;
    }

    public void setNumColors(int numColors) {
        this.numColors = numColors;
    }

    public List<ControlPoint> getControlPoints() {
        return ControlPoints;
    }

    public void setControlPoints(List<ControlPoint> ControlPoints) {
        this.ControlPoints = ControlPoints;
    }
}
