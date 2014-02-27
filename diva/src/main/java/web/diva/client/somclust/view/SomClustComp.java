/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.layout.HLayout;
import java.util.List;
import web.diva.client.core.model.SelectionManager;
import web.diva.shared.beans.ImgResult;
import web.diva.shared.beans.SomClusteringResults;

/**
 *
 * @author Yehia Farag
 */
public class SomClustComp extends HLayout {

    private final TreeGraph sideTree;
    private final TopTreeGraph topTree ;
    private  List<String> indexer;
    private  List<String> colIndexer;
    private int width ;
    private final VerticalPanel vp;

    public double getCurrentWidth() {
        return width;
    }

    public int getCurrentHeight() {
        return height;
    }

    private int height = 550;

    public List<String> getIndexer() {
        return indexer;
    }

    public SomClustComp(SomClusteringResults results, SelectionManager selectionManager) {
        width =(RootPanel.get("SomClusteringResults").getOffsetWidth()-50);
        this.setWidth(width);
       
        sideTree = new TreeGraph(results, "left", selectionManager, height, (width));
        this.addMember(sideTree.asWidget());
        this.indexer = sideTree.getIndexers();
        vp = new VerticalPanel();
        vp.setHeight((height+50)+"px");
        this.addMember(vp);
       
        topTree = new TopTreeGraph(results, "top", selectionManager, (height+50),(width));
        vp.add(topTree.asWidget());
        vp.setSpacing(0);
        this.colIndexer = topTree.getIndexers();
        
    }

    public void setImage(final ImgResult imageResult) {
        HeatMapGraph hmg = new HeatMapGraph(imageResult,(width), height);
        vp.add(hmg);
        colIndexer = null;
        indexer = null;
        sideTree.clearIndexer();
        topTree.clearIndexer();
                
        
    }

    public List<String> getColIndexer() {
        return colIndexer;
    }
    
    public void clearSelection(){}
    
    
    
    
    
    
    
//    public void updateSize(double width, double height) {
//        this.height = (int) height;
//        this.width = (int) width;
//        sideTree.resize((width*2/3), height);
//        image.setSize((width /3) + "px", height + "px");
//    }

}
