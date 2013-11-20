/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.geneTable.view;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
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
import web.jexpress.shared.model.core.model.ModularizedListener;
import web.jexpress.shared.model.core.model.Selection;
import web.jexpress.shared.model.core.model.SelectionManager;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;

/**
 * @author Yehia Farag
 */
public class GeneTable extends ModularizedListener implements SelectionChangedHandler, IsSerializable {

    private ListGrid geneTable;
    private ListGridRecord[] records;
    private String info;
    private SelectionManager selectionManager;

    public GeneTable(SelectionManager selectionManager, DatasetInformation datasetInfo) {
        this.datasetId = datasetInfo.getId();
        this.records = getRecodList(datasetInfo);
        this.info = datasetInfo.getDatasetInfo();
        this.geneTable = new ListGrid();
//        this.dataset = dataset;
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
        geneTable.setHeight("700px");

        geneTable.setShowAllRecords(true);
        ListGridField[] fields = new ListGridField[(3 + datasetInfo.getRowGroupsNumb())];
        ListGridField nameField = new ListGridField("gene", info);
        nameField.setWidth("50%");
        fields[0] = new ListGridField("index", "index");
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
            record.setAttribute(datasetInfo.getRowGroupsNames()[0], "<font color = " + datasetInfo.getGeneTabelData()[2][x] + " >" + "&diams;" + "</font>");
            record.setAttribute(datasetInfo.getRowGroupsNames()[1], "<font color = " + datasetInfo.getGeneTabelData()[3][x] + " >" + "&diams;" + "</font>");
            record.setAttribute("index", x);
            recordsInit[x] = record;
        }
        //datasetInfo.setGeneTabelData(null);
        return recordsInit;
    }

    @Override
    public void onSelectionChanged(SelectionEvent event) {

        ListGridRecord[] selectionRecord = event.getSelection();

        //manualSelection = true;
        if (selectionRecord.length > 0) {

            int[] selectedIndices = new int[selectionRecord.length];
//            String str = "";
            for (int index = 0; index < selectionRecord.length; index++) {
                ListGridRecord rec = selectionRecord[index];
                selectedIndices[index] = Integer.valueOf(rec.getAttributeAsInt("index"));
//                str+= selectedIndices[index]+" , "+rec.getAttribute("gene");
                //selectedIndices[index] = Integer.valueOf(dataset.getGeneNameIndexMap().get(selectionRecord[index].getAttribute("gene")));
            }
                updateSelectionManager(selectedIndices);

//            RootPanel.get().add(new Label("after updated >> "+str));

        }
    }
    
    private void updateTableSelection( ListGridRecord[] selectionRecord)
    {
                if (selectionRecord.length > 0) {

            int[] selectedIndices = new int[selectionRecord.length];
//            String str = "";
            for (int index = 0; index < selectionRecord.length; index++) {
                ListGridRecord rec = selectionRecord[index];
                selectedIndices[index] = Integer.valueOf(rec.getAttributeAsInt("index"));
//                str+= selectedIndices[index]+" , "+rec.getAttribute("gene");
                //selectedIndices[index] = Integer.valueOf(dataset.getGeneNameIndexMap().get(selectionRecord[index].getAttribute("gene")));
            }
                updateSelectionManager(selectedIndices);

//            RootPanel.get().add(new Label("after updated >> "+str));

        }
    
    
    
    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows(datasetId);
            if (sel != null) {
                //  RootPanel.get().add(new Label("selection values are( local central manager is working )"));
                int[] selectedRows = sel.getMembers();

                //update table selection             

                if (selectedRows != null && selectedRows.length != 0) {
                    geneTable.deselectAllRecords();
//                    hr.removeHandler();
                    if (geneTable.getSort().length == 0) {
                        geneTable.selectRecords(selectedRows);
                    } else {
                        //update sorted rows
                    }
                    geneTable.updateHover();
                    geneTable.scrollToRow(selectedRows[0]);
//                    geneTable.addSelectionChangedHandler(this);

                }
            }
        }
    }
}
