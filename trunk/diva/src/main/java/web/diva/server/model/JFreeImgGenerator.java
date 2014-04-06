/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.diva.server.model;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Yehia Farag
 */
public class JFreeImgGenerator {

    public synchronized String saveToFile(JFreeChart chart, double width, double height, String path, ChartRenderingInfo chartRenderingInfo, String imgName) {
        try {
            File f = new File(path, imgName + ".png");
            if (!f.exists()) {
                f.createNewFile();
            }
            try {
                ChartUtilities.saveChartAsPNG(f, chart,(int) width,(int) height, chartRenderingInfo);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            FileInputStream imageInFile = new FileInputStream(f);
            byte imageData[] = new byte[(int) f.length()];
            imageInFile.read(imageData);
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            f.delete();
            return base64;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return "";

    }

    public Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

}
