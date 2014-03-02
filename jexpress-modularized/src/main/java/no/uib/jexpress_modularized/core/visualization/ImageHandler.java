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

import java.awt.Image;

import java.awt.datatransfer.Clipboard;
import java.io.Serializable;

import javax.swing.*;

/**
 * This is class specialized to handle Images.
 *
 */
public class ImageHandler extends java.awt.Component implements Serializable {

    String lastpath;
    double percentSize = 100;
    ImagePrintPreview pw;
    PrintPreview pw2;

    /**
     * Empty constructor
     *
     */
    public ImageHandler() {
    }

    /**
     * Saves the image by calling saveimage in the FileHandler class
     *
     * @param image The image to be saved
     *
     * @return Return value telling if the save was successful.
     *
     *
     *
     */
    public static boolean saveImage(Image image) {

        //GifEncoder ge;

        /*FileDialog fd;

         File file;

         

         fd=new FileDialog(new Frame("Enter filename"));

         fd.setFile(".gif");

         fd.setMode(FileDialog.SAVE);

         if(lastpath!= null && lastpath !=""){fd.setDirectory(lastpath);}

         

         fd.show();

         file=new File(fd.getDirectory()+fd.getFile());

         if(fd.getFile()!=null){

         this.lastpath=fd.getDirectory();

         try{

         

         FileOutputStream output=new FileOutputStream(file);

         BufferedOutputStream buffer=new BufferedOutputStream(output);

         

         new ImageToGif(image, output, false);

         

         output.close();

         

         }

         catch(IOException e){}

         

         }*/



        filehandler fh = new filehandler();

        fh.saveimage(image);





        return true;
    }

    public static void printImage(final Image image) {

        ImageHandler imh = new ImageHandler();

        imh.printImage(image, null);

        System.gc();

    }

    public static void printComponent(final java.awt.print.Printable p) {

        ImageHandler imh = new ImageHandler();

        imh.printComponent(p, null);

        System.gc();

    }

    /**
     * Print an image
     *
     * @param image The image to be saved
     *
     * @param owner The Frame that will handle the printJob
     *
     */
    public void printImage(final Image image, java.awt.Frame owner) {



        //  Thread runner = new Thread() {

        //      public void run() {

        //  printData(image);

        try {

            final ImagePanel pn = new ImagePanel(image);

            final javax.swing.JSlider sl = new javax.swing.JSlider(javax.swing.JSlider.HORIZONTAL, 0, 130, 100);



            //JFrame f = new JFrame("Image");

            //f.getContentPane().add("Center",pn);

            javax.swing.JPanel p = new javax.swing.JPanel();

            javax.swing.border.Border black = javax.swing.BorderFactory.createLineBorder(java.awt.Color.black);



            javax.swing.border.TitledBorder tb = new javax.swing.border.TitledBorder(black, "Image Resolution", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP);

            //  TitledBorder tb2 = new TitledBorder(black,"View",TitledBorder.LEFT,TitledBorder.TOP);



            p.setBorder(tb);

            p.setLayout(new java.awt.BorderLayout());

            p.add("Center", sl);



            sl.setMinorTickSpacing(10);

            sl.setPaintTicks(true);

            sl.setPaintLabels(true);

            sl.setPaintTrack(true);



            pw = new ImagePrintPreview(pn, "Preview");

            pw.getNorthPanel().add("Center", p);



            pw.validate();

            pw.repaint();



            sl.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent e) {

                    percentSize = (double) sl.getValue();

                    pn.repaint();



                    pw.update();

                    pw.repaint();

                    pw.validate();

                    //pw = new ImagePrintPreview(pn,"Preview");

                }
            });



        } catch (OutOfMemoryError e) {

            ErrorMsg msg = new ErrorMsg(17);

        }

        // f.getContentPane().add("North",sl);

        // JButton b = new JButton("print");

        // b.addActionListener(new ActionListener(){

        /*     

         public void actionPerformed(ActionEvent e){

                          

         percentSize=(double)sl.getValue();

                          

         try {

         PrinterJob prnJob = PrinterJob.getPrinterJob();

         prnJob.setPrintable(pn);

         if (!prnJob.printDialog()) return;

                    

         setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

         prnJob.print();

         setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                    

         // JOptionPane.showMessageDialog(this, "Printing completed successfully", "JPEGEditor2", JOptionPane.INFORMATION_MESSAGE);

         }

                

         catch (PrinterException ex) {

         ex.printStackTrace();

         System.err.println("Printing error: "+ex.toString());

         }

                           

         }

                      

                     

                        

         });

                    

                    

         f.getContentPane().add("South",b);

                    

         };

                

         };

         //      */

        //  runner.start();



        /*

         JFrame f = new JFrame();

         PrintJob pjob = getToolkit().getPrintJob(f, "Image Print", null);

         if(pjob!=null){

         Graphics g=pjob.getGraphics();

             

         try {

         Dimension d = pjob.getPageDimension();

             

         g.drawImage(image, 30, 60, null);

         // Clean up after print Job...

         g.dispose();

         pjob.end();

             

         }

         catch (Exception e) {

         e.printStackTrace();

         System.err.println("Printing error: "+e.toString());

         }}

             

         */





    }

    /**
     * Print an image
     *
     * @param image The image to be saved
     *
     * @param owner The Frame that will handle the printJob
     *
     */
    public void printComponent(final java.awt.print.Printable com, java.awt.Frame owner) {



        //  Thread runner = new Thread() {

        //      public void run() {

        //  printData(image);

        try {



            final javax.swing.JSlider sl = new javax.swing.JSlider(javax.swing.JSlider.HORIZONTAL, 0, 130, 100);



            //JFrame f = new JFrame("Image");

            //f.getContentPane().add("Center",pn);

            javax.swing.JPanel p = new javax.swing.JPanel();

            javax.swing.border.Border black = javax.swing.BorderFactory.createLineBorder(java.awt.Color.black);



            javax.swing.border.TitledBorder tb = new javax.swing.border.TitledBorder(black, "Image Resolution", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP);

            //  TitledBorder tb2 = new TitledBorder(black,"View",TitledBorder.LEFT,TitledBorder.TOP);



            p.setBorder(tb);

            p.setLayout(new java.awt.BorderLayout());

            p.add("Center", sl);



            sl.setMinorTickSpacing(10);

            sl.setPaintTicks(true);

            sl.setPaintLabels(true);

            sl.setPaintTrack(true);





            pw2 = new PrintPreview(com, "Preview");





            pw2.validate();

            pw2.repaint();







            sl.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent e) {

                    percentSize = (double) sl.getValue();

                    //pn.repaint();





                    pw2.repaint();

                    pw2.validate();

                    //pw = new ImagePrintPreview(pn,"Preview");

                }
            });



        } catch (OutOfMemoryError e) {

            ErrorMsg msg = new ErrorMsg(17);

        }

        // f.getContentPane().add("North",sl);

        // JButton b = new JButton("print");

        // b.addActionListener(new ActionListener(){

        /*     

         public void actionPerformed(ActionEvent e){

                          

         percentSize=(double)sl.getValue();

                          

         try {

         PrinterJob prnJob = PrinterJob.getPrinterJob();

         prnJob.setPrintable(pn);

         if (!prnJob.printDialog()) return;

                    

         setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

         prnJob.print();

         setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                    

         // JOptionPane.showMessageDialog(this, "Printing completed successfully", "JPEGEditor2", JOptionPane.INFORMATION_MESSAGE);

         }

                

         catch (PrinterException ex) {

         ex.printStackTrace();

         System.err.println("Printing error: "+ex.toString());

         }

                           

         }

                      

                     

                        

         });

                    

                    

         f.getContentPane().add("South",b);

                    

         };

                

         };

         //      */

        //  runner.start();



        /*

         JFrame f = new JFrame();

         PrintJob pjob = getToolkit().getPrintJob(f, "Image Print", null);

         if(pjob!=null){

         Graphics g=pjob.getGraphics();

             

         try {

         Dimension d = pjob.getPageDimension();

             

         g.drawImage(image, 30, 60, null);

         // Clean up after print Job...

         g.dispose();

         pjob.end();

             

         }

         catch (Exception e) {

         e.printStackTrace();

         System.err.println("Printing error: "+e.toString());

         }}

             

         */





    }

    /*       

     public void printData() {

     getJMenuBar().repaint();

     try {

     PrinterJob prnJob = PrinterJob.getPrinterJob();

     prnJob.setPrintable(m_panel);

     if (!prnJob.printDialog())

     return;

     setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

     prnJob.print();

     setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

     JOptionPane.showMessageDialog(this, "Printing completed successfully", "JPEGEditor2",

     JOptionPane.INFORMATION_MESSAGE);

     }

     catch (PrinterException e) {

     e.printStackTrace();

     System.err.println("Printing error: "+e.toString());

     }

     }

     */
    public void printData(Image im) {

        Printer p = new Printer(im);





        try {

            java.awt.print.PrinterJob prnJob = java.awt.print.PrinterJob.getPrinterJob();

            prnJob.setPrintable(p);

            if (!prnJob.printDialog()) {
                return;
            }



            setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

            prnJob.print();

            setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));



            javax.swing.JOptionPane.showMessageDialog(this, "Printing completed successfully", "JPEGEditor2", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (java.awt.print.PrinterException e) {

            e.printStackTrace();

            System.err.println("Printing_error:_" + e.toString());

        }



    }

    class ImagePanel extends javax.swing.JPanel implements java.awt.print.Printable {

        protected java.awt.image.BufferedImage m_bi = null;
        public int m_maxNumPage = 1;

        public ImagePanel(Image im) {

            m_bi = (java.awt.image.BufferedImage) im;

            setPreferredSize(new java.awt.Dimension(m_bi.getTileWidth(), m_bi.getHeight()));

        }

        public void paint(java.awt.Graphics g) {



            int w = m_bi.getWidth(this);

            int h = m_bi.getHeight(this);



            //int ww= (int)(w*(percentSize/100));

            //int hh=(int)(h*(percentSize/100));



            g.setColor(java.awt.Color.white);

            g.fillRect(0, 0, this.getWidth(), this.getHeight());





            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;

            g2d.scale(percentSize / 100.0, percentSize / 100.0);





            int ml = (this.getWidth() / 2) - (w / 2);

            int mh = (this.getHeight() / 2) - (h / 2);



            if (m_bi != null) {
                g2d.drawImage(m_bi, ml, mh, this);
            }







        }

        public int print(java.awt.Graphics pg, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {

            if (pageIndex >= m_maxNumPage || m_bi == null) {
                return NO_SUCH_PAGE;
            }



            pg.translate((int) pageFormat.getImageableX() + 1, (int) pageFormat.getImageableY() + 1);

            int wPage = (int) pageFormat.getImageableWidth() + 1;

            int hPage = (int) pageFormat.getImageableHeight() + 1;





            double scale = percentSize / 100.0;



            wPage = (int) (wPage / scale);

            hPage = (int) (hPage / scale);

            /*    

             int wPage = (int)pageFormat.getImageableWidth();

             int hPage = (int)pageFormat.getImageableHeight();



             int w = m_bi.getWidth(this);

             int h = m_bi.getHeight(this);

             if (w == 0 || h == 0)

             return NO_SUCH_PAGE;

             int nCol = Math.max((int)Math.ceil((double)w/wPage), 1);

             int nRow = Math.max((int)Math.ceil((double)h/hPage), 1);

             m_maxNumPage = nCol*nRow;



             int iCol = pageIndex % nCol;

             int iRow = pageIndex / nCol;

             int x = iCol*wPage;

             int y = iRow*hPage;

             int wImage = Math.min(wPage, w-x);

             int hImage = Math.min(hPage, h-y);

             */





            int w = m_bi.getWidth(this);

            int h = m_bi.getHeight(this);



            //int resizedw = (int)(w*(percentSize/100.0));

            //int resizedh = (int)(h*(percentSize/100.0)); 



            java.awt.Graphics2D g2d = (java.awt.Graphics2D) pg;

            g2d.scale(percentSize / 100.0, percentSize / 100.0);



            if (w == 0 || h == 0) {
                return NO_SUCH_PAGE;
            }



            int nCol = Math.max((int) Math.ceil((double) w / wPage), 1);

            int nRow = Math.max((int) Math.ceil((double) h / hPage), 1);



            m_maxNumPage = nCol * nRow;



            int iCol = pageIndex % nCol;

            int iRow = pageIndex / nCol;

            int x = iCol * wPage;

            int y = iRow * hPage;

            int wImage = Math.min(wPage, w - x);

            int hImage = Math.min(hPage, h - y);



            g2d.drawImage(m_bi, 0, 0, wImage, hImage, x, y, x + wImage, y + hImage, this);

            //pg.drawImage(m_bi, 0, 0, 40, 40,  x, y, x+wImage, y+hImage, this);

            System.gc();



            return PAGE_EXISTS;

        }
    }

    public static void ImageToClipBoard(Image i) {



        final Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();

        JLabel l = new JLabel("");





        l.setIcon(new ImageIcon(i));

        l.setTransferHandler(new ImageSelection());

        if (clipboard == null) {
            System.out.print("\ncb is null");
        }



        TransferHandler handler = l.getTransferHandler();





        if (handler == null) {
            System.out.print("\thc is null");
        }



        handler.exportToClipboard(l, clipboard, TransferHandler.COPY);





    }
}
