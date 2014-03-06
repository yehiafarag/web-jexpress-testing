package web.diva.server;

import web.diva.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.TreeMap;
import javax.servlet.http.HttpSession;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.rank.computation.ComputeRank;
import no.uib.jexpress_modularized.rank.computation.RPResult;
import web.diva.server.dal.DB;
import web.diva.server.model.GroupColorUtil;
import web.diva.server.model.HMGen;
import web.diva.server.model.JexpressUtil;
import web.diva.server.model.LineChartGenerator;
import web.diva.server.model.PCAGenerator;
import web.diva.server.model.PCAUtil;
import web.diva.server.model.SOMClustUtil;
import web.diva.shared.beans.LineChartResults;
import web.diva.shared.beans.PCAResults;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResults;
import web.diva.shared.model.core.model.dataset.DatasetInformation;
import web.diva.shared.model.core.model.dataset.Group;
import web.diva.server.model.RankUtil;
import web.diva.shared.beans.HeatMapImgResult;
import web.diva.shared.beans.PCAImageResults;
import web.diva.shared.beans.PCAPoint;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    private final JexpressUtil util = new JexpressUtil();
    private Dataset jDataset;
    private web.diva.shared.model.core.model.dataset.Dataset dataset;
    private final SOMClustUtil somClustUtil = new SOMClustUtil();
    private SomClusteringResults results;
    private final PCAUtil pcaUtil = new PCAUtil();
    private final RankUtil rankUtil = new RankUtil();
    private DatasetInformation datasetInfo;
    private final GroupColorUtil colGen = new GroupColorUtil();
    private String path;
    private LineChartGenerator lcg;
    private PCAResults PCAResult;
    private final PCAGenerator pcaGen = new PCAGenerator();

    private final DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    private final Calendar cal = Calendar.getInstance();

    private final String imgColorName = "groupCol" + dateFormat.format(cal.getTime()).replace(":", " ");

    private final String lineChartImage = "lineChartImg " + dateFormat.format(cal.getTime()).replace(":", " ");

    private final String pcaChartImage = "pcachart " + dateFormat.format(cal.getTime()).replace(":", " ");
    private final String hmImage = "heatMapImg" + dateFormat.format(cal.getTime()).replace(":", " ");
    private final DB database = new DB();
    private boolean initSession = true;
    
    @Override
    public Map<Integer, String> getAvailableDatasets() {
        if(initSession){
         HttpSession httpSession = getThreadLocalRequest().getSession();
         httpSession = this.getThreadLocalRequest().getSession();
         httpSession.setAttribute("imgColorName", imgColorName);
         httpSession.setAttribute("lineChartImage", lineChartImage);
         httpSession.setAttribute("pcaChartImage", pcaChartImage);
         httpSession.setAttribute("hmImage", hmImage);
         initSession = false;
        }
        
        path = this.getServletContext().getInitParameter("fileFolder");
        database.loadDatasets(path);
        TreeMap<Integer, String> datasetsMap = database.getAvailableDatasets();
        return datasetsMap;
    }

    @Override
    public DatasetInformation loadDataset(int datasetId) {
        try {
            jDataset = database.getDataset(datasetId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        dataset = util.initWebDataset(jDataset, datasetId);
        datasetInfo = this.updateDatasetInfo(datasetId);
        return datasetInfo;
    }

    @Override
    public DatasetInformation updateDatasetInfo(int datasetId) {
        String[] geneTableData[] = new String[dataset.getRowGroups().size() + 1][dataset.getRowIds().length];
        //init gene names with index
        String[] geneNamesArr = util.initGeneNamesArr(dataset.getGeneIndexNameMap());
        dataset.setGeneNamesArr(geneNamesArr);
        geneTableData[0] = geneNamesArr;
        dataset.setGeneColorArr(util.initColorArr(geneNamesArr, dataset.getRowGroups()));
        int index = 1;
        String[][] rowGroupsNames = new String[jDataset.getRowGroups().size() - 1][];
        path = this.getServletContext().getInitParameter("fileFolder");
        // init groups name and images
        for (int x = 0; x < rowGroupsNames.length; x++) {
            Group g = dataset.getRowGroups().get(x + 1);
            String color = colGen.getImageColor(g.getColor(), path, imgColorName + g.getId());
            String[] groupFields = new String[]{g.getId(), color};
            rowGroupsNames[x] = groupFields;
        }

        for (Group g : dataset.getRowGroups()) {
            String[] col = new String[geneNamesArr.length];
            for (int x = 0; x < geneNamesArr.length; x++) {
                String color = "#FFFFFF";
                if (g.isActive() && g.getGeneList().contains(geneNamesArr[x])) {
                    for (String[] groupField : rowGroupsNames) {
                        if (groupField[0].equalsIgnoreCase(g.getId())) {
                            color = groupField[1];
                        }
                    }
                }
                col[x] = color;
            }
            geneTableData[index] = col;
            index++;

        }
        datasetInfo = new DatasetInformation();
        datasetInfo.setId(datasetId);
        datasetInfo.setRowsNumb(dataset.getDataLength());
        datasetInfo.setColNumb(dataset.getDataWidth());
        datasetInfo.setRowGroupsNumb(dataset.getRowGroups().size() - 1);
        datasetInfo.setColGroupsNumb(dataset.getColumnGroups().size() - 1);
        datasetInfo.setDatasetInfo(dataset.getInfoHeaders()[0]);
        LinkedHashMap<String, String> colNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < dataset.getColumnIds().length; x++) {
            colNamesMap.put("" + x, dataset.getColumnIds()[x]);
        }
        datasetInfo.setGeneTabelData(geneTableData);
        datasetInfo.setRowGroupsNames(rowGroupsNames);
        datasetInfo.setColNamesMap(colNamesMap);
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
    public SomClusteringResults computeSomClustering(int datasetId, int linkage, int distanceMeasure) throws IllegalArgumentException {
        String linkageStr = "WPGMA";
        if (linkage == 0) {
            linkageStr = "SINGLE";
        } else if (linkage == 1) {
            linkageStr = "WPGMA";
        } else if (linkage == 2) {
            linkageStr = "UPGMA";
        } else if (linkage == 3) {
            linkageStr = "COMPLETE";
        }
        results = somClustUtil.initHC(jDataset, distanceMeasure, linkageStr, true, dataset.getId());
        results = somClustUtil.initSelectedNodes(results);
        HashMap<String, String> toolTipsMap = somClustUtil.initToolTips(results.getSideTree(), dataset.getGeneIndexNameMap());
        HashMap<String, String> topToolTipsMap = somClustUtil.initTopToolTips(results.getTopTree());
        results.setToolTips(toolTipsMap);
        results.setTopToolTips(topToolTipsMap);
        results.setColsNames(jDataset.getColumnIds());
        results.setGeneNames(jDataset.getRowIds());
        return results;
    }

    @Override
    public HeatMapImgResult computeHeatmap(int datasetId, List<String> indexer, List<String> colIndexer) {
        HMGen hmGenerator = new HMGen(path, jDataset, indexer, colIndexer, hmImage);
        HeatMapImgResult imge = hmGenerator.getHeatMapResults();
        return imge;
    }

    @Override
    public PCAImageResults computePCA(int datasetId, int comI, int comII) {
        PCAResult = pcaUtil.getPCAResults(jDataset, dataset, comI, comII);
        PCAImageResults pcaImgResults = new PCAImageResults();
        pcaImgResults.setDatasetId(datasetId);
        pcaImgResults.setXyName(PCAResult.getXyName());
        return pcaImgResults;
    }

    @Override
    public PCAImageResults updatePCASelection(int datasetId, int[] selection, boolean zoom, boolean selectAll) {
        PCAImageResults pcaImgResults = pcaGen.generateChart(path, PCAResult, selection, zoom, selectAll, pcaChartImage);
        pcaImgResults.setDatasetId(datasetId);
        Object[] obj = pcaUtil.getTooltips(pcaImgResults, PCAResult.getPoints());
        HashMap<String, String> tooltips = (HashMap<String, String>) obj[0];
        PCAPoint[] indexeMap = (PCAPoint[]) obj[1];
        pcaImgResults.setXyName(tooltips);
        pcaImgResults.setIndexeMap(indexeMap);
        return pcaImgResults;

    }

    @Override
    public RankResult computeRank(int datasetId, String perm, String seed, String[] colGropNames, String log2) {
        String type = "TwoClassUnPaired";
        int iPerm = Integer.valueOf(perm);
        int iSeed = Integer.valueOf(seed);
        boolean log = false;
        if (log2.equalsIgnoreCase("log 2")) {
            log = true;
        }
        int[] col1 = null;
        int[] col2 = null;
        if (colGropNames.length == 1) {
            type = "OneClass";
            for (no.uib.jexpress_modularized.core.dataset.Group g : jDataset.getColumnGroups()) {
                if (colGropNames[0].split(",")[1].equalsIgnoreCase(g.getName())) {
                    col1 = g.getMembers();
                }
            }
        } else if (colGropNames.length == 2) {
            type = "TwoClassUnPaired";
            for (no.uib.jexpress_modularized.core.dataset.Group g : jDataset.getColumnGroups()) {
                if (colGropNames[0].split(",")[1].equalsIgnoreCase(g.getName())) {
                    col1 = g.getMembers();
                }
                if (colGropNames[1].split(",")[1].equalsIgnoreCase(g.getName())) {
                    col2 = g.getMembers();
                }
            }
        }
        ComputeRank cr = new ComputeRank(jDataset);
        ArrayList<RPResult> jResults = cr.createResult(type, iPerm, iSeed, col1, col2, log);
        RankResult rankResults = rankUtil.handelRankTable(jResults);
        rankResults.setDatasetId(datasetId);
        return rankResults;
    }

    @Override
    public Boolean createRowGroup(int datasetId, String name, String color, String type, int[] selection) {

        List<no.uib.jexpress_modularized.core.dataset.Group> updatedActiveGroupList = new ArrayList<no.uib.jexpress_modularized.core.dataset.Group>();

        for (int x = 0; x < jDataset.getRowGroups().size(); x++) {
            no.uib.jexpress_modularized.core.dataset.Group g = jDataset.getRowGroups().get(x);
            if (x == (jDataset.getRowGroups().size() - 1)) {
                g.setActive(true);
            } else {
                g.setActive(false);
            }
            updatedActiveGroupList.add(g);
        }
        jDataset.getRowGroups().clear();
        jDataset.getRowGroups().addAll(updatedActiveGroupList);

        List<Group> updatedDevaActiveGroupList = new ArrayList<Group>();

        for (int x = 0; x < dataset.getRowGroups().size(); x++) {
            Group g = dataset.getRowGroups().get(x);
            if (x == (dataset.getRowGroups().size() - 1)) {
                g.setActive(true);
            } else {
                g.setActive(false);
            }
            updatedDevaActiveGroupList.add(g);
        }

        dataset.getRowGroups().clear();
        dataset.getRowGroups().addAll(updatedDevaActiveGroupList);

        String gColor = "";
        Color c = null;
        if (color == null || color.equals("")) {
            c = generatRandColor();
            gColor = "#" + Integer.toHexString(c.getRGB()).substring(2);
        } else {
            c = hex2Rgb(color);
            gColor = color;
        }
        Selection.TYPE s = Selection.TYPE.OF_ROWS;
        String gType = "Row";

        no.uib.jexpress_modularized.core.dataset.Group jG = new no.uib.jexpress_modularized.core.dataset.Group(name, c, new Selection(s, selection));
        jG.setActive(true);
        jDataset.addRowGroup(jG);
        Group g = new Group();
        g.setType(gType);
        g.setColor(gColor);
        List<Integer> ind = new ArrayList<Integer>();
        for (int x : jG.getMembers()) {
            ind.add(x);
        }
        g.setIndices(ind);
        g.setActive(true);
        g.setGeneList(util.initGroupGeneList(dataset, jG.getMembers()));
        g.setId(jG.getName());
        dataset.addRowGroup(g);
        return true;
    }

    @Override
    public Boolean createColGroup(int datasetId, String name, String color, String type, String[] strSelection) {
        if (strSelection == null || strSelection.length == 0) {
            return false;
        }
        int[] selection = new int[strSelection.length];
        for (int x = 0; x < strSelection.length; x++) {
            selection[x] = Integer.valueOf(strSelection[x]);
        }
        String gColor = "";
        Color c = null;
        if (color == null || color.equals("")) {
            c = generatRandColor();
            gColor = "#" + Integer.toHexString(c.getRGB()).substring(2);
        } else {
            c = hex2Rgb(color);
            gColor = color;
        }
        Selection.TYPE s = null;
        String gType = "";

        s = Selection.TYPE.OF_COLUMNS;
        gType = "Column";

        no.uib.jexpress_modularized.core.dataset.Group jG = new no.uib.jexpress_modularized.core.dataset.Group(name, c, new Selection(s, selection));
        jG.setActive(true);
        jDataset.addColumnGroup(jG);
        Group g = new Group();
        g.setType(gType);
        g.setColor(gColor);
        List<Integer> ind = new ArrayList<Integer>();
        for (int x : jG.getMembers()) {
            ind.add(x);
        }
        g.setIndices(ind);
        g.setId(jG.getName());
        dataset.addColumnGroup(g);
        return true;
    }

    @Override
    public Integer createSubDataset(String name, int[] selection) {
        TreeMap<Integer, String> datasetsMap = database.getAvailableDatasets();
        int id = datasetsMap.lastKey() + 1;
        double[][] newdata = new double[selection.length][];
        String[] newRowIds = new String[selection.length];
        boolean[][] newMissingMeasurments = new boolean[selection.length][];
        for (int x = 0; x < selection.length; x++) {
            double[] row = jDataset.getData()[selection[x]];
            newdata[x] = row;
            newRowIds[x] = jDataset.getRowIds()[selection[x]];
            boolean[] mm = jDataset.getMissingMeasurements()[selection[x]];
            newMissingMeasurments[x] = mm;
        }

        Dataset newDS = new Dataset(newdata, newRowIds, jDataset.getColumnIds());
        newDS.setColumnIds(jDataset.getColumnIds());
        newDS.setMissingMeasurements(newMissingMeasurments);
        newDS.addRowAnnotationNameInUse(jDataset.getInfoHeaders()[0]);
        newDS.setName("SUB - " + jDataset.getName() + " - " + name + " - " + dateFormat.format(cal.getTime()).replace(":", " "));
        database.setDataset(newDS, id);
        return id;

    }

    private final Random rand = new Random();

    private Color generatRandColor() {

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
        String[] rowIds = dataset.getGeneNamesArr();
        String[] colours = dataset.getGeneColorArr();//new String[dataset.getRowIds().length];

        Number[] pointsArr[] = new Number[dataset.getDataLength()][dataset.getDataWidth()];
        for (int x = 0; x < dataset.getMemberMaps().size(); x++) {

            Number[] updatedRow = dataset.getMemberMaps().get(x);//new Number[rowData.length];
            pointsArr[x] = updatedRow;

        }
        lcg = new LineChartGenerator();
        dataset.setLineChartPointArr(pointsArr);
        lcResults.setImageString(lcg.generateChart(path, pointsArr, rowIds, colours, dataset.getColumnIds(), null, lineChartImage));
        return lcResults;
    }

    @Override
    public String updateLineChartSelection(int datasetId, int[] selection) {
        return lcg.generateChart(path, dataset.getLineChartPointArr(), dataset.getGeneNamesArr(), dataset.getGeneColorArr(), dataset.getColumnIds(), selection, lineChartImage);
    }

    @Override
    public Integer saveDataset(int datasetId, String newName) {
        TreeMap<Integer, String> datasetsMap = database.getAvailableDatasets();
        int id = datasetsMap.lastKey() + 1;
        jDataset.setName((jDataset.getName() + " - " + newName + " - " + dateFormat.format(cal.getTime())).replace(":", " "));
        database.setDataset(jDataset, id);

        return id;
    }

    @Override
    public LinkedHashMap<String, String>[] getGroupsPanelData(int datasetId) {
        LinkedHashMap<String, String>[] activeGroupsData;
        activeGroupsData = new LinkedHashMap[2];

        LinkedHashMap<String, String> rowGroupsNamesMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> colGroupsNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < dataset.getRowGroups().size(); x++) {
            rowGroupsNamesMap.put(dataset.getRowGroups().get(x).isActive() + "," + dataset.getRowGroups().get(x).getId(), dataset.getRowGroups().get(x).getId());
        }
        for (int x = 0; x < dataset.getColumnGroups().size(); x++) {
            colGroupsNamesMap.put(dataset.getColumnGroups().get(x).isActive() + "," + dataset.getColumnGroups().get(x).getId(), dataset.getColumnGroups().get(x).getId());
        }
        activeGroupsData[0] = rowGroupsNamesMap;
        activeGroupsData[1] = colGroupsNamesMap;

        return activeGroupsData;
    }

    @Override
    public String[] getPcaColNames(int datasetId) {
        String[] pcaColNames = new String[dataset.getColumnIds().length];
        for (int x = 0; x < pcaColNames.length; x++) {
            pcaColNames[x] = "Principal Component nr " + x;
        }
        return pcaColNames;
    }

    @Override
    public LinkedHashMap<String, String> getColNamesMaps(int datasetId) {
        LinkedHashMap<String, String> colNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < dataset.getColumnIds().length; x++) {
            colNamesMap.put("" + x, dataset.getColumnIds()[x]);
        }
        return colNamesMap;
    }

    public Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

}
