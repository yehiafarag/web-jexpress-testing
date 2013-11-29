package web.jexpress.server.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.uib.jexpress_modularized.core.dataset.DataSet;
import utility.FilesReader;
import web.jexpress.shared.model.core.model.dataset.Dataset;
import web.jexpress.shared.model.core.model.dataset.Group;

public class JexpressUtil {

    private File file;         // File to write to.
    private final FilesReader fr = new FilesReader();
    private DataSet dataset;

    public DataSet initJexpressDataset() {
        file = new File("F:\\files\\diauxic shift.txt");
         
         
//        file = new File("/home/probe/diauxic_shift.txt");

       
        this.dataset = fr.readDataset(file);
        return this.dataset;

    }

    public Map<Integer, String> initIndexNameGeneMap(Dataset dataset) {
        Map<Integer, String> geneMap = new HashMap<Integer, String>();


        for (int index = 0; index < dataset.getDataLength(); index++) {
            geneMap.put(index, dataset.getRowIds()[index]);
        }
        return geneMap;
    }

    public Dataset initWebDataset(DataSet jexpressDataset, int datasetId) {
        Dataset webDataset = new Dataset();
        webDataset.setId(datasetId);
        webDataset.setColumnIds(jexpressDataset.getColumnIds());
        webDataset.setInfoHeaders(jexpressDataset.getInfoHeaders());
        webDataset.setMeasurements(jexpressDataset.getData());
        webDataset.setRowIds(jexpressDataset.getRowIds());

        Map<Integer, String> geneIndexNameMap = initIndexNameGeneMap(webDataset);
        webDataset.setGeneIndexNameMap(geneIndexNameMap);
        webDataset.setGeneNameIndexMap(initNameIndexGeneMap(geneIndexNameMap));
        if (!jexpressDataset.getColumnGroups().isEmpty()) {
            for (no.uib.jexpress_modularized.core.dataset.Group jG : jexpressDataset.getColumnGroups()) {
                String hex = "#" + Integer.toHexString(jG.getColor().getRGB()).substring(2);
                Group g = new Group();
                g.setType("Col");
                g.setColor(hex);
                List<Integer> ind = new ArrayList<Integer>();
                for (int x : jG.getMembers()) {
                    ind.add(x);
                }
                g.setIndices(ind);
                g.setGeneList(initGroupGeneList(webDataset, jG.getMembers()));
                g.setId(jG.getName());
                webDataset.addColumnGroup(g);
            }
        }

        if (!jexpressDataset.getRowGroups().isEmpty()) {
            for (no.uib.jexpress_modularized.core.dataset.Group jG : jexpressDataset.getRowGroups()) {
                String hex = "#" + Integer.toHexString(jG.getColor().getRGB()).substring(2);
                Group g = new Group();
                g.setType("Row");
                g.setColor(hex);
                List<Integer> ind = new ArrayList<Integer>();
                for (int x : jG.getMembers()) {
                    ind.add(x);
                }
                g.setIndices(ind);

                g.setGeneList(initGroupGeneList(webDataset, jG.getMembers()));
                g.setId(jG.getName());
                webDataset.addRowGroup(g);
            }


        }
        webDataset.setMemberMaps(initIndexMembers(webDataset));


     



        return webDataset;
    }

    public Map<String, Integer> initNameIndexGeneMap(Map<Integer, String> indexNameMap) {
        Map<String, Integer> geneMap = new HashMap<String, Integer>();
        for (int key : indexNameMap.keySet()) {
            geneMap.put(indexNameMap.get(key), key);
        }
        return geneMap;
    }

    public List<String> initGroupGeneList(Dataset dataset, int[] members) {
        List<String> geneList = new ArrayList<String>();
        for (int x : members) {
            geneList.add(dataset.getGeneIndexNameMap().get(x));
        }


        return geneList;


    }

    public Map<Integer, Number[]> initIndexMembers(Dataset dataset) {
        Map<Integer, Number[]> memberMaps = new HashMap<Integer, Number[]>();
        for (int x = 0; x < dataset.getDataLength(); x++) {
            double[] row = dataset.getMeasurements()[x];
            Number[] points = new Number[row.length];
            for (int y = 0; y < dataset.getDataWidth(); y++) {
                points[y] = row[y];
            }

            memberMaps.put(x, points);

        }

        return memberMaps;
    }
    
    
    public String[] initGeneNamesArr(Map<Integer,String> geneIndexName)
    {
        String[] geneNameArr = new String[geneIndexName.size()];
        for(int key:geneIndexName.keySet()){
            geneNameArr[key] = geneIndexName.get(key);
        }
        return geneNameArr;    
    }
    
    public String[] initColorArr(String[]geneNameArr,List<Group> groupList )
    {
        String[] colorArr = new String[geneNameArr.length];
        for(int x=0;x<geneNameArr.length;x++)
        {
            String color ="#000000";
            for(Group g:groupList)
            {
                if(g.getId().equalsIgnoreCase("all"))
                    continue;
                else if(g.getGeneList().contains(geneNameArr[x]))
                {
                    color = g.getColor();
                }
                colorArr[x] = color;                
            }        
        }        
        return colorArr;
    
    }
}
