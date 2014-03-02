/*
 * classBars.java
 *
 * Created on 7. juli 2005, 10:40
 */
package no.uib.jexpress_modularized.core.visualization.charts;

//import Groups.classification;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.Vector;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author bjarte dysvik
 */
public class jfreeLineChart extends JPanel implements Serializable{

    private Vector classifications = new Vector();
    private Color[] groupColors;
    //This is the name of the classificators (genes)
    private String[] groupNames = new String[]{"class1", "class2", "class3", "class4"};
    private JFreeChart chart;
    private boolean legend = true;
    private boolean shapes = true;

    public JFreeChart getChart() {
        return chart;
    }

    public void createSample() {
        // @TODO: reimplement me!
//        classification c1 = new classification();
//        c1.actualClass="class1";
//        c1.classifications = new double[]{1.0,1.0,2.0,1.0};
//        c1.correctlyClassified=true;
//        getClassifications().addElement(c1);
//        
//        c1 = new classification();
//        c1.actualClass="class2";
//        c1.classifications = new double[]{1.5,1.3,2.0,1.7};
//        c1.correctlyClassified=true;
//        getClassifications().addElement(c1);
//        
//        c1 = new classification();
//        c1.actualClass="class3";
//        c1.classifications = new double[]{1.5,1.3,2.0,1.7};
//        c1.correctlyClassified=true;
//        getClassifications().addElement(c1);
        // setGroupColors(new Color[]{Color.red,Color.green,Color.blue,Color.yellow});
    }

    //the highest probability..
    public double getAllMax() {
        double ret = -1;
        // @TODO: reimplement me!
//        classification cl = null;
//        
//        for(int i=0;i<getClassifications().size();i++){
//            cl=(classification)getClassifications().elementAt(i);
//            for(int j=0;j<cl.classifications.length;j++){
//                if(ret<cl.classifications[j])ret = cl.classifications[j];
//            }
//        }
        return ret;
    }

    public Color createRandomColor() {
        return new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0), (int) (Math.random() * 255.0));
    }

    public void resetChart(String[] columns, String[] rows, double[][] data) {

        CategoryDataset dataset = createDataSet(columns, rows, data);
        chart = createChart(dataset);
        if (classifications.size() == 0) {
            return;
        }
        // @TODO: reimplement me!
        //setPreferredSize(new Dimension(Math.max(300, (classifications.size()*6)+(classifications.size()*((classification) classifications.elementAt(0)).classifications.length*8)),170));
    }

    public jfreeLineChart() {
        createSample();
        // @TODO: reimplement me!
        //setPreferredSize(new Dimension(Math.max(300, (getClassifications().size()*6)+(getClassifications().size()*((classification) getClassifications().elementAt(0)).classifications.length*8)),170));
        CategoryDataset dataset = createDataSet(null, null, null);
        chart = createChart(dataset);

        setLayout(new BorderLayout());
        this.add("Center", new ChartPanel(chart));
    }

    /**
     * Creates a new instance of classBars
     */
    public jfreeLineChart(String[] columns, String[] rows, double[][] data) {
        createSample();
        // @TODO: reimplement me!
        //setPreferredSize(new Dimension(Math.max(300, (getClassifications().size()*6)+(getClassifications().size()*((classification) getClassifications().elementAt(0)).classifications.length*8)),170));
        CategoryDataset dataset = createDataSet(columns, rows, data);
        chart = createChart(dataset);

        setLayout(new BorderLayout());
        this.add("Center", new ChartPanel(chart));
    }

    private JFreeChart createChart(CategoryDataset dataset) {

        // create the chart...
        JFreeChart chart = ChartFactory.createLineChart(
                "", // chart title
                "", // domain axis label
                "", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                legend, // include legend
                true, // tooltips?
                false // URLs?
                );



        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();


        chart.setBackgroundPaint(Color.white);
        plot.setBackgroundPaint(new Color(245, 245, 250));

        plot.setDomainGridlinesVisible(true);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        if (groupColors != null) {
            for (int i = 0; i < groupColors.length; i++) {
                renderer.setSeriesPaint(i, groupColors[i]);
            }
        }

        renderer.setBaseShapesVisible(shapes);

        if (chart.getLegend() != null) {
            chart.getLegend().setPosition(RectangleEdge.RIGHT);
        }
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

        return chart;

    }

    public void setShapeVisible(boolean shapes) {
        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(shapes);
        this.shapes = shapes;
    }

    public void setLegendVisible(boolean vis) {
        chart.getLegend().setVisible(vis);
        legend = vis;
    }

    public CategoryDataset createDataSet(String[] columns, String[] rows, double[][] data) {

        DefaultCategoryDataset ret = new DefaultCategoryDataset();

        if (data == null || columns == null || rows == null) {
            return ret;
        }

        //classification c1 = null; // @TODO: reimplement me?

        if (data == null) {
            return ret;
        }

        // @TODO: reimplement me!
//        JcategoryID[] colids = new JcategoryID[columns.length];
//        JcategoryID[] rowids = new JcategoryID[rows.length];
//
//        for (int i = 0; i < rows.length; i++) {
//            rowids[i] = new JcategoryID(new Integer(i), rows[i]);
//        }
//        for (int i = 0; i < columns.length; i++) {
//            //colids[i]=columns[i];
//            colids[i] = new JcategoryID(new Integer(i), columns[i]);
//        }
//
//        for (int i = 0; i < data.length; i++) {
//            for (int j = 0; j < data[i].length; j++) {
//                ret.addValue(data[i][j], rowids[i], colids[j]);
//            }
//        }


        return ret;
    }

    public static void main(String[] args) {
//        jfreeLineChart cla = new jfreeLineChart();
//        try{
//            javax.swing.UIManager.setLookAndFeel("expresscomponents.UI.JELookAndFeel");
//        } catch(Exception e){}
//        JFrame f = new JFrame();
//        f.getContentPane().add(cla);
//        f.setResizable(true);
//        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
//        //expresscomponents.DataSet data = jexpress.cluster.loadDataSet("resources/mindre.cfg","Yeast Elu");//"alpha1-18_2");//"nci60_fig3_nomiss.txt","Baldwin_alltimecourses_nomiss.txt","nci60_fig3_nomiss.txt","Alizadeh_65col_nomiss.txt"
//        f.setSize(300,200);
//        f.pack();
//        f.show();
    }

    public Vector getClassifications() {
        return classifications;
    }

    public void setClassifications(Vector classifications) {
        this.classifications = classifications;
    }

    public Color[] getGroupColors() {
        return groupColors;
    }

    public void setGroupColors(Color[] groupColors) {
        this.groupColors = groupColors;
    }

    public String[] getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String[] groupNames) {
        this.groupNames = groupNames;
    }
}
