/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package web.jexpress.server.dal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import utility.FilesReader;

/**
 *
 * @author Yehia Farag
 */
public class DatasetFileReader {
          // File to write to.
    private final FilesReader fr = new FilesReader();
    private Dataset dataset;

    public Dataset readDatasetFile(File file) {
        this.dataset = fr.readDataset(file);
        List<no.uib.jexpress_modularized.core.dataset.Group> updatedActiveGroupList = new ArrayList<no.uib.jexpress_modularized.core.dataset.Group>();
                for(no.uib.jexpress_modularized.core.dataset.Group g:dataset.getRowGroups())
                {
                      if(g.getName().equalsIgnoreCase("ALL")){
                        g.setActive(true);
                        updatedActiveGroupList.add(g);
                        break;
                      }
                }
                dataset.getRowGroups().clear();
                dataset.getRowGroups().addAll(updatedActiveGroupList);
                
                
                 List<no.uib.jexpress_modularized.core.dataset.Group> updatedColActiveGroupList = new ArrayList<no.uib.jexpress_modularized.core.dataset.Group>();
                for(no.uib.jexpress_modularized.core.dataset.Group g:dataset.getColumnGroups())
                {
                    if(g.getName().equalsIgnoreCase("ALL")){
                        g.setActive(true);
                        updatedColActiveGroupList.add(g);
                        break;
                      }
                }
                dataset.getColumnGroups().clear();
                dataset.getColumnGroups().addAll(updatedColActiveGroupList);
        
        return this.dataset;

    }
    
}
