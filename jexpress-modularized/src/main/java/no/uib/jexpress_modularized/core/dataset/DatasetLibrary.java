package no.uib.jexpress_modularized.core.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;


/**
 * @author pawels
 */
public class DatasetLibrary implements Serializable {

    private static DatasetLibrary instance;
    
    private Hashtable<String, DatasetNode> datasets;
    private HashSet<DatasetNode> nodeChierarchy;

    private DatasetLibrary() {
        datasets = new Hashtable<String, DatasetNode>();
        nodeChierarchy = new HashSet<DatasetNode>(4);
    }

    public static DatasetLibrary getDatasetLibrary() {
        if (instance == null) {
            instance = new DatasetLibrary();
        }
        return instance;
    }


    /*
     * Adds a Dataset into the library. If parentDatasetId is null the dataset
     * is added as a root in the hierarchy (dataset is not derived from any other
     * dataset in the library). Otherwise newly added dataset is added as a child
     * of the dataset identified by parentDatasetId
     *
     * @param dataset- the Dataset to add (can't be null)
     * @param parentDatasetId - id of the parent dataset. Null if the added dataset is root
     * @return unique id of the added dataset that can be used to retrieve it later
     */
    public String addDataset(Dataset dataset, String parentDatasetId) {
        if (dataset == null) {
            throw new IllegalArgumentException("Null datasets are not allowed in the DatasetLibrary");
        }
        if (dataset.getName() == null || dataset.getName().equals("")) {
            throw new IllegalArgumentException("Datasets with null and empty names are not allowed in the DatasetLibrary");
        }
        if (parentDatasetId != null && !datasets.containsKey(parentDatasetId)) {
            throw new IllegalArgumentException("Parent dataset not found in the library: "+parentDatasetId);
        }

        Set<DatasetNode> sameLevelNodes = parentDatasetId==null ?
                                          nodeChierarchy :
                                          datasets.get(parentDatasetId).getChildren();
        for (DatasetNode node : sameLevelNodes) {
            if (node.getDataset().getName().equals(dataset.getName())) {
                throw new IllegalArgumentException("Dataset with the same name already exists on this level: "+dataset.getName());
            }
        }
        
        DatasetNode node = new DatasetNode(dataset);
        datasets.put(node.getId(), node);
        if (parentDatasetId == null) {
            nodeChierarchy.add(node);
        } else {
            DatasetNode parent = datasets.get(parentDatasetId);
            parent.addChild(node);
        }
        
        return node.getId();
    }


    /**
     * Retrieves a Dataset from library
     * @param id - id of the Dataset
     * @return Dataset with the given id, or null if a Dataset with the given name is not in the library
     */
    public Dataset getDataset(String id) {
        DatasetNode node = datasets.get(id);
        return node==null ? null : node.getDataset();
    }


    /**
     * Get ids of all root datasets.
     * @return set of root dataset ids
     */
    public Set<String> getRootDatasets() {
        return getChildrenIdsForDataset(null);
    }

    
    /**
     * Get a set of ids of dataset's children or all root datasets.
     * @param datasetId - id of the dataset. If null, root dataset ids are returned
     * @return set of ids of dataset's children, or set of root dataset ids
     * @throws IllegalArgumentException if the dataset identified by datasetId
     * is not in the library
     */
    public Set<String> getChildrenIdsForDataset(String datasetId) {
        Set<DatasetNode> nodes;
        Set<String> ids = new HashSet<String>();

        if (datasetId == null) {                    // get root nodes
            nodes = nodeChierarchy;
        } else if (datasets.containsKey(datasetId)) {     // get children
            nodes = datasets.get(datasetId).getChildren();
        } else {                                      // dataset not in library
            throw new IllegalArgumentException("Dataset is not in the library: "+datasetId);
        }
        
        for (DatasetNode node : nodes) {
            ids.add(node.getId());
        }
        return ids;
    }

    /**
     * Get id of the parent of a dataset. Returns null if the dataset has no parent.
     * @param datasetId - id of the dataset to find parent for
     * @return id of the parent of a dataset identified by datasetId or null
     * if the dataset is a root (has no parent)
     * @throws IllegalArgumentException if the dataset identified by datasetId
     * is not in the library
     */
    public String getParentIdForDataset(String datasetId) {
        if (datasets.containsKey(datasetId)) {
            DatasetNode parent = datasets.get(datasetId).getParent();
            return parent!=null ? parent.getId() : null;
        }
        throw new IllegalArgumentException("Dataset is not in the library: "+datasetId);
    }


    /**
     * Removes a Dataset from library
     * @param id - ide of the dataset to remove
     */
    public void removeDataset(String id) {
        DatasetNode node = datasets.get(id);
        if (node != null) {

            // remove node's children first...
            // copy ids to array as elements can't be removed while iterating over the set
            Set<DatasetNode> children = node.getChildren();
            ArrayList<String> childrenIds = new ArrayList<String>(children.size());
            for (DatasetNode child : children) {
                childrenIds.add(child.getId());
            }
            
            for (String childId : childrenIds) {
                removeDataset(childId);
            }

            // ...and detach the node from parent or root chierarchy
            if (node.getParent() != null) {         // its a child node
                node.getParent().removeChild(node);
            } else {                                // its a root node
                nodeChierarchy.remove(node);
            }
        }
        datasets.remove(id);
    }
    
}
class DatasetNode {
    private String id;
    private Dataset dataset;
    private DatasetNode parent = null;
    private HashSet<DatasetNode> childNodes;

    DatasetNode(Dataset dataset) {
        this.dataset = dataset;
        id = UUID.randomUUID().toString();
        childNodes = new HashSet<DatasetNode>(4);
    }

    /**
     * Adds node as a child and sets this object as its parent
     * @param node
     */
    void addChild(DatasetNode childNode) {
        if (childNode != null) {
            childNodes.add(childNode);
            childNode.setParent(this);
        }
    }

    void removeChild(DatasetNode childNode) {
        if (childNode != null) {
            childNode.setParent(null);       // to allow garbage collection
            childNodes.remove(childNode);
        }
    }
    
    private void setParent(DatasetNode parentNode) {
        parent = parentNode;
    }
    
    Set<DatasetNode> getChildren() { return childNodes; }
    DatasetNode getParent() { return parent; }
    Dataset getDataset() { return dataset; }
    String getId() { return id; }

}