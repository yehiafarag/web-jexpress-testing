/*--------------------------------------------------------------------------

 *

 * Copyright (c) 2001 MolMine AS.  All rights reserved.

 *

 * All paper, computer, digital, graphical, or other representations of the source code remain

 * the property of MolMine AS. All patents, ideas, inventions or novelties contained within the

 * source code are the exclusive intellectual property of MolMine AS. Surce code is provided 

 * for reference and support purposes only. Copies of the source code in any form, whether this

 * be digital, graphical or any other media, may not be distributed, discussed, or revealed to 

 * any person, computer or organisation not directly involved in support of a related product 

 * provided by the licensee or organisation not authorzed by MolMine AS to be directly involved 

 * in source code level support of J-Express.

 

 * The source code may not be modified except where specifically authorized by MolMine AS. No 

 * part of the source code may be used  within any product other than J-Express.

 *

 * You undertake that:

 *  The source code will not be distributed except where specifical authorized by MolMine AS.

 *  That you will ensure that all copies and representations of the source code can be identified.

 *

 * DISCLAIMER:

 * THIS SOFTWARE IS PROVIDED BY MOLMINE AS "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, 

 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS

 * FOR A PARTICULAR PURPOSE  ARE DISCLAIMED.  IN NO EVENT SHALL MOLMINE OR ITS CONTRIBUTORS BE 

 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 

 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 

 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,

 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 

 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 *

 *---------------------------------------------------------------------------

 */
//package jexpress;
package no.uib.jexpress_modularized.core.model;

import java.io.Serializable;
import no.uib.jexpress_modularized.core.dataset.Dataset;

/**
 * Template for the main classes.
 */
public abstract class ModularizedListener extends javax.swing.JComponent implements SelectionChangeListener, Serializable {

    java.awt.Image dispImage;
    //NOTE: mode this variable protected (had default modifier originally)
    protected java.util.Vector components = new java.util.Vector();
    //NOTE: made this variable protected (had default modifier originally)
    protected Dataset data;
    //NOTE: made this variable protected (had default modifier originally)
    protected int classtype = 0;
    boolean inProjectTree = false;

    //1 mainThumbs
    //2 mainclust
    //3 mainSOM
    //4 mainPCA
    //5 mainKmeans
    //11 rank  table
    /**
     * Set the type of component.
     *
     * @param classtype The classtype.
     *
     * <dir>
     *
     * <li>1 mainThumbs</li>
     *
     * <li>2 mainclust</li>
     *
     * <li>3 mainSOM</li>
     *
     * <li>4 mainPCA</li>
     *
     * <li>5 mainKmeans</li>
     *
     * <li>7 TableComponent</li>
     *
     * <li>8 LineChartGraphComponent</li>
     *
     * <li>9 PCAComponent</li>
     *
     *  * <li>10 RankTable</li>      *
     * </dir>
     *
     */
    public void setClassType(int classtype) {

        this.classtype = classtype;

    }

    /**
     * Set the project state for this object
     *
     * @param inProjectTree true if this object is in the project
     *
     */
    public void setInProject(boolean inProjectTree) {

        this.inProjectTree = inProjectTree;

//        if(data!=null) data.setInProject(inProjectTree); //TODO reimplement

    }

    /**
     * Returns the classtype.
     *
     * @return the classtype
     *
     */
    public int getClassType() {

        return classtype;

    }

    /**
     * Creates new MainModule
     */
    public ModularizedListener() {
    }

    /**
     * Set the dataset represented by this object
     *
     * @param data the source data
     *
     */
    public void setDataSet(Dataset data) {

        this.data = data;

    }

    /**
     * Returns the dataset represented by this object
     *
     * @return The dataset represented by this object
     *
     */
    public Dataset getDataSet() {

        return data;

    }

    /**
     * Add a component to this objects list of components.
     *
     * @param comp A component that is used by this object
     *
     */
    public void addComponent(Object comp) {

        components.add(comp);

    }

    /**
     * Get the component at location index.
     *
     * @param index The index of the wanted component
     *
     * @return The component at the given index
     *
     */
    public Object getComponentAt(int index) {

        return components.elementAt(index);

    }

    /**
     * Remove a component from this objects list of used components.
     *
     * @param comp The component to be removed
     *
     */
    public void removeComponent(Object comp) {

        components.removeElement(comp);

    }

    /**
     * Remove this object from the dataset changelisteners list
     *
     */
    public void disconnectData() {

        /*

         if(data!=null && data.getParent()!=null){

         if(!inProjectTree)data.getParent().removeVChild(data);

         //            data.removeChild();

         }

         */

//        if(data!=null)data.removeDataListener(this);

//        if(getClassType()==4) data.removeDataListener( ((dataListener)this) );

        //if(getClassType()==2)  ((mainclust)this).FullDataSet.removeDataListener( ((mainclust)this) ) ;

        if (getClassType() == 2) {
//            if(data!=null)data.removeDataListener( ((mainclust)this) );
            // mainclust cl = ((mainclust)this);
            // cl.FullDataSet.removeDataListener( cl);
        }

        if (getClassType() == 6) {
//             ((mainGraph)this).stopDataListening(); //TODO
        }

//        if(getClassType()==3)  data.removeDataListener( ((mainSOM2)this) ) ; //TODO

        // if(getClassType()==7)  data.removeDataListener( ((mainPCA3D2)this) ) ;





    }

    /**
     * Returns an Image of this component
     *
     * @return An Image of this component
     *
     */
    public java.awt.Image getImage() {

        return dispImage;

    }

    public javax.swing.JTable getTable() {
        return null;
    }

    //NOTE: this method was inherited from Interface dataListener: probably redundant
//    /** Default method for datalisteners. Runs the dataHasChangedRedraw() method in the implementing class which should be overridden.
//
//     */    
//
//    public void SelectionHasChanged(Object source) {
//
//        //dataHasChangedRedraw();
//
//    }    
    /**
     * Called from the dataHasChanged() method.
     *
     */
    public void dataHasChangedRedraw() {
    }

    public void dataHasChanged() {

        dataHasChangedRedraw();

    }

    public void columnsHasChanged() {
    }
}

