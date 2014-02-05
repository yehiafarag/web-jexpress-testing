package web.jexpress.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.IButton;
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
import web.jexpress.client.view.ActivateGroupPanel;
import web.jexpress.client.view.DatasetPanel;
import web.jexpress.client.view.SaveDatasetDialog;
import web.jexpress.shared.beans.ImgResult;
import web.jexpress.shared.beans.LineChartResults;
import web.jexpress.shared.beans.PCAResults;
import web.jexpress.shared.beans.RankResult;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.Selection;
import web.jexpress.shared.model.core.model.dataset.DatasetInformation;
import com.google.gwt.storage.client.Storage;
  

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Test_GWT_2_1_0 implements EntryPoint {
    private Storage stockStore = null;
    private final SelectionManager selectionManager = new SelectionManager();
    private static final String SERVER_ERROR = "An error occurred while attempting to contact the server";
    private final Label errorLabel = new Label();
    private final Label rowLab = new Label();
    private final Label colLab = new Label();
    private final Label rowGroup = new Label();
    private final Label colGroup = new Label();
    private ListBox lb;
    private int datasetId;
    private DatasetInformation datasetInfo;
    private final Map<String, Integer> datasetsNames = new TreeMap<String, Integer>();
    private GeneTableView geneTable;  
    private int[] selectedRows;
    private int[] selectedCol;   
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
    private List<String> indexer;
    private SomClustView hc;
    private String path = "";
    private HorizontalPanel headerpanel ;
    private  Image lCImg;
    private  Image pcaImg;
     private  Image gtImg;
      private  Image rtImg;
    
    @Override
    public void onModuleLoad() {
        stockStore = Storage.getLocalStorageIfSupported();
        if(stockStore!=null){}
        this.initLayout();
    }
    
    private void initLayout() {
        RootPanel.get("loaderImage").setVisible(false);
        RootPanel.get("errorLabelContainer").add(errorLabel);        
        Image image = new Image("js/diva-logo.png");
        image.setWidth("361px");
        image.setHeight("26px");        
        
         lCImg = new Image("images/clear.jpg");
         lCImg.setHeight("300px");
         lCImg.setWidth("" + RootPanel.get("LineChartResults").getOffsetWidth() + "px");
          RootPanel.get("LineChartResults").add(lCImg);
          
          
          pcaImg = new Image("images/clear.jpg");
          pcaImg.setHeight("300px");
         pcaImg.setWidth("" + RootPanel.get("LineChartResults").getOffsetWidth() + "px");
         RootPanel.get("PCAChartResults").add(pcaImg);
         
          gtImg = new Image("images/gt.png");
          gtImg.setHeight("580px");
         gtImg.setWidth("" + RootPanel.get("geneTable").getOffsetWidth() + "px");
         RootPanel.get("geneTable").add(gtImg);
         
           rtImg = new Image("images/rt.png");
          rtImg.setHeight("270px");
         rtImg.setWidth(""+RootPanel.get("RankTablesResults").getOffsetWidth()+"px");
         RootPanel.get("RankTablesResults").add(rtImg);
          
          
        headerpanel = new HorizontalPanel();        
        headerpanel.setWidth(RootPanel.get("headerLogo").getOffsetWidth()+"px");
        headerpanel.setHeight("26px");
        headerpanel.add(image);       
        lb = new ListBox();
        lb.setWidth("300px");
        lb.addItem("Select Dataset");
        getDatasetsList();//get available dataset names
        headerpanel.add(lb);
        RootPanel.get("headerLogo").add(headerpanel);
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
                if (lb.getSelectedIndex() > 0) {try{
                    datasetId = datasetsNames.get(lb.getItemText(lb.getSelectedIndex()));                    
                    loadDataset(datasetId);
                }catch(Exception e){Window.alert("exp "+e.getMessage());}
                    
//                    exportGroupBtn.enable();   
                    
                    somClustBtn.enable();
                    somClustBtn.addClickHandler(new ClickHandler() {
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
                    
                    lineChartBtn.enable();
                    ClickHandler lineChartBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            viewLineChart(datasetId);
                        }
                    };
                    lineChartBtn.addClickHandler(lineChartBtnHandler);
                    
                    
                    pcaBtn.enable();
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

                    
                    rankBtn.enable();
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

                    createGroupBtn.enable();
                    ClickHandler createGroupBtnHandler = new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            Selection rowSel = selectionManager.getSelectedRows(datasetId);
                            Selection colSel = selectionManager.getSelectedColumns(datasetId);
                           
                            
                            if (rowSel != null) {
                                selectedRows = rowSel.getMembers();
                            }
                            if(colSel !=null){
                                selectedCol =colSel.getMembers();
                            }  

                            final DatasetPanel dsPanel = new DatasetPanel(datasetInfo, selectedRows, selectedCol);
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
                                            createRowGroup(datasetId, name, color, selectedRows, type);
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
                                            createSubDataset(name,selectedRows);
                                            dsPanel.hide();
                                            dsPanel.destroy();  
                                        
                                        }
                                        

                                    }

                                }
                            });
                        }
                    };
                    createGroupBtn.addClickHandler(createGroupBtnHandler);
                   
                    
                    actGroupBtn.enable();
                    actGroupBtn.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            final ActivateGroupPanel activateGroupPanel = new ActivateGroupPanel(datasetInfo);
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
                        }
                    });
                
                    saveBtn.enable();
                    saveBtn.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            final SaveDatasetDialog saveDs = new SaveDatasetDialog();
                            saveDs.getOkBtn().addClickHandler(new ClickHandler() {

                                @Override
                                public void onClick(ClickEvent event) {
                                String newName = saveDs.getName();
                                if(newName == null || newName.equals("")){
                                    saveDs.getErrorlabl().setVisible(true);
                                    saveDs.getForm().validate();
                                    }
                                else{
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
                    lb.addItem(results.get(key));
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
                        datasetInfo = datasetInfos;
                        errorLabel.setText("");
                        path = datasetInfo.getPath();
                        rowLab.setText("Rows : " + datasetInfo.getRowsNumb());
                        colLab.setText("Columns : " + datasetInfo.getColNumb());
                        rowGroup.setText("Row Groups : " + (datasetInfo.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : " + (datasetInfo.getColGroupsNumb()));
                        geneTable = new GeneTableView(selectionManager, datasetInfo);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable);
                        resetLayout();
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
                        hc = new SomClustView(path, result, selectionManager);
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

    private void createRowGroup(final int datasetId, String name, String color, int[] selection,String type) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.createRowGroup(datasetId, name, color, type, selection,
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

    private void createSubDataset( String name,int[] selection){   
         RootPanel.get("loaderImage").setVisible(true);
        greetingService.createSubDataset(name,selection,
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
                        datasetInfo = datasetInfos;
                        errorLabel.setText("");
                        path = datasetInfos.getPath();
                        rowLab.setText("Rows : " + datasetInfos.getRowsNumb());
                        colLab.setText("Columns : " + datasetInfos.getColNumb());
                        rowGroup.setText("Row Groups : " + (datasetInfos.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : " + (datasetInfos.getColGroupsNumb()));
                        geneTable = new GeneTableView(selectionManager, datasetInfo);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable);
                        resetLayout();
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });

    }
    
    private void resetLayout(){
         RootPanel.get("LineChartResults").clear();     
         RootPanel.get("LineChartResults").add(lCImg);
         RootPanel.get("PCAChartResults").clear();
         RootPanel.get("PCAChartResults").add(pcaImg);
         RootPanel.get("RankTablesResults").clear();
           RootPanel.get("RankTablesResults").add(rtImg);
         RootPanel.get("SomClusteringResults").clear();
         
    
    }



}
