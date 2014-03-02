/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.colorcomponents;

import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * @author Bjarte
 */
public class ValueRange implements Serializable{

    private double min = -1.0;
    private double max = 1.0;
    private double range = 2.0;
    private double dmin = -1.0;
    private double dmax = 1.0;
    private double drange = 2.0;
    protected boolean automatic = true;
    protected boolean symmetric = false;
    protected ValueRange parent;

    public ValueRange() {
    }

    public ValueRange getCopy() {
        ValueRange ret = new ValueRange();
        ret.automatic = isAutomatic();
        ret.symmetric = isSymmetric();
        ret.min = min;
        ret.max = max;
        ret.parent = this;
        return ret;
    }

    public boolean isAutomatic() {
        if (parent != null) {
            return parent.isAutomatic();
        }
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    public void setMin(double min) {
        this.min = min;
        range = max - min;
    }

    public void setMax(double max) {
        this.max = max;
        range = max - min;
    }

    public ValueRange(Hashtable src) {
        min = (Double) src.get("Vmin");
        max = (Double) src.get("Vmax");
        automatic = (Boolean) src.get("Vautomatic");
        symmetric = (Boolean) src.get("symmetric");
        range = max - min;
    }

    public void setRangeFromData(double min, double max) {
        if (parent != null) {
            parent.setRangeFromData(min, max);
        }
        dmin = min;
        dmax = max;
        drange = getDataMax() - getDataMin();
    }

    public ValueRange(double min, double max) {
        this.min = min;
        this.max = max;
        range = max - min;
    }

    public double relativeValue(double val) {

        //if(true) return 1;
        if (parent != null) {
            return parent.relativeValue(val);
        }
        if (isAutomatic()) {
            if (val < getDataMin()) {
                return 0.0;
            }
            if (val > getDataMax()) {
                return 1.0;
            }
            return ((val - getDataMin()) / drange);
        } else {
            if (val < min) {
                return 0.0;
            }
            if (val > max) {
                return 1.0;
            }
            return ((val - min) / range);
        }
    }

    public double relativeSymmetricValue(double val) {

        //if(true) return -1;
        double ret = 0.0;
        if (parent != null) {
            return parent.relativeSymmetricValue(val);
        }
        if (isAutomatic()) {
            double tmin = getDataMin();
            double tmax = getDataMax();
            if (val < tmin) {
                ret = 0.0;
            }
            if (val > tmax) {
                ret = 1.0;
            }

            if (val < 0) {
                ret = 0.5 - ((val / getDataMin()) * 0.5);
            } else if (val > 0) {
                ret = 0.5 + ((val / getDataMax()) * 0.5);
            } else {
                ret = 0.5;
            }
        } else {
            if (val < min) {
                ret = 0.0;
            }
            if (val > max) {
                ret = 1.0;
            }
            if (val < 0) {
                ret = 0.5 - ((val / min) * 0.5);
            } else if (val > 0) {
                ret = 0.5 + ((val / max) * 0.5);
            } else {
                ret = 0.5;
            }
        }
        //System.out.println(ret);
        return ret;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getDataMin() {
        //if(parent!=null) return parent.getDataMin();
        if (isSymmetric()) {
            return -Math.max(Math.abs(dmax), Math.abs(dmin));
        }
        return dmin;
    }

    public double getDataMax() {
        //if(parent!=null) return parent.getDataMax();
        //if(isSymmetric()) return -dmin;//return Math.min( Math.abs(dmax),Math.abs(dmin));
        if (isSymmetric()) {
            return Math.max(Math.abs(dmax), Math.abs(dmin));
        }
        return dmax;
    }

    public double getABSDataMax() {
        return Math.max(Math.abs(dmax), Math.abs(dmin));
    }

    public void putInHash(Hashtable hash) {
        hash.put("Vmin", min);
        hash.put("Vmax", max);
        hash.put("Vautomatic", automatic);
        hash.put("symmetric", isSymmetric());
    }

    public boolean isSymmetric() {
        return symmetric;
    }

    public void setSymmetric(boolean symmetric) {
        this.symmetric = symmetric;
    }
}
