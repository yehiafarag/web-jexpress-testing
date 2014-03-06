/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.HashMap;
import org.thechiselgroup.choosel.protovis.client.PV;
import org.thechiselgroup.choosel.protovis.client.PVClusterLayout;
import org.thechiselgroup.choosel.protovis.client.PVDom;
import org.thechiselgroup.choosel.protovis.client.PVDomNode;
import org.thechiselgroup.choosel.protovis.client.PVEventHandler;
import org.thechiselgroup.choosel.protovis.client.PVLine;
import org.thechiselgroup.choosel.protovis.client.PVPanel;
import org.thechiselgroup.choosel.protovis.client.ProtovisWidget;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsArgs;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsDoubleFunction;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsStringFunction;
import web.diva.shared.Selection;
import web.diva.shared.SelectionManager;
import web.diva.shared.CustomNode;
import web.diva.shared.Unit;
import web.diva.shared.UnitDomAdapter;
import web.diva.shared.beans.SomClusteringResults;

/**
 *
 * @author Yehia Farag
 */
public final class SideTreeGraph extends ProtovisWidget {

    private Unit root;
    private  SelectionManager selectionManager;
    private  HashMap<String, CustomNode> nodesMap;
     private  HashMap<String, String> tooltips;
    private List<String> indexers = new ArrayList<String>();
    
    private boolean initIndexer = true;
    private double height;
    private double width;
    private final double top;
    private final int datasetId;
    private boolean clear= true;
    
    public void resize(double width,double height)
    {
        this.height = height;
        this.width = width;
        getPVPanel().render();       
    }

    public List<String> getIndexers() {
        return indexers;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public SideTreeGraph(SomClusteringResults results, String orintation, SelectionManager selectionManager, double height, double width,double top ) {

        this.root = results.getSideTree();
        this.datasetId = results.getDatasetId();
        this.nodesMap = new HashMap<String, CustomNode>();
        nodesMap.putAll((root.getNodesMap()));
        this.tooltips = new HashMap<String, String>();
        tooltips.putAll(results.getToolTips());
        this.selectionManager = selectionManager;
        this.width = (width*2.0/3.0);
        this.height = height;
        this.top = top;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        initPVPanel();
        createVisualization(root);
        getPVPanel().render();
        initIndexer = false;
        root = null;
    }

    @Override
    protected void onDetach() {
        super.onDetach(); //To change body of generated methods, choose Tools | Templates.
        tooltips = null;
        root = null;
        nodesMap = null;
        selectionManager = null;
        vis = null;
    }
    PVEventHandler nodeMouseClickHandler = new PVEventHandler() {
        @Override
        public void onEvent(com.google.gwt.user.client.Event e, String pvEventType, JsArgs args) {
            PVClusterLayout _this = args.getThis();
            PVDomNode d = args.getObject();
            if (_this != null && d != null && d.firstChild()!= null &&(!d.nodeName().equalsIgnoreCase(clickNode))) {
                clickNode = d.nodeName();
                clickedNode = nodesMap.get(clickNode);
                clear = false;
                vis.render();
                updateSelectedList(clickedNode.getSelectedNodes());
            }
        }
    };
    PVEventHandler nodeMouseOverHandler = new PVEventHandler() {
        @Override
        public void onEvent(com.google.gwt.user.client.Event e, String pvEventType, JsArgs args) {
            PVClusterLayout _this = args.getThis();
            PVDomNode d = args.getObject();
            if (_this != null && d != null) {
                overNode = d.nodeName();
                vis.render();
            }
        }
    };
    private PVPanel vis;
    private String clickNode = "";
    private String overNode = "";
    private CustomNode clickedNode;

    private void createVisualization(Unit root) {
        vis = getPVPanel().width(width).height(height).left(10).right(5)
                .top(top).bottom(0);
        PVClusterLayout layout = vis
                .add(PV.Layout.Cluster())
                .nodes(PVDom.create(root, new UnitDomAdapter())
                        .sort(new Comparator<PVDomNode>() {
                            @Override
                            public int compare(PVDomNode o1, PVDomNode o2) {
                                return o1.nodeName().compareTo(o2.nodeName());
                            }
                        })
                        .nodes()).group(false).orient("left");
        layout.node().add(PV.Dot).radius(new JsDoubleFunction() {
            @Override
            public double f(JsArgs args) {
                PVDomNode n = args.getObject();
                if (initIndexer && indexers != null) {
                    if (n.firstChild() == null) {
                        indexers.add(n.nodeName());
                    }
                }

                if (n.nodeName().equalsIgnoreCase(overNode)) {
                    return 3.0;
                }
                return 0.5;
            }
        }).shape("square")
                .fillStyle(new JsStringFunction() {
                    @Override
                    public String f(JsArgs args) {
                        PVDomNode n = args.getObject();
                        if (n.nodeName().equalsIgnoreCase(overNode)) {
                            return "#FF4000";
                        }
                        return "#ccc";
                    }
                })
                .title(
                        new JsStringFunction() {
                            @Override
                            public String f(JsArgs args) {
                                PVDomNode n = args.getObject();
                                return tooltips.get(n.nodeName());
                            }
                        })
                .events("all")
                .event(PV.Event.CLICK, nodeMouseClickHandler)
                .event(PV.Event.MOUSEOVER, nodeMouseOverHandler);

        PVLine line = layout.link().add(PV.Line);

        line.lineWidth(1d).strokeStyle(new JsStringFunction() {
            @Override
            public String f(JsArgs args) {
                PVDomNode n = args.getObject();
                if (clickedNode != null && clickedNode.getChildernList().contains(n.nodeName())) {
                    return "#FF4000";
                }
                return "#ccc";
            }
        });

    }

    private void updateSelectedList(int[] selIndex) {
       
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selIndex);
        selectionManager.setSelectedRows(datasetId, selection,2);
        selection = null;
       

    }
    public void clearIndexer()
    {
        indexers = null;
    }
    public void clearSelection(){
    
    if (!clear) {
            clickedNode = null;
            overNode = "";
            clear = true;
            vis.render();
        }
    }
}
