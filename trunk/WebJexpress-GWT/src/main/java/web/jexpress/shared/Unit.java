/*
 * To change this template, choose Tools | Templates
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.TreeMap;

/**
 *
 * @author Yehia Farag
 */
public class Unit implements IsSerializable{
    
     public Unit[] children;

        public double value;

        public String name;
        
        private TreeMap<String, CustomNode> nodesMap;
        
         public Unit() {
        }

        public Unit(String name, double value) {
            this.value = value;
            this.name = name;
        }

        public Unit(String name, Unit... children) {
            this.children = children;
            this.name = name;
        }

    public TreeMap<String, CustomNode> getNodesMap() {
        return nodesMap;
    }

    public void setNodesMap(TreeMap<String, CustomNode> nodesMap) {
        this.nodesMap = nodesMap;
    }
    
}

