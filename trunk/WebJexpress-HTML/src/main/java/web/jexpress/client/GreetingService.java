package web.jexpress.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
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

    DatasetInformation loadDataset(int datasetId);

    LineChartResults computeLineChart(int datasetId);

    SomClusteringResults computeSomClustering(int datasetId) throws IllegalArgumentException;
    ImgResult computeHeatmap(int datasetId,List<String>indexer);
    
    PCAResults computePCA(int datasetId);
    
    RankResult computeRank(int datasetId);
    
    Boolean createGroup(int datasetId,String name,String color,String type,int[] selection);

    DatasetInformation updateDatasetInfo(int datasetId);
    
    
}
