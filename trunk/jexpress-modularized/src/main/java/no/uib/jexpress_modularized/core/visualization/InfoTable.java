
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
package no.uib.jexpress_modularized.core.visualization;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import no.uib.jexpress_modularized.core.dataset.Dataset;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import no.uib.jexpress_modularized.core.dataset.AnnotationFilter;
import no.uib.jexpress_modularized.core.dataset.ColumnAnnotationModel;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.core.model.Selection.TYPE;
import no.uib.jexpress_modularized.core.model.SelectionManager;
import no.uib.jexpress_modularized.core.dataset.RowAnnotationModel;

/**
 * @author Yehia Farag This infoTable has almost the same properties as the
 * InfoTable, member indices. <I>It is used in the TableComponent.</I>
 */
public class InfoTable extends JTable implements Serializable {

    private Dataset m_data;
    private boolean m_ROWS;
    private HashSet m_FILTER;
    private RowFilter<Object, Object> m_RowFilter;
    public TableRowSorter m_sorter;
    private boolean m_SelectionsUpdating;
    private boolean selfSelection;
    private TableModel m_model;
    private boolean noMembers = false;

    public InfoTable() {
    }

    @Override
    public TableRowSorter getRowSorter() {
        return m_sorter;
    }

    public HashSet getMembers() {
        return m_FILTER;
    }

    public void setMembers(HashSet filter) {
        m_FILTER = filter;
        if (filter.isEmpty()) {
            noMembers = true;
        } else {
            noMembers = false;
        }
        setupFilter();
        setupDefaults();
    }

    private void setupFilter() {
        m_RowFilter = new RowFilter<Object, Object>() {
            @Override
            public boolean include(RowFilter.Entry<? extends Object, ? extends Object> entry) {
                if (noMembers) {
                    return false;
                }
                if (m_FILTER == null || m_FILTER.contains(entry.getIdentifier())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    public void setData(Dataset dta, HashSet filter) {
        this.m_data = dta;
        m_FILTER = filter;
        setData(dta);
    }

    @Override
    public void setFont(java.awt.Font f) {
        super.setFont(f);
    }

    public void setUseColor(boolean b) {
    }

    public void setData(Dataset dta) {
        this.m_data = dta;
        this.getSelectionModel().setValueIsAdjusting(true);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setupDefaultModel(true);
        this.getSelectionModel().setValueIsAdjusting(false);
    }

    public void setupColumnModel(boolean transposed) {
    }

    public void setWay(boolean Columns) {
        setupDefaultModel(!Columns);
    }

    @Override
    public int[] getSelectedRows() {
        int[] index = getSelectedSortedRows();
        return index;
    }

    public int[] getSelectedSortedRows() {
        if (m_sorter == null) {
            return super.getSelectedRows();
        } else {
            int[] sel = super.getSelectedRows();
            int[] ret = new int[sel.length];
            for (int i = 0; i < sel.length; i++) {
                ret[i] = m_sorter.convertRowIndexToModel(sel[i]);
            }
            return ret;
        }
    }

    public void fireSelectionEvent() {
    }

    public void invertSelection() {

        int[] sel = this.getSelectedRows();
        boolean[] selected = new boolean[this.getRowCount()];
        for (int s : sel) {
            selected[s] = true;
        }
        List<Integer> newSel = new ArrayList<Integer>();

        getSelectionModel().clearSelection();
        for (int i = 0; i < selected.length; i++) {
            if (!selected[i]) {
                getSelectionModel().addSelectionInterval(i, i);
            }
        }
    }

    public void setRowSorter(TableRowSorter<?> sorter) {
        super.setRowSorter(sorter);
        m_sorter = sorter;
    }

//The actual rows in the data that has been selected in this table..
    public boolean[] getDataSelection() {
        boolean[] ret;
        if (m_ROWS) {
            ret = new boolean[m_data.getDataLength()];
        } else {
            ret = new boolean[m_data.getDataWidth()];
        }
        for (int in : this.getSelectedRows()) {
            int index = m_sorter.convertRowIndexToModel(in);
            ret[index] = true;
        }
        return ret;
    }

    private void setupDefaultModel(boolean ROW) {
        m_ROWS = ROW;

        if (m_ROWS) {
            m_model = new RowAnnotationModel(m_data);
        } else {
            m_model = new ColumnAnnotationModel(m_data);
        }

        setModel(m_model);
        setupDefaults();

    }

    public void setupDefaults() {
        if (m_ROWS && AnnotationFilter.showRowIndexes(m_data)) {
            this.getColumnModel().getColumn(0).setPreferredWidth(50);
            this.getColumnModel().getColumn(0).setWidth(50);
        } else if (!m_ROWS && AnnotationFilter.showColumnIndexes(m_data)) {
            this.getColumnModel().getColumn(0).setWidth(50);
        }

        GroupColorRenderer groupRenderer = new GroupColorRenderer();
        setDefaultRenderer(List.class, groupRenderer);

        m_sorter = new TableRowSorter(m_model);
        setRowSorter(m_sorter);


        RowFilter<Object, Object> searchFilter = new RowFilter<Object, Object>() {
            @Override
            public boolean include(RowFilter.Entry<? extends Object, ? extends Object> entry) {

                if (noMembers) {
                    return false;
                }
                if (m_FILTER == null || m_FILTER.isEmpty() || m_FILTER.contains(entry.getIdentifier())) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        m_sorter.setRowFilter(searchFilter);

        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            /**
             * If selections change on this table, then this method notifies the
             * SelectionManager so that other components that are listening on
             * changes on the same DataSet are notified. The method doesn't
             * notify the SelectionManager if the method is called due to a
             * selection that this class made itself through the public method
             * changeSelection(int[] rowsToSelect).
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (m_SelectionsUpdating || e.getValueIsAdjusting() || selfSelection) {
                    return;
                }

                m_SelectionsUpdating = true;

                int[] sel = getSelectedRows();


                if (m_ROWS) {
                    Selection selRows = new Selection(TYPE.OF_ROWS, sel);
                    SelectionManager.getSelectionManager().setSelectedRows(m_data, selRows);
                } else {
                    //NOTE: do nothing if selection is OF_COLUMNS?
                }

                m_SelectionsUpdating = false;
            }
        });


    }

    /**
     * Selects all the given rows in the table. If any rows are selected prior
     * to calling this method, they will not be deselected. </p> <b>Example</b>:
     * </p> If the contents of
     * <code>rowsToSelect</code> consists of the elements { 0, 1, 2, 3 }, the
     * rows numbered 0, 1, 2, 3 in the table will be selected.
     *
     * @param rowsToSelect an array of all the rows which should be selected
     */
    public void changeSelection(int[] rowsToSelect) {
        int[] ret = new int[rowsToSelect.length];

        for (int x = 0; x < rowsToSelect.length; x++) {
            int in = rowsToSelect[x];
            int index = m_sorter.convertRowIndexToView(in);
            ret[x] = index;
        }

        for (int i = 0; i < rowsToSelect.length; i++) {
            changeSelection(ret[i], ret[i], true, false);
        }


    }

    public void createGroup() {

        int[] ret = getSelectedSortedRows();
        if (ret == null && ret.length == 0)
            ; else {
            Selection selRows = new Selection(TYPE.OF_ROWS, ret);
            Group g1 = new Group("Created Group " + (this.m_data.getRowGroups().size() + 1), generatRandColor(), new Selection(Selection.TYPE.OF_ROWS, ret));
            g1.setActive(true);
            this.m_data.addRowGroup(g1);
            SelectionManager.getSelectionManager().setSelectedRows(m_data, selRows);

        }

    }

    private Color generatRandColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;
    }

    /**
     * Set whether or not the selections on this table is currently undergoing a
     * self selection. A self selection is a selection that was initiated
     * through the method changeSelection(int[] rowsToSelect); i.e. not a
     * selection that was done by something like a user through a MouseEvent.
     * </p>
     * This can be used by other components that want to modify the selection of
     * rows on the table; One can set this property to true before one updates
     * the selection on this table, and then set this property to false when one
     * is done. This way one avoids redundant ListSelectionEvents to be created,
     * since this class (JTable, to be precise) doesn't seem to differentiate
     * between programmatic changes that are made to this table, and changes
     * that are made to the table directly by users (such as by some
     * MouseEvent).
     *
     * @param isUpdating true if the table of this class is (or is about) to be
     * programmatically changed, false otherwise
     */
    //NOTE: it would probably make more sense for this class to use this field directly, like the mentioned changeSelection
    //method: but this doesn't seem to work. Frankly, I don't know why. Maybe it has to do with the flag being put on and off 
    //too fast for it to have an effect, since the events might be fired in another GUI-managed thread: so that the main-thread
    //sets the flag to off before all the calls to the method valueChanged have completed?
    public void setSelfSelection(boolean isSelfSelection) {
        selfSelection = isSelfSelection;
    }
}
