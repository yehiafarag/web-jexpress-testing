/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.rank.view;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import web.jexpress.shared.beans.RankResult;
import web.jexpress.shared.model.core.model.SelectionManager;

/**
 *
 * @author Y.M
 */
public class RankTablesView extends HorizontalPanel {
    
    
    public RankTablesView(SelectionManager selectionManager,RankResult results)
    {

        this.setWidth("100%");
        this.setHeight("300px");
        //this.setAlign(Alignment.CENTER);
       
        SectionStack secStackI = new SectionStack();
        secStackI.setVisibilityMode(VisibilityMode.MULTIPLE);  
        secStackI.setWidth(400);  
        secStackI.setHeight(200);  
        RankTable posRankTable = new RankTable(selectionManager, results.getDatasetId(),results.getPosTableHeader(), results.getPosTableData(),results.getPosRankToIndex(),results.getPosIndexToRank());
        
        SectionStackSection section1 = new SectionStackSection("Positive Score");  
        section1.setExpanded(true);  
        section1.addItem(posRankTable.getTable());  
        secStackI.addSection(section1);  
        this.add(secStackI);
        
        
        SectionStack secStackII = new SectionStack();
        secStackII.setVisibilityMode(VisibilityMode.MULTIPLE);  
        secStackII.setWidth(400);  
        secStackII.setHeight(200);  
        RankTable negRankTable = new RankTable(selectionManager, results.getDatasetId(),results.getNegTableHeader(), results.getNegTableData(),results.getNegRankToIndex(),results.getNegIndexToRank());
        
        SectionStackSection section11 = new SectionStackSection("Negative Score");  
        section11.setExpanded(true);  
        section11.addItem(negRankTable.getTable());  
        secStackII.addSection(section11);  
        this.add(secStackII);
        
    
    }
    
    
    
}
