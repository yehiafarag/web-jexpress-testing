/*

 * LineMark.java

 *

 * Created on 8. juli 2003, 13:21

 */



package no.uib.jexpress_modularized.core.visualization.LineStyles;

import java.awt.*;

import java.awt.geom.*;

import java.util.*;

import javax.swing.*;

import java.io.*;

import no.uib.jexpress_modularized.core.visualization.*;

/**

 *

 * @author  bjarte dysvik

 */

public class LineMark extends javax.swing.JComponent implements javax.swing.ListCellRenderer, java.io.Serializable{

     private Shape s=null;

     private Color color = Color.black;

     private boolean fill = true;

     private int key=0;

     private LineMark nullMark = null;//new LineMark();

     static final long serialVersionUID = -58223433468437L;     

     

    /** Creates a new instance of LineMark */

    public LineMark() { 

      setBackground(Color.white);

      setOpaque(true);

      setPreferredSize(new Dimension(10,22));

     

    }

    

     public LineMark(Shape s,Color color,int key,boolean fill) {

        this.s=s;

        this.fill=fill;

        this.color=color;

        this.key=key;

        setBackground(Color.white);

        setOpaque(true);

        setPreferredSize(new Dimension(10,22));

        

    }

    

     public void setColor(Color c){

      this.color=c;   

     }

      

     public int getKey(){

      return key;   

     }

     

    

    public void paint(Graphics g){

        

       // Graphics g = this.getGraphics();

        Graphics2D g2d = (Graphics2D)g;

        

        int h = this.getHeight();

        int w = this.getWidth();

        int x = w/2;

        int y = h/2;

        

        

        

        g.setColor(getBackground());

        g.fillRect(0,0, w,h);

        

        

        

        if(s==null) return;

        g.setColor(Color.black);

      



        int xx =(int)( s.getBounds2D().getMaxX()/2);

        int yy = (int)( s.getBounds2D().getMaxY()/2);

        

        

      

        

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        

        g2d.translate(x-xx,y-yy);

        if(fill) g2d.fill(s);

        else g2d.draw(s);

        g2d.translate(-x+xx,-y+yy);

        

        paintBorder(g);

 

        

    }

    

    public int getBoundWidth(){

     if(s==null) return 0;

     return (int)( s.getBounds2D().getMaxX());

        

    }

    

    

    public void paintAt(int x,int y, Graphics g){

        if(s==null) return;

        

        Graphics2D g2d = (Graphics2D)g;



        g.setColor(color);

       

        

        int xx =(int)( s.getBounds2D().getMaxX()/2);

        int yy = (int)( s.getBounds2D().getMaxY()/2);

        

         Object KeyAntialias = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

        

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.translate(x-xx,y-yy);

        if(fill) g2d.fill(s);

        else g2d.draw(s);

        g2d.translate(-x+xx,-y+yy);

        

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, KeyAntialias);

    }

    

    public LineMark getCopy(){

        

     return new LineMark(s,color,key,fill);   

    }

    

    public static Vector getMarkVector(){

        

      Vector elements=new Vector();

      elements.addElement(null);   

      elements.addElement(new LineMark(new Polygon(new int[]{0,3,6,3},new int[]{3,0,3,6}, 4) ,Color.black,0,true));   
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 5, 5),Color.black,10,true));
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 3, 6),Color.black,20,true));
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 6, 3),Color.black,30,true));
      elements.addElement(new LineMark(new java.awt.geom.Rectangle2D.Double(0, 0, 5, 5),Color.black,40,true));
      elements.addElement(new LineMark(new Polygon(new int[]{0,3,5,7,10,7,5,3},new int[]{5,3,0,3,5,7,10,7}, 8) ,Color.black,50,true));
      elements.addElement(new LineMark(new Polygon(new int[]{0,3,6},new int[]{6,0,6}, 3) ,Color.black ,60,true));
      elements.addElement(new LineMark(new Polygon(new int[]{0,3,6,3},new int[]{3,0,3,4}, 4) ,Color.black,100,false));   
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 5, 5),Color.black,110,false));
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 3, 6),Color.black,120,false));
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 6, 3),Color.black,130,false));
      elements.addElement(new LineMark(new java.awt.geom.Rectangle2D.Double(0, 0, 5, 5),Color.black,140,false));
      elements.addElement(new LineMark(new Polygon(new int[]{0,3,5,7,10,7,5,3},new int[]{5,3,0,3,5,7,10,7}, 8) ,Color.black,150,false));
      elements.addElement(new LineMark(new Polygon(new int[]{0,3,6},new int[]{6,0,6}, 3) ,Color.black ,160,false));
      elements.addElement(new LineMark(new Polygon(new int[]{0,5,10,5},new int[]{5,0,5,10}, 4) ,Color.black,200,true));   
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 10, 10),Color.black,210,true));
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 5, 10),Color.black,220,true));
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 10, 5),Color.black,230,true));
      elements.addElement(new LineMark(new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10),Color.black,240,true));
      elements.addElement(new LineMark(new Polygon(new int[]{0,5,7,9,14,9,7,5},new int[]{7,5,0,5,7,9,14,9}, 8) ,Color.black,250,true));
      elements.addElement(new LineMark(new Polygon(new int[]{0,5,10},new int[]{10,0,10}, 3) ,Color.black ,260,true));
      elements.addElement(new LineMark(new Polygon(new int[]{0,5,10,5},new int[]{5,0,5,10}, 4) ,Color.black,300,false));   
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 10, 10),Color.black,310,false));
      elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 5, 10),Color.black,320,false));
     elements.addElement(new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 10, 5),Color.black,330,false));
      elements.addElement(new LineMark(new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10),Color.black,340,false));
      elements.addElement(new LineMark(new Polygon(new int[]{0,5,7,9,14,9,7,5},new int[]{7,5,0,5,7,9,14,9}, 8) ,Color.black,350,false));
      elements.addElement(new LineMark(new Polygon(new int[]{0,5,10},new int[]{10,0,10}, 3) ,Color.black ,360,false));

        return elements;

    }

    

    public void setShapeFromKey(int key){
        LineMark lm = getMark(key);
        this.s=lm.s;
        
        
    }

    public static LineMark getMark(int key){

       if (key==0) return new LineMark(new Polygon(new int[]{0,3,6,3},new int[]{3,0,3,4}, 4) ,Color.black,0,true);  

       else if (key==10) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 5, 5),Color.black,10,true);

       else if (key==20) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 3, 6),Color.black,20,true);

       else if (key==30) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 6, 3),Color.black,30,true);

       else if (key==40) return new LineMark(new java.awt.geom.Rectangle2D.Double(0, 0, 5, 5),Color.black,40,true);

       else if (key==50) return new LineMark(new Polygon(new int[]{0,3,5,7,10,7,5,3},new int[]{5,3,0,3,5,7,10,7}, 8) ,Color.black,50,true);

       else if (key==60) return new LineMark(new Polygon(new int[]{0,5,10},new int[]{10,0,10}, 3) ,Color.black,60,true );

       

       if (key==100) return new LineMark(new Polygon(new int[]{0,3,6,3},new int[]{3,0,3,4}, 4) ,Color.black,100,false);  

       else if (key==110) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 5, 5),Color.black,110,false);

       else if (key==120) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 3, 6),Color.black,120,false);

       else if (key==130) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 6, 3),Color.black,130,false);

       else if (key==140) return new LineMark(new java.awt.geom.Rectangle2D.Double(0, 0, 5, 5),Color.black,140,false);

       else if (key==150) return new LineMark(new Polygon(new int[]{0,3,5,7,10,7,5,3},new int[]{5,3,0,3,5,7,10,7}, 8) ,Color.black,150,false);

       else if (key==160) return new LineMark(new Polygon(new int[]{0,5,10},new int[]{10,0,10}, 3) ,Color.black,160,false );

       

      else if (key==200) return new LineMark(new Polygon(new int[]{0,5,10,5},new int[]{5,0,5,10}, 4) ,Color.black,200,true);   

      else if (key==220) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 10, 10),Color.black,210,true);

      else if (key==220) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 5, 10),Color.black,220,true);

      else if (key==230) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 10, 5),Color.black,230,true);

      else if (key==240) return new LineMark(new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10),Color.black,240,true);

      else if (key==250) return new LineMark(new Polygon(new int[]{0,5,7,9,14,9,7,5},new int[]{7,5,0,5,7,9,14,9}, 8) ,Color.black,250,true);

      else if (key==260) return new LineMark(new Polygon(new int[]{0,5,10},new int[]{10,0,10}, 3) ,Color.black ,260,true);



      else if (key==300) return new LineMark(new Polygon(new int[]{0,5,10,5},new int[]{5,0,5,10}, 4) ,Color.black,300,false);   

      else if (key==310) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 10, 10),Color.black,310,false);

      else if (key==320) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 5, 10),Color.black,320,false);

      else if (key==330) return new LineMark(new java.awt.geom.Ellipse2D.Double(0, 0, 10, 5),Color.black,330,false);

      else if (key==340) return new LineMark(new java.awt.geom.Rectangle2D.Double(0, 0, 10, 10),Color.black,340,false);

      else if (key==350) return new LineMark(new Polygon(new int[]{0,5,7,9,14,9,7,5},new int[]{7,5,0,5,7,9,14,9}, 8) ,Color.black,350,false);

      else if (key==360) return new LineMark(new Polygon(new int[]{0,5,10},new int[]{10,0,10}, 3) ,Color.black ,360,false);

       // else if (key==380) return new LineMark(new Polygon(new int[]{0,5,10,15,2,4,5},new int[]{10,0,10,9,3,4}, 6) ,Color.black ,360,false);
      

       

       else return null;

        

    }


    public Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        LineMark ret =null;

        

        if(value==null){

            



            

            if(nullMark==null){

                nullMark = new LineMark();   

                //nullMark = LineMark.getMark(0);

                //nullMark.setBackground(Color.white);

                }

            ret = nullMark;

        }

        

        else ret = (LineMark)value;

        

        if(isSelected) ret.setBackground(new Color(205,205,220));

        else ret.setBackground(Color.white);

        ret.setBorder(null);

        //if(cellHasFocus) ret.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(100,100,120),1));

        //else ret.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(240,240,250),1));

        

        return ret;

        

    }    

    

    

    public static void main(String[] args){

     

        javax.swing.JFrame f = new javax.swing.JFrame("testing");

        

       JComboBox com =  new JComboBox(LineMark.getMarkVector());

       com.setRenderer(new LineMark());

        

        f.getContentPane().add(com);

        

        f.pack();

        f.setVisible(true);

    }

    

    /*

     private Shape s=null;

     private Color color = Color.black;

     private boolean fill = true;

     private int key=0;

     private LineMark nullMark = null;//new LineMark();

     */

        

    private void writeObject(java.io.ObjectOutputStream out) throws IOException{

      Hashtable h = new Hashtable();  

      h.put("key",new Integer(key));

      h.put("color",color);

      h.put("fill",new Boolean(fill));

     // h.put("s",s);

      

      out.writeObject(h);

    }

    

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{

        Hashtable h = (Hashtable)in.readObject();

        if(h.containsKey("key")) key = ((Integer)h.get("key")).intValue();

        if(h.containsKey("color")) color = (java.awt.Color)h.get("color");

        if(h.containsKey("fill")) fill = ((Boolean)h.get("fill")).booleanValue();

        setShapeFromKey(key);
        
       // if(h.containsKey("s")) s = (Shape)h.get("s");

    }

    public Hashtable toHash() {
      Hashtable h = new Hashtable();  
      h.put("key",new Integer(key));
      h.put("color",color);
      h.put("fill",new Boolean(fill));
      return h;
    }

    public static LineMark fromHash(Hashtable hashtable) {
         LineMark ret = LineMark.getMark(((Integer)hashtable.get("key")).intValue());
         ret.setColor((Color)hashtable.get("color"));
         return ret;
    }

    

    

    

    

}

