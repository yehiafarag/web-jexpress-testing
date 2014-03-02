/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.rank.computation;

import java.io.Serializable;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author Yehia Farag
 */
public class RPResult extends javax.swing.table.AbstractTableModel implements Serializable {

    private Vector rowData;
    private Vector columnNames;
    private Map<Integer, Integer> sortMap;

    public Map<Integer, Integer> getSortMap() {
        return sortMap;
    }

    public void setSortMap(Map<Integer, Integer> sortMap) {
        this.sortMap = sortMap;
    }

    public RPResult(Vector dat, Vector head) {
        rowData = dat;
        columnNames = head;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames.get(col).toString();
    }

    @Override
    public int getRowCount() {
        return rowData.size();
    }

    public Vector getRowData() {
        return rowData;
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        return ((Vector) rowData.get(row)).get(col);
    }

    @Override
    public Class getColumnClass(int column) {

        if ((column >= 0) && (column < getColumnCount())) {
            if (column == columnNames.size() - 3 || column == columnNames.size() - 6) {
                return java.math.BigInteger.class;
            } else {
                return getValueAt(0, column).getClass();
            }
        }

        return Object.class;

    }
}
