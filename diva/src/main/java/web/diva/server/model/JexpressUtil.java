package web.diva.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.uib.jexpress_modularized.core.dataset.Group;
import web.diva.server.model.beans.DivaDataset;
//import web.diva.shared.model.core.model.dataset.Group;

public class JexpressUtil {

    public Map<Integer, String> initIndexNameGeneMap(String [] rowIds) {
        Map<Integer, String> geneMap = new HashMap<Integer, String>();

        for (int index = 0; index < rowIds.length; index++) {
            geneMap.put(index, rowIds[index]);
        }
        return geneMap;
    }

//    public web.diva.shared.model.core.model.dataset.Dataset initDivaDs(Dataset divaDataset, int datasetId) {
//        web.diva.shared.model.core.model.dataset.Dataset webDataset = new web.diva.shared.model.core.model.dataset.Dataset();
//        webDataset.setId(datasetId);
//        webDataset.setColumnIds(divaDataset.getColumnIds());
//        webDataset.setInfoHeaders(divaDataset.getInfoHeaders());
//        webDataset.setMeasurements(divaDataset.getData());
//        webDataset.setRowIds(divaDataset.getRowIds());
//
//        Map<Integer, String> geneIndexNameMap = initIndexNameGeneMap(webDataset);
//        webDataset.setGeneIndexNameMap(geneIndexNameMap);
//        webDataset.setGeneNameIndexMap(initNameIndexGeneMap(geneIndexNameMap));
//        if (!divaDataset.getColumnGroups().isEmpty()) {
//            for (no.uib.jexpress_modularized.core.dataset.Group jG : divaDataset.getColumnGroups()) {
//                String hex = "#" + Integer.toHexString(jG.getColor().getRGB()).substring(2);
//                Group g = new Group();
//                g.setType("Col");
//                g.setColor(hex);
//                List<Integer> ind = new ArrayList<Integer>();
//                for (int x : jG.getMembers()) {
//                    ind.add(x);
//                }
//                g.setIndices(ind);
//                g.setGeneList(initGroupGeneList(webDataset, jG.getMembers()));
//                g.setId(jG.getName());
//                g.setActive(jG.isActive());
//                webDataset.addColumnGroup(g);
//            }
//        }
//
//        if (!divaDataset.getRowGroups().isEmpty()) {
//            for (no.uib.jexpress_modularized.core.dataset.Group jG : divaDataset.getRowGroups()) {
//                String hex = "#" + Integer.toHexString(jG.getColor().getRGB()).substring(2);
//
//                Group g = new Group();
//                g.setType("Row");
//                g.setColor(hex);
//                List<Integer> ind = new ArrayList<Integer>();
//                for (int x : jG.getMembers()) {
//                    ind.add(x);
//                }
//                g.setIndices(ind);
//
//                g.setGeneList(initGroupGeneList(webDataset, jG.getMembers()));
//                g.setId(jG.getName());
//                g.setActive(jG.isActive());
//                webDataset.addRowGroup(g);
//            }
//
//        }
//        webDataset.setMemberMaps(initIndexMembers(webDataset));
//
//        return webDataset;
//    }
    
    
    public DivaDataset initDivaDs(DivaDataset divaDataset, int datasetId) {
//        web.diva.shared.model.core.model.dataset.Dataset webDataset = new web.diva.shared.model.core.model.dataset.Dataset();
        divaDataset.setId(datasetId);
//        webDataset.setColumnIds(divaDataset.getColumnIds());
//        webDataset.setInfoHeaders(divaDataset.getInfoHeaders());
//        webDataset.setMeasurements(divaDataset.getData());
//        webDataset.setRowIds(divaDataset.getRowIds());

        Map<Integer, String> geneIndexNameMap = initIndexNameGeneMap(divaDataset.getRowIds());
        divaDataset.setGeneIndexNameMap(geneIndexNameMap);
        divaDataset.setGeneNameIndexMap(initNameIndexGeneMap(geneIndexNameMap));
        if (!divaDataset.getColumnGroups().isEmpty()) {
            List<Group>tgl = new ArrayList<Group>();
            for (Group g : divaDataset.getColumnGroups()) {
                g.setGeneList(initGroupGeneList(divaDataset.getGeneIndexNameMap(), g.getMembers()));
                tgl.add(g); 
            }
            divaDataset.getColumnGroups().clear();
            divaDataset.getColumnGroups().addAll(tgl);
        }

        if (!divaDataset.getRowGroups().isEmpty()) {
             List<Group>tgl = new ArrayList<Group>();
            for (Group g : divaDataset.getRowGroups()) {
                g.setGeneList(initGroupGeneList(divaDataset.getGeneIndexNameMap(), g.getMembers()));
                tgl.add(g);
            }
            divaDataset.getRowGroups().clear();
            divaDataset.getRowGroups().addAll(tgl);

        }
        Map<Integer, Number[]>  membersMap = initIndexMembers(divaDataset);
        divaDataset.setMembersMap(membersMap);
        return divaDataset;
    }


    public Map<String, Integer> initNameIndexGeneMap(Map<Integer, String> indexNameMap) {
        Map<String, Integer> geneMap = new HashMap<String, Integer>();
        for (int key : indexNameMap.keySet()) {
            geneMap.put(indexNameMap.get(key), key);
        }
        return geneMap;
    }

    public List<String> initGroupGeneList(Map<Integer, String> geneIndexNameMap, int[] members) {
        List<String> geneList = new ArrayList<String>();
        for (int x : members) {
            geneList.add(geneIndexNameMap.get(x));
        }

        return geneList;

    }

    public Map<Integer, Number[]> initIndexMembers(DivaDataset dataset) {
        Map<Integer, Number[]> memberMaps = new HashMap<Integer, Number[]>();
        for (int x = 0; x < dataset.getDataLength(); x++) {
            double[] row = dataset.getData()[x];
            Number[] points = new Number[row.length];
            for (int y = 0; y < dataset.getDataWidth(); y++) {
                points[y] = row[y];
            }

            memberMaps.put(x, points);

        }

        return memberMaps;
    }

    public String[] initGeneNamesArr(Map<Integer, String> geneIndexName) {
        String[] geneNameArr = new String[geneIndexName.size()];
        for (int key : geneIndexName.keySet()) {
            geneNameArr[key] = geneIndexName.get(key);
        }
        return geneNameArr;
    }

    public String[] initColorArr(String[] geneNameArr, List<Group> groupList) {
        String[] colorArr = new String[geneNameArr.length];
        for (int x = 0; x < geneNameArr.length; x++) {
            String color = "#BDBDBD";
            for (Group g : groupList) {
                if (g.getName().equalsIgnoreCase("all") && g.isActive()) {
                    color = "#000000";
                } else if (g.getGeneList().contains(geneNameArr[x]) && g.isActive()) {
                    color = g.getHashColor();
                }
                colorArr[x] = color;
            }
        }
        return colorArr;

    }
}
