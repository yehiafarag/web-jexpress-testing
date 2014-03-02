/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.core.dataset;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import no.uib.jexpress_modularized.core.model.Selection;

/**
 *
 * @author pawels
 */
public class DatasetUtils implements Serializable{


    /**
     * Extract a sub-dataset from a Dataset based on a Selection
     * @param ds - Dataset to extract from
     * @param s - selection of data to extarct
     * @return sub-dataset
     */
    public static Dataset getSubDatasetFromSelection(Dataset ds, Selection s) {

        int selectionSize = s.getMembers().length;
        double[][] newMeasurements,
                    oldMeasurements = ds.getData();
        boolean[][] newMissingMeasurements, 
                    oldMissingMeasurements = ds.getMissingMeasurements();
        String[] ids = new String[selectionSize];

        if (Selection.TYPE.OF_COLUMNS.equals(s.getType())) {

            newMeasurements = new double[ds.getDataLength()][selectionSize];
            newMissingMeasurements = new boolean[ds.getDataLength()][selectionSize];

            for (int i=0; i<ds.getDataLength(); i++) {
                
                int colIndex=0;
                for (int j=0; j<selectionSize; j++) {
                    
                    while (!s.hasMember(colIndex)) {
                        colIndex++;
                    }

                    if (colIndex < ds.getDataWidth()) {
                        newMeasurements[i][j] = oldMeasurements[i][colIndex];
                        newMissingMeasurements[i][j] = oldMissingMeasurements[i][colIndex];

                        if (i == 0) {
                            ids[j] = ds.getColumnIds()[colIndex];
                        }
                    } else {
                        throw new IllegalArgumentException("Selection/Group contains column index out of bounds of the dataset: "+colIndex);
                    }
                    colIndex++;
                }
            }

        } else {

            newMeasurements = new double[selectionSize][ds.getDataWidth()];
            newMissingMeasurements = new boolean[selectionSize][ds.getDataWidth()];

            int rowIndex=0;
            for (int i=0; i<selectionSize; i++) {
                
                while (!s.hasMember(rowIndex)) {
                    rowIndex++;
                }
                
                if (rowIndex < ds.getDataLength()) {
                    for (int j=0; j<ds.getDataWidth(); j++) {
                        newMeasurements[i][j] = oldMeasurements[rowIndex][j];
                        newMissingMeasurements[i][j] = oldMissingMeasurements[rowIndex][j];
                    }
                    
                    ids[i] = ds.getRowIds()[rowIndex];
                } else {
                    throw new IllegalArgumentException("Selection/Group contains row index out of bounds of the dataset: "+rowIndex);
                }
                rowIndex++;
            }            
        }

        Dataset newDataset = Dataset.newDataSet(newMeasurements, newMissingMeasurements);
        
        if (Selection.TYPE.OF_COLUMNS.equals(s.getType())) {
            newDataset.setColumnIds(ids);
            newDataset.setRowIds(Arrays.copyOf(ds.getRowIds(), ds.getDataLength()));
        } else {
            newDataset.setColumnIds(Arrays.copyOf(ds.getColumnIds(),ds.getDataWidth()));
            newDataset.setRowIds(ids);
        }
        return newDataset;
    }

    /**
     * Extract a Group of a Dataset and create a new sub-dataset of it
     * @param ds - Dataset to extract from
     * @param g - group to extacr
     * @return - subdataset
     */
    public static Dataset getSubDatasetFromGroup(Dataset ds, Group g) {
        return getSubDatasetFromSelection(ds,(Selection)g);
    }


    /**
     * Prints the dataset as a tab separated text file.
     *
     * @param outputFile the file to send the output to
     */
    public static void printDataSetToFile(Dataset ds, File outputFile) {

        try {

            FileWriter f = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(f);

            for (int i = 0; i < ds.getDataWidth(); i++) {
                bw.write("\t" + ds.getColumnIds()[i]);
            }
            bw.newLine();

            for (int j = 0; j < ds.getDataLength(); j++) {

                bw.write(ds.getRowIds()[j]);

                for (int i = 0; i < ds.getDataWidth(); i++) {
                    bw.write("\t" + ds.getData()[j][i]);
                }
                bw.newLine();
            }

            bw.close();
            f.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
