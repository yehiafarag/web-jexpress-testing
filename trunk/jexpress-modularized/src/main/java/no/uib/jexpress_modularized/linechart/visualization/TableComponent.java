/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.linechart.visualization;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Vector;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.core.model.SelectionManager;
import no.uib.jexpress_modularized.core.model.ModularizedListener;
import no.uib.jexpress_modularized.core.visualization.InfoTable;
/**
 *
 * @author Yehia Mokhtar
 */


public class TableComponent extends ModularizedListener implements Serializable{
    
    private Vector Vmembers;

    public InfoTable getTable() {
        return table;
    }
    private InfoTable table;
    public TableComponent(Dataset dataset)
    {
        this.data = dataset;
           
        table = new InfoTable();
        table.setVisible(true);       
        this.add(table);
        table.setUseColor(true);
        this.setSize(200, 600);  
        table.setSize(200, 600); 
        
        table.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
	            public void keyReleased(java.awt.event.KeyEvent e) {
	                table.fireSelectionEvent();
	            }
	        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {                   
	            public void mouseReleased(java.awt.event.MouseEvent e) {
	                if (e.getModifiers() != e.BUTTON3_MASK) {
	                    table.fireSelectionEvent();
	                }
	            }
	        });
         HashSet hs = null;
	 if (Vmembers != null) {
	        hs = new HashSet();
	        hs.addAll(Vmembers);
	   }
         table.setData(dataset, hs);
         System.out.println("check sorter "+table.m_sorter == null);
         classtype = 7;
         components.add(TableComponent.this);
        if (SelectionManager.getSelectionManager().getSelectedRows(dataset) != null) //NOTE: should this method be used, or should this class implement the 
        //SelectionChangeListener Interface and use that method instead?
        {     
             selectionChanged(Selection.TYPE.OF_ROWS);
        } else {
           //forceFullRepaint();            
        }
        SelectionManager.getSelectionManager().addSelectionChangeListener(this.data,TableComponent.this);
        

    }
    public void stopDataListening() {
        TableComponent source = null;

        //This is called from the ModularizedListener superClass' method "disconnectData()". Do not use it directly.

        while (components.size() > 0) {

            source = (TableComponent) components.elementAt(0);

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
    public void selectionChanged(no.uib.jexpress_modularized.core.model.Selection.TYPE type) {
        if (type == Selection.TYPE.OF_ROWS) {
            table.setSelfSelection(true);
            table.clearSelection();
            Selection sel = SelectionManager.getSelectionManager().getSelectedRows(data);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();
                if (selectedRows != null) {
                    table.changeSelection(selectedRows); 
                   // g.graphBean1.setDraw(g.infoTable1.getDataSelection());
                }
            } else {
              //  g.graphBean1.setDraw(g.infoTable1.getDataSelection());
            }
            table.setSelfSelection(false);
            table.repaint();
        }
        //TODO: currently no action if Selection type is columns. Should there be?
    }
     
     
     
     public void setAutoResizeMode(int mode){
         table.setAutoResizeMode(mode);
     }
     public  void setUseColor(boolean userColor)
     {
         table.setUseColor(userColor);
     }
     public void createGroup()
     {
       table.createGroup();    
     
     }
     
}
    
