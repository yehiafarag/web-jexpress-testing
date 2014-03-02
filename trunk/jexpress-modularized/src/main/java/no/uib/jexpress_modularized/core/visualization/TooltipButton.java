/*
 * TooltipButton.java
 *
 * Created on 15. februar 2008, 22:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JToolTip;

/**
 *
 * @author Bjarte Dysvik
 */
public class TooltipButton extends JComponent implements Serializable{

    private ImageIcon im1, im2;
    private boolean selected = false;
    private JeToolTip2 jtip;

    /**
     * Creates a new instance of TooltipButton
     */
    public TooltipButton() {
        im1 = new javax.swing.ImageIcon(getClass().getResource("/icons/q1.png"));
        im2 = new javax.swing.ImageIcon(getClass().getResource("/icons/q2.png"));

        this.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                selected = true;
                TooltipButton.this.repaint();
            }

            public void mouseExited(MouseEvent e) {
                selected = false;
                TooltipButton.this.repaint();
            }
        });


    }

    public Dimension getPreferredSize() {
        return new Dimension(19, 20);
    }

    public void paint(Graphics g) {
        if (selected) {
            g.drawImage(im2.getImage(), 0, 0, this);
        } else {
            g.drawImage(im1.getImage(), 0, 0, this);
        }
    }

    public void setPreferredSize(Dimension preferredSize) {
    }

    public JToolTip createToolTip() {
        jtip = new JeToolTip2(this);
        jtip.setFont(new Font("Times New Roman", 0, 16));
        return (jtip);
    }

    public void setToolTipText(String text) {
        String[] trimmed = text.split("<D>");


        super.setToolTipText("<b>" + trimmed[0] + "</b><br>" + trimmed[1]);
        //super.setToolTipText(trimmed);
    }

    public void setTip(String header, String text) {
        setToolTipText("<b>" + header + "</b><br>" + text);
    }

    public static void main(String[] args) {

        JFrame f = new JFrame();

        f.setLayout(new FlowLayout());

        TooltipButton tb = new TooltipButton();

        //tb.setTip("Header", "This is the actual help hiiasdf fewqkmnf oqwkef kamlfie alsifmie aklfmei fmlasifm feefeff");
        tb.setToolTipText("Testing<D>Testing to see i the tooltip is ok");
        f.add(tb);

        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);

        f.pack();

        f.setVisible(true);

    }
}
