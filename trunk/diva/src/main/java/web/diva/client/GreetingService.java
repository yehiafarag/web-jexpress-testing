package web.diva.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

    //load dataset information
    TreeMap<Integer,String> getAvailableDatasets();

    DatasetInformation loadDataset(int datasetId);

    LineChartResult computeLineChart(int datasetId,double w,double h);

    SomClusteringResult computeSomClustering(int datasetId, int linkage, int distanceMeasure) throws IllegalArgumentException;

    HeatMapImageResult computeHeatmap(int datasetId, ArrayList<String> indexer, ArrayList<String> colIndexer);

    PCAImageResult computePCA(int datasetId, int comI, int comII);

    RankResult computeRank(int datasetId, String perm, String seed, String[] colGropNames, String log2);

    Boolean createRowGroup(int datasetId, String name, String color, String type, int[] selection);

    Boolean createColGroup(int datasetId, String name, String color, String type, String[] selection);

    Integer createSubDataset(String name, int[] selection);

    DatasetInformation updateDatasetInfo(int datasetId);

    DatasetInformation activateGroups(int datasetId, String[] rowGroups, String[] colGroups);
    String exportData(int datasetId, String rowGroups);

    Integer saveDataset(int datasetId, String newName);

    LinkedHashMap<String, String>[] getGroupsPanelData(int datasetId);

    String[] getPcaColNames(int datasetId);

    LinkedHashMap<String, String> getColNamesMaps(int datasetId);

    String updateLineChartSelection(int datasetId, int[] selection,double w,double h);

    PCAImageResult updatePCASelection(int datasetId,int[]subSelectionData, int[] selection, boolean zoom, boolean selectAll,double w,double h);

}
