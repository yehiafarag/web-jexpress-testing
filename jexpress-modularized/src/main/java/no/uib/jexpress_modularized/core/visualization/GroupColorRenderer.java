package no.uib.jexpress_modularized.core.visualization;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class GroupColorRenderer extends DefaultTableCellRenderer implements Serializable {

    public GroupColorRenderer() {
        setOpaque(true);
        setBackground(Color.white);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean iss, boolean chf, int index, int column) {

        if (!(value instanceof List)) {
            System.out.print("\nColor renderer: Not a  list ");
            return this;
        }

        List<Color> colors = (List<Color>) value;

        DefaultTableCellRenderer ren = (DefaultTableCellRenderer) super.getTableCellRendererComponent(table, value, iss, chf, index, column);
        ren.setText("");
        //ren.setIcon(new javax.swing.ImageIcon(getClass().getResource("/expresscomponents/Visuals/im/fileloc.gif")));
        ren.setIcon(this.getIcon(colors, table.getRowHeight()));

        return ren;
    }

    public ImageIcon getIcon(List<Color> colors, int height) {
        int betweenSpace = 2;
        int brickWidth = 3;
        int startGap = 2;

        int imHeight = height;
        int imWidth = startGap + 4 * colors.size() + ((colors.size() - 1) * betweenSpace);

        if (imWidth < 1 || imHeight < 1) {
            return null;
        }

        BufferedImage bim = new BufferedImage(imWidth, imHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics g = bim.getGraphics();
        //g.setColor(Color.GREEN);
        //g.fillRect(2,2,6,6);

        int betSpace = 0;

        for (int i = 0; i < colors.size(); i++) {
            g.setColor((Color) colors.get(i));
            //g.setColor(Color.GREEN);
            int x = (i * brickWidth) + betSpace + startGap;
            int y = 2;
            int w = brickWidth;
            int h = height - 4;
            g.fillRect(x, y, w, h);
            betSpace += betweenSpace;
        }

        ImageIcon ret = new ImageIcon(bim);
        return ret;
    }
}
