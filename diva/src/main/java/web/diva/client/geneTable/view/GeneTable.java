/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.geneTable.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import web.diva.shared.ModularizedListener;
import web.diva.shared.Selection;
import web.diva.shared.SelectionManager;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * @author Yehia Farag
 */
public final class GeneTable extends ModularizedListener implements SelectionChangedHandler, IsSerializable {

    private final ListGrid geneTable;
    private ListGridRecord[] records;
    private final String info;
    private final SelectionManager selectionManager;
    private ListGrid selectionTable;
    private SelectItem colSelectionTable;

    public void setSelectionTable(ListGrid selectionTable) {
        this.selectionTable = selectionTable;
        selectionChanged(Selection.TYPE.OF_ROWS);
    }

    public GeneTable(SelectionManager selectionManager, DatasetInformation datasetInfo) {
        this.datasetId = datasetInfo.getId();
        this.records = getRecodList(datasetInfo);
        this.info = datasetInfo.getDatasetInfo();
        this.geneTable = new ListGrid();
        this.selectionManager = selectionManager;
        initGrid(datasetInfo);
        this.classtype = 1;
        this.components.add(GeneTable.this);
        this.selectionManager.addSelectionChangeListener(datasetId, GeneTable.this);

        datasetInfo = null;
        selectionChanged(Selection.TYPE.OF_ROWS);

    }

    private void initGrid(DatasetInformation datasetInfo) {
        geneTable.setShowRecordComponents(true);
        geneTable.setShowRecordComponentsByCell(true);
        geneTable.setCanRemoveRecords(false);
        geneTable.setAutoFetchData(true);
        geneTable.setHeight("500px");
        geneTable.setWidth("100%");
        geneTable.setShowAllRecords(false);
        geneTable.setShowRollOver(false);
        ListGridField[] fields = new ListGridField[(3 + datasetInfo.getRowGroupsNumb())];
        ListGridField nameField = new ListGridField("gene", info);
        nameField.setWidth("30%");
        nameField.setType(ListGridFieldType.TEXT);
        ListGridField indexField = new ListGridField("index", " ");
        indexField.setWidth("50px");
        indexField.setType(ListGridFieldType.INTEGER);

        ListGridField allField = new ListGridField("all", "ALL");
        allField.setWidth("8px");
        allField.setIconVAlign("center");

        allField.setType(ListGridFieldType.ICON);
        allField.setIcon("../images/b.png");

        fields[0] = indexField;
        fields[1] = (nameField);
        fields[2] = allField;
        for (int z = 0; z < datasetInfo.getRowGroupsNumb(); z++) {
            ListGridField temLGF = new ListGridField(datasetInfo.getRowGroupsNames()[z][0], datasetInfo.getRowGroupsNames()[z][0].toUpperCase());
            temLGF.setWidth("8px");
            temLGF.setIconVAlign("center");
            temLGF.setType(ListGridFieldType.IMAGE);
            temLGF.setIcon(datasetInfo.getRowGroupsNames()[z][1]);
            fields[z + 3] = temLGF;
        }
        geneTable.setFields(fields);
        geneTable.setCanResizeFields(true);
        geneTable.setData(records);
        geneTable.setSelectionType(SelectionStyle.MULTIPLE);
        geneTable.setLeaveScrollbarGap(false);
        geneTable.setCanDragSelect(true);
        geneTable.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord[] selectionRecord = geneTable.getSelectedRecords();
                if (selectionTable != null) {
                    selectionTable.setRecords(selectionRecord);
                    selectionTable.redraw();
                }
                updateTableSelection(selectionRecord);
            }
        });

        geneTable.addDragStopHandler(new DragStopHandler() {
            @Override
            public void onDragStop(DragStopEvent event) {
                ListGridRecord[] selectionRecord = geneTable.getSelectedRecords();
                if (selectionTable != null) {
                    selectionTable.setRecords(selectionRecord);
                    selectionTable.redraw();
                }
                updateTableSelection(selectionRecord);
            }
        });
        geneTable.setCanSort(false);
        records = null;

    }

    private void updateSelectionManager(int[] selectedIndices) {
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(datasetId, selection, classtype);
    }

    public ListGrid getGwtTable() {
        return geneTable;
    }

    private ListGridRecord[] getRecodList(DatasetInformation datasetInfo) {

        ListGridRecord[] recordsInit = new ListGridRecord[datasetInfo.getGeneTabelData()[0].length];
        for (int x = 0; x < recordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("gene", datasetInfo.getGeneTabelData()[0][x]);
            for (int c = 0; c < datasetInfo.getRowGroupsNames().length; c++) {
                if (!datasetInfo.getGeneTabelData()[c + 2][x].equalsIgnoreCase("#FFFFFF")) {
                    record.setAttribute(datasetInfo.getRowGroupsNames()[c][0], datasetInfo.getGeneTabelData()[c + 2][x]);//  record.setAttribute(datasetInfo.getRowGroupsNames()[c], "b");// "<font color = " + datasetInfo.getGeneTabelData()[c + 2][x] + " >" + "&diams;" + "</font>");
                }
            }
            record.setAttribute("index", x);
            recordsInit[x] = record;
        }
        return recordsInit;
    }

    @Override
    public void onSelectionChanged(SelectionEvent event) {
        ListGridRecord[] selectionRecord = event.getSelection();
        if (selectionRecord.length > 0) {
            int[] selectedIndices = new int[selectionRecord.length];
            for (int index = 0; index < selectionRecord.length; index++) {
                ListGridRecord rec = selectionRecord[index];
                selectedIndices[index] = Integer.valueOf(rec.getAttributeAsInt("index"));
            }
            updateSelectionManager(selectedIndices);
        }
    }

    private void updateTableSelection(ListGridRecord[] selectionRecord) {
        if (selectionRecord.length > 0) {
            int[] selectedIndices = new int[selectionRecord.length];
            for (int index = 0; index < selectionRecord.length; index++) {
                ListGridRecord rec = selectionRecord[index];
                selectedIndices[index] = Integer.valueOf(rec.getAttributeAsInt("index"));
            }
            updateSelectionManager(selectedIndices);
        }

    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows(datasetId);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                //update table selection             
                if (selectedRows != null && selectedRows.length != 0) {
                    geneTable.deselectAllRecords();
                    if (geneTable.getSort().length == 0) {
                        geneTable.selectRecords(selectedRows);
                        if (selectionTable != null) {
                            selectionTable.setRecords(geneTable.getSelectedRecords());
                            selectionTable.redraw();
                        }
                    }
                    geneTable.updateHover();
                    geneTable.scrollToRow(selectedRows[0]);
                }
            }
        } else if (type == Selection.TYPE.OF_COLUMNS) {
            Selection sel = selectionManager.getSelectedColumns(datasetId);
            if (sel != null) {
                int[] selectedColumn = sel.getMembers();
                //update table selection             
                if (selectedColumn != null && selectedColumn.length != 0 && colSelectionTable != null) {
                    String[] values = new String[selectedColumn.length];
                    for (int x = 0; x < selectedColumn.length; x++) {
                        values[x] = "" + selectedColumn[x];
                    }

                    colSelectionTable.setValues(values);
                    colSelectionTable.redraw();
                }
            }
        }
    }

    public void setColSelectionTable(SelectItem colSelectionTable) {
        this.colSelectionTable = colSelectionTable;
        selectionChanged(Selection.TYPE.OF_COLUMNS);
    }
}