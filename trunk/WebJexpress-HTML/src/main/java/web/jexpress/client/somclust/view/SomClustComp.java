/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.somclust.view;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.layout.HLayout;
import java.util.List;
import web.jexpress.client.core.model.SelectionManager;
import web.jexpress.shared.beans.ImgResult;
import web.jexpress.shared.beans.SomClusteringResults;

/**
 *
 * @author Yehia Farag
 */
public class SomClustComp extends HLayout {

    private final TreeGraph sideTree;
    private final List<String> indexer;
    private final List<String> colIndexer;
    private Image image;
    private int width ;
    private final VerticalPanel vp;

//    public void updateSize(double width, double height) {
//        this.height = (int) height;
//        this.width = (int) width;
//        sideTree.resize((width*2/3), height);
//        image.setSize((width /3) + "px", height + "px");
//    }

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

    public SomClustComp(String pass, SomClusteringResults results, SelectionManager selectionManager) {
//        this.setBorderWidth(0);
//        this.setSpacing(0);
        this.setHeight(height);
        width = RootPanel.get("SomClusteringResults").getOffsetWidth();
        this.setWidth(width);
        sideTree = new TreeGraph(results, "left", selectionManager, height, width);
        this.addMember(sideTree.asWidget());
        this.indexer = sideTree.getIndexers();
        vp = new VerticalPanel();
        vp.setHeight((height+50)+"px");
        this.addMember(vp);
       
        TopTreeGraph topTree = new TopTreeGraph(results, "top", selectionManager, (height+50), width);
        vp.add(topTree.asWidget());
        vp.setSpacing(0);
        this.colIndexer = topTree.getIndexers();
        
    }

    public void setImage(final ImgResult imageResult) {
        HeatMapGraph hmg = new HeatMapGraph(imageResult,width, height);
        vp.add(hmg);
        
    }

    public List<String> getColIndexer() {
        return colIndexer;
    }
}
