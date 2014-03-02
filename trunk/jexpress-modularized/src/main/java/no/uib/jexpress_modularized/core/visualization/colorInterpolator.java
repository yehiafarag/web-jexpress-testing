
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

package no.uib.jexpress_modularized.core.visualization;

import javax.swing.ImageIcon;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Color;
import java.io.Serializable;


public class colorInterpolator extends javax.swing.JLabel implements java.awt.event.ComponentListener, Serializable{
    public Color color1=Color.white;
    public Color color2=new Color(200,200,250);
    public Color color3=Color.blue;
    public Color color4=Color.red;
    public Color color5=Color.yellow;
    Image im;
    int[] pixels;
    
    
    public static void main(String[] args){
     javax.swing.JFrame jf = new javax.swing.JFrame();
     jf.setSize(300,20);
     jf.getContentPane().add(new colorInterpolator());
     jf.setVisible(true);
        
    }
    
    public void setColors(Color col1,Color col2,Color col3,Color col4,Color col5){
     color1=col1;
     color2=col2;
     color3=col3;
     color4=col4;
     color5=col5;
     Image i =getInterpolated();
     if(i!=null)setIcon( new ImageIcon(i));
    }
    
    
    public void setColor1(Color color1){
        this.color1=color1;
        Image i =getInterpolated();
        if(i!=null)setIcon( new ImageIcon(i));
    }
    
    public void setColor2(Color color2){
        this.color2=color2;
        Image i =getInterpolated();
        if(i!=null)setIcon( new ImageIcon(i));
    }

        public void setColor3(Color color3){
        this.color3=color3;
        Image i =getInterpolated();
        if(i!=null)setIcon( new ImageIcon(i));
    }
    
        public void setColor4(Color color4){
        this.color4=color4;
        Image i =getInterpolated();
        if(i!=null)setIcon( new ImageIcon(i));
    }
    
        public void setColor5(Color color5){
        this.color5=color5;
        Image i =getInterpolated();
        if(i!=null)setIcon( new ImageIcon(i));
    }
    
     public colorInterpolator(Color color1,Color color2,Color color3,Color color4,Color color5) {
    this.color1=color1;
    this.color2=color2;
    this.color3=color3;
    this.color4=color4;
    this.color5=color5;
         
         setText("");
        setPreferredSize(new Dimension(100,20));
        setSize(100,20);
        Image i =getInterpolated();
        if(i!=null)setIcon( new ImageIcon(i));
   }
    
    
    public colorInterpolator() {
        setText("");
        setPreferredSize(new Dimension(100,20));
        setSize(100,20);
        Image i =getInterpolated();
        if(i!=null)setIcon( new ImageIcon(i));
   }
    
   public int[] getInterpolatedColors(int length){
    
       int[] ret=new int[length];
    
    Dimension d=this.getSize();
    int frameheight=d.height;
    int framewidth=d.width;
    
       Color temp = null;
       int newred=0;
       int newgreen=0;
       int newblue=0;
       

       
       double[] red=new double[5];
       double[] green=new double[5];
       double[] blue=new double[5];
       
       red[0]=(double)color1.getRed();
       green[0]=(double)color1.getGreen();
       blue[0]=(double)color1.getBlue();
              
       red[1]=(double)color2.getRed();
       green[1]=(double)color2.getGreen();
       blue[1]=(double)color2.getBlue();
              
       red[2]=(double)color3.getRed();
       green[2]=(double)color3.getGreen();
       blue[2]=(double)color3.getBlue();
       
       red[3]=(double)color4.getRed();
       green[3]=(double)color4.getGreen();
       blue[3]=(double)color4.getBlue();
   
       red[4]=(double)color5.getRed();
       green[4]=(double)color5.getGreen();
       blue[4]=(double)color5.getBlue();
   
   
       double dqlength=(((double)length+1.0)/4.0);
              
       double tmp=0,tmp2=0;
       int itmp=0,itmp2=0;
       
       for(int i=0;i<ret.length;i++){
                
               tmp=((double)i)%(dqlength);
               tmp2=((double)i)/dqlength;
           
               itmp=(int)tmp;
               itmp2=(int)Math.floor(tmp2);
           
               //if(itmp2>ret.length-1)itmp2=ret.length-1;
               
               newred=(int) ((double)red[itmp2]+ (( (double)(red[itmp2+1]-red[itmp2])   /dqlength)*tmp));
                newgreen=(int) ((double)green[itmp2]+ (( (double)(green[itmp2+1]-green[itmp2])   /dqlength)*tmp));
                newblue=(int) ((double)blue[itmp2]+ (( (double)(blue[itmp2+1]-blue[itmp2])   /dqlength)*tmp));
            
           ret[i]=new Color(newred,newgreen,newblue).getRGB();
           
       }
       
       return ret;
   }
   
   
    public Image getInterpolated(){
    java.awt.image.BufferedImage img=null;
    addComponentListener(this);    
        
    Dimension d=this.getSize();
    int frameheight=d.height;
    int framewidth=d.width;
    double dstep=((double)framewidth/4.0);
    if(frameheight>0 && framewidth>0){  
    
        

    img=new java.awt.image.BufferedImage(framewidth,frameheight, java.awt.image.BufferedImage.TYPE_INT_ARGB);

    java.awt.Graphics2D g =(java.awt.Graphics2D) img.getGraphics();
    g.setColor(Color.black);
        
    int[] step=new int[4];
    
    
    
    for(int i=0;i<4;i++){
     step[i]=(int)((double)i*dstep);   
        
    }
    GradientPaint grad1 = new GradientPaint(0f,0f,color1,step[1], 0f,color2);
    GradientPaint grad2 = new GradientPaint(step[1],0f,color2,step[2], 0f,color3);
    GradientPaint grad3 = new GradientPaint(step[2],0f,color3,step[3], 0f,color4);
    GradientPaint grad4 = new GradientPaint(step[3],0f,color4,framewidth, 0f,color5);
    
    g.setPaint(grad1);
    g.fill(new java.awt.geom.Rectangle2D.Double(0, 0,step[1],frameheight));
    g.setPaint(grad2);
    g.fill(new java.awt.geom.Rectangle2D.Double(step[1], 0,step[1],frameheight));
    g.setPaint(grad3);
    g.fill(new java.awt.geom.Rectangle2D.Double(step[2], 0,step[1],frameheight));
    g.setPaint(grad4);
    g.fill(new java.awt.geom.Rectangle2D.Double(step[3], 0,step[1],frameheight));
    
    }
    
    
     pixels=new int[frameheight*framewidth];
     java.awt.image.PixelGrabber pg = new java.awt.image.PixelGrabber(img, 0, 0,framewidth, frameheight, pixels, 0, framewidth);
      try {pg.grabPixels();} catch (InterruptedException ie) {}
     
    this.im=img;
    return img;
    }

    public void componentShown(java.awt.event.ComponentEvent p1) {
    }    
    
    
    
    public void componentResized(java.awt.event.ComponentEvent p1) {
              
    Dimension d=this.getSize();
    int frameheight=d.height;
    int framewidth=d.width;
    
     Image i =getInterpolated();
     if(i!=null)setIcon( new ImageIcon(i));
    
    }    
    
    public void componentHidden(java.awt.event.ComponentEvent p1) {
    }    
    
    public void componentMoved(java.awt.event.ComponentEvent p1) {
    }    
    
    
}
