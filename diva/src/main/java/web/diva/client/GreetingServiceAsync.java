package web.diva.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import web.diva.shared.beans.HeatMapImgResult;
import web.diva.shared.beans.LineChartResults;
import web.diva.shared.beans.PCAImageResults;
import web.diva.shared.beans.PCAResults;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResults;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * The async counterpart of <code>GreetingService</code>.
 * <HC>
 * <LC>
 * 
 */
public interface GreetingServiceAsync {
 
    public void getAvailableDatasets(AsyncCallback<Map<Integer,String> >datasetResults);
    public void loadDataset(int datasetId,AsyncCallback<DatasetInformation> asyncCallback);
    public void computeLineChart(int datasetId,AsyncCallback<LineChartResults> asyncCallback);
    public void computeSomClustering(int datasetId,int linkage,int distanceMeasure,AsyncCallback<SomClusteringResults> asyncCallback);
    public void computeHeatmap(int datasetId,List<String>indexer,List<String>colIndexer,AsyncCallback<HeatMapImgResult> asyncCallback );
    public void computePCA(int datasetId,int comI,int comII, AsyncCallback<PCAImageResults> asyncCallback);
    public void computeRank(int datasetId,String perm,String seed,String[] colGropNames,String log2, AsyncCallback<RankResult> asyncCallback);
    public void createRowGroup(int datasetId, String name, String color, String type, int[] selection, AsyncCallback<Boolean> asyncCallback);
    public void createColGroup(int datasetId, String name, String color, String type, String[] selection, AsyncCallback<Boolean> asyncCallback);
    public void createSubDataset(String name,int[] selection,AsyncCallback<Integer> asyncCallback);
    public void updateDatasetInfo(int datasetId, AsyncCallback<DatasetInformation> asyncCallback);

    public void activateGroups(int datasetId, String[] rowGroups, String[] colGroups, AsyncCallback<DatasetInformation> asyncCallback);

    public void saveDataset(int datasetId, String newName, AsyncCallback<Integer> asyncCallback);

    public void getGroupsPanelData(int datasetId, AsyncCallback<LinkedHashMap<String, String>[]> asyncCallback);

    public void getPcaColNames(int datasetId, AsyncCallback<String[]> asyncCallback);

    public void getColNamesMaps(int datasetId, AsyncCallback<LinkedHashMap<String, String>> asyncCallback);

    public void updateLineChartSelection(int datasetId, int[] selection, AsyncCallback<String> asyncCallback);

    public void updatePCASelection(int datasetId, int[] selection, boolean zoom, boolean selectAll, AsyncCallback<PCAImageResults> asyncCallback);

}
