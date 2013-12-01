/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.somclust.view;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.impl.ImageResourcePrototype;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.SelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class SomClustComp extends HorizontalPanel {

    private TreeGraph sideTree;
    public SomClustComp(String pass,SomClusteringResults results, SelectionManager selectionManager) {
        this.setBorderWidth(0);
        this.setSpacing(10);     
        sideTree = new TreeGraph(results, "left", selectionManager);
        this.add(sideTree.asWidget());

        Image image = new Image("js/java-heat-chart.png");


        image.setWidth("164px");
        image.setHeight("600px");
      
//        ImageResource ir = new ImageResourcePrototype("heatmap", "file:///F:/files/java-heat-chart.png", 0, 0, 500, 500, true, true);
//
//        image.setResource(new ImageResource );
        //image.setUrl("file:///F:/files/java-heat-chart.png");
        this.add(image);
//        HeatMap hm = new HeatMap();
//        this.add(hm.asWidget());
//        CustomHeatMap cheatma = new CustomHeatMap(results, selectionManager);
//        this.add(cheatma.getLayout());
    }

   
    
    
    
}
