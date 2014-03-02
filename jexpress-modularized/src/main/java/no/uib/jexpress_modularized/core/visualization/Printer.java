package no.uib.jexpress_modularized.core.visualization;

import java.io.Serializable;

public class Printer extends java.awt.Component implements java.awt.print.Printable, Serializable {

    static String lineToPrint=null;

    int x1;

    int y1;

    

    protected java.awt.Image m_bi = null;

   int m_maxNumPage =1; 

   

   public Printer(java.awt.Image im){

       m_bi = im;

   }

   

    

    public int print(java.awt.Graphics pg, java.awt.print.PageFormat pageFormat, int pageIndex) throws java.awt.print.PrinterException {

        if (pageIndex >= m_maxNumPage || m_bi == null) return NO_SUCH_PAGE;

        

        pg.translate((int)pageFormat.getImageableX(),(int)pageFormat.getImageableY());

        

        int wPage = (int)(pageFormat.getImageableWidth());

        int hPage = (int)(pageFormat.getImageableHeight());

        

        int w = m_bi.getWidth(this);

        int h = m_bi.getHeight(this);

        if (w == 0 || h == 0) return NO_SUCH_PAGE;

        

        int nCol = Math.max((int)Math.ceil((double)w/wPage), 1);

        int nRow = Math.max((int)Math.ceil((double)h/hPage), 1);

        m_maxNumPage = nCol*nRow;

        

        

        int iCol = pageIndex % nCol;

        int iRow = pageIndex / nCol;

        int x = iCol*wPage;

        int y = iRow*hPage;

        int wImage = Math.min(wPage, w-x);

        int hImage = Math.min(hPage, h-y);

        

        

        

        pg.drawImage(m_bi, 0, 0, wImage, hImage, x, y, x+wImage, y+hImage, this);

        //pg.drawImage(m_bi, (int)pageFormat.getImageableX(),(int)pageFormat.getImageableY(), wPage, hPage, 0, 0, w, h,  this);

        System.gc();

        

        return PAGE_EXISTS;

    }







}



