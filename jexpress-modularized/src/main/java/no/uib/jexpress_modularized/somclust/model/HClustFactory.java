/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.somclust.model;

import java.io.Serializable;
import no.uib.jexpress_modularized.somclust.model.ClusterParameters.LINKAGE;

/**
 *
 * @author bjarte
 */
public class HClustFactory implements Serializable {

    private Node[] gendata;
    private boolean[] stepovergen;
    private int numgenes;
    private int totgenes;
    private int[] sortpan;
    private float[] sortpan2;
    private int minarrayupdates;
    private LINKAGE LINK = LINKAGE.WPGMA;

    public Node makeclusters(float[][] distance, int[] indices, LINKAGE link) {
        long diff = 0l;
        LINK = link;
        Node ret = null;
        gendata = buildmatrix(distance, indices);
        totgenes = numgenes;
        makeminarray(distance);

        int linkage = 0;
        if (LINK == LINKAGE.SINGLE) {
            linkage = 0;
        }
        if (LINK == LINKAGE.COMPLETE) {
            linkage = 3;
        }
        if (LINK == LINKAGE.WPGMA) {
            linkage = 1;
        }
        if (LINK == LINKAGE.UPGMA) {
            linkage = 2;
        }

        while (numgenes > 1) {
            //This one takes time!
            gendata = makeclusters(gendata, distance, linkage);

        }

        for (int i = 0; i < gendata.length; i++) {
            if (!stepovergen[i]) {
                ret = gendata[i];
            }
        }
        ret.normalizeMergedValues();

        return ret;
    }

    private Node[] buildmatrix(float[][] dist, int[] indices) {
        Node[] gene = new Node[dist.length];

        stepovergen = new boolean[dist.length];
        numgenes = dist.length;
        for (int i = 0; i < dist.length; i++) {
            gene[i] = new Node(i);
            gene[i].nme = indices[i];
        }

        // Set up the sorting matrix
        sortpan = new int[dist.length];
        sortpan2 = new float[dist.length];
        return gene;
    }

    private void makeminarray(float[][] dist) {
        float minst = 999999;
        int minstpeker = 0;
        for (int i = 0; i < dist.length; i++) {
            minst = 999999;

            for (int j = 0; j < i; j++) {
                if (!stepovergen[i] && !stepovergen[j]) {
                    if (dist[i][j] < minst) {
                        minst = dist[i][j];
                        minstpeker = j;
                    }
                }
            }
            sortpan[i] = minstpeker;
            sortpan2[i] = minst;

        }
    }

    private void updateminarray(int row, float[][] dist) {
        float minst = 9999999f;
        int minstpeker = 0;

        minst = 9999999;

        for (int j = 0; j < row; j++) {
            if (!stepovergen[j]) {
                if (dist[row][j] < minst) {
                    minst = dist[row][j];
                    minstpeker = j;
                }
            }
        }
        // }
        sortpan[row] = minstpeker;
        sortpan2[row] = minst;
        minarrayupdates++;
    }

    private Node[] makeclusters(Node[] data, float[][] dist, int linkage) {
       
        int minst1 = 0;
        int minst2 = 0;      
        float minst = 99999999999999f;
        for (int i = 0; i < sortpan.length; i++) {
            if (!stepovergen[i] && !stepovergen[sortpan[i]]) {
                if (sortpan2[i] < minst) {
                    minst = sortpan2[i];
                    minst1 = i;
                    minst2 = sortpan[i];
                }
            }
        }

        stepovergen[minst1] = true;
        numgenes--;

        int minst1members = data[minst1].members;
        int minst2members = data[minst2].members;

        data[minst2] = new Node(data[minst2], data[minst1], minst);// Dette er for avstanden i mellom dem

        for (int j = 0; j < dist[minst2].length; j++) {
            if (!stepovergen[j]) {
                if (linkage == 3) {
                    dist[minst2][j] = Math.max(dist[minst2][j], dist[minst1][j]);
                } else if (linkage == 0) {
                    dist[minst2][j] = Math.min(dist[minst2][j], dist[minst1][j]);
                } else if (linkage == 1) {
                    dist[minst2][j] = (dist[minst2][j] + dist[minst1][j]) / 2;
                } else if (linkage == 2) {
                    dist[minst2][j] = ((dist[minst2][j] * minst2members) + (dist[minst1][j] * minst1members)) / (minst2members + minst1members);
                }

            }
        }

        updateminarray(minst2, dist);

        for (int j = minst2 + 1; j < dist.length; j++) {
            // lengden paa dist[minst1] vil vaere like lang som denne kolonnen
            // Begynner paa cellen under diagonalen. Derfor minst2+1

            if (!stepovergen[j]) {

                if (j < minst1) {
                    if (linkage == 3) {
                        dist[j][minst2] = Math.max(dist[j][minst2], dist[minst1][j]);
                    } else if (linkage == 0) {
                        dist[j][minst2] = Math.min(dist[j][minst2], dist[minst1][j]);
                    } else if (linkage == 1) {
                        dist[j][minst2] = (dist[j][minst2] + dist[minst1][j]) / 2;
                    } else if (linkage == 2) {
                        dist[j][minst2] = ((dist[j][minst2] * minst2members) + (dist[minst1][j] * minst1members)) / (minst2members + minst1members);
                    }
                } else if (j > minst1) {
                    if (linkage == 3) {
                        dist[j][minst2] = Math.max(dist[j][minst2], dist[j][minst1]);
                    } else if (linkage == 0) {
                        dist[j][minst2] = Math.min(dist[j][minst2], dist[j][minst1]);
                        
                    }
                    else if (linkage == 1) {
                        dist[j][minst2] = (dist[j][minst2] + dist[j][minst1]) / 2;
                    } else if (linkage == 2) {
                        dist[j][minst2] = ((dist[j][minst2] * minst2members) + (dist[j][minst1] * minst1members)) / (minst2members + minst1members);
                    }
                }
            }

            if (dist[j][minst2] < sortpan2[j]) {
                sortpan2[j] = dist[j][minst2];
                sortpan[j] = minst2;
            } else if (sortpan[j] == minst1) {
                updateminarray(j, dist);
            } else if (sortpan[j] == minst2) {
                updateminarray(j, dist);
            }
        }
        return data;
    }
}
