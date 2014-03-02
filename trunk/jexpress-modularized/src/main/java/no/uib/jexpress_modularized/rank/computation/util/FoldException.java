/*
 * FoldException.java
 *
 * Created on 05 July 2007, 15:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.rank.computation.util;

import java.io.Serializable;

/**
 *
 * @author Anne-Kristin
 */
public class FoldException extends Exception implements Serializable{
    
    /**
     * Creates a new instance of <code>FoldException</code> without detail message.
     */
    public FoldException() {        
    }    
    
    /**
     * Constructs an instance of <code>FoldException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FoldException(String msg) {
        super(msg);
    }
}
