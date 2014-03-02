/*

 * buttonFramer.java

 *

 * Created on 26. april 2002, 12:08

 */
package no.uib.jexpress_modularized.core.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import java.awt.event.MouseEvent;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Hashtable;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.Border;
//import jexpress.cluster;

/**
 *
 *
 *
 * @author  bjarte dysvik
 *
 */
public class buttonFramer implements java.awt.event.MouseListener ,Serializable{

    boolean doFrame = false;
    private Border onBorder = new javax.swing.border.LineBorder(new java.awt.Color(200, 200, 220), 1);
    //private Border offBorder = new javax.swing.border.LineBorder(new java.awt.Color(35,35,75),1);
    private Border offBorder = new javax.swing.border.EmptyBorder(1, 1, 1, 1);
    //private Border offBorder =null;
    private Hashtable<AbstractButton, Icon> offIcons = new Hashtable<AbstractButton, Icon>();
    private Hashtable<AbstractButton, Icon> onIcons = new Hashtable<AbstractButton, Icon>();
   // private cluster cl;
/*
    public buttonFramer(cluster cl) {
        this.cl = cl;
    }
*/
    public buttonFramer() {
    }

    public void addBar(javax.swing.JPanel p) {

        for (int i = 0; i < p.getComponentCount(); i++) {

            if (p.getComponent(i) instanceof AbstractButton) {

                ((AbstractButton) p.getComponent(i)).addMouseListener(this);
/*
                if (cl != null) {
                    ((AbstractButton) p.getComponent(i)).addActionListener(cl);
                }
*/
                if (!(p.getComponent(i) instanceof ColorButton)) {
                    ((AbstractButton) p.getComponent(i)).setBackground((new java.awt.Color(214, 204, 240)));
                }

                //((AbstractButton)p.getComponent(i)).setBorder(null);

                //((AbstractButton)p.getComponent(i)).setBorder(new javax.swing.border.LineBorder(new java.awt.Color(160,160,160),1));
            }

            if (p.getComponent(i) instanceof JPanel) {
                addBar((JPanel) p.getComponent(i));

            }
        }

    }

    public void removeBar(javax.swing.JPanel p) {
        for (int i = 0; i < p.getComponentCount(); i++) {

            if (p.getComponent(i) instanceof AbstractButton) {
                ((AbstractButton) p.getComponent(i)).removeMouseListener(this);
            }

            if (p.getComponent(i) instanceof JPanel) {
                removeBar((JPanel) p.getComponent(i));

            }

        }
    }

    //Draws a frame and highlight it on mouseEnter if doFrame = true.
    public void addBar(javax.swing.JPanel p, boolean doFrame) {

        this.doFrame = doFrame;

        for (int i = 0; i < p.getComponentCount(); i++) {

            if (p.getComponent(i) instanceof AbstractButton) {

                ((AbstractButton) p.getComponent(i)).addMouseListener(this);

                if (!(p.getComponent(i) instanceof ColorButton)) {
                    ((AbstractButton) p.getComponent(i)).setBackground((new java.awt.Color(214, 204, 240)));
                }
                ((AbstractButton) p.getComponent(i)).setBorderPainted(true);
                ((AbstractButton) p.getComponent(i)).setBorder(offBorder);
/*
                if (cl != null && ((AbstractButton) p.getComponent(i)).getIcon() instanceof ImageIcon) {

                    ImageIcon I = (ImageIcon) ((AbstractButton) p.getComponent(i)).getIcon();
                    ImageIcon ic = new disabledIcon(I, (AbstractButton) p.getComponent(i));
                    ((AbstractButton) p.getComponent(i)).setDisabledIcon(ic);

                    ((AbstractButton) p.getComponent(i)).addActionListener(cl);
                }
                
                */
            }

            if (p.getComponent(i) instanceof JPanel) {
                addBar((JPanel) p.getComponent(i), doFrame);

            }
        }

    }

    public void addButton(javax.swing.JButton b) {

        b.addMouseListener(this);

        b.setBorder(offBorder);

    }

    public void addBar(javax.swing.JToolBar p) {

        for (int i = 0; i < p.getComponentCount(); i++) {

            if (p.getComponent(i) instanceof AbstractButton) {

                ((AbstractButton) p.getComponent(i)).addMouseListener(this);

                if (!(p.getComponent(i) instanceof ColorButton)) {

                    //((AbstractButton) p.getComponent(i)).setOpaque(true);
                    //((AbstractButton) p.getComponent(i)).setBackground((new java.awt.Color(214, 204, 240)));
                    ((AbstractButton) p.getComponent(i)).setBorder(offBorder);
                    ((AbstractButton) p.getComponent(i)).setBorderPainted(false);
                    //((AbstractButton) p.getComponent(i)).setContentAreaFilled(true);

                }



            }

        }

    }

    public void addActionListeners(javax.swing.JPanel p, ActionListener listener) {

        for (int i = 0; i < p.getComponentCount(); i++) {

            if (p.getComponent(i) instanceof AbstractButton) {
                ((AbstractButton) p.getComponent(i)).addActionListener(listener);
            }

        }

    }

    public void removeActionListeners(javax.swing.JPanel p, ActionListener listener) {

        for (int i = 0; i < p.getComponentCount(); i++) {

            if (p.getComponent(i) instanceof AbstractButton) {
                ((AbstractButton) p.getComponent(i)).removeActionListener(listener);
            }

        }

    }

    public void addActionListeners(javax.swing.JToolBar p, ActionListener listener) {

        for (int i = 0; i < p.getComponentCount(); i++) {

            if (p.getComponent(i) instanceof AbstractButton) {
                ((AbstractButton) p.getComponent(i)).addActionListener(listener);
            }

        }

    }

    public void removeActionListeners(javax.swing.JToolBar p, ActionListener listener) {

        for (int i = 0; i < p.getComponentCount(); i++) {

            if (p.getComponent(i) instanceof AbstractButton) {
                ((AbstractButton) p.getComponent(i)).removeActionListener(listener);
            }

        }

    }

    public void addActionListeners(javax.swing.JMenuBar p, ActionListener listener) {

        javax.swing.JMenu tmp = null;

        for (int k = 0; k < p.getMenuCount(); k++) {

            tmp = p.getMenu(k);

            for (int i = 0; i < tmp.getMenuComponentCount(); i++) {

                if (tmp.getMenuComponent(i) instanceof javax.swing.JMenuItem) {
                    ((javax.swing.JMenuItem) tmp.getMenuComponent(i)).addActionListener(listener);
                }

            }

        }

    }

    public void removeActionListeners(javax.swing.JMenuBar p, ActionListener listener) {

        javax.swing.JMenu tmp = null;

        for (int k = 0; k < p.getMenuCount(); k++) {

            tmp = p.getMenu(k);

            for (int i = 0; i < tmp.getMenuComponentCount(); i++) {

                if (tmp.getMenuComponent(i) instanceof javax.swing.JMenuItem) {
                    ((javax.swing.JMenuItem) tmp.getMenuComponent(i)).removeActionListener(listener);
                }

            }

        }

    }

    public void mouseClicked(MouseEvent mouseEvent) {
    }

    public void mouseEntered(MouseEvent mouseEvent) {

        if (doFrame && ((AbstractButton) mouseEvent.getSource()).isEnabled()) {
            ((AbstractButton) mouseEvent.getSource()).setBorder(onBorder);

            //if (((AbstractButton) mouseEvent.getSource()).getWidth() < 25 && ((AbstractButton) mouseEvent.getSource()).getHeight() < 25) {

            if (!offIcons.containsKey(mouseEvent.getSource())) {
                offIcons.put((AbstractButton) mouseEvent.getSource(), ((AbstractButton) mouseEvent.getSource()).getIcon());
            }

            if (!onIcons.containsKey(mouseEvent.getSource())) {
                onIcons.put((AbstractButton) mouseEvent.getSource(), createLightenedIcon(((AbstractButton) mouseEvent.getSource()).getIcon()));
            }

//                ((AbstractButton) mouseEvent.getSource()).setIcon(onIcons.get((AbstractButton) mouseEvent.getSource()));

            //((AbstractButton) mouseEvent.getSource()).setBackground(new Color(230, 230, 245));
            ((AbstractButton) mouseEvent.getSource()).setForeground(new Color(100, 100, 200));
//                ((AbstractButton)mouseEvent.getSource()).setBorderPainted(true);
//                ((AbstractButton)mouseEvent.getSource()).setBorder(new LineBorder(Color.GRAY));


            //}
        }
        //   if(((AbstractButton)mouseEvent.getSource()).isEnabled() && (!(mouseEvent.getSource() instanceof ColorButton)) ) ((AbstractButton)mouseEvent.getSource()).setOpaque(true);
    }

    public void mouseExited(MouseEvent mouseEvent) {

        //((AbstractButton)mouseEvent.getSource()).setBorder(null); //

        if (doFrame) {
            ((AbstractButton) mouseEvent.getSource()).setBorder(offBorder);
            if (((AbstractButton) mouseEvent.getSource()).getWidth() < 25 && ((AbstractButton) mouseEvent.getSource()).getHeight() < 25) {
                //((AbstractButton)mouseEvent.getSource()).setBorderPainted(false);
                ((AbstractButton) mouseEvent.getSource()).setIcon(offIcons.get((AbstractButton) mouseEvent.getSource()));
            }

            //((AbstractButton) mouseEvent.getSource()).setBackground(null);
            ((AbstractButton) mouseEvent.getSource()).setForeground(Color.BLACK);

        }
        //if((!(mouseEvent.getSource() instanceof ColorButton)))((AbstractButton)mouseEvent.getSource()).setOpaque(false);

    }

    public void mousePressed(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public ImageIcon createLightenedIcon(Icon ic) {
        if (ic == null) {
            return null;
        }

        BufferedImage bim = new BufferedImage(ic.getIconWidth(), ic.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics gr = bim.getGraphics();
        gr.setColor(new Color(150, 150, 250, 120));

        gr.drawImage(((ImageIcon) ic).getImage(), 0, 0, null);
        gr.fillRect(0, 0, bim.getWidth(), bim.getHeight());

        return new ImageIcon(bim);

    }
}
