/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package  no.uib.jexpress_modularized.core.dataset;

import java.io.Serializable;
import no.uib.jexpress_modularized.core.visualization.NewGroupIndexer;


/**
 *
 * @author Bjarte
 */
public class AnnotationFilter implements Serializable{

    private Dataset m_data;
    private NewGroupIndexer m_groupIndexer;

    public AnnotationFilter(Dataset data) {
        this.m_data = data;
        m_groupIndexer = new NewGroupIndexer(data.getRowGroups());
    }

    public int getRowAnnotationRows() {
        return m_data.getDataLength();
    }

    public int getRowAnnotationColumns() {
        int ret = 0;
        boolean[] usedInfos = getUseRowWOIndexandGroups();
        for (boolean b : usedInfos) {
            if (b) {
                ret++;
            }
        }
        return ret;
    }

    public int getColumnAnnotationRows() {
        return m_data.getDataWidth();
    }

    public int getColumnAnnotationColumns() {
        int ret = 0;
        boolean[] usedInfos = getUseColumnWOIndexandGroups();
        for (Boolean b : usedInfos) {
            if (b) {
                ret++;
            }
        }
        return ret;
    }

    public String[] getRowAnnotationHeader() {
        String[] ret = new String[getRowAnnotationColumns()];
        boolean[] use = getUseRowWOIndexandGroups();
        String[] h =m_data.getInfoHeaders();

        int cnt = 0;
        for (int i = 0; i < h.length; i++) {
            if (use[i]) {
                ret[cnt] = h[i];
                cnt++;
            }
        }

        return ret;
    }

    public String[] getColumnAnnotationHeader() {
        String[] ret = new String[getColumnAnnotationColumns()];
        boolean[] use = getUseColumnWOIndexandGroups();
        String[] h = m_data.getColInfoHeaders();
        int cnt = 0;
        for (int i = 0; i < h.length; i++) {
            if (use[i]) {
                ret[cnt] = h[i];
                cnt++;
            }
        }
        return ret;
    }

    public String[][] getRowAnnotation() {
        String[][] all = m_data.getInfos();
        String[][] ret = new String[all.length][getRowAnnotationColumns()];
        boolean[] use = getUseRowWOIndexandGroups();
        int cnt = 0;
        for (int j = 0; j < all[0].length; j++) {
            if (use[j]) {
                for (int i = 0; i < all.length; i++) {
                    ret[i][cnt] = all[i][j];
                }
                cnt++;
            }
        }
        return ret;
    }

    public String[][] getColumnAnnotation() {
        String[][] all = m_data.getColInfos();
        String[][] ret = new String[all.length][getColumnAnnotationColumns()];

        boolean[] use = getUseColumnWOIndexandGroups();
        int cnt = 0;
        for (int j = 0; j < all[0].length; j++) {
            if (use[j]) {
                for (int i = 0; i < all.length; i++) {
                    ret[i][cnt] = all[i][j];
                }
                cnt++;
            }
        }
        return ret;
    }

    private boolean[] getUseRowWOIndexandGroups() {
        boolean[] ret = null;
          if (m_data.getInfos()[0].length == m_data.getusedInfos().length - 2) {             
            ret = new boolean[m_data.getusedInfos().length - 2];
            for (int i = 1; i < m_data.getusedInfos().length - 1; i++) {
                ret[i - 1] = m_data.getusedInfos()[i];
            }
        } else {
            ret = m_data.getusedInfos();
        }
        return ret;
    }


    private boolean[] getUseColumnWOIndexandGroups() {

        boolean[] ret = null;
        if (m_data.getColInfos().length ==m_data.getusedColInfos().length - 2) {
            ret = new boolean[m_data.getusedColInfos().length - 2];
            for (int i = 1; i < m_data.getusedColInfos().length - 1; i++) {
                ret[i - 1] = m_data.getusedColInfos()[i];
            }
        } else {
            ret = m_data.getusedColInfos();
        }
        return ret;
    }

    public static boolean showRowIndexes(Dataset m_data) {
        if (m_data.getColInfos()[0].length == m_data.getusedColInfos().length - 2) {
            return m_data.getusedColInfos()[0];
        } else {
            return false;
        }
    }

    public static boolean showColumnIndexes(Dataset m_data) {
        if (m_data.getColInfos()[0].length == m_data.getusedInfos().length - 2) {
            return m_data.getusedInfos()[0];
        } else {
            return false;
        }
    }

    public static boolean showRowGroups(Dataset m_data) {

        if (m_data.getColInfos()[0].length == m_data.getusedColInfos().length - 2) {
            return m_data.getusedColInfos()[m_data.getusedColInfos().length - 1];
        } else {
            return false;
        }
    }

    public static boolean showColumnGroups(Dataset m_data) {
        if (m_data.getColInfos()[0].length == m_data.getusedInfos().length - 2) {
            return m_data.getusedInfos()[m_data.getusedInfos().length - 1];
        } else {
            return false;
        }
    }
}