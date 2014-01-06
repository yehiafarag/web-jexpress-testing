package web.jexpress.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import web.jexpress.shared.beans.ImgResult;
import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.beans.PCAResults;
import web.jexpress.shared.beans.RankResult;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;

/**
 * The async counterpart of <code>GreetingService</code>.
 * <HC>
 * <LC>
 * 
 */
public interface GreetingServiceAsync {
 
    public void loadDataset(int datasetId,AsyncCallback<DatasetInformation> asyncCallback);
    public void computeLineChart(int datasetId,AsyncCallback<LineChartResults> asyncCallback);
    public void computeSomClustering(int datasetId,AsyncCallback<SomClusteringResults> asyncCallback);
    public void computeHeatmap(int datasetId,List<String>indexer,AsyncCallback<ImgResult> asyncCallback );
    public void computePCA(int datasetId, AsyncCallback<PCAResults> asyncCallback);
    public void computeRank(int datasetId, AsyncCallback<RankResult> asyncCallback);
    public void createGroup(int datasetId, String name, String color, String type, int[] selection, AsyncCallback<Boolean> asyncCallback);
    public void updateDatasetInfo(int datasetId, AsyncCallback<DatasetInformation> asyncCallback);
}
