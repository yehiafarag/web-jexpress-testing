package web.jexpress.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import java.util.Map;
import web.jexpress.shared.beans.ImgResult;
import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.beans.PCAResults;
import web.jexpress.shared.beans.RankResult;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
    //load dataset information
    Map<Integer,String> getAvailableDatasets();

    DatasetInformation loadDataset(int datasetId);

    LineChartResults computeLineChart(int datasetId);

    SomClusteringResults computeSomClustering(int datasetId,int linkage,int distanceMeasure) throws IllegalArgumentException;
    ImgResult computeHeatmap(int datasetId,List<String>indexer);
    
    PCAResults computePCA(int datasetId,int comI,int comII);
    
    RankResult computeRank(int datasetId,String perm,String seed,String[] colGropNames,String log2);
    
    Boolean createGroup(int datasetId,String name,String color,String type,int[] selection);

    DatasetInformation updateDatasetInfo(int datasetId);
    
    DatasetInformation activateGroups(int datasetId,String[] rowGroups,String[] colGroups);
    
    
}
