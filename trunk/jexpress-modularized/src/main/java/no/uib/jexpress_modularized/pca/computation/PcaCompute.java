/*
 * this class is responsable for PCA compute
 */
package no.uib.jexpress_modularized.pca.computation;

import java.io.Serializable;
import no.uib.jexpress_modularized.core.dataset.Dataset;

/**
 *
 * @author Harald Barsnes
 * @author Yehia Farag
 */
public class PcaCompute implements Serializable {

    private Dataset data;
    private PcaResults pca;

    public PcaCompute(Dataset data) {
        this.data = data;
    }

    public PcaResults createPCA() {

        pca = new PcaResults(data.getData());

        return pca;

    }
}
