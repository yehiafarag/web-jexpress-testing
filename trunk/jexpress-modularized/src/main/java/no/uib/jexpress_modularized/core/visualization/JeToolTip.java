

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

import java.awt.Insets;
import java.io.Serializable;



public class JeToolTip extends javax.swing.JToolTip implements Serializable{



   // String tipText="";

    /** Creates new JeToolTip */

    javax.swing.JTextArea jt = new javax.swing.JTextArea();

    

    public JeToolTip() {
        java.awt.Font f = new java.awt.Font("Times New Roman",0,10);
        setTipText("");  

    }

    

    public JeToolTip(javax.swing.JComponent component) {

         javax.swing.ToolTipManager.sharedInstance().setInitialDelay(0);
        javax.swing.ToolTipManager.sharedInstance().setDismissDelay(3600000); 

//        super();     

        

        //setComponent( component ); 

    //    setBackground( Color.black );  

   //     setForeground( Color.red );

        

        super.setTipText("");  

        setTipText("");  


        jt.setMargin(new Insets(4,4,4,4));

        jt.setFont(getFont());

        jt.setText(component.getToolTipText());

        jt.setBackground(new java.awt.Color(230,230,240) );  

        this.setOpaque(false);

        super.setOpaque(false);

//        this.getRootPane().setOpaque(false);

         if(component==null || component.getToolTipText()==null) return;

        if(component.getToolTipText().equals("")) setVisible(false);

        else setVisible(true);

        

        this.setLayout(new java.awt.BorderLayout());

        this.add("Center",jt);

        //setPreferredSize(new Dimension(100,200));

        setPreferredSize(new java.awt.Dimension(jt.getPreferredSize().width+5,jt.getPreferredSize().height+5));

    }

  /*  

    public void paint(Graphics g){

        jt.paint(g);

        

    }

    */

    

   /* 

    public void setTipText(String tipText){

        this.tipText=tipText;

    }

        

    public String getTipText() {

        return tipText;}

     */   

    

    /*

    public JComponent getComponent(){

        

     JTextArea ta = new JTextArea();

     ta.setText("this.getTipText()");

        

        return ta;

    }

     

     */

    /*

    class CustomJToolTip extends JToolTip{

        public CustomJToolTip( JComponent component )    {

            super();     

            setComponent( component ); 

            setBackground( Color.black );  

            setForeground( Color.red );        // you can also do setFont(), and others.  

        }}

    */

    



}

