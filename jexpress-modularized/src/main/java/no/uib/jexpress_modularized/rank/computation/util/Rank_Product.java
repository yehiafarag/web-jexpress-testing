/*
 * Rank_Product.java
 *
 * Created on 03 July 2007, 11:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.rank.computation.util;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.ProgressMonitor;

/**
 *
 * @author Anne-Kristin
 */
public class Rank_Product implements Serializable {

    /**
     * Creates a new instance of Rank_Product
     */
    private int seed;

    public Rank_Product() {
    }

    public double[] getRankProductPaired(double[][] data, int[][] pairs, boolean isLog, boolean pos) throws FoldException {//,boolean descending){
        if (data == null || pairs == null) {
            return null;
        }
        return getProduct(getRanks(data, pairs, isLog, pos));
    }

    public double[] getRankProductOneClass(double[][] data, int[] g1, boolean neg) {//,boolean descending){
        if (data == null || g1 == null) {
            return null;
        }

        int[][] tmp = getRanks(data, g1);//,descending);
        int[][] ranks = null;
        if (neg) {
            java.util.Hashtable h = new java.util.Hashtable();
            int n = tmp[0].length;
            for (int i = 0; i < n; i++) {
                h.put(i + 1, n - i);
            }
            ranks = new int[tmp.length][tmp[0].length];

            for (int i = 0; i < ranks.length; i++) {
                for (int j = 0; j < ranks[0].length; j++) {
                    ranks[i][j] = ((Integer) h.get(tmp[i][j])).intValue();
                }
            }
        } else {
            ranks = tmp;
        }

        return getProduct(ranks);
    }

    public double[] getRankProductTwoClassUnpaired(double[][] data, int[] g1, int[] g2, boolean isLog) throws FoldException {

        if (data == null || g1 == null || g2 == null) {
            return null;
        }
        int[][] ranks = null;
        try {
            ranks = this.getRanks(data, g1, g2, isLog);
        } catch (FoldException e) {
            throw e;
        }

        return getProduct(ranks);
    }

    public double[] getProduct(int[][] ranks) {
        if (ranks == null || ranks[0] == null) {
            return null;
        }

        double[] ret = new double[ranks[0].length];
        int N = ranks[0].length;
        int sml = ranks.length;
        double rank = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < sml; j++) {
                if (j > 0) {
                    ret[i] = ret[i] * ((double) ranks[j][i] / (double) N);
                } else {
                    ret[i] = (double) ranks[j][i] / (double) N;
                }
            }
        }

        return ret;
    }

    public int[][] getRanks(double[][] data, int[] g1) {//,boolean descending){
        if (data == null || g1 == null) {
            return null;
        }
        int[][] ranks = new int[g1.length][];
        double[] dat1 = null;

        int sml = 0;

        for (int i = 0; i < g1.length; i++) {
            dat1 = new double[data.length];

            for (int g = 0; g < data.length; g++) {
                dat1[g] = data[g][g1[i]];
            }
            ranks[sml] = getRank(dat1);//,descending);

            sml++;
        }
        return ranks;
    }

    public int[][] getRanks(double[][] data, int[][] pairs, boolean isLog, boolean pos) throws FoldException {

        if (data == null || pairs == null) {
            return null;
        }

        int[][] ranks = new int[pairs[0].length][];

        int sml = 0;

        double[] dat1 = null;
        double[] dat2 = null;
        double[] fold = null;

        for (int i = 0; i < pairs[0].length; i++) {

            dat1 = new double[data.length];
            dat2 = new double[data.length];
            for (int g = 0; g < data.length; g++) {
                if (pos) {
                    dat1[g] = data[g][pairs[0][i]];
                    dat2[g] = data[g][pairs[1][i]];
                } else {
                    dat1[g] = data[g][pairs[1][i]];
                    dat2[g] = data[g][pairs[0][i]];
                }

            }
            try {
                fold = getFold(dat1, dat2, isLog);
            } catch (FoldException e) {
                e.printStackTrace();
                throw e;
            }

            ranks[sml] = getRank(fold);//,descending);            
            sml++;

        }
        return ranks;

    }

    public int[][] getRanks(double[][] data, int[] g1, int[] g2, boolean isLog) throws FoldException {
        int[][] ranks = new int[g1.length * g2.length][];

        int sml = 0;

        double[] dat1 = null;
        double[] dat2 = null;
        double[] fold = null;

        for (int i = 0; i < g1.length; i++) {
            for (int j = 0; j < g2.length; j++) {
                dat1 = new double[data.length];
                dat2 = new double[data.length];

                for (int g = 0; g < data.length; g++) {
                    dat1[g] = data[g][g1[i]];
                    dat2[g] = data[g][g2[j]];
                }
                try {
                    fold = getFold(dat1, dat2, isLog);
                } catch (FoldException e) {
                    throw e;
                }

                ranks[sml] = getRank(fold);

                sml++;
            }
        }
        return ranks;
    }

    public int[] getRank(double[] fold) {//,boolean descending){
        if (fold == null) {
            return null;
        }

        int[] rank = new int[fold.length];
        int[] dataIndex = new int[fold.length];

        for (int g = 0; g < fold.length; g++) {
            dataIndex[g] = g;
        }

        //Sorting in Ascending order
        Stat.parallelSort(fold, dataIndex);


        double tmp = 0.0;
        int temp = 0;
        //Sorting in Descending order:
        for (int i = 0; i < fold.length / 2; i++) {
            tmp = fold[i];
            fold[i] = fold[fold.length - 1 - i];
            fold[fold.length - 1 - i] = tmp;

            temp = dataIndex[i];
            dataIndex[i] = dataIndex[dataIndex.length - 1 - i];
            dataIndex[dataIndex.length - 1 - i] = temp;
        }


        for (int g = 0; g < dataIndex.length; g++) {
            rank[dataIndex[g]] = g + 1;
        }

        return rank;
    }

    public double[] getFold(double[] dat1, double[] dat2, boolean isLog) throws FoldException {
        if (dat1 == null || dat2 == null) {
            return null;
        }

        double[] fold = new double[dat1.length];

        if (isLog) {
            for (int g = 0; g < fold.length; g++) {
                fold[g] = Math.pow(2, (dat2[g] - dat1[g]));
            }
        } else {
            for (int g = 0; g < fold.length; g++) {
                if (dat2[g] == 0.0 && dat1[g] == 0.0) {
                    fold[g] = 1.0;
                } else if (dat2[g] == 0.0 || dat1[g] == 0.0) {
                    throw new FoldException("Infinity");
                } else if (Double.isNaN(dat1[g]) || Double.isNaN(dat2[g])) {
                    throw new FoldException("NaN");
                } else {
                    fold[g] = dat2[g] / dat1[g];
                }
            }
        }

        for (int g = 0; g < fold.length; g++) {
            if (fold[g] < 1) {
                fold[g] = -1 / fold[g];
            }
        }
        return fold;
    }

    public double[] getAveFold(double[][] dat, int[] g1, int[] g2, boolean isLog) throws FoldException {

         
        double[] fold = null;

        double[] dat1 = new double[dat.length];
        double[] dat2 = new double[dat.length];

        for (int i = 0; i < dat.length; i++) {
            for (int j = 0; j < g1.length; j++) {
                dat1[i] += dat[i][g1[j]];
            }
            dat1[i] = dat1[i] / (double) g1.length;

        }
        for (int i = 0; i < dat.length; i++) {
            for (int l = 0; l < g2.length; l++) {
                dat2[i] += dat[i][g2[l]];
            }
            dat2[i] = dat2[i] / (double) g2.length;

        }

        try {
            fold = getFold(dat1, dat2, isLog);
        } catch (FoldException e) {
            throw e;
        }

        return fold;

    }

    public double[] getAveFold(double[][] dat, int[] g1, boolean isLog) throws FoldException {

        double[] ret = new double[dat.length];
        //int sml = 0;

        double[] dat1 = null;
        double[] dat2 = null;

        for (int j = 0; j < g1.length; j++) {
            for (int i = 0; i < dat.length; i++) {
                ret[i] += dat[i][g1[j]];
            }
        }

        for (int f = 0; f < ret.length; f++) {
            if (isLog) {
                ret[f] = Math.pow(2, ret[f] / (double) (g1.length));
            } else {
                ret[f] = ret[f] / (double) (g1.length);
            }
        }

        return ret;

    }

    public double[] getAveFold(double[][] dat, int[][] pairs, boolean isLog) throws FoldException {

        double[] ret = new double[dat.length];

        for (int i = 0; i < dat.length; i++) {
            for (int j = 0; j < pairs[0].length; j++) {
                if (isLog) {
                    ret[i] += (dat[i][pairs[1][j]] - dat[i][pairs[0][j]]);
                } else {
                    ret[i] += (dat[i][pairs[1][j]] / dat[i][pairs[0][j]]);
                }
            }

            System.out.println("reti " + ret[i]);
            ret[i] = ret[i] / (double) pairs[0].length;
            System.out.println("etter deling " + ret[i]);
            if (isLog) {
                ret[i] = Math.pow(2, (ret[i]));
            }
            System.out.println("etter log" + ret[i]);
        }
        return ret;
    }

    public double[][] transpose(double[][] data) {
        double[][] dataTransposed = new double[data[0].length][data.length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                dataTransposed[j][i] = data[i][j];
            }
        }
        return dataTransposed;
    }

    //returns E(RP)
    public double[] permuteOneClass(double[] RP, int[][] original_ranks, int perm, java.util.Random ran, ProgressMonitor pm) {
        if (RP == null || perm == 0) {
            return null;
        }

        Shuffle s = new Shuffle();
        int[][] shuffled_ranks = new int[original_ranks.length][];

        double[] eRP = new double[RP.length];
        double[] perm_RP = null;

        for (int i = 0; i < perm; i++) {
            for (int j = 0; j < original_ranks.length; j++) {
                shuffled_ranks[j] = s.permuteOrder(original_ranks[j], ran);
            }
            perm_RP = this.getProduct(shuffled_ranks);

            //Tell antall bedre            

            updateNumBetter(RP, perm_RP, eRP);

            pm.setProgress(i);
        }

        for (int i = 0; i < eRP.length; i++) {
            eRP[i] = eRP[i] / (double) perm;
        }
        return eRP;
    }

    public double[] permuteOneClass(double[] RP, int[][] original_ranks, int perm, java.util.Random ran) {
        if (RP == null || perm == 0) {
            return null;
        }

        Shuffle s = new Shuffle();
        int[][] shuffled_ranks = new int[original_ranks.length][];

        double[] eRP = new double[RP.length];
        double[] perm_RP = null;

        for (int i = 0; i < perm; i++) {
            for (int j = 0; j < original_ranks.length; j++) {
                shuffled_ranks[j] = s.permuteOrder(original_ranks[j], ran);
            }
            perm_RP = this.getProduct(shuffled_ranks);

            //Tell antall bedre            

            updateNumBetter(RP, perm_RP, eRP);
        }

        for (int i = 0; i < eRP.length; i++) {
            eRP[i] = eRP[i] / (double) perm;
        }
        return eRP;
    }

    //returns E(RP)
    public double[] permuteTwoClassUnpaired(double[] RP, double[][] data, int[] g1, int[] g2, boolean isLog, int perm, java.util.Random ran, ProgressMonitor pm) throws FoldException {
        if (RP == null || data == null || g1 == null || g2 == null) {
            return null;
        }

        Shuffle s = new Shuffle();


        double[][] dataTransposed = transpose(data);
        double[][] t_shuffledData = new double[dataTransposed.length][];
        double[][] shuffledData = null;

        double[] perm_RP = null;
        double[] eRP = new double[RP.length];

        try {

            for (int i = 0; i < perm; i++) {
                for (int j = 0; j < dataTransposed.length; j++) {
                    t_shuffledData[j] = s.permuteOrder(dataTransposed[j], ran);
                }
                shuffledData = transpose(t_shuffledData);

                perm_RP = this.getRankProductTwoClassUnpaired(shuffledData, g1, g2, isLog);

                //Tell antall bedre

                updateNumBetter(RP, perm_RP, eRP);

                pm.setProgress(i);
            }
        } catch (FoldException e) {
            throw e;
        }

        for (int i = 0; i < eRP.length; i++) {
            eRP[i] = eRP[i] / (double) perm;
        }
        return eRP;
    }

    public double[] permuteTwoClassUnpaired(double[] RP, double[][] data, int[] g1, int[] g2, boolean isLog, int perm) throws FoldException {
        if (RP == null || data == null || g1 == null || g2 == null) {
            return null;
        }

        Shuffle s = new Shuffle();
        java.util.Random ran = new java.util.Random(seed);

        double[][] dataTransposed = transpose(data);
        double[][] t_shuffledData = new double[dataTransposed.length][];
        double[][] shuffledData = null;

        double[] perm_RP = null;
        double[] eRP = new double[RP.length];

        try {

            for (int i = 0; i < perm; i++) {
                for (int j = 0; j < dataTransposed.length; j++) {
                    t_shuffledData[j] = s.permuteOrder(dataTransposed[j], ran);
                }
                shuffledData = transpose(t_shuffledData);

                perm_RP = this.getRankProductTwoClassUnpaired(shuffledData, g1, g2, isLog);

                //Tell antall bedre

                updateNumBetter(RP, perm_RP, eRP);
            }
        } catch (FoldException e) {
            throw e;
        }

        for (int i = 0; i < eRP.length; i++) {
            eRP[i] = eRP[i] / (double) perm;
        }
        return eRP;
    }

    public double[][] getRandomScores(double[][] data, int[] g1, int[] g2, boolean isLog, int perm, java.util.Random ran) throws FoldException {
        if (data == null || g1 == null || g2 == null || ran == null) {
            return null;
        }

        Shuffle s = new Shuffle();


        double[][] dataTransposed = transpose(data);
        double[][] t_shuffledData = new double[dataTransposed.length][];
        double[][] shuffledData = null;

        double[][] perm_RP = new double[perm][];

        try {

            for (int i = 0; i < perm; i++) {
                for (int j = 0; j < dataTransposed.length; j++) {
                    t_shuffledData[j] = s.permuteOrder(dataTransposed[j], ran);
                }
                shuffledData = transpose(t_shuffledData);

                perm_RP[i] = this.getRankProductTwoClassUnpaired(shuffledData, g1, g2, isLog);
//                if(pm.isCanceled()) return null;
//                pm.setProgress(i);
            }
        } catch (FoldException e) {
            throw e;
        }
        return perm_RP;
    }

    public double[][] getRandomScores(int[][] original_ranks, int perm, java.util.Random ran, ProgressMonitor pm) {
        if (original_ranks == null || perm == 0) {
            return null;
        }

        Shuffle s = new Shuffle();
        int[][] shuffled_ranks = new int[original_ranks.length][];
        double[][] perm_RP = new double[perm][];

        for (int i = 0; i < perm; i++) {
            for (int j = 0; j < original_ranks.length; j++) {
                shuffled_ranks[j] = s.permuteOrder(original_ranks[j], ran);
            }
            perm_RP[i] = this.getProduct(shuffled_ranks);

        }
        return perm_RP;
    }

    public double[] getExpRP(double[] rp, double[][] ranRP) {

        if (rp == null || ranRP == null) {
            return null;
        }

        double[] eRP = new double[rp.length];

        for (int i = 0; i < ranRP.length; i++) {
            updateNumBetter(rp, ranRP[i], eRP);
        }

        for (int i = 0; i < eRP.length; i++) {
            eRP[i] = eRP[i] / (double) ranRP.length;
        }

        return eRP;
    }

    //returns E(RP)
    public double[] permutePairedAnalysis(double[] RP, int[][] ranks, int perm, java.util.Random ran, ProgressMonitor pm) {
        return this.permuteOneClass(RP, ranks, perm, ran, pm);
    }

    public void updateNumBetter(double[] rp, double[] permRP, double[] eRP) {
        int n = eRP.length;
        int l = 0, r = n - 1, better = 0;
        int m = (l + r) / 2;

        java.util.Arrays.sort(permRP);

        for (int i = 0; i < rp.length; i++) {
            l = 0;
            r = n - 1;
            m = (l + r) / 2;
            better = 0;
            while (r - l > 1) {
                if (rp[i] > 0) {
                    if (permRP[m] > rp[i]) {
                        r = m;
                    } else {
                        l = m;
                    }
                    m = (l + r) / 2;
                } else if (rp[i] < 0) {
                    if (permRP[m] < rp[i]) {
                        r = m;
                    } else {
                        l = m;
                    }
                    m = (l + r) / 2;
                }

            }

            better = 0;

            if (permRP[n - 1] <= rp[i]) {
                better = n;
            } else if (permRP[0] > rp[i]) {
                better = 0;
            } else if (permRP[r] > rp[i]) {
                better = r;
            } else {
                better = r + 1;
            }

            eRP[i] += better;


        }
    }

    public double[] qval(double[] RP, double[] eRP) {//,boolean neg){

        int[] dataIndex = new int[RP.length];
        double[] RPcopy = new double[RP.length];
        double[] qval = new double[RP.length];
        double[] fdr = new double[RP.length];

        for (int g = 0; g < RP.length; g++) {
            dataIndex[g] = g;
            RPcopy[g] = RP[g];
        }

        //Sorting in Ascending order
        Stat.parallelSort(RPcopy, dataIndex);

        for (int i = 0; i < dataIndex.length; i++) {
            fdr[dataIndex[i]] = eRP[dataIndex[i]] / (double) (i + 1);
        }


        for (int i = dataIndex.length - 2; i >= 0; i--) {
            if (fdr[dataIndex[i]] > fdr[dataIndex[i + 1]]) {
                fdr[dataIndex[i]] = fdr[dataIndex[i + 1]];
            }
        }


        return fdr;

    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getSeed() {
        return seed;
    }
}
