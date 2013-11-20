package web.jexpress.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;

/**
 * The async counterpart of <code>GreetingService</code>.
 * <HC>
 * <LC>
 * @param str
 */
public interface GreetingServiceAsync {
 
    public void loadDataset(int datasetId,AsyncCallback<DatasetInformation> asyncCallback);
    public void computeLineChart(int datasetId,AsyncCallback<LineChartResults> asyncCallback);
    public void computeSomClustering(String str,AsyncCallback<SomClusteringResults> asyncCallback);
    
   
}
