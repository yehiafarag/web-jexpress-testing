package web.jexpress.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
//import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.VLayout;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import web.jexpress.client.core.model.SelectionManager;
import web.jexpress.client.geneTable.view.GeneTable;
import web.jexpress.client.linechart.view.LineChartComp;
import web.jexpress.client.pca.view.PCAPlot;
import web.jexpress.client.rank.view.RankTablesView;
import web.jexpress.client.somclust.view.SomClustView;
import web.jexpress.shared.beans.ImgResult;
//import web.jexpress.shared.model.core.model.SelectionManager;

import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.beans.PCAResults;
import web.jexpress.shared.beans.RankResult;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.Selection;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
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
    private DatasetInformation datasetInfo;
    private Map<String,Integer> datasetsNames = new TreeMap<String, Integer>();

    private int nameCounter = 2;//remove in future
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
        getDatasetsList();        
        RootPanel.get("datasetList").add(lb);
        RootPanel.get("row").add(rowLab);
        RootPanel.get("col").add(colLab);
        RootPanel.get("rowGroups").add(rowGroup);
        RootPanel.get("colGroups").add(colGroup);
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(5);
        RootPanel.get("menubuttons").add(hp);//menubuttons
        
        final IButton somClustBtn = new IButton("Hierarchical Clustering");
        somClustBtn.setWidth(150);
        somClustBtn.setShowRollOver(true);
        somClustBtn.setShowDisabled(true);
        somClustBtn.setShowDown(true);
        somClustBtn.setTitleStyle("stretchTitle");
        somClustBtn.disable();
        hp.add(somClustBtn);        
        
        final IButton lineChartBtn = new IButton("Line Chart");
        lineChartBtn.setWidth(100);
        lineChartBtn.setShowRollOver(true);
        lineChartBtn.setShowDisabled(true);
        lineChartBtn.setShowDown(true);
        lineChartBtn.setTitleStyle("stretchTitle");
        lineChartBtn.disable();
        hp.add(lineChartBtn);        
        
        final IButton pcaBtn = new IButton("PCA");
        pcaBtn.setWidth(50);
        pcaBtn.setShowRollOver(true);
        pcaBtn.setShowDisabled(true);
        pcaBtn.setShowDown(true);
        pcaBtn.setTitleStyle("stretchTitle");
        pcaBtn.disable();
        hp.add(pcaBtn);
        
        final IButton rankBtn = new IButton("Rank Product");
        rankBtn.setWidth(100);
        rankBtn.setShowRollOver(true);
        rankBtn.setShowDisabled(true);
        rankBtn.setShowDown(true);
        rankBtn.setTitleStyle("stretchTitle");
        rankBtn.disable();
        hp.add(rankBtn);
        
        final IButton createGroupBtn = new IButton("Create Row Group");
        createGroupBtn.setWidth(150);
        createGroupBtn.setShowRollOver(true);
        createGroupBtn.setShowDisabled(true);
        createGroupBtn.setShowDown(true);
        createGroupBtn.setTitleStyle("stretchTitle");
        createGroupBtn.disable();
        hp.add(createGroupBtn);
        
        final IButton actGroupBtn = new IButton("Activate/Deactivate Groups");        
        actGroupBtn.setWidth(150);
        actGroupBtn.setShowRollOver(true);
        actGroupBtn.setShowDisabled(true);
        actGroupBtn.setShowDown(true);
        actGroupBtn.setTitleStyle("stretchTitle");
        actGroupBtn.disable();
        hp.add(actGroupBtn);
        actGroupBtn.addClickHandler(new ClickHandler() {            
            @Override
            public void onClick(ClickEvent event) {                
                final Window winModal = new Window();                
                winModal.setWidth(360);                
                winModal.setHeight(115);                
                winModal.setTitle("SELECT ACTIVE GROUPS");                
                winModal.setShowMinimizeButton(false);                
                winModal.setIsModal(false);                
//                winModal.setShowModalMask(true);                
                winModal.centerInPage();                
                winModal.addCloseClickHandler(new CloseClickHandler() {
                    @Override
                    public void onCloseClick(CloseClickEvent event) { 
                       
                        winModal.hide();
                        winModal.redraw();
                        winModal.destroy();                        
                    }                    
                });                
                DynamicForm form = new DynamicForm();                
                form.setHeight100();                
                form.setWidth100();                
                form.setPadding(5);                
                form.setLayoutAlign(VerticalAlignment.BOTTOM);                
                
                final SelectItem selectItemMultipleGrid = new SelectItem();
                selectItemMultipleGrid.setTitle("MAX 2 GROUPS");
                selectItemMultipleGrid.setMultiple(true);
                selectItemMultipleGrid.setMultipleAppearance(MultipleAppearance.GRID); 
        
        if(datasetInfo != null)
            selectItemMultipleGrid.setValueMap(datasetInfo.getRowGroupsNamesMap());
        else
            selectItemMultipleGrid.setValueMap("vovo","vhhtg","popo");  
  
                IButton okBtn = new IButton("OK");
                okBtn.setWidth(30);
                okBtn.setShowRollOver(true);
                okBtn.setShowDown(true);
                okBtn.setTitleStyle("stretchTitle");               
                form.setFields(selectItemMultipleGrid);
                form.redraw();                
                winModal.addItem(form);                
                winModal.addItem(okBtn);
                winModal.setAlign(Alignment.CENTER);
                winModal.show();               
                okBtn.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                       String[] values = selectItemMultipleGrid.getValues();
                       
                       winModal.hide();
                       winModal.redraw();
                       winModal.destroy();
                      
                    }
                });
            }            
        });
        

        lb.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (lb.getSelectedIndex() > 0) {
                    datasetId = datasetsNames.get(lb.getItemText(lb.getSelectedIndex()));
                    loadDataset(datasetId);
                    somClustBtn.enable();
                    lineChartBtn.enable();
                    pcaBtn.enable();
                    rankBtn.enable();
                    createGroupBtn.enable();//  
                    actGroupBtn.enable();
                    somClustBtn.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                           runSomClustering(datasetId);
                        }
                    });

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
                                    createRowGroup(datasetId, "" + nameCounter++, "red", selectedRows);
                                }
                            }

                        }
                    };
                    createGroupBtn.addClickHandler(createGroupBtnHandler);
                }
            }
        });
    }

    private List<String> indexer;
    private SomClustView hc;

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
                        hc = new SomClustView(pass, result, selectionManager);
                        RootPanel.get("SomClusteringResults").clear();
                        RootPanel.get("SomClusteringResults").add(hc.asWidget());
                        //RootPanel.get("loaderImage").setVisible(false);
                        indexer = hc.getIndexer();
                        generateHeatMap(indexer);
                    }
                });

    }

    private void generateHeatMap(List<String> indexers) {
        greetingService.computeHeatmap(datasetId, indexers,
                new AsyncCallback<ImgResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(ImgResult result) {
                        errorLabel.setText("");
                        //Image image = new Image(result.getImgString());
                        hc.setImge(result);
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
                    public void onSuccess(DatasetInformation datasetInfos) {
                        datasetInfo = datasetInfos;
                        errorLabel.setText("");
                        pass = datasetInfo.getPass();
                        rowLab.setText("Rows : " + datasetInfo.getRowsNumb());
                        colLab.setText("Columns : " + datasetInfo.getColNumb());
                        rowGroup.setText("Row Groups : " + (datasetInfo.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : " + (datasetInfo.getColGroupsNumb()));
                        GeneTable geneTable = new GeneTable(selectionManager, datasetInfo);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable.getGwtTable());
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

    private void updateApp(int datasetId) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.updateDatasetInfo(datasetId,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(DatasetInformation datasetInfos) {
                        datasetInfo = datasetInfos;
                        errorLabel.setText("");
                        pass = datasetInfos.getPass();
                        rowLab.setText("Rows : " + datasetInfos.getRowsNumb());
                        colLab.setText("Columns : " + datasetInfos.getColNumb());
                        rowGroup.setText("Row Groups : " + (datasetInfos.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : " + (datasetInfos.getColGroupsNumb()));
                        GeneTable geneTable = new GeneTable(selectionManager, datasetInfo);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable.getGwtTable());                        
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });

    }

    private void getDatasetsList() {
         RootPanel.get("loaderImage").setVisible(true);
        greetingService.getAvailableDatasets(new AsyncCallback<Map<Integer,String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(Map<Integer,String> results) {
                        errorLabel.setText("");     
                        for(int key:results.keySet()){
                             lb.addItem(results.get(key));
                             datasetsNames.put(results.get(key),key);
                        }
                        RootPanel.get("loaderImage").setVisible(false);
                    }
        
        });

    }

}
