/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.somclust.view;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import org.thechiselgroup.choosel.protovis.client.PVEventHandler;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsArgs;
import web.jexpress.shared.model.core.model.SelectionManager;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.ModularizedListener;
import web.jexpress.shared.model.core.model.Selection;

/**
 *
 * @author Y.M
 */
public class SomClustView extends ModularizedListener implements IsSerializable{

   

    private SelectionManager selectionManager;
    
    private SomClustComp somClustCom;

    public SomClustView(String pass,SomClusteringResults somClusteringResults,SelectionManager selectionManager)
    {

        
        somClustCom = new SomClustComp(pass,somClusteringResults, selectionManager);        
        this.classtype=2;
        this.datasetId = somClusteringResults.getDatasetId();
        this.components.add(SomClustView.this);
        this.selectionManager = selectionManager;
        this.selectionManager.addSelectionChangeListener(datasetId, SomClustView.this);
    
         
    
    }
     public HorizontalPanel asWidget() {
        return somClustCom;
    }
    
    
    @Override
    public void selectionChanged(Selection.TYPE type) {
     if (type == Selection.TYPE.OF_ROWS) {
        Selection sel =selectionManager.getSelectedRows(datasetId);           
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                if (selectedRows != null) {
            }
        }   
     }
    }
    
}
