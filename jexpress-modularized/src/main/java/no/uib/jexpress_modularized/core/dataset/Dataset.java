package no.uib.jexpress_modularized.core.dataset;

import no.uib.jexpress_modularized.core.model.Selection;
import java.awt.Color;
import java.io.Serializable;
import java.util.*;

/**
 *
 * A Dataset is a central object in the model. It contains the data/measurement
 * matrix, that is used by the computational and visualisation methods. The
 * matrix is stored as a two-dimensionsal array of doubles (
 *
 * @DataSet#measurements), together with a two-dimensionsal array of booleans
 * holding information about missing measurements (
 * @DataSet#missingMeasurements).
 *
 * The Dataset contains also two one-dimensional arrays of Strings with column
 * and row identifiers. The identifiers are used to link-in annotations via
 * @AnnotationManager. Order of the two identifier arrays should be kept in
 * synch with order of the data in measurements and missingMeasurements arrays
 * (in case of any row/column reshuffling). Since
 * @AnnotationManager holds annotations for all Datasets in the system, some of
 * them can be valid only for a subset of datasets. Thus every dataset keeps a
 * list of annotation names that are "in-use" (valid) for the dataset (i.e.
 * columnAnnotationNamesInUse and rowAnnotationNamesInUse).
 *
 * Selections are subsets of column/row indices. Two types of selections are
 * supported:
 * @Selection and
 * @Group. Selections are temporary and handled by
 * @SelectionManager. Groups are permanent, have a color attribute, and are
 * persisted together as part of the Dataset object (list of column/row
 * @Groups are attributes of the Dataset class).
 *
 *
 * @author pawels
 * @author Yehia Farag
 */
public class Dataset implements Serializable {

    //TODO: find the right place to put this
    public static final String NaNInDataMessage = "This dataset contains NaN or Inf values which can give strange results.\nMissing values can be replaced by Missing Value Imputation (Raw Data Menu).\nContinue Anyway?";
    private String name;
    private double[][] measurements;
    private boolean[][] missingMeasurements;
    private String[] columnIds;
    private String[] rowIds;
    private Set<String> columnAnnotationNamesInUse = null;
    private Set<String> rowAnnotationNamesInUse = null;
    private String[] infoHeaders;

    private Dataset() {
    }

    public static Dataset newDataSet(double[][] measurements, boolean[][] missingMeasurements) {

        Dataset dataset = new Dataset();

        if (measurements == null || measurements.length < 1 || measurements[0] == null) {
            throw new IllegalArgumentException("Measurements matrix was null");
        }
        dataset.measurements = measurements;

        if (missingMeasurements == null || missingMeasurements.length < 1
                || missingMeasurements[0] == null) {
            throw new IllegalArgumentException("MissingMeasurements matrix was null");
        } else if (missingMeasurements.length != measurements.length
                || missingMeasurements[0].length != measurements[0].length) {
            throw new IllegalArgumentException("Measurement and MissingMeasurements matrices differ in size");
        }
        dataset.missingMeasurements = missingMeasurements;

        dataset.columnIds = new String[measurements[0].length];
        dataset.rowIds = new String[measurements.length];
        for (int i = 0; i < dataset.columnIds.length; i++) {
            dataset.columnIds[i] = "c" + i;
        }
        for (int i = 0; i < dataset.rowIds.length; i++) {
            dataset.rowIds[i] = "r" + i;
        }
        dataset.infos = new String[dataset.rowIds.length][1];
        for (int i = 0; i < dataset.rowIds.length; i++) {
            dataset.infos[i][0] = dataset.rowIds[i];


        }


        return dataset;
    }

    public String getName() {
        return name;
    }

    //TODO: should it be possible to change the name?
    public void setName(String name) {
        this.name = name;
    }

    public double[][] getData() {
        return measurements;
    }

    public boolean[][] getMissingMeasurements() {
        return missingMeasurements;
    }

    public void setMissingMeasurements(boolean[][] missingMeasurements) {
        this.missingMeasurements = missingMeasurements;
    }

    public String[] getColumnIds() {
        return columnIds;
    }

    public String[] getRowIds() {
        return rowIds;
    }

    public void setColumnIds(String[] ids) {
        if (ids.length != getDataWidth()) {
            throw new IllegalArgumentException("Array of column identifiers has incorrect lenght");
        }
        columnIds = ids;
    }

    public void setRowIds(String[] ids) {
        if (ids.length != getDataLength()) {
            throw new IllegalArgumentException("Array of row identifiers has incorrect lenght");
        }
        rowIds = ids;
    }

    /*
     *
     * Operations retrieving data properties
     *
     */
    public int getDataWidth() {
        return measurements[0].length;
    }

    public int getDataLength() {
        return measurements.length;
    }

    /**
     * Get the absolute minimum value of this dataset or if it has a parent,
     * return the root objects minimum value.
     *
     * @return The minimum value.
     */
    public double getMinMeasurement() {
        /*
         * @TODO if parent is added to a dataset, uncomment this double[][] data
         * = null; DataSet first = this; if (first.maxminFromParent) { while
         * (first.getParent() != null && ((DataSet) first.getParent()).getData()
         * != null && ((DataSet) first.getParent()).getData().length > 0) {
         * first = (DataSet) first.getParent(); } }
         *
         * data = first.getData();
         */
        double min = Double.MAX_VALUE;
        for (int i = 0; i < measurements.length; i++) {
            for (int j = 0; j < measurements[0].length; j++) {
                if (min > measurements[i][j]) {
                    min = measurements[i][j];
                }
            }
        }
        return min;
    }

    /**
     * Get the absolute maximum value of this dataset or if it has a parent,
     * return the root objects maximum value.
     *
     * @return The maximum value.
     */
    public double getMaxMeasurement() {
        /*
         * @TODO if parent is added to a dataset, uncomment this double[][] data
         * = null; DataSet first = this; //if(first.maxminFromParent)
         * while(first.parentData!=null && first.parentData.getData()!=null &&
         * first.parentData.getMax()!=-9999999.0)first=first.parentData; if
         * (first.maxminFromParent) { while (first.getParent() != null &&
         * ((DataSet) first.getParent()).getData() != null && ((DataSet)
         * first.getParent()).getData().length > 0) { first = (DataSet)
         * first.getParent(); } } data = first.getData();
         */
        double max = Double.MIN_VALUE;
        for (int i = 0; i < measurements.length; i++) {
            for (int j = 0; j < measurements[0].length; j++) {
                if (max < measurements[i][j]) {
                    max = measurements[i][j];
                }
            }
        }
        return max;
    }

    /**
     * This returns true if any of the data values in this dataset is NaN
     *
     * @return true if any of the data values in this dataset is NaN
     */
    public boolean hasNaN() {
        int n = getDataLength();
        int m = getDataWidth();

        double[][] dat = getData();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (Double.isNaN(dat[i][j]) || Double.isInfinite(dat[i][j])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMissing(int xCoord, int yCoord) {
        /*
         * if(linked) { return ((DataSet) parent).isMissing(i, j); }
         */
        return missingMeasurements[xCoord][yCoord]
                || Double.isNaN(measurements[xCoord][yCoord])
                || Double.isInfinite(measurements[xCoord][yCoord]);
    }
    /*
     *
     * Group handling (could be managed outside of the @DataSet class, if it gets
     * to crowded)
     *
     */
    private List<Group> columnGroups = new ArrayList<Group>();
    private List<Group> rowGroups = new ArrayList<Group>();

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

    /*
     * Annotations handling
     */
    /**
     * Return list of column annotation names that are used for this dataset. If
     * null list is returned, all annotations registered in
     *
     * @AnnotationManager should be considered "in use".
     * @return list of column annotations used for this dataset, or null if all
     * are used
     */
    public Set<String> getColumnAnnotationNamesInUse() {
        return columnAnnotationNamesInUse;
    }

    /**
     * Return list of row annotation names that are used for this dataset. If
     * null list is returned, all annotations registered in
     *
     * @AnnotationManager should be considered "in use".
     * @return list of row annotations used for this dataset, or null if all are
     * used
     */
    public Set<String> getRowAnnotationNamesInUse() {
        return rowAnnotationNamesInUse;
    }

    /**
     * Add a column annotation to be used with the dataset. The annotation has
     * to be managed by
     *
     * @AnnotationManager
     * @param columnAnnotationName - a column annotation name to add to used
     * annotations
     */
    public void addColumnAnnotationNameInUse(String columnAnnotationName) {

        if (!AnnotationManager.getAnnotationManager().getManagedColumnAnnotationNames().contains(columnAnnotationName)) {
            throw new IllegalArgumentException("Trying to add annotation not managed by AnnotationManager: " + columnAnnotationName);
        }
        if (columnAnnotationNamesInUse == null) {
            columnAnnotationNamesInUse = new HashSet<String>();
        }
        columnAnnotationNamesInUse.add(columnAnnotationName);
    }

    /**
     * Add a row annotation to be used with the dataset. The annotation has to
     * be managed by
     *
     * @AnnotationManager
     * @param rowAnnotationName - a row annotation name to add to used
     * annotations
     */
    public void addRowAnnotationNameInUse(String rowAnnotationName) {
//        if (!AnnotationManager.getAnnotationManager().getManagedRowAnnotationNames().contains(rowAnnotationName)) {
//            throw new IllegalArgumentException("Trying to add annotation not managed by AnnotationManager: " + rowAnnotationName);
//        }
        if (rowAnnotationNamesInUse == null) {
            rowAnnotationNamesInUse = new HashSet<String>();
        }
        rowAnnotationNamesInUse.add(rowAnnotationName);
    }
    /*
     * Operations below are here only for documentation purposes
     */

    /**
     * Infos were tightly connected to a dataset, annotations are global,
     * meaning that several datasets can share annotations (one avoids quite
     * costly duplication of large String arrays). In Bjarte's J-Express infos
     * and colinfos (you correctly noticed the difference) are 2-dimensional
     * arrays (tables) of Strings containing annotation values. A column(or
     * row?) in this table is a list of annotations for a column/row in a
     * DataSet (so they have equal lengths). infoHeaders and colinfoHeader are
     * headers of these tables, so names of the annotations. Now, in modularized
     * J-Express, the annotations are kept by a singleton object -
     * #AnnotationManager (similarily to selections). The AnnotationManager
     * stores a number of named AnnotationLibraries (=columns from the infos
     * tables). The name of a library is a name of the annotation the library
     * represents (or one String from info header in previous nomenclature). An
     * AnnotationLibrary contains a set of key-value pairs: the key is a
     * row/column identifier in a dataset, the value is annotation's value for
     * this column/row. Notice that an AnnotationLibrary does not know if it's
     * row or column annotation. Each dataset can use different set of
     * annotations (so AnnotationLibraries). This information is kept as a list
     * of annotation names in a Dataset object
     * (Dataset.get[Row|Column]AnnotationNamesInUse()). So getUsedColInfos() and
     * getColInfoHeaders() are now essentially replaced by
     * dataset.getColumnAnnotationNamesInUse(). The difference in return objects
     * needs to handled properly in the code, so reimplementation of some bits
     * will be required. See for example how copySelectionText() and
     * getTableWidth() are changed in somclust.visualization.MainClust3. If you
     * are forced to use the old data structure for infos (a matrix/table of
     * Strings), look at MainClust3.drawTable(). And when reimplementing,
     * remember that Dataset.get[Row|Column]AnnotationNamesInUse() returns null
     * if all the annotations are in use (no customization has been done).
     *
     * @return UnsupportedOperationException
     */
    public boolean[] getusedInfos() {
        boolean[] usedInfos = new boolean[getInfoHeaders().length + 2];
        for (int i = 0, len = usedInfos.length; i < len; i++) {
            usedInfos[i] = true;
        }
        return usedInfos;
    }

    /**
     * @ Infos are called annotations now. Infos were tightly connected to a
     * dataset, annotations are global, meaning that several datasets can share
     * annotations (one avoids quite costly duplication of large String arrays).
     * In Bjarte's J-Express infos and colinfos (you correctly noticed the
     * difference) are 2-dimensional arrays (tables) of Strings containing
     * annotation values. A column(or row?) in this table is a list of
     * annotations for a column/row in a DataSet (so they have equal lengths).
     * infoHeaders and colinfoHeader are headers of these tables, so names of
     * the annotations. Now, in modularized J-Express, the annotations are kept
     * by a singleton object - AnnotationManager (similarily to selections). The
     * AnnotationManager stores a number of named AnnotationLibraries (=columns
     * from the infos tables). The name of a library is a name of the annotation
     * the library represents (or one String from info header in previous
     * nomenclature). An AnnotationLibrary contains a set of key-value pairs:
     * the key is a row/column identifier in a dataset, the value is
     * annotation's value for this column/row. Notice that an AnnotationLibrary
     * does not know if it's row or column annotation. Each dataset can use
     * different set of annotations (so AnnotationLibraries). This information
     * is kept as a list of annotation names in a Dataset object
     * (Dataset.get[Row|Column]AnnotationNamesInUse()). So getUsedColInfos() and
     * getColInfoHeaders() are now essentially replaced by
     * dataset.getColumnAnnotationNamesInUse(). The difference in return objects
     * needs to handled properly in the code, so reimplementation of some bits
     * will be required. See for example how copySelectionText() and
     * getTableWidth() are changed in somclust.visualization.MainClust3. If you
     * are forced to use the old data structure for infos (a matrix/table of
     * Strings), look at MainClust3.drawTable(). And when reimplementing,
     * remember that Dataset.get[Row|Column]AnnotationNamesInUse() returns null
     * if all the annotations are in use (no customization has been done).
     * @return UnsupportedOperationException
     */
    public boolean[] getusedColInfos() {
        boolean[] usedColInfos = new boolean[getColInfoHeaders().length + 2];
        for (int i = 0, len = usedColInfos.length; i < len; i++) {
            usedColInfos[i] = true;
        }
        return usedColInfos;

    }

    /**
     * @ Infos are called annotations now. Infos were tightly connected to a
     * dataset, annotations are global, meaning that several datasets can share
     * annotations (one avoids quite costly duplication of large String arrays).
     * In Bjarte's J-Express infos and colinfos (you correctly noticed the
     * difference) are 2-dimensional arrays (tables) of Strings containing
     * annotation values. A column(or row?) in this table is a list of
     * annotations for a column/row in a DataSet (so they have equal lengths).
     * infoHeaders and colinfoHeader are headers of these tables, so names of
     * the annotations. Now, in modularized J-Express, the annotations are kept
     * by a singleton object - AnnotationManager (similarily to selections). The
     * AnnotationManager stores a number of named AnnotationLibraries (=columns
     * from the infos tables). The name of a library is a name of the annotation
     * the library represents (or one String from info header in previous
     * nomenclature). An AnnotationLibrary contains a set of key-value pairs:
     * the key is a row/column identifier in a dataset, the value is
     * annotation's value for this column/row. Notice that an AnnotationLibrary
     * does not know if it's row or column annotation. Each dataset can use
     * different set of annotations (so AnnotationLibraries). This information
     * is kept as a list of annotation names in a Dataset object
     * (Dataset.get[Row|Column]AnnotationNamesInUse()). So getUsedColInfos() and
     * getColInfoHeaders() are now essentially replaced by
     * dataset.getColumnAnnotationNamesInUse(). The difference in return objects
     * needs to handled properly in the code, so reimplementation of some bits
     * will be required. See for example how copySelectionText() and
     * getTableWidth() are changed in somclust.visualization.MainClust3. If you
     * are forced to use the old data structure for infos (a matrix/table of
     * Strings), look at MainClust3.drawTable(). And when reimplementing,
     * remember that Dataset.get[Row|Column]AnnotationNamesInUse() returns null
     * if all the annotations are in use (no customization has been done).
     * @return UnsupportedOperationException
     */
    public String[][] getColInfos() {
        AnnotationManager am = AnnotationManager.getAnnotationManager();
        Set<String> annotations = getColumnAnnotationNamesInUse();
        if (annotations == null) {
            annotations = am.getManagedColumnAnnotationNames();
        }

        String[] columnIds = getColumnIds();
        String[][] all; //column annotation matrix
        if (annotations.isEmpty()) {
            assert getDataWidth() == columnIds.length : "length of column ids and width of dataset should be equal (?)";
            all = new String[getDataWidth()][1];
            for (int i = 0; i < all.length; i++) {
                all[i][0] = columnIds[i];
            }
        } else {
            String[] headers = annotations.toArray(new String[annotations.size()]);
            all = new String[getDataWidth()][annotations.size()];
            for (int i = 0; i < headers.length; i++) {
                AnnotationLibrary anns = am.getColumnAnnotations(headers[i]);
                for (int j = 0; j < all.length; j++) {
                    all[j][i] = anns.getAnnotation(columnIds[j]);
                }
            }
        }
        return all;

    }
    /**
     * @ Infos are called annotations now. Infos were tightly connected to a
     * dataset, annotations are global, meaning that several datasets can share
     * annotations (one avoids quite costly duplication of large String arrays).
     * In Bjarte's J-Express infos and colinfos (you correctly noticed the
     * difference) are 2-dimensional arrays (tables) of Strings containing
     * annotation values. A column(or row?) in this table is a list of
     * annotations for a column/row in a DataSet (so they have equal lengths).
     * infoHeaders and colinfoHeader are headers of these tables, so names of
     * the annotations. Now, in modularized J-Express, the annotations are kept
     * by a singleton object - AnnotationManager (similarily to selections). The
     * AnnotationManager stores a number of named AnnotationLibraries (=columns
     * from the infos tables). The name of a library is a name of the annotation
     * the library represents (or one String from info header in previous
     * nomenclature). An AnnotationLibrary contains a set of key-value pairs:
     * the key is a row/column identifier in a dataset, the value is
     * annotation's value for this column/row. Notice that an AnnotationLibrary
     * does not know if it's row or column annotation. Each dataset can use
     * different set of annotations (so AnnotationLibraries). This information
     * is kept as a list of annotation names in a Dataset object
     * (Dataset.get[Row|Column]AnnotationNamesInUse()). So getUsedColInfos() and
     * getColInfoHeaders() are now essentially replaced by
     * dataset.getColumnAnnotationNamesInUse(). The difference in return objects
     * needs to handled properly in the code, so reimplementation of some bits
     * will be required. See for example how copySelectionText() and
     * getTableWidth() are changed in somclust.visualization.MainClust3. If you
     * are forced to use the old data structure for infos (a matrix/table of
     * Strings), look at MainClust3.drawTable(). And when reimplementing,
     * remember that Dataset.get[Row|Column]AnnotationNamesInUse() returns null
     * if all the annotations are in use (no customization has been done).
     * @return UnsupportedOperationException
     */
    private String[][] infos;
    private boolean[] parentIndexes;

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

    /**
     * @ Infos are called annotations now. Infos were tightly connected to a
     * dataset, annotations are global, meaning that several datasets can share
     * annotations (one avoids quite costly duplication of large String arrays).
     * In Bjarte's J-Express infos and colinfos (you correctly noticed the
     * difference) are 2-dimensional arrays (tables) of Strings containing
     * annotation values. A column(or row?) in this table is a list of
     * annotations for a column/row in a DataSet (so they have equal lengths).
     * infoHeaders and colinfoHeader are headers of these tables, so names of
     * the annotations. Now, in modularized J-Express, the annotations are kept
     * by a singleton object - AnnotationManager (similarily to selections). The
     * AnnotationManager stores a number of named AnnotationLibraries (=columns
     * from the infos tables). The name of a library is a name of the annotation
     * the library represents (or one String from info header in previous
     * nomenclature). An AnnotationLibrary contains a set of key-value pairs:
     * the key is a row/column identifier in a dataset, the value is
     * annotation's value for this column/row. Notice that an AnnotationLibrary
     * does not know if it's row or column annotation. Each dataset can use
     * different set of annotations (so AnnotationLibraries). This information
     * is kept as a list of annotation names in a Dataset object
     * (Dataset.get[Row|Column]AnnotationNamesInUse()). So getUsedColInfos() and
     * getColInfoHeaders() are now essentially replaced by
     * dataset.getColumnAnnotationNamesInUse(). The difference in return objects
     * needs to handled properly in the code, so reimplementation of some bits
     * will be required. See for example how copySelectionText() and
     * getTableWidth() are changed in somclust.visualization.MainClust3. If you
     * are forced to use the old data structure for infos (a matrix/table of
     * Strings), look at MainClust3.drawTable(). And when reimplementing,
     * remember that Dataset.get[Row|Column]AnnotationNamesInUse() returns null
     * if all the annotations are in use (no customization has been done).
     * @return UnsupportedOperationException
     */
    public String[] getColInfoHeaders() {

        AnnotationManager am = AnnotationManager.getAnnotationManager();
        Set<String> columnAnnotations = getColumnAnnotationNamesInUse();
        if (columnAnnotations == null) {
            columnAnnotations = am.getManagedColumnAnnotationNames();
        }
        String[] colHeaders = null;
        if (columnAnnotations.isEmpty()) {
            colHeaders = new String[]{"Column ID"};
        } else {
            colHeaders = columnAnnotations.toArray(new String[columnAnnotations.size()]);
        }
        return colHeaders;

    }

    /**
     * @ Infos are called annotations now. Infos were tightly connected to a
     * dataset, annotations are global, meaning that several datasets can share
     * annotations (one avoids quite costly duplication of large String arrays).
     * In Bjarte's J-Express infos and colinfos (you correctly noticed the
     * difference) are 2-dimensional arrays (tables) of Strings containing
     * annotation values. A column(or row?) in this table is a list of
     * annotations for a column/row in a DataSet (so they have equal lengths).
     * infoHeaders and colinfoHeader are headers of these tables, so names of
     * the annotations. Now, in modularized J-Express, the annotations are kept
     * by a singleton object - AnnotationManager (similarily to selections). The
     * AnnotationManager stores a number of named AnnotationLibraries (=columns
     * from the infos tables). The name of a library is a name of the annotation
     * the library represents (or one String from info header in previous
     * nomenclature). An AnnotationLibrary contains a set of key-value pairs:
     * the key is a row/column identifier in a dataset, the value is
     * annotation's value for this column/row. Notice that an AnnotationLibrary
     * does not know if it's row or column annotation. Each dataset can use
     * different set of annotations (so AnnotationLibraries). This information
     * is kept as a list of annotation names in a Dataset object
     * (Dataset.get[Row|Column]AnnotationNamesInUse()). So getUsedColInfos() and
     * getColInfoHeaders() are now essentially replaced by
     * dataset.getColumnAnnotationNamesInUse(). The difference in return objects
     * needs to handled properly in the code, so reimplementation of some bits
     * will be required. See for example how copySelectionText() and
     * getTableWidth() are changed in somclust.visualization.MainClust3. If you
     * are forced to use the old data structure for infos (a matrix/table of
     * Strings), look at MainClust3.drawTable(). And when reimplementing,
     * remember that Dataset.get[Row|Column]AnnotationNamesInUse() returns null
     * if all the annotations are in use (no customization has been done).
     * @return UnsupportedOperationException
     */
    public String[] getInfoHeaders() {

        AnnotationManager am = AnnotationManager.getAnnotationManager();
        Set<String> rowAnnotations = getRowAnnotationNamesInUse();
        if (rowAnnotations == null) {
            rowAnnotations = am.getManagedColumnAnnotationNames();
        }
        // String[] rowHeaders = null;
        if (rowAnnotations.isEmpty()) {
            infoHeaders = new String[]{"Column ID"};
        } else {
            infoHeaders = rowAnnotations.toArray(new String[rowAnnotations.size()]);
        }
        return infoHeaders;

    }

    /**
     * Creates a new DataSet with an old constructor.. the colnames argument
     * will be reinitialized as a String[][] array
     *
     * @param data The data values as a double[][] array.
     * @param names A sigle column of identifiers.
     * @param colnames An array of the column identifiers.
     */
    public Dataset(double[][] data, String[] names, String[] colnames) {
        this.measurements = data;
        this.columnIds = colnames;
        this.rowIds = names;
        addNullClass();
        infos = new String[names.length][1];
        for (int i = 0; i < names.length; i++) {
            infos[i][0] = names[i];
        }
    }
    private Vector ColorClasses = new Vector();
    private boolean colorVectorHasChanged = true;
    private boolean columncolorVectorHasChanged = true;

    /**
     * Used by the dataLoader to set the indexes of the missing values in the
     * dataset.
     *
     * @param nulls A multidimensional array where each missing value has a true
     * value.
     */
    private void addNullClass() {
        if (ColorClasses == null) {
            ColorClasses = new Vector();
        }
        if (ColorClasses.size() > 0) {
            return;
        }
        colorVectorHasChanged = true;
        int[] allIndexes = new int[getDataLength()];
        for (int i = 0; i < getDataLength(); i++) {
            allIndexes[i] = i;
        }
        Group gr = new Group("ALL", Color.BLACK, new Selection(Selection.TYPE.OF_ROWS, allIndexes));
        gr.setActive(false);
        addRowGroup(gr);
        addNullColumnClass();
    }
    private Vector ColumnColorClasses = new Vector();

    private void addNullColumnClass() {
        if (ColumnColorClasses == null) {
            ColumnColorClasses = new Vector();
        }
        if (ColumnColorClasses.size() > 0) {
            return;
        }
        columncolorVectorHasChanged = true;
        int[] allIndexes = new int[getDataWidth()];
        for (int i = 0; i < getDataWidth(); i++) {
            allIndexes[i] = i;
        }
        addColumnGroup(new Group("ALL", Color.GRAY, new Selection(Selection.TYPE.OF_COLUMNS, allIndexes)));

    }
}
