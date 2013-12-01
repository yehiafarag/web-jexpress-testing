package web.jexpress.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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

    SomClusteringResults computeSomClustering(String name) throws IllegalArgumentException;
    
    PCAResults computePCA(int datasetId);
    
    RankResult computeRank(int datasetId);

}
