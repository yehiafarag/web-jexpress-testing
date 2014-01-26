/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.server.model;

import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.pca.computation.PcaCompute;
import web.jexpress.shared.beans.PCAPoint;
import web.jexpress.shared.beans.PCAResults;
//import web.jexpress.shared.model.core.model.dataset.Dataset;
import web.jexpress.shared.model.core.model.dataset.Group;

/**
 *
 * @author Y.M
 */
public class PCAUtil {
    
    public PCAResults getPCAResults(Dataset jDataset,web.jexpress.shared.model.core.model.dataset.Dataset dataset,int pcx,int pcy) {
        PcaCompute pcaCompute = new PcaCompute(jDataset);
        
        no.uib.jexpress_modularized.pca.computation.PcaResults jResults = pcaCompute.createPCA();  
       TreeMap<String,PCAPoint> pointList = new TreeMap<String,PCAPoint>();
        for(Group g : dataset.getRowGroups()) {
            if (g.getId().equalsIgnoreCase("ALL")) {

                for (int i = 0; i < g.getIndices().size(); i++) {
                    PCAPoint point = new PCAPoint();
                    point.setX(jResults.ElementAt(g.getIndices().get(i), pcx));
                    point.setY(jResults.ElementAt(g.getIndices().get(i), pcy));
                    point.setGeneIndex(g.getIndices().get(i));
                    point.setGeneName(g.getGeneList().get(i));
                    point.setColor(g.getColor());
                    pointList.put(point.getGeneName(), point);
                }
                continue;

            }
            for (int i = 0; i < g.getIndices().size() && g.isActive(); i++) {
                PCAPoint point = new PCAPoint();
               point.setX(jResults.ElementAt(g.getIndices().get(i), pcx));
               point.setY(jResults.ElementAt(g.getIndices().get(i), pcy));
               point.setGeneIndex(g.getIndices().get(i));
               point.setGeneName(g.getGeneList().get(i));
               point.setColor(g.getColor());
               if(! (pointList.containsKey(point.getGeneName()) && point.getColor().equalsIgnoreCase("#000000")))
               {
                   pointList.remove(point.getGeneName());
                   pointList.put(point.getGeneName(), point);
               }
            }

        }
        PCAResults res = new PCAResults();
        res.setPoints(pointList);
        res.setPcai(pcx);
        res.setPcaii(pcy);
        res.setHeader(dataset.getInfoHeaders()[0]);
        return res;
    }
    
    
    
}
