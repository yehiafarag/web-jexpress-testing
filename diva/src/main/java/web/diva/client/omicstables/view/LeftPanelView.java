/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.diva.client.omicstables.view;

import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import java.util.LinkedHashMap;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 *
 * @author Yehia Farag
 */
public class LeftPanelView extends SectionStack {
    
    private final ListGrid selectionTable;
    private final SelectItem colSelectionTable;
    
    public LeftPanelView(SelectionManager selectionManager, DatasetInformation datasetInfo) {
        this.setVisibilityMode(VisibilityMode.MUTEX);        
        
        this.setWidth((RootPanel.get("geneTable").getOffsetWidth()));
        this.setHeight(580);
        this.setScrollSectionIntoView(true);
        
        SectionStackSection section1 = new SectionStackSection(datasetInfo.getDatasetInfo());
        section1.setExpanded(true);
        
        OmicsTable geneTable = new OmicsTable(selectionManager, datasetInfo);        
        section1.addItem(geneTable.getGwtTable());        
        
        SectionStackSection section2 = new SectionStackSection("Row Selections");
        section2.setExpanded(false);
        selectionTable = initRowSelectionTable(datasetInfo);        
        section2.addItem(selectionTable);        
        geneTable.setSelectionTable(selectionTable);
        
        SectionStackSection section3 = new SectionStackSection("Column Selections");
        section3.setExpanded(false);
        colSelectionTable = initColSelectionTable(datasetInfo.getColNamesMap());
        DynamicForm form = new DynamicForm();
        form.setItems(colSelectionTable);
        form.setWidth((RootPanel.get("geneTable").getOffsetWidth()));
        form.setHeight(400);
        section3.addItem(form);        
        geneTable.setColSelectionTable(colSelectionTable);
        
        this.addSection(section3);        
        this.addSection(section2);        
        this.addSection(section1);        
        this.redraw();
        datasetInfo = null;
        
    }
    
    private ListGrid initRowSelectionTable(DatasetInformation datasetInfo) {
        ListGrid geneTable = new ListGrid();
        geneTable.setShowRecordComponents(true);
        geneTable.setShowRecordComponentsByCell(true);
        geneTable.setCanRemoveRecords(false);
        geneTable.setHeight("500px");
        geneTable.setWidth("100%");
        geneTable.setShowAllRecords(false);
        geneTable.setShowRollOver(false);
        ListGridField[] fields = new ListGridField[(3 + datasetInfo.getRowGroupsNumb())];
        ListGridField nameField = new ListGridField("gene", datasetInfo.getDatasetInfo());
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
            temLGF.setIcon(datasetInfo.getOmicsTabelData()[z + 2][0]);
            fields[z + 3] = temLGF;
        }
        geneTable.setFields(fields);
        geneTable.setCanResizeFields(true);
        geneTable.setCanSort(false);
        geneTable.setSelectionType(SelectionStyle.NONE);
        geneTable.setLeaveScrollbarGap(false);
        geneTable.setCanDragSelect(false);
        return geneTable;
    }
    
    private SelectItem initColSelectionTable(LinkedHashMap<String, String> colNamesMap) {
        SelectItem selectCols = new SelectItem();
        selectCols.setTitle(" Selected Columns ");
        selectCols.setWidth(RootPanel.get("geneTable").getOffsetWidth());
        selectCols.setTextAlign(Alignment.CENTER);
        selectCols.setTitleAlign(Alignment.CENTER);
        selectCols.setTitleOrientation(TitleOrientation.TOP);
        selectCols.setMultiple(true);        
        selectCols.setMultipleAppearance(MultipleAppearance.GRID);        
        selectCols.setValueMap(colNamesMap);
        selectCols.disable();
        
        return selectCols;
    }
    
    public ListGrid getSelectionTable() {
        return selectionTable;
    }
    
    public SelectItem getColSelectionTable() {
        return colSelectionTable;
    }
    
}
