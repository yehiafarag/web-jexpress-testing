/*
 * MetaInfoNode.java
 *
 * Created on 30. juli 2003, 15:32
 */
package no.uib.jexpress_modularized.core.visualization.documentation;

import java.util.Iterator;
import javax.swing.tree.*;
import java.util.*;

/**
 *
 * @author bjarte dysvik
 */
public class MetaInfoNode extends java.util.LinkedHashMap implements java.io.Serializable, TreeNode {

    String name = "UnNamed";
    public static String hclust = "H. Clustering";
    public static String hclustsub = "H. Clustering SubNode";
    public static String hclustdis = "H. Clustering W. Distance Matrix";
    public static String kmclust = "K-Means Clustering";
    public static String pca = "PCA";
    public static String old = "Old Meta Tag";
    public static String file = "File";
    public static String similar = "Similarity";
    public static String profiler = "Profiler";
    public static String som = "SOM";
    public static String group = "Grouping";
    public static String groupcontrol = "Group Controller";
    public static String filter = "Filter";
    public static String subdata = "Sub Data";
    public static String fss = "Fss";
    public static String impute = "Replaced Missing Values";
    public static String mds = "MDS";
    public static String ca = "Correspondence Analysis";
    public static String dataviewer = "Dataset Viewer";
    public static String link = "Linked to Parent";
    public static String unlink = "Unlinked from parent";
    public static String colrearr = "Column Rearranger";
    public static String searchnsort = "Search and Sort";
    public static String rawarr = "Raw Array";
    public static String processBatch = "Process Batch";
    public static String processParameter = "Process Parameter";
    public static String sample = "Sample";
    public static String experiment = "SpotPix Experiment";
    public static String root = "Root";
    public static String graph = "Graph";
    public static String spotv = "Spot View";
    public static String foldch = "Fold Change";
    public static String sam = "SAM";
    public static String script = "Script";
    public static String gsea = "GSEA";
    public static String collapse = "Collapse";
    public static String rankproduct = "Rank Product";
    public static String Illumina = "Illumina import";
    public static String RMA = "RMA";
    public static String merged = "Merged";
    public static String clustergrid = "Cluster grid";
    private static final long serialVersionUID = -7589377069041161459L;
    public Double ID;
    private Vector children = new Vector();
    private MetaInfoNode parent;
    // @TODO: i fail to find AnalysisStep??
    //public AnalysisStep as;
    javax.swing.ImageIcon icon;

    /**
     * Creates a new instance of MetaInfoNode
     */
    public MetaInfoNode() {
        super();
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (ID == null) {
            return false;
        } else if (((MetaInfoNode) obj).ID == null) {
            return false;
        } else if (((MetaInfoNode) obj).ID.equals(this.ID)) {
            return true;
        } else {
            return false;
        }

    }

    //Used for testing if all keys (Strings) in this component are keys in the description hashtable.
    public static void testAllKeys(java.util.Hashtable description) {
        // @TODO: reimplment??
//        Class c = MetaInfoNode.class;
//        MetaInfoNode mi = new MetaInfoNode();
//        
//        if(description==null){
//            System.out.print("\nNo Elements in hashtable, loading MethodDescriber");
//            
//            description = new export.MethodDescriber();
//        }
//        String key="";
//        java.lang.reflect.Field[] f = c.getFields();
//        try{
//            for(int i=0;i<f.length;i++){
//                key = (String)f[i].get(mi);
//                if(!description.containsKey(key)){
//                    System.out.print("\nCould not find key for component: "+key);
//                    
//                }
//            }
//        }
//        catch(Exception e){e.printStackTrace();}
    }

    public MetaInfoNode copyNode() {
        MetaInfoNode copy = new MetaInfoNode(name);
        //java.util.Enumeration e = this.keys();

        MetaInfoNode cp = null;
        for (int i = 0; i < this.getChildCount(); i++) {
            cp = (MetaInfoNode) this.getChildAt(i);
            copy.addChild(cp.copyNode());
        }

        Iterator it = keySet().iterator();


        //Object k = null;
        Object k = "";
        Object v = "";
        while (it.hasNext()) {
            k = it.next();
            Object o = get(k);
            //System.out.print("\nObject type: "+o.getClass().getName());
            //v = o;
            copy.put(k, o);
        }
        return copy;
    }
    /*
     * public AnalysisStep.Value_list getAsValueList(){ AnalysisStep.Value_list
     * list= new AnalysisStep.Value_list(); list.addAll(this); return list; }
     */

    public MetaInfoNode(String name) {
        super();
        this.name = name;
        this.ID = new Double(Math.random());
    }

    public String toString() {
        return name;
    }

    public static MetaInfoNode createNode(String ALG, String PARAM1, String VALUE1) {
        MetaInfoNode ret = new MetaInfoNode(ALG);

        ret.put(PARAM1, VALUE1);
        return ret;
    }

    public static MetaInfoNode createNode(String ALG, String PARAM1, String VALUE1, String PARAM2, String VALUE2) {
        MetaInfoNode ret = new MetaInfoNode(ALG);

        ret.put(PARAM1, VALUE1);
        ret.put(PARAM2, VALUE2);
        return ret;
    }

    public static MetaInfoNode createNode(String ALG, String PARAM1, String VALUE1, String PARAM2, String VALUE2, String PARAM3, String VALUE3) {
        MetaInfoNode ret = new MetaInfoNode(ALG);

        ret.put(PARAM1, VALUE1);
        ret.put(PARAM2, VALUE2);
        ret.put(PARAM3, VALUE3);
        return ret;
    }

    public static MetaInfoNode createNode(String ALG, java.util.Hashtable parameters) {
        MetaInfoNode ret = new MetaInfoNode(ALG);

        ret.putAll(parameters);
        return ret;
    }

    public void rename(String name) {
        this.name = name;
        //icon=null;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        // ((java.util.LinkedHashMap)this).writeObject(out);

        java.util.LinkedHashMap map = new java.util.LinkedHashMap(this);
        out.writeObject(name);
        out.writeObject(map);
        out.writeObject(children);
        out.writeObject(parent);
        out.writeObject(ID);
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        //((java.util.LinkedHashMap)this).readObject(out);

        name = (String) in.readObject();

        java.util.LinkedHashMap hm = (java.util.LinkedHashMap) in.readObject();
        this.putAll(hm);

        in.mark(10000000);


        try {

            Object o = in.readObject();

            if (o instanceof Vector) {
                children = (Vector) o;


                parent = (MetaInfoNode) in.readObject();
                ID = (Double) in.readObject();


            } else {
                in.reset();
            }

        } catch (Exception e) {
            children = new Vector();
        }



    }

    /*
     * public void resetParents(MetaInfoNode trunk){
     *
     * // Enumeration ch = trunk.children(); MetaInfoNode n=null;
     *
     * for(int i=0;i<getChildCount();i++){ n=(MetaInfoNode)getChildAt(i);
     * n.setParent(trunk); resetParents(n); }
     *
     *
     * }
     */
    public javax.swing.ImageIcon getIcon() {

        //     javax.swing.ImageIcon icon = null;
        if (icon == null) {
            if (name.equalsIgnoreCase(this.hclust)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bclust.gif"));
            }
            if (name.equalsIgnoreCase(this.hclustsub)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bclust.gif"));
            }
            if (name.equalsIgnoreCase(this.hclustdis)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/dclust.gif"));
            }
            if (name.equalsIgnoreCase(this.kmclust)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bkmeans.gif"));
            }
            if (name.equalsIgnoreCase(this.pca)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bpca.gif"));
            }
            if (name.equalsIgnoreCase(this.old)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/version10.gif"));
            }
            if (name.equalsIgnoreCase(this.file)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/load.gif"));
            }
            if (name.equalsIgnoreCase(this.similar)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/distance.gif"));
            }
            if (name.equalsIgnoreCase(this.profiler)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bprof.gif"));
            }
            if (name.equalsIgnoreCase(this.group)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bgroup.gif"));
            }
            if (name.equalsIgnoreCase(this.som)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bsom.gif"));
            }
            if (name.equalsIgnoreCase(this.groupcontrol)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bclasses.gif"));
            }
            if (name.equalsIgnoreCase(this.filter)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bfilter.gif"));
            }
            if (name.equalsIgnoreCase(this.subdata)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/subdata.gif"));
            }
            if (name.equalsIgnoreCase(this.fss)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/fss.gif"));
            }
            if (name.equalsIgnoreCase(this.impute)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bimpute.gif"));
            }
            if (name.equalsIgnoreCase(this.mds)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bmds.gif"));
            }
            if (name.equalsIgnoreCase(this.ca)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/correspondence.gif"));
            }
            if (name.equalsIgnoreCase(this.link)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/blink.gif"));
            }
            if (name.equalsIgnoreCase(this.unlink)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bunlink.gif"));
            }
            if (name.equalsIgnoreCase(this.dataviewer)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bviewdata.gif"));
            }
            if (name.equalsIgnoreCase(this.colrearr)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/barrange.gif"));
            }
            if (name.equalsIgnoreCase(this.searchnsort)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bsort.gif"));
            }
            if (name.equalsIgnoreCase(this.rawarr)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/barray.gif"));
            }
            if (name.equalsIgnoreCase(this.processBatch)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bprocessbatch.gif"));
            }
            if (name.equalsIgnoreCase(this.processParameter)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bprocessparameter.gif"));
            }
            if (name.equalsIgnoreCase(this.sample)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bsample.gif"));
            }
            if (name.equalsIgnoreCase(this.experiment)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bexperiment.gif"));
            }
            if (name.equalsIgnoreCase(this.root)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/root.gif"));
            }
            if (name.equalsIgnoreCase(this.graph)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bgraph.gif"));
            }
            if (name.equalsIgnoreCase(this.spotv)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bspotv.gif"));
            }
            if (name.equalsIgnoreCase("UnNamed")) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bquest.gif"));
            }
            if (name.equalsIgnoreCase(this.foldch)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bfoldch.gif"));
            }
            if (name.equalsIgnoreCase(this.sam)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bsam.gif"));
            }
            if (name.equalsIgnoreCase(this.script)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bscript.gif"));
            }
            if (name.equalsIgnoreCase(this.gsea)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/gsea.gif"));
            }
            if (name.equalsIgnoreCase(this.collapse)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/subdata.gif"));
            }
            if (name.equalsIgnoreCase(this.rankproduct)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/fss.gif"));
            }
            if (name.equalsIgnoreCase(this.Illumina)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/load.gif"));
            }
            if (name.equalsIgnoreCase(this.RMA)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/load.gif"));
            }
            if (name.equalsIgnoreCase(this.merged)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/icon/bmerged.gif"));
            }
            if (name.equalsIgnoreCase(this.clustergrid)) {
                icon = new javax.swing.ImageIcon(getClass().getResource("/expresscomponents/im/btiles.gif"));
            }
        }

        return icon;
    }

    //Execute this class to find objects not described in the MAGEdesc.txt file.
    //Change the export.metainfoextracter file if file does not excist or have errors.
    //All informations in that file is directly extracted from the java-files responsible for creating a meta-node.
    public static void main(String[] args) {

        MetaInfoNode.testAllKeys(null);
    }

    public java.util.Enumeration children() {
        return children.elements();
    }

    // @TODO: reimplment??
//    public AnalysisStep getTreeAsAnalysisStep(MAGEMLexportInfo expInfo, Hashtable counter){
//        AnalysisStep self = getAsAnalysisStep(expInfo, null);
//        getTreeAsAnalysisStepRec(expInfo,this,counter);
//        expInfo.addToAnalysisStep_list(self);
//        return self;
//    }
    // @TODO: reimplment??
//    private void getTreeAsAnalysisStepRec(MAGEMLexportInfo expInfo,MetaInfoNode parent,Hashtable counter){
//        for(int i=0;i<parent.getChildCount();i++){
//            MetaInfoNode child = (MetaInfoNode) parent.getChildAt(i);
//            child.getAsAnalysisStep(expInfo, counter);
//            parent.as.addChild(child.as);
//            
//            child.getTreeAsAnalysisStepRec(expInfo,child,counter);
//            
//            //AnalysisStep achild = parent.getAsAnalysisStep(expInfo); //child.getTreeAsAnalysisStep(expInfo, child.getAsAnalysisStep(expInfo));
//            //addChild(achild);
//        }
//    }
    // @TODO: reimplment??
//    private AnalysisStep getAsAnalysisStep(MAGEMLexportInfo expInfo, Hashtable counter){
//        AnalysisStep as = new AnalysisStep();
//        as.setParameter_list(this);
//        Integer ID = null;
//        if(counter==null) as.setAlgorithmId( getName() );
//        else{
//            ID=(Integer) counter.get(getName());
//            if(ID==null){
//                counter.put(getName(),new Integer(1));
//                as.setAlgorithmId( getName() +"-1");
//            }
//            else{
//                int nid = ID.intValue()+1;
//                as.setAlgorithmId( getName() +"-"+ nid);
//                counter.put(getName(),new Integer(nid));
//            }
//        }
//        
//        int[] rows=null;
//        int[] cols=null;
//        
//        if(containsKey("Row indexes")){
//            //System.out.print("\nRows");
//            rows=convertIndexes( (String)get("Row indexes"));
//        }
//        if(containsKey("Column indexes")){
//            //System.out.print("\nCols");
//            cols=convertIndexes( (String)get("Column indexes"));
//        }
//        as.setDimensions(rows, cols);
//        
//        java.text.DateFormat form = java.text.DateFormat.getDateInstance();
//        as.setApplicationDate(form.format(Calendar.getInstance().getTime()));
//        
//        
//        //System.out.print("\nNAME:"+name);
//        //System.out.print("\nAS:"+as.getAlgorithmId());
//        
//        this.as=as;
//        return as;
//    }
    private int[] convertIndexes(String indexes) {
        String[] s = indexes.split(",");
        int[] ret = new int[s.length];

        for (int i = 0; i < s.length; i++) {
            try {
                ret[i] = Integer.parseInt(s[i]);
            } catch (Exception e) {
            }
        }
        return ret;
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) children.elementAt(childIndex);
    }

    public int getChildCount() {
        return children.size();
    }

    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    public TreeNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        if (children.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void setParent(MetaInfoNode parent) {
        this.parent = parent;
    }

    public void addChild(MetaInfoNode child) {
        if (child == null) {
            return;
        }
        child.setParent(this);
        children.addElement(child);
    }

    public void removeChild(MetaInfoNode child) {
        children.remove(child);
    }
}
