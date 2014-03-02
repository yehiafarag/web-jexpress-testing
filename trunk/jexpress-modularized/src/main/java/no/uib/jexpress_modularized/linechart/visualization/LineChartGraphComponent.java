/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.linechart.visualization;

import java.awt.Dimension;
import java.io.Serializable;
import javax.swing.JScrollPane;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.core.model.SelectionManager;
import no.uib.jexpress_modularized.core.model.ModularizedListener;

/**
 *
 * @author Yehia Mokhtar
 */
public class LineChartGraphComponent extends ModularizedListener  implements Serializable{
    
    private no.uib.jexpress_modularized.linechart.visualization.LineChart graph;
    private boolean zoomed;
    private boolean aalias = false;
    private boolean selectList = true;

    public boolean isSelectList() {
        return selectList;
    }

    public void setSelectList(boolean selectList) {
        this.selectList = selectList;
    }

    public LineChart getGraph() {
        return graph;
    }
    private int startlabel = -1;
    private int endlabel = -1;
    private double max = -1;
    private double min = -1;
    private boolean[] members;
    public LineChartGraphComponent(Dataset dataset,boolean[] members)
    {
        this.members= members;
        this.data = dataset;
        classtype = 8;
        components.add(LineChartGraphComponent.this);
        this.graph = new LineChart();
        this.add(graph);
        
        int cnt = 0;
	        if (members == null) {
	            cnt = data.getDataLength();
	        } else {
	            for (int i = 0; i < members.length; i++) {
	                if (members[i]) {
	                    cnt++;
	                }
	            }
	        }
	        int autoalias = 500;

	        //@TODO: reimplement me?
//	            if(cl.props.containsKey("aalias")){
//	                autoalias = ((Integer)cl.props.get("aalias")).intValue();
//	            }

	        if (cnt < autoalias) {
	            aalias = true;
	        }
        
        graph.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
	            public void mouseReleased(java.awt.event.MouseEvent e) {
	                if (e.getModifiers() != e.BUTTON3_MASK) {
	                    double[] rect = graph.getValueFrame();
	                    if (rect != null && !graph.move) {
	                        createZoomedGraph(rect[1], rect[0], (int) rect[2], (int) rect[3]);
	                    }
	                }
	            }
	        });
          if (zoomed) {
	            graph.setData(dataset, Math.min(startlabel, endlabel), Math.max(startlabel, endlabel), max, min, members);
	        } else {
	            graph.setData(dataset, members);
	            
	        }
        if (aalias) {
	            graph.antialias = aalias;

	            //@TODO: reimplement me?
//	            if (data.getSelectedRows() != null) {
//	                SelectionHasChanged(this);
//	            } else {
//	                g.graphBean1.forceFullRepaint();
//	            }
	        }
        
         if (SelectionManager.getSelectionManager().getSelectedRows(data) != null && selectList == true) //NOTE: should this method be used, or should this class implement the 
        //SelectionChangeListener Interface and use that method instead?
        {    
             selectionChanged(Selection.TYPE.OF_ROWS);
        } else {
           this.forceFullRepaint();            
        }
          SelectionManager.getSelectionManager().addSelectionChangeListener(this.data,LineChartGraphComponent.this);
        

        
    
    }
    
    
    
    public void createZoomedGraph(double min, double max, int startColumn, int endColumn) {

        if (Math.abs(startColumn - endColumn) > 1) {
            //OBS: clue to where the actual graph is?
            //@TODO: reimplement me 
//            if (graphView != null) {
//                mainGraph zoomed = new mainGraph(data, cl, false, min, max, startColumn, endColumn, Vmembers);
//                zoomed.graphView = graphView;
//                graphView.components.add(zoomed);
//                graphView.g.JTabbedPane1.add(zoomed.g.JPanel8, "Zoomed");
//                graphView.g.JTabbedPane1.setSelectedIndex(graphView.g.JTabbedPane1.getComponentCount() - 1);
//            } else if (pcaView != null) {
//                mainGraph zoomed = new mainGraph(data, cl, false, min, max, startColumn, endColumn, Vmembers);
//                zoomed.pcaView = pcaView;
//                zoomed.g.JTabbedPane1.remove(zoomed.g.JPanel8);
//                pcaView.components.add(zoomed);
//                pcaView.pd.JTabbedPane1.add("Zoom", zoomed.g.JPanel8);
//                pcaView.pd.JTabbedPane1.setSelectedIndex(pcaView.pd.JTabbedPane1.getComponentCount() - 1);
//            } else if (mdsView != null) {
//                mainGraph zoomed = new mainGraph(data, cl, false, min, max, startColumn, endColumn, Vmembers);
//                zoomed.mdsView = mdsView;
//                zoomed.g.JTabbedPane1.remove(zoomed.g.JPanel8);
//                mdsView.components.add(zoomed);
//                mdsView.pd.JTabbedPane1.add("Zoom", zoomed.g.JPanel8);
//                mdsView.pd.JTabbedPane1.setSelectedIndex(mdsView.pd.JTabbedPane1.getComponentCount() - 1);
//            }
//             else if(kmeansView!=null){
//                 mainGraph zoomed = new mainGraph(data,cl,false,min,max,startColumn,endColumn,Vmembers);
//                 zoomed.kmeansView=kmeansView;
//                 zoomed.g.JTabbedPane1.remove(zoomed.g.JPanel8);
//                 kmeansView.components.add(zoomed);
//                 kmeansView.kr.JTabbedPane1.add("Zoom",zoomed.g.JPanel8);
//                 kmeansView.kr.JTabbedPane1.setSelectedIndex( kmeansView.kr.JTabbedPane1.getComponentCount()-1);
//             }
//              else if(thumbView!=null){
//                 mainGraph zoomed = new mainGraph(data,cl,false,min,max,startColumn,endColumn,Vmembers);
//                 zoomed.thumbView=thumbView;
//                 zoomed.g.JTabbedPane1.remove(zoomed.g.JPanel8);
//                 thumbView.components.add(zoomed);
//                 thumbView.kr.JTabbedPane1.add("Zoom",zoomed.g.JPanel8);
//                 thumbView.kr.JTabbedPane1.setSelectedIndex( thumbView.kr.JTabbedPane1.getComponentCount()-1);
//             }
        }



    }
    
    
    public void setPropertiesAndScrollPane(JScrollPane sp)
    {
        graph.setPropertiesAndScrollPane(sp);
        setSize(graph.getSize());
        this.forceFullRepaint();
    }
    
        public void stopDataListening() {
        LineChartGraphComponent source = null;

        //This is called from the ModularizedListener superClass' method "disconnectData()". Do not use it directly.

        while (components.size() > 0) {

            source = (LineChartGraphComponent) components.elementAt(0);

            //TODO: reimplement. reimplement by using selectionManager?
//            source.data.removeDataListener(source);


            //source.data.removeSelectionListener(source.g.infoTable1);
            //  source.data.removeSelectionListener(source);
            
            
            //itsmineeee
            //source.disposeAll();
            components.remove(source);
        }



    }
        @Override
    public void selectionChanged(Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            //g.infoTable1.setSelfSelection(true);
            //g.infoTable1.clearSelection();
            
            if(selectList== true){
            Selection sel = SelectionManager.getSelectionManager().getSelectedRows(data);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                if (selectedRows != null) {
                    //g.infoTable1.changeSelection(selectedRows); 
                    graph.setDraw(graph.getDataSelection(selectedRows));
                }
            } else {
                    graph.setDraw(graph.getDataSelection(new int[]{}));
            }
            graph.repaint();
            //g.infoTable1.setSelfSelection(false);
            }
        }
        //TODO: currently no action if Selection type is columns. Should there be?
    }
        public boolean getEnableSpeeding()
        {
            return graph.enableSpeeding;
        }
        public void setEnableSpeeding(boolean enableSpeeding)
        {
            this.graph.enableSpeeding = enableSpeeding;
        }
        public void forceFullRepaint()
        {
            this.graph.forceFullRepaint();
        }
        
        public  Dimension getPreferredScrollableViewportSize()
        {
            return this.graph.getPreferredScrollableViewportSize();
        }
        
        public void setPreferredCompoSize(Dimension d)
        {
            this.setPreferredSize(d);
            this.graph.setPreferredSize(d);
        }
        
        public void setMaximumCompoSize(Dimension d)
        {
            this.setMaximumSize(d);
            this.graph.setMaximumSize(d);
        }
    
    
}
