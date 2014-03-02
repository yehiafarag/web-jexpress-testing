/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.somclust.visualization;

import java.io.Serializable;
import javax.swing.JFrame;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters;
import no.uib.jexpress_modularized.somclust.model.ClusterResults;

/**
 *
 * @author Yehia Farag
 */
public class SomclustView implements Serializable{
    
    private MainClustComponent clust;
    public SomclustView(Dataset dataSet, ClusterParameters pm, ClusterResults result)
    {
        clust = new MainClustComponent(dataSet, pm, result);        
        clust.setDoubleBuffered(false);
        clust.setFocusable(true);
        clust.setRequestFocusEnabled(true);        
        HierarchicalClusteringPanel hcp = new HierarchicalClusteringPanel(clust);           

        JFrame j = new JFrame();
        j.add(hcp);
        j.setVisible(true);
        j.setSize(800, 500);
        j.setLocationRelativeTo(null);
    }
     public MainClustComponent getClust() {
        return clust;
    }
    
}
