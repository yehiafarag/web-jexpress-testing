/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.somclust.view;

import com.google.gwt.user.client.ui.HorizontalPanel;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.SelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class SomClustComp extends HorizontalPanel {

    private TreeGraph sideTree;
    public SomClustComp(SomClusteringResults results, SelectionManager selectionManager) {
        this.setBorderWidth(0);
        this.setSpacing(10);     
//        sideTree = new TreeGraph(results, "left", selectionManager);         
//        this.add(sideTree.asWidget());
//        HeatMap hm = new HeatMap();
//        this.add(hm.asWidget());
        CustomHeatMap cheatma = new CustomHeatMap(results, selectionManager);
        this.add(cheatma.getLayout());
    }

   
    
    
    
}
