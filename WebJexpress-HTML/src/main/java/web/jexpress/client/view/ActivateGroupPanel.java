/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.jexpress.client.view;

import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;

/**
 *
 * @author Yehia
 */
public class ActivateGroupPanel extends Window {
    private final DynamicForm form;
    private final IButton okBtn;
    private final SelectItem selectRowGroups,selectColGroups;
    private final HTML errorlabl;
    public ActivateGroupPanel(DatasetInformation datasetInfo){
        
          this.setWidth(400);
                            this.setHeight(400);
                            this.setTitle("SELECT ACTIVE GROUPS");
                            this.setShowMinimizeButton(false);
                            this.setIsModal(false);   
                            this.centerInPage();
                            this.addCloseClickHandler(new CloseClickHandler() {
                                @Override
                                public void onCloseClick(CloseClickEvent event) {
                                    hide();
                                    destroy();
                                }
                            });
                            
                             final VLayout vlo = new VLayout();
        vlo.setWidth100();
        vlo.setHeight100();
        this.addItem(vlo);
        
                            form = new DynamicForm();
                            form.setHeight(240);
                            form.setWidth100();
                            form.setPadding(5);
                            form.setGroupTitle("Select Groups to Activate");
                            form.setIsGroup(true);
                            form.setLayoutAlign(VerticalAlignment.BOTTOM);
                            

                            selectRowGroups = new SelectItem();
                            selectRowGroups.setTitle("ROW GROUPS (MAX 2)");
                            selectRowGroups.setTextAlign(Alignment.CENTER);
                            selectRowGroups.setTitleAlign(Alignment.CENTER);
                            selectRowGroups.setMultiple(true);
                            selectRowGroups.setMultipleAppearance(MultipleAppearance.GRID);

                            selectColGroups = new SelectItem();
                            selectColGroups.setTitle("COLUMN GROUPS (MAX 2)");
                            selectColGroups.setTextAlign(Alignment.CENTER);
                            selectColGroups.setTitleAlign(Alignment.CENTER);
                            selectColGroups.setMultiple(true);
                            selectColGroups.setMultipleAppearance(MultipleAppearance.GRID);

                            if (datasetInfo != null) {
                                selectRowGroups.setValueMap(datasetInfo.getRowGroupsNamesMap());

                                selectColGroups.setValueMap(datasetInfo.getColGroupsNamesMap());
                            }
                            form.setFields(selectRowGroups, selectColGroups);
                            form.redraw();
                            
                            HLayout hlo = new HLayout();
        hlo.setWidth100();
        hlo.setHeight(20);

        okBtn = new IButton("Activate");
                            okBtn.setAlign(Alignment.CENTER);
                            okBtn.setShowRollOver(true);
                            okBtn.setShowDown(true);
                            okBtn.setTitleStyle("stretchTitle");
        hlo.addMember(okBtn);
        hlo.setMargin(10);
        hlo.setMembersMargin(20);
        hlo.setAlign(Alignment.CENTER);
     

                          
                           

                            HTML infolable = new HTML("<h4 style='color:blue;margin-left: 20px;margin-top: 20px;height=30px;'>*SELECT (ALL GROUP) WILL DEACTIVATE THE REST OF THE GROUPS</h4>");
                            errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=30px;'>YOU CAN NOT SELECT MORE THAN 2 GROUPS</h4>");

                            
                            errorlabl.setVisible(false);
                            vlo.addMember(form);
                              vlo.addMember(hlo);
                            vlo.addMember(infolable);
                             vlo.addMember(errorlabl);
                            vlo.setAlign(Alignment.CENTER);
                            vlo.setMembersMargin(5);
                          
                            vlo.redraw();
                            show();
    
    
    }

    public DynamicForm getForm() {
        return form;
    }

    public IButton getOkBtn() {
        return okBtn;
    }

    public String[] getSelectRowGroups() {
        return selectRowGroups.getValues();
    }

    public String[] getSelectColGroups() {
        return selectColGroups.getValues();
    }

    public HTML getErrorlabl() {
        return errorlabl;
    }
    
}
