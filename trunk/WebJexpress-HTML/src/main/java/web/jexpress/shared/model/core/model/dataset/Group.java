/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.shared.model.core.model.dataset;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.List;

/**
 *
 * @author Yehia Farag
 */
public class Group implements IsSerializable{
    private String type;
    private String color;
    private List<Integer> indices; 
    private List<String> geneList;
    private boolean active;

    public List<String> getGeneList() {
        return geneList;
    }

    public void setGeneList(List<String> geneList) {
        this.geneList = geneList;
    }
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setIndices(List<Integer> indices) {        
        this.indices =indices;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    
}
