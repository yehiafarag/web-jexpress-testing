/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.client.somclust.view;

import org.thechiselgroup.choosel.protovis.client.Link;
import org.thechiselgroup.choosel.protovis.client.PVNodeAdapter;

/**
 *
 * @author Y.M
 *//**
 * Miserables data set from protovis examples.
 * 
 * @see http://vis.stanford.edu/protovis/ex/miserables.js
 */
public class HeatmapData {

    public static class HeatmapNodeAdapter implements
            PVNodeAdapter<HeatmapCell> {
       public int getNodeGroup(HeatmapCell t)
       {
           return t.getGroup();
       }

        public String getNodeName(HeatmapCell t) {
            return t.getNodeName();
        }

        public Object getNodeValue(HeatmapCell t) {            
            return null;
        }
    }

    public static class HeatmapCell {

        private String nodeName;

        private int group;


       
        public HeatmapCell(String nodeName, int group) {
            this.nodeName = nodeName;
            this.group = group;
        }

        

        public int getGroup() {
            return group;
        }

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

    }
    public final static HeatmapCell[] CELLS = new HeatmapCell[]{
        new HeatmapCell("Myriel", 1),
        new HeatmapCell("Napoleon", 1),
        new HeatmapCell("Myriel", 1), new HeatmapCell("Mlle. Baptistine", 1),
        new HeatmapCell("Mme. Magloire", 1), new HeatmapCell("Countess de Lo", 1),
        new HeatmapCell("Geborand", 1), new HeatmapCell("Champtercier", 1),
        new HeatmapCell("Cravatte", 1), new HeatmapCell("Count", 1), new HeatmapCell("Old Man", 1),
        new HeatmapCell("Labarre", 2), new HeatmapCell("Valjean", 2),
        new HeatmapCell("Marguerite", 3), new HeatmapCell("Mme. de R", 2),
        new HeatmapCell("Isabeau", 1),
        new HeatmapCell("Gervais", 2), new HeatmapCell("Tholomyes", 3),
        new HeatmapCell("Listolier", 3), new HeatmapCell("Fameuil", 3),
        new HeatmapCell("Blacheville", 3), new HeatmapCell("Favourite", 3),
        new HeatmapCell("Dahlia", 3),
        new HeatmapCell("Fantine", 3), new HeatmapCell("Zephine", 3),
        new HeatmapCell("Mme. Thenardier", 4), new HeatmapCell("Thenardier", 4),
        new HeatmapCell("Cosette", 5), new HeatmapCell("Javert", 4),
        new HeatmapCell("Fauchelevent", 0), new HeatmapCell("Bamatabois", 2),
        new HeatmapCell("Perpetue", 3), new HeatmapCell("Simplice", 2),
        new HeatmapCell("Scaufflaire", 2), new HeatmapCell("Woman 1", 2),
        new HeatmapCell("Judge", 2), new HeatmapCell("Champmathieu", 2),
        new HeatmapCell("Brevet", 2), new HeatmapCell("Chenildieu", 2),
        new HeatmapCell("Cochepaille", 2), new HeatmapCell("Pontmercy", 4),
        new HeatmapCell("Boulatruelle", 6), new HeatmapCell("Eponine", 4),
        new HeatmapCell("Anzelma", 4), new HeatmapCell("Woman 2", 5),
        new HeatmapCell("Mother Innocent", 0), new HeatmapCell("Gribier", 0),
        new HeatmapCell("Jondrette", 7), new HeatmapCell("Mme. Burgon", 7),
        new HeatmapCell("Gavroche", 8), new HeatmapCell("Gillenormand", 5),
        new HeatmapCell("Magnon", 5), new HeatmapCell("Mlle. Gillenormand", 5),
        new HeatmapCell("Marius", 8), new HeatmapCell("Mme. Pontmercy", 5),
        new HeatmapCell("Mlle. Vaubois", 5), new HeatmapCell("Lt. Gillenormand", 5),
        new HeatmapCell("Baroness T", 5),
        new HeatmapCell("Mabeuf", 8),
        new HeatmapCell("Enjolras", 8),
        new HeatmapCell("Combeferre", 8),
        new HeatmapCell("Prouvaire", 8),
        new HeatmapCell("Feuilly", 8),
        new HeatmapCell("Courfeyrac", 8), new HeatmapCell("Bahorel", 8),
        new HeatmapCell("Bossuet", 8), new HeatmapCell("Joly", 8),
        new HeatmapCell("Grantaire", 8), new HeatmapCell("Mother Plutarch", 9), new HeatmapCell("Gueulemer", 4),
        new HeatmapCell("Babet", 4),
        new HeatmapCell("Claquesous", 4),
        new HeatmapCell("Montparnasse", 4), new HeatmapCell("Toussaint", 5),
        new HeatmapCell("Child 1", 10),
        new HeatmapCell("Child 2", 10),
        new HeatmapCell("Brujon", 4), new HeatmapCell("Mme. Hucheloup", 8)
    };
    public static final Link[] LINKS = new Link[]{
        new Link(1, 0, 1), new Link(2, 0, 8), new Link(3, 0, 10), new Link(3, 2, 6),
        new Link(4, 0, 1), new Link(5, 0, 1), new Link(6, 0, 1), new Link(7, 0, 1),
        new Link(8, 0, 2), new Link(9, 0, 1), new Link(11, 10, 1), new Link(11, 3, 3),
        new Link(11, 2, 3), new Link(11, 0, 5), new Link(12, 11, 1), new Link(13, 11, 1),
        new Link(14, 11, 1), new Link(15, 11, 1), new Link(17, 16, 4), new Link(18, 16, 4),
        new Link(18, 17, 4), new Link(19, 16, 4), new Link(19, 17, 4), new Link(19, 18, 4),
        new Link(20, 16, 3), new Link(20, 17, 3), new Link(20, 18, 3), new Link(20, 19, 4),
        new Link(21, 16, 3), new Link(21, 17, 3), new Link(21, 18, 3), new Link(21, 19, 3),
        new Link(21, 20, 5), new Link(22, 16, 3), new Link(22, 17, 3), new Link(22, 18, 3),
        new Link(22, 19, 3), new Link(22, 20, 4), new Link(22, 21, 4), new Link(23, 16, 3),
        new Link(23, 17, 3), new Link(23, 18, 3), new Link(23, 19, 3), new Link(23, 20, 4),
        new Link(23, 21, 4), new Link(23, 22, 4), new Link(23, 12, 2), new Link(23, 11, 9),
        new Link(24, 23, 2), new Link(24, 11, 7), new Link(25, 24, 13), new Link(25, 23, 1),
        new Link(25, 11, 12), new Link(26, 24, 4)};
}

/*

 {source:26, target:11, value:31},
 {source:26, target:16, value:1},
 {source:26, target:25, value:1},
 {source:27, target:11, value:17},
 {source:27, target:23, value:5},
 {source:27, target:25, value:5},
 {source:27, target:24, value:1},
 {source:27, target:26, value:1},
 {source:28, target:11, value:8},
 {source:28, target:27, value:1},
 {source:29, target:23, value:1},
 {source:29, target:27, value:1},
 {source:29, target:11, value:2},
 {source:30, target:23, value:1},
 {source:31, target:30, value:2},
 {source:31, target:11, value:3},
 {source:31, target:23, value:2},
 {source:31, target:27, value:1},
 {source:32, target:11, value:1},
 {source:33, target:11, value:2},
 {source:33, target:27, value:1},
 {source:34, target:11, value:3},
 {source:34, target:29, value:2},
 {source:35, target:11, value:3},
 {source:35, target:34, value:3},
 {source:35, target:29, value:2},
 {source:36, target:34, value:2},
 {source:36, target:35, value:2},
 {source:36, target:11, value:2},
 {source:36, target:29, value:1},
 {source:37, target:34, value:2},
 {source:37, target:35, value:2},
 {source:37, target:36, value:2},
 {source:37, target:11, value:2},
 {source:37, target:29, value:1},
 {source:38, target:34, value:2},
 {source:38, target:35, value:2},
 {source:38, target:36, value:2},
 {source:38, target:37, value:2},
 {source:38, target:11, value:2},
 {source:38, target:29, value:1},
 {source:39, target:25, value:1},
 {source:40, target:25, value:1},
 {source:41, target:24, value:2},
 {source:41, target:25, value:3},
 {source:42, target:41, value:2},
 {source:42, target:25, value:2},
 {source:42, target:24, value:1},
 {source:43, target:11, value:3},
 {source:43, target:26, value:1},
 {source:43, target:27, value:1},
 {source:44, target:28, value:3},
 {source:44, target:11, value:1},
 {source:45, target:28, value:2},
 {source:47, target:46, value:1},
 {source:48, target:47, value:2},
 {source:48, target:25, value:1},
 {source:48, target:27, value:1},
 {source:48, target:11, value:1},
 {source:49, target:26, value:3},
 {source:49, target:11, value:2},
 {source:50, target:49, value:1},
 {source:50, target:24, value:1},
 {source:51, target:49, value:9},
 {source:51, target:26, value:2},
 {source:51, target:11, value:2},
 {source:52, target:51, value:1},
 {source:52, target:39, value:1},
 {source:53, target:51, value:1},
 {source:54, target:51, value:2},
 {source:54, target:49, value:1},
 {source:54, target:26, value:1},
 {source:55, target:51, value:6},
 {source:55, target:49, value:12},
 {source:55, target:39, value:1},
 {source:55, target:54, value:1},
 {source:55, target:26, value:21},
 {source:55, target:11, value:19},
 {source:55, target:16, value:1},
 {source:55, target:25, value:2},
 {source:55, target:41, value:5},
 {source:55, target:48, value:4},
 {source:56, target:49, value:1},
 {source:56, target:55, value:1},
 {source:57, target:55, value:1},
 {source:57, target:41, value:1},
 {source:57, target:48, value:1},
 {source:58, target:55, value:7},
 {source:58, target:48, value:7},
 {source:58, target:27, value:6},
 {source:58, target:57, value:1},
 {source:58, target:11, value:4},
 {source:59, target:58, value:15},
 {source:59, target:55, value:5},
 {source:59, target:48, value:6},
 {source:59, target:57, value:2},
 {source:60, target:48, value:1},
 {source:60, target:58, value:4},
 {source:60, target:59, value:2},
 {source:61, target:48, value:2},
 {source:61, target:58, value:6},
 {source:61, target:60, value:2},
 {source:61, target:59, value:5},
 {source:61, target:57, value:1},
 {source:61, target:55, value:1},
 {source:62, target:55, value:9},
 {source:62, target:58, value:17},
 {source:62, target:59, value:13},
 {source:62, target:48, value:7},
 {source:62, target:57, value:2},
 {source:62, target:41, value:1},
 {source:62, target:61, value:6},
 {source:62, target:60, value:3},
 {source:63, target:59, value:5},
 {source:63, target:48, value:5},
 {source:63, target:62, value:6},
 {source:63, target:57, value:2},
 {source:63, target:58, value:4},
 {source:63, target:61, value:3},
    {source:63, target:60, value:2},
    {source:63, target:55, value:1},
    {source:64, target:55, value:5},
    {source:64, target:62, value:12},
    {source:64, target:48, value:5},
    {source:64, target:63, value:4},
    {source:64, target:58, value:10},
    {source:64, target:61, value:6},
    {source:64, target:60, value:2},
    {source:64, target:59, value:9},
    {source:64, target:57, value:1},
    {source:64, target:11, value:1},
    {source:65, target:63, value:5},
    {source:65, target:64, value:7},
    {source:65, target:48, value:3},
    {source:65, target:62, value:5},
    {source:65, target:58, value:5},
    {source:65, target:61, value:5},
    {source:65, target:60, value:2},
    {source:65, target:59, value:5},
    {source:65, target:57, value:1},
    {source:65, target:55, value:2},
    {source:66, target:64, value:3},
    {source:66, target:58, value:3},
    {source:66, target:59, value:1},
    {source:66, target:62, value:2},
    {source:66, target:65, value:2},
    {source:66, target:48, value:1},
    {source:66, target:63, value:1},
    {source:66, target:61, value:1},
    {source:66, target:60, value:1},
    {source:67, target:57, value:3},
    {source:68, target:25, value:5},
    {source:68, target:11, value:1},
    {source:68, target:24, value:1},
    {source:68, target:27, value:1},
    {source:68, target:48, value:1},
    {source:68, target:41, value:1},
    {source:69, target:25, value:6},
    {source:69, target:68, value:6},
    {source:69, target:11, value:1},
    {source:69, target:24, value:1},
    {source:69, target:27, value:2},
    {source:69, target:48, value:1},
    {source:69, target:41, value:1},
    {source:70, target:25, value:4},
    {source:70, target:69, value:4},
    {source:70, target:68, value:4},
    {source:70, target:11, value:1},
    {source:70, target:24, value:1},
    {source:70, target:27, value:1},
    {source:70, target:41, value:1},
    {source:70, target:58, value:1},
    {source:71, target:27, value:1},
    {source:71, target:69, value:2},
    {source:71, target:68, value:2},
    {source:71, target:70, value:2},
    {source:71, target:11, value:1},
    {source:71, target:48, value:1},
    {source:71, target:41, value:1},
    {source:71, target:25, value:1},
    {source:72, target:26, value:2},
    {source:72, target:27, value:1},
    {source:72, target:11, value:1},
    {source:73, target:48, value:2},
    {source:74, target:48, value:2},
    {source:74, target:73, value:3},
    {source:75, target:69, value:3},
    {source:75, target:68, value:3},
    {source:75, target:25, value:3},
    {source:75, target:48, value:1},
    {source:75, target:41, value:1},
    {source:75, target:70, value:1},
    {source:75, target:71, value:1},
    {source:76, target:64, value:1},
    {source:76, target:65, value:1},
    {source:76, target:66, value:1},
    {source:76, target:63, value:1},
    {source:76, target:62, value:1},
    {source:76, target:48, value:1},
    {source:76, target:58, value:1}
    * 
    * */
