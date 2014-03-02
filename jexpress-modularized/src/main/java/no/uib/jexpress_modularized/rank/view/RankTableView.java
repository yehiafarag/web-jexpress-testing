/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.rank.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.Serializable;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.model.Selection;

/**
 *
 * @author Yehia Farag
 */
public class RankTableView extends javax.swing.JFrame implements Serializable{
    private RankTableComponent posTab_result, negTab_result;
    public RankTableView(RankTableComponent posTab_result,RankTableComponent negTab_result)
    {
     this.posTab_result = posTab_result;
     this.negTab_result= negTab_result;
      initComponents();
      JTabbedPane tab = new JTabbedPane();
       
           FlowLayout experimentLayout = new FlowLayout(FlowLayout.CENTER);
           JPanel positivePanel = new JPanel();
           positivePanel.setLayout(experimentLayout);
           JPanel panel2 = new JPanel();
           jScrollPane3.setViewportView(posTab_result.getTab_result()); //ScrollPane is already on form (put there by GUI editor)
           positivePanel.add(jScrollPane3, java.awt.BorderLayout.CENTER);
           positivePanel.add(panel2);
           panel2.add(bGroup);
           
           
           JPanel negativePanel = new JPanel();
           negativePanel.setLayout(experimentLayout);
           JPanel panel2n = new JPanel();
           jScrollPane3n.setViewportView(negTab_result.getTab_result()); //ScrollPane is already on form (put there by GUI editor)
            
           negativePanel.add(jScrollPane3n, java.awt.BorderLayout.CENTER);
           negativePanel.add(panel2n);
           panel2n.add(bGroupN);
           
           
           tab.add(positivePanel, "Positive Score", 0);
           tab.add(negativePanel, "Negative Score", 1);
           this.add(tab,java.awt.BorderLayout.CENTER);
    
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
  private void initComponents() {
   jScrollPane3  = new JScrollPane();          
   jScrollPane3.setVisible(true);
   jScrollPane3.setPreferredSize(new java.awt.Dimension(700, 500));
    jScrollPane3n  = new JScrollPane();          
   jScrollPane3n.setVisible(true);
   jScrollPane3n.setPreferredSize(new java.awt.Dimension(700, 500));
   bGroup = new JButton();
   bGroup.setText("Create group of selected");
   bGroup.setToolTipText("Create group of selected");
   bGroup.addActionListener(new java.awt.event.ActionListener() {
       @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGroupActionPerformed(evt);
            }
        });
   setVisible(true);
   
   bGroupN = new JButton();
   bGroupN.setText("Create group of selected");
   bGroupN.setToolTipText("Create group of selected");
   bGroupN.addActionListener(new java.awt.event.ActionListener() {
        @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGroupNActionPerformed(evt);
            }
        });
   setVisible(true);
   setResizable(false);
   setTitle("Differential Expression");
   setSize(new java.awt.Dimension(700, 600));          
    //pack();
  }// </editor-fold>                                         
     
     // Variables declaration - do not modify                     
   
    private javax.swing.ButtonGroup method;
    private javax.swing.ButtonGroup platform;
    private javax.swing.ButtonGroup values;
    // End of variables declaration                   

     // Variables declaration - do not modify  
    private javax.swing.JButton bGroup;
    private javax.swing.JButton bGroupN;
    private javax.swing.JScrollPane jScrollPane3;
     private javax.swing.JScrollPane jScrollPane3n;
    // End of variables declaration       
    
    private void bGroupActionPerformed(java.awt.event.ActionEvent evt) {                                       
       int[] sel = posTab_result.getTab_result().getSelectedToGroup();
       if(sel != null && sel.length != 0)
       {
            Group g1 = new Group("Created Group "+posTab_result.getDataSet().getRowGroups().size()+1, generatRandColor(), new Selection(Selection.TYPE.OF_ROWS, sel));
            g1.setActive(true);
            posTab_result.getDataSet().addRowGroup(g1);
       
       }
        
    } 
     private void bGroupNActionPerformed(java.awt.event.ActionEvent evt) {                                       
       int[] sel = negTab_result.getTab_result().getSelectedToGroup();
       if(sel != null && sel.length != 0)
       {
            Group g1 = new Group("Created Group "+negTab_result.getDataSet().getRowGroups().size()+1, generatRandColor(), new Selection(Selection.TYPE.OF_ROWS, sel));
            g1.setActive(true);
            negTab_result.getDataSet().addRowGroup(g1);
            
       
       }
        
    }   
     private Random rand = new Random();
     private Color generatRandColor()
     {
        
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;
     }
}
