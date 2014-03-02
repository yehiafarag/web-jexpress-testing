/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.linechart.visualization;

import java.io.Serializable;
import javax.swing.JFrame;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.model.SelectionManager;
import no.uib.jexpress_modularized.linechart.model.LineChartUtil;



/**
 *
 * @author Yehia Mokhtar
 */
public class LineChartView implements Serializable{
    private Dataset dataset;
    private boolean[] members;
    private TableComponent infoTable;

    public TableComponent getInfoTable() {
        return infoTable;
    }

    public void setInfoTable(TableComponent infoTable) {
        this.infoTable = infoTable;
    }

    public LineChartGraphComponent getGraph() {
        return graph;
    }

    public void setGraph(LineChartGraphComponent graph) {
        this.graph = graph;
    }
    private LineChartGraphComponent graph;
   private JFrame j ;

    public JFrame getJ() {
        return j;
    }

   
    public LineChartView(Dataset dataset,boolean[]members)
    {
        
        this.dataset =dataset;
        this.members = members;
        
        infoTable =new TableComponent(dataset);        
        infoTable.setVisible(true);
        graph = new LineChartGraphComponent(dataset, members);
        graph.setVisible(true);
        
        
        
        //set the graph and the tableinto the control panel  
        LinChartControlPanel panel = new LinChartControlPanel(infoTable,graph);
        j = new JFrame(); 
        j.add(panel);
        j.setVisible(true);
        j.setSize(700, 500);
        j.setLocationRelativeTo(null);
    
        
        /*
        JFrame j = new JFrame(); 
        j.add(infoTable);
        j.setVisible(true);
        j.setSize(500, 500);
        j.setLocationRelativeTo(null);
        
        JFrame j2 = new JFrame();      
        j2.add(graph);
        j2.setVisible(true);
        j2.setSize(500, 500);
        j2.setLocationRelativeTo(null);
        
        */ 
        
    }
   
    
    
}
