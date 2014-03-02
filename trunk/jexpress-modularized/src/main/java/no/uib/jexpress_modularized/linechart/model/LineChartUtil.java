package no.uib.jexpress_modularized.linechart.model;

import java.io.Serializable;
import no.uib.jexpress_modularized.core.dataset.Dataset;
//import no.uib.jexpress_modularized.linechart.visualization.GraphView;
//import no.uib.jexpress_modularized.linechart.visualization.LineChart;

public class LineChartUtil implements Serializable{
	 public boolean[]  createAllMembers(Dataset data) {
		 boolean[] members = new boolean[data.getDataLength()];
	        for (int i = 0; i < members.length; i++) {
	            members[i] = true;
	        }
	        return members;
	    }
//         public GraphView initGraphView(GraphView graphView)
//         { 
//             graphView.setTitle("Gene Graph" + datasetName
//	                );
//             graphView.setVisible(true);
//             return graphView;
//         }
//         
//         public LineChart initGraph(LineChart graph,GraphView graphView)
//         {
//            
//             graph.setPropertiesAndScrollPane(graphView.jScrollPane1);
//         
//             return graph;
//         }
	 
	 public void saveImage() {
	  //      g.graphBean1.LockFullRepaint = true;
	        //@TODO: reimplement me?
//	          org.freehep.util.export.ExportDialog export = new org.freehep.util.export.ExportDialog("Export");
//	          export.showExportDialog( cl.MW, "Export view as ...", g.graphBean1, "export" );
	  //      g.graphBean1.LockFullRepaint = false;
	    }

	    public void printImage() {
	       // g.graphBean1.LockFullRepaint = true;
	        //@TODO: reimplement me 
//	        expresscomponents.Print.PrintPreview2 pw = new expresscomponents.Print.PrintPreview2(cl.MW, true);
//	        pw.setComponent(g.graphBean1);
//	        pw.setVisible(true);
	     //   g.graphBean1.LockFullRepaint = false;
	    }

	    public void copyImage() {
	     //   g.graphBean1.LockFullRepaint = true;
	        //@TODO: reimplement me?
//	        org.freehep.util.export.VectorGraphicsTransferable vt = new org.freehep.util.export.VectorGraphicsTransferable(g.graphBean1);


	        //expresscomponents.JEVectorGraphicsTransferable vt = new expresscomponents.JEVectorGraphicsTransferable(g.graphBean1);
	   //     java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();

	        //@TODO: reimplement me 
//	        clipboard.setContents(vt, vt);
	     //   g.graphBean1.LockFullRepaint = false;
	    }

}
