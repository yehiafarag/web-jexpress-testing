/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.somclust.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.TreeMap;
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
import web.jexpress.client.core.model.SelectionManager;
import web.jexpress.shared.CustomNode;
import web.jexpress.shared.Unit;
import web.jexpress.shared.UnitDomAdapter;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.Selection;

/**
 *
 * @author Yehia Farag
 */
public final class TreeGraph extends ProtovisWidget implements IsSerializable {

    private final Unit root;
    private final SelectionManager selectionManager;
    private final SomClusteringResults results;
    private final TreeMap<String, CustomNode> nodesMap;
    private final List<String> indexers = new ArrayList<String>();
    private boolean initIndexer = true;
    private int height;
    private int width;
    
    public void resize(double width,double height)
    {
        this.height =(int) height;
        this.width =(int) width;
        getPVPanel().render();   
    
    }

    public List<String> getIndexers() {
        return indexers;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public TreeGraph(SomClusteringResults results, String orintation, SelectionManager selectionManager, int height, int width) {

        this.root = results.getSideTree();
        this.nodesMap = root.getNodesMap();
        this.selectionManager = selectionManager;
        this.results = results;
        this.width = (width*2/3);
        this.height = height;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        initPVPanel();
        createVisualization(root);
        getPVPanel().render();
        initIndexer = false;
    }
    PVEventHandler nodeMouseClickHandler = new PVEventHandler() {
        @Override
        public void onEvent(com.google.gwt.user.client.Event e, String pvEventType, JsArgs args) {
            PVClusterLayout _this = args.getThis();
            PVDomNode d = args.getObject();
            if (_this != null && d != null && (!d.nodeName().equalsIgnoreCase(clickNode))) {
                clickNode = d.nodeName();
                clickedNode = nodesMap.get(clickNode);
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
        vis = getPVPanel().width(width).height(height).left(0).right(5)
                .top(0).bottom(0);//.def("i", "-1").def("ii", "-1");
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
                if (initIndexer) {
                    if (n.firstChild() == null) {
                        indexers.add(n.nodeName());
                    }
                }

                if (n.nodeName().equalsIgnoreCase(overNode)) {//(cNode != null && cNode.getName().equalsIgnoreCase/*getChildernList().contains*/(n.nodeName())) {//vis.getObject("i").toString().equalsIgnoreCase(n.nodeName())) {
                    return 2.0;
                }
                return 0.5;
            }
        }).shape("square")
                .fillStyle(new JsStringFunction() {
                    public String f(JsArgs args) {
                        PVDomNode n = args.getObject();
                        if (n.nodeName().equalsIgnoreCase(overNode)) {// if (cNode != null && cNode.getChildernList().contains(n.nodeName())) {//  if (vis.getObject("i").toString().equalsIgnoreCase(n.nodeName())) {
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
                                // CustomNode cNode = util.getCustomNode(n.nodeName());
                                return results.getToolTips().get(n.nodeName());//"kokowawaw";//n.firstChild() != null ? "Merged at "+cNode.getValue()+" Nodes : "+(cNode.getSelectedNodes().length) :"  ";
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
                if (clickedNode != null && clickedNode.getChildernList().contains(n.nodeName())) {// && (!cNode.getName().equalsIgnoreCase(n.nodeName()))) {//vis.getObject("i").toString().equalsIgnoreCase(n.nodeName())) {
                    return "#FF4000";
                }
                return "#ccc";
            }
        });

    }

    private void updateSelectedList(int[] selIndex) {
        Selection selection = new Selection(Selection.TYPE.OF_ROWS, selIndex);
        selectionManager.setSelectedRows(results.getDatasetId(), selection);

    }
}
