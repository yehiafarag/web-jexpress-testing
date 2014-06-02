package web.diva.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import web.diva.shared.beans.HeatMapImageResult;
import web.diva.shared.beans.LineChartResult;
import web.diva.shared.beans.PCAImageResult;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResult;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * The async counterpart of <code>GreetingService</code>.
 * 
 *
 */
public interface GreetingServiceAsync {

    public void getAvailableDatasets(AsyncCallback<TreeMap<Integer,String>> datasetResults);

    public void loadDataset(int datasetId, AsyncCallback<DatasetInformation> asyncCallback);

    public void computeLineChart(int datasetId,double w,double h, AsyncCallback<LineChartResult> asyncCallback);

    public void computeSomClustering(int datasetId, int linkage, int distanceMeasure, AsyncCallback<SomClusteringResult> asyncCallback);

    public void computeHeatmap(int datasetId, ArrayList<String> indexer, ArrayList<String> colIndexer, AsyncCallback<HeatMapImageResult> asyncCallback);

    public void computeRank(int datasetId, String perm, String seed, String[] colGropNames, String log2, AsyncCallback<RankResult> asyncCallback);

    public void createRowGroup(int datasetId, String name, String color, String type, int[] selection, AsyncCallback<Boolean> asyncCallback);

    public void createColGroup(int datasetId, String name, String color, String type, String[] selection, AsyncCallback<Boolean> asyncCallback);

    public void createSubDataset(String name, int[] selection, AsyncCallback<Integer> asyncCallback);

    public void updateDatasetInfo(int datasetId, AsyncCallback<DatasetInformation> asyncCallback);

    public void activateGroups(int datasetId, String[] rowGroups, String[] colGroups, AsyncCallback<DatasetInformation> asyncCallback);
    
    public void exportData(int datasetId, String rowGroups, AsyncCallback<String> asyncCallback);

    public void saveDataset(int datasetId, String newName, AsyncCallback<Integer> asyncCallback);

    public void getGroupsPanelData(int datasetId, AsyncCallback<LinkedHashMap<String, String>[]> asyncCallback);

    public void getPcaColNames(int datasetId, AsyncCallback<String[]> asyncCallback);

    public void getColNamesMaps(int datasetId, AsyncCallback<LinkedHashMap<String, String>> asyncCallback);

    public void updateLineChartSelection(int datasetId, int[] selection,double w,double h, AsyncCallback<String> asyncCallback);

    public void updatePCASelection(int datasetId,int[]subSelectionData, int[] selection, boolean zoom, boolean selectAll,double w,double h, AsyncCallback<PCAImageResult> asyncCallback);

    public void computePCA(int datasetId, int comI, int comII, AsyncCallback<PCAImageResult> asyncCallback);

}
