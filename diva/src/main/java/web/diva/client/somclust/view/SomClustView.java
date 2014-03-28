/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.widgets.layout.HLayout;
import java.util.List;
import web.diva.shared.ModularizedListener;
import web.diva.shared.Selection;
import web.diva.shared.SelectionManager;
import web.diva.shared.beans.HeatMapImgResult;
import web.diva.shared.beans.SomClusteringResults;

/**
 *
 * @author Yehia Farag
 */
public class SomClustView extends ModularizedListener implements IsSerializable {

    private final SelectionManager selectionManager;
    private final SomClustComp somClustCom;
    private final List<String> indexer;
    private final List<String> colIndexer;

    public List<String> getColIndexer() {
        return colIndexer;
    }

    public void setImge(HeatMapImgResult imgeResut) {
        somClustCom.setImage(imgeResut);
    }

    public SomClustView(SomClusteringResults somClusteringResults, SelectionManager selectionManager) {
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

    public List<String> getIndexer() {
        return indexer;
    }

    public HLayout asWidget() {
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

}
