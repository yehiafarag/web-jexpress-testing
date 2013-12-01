/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.jexpress.server.model;

import java.util.ArrayList;
import no.uib.jexpress_modularized.rank.computation.RPmodel;
import web.jexpress.shared.beans.RankResult;

/**
 *
 * @author Y.M
 */
public class RankUtil {
    
    public RankResult handelRankTable( ArrayList<RPmodel> jResults)
    {

        RankResult results = new RankResult();
        results = this.initResults(results, jResults.get(0), 0);
        results = this.initResults(results, jResults.get(1), 1);
        return results;
    }

    private RankResult initResults(RankResult res, RPmodel table, int postive) {
        int[] rankToIndex = new int[table.getRowCount()];
        int[] indexToRank = new int[table.getRowCount()];
        String[] tableData[] = new String[table.getColumnCount()][table.getRowCount()];
        String[] headers = new String[table.getColumnCount()];
        for (int x = 0; x < table.getColumnCount(); x++) {
            headers[x] = table.getColumnName(x);
            String[] col = new String[table.getRowCount()];
            for (int y = 0; y < table.getRowCount(); y++) {
                col[y] = table.getValueAt(y, x).toString();
            }
            tableData[x] = col;
        }

        for (int x = 0; x < table.getRowCount(); x++) {
            rankToIndex[x] = table.getSortMap().get(x + 1);
        }
        for (int x = 0; x < table.getRowCount(); x++) {
            indexToRank[rankToIndex[x]] = x + 1;
        }

        if (postive == 0) {
            res.setPosTableHeader(headers);
            res.setPosTableData(tableData);
            res.setPosIndexToRank(indexToRank);
            res.setPosRankToIndex(rankToIndex);
        } else {
            res.setNegTableHeader(headers);
            res.setNegTableData(tableData);
            res.setNegIndexToRank(indexToRank);
            res.setNegRankToIndex(rankToIndex);
        }

        return res;




    }
}
