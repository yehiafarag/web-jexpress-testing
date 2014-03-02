/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.core.model;

/**
 *
 * Any window (view) that should be notified of selection changes on any
 * particular dataset, should implement the SelectionChangeListener interface,
 * and register in the SelectionManager as a listener for this particular dataset. 
 * @author pawels
 */
public interface SelectionChangeListener  {

    public void selectionChanged(Selection.TYPE type);

}
