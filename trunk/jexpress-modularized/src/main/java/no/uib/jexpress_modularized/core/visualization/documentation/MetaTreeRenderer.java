/*
 * MetaTreeRenderer.java
 *
 * Created on 28. desember 2004, 20:22
 */
package no.uib.jexpress_modularized.core.visualization.documentation;

import javax.swing.tree.*;
import java.awt.Color;
import java.io.Serializable;

public class MetaTreeRenderer extends DefaultTreeCellRenderer implements Serializable{

    public MetaTreeRenderer() {
        setOpaque(true);
    }

    //public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof MetaInfoNode) {

            setBackground(Color.white);
            if (sel) {
                setBackground(new Color(220, 220, 250));
            }


            setText(value.toString());
            //setIcon( ((MetaInfoNode)value).getIcon());
            setLeafIcon(((MetaInfoNode) value).getIcon());
            setOpenIcon(((MetaInfoNode) value).getIcon());
            setClosedIcon(((MetaInfoNode) value).getIcon());

            setIcon(((MetaInfoNode) value).getIcon());

        }

        return this;
    }
}
