/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.shared.model.core.model.dataset;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Y.M
 */
public class Dataset implements IsSerializable {

    private int id;
    private String[] columnIds;
    private String[] rowIds;
    private String[] infoHeaders;
    private double[][] measurements;
    private String[][] infos;
    private Map<Integer,String> geneIndexNameMap;
    private Map<Integer,Number[]> memberMaps;
    
    private String[] geneColorArr;
     private String[] geneNamesArr;
     
     private Number[][] lineChartPointArr;
    

   

    public Map<Integer, String> getGeneIndexNameMap() {
        return geneIndexNameMap;
    }

    public void setGeneIndexNameMap(Map<Integer, String> geneIndexNameMap) {
        this.geneIndexNameMap = geneIndexNameMap;
    }

    public Map<String,Integer> getGeneNameIndexMap() {
        return geneNameIndexMap;
    }

    public void setGeneNameIndexMap(Map<String,Integer> geneNameIndexMap) {
        this.geneNameIndexMap = geneNameIndexMap;
    }
    private Map<String,Integer> geneNameIndexMap;
    /*
     *  @gwt.typeArgs <web.jexpress.shared.model.core.model.dataset.Group>
     */
    private List<Group> columnGroups = new ArrayList<Group>();
     /*
     *  @gwt.typeArgs <web.jexpress.shared.model.core.model.dataset.Group>
     */
    private List<Group> rowGroups = new ArrayList<Group>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getColumnIds() {
        return columnIds;
    }

    public void setColumnIds(String[] columnIds) {
        this.columnIds = columnIds;
    }

    public String[] getRowIds() {
        return rowIds;
    }

    public void setRowIds(String[] rowIds) {
        this.rowIds = rowIds;
    }

    public String[] getInfoHeaders() {
        return infoHeaders;
    }

    public void setInfoHeaders(String[] infoHeaders) {
        this.infoHeaders = infoHeaders;
    }

    public double[][] getMeasurements() {
        return measurements;
    }

    public void setMeasurements(double[][] measurements) {
        this.measurements = measurements;
    }

    public String[][] getInfos() {

        String[][] ret = null;
        ret = infos;
        if (ret != null && ret.length != getDataLength()) {
            ret = new String[getDataLength()][1];
            for (int i = 0; i < ret.length; i++) {
                ret[i][0] = "Row" + (i + 1);
            }
            infos = ret;
        }
        return ret;

    }

    public int getDataWidth() {
        return measurements[0].length;
    }

    public int getDataLength() {
        return measurements.length;
    }

    public List<Group> getColumnGroups() {
        return columnGroups;
    }

    public List<Group> getRowGroups() {
        return rowGroups;
    }

    public void addColumnGroup(Group g) {
        if (g == null) {
            throw new IllegalArgumentException("Trying to add null Group");
        }
        columnGroups.add(g);
    }

    public void addRowGroup(Group g) {
        if (g == null) {
            throw new IllegalArgumentException("Trying to add null Group");
        }
        rowGroups.add(g);
    }

    public void removeColumnGroup(Group g) {
        columnGroups.remove(g);
    }

    public void removeRowGroup(Dataset ds, Group g) {
        rowGroups.remove(g);
    }

    public Map<Integer,Number[]> getMemberMaps() {
        return memberMaps;
    }

    public void setMemberMaps(Map<Integer,Number[]> memberMaps) {
        this.memberMaps = memberMaps;
    }

    public String[] getGeneColorArr() {
        return geneColorArr;
    }

    public void setGeneColorArr(String[] geneColorArr) {
        this.geneColorArr = geneColorArr;
    }

    public String[] getGeneNamesArr() {
        return geneNamesArr;
    }

    public void setGeneNamesArr(String[] geneNamesArr) {
        this.geneNamesArr = geneNamesArr;
    }

    public Number[][] getLineChartPointArr() {
        return lineChartPointArr;
    }

    public void setLineChartPointArr(Number[][] lineChartPointArr) {
        this.lineChartPointArr = lineChartPointArr;
    }


   
}
