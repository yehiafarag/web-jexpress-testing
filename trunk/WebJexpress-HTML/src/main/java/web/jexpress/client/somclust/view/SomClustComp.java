/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.somclust.view;

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import java.util.List;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.SelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class SomClustComp extends HorizontalPanel {

    private final TreeGraph sideTree;
    private final List<String>indexer;
    private Image image;

    public List<String> getIndexer() {
        return indexer;
    }
    public SomClustComp(String pass,SomClusteringResults results, SelectionManager selectionManager) {
        this.setBorderWidth(0);
        this.setSpacing(10);     
        sideTree = new TreeGraph(results, "left", selectionManager);        
        this.add(sideTree.asWidget());
        this.indexer = sideTree.getIndexers();
//          greetingService.computeHeatmap(results.getDatasetId(),sideTree.getIndexers(),
//                new AsyncCallback<ImgResult>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                
//            }
//
//            @Override
//            public void onSuccess(ImgResult heatmapresult) {
//               
//            }
//        });

      
        
        
        
        

        
        
        //Image image = new Image("js/java-heat-chart.png");
//       Image image = new Image(results.getImgString());
//       // Image image = new Image("data:image/png;base64,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");
       // image.setPixelSize(300, 500);


//        image.setWidth("140px");
//        image.setHeight("500px");
      
//        ImageResource ir = new ImageResourcePrototype("heatmap", "file:///F:/files/java-heat-chart.png", 0, 0, 500, 500, true, true);
//
//        image.setResource(new ImageResource );
        //image.setUrl("file:///F:/files/java-heat-chart.png");
//        this.add(image);
//        HeatMap hm = new HeatMap();
//        this.add(hm.asWidget());
//        CustomHeatMap cheatma = new CustomHeatMap(results, selectionManager);
//        this.add(cheatma.getLayout());
    }

    public void setImage(Image image) {
        this.image = image;
        this.image.setWidth("140px");
        this.image.setHeight("600px");
        final int col = 140/7;
        final double row = 600/391;
        this.image.addMouseOverHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                
                    Window.alert("x: column :"+ event.getX()/col+" ,  y: "+((double)event.getY()/row));
            }
        });
        this.add(this.image);
    }

   private String getValue(int x,int y)
   {
       return " koko wawa";
   }
    
    
    
}
