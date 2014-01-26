package web.jexpress.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
//import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ExportDisplay;
import com.smartgwt.client.types.ExportFormat;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.EnumUtil;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import web.jexpress.client.core.model.SelectionManager;
import web.jexpress.client.geneTable.view.GeneTableView;
import web.jexpress.client.linechart.view.LineChartComp;
import web.jexpress.client.pca.view.PCAPanel;
import web.jexpress.client.pca.view.PCAPlot;
import web.jexpress.client.rank.view.RankPanel;
import web.jexpress.client.rank.view.RankTablesView;
import web.jexpress.client.somclust.view.SomClustPanel;
import web.jexpress.client.somclust.view.SomClustView;
import web.jexpress.client.view.DatasetPanel;
import web.jexpress.shared.beans.ImgResult;

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
            + "attempting to contact the server";
    final Label errorLabel = new Label();
    final Label datasetInfoLabel = new Label();
    final Label rowLab = new Label();
    final Label colLab = new Label();
    final Label rowGroup = new Label();
    final Label colGroup = new Label();
    private ListBox lb;
    private int datasetId;
    private DatasetInformation datasetInfo;
    private final Map<String, Integer> datasetsNames = new TreeMap<String, Integer>();
    private GeneTableView geneTable;
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
        somClustBtn.setWidth(130);
        somClustBtn.setShowRollOver(true);
        somClustBtn.setShowDisabled(true);
        somClustBtn.setShowDown(true);
        somClustBtn.setTitleStyle("stretchTitle");
        somClustBtn.disable();
        hp.add(somClustBtn);

        final IButton lineChartBtn = new IButton("Line Chart");
        lineChartBtn.setWidth(80);
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

        final IButton createGroupBtn = new IButton("Create Groups/Datasets ");
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

        final IButton exportGroupBtn = new IButton("Export");
        exportGroupBtn.setWidth(60);
        exportGroupBtn.setShowRollOver(true);
        exportGroupBtn.setShowDisabled(true);
        exportGroupBtn.setShowDown(true);
        exportGroupBtn.setTitleStyle("stretchTitle");
        exportGroupBtn.disable();
        hp.add(exportGroupBtn);
        
        final IButton saveBtn = new IButton("Save");
        saveBtn.setWidth(50);
        saveBtn.setShowRollOver(true);
        saveBtn.setShowDisabled(true);
        saveBtn.setShowDown(true);
        saveBtn.setTitleStyle("stretchTitle");
        saveBtn.disable();
        hp.add(saveBtn);

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
//                    exportGroupBtn.enable();
                    somClustBtn.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final SomClustPanel somClustPanel = new SomClustPanel();
                            somClustPanel.getOkBtn().addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    try {
                                        runSomClustering(datasetId, somClustPanel.getX(), somClustPanel.getY());
                                        somClustPanel.hide();
                                        somClustPanel.destroy();

                                    } catch (Exception exp) {
                                    }

                                }
                            });
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
                            
                            final PCAPanel pcaPanel = new PCAPanel(datasetInfo);
                           
                            pcaPanel.getOkBtn().addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    try {
                                        int pcaI = pcaPanel.getPcaI();
                                        int pcaII = pcaPanel.getPcaII();
                                        viewPCAChart(datasetId,pcaI,pcaII);
                                            pcaPanel.hide();
                                            pcaPanel.destroy();
                                        
                                    } catch (Exception exp) {
                                    }

                                }
                            });
                            
                        }
                    };
                    pcaBtn.addClickHandler(PCAChartBtnHandler);

                    ClickHandler rankBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final RankPanel rankPanel = new RankPanel(datasetInfo);
                            rankPanel.getOkBtn().addClickHandler(new ClickHandler() {

                                @Override
                                public void onClick(ClickEvent event) {
                                    rankPanel.getErrorlabl().setVisible(false);
                                   String[] groups = rankPanel.getSelectColGroups();
                                   String seed = rankPanel.getSeed();
                                   String perm = rankPanel.getPermutation();
                                   String log2 = rankPanel.getRadioGroupItem();
                                   if(groups==null ||groups.length==0||groups.length>2||seed==null||seed.equals("")||perm==null||perm.equals(""))
                                   {
                                       rankPanel.getErrorlabl().setVisible(true);
                                       rankPanel.getForm2().validate();
                                   }
                                   else{
                                       viewRankTables(datasetId, perm, seed, groups, log2);
                                       rankPanel.hide();
                                       rankPanel.destroy();
                                        }
                                }
                            });

// viewRankTables(datasetId,rankPanel.getPermutation());
                        }
                    };
                    rankBtn.addClickHandler(rankBtnHandler);

                    ClickHandler createGroupBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            Selection sel = selectionManager.getSelectedRows(datasetId);
                            if (sel != null) {
                                final int[] selectedRows = sel.getMembers();

                                if (selectedRows != null) {
                                    
                                    final DatasetPanel dsPanel = new DatasetPanel();
                                    dsPanel.getOkBtn().addClickHandler(new ClickHandler() {

                                @Override
                                public void onClick(ClickEvent event) {
                                    dsPanel.getErrorlabl().setVisible(false);
                                   String processType = dsPanel.getProcessType();
                                   if(processType.equalsIgnoreCase("Groups"))
                                   {
                                       String name = dsPanel.getName().getValueAsString();
                                       String color = "";//dsPanel.getColorPicker();
                                       String type = dsPanel.getRadioGroupItem().getValueAsString();
                                        if(name==null||name.equals(""))
                                   {
                                       dsPanel.getErrorlabl().setVisible(true);
                                       dsPanel.getForm().validate();
                                   }
                                   else{
                                       createGroup(datasetId, name, color, selectedRows,type);                                
                                       dsPanel.hide();
                                       dsPanel.destroy();
                                        }
                                   
                                   }
                                   else //save sub-dataset
                                   {
                                   
                                   }                                   
                                  
                                }
                            });
                                    
                                    
                                 }
                            }

                        }
                    };
                    createGroupBtn.addClickHandler(createGroupBtnHandler);
                    actGroupBtn.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final Window winModal = new Window();
                            winModal.setWidth(400);
                            winModal.setHeight(600);
                            winModal.setTitle("SELECT ACTIVE GROUPS");
                            winModal.setShowMinimizeButton(false);
                            winModal.setIsModal(false);   
                            winModal.centerInPage();
                            winModal.addCloseClickHandler(new CloseClickHandler() {
                                @Override
                                public void onCloseClick(CloseClickEvent event) {
                                    winModal.hide();
                                    winModal.destroy();
                                }
                            });
                            DynamicForm form = new DynamicForm();
                            form.setHeight(240);
                            form.setWidth100();
                            form.setPadding(5);
                            form.setLayoutAlign(VerticalAlignment.BOTTOM);
                            

                            final SelectItem selectRowGroups = new SelectItem();
                            selectRowGroups.setTitle("ROW GROUPS (MAX 2)");
                            selectRowGroups.setTextAlign(Alignment.CENTER);
                            selectRowGroups.setTitleAlign(Alignment.CENTER);
                            selectRowGroups.setMultiple(true);
                            selectRowGroups.setMultipleAppearance(MultipleAppearance.GRID);

                            final SelectItem selectColGroups = new SelectItem();
                            selectColGroups.setTitle("COLUMN GROUPS (MAX 2)");
                            selectColGroups.setTextAlign(Alignment.CENTER);
                            selectColGroups.setTitleAlign(Alignment.CENTER);
                            selectColGroups.setMultiple(true);
                            selectColGroups.setMultipleAppearance(MultipleAppearance.GRID);

                            if (datasetInfo != null) {
                                selectRowGroups.setValueMap(datasetInfo.getRowGroupsNamesMap());

                                selectColGroups.setValueMap(datasetInfo.getColGroupsNamesMap());
                            }
                            form.setFields(selectRowGroups, selectColGroups);
                            form.redraw();

                            IButton okBtn = new IButton("Activate");
                            okBtn.setWidth(100);
                            okBtn.setAlign(Alignment.CENTER);
                            okBtn.setShowRollOver(true);
                            okBtn.setShowDown(true);
                            okBtn.setTitleStyle("stretchTitle");
                            okBtn.setPageLeft(30);//adding(5);

                            HTML infolable = new HTML("<h4 style='color:blue;margin-left: 20px;margin-top: 20px;height=30px;'>*SELECT (ALL GROUP) WILL DEACTIVATE THE REST OF THE GROUPS</h4>");
                            final HTML errorlabl = new HTML("<h4 style='color:red;margin-left: 20px;height=30px;'>YOU CAN NOT SELECT MORE THAN 2 GROUPS</h4>");

                            
                            errorlabl.setVisible(false);
                            winModal.addItem(form);
                            winModal.addItem(okBtn);
                            winModal.addItem(infolable);
                            winModal.addItem(errorlabl);
                            winModal.setAlign(Alignment.CENTER);
                            winModal.show();
                            okBtn.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    try {
                                        errorLabel.setVisible(false);
                                        String[] rowValues = selectRowGroups.getValues();
                                        String[] colValues = selectColGroups.getValues();
                                        if (rowValues != null && rowValues.length > 2) {
                                            errorlabl.setVisible(true);

                                        } else if (colValues != null && colValues.length > 2) {
                                            errorlabl.setVisible(true);

                                        } else {
                                            activateGroups(datasetId, rowValues, colValues);
                                            winModal.hide();
                                            winModal.destroy();
                                        }
                                    } catch (Exception exp) {
                                    }

                                }
                            });
                        }
                    });
                
//                    exportGroupBtn.addClickHandler(new ClickHandler() {
//
//                        @Override
//                        public void onClick(ClickEvent event) {
//                 try{          
//                String exportAs = "csv";              
//                   // exportAs is either XML or CSV, which we can do with requestProperties  
//                    DSRequest dsRequestProperties = new DSRequest();  
//                    dsRequestProperties.setExportAs((ExportFormat)EnumUtil.getEnum(ExportFormat.values(), exportAs));  
//                    dsRequestProperties.setExportDisplay(ExportDisplay.DOWNLOAD); 
//                    geneTable.getSelectionTable().exportData();//exportData(dsRequestProperties);  
//                 }catch(Exception exp){
//                 com.google.gwt.user.client.Window.alert(exp.getMessage());
//                 }
//            
//                        }
//                    });
                }
            }
        });
    }

    private List<String> indexer;
    private SomClustView hc;

    private void runSomClustering(int datasetId,int linkage,int distanceMeasure) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computeSomClustering(datasetId,linkage,distanceMeasure,
                new AsyncCallback<SomClusteringResults>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        datasetInfoLabel.setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(SomClusteringResults result) {
                        datasetInfoLabel.setVisible(true);
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
                        datasetInfoLabel.setVisible(true);
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
                        datasetInfoLabel.setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(DatasetInformation datasetInfos) {
                        datasetInfoLabel.setVisible(true);
                        datasetInfo = datasetInfos;
                        errorLabel.setText("");
                        pass = datasetInfo.getPass();
                        rowLab.setText("Rows : " + datasetInfo.getRowsNumb());
                        colLab.setText("Columns : " + datasetInfo.getColNumb());
                        rowGroup.setText("Row Groups : " + (datasetInfo.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : " + (datasetInfo.getColGroupsNumb()));
                        geneTable = new GeneTableView(selectionManager, datasetInfo);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable);
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
                        datasetInfoLabel.setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(LineChartResults result) {
                        datasetInfoLabel.setVisible(true);
                        errorLabel.setText("");
                        LineChartComp linechart = new LineChartComp(result, selectionManager);
                        RootPanel.get("LineChartResults").clear();
                        RootPanel.get("LineChartResults").add(linechart.getLayout());
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
    }

    private void viewPCAChart(int datasetId,int pcaI,int pcaII) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computePCA(datasetId,pcaI,pcaII,
                new AsyncCallback<PCAResults>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        datasetInfoLabel.setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(PCAResults result) {
                        errorLabel.setText("");
                        datasetInfoLabel.setVisible(true);
                        PCAPlot pca = new PCAPlot(result,selectionManager);
                        RootPanel.get("PCAChartResults").clear();
                        RootPanel.get("PCAChartResults").add(pca.getScatterPlotLayout());
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
    }

    private void viewRankTables(int datasetId,String perm,String seed,String[] colGropNames,String log2) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computeRank(datasetId,perm,seed,colGropNames,log2,
                new AsyncCallback<RankResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        datasetInfoLabel.setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(RankResult result) {
                        datasetInfoLabel.setVisible(true);
                        errorLabel.setText("");
                        RankTablesView rankTables = new RankTablesView(selectionManager, result);
                        RootPanel.get("RankTablesResults").clear();
                        RootPanel.get("RankTablesResults").add(rankTables);
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });

    }

    private void activateGroups(final int datasetId, String[] rowGroups, String[] colGroups) {

        boolean test = false;
        if (rowGroups != null && rowGroups.length > 0) {
            for (String rowGroup1 : rowGroups) {
                if (rowGroup1.startsWith("false,")) {                    
                    test = true;
                    break;
                }
            }
        }
        if (!test && colGroups != null && colGroups.length > 0) {
            for (String colGroup1 : colGroups) {
                if (colGroup1.startsWith("false,")) {
                    test = true;
                    break;
                }
            }
        }
        if(test){
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.activateGroups(datasetId,rowGroups, colGroups,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        datasetInfoLabel.setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(DatasetInformation result) {
                        datasetInfoLabel.setVisible(true);
                        datasetInfo = result;
                        errorLabel.setText("");
                        geneTable = new GeneTableView(selectionManager, datasetInfo);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable);                                
                        RootPanel.get("LineChartResults").clear();                        
                        RootPanel.get("PCAChartResults").clear();
                        RootPanel.get("RankTablesResults").clear();
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
        }

    }  

    private void createGroup(final int datasetId, String name, String color, int[] selection,String type) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.createGroup(datasetId, name, color, type, selection,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        datasetInfoLabel.setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        errorLabel.setText("");
                        datasetInfoLabel.setVisible(true);
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
                        datasetInfoLabel.setVisible(false);
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(DatasetInformation datasetInfos) {
                        datasetInfoLabel.setVisible(true);
                        datasetInfo = datasetInfos;
                        errorLabel.setText("");
                        pass = datasetInfos.getPass();
                        rowLab.setText("Rows : " + datasetInfos.getRowsNumb());
                        colLab.setText("Columns : " + datasetInfos.getColNumb());
                        rowGroup.setText("Row Groups : " + (datasetInfos.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : " + (datasetInfos.getColGroupsNumb()));
                        geneTable = new GeneTableView(selectionManager, datasetInfo);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable);
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });

    }

    private void getDatasetsList() {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.getAvailableDatasets(new AsyncCallback<Map<Integer, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                datasetInfoLabel.setVisible(false);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(Map<Integer, String> results) {
                errorLabel.setText("");
                datasetInfoLabel.setVisible(true);
                for (int key : results.keySet()) {
                    lb.addItem(results.get(key));
                    datasetsNames.put(results.get(key), key);
                }
                RootPanel.get("loaderImage").setVisible(false);
            }

        });

    }

}
