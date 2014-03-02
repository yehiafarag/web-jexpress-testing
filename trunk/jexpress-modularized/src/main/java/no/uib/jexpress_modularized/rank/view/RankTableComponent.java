/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.rank.view;

import java.io.Serializable;
import java.util.HashSet;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.core.model.SelectionManager;
import no.uib.jexpress_modularized.core.model.ModularizedListener;
import no.uib.jexpress_modularized.rank.computation.RPResult;

/**
 *
 * @author Yehia Farag
 */
public class RankTableComponent extends ModularizedListener implements Serializable{

    
    private RankTable  tab_result;
    private TableRowSorter sorter;

    public RankTable getTab_result() {
        return tab_result;
    }
    
    public RankTableComponent(RPResult rpmodel,Dataset dataset)
    {
        
        
        this.data = dataset;
        classtype =10;
        components.add(RankTableComponent.this);
        HashSet hs = null;
	 if (rpmodel.getRowData() != null) {
        
	        //hs = new HashSet();
	       // hs.addAll(rpmodel.getRowData());
	   }
        tab_result = new RankTable(rpmodel);
        sorter = new TableRowSorter<TableModel>(rpmodel);
        tab_result.setRowSorter(sorter);
        tab_result.setData(data,hs);
        
        
        tab_result.setVisible(true);        
        this.add(tab_result);
       
        
         if (SelectionManager.getSelectionManager().getSelectedRows(data) != null) //NOTE: should this method be used, or should this class implement the 
        {    
             selectionChanged(Selection.TYPE.OF_ROWS);
        }
         SelectionManager.getSelectionManager().addSelectionChangeListener(data, RankTableComponent.this);        
     
         
         
      
    
    }
    
    
    
    
    
    @Override
    public void selectionChanged(no.uib.jexpress_modularized.core.model.Selection.TYPE type) {
        if(type == no.uib.jexpress_modularized.core.model.Selection.TYPE.OF_ROWS&&tab_result.isM_SelectionsUpdating()==false)
        {
             
             tab_result.setSelfSelection(true);
             tab_result.clearSelection();
             Selection sel = SelectionManager.getSelectionManager().getSelectedRows(data);
            if (sel != null) {
                int[] selectedRows = sel.getMembers();       
                
                if (selectedRows != null) {
                    tab_result.changeSelection(selectedRows); 
                }
            } 
            tab_result.setSelfSelection(false);
        
        }
     }
    public void createGroup()
    {
        tab_result.createGroup();
    
    }
    
}
