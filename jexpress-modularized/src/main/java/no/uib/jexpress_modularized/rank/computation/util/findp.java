/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.rank.computation.util;

import java.io.Serializable;











class findp implements Serializable{

    

    public static void main(String []args){

	for(double c=0.1;c<1.01;c+=0.025){

	    double r=0.8;

	    double p=findMix(c,r);

	    System.out.println((float)c+": "+(float)p+" Score:"+

			      ((p*p)+2*(p*(1-p)*r)/Math.sqrt(c)+

			       ((1-p)*(1-p))/c) );

	}

    }



    public static double findMix(double c, double r){

	double min=1e6;

	double bestp=0;

	for(double p=0;p<1.01;p+=0.01){

	    double score=(p*p)+2*(p*(1-p)*r)/Math.sqrt(c)+((1-p)*(1-p))/c;

	    if(score<min){

		min=score;

		bestp=p;

	    }

	    //System.out.println("Score:"+score+" p="+p);

	}

	//System.out.println("Score: "+(float)min);

	return bestp;

	

    }





}

