/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.jexpress.client.rank.view;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;

/**
 *
 * @author Yehia
 */
public class RankPanel extends Window {

    private final SelectItem selectColGroups;
    private final RadioGroupItem radioGroupItem;
    private final TextItem permutation,seed;
    private final  IButton okBtn;
    private final HTML errorlabl;
    private final DynamicForm form2;

    public DynamicForm getForm2() {
        return form2;
    }

    public HTML getErrorlabl() {
        return errorlabl;
    }

  
    public RankPanel(DatasetInformation datasetInfo) {
        this.setWidth(400);
        this.setHeight(400);
        this.setTitle("Differential Expression");
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

        VLayout vlo = new VLayout();
        vlo.setWidth100();
        vlo.setHeight100();
        this.addItem(vlo);

        DynamicForm form = new DynamicForm();
        form.setHeight(150);
        form.setIsGroup(true);
        form.setWidth100();
        form.setPadding(5);
        form.setLayoutAlign(VerticalAlignment.BOTTOM);

        selectColGroups = new SelectItem();
        selectColGroups.setTitle("COLUMN GROUPS (MAX 2)");
        selectColGroups.setTextAlign(Alignment.CENTER);
        selectColGroups.setTitleAlign(Alignment.CENTER);
        selectColGroups.setMultiple(true);
        selectColGroups.setMultipleAppearance(MultipleAppearance.GRID);
        if (datasetInfo != null) {
            selectColGroups.setValueMap(datasetInfo.getColGroupsNamesMap());
        }
        radioGroupItem = new RadioGroupItem();
        radioGroupItem.setTitle("Values");        
        radioGroupItem.setValueMap("Log 2", "Linear");
        radioGroupItem.setValue("Log 2");
        radioGroupItem.setShouldSaveValue(true);
        

        form2 = new DynamicForm();
        form2.setGroupTitle("Permutations");
        form2.setIsGroup(true);
        permutation = new TextItem();
        permutation.setTitle("Permutation");
        permutation.setBrowserInputType("digits");
        permutation.setRequired(true);
        
        permutation.setValidators(new IsIntegerValidator());
        permutation.setValue(400);
        
        seed = new TextItem();
        seed.setTitle("Seed");
        seed.setRequired(true);
        seed.setBrowserInputType("digits");
        seed.setValue(Random.nextInt(1000000001));

        form.setFields(selectColGroups, radioGroupItem);
        form2.setFields(permutation,seed);
        form.redraw();
        form2.redraw();
        vlo.addMember(form);
        vlo.addMember(form2);
        
        HLayout hlo = new HLayout();
        hlo.setWidth100();
        hlo.setHeight(20);
        

        IButton newSeedBtn = new IButton("Create new seed");
        hlo.addMember(newSeedBtn);

        okBtn = new IButton("Process");
        hlo.addMember(okBtn);
        hlo.setMargin(10);
        hlo.setMembersMargin(20);
        hlo.setAlign(Alignment.CENTER);        
        vlo.addMember(hlo);
        
        errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=20px;'>PLEASE CHECK YOUR DATA INPUT .. PLEASE NOTE THAT YOU CAN NOT SELECT MORE THAN 2 GROUPS</h4>");
        errorlabl.setVisible(false);
        vlo.addMember(errorlabl);
        this.show();
        
        
        newSeedBtn.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                   seed.setValue(Random.nextInt(1000000001));//(int) ran.nextInt(1000000001)
                                 }
                            });
    
    
    }

    public String[] getSelectColGroups() {
        return selectColGroups.getValues();
    }

    public String getRadioGroupItem() {
        return radioGroupItem.getValueAsString();
    }

    public String getPermutation() {
        return permutation.getValueAsString();
    }

    public String getSeed() {
        return seed.getValueAsString();
    }

    public IButton getOkBtn() {
        return okBtn;
    }
    
}
