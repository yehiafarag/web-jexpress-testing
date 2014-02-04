/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.geneTable.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import web.jexpress.client.core.model.SelectionManager;
import web.jexpress.shared.model.core.model.ModularizedListener;
import web.jexpress.shared.model.core.model.Selection;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;

/**
 * @author Yehia Farag
 */
public class GeneTable extends ModularizedListener implements SelectionChangedHandler, IsSerializable {

    private ListGrid geneTable;
    private ListGridRecord[] records;
    private ListGridRecord[] colRecords;
    private String info;
    private SelectionManager selectionManager;
    private ListGrid selectionTable;
//    private ListGrid colSelectionTable;

    public void setSelectionTable(ListGrid selectionTable) {
        this.selectionTable = selectionTable;
    }

    public GeneTable(SelectionManager selectionManager, DatasetInformation datasetInfo) {
        this.datasetId = datasetInfo.getId();
        this.records = getRecodList(datasetInfo);
        this.colRecords = getColRecodList(datasetInfo);
        this.info = datasetInfo.getDatasetInfo();
        this.geneTable = new ListGrid();
        this.selectionManager = selectionManager;
        initGrid(datasetInfo);
        this.classtype = 1;
        this.components.add(GeneTable.this);
        this.selectionManager.addSelectionChangeListener(datasetId, GeneTable.this);
    }

    private void initGrid(DatasetInformation datasetInfo) {
        geneTable.setShowRecordComponents(true);
        geneTable.setShowRecordComponentsByCell(true);
        geneTable.setCanRemoveRecords(false);
        geneTable.setHeight("550px");
        geneTable.setWidth("100%");

        geneTable.setShowAllRecords(true);
        ListGridField[] fields = new ListGridField[(3 + datasetInfo.getRowGroupsNumb())];
        ListGridField nameField = new ListGridField("gene", info);
        nameField.setWidth("50%");
        ListGridField indexField =new ListGridField("index", " ");
        indexField.setWidth("10%");
        fields[0] =indexField;
        fields[1] = (nameField);
        fields[2] = new ListGridField("all", "ALL");
        for (int z = 0; z < datasetInfo.getRowGroupsNumb(); z++) {
            fields[z + 3] = (ListGridField) new ListGridField(datasetInfo.getRowGroupsNames()[z], datasetInfo.getRowGroupsNames()[z].toUpperCase());
        }
        geneTable.setFields(fields);
        geneTable.setCanResizeFields(true);
        geneTable.setData(records);
        geneTable.setSelectionType(SelectionStyle.MULTIPLE);
        geneTable.setLeaveScrollbarGap(false);
        geneTable.setCanDragSelect(true);
        geneTable.draw();
        //geneTable.addSelectionChangedHandler(this);
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
////        geneTable.addDragStartHandler(new DragStartHandler() {
////
////            @Override
////            public void onDragStart(DragStartEvent event) {
////////                 Window.alert("drag start");
////            }
////        });
//        geneTable.addDragCompleteHandler(new DragCompleteHandler() {
//
//            @Override
//            public void onDragComplete(DragCompleteEvent event) {
//            Window.alert("drag complete ");    
//            }
//        });
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

    }

    private void updateSelectionManager(int[] selectedIndices) {

        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(datasetId, selection);

    }

    public ListGrid getGwtTable() {
        return geneTable;
    }

    private ListGridRecord[] getRecodList(DatasetInformation datasetInfo) {

        ListGridRecord[] recordsInit = new ListGridRecord[datasetInfo.getGeneTabelData()[0].length];
        for (int x = 0; x < recordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("gene", datasetInfo.getGeneTabelData()[0][x]);
            record.setAttribute("all", "<font color = " + "#000000" + " >" + "&diams;" + "</font>");
            for (int c = 0; c < datasetInfo.getRowGroupsNames().length; c++) {
                record.setAttribute(datasetInfo.getRowGroupsNames()[c], "<font color = " + datasetInfo.getGeneTabelData()[c + 2][x] + " >" + "&diams;" + "</font>");

            }
//            record.setAttribute(datasetInfo.getRowGroupsNames()[0], "<font color = " + datasetInfo.getGeneTabelData()[2][x] + " >" + "&diams;" + "</font>");
//            record.setAttribute(datasetInfo.getRowGroupsNames()[1], "<font color = " + datasetInfo.getGeneTabelData()[3][x] + " >" + "&diams;" + "</font>");
            record.setAttribute("index", x);
            recordsInit[x] = record;
        }
        //datasetInfo.setGeneTabelData(null);
        return recordsInit;
    }

    private ListGridRecord[] getColRecodList(DatasetInformation datasetInfo) {
        ListGridRecord[] colRecordsInit = new ListGridRecord[datasetInfo.getColsNames().length];
        for (int x = 0; x < colRecordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("col", datasetInfo.getColsNames()[x]);
            colRecordsInit[x] = record;
        }
        return colRecordsInit;
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
                    } else {
                        //update sorted rows
                    }
                    geneTable.updateHover();
                    geneTable.scrollToRow(selectedRows[0]);
                }
            }
        } 
//        else{
//            Selection sel = selectionManager.getSelectedColumns(datasetId);
//            if (sel != null) {
//                int[] selectedColumn = sel.getMembers();
//                //update table selection             
//                if (selectedColumn != null && selectedColumn.length != 0) {
//                    ListGridRecord[] selectedRec = new ListGridRecord[selectedColumn.length];
//                    for (int x = 0; x < selectedColumn.length; x++) {
//                        selectedRec[x] = colRecords[selectedColumn[x]];
//                    }
//                    if (colSelectionTable != null && selectedRec != null && selectedRec.length != 0) {
//                        colSelectionTable.setRecords(selectedRec);
//                        colSelectionTable.redraw();
//                    }
//                }
//            }
//        }
    }

//    public void setColSelectionTable(ListGrid colSelectionTable) {
//        this.colSelectionTable = colSelectionTable;
//    }
}
