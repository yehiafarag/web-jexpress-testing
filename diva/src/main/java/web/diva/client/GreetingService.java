package web.diva.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import web.diva.shared.beans.HeatMapImgResult;
import web.diva.shared.beans.LineChartResults;
import web.diva.shared.beans.PCAImageResults;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResults;
import web.diva.shared.model.core.model.dataset.DatasetInformation;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

    //load dataset information
    Map<Integer, String> getAvailableDatasets();

    DatasetInformation loadDataset(int datasetId);

    LineChartResults computeLineChart(int datasetId,double w,double h);

    SomClusteringResults computeSomClustering(int datasetId, int linkage, int distanceMeasure) throws IllegalArgumentException;

    HeatMapImgResult computeHeatmap(int datasetId, List<String> indexer, List<String> colIndexer);

    PCAImageResults computePCA(int datasetId, int comI, int comII);

    RankResult computeRank(int datasetId, String perm, String seed, String[] colGropNames, String log2);

    Boolean createRowGroup(int datasetId, String name, String color, String type, int[] selection);

    Boolean createColGroup(int datasetId, String name, String color, String type, String[] selection);

    Integer createSubDataset(String name, int[] selection);

    DatasetInformation updateDatasetInfo(int datasetId);

    DatasetInformation activateGroups(int datasetId, String[] rowGroups, String[] colGroups);

    Integer saveDataset(int datasetId, String newName);

    LinkedHashMap<String, String>[] getGroupsPanelData(int datasetId);

    String[] getPcaColNames(int datasetId);

    LinkedHashMap<String, String> getColNamesMaps(int datasetId);

    String updateLineChartSelection(int datasetId, int[] selection,double w,double h);

    PCAImageResults updatePCASelection(int datasetId, int[] selection, boolean zoom, boolean selectAll,double w,double h);

}
