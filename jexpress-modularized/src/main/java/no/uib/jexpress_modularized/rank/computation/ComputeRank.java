/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.rank.computation;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import javax.swing.ProgressMonitor;
import no.uib.jexpress_modularized.core.computation.JDoubleSorter;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.rank.computation.util.FoldException;
import no.uib.jexpress_modularized.rank.computation.util.Rank_Product;

/**
 *
 * @author Yehia Farag
 */
public class ComputeRank implements Serializable {

    private Dataset data;
    private double[] fold = null;
    private double[] score_pos = null;
    private double[] eScore_pos = null;
    private double[] qval_pos = null;
    private double[] score_neg = null;
    private double[] eScore_neg = null;
    private double[] qval_neg = null;
    
   

    public ComputeRank(Dataset data) {
        this.data = data;
    }

    public ArrayList<RPResult> createResult(String type, int permutations, int seed, int[] g1, int[] g2, boolean log2) {

       
        double[][] dat = data.getData();
        ProgressMonitor pm = null;
          DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
                otherSymbols.setGroupingSeparator('.');
                DecimalFormat df = new DecimalFormat("#.###", otherSymbols);
        if (type.equals("TwoClassUnPaired")) {
            Rank_Product rp = new Rank_Product();
            java.util.Random ran = new java.util.Random(seed);
            try {
                double[] tfold = rp.getAveFold(dat, g1, g2, log2);
                double[] tscore_pos = rp.getRankProductTwoClassUnpaired(dat, g1, g2, log2);
                double[] tscore_neg = rp.getRankProductTwoClassUnpaired(dat, g2, g1, log2);
                double[][] pRP = rp.getRandomScores(dat, g1, g2, log2, permutations, ran);
                if (pRP == null) {
                    return null;
                }
                double[] tem_eScore_pos = rp.getExpRP(tscore_pos, pRP);
                double[] tem_eScore_neg = rp.getExpRP(tscore_neg, pRP);
                double[] tem_qval_pos = rp.qval(tscore_pos, tem_eScore_pos);
                double[] tem_qval_neg = rp.qval(tscore_neg, tem_eScore_neg);
                
                
                //format the results 
              
                fold = new double[tfold.length];
                for(int x = 0; x<tfold.length;x++)
                {
                    fold[x] = Double.valueOf(df.format(tfold[x]));                
                }
                
                 score_pos = new double[tscore_pos.length];
                for(int x = 0; x<tscore_pos.length;x++)
                {
                    score_pos[x] = Double.valueOf(df.format(tscore_pos[x]));                
                }
                score_neg = new double[tscore_neg.length];
                for(int x = 0; x<tscore_neg.length;x++)
                {
                    score_neg[x] = Double.valueOf(df.format(tscore_neg[x]));                
                }
                
                
                 eScore_pos = new double[tem_eScore_pos.length];
                for(int x = 0; x<tem_eScore_pos.length;x++)
                {
                    eScore_pos[x] = Double.valueOf(df.format(tem_eScore_pos[x]));                
                }
                
                
                eScore_neg = new double[tem_eScore_neg.length];
                for(int x = 0; x<tem_eScore_neg.length;x++)
                {
                    eScore_neg[x] = Double.valueOf(df.format(tem_eScore_neg[x]));                
                }
                
                
                qval_pos = new double[tem_qval_pos.length];
                for(int x = 0; x<tem_qval_pos.length;x++)
                {
                    qval_pos[x] = Double.valueOf(df.format(tem_qval_pos[x]));                
                }
                qval_neg = new double[tem_qval_neg.length];
                for(int x = 0; x<tem_qval_neg.length;x++)
                {
                    qval_neg[x] = Double.valueOf(df.format(tem_qval_neg[x]));                
                }

            } catch (FoldException e) {
            }


        } else if (type.equals("OneClass")) {

            Rank_Product rp = new Rank_Product();
            rp.setSeed(seed);
            int[][] ranks = rp.getRanks(dat, g1);
            java.util.Random ran = new java.util.Random(seed);
            try {
              double[]  tfold = rp.getAveFold(dat, g1, log2);
               
            double[] tscore_pos = rp.getRankProductOneClass(dat, g1, false);
            double[] tscore_neg = rp.getRankProductOneClass(dat, g1, true);
            double[][] pRP = rp.getRandomScores(ranks, permutations, ran, pm);
            if (pRP == null) {
                return null;
            }
            double[] tem_eScore_pos = rp.getExpRP(tscore_pos, pRP);
            double[] tem_eScore_neg = rp.getExpRP(tscore_neg, pRP);
            double[] tem_qval_pos = rp.qval(tscore_pos, tem_eScore_pos);
            double [] tem_qval_neg = rp.qval(tscore_neg, tem_eScore_neg);
            
            
             fold = new double[tfold.length];
                for(int x = 0; x<tfold.length;x++)
                {
                    fold[x] = Double.valueOf(df.format(tfold[x]));                
                }
                
                  score_pos = new double[tscore_pos.length];
                for(int x = 0; x<tscore_pos.length;x++)
                {
                    score_pos[x] = Double.valueOf(df.format(tscore_pos[x]));                
                }
                score_neg = new double[tscore_neg.length];
                for(int x = 0; x<tscore_neg.length;x++)
                {
                    score_neg[x] = Double.valueOf(df.format(tscore_neg[x]));                
                }
                
                 eScore_pos = new double[tem_eScore_pos.length];
                for(int x = 0; x<tem_eScore_pos.length;x++)
                {
                    eScore_pos[x] = Double.valueOf(df.format(tem_eScore_pos[x]));                
                }
                
                
                eScore_neg = new double[tem_eScore_neg.length];
                for(int x = 0; x<tem_eScore_neg.length;x++)
                {
                    eScore_neg[x] = Double.valueOf(df.format(tem_eScore_neg[x]));                
                }
                
                
                qval_pos = new double[tem_qval_pos.length];
                for(int x = 0; x<tem_qval_pos.length;x++)
                {
                    qval_pos[x] = Double.valueOf(df.format(tem_qval_pos[x]));                
                }
                qval_neg = new double[tem_qval_neg.length];
                for(int x = 0; x<tem_qval_neg.length;x++)
                {
                    qval_neg[x] = Double.valueOf(df.format(tem_qval_neg[x]));                
                }

 } catch (FoldException e) {
            }
        } else if (type.equals("Paired")) {
        } else {
        }
        return createTable();
    }
    private int[] posSortedresult;

    public int[] getPosSortedresult() {
        return posSortedresult;
    }

    public int[] getNegSortedresult() {
        return negSortedresult;
    }
    private int[] negSortedresult;

    private ArrayList<RPResult> createTable() {
        ArrayList<RPResult> rpmList = new ArrayList<RPResult>();

        Vector posResult = new Vector();
        Vector negResult = new Vector();

        Vector negRow = null;
        Vector posRow = null;
        Map<Integer, Integer> postiveSortMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> negativeSortMap = new HashMap<Integer, Integer>();

        int[] posDataIndex = new int[score_pos.length];
        int[] negDataIndex = new int[score_neg.length];

        for (int g = 0; g < posDataIndex.length; g++) {
            posDataIndex[g] = g;
        }
        for (int g = 0; g < negDataIndex.length; g++) {
            negDataIndex[g] = g;
        }

        //Sorting in Ascending order
        posDataIndex = JDoubleSorter.quickSort(score_pos);
        negDataIndex = JDoubleSorter.quickSort(score_neg);

        String[][] infos = data.getInfos();

        for (int i = 0; i < score_pos.length; i++) {
            posRow = new Vector();
            negRow = new Vector();
            posRow.add((i + 1));
            for (int j = 0; j < infos[0].length; j++) {
                posRow.add(infos[posDataIndex[i]][j]);
            }
            postiveSortMap.put(i + 1, posDataIndex[i]);
            posRow.add(fold[posDataIndex[i]]);

            posRow.add(score_pos[posDataIndex[i]]);
            posRow.add(eScore_pos[posDataIndex[i]]);
            posRow.add(qval_pos[posDataIndex[i]]);


            posResult.add(posRow);


        }

        posSortedresult = posDataIndex;

        String[] infoH = data.getInfoHeaders();
        Vector posTableHeader = new Vector();

        posTableHeader.add("Rank");
        for (int i = 0; i < infoH.length; i++) {
            posTableHeader.add(infoH[i]);
        }

        posTableHeader.add("Fold change");

        posTableHeader.add("Pos Score");
        posTableHeader.add("Expected Score");
        posTableHeader.add("q-value");

        for (int i = 0; i < score_neg.length; i++) {

            negRow = new Vector();
            negRow.add((i + 1));
            for (int j = 0; j < infos[0].length; j++) {
                negRow.add(infos[negDataIndex[i]][j]);
            }
            negativeSortMap.put(i + 1, negDataIndex[i]);
            negRow.add(fold[negDataIndex[i]]);



            if (score_neg != null) {
                negRow.add(score_neg[negDataIndex[i]]);
                negRow.add(eScore_neg[negDataIndex[i]]);
                negRow.add(qval_neg[negDataIndex[i]]);
            }
            negResult.add(negRow);


        }

        negSortedresult = negDataIndex;

        Vector negTableHeader = new Vector();
        negTableHeader.add("Rank");
        for (int i = 0; i < infoH.length; i++) {
            negTableHeader.add(infoH[i]);
        }

        negTableHeader.add("Fold change");

        if (score_neg != null) {
            negTableHeader.add("Neg Score");
            negTableHeader.add("Expected Score");
            negTableHeader.add("q-value");
        }
        RPResult posRpmod = new RPResult(posResult, posTableHeader);
        RPResult negRpmod = new RPResult(negResult, negTableHeader);
        posRpmod.setSortMap(postiveSortMap);
        negRpmod.setSortMap(negativeSortMap);
        rpmList.add(posRpmod);
        rpmList.add(negRpmod);

        return rpmList;

    }
}
