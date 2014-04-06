/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.omicstables.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * @author Yehia Farag
 */
public final class OmicsTable extends ModularizedListener implements SelectionChangedHandler, IsSerializable {

    private final ListGrid omicsIdTable ;
    private ListGridRecord[] records;
    private final String info;
    private final SelectionManager selectionManager;
    private ListGrid selectionTable;
    private SelectItem colSelectionTable;
    private boolean mouseSelection = false;

    public void setSelectionTable(ListGrid selectionTable) {
        this.selectionTable = selectionTable;
        selectionChanged(Selection.TYPE.OF_ROWS);
    }

    public OmicsTable(SelectionManager selectionManager, DatasetInformation datasetInfo) {
        this.datasetId = datasetInfo.getId();
        this.records = getRecodList(datasetInfo);
        this.info = datasetInfo.getDatasetInfo();
        this.omicsIdTable = new ListGrid();
        this.selectionManager = selectionManager;
        initGrid(datasetInfo);
        this.classtype = 1;
        this.components.add(OmicsTable.this);
        this.selectionManager.addSelectionChangeListener(datasetId, OmicsTable.this);

        datasetInfo = null;
        selectionChanged(Selection.TYPE.OF_ROWS);

    }

    private void initGrid(DatasetInformation datasetInfo) {
        omicsIdTable.setShowRecordComponents(true);
        omicsIdTable.setShowRecordComponentsByCell(true);
        omicsIdTable.setCanRemoveRecords(false);
        omicsIdTable.setAutoFetchData(true);
        omicsIdTable.setHeight("500px");
        omicsIdTable.setWidth("100%");
        omicsIdTable.setShowAllRecords(false);
        omicsIdTable.setShowRollOver(false);
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
        omicsIdTable.setFields(fields);
        omicsIdTable.setCanResizeFields(true);
        omicsIdTable.setData(records);
        omicsIdTable.setSelectionType(SelectionStyle.MULTIPLE);
        omicsIdTable.setLeaveScrollbarGap(false);
        omicsIdTable.setCanDragSelect(true);
        omicsIdTable.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord[] selectionRecord = omicsIdTable.getSelectedRecords();
                if (selectionTable != null) {
                    selectionTable.setRecords(selectionRecord);
                    selectionTable.redraw();
                }
                updateTableSelection(selectionRecord);
            }
        });
        omicsIdTable.addDragStartHandler(new DragStartHandler() {

            @Override
            public void onDragStart(DragStartEvent event) {
             mouseSelection = true;   
            }
        });

        omicsIdTable.addDragStopHandler(new DragStopHandler() {
            @Override
            public void onDragStop(DragStopEvent event) {               
                ListGridRecord[] selectionRecord = omicsIdTable.getSelectedRecords();
                if (selectionTable != null) {
                    selectionTable.setRecords(selectionRecord);
                    selectionTable.redraw();
                }
                updateTableSelection(selectionRecord);
                mouseSelection = false;
            }
        });
//        omicsIdTable.addSelectionChangedHandler(this);
        omicsIdTable.setCanSort(false);
        records = null;
        
        
        
       
       

    }

    private void updateSelectionManager(int[] selectedIndices) {
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(datasetId, selection, classtype);
    }

    public ListGrid getGwtTable() {
        return omicsIdTable;
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
        if(mouseSelection)//|| event.isCtrlKeyDown() || EventHandler.shiftKeyDown())
                return;
       
        ListGridRecord[] selectionRecord = omicsIdTable.getSelectedRecords();
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
                    omicsIdTable.deselectAllRecords();
                    if (omicsIdTable.getSort().length == 0) {
                        omicsIdTable.selectRecords(selectedRows);
                        if (selectionTable != null) {
                            selectionTable.setRecords(omicsIdTable.getSelectedRecords());
                            selectionTable.redraw();
                        }
                    }
                    omicsIdTable.updateHover();
                    omicsIdTable.scrollToRow(selectedRows[0]);
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
