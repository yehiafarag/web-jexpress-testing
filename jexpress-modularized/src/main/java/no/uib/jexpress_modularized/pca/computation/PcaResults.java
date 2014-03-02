/*--------------------------------------------------------------------------

 *

 * Copyright (c) 2001 MolMine AS.  All rights reserved.

 *

 * All paper, computer, digital, graphical, or other representations of the source code remain

 * the property of MolMine AS. All patents, ideas, inventions or novelties contained within the

 * source code are the exclusive intellectual property of MolMine AS. Surce code is provided 

 * for reference and support purposes only. Copies of the source code in any form, whether this

 * be digital, graphical or any other media, may not be distributed, discussed, or revealed to 

 * any person, computer or organisation not directly involved in support of a related product 

 * provided by the licensee or organisation not authorzed by MolMine AS to be directly involved 

 * in source code level support of J-Express.

 

 * The source code may not be modified except where specifically authorized by MolMine AS. No 

 * part of the source code may be used  within any product other than J-Express.

 *

 * You undertake that:

 *  The source code will not be distributed except where specifical authorized by MolMine AS.

 *  That you will ensure that all copies and representations of the source code can be identified.

 *

 * DISCLAIMER:

 * THIS SOFTWARE IS PROVIDED BY MOLMINE AS "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, 

 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS

 * FOR A PARTICULAR PURPOSE  ARE DISCLAIMED.  IN NO EVENT SHALL MOLMINE OR ITS CONTRIBUTORS BE 

 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 

 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 

 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,

 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 

 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 *

 *---------------------------------------------------------------------------

 */
package no.uib.jexpress_modularized.pca.computation;

import java.io.Serializable;
import no.uib.jexpress_modularized.pca.computation.Jama.EigenvalueDecomposition;
import no.uib.jexpress_modularized.pca.computation.Jama.Matrix;

public class PcaResults implements Serializable {

    public double[] eigenvalues;
    public double[][] projection;
    public double[][] eigenvectors;

    public PcaResults(double[] eigenvalues, double[][] eigenvectors, double[][] data) {

        this.eigenvalues = eigenvalues;
        this.eigenvectors = eigenvectors;

        Matrix A = new Matrix(data);
        Matrix B = new Matrix(eigenvectors);

        A = A.times(B);

        projection = A.getArray();
    }

    public PcaResults(double[][] data) {
        double[][] ret;
        ret = covar(data);
        projection = computePca(data, ret);
    }

    public PcaResults(double[][] data, boolean[] cancel) {

        double[][] ret;

        if (cancel[0]) {
            return;
        } else {
            ret = covar(data, cancel);
            projection = computePca(data, ret);
        }
    }

    public int dimension() {
        return eigenvectors.length;
    }

    public void replaceEigen(int component, double[] replacement, double[][] data) {

        for (int i = 0; i < replacement.length; i++) {
            eigenvectors[component][i] = replacement[i];
        }

        updateComp(data);
    }

    //calculate a nxn covariance matrix from input inmatrix.
    public double[][] covar(double[][] inmatrix) {
        double[] mean = new double[inmatrix[0].length];

        double[] X = new double[inmatrix.length];

        double[] Y = new double[inmatrix.length];

        double ret[][] = new double[inmatrix[0].length][inmatrix[0].length];

        double sum1 = 0.0;

        for (int i = 0; i < inmatrix.length; i++) {

            for (int j = 0; j < inmatrix[0].length; j++) {

                mean[j] = mean[j] + inmatrix[i][j];

            }

        }

        for (int i = 0; i < mean.length; i++) {

            mean[i] = mean[i] / (double) inmatrix.length;

        }

        for (int i = 0; i < inmatrix[0].length; i++) {

            for (int j = 0; j <= i; j++) {

                for (int k = 0; k < inmatrix.length; k++) {

                    X[k] = inmatrix[k][i] - mean[i];

                    Y[k] = inmatrix[k][j] - mean[j];

                }

                sum1 = 0.0;

                for (int k = 0; k < inmatrix.length; k++) {

                    sum1 = sum1 + X[k] * Y[k];

                }

                sum1 = sum1 / (double) (inmatrix.length - 1);

                ret[i][j] = sum1;

            }

        }

        for (int i = 0; i < ret.length; i++) {

            for (int j = 0; j < i; j++) {

                ret[j][i] = ret[i][j];

            }

        }

        return ret;

    }

    /**
     * calculate a nxn covariance matrix from input inmatrix.
     *
     * @param inmatrix
     * @param pr
     * @param cancel
     * @return
     */
    public double[][] covar(double[][] inmatrix, boolean[] cancel) {

        double[] mean = new double[inmatrix[0].length];
        double[] X = new double[inmatrix.length];
        double[] Y = new double[inmatrix.length];
        double ret[][] = new double[inmatrix[0].length][inmatrix[0].length];
        double sum1 = 0.0;


        for (int i = 0; i < inmatrix.length; i++) {

            for (int j = 0; j < inmatrix[0].length; j++) {

                mean[j] = mean[j] + inmatrix[i][j];

            }

            if (cancel[0]) {
                return null;
            }
        }


        for (int i = 0; i < mean.length; i++) {
            mean[i] = mean[i] / (double) inmatrix.length;
        }


        for (int i = 0; i < inmatrix[0].length; i++) {
            for (int j = 0; j <= i; j++) {
                for (int k = 0; k < inmatrix.length; k++) {
                    X[k] = inmatrix[k][i] - mean[i];
                    Y[k] = inmatrix[k][j] - mean[j];
                }

                sum1 = 0.0;

                for (int k = 0; k < inmatrix.length; k++) {
                    sum1 = sum1 + X[k] * Y[k];
                }

                sum1 = sum1 / (double) (inmatrix.length - 1);
                ret[i][j] = sum1;
            }

            if (cancel[0]) {
                return null;
            }
        }

        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < i; j++) {
                ret[j][i] = ret[i][j];
            }
        }

        return ret;
    }

    public double[][] gettransposedeigen() {
        Matrix etrans = new Matrix(eigenvectors);
        return etrans.transpose().getArray();
    }

    public double[][] PCA(double[][] data, double[][] covarmat) {

        double[][] ret;
        Matrix A, B;
        Matrix ma = new Matrix(covarmat);
        EigenvalueDecomposition ed = ma.eig();
        eigenvalues = ed.getRealEigenvalues();
        ret = ed.getV().getArray();
        eigenvectors = ret;
        A = new Matrix(data);
        B = new Matrix(ret);
        A = A.times(B);
        return A.getArray();
    }

    public double[][] computePca(double[][] data, double[][] covarmat) {

        double[][] ret;
        Matrix A, B;
        Matrix ma = new Matrix(covarmat);
        EigenvalueDecomposition ed = ma.eig();
        eigenvalues = ed.getRealEigenvalues();
        ret = ed.getV().getArray();
//Changed.. vectors are sorted in EigenvalueDecomp..
        ret = sorteigenvectors(ret);
        eigenvectors = ret;
        A = new Matrix(data);
        B = new Matrix(ret);
        A = A.times(B);
        return A.getArray();
    }

    public void updateComp(double[][] data) {
        Matrix A = new Matrix(data);
        Matrix B = new Matrix(eigenvectors);
        A = A.times(B);
        projection = A.getArray();
    }

    public double[][] getlow(double[][] data) {	//for neuroner.. transformerer punkt til computePca-space

        double[][] ret;
        Matrix A, B, C;
        A = new Matrix(data);
        B = new Matrix(eigenvectors);
        A = A.times(B);
        return A.getArray();
    }

    public double totalvariance() {

        double sum = 0.0;

        for (int i = 0; i < eigenvalues.length; i++) {
            sum = sum + eigenvalues[i];
        }

        return sum;
    }

    public double varianceaccounted(int component) {

        double ret = eigenvalues[component] / totalvariance();
        ret = ret * 100;
        return ret;
    }

    public String varianceastr(int component) {

        double temp = varianceaccounted(component);
        String ret = String.valueOf(temp);

        if (ret.length() > 5) {
            ret = ret.substring(0, 5);
        }

        return ret;
    }

    public double[][] sorteigenvectors(double[][] eigenvectors) {
        double temp = 0.0;
        for (int i = 0; i < eigenvalues.length - 1; i++) {
            for (int j = i + 1; j < eigenvalues.length; j++) {
                if (eigenvalues[j] > eigenvalues[i]) {

                    //skifter egenverdiene
                    temp = eigenvalues[i];
                    eigenvalues[i] = eigenvalues[j];
                    eigenvalues[j] = temp;

                    //skifter egenvektorene
                    for (int k = 0; k < eigenvectors.length; k++) {
                        temp = eigenvectors[k][i];
                        eigenvectors[k][i] = eigenvectors[k][j];
                        eigenvectors[k][j] = temp;
                    }
                }
            }
        }

        return eigenvectors;
    }

    public double ElementAt(int i, int j) {
        return projection[i][j];
    }

    public double[][] getProjection() {
        return projection;
    }

    public double nrPoints() {
        return projection.length;
    }
}
