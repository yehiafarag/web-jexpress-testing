package web.diva.server.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.uib.jexpress_modularized.core.dataset.Group;
import web.diva.server.model.beans.DivaDataset;

public class DivaUtil {

    public Map<Integer, String> initIndexNameGeneMap(String[] rowIds) {
        Map<Integer, String> geneMap = new HashMap<Integer, String>();

        for (int index = 0; index < rowIds.length; index++) {
            geneMap.put(index, rowIds[index]);
        }
        return geneMap;
    }

    public DivaDataset initDivaDs(DivaDataset divaDataset, int datasetId) {
        divaDataset.setId(datasetId);
        Map<Integer, String> geneIndexNameMap = initIndexNameGeneMap(divaDataset.getRowIds());
        divaDataset.setGeneIndexNameMap(geneIndexNameMap);
        divaDataset.setGeneNameIndexMap(initNameIndexGeneMap(geneIndexNameMap));
        if (!divaDataset.getColumnGroups().isEmpty()) {
            List<Group> tgl = new ArrayList<Group>();
            for (Group g : divaDataset.getColumnGroups()) {
                g.setGeneList(initGroupGeneList(divaDataset.getGeneIndexNameMap(), g.getMembers()));
                tgl.add(g);
            }
            divaDataset.getColumnGroups().clear();
            divaDataset.getColumnGroups().addAll(tgl);
        }

        if (!divaDataset.getRowGroups().isEmpty()) {
            List<Group> tgl = new ArrayList<Group>();
            for (Group g : divaDataset.getRowGroups()) {
                g.setGeneList(initGroupGeneList(divaDataset.getGeneIndexNameMap(), g.getMembers()));
                tgl.add(g);
            }
            divaDataset.getRowGroups().clear();
            divaDataset.getRowGroups().addAll(tgl);

        }
        Map<Integer, Number[]> membersMap = initIndexMembers(divaDataset);
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

    public String exportDataTotext(DivaDataset divaDataset, Group rowGroup, String path, String url, String textFileName) {
        File text = new File(path, textFileName + ".txt");
        try {
            if (text.exists()) {
                text.delete();
            }
            text.createNewFile();
            FileWriter outFile = new FileWriter(text, true);
            PrintWriter out1 = new PrintWriter(outFile);
            String header = divaDataset.getInfoHeaders()[0];
            for (int x = 0; x < divaDataset.getColumnIds().length; x++) {
                header += "\t" + divaDataset.getColumnIds()[x];
            }
            try {
                out1.append(header);
                out1.println();
                int[] dataIndex = rowGroup.getMembers();
                Arrays.sort(dataIndex);
                for (int x : dataIndex) {
                    String line = divaDataset.getGeneNamesArr()[x];
                    double[] data = divaDataset.getData()[x];
                    for (int z = 0; z < data.length; z++) {
                        line += "\t" + data[z];
                    }
                    out1.append(line);
                    out1.println();

                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                out1.close();
                outFile.close();
            }
             System.gc();
            return url + text.getName();
        } catch (IOException e) {
            System.err.println(e.getMessage());
             System.gc();
        }
        return "";

    }
}
