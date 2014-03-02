/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.rank.computation.util;




import java.io.Serializable;
import java.lang.*;




class Matrix extends Object implements Serializable{



    public static double [][]inverse(double [][]matrix){

	if(matrix.length!=matrix[0].length)

	    return null;

	int n=matrix.length;

	double [][]convmatr=new double[n][2*n];

	for(int i=0;i<n;i++){

	    convmatr[i][i+n]=1;

	    for(int j=0;j<n;j++)

		convmatr[i][j]=matrix[i][j];

	}

	for(int i=0;i<n;i++){

	    double c=1/convmatr[i][i];

	    for(int j=i;j<2*n;j++)

		convmatr[i][j]*=c;

	    for(int j=i+1;j<n;j++){

		double c1=convmatr[j][i]/convmatr[i][i];

		for(int k=i;k<2*n;k++)

		    convmatr[j][k]-=c1*convmatr[i][k];

	    }

	}

	for(int i=n-1;i>=0;i--){

	    for(int j=i-1;j>=0;j--){

		double c=convmatr[j][i]/convmatr[i][i];

		for(int k=0;k<2*n;k++)

		    convmatr[j][k]-=c*convmatr[i][k];

	    }

	}

	double [][]inv=new double[n][n];

	for(int i=0;i<n;i++)

	    for(int j=0;j<n;j++)

		inv[i][j]=convmatr[i][j+n];

	return inv;

      

    }



}

