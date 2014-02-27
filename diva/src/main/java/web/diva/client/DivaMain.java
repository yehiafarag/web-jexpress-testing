package web.diva.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import web.diva.client.core.model.SelectionManager;
import web.diva.client.geneTable.view.LeftPanelView;
import web.diva.client.linechart.view.LineChartComp;
import web.diva.client.pca.view.PCAPanel;
import web.diva.client.pca.view.PCAPlot;
import web.diva.client.rank.view.RankPanel;
import web.diva.client.rank.view.RankTablesView;
import web.diva.client.somclust.view.SomClustPanel;
import web.diva.client.somclust.view.SomClustView;
import web.diva.client.view.ActivateGroupPanel;
import web.diva.client.view.DatasetPanel;
import web.diva.client.view.SaveDatasetDialog;
import web.diva.shared.beans.ImgResult;
import web.diva.shared.beans.LineChartResults;
import web.diva.shared.beans.PCAResults;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResults;
import web.diva.shared.model.core.model.dataset.DatasetInformation;
import java.util.LinkedHashMap;
import web.diva.client.core.model.Selection;
import web.diva.client.view.ButtonsMenu;
import web.diva.client.view.HeaderLayout;
import web.diva.client.view.InitImgs;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DivaMain implements EntryPoint {

    private  SelectionManager selectionManager ;
    private static final String SERVER_ERROR = "An error occurred while attempting to contact the server";
    private final Label errorLabel = new Label();
    private final Label rowLab = new Label();
    private final Label colLab = new Label();
    private final Label rowGroup = new Label();
    private final Label colGroup = new Label();
    private int datasetId;
    private final Map<String, Integer> datasetsNames = new TreeMap<String, Integer>();
    private LeftPanelView geneTable;
    private int[] selectedRows;
    private int[] selectedCol;
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
    private List<String> indexer;
    private SomClustView hc;
    private InitImgs initImgs;
    private HeaderLayout header;

    @Override
    public void onModuleLoad() {
        selectionManager = new SelectionManager();
        this.initLayout();
    }

    private void initLayout() {
        
        RootPanel.get("loaderImage").setVisible(false);
        RootPanel.get("errorLabelContainer").add(errorLabel);
        //init header components 
        header = new HeaderLayout(RootPanel.get("headerLogo").getOffsetWidth() + "px", "26px");
        RootPanel.get("headerLogo").add(header);
        getDatasetsList();//get available dataset names
        RootPanel.get("row").add(rowLab);
        RootPanel.get("col").add(colLab);
        RootPanel.get("rowGroups").add(rowGroup);
        RootPanel.get("colGroups").add(colGroup);

        final ButtonsMenu btnMenue = new ButtonsMenu();
        RootPanel.get("menubuttons").add(btnMenue);

        initImgs = new InitImgs();
        RootPanel.get("geneTable").add(initImgs.getGtImg());
        RootPanel.get("LineChartResults").add(initImgs.getlCImg());
        RootPanel.get("PCAChartResults").add(initImgs.getPcaImg());
        RootPanel.get("RankTablesResults").add(initImgs.getRtImg());

        header.getLb().addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (header.getLb().getSelectedIndex() > 0) {
                    try {
                        datasetId = datasetsNames.get(header.getLb().getItemText(header.getLb().getSelectedIndex()));
                        loadDataset(datasetId);
                    } catch (Exception e) {
                        Window.alert("exp " + e.getMessage());
                    }
                    btnMenue.activatMenue();
                    btnMenue.getSomClustBtn().addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final SomClustPanel somClustPanel = new SomClustPanel();
                            somClustPanel.getOkBtn().addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    runSomClustering(datasetId, somClustPanel.getX(), somClustPanel.getY());
                                    somClustPanel.hide();
                                    somClustPanel.destroy();
                                    somClustPanel.destroy();
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
                    btnMenue.getLineChartBtn().addClickHandler(lineChartBtnHandler);

                    ClickHandler PCAChartBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            initPcaPanel();

                        }
                    };
                    btnMenue.getPcaBtn().addClickHandler(PCAChartBtnHandler);

                    ClickHandler rankBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            initGroupsPanel(2);
                        }
                    };
                    btnMenue.getRankBtn().addClickHandler(rankBtnHandler);

                    ClickHandler createGroupBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            Selection rowSel = selectionManager.getSelectedRows(datasetId);
                            Selection colSel = selectionManager.getSelectedColumns(datasetId);

                            if (rowSel != null) {
                                selectedRows = rowSel.getMembers();
                            }
                            if (colSel != null) {
                                selectedCol = colSel.getMembers();
                            }
                            initDsPanel();
                        }
                    };
                    btnMenue.getCreateGroupBtn().addClickHandler(createGroupBtnHandler);

                    btnMenue.getActGroupBtn().addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            initGroupsPanel(1);
                        }
                    });

                    btnMenue.getSaveBtn().addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final SaveDatasetDialog saveDs = new SaveDatasetDialog();
                            saveDs.getOkBtn().addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    String newName = saveDs.getName();
                                    if (newName == null || newName.equals("")) {
                                        saveDs.getErrorlabl().setVisible(true);
                                        saveDs.getForm().validate();
                                    } else {
                                        saveDs.getErrorlabl().setVisible(false);
                                        saveDataset(newName);
                                        saveDs.hide();
                                        saveDs.destroy();
                                    }
                                }
                            });

                        }
                    });
                }
            }
        });
    }

    private void getDatasetsList() {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.getAvailableDatasets(new AsyncCallback<Map<Integer, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("datasetInformation").setVisible(false);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(Map<Integer, String> results) {
                errorLabel.setText("");
                RootPanel.get("datasetInformation").setVisible(true);
                for (int key : results.keySet()) {
                    header.getLb().addItem(results.get(key));
                    datasetsNames.put(results.get(key), key);
                }
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
                        RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(DatasetInformation datasetInfos) {
                      RootPanel.get("datasetInformation").setVisible(true);
                        errorLabel.setText("");
                        rowLab.setText("Rows : " + datasetInfos.getRowsNumb());
                        colLab.setText("Columns : " + datasetInfos.getColNumb());
                        rowGroup.setText("Row Groups : " + (datasetInfos.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : " + (datasetInfos.getColGroupsNumb()));
                        geneTable = new LeftPanelView(selectionManager, datasetInfos);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable);
                        resetLayout();
                        datasetInfos = null;
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
    }

    private void runSomClustering(int datasetId, int linkage, int distanceMeasure) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computeSomClustering(datasetId, linkage, distanceMeasure,
                new AsyncCallback<SomClusteringResults>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(SomClusteringResults result) {
                        RootPanel.get("datasetInformation").setVisible(true);
                        errorLabel.setText("");
                        hc = new SomClustView(result, selectionManager);
                        RootPanel.get("SomClusteringResults").clear();
                        RootPanel.get("SomClusteringResults").add(hc.asWidget());
                        //RootPanel.get("loaderImage").setVisible(false);
                        indexer = hc.getIndexer();
                        generateHeatMap(indexer,hc.getColIndexer());
                    }
                });

    }

    private void generateHeatMap(List<String> indexers,List<String> colIndexer) {
        greetingService.computeHeatmap(datasetId, indexers,colIndexer,
                new AsyncCallback<ImgResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                         RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(ImgResult result) {
                        RootPanel.get("datasetInformation").setVisible(true);
                        errorLabel.setText("");
                        hc.setImge(result);
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
    }

    private void viewLineChart(int datasetId) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computeLineChart(datasetId,
                new AsyncCallback<LineChartResults>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(LineChartResults result) {
                        RootPanel.get("datasetInformation").setVisible(true);
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
                         RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(PCAResults result) {
                        errorLabel.setText("");
                         RootPanel.get("datasetInformation").setVisible(true);
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
                        RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(RankResult result) {
                        RootPanel.get("datasetInformation").setVisible(true);
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
                        RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(DatasetInformation result) {
                        RootPanel.get("datasetInformation").setVisible(true);
//                        datasetInfo = result;
                        errorLabel.setText("");
                        geneTable = new LeftPanelView(selectionManager, result);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable);                                
                        RootPanel.get("LineChartResults").clear();                        
                        RootPanel.get("PCAChartResults").clear();
                        RootPanel.get("RankTablesResults").clear();
                        RootPanel.get("loaderImage").setVisible(false);
                        result = null;
                    }
                });
        }

    }  

    private void createRowGroup(final int datasetId, String name, String color,String type) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.createRowGroup(datasetId, name, color, type, selectedRows,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        errorLabel.setText("");
                        RootPanel.get("datasetInformation").setVisible(true);
                        updateApp(datasetId);
                        selectedRows= null;
                    }
                });

    }
    
    private void createColGroup(final int datasetId, String name, String color, String[] selection,String type) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.createColGroup(datasetId, name, color, type, selection,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        errorLabel.setText("");
                        RootPanel.get("datasetInformation").setVisible(true);
                        updateApp(datasetId);
                    }
                });

    }

    private void saveDataset(String name){
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.saveDataset(datasetId,name,
                new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("datasetInformation").setVisible(false);
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(Integer datasetId) {
                        RootPanel.get("datasetInformation").setVisible(true);   
                        Window.Location.reload();
//                        resetLayout();
//                        lb.clear();
//                        getDatasetsList();                        
//                        loadDataset(datasetId);    
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
    
    
    }

    private void createSubDataset( String name){   
         RootPanel.get("loaderImage").setVisible(true);
        greetingService.createSubDataset(name,selectedRows,
                new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("datasetInformation").setVisible(false);
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(Integer datasetId) {
                        RootPanel.get("datasetInformation").setVisible(true); 
                        selectedRows = null;
//                        lb.clear();
//                        getDatasetsList();                        
//                        loadDataset(datasetId);   
                        Window.Location.reload();
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
    
    
    }
    
    private void updateApp(int datasetId) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.updateDatasetInfo(datasetId,
                new AsyncCallback<DatasetInformation>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        RootPanel.get("datasetInformation").setVisible(false);
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(DatasetInformation datasetInfos) {
                        RootPanel.get("datasetInformation").setVisible(true);
//                        datasetInfo = datasetInfos;
                        errorLabel.setText("");
                        rowLab.setText("Rows : " + datasetInfos.getRowsNumb());
                        colLab.setText("Columns : " + datasetInfos.getColNumb());
                        rowGroup.setText("Row Groups : " + (datasetInfos.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : " + (datasetInfos.getColGroupsNumb()));
                        geneTable = new LeftPanelView(selectionManager, datasetInfos);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable);
                        resetLayout();
                        RootPanel.get("loaderImage").setVisible(false);
                        datasetInfos = null;
                    }
                });
    }
    
    private void initGroupsPanel(final int panelType)
    {
         RootPanel.get("loaderImage").setVisible(true);
        greetingService.getGroupsPanelData(datasetId,new AsyncCallback<LinkedHashMap<String,String>[] >() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("datasetInformation").setVisible(false);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(LinkedHashMap<String,String>[] results) {
                errorLabel.setText("");
                   if(panelType==1)//activateGroupPanel
                   {
                            
                            final ActivateGroupPanel activateGroupPanel = new ActivateGroupPanel(results[0],results[1]);
                            activateGroupPanel.getOkBtn().addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    try {
                                        errorLabel.setVisible(false);
                                        String[] rowValues = activateGroupPanel.getSelectRowGroups();
                                        String[] colValues = activateGroupPanel.getSelectColGroups();
                                        if (rowValues != null && rowValues.length > 2) {
                                            activateGroupPanel.getErrorlabl().setVisible(true);

                                        } else if (colValues != null && colValues.length > 2) {
                                           activateGroupPanel.getErrorlabl().setVisible(true);

                                        } else {
                                            activateGroups(datasetId, rowValues, colValues);
                                            activateGroupPanel.hide();
                                            activateGroupPanel.destroy();
                                        }
                                    } catch (Exception exp) {
                                    }

                                }
                            });
                   }else if(panelType == 2){
                   
                        final RankPanel rankPanel =  new RankPanel(results[1]);
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

                   
                   }
                   results = null;
                   RootPanel.get("loaderImage").setVisible(false);
            }

        });
    
    }
    
    
    private void initPcaPanel()
    {
         RootPanel.get("loaderImage").setVisible(true);
        greetingService.getPcaColNames(datasetId,new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("datasetInformation").setVisible(false);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(String[] results) {
                errorLabel.setText("");
                   final PCAPanel pcaPanel = new PCAPanel(results);                           
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
                            
                   results = null;
                   RootPanel.get("loaderImage").setVisible(false);
            }

        });
    
    }
    
    private void initDsPanel(){
    
                RootPanel.get("loaderImage").setVisible(true);
        greetingService.getColNamesMaps(datasetId,new AsyncCallback<LinkedHashMap<String,String>>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("datasetInformation").setVisible(false);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(LinkedHashMap<String,String> results) {
                errorLabel.setText("");
                          final DatasetPanel dsPanel = new DatasetPanel(results, selectedRows, selectedCol);
                            dsPanel.getOkBtn().addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    dsPanel.getErrorlabl().setVisible(false);
                                    String processType = dsPanel.getProcessType();
                                    if (processType.equalsIgnoreCase("Groups")) {
                                        String name = dsPanel.getName().getValueAsString();
                                        String color = "";//dsPanel.getColorPicker();
                                        String type = dsPanel.getRadioGroupItemValue();
                                        if (name == null || name.equals("")) {
                                            dsPanel.getErrorlabl().setVisible(true);
                                            dsPanel.getForm().validate();
                                        } else if(type.equalsIgnoreCase("COLUMN GROUPS")){
                                            String[] selCol = dsPanel.getSelectColsValue();
                                            if(selCol == null || selCol.length == 0){                                            
                                                 dsPanel.getErrorlabl().setVisible(true);
                                                 dsPanel.getForm().validate();
                                            }
                                            else{
                                             dsPanel.getErrorlabl().setVisible(false);
                                            createColGroup(datasetId, name, color, selCol, type);
                                            dsPanel.hide();
                                            dsPanel.destroy();
                                            }
                                        }else{
                                             dsPanel.getErrorlabl().setVisible(false);
                                            createRowGroup(datasetId, name, color, type);
                                            dsPanel.hide();
                                            dsPanel.destroy();                                      
                                        
                                        }

                                    } else //save sub-dataset
                                    {
                                         String name = dsPanel.getDatasetName().getValueAsString();                                        
                                        if (name == null || name.equals("")) {
                                            dsPanel.getErrorlabl().setVisible(true);
                                            dsPanel.getForm2().validate();
                                        } 
                                        else{
                                            createSubDataset(name);
                                            dsPanel.hide();
                                            dsPanel.destroy();  
                                        
                                        }
                                        

                                    }

                                }
                            });
                   results = null;
                  
                   RootPanel.get("loaderImage").setVisible(false);
            }

        });
    
    
    
        
        
    }
    
    private void resetLayout(){
         RootPanel.get("LineChartResults").clear();     
         RootPanel.get("LineChartResults").add(initImgs.getlCImg());
         RootPanel.get("PCAChartResults").clear();
         RootPanel.get("PCAChartResults").add(initImgs.getPcaImg());
         RootPanel.get("RankTablesResults").clear();
         RootPanel.get("RankTablesResults").add(initImgs.getRtImg());
         RootPanel.get("SomClusteringResults").clear();        
          selectionManager.setSelectedColumns(datasetId, new Selection(Selection.TYPE.OF_COLUMNS,new int[]{}));
          selectionManager.setSelectedRows(datasetId, new Selection(Selection.TYPE.OF_ROWS,new int[]{}),10);
    }
}
