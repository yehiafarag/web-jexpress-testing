package web.jexpress.server;

import web.jexpress.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.DataSet;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.rank.computation.ComputeRank;
import web.jexpress.server.model.HMGen;
import web.jexpress.server.model.JexpressUtil;
import web.jexpress.server.model.PCAUtil;
import web.jexpress.server.model.SOMClustUtil;
import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.beans.PCAResults;
import web.jexpress.shared.beans.RankResult;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.dataset.Dataset;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;
import web.jexpress.shared.model.core.model.dataset.Group;
import no.uib.jexpress_modularized.rank.computation.RPmodel;
import web.jexpress.server.model.RankUtil;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    private JexpressUtil util = new JexpressUtil();
    private DataSet jDataset;
    private Dataset dataset;
    private SOMClustUtil somClustUtil = new SOMClustUtil();
    private SomClusteringResults results;
    private PCAUtil pcaUtil = new PCAUtil();
    private RankUtil rankUtil = new RankUtil();
    private DatasetInformation datasetInfo;

    @Override
    public DatasetInformation loadDataset(int datasetId) {
        jDataset = util.initJexpressDataset();
        dataset = util.initWebDataset(jDataset, datasetId);
        datasetInfo = this.updateDatasetInfo(datasetId);


        return datasetInfo;
    }
    @Override
    public DatasetInformation updateDatasetInfo(int datasetId)
    {
         
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
                if (g.getGeneList().contains(geneNamesArr[x])) {
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
        datasetInfo.setGeneTabelData(geneTableData);
        datasetInfo.setRowGroupsNames(rowGroupsNames);
        String pass = this.getServletContext().getRealPath("/");
        HMGen HMG = new HMGen(pass+"/js",jDataset);
        datasetInfo.setPass(HMG.getPass());
        return datasetInfo;   
    }

//    @Override
//    public LineChartResults computeLineChart(int datasetId) {
//        LineChartResults lcResults = new LineChartResults();
//        lcResults.setDatasetId(datasetId);
//        String[] geneNames = dataset.getGeneNamesArr();
//        String[] colours = dataset.getGeneColorArr();//new String[dataset.getRowIds().length];
//
//        Number[] pointsArr[] = new Number[dataset.getDataLength()][dataset.getDataWidth()];
//        for (int x = 0; x < dataset.getMemberMaps().size(); x++) {
//
//            Number[] updatedRow = dataset.getMemberMaps().get(x);//new Number[rowData.length];
//            pointsArr[x] = updatedRow;
//
//        }
//        lcResults.setColours(colours);
//        lcResults.setGeneNames(geneNames);
//        lcResults.setLineChartPoints(pointsArr);
//        return lcResults;
//    }
    
    @Override
    public LineChartResults computeLineChart(int datasetId) {
        LineChartResults lcResults = new LineChartResults();
        lcResults.setDatasetId(datasetId);
//        String[] geneNames = dataset.getGeneNamesArr();
//        String[] colours = dataset.getGeneColorArr();//new String[dataset.getRowIds().length];

//        Number[] pointsArr[] = new Number[dataset.getDataLength()][dataset.getDataWidth()];
//        for (int x = 0; x < dataset.getMemberMaps().size(); x++) {
//
//            Number[] updatedRow = dataset.getMemberMaps().get(x);//new Number[rowData.length];
//            pointsArr[x] = updatedRow;
//
//        }
        Map<String,Number[][]> indicesGroup = new TreeMap<String,Number[][]>();
        List<Group> gList = dataset.getRowGroups();
        
        for(Group gr : gList)
        {
           Number[][] pointAr =  new Number[gr.getIndices().size()][dataset.getDataWidth()];
           int x = 0;
           for(int index:gr.getIndices()){            
                Number[] updatedRow = dataset.getMemberMaps().get(index);//new Number[rowData.length];
                pointAr[x] = updatedRow;
                x++;
            }
           indicesGroup.put(gr.getColor(),pointAr);
        }
        lcResults.setIndicesGroup(indicesGroup);
//        lcResults.setColours(colours);
//        lcResults.setGeneNames(geneNames);
//        lcResults.setLineChartPoints(pointsArr);
        return lcResults;
    }

    @Override
    public SomClusteringResults computeSomClustering(int datasetId) throws IllegalArgumentException {

        results = somClustUtil.initHC(jDataset, 0, "UPGMA", true, dataset.getId());
        results = somClustUtil.initSelectedNodes(results);
        TreeMap<String, String> toolTipsMap = somClustUtil.initToolTips(results.getSideTree(), dataset.getGeneIndexNameMap());
        results.setToolTips(toolTipsMap);
        results.setColsNames(jDataset.getColumnIds());
        results.setGeneNames(jDataset.getRowIds());
        return results;
    }

    //	/**
    //	 * Escape an html string. Escaping data received from the client helps to
    //	 * prevent cross-site script vulnerabilities.
    //	 *
    //	 * @param html the html string to escape
    //	 * @return the escaped string
    //	 */
    //	private String escapeHtml(String html) {
    //		if (html == null) {
    //			return null;
    //		}
    //		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
    //				.replaceAll(">", "&gt;");
    //       
    //
    @Override
    public PCAResults computePCA(int datasetId) {      
        PCAResults res = pcaUtil.getPCAResults(jDataset,dataset,0,1);
        res.setDatasetId(datasetId);
        return res;
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
        @Override
    public RankResult computeRank(int datasetId) {
        String type = "TwoClassUnPaired";
        int perm = 400;
        int seed = 838809932;
        int[] col1 = jDataset.getColumnGroups().get(1).getMembers();
        int[] col2 = jDataset.getColumnGroups().get(2).getMembers();
        boolean log2 = true;
        ComputeRank cr = new ComputeRank(jDataset);
        ArrayList<RPmodel> jResults= cr.createResult(type, perm, seed, col1, col2, log2);
        RankResult rankResults = rankUtil.handelRankTable(jResults);
        rankResults.setDatasetId(datasetId);
        System.out.println("Rank is handeled");
        return rankResults;
    }
        
        @Override
        public Boolean createGroup(int datasetId,String name,String color,String type,int[] selection)
        {
            
            no.uib.jexpress_modularized.core.dataset.Group jG = new no.uib.jexpress_modularized.core.dataset.Group("Created Group "+name, generatRandColor(), new Selection(Selection.TYPE.OF_ROWS, selection));
            jG.setActive(true);
            jDataset.addRowGroup(jG);
             String hex = "#" + Integer.toHexString(jG.getColor().getRGB()).substring(2);
                Group g = new Group();
                g.setType("Row");
                g.setColor(hex);
                List<Integer> ind = new ArrayList<Integer>();
                for (int x : jG.getMembers()) {
                    ind.add(x);
                }
                g.setIndices(ind);

                g.setGeneList(util.initGroupGeneList(dataset, jG.getMembers()));
                g.setId(jG.getName());
                dataset.addRowGroup(g);
                System.out.println("done !!!");
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
}
