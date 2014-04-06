/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.selectionmanager;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author Yehia Farag
 */
public class SelectionManager implements IsSerializable {

    /**
     * Column selections per dataset
     */
    private final HashMap<Integer, Selection> selectedColumns;
    /**
     * Row selections per dataset
     */
    private final HashMap<Integer, Selection> selectedRows;
    /**
     * Registry of listeners for every dataset
     */
    private final HashMap<Integer, List<SelectionChangeListener>> selectionChangeListeners;

    public SelectionManager() {
        selectedColumns = new HashMap<Integer, Selection>();
        selectedRows = new HashMap<Integer, Selection>();
        selectionChangeListeners = new HashMap<Integer, List<SelectionChangeListener>>();
    }

    /**
     *
     * @param datasetId
     * @return selected columns, or null if no selection has been made
     */
    public Selection getSelectedColumns(int datasetId) {
        if (!selectedColumns.containsKey(datasetId)) {
            return null;
        }
        return selectedColumns.get(datasetId);
    }

    /**
     *
     * @param datasetId
     * @return selected rows, or null if no selection has been made
     */
    public Selection getSelectedRows(int datasetId) {
        if (!selectedRows.containsKey(datasetId)) {
            return null;
        }
        return selectedRows.get(datasetId);
    }

    /**
     *
     * @param datasetId - dataset to set selection on
     * @param s - selection of columns, if null - no selection
     */
    public void setSelectedColumns(int datasetId, Selection s) {
        if (datasetId == 0) {
            throw new IllegalArgumentException("Trying to select columns in a null DataSet");
        }
        if (s == null) {
            selectedColumns.remove(datasetId);
        } else if (!s.type.equals(Selection.TYPE.OF_COLUMNS)) {
            throw new IllegalArgumentException("Trying to select columns with an incorrect type of selection");
        } else {
            selectedColumns.put(datasetId, s);
        }
        selectionChangedEvent(datasetId, Selection.TYPE.OF_COLUMNS, selectedColumns.size());
    }

    /**
     *
     * @param datasetId - dataset id to set selection on
     * @param s - selection of rows, if null - no selection
     * @param compType component that made the event
     */
    public void setSelectedRows(int datasetId, Selection s, int compType) {
        if (datasetId == 0) {
            Window.alert("dataset is 0");
            throw new IllegalArgumentException("Trying to select rows in a null DataSet");
        }
        if (s == null) {
            Window.alert("selection is null");
            selectedRows.remove(datasetId);
        } else if (!s.type.equals(Selection.TYPE.OF_ROWS)) {
            Window.alert("selection type is not rows");
            throw new IllegalArgumentException("Trying to select rows with an incorrect type of selection");
        } else {

            selectedRows.put(datasetId, s);
        }
        selectionChangedEvent(datasetId, Selection.TYPE.OF_ROWS, selectedRows.size());
    }
    private int selectionTimerIndex = 0;
    private Timer timer;

    /**
     * Mediate information about selection change on a dataset.
     *
     * @param datasetId - dataset that has changed selection
     * @param type - of the selection (on row/columns)
     * @param size
     */
    private void selectionChangedEvent(final int datasetId, final Selection.TYPE type, int size) {
        if (datasetId == 0 || !selectionChangeListeners.containsKey(datasetId)) {
            return;
        }
        selectionTimerIndex = 0;
        timer = new Timer() {
            @Override
            public void run() {
                if (selectionTimerIndex < selectionChangeListeners.get(datasetId).size()) {
                    SelectionChangeListener listener = selectionChangeListeners.get(datasetId).get(selectionTimerIndex);
                    listener.selectionChanged(type);
                    selectionTimerIndex++;
                } else {
                    timer.cancel();
                    RootPanel.get("loaderImage").setVisible(false);
                }
            }
        };
        RootPanel.get("loaderImage").setVisible(true);
        timer.scheduleRepeating(Math.min(100, size));

    }

    /**
     * Add SelectionChangeListener for dataset
     *
     * @param datasetId - dataset to 'listen to'
     * @param listener
     */
    public void addSelectionChangeListener(int datasetId, SelectionChangeListener listener) {
        if (datasetId == 0 || listener == null) {
            throw new IllegalArgumentException("Dataset or listener is null");
        }

        if (!selectionChangeListeners.containsKey(datasetId)) {
            selectionChangeListeners.put(datasetId, new ArrayList<SelectionChangeListener>());
        }
        List<SelectionChangeListener> listeners = selectionChangeListeners.get(datasetId);
        if (!listeners.contains(listener)) {    // dont keep multiple entries for the same listener
            listeners.add(listener);
        }
    }

//    /**
//     * Remove SelectionChangeListener from dataset
//     *
//     * @param datasetId - dataset to stop listening to
//     * @param listener
//     */
//    public void removeSelectionChangeListener(int datasetId, SelectionChangeListener listener) {
//        if (datasetId == 0 || listener == null) {
//            throw new IllegalArgumentException("Dataset or listener is null");
//        }
//        if (!selectionChangeListeners.containsKey(datasetId)) {
//            return;
//        }
//
//        selectionChangeListeners.get(datasetId).remove(listener);
//    }
}
