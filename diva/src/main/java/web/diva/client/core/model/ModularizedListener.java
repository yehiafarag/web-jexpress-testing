/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.core.model;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Template for the main classes.
 */
public abstract class ModularizedListener  implements SelectionChangeListener, IsSerializable {

    //NOTE: mode this variable protected (had default modifier originally)
    protected java.util.Vector components = new java.util.Vector();
    //NOTE: made this variable protected (had default modifier originally)
    protected int datasetId=0;
    //NOTE: made this variable protected (had default modifier originally)
    protected int classtype = 0;
//    boolean inProjectTree = false;

    //1 genetable
    //2 somClust
    //3 lineChart
    //4 PCA
    //5 rank  table
    //10 empty selection
    /**
     * Set the type of component.
     *
     * @param classtype The class type.
     *
     * <dir>
     *
     * <li>1 GeneTable</li>
     *
     * <li>2 somClust</li>
     *
     * <li>3 LineChart</li>
     *
     * <li>4 PCA</li>
     *
     * <li>5 Rank Table</li>
     *
     * <li>10 Empty selection</li>
     *
     * 
     * </dir>
     *
     */
    public void setClassType(int classtype) {

        this.classtype = classtype;

    }

    

    /**
     * Returns the class type.
     *
     * @return classtype
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

