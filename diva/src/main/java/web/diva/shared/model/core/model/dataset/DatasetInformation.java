/*
 *this class will represent the dataset details on client side
 */
package web.diva.shared.model.core.model.dataset;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.LinkedHashMap;

/**
 *
 * @author Yehia Farag
 */
public class DatasetInformation implements IsSerializable{
    private int rowsNumb;
    private int colNumb;
    private int rowGroupsNumb;
    private int colGroupsNumb;
    private int id;
    private String datasetInfo;
    private String[][] rowGroupsNames;
    private String[][] omicsTabelData;

    private LinkedHashMap<String, String> colNamesMap;
   
    public String[][] getOmicsTabelData() {
        return omicsTabelData;
    }

    public void setOmicsTabelData(String[][] geneTabelData) {
        this.omicsTabelData = geneTabelData;
    }

    public int getRowsNumb() {
        return rowsNumb;
    }

    public void setRowsNumb(int rowsNumb) {
        this.rowsNumb = rowsNumb;
    }

    public int getColNumb() {
        return colNumb;
    }

    public void setColNumb(int colNumb) {
        this.colNumb = colNumb;
    }

    public int getRowGroupsNumb() {
        return rowGroupsNumb;
    }

    public void setRowGroupsNumb(int rowGroupsNumb) {
        this.rowGroupsNumb = rowGroupsNumb;
    }

    public int getColGroupsNumb() {
        return colGroupsNumb;
    }

    public void setColGroupsNumb(int colGroupsNumb) {
        this.colGroupsNumb = colGroupsNumb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatasetInfo() {
        return datasetInfo;
    }

    public void setDatasetInfo(String datasetInfo) {
        this.datasetInfo = datasetInfo;
    }

    public String[][] getRowGroupsNames() {
        return rowGroupsNames;
    }

    public void setRowGroupsNames(String[][] rowGroupsNames) {
        this.rowGroupsNames = rowGroupsNames;
    }


    public LinkedHashMap<String, String> getColNamesMap() {
        return colNamesMap;
    }

    public void setColNamesMap(LinkedHashMap<String, String> colNamesMap) {
        this.colNamesMap = colNamesMap;
    }
}
