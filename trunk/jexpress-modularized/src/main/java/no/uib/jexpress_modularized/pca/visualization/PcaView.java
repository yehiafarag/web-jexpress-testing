/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.jexpress_modularized.pca.visualization;

import java.io.Serializable;
import javax.swing.JFrame;
import no.uib.jexpress_modularized.core.dataset.Dataset;
import no.uib.jexpress_modularized.pca.computation.PcaResults;

/**
 *
 * @author Yehia Farag
 */
public class PcaView implements Serializable {

    private PCA2DComponent plot;
    private JFrame frame;

    public PCA2DComponent getPlot() {
        return plot;
    }

    public void setPlot(PCA2DComponent plot) {
        this.plot = plot;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public PcaView(PcaResults results, Dataset dataset) {
        plot = new PCA2DComponent(results, dataset);
        PCAControlPanel panel = new PCAControlPanel(plot, results);
        frame = new JFrame();
        frame.setTitle("PCA");
        frame.setSize(1000, 640);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }
}
