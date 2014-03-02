
package no.uib.jexpress_modularized.core.visualization.charts;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.core.visualization.*;
//import jexpress.mainGraph;

//import org.freehep.graphics2d.VectorGraphics;
//import org.freehep.util.export.ExportDialog;

import no.uib.jexpress_modularized.core.model.*;
import no.uib.jexpress_modularized.core.visualization.charts.GUI.LineProps2;

/**
 *
 * @author  bjarte
 */
public class BoxPlot2 extends JComponent implements Serializable{
    
    public JScrollPane scrollPane = new JScrollPane();
    
    boolean start = true;
    private JeToolTip tip;
    BackgroundFactory bgf = new BackgroundFactory(new boolean[]{false,true,true,true,true,true,true,true,true,true,true});
    public LineProps2 sp;
    public Color dotColor = Color.red;
    boolean FormUpdated = false; //for initially insert values to properties dialog.
    // public Color[] dotColors = null;
    public boolean[] inActiveGroup = null;
    public boolean[] members = null;
    public Hashtable props;
    //DataSet data = new DataSet(new double[][]{{1.2,2.3,4.2},{4.2,1.2,9.3},{2.5,2.2,7.5}},new String[][]{{"test1","test2","test3"},{"test11","test22","test33"}},new String[]{"COL1","COL2","Col3"})    ;
    Dataset data = null;//new DataSet(new double[][]{{0.0,0.0,0.0},{0.0,0.0,0.0},{0.0,0.0,0.0}},new String[][]{{"  ","  ","  "},{"  ","  ","  "}},new String[]{"  ","  ","  "})    ;
    public Axis yaxis;
    public LabelAxis xaxis;
    public ChartLabel topText = new ChartLabel("");
    //scrollpic scp;
    private int maxUnitIncrement = 10; //For the scrollable interface..
    
    public Dimension size = new Dimension(200,200);
    
    private boolean SelectedOnly=false;
    
    public boolean HorizontalLinesOnly=true;
    
    Cursor openHand,closedHand;
    public boolean shadow=true;
    public boolean[] draw;
    public float dashSize=5;
    public boolean multiGroups = true;
    
    private boolean rangeChanged=false;
    
    public boolean grayScale = false;
    
    public boolean FullRepaint=true;
    public boolean LockFullRepaint=false;
    
    public BufferedImage plot,MainPic;
    public boolean move=false;
    public boolean frame=true;
    public Rectangle valueArea;
    public Rectangle valueframe;  //The frame created by mouse drag
    public boolean paintTags=true;
    public int TagFrequency = 3;
    public int TagOffset = 5;
    
    
    BoxPlot[] boxes;
    //public int gridtrans=0;
    
    //public Color shadowColor=new Color(220,220,220,150);
    public Color shadowColor=new Color(160,160,160);
    public int lineWidth=1;
    public boolean antialias=false;
    public boolean zoomed=false;
    
    //For bordering (profiler functions) This is the upper (profile[0]) and lower (profile[1]) borders..
    
    public int[][] profiler;
    public Vector selectedPoints; //For profiler points that are to be painted another color..
    
    public int bottom = 45;
    public int left = 60;
    public int top = 10;
    public int right = 20;
    
    private int mouseX,mouseY;
    private boolean mouseDrag=false;
    
    // public boolean groupMeans=true;
    
    
    int startlabel;
    int endlabel;
    
    private int startx,starty,endx,endy;  //For mouse event framing
    private Point clickedPoint=new Point(0,0);
    private int hscrollBarClickPos=0;
    private int vscrollBarClickPos=0;
    
    
    public BoxPlot2(){
        this(null,null,null);
        setPreferredSize(new Dimension(100,100));
    }
    
    
    public void setupBoxes(Dataset data, double Q1, double Q3, double med){
        int length=data.getDataLength();
        double[][] dat =data.getData();
               
        int[]sel = SelectionManager.getSelectionManager().getSelectedRows(data).getMembers();
        
        if(sel==null){
            sel = new int[data.getRowIds().length];
               for(int i=0;i<sel.length;i++){
                   sel[i] = i;
               }     
        }

        if(SelectedOnly) length=sel.length;
        
        double[][] trans = trans = new double[dat[0].length][length];
        
        boxes=new BoxPlot[data.getDataWidth()+2];
        
        if(SelectedOnly){
        for(int i=0;i<sel.length;i++){
            for(int j=0;j<dat[0].length;j++){
                trans[j][i]=dat[sel[i]][j];
            }
        }
        }
        
        else{
         for(int i=0;i<length;i++){
            for(int j=0;j<dat[0].length;j++){
                trans[j][i]=dat[i][j];
            }
        }   
        }
        
        
        for(int i=0;i<trans.length;i++){
         boxes[i+1]=new BoxPlot();
         if(trans[i]!=null)boxes[i+1].newData(trans[i],true,Q1,Q3,med);
        }
    }
    
    
    
    public BoxPlot2(Hashtable props, Dataset data, final JScrollPane scroll){
        
        
        if(data!=null)this.data=data;
        if(scroll!=null)scrollPane=scroll;
        
        this.props=props;
        
        sp = getPropsWindow();
        
        //createDummy();
        init();
        
        addMouseMotionListener(new MouseMotionAdapter(){
            
            public void mouseDragged(MouseEvent me){
                if(move){
                    Point p = me.getPoint();
                    //p.x=p.x-(clickedPoint.x-p.x);
                    
                    //clickedPoint.x+=(clickedPoint.x-p.x);
                    
                    BoxPlot2.this.scrollPane.getHorizontalScrollBar().setValue(hscrollBarClickPos+(clickedPoint.x-me.getX()));
                    //LineChart.this.scrollPane.getHorizontalScrollBar().setValue(hscrollBarClickPos+(clickedPoint.x-p.x));
                    BoxPlot2.this.scrollPane.getVerticalScrollBar().setValue(vscrollBarClickPos+(clickedPoint.y-me.getY()));
                    //System.out.print("\n"+ hscrollBarClickPos+(clickedPoint.x-me.getX()) );
                    //clickedPoint=me.getPoint();
                    //clickedPoint.x-=(clickedPoint.x-me.getX());
                    hscrollBarClickPos=BoxPlot2.this.scrollPane.getHorizontalScrollBar().getValue();
                    vscrollBarClickPos=BoxPlot2.this.scrollPane.getVerticalScrollBar().getValue();
                    
                }
                else if(frame){
                    if(valueframe==null) return;
                    
                    endx=me.getX();
                    endy=me.getY();
                    
                    int frstartx = valueArea.x;
                    int frendx = valueArea.x+valueArea.width;
                    
                    int frstarty = valueArea.y;
                    int frendy = valueArea.y+valueArea.height;
                    
                    endx=Math.min(endx,frendx);
                    endx=Math.max(endx,frstartx);
                    endx=Math.min(endx,frendx);
                    endx=Math.max(endx,frstartx);
                    
                    endy=Math.min(endy,frendy);
                    endy=Math.max(endy,frstarty);
                    endy=Math.min(endy,frendy);
                    endy=Math.max(endy,frstarty);
                    
                    int sqxstart=Math.min(startx,endx);
                    int sqystart=Math.min(starty,endy);
                    int sqxend= (Math.max(startx,endx) - Math.min(startx,endx)) ;
                    int sqyend= (Math.max(starty,endy) - Math.min(starty,endy)) ;
                    
                    
                    valueframe.setBounds(sqxstart,sqystart,sqxend,sqyend);
                    mouseDrag = true;
                    //System.out.print("\ndrag");
                    repaint();
                    //packupComponent();
                }
            }
        });
        
        addMouseListener(new MouseAdapter(){
            
            public void mouseReleased(MouseEvent me){
                
                if(me.getModifiers()==me.BUTTON3_MASK){
                   JRootPane root = getRootPane();
                    sp = getPropsWindow();
                    sp.setLocationRelativeTo(root);
                    
                    sp.pack();
                    sp.setVisible(true);
                }
                
            }
            
            public void mousePressed(MouseEvent me){
                startx=me.getX();
                starty=me.getY();
                endx=me.getX();
                endy=me.getY();
                
                if(me.getModifiers()!=me.BUTTON3_MASK){
                    
                    if(move){
                        clickedPoint.setLocation(me.getPoint());
                        hscrollBarClickPos=BoxPlot2.this.scrollPane.getHorizontalScrollBar().getValue();
                        vscrollBarClickPos=BoxPlot2.this.scrollPane.getVerticalScrollBar().getValue();
                        
                        //  setCursor(new Cursor(Cursor.MOVE_CURSOR ));
                        
                    }
                    else if (frame){
                        startx=me.getX();
                        starty=me.getY();
                        endx=me.getX();
                        endy=me.getY();
                        
                        int frstartx = valueArea.x;
                        int frendx = valueArea.x+valueArea.width;
                        
                        int frstarty = valueArea.y;
                        int frendy = valueArea.y+valueArea.height;
                        
                        if(startx< frendx && startx>frstartx && starty < frendy && endy>frstarty){
                            valueframe=new Rectangle(startx,starty,0,0);
                        }
                        
                        repaint();
                    }
                }
            }
            
        });
        
        
                scrollPane.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                //size=new Dimension(xaxis.predictLength()+yaxis.predictWidth(),LineChart.this.scrollPane.getSize().height);
                forceFullRepaint();
            }
        });
        
        
        
        
      
    }
    


public LineProps2 getPropsWindow(){
    
    Component owner = null;
    if(getRootPane()!=null ) owner=getRootPane().getTopLevelAncestor();
    LineProps2 sp = new LineProps2((JFrame)owner,true);
    
    
    
    sp.copy.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
            ImageHandler.ImageToClipBoard(getImage());
        }
    });
    
    
    KeyListener k = new KeyAdapter(){
        
        public void keyPressed(KeyEvent e){
            rangeChanged=true;
        }
    };
    
    
    sp.ymin.addKeyListener(k);
    sp.ymax.addKeyListener(k);
    
    bgf.fillCombo(sp.SbgSV);
    bgf.fillGradCombo(sp.gradtypeSV);
    
    sp.reset.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
            //yaxis.resetAxis(Ry);
            
            yaxis.setValueRange(BoxPlot2.this.data.getMinMeasurement(),BoxPlot2.this.data.getMaxMeasurement());
            yaxis.setManualRange(false);
            //yaxis.force_end_labels=false;
            
            rangeChanged=false;
            //updateAxisValues();
            forceFullRepaint();
        }
    });
    
    
      sp.ok.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                readForm();
                forceFullRepaint();
            }
        });
        
        
        sp.defaults.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                writeValues();
            }
        });
        

    
    readValues(sp);
    
    
    
    return sp;
}


public void setPropertiesAndScrollPane(Hashtable p, JScrollPane sp){
    scrollPane = null;
    
    sp.addComponentListener(new ComponentAdapter(){
        public void componentResized(ComponentEvent e){
            //size=new Dimension(xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth(),LineChart.this.scrollPane.getViewport().getSize().height);
            //setPreferredSize(new Dimension(size.width,size.height));
            //setSize(new Dimension(size.width,size.height));
            forceFullRepaint();
        }
    });
    //size=new Dimension(xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth(),sp.getViewport().getSize().height);
    //size=new Dimension(500,500);
    //setPreferredSize(new Dimension(size.width,size.height));
    //setSize(new Dimension(size.width,size.height));
    scrollPane = sp;
    this.props=p;
    readValues(this.sp);
    readForm();
    forceFullRepaint();
}

public Rectangle getValueBounds(){
    return new Rectangle(valueArea.x-1,valueArea.y-1,valueArea.width+2,valueArea.height+2);
}

public Rectangle getSelectionBounds(){
    return new Rectangle(valueframe.x-1,valueframe.y-1,valueframe.width+2,valueframe.height+2);
}

   /*
    public void updateAxisValues(){
        sp.ymin.setValue(yaxis.minimum);
        sp.ymax.setValue(yaxis.maximum);
    }
    */

public void createDummy(){
    if(data==null) return;
    boolean[] gr = new boolean[data.getDataLength()];
    for(int i=0;i<1;i++){
        gr[i]=true;
    }
    
//    data.addGroup(new Boolean(true),"testgroup",Color.red,gr,false);
    
    
    boolean[] gr2 = new boolean[data.getDataLength()];
    for(int i=0;i<2;i++){
        gr2[i]=true;
    }
    
//    data.addGroup(new Boolean(true),"testgroup",Color.green,gr2,false);
    
    
        /*
         draw=new boolean[data.getDataLength()];
        draw[4]=true;
        draw[2]=true;
        draw[1]=true;
        draw[8]=true;
         */
}


public void setData(Dataset data,double Q1,double Q3,double med){
    /*
    this.data=data;
    
        
    //if(true) return;    
    
    yaxis.setValueRange(data.getMinMeasurment(),data.getMaxMeasurement());
   // yaxis.setValueRange(-6,6);

     String[][] colinfos=createSpacedColInfos();
    boolean[] visibleRows=createVisibleRowIndexes();
    if(data!=null) visibleRows = data.getusedColInfos();
    
    
    xaxis.setLabels(colinfos,visibleRows);

        
    
    
    this.setupBoxes(data,Q1,Q3,med);
    
    

    
    readForm();
    updateForm();
    forceFullRepaint();
    * */
}

//public void setXaxisLabels(){
//    
//    
////    boolean[] visibleRows=null;
////    
////    String[][] labels=new String[endlabel-startlabel][data.getColInfos()[0].length];
////    
////    for(int i=0;i<labels.length;i++){
////        for(int j=0;j<labels[0].length;j++){
////            labels[i][j]=data.getColInfos()[i+startlabel][j];
////        }
////    }
//    
////    visibleRows=createVisibleRowIndexes();
//    
//    
//    
//     String[][] colinfos=createSpacedColInfos();
//    boolean[] visibleRows=createVisibleRowIndexes();
//    
//    
//     boolean[] extendedVisibleRows = new boolean[visibleRows.length];
//     
//     for(int i=0;i<visibleRows.length;i++){
//         extendedVisibleRows[i+1]=visibleRows[i];
//     }
//    extendedVisibleRows[0]=true;
//    extendedVisibleRows[extendedVisibleRows.length-1]=true;
//    
//    
//    xaxis.setLabels(colinfos,extendedVisibleRows);
//
//    
//    
//    
//    
//    
//    
//    
//    
//    
////    xaxis.setLabels(colinfos,visibleRows);
//    //size=new Dimension(xaxis.predictLength()+xaxis.endLength()+yaxis.predictWidth(),LineChart.this.scrollPane.getViewport().getSize().height);
//    //setPreferredSize(new Dimension(size.width,size.height));
//    
//}

public void setMembers(boolean[] members){
    this.members=members;
    forceFullRepaint();
    
}

public boolean[] getMembers(){
    return members;
    
}



public double[] getValueFrame(){
    double[] ret=null;
    
    if( valueframe==null) return null;
    
    else{
        ret=new double[4];
        //System.out.print("\nStartLabel: "+xaxis.getLeftLabel(startx));
        //System.out.print("\nEndLabel: "+xaxis.getRightLabel(endx));
        ret[0]=yaxis.getDouble(valueframe.y);
        ret[1]=yaxis.getDouble(valueframe.y+valueframe.height);
        ret[2]=(double) xaxis.getLeftLabel(startx);
        ret[3]=(double) xaxis.getRightLabel(endx)+1;
        
    }
    
    // else return new Rectangle( yaxis.getDouble(valueframe.x,valueframe.y,
    // if(valueframe==null) return null;
    
    // mainGraph gr= new mainGraph(data,cl,false);
    
    return ret;
    
}







public void setXaxisTitle(String label){
    xaxis.setTitleText(label);
}

public void setYaxisTitle(String label){
    yaxis.setTitleText(label);
}
/*

public boolean[] createVisibleRowIndexes(){
    boolean[] ret=null;
    Boolean[] ind=null;
    try{
        if(data.structures.containsKey("VisibleTransposedColumns")){
            ind= (Boolean[]) data.structures.get("VisibleTransposedColumns");
            ret=new boolean[ind.length];
            for(int i=0;i<ret.length;i++) ret[i]=ind[i].booleanValue();
            
        }
    }
    catch(Exception e){
                
    }
    return ret;
    
}

*/
/*

public String[][] createSpacedColInfos(){
     String[][] colinfos = data.getColInfos();
        
        String[][] colinfos2 = new String[colinfos.length+2][colinfos[0].length];
        for(int i=0;i<colinfos.length;i++){
             for(int j=0;j<colinfos[0].length;j++){
                 colinfos2[i+1][j]=colinfos[i][j];
             }
        }
          for(int j=0;j<colinfos2[0].length;j++){
                 colinfos2[0][j]="";
                 colinfos2[colinfos2.length-1][j]="";
             }       
        return colinfos2;
}
*/

public void init(){
   /* 
    //setOpaque(true);
    //setBackground(new Color(210,204,204));
    //scrollPane.getViewport().setBackground(getBackground());
    setForeground(Color.black);
    
    scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER,new JPanel());
    
    if(data!=null) yaxis = new Axis(1,this,data.getMin(),data.getMax());
    else yaxis = new Axis(1,this,-10,10);
    
    yaxis.correctForCloseValues=false;
    
    boolean[] visibleRows = createVisibleRowIndexes();
    
    if(data!=null) visibleRows = data.getusedColInfos();
    
    int cnt = 0;
    
    if(data!=null){
        
       String[][] colinfos=createSpacedColInfos();
                
        
        xaxis = new LabelAxis(0,this,colinfos,visibleRows);
        
        
        
    }
    
    
    
    
    
    
    else xaxis = new LabelAxis(0,this,new String[][]{{"s"},{"  "}},new boolean[]{true,true});
    
    
    
    xaxis.axiscolor=getForeground();
    yaxis.axiscolor=getForeground();
    
    yaxis.dropFirstGridLine=true;
    yaxis.dropLastGridLine=true;
    xaxis.dropFirstGridLine=true;
    xaxis.dropLastGridLine=true;
    
    setXaxisTitle("");
    setYaxisTitle("");
    xaxis.setTitleFont(new Font("Times New Roman",1,13));
    yaxis.setTitleFont(new Font("Times New Roman",1,13));
    topText.setFont(new Font("Times New Roman",1,16));
    
    
    if(true) return;
    
    //setSize(new Dimension(xaxis.predictLength()+yaxis.predictWidth(),350));
    sp = getPropsWindow();
    readForm();
    updateForm();FormUpdated=true;
    forceFullRepaint();
   */
}




public void setDraw(boolean[] draw){
    this.draw=draw;
    forceFullRepaint();
}

public boolean[] getDraw(){
    return draw;
}

//public int Height(){return getSize().height;}
//public int Width(){return getSize().width;}

public int Height(){return getSize().height;}//size.height;}
public int Width(){return getSize().width;}//return size.width;}

//   public int Height(){return 300;}
//   public int Width(){return 450;}

public void forceFullRepaint(){
    FullRepaint=true;
    repaint();
}



public void paintComponent(Graphics gr){
    /*
    int[] x =null;
    
    Graphics g = null;
    if(LockFullRepaint) FullRepaint=true;
    
    
    if(FullRepaint || LockFullRepaint){
        
        if( props!= null && props.containsKey("WholeLine")) HorizontalLinesOnly = ( ((Boolean)props.get("WholeLine")).booleanValue());
        
        
        yaxis.force_end_labels=sp.endlabelsSV.isSelected();
        
        if(!LockFullRepaint) {
            if(!grayScale) plot = new BufferedImage(Width(),Height(), BufferedImage.TYPE_INT_RGB);
            else plot = new BufferedImage(Width(),Height(), BufferedImage.TYPE_BYTE_GRAY);
            g = plot.getGraphics();
        }
        else g=gr;
        
        if(rangeChanged && !zoomed){
            
            //if the values are fixed, use these:
            
            double max = sp.ymax.getValue();
            double min = sp.ymin.getValue();
            
            //   if(data.structures.containsKey("ChartYmin")) min = new Double((String)data.structures.get("ChartYmin")).doubleValue();
            //   if(data.structures.containsKey("ChartYmax")) max = new Double((String)data.structures.get("ChartYmax")).doubleValue();
            
            if(min<max){
                yaxis.setManualRange(true);
                yaxis.setValueRange(min,max);
            }
            else{
                JOptionPane.showMessageDialog(null,"Invalid bounds for chart in dataset\nContinuing with precalculated bounds.", "Error",JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
        else{
            yaxis.setManualRange(false);
        }

        yaxis.setLabelFont(new Font("Arial", 0, 13));
        
        yaxis.LabelAditionalLength=5;

        
        int TitleHeight = topText.getHeight(g);
        
        int HTitlePos = Width()/2-(topText.getWidth(g)/2);
        
        if(Width()==0 || Height()==0)return;
        
        int xaxisLength = xaxis.predictLength();
        //int xaxistotLength = xaxis.predictLength()+xaxis.endLength();
        
        g.setColor(getBackground());
        g.fillRect(0,0,Width(),Height());
        
        g.setColor(getForeground());
        topText.draw(g,HTitlePos,top+5);
        
        left=yaxis.getAxisWidth(g)-5;
        bottom=xaxis.getAxisWidth(g);
        
        
        
        //if(!xaxis.positionAxis(left,left+xaxis.getAxisLength(g),Height()-bottom,Height()-bottom)) System.out.print("\nFailed to init xaxis");
        if(!xaxis.positionAxis(left,left+xaxisLength,Height()-bottom,Height()-bottom)) System.out.print("\nFailed to init xaxis");
        if(!yaxis.positionAxis(left,left,top+TitleHeight,Height()-bottom)) System.out.print("\nFailed to init yaxis");
        
        //xaxisLength = xaxis.predictLength()-100;
        
        xaxis.prepareLocations(g);
        x = xaxis.ylocations;
        
        yaxis.calculateGridLabels();
        
        //yaxis.data_window.setSize(xaxis.getAxisLength(g),10);
        yaxis.data_window.setSize(xaxisLength,10);
        xaxis.data_window.setSize(10,Height()-bottom-top-TitleHeight);
        
        //valueArea = new Rectangle(left,top+TitleHeight,xaxis.getAxisLength(g),Height()-bottom-top-TitleHeight);
        valueArea = new Rectangle(left,top+TitleHeight,xaxisLength,Height()-bottom-top-TitleHeight);
        
       // if(!LockFullRepaint) g.setClip(valueArea);
        
        
        // System.out.print("\nsp.pathSV.getText() : "+sp.pathSV.getText());
        
        bgf.externalImage = sp.pathSV.getText();
        bgf.tileImages = sp.tileSV.isSelected();
        
        bgf.paintBackground(g,valueArea,sp.SbgSV.getSelectedIndex());
        
        Graphics2D g2d = (Graphics2D)g;
        //if(!(g2d instanceof org.freehep.graphicsio.emf.EMFGraphics2D))
        g2d.setStroke(new BasicStroke(lineWidth));
        
        
        if(antialias) g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        else g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        
        
        Vector Class = null;
        Boolean activeSet=null;
        Color classColor = null;
        boolean[] indexes = null;
        
        
        
        Font fbefore = g.getFont();
        Color cbefore = g.getColor();
        
        if(data==null) return;
        yaxis.getInteger(data.getData());

        
        
          
        g.setFont(fbefore);
        g2d.setStroke(new BasicStroke());
        g.setColor(getForeground());
        
        //g.setClip(0,0,Width(),Height());
        g2d.setClip(null);
        xaxis.axiscolor=getForeground();
        yaxis.axiscolor=getForeground();
        
        yaxis.dropFirstGridLine=true;
        yaxis.dropLastGridLine=true;
        
        if(yaxis!=null) yaxis.drawAxis(g);
        g.setClip(null);
        
        
        String[][] colinfos = data.getColInfos();
        
        Color[] groupColors = new Color[ colinfos.length +2 ];
        java.util.Arrays.fill(groupColors,Color.black);
        Vector groups = data.getColumnGroups();
        Group grp = null;
        
        for(int i=0;i<groups.size();i++){
            
            grp=(Group)groups.elementAt(groups.size()-1-i);
            if(!grp.isActive()) continue;
            Color c = gr.getColor();
            boolean[] b = grp.getMembers();
            for(int j=0;j<b.length;j++) if(b[j]) groupColors[j+1]=c;
        }
        
        xaxis.setGroupColors(groupColors);
        
        
        
 //       g.setClip(null);
        if(xaxis!=null) xaxis.drawAxis(g);
        
        sp.ymin.setValue(yaxis.minimum);
        sp.ymax.setValue(yaxis.maximum);
        
        g.setColor(getForeground());
        g.drawLine(left+1,top+TitleHeight,left+xaxisLength,top+TitleHeight);
        g.drawLine(left+xaxisLength,top+TitleHeight,left+xaxisLength,Height()-bottom);
        
        double min = yaxis.minimum;//.getDataMin();
        double max = yaxis.maximum;//getDataMax();
        
        int imin = yaxis.getInteger(yaxis.getDataMin());
        int imax = yaxis.getInteger(yaxis.getDataMax());
        
        int nullv = yaxis.getInteger(0.0);
       // g.setColor(Color.red);
       // g2d.draw(valueArea);
        

        if(this.boxes!=null){
            for(int i=0;i<boxes.length;i++){
                if(boxes[i]!=null){
                boxes[i].setminmax(min,max);
                boxes[i].setArea(valueArea);
                boxes[i].paint(g,x[i], valueArea.y,nullv,valueArea.height,imin,imax);
                }
            }
        }
        
        
 
        
    } //fullrepaint
    
    FullRepaint=false;
    
    
    BufferedImage plot2 = null;
   
    if(getParent() != null && getParent() instanceof JViewport && !LockFullRepaint) gr.setClip(((JViewport)getParent()).getViewRect());
    
    if(!LockFullRepaint && plot!=null) gr.drawImage(plot,0,0,this);
    
    
    
    if(valueframe!=null && !LockFullRepaint){
    
        
        //Graphics2D g2d = (Graphics2D)tg;
        Graphics2D g2d = (Graphics2D)gr;
        
        
         float[] dashPattern = new float[]{ 3, 3, 3, 3 };
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10, dashPattern, 0));
        g2d.setColor(new Color(169,160,160));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f) );
        g2d.draw(valueframe);
        
        
        
    }
    
    if(profiler!=null){
        //gr.setClip(null);
        gr.setClip(((JViewport)getParent()).getViewRect());
        Graphics2D g2d = (Graphics2D)gr;
        
        if(x==null) x = xaxis.ylocations;
        for(int i=0;i<profiler[0].length;i++){
            g2d.setStroke(new BasicStroke(1));
            gr.setColor(Color.green);
            if(selectedPoints!=null) for(int j=0;j<selectedPoints.size();j++) if(((Point)selectedPoints.elementAt(j)).x==i && ((Point)selectedPoints.elementAt(j)).y==0) gr.setColor(Color.red);
            
            gr.drawRect( x[i]-3, profiler[0][i]-3,6,6);
            gr.drawLine( x[i]-8, profiler[0][i],x[i]+8,profiler[0][i]);
            
            gr.setColor(Color.green);
            if(selectedPoints!=null)for(int j=0;j<selectedPoints.size();j++) if(((Point)selectedPoints.elementAt(j)).x==i && ((Point)selectedPoints.elementAt(j)).y==2) gr.setColor(Color.red);
            gr.drawRect( x[i]-3, profiler[1][i]-3,6,6);
            gr.drawLine( x[i]-8, profiler[1][i],x[i]+8,profiler[1][i]);
            
            gr.setColor(new Color(160,250,160,160));
            g2d.setStroke(new BasicStroke(2));
            if(profiler[0][i] > profiler[1][i]) gr.setColor(new Color(250,160,160,160));
            gr.drawLine( x[i], profiler[0][i],x[i],profiler[1][i]);
            
            
        }
        
        
    }
    
    if(profiler==null)  firePropertyChange("painted",new Boolean(false),new Boolean(true));
    
    */
    
}



public void drawPolyLine(int[] x,int[]y,double[] dx,Graphics g){
    if(HorizontalLinesOnly){
        int step = ((x[1]-x[0])/2)-3;
        for(int i=0;i<x.length;i++){
            if(!Double.isNaN(dx[i])) g.drawLine(x[i]-step,y[i],x[i]+step,y[i]);
        }
    }
    else{
        for(int i=0;i<x.length-1;i++){
            if(!Double.isNaN(dx[i]) && !Double.isNaN(dx[i+1])) g.drawLine(x[i],y[i],x[i+1],y[i+1]);
        }
    }
}


public Image getProfilerImage(){
    BufferedImage cl = new BufferedImage(plot.getWidth(),plot.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics gr = cl.getGraphics();
    gr.drawImage(plot,0,0,this);
    
    if(profiler!=null){
        //gr.setClip(null);
        gr.setClip(((JViewport)getParent()).getViewRect());
        Graphics2D g2d = (Graphics2D)gr;
        
        int[] x = xaxis.ylocations;
        for(int i=0;i<profiler[0].length;i++){
            g2d.setStroke(new BasicStroke(1));
            gr.setColor(Color.green);
            if(selectedPoints!=null) for(int j=0;j<selectedPoints.size();j++) if(((Point)selectedPoints.elementAt(j)).x==i && ((Point)selectedPoints.elementAt(j)).y==0) gr.setColor(Color.red);
            
            gr.drawRect( x[i]-3, profiler[0][i]-3,6,6);
            gr.drawLine( x[i]-8, profiler[0][i],x[i]+8,profiler[0][i]);
            
            gr.setColor(Color.green);
            if(selectedPoints!=null)for(int j=0;j<selectedPoints.size();j++) if(((Point)selectedPoints.elementAt(j)).x==i && ((Point)selectedPoints.elementAt(j)).y==2) gr.setColor(Color.red);
            gr.drawRect( x[i]-3, profiler[1][i]-3,6,6);
            gr.drawLine( x[i]-8, profiler[1][i],x[i]+8,profiler[1][i]);
            
            gr.setColor(new Color(160,250,160,160));
            g2d.setStroke(new BasicStroke(2));
            if(profiler[0][i] > profiler[1][i]) gr.setColor(new Color(250,160,160,160));
            gr.drawLine( x[i], profiler[0][i],x[i],profiler[1][i]);
            
            
        }
        
        
    }
    
    return cl;
}




public void setProfiler(int[][] profiler){
    this.profiler=profiler;
    repaint();
}


//If this is bordered by profiler arrays, return the point at x,y or null if the profiles are empty or no hit is found.
public Point getProfilerPointAt(int x,int y){
    Point res = null;
    Rectangle r = null;
    
    if(profiler==null){System.out.print("\nProfiles are null!");return null;}
    
    else{
        
        // System.out.print("\nLooking for points at:" +)
        
        
        for(int i=0;i<profiler[0].length;i++){
            r=new Rectangle(xaxis.ylocations[i]-3,profiler[0][i]-3,6,6);
            if(r.contains(x,y)){ res=new Point(i,0); break;}
            r=new Rectangle(xaxis.ylocations[i]-3,profiler[1][i]-3,6,6);
            if(r.contains(x,y)){ res=new Point(i,2); break;}
        }
    }
    return res;
}

    /*
    public void saveImage(){
          LockFullRepaint=true;
          ExportDialog export = new ExportDialog("Export");
          export.showExportDialog( null, "Export view as ...", this, "export" );
     
          LockFullRepaint=false;
    }
     */
public void printImage(){
    /*
    LockFullRepaint=true;
    expresscomponents.Print.PrintPreview2 pw= new expresscomponents.Print.PrintPreview2(null,true);
    pw.setComponent(this);
    pw.show();
    LockFullRepaint=false;
    
    */
}



public Image getImage(){
    
    
    
    
    
    
    
    //  if(getWidth()<1 || getHeight()<1) return null;
/*
        Graphics2D g = new EpsGraphics2D();
        this.FullRepaint=true;
 
      //  BufferedImage bim=new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_ARGB);
      //  Graphics gg= bim.getGraphics();
      //  this.FullRepaint=true;
       paintComponent(g);
 
       String output = g.toString();
 
 
        g.dispose();
 
        try{
         java.io.FileOutputStream fo = new java.io.FileOutputStream(new java.io.File("c:/TESTER.eps"));
        fo.write(output.getBytes());
 
        fo.flush();
        fo.close();
        }
 
        catch(Exception e){}
 */
    
    //  return bim;
    
    return plot;
}


public void updateForm(){
    // sp.mixticsSV.setValue(xaxis.minor_tic_count);
    sp.transparencySV.setValue(xaxis.transparency);
    sp.miyticsSV.setValue(yaxis.minor_tic_count);
    sp.paintGridSV.setSelected(xaxis.paintGrid & yaxis.paintGrid);
    sp.paintGridSV.setSelected(xaxis.paintGrid & yaxis.paintGrid);
    sp.gridColorSV.setBackground(yaxis.gridcolor);
    sp.Xaxis.setText(xaxis.getTitleText());
    sp.Yaxis.setText(yaxis.getTitleText());
    
}


public void readForm(){
    /*
    topText.setText(sp.Header.getText());
    
    xaxis.paintGrid=yaxis.paintGrid=sp.paintGridSV.isSelected();
    
    xaxis.transparency= sp.transparencySV.getValue();
    yaxis.transparency= sp.transparencySV.getValue();
    
    xaxis.minimumSize= sp.mixsizeSV.getValue();
    
    xaxis.gridcolor = sp.gridColorSV.getBackground();
    yaxis.gridcolor = sp.gridColorSV.getBackground();
    
    if(sp.Xaxis.getText().length()>0) xaxis.setTitleText(sp.Xaxis.getText());
    if(sp.Yaxis.getText().length()>0) yaxis.setTitleText(sp.Yaxis.getText());
    
    xaxis.TICS_IN_BOTH_ENDS=sp.xticsbothsidesSV.isSelected();
    yaxis.TICS_IN_BOTH_ENDS=sp.yticsbothsidesSV.isSelected();
    
    yaxis.minor_tic_count=sp.miyticsSV.getValue();
    
    xaxis.setRotated(sp.RotXlabelsSV.isSelected());
    
    bgf.GradientType = sp.gradtypeSV.getSelectedIndex();
    bgf.Single = sp.singlebgSV.getColor();
    
    bgf.gradient1 = sp.grad1SV.getColor();
    bgf.gradient2 = sp.grad2SV.getColor();
    
    lineWidth=sp.lineSizeSV.getValue();
    
    setBackground(sp.chartbgSV.getColor());
    setForeground(sp.AxisColorSV.getBackground());
    scrollPane.getViewport().setOpaque(true);
    scrollPane.getViewport().setBackground(sp.chartbgSV.getColor());
    
    
    scrollPane.setBackground(sp.chartbgSV.getColor());
    //size = new Dimension(sp.mixsizeSV.getValue(), sp.minHeightSV.getValue());
    
   Dimension dim = new Dimension(Math.max(xaxis.predictLength()+yaxis.predictWidth()+xaxis.endLength(), sp.mixsizeSV.getValue()+xaxis.endLength()), sp.minHeightSV.getValue());
    
    
    // Dimension dim = new Dimension(200, sp.minHeightSV.getValue());
    
    setMinimumSize(dim);
    setPreferredSize(dim);
    setSize(dim);
    */
}






public void writeValues(){
/*    tools to = new tools();
    if(props!=null)to.writedialogStatus(sp,"LineChart",props);
    * 
    */
}

public void readValues(JDialog sp){
    /*
    tools to = new tools();
    if(props!=null)to.readialogStatus(sp,"LineChart",props);
    * 
    */
}






public JToolTip createToolTip()   {
    return( new JeToolTip( this ) );
}



public Dimension getPreferredScrollableViewportSize() {
    return getPreferredSize();
}


public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    
    // System.out.print("\nRectangle X:"+ visibleRect.x+" Y:"+visibleRect.y+" W:"+visibleRect.width+" H:"+visibleRect.height);
    
    //Get the current position.
    int currentPosition = 0;
    if (orientation == SwingConstants.HORIZONTAL) currentPosition = visibleRect.x;
    else currentPosition = visibleRect.y;
    
    //Return the number of pixels between currentPosition
    //and the nearest tick mark in the indicated direction.
    if (direction < 0) {
        int newPosition = currentPosition - (currentPosition / maxUnitIncrement) * maxUnitIncrement;
        return (newPosition == 0) ? maxUnitIncrement : newPosition;
    }
    else {
        return ((currentPosition / maxUnitIncrement) + 1) * maxUnitIncrement - currentPosition;
    }
}

public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
    if (orientation == SwingConstants.HORIZONTAL) return visibleRect.width - maxUnitIncrement;
    else return visibleRect.height - maxUnitIncrement;
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




public static void main(String[] args){
    /*
    double[] Tx = new double[]{0.0021, -0.004, -0.015, -0.004, 0.0021, -0.014, -0.03, -0.07, -0.0036, -0.014, -0.0042, -0.0034, -0.023, -0.0017}; //Raw X
    
    
    
    Hashtable props=null;
    
    try{
        java.io.FileInputStream istream = new java.io.FileInputStream("jexpress.cfg");
        java.io.ObjectInputStream p=new java.io.ObjectInputStream(istream);
        props=(java.util.Hashtable)p.readObject();
    }
    catch(Exception e){e.printStackTrace();}
    
    
    
//expresscomponents.DataSet data = jexpress.cluster.loadDataSet("resources/mindre.cfg","Yeast Elu");//"alpha1-18_2");//"nci60_fig3_nomiss.txt","Baldwin_alltimecourses_nomiss.txt","nci60_fig3_nomiss.txt","Alizadeh_65col_nomiss.txt"
        
    //DataSet data = jexpress.cluster.loadDataSet("c:/java/cvs/expresspro/mindre2.cfg","alpha1-18_2");
    
     //while(data==null){}
    
    if(data==null) System.out.print("missing data!");
    
    
        
   //if(true) return;
    
    final BoxPlot2 pl = new BoxPlot2();//props,data,jp);
    
    
    
    JScrollPane jp = new JScrollPane(pl);
    
    pl.setPropertiesAndScrollPane(props, jp);
    
    

    
    pl.setData(data,0.25,0.75,0.5);
    pl.forceFullRepaint(); 
    
    
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
    
    f.getContentPane().add(jp);
    
    
    
    
    f.setSize(400,300);
    f.show();
    */
    
}

/** Getter for property SelectedOnly.
 * @return Value of property SelectedOnly.
 *
 */
public boolean isSelectedOnly() {
    return SelectedOnly;
}

/** Setter for property SelectedOnly.
 * @param SelectedOnly New value of property SelectedOnly.
 *
 */
public void setSelectedOnly(boolean SelectedOnly) {
    this.SelectedOnly = SelectedOnly;
}


}
