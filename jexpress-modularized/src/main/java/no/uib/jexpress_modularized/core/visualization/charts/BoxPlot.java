/*

 * BoxPlot.java

 *

 * Created on 15. mars 2004, 14:48

 */



package no.uib.jexpress_modularized.core.visualization.charts;

import java.awt.*;
import java.io.Serializable;

import java.text.*;








public class BoxPlot extends javax.swing.JComponent implements Serializable{

  private Univariate x=null;

  private double[] sortx;

  private int[] margins;

  private boolean box,haveMinMax,haveXLabel,haveTitle;

  private String xlab,title;

  private int width,height,n;

  private double min,Q1,median,Q3,max;

  private double iqr,lf,uf;

  private int lwi,uwi;

  private Dimension size;

  private Font labelFont,titleFont;

  private Image offScrImage;

  private Graphics offScrGraphics;

  

  /* Define constants */

 // private final static Format f=new Format("%1.2f");

  

  private NumberFormat f = NumberFormat.getNumberInstance();

  private final static Font DEFAULT_FONT=new Font("TimesRoman",Font.PLAIN,10),

                            trb12=new Font("TimesRoman",Font.BOLD,12);



  public BoxPlot() {

      super(); 

       

      margins=new int[4];

    margins[0]=0;

    margins[1]=0;

    margins[2]=0;

    margins[3]=0;

  }



  public void addNotify() {

    super.addNotify();

    if(width<1 || height<1)return; 

    offScrImage=createImage(width,height);

    offScrGraphics=offScrImage.getGraphics();

  }



  public void update(Graphics g) {

    paint(offScrGraphics);

    g.drawImage(offScrImage,0,0,this);

  }



  public void setSize(int width,int height) {

    this.width=width;

    this.height=height;

    size=new Dimension(width,height);

  }

  

  public void setArea(Rectangle area){

     this.height=area.height;

     margins[2]=area.y;

  }

  

  public BoxPlot(int width,int height) {

    super.setSize(width,height);

    this.width=width;

    this.height=height;

    size=new Dimension(width,height);

    margins=new int[4];

    margins[0]=0;

    margins[1]=0;

    margins[2]=0;

    margins[3]=0;

    super.setBackground(Color.white);

    haveXLabel=false;

    haveTitle=false;

    box=false;

    labelFont=DEFAULT_FONT;

    titleFont=trb12;

  }



  public BoxPlot(int width,int height,boolean box) {

    super.setSize(width,height);

    this.width=width;

    this.height=height;

    size=new Dimension(width,height);

    margins=new int[4];

    margins[0]=0;

    margins[1]=0;

    margins[2]=0;

    margins[3]=0;

    super.setBackground(Color.white);

    haveXLabel=false;

    haveTitle=false;

    this.box=box;

    labelFont=DEFAULT_FONT;

    titleFont=trb12;

  }



    public static void main(String[] args){

      

      javax.swing.JFrame frame = new javax.swing.JFrame();

      frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

      

      BoxPlot box = new BoxPlot(90,100);

      

      double[] dat = new double[]{1.0,2.0,4.0,2.0};

      double[] dat2 = new double[]{1.0,2.0,3.0,4.0,Double.NaN,Double.NaN,Double.NaN,5.0,6.0,7.0,8.0,9.0,Double.NaN,10.0,3.0,11.0,2.0,2.0,2.0,4.4,22.0,Double.NaN,Double.NaN,Double.NaN};

      

      //box.newData(dat,true);

      box.newData(dat2,new double[]{-4.0,52.0},true);

      

      frame.getContentPane().add(box);

      

      frame.setSize(300,300);

      

      frame.setVisible(true);

      

  }

  

  public void setMargins(int[] margins) {

    this.margins=(int[])margins.clone();

    if(x!=null) setBoxParams();

  }



  public void setGraphicsParams(boolean box) {

    this.box=box;

  }

/*

  public void addXLabel(String xlab) {

    this.xlab=xlab;

    if(!haveXLabel)

      margins[0]+=15;

    haveXLabel=true;

  }



  public void addTitle(String title) {

    this.title=title;

    if(!haveTitle)

      margins[3]+=15;

    haveTitle=true;

  }



  public void removeXLabel() {

    haveXLabel=false;

    margins[0]-=10;

  }



  public void removeTitle() {

    margins[3]-=15;

    haveTitle=false;

  }



  public void setLabelFont(Font labelFont) {

    this.labelFont=labelFont;

  }

valueArea

  public void setTitleFont(Font titleFont) {

    this.titleFont=titleFont;

  }

*/

  public void newData(Univariate data,boolean plot) {

    x=data;

    haveMinMax=false;

    setBoxParams();

    if(plot) repaint();

  }



  

  public void setminmax(double min, double max){

       this.min=min;

       this.max=max;

  }

  

  public void newData(Univariate data,double[] xlim,boolean plot) {

    x=data;

    min=xlim[0];

    max=xlim[1];

    setBoxParams();

    if(plot) repaint();

  }



  public void newData(double[] data,boolean plot, double Q1, double Q3, double median) {

    x=new Univariate(data);

    min=x.min();

    max=x.max();

    setBoxParams(Q1,Q3,median);

    

    if(plot) repaint();

  }



  public void newData(double[] data,double[] xlim,boolean plot) {

    x=new Univariate(data);

    min=xlim[0];

    max=xlim[1];

    setBoxParams();

    if(plot) repaint();

  }



   public void paint(Graphics g) {

       paint(g,10,10,50,100,0,100);

   }

  

  public void paint(Graphics g,int X,int Y,int nullv, int Vheight,int imax,int imin) {

      

    //  int Y=0;

    //  int X=0;

      

      //double tmp=max;

      //max=min;

      //min=tmp;

      

    if(x==null) return;

    g.setColor(Color.white);

    g.fillRect(0,0,width-1,height-1);

    int yOrigin=height,

        xOrigin=0,

        top=margins[0],

        left=margins[1],

        bottom=-margins[2],

        right=margins[3],

        xtick,

        xlabWidth,

        x1,y1,i;

    double scalex=(height)/(max-min);

    

    //scalex=Math.abs(scalex);

    

 //   System.out.print("\n"+scalex);

    

    double b=nullv+1;//-scalex*max;

//    b+=Y;

    

    

    FontMetrics fm=getFontMetrics(DEFAULT_FONT);

    g.setFont(DEFAULT_FONT);

    g.setColor(Color.black);



    

    

    

  //  if(box) g.drawRect(0,0,height-1,width-1);

    // draw x-axis

//    g.drawLine(xOrigin+left,yOrigin+bottom,width-right,yOrigin+bottom);



//    xtick=(int)((width-left-right)*.25);

//    for (i=0;i<5;i++) {

      // create x-axis tick marks and xlabs

//      g.drawLine(yOrigin+bottom,xOrigin+left+i*xtick,yOrigin+bottom+5,xOrigin+left+i*xtick);

//      double xLabel=min+i*(max-min)*.25;

//      xlabWidth=(int)(fm.stringWidth(f.format(xLabel))*.5);

//      g.drawString(f.form(xLabel),xOrigin+left+i*xtick-xlabWidth,yOrigin+bottom+15);

//    }



    // draw boxplot

    int y=X;//(int)((yOrigin+bottom-top)*0.67);

    int boxHeight=6;//Math.min(20,(int)((yOrigin+bottom-top)*0.25));

    

    

     Graphics2D g2d = (Graphics2D)g;

    

    GradientPaint p = new GradientPaint(0.0f,(float)imin,new Color(100,240,100),0.0f,(float)imax,new Color(100,100,240));

//    g2d.setPaint(p);

        

    for(i=0;i<lwi;i++)

      g.drawLine(y-2,(int)((sortx[i]*-scalex)  +b),y+2,(int)((sortx[i]*-scalex)+b));

      //g.drawString("*",y,(int)(sortx[i]*scalex+b));

    for(i=uwi+1;i<n;i++)

      g.drawLine(y-2,(int)(sortx[i]*-scalex+b),y+2,(int)(sortx[i]*-scalex+b));

    

    g.setColor(new Color(230,30,30));

//    g2d.setPaint(null);

 

    if(sortx!=null){

    g.drawLine(y-2,(int)(sortx[lwi]*-scalex+b),y+2,(int)(sortx[lwi]*-scalex+b));

    g.drawLine(y,(int)(sortx[lwi]*-scalex+b),y,(int)(Q1*-scalex+b));

    

    g.drawLine(y,(int)(sortx[uwi]*-scalex+b),y,(int)(Q3*-scalex+b));

    g.drawLine(y-2,(int)(sortx[uwi]*-scalex+b),y+2,(int)(int)(sortx[uwi]*-scalex+b));

    }

 //   int u = (int)(Q1*scalex+b);

 //   int d = (int)(Q3*scalex+b);

 //   int l = u-d;

    //g.drawRect(y-boxHeight,(int)(Q1*scalex+b),2*boxHeight,(int)(iqr*scalex));

    //g.drawLine(y-boxHeight,(int)(median*scalex+b),y+boxHeight,(int)(median*scalex+b));

    

//    g.drawRect(y-boxHeight,d,2*boxHeight,l);

    

    g.setColor(Color.blue);

//    g2d.setPaint(p);

   

    

    g.setColor(new Color(210,210,230,100));

    //g.fillRect(y-boxHeight,(int)((Q3*-scalex)+b),2*boxHeight,(int)(iqr*scalex));

    

    Rectangle rect = new Rectangle(y-boxHeight,(int)((Q3*-scalex)+b),2*boxHeight,(int)(iqr*scalex));
    
    
    Rectangle half1 = new Rectangle(rect.x,rect.y,rect.width/2,rect.height);
    Rectangle half2 = new Rectangle(rect.x+rect.width/2,rect.y,rect.width/2,rect.height);
    
    Color gradCol1 = new Color(255,100,100);
    Color gradCol2 = Color.white;  
    
            GradientPaint gp1 = new GradientPaint((float)half1.x, (float)half1.y, gradCol1,(float)half1.x+(float)half1.width, (float)half2.y, gradCol2);
            
            GradientPaint gp2 = new GradientPaint((float)half2.x, (float)half2.y, gradCol2,(float)half2.x+(float)half2.width, (float)half2.y, gradCol1);
            
            g2d.setPaint(gp1);  
            g2d.fill(half1);   
             g2d.setPaint(gp2);  
            g2d.fill(half2);   
    
    
//    g2d.setPaint(null);

    g.setColor(Color.black);

    g.drawRect(y-boxHeight,(int)((Q3*-scalex)+b),2*boxHeight,(int)(iqr*scalex));

    // System.out.print("\nScaleX:"+scalex);

        

    g.drawLine(y-boxHeight,(int)(median*-scalex+b),y+boxHeight,(int)(median*-scalex+b));

    

    

    

/*

    if(haveXLabel) {

      fm=getFontMetrics(labelFont);

      g.setFont(labelFont);

      int xlabCenter=(int)(fm.stringWidth(xlab)*.5);

      g.drawString(xlab,yOrigin-4,left+(int)((width-right)*.5)-xlabCenter);

    }

    if(haveTitle) {

      fm=getFontMetrics(titleFont);

      g.setFont(titleFont);

      int titleCenter=(int)(fm.stringWidth(title)*.5);

      g.drawString(title,top-2,(int)((width-right)*.5)-titleCenter);

    }

 */

  }



  private void setBoxParams() {

    sortx=x.sort();

    n=sortx.length;

    Q1=x.quant(0.25);

    median=x.quant(0.50);

    Q3=x.quant(0.75);

    iqr=Q3-Q1;

    lf=median-1.5*iqr;

    uf=median+1.5*iqr;

    lwi=x.compare(lf);

    uwi=x.compare(uf);

  }



  private void setBoxParams(double nQ1, double nQ3, double nmedian) {

    sortx=x.sort();

    n=sortx.length;

    Q1=x.quant(nQ1);

    median=x.quant(nmedian);

    Q3=x.quant(nQ3);

    iqr=Q3-Q1;

    lf=median-1.5*iqr;

    uf=median+1.5*iqr;

    lwi=x.compare(lf);

    uwi=x.compare(uf);

  }

  

  

  /* Canvas Functions */

  public Dimension getPreferredSize() {

    return(size);

  }



  public Dimension getMinimumSize() {

    return(size);

  }

}
class Univariate {

  private double[] x,sortx;

  private double[] summary=new double[6];

  private boolean isSorted=false;

  public double[] five=new double[5];

  private int n;

  private double mean,variance,stdev;

  private double median,min,Q1,Q3,max;



  public Univariate(double[] data) {

    //x=(double[])data.clone();

    n=0;

    for(int i=0;i<data.length;i++) if(!Double.isNaN(data[i])) n++;

    

    int counter=0;

    x=new double[n];

    

    for(int i=0;i<data.length;i++) if(!Double.isNaN(data[i])){ x[counter]=data[i]; counter++;}

    

    

    //n=x.length;

    

    

    createSummaryStats();

  }



  private void createSummaryStats() {

    int i;

    mean=0;

    for(i=0;i<n;i++)

      mean+=x[i];

    mean/=n;

    variance=variance();

    stdev=stdev();



    double sumxx=0;

    variance=0;

    for(i=0;i<n;i++)

      sumxx+=x[i]*x[i];

    if(n>1) variance=(sumxx-n*mean*mean)/(n-1);

    stdev=Math.sqrt(variance);

  }



  public double[] summary() {

    summary[0]=n;

    summary[1]=mean;

    summary[2]=variance;

    summary[3]=stdev;

    summary[4]=Math.sqrt(variance/n);

    summary[5]=mean/summary[4];

    return(summary);

  }





  public double mean() {

    return(mean);

  }



  public double variance() {

    return(variance);

  }



  public double stdev() {

    return(stdev);

  }



  public double SE() {

    return(Math.sqrt(variance/n));

  }



  public double max() {

    if(!isSorted) sortx=sort();

    return(sortx[n-1]);

  }



  public double min() {

    if(!isSorted) sortx=sort();

    return(sortx[0]);

  }

  

  public double median() {

    return(quant(0.50));

  }

    

  public double quant(double q) {

    if(!isSorted) sortx=sort();

    if (q > 1 || q < 0)

      return (0);

    else {

      double index=(n+1)*q;

      if (index-(int)index == 0)

        return sortx[(int)index - 1];

      else

        return q*sortx[(int)Math.floor(index)-1]+(1-q)*sortx[(int)Math.ceil(index)-1];

    }

  }



  public double[] sort() {

    sortx=(double[])x.clone();

    /*

    int incr=(int)(n*.5);

    while (incr >= 1) {

      for (int i=incr;i<n;i++) {

        double temp=sortx[i];

        int j=i;

        while (j>=incr && temp<sortx[j-incr]) {

          sortx[j]=sortx[j-incr];

          j-=incr;

        }

        sortx[j]=temp;

      }

      incr/=2;

    }

    isSorted=true;

    return(sortx);

    */

    isSorted=true;

    java.util.Arrays.sort(sortx);

    return sortx;

  }



  public double[] getData() {

    return(x);

  }



  public int size() {

    return (n);

  }



  public double elementAt(int index) {

    double element=0;

    try {

      element=x[index];

    }

    catch(ArrayIndexOutOfBoundsException e) {

      System.out.println("Index "+ index +" does not exist in data.");

    }

    return(element);

  }



  public double[] subset(int[] indices) {

    int k=indices.length,i=0;

    double elements[]=new double[k];

    try {

      for(i=0;i<k;i++)

        elements[i]=x[k];

    }

    catch(ArrayIndexOutOfBoundsException e) {

      System.out.println("Index "+ i +" does not exist in data.");

    }

    return(elements);

  }



  public int compare(double t) {

    int index=n-1;

    int i;

    boolean found=false;

    for(i=0;i<n && !found;i++)

      if(sortx[i]>t) {

        index=i;

        found=true;

      }

    return(index);

  }



  public int[] between(double t1,double t2) {

    int[] indices=new int[2];

    indices[0]=compare(t1);

    indices[1]=compare(t2);

    return(indices);

  }



  public int indexOf(double element) {

    int index=-1;

    for(int i=0;i<n;i++)

      if(Math.abs(x[i]-element)<1e-6) index=i;

    return(index);

  }

  

  



  

  

}



