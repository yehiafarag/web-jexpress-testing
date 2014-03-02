package no.uib.jexpress_modularized.linechart.visualization;

import no.uib.jexpress_modularized.core.visualization.charts.LabelAxis;
import no.uib.jexpress_modularized.core.visualization.charts.ChartLabel;
import no.uib.jexpress_modularized.core.visualization.charts.Axis;
import no.uib.jexpress_modularized.core.visualization.LineStyles.LineMark;
import no.uib.jexpress_modularized.core.visualization.charts.GUI.LineProps2;
import no.uib.jexpress_modularized.core.visualization.*;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.visualization.Print.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;
import no.uib.jexpress_modularized.core.dataset.Group;
//import mainGraph;

//import org.freehep.graphics2d.VectorGraphics;
//import org.freehep.util.export.ExportDialog;
/**
 *
 * @author bjarte
 */
public class LineChart extends JComponent implements Scrollable, Serializable {

    public JScrollPane scrollPane = new JScrollPane();
    boolean start = true;
    private JeToolTip tip;
    BackgroundFactory bgf = new BackgroundFactory(new boolean[]{false, true, true, true, true, true, true, true, true, true, true});
    public LineProps2 sp;
    public Color dotColor = Color.red;
    boolean FormUpdated = false; //for initially insert values to properties dialog.
    // public Color[] dotColors = null;
    public boolean[] inActiveGroup = null;
    public boolean[] members = null;
    public Hashtable props;
    Dataset data = null;//new DataSet(new double[][]{{0.0,0.0,0.0},{0.0,0.0,0.0},{0.0,0.0,0.0}},new String[][]{{"  ","  ","  "},{"  ","  ","  "}},new String[]{"  ","  ","  "})    ;
    public Axis yaxis;
    public LabelAxis xaxis;
    public ChartLabel topText = new ChartLabel("");
    //scrollpic scp;
    private int maxUnitIncrement = 10; //For the scrollable interface..
    public double minthresholdpercent = 15.0;
    private double minthreshold = 0.0; //settes i paint..
    public int minover = 1;
    public int minVisible = 1000;
    private boolean useSpeeding = true;
    public boolean enableSpeeding = true;
    public Dimension size = new Dimension(400, 400);
    public boolean HorizontalLinesOnly = true;
    Cursor openHand, closedHand;
    public boolean shadow = true;
    //TODO: document this field (draw)
    public boolean[] draw;
    public float dashSize = 5;
    public boolean multiGroups = true;
    //This contains columns which corresponds to gaps in the lines..
    private boolean[] bgaps;
    private boolean rangeChanged = false;
    public boolean grayScale = false;
    public boolean FullRepaint = true;
    public boolean LockFullRepaint = false;
    public BufferedImage plot, MainPic;
    public boolean move = false;
    public boolean frame = true;
    public Rectangle valueArea;
    public Rectangle valueframe;  //The frame created by mouse drag
    public boolean paintTags = true;

    public void setAntialias(boolean antialias) {
        this.antialias = antialias;
    }
    public int TagFrequency = 3;
    public int TagOffset = 5;
    //public int gridtrans=0;
    //public Color shadowColor=new Color(220,220,220,150);
    public Color shadowColor =Color.LIGHT_GRAY;// new Color(160, 160, 160);
    public int lineWidth = 1;
    public boolean antialias = false;
    public boolean zoomed = false;
    //For bordering (profiler functions) This is the upper (profile[0]) and lower (profile[1]) borders..
    public int[][] profiler;
    public Vector selectedPoints; //For profiler points that are to be painted another color..
    public int bottom = 45;
    public int left = 60;
    public int top = 10;
    public int right = 20;
    private int mouseX, mouseY;
    private boolean mouseDrag = false;
    // public boolean groupMeans=true;
    int startlabel;
    int endlabel;
    private int startx, starty, endx, endy;  //For mouse event framing
    private Point clickedPoint = new Point(0, 0);
    private int hscrollBarClickPos = 0;
    private int vscrollBarClickPos = 0;

    public LineChart() {
        this(null, null, null);
        setPreferredSize(new Dimension(400, 400));
    }

    private void createBGaps(int[] gaps) {
        bgaps = new boolean[data.getDataWidth()];
        //java.util.Arrays.fill(bgaps,f);
        for (int i = 0; i < gaps.length; i++) {
            bgaps[gaps[i]] = true;
        }
    }

    public LineChart(Hashtable props, Dataset data, final JScrollPane scroll) {
       

        //@TODO: reimplement me ?
        //NOTE: "structures" doesn't seem important
//        if (data != null && data.structures != null && data.structures.containsKey("gaps")) {
//            int[] gaps = (int[]) data.structures.get("gaps");
//            createBGaps(gaps);
//        }


        if (data != null) {
            this.data = data;
        }
        if (scroll != null) {
            scrollPane = scroll;
        }

        this.props = props;


        sp = getPropsWindow();


        createDummy();
        init();


        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent me) {
                if (move) {
                    Point p = me.getPoint(); //p.x=p.x-(clickedPoint.x-p.x);

                    //clickedPoint.x+=(clickedPoint.x-p.x);

                    LineChart.this.scrollPane.getHorizontalScrollBar().setValue(hscrollBarClickPos + (clickedPoint.x - me.getX()));
                    //LineChart.this.scrollPane.getHorizontalScrollBar().setValue(hscrollBarClickPos+(clickedPoint.x-p.x));
                    LineChart.this.scrollPane.getVerticalScrollBar().setValue(vscrollBarClickPos + (clickedPoint.y - me.getY()));
                    //System.out.print("\n"+ hscrollBarClickPos + (clickedPoint.x - me.getX()) );
                    //clickedPoint=me.getPoint();
                    //clickedPoint.x-=(clickedPoint.x-me.getX());
                    hscrollBarClickPos = LineChart.this.scrollPane.getHorizontalScrollBar().getValue();
                    vscrollBarClickPos = LineChart.this.scrollPane.getVerticalScrollBar().getValue();

                } else if (frame) {
                    if (valueframe == null) {
                        return;
                    }

                    endx = me.getX();
                    endy = me.getY();

                    int frstartx = valueArea.x;
                    int frendx = valueArea.x + valueArea.width;

                    int frstarty = valueArea.y;
                    int frendy =
                            valueArea.y + valueArea.height;

                    endx = Math.min(endx, frendx);
                    endx = Math.max(endx, frstartx);
                    endx = Math.min(endx, frendx);
                    endx = Math.max(endx, frstartx);

                    endy = Math.min(endy, frendy);
                    endy = Math.max(endy, frstarty);
                    endy = Math.min(endy, frendy);
                    endy = Math.max(endy, frstarty);

                    int sqxstart = Math.min(startx, endx);
                    int sqystart = Math.min(starty, endy);
                    int sqxend = (Math.max(startx, endx)
                            - Math.min(startx, endx));
                    int sqyend = (Math.max(starty, endy)
                            - Math.min(starty, endy));


                    valueframe.setBounds(sqxstart, sqystart, sqxend, sqyend);
                    mouseDrag =
                            true; //System.out.print("\ndrag"); repaint(); //packupComponent(); }
                }
            }
        });

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent me) {

                if (me.getModifiers() == me.BUTTON3_MASK) {
                    JRootPane root =
                            getRootPane();
                    sp = getPropsWindow();
                    sp.setLocationRelativeTo(root);

                    sp.pack();
                    sp.show();
                }

            }
            @Override
            public void mousePressed(MouseEvent me) {
                startx = me.getX();
                starty = me.getY();
                endx = me.getX();
                endy = me.getY();

                if (me.getModifiers() != me.BUTTON3_MASK) {

                    if (move) {
                        clickedPoint.setLocation(me.getPoint());
                        hscrollBarClickPos = LineChart.this.scrollPane.getHorizontalScrollBar().getValue();
                        vscrollBarClickPos = LineChart.this.scrollPane.getVerticalScrollBar().getValue();

                        // setCursor(new Cursor(Cursor.MOVE_CURSOR ));

                    } else if (frame) {
                        startx = me.getX();
                        starty = me.getY();
                        endx = me.getX();
                        endy = me.getY();

                        int frstartx = valueArea.x;
                        int frendx = valueArea.x + valueArea.width;

                        int frstarty = valueArea.y;
                        int frendy =
                                valueArea.y + valueArea.height;

                        if (startx < frendx && startx > frstartx && starty < frendy
                                && endy > frstarty) {
                            valueframe = new Rectangle(startx, starty, 0, 0);
                        }

                        repaint();
                    }
                }
            }
        });


        scrollPane.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) { //size=new Dimension(xaxis.predictLength() + yaxis.predictWidth(), LineChart.this.scrollPane.getSize().height);
                forceFullRepaint();
            }
        });


    }

    public LineProps2 getPropsWindow() {

        Component owner = null;
        if (getRootPane() != null) {
            owner = getRootPane().getTopLevelAncestor();
        }
        LineProps2 sp = new LineProps2((JFrame) owner, true);



        sp.copy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ImageHandler.ImageToClipBoard(getImage());
            }
        });


        KeyListener k = new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                rangeChanged = true;
            }
        };


        sp.ymin.addKeyListener(k);
        sp.ymax.addKeyListener(k);

        bgf.fillCombo(sp.SbgSV);
        bgf.fillGradCombo(sp.gradtypeSV);

        sp.reset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //yaxis.resetAxis(Ry);

                yaxis.setValueRange(LineChart.this.data.getMinMeasurement(), LineChart.this.data.getMaxMeasurement());
                yaxis.setManualRange(false);
                //yaxis.force_end_labels=false;

                rangeChanged = false;
                //updateAxisValues();
                forceFullRepaint();
            }
        });


        sp.ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                readForm();
                forceFullRepaint();
            }
        });


        sp.defaults.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                writeValues();
            }
        });



        readValues(sp);



        return sp;
    }

    //public void setPropertiesAndScrollPane(Hashtable p, JScrollPane sp) {
    public void setPropertiesAndScrollPane(JScrollPane sp) {
        scrollPane = null;

        if (sp != null) {
            sp.addComponentListener(new ComponentAdapter() {

                public void componentResized(ComponentEvent e) {
                  //  System.out.println("the lol fole is ( "+xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth()+"  ,  "+LineChart.this.scrollPane.getViewport().getSize().height);
                  //  size=new Dimension(xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth(),LineChart.this.scrollPane.getViewport().getSize().height);
                    int width = 0;
                    if((xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth()) > 400)
                            width = (xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth());
                    else
                        width = 400; 
                    size=new Dimension(width,400);
                 //  System.out.println("the lol fole is ( "+(size.width)+"  ,  "+size.height);
                  //
                    // size.setSize(500, 400);
                    setPreferredSize(new Dimension(size.width,size.height));
                    setSize(new Dimension(size.width,size.height));
                    forceFullRepaint();
                }
            });
        }
      //  size=new Dimension(xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth(),sp.getViewport().getSize().height);
       // size=new Dimension(200,200);
        int width = 0;
        if((xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth()) > 400)
                width = (xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth());
        else
            width = 400;
        size=new Dimension(width,400);
        setPreferredSize(new Dimension(size.width,size.height));
        setSize(new Dimension(size.width,size.height));
        scrollPane = sp;
        //this.props = p;
        readValues(this.sp);
        readForm();
        forceFullRepaint();
    }

    public Rectangle getValueBounds() {
        return new Rectangle(valueArea.x - 1, valueArea.y - 1, valueArea.width + 2, valueArea.height + 2);
    }

    public Rectangle getSelectionBounds() {
        return new Rectangle(valueframe.x - 1, valueframe.y - 1, valueframe.width + 2, valueframe.height + 2);
    }

    /*
     * public void updateAxisValues(){ sp.ymin.setValue(yaxis.minimum);
     * sp.ymax.setValue(yaxis.maximum); }
     */
    public void createDummy() {
        //NOTE: tautology: rest of method is dead code. Don't know why it's here
        if (true) {
            return;
        }

        boolean[] gr = new boolean[data.getDataLength()];
        for (int i = 0; i < 1; i++) {
            gr[i] = true;
        }

        //data.addGroup(new Boolean(true),"testgroup",Color.red,gr,false);


        boolean[] gr2 = new boolean[data.getDataLength()];
        for (int i = 0; i < 2; i++) {
            gr2[i] = true;
        }

        //data.addGroup(new Boolean(true),"testgroup",Color.green,gr2,false);


        /*
         * draw=new boolean[data.getDataLength()]; draw[4]=true; draw[2]=true;
         * draw[1]=true; draw[8]=true;
         */
    }

    public void setData(Dataset data) {

        //@TODO: reimplement me 
//        if (data != null && data.structures != null && data.structures.containsKey("gaps")) {
//            int[] gaps = (int[]) data.structures.get("gaps");
//            createBGaps(gaps);
//        }
//
        this.data = data;
        yaxis.setValueRange(data.getMinMeasurement(), data.getMaxMeasurement());

        boolean[] visibleRows = data.getusedColInfos();//createVisibleRowIndexes();
        xaxis.setLabels(data.getColInfos(), visibleRows);




        //size=new Dimension(xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth(),LineChart.this.scrollPane.getViewport().getSize().height);

        //System.out.print("\nwidth: "+size.width+ ", height: "+size.height);

        //setPreferredSize(size);

        //setSize(new Dimension(xaxis.predictLength()+yaxis.predictWidth(),Height()));
        readForm();
        updateForm();
        forceFullRepaint();
    }

    public void setXaxisLabels() {

        boolean[] visibleRows = null;

        String[][] labels = new String[endlabel - startlabel][data.getColInfos()[0].length];

        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[0].length; j++) {
                labels[i][j] = data.getColInfos()[i + startlabel][j];
            }
        }

        //NOTE: fix
        visibleRows = data.getusedColInfos();//createVisibleRowIndexes();

        xaxis.setLabels(labels, visibleRows);


        //size=new Dimension(xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth(),LineChart.this.scrollPane.getViewport().getSize().height);
        //setPreferredSize(new Dimension(size.width,size.height));


    }

    public void setMembers(boolean[] members) {
        this.members = members;
        forceFullRepaint();

    }

    public boolean[] getMembers() {
        return members;

    }

    public void setData(Dataset data, int startlabel, int endlabel, double max, double min, boolean[] members) {
        this.data = data;

        //@TODO: reimplement me 
//        if (data != null && data.structures != null && data.structures.containsKey("gaps")) {
//            int[] gaps = (int[]) data.structures.get("gaps");
//            createBGaps(gaps);
//        }

        this.startlabel = startlabel;
        this.endlabel = endlabel;
        this.members = members;
        zoomed = true;
        yaxis.setManualRange(true);
        //yaxis.force_end_labels=false;
        yaxis.setValueRange(min, max);
        setXaxisLabels();

        //setSize(new Dimension(xaxis.predictLength()+yaxis.predictWidth(),Height()));
        readForm();
        updateForm();
        forceFullRepaint();
    }

    public double[] getValueFrame() {
        double[] ret = null;

        if (valueframe == null) {
            return null;
        } else {
            ret = new double[4];
            //System.out.print("\nStartLabel: "+xaxis.getLeftLabel(startx));
            //System.out.print("\nEndLabel: "+xaxis.getRightLabel(endx));
            ret[0] = yaxis.getDouble(valueframe.y);
            ret[1] = yaxis.getDouble(valueframe.y + valueframe.height);
            ret[2] = (double) xaxis.getLeftLabel(startx);
            ret[3] = (double) xaxis.getRightLabel(endx) + 1;

        }

        // else return new Rectangle( yaxis.getDouble(valueframe.x,valueframe.y,
        // if(valueframe==null) return null;

        // mainGraph gr= new mainGraph(data,cl,false);

        return ret;

    }

    public void setData(Dataset data, boolean[] members) {

        this.data = data;
        this.members = members;
        
        
       
        yaxis.setValueRange(data.getMinMeasurement(), data.getMaxMeasurement());

        //@TODO: reimplement me?
        //'structure' Doesn't seem important, judging by the description in DataSet :
        //              "//for storing sessionspecific structure such as tablemodels..."
//        if (data != null && data.structures != null && data.structures.containsKey("gaps")) {
//            int[] gaps = (int[]) data.structures.get("gaps");
//            createBGaps(gaps);
//        }

        //NOTE: seems important
        //TODO: fix
        boolean[] visibleRows =data.getusedColInfos();//createVisibleRowIndexes();
        //String[][] table = getColInfos(data);
        
        xaxis.setLabels(data.getColInfos(), visibleRows);

        size = new Dimension(xaxis.predictLength() + xaxis.endLength() + yaxis.predictWidth(), Math.max(100, LineChart.this.scrollPane.getSize().height));
        //System.out.println("width: "+size.width+ ", height: "+size.height);


        readForm();
        updateForm();
        forceFullRepaint();


    }
    /*
     * //public void showPropertiesWindow(){ public void
     * showPropertiesWindow(Point Location, Frame owner){
     * sp.setLocation(LineChart.this.getLocationOnScreen().x+10,LineChart.this.getLocationOnScreen().y+10);
     * if(owner!=null) sp2.setLocationRelativeTo(owner); else
     * sp2.setLocation(Location);
     *
     * //sp.setVisible(true); sp.pack(); sp.show(); }
     */

    public void setXaxisTitle(String label) {
        xaxis.setTitleText(label);
    }

    public void setYaxisTitle(String label) {
        yaxis.setTitleText(label);
    }

    public boolean[] createVisibleRowIndexes() {

        boolean[] ret = null;
        Boolean[] ind = null;

        //@TODO: reimplement me
//        try {
//            if (data.structures.containsKey("VisibleTransposedColumns")) {
//                ind = (Boolean[]) data.structures.get("VisibleTransposedColumns");
//                ret = new boolean[ind.length];
//                for (int i = 0; i < ret.length; i++) {
//                    ret[i] = ind[i].booleanValue();
//                }
//
//            }
//        } catch (Exception e) {
//            //ind= new Boolean[data.getColInfoHeaders().length];
//            // ret=new boolean[data.getColInfoHeaders().length];
//            // for(int i=0;i<ret.length;i++) ret[i]=true;
//            
//            e.printStackTrace();
//        }

        /*
         * try{ JCheckBox[] ch = (JCheckBox[])
         * data.structures.get("VisibleTransposedColumns"); ret=new
         * boolean[ch.length]; for(int i=0;i<ret.length;i++)
         * ret[i]=ch[i].isSelected();
         * if(ret.length==data.getColInfoHeaders().length+2) return ret; }
         * catch(Exception e){ }
         *
         * ret=new boolean[data.getColInfoHeaders().length+2]; for(int
         * i=0;i<ret.length;i++) ret[i]=true;
         */
        return ret;


    }

    public void init() {


        //setOpaque(true); //setBackground(new Color(210,204,204));
        //scrollPane.getViewport().setBackground(getBackground());

        setForeground(Color.black);


        if (scrollPane != null) {
            scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, new JPanel());
        }




        if (data != null) {
            yaxis = new Axis(1, this, data.getMinMeasurement(), data.getMaxMeasurement());
        } else {
            yaxis = new Axis(1, this, -10, 10);
        }

        yaxis.correctForCloseValues = false;

        boolean[] visibleRows = createVisibleRowIndexes();

        int cnt = 0;


        //if(data==null) System.out.print("\nNO DATA");


        if (data != null) {
            xaxis = new LabelAxis(0, this, data.getColInfos(), visibleRows);
        } else {
            xaxis = new LabelAxis(0, this, new String[][]{{"s"}, {" "}}, new boolean[]{true, true});
        }

        //if(true) return;

        xaxis.axiscolor = getForeground();
        yaxis.axiscolor = getForeground();

        yaxis.dropFirstGridLine = true;
        yaxis.dropLastGridLine = true;
        xaxis.dropFirstGridLine = true;
        xaxis.dropLastGridLine = true;



        setXaxisTitle("");
        setYaxisTitle("");
        xaxis.setTitleFont(new Font("Times New Roman", 1, 15));
        yaxis.setTitleFont(new Font("Times New Roman", 1, 15));

        topText.setFont(new Font("Times New Roman", 1, 16));

        //NOTE: a tautology, so the rest of the code of the method is in effect unreachable. 
        //not removed since it is the same in JExpress, and that seems to work
        if (true) {
            return;
        }


        //setSize(new Dimension(xaxis.predictLength()+yaxis.predictWidth(),350)); sp = getPropsWindow();

        readForm();
        updateForm();
        FormUpdated = true;
        forceFullRepaint();



    }

// public void packupComponent(){
    /*
     * if(MainPic==null || MainPic.getWidth()!=getPreferredSize().width ||
     * MainPic.getWidth()!=getPreferredSize().height){ MainPic=new
     * BufferedImage(getPreferredSize().width,getPreferredSize().height,
     * BufferedImage.TYPE_INT_ARGB); }
     *
     * Graphics tempg=MainPic.getGraphics(); //readForm();
     *
     * FullRepaint=true; // paintChart(tempg); if(scp!=null)scp.repaint();
     * repaint(); parent.getViewport().repaint();
     *
     * if(scp==null){scp = new scrollpic(new ImageIcon(MainPic),1);
     *
     * parent.setViewportView(scp); } else scp.setIcon(new ImageIcon(MainPic));
     *
     * // scp.setBorder(new javax.swing.border.EtchedBorder());
     */
//      repaint();
//  }
    //TODO: document this method
    public void setDraw(boolean[] draw) {
        this.draw = draw;
        forceFullRepaint();
    }
    public boolean[] getDataSelection(int[] selectedRaw)
    {
         boolean[] selection = new boolean[data.getDataLength()];
            for(int index=0;index<data.getDataLength();index++)
            {
                boolean  selected =false;
                for(int x:selectedRaw)
                {
                    if(index == x)
                    {
                        selected = true;
                        break;
                    }
                }
                selection[index] = selected;
//                System.out.println(index+"  --> "+selected);
            }
            return selection;
                     
    
    }

    public boolean[] getDraw() {
        return draw;
    }

//public int Height(){return getSize().height;}
//public int Width(){return getSize().width;}
    public int Height() {
        return getSize().height;
    }//size.height;}

    public int Width() {
        return getSize().width;
    }//return size.width;}

//   public int Height(){return 300;}
//   public int Width(){return 450;}
    public void forceFullRepaint() {
        FullRepaint = true;
        repaint();
    }
    @Override
    public void paintComponent(Graphics gr) {

        int[] x = null;
        Graphics g = null;
        //public double minthresholdpercent = 10;
        //private double minthreshold = 0.0; //settes i paint..


        //things for speeding up drawing..

        //turned off... still to difficult!
        if (data != null) {
            double mm = data.getMaxMeasurement();
            if (mm < Math.abs(data.getMinMeasurement())) {
                mm = Math.abs(data.getMinMeasurement());
            }
            minthreshold = (minthresholdpercent / 100.0) * mm;
        }

        // System.out.print("\nmint : "+minthreshold);
        //  System.out.print("\nmintp : "+minthresholdpercent);
        int totalvis = totalVisble();
        int totalshad = totalShadowed();


        useSpeeding = false;
        //if(totalvis>minVisible) useSpeeding=true;

        if ((totalvis > minVisible && (totalshad > minVisible || totalshad == 0))) {
            useSpeeding = true;
        } else {
            useSpeeding = false;
        }





        if (useSpeeding && !enableSpeeding) {
            useSpeeding = enableSpeeding; //enablespeeding is set globally and has veto
        } //   if(useSpeeding) System.out.print("\nPP");
        //   else System.out.print("\nNN");



        if (LockFullRepaint) {
            FullRepaint = true;
        }
        boolean bufferAll = true; //Are we buffering all in a bufferedImage or drawing directly?
        if (FullRepaint || LockFullRepaint || !bufferAll) {
            if (props != null && props.containsKey("WholeLine")) {
                HorizontalLinesOnly = (((Boolean) props.get("WholeLine")).booleanValue());
            }
            
            HorizontalLinesOnly = false; // @TODO: should not be hardcoded...
            
            yaxis.force_end_labels = sp.endlabelsSV.isSelected();
//            if (!LockFullRepaint && bufferAll) {
//                if (!grayScale) {
//                    plot = new BufferedImage(Width(), Height(), BufferedImage.TYPE_INT_RGB);
//                } else {
//                    plot = new BufferedImage(Width(), Height(), BufferedImage.TYPE_BYTE_GRAY);
//                }
//                g = plot.getGraphics();
//            } else {
//                g = gr;
//            }
            
            g = gr; // @TODO: should not be hardcoded, but using the BufferedImage does not seem to work...
            
            if (rangeChanged && !zoomed) {
                double max = sp.ymax.getValue();
                double min = sp.ymin.getValue();
                if (min < max) {
                    yaxis.setManualRange(true);
                    yaxis.setValueRange(min, max);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid bounds for chart in dataset\nContinuing with precalculated bounds.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (zoomed) {
                yaxis.setManualRange(true);
                // if(data.structures.containsKey("ForcedEndLabels") && !zoomed)yaxis.force_end_labels= new Boolean((String)data.structures.get("ForcedEndLabels")).booleanValue();
                yaxis.force_end_labels = false;
            }




            if (data == null) {
                return;
            }
            double[][] dat = data.getData();
            boolean[] painted = new boolean[data.getDataLength()];
            int[] y = new int[dat[0].length];
            if (y.length == 1 && !HorizontalLinesOnly) {
                return;
            }
            int TitleHeight = topText.getHeight(g);
            int HTitlePos = Width() / 2 - (topText.getWidth(g) / 2);
            if (Width() == 0 || Height() == 0) {
                return;
            }
            int xaxisLength = xaxis.predictLength();
            g.setColor(getBackground());
            g.fillRect(0, 0, Width(), Height());
            g.setColor(getForeground());
            topText.draw(g, HTitlePos, top + 5);
            left = yaxis.getAxisWidth(g);
            bottom = xaxis.getAxisWidth(g);
            if (!xaxis.positionAxis(left, left + xaxisLength, Height() - bottom, Height() - bottom)) {
                System.out.print("\nFailed to init xaxis");
            }
            if (!yaxis.positionAxis(left, left, top + TitleHeight, Height() - bottom)) {
                System.out.print("\nFailed to init yaxis");
            }
            xaxis.prepareLocations(g);
            x = xaxis.ylocations;



            yaxis.calculateGridLabels();



            yaxis.data_window.setSize(xaxisLength, 10);
            xaxis.data_window.setSize(10, Height() - bottom - top - TitleHeight);
            valueArea = new Rectangle(left, top + TitleHeight, xaxisLength, Height() - bottom - top - TitleHeight);

            //valueArea=this.getVisibleRect();
            if (!LockFullRepaint) {
                g.setClip(valueArea);
            }
            bgf.externalImage = sp.pathSV.getText();
            bgf.tileImages = sp.tileSV.isSelected();
            bgf.paintBackground(g, valueArea, sp.SbgSV.getSelectedIndex());
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(lineWidth));

            if (antialias) {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            } else {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            }
            Group group = null;
            boolean activeSet = false;
            Color classColor = null;

            //Generate the painting order of the lines..----------------------------
            int[] paintorder = new int[data.getDataLength()];
            LineMark mark = null;
            Vector[] paintColors = null;
            Color[] singleColors = null;
            Vector[] Tags = null;

            ArrayList<Group> rowGroups = null;
            //If only single colors are chosen, drawing will be much faster and use less memory
            //if we consider this in this drawing section.
            if (multiGroups) {
                paintColors = new Vector[data.getDataLength()];
            } else {
                //NOTE: from JExpress
//                singleColors = new Color[data.getGroups().size()];
                //NOTE: attempt to reimplement the same functionality with modularized DataSet class:
                rowGroups = (ArrayList<Group>) data.getRowGroups();
                singleColors = new Color[rowGroups.size()];
            }
            if (paintTags) {
                Tags = new Vector[data.getDataLength()];
            }
            for (int i = 0; i < paintorder.length; i++) {
                paintorder[i] = -1;  //This makes sure that rows not member in any visible group will not be painted
            }
            Font fbefore = g.getFont();
            Color cbefore = g.getColor();

            //NOTE: from JExpress
//            int[][] integers = null;
//            Color[] allGroupColors = new Color[data.getGroups().size()];
//            int[][] groupColor = new int[data.getGroups().size()][];
//            LineMark[] allmarks = new LineMark[data.getGroups().size()];
//            int[][] groupMark = new int[data.getGroups().size()][];

            //NOTE: attempt to reimplement the same functionality with modularized DataSet class:
            int[][] integers = null;
            Color[] allGroupColors = new Color[data.getRowGroups().size()];
            int[][] groupColor = new int[data.getRowGroups().size()][];
            LineMark[] allmarks = new LineMark[data.getRowGroups().size()];
            int[][] groupMark = new int[data.getRowGroups().size()][];


            Vector tmpGroupMark = new Vector();
            int[] buffer = new int[data.getDataLength()];
            int bufferPointer = 0;
            int[] markbuffer = new int[data.getDataLength()];
            int markbufferpointer = 0;
            if (zoomed) {
                integers = yaxis.getInteger(data.getData(), startlabel, endlabel);
            } else {
                integers = yaxis.getInteger(data.getData());
            }

            Rectangle visible = this.getVisibleRect();
            // System.out.print("\npainting: "+visible.toString());

            Color allColor = null;


            //NOTE: all calls the method "getGroups()" have been replaced with a call to method "getRowGroups()"
            if (data.getRowGroups().size() > 0) {
                for (int j = data.getRowGroups().size() - 1; j > -1; j--) {
                    group = (Group) data.getRowGroups().get(j); //NOTE: was method call elementAt(j) (but that was for the class Vector)
                    activeSet = group.isActive();//(Boolean)Class.elementAt(0);

                    //NOTE: seems important (The GraphView view in J-Express glitches out if this part is commented out)
                    allGroupColors[j] = group.getColor();//(Color) Class.elementAt(2);

                    //TODO: reimplement me
//                    if (group.getLineMark() != null) {
//                        allmarks[j] = group.getLineMark();//(LineMark)Class.elementAt(5);
//                    }

                    if (j == data.getRowGroups().size() - 1) {
                        allColor = group.getColor();//(Color)Class.elementAt(2);
                    }
                    if (activeSet) {
                        for (int i = 0; i < painted.length; i++) {
                            if ((members == null || members[i])) {
                                if (multiGroups) {
                                    if (group.hasMember(i) && j != data.getRowGroups().size() - 1) {
                                        buffer[bufferPointer] = (int) i;
                                        bufferPointer++;
                                    }
                                    if (group.hasMember(i) && paintTags) {
                                        markbuffer[markbufferpointer] = (int) i;
                                        markbufferpointer++;
                                    }
                                } else if (group.hasMember(i)) {
                                    singleColors[j] = group.getColor();//(Color)Class.elementAt(2);
                                }
                                if (activeSet && group.hasMember(i)) {
                                    paintorder[i] = j;
                                }
                            }
                        }
                        //Create the tag and color arrays..
                    }
                    groupColor[j] = new int[bufferPointer];
                    groupMark[j] = new int[markbufferpointer];
                    for (int i = 0; i < bufferPointer; i++) {
                        groupColor[j][i] = buffer[i];
                    }
                    for (int i = 0; i < markbufferpointer; i++) {
                        groupMark[j][i] = markbuffer[i];
                    }
                    markbufferpointer = 0;
                    bufferPointer = 0;
                }
            }

            //remap matrices...
            //remapping colors------------------------------
            int[] tmpmat = new int[data.getDataLength()];
            for (int i = 0; i < groupColor.length; i++) {
                for (int j = 0; j < groupColor[i].length; j++) {
                    tmpmat[ groupColor[i][j]]++;
                }
            }
            int[][] tmpmat2 = new int[data.getDataLength()][];
            for (int i = 0; i < tmpmat2.length; i++) {
                tmpmat2[i] = new int[tmpmat[i]];
            }
            int[] counters = new int[data.getDataLength()];
            int pointer = 0;
            for (int i = 0; i < groupColor.length; i++) {
                for (int j = 0; j < groupColor[i].length; j++) {
                    pointer = groupColor[i][j];
                    tmpmat2[pointer][counters[pointer]] = (int) i;
                    counters[pointer]++;
                }
            }


            groupColor = tmpmat2;
            //Remapping colors done..
            //Remapping marks--------------------
            if (paintTags) {
                java.util.Arrays.fill(tmpmat, (int) 0);
                for (int i = 0; i < groupMark.length; i++) {
                    for (int j = 0; j < groupMark[i].length; j++) {
                        tmpmat[ groupMark[i][j]]++;
                    }
                }
                tmpmat2 = new int[data.getDataLength()][];
                for (int i = 0; i < tmpmat2.length; i++) {
                    tmpmat2[i] = new int[tmpmat[i]];
                }
                java.util.Arrays.fill(counters, 0);
                pointer = 0;
                for (int i = 0; i < groupMark.length; i++) {
                    for (int j = 0; j < groupMark[i].length; j++) {
                        pointer = groupMark[i][j];
                        tmpmat2[pointer][counters[pointer]] = (int) i;
                        counters[pointer]++;
                    }
                }
                groupMark = tmpmat2;
            }


            //-----------------------------------

            if (shadow) {
               // shadowColor = sp.ShadowColorSV.getColor();
//                if (sp.ShadowTransSV.isSelected()) {
//                    shadowColor = new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), 60);
//                }
                //Draw the shadowed rows...
                boolean useSpeed = enableSpeeding;
                if ((totalvis - totalshad) < minVisible) {
                    useSpeed = false;
                }
                //if(useSpeed) System.out.print("\nuseSpeed: 1");
                //else System.out.print("\nuseSpeed: 0");

                g.setColor(shadowColor);
                for (int i = 0; i < dat.length; i++) {
                    if ((members == null || members[i]) && draw != null && !draw[i] && paintorder[i] != -1) {
                        //if(sp.paintShadowsSV.isSelected()) {g2d.drawPolyline(x,integers[i],integers[i].length);}//drawPolyLine(x,integers[i],dat[i],g);}
                    //    if (sp.paintShadowsSV.isSelected()) {
                            drawPolyLine(x, integers[i], dat[i], g, useSpeed);
                    //    }
                        painted[i] = true;
                    }
                }
            }
            //if(true) return;

            float[] dashPattern = null;
            //Paint the lines..-----------------------------------------------------
            if (data.getRowGroups().size() > 0) {

                for (int j = data.getRowGroups().size() - 1; j > -1; j--) {
                    for (int i = 0; i < paintorder.length; i++) {
                        if ((members == null || members[i]) && !painted[i]) {
                            if (paintorder[i] == j) {
                                y = integers[i];
                                if (multiGroups) {
                                    if (groupColor[i].length > 0) {
                                        dashPattern = new float[]{dashSize, dashSize * (groupColor[i].length - 1)};
                                        for (int k = 0; k < groupColor[i].length; k++) {
                                            if (dashPattern != null) {
                                                g2d.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10, dashPattern, k * dashSize));
                                            }
                                            g.setColor(allGroupColors[ groupColor[i][k]]);
                                            drawPolyLine(x, y, dat[i], g2d, false);
                                        }
                                    } else {
                                        g.setColor(allColor);
                                        g2d.setStroke(new BasicStroke(lineWidth));
                                        drawPolyLine(x, y, dat[i], g, false);
                                    }
                                    if (paintTags && groupMark != null && groupMark[i].length > 0) {
                                        g2d.setStroke(new BasicStroke(1));
                                        int widthUsed = 0;
                                        int step = (x[1] - x[0]);
                                        int mid = step / 2;
                                        int totwidth = 0;
                                        for (int m = 0; m < groupMark[i].length; m++) {
                                            LineMark ct = allmarks[groupMark[i][m]];
                                            if (ct != null) {
                                                totwidth += ct.getBoundWidth();
                                            }
                                        }
                                        int mrk = 0;
                                        int hwdt = 0;
                                        int hhgt = 0;
                                        for (int l = 0; l < x.length; l++) {
                                            widthUsed = 0;
                                            LineMark ct = allmarks[groupMark[i][mrk % groupMark[i].length]];   //(LineMark) Tags[i].elementAt(m);
                                            mrk++;
                                            if (ct != null) {
                                                ct.paintAt(x[l], y[l], g2d);
                                            }
                                            if (ct != null) {
                                                widthUsed += ct.getBoundWidth();
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("its one group");
                                    g.setColor(singleColors[j]);
                                    drawPolyLine(x, y, dat[i], g, false);
                                }
                                painted[i] = true;
                            }
                        }
                    }
                }
            } //Should not happen.. All datasets should have a nullgroup..
            else {
                for (int i = 0; i < dat.length; i++) {
                    if (!painted[i]) {
                        y = integers[i];
                        drawPolyLine(x, y, dat[i], g, false);
                        painted[i] = true;
                    }
                }
            }
            //if(true)return;

            System.gc();
            g.setFont(fbefore);
            g2d.setStroke(new BasicStroke());
            g.setColor(getForeground());
            g2d.setClip(null);
            xaxis.axiscolor = getForeground();
            yaxis.axiscolor = getForeground();

            if (yaxis != null) {
                yaxis.drawAxis(g);
            }
            g.setClip(null);

            //TODO: reimplement me
//            xaxis.setGroupColors(data.getIndexedColumnGroupColors());


            g.setClip(null);
            if (xaxis != null) {
                xaxis.drawAxis(g);
            }
            sp.ymin.setValue(yaxis.minimum);
            sp.ymax.setValue(yaxis.maximum);
            g.setColor(getForeground());
            g.drawLine(left + 1, top + TitleHeight, left + xaxisLength, top + TitleHeight);
            g.drawLine(left + xaxisLength, top + TitleHeight, left + xaxisLength, Height() - bottom);
            firePropertyChange("painted", new Boolean(false), new Boolean(true));

        }
    }

    public void drawPolyLine(int[] x, int[] y, double[] dx, Graphics g, boolean forceall) {
        //Rectangle r = this.getVisibleRect();
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        if (HorizontalLinesOnly) {
            int step = ((x[1] - x[0]) / 2) - 3;


            for (int i = 0; i < x.length; i++) {
                x1 = x[i] - step;
                y1 = y[i];
                x2 = x[i] + step;
                if (!Double.isNaN(dx[i])) {
                    //if(getVisibleRect().contains(x1,y1) || getVisibleRect().contains(x2,y1))
                    g.drawLine(x1, y1, x2, y1);
                }
            }
        } else {

//    if(useSpeeding) System.out.print("\nspeed: 1");
//    else System.out.print("\nspeed: 0");
//    if(forceall) System.out.print("\nforce: 1");
//    else System.out.print("\nforce: 0");

            if (!overcenterdist(dx) && (useSpeeding || forceall)) {
//        System.out.print("-");
                return;
            }

            for (int i = 0; i < x.length - 1; i++) {
                if (!Double.isNaN(dx[i]) && !Double.isNaN(dx[i + 1])) {

                    if (bgaps == null || !bgaps[i]) {
                        x1 = x[i];
                        x2 = x[i + 1];
                        y1 = y[i];
                        y2 = y[i + 1];
                        //if(r.contains(x1,y1) || r.contains(x2,y1)) 
                        g.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }
    }

    public int totalVisble() {
        int ret = 0;
        if (data == null) {
            return 0;
        }
        //if(draw!=null){
        //    for(int i=0;i<draw.length;i++) if(draw[i]) ret++;
        //    return ret;
        //}
        if (members == null) {
            return data.getDataLength();
        }
        for (int i = 0; i < members.length; i++) {
            if ((members != null && members[i])) {
                ret++;
            }
        }
        return ret;
    }

    public int totalShadowed() {
        int ret = 0;
        if (data == null) {
            return 0;
        }
        if (draw != null) {
            for (int i = 0; i < draw.length; i++) {
                if (draw[i]) {
                    ret++;
                }
            }
            return ret;
        }
        return ret;
    }

    public Image getProfilerImage() {
        BufferedImage cl = new BufferedImage(plot.getWidth(), plot.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics gr = cl.getGraphics();
        gr.drawImage(plot, 0, 0, this);

        if (profiler != null) {
            //gr.setClip(null);
            gr.setClip(((JViewport) getParent()).getViewRect());
            Graphics2D g2d = (Graphics2D) gr;

            int[] x = xaxis.ylocations;
            for (int i = 0; i < profiler[0].length; i++) {
                g2d.setStroke(new BasicStroke(1));
                gr.setColor(Color.green);
                if (selectedPoints != null) {
                    for (int j = 0; j < selectedPoints.size(); j++) {
                        if (((Point) selectedPoints.elementAt(j)).x == i && ((Point) selectedPoints.elementAt(j)).y == 0) {
                            gr.setColor(Color.red);
                        }
                    }
                }

                gr.drawRect(x[i] - 3, profiler[0][i] - 3, 6, 6);
                gr.drawLine(x[i] - 8, profiler[0][i], x[i] + 8, profiler[0][i]);

                gr.setColor(Color.green);
                if (selectedPoints != null) {
                    for (int j = 0; j < selectedPoints.size(); j++) {
                        if (((Point) selectedPoints.elementAt(j)).x == i && ((Point) selectedPoints.elementAt(j)).y == 2) {
                            gr.setColor(Color.red);
                        }
                    }
                }
                gr.drawRect(x[i] - 3, profiler[1][i] - 3, 6, 6);
                gr.drawLine(x[i] - 8, profiler[1][i], x[i] + 8, profiler[1][i]);

                gr.setColor(new Color(160, 250, 160, 160));
                g2d.setStroke(new BasicStroke(2));
                if (profiler[0][i] > profiler[1][i]) {
                    gr.setColor(new Color(250, 160, 160, 160));
                }
                gr.drawLine(x[i], profiler[0][i], x[i], profiler[1][i]);


            }


        }

        return cl;
    }

    public boolean overcenterdist(double[] dat) {
        //public double minthresholdpercent = 10;
        //public int minover = 2;
        int cnt = 0;
        for (int i = 0; i < dat.length; i++) {
            if (Math.abs(dat[i]) > minthreshold) {
                cnt++;
                if (cnt == minover) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setProfiler(int[][] profiler) {
        this.profiler = profiler;
        repaint();
    }

//If this is bordered by profiler arrays, return the point at x,y or null if the profiles are empty or no hit is found.
    public Point getProfilerPointAt(int x, int y) {
        Point res = null;
        Rectangle r = null;

        if (profiler == null) {
            System.out.print("\nProfiles are null!");
            return null;
        } else {

            // System.out.print("\nLooking for points at:" +)


            for (int i = 0; i < profiler[0].length; i++) {
                r = new Rectangle(xaxis.ylocations[i] - 3, profiler[0][i] - 3, 6, 6);
                if (r.contains(x, y)) {
                    res = new Point(i, 0);
                    break;
                }
                r = new Rectangle(xaxis.ylocations[i] - 3, profiler[1][i] - 3, 6, 6);
                if (r.contains(x, y)) {
                    res = new Point(i, 2);
                    break;
                }
            }
        }
        return res;
    }

    /*
     * public void saveImage(){ LockFullRepaint=true; ExportDialog export = new
     * ExportDialog("Export"); export.showExportDialog( null, "Export view as
     * ...", this, "export" );
     *
     * LockFullRepaint=false; }
     */
    public void printImage() {
        LockFullRepaint = true;
        PrintPreview2 pw = new PrintPreview2(null, true);
        pw.setComponent(this);
        pw.setVisible(true);
        LockFullRepaint = false;
    }

    public Image getImage() {







        //  if(getWidth()<1 || getHeight()<1) return null;
/*
         * Graphics2D g = new EpsGraphics2D(); this.FullRepaint=true;
         *
         * // BufferedImage bim=new BufferedImage(getWidth(),getHeight(),
         * BufferedImage.TYPE_INT_ARGB); // Graphics gg= bim.getGraphics(); //
         * this.FullRepaint=true; paintComponent(g);
         *
         * String output = g.toString();
         *
         *
         * g.dispose();
         *
         * try{ java.io.FileOutputStream fo = new java.io.FileOutputStream(new
         * java.io.File("c:/TESTER.eps")); fo.write(output.getBytes());
         *
         * fo.flush(); fo.close(); }
         *
         * catch(Exception e){}
         */

        //  return bim;

        return plot;
    }

    public void updateForm() {
        // sp.mixticsSV.setValue(xaxis.minor_tic_count);
        sp.transparencySV.setValue(xaxis.transparency);
        sp.miyticsSV.setValue(yaxis.minor_tic_count);
        sp.paintGridSV.setSelected(xaxis.paintGrid & yaxis.paintGrid);
        sp.paintGridSV.setSelected(xaxis.paintGrid & yaxis.paintGrid);
        sp.gridColorSV.setBackground(yaxis.gridcolor);
        sp.Xaxis.setText(xaxis.getTitleText());
        sp.Yaxis.setText(yaxis.getTitleText());

    }

    public void readForm() {
        topText.setText(sp.Header.getText());
      //   topText.setText("koko wawa");

        xaxis.paintGrid = yaxis.paintGrid = true;//sp.paintGridSV.isSelected();

        
        xaxis.transparency = sp.transparencySV.getValue();
        yaxis.transparency = sp.transparencySV.getValue();

        xaxis.minimumSize = 400;//sp.mixsizeSV.getValue();

        xaxis.gridcolor = Color.LIGHT_GRAY;//sp.gridColorSV.getBackground();
        yaxis.gridcolor = Color.LIGHT_GRAY;//sp.gridColorSV.getBackground();

        if (sp.Xaxis.getText().length() > 0) {
            xaxis.setTitleText(sp.Xaxis.getText());
        }
        if (sp.Yaxis.getText().length() > 0) {
            yaxis.setTitleText(sp.Yaxis.getText());
        }

        xaxis.TICS_IN_BOTH_ENDS = sp.xticsbothsidesSV.isSelected();
        yaxis.TICS_IN_BOTH_ENDS = sp.yticsbothsidesSV.isSelected();

        yaxis.minor_tic_count = sp.miyticsSV.getValue();

        xaxis.setRotated(sp.RotXlabelsSV.isSelected());

        bgf.GradientType = 1;//sp.gradtypeSV.getSelectedIndex();
        bgf.Single =Color.WHITE;// sp.singlebgSV.getColor();

        bgf.gradient1 = sp.grad1SV.getColor();
        bgf.gradient2 = sp.grad2SV.getColor();
        

        lineWidth = sp.lineSizeSV.getValue();
                   //setSize(400, 400);
        setBackground(Color.WHITE);//sp.chartbgSV.getColor());
        setForeground(Color.BLUE);//sp.AxisColorSV.getBackground());
        if (scrollPane != null) {
            scrollPane.getViewport().setOpaque(true);
        }
        if (scrollPane != null) {
            scrollPane.getViewport().setBackground(Color.WHITE);//sp.chartbgSV.getColor());
        }

        /*
         *
         * boolean resetyAxis=false; double ymin=sp.ymin.getValue(); double
         * ymax=sp.ymax.getValue();
         *
         * if(ymin!=ymax){ if( ymin != yaxis.minimum){resetyAxis=true; } if(
         * ymax != yaxis.maximum){resetyAxis=true; }
         *
         * if(resetyAxis) { yaxis.force_end_labels=sp.endlabelsSV.isSelected();
         * yaxis.minimum=ymin; yaxis.maximum=ymax; } }
         *
         */

        if (scrollPane != null) {
            scrollPane.setBackground(sp.chartbgSV.getColor());
        }
        //size = new Dimension(sp.mixsizeSV.getValue(), sp.minHeightSV.getValue());

        int width = Math.max(xaxis.predictLength() + yaxis.predictWidth() + xaxis.endLength(), sp.mixsizeSV.getValue() + xaxis.endLength());
       // width = Math.max(400,width);
        Dimension dim = new Dimension(width, sp.minHeightSV.getValue());


        setMinimumSize(dim);
        setPreferredSize(dim);
        setSize(dim);

    }

    public void writeValues() {
        Tools to = new Tools();
        if (props != null) {
            to.writedialogStatus(sp, "LineChart", props);
        }
    }

    public void readValues(JDialog sp) {
        Tools to = new Tools();
        if (props != null) {
            to.readialogStatus(sp, "LineChart", props);
        }
    }

    public JToolTip createToolTip() {
        return (new JeToolTip(this));
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {

        // System.out.print("\nRectangle X:"+ visibleRect.x+" Y:"+visibleRect.y+" W:"+visibleRect.width+" H:"+visibleRect.height);

        //Get the current position.
        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }

        //Return the number of pixels between currentPosition
        //and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition - (currentPosition / maxUnitIncrement) * maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1) * maxUnitIncrement - currentPosition;
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public void setMaxUnitIncrement(int pixels) {
        maxUnitIncrement = pixels;
    }

//    public static void main(String[] args) {
//
//        //double[] Tx = new double[]{0.0021, -0.004, -0.015, -0.004, 0.0021, -0.014, -0.03, -0.07, -0.0036, -0.014, -0.0042, -0.0034, -0.023, -0.0017}; //Raw X
//
//
//
//
//        //DataSet data = null;//new DataSet(new double[][]{{-1.0,0.0,0.0},{1.0,0.0,0.0},{0.0,0.0,0.0}},new String[][]{{"  ","  ","  "},{"  ","  ","  "}},new String[]{"  ","  ","  "})    ;
//
//
//        Hashtable props = null;
//
//        try {
//            java.io.FileInputStream istream = new java.io.FileInputStream("cfg");
//            java.io.ObjectInputStream p = new java.io.ObjectInputStream(istream);
//            props = (java.util.Hashtable) p.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JScrollPane jp = new JScrollPane();
//
//        //DataSet data = new DataSet(new double[][]{{0.002,0.003,0.02},{0.002,0.02,0.003},{0.007,0.0003,0.215}},new String[][]{{"test1","test2","test3"},{"test11","test22","test33"}},new String[][]{{"COL2","Col3"},{"COL1","Col3"},{"COL1","COL2"}})    ;
//        //by Kidane: fix me
//        // commented out just to make it compile
//   /*
//         * expresscomponents.DataSet data =
//         * cluster.loadDataSet("resources/testproject.pro","Raw
//         * data");//"alpha1-18_2");//"nci60_fig3_nomiss.txt","Baldwin_alltimecourses_nomiss.txt","nci60_fig3_nomiss.txt","Alizadeh_65col_nomiss.txt"
//         *
//         * final LineChart pl = new LineChart(props,data,jp);
//         *
//         * jp.setViewportView(pl);
//         *
//         * JFrame f = new JFrame(); f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
//         *
//         * f.getContentPane().add("Center",jp);
//         *
//         *
//         *
//         * JButton b = new JButton("test"); b.addActionListener(new
//         * ActionListener(){ public void actionPerformed(ActionEvent e){ //
//         * these are temporary!
//         *
//         *
//         *
//         * // pl.setData(Tx,Ty); pl.printImage(); }
//         *
//         *
//         * });
//         *
//         * //f.getContentPane().add("South",b);
//         *
//         *
//         * //f.pack();
//         *
//         * f.setSize(400,250); f.setVisible(true);
//         *
//         *
//         */
//    }
//    
//     private static boolean[] getusedColInfos(DataSet m_data) {
//        boolean[] usedColInfos = new boolean[getColInfoHeaders(m_data).length + 2];
//        java.util.Arrays.fill(usedColInfos, true);
//        return usedColInfos;
//    }
//     
       /**
     * Method for getting column info headers on the old format, ie the one used
     * in J-Express (array of Strings).
     */
//    private static String[] getColInfoHeaders(DataSet m_data) {
//        AnnotationManager am = AnnotationManager.getAnnotationManager();
//        Set<String> columnAnnotations = m_data.getColumnAnnotationNamesInUse();
//        if (columnAnnotations == null) {
//            columnAnnotations = am.getManagedColumnAnnotationNames();
//        }
//        String[] colHeaders = null;
//        if (columnAnnotations.isEmpty()) {
//            colHeaders = new String[]{"Column ID"};
//        } else {
//            colHeaders = columnAnnotations.toArray(new String[columnAnnotations.size()]);
//        }
//        return colHeaders;
//    }
    
     /**
     * Method that returns the annotations for the columns of the DataSet. This
     * method has the same name as the method in the old DataSet class
     * (J-Express), and returns the annotations on the same format as the old
     * DataSet class. This method is only used for legacy-methods that rely on
     * the old way of storing annotations (for the new way for handling
     * annotations on columns and rows on datasets, see AnnotationLibrary and
     * AnnotationManager in the core package).
     */
//    private  String[][] getColInfos(DataSet m_data) {
//        AnnotationManager am = AnnotationManager.getAnnotationManager();
//        Set<String> annotations = m_data.getColumnAnnotationNamesInUse();
//        if (annotations == null) {
//            annotations = am.getManagedColumnAnnotationNames();
//        }
//
//        String[] columnIds = m_data.getColumnIds();
//        String[][] all; //column annotation matrix
//        if (annotations.isEmpty()) {
//            assert m_data.getDataWidth() == columnIds.length : "length of column ids and width of dataset should be equal (?)";
//            all = new String[m_data.getDataWidth()][1];
//            for (int i = 0; i < all.length; i++) {
//                all[i][0] = columnIds[i];
//            }
//        } else {
//            String[] headers = annotations.toArray(new String[annotations.size()]);
//            all = new String[m_data.getDataWidth()][annotations.size()];
//            for (int i = 0; i < headers.length; i++) {
//                AnnotationLibrary anns = am.getColumnAnnotations(headers[i]);
//                for (int j = 0; j < all.length; j++) {
//                    all[j][i] = anns.getAnnotation(columnIds[j]);
//                }
//            }
//        }
//        return all;
//    }
    
}
