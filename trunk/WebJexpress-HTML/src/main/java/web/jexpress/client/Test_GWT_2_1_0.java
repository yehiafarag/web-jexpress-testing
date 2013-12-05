package web.jexpress.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import web.jexpress.client.geneTable.view.GeneTable;
import web.jexpress.client.linechart.view.LineChartComp;
import web.jexpress.client.pca.view.PCAPlot;
import web.jexpress.client.rank.view.RankTablesView;
import web.jexpress.client.somclust.view.SomClustView;
import web.jexpress.shared.model.core.model.SelectionManager;


import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.beans.PCAResults;
import web.jexpress.shared.beans.RankResult;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.Selection;
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
    private int datasetId;
    
    private int nameCounter=2;//remove in future
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
        this.initLayout();
    }

    private void initLayout() {
        RootPanel.get("loaderImage").setVisible(false);
        RootPanel.get("errorLabelContainer").add(errorLabel);
        RootPanel.get("datasetInfo").add(datasetInfoLabel);
        lb = new ListBox();
        lb.setWidth("200px");
        lb.addItem("Select Dataset");
        //get available dataset names
        for (int x = 0; x < getDatasetsList().length; x++) {
            lb.addItem(getDatasetsList()[x]);
        }

        RootPanel.get("datasetList").add(lb);
        RootPanel.get("row").add(rowLab);
        RootPanel.get("col").add(colLab);
        RootPanel.get("rowGroups").add(rowGroup);
        RootPanel.get("colGroups").add(colGroup);
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(10);
        RootPanel.get("menubuttons").add(hp);//menubuttons

        final Button somClustBtn = new Button("Hierarchical Clustering");
        somClustBtn.setEnabled(false);
        hp.add(somClustBtn);


        final Button lineChartBtn = new Button("Line Chart");
        lineChartBtn.setEnabled(false);
        hp.add(lineChartBtn);

        final Button pcaBtn = new Button("PCA");
        pcaBtn.setEnabled(false);
        hp.add(pcaBtn);

        final Button rankBtn = new Button("Rank Product");
        rankBtn.setEnabled(false);
        hp.add(rankBtn);

        final Button createGroupBtn = new Button("Create Row Group");
        createGroupBtn.setEnabled(false);
        hp.add(createGroupBtn);

        lb.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (lb.getSelectedIndex() > 0) {
                    datasetId = 1;
                    loadDataset(datasetId);
                    somClustBtn.setEnabled(true);
                    lineChartBtn.setEnabled(true);
                    pcaBtn.setEnabled(true);
                    rankBtn.setEnabled(true);
                    createGroupBtn.setEnabled(true);
                    ClickHandler somClustBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            runSomClustering(datasetId);
                        }
                    };
                    somClustBtn.addClickHandler(somClustBtnHandler);

                    ClickHandler lineChartBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            viewLineChart(datasetId);
                        }
                    };
                    lineChartBtn.addClickHandler(lineChartBtnHandler);


                    ClickHandler PCAChartBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            viewPCAChart(datasetId);
                        }
                    };
                    pcaBtn.addClickHandler(PCAChartBtnHandler);

                    ClickHandler rankBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            viewRankTables(datasetId);
                        }
                    };
                    rankBtn.addClickHandler(rankBtnHandler);


                    ClickHandler createGroupBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            Selection sel = selectionManager.getSelectedRows(datasetId);
                            if (sel != null) {
                                int[] selectedRows = sel.getMembers();

                                if (selectedRows != null) {
                                    createRowGroup(datasetId, ""+nameCounter++, "red", selectedRows);
                                }
                            }

                        }
                    };
                    createGroupBtn.addClickHandler(createGroupBtnHandler);
                }
            }
        });
    }

    private void runSomClustering(int datasetId) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computeSomClustering(datasetId,
                new AsyncCallback<SomClusteringResults>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(SomClusteringResults result) {
                errorLabel.setText("");
                SomClustView hc = new SomClustView(pass, result, selectionManager);
                RootPanel.get("SomClusteringResults").clear();
                RootPanel.get("SomClusteringResults").add(hc.asWidget());
                RootPanel.get("loaderImage").setVisible(false);
            }
        });
    }

    private void loadDataset(int datasetId) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.loadDataset(datasetId,
                new AsyncCallback<DatasetInformation>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(DatasetInformation datasetInfo) {
                errorLabel.setText("");
                pass = datasetInfo.getPass();
                rowLab.setText("Rows : " + datasetInfo.getRowsNumb());
                colLab.setText("Columns : " + datasetInfo.getColNumb());
                rowGroup.setText("Row Groups : " + (datasetInfo.getRowGroupsNumb()));
                colGroup.setText("Column Groups : " + (datasetInfo.getColGroupsNumb()));
                GeneTable geneTable = new GeneTable(selectionManager, datasetInfo);
                RootPanel.get("geneTable").clear();
                RootPanel.get("geneTable").add(geneTable.getGwtTable());
                datasetInfo = null;
                RootPanel.get("loaderImage").setVisible(false);
            }
        });
    }
    private String pass = "";

    private void viewLineChart(int datasetId) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computeLineChart(datasetId,
                new AsyncCallback<LineChartResults>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(LineChartResults result) {
                errorLabel.setText("");
                LineChartComp linechart = new LineChartComp(result, selectionManager);
                RootPanel.get("LineChartResults").clear();
                RootPanel.get("LineChartResults").add(linechart.getLayout());
                result = null;
                RootPanel.get("loaderImage").setVisible(false);
            }
        });
    }

    private void viewPCAChart(int datasetId) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computePCA(datasetId,
                new AsyncCallback<PCAResults>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(PCAResults result) {
                errorLabel.setText("");
                PCAPlot pca = new PCAPlot(result, 1, 2, selectionManager);
                RootPanel.get("PCAChartResults").clear();
                RootPanel.get("PCAChartResults").add(pca.getScatterPlotLayout());
                RootPanel.get("loaderImage").setVisible(false);
            }
        });
    }

    private void viewRankTables(int datasetId) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computeRank(datasetId,
                new AsyncCallback<RankResult>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(RankResult result) {
                errorLabel.setText("");
                RankTablesView rankTables = new RankTablesView(selectionManager, result);
                RootPanel.get("RankTablesResults").clear();
                RootPanel.get("RankTablesResults").add(rankTables);
                RootPanel.get("loaderImage").setVisible(false);
            }
        });

    }

    private void createRowGroup(final int datasetId, String name, String color, int[] selection) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.createGroup(datasetId, name, color, "Row", selection,
                new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(Boolean result) {
                errorLabel.setText("");
                updateApp(datasetId);
                //RootPanel.get("loaderImage").setVisible(false);
            }
        });


    }
    
    private void updateApp(int datasetId)
    {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.updateDatasetInfo(datasetId,
                new AsyncCallback<DatasetInformation>() {
            @Override
            public void onFailure(Throwable caught) { 
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("loaderImage").setVisible(false);
            }
            @Override
            public void onSuccess(DatasetInformation datasetInfo) {
                errorLabel.setText("");
                pass = datasetInfo.getPass();
                rowLab.setText("Rows : " + datasetInfo.getRowsNumb());
                colLab.setText("Columns : " + datasetInfo.getColNumb());
                rowGroup.setText("Row Groups : " + (datasetInfo.getRowGroupsNumb()));
                colGroup.setText("Column Groups : " + (datasetInfo.getColGroupsNumb()));
                GeneTable geneTable = new GeneTable(selectionManager, datasetInfo);
                RootPanel.get("geneTable").clear();
                RootPanel.get("geneTable").add(geneTable.getGwtTable());
                datasetInfo = null;   
                RootPanel.get("loaderImage").setVisible(false);
            }
        });

    
    
    }
     private String[] getDatasetsList() {
        String[] datasetList = new String[]{"diauxic shift"};
        return datasetList;

    }
    
}
