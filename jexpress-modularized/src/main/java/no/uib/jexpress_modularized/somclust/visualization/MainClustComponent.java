
/*--------------------------------------------------------------------------
 *
 * Copyright (c) 2001 MolMine AS.  All rights reserved.
 *
 * All paper, computer, digital, graphical, or other representations of the source code remain
 * the property of MolMine AS. All patents, ideas, inventions or novelties contained within the
 * source code are the exclusive intellectual property of MolMine AS. Surce code is provided
 * for reference and support purposes only. Copies of the source code in any form, whether this
 * be digital, graphical or any other media, may not be distributed, discussed, or revealed to
 * any person, computer or organisation not directly involved in support of a related product
 * provided by the licensee or organisation not authorzed by MolMine AS to be directly involved
 * in source code level support of J-Express.
 
 * The source code may not be modified except where specifically authorized by MolMine AS. No
 * part of the source code may be used  within any product other than J-Express.
 *
 * You undertake that:
 *  The source code will not be distributed except where specifical authorized by MolMine AS.
 *  That you will ensure that all copies and representations of the source code can be identified.
 * 
 * DISCLAIMER:
 * THIS SOFTWARE IS PROVIDED BY MOLMINE AS "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE  ARE DISCLAIMED.  IN NO EVENT SHALL MOLMINE OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *---------------------------------------------------------------------------
 */
package no.uib.jexpress_modularized.somclust.visualization;

import no.uib.jexpress_modularized.somclust.model.ClusterResults;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters;
import no.uib.jexpress_modularized.somclust.model.Node;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import javax.swing.*;
import no.uib.jexpress_modularized.core.computation.JMath;
import no.uib.jexpress_modularized.core.dataset.AnnotationLibrary;
import no.uib.jexpress_modularized.core.dataset.AnnotationManager;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.dataset.Group;
import no.uib.jexpress_modularized.somclust.model.ClusterSettings;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters.LINKAGE;
import no.uib.jexpress_modularized.core.model.Selection;
import no.uib.jexpress_modularized.core.model.SelectionManager;
import no.uib.jexpress_modularized.core.model.ModularizedListener;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactory;
import no.uib.jexpress_modularized.core.visualization.colors.ui.ScaleAndAxis;
import no.uib.jexpress_modularized.core.visualization.SwingWorker;
import no.uib.jexpress_modularized.core.visualization.TreeView;
import no.uib.jexpress_modularized.core.visualization.documentation.MetaInfoList;
import no.uib.jexpress_modularized.core.visualization.documentation.MetaInfoNode;
import no.uib.jexpress_modularized.core.visualization.ErrorMsg;
import no.uib.jexpress_modularized.core.visualization.Scrollpic;
import no.uib.jexpress_modularized.core.visualization.Tools;
import no.uib.jexpress_modularized.core.visualization.colors.colorcomponents.ColorFactoryList;
import org.freehep.util.export.ExportDialog;

/**
 * This is the class that performs hierarchical clustering and produces a
 * dendrogram. It also handles all events related to this component.
 */
/*
 * MetaInfoCreator:H. Clustering Algorithm Version:1.0 Description of Method:
 * This is the Hierarchical Clustering component in J-Express. See more info in
 * the J-Express Pro 2.0 Manual at www.molmine.com -> download -> manual
 * Parameters: Distance Metric: The clustering distance metric to use. Cluster
 * Columns: If yes, the columns will also be clustered Twig Type: Either
 * weighted or non-weighted. Tree Height: The height of the column cluster
 * dendrogram. Tree Width: The width of the row cluster dendrogram Vertical
 * Distances: The height of each row in the heat map or table Horizontal
 * Distances: The Width of each column in the heat map. Column indexes: An array
 * of comma separated integers defining the columns extracted Row indexes: An
 * array of comma separated integers defining the rows extracted
 */
public class MainClustComponent extends ModularizedListener implements ClipboardOwner, Serializable {

    private boolean db = true;
    int minarrayupdates = 0;
    private SaveComponent Sc = new SaveComponent();
    private int distance = 2;   //the distance measure used (see expresscomponents.JMath.java)
    private boolean TOPREADY = false;
    private boolean LEFTREADY = false;
    public boolean paintInfo = true;
    public boolean paintScale = true;
    public boolean paintSquares = true;
    public boolean paintTable = true;
    public boolean paintTop = true;
    public boolean paintGrid = true;
    public boolean paintDendrogram = true;
    public Color bgCol = Color.WHITE;
    public Color tableHDCol = Color.LIGHT_GRAY;
    public Color GridCol = Color.DARK_GRAY;
    private Color markColor = Color.red;
    public boolean autoSelection = true;
    private int squareW = 12;
    private int squareL = 2;
    private boolean ClusterColumns = true;
    private int LeftTreeWidth = 200;
    private int TopTreeHeight = 70;
    private boolean ValueDistances = true;
    private String metric;

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
    public LINKAGE LINK = LINKAGE.WPGMA;
    private int[] Wd;
    private int[] WdSUM;
    private Point tableLastPaintedAt;
    private Point SquaresLastPaintedAt;
    private java.text.NumberFormat numformat;
    private Vector draggedOverIndices = new Vector();
    private Selection SelectedRows = new Selection(Selection.TYPE.OF_ROWS);
    private int startSelectionIndex = -1;
    public float[][] tmpDist;
    private static int ROWS = 0;
    private static int COLUMNS = 1;
    private int clusterWay = 0;
    private boolean distanceClustering = false;
    private boolean distanceMatrixOnly = false;
    public boolean transposed = false;
    public boolean logColors = true; //Just testing how log dist works on the color scale.. (070502)
    private boolean openDisplay = true;
    private DefaultListSelectionModel selmodel = new DefaultListSelectionModel();
    private boolean gengenscale = false;
    int imageHandle = 0;   //0 = save, 1 = print.
    Node[] gendata;
    Node[] topgendata;
    private Node marked = null;
    boolean[] selection;
    boolean[] stepovergen;
    int numgenes, totgenes;
    Dataset subdata;
    int topOffset = -10;   //Used to create a space in the dendrogram so that it starts in the same height as the table.
    boolean antialias = false;
    DendrogramDialog cd;
    int datalength; //If some data has been deleted, this will be smaller than data.getDataLength().
    boolean justrepaint;
    int cutted = 0;
    public JMath jmath = new JMath();
    int mouseclickmode = 0;
    Color background = Color.white;
    int leftnameswidth = 100;
    int topnamesheight = 0;
    LINKAGE link;
    boolean zoom = false;
    boolean fromtree = false;
    TreeView upper, left;
    Image leftim, topim;
    double max, min;
    Font font = getTableFont(12);
    //int genenumber;	//used to count the number of rows..
    Scrollpic tree;
    Node trunk, clickedgen, maintrunk, root, toproot;
    int[] sortpan;
    float[] sortpan2;
    //Number of zoomed to display on the tab.
    int zoomnum = 1;
    Dataset FullDataSet;    //The complete dataset (if this dendrogram is zoomed, FullDataSet will contain the elements of the parent dendrogram).
    //Dialog saying 'drawing'
    float[][] dist; //distance matrix
    int mode = 0;
    MainClustComponent zoommainc;
    MainClustComponent parent;
    Node lastColored, lastTopColored = null;
    Color lastColoredColor;
    int topw = 0, toph = 0;
    int rightw = 0, righth = 0;
    int squaresw = 0, squaresh = 0;
    int infow = 0, infoh = 0;
    int setsw = 0, setsh = 0;
    int setHw = 0, setHh = 0;
    int leftimw = 0, leftimh = 0;
    int topimw = 0, topimh = 0;
    //Need to keep track of the buttons above the rightTable..
    String metainfo = "";
    private boolean clipTree = false;
    private boolean groupTree = false;
    private Rectangle TableWidth;
    MetaInfoList metalist = new MetaInfoList();

    public void setMarkColor(Color markColor) {
        this.markColor = markColor;
    }

    public void SetClipSubset(boolean clipTree) {
        this.clipTree = clipTree;
    }

    public void SetGroupSubset(boolean groupTree) {
        this.groupTree = groupTree;
    }

    public Node[] buildmatrix(float[][] dist) {
        Node[] node = new Node[dist.length];

        stepovergen = new boolean[dist.length];
        numgenes = dist.length;
        for (int i = 0; i < dist.length; i++) {
            node[i] = new Node(i);
        }

        // Set up the sorting matrix
        sortpan = new int[dist.length];
        sortpan2 = new float[dist.length];
        return node;
    }

    public Dataset getSubDataSet(Node g, boolean leftTree) {
        Dataset ret = null;
        Tools t = new Tools();

        Stack st = new Stack();
        Vector v = new Vector();
        g.fillMembers(v, st);

        t.sort(v);
        return ret;

    }

    //THE NEW VERSION!
    public MainClustComponent(Dataset data, ClusterParameters params, ClusterResults results) {
        classtype = 2;
        distance = params.getDistance();
        jmath.setMetric(distance);
        LINK = params.getLink();
        parent = this;
        root = results.getRowDendrogramRootNode();
        toproot = results.getColumnDendrogramRootNode();
        this.trunk = root;
        FullDataSet = data;
        this.data = data;
        gocluster();

        numformat = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);
        numformat.setMaximumFractionDigits(3);
        numformat.setMinimumFractionDigits(1);

        clusterWay = ROWS;
        FullDataSet = data;
        Sc = new SaveComponent();
        Sc.setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        JScrollPane sp = new JScrollPane(Sc);
        this.add(sp);
        sp.getVerticalScrollBar().setUnitIncrement(squareL);
        addListeners();
        components.add(MainClustComponent.this);
        SelectionManager.getSelectionManager().addSelectionChangeListener(data, MainClustComponent.this);

    }

    private void addListeners() {
        Sc.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                boolean needrepaint = false;
                if (!(LEFTREADY && TOPREADY)) {
                    return;
                }
                if (SquaresLastPaintedAt == null) {
                    return;
                }
                if (!autoSelection) {
                    return;
                }

                if (marked != null) {
                    marked.mark = false;
                    needrepaint = true;
                    marked = null;
                }

                if (e.getY() < SquaresLastPaintedAt.y) {
                    Node gn2 = getNodeAt(e.getPoint().y, e.getPoint().x - 4 - SquaresLastPaintedAt.x, toproot);

                    if (gn2 != null) {
                        Sc.setToolTipText("Merged at " + numformat.format(gn2.value) + " ; Nodes: " + gn2.members);
                        needrepaint = true;
                        gn2.mark = true;
                        if (marked != null && marked != gn2) {
                            marked.mark = false;
                        }
                        marked = gn2;
                    }

                }

                if (e.getX() < SquaresLastPaintedAt.x) {
                    Node gn = getNodeAt(e.getPoint().x - 4, e.getPoint().y - 4 - tableLastPaintedAt.y, trunk);



                    if (gn != null) {
                        gn.mark = true;
                        if (marked != null && marked != gn) {
                            marked.mark = false;
                        }
                        marked = gn;
                        needrepaint = true;
                        SelectedRows.clear();
                        Sc.setToolTipText("Merged at " + numformat.format(gn.value) + " ; Nodes: " + gn.members);
                        //gn.setColor( markColor );    
                        Stack st = new Stack();
                        Vector v = new Vector();
                        gn.fillMembers(v, st);

                        for (int i = 0; i < v.size(); i++) {
                            SelectedRows.addMember((Integer) v.elementAt(i));
                        }

                        SelectionManager m = SelectionManager.getSelectionManager();
                        m.setSelectedRows(data, SelectedRows);
                        needrepaint = false;
                        MainClustComponent.this.requestFocusInWindow();
                    }

                } else {
                    String s = getColAnnotation(e.getX(), e.getY());
                    if (s != null) {
                        Sc.setToolTipText(s);
                    }
                }

                if (needrepaint) {
                    Sc.repaint();
                }

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseDrag(e.getPoint());
            }
        });


        Sc.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startSelectionIndex = -1;
                draggedOverIndices.clear();
                if (!e.isControlDown()) {
                    SelectionManager sm = SelectionManager.getSelectionManager();
                    sm.setSelectedRows(data, SelectedRows);
                }
                MainClustComponent.this.requestFocusInWindow();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                Node gn = getNodeAt(e.getPoint().x - 4, e.getPoint().y - 4 - tableLastPaintedAt.y, trunk);
                Node gn2 = getNodeAt(e.getPoint().y, e.getPoint().x - 4 - SquaresLastPaintedAt.x, toproot);

                if (gn != null) {
                    if (clipTree) {
                        Dataset clipped = getSubDataSet(gn, true);
                    } else if (groupTree) {
                        Tools t = new Tools();
                        Stack st = new Stack();
                        Vector v = new Vector();
                        gn.fillMembers(v, st);
                        t.sort(v);

                        boolean[] indices = new boolean[data.getDataLength()];
                        for (int i = 0; i < v.size(); i++) {
                            int k = ((Integer) v.elementAt(i)).intValue();
                            indices[k] = true;
                        }

                    } else {
                        gn.setColor(markColor);
                    }
                }

                if (gn2 != null) {
                    if (clipTree) {
                        Dataset clipped = getSubDataSet(gn2, false);
                    } else if (groupTree) {
                        Tools t = new Tools();
                        Stack st = new Stack();
                        Vector v = new Vector();
                        gn2.fillMembers(v, st);
                        t.sort(v);

                        boolean[] indices = new boolean[data.getDataWidth()];
                        for (int i = 0; i < v.size(); i++) {
                            int k = ((Integer) v.elementAt(i)).intValue();
                            indices[k] = true;
                        }
                    } else {
                        gn2.setColor(markColor);
                    }
                } else {
                    startSelectionIndex = -1;
                    draggedOverIndices.clear();
                    if (!e.isControlDown()) {
                        SelectionManager sm = SelectionManager.getSelectionManager();
                        sm.setSelectedRows(data, SelectedRows);
                    }
                    MainClustComponent.this.requestFocusInWindow();
                }

                Sc.repaint();
            }
        });





    }

    public boolean controlMem() {

        long freemem = java.lang.Runtime.getRuntime().freeMemory();
        long maxmem = java.lang.Runtime.getRuntime().maxMemory();
        long tot = java.lang.Runtime.getRuntime().totalMemory();

        long avail = tot - freemem;

        long max = (maxmem - avail) / 1000000l;


        long req = 0;

        if (clusterWay == ROWS) {
            req = data.getDataLength();
            req = req * req * 4;
            req = req / 2;
        } else {
            req = data.getDataWidth();
            req = req * req * 4;
            req = req / 2;
        }


        req = req / 1000000;


        if (max < (req + 20)) {

            if (JOptionPane.showConfirmDialog(null, "The Amount of memory required for performing this clustering is predicted to be ca " + req + "MB " + "but only " + max + " MB is available\n(See help on memory settings to increase available resources or use filtering or another method prior to hierarchical clustering)\nContinue anyway?", "Warning", JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
                return true;
            } else {

                if (cd != null) {
                    cd.setVisible(false);
                    cd.dispose();
                }
                return false;
            }
        } else {
            return true;
        }



    }

    public Vector getindexes(Node root, boolean isleft) {

        Vector v = new Vector();

        if (isleft) {
            for (int i = 0; i < left.actualLength; i++) {
                v.add(new Integer(left.arrangement[i]));
            }
        } else if (upper == null) {
            for (int i = 0; i < FullDataSet.getDataWidth(); i++) {
                v.add(new Integer(i));
            }
        } else {
            for (int i = 0; i < upper.actualLength; i++) {
                v.add(new Integer(upper.arrangement[i]));
            }
        }

        return v;
    }

    public void gocluster() {
        root = trunk;

        int verticalItems = countgenes(root);

        left = new TreeView(root, FullDataSet.getDataLength(), getBackground(), Color.black);
        left.leafdist = squareL;
        left.actualLength = verticalItems;
        left.leftmargin = (int) Math.round(squareL / 2);
        left.drawframe = false;
        left.valuedistances = true;
        left.rightmargin = 0;
        left.drawrects = false;
        left.bottommargin = 0;
        //if(result!=null)
        left.treewidth = LeftTreeWidth;
        left.generatecoords();

        LEFTREADY = true;
        if (toproot != null) {
            int horizontalItems = countgenes(toproot);
            upper = new TreeView(toproot, horizontalItems, getBackground(), Color.black);

            upper.leafdist = squareW;
            upper.actualLength = horizontalItems;
            upper.horizontal = false;
            upper.leftmargin = (int) Math.round(squareW / 2);
            upper.drawframe = false;
            upper.valuedistances = ValueDistances;
            upper.topmargin = 0;
            upper.drawrects = false;
            upper.bottommargin = 0;
            upper.rightmargin = 0;
            upper.treewidth = TopTreeHeight;
        }

        TOPREADY = true;

        if (ClusterColumns && upper != null) {
            int[] tmp = new int[upper.arrangement.length];
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = i;
            }
            upper.arrangement = tmp;
        }

        if (ClusterColumns && upper != null && squareW > 0) {
            topim = (Image) upper.getImage();
        }
        max = getMaximum();
        min = getMinimum();

        dist = null;
        Sc.setBounds(0, 0, Sc.getPreferredSize().width, Sc.getPreferredSize().height);

        Sc.revalidate();
        Sc.repaint();

        this.revalidate();
        System.gc();
    }

    public double getMinimum() {
        return -Math.max(Math.abs(FullDataSet.getMinMeasurement()), Math.abs(FullDataSet.getMaxMeasurement()));
    }

    public double getMaximum() {
        return Math.max(Math.abs(FullDataSet.getMinMeasurement()), Math.abs(FullDataSet.getMaxMeasurement()));
    }

    public double dist(double v1, double v2) {
        double temp = 0;
        double ret = 0;
        if (v1 < v2) {
            temp = v1;
            v1 = v2;
            v2 = temp;
        }
        ret = v1 - v2;
        return (ret);

    }

    public Node makeclusters(boolean paint, boolean transposed, float[][] distance) {
        long diff = 0l;
        Node ret = null;
        gendata = buildmatrix(distance);
        totgenes = numgenes;
        makeminarray(distance);
        int linkage = 0;
        if (LINK == LINKAGE.SINGLE) {
            linkage = 0;
        }
        if (LINK == LINKAGE.COMPLETE) {
            linkage = 3;
        }
        if (LINK == LINKAGE.WPGMA) {
            linkage = 1;
        }
        if (LINK == LINKAGE.UPGMA) {
            linkage = 2;
        }

        while (numgenes > 1) {

            //This one takes time!
            gendata = makeclusters(gendata, distance, linkage);

        }
        if (!transposed) {
            for (int i = 0; i < data.getDataLength(); i++) {
                if (!stepovergen[i]) {
                    ret = gendata[i];
                    root = gendata[i];
                    maintrunk = gendata[i];
                }
            }
        } else {
            for (int i = 0; i < data.getDataWidth(); i++) {
                if (!stepovergen[i]) {
                    ret = gendata[i];
                    root = gendata[i];
                    maintrunk = gendata[i];
                }
            }
        }

        ret.normalizeMergedValues();

        return ret;
    }

    public void copySelectionText() {

        if (SelectedRows.size() == 0) {
            return;
        }

        AnnotationManager m = AnnotationManager.getAnnotationManager();
        Set<String> annNames = data.getRowAnnotationNamesInUse();
        if (annNames == null) {
            annNames = m.getManagedRowAnnotationNames();
        }

        StringBuilder sb = new StringBuilder();
        boolean addTab = false;
        for (String annName : annNames) {
            if (addTab) {
                sb.append("\t");
            }
            addTab = true;
            sb.append(annName);
        }

        sb.append("\n");

        for (int i = 0; i < SelectedRows.size(); i++) {
            addTab = false;
            if (i > 0) {
                sb.append("\n");
            }

            for (String annName : annNames) {
                if (addTab) {
                    sb.append("\t");
                }
                addTab = true;
                String rowId = data.getRowIds()[SelectedRows.getMembers()[i]];
                sb.append(m.getRowAnnotations(annName).getAnnotation(rowId));
            }
        }

        StringSelection stringSelection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, MainClustComponent.this);
    }
    /*
     * not used? public void updateSelection() { // int[]
     * tst=result.rightTable.getMappedSelectedRows();//getSelectedRows() ;
     * //data.removeSelectionListener(this); // data.setSelectedRows(tst);
     *
     * SelectionManager.getSelectionManager().selectionChangedEvent(data);
     * //data.fireSelectionChangeEvent(this); //data.addSelectionListener(this);
     * }
     */

    public void saveImage() {
        SaveComponent c = new SaveComponent();
         javax.swing.JFrame f = new javax.swing.JFrame("test");
         f.getContentPane().add(c);
         ExportDialog export = new ExportDialog();
         export.showExportDialog( f, "Export view as ...", c, "export" );
         f.dispose();
    }

    public void copyImage() {

        SaveComponent c = new SaveComponent();
        javax.swing.JFrame f = new javax.swing.JFrame("test");
        f.getContentPane().add(c);
        f.pack();

    }

    public void openClusterWindow() {
        SaveComponent c = new SaveComponent();

        final javax.swing.JInternalFrame f = new javax.swing.JInternalFrame("Cluster");
        f.setClosable(true);
        f.setResizable(true);
        f.setIconifiable(true);

        JScrollPane sc = new JScrollPane(c);
        f.getContentPane().add(sc);

        JButton b = new JButton("Close");
        f.getContentPane().add("South", b);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                f.dispose();
            }
        });
        f.pack();
        f.setSize(new Dimension(300, 400));
        f.setLocation(200, 200);
        f.show();

    }

    class SaveComponent extends javax.swing.JComponent implements java.awt.print.Printable {

        Rectangle top, upperr, squares, leftr, table, info, scale, sets, setHeaders;

        @Override
        public Dimension getSize() {
            Dimension d = getPreferredSize();

            if (d.width > 0 && d.height > 0) {
                return d;
            } else {
                return new Dimension(300, 500);
            }
        }

        public Rectangle getTop() {
            return top;
        }

        public Rectangle getUpper() {
            return upperr;
        }

        public Rectangle getSquares() {
            return squares;
        }

        public Rectangle getLeft() {
            return leftr;
        }

        public Rectangle getTable() {
            return table;
        }

        @Override
        public Dimension getPreferredSize() {

            if (left == null) {
                return new Dimension(500, 600);
            }

            if (left != null && (!openDisplay || paintDendrogram)) {
                leftr = new Rectangle(left.getWidth(), left.getHeight());
            }
            if (upper != null && (!openDisplay || paintSquares)) {
                upperr = new Rectangle(upper.getWidth(), upper.getHeight());
            }
            if (!openDisplay || paintInfo) {
                info = getInfoBounds(this.getGraphics());
            }
            if (!openDisplay || paintScale) {
                scale = getScaleBounds();
            }

            //remember to set correct table size here
            if (upperr != null && upperr.height == 0 && (!openDisplay || paintTable)) {
            }

            if (!openDisplay || paintTop) {
                top = getTopBounds();
                setHeaders = getSetHeaderBounds();
            }

            if (!openDisplay || paintSquares) {
                squares = getSquaresBounds();
                sets = getSetBounds();
            }


            if (!openDisplay || paintTable) {
                table = getTableWidth();
            }

            if (!openDisplay || paintInfo) {
                info = getInfoBounds(this.getGraphics());
            }
            if (!openDisplay || paintScale) {
                scale = getScaleBounds();
            }

            int maxtop = 100;

            if (left != null) {
                maxtop = Math.max(squares.height, leftr.height);
                maxtop = Math.max(maxtop, getTableWidth().height);
            }

            if (info == null) {
                info = new Rectangle(0, 0, 0, 0);
            }
            if (scale == null) {
                scale = new Rectangle(0, 0, 0, 0);
            }
            if (table == null) {
                table = new Rectangle(0, 0, 0, 0);
            }
            if (upperr == null) {
                upperr = new Rectangle(0, 0, 0, 0);
            }

            int maxbottom = Math.max(info.height, scale.height);

            int width = 0;

            if (left != null) {
                width = leftr.width + squares.width + sets.width + table.width;
            }
            int height = upperr.height + top.height + maxtop + maxbottom + 5 + 45;

            Dimension d = new Dimension(width, height);
            return d;
        }

        @Override
        public void paint(Graphics g) {

            if (!(TOPREADY && LEFTREADY)) {
                return;
            }

            drawImage(g);
            if (this.getBorder() != null) {
                this.getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());

            }
        }

        @Override
        public int print(Graphics graphics, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {
            graphics.setColor(Color.red);
            graphics.drawLine(0, 0, 100, 100);
            return this.PAGE_EXISTS;
        }
    }

    public javax.swing.JComponent getSaveComponent() {
        return new SaveComponent();
    }

    public void printim() {
    }

    public Image paintInfo() {

        if (!paintInfo) {
            return null;
        }
        BufferedImage nfo = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics infgr = nfo.getGraphics();

        Rectangle bounds = getInfoBounds(infgr);

        nfo = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
        infgr = nfo.getGraphics();
        infgr.setColor(getBackground());
        infgr.fillRect(0, 0, nfo.getWidth(), nfo.getHeight());
        drawInfo(infgr, new Point(0, 0));
        return nfo;
    }

    public Rectangle getInfoBounds(Graphics gr) {

        if (gr == null) {
            BufferedImage tmp = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            gr = tmp.getGraphics();

        }

        if (antialias) {
            Graphics2D g2 = (Graphics2D) gr;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        String metr = "";
        if (parent != null) {
            metr = parent.jmath.getMetricsString();
        } else {
            metr = jmath.getMetricsString();
        }

        String metric = "Distance metrics: " + metr;
        String clusmethod = "Linkage: " + LINK.toString();

        int dmetw = gr.getFontMetrics().stringWidth(metric);//(int)getFont().getStringBounds(metric,fr).getWidth();
        int methw = gr.getFontMetrics().stringWidth(clusmethod);// (int)getFont().getStringBounds(clusmethod,fr).getWidth(); //infm.stringWidth(clusmethod);

        return new Rectangle(Math.max(dmetw, methw) + 8, 30);

    }

    public void drawInfo(Graphics infgr, Point start) {

        Rectangle r = new Rectangle(start.x, start.y, 200, 30);

        if (infgr.getClipBounds() != null && !infgr.getClipBounds().intersects(r)) {
            return;
        }

        infgr.translate(start.x, start.y);

        Font f = getTableFont(12);
        //Font f = new Font("Arial",0,12);
        infgr.setFont(f);

        String metr = "";
        if (parent != null) {
            metr = parent.jmath.getMetricsString();
        } else {
            metr = jmath.getMetricsString();
        }

        String metric = "Distance metrics: " + metr;
        String clusmethod = "Linkage: " + LINK.toString();

        if (antialias) {
            Graphics2D g2 = (Graphics2D) infgr;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        java.awt.FontMetrics infm = infgr.getFontMetrics();
        int dmetw = infm.stringWidth(metric);
        int methw = infm.stringWidth(clusmethod);

        infgr.setColor(Color.black);
        infgr.drawString(metric, 8, 10);
        infgr.drawString(clusmethod, 8, 24);

        infgr.translate(-start.x, -start.y);

    }

    public Image paintTop() {
        Rectangle bounds = getTopBounds();

        BufferedImage top = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics topgr = top.getGraphics();
        topgr.setColor(getBackground());
        topgr.fillRect(0, 0, top.getWidth(), top.getHeight());
        drawTop(topgr, new Point(0, 0));
        return top;
    }

    private Rectangle getTopBounds() {
        setautotop();
        boolean[] visibleIndexes = createVisibleRowIndexes();
        int[] upperArrangement = null;
        if (upper == null || upper.arrangement == null) {
            upperArrangement = new int[data.getDataWidth()];
            for (int i = 0; i < upperArrangement.length; i++) {
                upperArrangement[i] = i;
            }
        } else {
            upperArrangement = upper.arrangement;
        }

        if (topnamesheight > 0 && squareW > 0) {
            return new Rectangle(upperArrangement.length * squareW + 1, topnamesheight);
        } else {
            return new Rectangle(0, 0);
        }
    }

    public void createGroupOfSelection() {

        int[] sel = new int[draggedOverIndices.size()];
        int[] totSel = null;

        boolean[] selection = new boolean[data.getDataLength()];
        SelectionManager sm = SelectionManager.getSelectionManager();
        Selection s = sm.getSelectedRows(data);
        int[] datsel;
        if (s != null && s.getMembers() != null) {
            datsel = sm.getSelectedRows(data).getMembers();
        } else {
            // if no selection, take all -> datsel[i]=i
            datsel = new int[data.getDataLength()];
            for (int i = 0; i < data.getDataLength();
                    datsel[i] = i++);
        }

        for (int i = 0; i < datsel.length; i++) {
            selection[datsel[i]] = true;
        }


        for (int i = 0; i < draggedOverIndices.size(); i++) {
            selection[((Integer) draggedOverIndices.elementAt(i)).intValue()] = true;
        }
        int selcnt = 0;
        for (int i = 0; i < selection.length; i++) {
            if (selection[i]) {
                selcnt++;
            }
        }
        int[] retsel = new int[selcnt];
        selcnt = 0;
        for (int i = 0; i < selection.length; i++) {
            if (selection[i]) {
                retsel[selcnt] = i;
                selcnt++;
            }
        }

        if (retsel != null && retsel.length != 0 && retsel.length != selection.length) {
            Group g1 = new Group("Created Group " + (data.getRowGroups().size() + 1), generatRandColor(), new Selection(Selection.TYPE.OF_ROWS, retsel));
            g1.setActive(true);
            data.addRowGroup(g1);

        }

        draggedOverIndices.clear();


    }

    private Color generatRandColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;
    }

    public Color[] getIndexedColumnGroupColors() {
        Color[] colors = new Color[data.getDataWidth()];
        java.util.List<Group> groups = data.getColumnGroups();
        for (Group group : groups) {
            if (group.isActive()) {
                Color color = group.getColor();
                for (int j = 0; j < colors.length; j++) {
                    colors[j] = group.hasMember(j) ? color : Color.gray;
                }
            }
        }
        return colors;
    }

    private String[] getColumnAnnotation() {

        return data.getColumnIds();
    }

    private void drawTop(Graphics topnamesgr, Point start) {
        int[] upperArrangement = null;
        if (upper == null || upper.arrangement == null) {
            upperArrangement = new int[data.getDataWidth()];
            for (int i = 0; i < upperArrangement.length; i++) {
                upperArrangement[i] = i;
            }
        } else {
            upperArrangement = upper.arrangement;
        }

        Rectangle view = new Rectangle(999999, 999999);
        if (db) {
            view = this.getTopBounds();
        }
        view.translate(start.x, start.y);

        Rectangle top = new Rectangle(0, 0, upperArrangement.length * squareW, topnamesheight);

        Rectangle topV = new Rectangle(start.x, start.y, upperArrangement.length * squareW, topnamesheight);
        if (view == null || top == null || !view.intersects(topV)) {
            return;
        }
        topnamesgr.translate(start.x, start.y);
        Font f = getTableFont(squareW - 2);
        double rot = Math.PI / 2;
        Graphics2D g2d = (Graphics2D) topnamesgr;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(bgCol);
        g2d.fill(top);
        topnamesgr.setFont(f);
        topnamesgr.setColor(Color.black);
        Color[] groupColors = getIndexedColumnGroupColors();

        int halfFontHeight = topnamesgr.getFontMetrics().getMaxDescent();

        int FontHeight = topnamesgr.getFontMetrics().getAscent() + topnamesgr.getFontMetrics().getDescent();// topnamesgr.getFontMetrics().getHeight();
        int usedSpace = 0;
        int availSpace = squareW;

        String[] colInf = getColumnAnnotation();
        for (int j = 0; j < upperArrangement.length; j++) {
            usedSpace = 0;

            if ((!openDisplay || ClusterColumns) && upper != null) {
                g2d.setColor(groupColors[upperArrangement[j]]);


                int transX = -halfFontHeight + (squareW * (j + 1) + usedSpace);
                int transY = topnamesheight - 2;


                g2d.translate(transX, transY);
                g2d.rotate(-rot);
                g2d.drawString(colInf[upperArrangement[j]], 0, 0);
                g2d.rotate(rot);
                g2d.translate(-transX, -transY);
                usedSpace += FontHeight;

            } else {
                int k = 0;
                int transY = topnamesheight - 2;

                g2d.setColor(groupColors[j]);

                while (FontHeight > 0 && (usedSpace + FontHeight > availSpace - 2)) {
                    FontHeight--;
                    f = getTableFont(FontHeight);
                    topnamesgr.setFont(f);
                }

                halfFontHeight = topnamesgr.getFontMetrics().getMaxDescent();
                int transX = -halfFontHeight + (squareW * (j + 1) + usedSpace);
                g2d.translate(transX, transY);
                g2d.rotate(-rot);

                g2d.drawString(colInf[j], 0, 0);
                g2d.rotate(rot);
                g2d.translate(-transX, -transY);
                usedSpace += FontHeight;
            }
        }



        if (paintGrid) {
            topnamesgr.setColor(GridCol);
            for (int i = 0; i < upperArrangement.length; i++) {
                topnamesgr.drawLine(i * squareW, 0, i * squareW, topnamesheight);
            }

            topnamesgr.drawLine(0, 0, upperArrangement.length * squareW, 0);
            topnamesgr.drawLine(upperArrangement.length * squareW, 0, upperArrangement.length * squareW, topnamesheight);
        }


        topnamesgr.translate(-start.x, -start.y);
    }

    private Rectangle getSetBounds() {
        int numsets = 0;
        int[] upperArrangement = null;

        if (upper == null || upper.arrangement == null) {
            upperArrangement = new int[data.getDataWidth()];
            for (int i = 0; i < upperArrangement.length; i++) {
                upperArrangement[i] = i;
            }
        } else {
            upperArrangement = upper.arrangement;
        }

        for (Group group : FullDataSet.getRowGroups()) {
            if (group.isActive()) {
                numsets++;
            }
        }

        if (left != null && numsets > 0) {
            if (left == null) {
                System.out.print("\nNO LEFT\n");
            }
            if (paintGrid) {
                return new Rectangle((squareW * numsets) + 1, (left.actualLength * squareL) + 1);
            } else {
                return new Rectangle((squareW * numsets), (left.actualLength * squareL) + 1);
            }

        }
        return new Rectangle(0, 0);
    }

    private void drawSets(Graphics sets, Point start, Rectangle bounds) {
        if (left == null) {
            return;
        }
        sets.translate(start.x, start.y);
        if (bounds != null) {
            bounds.translate(-start.x, -start.y);
        }
        int rows = left.actualLength;
        int counter = 0;
        int numsets = 0;
        int setnum = 0;
        String Ntmp = null;
        int setwidth = 0;

        int[] upperArrangement = null;
        if (upper == null || upper.arrangement == null) {
            upperArrangement = new int[data.getDataWidth()];
            for (int i = 0; i < upperArrangement.length; i++) {
                upperArrangement[i] = i;
            }
        } else {
            upperArrangement = upper.arrangement;
        }

        for (Group group : FullDataSet.getRowGroups()) {
            if (group.isActive()) {
                numsets++;
            }
        }

        if (numsets > 0) {

            setwidth = numsets * squareW;
            boolean[] indexes = null;
            Color color = null;

            for (Group group : FullDataSet.getRowGroups()) {
                if (group.isActive()) {
                    Ntmp = group.getName();
                    color = group.getColor();
                    counter = 0;

                    for (int j = 0; j < FullDataSet.getDataLength(); j++) {
                        if (j < left.arrangement.length && left.arrangement[j] != -1 && left.arrangement[j] < FullDataSet.getDataLength()) {
                            if (bounds == null || bounds.intersects(setnum * squareW, j * squareL, squareW, squareL)) {
                                if (group.hasMember(left.arrangement[j])) {
                                    sets.setColor(color);
                                    sets.fillRect(setnum * squareW, j * squareL, squareW, squareL);
                                }
                            }
                            counter++;
                            if (counter == rows) {
                                break;
                            }
                        }

                    }
                    setnum++;
                }
            }

            if (paintGrid) {
                sets.setColor(GridCol);

                for (int i = 0; i < rows + 1; i++) {
                    if (bounds == null || bounds.intersectsLine(0, i * squareL, numsets * squareW, i * squareL)) {
                        sets.drawLine(0, i * squareL, numsets * squareW, i * squareL);
                    }
                }

                for (int j = 0; j < numsets + 1; j++) {
                    if (bounds == null || bounds.intersectsLine(j * squareW, 0, j * squareW, rows * squareL)) {
                        sets.drawLine(j * squareW, 0, j * squareW, rows * squareL);
                    }
                }
                sets.drawLine(numsets * squareW, 0, numsets * squareW, rows * squareL);

            }
        }
        sets.translate(-start.x, -start.y);
        if (bounds != null) {
            bounds.translate(start.x, start.y);
        }
    }

    private Rectangle getSetHeaderBounds() {
        int numsets = 0;
        for (Group group : FullDataSet.getRowGroups()) {
            if (group.isActive()) {
                numsets++;
            }
        }

        if (numsets > 0) {
            return new Rectangle(squareW * numsets + 1, topnamesheight + 1);
        }

        return new Rectangle(0, 0);
    }

    public Image paintSetHeaders() {

        Rectangle bounds = getSetHeaderBounds();

        BufferedImage seth = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics setgr = seth.getGraphics();
        setgr.setColor(getBackground());
        setgr.fillRect(0, 0, seth.getWidth(), seth.getHeight());
        drawSetHeaders(setgr, new Point(0, 0));
        return seth;

    }

    public void drawSetHeaders(Graphics sets, Point start) {
        sets.translate(start.x, start.y);
        int Height = topnamesheight;
        int numsets = 0;
        int setnum = 0;
        String Ntmp = null;

        int setwidth = 0;
        Tools to = new Tools();
        BufferedImage setim = null;
        Image tempim;

        Font f = getTableFont(squareW - 2);
        sets.setFont(f);
        Graphics2D g2d = (Graphics2D) sets;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        for (Group group : FullDataSet.getRowGroups()) {
            if (group.isActive()) {
                numsets++;
            }
        }

        int halfFontHeight = sets.getFontMetrics().getHeight() / 4;
        double rot = Math.PI / 2;
        Color groupColor = null;

        if (numsets > 0) {

            setwidth = numsets * squareW;

            int counter = 0;
            for (Group group : FullDataSet.getRowGroups()) {
                if (group.isActive()) {
                    Ntmp = group.getName();
                    groupColor = group.getColor();

                    if (topnamesheight > 0) {
                        g2d.setColor(groupColor);

                        g2d.translate(-2 - halfFontHeight + (squareW * (counter + 1)) + 2, -2 + Height);
                        g2d.rotate(-rot);
                        g2d.drawString(Ntmp, 0, 0);
                        g2d.rotate(rot);
                        g2d.translate(2 + halfFontHeight - (squareW * (counter + 1)) - 2, 2 - (Height));
                        counter++;
                    }
                    setnum++;
                }
            }

            if (paintGrid && upper != null) {
                sets.setColor(GridCol);

                for (int i = 0; i < numsets + 1; i++) {
                    sets.drawLine(i * squareW, 0, i * squareW, Height);
                }
                sets.drawLine(0, 0, numsets * squareW, 0);
                sets.drawLine(0, Height, numsets * squareW, Height);

            }
        }

        sets.translate(-start.x, -start.y);
    }

    private Rectangle getSquaresBounds() {
        int[] upperArrangement = null;
        if (upper == null || upper.arrangement == null) {
            upperArrangement = new int[data.getDataWidth()];
        } else {
            upperArrangement = upper.arrangement;
        }
        if (upperArrangement == null || left == null) {
            return new Rectangle(0, 0);
        }
        return new Rectangle(upperArrangement.length * squareW + 1, (left.actualLength * squareL) + 1);
    }

    private void drawSquares(Graphics squares, Point start, Rectangle bounds, int additionalLength) {

        ColorFactory colors = ColorFactoryList.getInstance().getActiveColorFactory(data);
        SquaresLastPaintedAt = start;
        Rectangle view = new Rectangle(999999, 999999);
        if (db) {
            view = getSquaresBounds();
        }
        squares.translate(start.x, start.y);
        int paintvalue = 0;
        int rows = 0;
        if (left != null) {
            rows = this.countgenes(this.trunk);//left.leaves;//.actualLength;
        } else {
            rows = this.FullDataSet.getDataLength();
        }
        int counter = 0;


        double[] gengenscalevals = null;
        int[] upperArrangement = null;
        if (upper == null || upper.arrangement == null) {
            upperArrangement = new int[data.getDataWidth()];
            for (int i = 0; i < upperArrangement.length; i++) {
                upperArrangement[i] = i;
            }
        } else {
            upperArrangement = upper.arrangement;
        }

        double highest = Math.max(Math.abs(min), Math.abs(max));
        double[][] dat = null;
        if (!transposed) {
            dat = FullDataSet.getData();
        } else {
            dat = Tools.transpose(FullDataSet.getData());
        }

        if (left == null) {
            return;
        }
        double mean = 0.0;

        if (gengenscale) {
            gengenscalevals = new double[upperArrangement.length];
        }

        for (int i = 0; i < left.arrangement.length; i++) {
            mean = 0;

            if (gengenscale) {
                if (bounds == null || bounds.intersects(0, (i * squareL), squareW * upperArrangement.length, (i + 1) * squareL)) {
                    for (int j = 0; j < dat[0].length; j++) {
                        mean += dat[left.arrangement[i]][j];
                    }
                    mean = mean / (double) dat[0].length;
                    //System.out.print("\nMEAN:"+mean);
                } else {
                    mean = Double.NaN;
                }

                //find the max difference from mean..
                highest = 0.0;
                for (int j = 0; j < dat[0].length; j++) {
                    gengenscalevals[j] = dat[left.arrangement[i]][j] - mean;
                    if (Math.abs(gengenscalevals[j]) > highest) {
                        highest = Math.abs(gengenscalevals[j]);
                    }
                }

            }


            double v = 0;
            Rectangle sqr = new Rectangle(0, 0, squareW, squareL);
            for (int j = 0; j < upperArrangement.length; j++) {
                if (bounds == null || bounds.intersects((j * squareW), (i * squareL), squareW, squareL)) {

                    if (ClusterColumns && upper != null) {

                        sqr.setLocation((j * squareW), (i * squareL));
                        if (!view.intersects(sqr)) {
                            continue;
                        }

                        if (left.arrangement[i] != -1 && upperArrangement[j] != -1) {

                            if (data.isMissing(left.arrangement[i], upperArrangement[j])) {
                                squares.setColor(colors.getMissing());
                            } else {
                                if (!gengenscale) {
                                    v = dat[left.arrangement[i]][upperArrangement[j]];
                                    squares.setColor(colors.getColor(v));
                                    //paintvalue=Math.abs((int)((v/(highest))*((double)pos.length-1)));
                                } else {
                                    v = gengenscalevals[upperArrangement[j]];
                                    squares.setColor(colors.getColor(v));
                                }
                            }
                            /*
                             * if(paintvalue>250 || paintvalue<0){paintvalue=0;}
                             * if(v<0){
                             *
                             * squares.setColor(colors.getColor(v)); //
                             * squares.setColor(neg[paintvalue]); } else
                             * if(v>0){ squares.setColor(pos[paintvalue]); }
                             * else squares.setColor(nullc);
                             */

                            squares.fillRect((j * squareW), (i * squareL), squareW, squareL);
                        }
                    } else {
                        sqr.setLocation((j * squareW), (i * squareL));
                        if (!view.intersects(sqr)) {
                            continue;
                        }

                        v = dat[left.arrangement[i]][upperArrangement[j]];
                        /*
                         * if(!gengenscale){ v=
                         * dat[left.arrangement[i]][upperArrangement[j]];
                         * paintvalue=Math.abs((int)((v/(highest))*((double)pos.length-1)));
                         * } else{
                         * v=gengenscalevals[upperArrangement[j]]/(highest);
                         * paintvalue=Math.abs((int)((gengenscalevals[upperArrangement[j]]/(highest))*((double)pos.length-1)));
                         * * } * if(v<0){ squares.setColor(neg[paintvalue]); }
                         *
                         * else if(v>0){ squares.setColor(pos[paintvalue]); }
                         * else squares.setColor(nullc);
                         *
                         */

                        if (data.isMissing(left.arrangement[i], upperArrangement[j])) {
                            squares.setColor(colors.getMissing());
                        } else {
                            squares.setColor(colors.getColor(v));
                        }

                        squares.fillRect((j * squareW), (i * squareL), squareW, squareL);
                    }
                }
            }
            counter++;
            if (counter == rows) {
                break;
            }
        }

        counter = 0;
        if (paintGrid) {
            squares.setColor(GridCol);
            for (int i = 0; i < left.arrangement.length + 1; i++) {
                if (bounds == null || bounds.intersects(0, i * squareL, upperArrangement.length * squareW, i * squareL)) {
                    squares.drawLine(0, i * squareL, (upperArrangement.length * squareW) + additionalLength, i * squareL);
                }
                counter++;
                if (counter > rows) {
                    break;
                }
            }
            for (int j = 0; j < upperArrangement.length; j++) {
                if (bounds == null || bounds.intersects(j * squareW, 0, j * squareW, rows * squareL)) {
                    squares.drawLine(j * squareW, 0, j * squareW, rows * squareL);
                }
            }

            if (bounds == null || bounds.intersects(upperArrangement.length * squareW, 0, upperArrangement.length * squareW, rows * squareL)) {
                squares.drawLine(upperArrangement.length * squareW, 0, upperArrangement.length * squareW, rows * squareL);
            }

        }

        squares.translate(-start.x, -start.y);

    }

    public int intvalue(String s) {
        Integer i = new Integer(s);
        return i.intValue();
    }

    public void makeminarray(float[][] dist) {
        float minst = 999999;
        int minstpeker = 0;
        for (int i = 0; i < dist.length; i++) {
            minst = 999999;

            for (int j = 0; j < i; j++) {
                if (!stepovergen[i] && !stepovergen[j]) {
                    if (dist[i][j] < minst) {
                        minst = dist[i][j];
                        minstpeker = j;
                    }
                }
            }
            sortpan[i] = minstpeker;
            sortpan2[i] = minst;

        }
    }

    public void updateminarray(int row, float[][] dist) {
        float minst = 9999999f;
        int minstpeker = 0;

        minst = 9999999;

        for (int j = 0; j < row; j++) {
            if (!stepovergen[j]) {
                if (dist[row][j] < minst) {
                    minst = dist[row][j];
                    minstpeker = j;
                }
            }
        }
        // }
        sortpan[row] = minstpeker;
        sortpan2[row] = minst;
        minarrayupdates++;
    }

    public void updateminarray(int row, int col, float[][] dist) {
        int minstpeker = 0;
        if (stepovergen[sortpan[row]]) {
            updateminarray(row, dist);
        } else {

            //  if(distanceClustering && isCorrelation){
            if ((1 - dist[row][col]) < sortpan2[row]) {
                minstpeker = col;
                sortpan[row] = minstpeker;
                sortpan2[row] = dist[row][col];
            }
        }
        minarrayupdates++;
    }

    /*
     * The old slow version... public Gen[] makeclusters(Gen[] data){
     *
     * float minst = 999999990.0f; int minst1 = 0; int minst2 = 0;
     *
     * for(int i=0;i<values.length;i++){ if(!stepovergen[i]){ for(int
     * j=0;j<i;j++){ if(values[i][j]<minst && !stepovergen[j]){
     * minst=values[i][j]; minst1=i; minst2=j;} } } } data[minst2]=new
     * Gen(data[minst2],data[minst1],minst);// Dette er for avstanden i mellom
     * dem stepovergen[minst1]=true; numgenes--; System.out.print("\n ");
     * for(int i=0;i<values[minst2].length;i++){ if(!stepovergen[i]){
     * values[minst2][i] = Math.max(values[minst2][i],values[minst1][i]);
     * System.out.print(" "+values[minst2][i]);} }
     *
     * for(int i=minst2+1; i< values.length; i++){ if(i<minst1 &&
     * !stepovergen[i]){ values[i][minst2] =
     * Math.max(values[i][minst2],values[minst1][i]); } else if(i>minst1 &&
     * !stepovergen[i]){ values[i][minst2] =
     * Math.max(values[i][minst2],values[i][minst1]); }
     * if(!stepovergen[i])System.out.print(" "+values[i][minst2]); }
     *
     *
     *
     *
     * return data;
     *
     * }
     */
    public Node[] makeclusters(Node[] data, float[][] dist, int linkage) {
        int pastright = 0;
        int pastdown = 0;
        int right = 0;
        int left = 0;
        int tempo = 0;
        int minst1 = 0;
        int minst2 = 0;
        double minsteirow = 99999999999999999.9;
        int minsteirowpeker = 99999999;
        double fusion = 0;
        float minst = 99999999999999f;
        for (int i = 0; i < sortpan.length; i++) {
            if (!stepovergen[i] && !stepovergen[sortpan[i]]) {
                if (sortpan2[i] < minst) {
                    minst = sortpan2[i];
                    minst1 = i;
                    minst2 = sortpan[i];
                }
            }
        }

        stepovergen[minst1] = true;
        numgenes--;

        int minst1members = data[minst1].members;
        int minst2members = data[minst2].members;

        data[minst2] = new Node(data[minst2], data[minst1], minst);// Dette er for avstanden i mellom dem

        for (int j = 0; j < dist[minst2].length; j++) {
            if (!stepovergen[j]) {
                if (linkage == 3) {
                    dist[minst2][j] = Math.max(dist[minst2][j], dist[minst1][j]);
                } else if (linkage == 0) {
                    dist[minst2][j] = Math.min(dist[minst2][j], dist[minst1][j]);
                } else if (linkage == 1) {
                    dist[minst2][j] = (dist[minst2][j] + dist[minst1][j]) / 2;
                } else if (linkage == 2) {
                    dist[minst2][j] = ((dist[minst2][j] * minst2members) + (dist[minst1][j] * minst1members)) / (minst2members + minst1members);
                }

            }
        }

        updateminarray(minst2, dist);

        for (int j = minst2 + 1; j < dist.length; j++) {
            // lengden paa dist[minst1] vil vaere like lang som denne kolonnen
            // Begynner paa cellen under diagonalen. Derfor minst2+1

            if (!stepovergen[j]) {

                if (j < minst1) {
                    if (linkage == 3) {
                        dist[j][minst2] = Math.max(dist[j][minst2], dist[minst1][j]);
                    } else if (linkage == 0) {
                        dist[j][minst2] = Math.min(dist[j][minst2], dist[minst1][j]);
                    } else if (linkage == 1) {
                        dist[j][minst2] = (dist[j][minst2] + dist[minst1][j]) / 2;
                    } else if (linkage == 2) {
                        dist[j][minst2] = ((dist[j][minst2] * minst2members) + (dist[minst1][j] * minst1members)) / (minst2members + minst1members);
                    }
                } else if (j > minst1) {
                    if (linkage == 3) {
                        dist[j][minst2] = Math.max(dist[j][minst2], dist[j][minst1]);
                    } else if (linkage == 0) {
                        dist[j][minst2] = Math.min(dist[j][minst2], dist[j][minst1]);
                    } else if (linkage == 1) {
                        dist[j][minst2] = (dist[j][minst2] + dist[j][minst1]) / 2;
                    } else if (linkage == 2) {
                        dist[j][minst2] = ((dist[j][minst2] * minst2members) + (dist[j][minst1] * minst1members)) / (minst2members + minst1members);
                    }
                }
            }

            if (dist[j][minst2] < sortpan2[j]) {
                sortpan2[j] = dist[j][minst2];
                sortpan[j] = minst2;
            } else if (sortpan[j] == minst1) {
                updateminarray(j, dist);
            } else if (sortpan[j] == minst2) {
                updateminarray(j, dist);
            }
        }
        return data;
    }

    public float absmax(float a, float b) {
        if (Math.abs(a) < Math.abs(b)) {
            return a;
        } else {
            return b;
        }
    }

    public String getMeta() {
        String ret = "";
        String clusmethod = "";
        Tools t = new Tools();

        if (!fromtree) {
            if (cd.rb1SV.isSelected()) {
                clusmethod = "Cluster method: Single Linkage";
            } else if (cd.rb2SV.isSelected()) {
                clusmethod = "Cluster method: Average Linkage (WPGM";
            } else if (cd.rb3SV.isSelected()) {
                clusmethod = "Cluster method: Complete Linkage";
            } else if (cd.rb4SV.isSelected()) {
                clusmethod = "Cluster method: Average Linkage (UPGM";
            }
        } else {
            if (link == LINKAGE.SINGLE) {
                clusmethod = "Cluster method: Single Linkage";
            }
            if (link == LINKAGE.WPGMA) {
                clusmethod = "Cluster method: Average Linkage (WPGM";
            }
            if (link == LINKAGE.COMPLETE) {
                clusmethod = "Cluster method: Complete Linkage";
            }
            if (link == LINKAGE.UPGMA) {
                clusmethod = "Cluster method: Average Linkage (UPGM";
            }
        }
        ret += "Distance metric: " + jmath.getMetricsString() + "\n";
        ret += clusmethod + "\n";
        ret += "Cluster Columns: " + t.booltoprop(ClusterColumns) + "\n";
        ret += "Vertical Distances: " + squareL + "\n";
        ret += "Horizontal Distances: " + squareW + "\n";
        return ret;
    }

    public MetaInfoNode getMetaNode() {
        String ret = "";
        String clusmethod = "";
        Tools t = new Tools();

        java.util.Hashtable parameters = new java.util.Hashtable();

        if (!fromtree) {
            if (cd.rb1SV.isSelected()) {
                clusmethod = " Cluster method: Single Linkage";
            } else if (cd.rb2SV.isSelected()) {
                clusmethod = " Cluster method: Average Linkage (WPGM";
            } else if (cd.rb3SV.isSelected()) {
                clusmethod = " Cluster method: Complete Linkage";
            } else if (cd.rb4SV.isSelected()) {
                clusmethod = " Cluster method: Average Linkage (UPGM";
            }
        } else {
            if (link == LINKAGE.SINGLE) {
                clusmethod = " Cluster method: Single Linkage";
            }
            if (link == LINKAGE.WPGMA) {
                clusmethod = " Cluster method: Average Linkage (WPGM";
            }
            if (link == LINKAGE.COMPLETE) {
                clusmethod = " Cluster method: Complete Linkage";
            }
            if (link == LINKAGE.UPGMA) {
                clusmethod = " Cluster method: Average Linkage (UPGM";
            }
        }
        parameters.put("Distance Metric", jmath.getMetricsString());
        parameters.put("Cluster Columns", t.booltoprop(ClusterColumns));

        /*
         * Column indexes: An array of integers defining the columns extracted
         * Row indexes: An array of integers defining the rows extracted
         */


        return MetaInfoNode.createNode(MetaInfoNode.hclust, parameters);
    }

    public void toggleSelection(int sel) {
        if (selection[sel]) {
            selection[sel] = false;
        } else {
            selection[sel] = true;
        }

    }

    public void addSelection(int sel) {
        selmodel.addSelectionInterval(sel, sel);
    }

    public void clearSelection() {
        java.util.Arrays.fill(selection, false);
    }

    public int[] getSelection() {
        int cnt = 0;
        for (int i = 0; i < selection.length; i++) {
            if (selection[i]) {
                cnt++;
            }
        }
        int[] ret = new int[cnt];
        cnt = 0;
        for (int i = 0; i < selection.length; i++) {
            if (selection[i]) {
                ret[cnt] = i;
                cnt++;
            }
        }
        return ret;
    }

    public void setautotop() {

        boolean[] visibleIndexes = createVisibleRowIndexes();
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        Graphics gr = img.getGraphics();

        gr.setColor(getBackground());
        gr.fillRect(0, 0, img.getWidth(), img.getHeight());

        Font f = getTableFont(squareW - 2);
        gr.setFont(f);
        int maxtop = -99999;


        if (antialias) {
            Graphics2D g2d = (Graphics2D) gr;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        java.awt.FontMetrics fm = gr.getFontMetrics();

        String[] colInf = getColumnAnnotation();
        for (int i = 0; i < colInf.length; i++) {
            if (fm.stringWidth(colInf[i]) > maxtop) {
                maxtop = fm.stringWidth(colInf[i]);
            }
        }

        //If one or more set is active.. the name of this must also be considered.
        String tmp = "";
        Group group = null;
        boolean active = false;

        for (int i = 0; i < data.getRowGroups().size(); i++) {
            group = (Group) data.getRowGroups().get(i);
            tmp = group.getName();
            active = group.isActive();
            if (fm.stringWidth(tmp) > maxtop && active) {
                maxtop = fm.stringWidth(tmp);
            }
        }

        if (maxtop > 0) {
            topnamesheight = maxtop + 5;
        } else {
            topnamesheight = 0;
        }

    }

    private Font getTableFont(int size) {
        Font f;
        f = new Font("Sans Serif", 0, size);
        return f.deriveFont((float) size);
    }

    //Set automaticly the width on the nameImage on the right.
    public int GetMaxAccent(String[] strings) {
        int max = -99999;

        Font f = getTableFont(Math.max(squareW, 12));

        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        Graphics gr = img.getGraphics();
        gr.setFont(f);

        if (antialias) {
            Graphics2D g2d = (Graphics2D) gr;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        java.awt.FontMetrics fm = gr.getFontMetrics();

        for (int i = 0; i < strings.length; i++) {
            if (fm.stringWidth(strings[i]) > max) {
                max = fm.stringWidth(strings[i]);
            }
        }

        return max;

    }

    public void makematrixfromtrunk(Node trunk, Vector v) {
        if (trunk.merged) {
            makematrixfromtrunk(trunk.right, v);
            makematrixfromtrunk(trunk.left, v);
        } else {
            v.addElement(new Integer(trunk.nme));
        }
    }

    public int countgenes(Node trunk) {
        java.util.Stack c = new java.util.Stack();
        int ret = 0;
        c.push(trunk);
        Node tr = trunk;

        if (trunk == null) {
            System.out.print("\n!No trunk\n");
        }

        while (!c.empty()) {
            tr = (Node) c.pop();

            if (tr.merged) {
                c.push(tr.left);
                c.push(tr.right);
            } else {
                ret++;
            }
        }

        return ret;
    }

    public Node getLeftRoot() {
        return this.left.root;
    }

    public Node getTopRoot() {
        return toproot;
    }

    public void clearTreeColors(Node trunk) {
        java.util.Stack c = new java.util.Stack();
        c.push(trunk);
        Node tr = trunk;

        if (trunk == null) {
            System.out.print("\n!No trunk\n");
        }

        while (!c.empty()) {
            tr = (Node) c.pop();

            if (tr != null && tr.merged) {
                c.push(tr.left);
                c.push(tr.right);
            }
            if (tr != null) {
                tr.setColor(null);
            }
        }

        this.repaint();

    }

    public void setClusterSettings(ClusterSettings settings) {

        TableWidth = null;
        Wd = null;
        WdSUM = null;

        LeftTreeWidth = settings.getLeftTreeSize();
        TopTreeHeight = settings.getTopTreeSize();
        squareL = settings.getSquareL();
        squareW = settings.getSquareW();
        markColor = settings.getMarkColor();
        paintGrid = settings.paintGrid();


        if (left != null) {
            left.leafdist = squareL;
            left.leftmargin = (int) Math.round(squareL / 2);
            left.treewidth = LeftTreeWidth;
            left.generatecoords();
        }
        if (upper != null) {
            upper.leafdist = squareW;
            upper.horizontal = false;
            upper.leftmargin = (int) Math.round(squareW / 2);
            upper.treewidth = TopTreeHeight;
            upper.generatecoords();
        }
        Sc.setBounds(0, 0, Sc.getPreferredSize().width, Sc.getPreferredSize().height);
        Sc.revalidate();
        Sc.repaint();

        this.revalidate();
    }

    public void drawImage(Graphics g) {
        g.translate(4, 2);
        int totwidth = 0;
        g.setFont(font);
        Rectangle top = new Rectangle(0, 0);
        Rectangle squares = new Rectangle(0, 0);
        Rectangle sets = new Rectangle(0, 0);
        Rectangle setHeaders = new Rectangle(0, 0);
        Rectangle info = new Rectangle(0, 0);
        Rectangle scale = new Rectangle(0, 0);
        Rectangle leftr = new Rectangle(0, 0);
        Rectangle upperr = new Rectangle(0, 0);
        Rectangle table = new Rectangle(0, 0);
        if (left != null && (!openDisplay || paintDendrogram)) {
            leftr = new Rectangle(left.getWidth(), left.getHeight());
        }
        if (upper != null && (!openDisplay || paintSquares)) {
            upperr = new Rectangle(upper.getWidth(), upper.getHeight());
        }
        //set correct table size again..
        if (openDisplay && upperr.height == 0 && (paintTable)) {
        }


        if (!openDisplay || paintInfo) {
            info = getInfoBounds(g);
        }
        if (!openDisplay || paintScale) {
            scale = getScaleBounds();
        }

        if (!openDisplay || paintSquares) {

            if (upper != null) {
                g.translate(leftr.width, 0);
                if (db) {
                    upper.setBnds(g.getClipBounds());
                } else {
                    upper.setBnds(null);
                }

                upper.painttree(upper.root, g, Color.black);
                g.translate(-leftr.width, 0);
            }
            if (leftr != null) {
                drawTop(g, new Point(leftr.width, upperr.height));
            }
            top = getTopBounds();
        }
        if (!openDisplay || paintDendrogram) {
            if (left != null) {
                g.translate(0, upperr.height + top.height);
                if (db) {
                    left.setBnds(g.getClipBounds());
                } else {
                    left.setBnds(null);
                }
                left.painttree(left.root, g, Color.black);
                g.translate(0, -top.height - upperr.height);
            }
        }

        if (!openDisplay || paintSquares) {
            drawSquares(g, new Point(leftr.width, upperr.height + top.height), null, 0);
            squares = getSquaresBounds();


            if (db) {
                drawSets(g, new Point(2 + leftr.width + squares.width, upperr.height + top.height), g.getClipBounds());
            } else {
                drawSets(g, new Point(2 + leftr.width + squares.width, upperr.height + top.height), null);
            }
            sets = getSetBounds();
            drawSetHeaders(g, new Point(2 + leftr.width + squares.width, upperr.height));
            setHeaders = getSetHeaderBounds();
        }
        if (!openDisplay || paintTable) {
            drawTable(g, new Point(4 + leftr.width + squares.width + sets.width, upperr.height + top.height - squareL));
            table = getTableWidth();
        }
        int maxtop = Math.max(squares.height, leftr.height);
        maxtop = Math.max(maxtop, getTableWidth().height);
        int maxbottom = Math.max(info.height, scale.height);
        if (!openDisplay || paintInfo) {
            drawInfo(g, new Point(0, 10 + upperr.height + top.height + maxtop));
        }
        if (!openDisplay || paintScale) {
            float W = (float) (Math.min((leftr.width + squares.width + sets.width + table.width) - (info.width + 10), 250));
            drawScale(g, new Point(info.width + 10, 10 + upperr.height + top.height + maxtop), W, (float) maxbottom);
        }
        g.translate(-4, -2);
    }

    public Rectangle getTableWidth() {

        if (TableWidth != null) {
            return TableWidth;
        }

        Font f = getTableFont(squareL - 1);
        JLabel l = new JLabel("    ");
        l.setFont(f);
        l.setBorder(javax.swing.BorderFactory.createLineBorder(GridCol));
        l.setIconTextGap(2);
        l.setMaximumSize(new Dimension(200, squareL));
        AnnotationManager m = AnnotationManager.getAnnotationManager();
        Set<String> annotationNames = data.getRowAnnotationNamesInUse();
        if (annotationNames == null) {
            annotationNames = m.getManagedRowAnnotationNames();
        }
        int[] Wd = new int[annotationNames.size()];
        Iterator<String> it = annotationNames.iterator();
        for (int i = 0; it.hasNext(); i++) {
            l.setText(it.next());
            l.validate();
            if (l.getPreferredSize().width > Wd[i]) {
                Wd[i] = l.getPreferredSize().width + 6;
            }
        }

        String[][] inf = data.getInfos();
        for (int i = 0; i < data.getDataLength(); i++) {
            for (int j = 0; j < Wd.length; j++) {
                if (squareL < 6) {
                    Wd[j] = 5;
                    continue;
                }
                String rowId = data.getRowIds()[i];
                l.setText(inf[i][j]);
                l.validate();
                if (l.getPreferredSize().width > Wd[j]) {
                    Wd[j] = l.getPreferredSize().width + 6;
                }
            }
        }

        int[] WdSUM = new int[annotationNames.size()];

        int totalW = 10;

        for (int j = 0; j < Wd.length; j++) {
            totalW += Wd[j] + 3;
        }

        for (int i = 1; i < Wd.length; i++) {
            for (int j = 0; j < Wd.length; j++) {
                WdSUM[i] += Wd[j] + 3;
            }
        }

        TableWidth = new Rectangle(totalW + 30, squareL * data.getDataLength());

        return TableWidth;
    }

    public void mouseDrag(Point p) {

        int Xloc = p.x;
        int Yloc = p.y;

        int Gx = -1;
        int Gy = -1;

        int NendSelectionIndex = -1;
        int NstartSelectionIndex = -1;
        int endSelectionIndex = -1;

        int Y = (Yloc - tableLastPaintedAt.y - squareL) / squareL;
        if (Y >= 0 && Y <= data.getDataLength()) {
            Gy = Y;
        }

        Rectangle r = new Rectangle(p.x, p.y, 30, squareL);
        Sc.scrollRectToVisible(r);

        int X = (Xloc - SquaresLastPaintedAt.x - squareW) / squareW;
        if (X >= 0 && X < data.getDataWidth()) {
            Gx = X;
        }
        if (Gy > -1) {

            if (startSelectionIndex == -1) {
                startSelectionIndex = Gy;
            }
            endSelectionIndex = Gy;
            draggedOverIndices.clear();
            SelectedRows.clear();

            NstartSelectionIndex = Math.min(startSelectionIndex, endSelectionIndex);
            NendSelectionIndex = Math.max(startSelectionIndex, endSelectionIndex);

            if (NstartSelectionIndex < 0) {
                NstartSelectionIndex = 0;
            }
            if (NendSelectionIndex > data.getDataLength() - 1) {
                NendSelectionIndex = data.getDataLength() - 1;
            }

            for (int i = NstartSelectionIndex; i <= NendSelectionIndex; i++) {
                draggedOverIndices.add(new Integer(left.arrangement[i]));
                SelectedRows.addMember(left.arrangement[i]);
            }

            Sc.repaint();
        }
    }

    public String getColAnnotation(int Xloc, int Yloc) {

        String ret = "";
        if (left == null) {
            return null;
        }

        int Y = (Yloc - tableLastPaintedAt.y - squareL) / squareL;
        if (Y >= 0 && Y < data.getDataLength()) {
            ret = data.getRowIds()[left.arrangement[Y]];
        }

        int X = (-4 + Xloc - SquaresLastPaintedAt.x) / squareW;

        if (X >= 0 && X < data.getDataWidth()) {
            if (upper != null) {
                X = upper.arrangement[X];
            }
            if (ret.length() == 0) {
                ret = data.getColumnIds()[X];
            } else {
                ret = ret + " ; " + data.getColumnIds()[X];
            }
        }
        if (ret.length() == 0) {
            return null;
        } else {
            return ret;
        }
    }

    private boolean[] selectedRows(int[] selectedRows) {
        boolean[] ret = new boolean[data.getDataLength()];
        if (selectedRows != null) {
            for (int i = 0; i < selectedRows.length; i++) {
                ret[selectedRows[i]] = true;
            }
        }
        for (int i = 0; i < draggedOverIndices.size(); i++) {
            ret[((Integer) draggedOverIndices.elementAt(i)).intValue()] = true;
        }
        return ret;
    }

    public void drawTable(Graphics gr, Point UL) {

        tableLastPaintedAt = UL;

        Font f = getTableFont(squareL - 1);
        AnnotationManager annManager = AnnotationManager.getAnnotationManager();
        String[] rowIds = data.getRowIds();

        Set<String> annotations = data.getRowAnnotationNamesInUse();
        if (annotations == null) {
            annotations = annManager.getManagedRowAnnotationNames();
        }

        String[][] inf;   // row annotation matrix
        String[] headers;  // header of the row annotation matrix
        if (annotations.size() == 0) {
            inf = new String[data.getDataLength()][1];
            for (int i = 0; i < inf.length; i++) {
                inf[i][0] = rowIds[i];
            }
            headers = new String[]{"Row ID"};
        } else {
            headers = annotations.toArray(new String[annotations.size()]);
            inf = new String[data.getDataLength()][annotations.size()];
            for (int i = 0; i < headers.length; i++) {
                //ann manager need to re implemeinted?
                AnnotationLibrary anns = annManager.getRowAnnotations(headers[i]);
                for (int j = 0; j < inf.length; j++) {
                    inf[j][i] = rowIds[j];//anns.getAnnotation(rowIds[j]);//
                }
            }
        }

        Graphics2D g2d = (Graphics2D) gr;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int X = UL.x;
        int Y = UL.y;
        int H = squareL;

        int L = data.getDataLength();
        int W = headers.length;

        JLabel l = new JLabel("    ");
        l.setFont(f);
        l.setIconTextGap(2);
        javax.swing.border.Border UB = javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, GridCol);
        javax.swing.border.Border LB = javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, GridCol);

        l.setMaximumSize(new Dimension(200, squareL));

        boolean drawTableHeader = true;

        //if there is not enough room for a header.. skip header.
        if (UL.y < squareL) {
            drawTableHeader = false;
        }

        if (Wd == null) {
            Wd = new int[inf[0].length];
            WdSUM = new int[inf[0].length];

            if (drawTableHeader) {
                for (int i = 0; i < headers.length; i++) {
                    l.setText(headers[i]);
                    l.validate();
                    if (l.getPreferredSize().width > Wd[i]) {
                        Wd[i] = l.getPreferredSize().width + 16;
                    }
                }
            }
            for (int i = 0; i < inf.length; i++) {
                for (int j = 0; j < Wd.length; j++) {
                    if (squareL < 6) {
                        Wd[j] = 5;
                        continue;
                    }
                    l.setText(inf[i][j]);
                    l.validate();
                    if (l.getPreferredSize().width > Wd[j]) {
                        Wd[j] = l.getPreferredSize().width + 16;
                    }
                }
            }

            WdSUM[0] = 0;

            for (int i = 0; i < Wd.length; i++) {
                WdSUM[i] = -1;
                for (int j = 0; j < i; j++) {
                    WdSUM[i] += Wd[j] + 3;
                }
            }
        }

        Rectangle BNDS = new Rectangle();

        l.setBackground(Color.WHITE);
        l.setOpaque(true);
        if (left == null) {
            return;
        }

        f = getTableFont(squareL - 1);
        l.setFont(f);


        int[] LArr = left.arrangement;
        int Rindex = 0;


        //draw the table header.. (if wanted)
        if (drawTableHeader) {

            l.setBackground(Color.lightGray);
            l.setForeground(Color.white);

            for (int j = 0; j < W; j++) {
                X = UL.x + WdSUM[j];
                Y = UL.y;
                BNDS.setBounds(X, Y, Wd[j], squareL + 1);

                if (gr.getClipBounds() != null && !gr.getClipBounds().intersects(BNDS)) {
                    continue;
                }
                gr.translate(X, Y);
                l.setBounds(0, 0, Wd[j] + 1, squareL + 1);
                l.setBorder(LB);

                if (squareL >= 6) {
                    l.setText(headers[j]);
                }
                l.validate();
                l.paint(gr);
                gr.translate(-X, -Y);
            }
        }

        l.setForeground(Color.BLACK);

        Selection s = SelectionManager.getSelectionManager().getSelectedRows(data);
        boolean[] sel = selectedRows(s == null ? null : s.getMembers());

        for (int i = 0; i < L; i++) {
            Rindex = LArr[i];

            for (int j = 0; j < W; j++) {
                X = UL.x + WdSUM[j];
                Y = UL.y + (squareL * (i + 1));

                BNDS.setBounds(X, Y, Wd[j], squareL + 1);

                if (gr.getClipBounds() != null && !gr.getClipBounds().intersects(BNDS)) {
                    continue;
                }

                if (sel[LArr[i]]) {
                    if (squareL < 6) {
                        l.setBackground(new Color(125, 125, 255));
                    } else {
                        l.setBackground(new Color(225, 225, 255));
                    }
                } else {
                    l.setBackground(Color.WHITE);
                }

                gr.translate(X, Y);
                l.setBounds(0, 0, Wd[j] + 1, squareL + 1);

                if (i < L - 1) {
                    l.setBorder(UB);
                } else {
                    l.setBounds(0, 0, Wd[j] + 1, squareL + 1);
                    l.setBorder(LB);
                }
                if (squareL >= 6) {
                    l.setText(inf[Rindex][j]);
                }
                l.validate();
                l.paint(gr);
                gr.translate(-X, -Y);
            }
        }

        if (squareL < 6) {
            return;
        }

        l.setBackground(Color.LIGHT_GRAY);
        f = getTableFont(squareL - 2);
        //f = new Font("Arial",1,squareL-2);
        l.setFont(f);


        for (int j = 0; j < W; j++) {
            X = UL.x + WdSUM[j];
            Y = UL.y;

            BNDS.setBounds(X, Y, Wd[j], squareL + 1);
            if (gr.getClipBounds() != null && !gr.getClipBounds().intersects(BNDS)) {
                continue;
            }

            gr.translate(X, Y);
            l.setBounds(0, 0, Wd[j], squareL + 1);
            l.setBorder(javax.swing.BorderFactory.createLineBorder(GridCol));
            l.setText(headers[j]);
            l.validate();
            gr.translate(-X, -Y);

        }
    }

    public float[][] makematrix(int lengde) {
        float[][] inmatrix = new float[lengde][];
        for (int i = 0; i < lengde; i++) {
            inmatrix[i] = new float[i];
        }
        return inmatrix;
    }

    public void calcdistNoThread(final boolean transposed) {

        jmath.setMetric(distance);

        float[][] dist = null;

        try {
            if (!transposed) {
                dist = makematrix(data.getDataLength());
            } else {
                dist = makematrix(data.getDataWidth());
            }

            if (!transposed) { //This is not the transposed version. Proceed without transposing.
                double[][] dst = data.getData();
                boolean[][] nulls = data.getMissingMeasurements();
                int n = dist.length;

                if (nulls != null) {   // nulls present, use the getnulls matrix.
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < i; j++) {
                            dist[i][j] = (float) jmath.dist(dst[i], dst[j], nulls[i], nulls[j]);

                        }
                    }
                } else {   //No nulls, do not use the getnulls matrix.
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < i; j++) {
                            dist[i][j] = (float) jmath.dist(dst[i], dst[j]);

                        }
                    }
                }

                if (distanceClustering && clusterWay == ROWS) {
                    tmpDist = dist;
                }
            } else { //This is the transposed version. Proceed without transposing.

                double[][] trans = data.getData();
                boolean[][] transnulls = data.getMissingMeasurements();

                double[][] dst = new double[trans[0].length][trans.length];
                boolean[][] nulls = null;

                boolean nullsPresent = false;
                if (transnulls != null) {
                    nulls = new boolean[transnulls[0].length][transnulls.length];
                    nullsPresent = true;
                }

                for (int i = 0; i < trans.length; i++) {
                    for (int j = 0; j < trans[0].length; j++) {
                        dst[j][i] = trans[i][j];

                        if (nullsPresent) {
                            nulls[j][i] = transnulls[i][j];
                        }
                    }
                }

                int n = dist.length;

                if (nulls != null) {   // nulls present, use the getnulls matrix.
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < i; j++) {
                            dist[i][j] = (float) jmath.dist(dst[i], dst[j], nulls[i], nulls[j]);

                        }
                    }
                } else {   //No nulls, do not use the getnulls matrix.
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < i; j++) {
                            dist[i][j] = (float) jmath.dist(dst[i], dst[j]);

                        }
                    }
                }

                if (distanceClustering && clusterWay == COLUMNS) {
                    tmpDist = dist;
                }

            }
        } catch (OutOfMemoryError er) {
            dist = null;
            System.gc();
        }

        controlDistanceMatrix(dist);

        if (mode == 3) {
            if (!distanceMatrixOnly) {
                toproot = makeclusters(true, true, dist);
            }

            gocluster();
            return;
        }

        if (mode != 1) {
            if (!distanceMatrixOnly) {
                trunk = makeclusters(true, false, dist);
            }
            if (!ClusterColumns) {
                gocluster();
            } else {
                mode = 3;
                calcdistNoThread(true); //recursive call for the top component..
            }
        }


    }

    public void calcdist(final boolean transposed) {
        jmath.setMetric(distance);
        final SwingWorker worker = new SwingWorker() {
            boolean stop = false;
            float[][] dist = null;
            javax.swing.ProgressMonitor pm;

            @Override
            public Object construct() {

                try {

                    if (data == null) {
                        System.out.print("\nNO DATA!\n");
                    }

                    if (!transposed) {
                        dist = makematrix(data.getDataLength());
                    } else {
                        dist = makematrix(data.getDataWidth());
                    }



                    if (!transposed) { //This is not the transposed version. Proceed without transposing.
                        double[][] dst = data.getData();
                        boolean[][] nulls = data.getMissingMeasurements();
                        int n = dist.length;
                        pm = new javax.swing.ProgressMonitor(MainClustComponent.this, "calculating distances", "calculating", 0, n);

                        if (nulls != null) {   // nulls present, use the getnulls matrix.
                            for (int i = 0; i < n; i++) {
                                for (int j = 0; j < i; j++) {
                                    dist[i][j] = (float) jmath.dist(dst[i], dst[j], nulls[i], nulls[j]);

                                }

                                if (i % 10 == 0) {
                                    pm.setProgress(i);
                                }
                                if (Thread.interrupted()) {
                                    dist = null;
                                    return null;
                                }
                            }
                        } else {   //No nulls, do not use the getnulls matrix.
                            for (int i = 0; i < n; i++) {
                                for (int j = 0; j < i; j++) {
                                    dist[i][j] = (float) jmath.dist(dst[i], dst[j]);

                                }
                                if (Thread.interrupted()) {
                                    dist = null;
                                    return null;
                                }
                                if (i % 10 == 0) {
                                    pm.setProgress(i);//pr2.JProgressBar2.setValue(i);
                                }
                            }
                        }

                        if (distanceClustering && clusterWay == ROWS) {
                            tmpDist = dist;
                        }
                    } else { //This is the transposed version. Proceed without transposing.

                        double[][] trans = data.getData();
                        boolean[][] transnulls = data.getMissingMeasurements();
                        double[][] dst = new double[trans[0].length][trans.length];
                        boolean[][] nulls = null;
                        boolean nullsPresent = false;

                        if (transnulls != null) {
                            nulls = new boolean[transnulls[0].length][transnulls.length];
                            nullsPresent = true;
                        }

                        for (int i = 0; i < trans.length; i++) {
                            for (int j = 0; j < trans[0].length; j++) {
                                dst[j][i] = trans[i][j];
                                if (nullsPresent) {
                                    nulls[j][i] = transnulls[i][j];
                                }
                            }
                        }

                        int n = dist.length;
                        javax.swing.ProgressMonitor pm = new javax.swing.ProgressMonitor(MainClustComponent.this, "calculating distances", "calculating", 0, n);
                        if (nulls != null) {   // nulls present, use the getnulls matrix.
                            for (int i = 0; i < n; i++) {
                                for (int j = 0; j < i; j++) {
                                    dist[i][j] = (float) jmath.dist(dst[i], dst[j], nulls[i], nulls[j]);
                                    if (stop) {
                                        break;
                                    }
                                }
                                if (Thread.interrupted()) {
                                    dist = null;
                                    return null;
                                }
                                if (i % 10 == 0) {
                                    pm.setProgress(i);//pr2.JProgressBar2.setValue(i);
                                }
                            }
                        } else {   //No nulls, do not use the getnulls matrix.
                            for (int i = 0; i < n; i++) {
                                for (int j = 0; j < i; j++) {
                                    dist[i][j] = (float) jmath.dist(dst[i], dst[j]);

                                }
                                if (Thread.interrupted()) {
                                    dist = null;
                                    return null;
                                }
                                if (i % 10 == 0) {
                                    pm.setProgress(i);//pr2.JProgressBar2.setValue(i);
                                }
                            }
                        }

                        if (distanceClustering && clusterWay == COLUMNS) {
                            tmpDist = dist;
                        }

                    }
                } catch (OutOfMemoryError er) {
                    ErrorMsg msg = new ErrorMsg(5);
                    dist = null;
                    stop = true;
                    //calc.mc = null;
                    pm.close();
                    System.gc();
                }

                return dist;

            }

            //Runs on the event-dispatching thread.
            @Override
            public void finished() {
                if (dist == null) {
                    return;
                }

                controlDistanceMatrix(dist);

                if (mode == 3) {
                    if (!distanceMatrixOnly) {
                        toproot = makeclusters(true, true, dist);
                    }
                    gocluster();
                    return;
                }

                if (mode != 1) {


                    if (!distanceMatrixOnly) {
                        trunk = makeclusters(true, false, dist);
                    }
                    if (!ClusterColumns) {
                        gocluster();
                    } else {
                        mode = 3;
                        calcdist(true); //recursive call for the top component..
                    }

                }

                pm.close();
            }
        };
        worker.start();
    }

    public void controlDistanceMatrix(float[][] dist) {

        float max = 0;
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[0].length; j++) {
                if (dist[i][j] > max) {
                    max = dist[i][j];
                }
            }
        }

        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[0].length; j++) {
                if (Float.isNaN(dist[i][j])) {
                    dist[i][j] = max;
                }
                if (Float.isInfinite(dist[i][j])) {
                    dist[i][j] = max;
                }
            }
        }

    }

    public Node clonetree(Node trunk) {

        return (Node) trunk.clone();

    }

    public Node getNodeAt(int xcor, int ycor, Node trunk) {
        Node ret = null;

        if (trunk != null) {

            if (trunk.getx() > xcor - squareL && trunk.getx() < xcor + squareL
                    && trunk.gety() > ycor - squareL && trunk.gety() < ycor + squareL) {
                ret = trunk;
            } else {
                ret = getNodeAt(xcor, ycor, trunk.right);
            }
            if (ret == null) {
                ret = getNodeAt(xcor, ycor, trunk.left);
            }
        }
        return ret;
    }

    @Override
    public void dataHasChanged() {
        if (distanceClustering) {
            return;
        }
    }

    public Rectangle getScaleBounds() {
        if (!paintScale) {
            return new Rectangle(0, 0);
        }
        return new Rectangle(200, 35);
    }

    public Image generateScale() {

        Rectangle r = getScaleBounds();
        float width = (float) r.width;
        float height = (float) r.height;

        BufferedImage buff = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics off = buff.getGraphics();
        drawScale(off, new Point(0, 0), width, height);
        return buff;
    }

    public void drawScale(Graphics off, Point st, float width, float height) {

        Rectangle r = new Rectangle(st.x, st.y, (int) width, (int) height);
        if (off.getClipBounds() != null && !off.getClipBounds().intersects(r)) {
            return;
        }

        if (width < 50 || height < 25) {
            return;
        }
        ScaleAndAxis sc = new ScaleAndAxis();
        sc.setColorFactory(ColorFactoryList.getInstance().getActiveColorFactory(data));
        off.translate(st.x, st.y);
        Rectangle bac = off.getClipBounds();
        sc.setLocation(r.x, r.y);
        sc.setSize(r.width, r.height + 10);
        sc.paintComponent(off);
        off.setClip(bac);
        off.translate(-st.x, -st.y);

    }

    private boolean[] createVisibleRowIndexes() {
        boolean[] ret = null;



        try {

            if (ret.length == data.getDataWidth() + 2) {
                return ret;
            }
        } catch (Exception e) {
        }

        ret = new boolean[data.getDataWidth() + 2];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = true;
        }
        return ret;
    }

    @Override
    public void selectionChanged(Selection.TYPE type) {
        repaint();
    }
}
