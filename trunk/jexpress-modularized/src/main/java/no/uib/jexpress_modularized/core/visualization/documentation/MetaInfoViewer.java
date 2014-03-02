/*

 * MetaInfoViewer.java

 *

 * Created on 30. juli 2003, 16:00

 */
package no.uib.jexpress_modularized.core.visualization.documentation;

import javax.swing.table.*;


import javax.swing.tree.*;

import java.awt.*;
import java.io.Serializable;


/**
 *
 *
 *
 * @author bjarte dysvik
 *
 */
public class MetaInfoViewer extends javax.swing.JPanel implements Serializable{

    MetaInfoList metalist = new MetaInfoList();
    MetaInfoNode selected;

    /**
     * Creates new form MetaInfoViewer
     */
    public MetaInfoViewer() {

        initComponents();

        //   jList1.setModel(new DefaultListModel());

        //   jList1.setCellRenderer(new metaListRenderer());



        tree.setCellRenderer(new MetaTreeRenderer());

        //((javax.swing.DefaultListCellRenderer)jList1.getCellRenderer()).setIcon(new javax.swing.ImageIcon(getClass().getResource("/expresscomponents/Visuals/im/execution.gif")));

    }

    public void setMetaInfoList(MetaInfoList metalist) {

        //this.metalist=metalist;

        //((DefaultListModel)jList1.getModel()).removeAllElements();



        //int count = jTable1.getRowCount();

        //for(int i=0;i<count;i++) ((DefaultTableModel)jTable1.getModel()).removeRow(0);









        // if(metalist!=null) for(int i=0;i<metalist.size();i++) ((DefaultListModel)jList1.getModel()).addElement(metalist.elementAt(i));





        MetaInfoNode root = new MetaInfoNode(MetaInfoNode.root);

        if (metalist != null) {
            for (int i = 0; i < metalist.size(); i++) {
                root.addChild((MetaInfoNode) metalist.elementAt(i));
            }
        }



        //metalist.removeAllElements();



        DefaultTreeModel mod = new DefaultTreeModel(root);

        tree.setModel(mod);





    }

    public void setMetaInfoNodes(MetaInfoNode[] nodes) {

        //this.metalist=metalist;

        //((DefaultListModel)jList1.getModel()).removeAllElements();



        //int count = jTable1.getRowCount();

        //for(int i=0;i<count;i++) ((DefaultTableModel)jTable1.getModel()).removeRow(0);









        // if(metalist!=null) for(int i=0;i<metalist.size();i++) ((DefaultListModel)jList1.getModel()).addElement(metalist.elementAt(i));





        MetaInfoNode root = new MetaInfoNode();

        for (int i = 0; i < nodes.length; i++) {
            root.addChild(nodes[i]);
        }



        DefaultTreeModel mod = new DefaultTreeModel(root);

        tree.setModel(mod);





    }

    public void setTableElement(MetaInfoNode node) {

        //for(int i=0;i<node.size();i++){

        if (selected == node || node == null) {
            return;
        } else {
            selected = node;
        }



        java.util.Iterator it = node.keySet().iterator();



        /*
         *
         * //Object k = null;
         *
         * String k ="";
         *
         * String v ="";
         *
         * while(it.hasNext()){
         *
         * k=(String)it.next();
         *
         * v =(String) get(k);
         *
         * copy.put(k,v); * }
         *
         */

        //java.util.Enumeration e = node.keys();

        Object tmp = null;



        int count = jTable1.getRowCount();

        for (int i = 0; i < count; i++) {
            ((DefaultTableModel) jTable1.getModel()).removeRow(0);
        }





        while (it.hasNext()) {

            tmp = it.next();

            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[]{tmp, node.get(tmp)});

        }



    }

    public static void main(String[] args) {



        javax.swing.JFrame f = new javax.swing.JFrame("test");

        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);



        MetaInfoViewer mi = new MetaInfoViewer();





        MetaInfoList l = new MetaInfoList();



        MetaInfoNode node1 = MetaInfoNode.createNode(MetaInfoNode.hclust, "TREE P INDEX1", "10,30");

        MetaInfoNode node2 = MetaInfoNode.createNode(MetaInfoNode.hclust, "TREE P INDEX1", "10,30");

        MetaInfoNode node3 = MetaInfoNode.createNode(MetaInfoNode.hclust, "TREE P INDEX1", "10,30");



        MetaInfoNode node4 = MetaInfoNode.createNode(MetaInfoNode.rawarr, "TREE P INDEX1", "10,30");

        MetaInfoNode node5 = MetaInfoNode.createNode(MetaInfoNode.kmclust, "TREE P INDEX1", "10,30");

        MetaInfoNode node6 = MetaInfoNode.createNode(MetaInfoNode.kmclust, "TREE P INDEX1", "10,30");



        node1.addChild(node2);

        node2.addChild(node3);

        l.addElement(node1);



        node4.addChild(node5);

        node4.addChild(node6);



        l.addElement(node4);









        // l.addElement(MetaInfoNode.createNode(MetaInfoNode.hclust,"TREE PAA INDEX","10,30"));

        // l.addElement(MetaInfoNode.createNode(MetaInfoNode.kmclust,"TREE PAA INDEX","10,30","PARAMETER 2","PAR 2 VALUE"));

        // l.addElement(MetaInfoNode.createNode(MetaInfoNode.pca,"TREE PAA INDEX","10,30","PARAMETER 2","PAR 2 VALUE"));

        mi.setMetaInfoList(l);





        //mi.setMetaInfoNodes(new MetaInfoNode[]{node1,node4});





        f.getContentPane().add(mi);

        f.pack();

        f.setVisible(true);
    }

    /**
     * This method is called from within the constructor to
     *
     * initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is
     *
     * always regenerated by the Form Editor.
     *
     */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jSplitPane1 = new javax.swing.JSplitPane();
    jPanel2 = new javax.swing.JPanel();
    jPanel4 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();
    jPanel6 = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    jPanel1 = new javax.swing.JPanel();
    jPanel5 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jPanel7 = new javax.swing.JPanel();
    jScrollPane3 = new javax.swing.JScrollPane();
    tree = new javax.swing.JTree();

    setLayout(new java.awt.BorderLayout());

    jSplitPane1.setDividerLocation(200);
    jSplitPane1.setOneTouchExpandable(true);

    jPanel2.setLayout(new java.awt.BorderLayout());

    jPanel4.setLayout(new java.awt.BorderLayout());

    jTable1.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null},
        {null, null}
      },
      new String [] {
        "Parameter", "Value"
      }
    ) {
      Class[] types = new Class [] {
        java.lang.String.class, java.lang.String.class
      };
      boolean[] canEdit = new boolean [] {
        false, false
      };

      public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    jScrollPane1.setViewportView(jTable1);

    jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

    jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

    jLabel2.setText("Process Parameters");
    jPanel6.add(jLabel2);

    jPanel4.add(jPanel6, java.awt.BorderLayout.NORTH);

    jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

    jSplitPane1.setRightComponent(jPanel2);

    jPanel1.setLayout(new java.awt.BorderLayout());

    jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

    jLabel1.setText("Process List");
    jPanel5.add(jLabel1);

    jPanel1.add(jPanel5, java.awt.BorderLayout.NORTH);

    jPanel7.setLayout(new java.awt.BorderLayout());

    tree.setRowHeight(22);
    tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        treeValueChanged(evt);
      }
    });
    jScrollPane3.setViewportView(tree);

    jPanel7.add(jScrollPane3, java.awt.BorderLayout.CENTER);

    jPanel1.add(jPanel7, java.awt.BorderLayout.CENTER);

    jSplitPane1.setLeftComponent(jPanel1);

    add(jSplitPane1, java.awt.BorderLayout.CENTER);
  }// </editor-fold>//GEN-END:initComponents

    private void treeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeValueChanged



        if (evt.getNewLeadSelectionPath() == null) {
            return;
        }

        TreeNode node = (TreeNode) evt.getNewLeadSelectionPath().getLastPathComponent();

        //TreeNode node = ((TreeNode)((DefaultTreeModel)tree.getModel()).getRoot()).g

        setTableElement((MetaInfoNode) node);







    }//GEN-LAST:event_treeValueChanged
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JPanel jPanel6;
  private javax.swing.JPanel jPanel7;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JTable jTable1;
  private javax.swing.JTree tree;
  // End of variables declaration//GEN-END:variables
}
class metaListRenderer extends javax.swing.DefaultListCellRenderer {

    public metaListRenderer() {
    }

    public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {



        if (value instanceof MetaInfoNode) {

            setBackground(Color.white);

            if (isSelected) {
                setBackground(new Color(220, 220, 250));
            }





            setText(value.toString());

            setIcon(((MetaInfoNode) value).getIcon());





        }



        return this;

    }
}
//class MetaTreeRenderer extends DefaultTreeCellRenderer{
//
//    
//
//    public MetaTreeRenderer(){
//
//        setOpaque(true);
//
//    }
//
//    
//
//    //public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//
//    public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){      
//
//     if(value instanceof MetaInfoNode) {
//
//         
//
//        setBackground(Color.white);
//
//        if(sel) setBackground(new Color(220,220,250));
//
//        
//
//        
//
//         setText(value.toString());
//
//         //setIcon( ((MetaInfoNode)value).getIcon());
//
//         setLeafIcon(((MetaInfoNode)value).getIcon()); 
//
//         setOpenIcon(((MetaInfoNode)value).getIcon());
//
//         setClosedIcon(((MetaInfoNode)value).getIcon());
//
//         
//
//         setIcon(((MetaInfoNode)value).getIcon());
//
//         
//
//     }
//
//        
//
//       return this; 
//
//    }
//    
//
//    
//
//}

