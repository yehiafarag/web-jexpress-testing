/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.diva.server.dal;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import no.uib.jexpress_modularized.core.dataset.Dataset;

/**
 *
 * @author Yehia Farag
 */
public class Util {
    private final DatasetFileReader reader = new DatasetFileReader();
    public Map<Integer,Dataset> readDatasetsFiles(String path)
    {
        Map<Integer,Dataset> datasetsMap = new HashMap<Integer,Dataset>();
        File appFolder = new File(path);
        System.out.println(appFolder.isDirectory());
        int index=1;
        for (File f2 : appFolder.listFiles()) {
            if(f2.getName().endsWith(".txt")){
                Dataset ds = reader.readDatasetFile(f2);
                datasetsMap.put(index,ds);    
                index++;
            
            }
        }        
        return datasetsMap; 
    
    }
    
}
