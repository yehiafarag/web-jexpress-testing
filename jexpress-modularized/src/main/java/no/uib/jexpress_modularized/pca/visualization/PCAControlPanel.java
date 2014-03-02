package no.uib.jexpress_modularized.pca.visualization;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Vector;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.visualization.buttonFramer;
import no.uib.jexpress_modularized.pca.computation.PcaResults;

/**
 *
 * @author Yehia Farag
 */
public class PCAControlPanel extends javax.swing.JInternalFrame implements ActionListener, java.awt.event.MouseMotionListener, java.awt.event.MouseListener, Serializable {

    private String[][] SpotNames = null;

    @Override
    public void mouseDragged(MouseEvent me) {
        BigDecimal bd = new BigDecimal(plot.getXaxis().getDouble(me.getX()));
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_DOWN);

        BigDecimal bd2 = new BigDecimal(plot.getYaxis().getDouble(me.getY()));
        bd2 = bd2.setScale(1, BigDecimal.ROUND_HALF_DOWN);
        //NOTE: the x,y coordinates that are in the lower left of the window? The x-coordinate is unresponsive, in that case.
        location.setText("X: " + bd.doubleValue() + " Y: " + bd2.doubleValue());

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        boolean[] tolerated = plot.getPlot().getTolerated();

        Vector hits = plot.getPlot().getIndexesAtPoint(e.getPoint(), 5);

        String ret = "";
        for (int i = 0; i < Math.min(hits.size(), 7); i++) {
            int hit = ((Integer) hits.elementAt(i)).intValue();
            if ((tolerated == null) || (tolerated[hit])) {
                for (int j = 0; j < plot.getDataSet().getInfoHeaders().length; j++) {
                    if (plot.getDataSet().getusedInfos()[1 + j]) {
                        ret += plot.getDataSet().getInfoHeaders()[j] + ": ";
                        ret += plot.getDataSet().getInfos()[hit][j];
                        if (j != plot.getDataSet().getInfoHeaders().length - 1) {
                            ret = ret + "\n";
                        }
                    }
                }

                if (i != hits.size() - 1) {
                    ret = ret + "\n-------\n";
                }
            }
        }


        if (ret.endsWith("\n")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        if (tooltip.isSelected()) {
            plot.getPlot().setToolTipText(ret);
        } else {
            plot.getPlot().setToolTipText(null);
        }

        BigDecimal bd = new BigDecimal(plot.getXaxis().getDouble(e.getX()));
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_DOWN);

        BigDecimal bd2 = new BigDecimal(plot.getYaxis().getDouble(e.getY()));
        bd2 = bd2.setScale(1, BigDecimal.ROUND_HALF_DOWN);

        location.setText("X: " + bd.doubleValue() + " Y: " + bd2.doubleValue());


    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //       throw new UnsupportedOperationException("mousePressed Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if (!plot.getPlot().isZoompca() && !plot.getPlot().isPaintNamesonClick()) {
            if (plot.getPlot().getFramedIndexes() != null) {
                plot.sweep(plot.getPlot().getFramedIndexes());
            }
            plot.getPlot().clearPath();
            plot.getPlot().repaint();

        } else if (plot.getPlot().isPaintNamesonClick()) {
            Vector sel = new Vector();
            boolean[] bl = plot.getPlot().getFramedIndexes();

            for (int i = 0; i < bl.length; i++) {
                if (bl[i]) {
                    sel.addElement(new Integer(i));
                }
            }
            if (sel.isEmpty()) {
                sel = plot.getPlot().getIndexesAtPoint(me.getPoint(), plot.getPlot().dotsize);
            }

            if (sel.isEmpty()) {
                return;
            }

            if (SpotNames == null) {
                SpotNames = new String[plot.getDataSet().getDataLength()][];
                plot.getPlot().setSpotNames(SpotNames);
            }

            int cnt = 0;
            boolean[] b = plot.getDataSet().getusedInfos();
            for (int i = 1; i < b.length - 1; i++) {
                if (b[i]) {
                    cnt++;
                }
            }
            int cnt2 = 0;

            String s = null;
            for (int i = 0; i < sel.size(); i++) {

                if (SpotNames[((Integer) sel.elementAt(i)).intValue()] != null) {
                    SpotNames[((Integer) sel.elementAt(i)).intValue()] = null;
                } else {


                    SpotNames[((Integer) sel.elementAt(i)).intValue()] = new String[cnt];
                    cnt2 = 0;
                    for (int j = 1; j < b.length - 1; j++) {

                        if (b[j]) {
                            s = plot.getDataSet().getInfos()[((Integer) sel.elementAt(i)).intValue()][j - 1];
                            SpotNames[((Integer) sel.elementAt(i)).intValue()][cnt2] = s;
                            cnt2++;
                        }
                    }

                }
            }

            plot.getPlot().forceFullRepaint();

        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //  throw new UnsupportedOperationException("mouseEntered Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // throw new UnsupportedOperationException("mouseExited Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public buttonFramer bFramer = new buttonFramer();
    private java.awt.Frame parent;
    private boolean quadratic = true;
    private no.uib.jexpress_modularized.pca.computation.PcaResults pcaResults;

    public PCAControlPanel(PCA2DComponent plot, no.uib.jexpress_modularized.pca.computation.PcaResults pcaResults) {
        this.pcaResults = pcaResults;
        this.plot = plot;
        plot.getPlot().forceFullRepaint();
        initComponents();
        initListeners();
        initOther();
        manageResult();
        SetPlotProperties();
        lay();
    }

    public PCAControlPanel(PcaResults pcaResults, Dataset data) {
    }

    public void SetPlotProperties() {
        varx.setText("Principal Component nr." + String.valueOf(plot.getPcax() + 1) + " : " + pcaResults.varianceastr(plot.getPcax()) + "% var.");
        vary.setText("Principal Component nr." + String.valueOf(plot.getPcay() + 1) + " : " + pcaResults.varianceastr(plot.getPcay()) + "% var.");

        double totalvar = 0.0;
        java.text.NumberFormat numformat = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);
        numformat.setMaximumFractionDigits(1);

        if (plot.getPcax() != plot.getPcay()) {
            totalvar = pcaResults.varianceaccounted(plot.getPcax()) + pcaResults.varianceaccounted(plot.getPcay());
        } else {
            totalvar = pcaResults.varianceaccounted(plot.getPcax());
        }
        vartot.setText("Total variance retained: " + numformat.format(totalvar) + "% var.");
    }

    public void manageResult() {



        plot.getPlot().setHex(!quadratic);

        for (int i = 0; i < pcaResults.eigenvalues.length; i++) {
            Xaxis.addItem("Principal Component nr." + String.valueOf(i + 1) + " - " + pcaResults.varianceastr(i) + "% var.");
            Yaxis.addItem("Principal Component nr." + String.valueOf(i + 1) + " - " + pcaResults.varianceastr(i) + "% var.");
            Zaxis.addItem("Principal Component nr." + String.valueOf(i + 1) + " - " + pcaResults.varianceastr(i) + "% var.");
        }

        Xaxis.setSelectedIndex(0);
        Yaxis.setSelectedIndex(1);
        Zaxis.setSelectedIndex(2);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                //NOTE: print for seeing which button is which; remove later
                plot.setPcax(Xaxis.getSelectedIndex());
                plot.setPcay(Yaxis.getSelectedIndex());
                plot.setPcaz(Zaxis.getSelectedIndex());
                SetPlotProperties();
                plot.updatePlot();
            }
        });

        plot.getPlot().addMouseMotionListener(this);
        plot.getPlot().addMouseListener(this);


        bFramer.addActionListeners(ButtonBar, this);
        bFramer.addActionListeners(jMenuBar1, this);

        setTitle("PCA -" + plot.getDataSet().getName());

        //Stop datalistening if the window is closed. If not removed, this will not be garbage-collected.
        addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent we) {
                pcaResults = null;
                axis.setVisible(false);
                axis.dispose();

                bFramer.removeActionListeners(ButtonBar, PCAControlPanel.this);
                bFramer.removeActionListeners(jMenuBar1, PCAControlPanel.this);

                setJMenuBar(null);
                plot.disconnectData();

            }
        });

        pack();
        if (showPCA) {
            show();
        }

        plot.getPlot().setToolTipText("");

    }
    private boolean showPCA = true;

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;


        selectpop = new javax.swing.JPopupMenu(); //need to fix throw Exception


        square = new javax.swing.JMenuItem();
        lasso = new javax.swing.JMenuItem();
        red = new javax.swing.JRadioButtonMenuItem();
        green = new javax.swing.JRadioButtonMenuItem();
        blue = new javax.swing.JRadioButtonMenuItem();
        gray = new javax.swing.JRadioButtonMenuItem();
        nofill = new javax.swing.JRadioButtonMenuItem();
        mask = new javax.swing.ButtonGroup();
        axis = new javax.swing.JDialog(parent, true);
        jPanel4 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        Zaxis = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        Xaxis = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        Yaxis = new javax.swing.JComboBox();
        buttonpanel = new javax.swing.JPanel();
        ok = new javax.swing.JButton();
        close = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        backSV = new no.uib.jexpress_modularized.core.visualization.ColorButton();
        fogSV = new javax.swing.JCheckBox();
        jLabel31 = new javax.swing.JLabel();
        jLabel311 = new javax.swing.JLabel();
        jLabel312 = new javax.swing.JLabel();



        jPanel1 = new javax.swing.JPanel();
        PCA = new javax.swing.JPanel();

        southPanel = new javax.swing.JPanel();
        variancePanel = new javax.swing.JPanel();
        varx = new javax.swing.JLabel();
        vary = new javax.swing.JLabel();
        vartot = new javax.swing.JLabel();
        colorscale = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        location = new javax.swing.JLabel();
        windowsize = new javax.swing.JLabel();
        jEPanel1 = new no.uib.jexpress_modularized.core.visualization.JEPanel();
        ButtonBar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        b2 = new javax.swing.JButton();
        b3 = new javax.swing.JButton();
        b8 = new javax.swing.JButton();
        b2121 = new javax.swing.JButton();
        b212 = new javax.swing.JButton();
        b213 = new javax.swing.JButton();
        b241 = new javax.swing.JButton();
        b15 = new javax.swing.JButton();
        b21211 = new javax.swing.JButton();
        b211 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();

        jMenu21 = new javax.swing.JMenu();
        m6 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        m2211 = new javax.swing.JMenuItem();
        m19 = new javax.swing.JMenuItem();
        Variance = new javax.swing.JCheckBoxMenuItem();
        tooltip = new javax.swing.JCheckBoxMenuItem();
        jMenu12 = new javax.swing.JMenu();
        m81 = new javax.swing.JMenuItem();

        square.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/square.gif"))); // NOI18N
        square.setText("Square");
        square.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareActionPerformed(evt);
            }
        });
        selectpop.add(square);

        lasso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/lasso.gif"))); // NOI18N
        lasso.setText("Lasso");
        lasso.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lassoActionPerformed(evt);
            }
        });
        selectpop.add(lasso);
        mask.add(red);
        red.setText("Red");
        red.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redActionPerformed(evt);
            }
        });
        selectpop.add(red);

        mask.add(green);
        green.setText("Green");
        green.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                greenActionPerformed(evt);
            }
        });
        selectpop.add(green);

        mask.add(blue);
        blue.setText("Blue");
        blue.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blueActionPerformed(evt);
            }
        });
        selectpop.add(blue);

        mask.add(gray);
        gray.setText("Gray");
        gray.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grayActionPerformed(evt);
            }
        });
        selectpop.add(gray);

        mask.add(nofill);
        nofill.setSelected(true);
        nofill.setText("No fill");
        nofill.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nofillActionPerformed(evt);
            }
        });
        selectpop.add(nofill);

        axis.setTitle("PCA Axis");
        axis.setResizable(false);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel22.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel22.setText("Z Axis");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 12, 3);
        jPanel31.add(jLabel22, gridBagConstraints);

        Zaxis.setMinimumSize(new java.awt.Dimension(126, 20));
        Zaxis.setPreferredSize(new java.awt.Dimension(280, 20));
        Zaxis.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZaxisActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 12, 0);
        jPanel31.add(Zaxis, gridBagConstraints);

        jPanel4.add(jPanel31, java.awt.BorderLayout.SOUTH);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("2D Plot"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText("X Axis");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 2, 2, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

        Xaxis.setMinimumSize(new java.awt.Dimension(126, 20));
        Xaxis.setPreferredSize(new java.awt.Dimension(280, 20));
        Xaxis.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XaxisActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 2, 2, 2);
        jPanel3.add(Xaxis, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel21.setText("Y Axis");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 12, 5);
        jPanel3.add(jLabel21, gridBagConstraints);

        Yaxis.setMinimumSize(new java.awt.Dimension(126, 20));
        Yaxis.setPreferredSize(new java.awt.Dimension(280, 20));
        Yaxis.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YaxisActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 12, 2);
        jPanel3.add(Yaxis, gridBagConstraints);

        jPanel4.add(jPanel3, java.awt.BorderLayout.CENTER);

        axis.getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        buttonpanel.setLayout(new java.awt.FlowLayout(2));

        ok.setBackground(new java.awt.Color(204, 204, 214));
        ok.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        ok.setText("Ok");
        ok.setOpaque(false);
        ok.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });
        buttonpanel.add(ok);

        close.setBackground(new java.awt.Color(204, 204, 214));
        close.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        close.setText("Close");
        close.setOpaque(false);
        close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeActionPerformed(evt);
            }
        });
        buttonpanel.add(close);

        axis.getContentPane().add(buttonpanel, java.awt.BorderLayout.SOUTH);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));
        jPanel5.setOpaque(false);
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Background");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 5);
        jPanel5.add(jLabel3, gridBagConstraints);

        backSV.setMargin(new java.awt.Insets(2, 2, 2, 2));
        backSV.setPreferredSize(new java.awt.Dimension(25, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel5.add(backSV, gridBagConstraints);

        fogSV.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        fogSV.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        jPanel5.add(fogSV, gridBagConstraints);

        jLabel31.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel31.setText("Point Size");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 5);
        jPanel5.add(jLabel31, gridBagConstraints);

        jLabel311.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel311.setText("Size Deviation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 5);
        jPanel5.add(jLabel311, gridBagConstraints);

        jLabel312.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel312.setText("Fog Effect");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 5);
        jPanel5.add(jLabel312, gridBagConstraints);


        setBackground(java.awt.Color.black);
        setClosable(false);
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        PCA.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        PCA.setLayout(new java.awt.BorderLayout(2, 2));

        plot.getPlot().setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 120, 120)));
        plot.getPlot().setLayout(new java.awt.FlowLayout(0, 5, 1));
        PCA.add(plot.getPlot(), java.awt.BorderLayout.CENTER);

        southPanel.setLayout(new java.awt.GridLayout(1, 0, 2, 2));

        variancePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 120, 120)));
        variancePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                variancePanelComponentResized(evt);
            }
        });
        variancePanel.setLayout(new java.awt.GridBagLayout());

        varx.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        varx.setText("Varx");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        variancePanel.add(varx, gridBagConstraints);

        vary.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        vary.setText("Vary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        variancePanel.add(vary, gridBagConstraints);

        vartot.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        vartot.setText("Tot var");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        variancePanel.add(vartot, gridBagConstraints);

        southPanel.add(variancePanel);
        PCA.add(southPanel, java.awt.BorderLayout.SOUTH);
        jPanel1.add(PCA, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(204, 204, 214));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        location.setText("X:0 Y:0");
        jPanel2.add(location);

        windowsize.setText("Plot Size");
        jPanel2.add(windowsize);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        jEPanel1.setLColor(new java.awt.Color(215, 215, 255));
        jEPanel1.setUColor(new java.awt.Color(225, 225, 225));
        jEPanel1.setLayout(new java.awt.BorderLayout());

        ButtonBar.setLayout(new javax.swing.BoxLayout(ButtonBar, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("  ");
        ButtonBar.add(jLabel1);

        b2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save.gif"))); // NOI18N
        b2.setToolTipText("Save Image");
        b2.setActionCommand("save");
        b2.setBorder(null);
        b2.setBorderPainted(false);
        b2.setContentAreaFilled(false);
        b2.setMaximumSize(new java.awt.Dimension(22, 22));
        b2.setMinimumSize(new java.awt.Dimension(22, 22));
        b2.setPreferredSize(new java.awt.Dimension(22, 22));
        ButtonBar.add(b2);

        b3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/printer.gif"))); // NOI18N
        b3.setToolTipText("Print Image");
        b3.setActionCommand("print");
        b3.setBorder(null);
        b3.setBorderPainted(false);
        b3.setContentAreaFilled(false);
        b3.setMaximumSize(new java.awt.Dimension(22, 22));
        b3.setMinimumSize(new java.awt.Dimension(22, 22));
        b3.setPreferredSize(new java.awt.Dimension(22, 22));
        ButtonBar.add(b3);

        b8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/square.gif"))); // NOI18N
        b8.setToolTipText("Selection style");
        b8.setBorder(null);
        b8.setBorderPainted(false);
        b8.setContentAreaFilled(false);
        b8.setMaximumSize(new java.awt.Dimension(22, 22));
        b8.setMinimumSize(new java.awt.Dimension(22, 22));
        b8.setPreferredSize(new java.awt.Dimension(22, 22));
        b8.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b8ActionPerformed(evt);
            }
        });
        ButtonBar.add(b8);

        b2121.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/framepca.gif"))); // NOI18N
        b2121.setToolTipText("Framecontents to PCA");
        b2121.setActionCommand("zoompca");
        b2121.setBorder(null);
        b2121.setBorderPainted(false);
        b2121.setContentAreaFilled(false);
        b2121.setMaximumSize(new java.awt.Dimension(22, 22));
        b2121.setMinimumSize(new java.awt.Dimension(22, 22));
        b2121.setPreferredSize(new java.awt.Dimension(22, 22));
        ButtonBar.add(b2121);

        b212.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/framechart.gif"))); // NOI18N
        b212.setToolTipText("select clusters");
        b212.setActionCommand("zoomchart");
        b212.setBorder(null);
        b212.setBorderPainted(false);
        b212.setContentAreaFilled(false);
        b212.setMaximumSize(new java.awt.Dimension(22, 22));
        b212.setMinimumSize(new java.awt.Dimension(22, 22));
        b212.setPreferredSize(new java.awt.Dimension(22, 22));
        b212.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b212ActionPerformed(evt);
            }
        });
        ButtonBar.add(b212);

        b213.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/frametotext.gif"))); // NOI18N
        b213.setToolTipText("Select labels");
        b213.setActionCommand("labelchart");
        b213.setBorder(null);
        b213.setBorderPainted(false);
        b213.setContentAreaFilled(false);
        b213.setMaximumSize(new java.awt.Dimension(22, 22));
        b213.setMinimumSize(new java.awt.Dimension(22, 22));
        b213.setPreferredSize(new java.awt.Dimension(22, 22));
        ButtonBar.add(b213);

        b241.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/listselection.gif"))); // NOI18N
        b241.setToolTipText("Toggle Shadow Unselected");
        b241.setActionCommand("selectlist");
        b241.setBorderPainted(false);
        b241.setContentAreaFilled(false);
        b241.setMaximumSize(new java.awt.Dimension(22, 22));
        b241.setMinimumSize(new java.awt.Dimension(22, 22));
        b241.setPreferredSize(new java.awt.Dimension(22, 22));
        ButtonBar.add(b241);
        b21211.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/axis.gif"))); // NOI18N
        b21211.setToolTipText("Set chart axes");
        b21211.setActionCommand("axis");
        b21211.setBorder(null);
        b21211.setBorderPainted(false);
        b21211.setContentAreaFilled(false);
        b21211.setMaximumSize(new java.awt.Dimension(22, 22));
        b21211.setMinimumSize(new java.awt.Dimension(22, 22));
        b21211.setPreferredSize(new java.awt.Dimension(22, 22));
        ButtonBar.add(b21211);

        b211.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/zoomout.gif"))); // NOI18N
        b211.setToolTipText("Zoom out");
        b211.setActionCommand("zoomout");
        b211.setBorder(null);
        b211.setBorderPainted(false);
        b211.setContentAreaFilled(false);
        b211.setMaximumSize(new java.awt.Dimension(22, 22));
        b211.setMinimumSize(new java.awt.Dimension(22, 22));
        b211.setPreferredSize(new java.awt.Dimension(22, 22));
        b211.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b211ActionPerformed(evt);
            }
        });
        ButtonBar.add(b211);


        jEPanel1.add(ButtonBar, java.awt.BorderLayout.NORTH);
        getContentPane().add(jEPanel1, java.awt.BorderLayout.NORTH);



        jMenuBar1.add(jMenu2);

        jMenu21.setText("PCA");

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/listselection.gif"))); // NOI18N
        jMenuItem4.setText("Shadow unselected");
        jMenuItem4.setActionCommand("selectlist");
        jMenu21.add(jMenuItem4);

        m2211.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        m2211.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/axis.gif"))); // NOI18N
        m2211.setText("Set chart axes");
        m2211.setActionCommand("axis");
        jMenu21.add(m2211);

        m19.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        m19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/frametotext.gif"))); // NOI18N
        m19.setText("Clear plot text");
        m19.setActionCommand("clearText");
        jMenu21.add(m19);

        Variance.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        Variance.setSelected(true);
        Variance.setText("Show variance");
        Variance.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                VarianceItemStateChanged(evt);
            }
        });
        Variance.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VarianceActionPerformed(evt);
            }
        });
        jMenu21.add(Variance);
        tooltip.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        tooltip.setSelected(true);
        tooltip.setText("Show tooltip");
        tooltip.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tooltipItemStateChanged(evt);
            }
        });
        jMenu21.add(tooltip);

        jMenuBar1.add(jMenu21);

        jMenu12.setText("Help");

        m81.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        m81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bquestion.gif"))); // NOI18N
        m81.setText("Principal Component Analysis");
        jMenu12.add(m81);
        jMenuBar1.add(jMenu12);
        setJMenuBar(jMenuBar1);
        pack();
    }// </editor-fold>       

    private void okActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
    }

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {
        axis.setVisible(false);
    }

    private void YaxisActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
    }

    private void ZaxisActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
    }

    private void XaxisActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
    }

    private void b212ActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
    }

    private void b211ActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
    }

    private void VarianceActionPerformed(java.awt.event.ActionEvent evt) {

        if (!Variance.isSelected()) {
            southPanel.remove(variancePanel);
        } else {
            southPanel.add(variancePanel);
        }

        southPanel.revalidate();//.doLayout();
        southPanel.repaint();
    }

    private void variancePanelComponentResized(java.awt.event.ComponentEvent evt) {

        boolean ok = false;
        int fontSize = 12;

        while (!ok) {

            java.awt.Font smaller = new java.awt.Font("ARIAL", 0, fontSize);
            varx.setFont(smaller);
            vary.setFont(smaller);
            vartot.setFont(smaller);

            if (variancePanel.getWidth() < varx.getPreferredSize().width || variancePanel.getWidth() < vary.getPreferredSize().width || variancePanel.getWidth() < vartot.getPreferredSize().width) {
                ok = false;
            } else {
                ok = true;
            }

            fontSize--;
        }
    }

    private void lassoActionPerformed(java.awt.event.ActionEvent evt) {
        plot.setframeType(1);
        b8.setIcon(((javax.swing.JMenuItem) evt.getSource()).getIcon());
    }

    private void squareActionPerformed(java.awt.event.ActionEvent evt) {
        plot.setframeType(0);
        b8.setIcon(((javax.swing.JMenuItem) evt.getSource()).getIcon());
    }

    private void nofillActionPerformed(java.awt.event.ActionEvent evt) {
        plot.setFrameBG(Color.white);
        plot.forceFullRepaint();
    }

    private void grayActionPerformed(java.awt.event.ActionEvent evt) {
        plot.setFrameBG(Color.gray);
    }

    private void blueActionPerformed(java.awt.event.ActionEvent evt) {
        plot.setFrameBG(Color.blue);
    }

    private void greenActionPerformed(java.awt.event.ActionEvent evt) {
        plot.setFrameBG(Color.green);
    }

    private void redActionPerformed(java.awt.event.ActionEvent evt) {
        plot.setFrameBG(Color.red);
    }

    private void b8ActionPerformed(java.awt.event.ActionEvent evt) {
        selectpop.show((javax.swing.JButton) evt.getSource(), 0, 0);
    }

    private void tooltipItemStateChanged(java.awt.event.ItemEvent evt) {
    }

    private void VarianceItemStateChanged(java.awt.event.ItemEvent evt) {
    }
    public javax.swing.JPanel ButtonBar;
    public javax.swing.JPanel PCA;
    public javax.swing.JCheckBoxMenuItem Variance;
    public javax.swing.JComboBox Xaxis;
    public javax.swing.JComboBox Yaxis;
    public javax.swing.JComboBox Zaxis;
    public javax.swing.JDialog axis;
    public javax.swing.JButton b15;
    public javax.swing.JButton b2;
    public javax.swing.JButton b211;
    public javax.swing.JButton b212;
    public javax.swing.JButton b2121;
    public javax.swing.JButton b21211;
    public javax.swing.JButton b213;
    public javax.swing.JButton b241;
    public javax.swing.JButton b3;
    public javax.swing.JButton b8;
    public no.uib.jexpress_modularized.core.visualization.ColorButton backSV;
    public javax.swing.JRadioButtonMenuItem blue;
    private javax.swing.JPanel buttonpanel;
    public javax.swing.JButton close;
    public javax.swing.JLabel colorscale;
    public javax.swing.JCheckBox fogSV;
    public javax.swing.JRadioButtonMenuItem gray;
    public javax.swing.JRadioButtonMenuItem green;
    private no.uib.jexpress_modularized.core.visualization.JEPanel jEPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel311;
    private javax.swing.JLabel jLabel312;
    public javax.swing.JMenu jMenu12;
    public javax.swing.JMenu jMenu2;
    public javax.swing.JMenu jMenu21;
    public javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    public javax.swing.JMenuItem lasso;
    public javax.swing.JLabel location;
    public javax.swing.JMenuItem m19;
    public javax.swing.JMenuItem m2211;
    public javax.swing.JMenuItem m6;
    public javax.swing.JMenuItem m81;
    private javax.swing.ButtonGroup mask;
    public javax.swing.JRadioButtonMenuItem nofill;
    public javax.swing.JButton ok;
    private PCA2DComponent plot;
    public javax.swing.JRadioButtonMenuItem red;
    public javax.swing.JPopupMenu selectpop;
    public javax.swing.JPanel southPanel;
    public javax.swing.JMenuItem square;
    public javax.swing.JCheckBoxMenuItem tooltip;
    public javax.swing.JPanel variancePanel;
    public javax.swing.JLabel vartot;
    public javax.swing.JLabel varx;
    public javax.swing.JLabel vary;
    public javax.swing.JLabel windowsize;
    // End of variables declaration    

    public void initListeners() {
        bFramer.addBar(ButtonBar);
    }

    public void initOther() {

        if (!Variance.isSelected()) {
            southPanel.remove(variancePanel);
        } else {
            southPanel.add(variancePanel);
        }

        southPanel.revalidate();
        southPanel.repaint();

    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        //NOTE: println methods-calls used to dinstinguish which button is which. Only used for debugging and 
        //investigating code.

        //NOTE: when this button is pressed, a NullPointerException is thrown - 19.06.12
        if (e.getActionCommand().equals("zoomout")) {
            //NOTE: print for seeing which button is which: remove later
            plot.zoomout();
        }

        if (e.getActionCommand().equals("zoompca")) {
            plot.getPlot().zoompca = true;
            plot.setPaintNamesonClick(false);
            plot.getPlot().paintNamesonClick = true;
        }


        if (e.getActionCommand().equals("clearText")) {
            //NOTE: print for seeing which button is which: remove later
            java.util.Arrays.fill(SpotNames, null);
            plot.forceFullRepaint();
        }


        if (e.getActionCommand().equals("zoomchart")) {
            //NOTE: print for seeing which button is which: remove later
            plot.setZoomPca(false);
            plot.setPaintNamesonClick(false);
            //TODO: see previous comment about properties
        }
        if (e.getActionCommand().equals("labelchart")) {
            //NOTE: print for seeing which button is which: remove later
            plot.setPaintNamesonClick(true);
            plot.setZoomPca(false);
        }



        if (e.getActionCommand().equals("axis")) {
            //NOTE: print for seeing which button is which: remove later
            axis.pack();
            axis.setLocation(b21211.getLocationOnScreen());
            axis.setVisible(true);
        }


        if (e.getActionCommand().equals("print")) {
            //NOTE: print for seeing which button is which: remove later
            System.out.println("action: print");
        } else if (e.getActionCommand().equals("save")) {
            //NOTE: print for seeing which button is which: remove later
            System.out.println("action: save");
        }

        if (e.getActionCommand().equals("savexy")) {

            //NOTE: print for seeing which button is which: remove later
            if (pcaResults == null) {
                return;
            }
        } else if (e.getActionCommand().equals("selectlist")) {
            //NOTE: print for seeing which button is which: remove later
            if (shadowUnselected) {
                shadowUnselected = false;
            } else {
                shadowUnselected = true;
            }

            plot.setShadowUnselected(shadowUnselected);
            if (shadowUnselected == false) {
                plot.setNotShaded(plot.getSelectedIndexes());
            } else {
                plot.setNotShaded(null);

            }
            plot.forceFullRepaint();

        }

    }
    private boolean shadowUnselected = false;

    /**
     * Initialize the GUI.
     */
    private void lay() {
        if (!showPCA) {
            return;
        }
        plot.getPlot().sizeMonitor = windowsize;

    }
}
