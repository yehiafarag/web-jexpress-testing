/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.server.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import no.uib.jexpress_modularized.core.dataset.DataSet;
import no.uib.jexpress_modularized.somclust.computation.SOMClustCompute;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters;
import no.uib.jexpress_modularized.somclust.model.ClusterResults;
import no.uib.jexpress_modularized.somclust.model.Node;
import web.jexpress.shared.CustomNode;
import web.jexpress.shared.Unit;
import web.jexpress.shared.beans.SomClusteringResults;

/**
 *
 * @author Yehia Farag
 */
public class SOMClustUtil {
    private DecimalFormat df = null;
    public Map<String, CustomNode> getNodesMap() {
        return nodesMap;
    }
    public SomClusteringResults initSelectedNodes(SomClusteringResults results)
    {
        TreeMap<String,CustomNode> nodesMap = results.getSideTree().getNodesMap();
        
        TreeMap<String,CustomNode> updatedNodesMap = results.getSideTree().getNodesMap();
        for(String key : nodesMap.keySet())
        {
            CustomNode cn = nodesMap.get(key);
            List<Integer> tempList = new ArrayList<Integer>();
            for (String str : cn.getChildernList()) {
                if (!str.contains("gene")) {
                    tempList.add(Integer.valueOf(str));
                }
            }
            int[] indexArr = new int[tempList.size()];
            for(int index = 0;index <tempList.size();index++)
                indexArr[index] = tempList.get(index);
            cn.setSelectedNodes(indexArr);
            updatedNodesMap.put(key, cn);
        }
        results.getSideTree().setNodesMap(updatedNodesMap);
        return results;
    }

    public SomClusteringResults initHC(DataSet dataset,int distance,String linkageType,boolean clusterColumn,int datasetId ) {

       
        ClusterParameters parameter = new ClusterParameters();
        parameter.setDistance(distance);
        parameter.setClusterSamples(clusterColumn);
        ClusterParameters.LINKAGE link = null;
        if(linkageType.equalsIgnoreCase("UPGMA"))
            link = ClusterParameters.LINKAGE.UPGMA;
        else if(linkageType.equalsIgnoreCase("SINGLE"))
            link = ClusterParameters.LINKAGE.SINGLE;
         else if(linkageType.equalsIgnoreCase("COMPLETE"))
            link = ClusterParameters.LINKAGE.COMPLETE;
          else if(linkageType.equalsIgnoreCase("WPGMA"))
            link = ClusterParameters.LINKAGE.WPGMA;
        
        
        parameter.setLink(link);

        SOMClustCompute som = new SOMClustCompute(dataset, parameter);
        ClusterResults results = som.runClustering();
        
        SomClusteringResults somClustResults = new SomClusteringResults();
        
        if(clusterColumn)
        {
            Unit topTree = initTree(results.getColumnDendrogramRootNode());
            somClustResults.setTopTree(topTree);
        
        }
        Unit sideTreeUnit = initTree(results.getRowDendrogramRootNode());
        somClustResults.setSideTree(sideTreeUnit);
        
        somClustResults.setHeight(this.getHeightPix(dataset.getRowIds().length));
        somClustResults.setDatasetId(datasetId);
        return somClustResults;

      

    }
    
    
    private Unit initTree(Node root)
    {
          int index = 0;
        CustomNode parent = new CustomNode();
        parent.setIndex(index);
        parent.setName("gene " + index);
        parent.setIndex(index);
        
        parent.setChildernList(new ArrayList<String>());
        parent.getChildernList().add("gene " + index);
        parent.setValue(root.getval());
        
        nodesMap.put("gene " + index, parent);
        Unit unit = new Unit("gene " + index, initUnit(root.left, parent), initUnit(root.right, parent));
        cleanNodesMap();
        unit.setNodesMap(nodesMap);
        unit.value = root.getval();
        
        return unit;
    
    
    }
    private TreeMap<String, CustomNode> nodesMap = new TreeMap<String, CustomNode>();

    private Unit initUnit(Node root,  CustomNode parent) {
        Unit unit = null;
        CustomNode node = null;
        if (root == null) {
            return null;
        }
        if (root.left == null && root.right == null) {
             long localIndex = System.currentTimeMillis()+ (long)(Math.random()*1000000.0);
            unit = new Unit(""+root.nme,1000);//"gene " +localIndex, 1000);
            node = new CustomNode();
            node.setValue(root.getval());
            node.setChildernList(new ArrayList<String>());
            node.setName(""+root.nme);
            node.setGeneIndex(root.nme);
            node.getChildernList().add(""+root.nme);
            node.setIndex(localIndex);
            nodesMap.put(""+root.nme, node);
            unit.value = root.value;
            parent.getChildernList().addAll(node.getChildernList());
            return unit;
        } else {
            long localIndex = System.currentTimeMillis()+ (long)(Math.random()*1000000.0);
            node = new CustomNode();
            List<String> newChildernList = new ArrayList<String>();
            node.setChildernList(newChildernList);
            node.setIndex(localIndex);
            node.setValue(root.getval());
            node.setName("gene " +localIndex);
            nodesMap.put("gene " +localIndex, node);
            node.getChildernList().add("gene " +localIndex);
            unit = new Unit(("gene " +localIndex), initUnit(root.left, node), initUnit(root.right, node));
            unit.value = root.value;
            parent.getChildernList().addAll(node.getChildernList());
            
        }
        return unit;
    }

    public TreeMap<String, String> initToolTips(Unit results, Map<Integer, String> geneMap) {
        TreeMap<String, String> toolTipsMap = new TreeMap<String, String>();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.##", otherSymbols);
        for (String key : results.getNodesMap().keySet()) {
            CustomNode cNode = results.getNodesMap().get(key);
            String value = "";
            if (cNode.getName().contains("gene")) {
                value = "Merged at " + df.format(cNode.getValue()) + " #Nodes : " + (cNode.getSelectedNodes().length);
            } else {
                value = geneMap.get(cNode.getGeneIndex());
            }
            toolTipsMap.put(key, value);
        }
        return toolTipsMap;//"kokowawaw";//n.firstChild() != null ? "Merged at "+cNode.getValue()+" Nodes : "+(cNode.getSelectedNodes().length) :"  ";

    }
    
       public int getHeightPix(int rowNumb)
    {
         int height =(rowNumb*22);
         return height;
    
    }
       public void cleanNodesMap()
       {
              TreeMap<String, CustomNode> cleanNodesMap = new TreeMap<String, CustomNode>();
              cleanNodesMap.putAll(nodesMap);
              nodesMap.clear();
              for(String key:cleanNodesMap.keySet())
              {
                  CustomNode cn = cleanNodesMap.get(key);
                  cn.getChildernList().remove(cn.getName());
                  nodesMap.put(key, cn);
              
              }

           
       
       }
       
       

   
    
}
