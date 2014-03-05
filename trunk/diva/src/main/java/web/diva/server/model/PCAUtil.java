/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.pca.computation.PcaCompute;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import web.diva.shared.beans.PCAImageResults;
import web.diva.shared.beans.PCAPoint;
import web.diva.shared.beans.PCAResults;
//import web.jexpress.shared.model.core.model.dataset.Dataset;
import web.diva.shared.model.core.model.dataset.Group;

/**
 *
 * @author Yehia Farag
 */
public class PCAUtil {
    
    public PCAResults getPCAResults(Dataset jDataset,web.diva.shared.model.core.model.dataset.Dataset dataset,int pcx,int pcy) {
        PcaCompute pcaCompute = new PcaCompute(jDataset);
//         TreeMap<String,String> xyName = new TreeMap<String, String>();
        no.uib.jexpress_modularized.pca.computation.PcaResults jResults = pcaCompute.createPCA();  
       TreeMap<Integer,PCAPoint> pointList = new TreeMap<Integer,PCAPoint>();
        for(Group g : dataset.getRowGroups()) {
            if (g.getId().equalsIgnoreCase("ALL")) {

                for (int i = 0; i < g.getIndices().size(); i++) {
                    PCAPoint point = new PCAPoint();
                    point.setX(jResults.ElementAt(g.getIndices().get(i), pcx));
                    point.setY(jResults.ElementAt(g.getIndices().get(i), pcy));
                    point.setGeneIndex(g.getIndices().get(i));
                    point.setGeneName(g.getGeneList().get(i));
                    point.setColor(g.getColor());
                    pointList.put(point.getGeneIndex(), point);
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
               if(! (pointList.containsKey(point.getGeneIndex()) && point.getColor().equalsIgnoreCase("#000000")))
               {
                   pointList.remove(point.getGeneIndex());
                   pointList.put(point.getGeneIndex(), point);
                   
               }
            }

        }
        
        
//        for(PCAPoint p:pointList.values()){
//        String str = ""+p.getX()+","+p.getY();
//        if(xyName.containsKey(str)){
//            String tooltip = xyName.get(str);
//            tooltip = tooltip+","+p.getGeneName();
//                xyName.put(str,tooltip);
//        }else
//        xyName.put(str,p.getGeneName());
//        
//        }
        PCAResults res = new PCAResults();
        res.setPoints(pointList);
        res.setPcai(pcx);
        res.setPcaii(pcy);
        res.setHeader(dataset.getInfoHeaders()[0]);
//        res.setXyName(xyName);
        
//        for(PCAPoint p : pointList.values()){
//         if(p.getX() == 0.6637092519213141)
//            System.out.println("real X "+p.getX()+"  "+p.getY()+"  str  "+p.getGeneName()+"   name "+p.getGeneIndex());
//        
//        }
      
        
        return res;
    }
    
    public Object[] getTooltips(PCAImageResults results, TreeMap<Integer,PCAPoint> pointList){
    
         PCAPoint[] indexeMap = new PCAPoint[pointList.size()];
        TreeMap<String,String> xyName = new TreeMap<String, String>();
        double xTicksNum = results.getMaxX()-results.getMinX();
        double xArea = results.getDataAreaMaxX() - results.getDataAreaMinX();
        double xFactor = xArea/xTicksNum;     
          double yTicksNum = results.getMaxY()-results.getMinY();
        double yArea = results.getDataAreaMaxY() - results.getDataAreaMinY();        
        double yFactor = yArea/yTicksNum;    
       
        for(PCAPoint p:pointList.values()){
            
            
            double conX = (p.getX()-results.getMinX())*xFactor;
            if(conX <0.0)
                conX = conX*-1.0;
            conX = conX+results.getDataAreaMinX();    
            
             double conY = (results.getMaxY()-p.getY())*yFactor;
            if(conY <0.0)
                conY = conY*-1.0;
            conY = conY+results.getDataAreaMinY();
            
            PCAPoint uP = new PCAPoint();
            uP.setX(conX);
            uP.setY(conY);
            uP.setGeneIndex(p.getGeneIndex());
            indexeMap[p.getGeneIndex()] = uP;
            
            
        String str = ""+((int)conX)+","+((int)conY);
        
       if(xyName.containsKey(str)){
            String tooltip = xyName.get(str);
            tooltip = tooltip+","+p.getGeneName();
                xyName.put(str,tooltip);
                
        }else
        xyName.put(str,p.getGeneName());
        
        }     
//        int  intYArea = (int)yArea;
//        int intXArea = (int)xArea;
//          String[][] geneMatrix = new String[intYArea][intXArea];
//          for(int y=0;y<intYArea;y++){
//              for(int x=0;x<intXArea;x++){
//              
//              }
//              
//          }

         Object[] obj = new Object[2];
         obj[0] = xyName;
         obj[1] = indexeMap;
    return obj;
    }
    

    
    
    
}
