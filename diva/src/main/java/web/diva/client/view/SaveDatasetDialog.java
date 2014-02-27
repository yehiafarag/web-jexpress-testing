/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.diva.client.view;

import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * @author Yehia
 */
public class SaveDatasetDialog extends Window{

    private final TextItem name;
     private final IButton okBtn;

    public DynamicForm getForm() {
        return form;
    }
     private final DynamicForm form ;

    public IButton getOkBtn() {
        return okBtn;
    }

    public HTML getErrorlabl() {
        return errorlabl;
    }
       private final HTML errorlabl;

    public String getName() {
        return name.getValueAsString();
    }
    public SaveDatasetDialog() {
        
        this.setWidth(300);
        this.setHeight(200);
        this.setTitle("Save Dataset");
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
        form.setIsGroup(false);
        form.setWidth100();
        form.setPadding(5);
        form.setLayoutAlign(VerticalAlignment.BOTTOM);
        name = new TextItem();
        name.setTitle("Dataset Name");
        name.setRequired(true);
        form.setFields(name);
        vlo.addMember(form);
        
         HLayout hlo = new HLayout();
        hlo.setWidth100();
        hlo.setHeight(20);

        okBtn = new IButton("OK");
        hlo.addMember(okBtn);
        hlo.setMargin(10);
        hlo.setMembersMargin(20);
        hlo.setAlign(Alignment.CENTER);
        vlo.addMember(hlo);

        errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=20px;'>PLEASE CHECK YOUR DATA INPUT  </h4>");
        errorlabl.setVisible(false);
        vlo.addMember(errorlabl);
          vlo.redraw();
        this.show();

    }
    
}
