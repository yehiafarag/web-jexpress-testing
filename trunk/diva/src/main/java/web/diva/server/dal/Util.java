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
import java.util.Map;
import no.uib.jexpress_modularized.core.dataset.Dataset;

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
        for (File f2 : appFolder.listFiles()) {
            if (f2.getName().endsWith(".txt") || f2.getName().endsWith(".ser")) {
                datasetsMap.put(index, f2.getName());
                index++;

            }
        }
        return datasetsMap;

    }

    public Dataset getDataset(String name, String path) {
        File appFolder = new File(path);
        Dataset ds = null;
        for (File f2 : appFolder.listFiles()) {
            if (f2.getName().equalsIgnoreCase(name)) {
                if (name.endsWith(".txt")) {
                    ds = reader.readDatasetFile(f2);
                } else { //deserialise the file
                    ds = deSerializeDataset(name, path);

                }
            }
        }
        return ds;

    }

    private Dataset deSerializeDataset(String name, String path) {

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
            Dataset serDataset = (Dataset) input.readObject();
            return serDataset;

        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

}
