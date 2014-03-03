/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.diva.server.dal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Dataset;

/**
 *
 * @author Yehia Farag
 */
public class DB {
    private final Util databaseUtil = new Util();
    private  Map<Integer,String> datasetsNameMap;
    private String path;
    
    public void loadDatasets(String fileFolderPath)
    {
        path = fileFolderPath;
        datasetsNameMap= databaseUtil.getDatasetsNameMap(fileFolderPath); 

    
    }
    public  TreeMap<Integer, String> getAvailableDatasets()
    {
        TreeMap<Integer, String> datasetsTitleMap = new TreeMap<Integer, String>();
        for(int key:datasetsNameMap.keySet()){
            String name = datasetsNameMap.get(key);
            name = name.substring(0,(name.length()-4));
            datasetsTitleMap.put(key, name);
        }
        return datasetsTitleMap;
    }
    
    public Dataset getDataset(int datasetId){
        Dataset jDataset = databaseUtil.getDataset(datasetsNameMap.get(datasetId), path);
        Dataset newDS = new Dataset(jDataset.getData(),jDataset.getRowIds(),jDataset.getColumnIds());
        newDS.setColumnIds(jDataset.getColumnIds());
        newDS.setMissingMeasurements(jDataset.getMissingMeasurements());
        newDS.setRowIds(jDataset.getRowIds());
        newDS.getColumnGroups().clear();
        newDS.addRowAnnotationNameInUse(jDataset.getInfoHeaders()[0]);
        newDS.getColumnGroups().addAll(jDataset.getColumnGroups());
        newDS.getRowGroups().clear();
        newDS.getRowGroups().addAll(jDataset.getRowGroups());  
        newDS.setName(jDataset.getName());
        return newDS;
    }
    public void setDataset(Dataset ds,int id){
         try { 
            File dbFile = new File(path,ds.getName()+".ser");
            if(!dbFile.exists())
                dbFile.createNewFile();
            OutputStream file = new FileOutputStream(dbFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(ds);
            output.flush();
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());//.log(Level.SEVERE, "Cannot perform output.", ex);
        }
         datasetsNameMap.put(id,ds.getName()+".ser");

    
    }
    
//    private void storeList(){
//      TreeMap<Integer,Dataset> serDatasetsMap = new TreeMap<Integer,Dataset>();
//      serDatasetsMap.putAll(DATASET_MAP);
//        try { 
//            File dbFile = new File(path,"db1.ser");
//            if(!dbFile.exists())
//                dbFile.createNewFile();
//            OutputStream file = new FileOutputStream(dbFile);
//            OutputStream buffer = new BufferedOutputStream(file);
//            ObjectOutput output = new ObjectOutputStream(buffer);
//            output.writeObject(serDatasetsMap);
//            output.flush();
//            output.close();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            System.err.println(ex.getMessage());//.log(Level.SEVERE, "Cannot perform output.", ex);
//        }
//
//    }

//    private TreeMap<Integer, Dataset> getSavedList() {
//        try {
//            File dbFile = new File(path, "db1.ser");
//            if (!dbFile.exists()) {
//                System.out.println("cant find the file");
//                return null;
//            }
//            System.out.println("befor error---->>> ");
//            InputStream file = new FileInputStream(dbFile);
//            InputStream buffer = new BufferedInputStream(file);
//            ObjectInput input = new ObjectInputStream(buffer);
//            System.out.println("after fileinput ---->>> ");
//            
//            //deserialize the List
//            TreeMap<Integer, Dataset> serDatasetsMap = (TreeMap<Integer, Dataset>) input.readObject();
//             System.out.println("after error---->>> "+serDatasetsMap==null);
//            
//             return serDatasetsMap;
//
//        } catch (ClassNotFoundException ex) {
//            System.err.println(ex.getMessage());
//        } catch (Exception ex) {
//             System.err.println(ex.getMessage());
//        }
//        return null;
//    }
//    
//     private Object getSavedList(int x) {
//        try {
//            File dbFile = new File(path+ "\\db.ser");
//            System.out.println(path+"db.ser");
//            if (!dbFile.exists()) {
//                System.out.println("cant find the file");
//                return null;
//            } 
//            
//                 
//            
//            System.out.println("befor error---->>> ");
//            InputStream fileinput = new FileInputStream(path+ "/db.ser");
//            InputStream bufferInput = new BufferedInputStream(fileinput);
//            ObjectInput input = new ObjectInputStream(bufferInput);
//            System.out.println("after fileinput ---->>> ");
//
//            
//            //deserialize the List
//           Object serDatasetsMap = input.readObject();
//           input.close();
//             System.out.println("after error---->>> "+serDatasetsMap==null);
//            return serDatasetsMap;
//
//        }  catch (Exception ex) {
//             ex.printStackTrace();
//        }
//        return null;
//
//    }

}
