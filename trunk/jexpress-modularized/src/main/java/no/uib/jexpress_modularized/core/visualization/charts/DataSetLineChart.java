/*
 * SelectedLineChart.java
 *
 * Created on 3. august 2005, 10:39
 */
package no.uib.jexpress_modularized.core.visualization.charts;

import java.awt.Color;
import java.io.Serializable;
import no.uib.jexpress_modularized.core.dataset.Dataset;
//import org.freehep.util.export.ExportDialog; 
//import 

/**
 *
 * @author bjarte dysvik
 */
public class DataSetLineChart extends javax.swing.JInternalFrame implements Serializable{

    private Dataset data;
    //private dataListener listener; // @TODO: reimplement me!
    private boolean showLegend;
    private Color[] colors;

    /**
     * Creates new form SelectedLineChart
     */
    public DataSetLineChart() {
        initComponents();

    }

    public void setData(Dataset data, boolean showLegend, Color[] colors) {
        this.showLegend = showLegend;
        this.colors = colors;
        //if(data!=null) data.removeDataListener(listener); // @TODO: reimplement me!
        this.data = data;
        addListener();
        setupPlot();
    }

    public void addListener() {
        // @TODO: reimplement me!
//         if(listener==null){
//            
//         listener =new dataListener(){
//            public void SelectionHasChanged(Object source) {
//                setupPlot();
//            }
//            
//            public void dataHasChanged(){}
//            
//            public void columnsHasChanged(){}
//            
//            public void ColumnSelectionHasChanged(Object source) {
//                setupPlot();
//            }
//        };
//         }
//         data.addDataListener(listener);
    }

    public void setupPlot() {
        // @TODO: reimplement me!
//        int colid=0;
//        int rowid=0;
//        
//        String[][] rowID = data.getInfos();
//        String[][] colID = data.getColInfos();
//        
//      //  int[] sel = data.getSelectedRows();
//      //  int[] colsel = data.getSelectedColumns();
//        
//   //     if(colsel==null || colsel.length<1){
//   //         colsel=new int[data.getDataWidth()];
//   //         for(int i=0;i<colsel.length;i++)colsel[i]=i;
//   //     }
//        
//        //System.out.print("\nW:"+colsel.length);
//        
//        String[] colinf = new String[data.getDataWidth()];
//        String[] rowinf = new String[data.getDataLength()];
//        
//        for(int i=0;i<colinf.length;i++){
//            colinf[i]=colID[i][colid];
//        }
//        
//        double[][] dt = data.getData();
//        double[][] seldat = new double[data.getDataLength()][data.getDataWidth()];
//        
//        for(int i=0;i<seldat.length;i++){
//            for(int j=0;j<seldat[i].length;j++){
//                seldat[i][j]=dt[i][j];
//            }
//            rowinf[i]=rowID[i][rowid];
//        }
//        
//        chart.setLegendVisible(showLegend);
//        chart.setShapeVisible(showLegend);
//        chart.setGroupColors(colors);
//        chart.resetChart(colinf, rowinf,seldat);
//        chart.repaint();
//        
    }

    //expresscomponents.DataSet data = jexpress.cluster.loadDataSet("resources/mindre.cfg","Yeast Elu");//"alpha1-18_2");//"nci60_fig3_nomiss.txt","Baldwin_alltimecourses_nomiss.txt","nci60_fig3_nomiss.txt","Alizadeh_65col_nomiss.txt"
//    public static void main(String[] args) {
//
//        DataSetLineChart ch = new DataSetLineChart();
//
//        try {
//            javax.swing.UIManager.setLookAndFeel("expresscomponents.UI.JELookAndFeel");
//        } catch (Exception e) {
//        }
//
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
//
//        JDesktopPane de = new JDesktopPane();
//        f.getContentPane().add(de);
//
//        f.setResizable(true);
//        //expresscomponents.DataSet data = jexpress.cluster.loadDataSet("C:/data/ny publikasjon/ALLAML2.pro","Subset of data_set_ALL_AML_train3.txt");//"alpha1-18_2");//"nci60_fig3_nomiss.txt","Baldwin_alltimecourses_nomiss.txt","nci60_fig3_nomiss.txt","Alizadeh_65col_nomiss.txt"
//        //expresscomponents.DataSet data = jexpress.cluster.loadDataSet("C:/data/ny publikasjon/ALLAML2.pro","Mean and variance normalizedSubset of data_set_ALL_AML_train3.txt");//"alpha1-18_2");//"nci60_fig3_nomiss.txt","Baldwin_alltimecourses_nomiss.txt","nci60_fig3_nomiss.txt","Alizadeh_65col_nomiss.txt"
//        DataSet data = jexpress.cluster.loadDataSet("resources/mindre.cfg", "Yeast Elu");//"alpha1-18_2");//"nci60_fig3_nomiss.txt","Baldwin_alltimecourses_nomiss.txt","nci60_fig3_nomiss.txt","Alizadeh_65col_nomiss.txt"
//
//        data.setSelectedRows(new int[]{1, 2, 3, 4, 5});
//        data.setSelectedColumns(new int[]{3, 4, 5});
//
//        ch.setData(data, false, null);
//
//        ch.setSize(300, 200);
//        ch.pack();
//        ch.show();
//
//        de.add(ch);
//
//
//        f.setSize(400, 200);
//        f.show();
//    }

    public void saveImage() {
        // @TODO: reimplement me!
//        //Component owner = DensScatterPlot.this.getRootPane().getTopLevelAncestor();
//        //LockFullRepaint=true;
//        ExportDialog export = new ExportDialog();
//        export.showExportDialog(this, "Save Chart as ...", chart, "chart");
//        //LockFullRepaint=false;

    }

    public void printImage() {
        // @TODO: reimplement me!
//        expresscomponents.Print.PrintPreview2 pw = new expresscomponents.Print.PrintPreview2((Frame) this.getTopLevelAncestor(), true);
//        pw.setComponent(chart);
//        pw.show();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jEPanel1 = new no.uib.jexpress_modularized.core.visualization.JEPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        chart = new no.uib.jexpress_modularized.core.visualization.charts.jfreeLineChart();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Selected Profiles");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jEPanel1.setLayout(new java.awt.BorderLayout());

        jEPanel1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(4, 4, 4, 4)));
        jEPanel1.setLColor(new java.awt.Color(220, 220, 255));
        jEPanel1.setPaintText(false);
        jEPanel1.setPercentStartGradient(0.0);
        jEPanel1.setPreferredSize(new java.awt.Dimension(450, 350));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 120, 120)));
        jPanel2.add(chart, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setOpaque(false);
        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel4.setOpaque(false);
        jPanel1.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jEPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jEPanel1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Chart");
        jMenuItem1.setText("Save Chart");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Print Chart");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        printImage();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        saveImage();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        //data.removeDataListener(listener); // @TODO: reimplement me!
    }//GEN-LAST:event_formInternalFrameClosing
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private no.uib.jexpress_modularized.core.visualization.charts.jfreeLineChart chart;
    private no.uib.jexpress_modularized.core.visualization.JEPanel jEPanel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
}
