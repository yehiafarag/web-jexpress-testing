/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.client.somclust.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.TreeMap;
import web.jexpress.shared.CustomNode;

/**
 *
 * @author Y.M
 */
public class SomClustClientModel implements IsSerializable{
    
    private CustomNode clickedNode; 
    private TreeMap<String,CustomNode> nodesMap;// = root.getNodesMap();
    
    
    public CustomNode getCustomNode(String nodeName)    {        
         return nodesMap.get(nodeName);    
    }



   

    public CustomNode getClickedNode() {
        return clickedNode;
    }

    public void setClickedNode(CustomNode clickedNode) {
        this.clickedNode = clickedNode;
    }

    public TreeMap<String,CustomNode> getNodesMap() {
        return nodesMap;
    }

    public void setNodesMap(TreeMap<String,CustomNode> nodesMap) {
        this.nodesMap = nodesMap;
    }
    
}
