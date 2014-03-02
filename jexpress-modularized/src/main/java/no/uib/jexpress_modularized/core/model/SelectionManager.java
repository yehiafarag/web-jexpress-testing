package no.uib.jexpress_modularized.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import no.uib.jexpress_modularized.core.dataset.Dataset;

/**
 * Singleton class containing selections for datasets, registry of
 * SelectionChangeListeners, and a mediator of selection change events. Any
 * window (view) that should be notified of selection changes on any particular
 * dataset, should implement the SelectionChangeListener interface and
 * register in the SelectionManager as a listener for this particular dataset.
 *
 * @author pawels
 */
public class SelectionManager implements Serializable{

    /* Singleton instance of this class */
   private static SelectionManager instance = null;

    /** Column selections per dataset */
    private Hashtable<Dataset, Selection> selectedColumns;
    /** Row selections per dataset */
    private Hashtable<Dataset, Selection> selectedRows;

    /** Registry of listeners for every dataset */
    private Hashtable<Dataset, List<SelectionChangeListener>> selectionChangeListeners;


    private SelectionManager() {
        selectedColumns = new Hashtable<Dataset,Selection>();
        selectedRows = new Hashtable<Dataset, Selection>();
        selectionChangeListeners = new Hashtable<Dataset, List<SelectionChangeListener>>();
    }


    /**
     * Get hold of the singleton instance of SelectionManager
     * @return SelectionManager instance
     */
    public static SelectionManager getSelectionManager() {
        if (instance == null) {
            instance = new SelectionManager();
        }
        return instance;
    }

     /**
     *
     * @param ds
     * @return selected columns, or null if no selection has been made
     */
    public Selection getSelectedColumns(Dataset ds) {
        if (!selectedColumns.containsKey(ds)) { return null; }
        return selectedColumns.get(ds);
    }

    /**
     *
     * @param ds
     * @return selected rows, or null if no selection has been made
     */
    public Selection getSelectedRows(Dataset ds) {
        //would it perhaps be better to return an empty Selection rather than a null reference? That way there is a reduced chance
        //of NullPointerException happening from using the reference that this method returns directly.
        if (!selectedRows.containsKey(ds)) { return null; }
        return selectedRows.get(ds);
    }

    /**
     *
     * @param ds - dataset to set selection on
     * @param s - selection of columns, if null - no selection
     */
    public void setSelectedColumns(Dataset ds, Selection s) {
        if (ds == null) { throw new IllegalArgumentException("Trying to select columns in a null DataSet"); }
        if (s == null) {
            selectedColumns.remove(ds);
        } else if (!s.type.equals(Selection.TYPE.OF_COLUMNS)) {
            throw new IllegalArgumentException("Trying to select columns with an incorrect type of selection");
        } else {
            selectedColumns.put(ds,s);
        }
        selectionChangedEvent(ds, Selection.TYPE.OF_COLUMNS);
    }

    /**
     *
     * @param ds - dataset to set selection on
     * @param s - selection of rows, if null - no selection
     */
    public void setSelectedRows(Dataset ds, Selection s) {
        if (ds == null) { throw new IllegalArgumentException("Trying to select rows in a null DataSet"); }
        if (s == null) {
            selectedRows.remove(ds);
        } else if (!s.type.equals(Selection.TYPE.OF_ROWS)) {
            throw new IllegalArgumentException("Trying to select rows with an incorrect type of selection");
        } else {
            selectedRows.put(ds,s);
        }
        selectionChangedEvent(ds, Selection.TYPE.OF_ROWS);
    }


    /**
     * Mediate information about selection change on a dataset. 
     * @param dataset - dataset that has changed selection
     * @param type - of the selection (on row/columns)
     */
    private void selectionChangedEvent(Dataset dataset, Selection.TYPE type) {
        if (dataset == null || !selectionChangeListeners.containsKey(dataset)) return;

        for (SelectionChangeListener listener : selectionChangeListeners.get(dataset)) {
            listener.selectionChanged(type);
        }
    }



    /*
     *
     * SelectionChangeListeners handling
     *
     */

    /**
     * Add SelectionChangeListener for dataset
     * @param dataset - dataset to 'listen to'
     * @param listener
     */
    public void addSelectionChangeListener(Dataset dataset, SelectionChangeListener listener) {
        if (dataset == null || listener == null) {throw new IllegalArgumentException("Dataset or listener is null");}

        if (!selectionChangeListeners.containsKey(dataset)) {
            selectionChangeListeners.put(dataset, new ArrayList<SelectionChangeListener>());
        }
        List<SelectionChangeListener> listeners = selectionChangeListeners.get(dataset);
        if (!listeners.contains(listener)) {    // dont keep multiple entries for the same listener
           listeners.add(listener);
        }
    }


    /**
     * Remove SelectionChangeListener from dataset
     * @param dataset - dataset to stop listening to
     * @param listener
     */
    public void removeSelectionChangeListener(Dataset dataset, SelectionChangeListener listener) {
        if (dataset == null || listener == null) {throw new IllegalArgumentException("Dataset or listener is null");}
        if (!selectionChangeListeners.containsKey(dataset)) { return; }

        selectionChangeListeners.get(dataset).remove(listener);
    }


}
