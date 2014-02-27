/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.somclust.computation.SOMClustCompute;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters;
import no.uib.jexpress_modularized.somclust.model.ClusterResults;
import no.uib.jexpress_modularized.somclust.model.Node;
import web.diva.shared.CustomNode;
import web.diva.shared.Unit;
import web.diva.shared.beans.SomClusteringResults;

/**
 *
 * @author Yehia Farag
 */
public class SOMClustUtil {
    private DecimalFormat df = null;
    
    public Map<String, CustomNode> getNodesMap() {
        return nodesMap;
    }
    
    public SomClusteringResults initSelectedNodes(SomClusteringResults results) {
        TreeMap<String, CustomNode> nodesMap = results.getSideTree().getNodesMap();
        TreeMap<String, CustomNode> updatedNodesMap = results.getSideTree().getNodesMap();
        for (String key : nodesMap.keySet()) {
            CustomNode cn = nodesMap.get(key);
            List<Integer> tempList = new ArrayList<Integer>();
            for (String str : cn.getChildernList()) {
                if (!str.contains("gene")) {
                    tempList.add(Integer.valueOf(str));
                }
            }
            int[] indexArr = new int[tempList.size()];
            for (int index = 0; index < tempList.size(); index++) {
                indexArr[index] = tempList.get(index);
            }
            cn.setSelectedNodes(indexArr);
            updatedNodesMap.put(key, cn);
        }
        results.getSideTree().setNodesMap(updatedNodesMap);
        results =initTopSelectedNodes(results);
        
        return results;
    }
    private SomClusteringResults initTopSelectedNodes(SomClusteringResults results) {
        TreeMap<String, CustomNode> nodesMap = results.getTopTree().getNodesMap();
        TreeMap<String, CustomNode> updatedNodesMap = results.getTopTree().getNodesMap();
        for (String key : nodesMap.keySet()) {
            CustomNode cn = nodesMap.get(key);
            List<Integer> tempList = new ArrayList<Integer>();
            for (String str : cn.getChildernList()) {
                if (!str.contains("col")) {
                    tempList.add(Integer.valueOf(str));
                }
            }
            int[] indexArr = new int[tempList.size()];
            for (int index = 0; index < tempList.size(); index++) {
                indexArr[index] = tempList.get(index);
            }
            cn.setSelectedNodes(indexArr);
            updatedNodesMap.put(key, cn);
        }
        results.getTopTree().setNodesMap(updatedNodesMap);
        return results;
    }

    public SomClusteringResults initHC(Dataset dataset,int distance,String linkageType,boolean clusterColumn,int datasetId ) {
        ClusterParameters parameter = new ClusterParameters();
        parameter.setDistance(distance);
        parameter.setClusterSamples(clusterColumn);
        ClusterParameters.LINKAGE link = null;
        if (linkageType.equalsIgnoreCase("UPGMA")) {
            link = ClusterParameters.LINKAGE.UPGMA;
        } else if (linkageType.equalsIgnoreCase("SINGLE")) {
            link = ClusterParameters.LINKAGE.SINGLE;
        } else if (linkageType.equalsIgnoreCase("COMPLETE")) {
            link = ClusterParameters.LINKAGE.COMPLETE;
        } else if (linkageType.equalsIgnoreCase("WPGMA")) {
            link = ClusterParameters.LINKAGE.WPGMA;
        }
        parameter.setLink(link);
        SOMClustCompute som = new SOMClustCompute(dataset, parameter);
        ClusterResults results = som.runClustering();
        SomClusteringResults somClustResults = new SomClusteringResults();
        if (clusterColumn) {
            Unit topTree = initTopTree(results.getColumnDendrogramRootNode());
            somClustResults.setTopTree(topTree);
        }
        Unit sideTreeUnit = initSideTree(results.getRowDendrogramRootNode());
        somClustResults.setSideTree(sideTreeUnit);
        somClustResults.setHeight(this.getHeightPix(dataset.getRowIds().length));
        somClustResults.setDatasetId(datasetId);
        return somClustResults;
    }
        
    private Unit initSideTree(Node root)
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
        Unit unit = new Unit("gene " + index, initSideUnit(root.left, parent), initSideUnit(root.right, parent));
        cleanNodesMap();
        unit.setNodesMap(nodesMap);
        unit.value = root.getval();
        
        return unit;
    
    
    }
    
    private Unit initTopTree(Node root)
    {
          int index = 0;
        CustomNode parent = new CustomNode();
        parent.setIndex(index);
        parent.setName("col " + index);
        parent.setIndex(index);
        
        parent.setChildernList(new ArrayList<String>());
        parent.getChildernList().add("col " + index);
        parent.setValue(root.getval());
        
        colNodesMap.put("col " + index, parent);
        Unit unit = new Unit("col " + index, initTopUnit(root.left, parent), initTopUnit(root.right, parent));
        cleanColNodesMap();
        unit.setNodesMap(colNodesMap);
        unit.value = root.getval();
        
        return unit;
    
    
    }
   
    private TreeMap<String, CustomNode> nodesMap = new TreeMap<String, CustomNode>();
    private TreeMap<String, CustomNode> colNodesMap = new TreeMap<String, CustomNode>();

    private Unit initSideUnit(Node root,  CustomNode parent) {
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
            unit = new Unit(("gene " +localIndex), initSideUnit(root.left, node), initSideUnit(root.right, node));
            unit.value = root.value;
            parent.getChildernList().addAll(node.getChildernList());
            
        }
        return unit;
    }

    
    private Unit initTopUnit(Node root,  CustomNode parent) {
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
            colNodesMap.put(""+root.nme, node);
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
            node.setName("col " +localIndex);
            colNodesMap.put("col " +localIndex, node);
            node.getChildernList().add("col " +localIndex);
            unit = new Unit(("col " +localIndex), initTopUnit(root.left, node), initTopUnit(root.right, node));
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
    
    
     public TreeMap<String, String> initTopToolTips(Unit results) {
        TreeMap<String, String> toolTipsMap = new TreeMap<String, String>();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.##", otherSymbols);
        for (String key : results.getNodesMap().keySet()) {
            CustomNode cNode = results.getNodesMap().get(key);
            String value = "";
            if (cNode.getName().contains("col")) {
                value ="";// "Merged at " + df.format(cNode.getValue()) + " #Nodes : " + (cNode.getSelectedNodes().length);
            } else {
                value = cNode.getName();
            }
            toolTipsMap.put(key, value);
        }
        return toolTipsMap;//"kokowawaw";//n.firstChild() != null ? "Merged at "+cNode.getValue()+" Nodes : "+(cNode.getSelectedNodes().length) :"  ";

    }
    
    public int getHeightPix(int rowNumb) {
        int height = (rowNumb * 22);
        return height;

    }

    public void cleanNodesMap() {
        TreeMap<String, CustomNode> cleanNodesMap = new TreeMap<String, CustomNode>();
        cleanNodesMap.putAll(nodesMap);
        nodesMap.clear();
        for (String key : cleanNodesMap.keySet()) {
            CustomNode cn = cleanNodesMap.get(key);
            cn.getChildernList().remove(cn.getName());
            nodesMap.put(key, cn);
        }

    }
    public void cleanColNodesMap() {
        TreeMap<String, CustomNode> cleanNodesMap = new TreeMap<String, CustomNode>();
        cleanNodesMap.putAll(colNodesMap);
        colNodesMap.clear();
        for (String key : cleanNodesMap.keySet()) {
            CustomNode cn = cleanNodesMap.get(key);
            cn.getChildernList().remove(cn.getName());
            colNodesMap.put(key, cn);
        }

    }

//    public String heatMapGenerator() {
//        double[][] data = new double[][]{{3, 2, 3, 4, 5, 6},
//        {2, 3, 4, 5, 6, 7},
//        {3, 4, 5, 6, 7, 6},
//        {4, 5, 6, 7, 6, 5}};
//
//// Step 1: Create our heat map chart using our data.
//        HeatChart map = new HeatChart(data);
//
//// Step 2: Customise the chart.
//        map.setTitle("Yehia");
//        map.setXAxisLabel("X Axis");
//        map.setYAxisLabel("Y Axis");
//
//// Step 3: Output the chart to a file.
//        File img = new File("D:\\files\\java-heat-chart.png");
//        String base64 ="";
//        try {
//            img.createNewFile();
//            map.saveToFile(img);
//            // Reading a Image file from file system
//            FileInputStream imageInFile = new FileInputStream(img);
//            byte imageData[] = new byte[(int) img.length()];
//            imageInFile.read(imageData);
// 
//            // Converting Image byte array into Base64 String
//            //String imageDataString = encodeImage(imageData);
// 
//            // Converting a Base64 String into Image byte array
//           // byte[] imageByteArray = decodeImage(imageDataString);
//            
//            base64 =  Base64.encode(imageData);//com.google.gwt.user.server.Base64Utils.toBase64(imageData); 
//           
//            base64 = "data:image/png;base64,"+base64;
//            System.out.println("base64 "+base64);
//        } catch (IOException exp) {
//            exp.printStackTrace();
//        }
//
//
//        return base64 ;
//    }

}
