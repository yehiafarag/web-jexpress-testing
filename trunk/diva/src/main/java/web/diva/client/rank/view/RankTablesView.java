/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.rank.view;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import web.diva.client.core.model.SelectionManager;
import web.diva.shared.beans.RankResult;
//import web.jexpress.shared.model.core.model.SelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class RankTablesView extends HorizontalPanel {
    
    
    public RankTablesView(SelectionManager selectionManager,RankResult results)
    {

        this.setWidth(""+RootPanel.get("RankTablesResults").getOffsetWidth()+"px");
        this.setHeight("270px");
        //this.setAlign(Alignment.CENTER);
       
        SectionStack secStackI = new SectionStack();
        secStackI.setVisibilityMode(VisibilityMode.MULTIPLE);  
        secStackI.setWidth((RootPanel.get("RankTablesResults").getOffsetWidth()/2));  
        secStackI.setHeight(270);  
        
        RankTable posRankTable = new RankTable(selectionManager, results.getDatasetId(),results.getPosTableHeader(), results.getPosTableData(),results.getPosRankToIndex(),results.getPosIndexToRank());
        
        SectionStackSection section1 = new SectionStackSection("Positive Score");  
        section1.setExpanded(true);  
        section1.addItem(posRankTable.getTable());  
        secStackI.addSection(section1);  
        this.add(secStackI);
        
        
        SectionStack secStackII = new SectionStack();
        secStackII.setVisibilityMode(VisibilityMode.MULTIPLE);  
        secStackII.setWidth((RootPanel.get("RankTablesResults").getOffsetWidth()/2));  
        secStackII.setHeight(270);  
        RankTable negRankTable = new RankTable(selectionManager, results.getDatasetId(),results.getNegTableHeader(), results.getNegTableData(),results.getNegRankToIndex(),results.getNegIndexToRank());
        
        SectionStackSection section11 = new SectionStackSection("Negative Score");  
        section11.setExpanded(true);  
        section11.addItem(negRankTable.getTable());  
        secStackII.addSection(section11);  
        this.add(secStackII);
        results = null;
        
    
    }
    
    
    
}
