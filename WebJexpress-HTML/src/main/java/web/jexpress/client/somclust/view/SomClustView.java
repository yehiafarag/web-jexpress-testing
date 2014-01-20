/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.somclust.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import java.util.List;
import web.jexpress.client.core.model.SelectionManager;
import web.jexpress.shared.beans.ImgResult;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.ModularizedListener;
import web.jexpress.shared.model.core.model.Selection;

/**
 *
 * @author Yehia Farag
 */
public class SomClustView extends ModularizedListener implements IsSerializable {

    private final SelectionManager selectionManager;
    private final SomClustComp somClustCom;
    private final List<String> indexer;

    public void setImge(ImgResult imgeResut) {
        somClustCom.setImage(imgeResut);
    }

    public SomClustView(String pass, SomClusteringResults somClusteringResults, SelectionManager selectionManager) {
        somClustCom = new SomClustComp(pass, somClusteringResults, selectionManager);
        this.classtype = 2;
        this.datasetId = somClusteringResults.getDatasetId();
        this.components.add(SomClustView.this);
        this.selectionManager = selectionManager;
        this.selectionManager.addSelectionChangeListener(datasetId, SomClustView.this);
        this.indexer = somClustCom.getIndexer();
    }

    public List<String> getIndexer() {
        return indexer;
    }

    public HorizontalPanel asWidget() {
        return somClustCom;
    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            Selection sel = selectionManager.getSelectedRows(datasetId);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                if (selectedRows != null) {
                }
            }
        }
    }

}
