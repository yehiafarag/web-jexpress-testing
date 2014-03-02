/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.colorcomponents;

import java.awt.Color;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Bjarte
 */
public class ControlPoint implements Serializable {

    private double location;
    private Color color;
    private boolean fixed = false;
    private ControlPoint partner;
    protected long ID;
    protected long partnerID = -1l;

    public Hashtable toHash() {
        Hashtable ret = new Hashtable();
        ret.put("location", location);
        ret.put("color", color.getRGB());
        ret.put("fixed", fixed);
        ret.put("ID", ID);
        if (partner != null) {
            ret.put("partnerID", partner.ID);
        }
        return ret;
    }

    public static ControlPoint fromHash(Hashtable hash) {
        ControlPoint ret = new ControlPoint();

        ret.setLocation((Double) hash.get("location"));
        ret.setColor(new Color((Integer) hash.get("color")));
        ret.setFixed((Boolean) hash.get("fixed"));
        ret.ID = (Long) hash.get("ID");
        if (hash.containsKey("partnerID")) {
            ret.partnerID = (Long) hash.get("partnerID");
        }

        return ret;
    }

    public void resolvePartner(List<ControlPoint> P) {

        for (ControlPoint p : P) {
            if (p.ID == partnerID) {
                partner = p;
            }
        }

    }

    public ControlPoint() {
        ID = (long) (Math.random() * 10000000.0);

    }

    public double getLocation() {
        return location;
    }

    public void setLocation(double location) {
        this.location = location;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public ControlPoint getPartner() {
        return partner;
    }

    public void setPartner(ControlPoint partner) {
        this.partner = partner;
    }
}
