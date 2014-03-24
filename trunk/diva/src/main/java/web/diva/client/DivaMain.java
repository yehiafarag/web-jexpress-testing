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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import web.diva.shared.SelectionManager;
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
import web.diva.shared.beans.HeatMapImgResult;
import web.diva.shared.beans.LineChartResults;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResults;
import web.diva.shared.model.core.model.dataset.DatasetInformation;
import java.util.LinkedHashMap;
import web.diva.shared.Selection;
import web.diva.client.view.ButtonsMenu;
import web.diva.client.view.HeaderLayout;
import web.diva.client.view.InitImgs;
import web.diva.shared.ModularizedListener;
import web.diva.shared.beans.PCAImageResults;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DivaMain implements EntryPoint {

    private SelectionManager selectionManager;
    private static final String SERVER_ERROR = "An error occurred while attempting to contact the server";
    private final Label errorLabel = new Label();
    private final Label rowLab = new Label();
    private final Label colLab = new Label();
    private final Label rowGroup = new Label();
    private final Label colGroup = new Label();
    private int datasetId;
    private final Map<String, Integer> datasetsNames = new TreeMap<String, Integer>();
    private LeftPanelView geneTable;
    private SaveDatasetDialog saveDs;
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
    private final ModularizedListener[]  compponents = new ModularizedListener[2];

    private SomClustView hc;
    private InitImgs initImgs;
    private HeaderLayout header;
    private SomClustPanel somClustPanel;
    private PCAPanel pcaPanel;
    private DatasetPanel dsPanel;
    private ActivateGroupPanel activateGroupPanel;
    private RankPanel rankPanel;
    private int pcaI = 0;
    private int pcaII = 1;

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
         RootPanel.get("SomClusteringResults").add(initImgs.getHcImg());

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
                            if (somClustPanel == null) {
                                somClustPanel = new SomClustPanel();
                                somClustPanel.getOkBtn().addClickHandler(new ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent event) {
                                        RootPanel.get("loaderImage").setVisible(true);
                                        runSomClustering(datasetId, somClustPanel.getX(), somClustPanel.getY());
                                        somClustPanel.hide();

                                    }
                                });
                            }
                            somClustPanel.show();

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
                            int[] selectedRows = null;
                            int[] selectedCol = null;

                            if (rowSel != null) {
                                selectedRows = rowSel.getMembers();
                            }
                            if (colSel != null) {
                                selectedCol = colSel.getMembers();
                            }
                            initDsPanel(selectedRows, selectedCol);
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
                            if (saveDs == null) {
                                saveDs = new SaveDatasetDialog();
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
                                        }
                                    }
                                });
                            }
                            saveDs.show();

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
                        generateHeatMap(hc.getIndexer(), hc.getColIndexer());
                    }
                });

    }

    private void generateHeatMap(List<String> indexers, List<String> colIndexer) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computeHeatmap(datasetId, indexers, colIndexer,
                new AsyncCallback<HeatMapImgResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(HeatMapImgResult result) {
                        RootPanel.get("datasetInformation").setVisible(true);
                        errorLabel.setText("");
                        hc.setImge(result);
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
    }

    private void viewLineChart(int datasetId) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computeLineChart(datasetId,RootPanel.get("LineChartResults").getOffsetWidth(),300.0,
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
                        LineChartComp linechart = new LineChartComp(result, selectionManager, greetingService,initImgs.getlCImg());
                        compponents[0] = linechart;
                        RootPanel.get("LineChartResults").clear();
                        RootPanel.get("LineChartResults").add(linechart.getLayout());
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
    }

    private void viewPCAChart(int datasetId, int pcaI, int pcaII) {
        this.pcaI = pcaI;
        this.pcaII = pcaII;
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computePCA(datasetId, pcaI, pcaII,
                new AsyncCallback<PCAImageResults>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                    }

                    @Override
                    public void onSuccess(PCAImageResults result) {
                        errorLabel.setText("");
                        RootPanel.get("datasetInformation").setVisible(true);
                        PCAPlot pca = new PCAPlot(result, selectionManager, greetingService);
                        compponents[1] = (pca);
                        RootPanel.get("PCAChartResults").clear();
                        RootPanel.get("PCAChartResults").add(pca.getScatterPlotLayout());
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });
    }

    private void viewRankTables(int datasetId, String perm, String seed, String[] colGropNames, String log2) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.computeRank(datasetId, perm, seed, colGropNames, log2,
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
        if (test) {
            RootPanel.get("loaderImage").setVisible(true);
            greetingService.activateGroups(datasetId, rowGroups, colGroups,
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
                            errorLabel.setText("");
                            geneTable = new LeftPanelView(selectionManager, result);
                            RootPanel.get("geneTable").clear();
                            RootPanel.get("geneTable").add(geneTable);
                            updateRowGroups();
//                            resetLayout();
                            RootPanel.get("loaderImage").setVisible(false);
                            result = null;
                            
                        }
                    });
        }

    }

    private void createRowGroup(final int datasetId, String name, String color, String type, int[] selectedRows) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.createRowGroup(datasetId, name, color, type, selectedRows,
                new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorLabel.setText(SERVER_ERROR);
                        RootPanel.get("datasetInformation").setVisible(false);
                        RootPanel.get("loaderImage").setVisible(false);
                        dsPanel.getOkBtn().enable();
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        errorLabel.setText("");
                        RootPanel.get("datasetInformation").setVisible(true);
                        updateApp(datasetId);
//                        updateRowGroups();
                        dsPanel.getOkBtn().enable();
                    }
                });

    }

    private void createColGroup(final int datasetId, String name, String color, String[] selection, String type) {
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

    private void saveDataset(String name) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.saveDataset(datasetId, name,
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
                        RootPanel.get("loaderImage").setVisible(false);
                    }
                });

    }

    private void createSubDataset(String name, int[] selectedRows) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.createSubDataset(name, selectedRows,
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
                        errorLabel.setText("");
                        rowLab.setText("Rows : " + datasetInfos.getRowsNumb());
                        colLab.setText("Columns : " + datasetInfos.getColNumb());
                        rowGroup.setText("Row Groups : " + (datasetInfos.getRowGroupsNumb()));
                        colGroup.setText("Column Groups : " + (datasetInfos.getColGroupsNumb()));
                        geneTable = new LeftPanelView(selectionManager, datasetInfos);
                        RootPanel.get("geneTable").clear();
                        RootPanel.get("geneTable").add(geneTable);
//                        resetLayout();
                        RootPanel.get("loaderImage").setVisible(false);
                        datasetInfos = null;
                        updateRowGroups();
                    }
                });
    }

    private void initGroupsPanel(final int panelType) {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.getGroupsPanelData(datasetId, new AsyncCallback<LinkedHashMap<String, String>[]>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("datasetInformation").setVisible(false);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(LinkedHashMap<String, String>[] results) {
                errorLabel.setText("");
                if (panelType == 1)//activateGroupPanel
                {
                    if (activateGroupPanel == null) {
                        activateGroupPanel = new ActivateGroupPanel(results[0], results[1]);
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
                                    }
                                } catch (Exception exp) {
                                }

                            }
                        });
                    } else {
                        activateGroupPanel.updataChartData(results[0], results[1]);
                    }
                    activateGroupPanel.show();
                } else if (panelType == 2) {

                    if (rankPanel == null) {
                        rankPanel = new RankPanel(results[1]);
                        rankPanel.getOkBtn().addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                rankPanel.getErrorlabl().setVisible(false);
                                String[] groups = rankPanel.getSelectColGroups();
                                String seed = rankPanel.getSeed();
                                String perm = rankPanel.getPermutation();
                                String log2 = rankPanel.getRadioGroupItem();
                                if (groups == null || groups.length == 0 || groups.length > 2 || seed == null || seed.equals("") || perm == null || perm.equals("")) {
                                    rankPanel.getErrorlabl().setVisible(true);
                                    rankPanel.getForm2().validate();
                                } else {
                                    viewRankTables(datasetId, perm, seed, groups, log2);
                                    rankPanel.hide();
                                }
                            }
                        });
                    } else {
                        rankPanel.updateData(results[1]);
                    }
                    rankPanel.show();

                }
                results = null;
                RootPanel.get("loaderImage").setVisible(false);
            }

        });

    }

    private void initPcaPanel() {
        RootPanel.get("loaderImage").setVisible(true);
        greetingService.getPcaColNames(datasetId, new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("datasetInformation").setVisible(false);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(String[] results) {
                errorLabel.setText("");
                if (pcaPanel == null) {
                    pcaPanel = new PCAPanel(results);
                    pcaPanel.getOkBtn().addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            try {
                                int pcaI = pcaPanel.getPcaI();
                                int pcaII = pcaPanel.getPcaII();
                                viewPCAChart(datasetId, pcaI, pcaII);
                                pcaPanel.hide();

                            } catch (Exception exp) {
                            }

                        }
                    });
                }
                pcaPanel.show();

                results = null;
                RootPanel.get("loaderImage").setVisible(false);
            }

        });

    }

    private void initDsPanel(final int[] selectedRows, final int[] selectedCol) {

        RootPanel.get("loaderImage").setVisible(true);
        greetingService.getColNamesMaps(datasetId, new AsyncCallback<LinkedHashMap<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                errorLabel.setText(SERVER_ERROR);
                RootPanel.get("datasetInformation").setVisible(false);
                RootPanel.get("loaderImage").setVisible(false);
            }

            @Override
            public void onSuccess(LinkedHashMap<String, String> results) {
                errorLabel.setText("");
                if (dsPanel == null) {
                    dsPanel = new DatasetPanel(results, selectedRows, selectedCol);
                    dsPanel.getOkBtn().addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            dsPanel.getOkBtn().disable();
                            dsPanel.getErrorlabl().setVisible(false);
                            String processType = dsPanel.getProcessType();
                            if (processType.equalsIgnoreCase("Groups")) {
                                String name = dsPanel.getName().getValueAsString();
                                String color = dsPanel.getColor();
                                String type = dsPanel.getRadioGroupItemValue();
                                if (name == null || name.equals("")) {
                                    dsPanel.getErrorlabl().setVisible(true);
                                    dsPanel.getForm().validate();
                                } else if (type.equalsIgnoreCase("COLUMN GROUPS")) {
                                    String[] selCol = dsPanel.getSelectColsValue();
                                    if (selCol == null || selCol.length == 0) {
                                        dsPanel.getErrorlabl().setVisible(true);
                                        dsPanel.getForm().validate();
                                    } else {
                                        dsPanel.getErrorlabl().setVisible(false);
                                        createColGroup(datasetId, name, color, selCol, type);
                                        dsPanel.hide();
                                    }
                                } else {
                                    dsPanel.getErrorlabl().setVisible(false);

                                    createRowGroup(datasetId, name, color, type, dsPanel.getRowSelection());
                                    dsPanel.hide();

                                }

                            } else //save sub-dataset
                            {
                                String name = dsPanel.getDatasetName().getValueAsString();
                                if (name == null || name.equals("")) {
                                    dsPanel.getErrorlabl().setVisible(true);
                                    dsPanel.getForm2().validate();
                                } else {
                                    createSubDataset(name, selectedRows);
                                    dsPanel.hide();

                                }

                            }

                        }
                    });

                } else {
                    dsPanel.updateDataValues(selectedRows, selectedCol);
                }
                dsPanel.show();
                results = null;
                RootPanel.get("loaderImage").setVisible(false);
            }

        });

    }
/*in case of changing datasets only*/
    private void resetLayout() {
        RootPanel.get("LineChartResults").clear();
        RootPanel.get("LineChartResults").add(initImgs.getlCImg());
        RootPanel.get("PCAChartResults").clear();
        RootPanel.get("PCAChartResults").add(initImgs.getPcaImg());        
        RootPanel.get("SomClusteringResults").clear();
         RootPanel.get("SomClusteringResults").add(initImgs.getHcImg());
            hc=null;
            compponents[0]= null;
            compponents[1]=null;
             RootPanel.get("RankTablesResults").clear();
         RootPanel.get("RankTablesResults").add(initImgs.getRtImg());
    }
    
    /*on activate or create new row groups*/
    private void updateRowGroups() {
        for (ModularizedListener o : compponents) {
            if(o == null)
                continue;
            if (o instanceof LineChartComp) {
                viewLineChart(datasetId);

            }
            else{
                viewPCAChart(datasetId, pcaI,pcaII);            
            }
        }

    }
}
