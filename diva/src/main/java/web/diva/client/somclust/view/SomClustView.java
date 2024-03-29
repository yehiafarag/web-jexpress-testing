/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.widgets.layout.HLayout;
import java.util.ArrayList;
import web.diva.client.selectionmanager.ModularizedListener;
import web.diva.client.selectionmanager.Selection;
import web.diva.client.selectionmanager.SelectionManager;
import web.diva.shared.beans.HeatMapImageResult;
import web.diva.shared.beans.SomClusteringResult;

/**
 *
 * @author Yehia Farag
 */
public class SomClustView extends ModularizedListener implements IsSerializable {

    private  SelectionManager selectionManager;
    private  SomClustComp somClustCom;
    private  ArrayList<String> indexer;
    private  ArrayList<String> colIndexer;

    public ArrayList<String> getColIndexer() {
        return colIndexer;
    }

    public void setImge(HeatMapImageResult imgeResut) {
        somClustCom.setImage(imgeResut);
    }

    public SomClustView(SomClusteringResult somClusteringResults, SelectionManager selectionManager) {
        somClustCom = new SomClustComp(somClusteringResults, selectionManager);
        this.classtype = 2;
        this.datasetId = somClusteringResults.getDatasetId();
        this.components.add(SomClustView.this);
        this.selectionManager = selectionManager;
        this.selectionManager.addSelectionChangeListener(datasetId, SomClustView.this);
        this.indexer = somClustCom.getIndexer();
        this.colIndexer =somClustCom.getColIndexer();
        somClusteringResults = null;
    }

    public ArrayList<String> getIndexer() {
        return indexer;
    }

    public HLayout componentView() {
        return somClustCom;
    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows(datasetId);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                if (selectedRows != null) {
                    clearSelection();
                }
            }
        }
    }

    public void clearSelection() {

        somClustCom.clearSelection();

    }
    @Override
    public void remove(){
    selectionManager=null;
    somClustCom.remove();
     somClustCom=null;
     indexer=null;
    colIndexer=null;
    }

}
