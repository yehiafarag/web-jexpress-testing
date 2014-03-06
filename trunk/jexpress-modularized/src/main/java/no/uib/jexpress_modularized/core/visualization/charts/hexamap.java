/*
 * hexamap.java
 *
 * Created on 6. april 2002, 17:31
 */

package no.uib.jexpress_modularized.core.visualization.charts;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;
import no.uib.jexpress_modularized.core.dataset.Dataset;




/**
 *
 * @author  bjarte dysvik
 */
public class hexamap extends javax.swing.JLabel implements ActionListener, Serializable {
    
     private static double DEG2RAD = Math.PI/180;  
     private static double RAD2DEG = 180/Math.PI;
    
     private Dataset data;
     
    private hexagon[][] components=new hexagon[3][6];
    //private square[][]  components2=new square[10][10];
    
    boolean hex=true;
    
    private double[][][] values;
    //private JeToolTip tip; //@TODO: reimplement me!
    public int compSize = 20;
    
    private Font font = new Font("Times new roman",0,9);
    public boolean blueAlpha = true;
    Rectangle scale = new Rectangle(100,100);
    
    private Color[][] colors;
    
    /** Creates new form hexamap */
    public hexamap() {
        initComponents();
        initHexas();
        this.setPreferredSize(new Dimension(200,200));
    }
    
    public void setDataSet(Dataset data){
        this.data=data;
    }
    
    public void setBlueAlpha(boolean blueAlpha){
        this.blueAlpha=blueAlpha;
        
        if(values==null) return;
        
        double maxvalue1= Double.MIN_VALUE;
        double maxvalue2= Double.MIN_VALUE;
        double maxvalue3= Double.MIN_VALUE;
        
        for(int i=0;i<components.length;i++){
            for(int j=0;j<components[i].length;j++){
                if(values[0][i][j]>maxvalue1) maxvalue1=values[0][i][j];
                if(values[1][i][j]>maxvalue2) maxvalue2=values[1][i][j];
                if(values[2][i][j]>maxvalue3) maxvalue3=values[2][i][j];
            }
        }
        
//        System.out.print("DSFG");
        
        for(int i=0;i<components.length;i++){
            for(int j=0;j<components[i].length;j++){

                if(values[0][i][j]<0 || values[1][i][j]<0 || values[2][i][j]<0){
                    components[i][j].color=Color.white;
                    continue;
                }


                if(blueAlpha) components[i][j].color=new Color((int)((values[1][i][j]/maxvalue2)*254), 254-(int)((values[0][i][j]/maxvalue1)*254),0,(int)((values[2][i][j]/maxvalue3)*254))    ;
                else components[i][j].color=new Color((int)((values[1][i][j]/maxvalue2)*254), 254-(int)((values[0][i][j]/maxvalue1)*254),0)    ;
            }
        }
        
        //if(blueAlpha) blueAlpha=false;
        //else blueAlpha=true;
        //mth.cl.props.put("blueAlpha","yes");
        repaint();
    }
    
    public hexamap(double[][] values) {
        initComponents();
        initHexas(values);
        setupMouseListener();
    }
    
    public hexamap(double[][][] values) {
        this.values=values;
        initComponents();
        
        initHexas(values);
        setupMouseListener();
    }
       
    
    public void setValues(double[][][] values,boolean hex){
        this.values=values;
        this.hex=hex;
        initHexas(values);
        setupMouseListener();
    }
    
    public void setColors(Color[][] colors){
        this.colors=colors;
        initHexas(colors);
        setupMouseListener();
    }

    public void setColors(Color[][] colors, boolean hex){
        this.colors=colors;
        this.hex = hex;
        initHexas(colors);
        setupMouseListener();
    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

    
    //@TODO: reimplement me!
//    public JToolTip createToolTip()   {
//        return( new JeToolTip( this ) );
//    }
    
    public ImageIcon getScale(){
       return new ImageIcon( getClass().getResource( "/expresscomponents/Charts/im/variancescale.jpg" ) ) ;
    }
    
    
    
    public void paint(Graphics g){
        int compSize=0;
        int panesize =0;
        int lattice = 0;
        
       //4 g.setColor(Color.red);
        //g.fillRect(0,0,100,100);
        
        //if(true) return;
        
        
         if(hex){
             panesize = Math.min(getWidth(),getHeight());
             lattice = panesize/ ((Math.max(components.length,components[0].length)*2)+4);
             compSize=lattice*2;
             
         }
         else{
             
             panesize = Math.min(getWidth(),getHeight());
             lattice = panesize/ (Math.max(components.length,components[0].length)+1);
             compSize=lattice;
         }

        int kat=compSize/2;

        int widthLeft=0;
        int heightLeft=0;
        
        
         if(hex){
            widthLeft=getWidth() - (compSize*(components[0].length)) +kat;  
            heightLeft = getHeight() - (compSize*(components.length)) -kat;
        }
        else{
            widthLeft=getWidth() - (compSize*(components[0].length));  
            heightLeft = getHeight() - (compSize*(components.length));
        }

        
        if(components!=null){
            for(int i=0;i<components.length;i++){
                for(int j=0;j<components[i].length;j++){
                    if(hex)components[i][j].resizeAndRelocate(compSize,new Point(compSize*j+((i%2)*kat) +(widthLeft/2)   ,(2*kat*i) +(heightLeft/2)   )    );
                    else components[i][j].resizeAndRelocate(compSize,new Point(compSize*j+(widthLeft/2)   ,(compSize*i) +(heightLeft/2)));
            }
         }
        }
        
        //g.setColor(Color.red);
        
        g.setColor(getBackground());
        g.fillRect(0,0,getSize().width,getSize().height);
        
        if (blueAlpha) paintDiagonalLines(g);
        
        g.setColor(Color.black);
        
        Graphics2D g2d = (Graphics2D)g;
        //g2d.setStroke(new BasicStroke(2.0f));
        
        if(components!=null){
            for(int i=0;i<components.length;i++){
                for(int j=0;j<components[i].length;j++){
                    if(components[i][j]!=null){
                        
                        if(colors!=null)components[i][j].color=colors[i][j];
                        components[i][j].paint(g);
                    }
                }
            }
        }
    }
    
    public void setupMouseListener(){
        
        // setToolTipText("this is a test");
        
        addMouseMotionListener(new MouseMotionAdapter(){
            
            public void mouseMoved(MouseEvent e){
               
                if(values==null) return;
                
                for(int i=0;i<components.length;i++){
                    for(int j=0;j<components[0].length;j++){
                        
                        if(components[i][j]!=null && components[i][j].poly!=null && components[i][j].poly.contains(e.getX(),e.getY())) {
                          
                            java.math.BigDecimal bd = new java.math.BigDecimal(values[0][i][j]);
                            bd = bd.setScale(3,java.math.BigDecimal.ROUND_UP);
                            
                            String tip = "Within Variance: "+bd.doubleValue();
                            
                            bd = new java.math.BigDecimal(values[1][i][j]);
                            bd = bd.setScale(3,java.math.BigDecimal.ROUND_UP);
                            
                            tip+="\nBetween Variance: "+bd.doubleValue();
                            tip+="\nCluster Size: "+new Double(values[2][i][j]).intValue();
                            
                            setToolTipText(tip);
                        }
                        
                    }
                }
            }
        });
    }
    
    
    
    public void initHexas(){
        
        //System.out.print("\nOpening window");
        
        int kat = (int)Math.sqrt( (compSize*compSize)/2  );
        
        for(int i=0;i<components.length;i++){
            for(int j=0;j<components[i].length;j++){
                if(hex) components[i][j]=new hexagon(compSize,new Point(2*kat*i+((j%2)*kat),2*kat*j),null);
                else components[i][j]=new square(compSize,new Point(compSize*i,compSize*j),null);
                components[i][j].addActionListener(this);
                addMouseListener(components[i][j]);
                
            }
        }
        //setPreferredSize(new Dimension((int) Math.max(scale.getHeight(),  (2*kat*components.length)+1+kat ),  (int)((2*kat*components[0].length)+kat+scale.getWidth())));
    }
    
    
    public void initHexas(double[][] values){
        int kat = (int)Math.sqrt( (compSize*compSize)/2  );
        components=new hexagon[values.length][values[0].length];
        
        double maxvalue= Double.MIN_VALUE;
        
        for(int i=0;i<components.length;i++){
            for(int j=0;j<components[i].length;j++){
                if(values[i][j]>maxvalue) maxvalue=values[i][j];
            }
        }
        
        
        for(int i=0;i<components.length;i++){
            for(int j=0;j<components[i].length;j++){
                components[i][j]=new hexagon(compSize,new Point(2*kat*i+((j%2)*kat),2*kat*j),new Color(0, (int)((values[i][j]/maxvalue)*254),0));
                components[i][j].addActionListener(this);
                addMouseListener(components[i][j]);
                
            }
        }
       // setPreferredSize(new Dimension((2*kat*components.length)+1+kat,(2*kat*components[0].length)+kat  ));
        
        
        
    }
    
    
    
    public void initHexas(double[][][] values){
        boolean blueSize = false;
        
        int kat = compSize/2;//(int)Math.sqrt( (compSize*compSize)/2  );
        components=new hexagon[values[0].length][values[0][0].length];
        
 
        
        double maxvalue1= Double.MIN_VALUE;
        double maxvalue2= Double.MIN_VALUE;
        double maxvalue3= Double.MIN_VALUE;
        
        for(int i=0;i<components.length;i++){
            for(int j=0;j<components[i].length;j++){
                if(values[0][i][j]>maxvalue1) maxvalue1=values[0][i][j];
                if(values[1][i][j]>maxvalue2) maxvalue2=values[1][i][j];
                if(values[2][i][j]>maxvalue3) maxvalue3=values[2][i][j];
            }
        }
        
        
        for(int i=0;i<components.length;i++){
            for(int j=0;j<components[i].length;j++){
                //if(blueSize)components[i][j]=new hexagon(compSize,new Point(2*kat*j+((i%2)*kat),2*kat*i),new Color(254-(int)((values[0][i][j]/maxvalue1)*254), (int)((values[1][i][j]/maxvalue2)*254),(int)((values[2][i][j]/maxvalue3)*254)));
                //else components[i][j]=new hexagon(compSize,new Point(2*kat*j+((i%2)*kat),2*kat*i),new Color(254-(int)((values[0][i][j]/maxvalue1)*254), (int)((values[1][i][j]/maxvalue2)*254),0));


                if(values[0][i][j]<0 || values[1][i][j]<0 || values[2][i][j]<0){
                    if(hex)components[i][j]=new hexagon(compSize,new Point(compSize*j+((i%2)*kat),2*(kat)*i),Color.white);
                    else components[i][j]=new square(compSize,new Point(compSize*i,compSize*j),Color.white);

                    continue;
                }


                if(blueSize){
                    
                    if(hex)components[i][j]=new hexagon(compSize,new Point(compSize*j+((i%2)*kat),2*(kat)*i),new Color((int)((values[1][i][j]/maxvalue2)*254.0),254- (int)((values[0][i][j]/maxvalue1)*254.0),(int)((values[2][i][j]/maxvalue3)*254.0)));
                    else components[i][j]=new square(compSize,new Point(compSize*i,compSize*j),new Color((int)((values[0][i][j]/maxvalue1)*254.0),254-(int)((values[1][i][j]/maxvalue2)*254.0),(int)((values[2][i][j]/maxvalue3)*254.0)));
                }
                else if(blueAlpha){
                 
                    if(hex)components[i][j]=new hexagon(compSize,new Point(compSize*j+((i%2)*kat),2*(kat)*i),new Color((int)((values[1][i][j]/maxvalue2)*254.0),254- (int)((values[0][i][j]/maxvalue1)*254.0),0,(int)((values[2][i][j]/maxvalue3)*254.0))    );
                    else components[i][j]=new square(compSize,new Point(compSize*i,compSize*j),new Color((int)((values[0][i][j]/maxvalue1)*254.0),254- (int)((values[1][i][j]/maxvalue2)*254),0,(int)((values[2][i][j]/maxvalue3)*254)));
                }
                else{
                    if(hex)components[i][j]=new hexagon(compSize,new Point(compSize*j+((i%2)*kat),2*(kat)*i),new Color((int)((values[1][i][j]/maxvalue2)*254.0),254- (int)((values[0][i][j]/maxvalue1)*254.0),0)    );
                    else components[i][j]=new square(compSize,new Point(compSize*i,compSize*j),new Color((int)((values[0][i][j]/maxvalue1)*254.0),254-(int)((values[1][i][j]/maxvalue2)*254.0),0));
                    
                }
                components[i][j].addActionListener(this);
                addMouseListener(components[i][j]);
            }
        }
        
        //setPreferredSize(new Dimension((2*kat*components.length)+1+kat,(2*kat*components[0].length)+kat  ));
    }
    
    
    public void initHexas(Color[][] colors){
        boolean blueSize = false;
        
        int kat = compSize/2;//(int)Math.sqrt( (compSize*compSize)/2  );
        components=new hexagon[colors.length][colors[0].length];
   
        
        for(int i=0;i<components.length;i++){
            for(int j=0;j<components[i].length;j++){

                
                if(blueSize){
                    
                    if(hex)components[i][j]=new hexagon(compSize,new Point(compSize*j+((i%2)*kat),2*(kat)*i),colors[i][j]);
                    else components[i][j]=new square(compSize,new Point(compSize*i,compSize*j),colors[i][j]);
                }
                else if(blueAlpha){
                 
                    if(hex)components[i][j]=new hexagon(compSize,new Point(compSize*j+((i%2)*kat),2*(kat)*i),colors[i][j] );
                    else components[i][j]=new square(compSize,new Point(compSize*i,compSize*j),colors[i][j]);
                }
                else{
                    if(hex)components[i][j]=new hexagon(compSize,new Point(compSize*j+((i%2)*kat),2*(kat)*i),colors[i][j]);
                    else components[i][j]=new square(compSize,new Point(compSize*i,compSize*j),colors[i][j]);
                    
                }
                components[i][j].addActionListener(this);
                addMouseListener(components[i][j]);
            }
        }
    }
    
    
    
    
    public void paintDiagonalLines(Graphics g){
        
         g.setColor(new Color(100,100,100));
         Graphics2D g2d = (Graphics2D)g;
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         int length = Math.max(getWidth(),getHeight());
         int counter = 0;
         //System.out.print("*");
         for(int i=0;  i< length/2    ;i++){
             
         g.drawLine(0, i*8, i*8, 0  );
         //g.drawLine(i*4,0, 0, i*4 );
         }
        
        // for(int i=0;i<this.getWidth()*2;i=i*4){
        //    g.drawLine(0, i, i, 0  );
        // }
         g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    
    
    
    public static void main(String[]args){
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        hexamap h = new hexamap();
        
        f.getContentPane().add("Center",h);
        //f.getContentPane().add("North",h.getScale());
        //f.getContentPane().add("North",new JLabel("This is a test"));

        f.setSize(new Dimension(400,300));
        f.setVisible(true);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Point p =  null;
        
        for(int i=0;i<components.length;i++){
            for(int j=0;j<components[i].length;j++){
                if(components[i][j].equals(actionEvent.getSource())) p=new Point(i,j);
            }
        }
        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    
}
class hexagon extends JButton implements MouseListener{
    public int size=10;
    public Point location = new Point(20,20);
    public Color color=null;
    public Polygon poly;
    private int[] x=new int[6];
    private int[] y=new int[6];
    
    
    public hexagon(int size,Point location,Color color){
        this.size=size;
        this.color=color;
        this.location=location;
        generatecoords();
        //generateRandomColor();
        addMouseListener(this);
    }
    
    public void resizeAndRelocate(int size,Point location){
        this.size=size;
        this.location=location;
        generatecoords();
   }
    
    
    
    public void generateRandomColor(){
        color= new Color(0,(int)(Math.random()*255),0);
    }
    
    
    public void generatecoords(){
        //int kat = (int)Math.sqrt( (size*size)/2  );
        double hlf = size/2;
        
        int x0 = location.x;
        int y0 = location.y;
        x[0]= (int)(x0+hlf);
        x[1]= (int)(x0+hlf*2.0);
        x[2]= (int)(x0+hlf*2.0);
        x[3]= (int)(x0+hlf);
        x[4]= (int)x0;
        x[5]= (int)x0;
        
        y[0]= (int)y0;
        y[1]= (int)(y0+hlf);
        y[2]= (int)(y0+hlf+hlf);
        y[3]= (int)(y0+(3*hlf));
        y[4]= (int)(y0+hlf+hlf);
        y[5]= (int)(y0+hlf);
        
        poly=new Polygon(x, y, 6);
    }
    
    public void paint(Graphics g){
        //Graphics2D g2d = (Graphics2D)g;
        if(color!=null){
            g.setColor(color);
            g.fillPolygon(x,y,6);
        }
        g.setColor(Color.black);
        g.drawPolygon(x,y,6);
    }
    
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        if(poly!=null && poly.contains((int)mouseEvent.getX(),(int)mouseEvent.getY())) 
            this.fireActionPerformed(new ActionEvent(this,0,"hexa"));
    }
    
    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }
    
}



class square extends hexagon{
    //public int size=10;
    //public Point location = new Point(20,20);
    //public Color color=null;
    //public Polygon poly;
    private int[] x=new int[4];
    private int[] y=new int[4];
    
   //  public square(){}
    
    public square(int size,Point location,Color color){
        super(size,location,color);
    }
     /*   
        this.size=size;
        this.color=color;
        this.location=location;
        generatecoords();
     //   generateRandomColor();
        addMouseListener(this);
    }
    */
    public void resizeAndRelocate(int size,Point location){
        this.size=size;
        this.location=location;
        generatecoords();
   }
    
    public void generateRandomColor(){
        color= new Color(0,0,(int)(Math.random()*255));
    }
    
    
    public void generatecoords(){
        x=new int[4];
        y=new int[4];
        int x0 = location.x;
        int y0 = location.y;
        x[0]= (int)x0;
        x[1]= (int)x0+size;
        x[2]= (int)x0+size;
        x[3]= (int)x0;
        
        y[0]= (int)y0;
        y[1]= (int)y0;
        y[2]= (int)y0+size;
        y[3]= (int)y0+size;
        
        poly=new Polygon(x, y, 4); 
    }
    
 public void paint(Graphics g){
  //Graphics2D g2d = (Graphics2D)g;   
     if(color!=null){
         g.setColor(color);
         g.fillPolygon(x,y,4);
     }
     
     g.setColor(Color.black);
     g.drawPolygon(x,y,4);
     
     
 }
    
 public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
     if(poly!=null && poly.contains((int)mouseEvent.getX(),(int)mouseEvent.getY())) this.fireActionPerformed(new ActionEvent(this,0,"hexa"));
 } 
    
 public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
 } 
    
 public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
 } 
    
 public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
 }
 
 public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
 }
 
}