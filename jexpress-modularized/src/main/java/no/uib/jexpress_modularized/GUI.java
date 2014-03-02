/*
 *this is simple GUI to simulate the j-express user interface 
 */
package no.uib.jexpress_modularized;

import utility.FilesReader;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.visualization.ProgressDialog;
import no.uib.jexpress_modularized.core.visualization.ProgressDialogParent;
import no.uib.jexpress_modularized.linechart.model.LineChartUtil;
import no.uib.jexpress_modularized.linechart.visualization.LineChartGraphComponent;
import no.uib.jexpress_modularized.linechart.visualization.LineChartView;
import no.uib.jexpress_modularized.linechart.visualization.TableComponent;
import no.uib.jexpress_modularized.pca.computation.PcaCompute;
import no.uib.jexpress_modularized.pca.computation.PcaResults;
import no.uib.jexpress_modularized.pca.visualization.PCA2DComponent;
import no.uib.jexpress_modularized.pca.visualization.PcaView;
import no.uib.jexpress_modularized.rank.computation.ComputeRank;
import no.uib.jexpress_modularized.rank.computation.RPResult;
import no.uib.jexpress_modularized.rank.view.RankTableComponent;
import no.uib.jexpress_modularized.rank.view.RankTableView;
import no.uib.jexpress_modularized.somclust.computation.SOMClustCompute;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters;
import no.uib.jexpress_modularized.somclust.model.ClusterResults;
import no.uib.jexpress_modularized.somclust.visualization.MainClustComponent;
import no.uib.jexpress_modularized.somclust.visualization.SomclustView;


/**
 *
 * @author Yehia Farag
 * 
 * 
 */
public class GUI extends javax.swing.JFrame implements ProgressDialogParent,Serializable {

    
    
    private Dataset dataset;
    private ClusterParameters parameter;
    private ClusterResults clusterResults ;
    private PcaResults pcaResults;
    private Random ran = new java.util.Random();
    private final ProgressDialog progressDialog = new ProgressDialog(this, true);
    private DefaultListModel listModel = new DefaultListModel();
    private JList list;
    private  boolean[]members;
    private boolean activePca=false;
    private boolean activeHc= false;
    private boolean activeRank=false;
    private boolean activeLC = false;
    
    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        this.setResizable(false);

        jButton6.setText("Test All");
        jButton6.setEnabled(false);
        jButton6.setToolTipText("YOU NEED TO RUN ALL MODULES FIRST TO ACTIVET THE BUTTON");
        jButton9.setEnabled(false);
        /////////////////////////////////
        jButton1.setText("Load Data");
        jButton7.setText("Browse");
        jLabel7.setForeground(Color.red);
        jLabel7.setText("* Error Please Chack the Data File");
        jLabel7.setVisible(false);
        ////////////////////////////////

        jTextField1.setText("C:\\Users\\Y.M\\Desktop\\diauxic shift.txt");//("Please Choose Dataset File (.txt)");
        jLabel3.setText("Load Dataset File:");
        jLabel3.setForeground(Color.BLUE);

        //init Hierarchical Clustering part
        jLabel4.setText("Hierarchical Clustering:");
        jLabel4.setForeground(Color.BLUE);
        jComboBox1.removeAllItems();
        jButton2.setText("Start Clustering");
        jComboBox1.addItem("Squared Euclidean");
        jComboBox1.addItem("Euclidean");
        //combo.addItem("Euclidean (Nullweighted)");
        //combo.addItem("Canberra");
        //combo.addItem("Squared Chord");
        //combo.addItem("Squared Chi-Squared");
        jComboBox1.addItem("Bray Curtis");
        jComboBox1.addItem("Manhattan");
        jComboBox1.addItem("Cosine Correlation");
        jComboBox1.addItem("Pearson Correlation");
        jComboBox1.addItem("Uncentered Pearson Correlation");
        jComboBox1.addItem("Euclidean (Nullweighted)");
        jComboBox1.addItem("Camberra");
        jComboBox1.addItem("Chebychev");
        jComboBox1.addItem("Spearman Rank Correlation");
        jComboBox1.setSelectedIndex(0);

        jComboBox2.removeAllItems();
        jComboBox2.addItem("SINGLE");
        jComboBox2.addItem("WPGMA");
        jComboBox2.addItem("UPGMA");
        jComboBox2.addItem("COMPLETE");
        jComboBox2.setSelectedIndex(1);
        jCheckBox1.setText("Cluster Columns");
        //end of HC init
        //line chart
        jLabel5.setText("Line Chart:");
        jLabel5.setForeground(Color.BLUE);
        jButton3.setText("Start Process");
        //end Line Chart
        //start PCA
        jLabel6.setText("PCA:");
        jLabel6.setForeground(Color.BLUE);
        jButton4.setText("PCA");
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
        //Rank Product
        jLabel8.setText("Rank Product (UnPaired) ");
        jPanel1.setName("Permutation");
        jLabel13.setText("Permutation");
        jPanel1.setBorder(jTextField1.getBorder());
        jLabel11.setText("Permutations");
        jLabel12.setText("Seed");
        jTextField2.setText(400 + "");
        jTextField3.setText(String.valueOf((int) ran.nextInt(1000000001)));

        jButton8.setText("create new seed");
//        jPanel2.setName("Values");
        jLabel14.setText("Values");
        jComboBox3.removeAllItems();
        jComboBox3.addItem("Log 2");
        jComboBox3.addItem("Linear");
        jComboBox3.setSelectedIndex(0);
//        jPanel2.setBorder(jTextField1.getBorder());
        jLabel8.setForeground(Color.BLUE);
        jLabel9.setText("Select Groups ");
        jLabel10.setText("* Maximum Selection Number is 2");
        jLabel10.setForeground(Color.RED);
        jLabel10.setVisible(false);
        jButton5.setText("Process");
//        jButton6.setText("OneClass");
//       jPanel2.setName("Values");

        jButton5.setEnabled(false);
//        jButton6.setEnabled(false);
        jComboBox1.setEnabled(false);
        jComboBox2.setEnabled(false);

        //        jTextField1.setText("");
        //        jTextField1.setToolTipText("Please Enter Integer Value");
    }

    @Override
    public void cancelProgress() {
        progressDialog.dispose();
        System.exit(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jButton9 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("jButton5");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Distance Measure:");

        jLabel2.setText("Linkage ");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel4");

        jLabel5.setText("jLabel5");

        jLabel6.setText("jLabel6");

        jTextField1.setText("jTextField1");

        jButton7.setText("jButton7");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel7.setText("jLabel7");

        jLabel8.setText("jLabel8");

        jLabel9.setText("jLabel9");

        jLabel10.setText("jLabel10");

        jLabel11.setText("jLabel11");

        jLabel12.setText("jLabel12");

        jTextField2.setText("jTextField2");

        jButton8.setText("jButton8");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jTextField3.setText("jTextField3");

        jLabel13.setText("jLabel13");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel11))
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addComponent(jTextField3))
                .addGap(10, 10, 10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8))
        );

        jLabel14.setText("jLabel14");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jButton9.setText("Test Results");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton6.setText("jButton6");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                        .addGap(632, 632, 632))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(21, 21, 21)
                                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                                        .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(12, 12, 12)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(47, 47, 47)
                                        .addComponent(jCheckBox1))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel14)
                                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(10, 10, 10)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 20, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton3)
                                    .addComponent(jLabel5))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jButton4))
                                .addGap(50, 50, 50))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 28, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private FilesReader reader = new FilesReader();
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String path = jTextField1.getText();
         File file = new File(path);
        dataset = reader.readDataset(file);
        if (dataset == null) {
            jLabel7.setVisible(true);

        } else {
            jLabel7.setVisible(false);
            jButton1.setEnabled(false);
            jButton7.setEnabled(false);
            jTextField1.setText("Please Choose Dataset File (.txt)");
            jButton2.setEnabled(true);
            jButton3.setEnabled(true);
            jButton4.setEnabled(true);
            jButton5.setEnabled(true);
            jButton9.setEnabled(true);
            jComboBox1.setEnabled(true);
            jComboBox2.setEnabled(true);
            list = new JList(listModel);
            list.setCellRenderer(new SelectedListCellRenderer());
            for (int x = 0; x < dataset.getColumnGroups().size(); x++) {
                Group g = dataset.getColumnGroups().get(x);
                listModel.addElement(g.getName());

            }

            jScrollPane1.setViewportView(list);

        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private int distance = -100;
    /*
     * this function are responsable for computing and visulaize 
     * the SOMClustering module
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        distance = -100;
        distance = jComboBox1.getSelectedIndex();
        progressDialog.setTitle("Loading Clustering. Please Wait...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setVisible(true);
            }
        }, "ProgressDialog").start();
        new Thread("DisplayThread") {
            @Override
            public void run() {
                try {
                    progressDialog.setIndeterminate(false);
                    progressDialog.setMax(100);
                    progressDialog.setTitle("Computing Clustering. Please Wait...");
                    parameter = new ClusterParameters();
                    parameter.setClusterSamples(true);
                    if (distance != -100) {
                        parameter.setDistance(distance);
                    }
                    if (jComboBox2.getSelectedIndex() == 0) {
                        parameter.setLink(ClusterParameters.LINKAGE.SINGLE);
                    } else if (jComboBox2.getSelectedIndex() == 1) {
                        parameter.setLink(ClusterParameters.LINKAGE.WPGMA);
                    } else if (jComboBox2.getSelectedIndex() == 2) {
                        parameter.setLink(ClusterParameters.LINKAGE.UPGMA);
                    } else if (jComboBox2.getSelectedIndex() == 3) {
                        parameter.setLink(ClusterParameters.LINKAGE.COMPLETE);
                    }
                    parameter.setClusterSamples(jCheckBox1.isSelected());

                    SOMClustCompute som = new SOMClustCompute(dataset, parameter);
                    SWorkerThread t = new SWorkerThread(som);
                    t.execute();
                    while (!t.isDone()) {
                        progressDialog.setValue(som.getProgress());
                        Thread.sleep(50);
                    }

                    clusterResults = t.get();
                    progressDialog.setIndeterminate(true);
                    progressDialog.setTitle("Displaying Clustering. Please Wait...");
                    SomclustView clustView = new SomclustView(dataset, parameter, clusterResults);
                    progressDialog.setVisible(false);
                    progressDialog.dispose();

                    setLocationRelativeTo(null);
                    setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
        }.start();

        activeHc =true;
        if(activeHc&&activePca&&activeRank&&activeLC)
            jButton6.setEnabled(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    /*
     * this function are responsable for computing and visulaize 
     * the Line Chart module
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if (dataset == null) {
        } else {
            LineChartUtil lineChartUtil = new LineChartUtil();
    
           this.members =  lineChartUtil.createAllMembers(dataset);
           LineChartView viewLineChart = new LineChartView(dataset,members);
            activeLC =true;
        if(activeHc&&activePca&&activeRank&&activeLC)
            jButton6.setEnabled(true);

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /*
     * this function are responsable for computing and visulaize 
     * the PCA module
     */
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        PcaCompute pcaCompute = new PcaCompute(dataset);
        pcaResults = pcaCompute.createPCA();      
        PcaView pcaperview = new PcaView(pcaResults,dataset);
        activePca =true;
         if(activeHc&&activePca&&activeRank&&activeLC)
            jButton6.setEnabled(true);

    }//GEN-LAST:event_jButton4ActionPerformed

    private ArrayList<RPResult> rpmodList;
    private int[] col1;
    private int[] col2;
    private int perm;
    private int seed;
    private boolean log2;
    /*
     * this function are responsable for computing and visulaize 
     * the Ranking product module
     */
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        col1 = null;
         col2 = null;
        String permStr = jTextField2.getText();
        String seedStr = jTextField3.getText();
        if (jComboBox3.getSelectedItem().toString().equalsIgnoreCase("Log 2")) {
            log2 = true;
        } else {
            log2 = false;
        }
        try {
            perm = Integer.valueOf(permStr);
        } catch (NumberFormatException exp) {
            jLabel10.setText("You Need To Enter Valid Permutations Integer Number");
            jLabel10.setVisible(true);
            return;
        }
        try {
            seed = Integer.valueOf(seedStr);
        } catch (NumberFormatException exp) {
            jLabel10.setText("You Need To Enter Valid Seed Integer Number");
            jLabel10.setVisible(true);
            return;
        }
        if (list.getSelectedValuesList().isEmpty() || list.getSelectedValuesList().size() > 2) {

            jLabel10.setText("You Need To Select (1 to 2) Groups");
            jLabel10.setVisible(true);
        } else {
            ArrayList<Group> groupList = new ArrayList<Group>();
            for (int z = 0; z < list.getSelectedValuesList().size(); z++) {
                String str = (String) list.getSelectedValuesList().get(z);
                for (Group g : dataset.getColumnGroups()) {
                    if (str.equalsIgnoreCase(g.getName())) {
                        groupList.add(g);
                        break;
                    }

                }
            }
           if (groupList.size() == 1) {
                col1 = groupList.get(0).getMembers();
                col2 = null;
            } else {
                col1 = groupList.get(0).getMembers();
                col2 = groupList.get(1).getMembers();
            }

            progressDialog.setTitle("Loading Product Ranking. Please Wait...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.setVisible(true);
                }
            }, "ProgressDialog").start();
            final ComputeRank cr = new ComputeRank(dataset);
            Thread t = new Thread() {

                public void run() {
                    try{
                        progressDialog.setIndeterminate(false);
                        progressDialog.setMax(100);
                       String type ="";
                        progressDialog.setTitle("Computing Product Ranking. Please Wait...");
                        if(col2!=null)
                           type= "TwoClassUnPaired";// rpmod = cr.createResult("TwoClassUnPaired", perm, seed, col1, col2, log2);
                        else
                             type= "OneClass";//rpmod = cr.createResult("OneClass", perm, seed, col1, col2, log2);

                        RWorkerThread r = new RWorkerThread(cr, type, perm, seed, col1, col2, log2);
                        r.execute();
                        while (!r.isDone()) {   
                            progressDialog.incrementValue();
                            Thread.sleep(50);
                        }

                        rpmodList = r.get();
                         progressDialog.setVisible(false);
            progressDialog.dispose();
            
            RankTableComponent prt = new RankTableComponent(rpmodList.get(0), dataset);
             RankTableComponent nrt = new RankTableComponent(rpmodList.get(1), dataset);
           
            RankTableView vrt = new RankTableView(prt,nrt);
            vrt.setLocationRelativeTo(GUI.this);
                    }catch(Exception e){e.printStackTrace();}
                }

            };

            t.start();

           activeRank =true;
         if(activeHc&&activePca&&activeRank&&activeLC)
            jButton6.setEnabled(true);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /*
     * this function are responsable for importing the dataset file and init the Dataset Object
     */
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        JFileChooser chooser = new JFileChooser();
        FileFilter ff = new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".txt");
            }

            public String getDescription() {
                return "*.txt";
            }
        };

        chooser.addChoosableFileFilter(ff);
        int option = chooser.showOpenDialog(this); // parentComponent must a component like JFrame, JDialog...
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            jTextField1.setText(path);
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    /*
     * this function are responsible for generating 
     * the seed value which required for Rank Product Computing
     */
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jTextField3.setText(String.valueOf((int) ran.nextInt(1000000001)));
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

     /*
     * this function are responsible for full testing panel
     * The main idea of this function is to provide an easy example to show how to reuse
     * view charts and tables from its container panels and give clear view for the interaction
     * between the different modules.
     */
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        
         JPanel panel  = new JPanel();
         panel.setLayout(new GridLayout(1,2));
         
         JScrollPane jScrollPane1  = new JScrollPane();
         JScrollPane jScrollPane2  = new JScrollPane();
         JScrollPane jScrollPane3  = new JScrollPane();
        
        
        
        TableComponent  infoTable =new TableComponent(dataset);        
        infoTable.setVisible(true);
        jScrollPane1.setViewportView(infoTable.getTable());
        
        LineChartGraphComponent graph = new LineChartGraphComponent(dataset, members);
        graph.getGraph().setVisible(true);
        graph.getGraph().setPropertiesAndScrollPane(jScrollPane2);
         
        
       
        PCA2DComponent pca = new PCA2DComponent(pcaResults,dataset);
        pca.getPlot().setSize(200,200);
        jScrollPane3.setViewportView(pca.getPlot());
        
         
        panel.add(jScrollPane1);
        panel.add(graph.getGraph());
        panel.add(jScrollPane3);
        
        
        
        JPanel panel2  = new JPanel();
        panel2.setLayout(new GridLayout(1,2));
        JScrollPane jScrollPane4  = new JScrollPane();
        JScrollPane jScrollPane5  = new JScrollPane();
        JScrollPane jScrollPane6  = new JScrollPane();
        

        MainClustComponent clust = new MainClustComponent(dataset, parameter, clusterResults);
        clust.setVisible(true);
        clust.setSize(100,100);
        jScrollPane4.setViewportView(clust);
        
        RankTableComponent prt = new RankTableComponent(rpmodList.get(0), dataset);
        RankTableComponent nrt = new RankTableComponent(rpmodList.get(1), dataset);
        jScrollPane5.setViewportView(prt.getTab_result());
        jScrollPane6.setViewportView(nrt.getTab_result());
        
        panel2.add(jScrollPane4);
        panel2.add(jScrollPane5);
        panel2.add(jScrollPane6);
        
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setLeftComponent(panel);
        splitPane.setRightComponent(panel2);
      //  p3.add(panel2);
        
        
         JFrame f = new JFrame();
         f.add(splitPane);   
         f.setSize(2000,2000);
      
         f.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        WebTestingFrame wtf = new WebTestingFrame();
        wtf.setVisible(false);
        ClusterParameters parameter = new ClusterParameters();
        parameter.setDistance(0);
        parameter.setClusterSamples(true);
        parameter.setLink(ClusterParameters.LINKAGE.UPGMA);
          SOMClustCompute som = new SOMClustCompute(dataset, parameter);
                    SWorkerThread t = new SWorkerThread(som);
                    t.execute();
                    try{
                    while (!t.isDone()) {
                        
                        Thread.sleep(50);
                        } 
                    clusterResults = t.get();
                    }catch(Exception e){}

                   
        wtf.startView(clusterResults);
        wtf.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables

    class SelectedListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (isSelected) {
                for (int x = 0; x < dataset.getColumnGroups().size(); x++) {
                    Group g = dataset.getColumnGroups().get(x);
                    if (c.getName().equalsIgnoreCase(g.getName())) {
                        c.setBackground(g.getColor());
                    }
                }

            }
            return c;
        }
    }

}
class SWorkerThread extends SwingWorker<ClusterResults, Integer> {

    private SOMClustCompute som;

    public SWorkerThread(SOMClustCompute som) {
        this.som = som;
    }

    @Override
    protected ClusterResults doInBackground() throws Exception {
        return som.runClustering();
    }
}

class RWorkerThread extends SwingWorker< ArrayList<RPResult>, Integer> {

    private ComputeRank cr;
    private String type;
    private int permutations, seed;
    private int[] g1, g2;
    private boolean log2;

    public RWorkerThread(ComputeRank cr, String type, int permutations, int seed, int[] g1, int[] g2, boolean log2) {
        this.cr = cr;
        this.type = type;
        this.permutations = permutations;
        this.seed = seed;
        this.g1 = g1;
        this.g2 = g2;
        this.log2 = log2;
    }

    @Override
    protected ArrayList<RPResult> doInBackground() throws Exception {
        return cr.createResult(type, permutations, seed, g1, g2, log2);
    }

}
