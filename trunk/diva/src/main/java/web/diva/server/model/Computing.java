/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.rank.computation.ComputeRank;
import no.uib.jexpress_modularized.rank.computation.RPResult;
import web.diva.server.dal.DB;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.HeatMapImgResult;
import web.diva.shared.beans.LineChartResults;
import web.diva.shared.beans.PCAImageResults;
import web.diva.shared.beans.PCAPoint;
import web.diva.shared.beans.PCAResults;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResults;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 *
 * @author yehia Farag
 */
public class Computing {

    private final DB database = new DB();
    private final DivaUtil util = new DivaUtil();
    private final GroupColorUtil colGen = new GroupColorUtil();

    /*
     * this method to initialize the all datasets and convert them from text files to serialized files
     * this method will be called at the startup of the application 
     */
    public void initDivaDatasets(String fileFolderPath) {
        database.initDivaDatasets(fileFolderPath);
    }

    /*
     *this method is used to get the available datasets from file system layer (to be replaced by DB later) 
     * 
     */
    public TreeMap<Integer, String> getAvailableDatasetsMap(String path) {
        TreeMap<Integer, String> datasetsMap = database.getAvailableDatasets(path);
        return datasetsMap;
    }
    /*
     *this method is used to get the available computing files from file system layer (to be replaced by DB later) 
     * 
     */

    public Set< String> getAvailableComputingFileList(String path) {
        Set<String> datasetsMap = database.getAvailableComputingFileList(path);
        return datasetsMap;
    }

    /*
     *
     *this method is used in init selected divaDataset from the file system 
     *
     */
    public DivaDataset getDataset(int datasetId) {
        DivaDataset divaDataset = null;
        try {
            divaDataset = database.getDivaDataset(datasetId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return divaDataset;

    }

    /*
     *
     *this method is used to load selected divaDataset from the file system 
     *
     */
    public DivaDataset getDivaDataset(int datasetId) {
        DivaDataset divaDataset = null;
        try {
            divaDataset = database.getDivaDataset(datasetId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return divaDataset;

    }

    /*
     * 
     *this method to update the dataset information on adding groups or loading datasets
     *
     *
     */
    public DatasetInformation updateDatasetInfo(int datasetId, DivaDataset divaDataset, String path, String imgColorName) {
        String[] geneTableData[] = new String[divaDataset.getRowGroups().size() + 1][divaDataset.getRowIds().length];
        //init gene names with index
        String[] geneNamesArr = divaDataset.getGeneNamesArr();
        geneTableData[0] = divaDataset.getGeneNamesArr();
        divaDataset.setGeneColorArr(util.initColorArr(geneNamesArr, divaDataset.getRowGroups()));
        int index = 1;
        String[][] rowGroupsNames = new String[divaDataset.getRowGroups().size() - 1][];

        // init groups name and images
        for (int x = 0; x < rowGroupsNames.length; x++) {
            Group g = divaDataset.getRowGroups().get(x + 1);
            String color = colGen.getImageColor(g.getHashColor(), path, imgColorName + g.getName());
            String[] groupFields = new String[]{g.getName(), color};
            rowGroupsNames[x] = groupFields;
        }

        for (Group g : divaDataset.getRowGroups()) {
            String[] col = new String[geneNamesArr.length];
            for (int x = 0; x < geneNamesArr.length; x++) {
                String color = "#FFFFFF";
                if (g.isActive() && g.getGeneList().contains(geneNamesArr[x])) {
                    for (String[] groupField : rowGroupsNames) {
                        if (groupField[0].equalsIgnoreCase(g.getName())) {
                            color = groupField[1];
                        }
                    }
                }
                col[x] = color;
            }
            geneTableData[index] = col;
            index++;

        }
        DatasetInformation datasetInfo = new DatasetInformation();
        datasetInfo.setId(datasetId);
        datasetInfo.setRowsNumb(divaDataset.getDataLength());
        datasetInfo.setColNumb(divaDataset.getDataWidth());
        datasetInfo.setRowGroupsNumb(divaDataset.getRowGroups().size() - 1);
        datasetInfo.setColGroupsNumb(divaDataset.getColumnGroups().size() - 1);
        datasetInfo.setDatasetInfo(divaDataset.getInfoHeaders()[0]);
        LinkedHashMap<String, String> colNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < divaDataset.getColumnIds().length; x++) {
            colNamesMap.put("" + x, divaDataset.getColumnIds()[x]);
        }
        datasetInfo.setGeneTabelData(geneTableData);
        datasetInfo.setRowGroupsNames(rowGroupsNames);
        datasetInfo.setColNamesMap(colNamesMap);
        return datasetInfo;
    }

    /*
     *this method is to activate groups 
     *
     *
     */
    public DivaDataset activateGroups(int datasetId, String[] rowGroups, String[] colGroups, DivaDataset divaDataset) {

        if (rowGroups != null && rowGroups.length > 0) {
            List<no.uib.jexpress_modularized.core.dataset.Group> updatedActiveGroupList = new ArrayList<no.uib.jexpress_modularized.core.dataset.Group>();
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getRowGroups()) {
                g.setActive(false);
                updatedActiveGroupList.add(g);
            }
            divaDataset.getRowGroups().clear();
            divaDataset.getRowGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();

            for (String str : rowGroups) {
                String gName = str.split(",")[1];
                if (gName.equalsIgnoreCase("ALL")) {
                    updatedActiveGroupList.clear();
                    for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getRowGroups()) {
                        if (g.getName().equalsIgnoreCase("ALL")) {
                            g.setActive(true);
                        } else {
                            g.setActive(false);
                        }
                        updatedActiveGroupList.add(g);
                    }
                    break;

                } else {

                    for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getRowGroups()) {
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
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                g.setActive(false);
                updatedActiveGroupList.add(g);
            }
            divaDataset.getColumnGroups().clear();
            divaDataset.getColumnGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();

            for (String str : colGroups) {
                String gName = str.split(",")[1];
                if (gName.equalsIgnoreCase("ALL")) {
                    updatedActiveGroupList.clear();
                    for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                        if (g.getName().equalsIgnoreCase("ALL")) {
                            g.setActive(true);
                        } else {
                            g.setActive(false);
                        }
                        updatedActiveGroupList.add(g);
                    }
                    break;

                } else {

                    for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                        if (g.getName().equalsIgnoreCase(gName)) {
                            g.setActive(true);
                        }
                        updatedActiveGroupList.add(g);
                    }
                }
            }
            divaDataset.getColumnGroups().clear();
            divaDataset.getColumnGroups().addAll(updatedActiveGroupList);
            updatedActiveGroupList.clear();

        }
        return divaDataset;

    }

    /*
     *
     *this method is to create row groups
     *
     */
    public DivaDataset createRowGroup(int datasetId, String name, String color, String type, int[] selection, DivaDataset divaDataset) {

        List<Group> updatedActiveGroupList = new ArrayList<Group>();
        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
            no.uib.jexpress_modularized.core.dataset.Group g = divaDataset.getRowGroups().get(x);
            if (x == (divaDataset.getRowGroups().size() - 1)) {
                g.setActive(true);
            } else {
                g.setActive(false);
            }
            updatedActiveGroupList.add(g);
        }
        divaDataset.getRowGroups().clear();
        divaDataset.getRowGroups().addAll(updatedActiveGroupList);
        List<Group> updatedDevaActiveGroupList = new ArrayList<Group>();
        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
            Group g = divaDataset.getRowGroups().get(x);
            if (x == (divaDataset.getRowGroups().size() - 1)) {
                g.setActive(true);
            } else {
                g.setActive(false);
            }
            updatedDevaActiveGroupList.add(g);
        }
        divaDataset.getRowGroups().clear();
        divaDataset.getRowGroups().addAll(updatedDevaActiveGroupList);
        Color c = null;
        if (color == null || color.equals("")) {
            c = generatRandColor();
        } else {
            c = hex2Rgb(color);
        }
        Selection.TYPE s = Selection.TYPE.OF_ROWS;
        Group jG = new Group(name, c, new Selection(s, selection));
        jG.setActive(true);
        jG.setGeneList(util.initGroupGeneList(divaDataset.getGeneIndexNameMap(), jG.getMembers()));
        divaDataset.addRowGroup(jG);
        return divaDataset;
    }

    public DivaDataset createColGroup(int datasetId, String name, String color, String type, String[] strSelection, DivaDataset divaDataset) {
        if (strSelection == null || strSelection.length == 0) {
            return null;
        }
        int[] selection = new int[strSelection.length];
        for (int x = 0; x < strSelection.length; x++) {
            selection[x] = Integer.valueOf(strSelection[x]);
        }
        Color c = null;
        if (color == null || color.equals("")) {
            c = generatRandColor();
        } else {
            c = hex2Rgb(color);
        }
        Selection.TYPE s = null;
        s = Selection.TYPE.OF_COLUMNS;
        no.uib.jexpress_modularized.core.dataset.Group jG = new no.uib.jexpress_modularized.core.dataset.Group(name, c, new Selection(s, selection));
        jG.setActive(true);
        divaDataset.addColumnGroup(jG);
        return divaDataset;
    }

    /*
     *method to create and store sub-dataset
     *
     *
     */
    public Integer createSubDataset(String name, int[] selection, String path, DivaDataset divaDataset, String newDsName) {
        TreeMap<Integer, String> datasetsMap = getAvailableDatasetsMap(path);
        int id = datasetsMap.lastKey() + 1;
        double[][] newdata = new double[selection.length][];
        String[] newRowIds = new String[selection.length];
        boolean[][] newMissingMeasurments = new boolean[selection.length][];
        for (int x = 0; x < selection.length; x++) {
            double[] row = divaDataset.getData()[selection[x]];
            newdata[x] = row;
            newRowIds[x] = divaDataset.getRowIds()[selection[x]];
            boolean[] mm = divaDataset.getMissingMeasurements()[selection[x]];
            newMissingMeasurments[x] = mm;
        }

        DivaDataset newDS = new DivaDataset(newdata, newRowIds, divaDataset.getColumnIds());
        newDS.setColumnIds(divaDataset.getColumnIds());
        newDS.setMissingMeasurements(newMissingMeasurments);
        newDS.addRowAnnotationNameInUse(divaDataset.getInfoHeaders()[0]);
        newDS.setName(newDsName);
        database.setDataset(newDS, id);
        return id;

    }
    /*
     * method to save text dataset 
     *
     ***/

    public void saveTxtDataset(int datasetId, DivaDataset divaDataset) {
        database.setDataset(divaDataset, datasetId);
    }

    /*
     * method to save dataset 
     *
     ***/
    public Integer saveDataset(int datasetId, String newName, DivaDataset divaDataset, String path) {
        TreeMap<Integer, String> datasetsMap = getAvailableDatasetsMap(path);
        int id = datasetsMap.lastKey() + 1;
        divaDataset.setName(newName);
        database.setDataset(divaDataset, id);
        return id;
    }

    /*
     *this method to get the columns name map to use it for activate column groups panel
     */
    public LinkedHashMap<String, String> getColNamesMaps(DivaDataset divaDataset) {
        LinkedHashMap<String, String> colNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < divaDataset.getColumnIds().length; x++) {
            colNamesMap.put("" + x, divaDataset.getColumnIds()[x]);
        }
        return colNamesMap;
    }

    /*
     *
     * this method to get lables for pca computing panel selection
     */
    public String[] getPcaColNames(DivaDataset divaDataset) {
        String[] pcaColNames = new String[divaDataset.getColumnIds().length];
        for (int x = 0; x < pcaColNames.length; x++) {
            pcaColNames[x] = "Principal Component nr " + x;
        }
        return pcaColNames;
    }

    /*
     *
     * this method to get initial information required for activate group panel
     *
     */
    public LinkedHashMap<String, String>[] getGroupsPanelData(DivaDataset divaDataset) {
        LinkedHashMap<String, String>[] activeGroupsData;
        activeGroupsData = new LinkedHashMap[2];

        LinkedHashMap<String, String> rowGroupsNamesMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> colGroupsNamesMap = new LinkedHashMap<String, String>();
        for (int x = 0; x < divaDataset.getRowGroups().size(); x++) {
            rowGroupsNamesMap.put(divaDataset.getRowGroups().get(x).isActive() + "," + divaDataset.getRowGroups().get(x).getName(), divaDataset.getRowGroups().get(x).getName());
        }
        for (int x = 0; x < divaDataset.getColumnGroups().size(); x++) {
            colGroupsNamesMap.put(divaDataset.getColumnGroups().get(x).isActive() + "," + divaDataset.getColumnGroups().get(x).getName(), divaDataset.getColumnGroups().get(x).getName());
        }
        activeGroupsData[0] = rowGroupsNamesMap;
        activeGroupsData[1] = colGroupsNamesMap;

        return activeGroupsData;
    }

    private final Random rand = new Random();
    /*
     * method to generate random color in case the user didnt select color when he create the group
     */

    private Color generatRandColor() {

        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;
    }
    /*
     *method to convert hash string color to  awt color
     **/

    private Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    /**
     * ***** analysis methods ********
     */
    /*
     *
     * this method to compute SomClustring 
     *
     */
    private final SOMClustUtil somClustUtil = new SOMClustUtil();

    public SomClusteringResults computeSomClustering(int datasetId, int linkage, int distanceMeasure, DivaDataset divaDataset) throws IllegalArgumentException {
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

        SomClusteringResults results = somClustUtil.initHC(divaDataset, distanceMeasure, linkageStr, true, divaDataset.getId());

        results = somClustUtil.initSelectedNodes(results);
        HashMap<String, String> toolTipsMap = somClustUtil.initToolTips(results.getSideTree(), divaDataset.getGeneIndexNameMap());
        HashMap<String, String> topToolTipsMap = somClustUtil.initTopToolTips(results.getTopTree());
        results.setToolTips(toolTipsMap);
        results.setTopToolTips(topToolTipsMap);
        results.setColsNames(divaDataset.getColumnIds());
        results.setGeneNames(divaDataset.getRowIds());
        return results;
    }

    /*
     * this method to compute the clustering heatmap based on indexes from the clustering tree
     */
    public HeatMapImgResult computeHeatmap(int datasetId, List<String> indexer, List<String> colIndexer, DivaDataset divaDataset, String path, String hmImage) {
        HMGen hmGenerator = new HMGen(path, divaDataset, indexer, colIndexer, hmImage);
        HeatMapImgResult imge = hmGenerator.getHeatMapResults();
        return imge;
    }

    private final LineChartGenerator lcg = new LineChartGenerator();

    /*
     *this method to compute linechart 
     */
    public LineChartResults computeLineChart(int datasetId, double w, double h, DivaDataset divaDataset, String path, String lineChartImage) {
        LineChartResults lcResults = new LineChartResults();
        lcResults.setDatasetId(datasetId);
        Number[] pointsArr[] = new Number[divaDataset.getDataLength()][divaDataset.getDataWidth()];
        for (int x = 0; x < divaDataset.getMembersMap().size(); x++) {

            Number[] updatedRow = divaDataset.getMembersMap().get(x);
            pointsArr[x] = updatedRow;

        }
        divaDataset.setLineChartPointArr(pointsArr);
        return lcResults;
    }

    /*
     * this method to update linechart selection
     */
    public String updateLineChartSelection(int datasetId, int[] selection, double w, double h, DivaDataset divaDataset, String path, String lineChartImage) {
        if (selection == null) {
            int z = 0;
            selection = new int[divaDataset.getGeneIndexNameMap().keySet().size()];
            for (int x : divaDataset.getGeneIndexNameMap().keySet()) {
                selection[z] = x;
                z++;
            }

        }
        return lcg.generateChart(path, divaDataset.getLineChartPointArr(), divaDataset.getGeneNamesArr(), divaDataset.getGeneColorArr(), divaDataset.getColumnIds(), selection, lineChartImage, w, h);
    }

    private final PCAUtil pcaUtil = new PCAUtil();
    /*
     * pca computing method
     */

    public PCAResults computePCA(int datasetId, int comI, int comII, DivaDataset divaDataset) {
        PCAResults PCAResult = pcaUtil.getPCAResults(divaDataset, comI, comII);
        return PCAResult;
    }

    private final PCAGenerator pcaGen = new PCAGenerator();
    /*
     * this method for pca update selection
     */

    public PCAImageResults updatePCASelection(int datasetId, int[] subSelectionData, int[] selection, boolean zoom, boolean selectAll, double w, double h, DivaDataset divaDataset, PCAResults pCAResult, String path, String pcaChartImage) {
        PCAImageResults pcaImgResults = pcaGen.generateChart(path, pCAResult, subSelectionData, selection, zoom, selectAll, pcaChartImage, w, h, divaDataset);
        pcaImgResults.setDatasetId(datasetId);
        Object[] obj = pcaUtil.getTooltips(pcaImgResults, pCAResult.getPoints());
        HashMap<String, String> tooltips = (HashMap<String, String>) obj[0];
        PCAPoint[] indexeMap = (PCAPoint[]) obj[1];
        pcaImgResults.setXyName(tooltips);
        pcaImgResults.setIndexeMap(indexeMap);
        return pcaImgResults;

    }

    private final RankUtil rankUtil = new RankUtil();
    /*
     * this method to compute rank product 
     */

    public RankResult computeRank(int datasetId, String perm, String seed, String[] colGropNames, String log2, DivaDataset divaDataset) {
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
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                if (colGropNames[0].split(",")[1].equalsIgnoreCase(g.getName())) {
                    col1 = g.getMembers();
                }
            }
        } else if (colGropNames.length == 2) {
            type = "TwoClassUnPaired";
            for (no.uib.jexpress_modularized.core.dataset.Group g : divaDataset.getColumnGroups()) {
                if (colGropNames[0].split(",")[1].equalsIgnoreCase(g.getName())) {
                    col1 = g.getMembers();
                }
                if (colGropNames[1].split(",")[1].equalsIgnoreCase(g.getName())) {
                    col2 = g.getMembers();
                }
            }
        }
        ComputeRank cr = new ComputeRank(divaDataset);
        ArrayList<RPResult> jResults = cr.createResult(type, iPerm, iSeed, col1, col2, log);
        RankResult rankResults = rankUtil.handelRankTable(jResults);
        rankResults.setDatasetId(datasetId);
        return rankResults;
    }

    /*
     * this method is to store somClust computing results
     */
    public void saveSomClustResult(String id, SomClusteringResults results) {
        database.saveSomClustResult(id, results);
    }
    /*
     * this method is to get somClust computing results
     */

    public SomClusteringResults getSomClustResult(String id) {
        SomClusteringResults results = database.getSomClustResult(id);
        return results;
    }

    /*
     * this method is to store pca computing results
     */
    public void savePCAResult(String id, PCAResults results) {
        database.savePCAResult(id, results);
    }
    /*
     * this method is to get PCA computing results
     */

    public PCAResults getPCAResult(String id) {
        PCAResults results = database.getPCAResult(id);
        return results;
    }

    /*
     * this method is to store pca computing results
     */
    public void saveRankResult(String id, RankResult results) {
        database.saveRankResult(id, results);
    }
    /*
     * this method is to get PCA computing results
     */

    public RankResult getRankResult(String id) {
        RankResult results = database.getRankResult(id);
        return results;
    }
    /*
     *this method to export data to textfile
     */

    public String exportDataTotext(DivaDataset divaDataset, String groupName, String path, String url, String textFileName) {
        String gName = groupName.split(",")[1];
        Group g = null;
        for (Group gr : divaDataset.getRowGroups()) {
            if (gr.getName().equalsIgnoreCase(gName)) {
                g = gr;
                break;
            }
        }
        return util.exportDataTotext(divaDataset, g, path, url, textFileName);

    }

}
