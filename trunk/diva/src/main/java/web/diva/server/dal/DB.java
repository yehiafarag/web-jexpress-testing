/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.dal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import web.diva.server.model.JexpressUtil;
import web.diva.server.model.beans.DivaDataset;
import web.diva.shared.beans.PCAResults;
import web.diva.shared.beans.RankResult;
import web.diva.shared.beans.SomClusteringResults;

/**
 *
 * @author Yehia Farag
 */
public class DB {

    private final Util databaseUtil = new Util();
    private Map<Integer, String> datasetsNameMap;
    private String path;
    private final JexpressUtil util = new JexpressUtil();

   public void initDivaDatasets(String fileFolderPath){
       path = fileFolderPath;
         File appFolder = new File(path);
        for (File datasetFile : appFolder.listFiles()) {
            if (datasetFile.getName().endsWith(".txt")) {
                DivaDataset divaDs = this.getDataset(0,datasetFile.getName());
                this.setDataset(divaDs, 0);

            }
        }
   
   }

    public TreeMap<Integer, String> getAvailableDatasets(String fileFolderPath) {
        path = fileFolderPath;
        datasetsNameMap = databaseUtil.getDatasetsNameMap(fileFolderPath);
        TreeMap<Integer, String> datasetsTitleMap = new TreeMap<Integer, String>();
        for (int key : datasetsNameMap.keySet()) {
            String name = datasetsNameMap.get(key);
            name = name.substring(0, (name.length() - 4));
            datasetsTitleMap.put(key, name);
        }
        return datasetsTitleMap;
    }
     public Set<String> getAvailableComputingFileList(String fileFolderPath) {
       
        Set<String> computingFileList = databaseUtil.getAvailableComputingFileList(fileFolderPath);        
        return computingFileList;
    }

//    public Dataset getDataset(int datasetId) {
//        Dataset jDataset = databaseUtil.getDataset(datasetsNameMap.get(datasetId), path);
//        Dataset newDS = new Dataset(jDataset.getData(), jDataset.getRowIds(), jDataset.getColumnIds());
//        newDS.setColumnIds(jDataset.getColumnIds());
//        newDS.setMissingMeasurements(jDataset.getMissingMeasurements());
//        newDS.setRowIds(jDataset.getRowIds());
//        newDS.getColumnGroups().clear();
//        newDS.addRowAnnotationNameInUse(jDataset.getInfoHeaders()[0]);
//        newDS.getColumnGroups().addAll(jDataset.getColumnGroups());
//        newDS.getRowGroups().clear();
//        newDS.getRowGroups().addAll(jDataset.getRowGroups());
//        newDS.setName(jDataset.getName());
//        return newDS;
//    }
    private DivaDataset getDataset(int datasetId,String datasetName) {
        if(datasetName == null)
            datasetName = datasetsNameMap.get(datasetId);
        Dataset jDataset = databaseUtil.getJexpressDataset(datasetName, path);
        DivaDataset newDS = new DivaDataset(jDataset.getData(), jDataset.getRowIds(), jDataset.getColumnIds());
        newDS.setColumnIds(jDataset.getColumnIds());
        newDS.setMissingMeasurements(jDataset.getMissingMeasurements());
        newDS.setRowIds(jDataset.getRowIds());
        newDS.getColumnGroups().clear();
        newDS.addRowAnnotationNameInUse(jDataset.getInfoHeaders()[0]);
        newDS.getColumnGroups().addAll(jDataset.getColumnGroups());
        newDS.getRowGroups().clear();
        newDS.getRowGroups().addAll(jDataset.getRowGroups());
        newDS.setName(jDataset.getName());
        newDS = util.initDivaDs(newDS, datasetId);
         String[] geneNamesArr = util.initGeneNamesArr(newDS.getGeneIndexNameMap());
        newDS.setGeneNamesArr(geneNamesArr);
        
        
        return newDS;
    }
    
    public DivaDataset getDivaDataset(int datasetId){
    DivaDataset divaDS =  databaseUtil.getDivaDataset(datasetsNameMap.get(datasetId), path);
    divaDS.setId(datasetId);
    return divaDS;
    }

    public void setDataset(DivaDataset ds, int id) {
        try {
            File dbFile = new File(path, ds.getName() + ".ser");
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            }
            else
                return;
            OutputStream file = new FileOutputStream(dbFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(ds);
            output.flush();
            output.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        if(datasetsNameMap != null)
            datasetsNameMap.put(id, ds.getName() + ".ser");

    }
    
    public void saveSomClustResult(String id, SomClusteringResults results){
    
         try {
            File dbFile = new File(path+"/computing", id);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            }
            else
                return;
            OutputStream file = new FileOutputStream(dbFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(results);
            output.flush();
            output.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    
    }
      public SomClusteringResults getSomClustResult(String id){
         SomClusteringResults results = databaseUtil.deSerializeSomClustResult(id, path+"/computing");
         return results;
     }
      
       public void savePCAResult(String id, PCAResults results){
    
         try {
            File dbFile = new File(path+"/computing", id);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            }
            else
                return;
            OutputStream file = new FileOutputStream(dbFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(results);
            output.flush();
            output.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    
    }
      public PCAResults getPCAResult(String id){
         PCAResults results = databaseUtil.deSerializePCAResult(id, path+"/computing");
         return results;
     }
      
      public void saveRankResult(String id, RankResult results){
    
         try {
            File dbFile = new File(path+"/computing", id);
            if (!dbFile.exists()) {
                dbFile.createNewFile();
            }
            else
                return;
            OutputStream file = new FileOutputStream(dbFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(results);
            output.flush();
            output.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    
    }
      public RankResult getRankResult(String id){
         RankResult results = databaseUtil.deSerializeRankResult(id, path+"/computing");
         return results;
     }
}
