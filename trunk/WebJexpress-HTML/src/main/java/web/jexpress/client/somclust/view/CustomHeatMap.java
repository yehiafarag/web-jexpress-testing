/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.somclust.view;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import web.jexpress.shared.beans.SomClusteringResults;
import web.jexpress.shared.model.core.model.SelectionManager;

/**
 *
 * @author Y.M
 */
public class CustomHeatMap {
    
    private String[] geneNames;
    private String[] colID;
    private VLayout layout;
    private SomClusteringResults results;
    private SelectionManager selectionManager;
    public CustomHeatMap (SomClusteringResults results, SelectionManager selectionManager)
    {
         geneNames = results.getGeneNames();
         colID = results.getColsNames();
         this.results=results;
         
    
        creatVisulization();
    
    }
    
    private void creatVisulization()
    {
        layout = new VLayout();  
        layout.setWidth(500);  
        layout.setHeight(500);  
       // layout.setMembersMargin(0);  
        
        HLayout colHeader = new HLayout();
        colHeader.setHeight("2px");
        
        for(int x=0;x<colID.length;x++)
        {
            Cell l = new Cell(colID[x]);
//           l.setAlign(Alignment.CENTER);  
//           l.setBorder("1px solid #808080");           
//           // l.setContents("<font style='font-size:1px'>"+colID[x]+"</font>"); 
//            l.setHeight("2px");
//            l.setWidth("5px");
            colHeader.addMember(l);
        }
         Cell l = new Cell(" ");
//           l.setAlign(Alignment.CENTER);     
//            l.setContents("");  
            colHeader.addMember(l);
        layout.addMember(colHeader);
        
        
        for(int x=0;x<geneNames.length;x++)
        {
            HLayout rowLayout = new HLayout();
            Cell geneName = new Cell(geneNames[x]);
//              geneName.setAlign(Alignment.CENTER);  
//           geneName.setBorder("1px solid #808080"); 
//           // geneName.setContents("<font style='font-size:1px'>"+geneNames[x]+"</font>");
//            geneName.setHeight("2px");
//            geneName.setWidth("5px");
            
            
            for(int y=0;y<colID.length;y++)
        {
            Cell cell = new Cell(" ");
//           cell.setAlign(Alignment.CENTER);  
//           cell.setBorder("1px solid #808080");  
           cell.setBackgroundColor("#C3D9FF"); 
//////           cell.setHeight("2px");
//////           cell.setWidth("5px");
            rowLayout.addMember(cell);
        }
            rowLayout.addMember(geneName);
              layout.addMember(rowLayout);
        
        }
        
        
        
        
        
    
    }

    public VLayout getLayout() {
        return layout;
    }

    public void setLayout(VLayout layout) {
        this.layout = layout;
    }
    
    class Cell extends Label{
        Cell(String str)
        {
            setBorder("1px solid #808080");
            setHeight("2px");
            setWidth("5px");
            setAlign(Alignment.CENTER);
            setContents("<font style='font-size:1px'>"+str+"</font>");
            
        }
    
    }
    
}
