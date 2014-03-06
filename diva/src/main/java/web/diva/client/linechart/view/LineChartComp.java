/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.linechart.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import web.diva.client.GreetingServiceAsync;
import web.diva.shared.ModularizedListener;
import web.diva.shared.Selection;
import web.diva.shared.SelectionManager;
import web.diva.shared.beans.LineChartResults;

/**
 *
 * @author Yehia Farag
 */
public class LineChartComp extends ModularizedListener {
    
    private SelectionManager selectionManager;
    
    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows(datasetId);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                this.updateSelection(selectedRows);
            }
        }
    }

    private VerticalPanel layout;
     private Image image;
     private final GreetingServiceAsync greetingService;
    
    public LineChartComp(LineChartResults results, SelectionManager selectionManager,GreetingServiceAsync greetingService) {
        this.greetingService =  greetingService;
        try {
            
            this.classtype = 3;
            this.datasetId = results.getDatasetId();
            this.components.add(LineChartComp.this);
            this.selectionManager = selectionManager;
            this.selectionManager.addSelectionChangeListener(datasetId, LineChartComp.this);
            
            layout = new VerticalPanel();
            layout.setHeight("300px");
            layout.setWidth("" + RootPanel.get("LineChartResults").getOffsetWidth() + "px");
            layout.setBorderWidth(1);
          
            
            Selection sel = selectionManager.getSelectedRows(datasetId);            
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                this.updateSelection(selectedRows);
            }else{
                initImage(results.getImageString());
            }            
            
        } catch (Exception exp) {
            Window.alert(exp.getMessage());
        }
        
    }
    
    private void updateSelection(int[] selection) {
        if (selection != null && selection.length > 0) {            
             greetingService.updateLineChartSelection(datasetId, selection,new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(String result) {
                        initImage(result);
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
        }
        
    }
    
    public VerticalPanel getLayout() {
        return layout;
    }
    
    private void initImage(String url){ 
        if(image != null ){
        layout.remove(image);
        image = null;
        }
        image = new Image(url);
        image.setHeight("300px");
        image.setWidth(RootPanel.get("LineChartResults").getOffsetWidth()+"px");
        layout.add(image);
        layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    
    }
}
