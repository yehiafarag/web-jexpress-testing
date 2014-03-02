/*
 * ControlPoint.java
 *
 * Created on 13. august 2005, 01:22
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package no.uib.jexpress_modularized.core.visualization.charts;

import java.io.Serializable;

/**
 *
 * @author bjarte dysvik
 */
public class ControlPoint extends Object implements Serializable{

    public int x;
    public int y;
    public static final int PT_SIZE = 4;

    public ControlPoint(int a, int b) {

        x = a;

        y = b;

    }

    public boolean within(int a, int b) {

        if (a >= x - PT_SIZE
                && b >= y - PT_SIZE
                && a <= x + PT_SIZE
                && b <= y + PT_SIZE) {
            return true;
        } else {
            return false;
        }

    }
}
