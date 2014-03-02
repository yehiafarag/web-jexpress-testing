/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.dataset;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import no.uib.jexpress_modularized.core.visualization.NewGroupIndexer;

/**
 *
 * @author Bjarte
 */
public class RowAnnotationModel extends AbstractTableModel implements Serializable {

    private Dataset m_data;
    private boolean showGroups = false;
    private boolean showIndexes = false;
    private AnnotationFilter filter;
    private String[][] annotation;
    private String[] header;
    private int rows;
    private int columns;
    private NewGroupIndexer indexer;
    private List<Color> groupBuffer = new ArrayList<Color>();

    public RowAnnotationModel(Dataset data) {
        m_data = data;
        filter = new AnnotationFilter(data);
        rows = filter.getRowAnnotationRows();
        columns = filter.getRowAnnotationColumns();
        header = filter.getRowAnnotationHeader();
        indexer = new NewGroupIndexer(data.getRowGroups());
        annotation = filter.getRowAnnotation();
        showGroups = showGroups();
        showIndexes = showIndexes();
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        int ret = columns;
        if (showIndexes) {
            ret++;
        }
        if (showGroups) {
            ret++;
        }
        return ret;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //debug
        if (columnIndex == 0 && showIndexes) {
            return rowIndex;
        } else if (isGroupColumn(columnIndex)) {
            indexer.fillRowColors(groupBuffer, rowIndex);
            return groupBuffer;
        } else {
            if (showIndexes) {
                return annotation[rowIndex][columnIndex - 1];
            } else {
                return annotation[rowIndex][columnIndex];
            }
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0 && showIndexes) {
            return Integer.class;
        } else if (isGroupColumn(columnIndex)) {
            return List.class;
        } else {
            return String.class;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {

        if (columnIndex == 0 && showIndexes) {
            return "Index";
        } else if (isGroupColumn(columnIndex)) {
            return "Groups";
        } else if (showIndexes) {
            return header[columnIndex - 1];
        } else {
            return header[columnIndex];
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    private boolean isGroupColumn(int columnIndex) {
        if (showIndexes && columnIndex - 1 == header.length) {
            return true;
        } else if (!showIndexes && columnIndex == header.length) {
            return true;
        } else {
            return false;
        }
    }

    private boolean showIndexes() {
        boolean[] vis = m_data.getusedInfos();
        String[] headers = m_data.getInfoHeaders();

        if (vis.length == headers.length + 2) {
            return vis[0];
        } else {
            return true;
        }
    }

    private boolean showGroups() {
        boolean[] vis = m_data.getusedInfos();
        String[] headers = m_data.getInfoHeaders();

        if (vis.length == headers.length + 2) {
            return vis[vis.length - 1];
        } else {
            return true;
        }
    }
}
