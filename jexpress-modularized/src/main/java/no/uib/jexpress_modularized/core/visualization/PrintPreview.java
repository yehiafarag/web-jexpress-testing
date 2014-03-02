package no.uib.jexpress_modularized.core.visualization;



import java.awt.event.ActionListener;

import java.awt.Image;

import java.awt.Dimension;

import java.awt.Insets;
import java.io.Serializable;





public class PrintPreview extends javax.swing.JFrame implements Serializable

{

  protected int m_wPage;

  protected int m_hPage;

  protected java.awt.print.Printable m_target;

  protected javax.swing.JComboBox m_cbScale;

  protected PrintPreview.PreviewContainer m_preview;



  public PrintPreview(java.awt.print.Printable target) {

    this(target, "Print Preview");

  }



  public PrintPreview(java.awt.print.Printable target, String title) {

    super(title);

    setSize(600, 400);

    m_target = target;



    javax.swing.JToolBar tb = new javax.swing.JToolBar();

    javax.swing.JButton bt = new javax.swing.JButton("Print", new javax.swing.ImageIcon("print.gif"));

    ActionListener lst = new ActionListener() { 

      public void actionPerformed(java.awt.event.ActionEvent e) { 

        try {

          // Use default printer, no dialog

          java.awt.print.PrinterJob prnJob = java.awt.print.PrinterJob.getPrinterJob();

          prnJob.setPrintable(m_target);

          setCursor( java.awt.Cursor.getPredefinedCursor(

            java.awt.Cursor.WAIT_CURSOR));

          prnJob.print();

          setCursor( java.awt.Cursor.getPredefinedCursor(

            java.awt.Cursor.DEFAULT_CURSOR));

          dispose();

        }

        catch (java.awt.print.PrinterException ex) {

          ex.printStackTrace();

          System.err.println("Printing error: "+ex.toString());

        }

      }

    };

    bt.addActionListener(lst);

    bt.setAlignmentY(0.5f);

    bt.setMargin(new Insets(4,6,4,6));

    tb.add(bt);



    bt = new javax.swing.JButton("Close");

    lst = new ActionListener() { 

      public void actionPerformed(java.awt.event.ActionEvent e) { 

        dispose();

      }

    };

    bt.addActionListener(lst);

    bt.setAlignmentY(0.5f);

    bt.setMargin(new Insets(2,6,2,6));

    tb.add(bt);



    String[] scales = { "10 %", "25 %", "50 %", "100 %" };

    m_cbScale = new javax.swing.JComboBox(scales);

    lst = new ActionListener() { 

      public void actionPerformed(java.awt.event.ActionEvent e) { 

        Thread runner = new Thread() {

          public void run() {

            String str = m_cbScale.getSelectedItem().

              toString();

            if (str.endsWith("%"))

              str = str.substring(0, str.length()-1);

            str = str.trim();

              int scale = 0;

            try { scale = Integer.parseInt(str); }

            catch (NumberFormatException ex) { return; }

            int w = (int)(m_wPage*scale/100);

            int h = (int)(m_hPage*scale/100);



            java.awt.Component[] comps = m_preview.getComponents();

            for (int k=0; k<comps.length; k++) {

              if (!(comps[k] instanceof PagePreview))

                continue;

              PagePreview pp = (PagePreview)comps[k];

                pp.setScaledSize(w, h);

            }

            m_preview.doLayout();

            m_preview.getParent().getParent().validate();

          }

        };

        runner.start();

      }

    };

    m_cbScale.addActionListener(lst);

    m_cbScale.setMaximumSize(m_cbScale.getPreferredSize());

    m_cbScale.setEditable(true);

    tb.addSeparator();

    tb.add(m_cbScale);

    getContentPane().add(tb, java.awt.BorderLayout.NORTH);



    m_preview = new PrintPreview.PreviewContainer();



    java.awt.print.PrinterJob prnJob = java.awt.print.PrinterJob.getPrinterJob();

    java.awt.print.PageFormat pageFormat = prnJob.defaultPage();

    if (pageFormat.getHeight()==0 || pageFormat.getWidth()==0) {

      System.err.println("Unable to determine default page size");

        return;

    }

    m_wPage = (int)(pageFormat.getWidth());

    m_hPage = (int)(pageFormat.getHeight());

    int scale = 10;

    int w = (int)(m_wPage*scale/100);

    int h = (int)(m_hPage*scale/100);



    int pageIndex = 0;

    try {

      while (true) {

        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(m_wPage, 

          m_hPage, java.awt.image.BufferedImage.TYPE_INT_RGB);

        java.awt.Graphics g = img.getGraphics();

        g.setColor(java.awt.Color.white);

        g.fillRect(0, 0, m_wPage, m_hPage);

        if (target.print(g, pageFormat, pageIndex) != 

         java.awt.print.Printable.PAGE_EXISTS)

          break;

        PagePreview pp = new PagePreview(w, h, img);

        m_preview.add(pp);

        pageIndex++;

      }

    }

    catch (java.awt.print.PrinterException e) {

      e.printStackTrace();

      System.err.println("Printing error: "+e.toString());

    }



    javax.swing.JScrollPane ps = new javax.swing.JScrollPane(m_preview);

    getContentPane().add(ps, java.awt.BorderLayout.CENTER);



    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    setVisible(true);

  }



  class PreviewContainer extends javax.swing.JPanel

  {

    protected int H_GAP = 16;

    protected int V_GAP = 10;



    public Dimension getPreferredSize() {

      int n = getComponentCount();

      if (n == 0)

        return new Dimension(H_GAP, V_GAP);

      java.awt.Component comp = getComponent(0);

      Dimension dc = comp.getPreferredSize();

      int w = dc.width;

      int h = dc.height;

           

      Dimension dp = getParent().getSize();

      int nCol = Math.max((dp.width-H_GAP)/(w+H_GAP), 1);

      int nRow = n/nCol;

      if (nRow*nCol < n)

        nRow++;



      int ww = nCol*(w+H_GAP) + H_GAP;

      int hh = nRow*(h+V_GAP) + V_GAP;

      Insets ins = getInsets();

      return new Dimension(ww+ins.left+ins.right, 

        hh+ins.top+ins.bottom);

    }



    public Dimension getMaximumSize() {

      return getPreferredSize();

    }



    public Dimension getMinimumSize() {

      return getPreferredSize();

    }



    public void doLayout() {

      Insets ins = getInsets();

      int x = ins.left + H_GAP;

      int y = ins.top + V_GAP;



      int n = getComponentCount();

      if (n == 0)

        return;

      java.awt.Component comp = getComponent(0);

      Dimension dc = comp.getPreferredSize();

      int w = dc.width;

      int h = dc.height;

            

      Dimension dp = getParent().getSize();

      int nCol = Math.max((dp.width-H_GAP)/(w+H_GAP), 1);

      int nRow = n/nCol;

      if (nRow*nCol < n)

        nRow++;



      int index = 0;

      for (int k = 0; k<nRow; k++) {

        for (int m = 0; m<nCol; m++) {

          if (index >= n)

            return;

          comp = getComponent(index++);

          comp.setBounds(x, y, w, h);

          x += w+H_GAP;

        }

        y += h+V_GAP;

        x = ins.left + H_GAP;

      }

    }

  }



  class PagePreview extends javax.swing.JPanel

  {

    protected int m_w;

    protected int m_h;

    protected Image m_source;

    protected Image m_img;



    public PagePreview(int w, int h, Image source) {

      m_w = w;

      m_h = h;

      m_source= source;

      m_img = m_source.getScaledInstance(m_w, m_h, 

        Image.SCALE_SMOOTH);

      m_img.flush();

      setBackground(java.awt.Color.white);

      setBorder(new javax.swing.border.MatteBorder(1, 1, 2, 2, java.awt.Color.black));

    }



    public void setScaledSize(int w, int h) {

      m_w = w;

      m_h = h;

      m_img = m_source.getScaledInstance(m_w, m_h, 

        Image.SCALE_SMOOTH);

      repaint();

    }



    public Dimension getPreferredSize() {

      Insets ins = getInsets();

      return new Dimension(m_w+ins.left+ins.right, 

        m_h+ins.top+ins.bottom);

    }



    public Dimension getMaximumSize() {

      return getPreferredSize();

    }



    public Dimension getMinimumSize() {

      return getPreferredSize();

    }



    public void paint(java.awt.Graphics g) {

      g.setColor(getBackground());

      g.fillRect(0, 0, getWidth(), getHeight());

      g.drawImage(m_img, 0, 0, this);

      paintBorder(g);

    }

  }

}

