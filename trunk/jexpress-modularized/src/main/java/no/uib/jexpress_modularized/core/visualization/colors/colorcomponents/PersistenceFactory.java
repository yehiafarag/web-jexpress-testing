/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization.colors.colorcomponents;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Bjarte
 */
public class PersistenceFactory implements Serializable{

    public static Hashtable toHash(ColorFactory factory) {
        Hashtable ret = new Hashtable();
        ret.put("symmetric", factory.isSymmetric());

        List<Hashtable> hashs = new ArrayList<Hashtable>();
        List<ControlPoint> cp = factory.getControlPoints();
        for (ControlPoint p : cp) {
            hashs.add(p.toHash());
        }

//        if(factory.getPos()!=null){
//          ret.put("pos", factory.getPos());
//          ret.put("neg", factory.getNeg());
//          ret.put("col", factory.getAllColors());
//        }

        ret.put("controlpoints", hashs);
        if (factory.getMissing() == null) {
            factory.setMissing(Color.gray);
        }
        ret.put("missing", factory.getMissing());
        ret.put("ID", factory.getID());

        factory.getValueRange().putInHash(ret);
        return ret;
    }

    public static ColorFactory fromHash(Hashtable hash) {

        ColorFactory factory = new ColorFactory();
        factory.setSymmetric((Boolean) hash.get("symmetric"));
        factory.setID((Integer) hash.get("ID"));

        List<Hashtable> hashs = (List<Hashtable>) hash.get("controlpoints");
        List<ControlPoint> cp = new ArrayList<ControlPoint>();
        for (Hashtable h : hashs) {
            cp.add(ControlPoint.fromHash(h));
        }

        for (ControlPoint p : cp) {
            p.resolvePartner(cp);
        }

//        if(hash.containsKey("pos")){
//          
//          factory.setNeg((Color[])hash.get("neg"));
//          factory.setPos((Color[])hash.get("pos"));
//          factory.setAllColors((Color[])hash.get("col"));
//
//        }


        factory.setControlPoints(cp);
        factory.setValueRange(new ValueRange(hash));

        return factory;
    }

    public static void restoreFromHash(ColorFactory factory, Hashtable hash) {

        factory.setSymmetric((Boolean) hash.get("symmetric"));
        factory.setID((Integer) hash.get("ID"));

        List<Hashtable> hashs = (List<Hashtable>) hash.get("controlpoints");
        List<ControlPoint> cp = new ArrayList<ControlPoint>();
        for (Hashtable h : hashs) {
            cp.add(ControlPoint.fromHash(h));
        }

        for (ControlPoint p : cp) {
            p.resolvePartner(cp);
        }

        factory.setControlPoints(cp);
        factory.setValueRange(new ValueRange(hash));

    }
}
