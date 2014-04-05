/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.dal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.PCAResults;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResults;

/**
 *
 * @author Yehia Farag
 */
public class Util {

    private final DatasetFileReader reader = new DatasetFileReader();

    public Map<Integer, String> getDatasetsNameMap(String path) {
        Map<Integer, String> datasetsMap = new HashMap<Integer, String>();
        File appFolder = new File(path);
        int index = 1;
        for (File datasetFile : appFolder.listFiles()) {
            if (datasetFile.getName().endsWith(".ser")) {
                datasetsMap.put(index, datasetFile.getName());
                index++;

            }
        }
        return datasetsMap;

    }
     public Set<String> getAvailableComputingFileList(String fileFolderPath) {
        Set<String> computingFileList = new HashSet<String>();
        File appFolder = new File(fileFolderPath+"/computing");
        for (File datasetFile : appFolder.listFiles()) {
            if (datasetFile.getName().endsWith(".ser")) {
                computingFileList.add(datasetFile.getName());

            }
        }
        return computingFileList;
    }

    public Dataset getJexpressDataset(String name, String path) {
        File appFolder = new File(path);
        Dataset ds = null;
        for (File f2 : appFolder.listFiles()) {
            if (f2.getName().equalsIgnoreCase(name)) {
                if (name.endsWith(".txt")) {
                    ds = reader.readDatasetFile(f2);
                }
            }
        }
         System.gc();
        return ds;

    }
      public DivaDataset getDivaDataset(String name, String path) {
        File appFolder = new File(path);
        DivaDataset ds = null;
        for (File f2 : appFolder.listFiles()) {
            if (f2.getName().equalsIgnoreCase(name)) {
                    ds = deSerializeDataset(name, path);
            }
        }
         System.gc();
        return ds;

    }


    private DivaDataset deSerializeDataset(String name, String path) {

        try {
            File dbFile = new File(path, name);
            if (!dbFile.exists()) {
                System.out.println("cant find the file");
                return null;
            }
            InputStream file = new FileInputStream(dbFile);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //deserialize the List
            DivaDataset serDataset = (DivaDataset) input.readObject();
             System.gc();
            return serDataset;

        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
    public SomClusteringResults deSerializeSomClustResult(String name, String path) {

        try {
            File dbFile = new File(path, name);
            if (!dbFile.exists()) {
                System.out.println("cant find the file");
                return null;
            }
            InputStream file = new FileInputStream(dbFile);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //deserialize the List
            SomClusteringResults serResult = (SomClusteringResults) input.readObject();
             System.gc();
            return serResult;

        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
    
     public PCAResults deSerializePCAResult(String name, String path) {

        try {
            File dbFile = new File(path, name);
            if (!dbFile.exists()) {
                System.out.println("cant find the file");
                return null;
            }
            InputStream file = new FileInputStream(dbFile);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //deserialize the List
            PCAResults serResult = (PCAResults) input.readObject();
             System.gc();
            return serResult;

        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
    
      public RankResult deSerializeRankResult(String name, String path) {

        try {
            File dbFile = new File(path, name);
            if (!dbFile.exists()) {
                System.out.println("cant find the file");
                return null;
            }
            InputStream file = new FileInputStream(dbFile);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            //deserialize the List
            RankResult serResult = (RankResult) input.readObject();
             System.gc();
            return serResult;

        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
    

}
