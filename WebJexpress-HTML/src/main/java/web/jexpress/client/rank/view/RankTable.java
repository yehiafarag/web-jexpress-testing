/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.rank.view;

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
//import web.jexpress.shared.model.core.model.SelectionManager;

/**
 *
 * @author Y.M
 */
public class RankTable extends ModularizedListener implements SelectionChangedHandler, IsSerializable {

     @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows(datasetId);
            if (sel != null) {
                //  RootPanel.get().add(new Label("selection values are( local central manager is working )"));
                int[] selectedRows = new int[sel.getMembers().length];
                for(int x=0;x<selectedRows.length;x++)
                    selectedRows[x] = (indexToRank[sel.getMembers()[x]]-1);
                

                //update table selection             

                if (selectedRows != null && selectedRows.length != 0) {
                    table.deselectAllRecords();
//                    hr.removeHandler();
                    if (table.getSort().length == 0) {
                        table.selectRecords(selectedRows);
                    } else {
                        //update sorted rows
                    }
                    table.updateHover();
                    table.scrollToRow(selectedRows[0]);
//                    geneTable.addSelectionChangedHandler(this);

                }
            }
        }
    }

    @Override
    public void onSelectionChanged(SelectionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private ListGrid table;
    private ListGridRecord[] records;
    private SelectionManager selectionManager;
    private int[] rankToIndex, indexToRank;
    public RankTable(SelectionManager selectionManager,int datasetId,String[]headers,String[][] tableData,int[] rankToIndex,int[] indexToRank) {
        this.datasetId = datasetId;
        this.records = getRecodList(tableData,headers);
        this.table = new ListGrid();
        this.indexToRank = indexToRank;
        this.rankToIndex = rankToIndex;
//        this.dataset = dataset;
        this.selectionManager = selectionManager;
        initGrid(headers);
        this.classtype = 1;
        this.components.add(RankTable.this);
        this.selectionManager.addSelectionChangeListener(datasetId, RankTable.this);
    }
    
    private void initGrid(String[]headers) {
        table.setShowRecordComponents(true);
        table.setShowRecordComponentsByCell(true);
        table.setCanRemoveRecords(false);
        table.setHeight("260px");
        table.setShowAllRecords(true);
        table.setCanSort(Boolean.FALSE);
        ListGridField[] fields = new ListGridField[headers.length];
        //ListGridField nameField = new ListGridField("gene", info);
       // nameField.setWidth("50%");
        //fields[0] = new ListGridField("index", "index");
        //fields[1] = (nameField);
        //fields[2] = new ListGridField("all", "ALL");
        for (int z = 0; z < headers.length; z++) {
          ListGridField l = (ListGridField) new ListGridField(headers[z],headers[z].toUpperCase());
          if(z==1)
              l.setWidth("35%");
         fields[z] = l; 
                 }
        table.setFields(fields);
        table.setCanResizeFields(true);
        
        table.setData(records);
        table.setSelectionType(SelectionStyle.MULTIPLE);
        table.setLeaveScrollbarGap(false);
        table.setCanDragSelect(true);
        table.draw();
        //geneTable.addSelectionChangedHandler(this);
        table.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ListGridRecord[] selectionRecord = table.getSelectedRecords();
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
        table.addDragStopHandler(new DragStopHandler() {
            @Override
            public void onDragStop(DragStopEvent event) {
                 ListGridRecord[] selectionRecord = table.getSelectedRecords();
                updateTableSelection(selectionRecord);
            }
        });
    }
    
     private void updateTableSelection( ListGridRecord[] selectionRecord)
    {
                if (selectionRecord.length > 0) {

            int[] selectedIndices = new int[selectionRecord.length];
//            String str = "";
            for (int index = 0; index < selectionRecord.length; index++) {
                ListGridRecord rec = selectionRecord[index];
                selectedIndices[index] = rankToIndex[Integer.valueOf(rec.getAttributeAsInt("Rank"))-1];
//                str+= selectedIndices[index]+" , "+rec.getAttribute("gene");
                //selectedIndices[index] = Integer.valueOf(dataset.getGeneNameIndexMap().get(selectionRecord[index].getAttribute("gene")));
            }
                updateSelectionManager(selectedIndices);

//            RootPanel.get().add(new Label("after updated >> "+str));

        }
    
    
    
    }

    
    private ListGridRecord[] getRecodList(String[][] tableData, String[] headers) {


        ListGridRecord[] recordsInit = new ListGridRecord[tableData[0].length];

        for (int x = 0; x < recordsInit.length; x++) {
            ListGridRecord record = new ListGridRecord();
            for (int y = 0; y < headers.length; y++) {
                record.setAttribute(headers[y], tableData[y][x]);
            }
            recordsInit[x] = record;

        }
        
        //datasetInfo.setGeneTabelData(null);
        return recordsInit;
    }
    public ListGrid getTable() {
        return table;
    }
    
    
    private void updateSelectionManager(int[] selectedIndices) {

        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selectedIndices);
        selectionManager.setSelectedRows(datasetId, selection);

    }
    
}
