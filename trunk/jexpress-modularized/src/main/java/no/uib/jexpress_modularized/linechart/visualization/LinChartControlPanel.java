/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.linechart.visualization;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Vector;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.core.visualization.SwingWorker;
import no.uib.jexpress_modularized.core.visualization.buttonFramer;
//import no.uib.jexpress_modularized.core.visualization.buttonFramer;

/**
 *
 * @author Yehia Mokhtar
 */
public class LinChartControlPanel extends javax.swing.JInternalFrame implements java.awt.event.ActionListener, Serializable{
    
    public buttonFramer fr = new buttonFramer();
    private int startlabel = -1;
    private int endlabel = -1;
    private double max = -1;
    private double min = -1;
    private boolean[] members;
    private String br = System.getProperty("line.separator");
    
        private  boolean frameVisible = true;
    private boolean externallist = false;
    private boolean listselected = false;    
    private boolean aalias = false;
     
    private boolean zoomed = false;
    private Vector Vmembers;
    
    // Variables declaration - do not modify                     
    public javax.swing.JPanel JPanel12;
    public javax.swing.JPanel JPanel8;
    public javax.swing.JPanel JPanel9;
    public javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JSplitPane JSplitPane4;
//    public javax.swing.JTabbedPane JTabbedPane1;
    public javax.swing.JButton b1;
//    public javax.swing.JButton b10;
    public javax.swing.JButton b2;
//    public javax.swing.JButton b21;
//    public javax.swing.JButton b22;
//    public javax.swing.JButton b23;
    public javax.swing.JButton b3;
//    public javax.swing.JButton b4;
//    public javax.swing.JButton b6;
    public javax.swing.JButton b7;
    public javax.swing.JButton b9;
    public javax.swing.JPanel buttonBar;
//    public javax.swing.JButton frame;
    public LineChartGraphComponent graphBean1;
    public TableComponent infoTable1;
    private no.uib.jexpress_modularized.core.visualization.JEPanel jEPanel1;
    public javax.swing.JLabel jLabel1211;
    public javax.swing.JLabel jLabel122;
//    public javax.swing.JMenu jMenu1;
//    public javax.swing.JMenu jMenu11;
//    private javax.swing.JMenu jMenu12;
//    private javax.swing.JMenu jMenu2;
//    public javax.swing.JMenuBar jMenuBar1;
//    private javax.swing.JMenuItem jMenuItem10;
//    private javax.swing.JMenuItem jMenuItem6;
//    private javax.swing.JMenuItem jMenuItem7;
//    private javax.swing.JMenuItem jMenuItem8;
//    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JSeparator jSeparator222211;
    public javax.swing.JSeparator jSeparator22222;
//    public javax.swing.JSeparator jSeparator222221;
//    public javax.swing.JMenuItem m1;
//    public javax.swing.JMenuItem m11;
//    public javax.swing.JMenuItem m12;
//    public javax.swing.JMenuItem m13;
//    public javax.swing.JMenuItem m14;
//    public javax.swing.JMenuItem m2;
//    public javax.swing.JMenuItem m3;
//    public javax.swing.JMenuItem m31;
//    public javax.swing.JMenuItem m4;
//    public javax.swing.JMenuItem m5;
//    public javax.swing.JMenuItem m6;
////    public javax.swing.JMenuItem m8;
//    public javax.swing.JMenuItem m91;
//    public javax.swing.JButton move;
    // End of variables declaration    
     

    public LinChartControlPanel(TableComponent table, LineChartGraphComponent graph) {
        this.graphBean1 = graph;
        this.infoTable1 = table;   
        initComponents();
        initListeners();  
        graphBean1.getGraph().setPropertiesAndScrollPane(this.jScrollPane1);
              
        this.setTitle("Gene Graph" + " - "   + graph.getDataSet().getName());	       
	        this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
	            @Override
                    public void internalFrameClosing(javax.swing.event.InternalFrameEvent we) {
	                disconnectData();
	            }
	        });
                
                if (true){//showframe) {


            int W = 0;
            //NOTE: added the if-statement around the next expression, because one of the calls sometimes
            //returns null, which causes a NullPointerException. This is likely because g (GraphView) 
            //doesn't have a parent Frame (see next comment)
            if (this.getDesktopPane() != null) {

                W =
                        //NOTE: seems like getDesktopPane() returns null : does Graphview need a desktopPane to function?
                        //from the javadoc of the class that returns null: 
                        //* Generally,
                        //* you add <code>JInternalFrame</code>s to a <code>JDesktopPane</code>. 
                        this.getDesktopPane().getWidth()
                        - (this.jScrollPane1.getLocationOnScreen().x - this.getDesktopPane().getLocationOnScreen().x - this.infoTable1.getWidth() + 35) - 5;
            }
            this.JPanel8.setPreferredSize(new java.awt.Dimension(Math.min(W, this.graphBean1.getGraph().getPreferredScrollableViewportSize().width + (this.infoTable1.getWidth() + 35)), this.graphBean1.getGraph().getPreferredScrollableViewportSize().height + 10));

            // pack();
            //g.JPanel12.setPreferredSize(new java.awt.Dimension(g.graphBean1.getWidth(), g.graphBean1.getHeight()));
            //g.JPanel8.setPreferredSize(new java.awt.Dimension(g.graphBean1.getWidth()+g.infoTable1.getWidth()+35,g.graphBean1.getHeight()+20));
            this.pack();
        }                 
                graph.setPreferredCompoSize(new java.awt.Dimension(10, 10));
                
                this.fr.addActionListeners(this.buttonBar, this);
//                this.fr.addActionListeners(this.jMenuBar1, this);
                this.b7.doClick();               
                
        //may remove it totable componant class
                HashSet hs = null;
        if (Vmembers != null) {
            hs = new HashSet();
            hs.addAll(Vmembers);
        }
                
       this.setVisible(true);    

    }

    public LineChartGraphComponent getGraphBean1() {
        return graphBean1;
    }

    public TableComponent getInfoTable1() {
        return infoTable1;
    }
    private void initListeners(){
         fr.addBar(buttonBar);
    } 
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
//        JTabbedPane1 = new javax.swing.JTabbedPane();
        JPanel8 = new javax.swing.JPanel();
        JSplitPane4 = new javax.swing.JSplitPane();
        JPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        //graphBean1 = new no.uib.jexpress_modularized.linechart.visualization.LineChart();
        JPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        //infoTable1 = new InfoTable();
        jEPanel1 = new no.uib.jexpress_modularized.core.visualization.JEPanel();
        buttonBar = new javax.swing.JPanel();
        jLabel122 = new javax.swing.JLabel();
        b2 = new javax.swing.JButton();
        b3 = new javax.swing.JButton();
        jSeparator222211 = new javax.swing.JSeparator();
//        b6 = new javax.swing.JButton();
        b7 = new javax.swing.JButton();
        b9 = new javax.swing.JButton();
//        b10 = new javax.swing.JButton();
        jLabel1211 = new javax.swing.JLabel();
        jSeparator22222 = new javax.swing.JSeparator();
//        frame = new javax.swing.JButton();
//        move = new javax.swing.JButton();
//        jSeparator222221 = new javax.swing.JSeparator();
//        b4 = new javax.swing.JButton();
        b1 = new javax.swing.JButton();
//        b21 = new javax.swing.JButton();
//        b22 = new javax.swing.JButton();
//        b23 = new javax.swing.JButton();
//        jMenuBar1 = new javax.swing.JMenuBar();
//        jMenu1 = new javax.swing.JMenu();
//        m1 = new javax.swing.JMenuItem();
//        m2 = new javax.swing.JMenuItem();
//        m6 = new javax.swing.JMenuItem();
//        jMenu11 = new javax.swing.JMenu();
//        m11 = new javax.swing.JMenuItem();
//        m12 = new javax.swing.JMenuItem();
//        m13 = new javax.swing.JMenuItem();
//        m14 = new javax.swing.JMenuItem();
//        m31 = new javax.swing.JMenuItem();
//        m91 = new javax.swing.JMenuItem();
//        m3 = new javax.swing.JMenuItem();
//        m4 = new javax.swing.JMenuItem();
//        m5 = new javax.swing.JMenuItem();
//        jMenu2 = new javax.swing.JMenu();
//        jMenuItem6 = new javax.swing.JMenuItem();
//        jMenuItem7 = new javax.swing.JMenuItem();
//        jMenuItem8 = new javax.swing.JMenuItem();
//        jMenuItem9 = new javax.swing.JMenuItem();
//        jMenuItem10 = new javax.swing.JMenuItem();
//        jMenu12 = new javax.swing.JMenu();
//        m8 = new javax.swing.JMenuItem();

        setClosable(false);
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/fgraph.gif"))); // NOI18N

        jPanel2.setLayout(new java.awt.BorderLayout());

//        JTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        JPanel8.setLayout(new java.awt.BorderLayout());

        JSplitPane4.setDividerLocation(170);
        JSplitPane4.setDividerSize(8);
        JSplitPane4.setOneTouchExpandable(true);

        
        
        
        JPanel12.setMaximumSize(new java.awt.Dimension(500, 500));
        JPanel12.setLayout(new java.awt.BorderLayout());

//        graphBean1.setMaximumCompoSize(new java.awt.Dimension(134500, 103453450));
//        graphBean1.setPreferredCompoSize(new java.awt.Dimension(10, 10));
//       
        jScrollPane1.setMaximumSize(new java.awt.Dimension(200, 200));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 200));
        jScrollPane1.setSize(new java.awt.Dimension(200, 200));
        
        jScrollPane1.setViewportView(graphBean1.getGraph());
        JPanel12.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        JSplitPane4.setRightComponent(JPanel12);

        JPanel9.setLayout(new java.awt.BorderLayout());

        
        jScrollPane2.setPreferredSize(new java.awt.Dimension(453, 203));

        infoTable1.setUseColor(true);
        jScrollPane2.setViewportView(infoTable1.getTable());

        JPanel9.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        JSplitPane4.setLeftComponent(JPanel9);

        JPanel8.add(JSplitPane4, java.awt.BorderLayout.CENTER);

//        JTabbedPane1.addTab("Main Chart", JPanel8);

        jPanel2.add(JPanel8, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jEPanel1.setLColor(new java.awt.Color(215, 215, 255));
        jEPanel1.setUColor(new java.awt.Color(225, 225, 225));
        jEPanel1.setLayout(new java.awt.BorderLayout());

        buttonBar.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(160, 160, 160)));
        buttonBar.setLayout(new javax.swing.BoxLayout(buttonBar, javax.swing.BoxLayout.LINE_AXIS));

        jLabel122.setText("  ");
        buttonBar.add(jLabel122);

        b2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save.gif"))); // NOI18N
        b2.setToolTipText("Save Image");
        b2.setActionCommand("save");
        b2.setBorder(null);
        b2.setBorderPainted(false);
        b2.setContentAreaFilled(false);
        b2.setMaximumSize(new java.awt.Dimension(22, 22));
        b2.setMinimumSize(new java.awt.Dimension(22, 22));
        b2.setPreferredSize(new java.awt.Dimension(22, 22));
        buttonBar.add(b2);

        b3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.gif"))); // NOI18N
        b3.setToolTipText("Print Image");
        b3.setActionCommand("print");
        b3.setBorder(null);
        b3.setBorderPainted(false);
        b3.setContentAreaFilled(false);
        b3.setMaximumSize(new java.awt.Dimension(22, 22));
        b3.setMinimumSize(new java.awt.Dimension(22, 22));
        b3.setPreferredSize(new java.awt.Dimension(22, 22));
        buttonBar.add(b3);

        jSeparator222211.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator222211.setMaximumSize(new java.awt.Dimension(2, 20));
        jSeparator222211.setMinimumSize(new java.awt.Dimension(2, 20));
        jSeparator222211.setPreferredSize(new java.awt.Dimension(0, 20));
        buttonBar.add(jSeparator222211);

//        b6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/world2.gif"))); // NOI18N
//        b6.setToolTipText("Open External Link List");
//        b6.setActionCommand("list");
//        b6.setBorder(null);
//        b6.setBorderPainted(false);
//        b6.setContentAreaFilled(false);
//        b6.setMaximumSize(new java.awt.Dimension(22, 22));
//        b6.setMinimumSize(new java.awt.Dimension(22, 22));
//        b6.setPreferredSize(new java.awt.Dimension(22, 22));
//        buttonBar.add(b6);

        b7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/listselection.gif"))); // NOI18N
        b7.setToolTipText("Shadow Unselected");
        b7.setActionCommand("selectlist");
        b7.setBorder(null);
        b7.setBorderPainted(false);
        b7.setContentAreaFilled(false);
        b7.setMaximumSize(new java.awt.Dimension(22, 22));
        b7.setMinimumSize(new java.awt.Dimension(22, 22));
        b7.setPreferredSize(new java.awt.Dimension(22, 22));
        b7.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b7ActionPerformed(evt);
            }
        });
        buttonBar.add(b7);

        b9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/aalias.gif"))); // NOI18N
        b9.setToolTipText("Antialias Image");
        b9.setActionCommand("antialias");
        b9.setBorder(null);
        b9.setBorderPainted(false);
        b9.setContentAreaFilled(false);
        b9.setMaximumSize(new java.awt.Dimension(22, 22));
        b9.setMinimumSize(new java.awt.Dimension(22, 22));
        b9.setPreferredSize(new java.awt.Dimension(22, 22));
        buttonBar.add(b9);

//        b10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/html.gif"))); // NOI18N
//        b10.setToolTipText("Export Image To HTML");
//        b10.setActionCommand("html");
//        b10.setBorder(null);
//        b10.setBorderPainted(false);
//        b10.setContentAreaFilled(false);
//        b10.setMaximumSize(new java.awt.Dimension(22, 22));
//        b10.setMinimumSize(new java.awt.Dimension(22, 22));
//        b10.setPreferredSize(new java.awt.Dimension(22, 22));
//        buttonBar.add(b10);

        jLabel1211.setMaximumSize(new java.awt.Dimension(4, 17));
        jLabel1211.setMinimumSize(new java.awt.Dimension(4, 17));
        jLabel1211.setPreferredSize(new java.awt.Dimension(4, 17));
        buttonBar.add(jLabel1211);

        jSeparator22222.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator22222.setMaximumSize(new java.awt.Dimension(2, 20));
        jSeparator22222.setMinimumSize(new java.awt.Dimension(2, 20));
        jSeparator22222.setPreferredSize(new java.awt.Dimension(0, 20));
        buttonBar.add(jSeparator22222);

//        frame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/square.gif"))); // NOI18N
//        frame.setToolTipText("Zoom In");
//        frame.setActionCommand("frame");
//        frame.setBorder(null);
//        frame.setBorderPainted(false);
//        frame.setContentAreaFilled(false);
//        frame.setMaximumSize(new java.awt.Dimension(22, 22));
//        frame.setMinimumSize(new java.awt.Dimension(22, 22));
//        frame.setPreferredSize(new java.awt.Dimension(22, 22));
//        buttonBar.add(frame);

//        move.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/move.gif"))); // NOI18N
//        move.setToolTipText("Move");
//        move.setActionCommand("move");
//        move.setBorder(null);
//        move.setBorderPainted(false);
//        move.setContentAreaFilled(false);
//        move.setMaximumSize(new java.awt.Dimension(22, 22));
//        move.setMinimumSize(new java.awt.Dimension(22, 22));
//        move.setPreferredSize(new java.awt.Dimension(22, 22));
//        buttonBar.add(move);

//        jSeparator222221.setOrientation(javax.swing.SwingConstants.VERTICAL);
//        jSeparator222221.setMaximumSize(new java.awt.Dimension(2, 20));
//        jSeparator222221.setMinimumSize(new java.awt.Dimension(2, 20));
//        jSeparator222221.setPreferredSize(new java.awt.Dimension(0, 20));
//        buttonBar.add(jSeparator222221);

//        b4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/trash2.gif"))); // NOI18N
//        b4.setToolTipText("Remove Component");
//        b4.setActionCommand("delete");
//        b4.setBorder(null);
//        b4.setBorderPainted(false);
//        b4.setContentAreaFilled(false);
//        b4.setMaximumSize(new java.awt.Dimension(22, 22));
//        b4.setMinimumSize(new java.awt.Dimension(22, 22));
//        b4.setPreferredSize(new java.awt.Dimension(22, 22));
//        buttonBar.add(b4);

        b1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/classes.gif"))); // NOI18N
        b1.setToolTipText("Create Group of Selected");
        b1.setActionCommand("sets");
        b1.setBorder(null);
        b1.setBorderPainted(false);
        b1.setContentAreaFilled(false);
        b1.setMaximumSize(new java.awt.Dimension(22, 22));
        b1.setMinimumSize(new java.awt.Dimension(22, 22));
        b1.setPreferredSize(new java.awt.Dimension(22, 22));
        buttonBar.add(b1);

//        b21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/repaint.gif"))); // NOI18N
//        b21.setToolTipText("Repaint Component");
//        b21.setActionCommand("repaint");
//        b21.setBorder(null);
//        b21.setBorderPainted(false);
//        b21.setContentAreaFilled(false);
//        b21.setMaximumSize(new java.awt.Dimension(22, 22));
//        b21.setMinimumSize(new java.awt.Dimension(22, 22));
//        b21.setPreferredSize(new java.awt.Dimension(22, 22));
//        buttonBar.add(b21);

//        b22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clip.gif"))); // NOI18N
//        b22.setToolTipText("Copy Chart Image to System Clipboard");
//        b22.setActionCommand("copy");
//        b22.setBorder(null);
//        b22.setBorderPainted(false);
//        b22.setContentAreaFilled(false);
//        b22.setMaximumSize(new java.awt.Dimension(22, 22));
//        b22.setMinimumSize(new java.awt.Dimension(22, 22));
//        b22.setPreferredSize(new java.awt.Dimension(22, 22));
//        buttonBar.add(b22);

//        b23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/speed.gif"))); // NOI18N
//        b23.setToolTipText("Toggle Chart Speed Drawing");
//        b23.setActionCommand("speedup");
//        b23.setBorder(null);
//        b23.setBorderPainted(false);
//        b23.setContentAreaFilled(false);
//        b23.setMaximumSize(new java.awt.Dimension(22, 22));
//        b23.setMinimumSize(new java.awt.Dimension(22, 22));
//        b23.setPreferredSize(new java.awt.Dimension(22, 22));
//        b23.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                b23ActionPerformed(evt);
//            }
//        });
//        buttonBar.add(b23);

        jEPanel1.add(buttonBar, java.awt.BorderLayout.NORTH);

        getContentPane().add(jEPanel1, java.awt.BorderLayout.NORTH);

//        jMenu1.setText("Image");
//        jMenu1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//
//        m1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m1.setText("Save");
//        m1.setActionCommand("save");
//        m1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save.gif"))); // NOI18N
//        jMenu1.add(m1);
//
//        m2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.gif"))); // NOI18N
//        m2.setText("Print");
//        m2.setActionCommand("print");
//        jMenu1.add(m2);
//
//        m6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clip.gif"))); // NOI18N
//        m6.setText("Copy Chart Image to System Clipboard");
//        m6.setActionCommand("copy");
//        jMenu1.add(m6);

//        jMenuBar1.add(jMenu1);

//        jMenu11.setText("Line Chart");
//        jMenu11.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//
//        m11.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m11.setText("Remove Tab");
//        m11.setActionCommand("selectlist");
//        m11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/trash2.gif"))); // NOI18N
//        m11.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                m11ActionPerformed(evt);
//            }
//        });
//        jMenu11.add(m11);
//
//        m12.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m12.setText("Toggle Shadows");
//        m12.setActionCommand("selectlist");
//        m12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/listselection.gif"))); // NOI18N
//        m12.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                m12ActionPerformed(evt);
//            }
//        });
//        jMenu11.add(m12);
//
//        m13.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/aalias.gif"))); // NOI18N
//        m13.setText("Toggle Anti Alias");
//        m13.setActionCommand("antialias");
//        jMenu11.add(m13);
//
//        m14.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m14.setText("Toggle External Link List");
//        m14.setActionCommand("list");
//        m14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/world2.gif"))); // NOI18N
//        jMenu11.add(m14);
//
//        m31.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/ruler.gif"))); // NOI18N
//        m31.setText("Chart Layout");
//        m31.setActionCommand("layout");
//        m31.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                m31ActionPerformed(evt);
//            }
//        });
//        jMenu11.add(m31);
//
//        m91.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/totree.gif"))); // NOI18N
//        m91.setText("Put in Tree");
//        m91.setActionCommand("toTree");
//        jMenu11.add(m91);
//
//        m3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/repaint.gif"))); // NOI18N
//        m3.setText("Update And Repaint");
//        m3.setActionCommand("repaint");
//        jMenu11.add(m3);
//
//        m4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/color.gif"))); // NOI18N
//        m4.setText("Full Color Mode");
//        m4.setActionCommand("color");
//        m4.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                m4ActionPerformed(evt);
//            }
//        });
//        jMenu11.add(m4);
//
//        m5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bw.gif"))); // NOI18N
//        m5.setText("Black and White Mode");
//        m5.setActionCommand("bw");
//        jMenu11.add(m5);
//
//        jMenu2.setText("Table Column Resize Model");
//        jMenu2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N

//        jMenuItem6.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
//        jMenuItem6.setText("Auto All");
//        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jMenuItem6ActionPerformed(evt);
//            }
//        });
//        jMenu2.add(jMenuItem6);
//
//        jMenuItem7.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
//        jMenuItem7.setText("Auto Next");
//        jMenuItem7.setActionCommand("Auto Last");
//        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jMenuItem7ActionPerformed(evt);
//            }
//        });
//        jMenu2.add(jMenuItem7);
//
//        jMenuItem8.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
//        jMenuItem8.setText("Auto Last");
//        jMenuItem8.setActionCommand("Auto Next");
//        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jMenuItem8ActionPerformed(evt);
//            }
//        });
//        jMenu2.add(jMenuItem8);
//
//        jMenuItem9.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
//        jMenuItem9.setText("Auto Subsequent");
//        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jMenuItem9ActionPerformed(evt);
//            }
//        });
//        jMenu2.add(jMenuItem9);
//
//        jMenuItem10.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
//        jMenuItem10.setText("Off");
//        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jMenuItem10ActionPerformed(evt);
//            }
//        });
//        jMenu2.add(jMenuItem10);
//
//        jMenu11.add(jMenu2);
//
//        jMenuBar1.add(jMenu11);
//
//        jMenu12.setText("Help");
//        jMenu12.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N

//        m8.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
//        m8.setText("Gene Graph Viewer");
//        m8.setActionCommand("delete");
//        m8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bquestion.gif"))); // NOI18N
//        jMenu12.add(m8);

//        jMenuBar1.add(jMenu12);

//        setJMenuBar(jMenuBar1);

        pack();
    }

//     private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {                                            
//		infoTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//    }                                           
//
//    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {                                           
//		infoTable1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
//    }                                          
//
//    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {                                           
//		infoTable1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
//    }                                          
//
//    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {                                           
//		infoTable1.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
//        
//    }                                          
//
//    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {                                           
//		infoTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//    }                                          

//    private void b23ActionPerformed(java.awt.event.ActionEvent evt) {                                    
//        //b23.setIcon
//        if(graphBean1.getEnableSpeeding()){
//          graphBean1.setEnableSpeeding(false);
//          
//          b23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/speedof.gif")));  
//          graphBean1.forceFullRepaint();
//        } 
//        else{
//            graphBean1.setEnableSpeeding(true);
//            
//            b23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/speed.gif")));
//            graphBean1.forceFullRepaint();
//        }
//        
//        
//    }                                   



//    private void m4ActionPerformed(java.awt.event.ActionEvent evt) {                                   
//
//        // Add your handling code here:
//
//    }                                  
//
//
//
//    private void m31ActionPerformed(java.awt.event.ActionEvent evt) {                                    
//
//        //graphBean1.sp.show();
//
//    }                                   
//
//
//
//    private void m12ActionPerformed(java.awt.event.ActionEvent evt) {                                    
//
//        // Add your handling code here:
//
//    }                                   
//
//
//
//    private void m11ActionPerformed(java.awt.event.ActionEvent evt) {                                    
//
//        // Add your handling code here:
//
//    }                                   

    private void b7ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
    }                                  

      private void disconnectData()
    {
        graphBean1.disconnectData();
        infoTable1.disconnectData();
    }
      
       public LinChartControlPanel getLineChartPanelView() {
        return this;
    }
      public LinChartControlPanel getSource()
    {
    	//LineChartPanel source = (LineChartPanel) components.elementAt(graphView.JTabbedPane1.getSelectedIndex());
    	return this;
    }
      @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        //listselected=g.b7.isSelected();
        //NOTE: println methods-calls used to dinstinguish which button is which. Only used for debugging and 
        //investigating code.
        LinChartControlPanel source = null;
        if (frameVisible) {
            source = this.getSource();
        } else {
            source = this;
        }



        if (e.getActionCommand().equals("sets")) {
            //NOTE: print used to see which button triggers which action
            //(looks like it is the button "Create Group Of Selected")
            System.out.println("LineChartPanel: action 'sets'");
            this.getInfoTable1().createGroup();
             
            if (source.graphBean1.getGraph().getDraw() != null) {
                //@TODO: reimplement me?
//                expresscomponents.GroupCreator.groupCreator2 gr = new expresscomponents.GroupCreator.groupCreator2(cl.MW,true,data,cl,source.g.graphBean1.getDraw(),false);
//                gr.setLocationRelativeTo(this);
//                gr.show();
                // data.addGroup(new Boolean(true),"Graph",source.g.graphBean1.getDraw());  //g.infoTable1.getDataSelection());
            } else {
                //@TODO: reimplement me?
//                 expresscomponents.GroupCreator.groupCreator2 gr = new expresscomponents.GroupCreator.groupCreator2(cl.MW,true,data,cl,source.g.graphBean1.getMembers(),false);
//                gr.setLocationRelativeTo(this);
//                gr.show();
                //data.addGroup(new Boolean(true),"Graph",source.g.graphBean1.getMembers());
            }

            //@TODO: reimplement me?
//             data.fireChangeEve<t();
             /*
             * if(cl.sets==null){
             *
             * //cl.sets = new SetController(cl,getDataSet()); cl.sets = new
             * SetController(cl,data); cl.sets.addActionListener(this);
             * cl.sets.chooser.toFront();
             *
             * }
             *
             * Vector v = new Vector(); cl.sets.setItems(v);
             *
             * cl.sets.setDataSet(data); cl.sets.addActionListener(this);
             * cl.sets.setItems(v); cl.sets.setVisible(true);
             * cl.sets.chooser.toFront();
             */
        }

        if (e.getActionCommand().equals("repaint")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'repaint'");
            source.graphBean1.dataHasChangedRedraw();
            source.infoTable1.dataHasChangedRedraw();
        }

//        if (e.getActionCommand().equals("delete")) {
//            //NOTE: print used to see which button triggers which action
//            System.out.println("LineChartView: action 'delete'");
//
//            int index = source.getSelectedIndex();
//            if (index != 0) {
//               // source.removeComp(index);
//                source.setSelectedIndex(index - 1);
//                source.removeTabAt(index);
//            }
//        }


        if (e.getActionCommand().equals("layout")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartCompute: action 'layout'");
            source.setLocationRelativeTo(source.getLineChartPanelView());
            source.showGraph();

        }

        if (e.getActionCommand().equals("mbars")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'mbars'");
        }

        if (e.getActionCommand().equals("save")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'save'");

            //lineChartUtil.saveImage();


            // g.graphBean1.saveImage();
        }

        if (e.getActionCommand().equals("print")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'print'");

            //lineChartUtil.printImage();


            // g.graphBean1.printImage();
           /*
             * g.graphBean1.LockFullRepaint=true;
             * expresscomponents.Print.PrintPreview2 pw= new
             * expresscomponents.Print.PrintPreview2(cl.MW,true);
             * pw.setComponent(g.graphBean1); pw.show();
             * g.graphBean1.LockFullRepaint=false;
             */
            //ImageHandler ih = new ImageHandler();
            //ih.printImage(getImage(),cl.MW);
            //cl.fileSystem.getLastPath()=ih.lastpath;

        }


        if (e.getActionCommand().equals("copy")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'copy'");


            // g.graphBean1.LockFullRepaint = true;

            /*
             * final Clipboard clipboard =
             * java.awt.Toolkit.getDefaultToolkit().getSystemClipboard(); JLabel
             * l = new JLabel("");
             *
             *
             * l.setIcon(new ImageIcon(getImage())); l.setTransferHandler(new
             * expresscomponents.ImageSelection()); if(clipboard==null)
             * System.out.print("\ncb is null");
             *
             * TransferHandler handler = l.getTransferHandler();
             *
             *
             * if(handler==null) System.out.print("\thc is null");
             *
             * handler.exportToClipboard(l, clipboard, TransferHandler.COPY);
             */
           // lineChartUtil.copyImage();

        }





        if (e.getActionCommand().equals("list")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'list'");

            if (source.isExternallist()) {
                source.setExternallist(false);
            } else {
                source.setExternallist(true);
            }

            if (source.isExternallist()) {

                //@TODO: reimplement me 
//                if (source.le != null) {
//                    source.le = null;
//                }

                //@TODO: reimplement me 
//                source.le = new ListEngine(source.data, Vmembers, cl, false);

                javax.swing.JSplitPane split = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
                split.setOneTouchExpandable(true);
                split.setTopComponent((java.awt.Component) source.getLineChartPanelView().jScrollPane2);

                //@TODO: reimplement me 
//                split.setBottomComponent((java.awt.Component) source.le.GUI.JScrollPane1);

                source.getLineChartPanelView().JPanel9.removeAll();
                source.getLineChartPanelView().JPanel9.add("Center", split);
                source.getLineChartPanelView().JPanel9.validate();

                split.setDividerLocation(0.5);

            } else {
               source.getLineChartPanelView().JPanel9.removeAll();
               source.getLineChartPanelView().JPanel9.add("Center", source.getLineChartPanelView().jScrollPane2);
               source.getLineChartPanelView().JPanel9.validate();
            }
        } else if (e.getActionCommand().equals("antialias")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'antialias'");

            //g.graphBean1.antialias=g.b9.isSelected();
            if (source.isAalias()) {
                source.setAalias(false);
            } else {
                source.setAalias(true);
            }

            source.setGraphAntialias(source.isAalias());
            source.forceFullRepaint();

        }


        if (e.getActionCommand().equals("toTree")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'toTree'");
            source.putInTree();
        } else if (e.getActionCommand().equals("color")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'color'");

            source.setGrayScale(false);
            source.forceFullRepaint();
        } else if (e.getActionCommand().equals("bw")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'bw'");

            source.setGrayScale(true);
            source.forceFullRepaint();
        } else if (e.getActionCommand().equals("move")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'move'");
            source.setMove(true);
            source.setGraphCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        } else if (e.getActionCommand().equals("frame")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'frame'");


            source.setMove(false);
            source.setGraphCursor(new java.awt.Cursor(java.awt.Cursor.MOVE_CURSOR));
        } else if (e.getActionCommand().equals("html")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'html'");



            //@TODO: reimplement me 
//            final progress2 pr2 = new progress2();
            //@TODO: reimplement me?
//              cl.MW.JDesktopPane1.add(pr2);


            //@TODO: reimplement me 
//            pr2.JProgressBar2.setMaximum(data.getDataLength());
//
//            pr2.setLocation(cl.MW.projectPanel.getSize().width + 10, 20);
//
//            pr2.JLabel2.setText("Generating HTML");
//
//            pr2.pack();



            final SwingWorker worker = new SwingWorker() {
                java.io.FileOutputStream ostream;
                //BufferedOutputStream
                java.io.FileOutputStream buffer;
                java.io.DataOutputStream dataout;

                public Object construct() {

                    //@TODO: reimplement me
//                    pr2.JButton1.addActionListener(new java.awt.event.ActionListener() {
//
//                        public void actionPerformed(java.awt.event.ActionEvent e) {
//                            stop = true;
//                            interrupt();
//                        }
//                    });




                    //@TODO: reimplement me 
//                    Color[] fg = new Color[data.getDataLength()];
                    Group Class = null;
                    Color classColor = null;
                    boolean activeSet = false;
                    boolean[] indexes = null;

                    //@TODO: reimplement me 
//                    boolean[] hasbeenpainted = new boolean[data.getDataLength()];



                    // String allClasses=null;
                    //FileDialog fd;
                    File f;
                    String rr = "";
                    String gg = "";
                    String bb = "";

                    int visiblegroups = 0;
                    boolean[] visible = null;

                    //@TODO: reimplement me 
//                    expresscomponents.filehandler fh = new expresscomponents.filehandler();



                    //DataOutputStream dataout;
                    //   int temp=0;
                    java.util.Date d = new java.util.Date();
                    //fd=new FileDialog(new Frame());
                    //fd.setMode(FileDialog.SAVE);
                    //fd.show();

                    javax.swing.JFileChooser fd = new javax.swing.JFileChooser();
                    fd.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        public boolean accept(File f) {
                            if (f.getName().endsWith(".html")) {
                                return true;
                            } else if (f.isDirectory()) {
                                return true;
                            } else {
                                return false;
                            }
                        }

                        public String getDescription() {
                            return "HTML Documents (*.html)";
                        }
                    });

                    //@TODO: reimplement me?
//             if(cl.fileSystem.getLastPath()!= null && cl.fileSystem.getLastPath() !=""){fd.setCurrentDirectory(new File(cl.fileSystem.getLastPath()));}

                    //fd.showSaveDialog(new JFrame());
                    int returnVal = fd.showSaveDialog(new javax.swing.JFrame());
                    f = fd.getSelectedFile();
                    File dir = fd.getCurrentDirectory();

                    if (returnVal == fd.CANCEL_OPTION) {
                        return null;
                    }

                    if (!(f.getAbsolutePath().toLowerCase().endsWith(".htm") || f.getAbsolutePath().toLowerCase().endsWith(".html"))) {
                        f = new File(f.getAbsolutePath() + ".html");
                    }

                    try {
                        //@TODO: reimplement me 
//                        pr2.setVisible(true);
                        if (f != null && !f.isDirectory() && returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {

                            //@TODO: reimplement me?
//                     cl.fileSystem.setLastPath(dir.getAbsolutePath());

                            //ostream = new FileOutputStream(f);
                            buffer = new java.io.FileOutputStream(f);//BufferedOutputStream(ostream);
                            //dataout = new DataOutputStream(buffer);

                            //   dataout.writeBytes("Identifiers");
                            String identifier = "";
                            if (f.getName().indexOf(".") > 0) {
                                identifier = "-" + f.getName().substring(0, f.getName().indexOf("."));
                            } else {
                                identifier = "-" + f.getName();
                            }

                            buffer.write(("<html><Body bgColor=\"#ffffff\">").getBytes());
                            buffer.write(("<p><font size =4 ><B>" + "Date Of Analysis:" + d.toLocaleString() + "</b></font><hr>").getBytes());

                            buffer.write(("<br><img src=\"im/graph" + identifier + ".png\" border=1><BR><BR>").getBytes());

                            File f2 = new File(dir.getAbsolutePath(), "im");


                            f2.mkdir();

                            //   fh.saveimage(getAllImages(),f2.getAbsolutePath()+f.separatorChar+"all"+d.getMonth()+"-"+d.getDay()+"-"+d.getHours()+"-"+d.getMinutes()+"-"+d.getSeconds()+".png");

                            //@TODO: reimplement me 
//                            fh.saveimage(getImage(), f2.getAbsolutePath() + f.separatorChar + "graph" + identifier + ".png");

                            buffer.write(("<font size=5><i>" + "Members:" + "</i></font></br>" + br).getBytes());
                            buffer.write(("<Table border=1 cellspacing=0 cellpadding=1>" + br).getBytes());
                            //htmlString=htmlString+("<tr><td colspan="+ (2+data.getGroups().size())+"><font size=5><i>Members:</i></font></tr>\n");


                            //Do not paint genes that are not in a visible set!
                            //@TODO: reimplement me 
//                            visible = new boolean[data.getDataLength()];

                            //@TODO: reimplement me 
//                            for (int i = 0; i < data.getDataLength() - 1; i++) {
//                                for (int j = 0; j < data.getGroups().size(); j++) {
//                                    Class = (Group) data.getGroups().elementAt(j);
//                                    activeSet = Class.isActive();//(Boolean)Class.elementAt(0);
//                                    indexes = Class.getMembers();//(boolean[]) Class.elementAt(3);
//                                    if (activeSet) {
//                                        if (indexes[i]) {
//                                            visible[i] = true;
//                                        }
//                                    }
//                                }
//                            }


                            //Count the number of active groups..
                            //@TODO: reimplement me 
//                            for (int j = 0; j < data.getGroups().size() - 1; j++) {
//                                Class = (Group) data.getGroups().elementAt(j);
//                                activeSet = Class.isActive();//(Boolean)Class.elementAt(0);
//
//                                if (activeSet) {
//                                    visiblegroups++;
//                                }
//
//                            }

                            //@TODO: reimplement me 
//                            if (data.getGroups().size() > 1) {
//                                //Print group headers..
//                                //if(data.getInfos()==null && visiblegroups>0) htmlString=htmlString+("<tr bgcolor=\"#aaaabb\"><td>Groups</td>"+br);
//                                buffer.write(("<tr bgcolor=\"#aaaabb\"><td Colspan=" + data.getInfoHeaders().length + ">" + "Groups" + "</td>" + br).getBytes());
//
//
//                                for (int j = 0; j < data.getGroups().size() - 1; j++) {
//                                    Class = (Group) data.getGroups().elementAt(j);
//                                    activeSet = Class.isActive();//(Boolean)Class.elementAt(0);
//                                    classColor = Class.getColor();//(Color)Class.elementAt(2);
//                                    indexes = Class.getMembers();//(boolean[]) Class.elementAt(3);
//                                    if (activeSet) {
//                                        buffer.write(("<td>" + (String) Class.getName() + "</td>" + br).getBytes());
//
//                                    }
//                                }
//                                buffer.write(("</tr>" + br).getBytes());
//                                //group headers done....................
//
//                            }

                            buffer.write(("<tr bgColor=\"#eeeeee\">").getBytes());

                            //@TODO: reimplement me 
//                            for (int j = 0; j < data.getInfoHeaders().length; j++) {
//                                if (data.getusedInfos()[j]) {
//                                    buffer.write(("<td><b>" + data.getInfoHeaders()[j] + "</b></td>").getBytes());
//                                }
//                            }

                            for (int i = 0; i < visiblegroups; i++) {
                                buffer.write(("<td>&nbsp;</td>").getBytes());
                            }
                            buffer.write(("</tr>" + br).getBytes());


                            //@TODO: reimplement me 
//                            for (int i = 0; i < data.getDataLength(); i++) {
//
//                                if (visible[i] && members[i]) {
//
//
//                                    buffer.write(("<tr>" + br).getBytes());
//
//
//                                    for (int j = 0; j < data.getInfoHeaders().length; j++) {
//
//                                        if (data.getusedInfos()[j]) {
//
//
//                                            buffer.write(("<td>").getBytes());
//                                            if (g.graphBean1.draw != null && !g.graphBean1.draw[i]) {
//                                                buffer.write(("<font color=\"bbbbbb\">").getBytes());
//                                            }
//
//                                            buffer.write((data.getInfos()[i][j]).getBytes());
//                                            if (g.graphBean1.draw != null && !g.graphBean1.draw[i]) {
//                                                buffer.write(("</font>").getBytes());
//                                            }
//                                            buffer.write(("</td>" + br).getBytes());
//
//                                        }
//                                    }
//
//                                    for (int j = 0; j < data.getGroups().size() - 1; j++) {
//                                        Class = (Group) data.getGroups().elementAt(j);
//                                        activeSet = Class.isActive();//(Boolean)Class.elementAt(0);
//                                        classColor = Class.getColor();//(Color)Class.elementAt(2);
//                                        indexes = Class.getMembers();//(boolean[]) Class.elementAt(3);
//
//                                        //We do not want to paint the line more than once.
//                                        if (activeSet && indexes[i]) {
//                                            fg[i] = classColor;
//                                            rr = Integer.toHexString(classColor.getRed());
//                                            gg = Integer.toHexString(classColor.getGreen());
//                                            bb = Integer.toHexString(classColor.getBlue());
//                                            if (rr.length() == 1) {
//                                                rr = "0" + rr;
//                                            }
//                                            if (gg.length() == 1) {
//                                                gg = "0" + gg;
//                                            }
//                                            if (bb.length() == 1) {
//                                                bb = "0" + bb;
//                                            }
//
//                                            buffer.write(("<td><Font color=\"" + rr + gg + bb + "\">").getBytes());
//                                            buffer.write(("<center>X</center>").getBytes());
//                                            buffer.write(("</td></font>" + br).getBytes());
//
//                                        } else if (activeSet && !indexes[i]) {
//                                            buffer.write(("<td>&nbsp;</td>" + br).getBytes());
//                                        }
//                                    }
//
//                                    buffer.write(("</tr>" + br).getBytes());
//                                }
//                                if (stop) {
//                                    break;
//                                }
//                                pr2.JProgressBar2.setValue(pr2.JProgressBar2.getValue() + 1);
//
//                            }//The check if a gene is visible by visible[i]....



                            buffer.write(("</Table>").getBytes());
                            //if(visiblegroups>1)  buffer.write( ("<p><b>Note:</b><br>indices with more than one color are painted in the rightmost color.").getBytes());
                            buffer.write(("</html>").getBytes());
                            // dataout.writeBytes(htmlString);

                            buffer.flush();
                            buffer.close();

                        }
                    } catch (java.io.IOException ex) {
                        //@TODO: reimplement me 
//                        pr2.setVisible(false);
//                        pr2.dispose();
                    }

                    return null;

                }

                @Override
                public void finished() {
                    try {
                        if (buffer != null) {
                            buffer.flush();
                            buffer.close();
                        }
//            ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //@TODO: reimplement me 
//                    pr2.setVisible(false);
//                    pr2.dispose();
                }
            };

            worker.start();


        } //the listselection button is toggled
        else if (e.getActionCommand().equals("selectlist")) {
            //NOTE: print used to see which button triggers which action
            System.out.println("LineChartView: action 'selectlist'");

            if (source.isListselected()) {
                source.setListselected(false);               
                
            } else {
                source.setListselected(true);
            }

            if (source.isListselected()) {
                boolean[] temp = source.getDataSelection();//new boolean[source.data.getDataLength()];
                source.setDraw(temp);

            } else {
                boolean[] temp = null;
                source.setDraw(temp);
            }

        }
    }
      
      public void setLocationRelativeTo(LinChartControlPanel source)
    {
            graphBean1.getGraph().sp.setLocationRelativeTo(source);
    }
    
    public void showGraph()
    {
    	 graphBean1.getGraph().sp.show();
    }
    
    
     public boolean isExternallist()
    {
    	return this.externallist;
    }
    public void setExternallist(boolean externallist)
    {
    	this.externallist = externallist;
    }
    public boolean isAalias()
    {
    	return this.aalias;
    }
    public void setAalias(boolean aalias)
    {
    	this.aalias = aalias;
    }
    public void setGraphAntialias(boolean antialias)
    {
    	graphBean1.getGraph().antialias = antialias;
    }
    public void forceFullRepaint()
    {
    	graphBean1.getGraph().forceFullRepaint();
    }
	public void setGrayScale(boolean grayScale)
	{
		graphBean1.getGraph().grayScale = grayScale;
	}
	public void setMove(boolean move)
	{
		graphBean1.getGraph().move = move;
	}
	public void setGraphCursor(java.awt.Cursor cursor)
	{
		graphBean1.getGraph().setCursor(cursor);
	}
	public boolean isListselected()
	{
		return listselected;
	}
	public void setListselected(boolean listselected)
	{
		this.listselected =listselected;
                this.graphBean1.setSelectList(listselected);
	}
	public boolean[] getDataSelection()
	{
		return infoTable1.getTable().getDataSelection();
	}
	public void   setDraw( boolean[] temp)
	{
       
		graphBean1.getGraph().setDraw(temp);

	}

	public void setTable1SelfSelection(boolean selection)
	{
		 infoTable1.getTable().setSelfSelection(selection);
         
	}
	public void clearTableSelection()
	{
		infoTable1.getTable().clearSelection();
	}
	public  void  changeTableSelection(int[] selectedRows)
	{
		  
		infoTable1.getTable().changeSelection(selectedRows); 
	}
        public void putInTree() {


        java.util.Hashtable h = new java.util.Hashtable();

        if (zoomed) {
            h.put("Startcol", new Integer(startlabel));
            h.put("Endcol", new Integer(endlabel));
            h.put("max", new Double(max));
            h.put("min", new Double(min));

//         h.put("meta",data.getMeta());
            // h.put("info",data.getInfo());
        }
        if (Vmembers != null) {
            h.put("Members", Vmembers);
        }

        //@TODO: reimplement me 
//        expresscomponents.ProjectNodes.GraphNode th = new expresscomponents.ProjectNodes.GraphNode(cl, h);
//        data.add(th);
        //@TODO: reimplement me?
//        cl.getTreeModel().reload(data);

        //@TODO: reimplement me 
//        MetaInfoList ml = data.getMetaList().copyList();
        //@TODO: reimplement me 
//        ml.addElement(metainfonode);

        //MetaInfoNode

        //@TODO: reimplement me 
//        h.put("MetaList", ml);
//        th.setMetaList(ml.copyList());

        // data.addChild();
        //((javax.swing.tree.DefaultTreeModel)data.getTree().getModel()).reload(data.getNode());
        //data.getTree().makeVisible(new javax.swing.tree.TreePath(th.getNode().getPath()));

    }

      
}
