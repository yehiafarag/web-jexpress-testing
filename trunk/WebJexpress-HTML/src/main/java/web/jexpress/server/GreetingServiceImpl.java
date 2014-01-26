package web.jexpress.server;

import web.jexpress.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.rank.computation.ComputeRank;
import no.uib.jexpress_modularized.rank.computation.RPResult;
import web.jexpress.server.model.HMGen;
import web.jexpress.server.model.JexpressUtil;
import web.jexpress.server.model.PCAUtil;
import web.jexpress.server.model.SOMClustUtil;
import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.beans.PCAResults;
import web.jexpress.shared.beans.RankResult;
import web.jexpress.shared.beans.SomClusteringResults;
//import web.jexpress.shared.model.core.model.dataset.Dataset;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;
import web.jexpress.shared.model.core.model.dataset.Group;
import web.jexpress.server.model.RankUtil;
import web.jexpress.shared.beans.ImgResult;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

   

    private JexpressUtil util = new JexpressUtil();
    private Dataset jDataset;
    private web.jexpress.shared.model.core.model.dataset.Dataset dataset;
    private SOMClustUtil somClustUtil = new SOMClustUtil();
    private SomClusteringResults results;
    private PCAUtil pcaUtil = new PCAUtil();
    private RankUtil rankUtil = new RankUtil();
    private DatasetInformation datasetInfo;

    @Override
    public Map<Integer, String> getAvailableDatasets() {
        Map<Integer, String> datasetsMap = new TreeMap<Integer, String>();
        datasetsMap.put(1, "diauxic shift");
        datasetsMap.put(2, "dataset-2");
        return datasetsMap;
    }

    @Override
    public DatasetInformation loadDataset(int datasetId) {
        jDataset = util.initJexpressDataset(datasetId);
        dataset = util.initWebDataset(jDataset, datasetId);
        datasetInfo = this.updateDatasetInfo(datasetId);        
        return datasetInfo;
    }
    
    
    @Override
    public DatasetInformation updateDatasetInfo(int datasetId){         
        String[] geneTableData[] = new String[dataset.getRowGroups().size() + 1][dataset.getRowIds().length];
        //init gene names with index
        String[] geneNamesArr = util.initGeneNamesArr(dataset.getGeneIndexNameMap());
        dataset.setGeneNamesArr(geneNamesArr);
        geneTableData[0] = geneNamesArr;    
        dataset.setGeneColorArr(util.initColorArr(geneNamesArr, dataset.getRowGroups()));
        int index = 1;
        String[] rowGroupsNames = new String[jDataset.getRowGroups().size() - 1];
        for (Group g : dataset.getRowGroups()) {
            String[] col = new String[geneNamesArr.length];
            for (int x = 0; x < geneNamesArr.length; x++) {
                String color = "#FFFFFF";
                if (g.isActive() && g.getGeneList().contains(geneNamesArr[x])) {
                    color = g.getColor();
                }
                col[x] = color;
            }
            geneTableData[index] = col;
            if (index > 1) {
                rowGroupsNames[index - 2] = g.getId();
            }
            index++;

        }
        datasetInfo = new DatasetInformation();
        datasetInfo.setId(dataset.getId());
        datasetInfo.setRowsNumb(dataset.getDataLength());
        datasetInfo.setColNumb(dataset.getDataWidth());
        datasetInfo.setRowGroupsNumb(dataset.getRowGroups().size() - 1);
        datasetInfo.setColGroupsNumb(dataset.getColumnGroups().size() - 1);
        datasetInfo.setDatasetInfo(dataset.getInfoHeaders()[0]);
        LinkedHashMap<String,String> rowGroupsNamesMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String,String> colGroupsNamesMap = new LinkedHashMap<String, String>();
        for(int x=0;x<dataset.getRowGroups().size();x++){
            rowGroupsNamesMap.put(dataset.getRowGroups().get(x).isActive()+","+dataset.getRowGroups().get(x).getId(), dataset.getRowGroups().get(x).getId());
        }
         for(int x=0;x<dataset.getColumnGroups().size();x++){
            colGroupsNamesMap.put(dataset.getColumnGroups().get(x).isActive()+","+dataset.getColumnGroups().get(x).getId(), dataset.getColumnGroups().get(x).getId());
        }
         String[] pcaColNames = new String[dataset.getColumnIds().length];
         for(int x=0;x<pcaColNames.length;x++)
             pcaColNames[x] = "Principal Component nr "+x;
        datasetInfo.setColsNames(pcaColNames);
        datasetInfo.setColGroupsNamesMap(colGroupsNamesMap);
        datasetInfo.setRowGroupsNamesMap(rowGroupsNamesMap);
        datasetInfo.setGeneTabelData(geneTableData);
        datasetInfo.setRowGroupsNames(rowGroupsNames);       
        return datasetInfo;   
    }
     @Override
    public DatasetInformation activateGroups(int datasetId, String[] rowGroups, String[] colGroups) {

        if (rowGroups != null && rowGroups.length > 0) {
            List<no.uib.jexpress_modularized.core.dataset.Group> updatedActiveGroupList = new ArrayList<no.uib.jexpress_modularized.core.dataset.Group>();
            for (no.uib.jexpress_modularized.core.dataset.Group g : jDataset.getRowGroups()) {
                g.setActive(false);
                updatedActiveGroupList.add(g);
            }
            jDataset.getRowGroups().clear();
            jDataset.getRowGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();

            for (String str : rowGroups) {
                String gName = str.split(",")[1];

                if (gName.equalsIgnoreCase("ALL")) {
                    updatedActiveGroupList.clear();
                    for (no.uib.jexpress_modularized.core.dataset.Group g : jDataset.getRowGroups()) {
                        if (g.getName().equalsIgnoreCase("ALL")) {
                            g.setActive(true);
                        } else {
                            g.setActive(false);
                        }
                        updatedActiveGroupList.add(g);
                    }  
                    break;

                } else {

                    for (no.uib.jexpress_modularized.core.dataset.Group g : jDataset.getRowGroups()) {
                        if (g.getName().equalsIgnoreCase(gName)) {
                            g.setActive(true);
                        }
                        updatedActiveGroupList.add(g);
                    }
                }
            }
        }
            
           if (colGroups != null && colGroups.length > 0) {
            List<no.uib.jexpress_modularized.core.dataset.Group> updatedActiveGroupList = new ArrayList<no.uib.jexpress_modularized.core.dataset.Group>();
            for (no.uib.jexpress_modularized.core.dataset.Group g : jDataset.getColumnGroups()) {
                g.setActive(false);
                updatedActiveGroupList.add(g);
            }
            jDataset.getColumnGroups().clear();
            jDataset.getColumnGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();

            for (String str : colGroups) {
                String gName = str.split(",")[1];
                System.out.println("gName is " + gName);

                if (gName.equalsIgnoreCase("ALL")) {
                    updatedActiveGroupList.clear();
                    for (no.uib.jexpress_modularized.core.dataset.Group g : jDataset.getColumnGroups()) {
                        if (g.getName().equalsIgnoreCase("ALL")) {
                            g.setActive(true);
                        } else {
                            g.setActive(false);
                        }
                        updatedActiveGroupList.add(g);
                    }  
                    break;

                } else {

                    for (no.uib.jexpress_modularized.core.dataset.Group g : jDataset.getColumnGroups()) {
                        if (g.getName().equalsIgnoreCase(gName)) {
                            g.setActive(true);
                        }
                        updatedActiveGroupList.add(g);
                    }
                }
            }
            
            
            
            
            
            jDataset.getColumnGroups().clear();
            jDataset.getColumnGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();                

        }

    
        dataset = util.initWebDataset(jDataset, datasetId);
        datasetInfo = this.updateDatasetInfo(datasetId);

        return datasetInfo;
    }

    
    @Override
    public SomClusteringResults computeSomClustering(int datasetId,int linkage,int distanceMeasure) throws IllegalArgumentException {
        String linkageStr = "WPGMA";
        if(linkage == 0)
            linkageStr = "SINGLE";
        else  if(linkage == 1)
            linkageStr = "WPGMA";
        else  if(linkage == 2)
            linkageStr = "UPGMA";
         else  if(linkage == 3)
            linkageStr = "COMPLETE";
        results = somClustUtil.initHC(jDataset, distanceMeasure, linkageStr, true, dataset.getId());
        results = somClustUtil.initSelectedNodes(results);
        TreeMap<String, String> toolTipsMap = somClustUtil.initToolTips(results.getSideTree(), dataset.getGeneIndexNameMap());
        results.setToolTips(toolTipsMap);
        results.setColsNames(jDataset.getColumnIds());
        results.setGeneNames(jDataset.getRowIds());
 
        return results;
    }
    @Override
    public ImgResult computeHeatmap(int datasetId, List<String> indexer) {
       String pass = this.getServletContext().getRealPath("/");
        HMGen hmGenerator= new HMGen(pass+"/js",jDataset,indexer);        
        ImgResult imge = hmGenerator.getHeatMapResults();
        return imge;
    }

    @Override
    public PCAResults computePCA(int datasetId,int comI,int comII) {
        PCAResults res = pcaUtil.getPCAResults(jDataset,dataset,comI,comII);
        res.setDatasetId(datasetId);
        return res;
   }
    
    
        @Override
    public RankResult computeRank(int datasetId,String perm,String seed,String[] colGropNames,String log2) {
        String type = "TwoClassUnPaired";
        int iPerm = Integer.valueOf(perm);
        int iSeed = Integer.valueOf(seed);
        
        boolean log = false;
        if(log2.equalsIgnoreCase("log 2"))
            log = true;
         int[] col1=null;
         int[] col2=null;
        if(colGropNames.length == 1){
             type = "OneClass";
            for(no.uib.jexpress_modularized.core.dataset.Group g:jDataset.getColumnGroups()){
                if(colGropNames[0].split(",")[1].equalsIgnoreCase(g.getName()))
                     col1 = g.getMembers();
            }
        }
        else if(colGropNames.length==2) {
           type = "TwoClassUnPaired";
           for(no.uib.jexpress_modularized.core.dataset.Group g:jDataset.getColumnGroups()){
                if(colGropNames[0].split(",")[1].equalsIgnoreCase(g.getName()))
                     col1 = g.getMembers();
                if(colGropNames[1].split(",")[1].equalsIgnoreCase(g.getName()))
                     col2 = g.getMembers();
            }
        }      
        ComputeRank cr = new ComputeRank(jDataset);
        ArrayList<RPResult> jResults= cr.createResult(type, iPerm, iSeed, col1, col2, log);
        RankResult rankResults = rankUtil.handelRankTable(jResults);
        rankResults.setDatasetId(datasetId);
        System.out.println("Rank is handeled");
        return rankResults;
    }
        
        @Override
        public Boolean createGroup(int datasetId,String name,String color,String type,int[] selection)
        {
           String gColor = "";
           Color c = null;
           if(color == null || color.equals("")){
                c = generatRandColor();
                gColor  = "#" + Integer.toHexString(c.getRGB()).substring(2);
           }else
           {
               c = Color.getColor(color);
               gColor = color;
           }
                Selection.TYPE s = null;
                String gType = "";
           if(type.equals("ROW GROUPS")){
                s =  Selection.TYPE.OF_ROWS;
                gType = "Row";
           }
           else{
               s =  Selection.TYPE.OF_COLUMNS;
               gType = "Column";
           }
            no.uib.jexpress_modularized.core.dataset.Group jG = new no.uib.jexpress_modularized.core.dataset.Group(name, c, new Selection(s, selection));
            jG.setActive(true);
            jDataset.addRowGroup(jG);
             //String hex = "#" + Integer.toHexString(jG.getColor().getRGB()).substring(2);
                Group g = new Group();
                g.setType(gType);
                g.setColor(gColor);
                List<Integer> ind = new ArrayList<Integer>();
                for (int x : jG.getMembers()) {
                    ind.add(x);
                }
                g.setIndices(ind);

                g.setGeneList(util.initGroupGeneList(dataset, jG.getMembers()));
                g.setId(jG.getName());
                dataset.addRowGroup(g);
                return true;
        
        
        
        }
          
     private Random rand = new Random();
     private Color generatRandColor()
     {
        
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;
     }
   
    @Override
    public LineChartResults computeLineChart(int datasetId) {
        LineChartResults lcResults = new LineChartResults();
        lcResults.setDatasetId(datasetId);
        String[] geneNames = dataset.getGeneNamesArr();
        String[] colours = dataset.getGeneColorArr();//new String[dataset.getRowIds().length];

        Number[] pointsArr[] = new Number[dataset.getDataLength()][dataset.getDataWidth()];
        for (int x = 0; x < dataset.getMemberMaps().size(); x++) {

            Number[] updatedRow = dataset.getMemberMaps().get(x);//new Number[rowData.length];
            pointsArr[x] = updatedRow;

        }
        lcResults.setColours(colours);
        lcResults.setGeneNames(geneNames);
        lcResults.setLineChartPoints(pointsArr);
        return lcResults;
    }
    
}
