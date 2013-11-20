/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.somclust.view;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;
import java.util.Comparator;
import org.thechiselgroup.choosel.protovis.client.Link;
import org.thechiselgroup.choosel.protovis.client.MiserablesData.NovelCharacter;

import org.thechiselgroup.choosel.protovis.client.PV;
import org.thechiselgroup.choosel.protovis.client.PVColor;
import org.thechiselgroup.choosel.protovis.client.PVLink;
import org.thechiselgroup.choosel.protovis.client.PVMatrixLayout;
import org.thechiselgroup.choosel.protovis.client.PVNode;
import org.thechiselgroup.choosel.protovis.client.PVOrdinalScale;
import org.thechiselgroup.choosel.protovis.client.PVPanel;
import org.thechiselgroup.choosel.protovis.client.ProtovisWidget;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsArgs;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsFunction;
import web.jexpress.client.somclust.view.HeatmapData.HeatmapCell;
import web.jexpress.client.somclust.view.HeatmapData.HeatmapNodeAdapter;

/**
 *
 * @author Yehia Farag
 */
public class HeatMap extends ProtovisWidget implements IsSerializable {

    @Override
    public Widget asWidget() {
        return this;
    }
     private void createVisualization(HeatmapData.HeatmapCell[] nodes, Link[] links) {
        final PVOrdinalScale color = PV.Colors.category19();
        PVPanel vis = getPVPanel().width(693).height(693).top(90).left(90);

        PVMatrixLayout layout = vis.add(PV.Layout.Matrix())
                
                .nodes(new HeatmapNodeAdapter(), nodes)
                
                .links(links)
                .sort(new Comparator<PVNode>() {
                    public int compare(PVNode a, PVNode b) {
                         HeatmapCell ac = a.object();
                        HeatmapCell bc = b.object();
                        return bc.getGroup() - ac.getGroup();
                    }
                });
        layout.link().add(PV.Bar).fillStyle(new JsFunction<PVColor>() {
            public PVColor f(JsArgs args) {
                PVLink l = args.getObject();
                if (l.linkValue() != 0) {
                    int targetGroup = l.targetNode().<HeatmapCell>object()
                            .getGroup();
                    int sourceGroup = l.sourceNode().<HeatmapCell>object()
                            .getGroup();
                    return (targetGroup == sourceGroup) ? color
                            .fcolor(sourceGroup) : PV.color("#555");
                }
                return PV.color("#eee");
            }
        }).antialias(false).lineWidth(3);

//        layout.label().add(PV.Label).textStyle(new JsFunction<PVColor>() {
//            public PVColor f(JsArgs args) {
//                PVNode node = args.getObject();
//                return color.fcolor(node.<HeatmapCell> object().getGroup());
//            }
//        });
        layout.label().add(PV.Label);
    }

    

    public String getProtovisExampleURL() {
        return "http://vis.stanford.edu/protovis/ex/matrix.html";
    }

    public String getSourceCodeFile() {
        return "MatrixDiagramExample.java";
    }

    protected void onAttach() {
        super.onAttach();
        initPVPanel();
        createVisualization(HeatmapData.CELLS, HeatmapData.LINKS);
        getPVPanel().render();
    }

    public String toString() {
        return "Matrix Diagram";
    }
//
//    public static class MyDataClass {
//
//        public MyDataClass[] children;
//        public int value;
//        public String name;
//        private String additionalProperty = null;
//
//        public MyDataClass(String name, int value) {
//            this.value = value;
//            this.name = name;
//        }
//
//        public MyDataClass(String name, int value, String addionalProperty) {
//            this.value = value;
//            this.name = name;
//            this.additionalProperty = addionalProperty;
//        }
//
//        public MyDataClass(String name, MyDataClass... children) {
//            this.children = children;
//            this.name = name;
//        }
//
//        public String getAdditionalProperty() {
//            return additionalProperty;
//        }
//    }
//
//    public static class MyDomAdapter implements PVDomAdapter<MyDataClass> {
//
//        public MyDataClass[] getChildren(MyDataClass t) {
//            return t.children == null ? new MyDataClass[0] : t.children;
//        }
//
//        public String getNodeName(MyDataClass t) {
//            return t.name;
//        }
//
//        public double getNodeValue(MyDataClass t) {
//            return t.value;
//        }
//    }
//
//    private  MyDataClass data() {
//        return  new MyDataClass("flare", new MyDataClass("row1", 10), new MyDataClass("col2", 10),
//                new MyDataClass("row2", 10), new MyDataClass("col2", 10), new MyDataClass("row3",10), new MyDataClass("col2", 10),
//                new MyDataClass("row4",10), new MyDataClass("col2", 10),
//                new MyDataClass("row5",10), new MyDataClass("col2", 10));
//    }
//    @Override
//    public Widget asWidget() {
//        return this;
//    }
//    int w = 24, h = 13;
//
//    private void createVisualization(NovelCharacter[] nodes, Link[] links) {
////
////        Object[] nba = new Object[]{"Yehia","Tamer","Mohsen"};
////       
////        
////        Object[] cols = new Object[]{"lol1","lol2","lol3"};
////        
////        
////        
////        Object[] nbaData[] = new Object[3][];
////        nbaData[0] = new Object[]{10,12,16};
////        nbaData[1] = new Object[]{20,25,36};
////        nbaData[2] = new Object[]{30,40,50};
////        
////        Object[] colsData[] = new Object[3][];
////        cols[0] = new Object[]{10,20,30};
////        cols[1] = new Object[]{12,25,40};
////        cols[2] = new Object[]{16,36,50};
////        
////        
////        
////      
////
////
////
////
////        final PVPanel vis = getPVPanel().width(500).height(500).top(30).left(100);
////        vis.add(PV.Panel)
////                .data(nbaData).top(20)
////                .width(20*nba.length)
////                .add(PV.Panel)
////                .data(colsData).left(w)
////                .height(20*cols.length)
//////                .fillStyle(new JsFunction<PVColor>() {
//////            @Override
//////            public PVColor f(JsArgs args) {
//////                return PV.color("red");
//////            }
//////        })
////                .strokeStyle("black")
////                .lineWidth(1)
////                .antialias(false);
////        vis.add(PV.Label)
////                .data(nba)
////                .textAngle(-Math.PI / 2)
////                .textBaseline("middle")
//////                .text(new JsStringFunction() {
//////            @Override
//////            public String f(JsArgs args) {
//////                return args.getObject().toString();
//////               // return "function";
//////            }
//////        })
////                ;
////        vis.add(PV.Label)
////    .data(cols)
////   // .top(function() this.index * h + h / 2)
////    .textAlign("right")
////    .textBaseline("middle")
////    .text("lolo");
////
////
////        vis.add(PV.Label)
////                .data(cols)
////                .textAlign("right")
////                .textBaseline("middle");
////
////        vis.render();
//        
//        
//        /*********************************************************************************/
//
////        treemap.leaf().add(PV.Panel).fillStyle(new JsFunction<PVColor>() {
////            public PVColor f(JsArgs args) {
////                PVDomNode d = args.getObject();
////                PVMark _this = args.getThis();
////
////                if (vis.getInt("i") == _this.index()) {
////                    return PV.color("orange");
////                }
////
////                if (d.parentNode() == null) {
////                    return category19.fcolor((String) null);
////                }
////                return category19.fcolor(d.parentNode().nodeName());
////            }
////        }).strokeStyle("#fff").lineWidth(1d).antialias(false)
////                .event(PV.Event.MOUSEOVER, new PVEventHandler() {
////            @Override
////            public void onEvent(com.google.gwt.user.client.Event e, String pvEventType, JsArgs args) {
////                PVMark _this = args.getThis();
////                vis.set("i", _this.index());
////                _this.render();
////
////            }
////        }).event(PV.Event.MOUSEOUT, new PVEventHandler() {
////            public void onEvent(com.google.gwt.user.client.Event e, String pvEventType, JsArgs args) {
////                PVMark _this = args.getThis();
////                vis.set("i", -1);
////                _this.render();
////            }
////        });
////
////        treemap.label().add(PV.Label).font(new JsStringFunction() {
////            public String f(JsArgs args) {
////                PVDomNode d = args.getObject();
////                MyDataClass myData = d.nodeObject();
////                return myData.getAdditionalProperty() != null ? "bold 20px serif"
////                        : "italic 10px serif";
////            }
////        });
//        
//        
//        final PVOrdinalScale color = PV.Colors.category19();
//        PVPanel vis = getPVPanel().width(693).height(693).top(90).left(90);
//
//        PVMatrixLayout layout = vis.add(PV.Layout.Matrix())
//                .nodes(new NovelCharacterNodeAdapter(), nodes).links(links)
//                .sort(new Comparator<PVNode>() {
//                    public int compare(PVNode a, PVNode b) {
//                        NovelCharacter ac = a.object();
//                        NovelCharacter bc = b.object();
//                        return bc.getGroup() - ac.getGroup();
//                    }
//                });
//        layout.link().add(PV.Bar).fillStyle(new JsFunction<PVColor>() {
//            public PVColor f(JsArgs args) {
//                PVLink l = args.getObject();
//                if (l.linkValue() != 0) {
//                    int targetGroup = l.targetNode().<NovelCharacter> object()
//                            .getGroup();
//                    int sourceGroup = l.sourceNode().<NovelCharacter> object()
//                            .getGroup();
//                    return (targetGroup == sourceGroup) ? color
//                            .fcolor(sourceGroup) : PV.color("#555");
//                }
//                return PV.color("#eee");
//            }
//        }).antialias(false).lineWidth(1);
//
//        layout.label().add(PV.Label).textStyle(new JsFunction<PVColor>() {
//            public PVColor f(JsArgs args) {
//                PVNode node = args.getObject();
//                return color.fcolor(node.<NovelCharacter> object().getGroup());
//            }
//        });
//    }
//
//    public String getProtovisExampleURL() {
//        return null;
//    }
//
//    public String getSourceCodeFile() {
//        return "TreemapExample2.java";
//    }
//
//    protected void onAttach() {
//        super.onAttach();
//        initPVPanel();
//        createVisualization(HeatmapData.CHARACTERS, HeatmapData.LINKS);
//        getPVPanel().render();
//    }
//
//    public String toString() {
//        return "Treemaps (highlighting, font)";
//    }
}
