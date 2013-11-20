package web.jexpress.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import web.jexpress.client.geneTable.view.GeneTable;
import web.jexpress.client.linechart.view.LineChartComp;
import web.jexpress.client.somclust.view.SomClustView;
import web.jexpress.shared.model.core.model.SelectionManager;


import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;

/**
 * Entry point classes define
 * <code>onModuleLoad()</code>.
 */
public class Test_GWT_2_1_0 implements EntryPoint {

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    //private Dataset dataset;
    private final SelectionManager selectionManager = new SelectionManager();
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again."; 
    final Label errorLabel = new Label();
    final Label datasetInfoLabel = new Label();
    
    final Label rowLab = new Label();
    final Label colLab = new Label();
    final Label rowGroup = new Label();
    final Label colGroup = new Label();
    private ListBox lb;
    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    private final GreetingServiceAsync greetingService = GWT
            .create(GreetingService.class);

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        RootPanel.get("errorLabelContainer").add(errorLabel);
        RootPanel.get("datasetInfo").add(datasetInfoLabel);
        lb = new ListBox();
        lb.setWidth("200px");
        lb.addItem("Select Dataset");
        lb.addItem("diauxic shift");
        RootPanel.get("datasetList").add(lb);

        RootPanel.get("row").add(rowLab);
        RootPanel.get("col").add(colLab);
        RootPanel.get("rowGroups").add(rowGroup);
        RootPanel.get("colGroups").add(colGroup);
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(10);
        
        RootPanel.get("menubuttons").add(hp);
        final Button somClustBtn = new Button("Hierarchical Clustering");
        // We can add style names to widgets
        somClustBtn.setEnabled(false);
        hp.add(somClustBtn);
        
     //   RootPanel.get("somClustBtn").add(somClustBtn);

        final Button lineChartBtn = new Button("Line Chart");
        // We can add style names to widgets
        lineChartBtn.setEnabled(false);
       // RootPanel.get("lineChartBtn").add(lineChartBtn);
          hp.add(lineChartBtn);

        final Button pcaBtn = new Button("PCA");
        // We can add style names to widgets
        pcaBtn.setEnabled(false);
//        RootPanel.get("pcaBtn").add(pcaBtn);
          hp.add(pcaBtn);

        final Button rankBtn = new Button("Rank Product");
        // We can add style names to widgets
        rankBtn.setEnabled(false);
//        RootPanel.get("rankBtn").add(rankBtn);
          hp.add(rankBtn);
          
          



        lb.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
            if (lb.getSelectedIndex() > 0) {
                //datasetList
                loadDataset(1);
                somClustBtn.setEnabled(true);
                lineChartBtn.setEnabled(true);
                
                
               
                ClickHandler somClustBtnHandler = new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                         runSomClustering();
                       }
                };
                somClustBtn.addClickHandler(somClustBtnHandler);
                
                 ClickHandler lineChartBtnHandler = new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                         viewLineChart(1);
                       }
                };
                lineChartBtn.addClickHandler(lineChartBtnHandler);

            }
            
            



                
                
                }
        });
            
    // Add it to the root panel.
        
        
       
        
        
        
        
//        // Create a handler for the sendButton and nameField
//        class MyHandler implements ClickHandler, KeyUpHandler {
//
//            /**
//             * Fired when the user clicks on the sendButton.
//             */
//            @Override
//            public void onClick(ClickEvent event) {
//                sendNameToServer();
//            }
//
//            /**
//             * Fired when the user types in the nameField.
//             */
//            @Override
//            public void onKeyUp(KeyUpEvent event) {
//                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//                    sendNameToServer();
//                }
//            }
//
//            /**
//             * Send the name from the nameField to the server and wait for a
//             * response.
//             */
//            private void sendNameToServer() {
//                greetingService.computeSomClustering("SomClustering",
//                        new AsyncCallback<SomClusteringResults>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//
//                        errorLabel.setText(SERVER_ERROR);
//                    }
//
//                    @Override
//                    public void onSuccess(SomClusteringResults result) {
//                        errorLabel.setText("");
//                        SomClustView hc = new SomClustView(result, selectionManager, dataset);
//                       
//                        RootPanel.get("SomClusteringResults").add(hc.asWidget());
//                        
//
//                    }
//                });
//            }
//        }

        // Add a handler to send the name to the server

        
    }
    
        private void runSomClustering() {
            
//            final Label versionLabel = new Label("d3.js current version: " + D3.version());
//    RootPanel.get().add(versionLabel);
            
            ///////////////////////////////////////
            
            
            
        greetingService.computeSomClustering("SomClustering",
                new AsyncCallback<SomClusteringResults>() {
            @Override
            public void onFailure(Throwable caught) {

                errorLabel.setText(SERVER_ERROR);
            }

            @Override
            public void onSuccess(SomClusteringResults result) {
                errorLabel.setText("");
                SomClustView hc = new SomClustView(result, selectionManager);
                RootPanel.get("SomClusteringResults").clear();
                RootPanel.get("SomClusteringResults").add(hc.asWidget());

            }
        });
    }
    private void loadDataset(int datasetId){        
        greetingService.loadDataset(datasetId,
                        new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                    }
                    @Override
                    public void onSuccess(DatasetInformation datasetInfo) {
                        errorLabel.setText("");
//                        dataset = rDataset;
                        rowLab.setText("Rows : "+datasetInfo.getRowsNumb());
                        colLab.setText("Columns : "+datasetInfo.getColNumb());
                        rowGroup.setText("Row Groups : "+(datasetInfo.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : "+(datasetInfo.getColGroupsNumb()));
                        GeneTable geneTable = new GeneTable(selectionManager, datasetInfo);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable.getGwtTable());
                        datasetInfo = null;
                    }
                });     
    }

    private void viewLineChart(int datasetId) {

        greetingService.computeLineChart(datasetId,
                new AsyncCallback<LineChartResults>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
            }

            @Override
            public void onSuccess(LineChartResults results) {
                errorLabel.setText("");
//                        dataset = rDataset;
                LineChartComp linechart = new LineChartComp(results, selectionManager);
                RootPanel.get("LineChartResults").clear();
                RootPanel.get("LineChartResults").add(linechart.getChart());
//                results = null;

            }
        });


    }
}


