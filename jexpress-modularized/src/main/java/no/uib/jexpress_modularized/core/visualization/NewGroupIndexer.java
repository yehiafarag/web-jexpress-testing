/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.core.visualization;

//import expresscomponents.Group;
import java.awt.Color;
import java.io.Serializable;
import java.util.List;

import java.util.ArrayList;
import no.uib.jexpress_modularized.core.dataset.Group;

/**
 *
 * @author bjarte
 */
public class NewGroupIndexer implements Serializable{

    private ArrayList<Group> m_groups;
    private boolean m_includeAll = false;

    public NewGroupIndexer(List<Group> groups) {
        m_groups = (ArrayList<Group>) groups;
    }

    public void fillRowColors(List<Color> buffer, int row) {
        buffer.clear();

        ArrayList<Group> groups = m_groups;
        for (Group gr : groups) {
            if (!gr.isActive()) {
                continue;
            }
            int[] mem = gr.getMembers();
            if (gr.hasMember(row)) {
                buffer.add(gr.getColor());
            }
        }
        //if(!m_includeAll && buffer.size()>0) buffer.remove(buffer.size()-1);
    }

    public void setIncludeAllGroup(boolean includeAll) {
        m_includeAll = includeAll;
    }
}
