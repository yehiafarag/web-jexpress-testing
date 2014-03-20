package web.diva.server;

import web.diva.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpSession;
import web.diva.server.model.Computing;
import web.diva.shared.beans.LineChartResults;
import web.diva.shared.beans.PCAResults;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResults;
import web.diva.shared.model.core.model.dataset.DatasetInformation;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.HeatMapImgResult;
import web.diva.shared.beans.PCAImageResults;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    private DivaDataset divaDataset;
    private DatasetInformation datasetInfo;
    private String path;
    private PCAResults PCAResult;

    private final DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    private final Calendar cal = Calendar.getInstance();

    private final String imgColorName = "groupCol" + dateFormat.format(cal.getTime()).replace(":", " ");

    private final String lineChartImage = "lineChartImg " + dateFormat.format(cal.getTime()).replace(":", " ");

    private final String pcaChartImage = "pcachart " + dateFormat.format(cal.getTime()).replace(":", " ");
    private final String hmImage = "heatMapImg" + dateFormat.format(cal.getTime()).replace(":", " ");
    private boolean initSession = true;

    private final Computing compute = new Computing();

    @Override
    public Map<Integer, String> getAvailableDatasets() {
        if (initSession) {
            HttpSession httpSession = getThreadLocalRequest().getSession();
            httpSession.setAttribute("imgColorName", imgColorName);
            httpSession.setAttribute("lineChartImage", lineChartImage);
            httpSession.setAttribute("pcaChartImage", pcaChartImage);
            httpSession.setAttribute("hmImage", hmImage);
            initSession = false;
        }

        path = this.getServletContext().getInitParameter("fileFolder");
        TreeMap<Integer, String> datasetsMap = compute.getAvailableDatasetsMap(path);
        return datasetsMap;
    }

    @Override
    public DatasetInformation loadDataset(int datasetId) {
        try {
            divaDataset = compute.getDataset(datasetId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        datasetInfo = compute.updateDatasetInfo(datasetId, divaDataset, path, imgColorName);
        return datasetInfo;
    }

    @Override
    public DatasetInformation updateDatasetInfo(int datasetId) {
        datasetInfo = compute.updateDatasetInfo(datasetId, divaDataset, path, imgColorName);
        return datasetInfo;
    }

    @Override
    public DatasetInformation activateGroups(int datasetId, String[] rowGroups, String[] colGroups) {
        divaDataset = compute.activateGroups(datasetId, rowGroups, colGroups, divaDataset);
        datasetInfo = compute.updateDatasetInfo(datasetId, divaDataset, path, imgColorName);
        return datasetInfo;
    }

    @Override
    public SomClusteringResults computeSomClustering(int datasetId, int linkage, int distanceMeasure) throws IllegalArgumentException {
        SomClusteringResults results = compute.computeSomClustering(datasetId, linkage, distanceMeasure, divaDataset);
        return results;
    }

    @Override
    public HeatMapImgResult computeHeatmap(int datasetId, List<String> indexer, List<String> colIndexer) {
        HeatMapImgResult imge = compute.computeHeatmap(datasetId, indexer, colIndexer, divaDataset, path, hmImage);//hmGenerator.getHeatMapResults();
        return imge;
    }

    @Override
    public PCAImageResults computePCA(int datasetId, int comI, int comII) {
        PCAResult = compute.computePCA(datasetId, comI, comII, divaDataset);
        PCAImageResults pcaImgResults = new PCAImageResults();
        pcaImgResults.setDatasetId(datasetId);
        pcaImgResults.setXyName(PCAResult.getXyName());
        return pcaImgResults;
    }

    @Override
    public PCAImageResults updatePCASelection(int datasetId, int[] selection, boolean zoom, boolean selectAll, double w, double h) {
        PCAImageResults pcaImgResults = compute.updatePCASelection(datasetId, selection, zoom, selectAll, w, h, divaDataset, PCAResult, path, pcaChartImage);
//                pcaGen.generateChart(path, PCAResult, selection, zoom, selectAll, pcaChartImage, w, h);
//        pcaImgResults.setDatasetId(datasetId);
//        Object[] obj = pcaUtil.getTooltips(pcaImgResults, PCAResult.getPoints());
//        HashMap<String, String> tooltips = (HashMap<String, String>) obj[0];
//        PCAPoint[] indexeMap = (PCAPoint[]) obj[1];
//        pcaImgResults.setXyName(tooltips);
//        pcaImgResults.setIndexeMap(indexeMap);
        return pcaImgResults;

    }

    @Override
    public RankResult computeRank(int datasetId, String perm, String seed, String[] colGropNames, String log2) {
        RankResult rankResults = compute.computeRank(datasetId, perm, seed, colGropNames, log2, divaDataset);
        return rankResults;
    }

    @Override
    public Boolean createRowGroup(int datasetId, String name, String color, String type, int[] selection) {
        divaDataset = compute.createRowGroup(datasetId, name, color, type, selection, divaDataset);
        if (divaDataset != null) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public Boolean createColGroup(int datasetId, String name, String color, String type, String[] strSelection) {
        divaDataset = compute.createColGroup(datasetId, name, color, type, strSelection, divaDataset);
        if (divaDataset != null) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public Integer createSubDataset(String name, int[] selection) {
        String newDsName = "SUB - " + divaDataset.getName() + " - " + name + " - " + dateFormat.format(cal.getTime()).replace(":", " ");
        int id = compute.createSubDataset(name, selection, path, divaDataset, newDsName);//new Dataset(newdata, newRowIds, divaDataset.getColumnIds());
        return id;

    }

    @Override
    public LineChartResults computeLineChart(int datasetId, double w, double h) {
        LineChartResults lcResults = compute.computeLineChart(datasetId, w, h, divaDataset, path, lineChartImage);
        return lcResults;
    }

    @Override
    public String updateLineChartSelection(int datasetId, int[] selection, double w, double h) {
        return compute.updateLineChartSelection(datasetId, selection, w, h, divaDataset, path, lineChartImage);//lcg.generateChart(path, divaDataset.getLineChartPointArr(), divaDataset.getGeneNamesArr(), divaDataset.getGeneColorArr(), divaDataset.getColumnIds(), selection, lineChartImage,w,h);
    }

    @Override
    public Integer saveDataset(int datasetId, String newName) {
        newName = (divaDataset.getName() + " - " + newName + " - " + dateFormat.format(cal.getTime())).replace(":", " ");
        int id = compute.saveDataset(datasetId, newName, divaDataset, path);
        return id;
    }

    @Override
    public LinkedHashMap<String, String>[] getGroupsPanelData(int datasetId) {
        LinkedHashMap<String, String>[] activeGroupsData = compute.getGroupsPanelData(divaDataset);
        return activeGroupsData;
    }

    @Override
    public String[] getPcaColNames(int datasetId) {
        String[] pcaColNames = compute.getPcaColNames(divaDataset);
        return pcaColNames;
    }

    @Override
    public LinkedHashMap<String, String> getColNamesMaps(int datasetId) {
        LinkedHashMap<String, String> colNamesMap = compute.getColNamesMaps(divaDataset);
        return colNamesMap;
    }

}
