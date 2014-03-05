/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import no.uib.jexpress_modularized.core.dataset.Dataset;

/**
 *
 * @author Y.M
 */
public class FilesReader implements Serializable{
    
    public Dataset readDataset(File file)
    {
       
        if(!file.exists()||(!file.getName().endsWith(".txt")))
            return null;
        String fileName  = file.getName().split("\\.")[0].toUpperCase();
        Dataset dataset ;
        
        
        try{
         FileReader fr = new FileReader(file);
            BufferedReader bufRdr  = new BufferedReader(fr);
            String line = "";
            int index = 0;
            String info = "";
            int column = 0;
            ArrayList<String> rowNameList = new ArrayList<String>();
            ArrayList<String> columnNameList = new ArrayList<String>();
            Map<Integer,double[]>geneMap = new TreeMap<Integer,double[]>();
              while((line = bufRdr.readLine()) != null )
		{
                    line = line.toUpperCase();
                    
                    String[] strArr = line.trim().split("\t");
                    if(index==0)
                    {
                        info = strArr[0].trim();
                        
                        String col="";
//                        int tag =0;
                        for(int z =1;z<strArr.length;z++)
                        {
                            String s= strArr[z];
//                            if(tag < 2)
//                            {
                             col = col+s;
//                             tag++;
//                            }
//                            if(tag==2){
                            columnNameList.add(col.trim());
//                            tag = 0;
                            col ="";
//                            }
                           
                            
                          
                        }
                        rowNameList.add(info);
                        index++;
                        continue;
                    }
                    double[] raw = new double[columnNameList.size()];
                    for(int x = 0; x<raw.length;x++){
                        try{
                        raw[x] = Double.valueOf(strArr[x+1]);
                       
                        }catch(NumberFormatException nfx){
                            raw[x] =0.0;
                        }catch(ArrayIndexOutOfBoundsException exp){
                            raw[x] =0.0;
                        
                        }
                    }
                    column = strArr.length-1;
                   geneMap.put((index-1), raw);
                   rowNameList.add(strArr[0]);
                    index++;
                }
            
             double[][] data= new double[geneMap.size()][column];
             ///here we  select the rowGrups for eazy work
             ArrayList<Integer> selectionRowsA = new ArrayList<Integer>();
              ArrayList<Integer> selectionRowsB = new ArrayList<Integer>();
             
             for(int i=0;i<geneMap.size();i++)
             {
                 double[] row= geneMap.get(i);
                 if(row[row.length-1] <0)
                     selectionRowsA.add(i);
                 else if(row[row.length-1] >0)
                     selectionRowsB.add(i);
//                 for(int j=0;j<row.length;j++)
//                 {
                     data[i]=row;
                 
//                 }                            
             }
             String[]rowIds = new String[rowNameList.size()-1];
             for(int i=1;i<rowNameList.size();i++)
             {
                 rowIds[i-1] =rowNameList.get(i);                 
             }
              String[]columnsIds = new String[columnNameList.size()];
             for(int i=0;i<columnNameList.size();i++)
             {
                 columnsIds[i] =columnNameList.get(i);                 
             }
             dataset = new Dataset(data, rowIds, columnsIds);
             boolean[][] nulls = new boolean [geneMap.size()][columnNameList.size()];
            for (int i = 0; i < geneMap.size(); i++) {
                for (int j = 0; j < columnNameList.size(); j++) {
                    if(data[i][j]== 0.0)
                        nulls[i][j] = true;
                    else
                        nulls[i][j] = false;
                }
            }
             
             dataset.setMissingMeasurements(nulls);
             dataset.addRowAnnotationNameInUse(info);
             dataset.setName(fileName);
             
          //    System.out.println("raw size is "+rowIds.length+"   info "+info+"  colomn "+columnsIds.length+"  --columnname "+columnNameList);
           //  System.out.println("rawsnames "+rowNameList);
            
             
            // dataset = DataSet.newDataSet(data, nulls);
             
             
             ///add the groups
//              int[] selectionRowsAArr = new int[selectionRowsA.size()] ;
//              for(int c=0;c<selectionRowsA.size();c++)
//              {
//                  selectionRowsAArr[c] = selectionRowsA.get(c);
//              }
//              int[] selectionRowsBArr = new int[selectionRowsB.size()] ;
//              for(int c=0;c<selectionRowsB.size();c++)
//              {
//                  selectionRowsBArr[c] = selectionRowsB.get(c);
//              }
//              Group g1 = new Group("test group rows A", new Color(180,49,4), new Selection(Selection.TYPE.OF_ROWS, selectionRowsAArr));
//              g1.setActive(true);
//              dataset.addRowGroup(g1);
//              Group g2 = new Group("test group rows B", new Color(4,180,49), new Selection(Selection.TYPE.OF_ROWS, selectionRowsBArr));
//              g2.setActive(true);              
//              dataset.addRowGroup(g2);
//              
//              int[] selectionCol = {0,1,2,3};                
//              dataset.addColumnGroup(new Group("test group columns A", Color.CYAN, new Selection(Selection.TYPE.OF_COLUMNS, selectionCol)));
//                int[] selectionCol2 = new int[]{4,5,6};
//              dataset.addColumnGroup(new Group("test group columns B", Color.orange, new Selection(Selection.TYPE.OF_COLUMNS, selectionCol2)));

//                
             
        
             return dataset;
        }catch(Exception e){e.printStackTrace();}

    
        return null;
    }
    
}
