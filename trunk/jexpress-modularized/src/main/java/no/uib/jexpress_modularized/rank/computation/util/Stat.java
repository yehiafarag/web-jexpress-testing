/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.uib.jexpress_modularized.rank.computation.util;
import java.io.Serializable;
import java.util.*;

public class Stat extends Object implements Serializable{

    public static int counter=0;
    
    /** 
     *@param x Coordinate vector
     *@param y Coordinate vector
     *@return Euclid distance between the points x and y
     */
    public static double dist(double [] x, double [] y){
	
	double diff=0.0, sum=0.0;
	for(int i=0;i<x.length;i++){
	    diff=x[i]-y[i];
	    sum+=diff*diff;
	}
	return Math.sqrt(sum);
    }

    /** 
     *@param x Coordinate vector
     *@param y Coordinate vector
     *@return Euclid distance between the points x and -y
     */
    public static double flipDist(double [] x, double [] y){
	double diff=0.0, sum=0.0;
	for(int i=0;i<x.length;i++){
	    diff=x[i]+y[i];
	    sum+=diff*diff;
	}
	return Math.sqrt(sum);
    }

    /** 
     *@param x Coordinate vector
     *@param y Coordinate vector
     *@return Euclid distance between the normalized points x and y
     */
    public static double normDist(double [] x, double [] y){
	double xy=0, x2=0, y2=0, n=(double)x.length;
	for(int i=0;i<x.length;i++){
	    xy+=x[i]*y[i];
	    x2+=x[i]*x[i];
	    y2+=y[i]*y[i];
	}
	double c=xy/Math.sqrt(x2*y2);
	double d=Math.sqrt(2*(1-c)*(n-1));
	return  d;
    }

    /** 
     *@param x Coordinate vector
     *@param y Coordinate vector
     *@return Euclid distance between the normalized points x and -y
     */
    public static double normFlipDist(double [] x, double [] y){
	double xy=0, x2=0, y2=0, n=(double)x.length;
	for(int i=0;i<x.length;i++){
	    xy+=-(x[i]*y[i]);
	    x2+=x[i]*x[i];
	    y2+=y[i]*y[i];
	}
	double c=xy/Math.sqrt(x2*y2);
	double d=Math.sqrt(2*(1-c)*(n-1));
	return  d;
    }

    /** 
     *
     *
     *@return Pearson correlation between the vectors x and y
     */
    public static double corr(double [] x, double [] y){
	double x2=0, y2=0, xy=0, xm=0, ym=0, n=(double)x.length;
	for(int i=0;i<x.length;i++){
	    xy+=x[i]*y[i];
	    x2+=x[i]*x[i];
	    y2+=y[i]*y[i];
	    xm+=x[i];
	    ym+=y[i];
	}
	xm/=n;
	ym/=n;
	double r=(xy-n*xm*ym)/Math.sqrt((x2-n*xm*xm)*(y2-n*ym*ym));
	if(Math.abs(r)>1)
	    return 0;
	else
	    return r;
    }

    /** 
     * 
     *
     *@return Spearman rank-correlation between the vectors x and y
     */
    public static double rankCorr(double [] x, double [] y){
	double [] rx=new double [x.length];
	double [] ry=new double [y.length];
	double rankx,ranky;
	for(int i=0;i<x.length;i++){
	    rankx=1;
	    ranky=1;
	    for(int j=0;j<x.length;j++){
		if(j!=i){
		    if(x[j]<x[i]) rankx++;
		    if(x[j]==x[i]) rankx+=0.5;
		    if(y[j]<y[i]) ranky++;
		    if(y[j]==y[i]) ranky+=0.5;
		}
	    }
	    rx[i]=rankx;
	    ry[i]=ranky;
	}
	double x2=0, y2=0, xy=0, xm=0, ym=0, n=(double)x.length;
	for(int i=0;i<x.length;i++){
	    xy+=rx[i]*ry[i];
	    x2+=rx[i]*rx[i];
	    y2+=ry[i]*ry[i];
	}
	xm=(n+1)/2;
	ym=(n+1)/2;
	double r=(xy-n*xm*ym)/Math.sqrt((x2-n*xm*xm)*(y2-n*ym*ym));
	return r;
    }

 //rx and ry are rank numbers...
    public static double rankCorrWRanks(double[] rx, double[] ry){
	double x2=0, y2=0, xy=0, xm=0, ym=0, n=(double)rx.length;
	for(int i=0;i<rx.length;i++){
	    xy+=rx[i]*ry[i];
	    x2+=rx[i]*rx[i];
	    y2+=ry[i]*ry[i];
	}
	xm=(n+1)/2;
	ym=(n+1)/2;
	double r=(xy-n*xm*ym)/Math.sqrt((x2-n*xm*xm)*(y2-n*ym*ym));
	return r;
    }
    
    
    
    
    public static double stdDev(double [] obs){
	//empirical standard deviation
	double mean=0;
	for(int i=0;i<obs.length;i++)
	    mean+=obs[i];
	double sum=0;
	double n=(double)obs.length;
	mean/=n;
	for(int i=0;i<obs.length;i++){
	    double diff=obs[i]-mean;
	    sum+=diff*diff;
	}
	return Math.sqrt(sum/(n-1));
    }

    public static double variance(double [] obs){
	//empirical variance
	double sum=0;
	double sq=0;
	for(int i=0;i<obs.length;i++){
	    sum+=obs[i];
	    sq+=obs[i]*obs[i];
	}
	double n=obs.length;
	return (sq-sum*sum/n)/(n-1);
    }

    public static double oneSample_fold(double []obs){
	double []obs1=new double[obs.length];
	for(int i=0;i<obs1.length;i++)
	    obs1[i]=Math.pow(2,obs[i]);
	double m1=0;
	for(int i=0;i<obs1.length;i++)
	    m1+=obs1[i];
	m1/=obs1.length;
	if(m1>1)
	    return m1;
	else
	    return -1/m1;
    }

    public static double twoSample_fold(double []obs1, double obs2[]){
	double []r1=new double[obs1.length];
	double []r2=new double[obs2.length];
	for(int i=0;i<obs1.length;i++)
	    r1[i]=Math.pow(2,obs1[i]);
	for(int i=0;i<obs2.length;i++)
	    r2[i]=Math.pow(2,obs2[i]);
	double m1=0,m2=0;
	for(int i=0;i<obs1.length;i++)
	    m1+=r1[i];
	for(int i=0;i<obs2.length;i++)
	    m2+=r2[i];
	m1/=obs1.length;
	m2/=obs2.length;
	if(m1>m2)
	    return m1/m2;
	else
	    return -m2/m1;
    }

    public static double oneSample_Wilcox_z(double []obs){
	//Wilcoxon's signed rank statistic
	return 0;
    }


    public static double tStatisticOneSample(double []obs, double my0){
	double m=0,ss=0,n=(double)obs.length;
	for(int i=0;i<obs.length;i++){
	    m+=obs[i];
	    ss+=obs[i]*obs[i];
	}
	m/=n;
	double var=(ss-m*m*n)/(n-1);
        if((m-my0)==0 || var==0)
            return 0;
        else
            return (m-my0)/Math.sqrt(var/n);
    }


    public static double tStatistic(double []obs1, double []obs2){
	//two-sample t-statistic
	double var1,var2,ss1=0,ss2=0;
	double n1,n2,am,bm;
	n1=obs1.length;
	n2=obs2.length;
	am=0;bm=0;
	for(int i=0;i<obs1.length;i++)
	    am+=obs1[i];
	am/=n1;
	for(int i=0;i<obs2.length;i++)
	    bm+=obs2[i];
	bm/=n2;
	for(int i=0;i<obs1.length;i++){
	    ss1+=obs1[i]*obs1[i];
	}
	ss1-=am*am*n1;
	for(int i=0;i<obs2.length;i++){
	    ss2+=obs2[i]*obs2[i];
	}
	ss2-=bm*bm*n2;
	//System.out.println("am="+am+" bm="+bm+" var1="+var1+" var2="+var2);
	if(am==bm || (ss1+ss2)==0 || n1==0 || n2==0)
	    return 0;
	else
	    return (am-bm)/(Math.sqrt((ss1+ss2)/(n1+n2-2))*
				 Math.sqrt((1/n1)+(1/n2)));
    }

    public static double ANOVA_F(double [][]obs){
	int k=obs.length;
	int n=0;
	for(int i=0;i<k;i++)
	    n+=obs[i].length;
	double ssb=0,sse=0;
	double df1=k-1, df2=n-k,mean=0;
	double []means=new double[k];
	for(int i=0;i<k;i++){
	    for(int j=0;j<obs[i].length;j++){
		means[i]+=obs[i][j];
		mean+=obs[i][j];
	    }
	    means[i]/=obs[i].length;
	}
	mean/=n;
	for(int i=0;i<k;i++){
	    for(int j=0;j<obs[i].length;j++){
		double diff=obs[i][j]-means[i];
		sse+=diff*diff;
	    }
	    double diff=means[i]-mean;
	    ssb+=diff*diff*obs[i].length;
	}
	double F=(ssb/df1)/(sse/df2);
	return F;
    }

    public static double GolubStatistic(double []obs1, double []obs2){
	//statistic published by Golub et al |m1+m2/s1+s2|
	double var1,var2,ss1=0,ss2=0;
	double n1,n2,am,bm;
	n1=obs1.length;
	n2=obs2.length;
	am=0;bm=0;
	for(int i=0;i<obs1.length;i++)
	    am+=obs1[i];
	am/=n1;
	for(int i=0;i<obs2.length;i++)
	    bm+=obs2[i];
	bm/=n2;
	for(int i=0;i<obs1.length;i++){
	    ss1+=obs1[i]*obs1[i];
	}
	ss1-=am*am*n1;
	for(int i=0;i<obs2.length;i++){
	    ss2+=obs2[i]*obs2[i];
	}
	ss2-=bm*bm*n2;
	//System.out.println("am="+am+" bm="+bm+" var1="+var1+" var2="+var2);
	if(am==bm || (ss1+ss2)==0 || n1==0 || n2==0)
	    return 0;
	else
	    return Math.abs((am-bm)/
			    (Math.sqrt(ss1/(n1-1))+Math.sqrt(ss2/(n2-1))));
    }


    
    public static double sigmoid(double c){
	return 1/(1+Math.exp(-c));
    }
    
    public static double interquartilerange(double []x){
        int n=x.length;
        double tmp[]=new double[n];
        for(int i=0;i<n;i++)
            tmp[i]=x[i];
        Arrays.sort(tmp);
        return tmp[3*n/4]-tmp[n/4];
    }
    
    public static double median(double []x){
        int n=x.length;
        double tmp[]=new double[n];
        for(int i=0;i<n;i++)
            tmp[i]=x[i];
        Arrays.sort(tmp);
        if(n%2==1)
            return tmp[n/2];
        else
            return (tmp[n/2]+tmp[n/2-1])/2;
    }

    public static double mean(double []x){
        double m=0;
        
        for(int i=0;i<x.length;i++)
            m+=x[i];
        return m/x.length;
    }

    public static double trimmedMean(double[] vals){
        java.util.Arrays.sort(vals);
        int q1 = (int)(vals.length*0.25);
        int q3 = (int)(vals.length*0.75);
        double sum = 0;
        double n=0;

        if(vals.length==0)return 0.0;
        if(vals.length==1)return vals[0];
        else if(vals.length==2)return (vals[0]+vals[1])/2.0;
        else if(vals.length==3)return vals[1];

        for(int i=q1;i<q3;i++){
            sum+=vals[i];
            n++;
        }
        return sum/n;
    }
    
    public static double max(double[] x){
        double max = -1*Double.MAX_VALUE;
        
        for(int i=0;i<x.length;i++){
            if(x[i]>max)
                max=x[i];
        }
        return max;
    }

    public static double MAD(double []x){
        int n=x.length;
        double tmp[]=new double[n];
        for(int i=0;i<n;i++)
            tmp[i]=x[i];
        Arrays.sort(tmp);
        double median=0;
        if(n%2==1)
            median=tmp[n/2];
        else
            median=(tmp[n/2]+tmp[n/2-1])/2;
        for(int i=0;i<n;i++)
            tmp[i]=Math.abs(median-x[i]);
        Arrays.sort(tmp);
        if(n%2==1)
            return tmp[n/2]/0.64;
        else
            return ((tmp[n/2]+tmp[n/2-1])/2)/0.64;
    }
    
    public static double covariance(double []vector1, double []vector2){
	double s1=0,s2=0,prod=0;
	int n=vector1.length;
	for(int i=0;i<n;i++){
	    s1+=vector1[i];
	    s2+=vector2[i];
	    prod+=vector1[i]*vector2[i];
	}
	return (prod-s1*s2/(double)n)/(double)(n-1);
    }

    public static double [][]estimateMissingValues(double [][]data, 
						   boolean [][]missing,
						   int iter,
						   int method) throws Exception{
	//System.out.println("method="+method);
	if(iter<1) iter=1;
	if(iter>5) iter=5;
	
	double [][]estdata=estimateMissingRowMean(data,missing);
	for(int i=0;i<iter;i++){
	    if(method==0)
		estdata=estimateMissingBivar(estdata,missing);
	    if(method==1){
		//estdata=estimateMissingRowMean(estdata,missing);
		estdata=estimateMissingBivar(estdata,missing);
		estdata=estimateMissingLeastSquares(estdata,missing);
		//estdata=estimateMissingLeastSquares(estdata,missing);
	    }
	    if(method==2)
		estdata=estimateMissingSelectedLeastSquares(estdata,missing);
	    if(method==3){
		//System.out.println("Hello combined");
		
		estdata=estimateMissingCombined(estdata,missing);
	    }
	    if(method==4){
		//System.out.println("Hello combined");
		estdata=estimateMissingAdaptiveCombined(estdata,missing);
	    }
	}
    
    counter=-1;
    
	return estdata;
    }

    public static double[][] estimateMissingCombined(double [][]data, 
						     boolean [][]missing) throws Exception{
	int n=data.length;
	int m=data[0].length;
	int nmissing=0;
	for(int i=0;i<n;i++)
	    for(int j=0;j<m;j++)
		if(missing[i][j]) nmissing++;
	/*
	System.out.println("Percent missing originally: "+
			   (double)(nmissing*100)/(n*m));
	*/	
	boolean [][]mymissing=new boolean[n][m];
	int plusmissing=(n*m)/20;//add artificially 5% missing entries;
	int count=0;
        
        //The following lines of code were added 25.02.2005 to better handle data  with few columns
        //Avoids deleting all data of a row with the extra missing values
        double left[]=new double[n];
        for(int i=0;i<n;i++)
            for(int j=0;j<m;j++)
                if(!missing[i][j])
                    left[i]++;
        //End code 25.02.2005
        
	while(count<plusmissing){
	    int i=(int)(Math.random()*(double)n);
	    int j=(int)(Math.random()*(double)m);
	    if(!missing[i][j] && !mymissing[i][j] && left[i]>5){
		mymissing[i][j]=true;
		count++;
	    }
	}
	boolean [][]summissing=new boolean[n][m];
	for(int i=0;i<n;i++)
	    for(int j=0;j<m;j++)
		summissing[i][j]=(missing[i][j] || mymissing[i][j]);
	double [][]estrow=estimateMissingBivar(data,summissing);
	double [][]estcol=estimateMissingLeastSquares(estrow,summissing);
	double []errrow=new double[plusmissing];
	double []errcol=new double[plusmissing];
	count=0;
	for(int i=0;i<n;i++)
	    for(int j=0;j<m;j++)
		if(mymissing[i][j]){
		    errrow[count]=estrow[i][j]-data[i][j];
		    errcol[count]=estcol[i][j]-data[i][j];
		    count++;
		}
	double corr=corr(errrow,errcol);
	double varrow=variance(errrow);
	double varcol=variance(errcol);
	/*
	System.out.println("corr="+corr+
			   "\nvarrow="+varrow+
			   "\nvarcol="+varcol);
	*/
	double p=findp.findMix(varrow/varcol,corr);
	//System.out.println("p="+p);
	estrow=estimateMissingBivar(data,missing);
	estcol=estimateMissingLeastSquares(estrow,missing);
	double [][]estcomb=new double[n][m];
	for(int i=0;i<n;i++)
	    for(int j=0;j<m;j++)
		if(!missing[i][j])
		    estcomb[i][j]=data[i][j];
		else
		    estcomb[i][j]=p*estrow[i][j]+(1-p)*estcol[i][j];
	return estcomb;
    }

    public static double[][] estimateMissingAdaptiveCombined(double [][]data, 
						     boolean [][]missing) throws Exception{
	int n=data.length;
	int m=data[0].length;
	int nmissing=0;
	for(int i=0;i<n;i++)
	    for(int j=0;j<m;j++)
		if(missing[i][j]) nmissing++;
	/*
	System.out.println("Percent missing originally: "+
			   (double)(nmissing*100)/(n*m));
	*/	
	boolean [][]mymissing=new boolean[n][m];
	int plusmissing=(n*m)/20;//add artificially 5% missing entries;
	int count=0;
        
	double [][]estrow=estimateMissingBivar(data,missing);
	double [][]estcol=estimateMissingLeastSquares(estrow,missing);
	
        //The following lines of code were added 25.02.2005 to better handle data  with few columns
        //Avoids deleting all data of a row with the extra missing values
        double left[]=new double[n];
        for(int i=0;i<n;i++)
            for(int j=0;j<m;j++)
                if(!missing[i][j])
                    left[i]++;
        //End code 25.02.2005
        
        while(count<plusmissing){
	    int i=(int)(Math.random()*(double)n);
	    int j=(int)(Math.random()*(double)m);
	    if(!missing[i][j] && !mymissing[i][j] && left[i]>5){
		mymissing[i][j]=true;
		count++;
	    }
	}

	double [][]maxabscorr=new double[n][m];
	double [][]W=new double[n][m];
	double [][]maxcorrvariance=new double[n][m];

	boolean [][]summissing=new boolean[n][m];
	for(int i=0;i<n;i++)
	    for(int j=0;j<m;j++)
		summissing[i][j]=(missing[i][j] || mymissing[i][j]);

	//---------------------------------------------
	/*
	//array mean and covariance estimate calculation
	//alternatively use EM for maximum likelihood estimation
	double [][]sigma=new double[m][m];
	double []mean=new double[m];
	double [][]varest=new double [n][m];


	//first for artificially missing data

	for(int i=0;i<m;i++){
	    double []x=new double[n];
	    mean[i]=0;
	    for(int a=0;a<n;a++){
		x[a]=data[a][i];
		mean[i]+=data[a][i];
	    }
	    mean[i]/=(double)n;
	    for(int j=0;j<i+1;j++){
		double []y=new double[n];
		for(int a=0;a<n;a++)
		    y[a]=data[a][j];
		sigma[i][j]=covariance(x,y);
		sigma[j][i]=sigma[i][j];
		
	    }
	}

	for(int i=0;i<n;i++){
	    int miss=0;
	    for(int j=0;j<m;j++)
		if(summissing[i][j]) 
		    miss++;

	    int n1=miss,n2=m-miss;

	    if(n1>0 && n2>0){
		double []x2=new double[n2];
		int []indx=new int[m];
		int c1=0,c2=n1;
		for(int j=0;j<m;j++)
		    if(summissing[i][j]){
			indx[c1]=j;
			c1++;
		    }
		    else{
			indx[c2]=j;
			c2++;
		    }
		
		double [][]S=new double[m][m];
		for(int j=0;j<m;j++)
		    for(int k=0;k<m;k++)
			S[j][k]=sigma[indx[j]][indx[k]];
		    
		double [][]S22=new double[n2][n2];
		for(int a=n1;a<m;a++)
		    for(int b=n1;b<m;b++){
			S22[a-n1][b-n1]=S[a][b];
		    }
		double [][]S22inv=Matrix.inverse(S22);
				
		double [][]S12xS22inv=new double[n1][n2];
		for(int a=0;a<n1;a++)
		    for(int b=0;b<n2;b++){
			double sum=0;
			for(int k=0;k<n2;k++)
			    sum+=S[a][n1+k]*S22inv[k][b];
			S12xS22inv[a][b]=sum;
		    }

		double [][]S2=new double[n1][n1];
		
		for(int a=0;a<n1;a++)
		    for(int b=0;b<n1;b++){
			double sum=0;
			for(int k=0;k<n2;k++)
			    sum+=S12xS22inv[a][k]*S[n1+k][b];
			S2[a][b]=sum;
		    }
				
		for(int a=0;a<n1;a++)
		    varest[i][indx[a]]=(S[a][a]-S2[a][a])/S[a][a];
	    }
	}

	//then for the real missing data
	
	for(int i=0;i<m;i++){
	    double []x=new double[n];
	    mean[i]=0;
	    for(int a=0;a<n;a++){
		x[a]=data[a][i];
		mean[i]+=data[a][i];
	    }
	    mean[i]/=(double)n;
	    for(int j=0;j<i+1;j++){
		double []y=new double[n];
		for(int a=0;a<n;a++)
		    y[a]=data[a][j];
		sigma[i][j]=covariance(x,y);
		sigma[j][i]=sigma[i][j];
		
	    }
	}

	for(int i=0;i<n;i++){
	    int miss=0;
	    for(int j=0;j<m;j++)
		if(missing[i][j]) 
		    miss++;

	    int n1=miss,n2=m-miss;

	    if(n1>0 && n2>0){
		double []x2=new double[n2];
		int []indx=new int[m];
		int c1=0,c2=n1;
		for(int j=0;j<m;j++)
		    if(missing[i][j]){
			indx[c1]=j;
			c1++;
		    }
		    else{
			indx[c2]=j;
			c2++;
		    }
		
		double [][]S=new double[m][m];
		for(int j=0;j<m;j++)
		    for(int k=0;k<m;k++)
			S[j][k]=sigma[indx[j]][indx[k]];
		    
		double [][]S22=new double[n2][n2];
		for(int a=n1;a<m;a++)
		    for(int b=n1;b<m;b++){
			S22[a-n1][b-n1]=S[a][b];
		    }
		double [][]S22inv=Matrix.inverse(S22);
				
		double [][]S12xS22inv=new double[n1][n2];
		for(int a=0;a<n1;a++)
		    for(int b=0;b<n2;b++){
			double sum=0;
			for(int k=0;k<n2;k++)
			    sum+=S[a][n1+k]*S22inv[k][b];
			S12xS22inv[a][b]=sum;
		    }

		double [][]S2=new double[n1][n1];
		
		for(int a=0;a<n1;a++)
		    for(int b=0;b<n1;b++){
			double sum=0;
			for(int k=0;k<n2;k++)
			    sum+=S12xS22inv[a][k]*S[n1+k][b];
			S2[a][b]=sum;
		    }
				
		for(int a=0;a<n1;a++)
		    varest[i][indx[a]]=(S[a][a]-S2[a][a])/S[a][a];
	    }
	}


	//---------------------------------------------
	*/

	///////////////////////////////////////

	double []abscorr=new double[n];
	double []variance=new double[n];
	int []indx=new int[n];

	//first for artificially missing data
	int incl=1;

	for(int i=0;i<n;i++){
	    
	    boolean hasmissing=false;
	    for(int j=0;j<m && !hasmissing;j++)
		if(summissing[i][j]) hasmissing=true;
	    if(hasmissing){
		for(int x=0;x<n;x++){
		    if(x!=i){
			int c=0;
			for(int a=0;a<m;a++)
			    if(!summissing[i][a] && !summissing[x][a]) c++;
			double []v1=new double[c];
			double []v2=new double[c];
			c=0;
			for(int a=0;a<m;a++)
			    if(!summissing[i][a] && !summissing[x][a]){
				v1[c]=data[i][a];
				v2[c]=data[x][a];
				c++;
			    }
			variance[x]=variance(v1);
			abscorr[x]=Math.abs(corr(v1,v2));
		    }
		    else
			abscorr[x]=0;
		    indx[x]=x;
		}
	    }
	    parallelSort(abscorr,indx);
	    
	    int use=10;
	    int []model=new int[incl];
	    for(int j=0;j<m;j++){
		if(summissing[i][j]&&use>0){
		    int x=1,y=1;
		    double avg=0;
		    double maxcorr=0;
		    double maxcorrvar=0;
		    while(x<use+1){
			if(!summissing[indx[n-y]][j]){
			    double r=abscorr[n-y];
			    double w=r*r/(1-r*r+1e-3);
			    w*=w;
			    W[i][j]+=w;
			    if(abscorr[n-y]>maxcorr){ 
				maxcorr=abscorr[n-y];
				maxcorrvar=variance[indx[n-y]];
			    }
			    x++;
			}
			y++;
		    }
		    maxabscorr[i][j]=maxcorr;
		    maxcorrvariance[i][j]=maxcorrvar;
		}
	    }
        
        
         counter++;
         if (Thread.interrupted()) throw new InterruptedException();
        
	}

	//then for the real missing data

	for(int i=0;i<n;i++){
	    
	    boolean hasmissing=false;
	    for(int j=0;j<m && !hasmissing;j++)
		if(missing[i][j]) hasmissing=true;
	    if(hasmissing){
		for(int x=0;x<n;x++){
		    if(x!=i){
			int c=0;
			for(int a=0;a<m;a++)
			    if(!missing[i][a] && !missing[x][a]) c++;
			double []v1=new double[c];
			double []v2=new double[c];
			c=0;
			for(int a=0;a<m;a++)
			    if(!missing[i][a] && !missing[x][a]){
				v1[c]=data[i][a];
				v2[c]=data[x][a];
				c++;
			    }
			//variance[x]=variance(v1);
			abscorr[x]=Math.abs(corr(v1,v2));
		    }
		    else
			abscorr[x]=0;
		    indx[x]=x;
		}
	    }
	    parallelSort(abscorr,indx);
	    
	    int use=10;
	    int []model=new int[incl];
	    for(int j=0;j<m;j++){
		if(missing[i][j]&&use>0){
		    int x=1,y=1;
		    double avg=0;
		    double maxcorr=0;
		    double maxcorrvar=0;
		    while(x<use+1){
			if(!missing[indx[n-y]][j]){
			    double r=abscorr[n-y];
			    double w=r*r/(1-r*r+1e-3);
			    w*=w;
			    W[i][j]+=w;
			    if(abscorr[n-y]>maxcorr){ 
				maxcorr=abscorr[n-y];
				//maxcorrvar=variance[indx[n-y]];
			    }
			    x++;
			}
			y++;
		    }
		    
		    maxabscorr[i][j]=maxcorr;
		    maxcorrvariance[i][j]=maxcorrvar;
		}
	    }
        
        counter++;
        if (Thread.interrupted()) throw new InterruptedException();
        
	}



	///////////////////////////////////////


	double [][]estrow2=estimateMissingBivar(data,summissing);
	double [][]estcol2=estimateMissingLeastSquares(estrow2,summissing);
	double []errrow=new double[plusmissing];
	double []errcol=new double[plusmissing];
	double []abserrdiff=new double[plusmissing];
	double []maxcorr=new double[plusmissing];
	double []logW=new  double[plusmissing];
	double minw=1e6, maxw=-1e6;
	count=0;
	indx=new int[plusmissing];
	for(int i=0;i<n;i++)
	    for(int j=0;j<m;j++)
		if(mymissing[i][j]){
		    errrow[count]=estrow2[i][j]-data[i][j];
		    errcol[count]=estcol2[i][j]-data[i][j];
		    abserrdiff[count]=Math.abs(errrow[count])-
			Math.abs(errcol[count]);
		    maxcorr[count]=maxabscorr[i][j];
		    logW[count]=Math.log(W[i][j]);
		    if(logW[count]<minw) minw=logW[count];
		    if(logW[count]>maxw) maxw=logW[count];
		    indx[count]=count;
		    count++;
		}
	/*
	PrintStream ps=new PrintStream(new FileOutputStream("arrerr.txt"));
	for(int i=0;i<plusmissing;i++)
	    ps.println(maxcorr[i]+"\t"+errrow[i]+"\t"+errcol[i]+"\t"+logW[i]);
	ps.close();
	*/
	//System.out.println(Stat.corr(maxcorr,abserrdiff));
	//System.out.println(Stat.corr(logW,abserrdiff));

	boolean usew=false;
	if(usew)
	    for(int i=0;i<plusmissing;i++)
		maxcorr[i]=(logW[i]-minw)/(maxw-minw);
	

	parallelSort(maxcorr,indx);

	double []ersq=new double[plusmissing];
	double []ecsq=new double[plusmissing];
	double []erxec=new double[plusmissing];

	double []avger2=new double[101];
	double []avgec2=new double[101];
	double []avgerec=new double[101];

	for(int i=0;i<plusmissing;i++){
	    ersq[i]=errrow[indx[i]]*errrow[indx[i]];
	    ecsq[i]=errcol[indx[i]]*errcol[indx[i]];
	    erxec[i]=errrow[indx[i]]*errcol[indx[i]];
	}

	for(int i=0;i<101;i++){
	    double r=(double)i;
	    r/=100;
	    double a=r-0.05;
	    double b=r+0.05;
	    int indxb=plusmissing-1,indxa=0;
	    while(indxa<plusmissing-1 && maxcorr[indxa]<a) indxa++;
	    while(indxb>0 && maxcorr[indxb]>b) indxb--;

	    int s=0;
	    while(indxb-indxa<99){
		//interval too small, expand interval
		if(indxa>0 && s%2==0) indxa--;
		if(indxb<plusmissing-1 && s%2!=0) indxb++;
		s++;
	    }

	    double sum1=0,sum2=0,sum3=0,tot=indxb-indxa+1;
	    for(int j=indxa;j<=indxb;j++){
		sum1+=ersq[j];
		sum2+=ecsq[j];
		sum3+=erxec[j];
	    }
	    avger2[i]=sum1/tot;
	    avgec2[i]=sum2/tot;
	    avgerec[i]=sum3/tot;
	}
	
	double [][]estcomb=new double[n][m];
	for(int i=0;i<n;i++)
	    for(int j=0;j<m;j++)
		if(!missing[i][j])
		    estcomb[i][j]=data[i][j];
		else{
		    double mc=maxabscorr[i][j];
		    double logw=Math.log(W[i][j]);
		    int s=Math.round((float)mc*100);
		    int t=Math.round((float)((logw-minw)*100/(maxw-minw)));
		    if(t>100) t=100;
		    if(t<0) t=0; 
		    if(usew) s=t;
		    double arg3=avger2[s]/avgec2[s];
		    double arg4=avgerec[s]/Math.sqrt(avger2[s]*avgec2[s]);
		    double p_ij=findp.findMix(arg3,arg4);
		    
		    estcomb[i][j]=p_ij*estrow[i][j]+(1-p_ij)*estcol[i][j];
		
                    
            counter++;
            if (Thread.interrupted()) throw new InterruptedException();
        
        }
	return estcomb;
    }


    public static double[][] estimateMissingBivar(double [][]data, 
					   boolean [][]missing) throws Exception{
	int n=data.length;
	int m=data[0].length;
	double estimates[][]=new double[n][m];

	double []abscorr=new double[n];
	int []indx=new int[n];
	double []alpha=new double[n];
	double []beta=new double[n];
	double []mean_1=new double[n];
	double []mean_2=new double[n];
	double minmaxcorr=1;
	int nmissing=0;
	for(int i=0;i<n;i++){
	    boolean hasmissing=false;
	    for(int j=0;j<m && !hasmissing;j++)
		if(missing[i][j])
		    hasmissing=true;
	    		
	    if(hasmissing){
		for(int j=0;j<n;j++){
		    mean_1[j]=0;
		    mean_2[j]=0;
		}
		for(int x=0;x<n;x++){
		    if(x!=i){
			int c=0;
			for(int a=0;a<m;a++)
			    if(!missing[i][a] && !missing[x][a]) c++;
			double []v1=new double[c];
			double []v2=new double[c];
			c=0;
			for(int a=0;a<m;a++)
			    if(!missing[i][a] && !missing[x][a]){
				v1[c]=data[i][a];
				v2[c]=data[x][a];
				//mean_1[x]+=data[i][a];
				//mean_2[x]+=data[x][a];
				c++;
			    }
			/*
			mean_1[x]/=c;
			mean_2[x]/=c;
			alpha[x]=mean_1[x];
			double var1=variance(v1);
			double var2=variance(v2);
			double cov=covariance(v1,v2);
			beta[x]=cov/var2;
			*/
			abscorr[x]=Math.abs(corr(v1,v2));
                        if(c<5) abscorr[x]=0;
			//Math.abs(corr(v1,v2));
			//Math.abs(cov/Math.sqrt(var1*var2));
		    }
		    else
			abscorr[x]=0;
		    indx[x]=x;
		}
	    
		//long start=System.currentTimeMillis();
        
        
        
		//parallelQuickSort(abscorr,indx,0,n-1);
        indx = JDoubleSorter.quickSort(abscorr);
        
		//parallelSort(abscorr,indx);
		//long stop=System.currentTimeMillis();
		//System.out.println("Sorting time: "+(stop-start)+"ms");
	    }
	    
	    int use=10;
	    	    
	    if(abscorr[n-1]<minmaxcorr)
		minmaxcorr=abscorr[n-1];
	    
	    double mean=0,num=0;
	    for(int j=0;j<m;j++){
		if(!missing[i][j]){
		    estimates[i][j]=data[i][j];
		    num++;
		    mean+=data[i][j];
		}
	    }
	    mean/=num;
	    
	    for(int j=0;j<m;j++){
		if(missing[i][j]&& use>0){
		    nmissing++;
		    int x=0,y=1;
		    double wsum=0,theest=0;
		    while(x<use){
			if(!missing[indx[n-y]][j]){
			    int index=indx[n-y];
			    
			    int common=0;
			    double mean1=0,mean2=0;
			    for(int k=0;k<m;k++)
				if(!missing[i][k] && !missing[index][k]){
				    common++;
				}
				
			    double []vector1=new double[common];
			    double []vector2=new double[common];
			    int c=0;
			    for(int k=0;k<m;k++)
				if(!missing[i][k] && !missing[index][k]){
				    vector1[c]=data[i][k];
				    vector2[c]=data[index][k];
				    mean1+=data[i][k];
				    mean2+=data[index][k];
				    c++;
				}
			    mean1/=common;
			    mean2/=common;
			    			    
			    double cxy=covariance(vector1,vector2);
			    double vary=variance(vector2);
			    			    
			    double aest=mean1+
				(cxy/vary)*(data[index][j]-mean2);
                            
			    /*
			    double aest=alpha[index]+
				beta[index]*
				(data[index][j]-mean_2[index]);
			    */
			    double r=abscorr[n-y];//corr(vector1,vector2);
			    double w=r*r/(1-r*r+1e-6);
			    //System.out.println("r="+r+" w="+w);
			    w*=w;
			    theest+=aest*w;
			    wsum+=w;
			    x++;
			    
			}
			y++;
		    }
		    //System.out.println("wsum="+wsum);
		    theest/=wsum;
                    if(wsum==0){ 
                        //Means that this row doesn't have enough data to estimate from
                        //Use row mean as estimate
                        //This fix was added 25.02.2005 to better handle data with few columns
                        double rmean=0;
                        int elements=0;
                        for(int k=0;k<m;k++)
                            if(!missing[i][k]){
                                rmean+=data[i][k];
                                elements++;
                            }
                        theest=rmean/elements;
                    }
                        
		    estimates[i][j]=theest;

		}
		if(missing[i][j]&& use==0){
		    estimates[i][j]=mean;
		}
	    }
        
        
           counter++;
           if (Thread.interrupted()) throw new InterruptedException();
        
	}
	//System.out.println(nmissing);
	return estimates;
    
    }

    public static double[][] estimateMissingRowLS(double [][]data, 
					   boolean [][]missing){
	int n=data.length;
	int m=data[0].length;
	double estimates[][]=new double[n][m];

	double []abscorr=new double[n];
	int []indx=new int[n];
	double minmaxcorr=1;
	for(int i=0;i<n;i++){
	    
	    boolean hasmissing=false;
	    for(int j=0;j<m && !hasmissing;j++)
		if(missing[i][j]) hasmissing=true;
	    if(hasmissing){
		for(int x=0;x<n;x++){
		    if(x!=i){
			int c=0;
			for(int a=0;a<m;a++)
			    if(!missing[i][a] && !missing[x][a]) c++;
			double []v1=new double[c];
			double []v2=new double[c];
			c=0;
			for(int a=0;a<m;a++)
			    if(!missing[i][a] && !missing[x][a]){
				v1[c]=data[i][a];
				v2[c]=data[x][a];
				c++;
			    }
			abscorr[x]=Math.abs(corr(v1,v2));
		    }
		    else
			abscorr[x]=0;
		    indx[x]=x;
		}
	    }
	    parallelSort(abscorr,indx);
	    int use=m/5;
	    	    
	    if(abscorr[n-1]<minmaxcorr)
		minmaxcorr=abscorr[n-1];
	    double [][]indata=new double[use+1][m];

	    /*
	    //These lines doesn't ensure that we don't use missing values
	    //estimate missing values;
	    for(int x=0;x<use+1;x++){
		if(x==0)
		    for(int j=0;j<m;j++)
			indata[x][j]=data[i][j];
		else
		    for(int j=0;j<m;j++)
			indata[x][j]=data[indx[n-x]][j];
	    }
	    */

	    double mean=0,num=0;
	    for(int j=0;j<m;j++){
		if(!missing[i][j]){
		    estimates[i][j]=data[i][j];
		    num++;
		    mean+=data[i][j];
		}
		indata[0][j]=data[i][j];
	    }
	    mean/=num;
	    for(int j=0;j<m;j++){
		if(missing[i][j]&& use>0){
		    int x=1,y=1;
		    while(x<use+1){
			if(!missing[indx[n-y]][j]){
			    for(int k=0;k<m;k++)
				indata[x][k]=data[indx[n-y]][k];
			    x++;
			}
			y++;
		    }
		    double theest=missingEstimate(indata,0,j);
		    estimates[i][j]=theest;

		}
		if(missing[i][j]&& use==0){
		    estimates[i][j]=mean;
		}
	    }
	    //System.out.print("\ri="+i);
	}
	//System.out.println("minmaxcorr="+minmaxcorr);
	return estimates;
    
    }

    public static double [][]estimateMissingColumnEM(double [][]data, 
					 boolean [][]missing){
	int n=data.length;
	int m=data[0].length;
	//n and m should have at least a factor 5 difference
	//otherwise the estimates might be inaccurate
	// n=m (or close) could be a total disaster
	
	if(m>n){
	    double [][]data_t=new double[m][n];
	    boolean [][]missing_t=new boolean[m][n];
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++){
		    data_t[j][i]=data[i][j];
		    missing_t[j][i]=missing[i][j];
		}
	    double [][]estimate_t=
		estimateMissingColumnEM(data_t,missing_t);
	    double [][]estimate=new double[n][m];
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++)
		    estimate[i][j]=estimate_t[j][i];
	    return estimate;
	}
	
	//mean and covariance estimate calculation

	double [][]sigma=new double[m][m];
	double []mean=new double[m];

	double []row_mean=new double[n];
	double []row_stdv=new double[n];
	double avgstdv=0,avgm=0;

	for(int i=0;i<n;i++){
	    double c=0;
	    row_mean[i]=0;
	    for(int j=0;j<m;j++)
		//if(!missing[i][j])
		{
		    c++;
		    row_mean[i]+=data[i][j];
		}
	    if(c==0){
		//System.out.println("c="+c);
		row_stdv[i]=1;
		row_mean[i]=0;
	    }
	    else{
		row_mean[i]/=c;
		/*
		if(Math.abs(row_mean[i])>0.05)
		    System.out.print(row_mean[i]+" ");
		*/
		if(c>1){
		    double sum=0;
		    for(int j=0;j<m;j++)
			//if(!missing[i][j])
			{
			    double diff=data[i][j]-row_mean[i];
			    sum+=diff*diff;
			}
		    row_stdv[i]=Math.sqrt(sum/(c-1));
		}
		else
		    row_stdv[i]=1;
		avgm+=row_mean[i]*row_mean[i];
		avgstdv+=row_stdv[i];
		//if(Math.abs(row_stdv[i])<0.01) row_stdv[i]=1;
	    }
	}
	avgstdv/=(double)n;
	avgm/=(double)n;
	//System.out.println("avgstdv="+avgstdv+" avgm="+avgm);

	for(int i=0;i<m;i++){
	    mean[i]=0;
	    for(int a=0;a<n;a++)
		if(!missing[a][i])
		    mean[i]+=data[a][i];
		else
		    mean[i]+=mean[i]/(a+1);//mean so far
	    mean[i]/=n;
	}

	//alternatively transform data
	boolean transform=false;
	boolean varnorm=false;
	double [][]tdata=new double[n][m];
	if(transform)
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++){
		    tdata[i][j]=data[i][j]-row_mean[i];
		    if(varnorm) tdata[i][j]/=row_stdv[i];
		}
	else
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++)
		    if(!missing[i][j])
			tdata[i][j]=data[i][j];
		    else
			tdata[i][j]=row_mean[i];
		    
	boolean missingfreeonly=false;
	
	for(int i=0;i<m;i++){
	    double x[]=new double[n];
	    for(int a=0;a<n;a++)
		    x[a]=tdata[a][i];
	    for(int j=0;j<i+1;j++){
		double []y=new double[n];
		for(int a=0;a<n;a++)
		    y[a]=tdata[a][j];
		sigma[i][j]=covariance(x,y);
		sigma[j][i]=sigma[i][j];
		
	    }
	}

	double [][]estimate=new double[n][m];
	double [][]sigma_next=new double[m][m];
	double []mean_next=new double[m];

	double nmissing=0;
	for(int i=0;i<n;i++)
	    for(int j=0;j<m;j++)
		if(missing[i][j]) nmissing++;

	double diffsq=1e100;;
	double prev=1e200;
	int count=0;
	while(count<100 && 1e-5<diffsq){
	    prev=diffsq;
	    diffsq=0;
	    count++;
	    for(int i=0;i<m;i++){
		for(int j=0;j<m;j++)
		    sigma_next[i][j]=0;
		mean_next[i]=0;
	    }
	    //System.out.println("EM round: "+count);
	    for(int i=0;i<n;i++){

		int miss=0;
		for(int j=0;j<m;j++)
		    if(missing[i][j]) 
			miss++;
		    else
			estimate[i][j]=tdata[i][j];
		int n1=miss,n2=m-miss;
		
		
		double []x2=new double[n2];
		int []indx=new int[m];
		int c1=0,c2=n1;
		for(int j=0;j<m;j++)
		    if(missing[i][j]){
			indx[c1]=j;
			c1++;
		    }
		    else{
			indx[c2]=j;
			c2++;
		    }

		double [][]S12xS22invxS21=new double [n1][n1];
		if(n2>0){
		    double []my=new double[m];
		    double []xx=new double[m];
		    double [][]S=new double[m][m];
		    for(int j=0;j<m;j++){
			my[j]=mean[indx[j]];
			xx[j]=tdata[i][indx[j]];
			for(int k=0;k<m;k++){
			    S[j][k]=sigma[indx[j]][indx[k]];
			}
		    }
		    double [][]S22=new double[n2][n2];
		    for(int a=n1;a<m;a++)
			for(int b=n1;b<m;b++){
			    S22[a-n1][b-n1]=S[a][b];
			}
		    double [][]S22inv= Matrix.inverse(S22);
		    
		    //then to the prediction step
		    
		    double [][]S12xS22inv=new double[n1][n2];
		    for(int a=0;a<n1;a++)
			for(int b=0;b<n2;b++){
			    double sum=0;
			    for(int k=0;k<n2;k++)
				sum+=S[a][n1+k]*S22inv[k][b];
			    S12xS22inv[a][b]=sum;
			}
		    
		    
		    for(int a=0;a<n1;a++)
			for(int b=0;b<n1;b++){
			    double sum=0;
			    for(int c=0;c<n2;c++)
				sum+=S12xS22inv[a][c]*S[n1+c][b];
			    S12xS22invxS21[a][b]=sum;	
			}
		    
		    double []x1=new double[n1];
		    for(int a=0;a<n1;a++){
			double sum=0;
			for(int b=0;b<n2;b++)
			    sum+=S12xS22inv[a][b]*(xx[n1+b]-my[n1+b]);
			x1[a]=my[a]+sum;
		    }

		    for(int a=0;a<n1;a++){
			double diff=(estimate[i][indx[a]]-x1[a]);
			diffsq+=diff*diff/S[a][a];
			estimate[i][indx[a]]=x1[a];
		    }
		
		}else{
		    //Nothing to estimate from, use column mean
		    for(int a=0;a<m;a++){
			estimate[i][a]=mean[a];
		    }
		}
		
		//prepare for next step
		for(int a=0;a<m;a++){
		    mean_next[a]+=estimate[i][a];
		    for(int b=0;b<m;b++)
			sigma_next[a][b]+=estimate[i][a]*estimate[i][b];
		}

		//adjust for products of estimated values
		
		for(int a=0;a<n1;a++)
		    for(int b=0;b<n1;b++){
			if(n2>0)
			    sigma_next[indx[a]][indx[b]]+=
				sigma[indx[a]][indx[b]]-S12xS22invxS21[a][b];
			else
			    sigma_next[indx[a]][indx[b]]+=
				sigma[indx[a]][indx[b]];
			if(!missing[i][indx[a]] || !missing[i][indx[b]])
			    System.out.println("Correcting the wrong elements");
		    }

	    }

	    //
	    for(int i=0;i<m;i++)
		mean_next[i]/=n;
	    for(int i=0;i<m;i++){
		for(int j=0;j<m;j++)
		    sigma[i][j]=sigma_next[i][j]/n-mean_next[i]*mean_next[j];
		mean[i]=mean_next[i];
	    }
	    diffsq/=nmissing;
	    //System.out.println("diffsq="+(float)diffsq+" ratio with prev:"+
	    //	       (float)(diffsq/prev));
	}
	//transform back data
	if(transform)
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++){
		    if(varnorm) estimate[i][j]*=row_stdv[i];
		    estimate[i][j]+=row_mean[i];
		    if(!missing[i][j] && 
		       Math.abs(estimate[i][j]-data[i][j])>1e-4)
			System.out.println("Warning!!"+ 
					   (estimate[i][j]-data[i][j]));
		}
	
	return estimate;
    }

    public static double [][]estimateMissingRowEM(double [][]data, 
					 boolean [][]missing) throws Exception{
	int n=data.length;
	int m=data[0].length;
	double estimates[][]=new double[n][m];

	double []abscorr=new double[n];
	int []indx=new int[n];
	double []alpha=new double[n];
	double []beta=new double[n];
	double []mean_1=new double[n];
	double []mean_2=new double[n];
	double minmaxcorr=1;
	int nmissing=0;
	for(int i=0;i<n;i++){
	    
	    boolean hasmissing=false;
	    for(int j=0;j<m && !hasmissing;j++)
		if(missing[i][j])
		    hasmissing=true;
	    		
	    if(hasmissing){
		for(int j=0;j<n;j++){
		    mean_1[j]=0;
		    mean_2[j]=0;
		}
		for(int x=0;x<n;x++){
		    if(x!=i){
			int c=0;
			for(int a=0;a<m;a++)
			    if(!missing[i][a] && !missing[x][a]) c++;
			double []v1=new double[c];
			double []v2=new double[c];
			c=0;
			for(int a=0;a<m;a++)
			    if(!missing[i][a] && !missing[x][a]){
				v1[c]=data[i][a];
				v2[c]=data[x][a];
				//mean_1[x]+=data[i][a];
				//mean_2[x]+=data[x][a];
				c++;
			    }
			/*
			mean_1[x]/=c;
			mean_2[x]/=c;
			alpha[x]=mean_1[x];
			double var1=variance(v1);
			double var2=variance(v2);
			double cov=covariance(v1,v2);
			beta[x]=cov/var2;
			*/
			abscorr[x]=Math.abs(corr(v1,v2));
			//Math.abs(corr(v1,v2));
			//Math.abs(cov/Math.sqrt(var1*var2));
		    }
		    else
			abscorr[x]=0;
		    indx[x]=x;
		}
	    
		//long start=System.currentTimeMillis();
        indx = JDoubleSorter.quickSort(abscorr);
		//parallelQuickSort(abscorr,indx,0,n-1);
		//parallelSort(abscorr,indx);
		//long stop=System.currentTimeMillis();
		//System.out.println("Sorting time: "+(stop-start)+"ms");
	    }	    
	    int use=10;
	    
	    double [][]indata=new double[m][use+1];
	    boolean [][]inmissing=new boolean[m][use+1];
	    
	    double mean=0,num=0;
	    for(int j=0;j<m;j++){
		if(!missing[i][j]){
		    estimates[i][j]=data[i][j];
		    num++;
		    mean+=data[i][j];
		}
	    }
	    mean/=num;
	    for(int j=0;j<m;j++){
		indata[j][0]=data[i][j];
		if(missing[i][j])
		    inmissing[j][0]=true;
		else
		    inmissing[j][0]=false;
	    }
	    //System.out.print("\rrow "+i);
	    for(int j=0;j<m;j++){
		if(missing[i][j]&& use>0){
		    int x=1,y=1;
		    while(x<use+1){
			if(!missing[indx[n-y]][j]){
			    for(int k=0;k<m;k++){
				indata[k][x]=data[indx[n-y]][k];
				if(missing[indx[n-y]][k])
				    inmissing[k][x]=true;
				else
				    inmissing[k][x]=false;
			    }
			    x++;
			}
			y++;
		    }
		    double [][]theest=
			estimateMissingColumnEM(indata,inmissing);
		    estimates[i][j]=theest[j][0];

		}
		if(missing[i][j]&& use==0){
		    estimates[i][j]=mean;
		}
	    }

        counter++;
        if (Thread.interrupted()) throw new InterruptedException();
	}
	//System.out.println(nmissing);
	return estimates;
    }

    

    public static double [][]estimateMissingLeastSquares(double [][]data, 
					 boolean [][]missing) throws Exception{
	int n=data.length;
	int m=data[0].length;
	//n and m should have at least a factor 5 difference
	//otherwise the estimates might be inaccurate
	// n=m (or close) could be a total disaster
	
	if(m>n){
	    double [][]data_t=new double[m][n];
	    boolean [][]missing_t=new boolean[m][n];
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++){
		    data_t[j][i]=data[i][j];
		    missing_t[j][i]=missing[i][j];
		}
	    double [][]estimate_t=
		estimateMissingLeastSquares(data_t,missing_t);
	    double [][]estimate=new double[n][m];
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++)
		    estimate[i][j]=estimate_t[j][i];
	    return estimate;
	}
	
	//mean and covariance estimate calculation

	double [][]sigma=new double[m][m];
	double []mean=new double[m];

	double []row_mean=new double[n];
	double []row_stdv=new double[n];
	double avgstdv=0,avgm=0;

	for(int i=0;i<n;i++){
	    double c=0;
	    row_mean[i]=0;
	    for(int j=0;j<m;j++)
		//if(!missing[i][j])
		{
		    c++;
		    row_mean[i]+=data[i][j];
		}
	    if(c==0){
		//System.out.println("c="+c);
		row_stdv[i]=1;
		row_mean[i]=0;
	    }
	    else{
		row_mean[i]/=c;
		/*
		if(Math.abs(row_mean[i])>0.05)
		    System.out.print(row_mean[i]+" ");
		*/
		if(c>1){
		    double sum=0;
		    for(int j=0;j<m;j++)
			//if(!missing[i][j])
			{
			    double diff=data[i][j]-row_mean[i];
			    sum+=diff*diff;
			}
		    row_stdv[i]=Math.sqrt(sum/(c-1));
		}
		else
		    row_stdv[i]=1;
		avgm+=row_mean[i]*row_mean[i];
		avgstdv+=row_stdv[i];
		//if(Math.abs(row_stdv[i])<0.01) row_stdv[i]=1;
	    }
        
        counter++;
        if (Thread.interrupted()) throw new InterruptedException();
        
	}
	avgstdv/=(double)n;
	avgm/=(double)n;
	//System.out.println("avgstdv="+avgstdv+" avgm="+avgm);


	//alternatively transform data
	boolean transform=false;
	boolean varnorm=false;
	double [][]tdata=new double[n][m];
	if(transform)
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++){
		    tdata[i][j]=data[i][j]-row_mean[i];
		    if(varnorm) tdata[i][j]/=row_stdv[i];
		}
	else
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++)
		    tdata[i][j]=data[i][j]; 
		    
	boolean missingfreeonly=false;
	
	for(int i=0;i<m;i++){
	    mean[i]=0;
	    double []x=new double[n];
	    for(int a=0;a<n;a++){
		x[a]=tdata[a][i];
		mean[i]+=tdata[a][i];
	    }
	    mean[i]/=n;
	    for(int j=0;j<i+1;j++){
		double []y=new double[n];
		for(int a=0;a<n;a++)
		    y[a]=tdata[a][j];
		sigma[i][j]=covariance(x,y);
		sigma[j][i]=sigma[i][j];
		
	    }
	}

	double [][]estimate=new double[n][m];

	for(int i=0;i<n;i++){
	    int miss=0;
	    for(int j=0;j<m;j++)
		if(missing[i][j]) 
		    miss++;
		else
		    estimate[i][j]=tdata[i][j];
	    int n1=miss,n2=m-miss;

	    if(n1>0 && n2>0){
		double []x2=new double[n2];
		int []indx=new int[m];
		int c1=0,c2=n1;
		for(int j=0;j<m;j++)
		    if(missing[i][j]){
			indx[c1]=j;
			c1++;
		    }
		    else{
			indx[c2]=j;
			c2++;
		    }
		double []my=new double[m];
		double []xx=new double[m];
		double [][]S=new double[m][m];
		for(int j=0;j<m;j++){
		    my[j]=mean[indx[j]];
		    xx[j]=tdata[i][indx[j]];
		    for(int k=0;k<m;k++){
			S[j][k]=sigma[indx[j]][indx[k]];
		    }
		}
		double [][]S22=new double[n2][n2];
		for(int a=n1;a<m;a++)
		    for(int b=n1;b<m;b++){
			S22[a-n1][b-n1]=S[a][b];
		    }
		double [][]S22inv=Matrix.inverse(S22);
		
		//then to the prediction step
		
		double [][]S12xS22inv=new double[n1][n2];
		for(int a=0;a<n1;a++)
		    for(int b=0;b<n2;b++){
			double sum=0;
			for(int k=0;k<n2;k++)
			    sum+=S[a][n1+k]*S22inv[k][b];
			S12xS22inv[a][b]=sum;
		    }
		
		double []x1=new double[n1];
		for(int a=0;a<n1;a++){
		    double sum=0;
		    for(int b=0;b<n2;b++)
			sum+=S12xS22inv[a][b]*(xx[n1+b]-my[n1+b]);
		    x1[a]=my[a]+sum;
		}
		
		for(int a=0;a<n1;a++)
		    estimate[i][indx[a]]=x1[a];
	    }
	    if(n2==0) // the exception: nothing to estimate from
		for(int a=0;a<m;a++)
		    estimate[i][a]=mean[a];
            counter++;
            if (Thread.interrupted()) throw new InterruptedException();
	}
	
	//transform back data
	if(transform)
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++){
		    if(varnorm) estimate[i][j]*=row_stdv[i];
		    estimate[i][j]+=row_mean[i];
		    if(!missing[i][j] && 
		       Math.abs(estimate[i][j]-data[i][j])>1e-4)
			System.out.println("Warning!!"+ 
					   (estimate[i][j]-data[i][j]));
		}
	
	return estimate;
    }


    public static double [][]estimateMissingSelectedLeastSquares
	(double [][]data, boolean [][]missing){
	int n=data.length;
	int m=data[0].length;
	//n and m should have at least a factor 5 difference
	//otherwise the estimates might be inaccurate
	// n=m (or close) could be a total disaster
	
	if(m>n){
	    double [][]data_t=new double[m][n];
	    boolean [][]missing_t=new boolean[m][n];
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++){
		    data_t[j][i]=data[i][j];
		    missing_t[j][i]=missing[i][j];
		}
	    double [][]estimate_t=
		estimateMissingSelectedLeastSquares(data_t,missing_t);
	    double [][]estimate=new double[n][m];
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++)
		    estimate[i][j]=estimate_t[j][i];
	    return estimate;
	}
	
	//mean and covariance estimate calculation
	//alternatively use EM for maximum likelihood estimation
	double [][]sigma=new double[m][m];
	double []mean=new double[m];

	double []row_mean=new double[n];
	double []row_stdv=new double[n];
	double avgstdv=0,avgm=0;

	for(int i=0;i<n;i++){
	    double c=0;
	    row_mean[i]=0;
	    for(int j=0;j<m;j++)
		if(!missing[i][j]){
		    c++;
		    row_mean[i]+=data[i][j];
		}
	    if(c==0){
		//System.out.println("c="+c);
		row_stdv[i]=1;
		row_mean[i]=0;
	    }
	    else{
		row_mean[i]/=c;
		/*
		if(Math.abs(row_mean[i])>0.05)
		    System.out.print(row_mean[i]+" ");
		*/
		if(c>1){
		    double sum=0;
		    for(int j=0;j<m;j++)
			if(!missing[i][j]){
			    double diff=data[i][j]-row_mean[i];
			    sum+=diff*diff;
			}
		    row_stdv[i]=Math.sqrt(sum/(c-1));
		}
		else
		    row_stdv[i]=1;
		avgm+=row_mean[i]*row_mean[i];
		avgstdv+=row_stdv[i];
		//if(Math.abs(row_stdv[i])<0.01) row_stdv[i]=1;
	    }
	}
	avgstdv/=(double)n;
	avgm/=(double)n;
	//System.out.println("avgstdv="+avgstdv+" avgm="+avgm);


	//alternatively transform data
	boolean transform=false;
	boolean varnorm=false;
	double [][]tdata=new double[n][m];
	if(transform)
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++){
		    tdata[i][j]=data[i][j]-row_mean[i];
		    if(varnorm) tdata[i][j]/=row_stdv[i];
		}
	else
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++)
		    tdata[i][j]=data[i][j]; 
		    
	for(int i=0;i<m;i++){
	    double []x=new double[n];
	    mean[i]=0;
	    for(int a=0;a<n;a++){
		x[a]=tdata[a][i];
		mean[i]+=tdata[a][i];
	    }
	    mean[i]/=(double)n;
	    
	    for(int j=i;j<m;j++){
		double []y=new double[n];
		for(int a=0;a<n;a++)
		    y[a]=tdata[a][j];
		sigma[i][j]=covariance(x,y);
		sigma[j][i]=sigma[i][j];
	    }
	}

	double [][]estimate=new double[n][m];
	
	for(int i=0;i<n;i++){
	    int miss=0;
	    for(int j=0;j<m;j++)
		if(missing[i][j]) 
		    miss++;
		else
		    estimate[i][j]=tdata[i][j];
	    int n1=miss,n2=m-miss;
	    
	    int use=Math.min(10,n2);

	    if(n1>0 && n2>0){
		double [][]indata=new double[n][use+1];
		for(int j=0;j<m;j++)
		    if(missing[i][j]){
			double []corrs=new double[n2];
			int []indx=new int[n2];
			
			//find higest correlated columns
			int count=0;
			for(int k=0;k<m;k++)
			    if(!missing[i][k] && k!=j){
				indx[count]=k;
				corrs[count]=sigma[j][k]/
				    Math.sqrt(sigma[j][j]*sigma[k][k]);
				corrs[count]=Math.abs(corrs[count]);
				count++;
			    }
			parallelSort(corrs,indx);
			
			double [][]sigma22=new double[use][use];
			double []sigma12=new double[use]; 
			
			for(int a=0;a<use;a++){
			    sigma12[a]=sigma[j][indx[n2-1-a]];
			    for(int b=a;b<use;b++){
				sigma22[a][b]=
				    sigma[indx[n2-1-a]][indx[n2-1-b]];
				sigma22[b][a]=sigma22[a][b];
			    }
			}
			double [][]sigma22inv=Matrix.inverse(sigma22);
			
			double sum=mean[j];
			double []beta=new double[use];
			for(int a=0;a<use;a++)
			    for(int b=0;b<use;b++)
				beta[a]+=sigma12[b]*sigma22inv[b][a];

			for(int a=0;a<use;a++)
			    sum+=beta[a]*(tdata[i][indx[n2-1-a]]-
					  mean[indx[n2-1-a]]);
			estimate[i][j]=sum;
			//System.out.print("\r i="+i+" est="+estimate[i][j]);
		    }

	    }
	    if(n2==0) // the exception: nothing to estimate from
		for(int a=0;a<m;a++)
		    estimate[i][a]=mean[a];
	}
	
	//transform back data
	if(transform)
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++){
		    if(varnorm) estimate[i][j]*=row_stdv[i];
		    estimate[i][j]+=row_mean[i];
		    if(!missing[i][j] && 
		       Math.abs(estimate[i][j]-data[i][j])>1e-4)
			System.out.println("Warning!!"+ 
					   (estimate[i][j]-data[i][j]));
		}
	
	return estimate;
    }


    public static double[] subtractRowMean(double [][]data){
	int m=data[0].length;
	int n=data.length;
	double []means=new double[n];
	for(int i=0;i<n;i++){
	    double mean=0;
	    for(int j=0;j<m;j++)
		mean+=data[i][j];
	    mean/=(double)m;
	    means[i]=mean;
	    for(int j=0;j<m;j++)
		data[i][j]-=mean;
	}
	return means;
    }

    public static void addRowMean(double [][]data, double []means){
	int m=data[0].length;
	int n=data.length;
	for(int i=0;i<n;i++)
	    for(int j=0;j<m;j++)
		data[i][j]+=means[i];
    }


    public static double missingEstimate(double [][]data, int rowindx, 
					 int colindx){
	//computes multivariate expectations for missing value
	//missing value indicated by indexes

	int n=data.length;
	int m=data[0].length;

	if(n>m){
	    double [][]data_t=new double[m][n];
	    for(int i=0;i<n;i++)
		for(int j=0;j<m;j++)
		    data_t[j][i]=data[i][j];
	    return missingEstimate(data_t,colindx,rowindx);
	}

	double []sigma12=new double[n-1];
	double [][]sigma22=new double[n-1][n-1];
	double sigma11=0;
	double rowmean=0;
	double []dev=new double[n-1];

	int a=0,b=0;
	for(int i=0;i<n;i++){
	    if(i==rowindx){
		double []x=new double[m-1];
		int c=0;
		for(int k=0;k<m;k++)
		    if(k!=colindx){
			x[c]=data[i][k];
			c++; 
		    }
		sigma11=variance(x);
	    }
	    b=a;
	    for(int j=i;j<n && i!=rowindx;j++){
		if(j!=rowindx){
		    //System.out.println(a+" "+b);
		    double []x=new double[m-1];
		    double []y=new double[m-1];
		    int c=0;
		    for(int k=0;k<m;k++)
			if(k!=colindx){
			   x[c]=data[i][k];
			   y[c]=data[j][k];
			   c++; 
			}
		    sigma22[a][b]=covariance(x,y);
		    if(b!=a)
			sigma22[b][a]=sigma22[a][b];
		    b++;
		}
		
	    }
	    if(i!=rowindx){
		double []x=new double[m-1];
		double []y=new double[m-1];
		int c=0;
		for(int j=0;j<m;j++)
		    if(j!=colindx){
			x[c]=data[rowindx][j];
			y[c]=data[i][j];
			c++;
		    }
		//System.out.println("x:"+x[0]+" "+x[1]);
		sigma12[a]=covariance(x,y);
		//System.out.println("sigma?="+variance(x));
		//System.out.println("sigma12[0]="+sigma12[a]);
		//System.out.println(corr(x,y));
		a++;
	    }
	}
	for(int i=0;i<m;i++)
	    if(i!=colindx)
		rowmean+=data[rowindx][i];
	rowmean/=(double)(m-1);
	//System.out.println("Mean="+rowmean);

	double[][]sigma22inv;
	if(n-1>1){
	    sigma22inv=Matrix.inverse(sigma22);
	    /*
	    System.out.println("Sigma22:");
	    for(int i=0;i<n-1;i++){
		for(int j=0;j<n-1;j++)
		    System.out.print((float)sigma22[i][j]+"\t");
		System.out.println();
	    }
	    
	    System.out.println("Sigma22 inverse:");
	    for(int i=0;i<n-1;i++){
		for(int j=0;j<n-1;j++)
		    System.out.print((float)sigma22inv[i][j]+"\t");
		System.out.println();
	    }
	    
	    System.out.println("Sigma12:");
	    for(int i=0;i<n-1;i++)
		System.out.print((float)sigma12[i]+"\t");
	    System.out.println();
	    */
	}
	else{
	    sigma22inv=new double[1][1];
	    sigma22inv[0][0]=1/sigma22[0][0];
	    //System.out.println("sigma[0][0]="+sigma22[0][0]);
	}

	a=0;
	for(int i=0;i<n;i++){
	    if(i!=rowindx){
		double mean=0;
		for(int j=0;j<m;j++)
		    if(j!=colindx)
			mean+=data[i][j];
		mean/=(double)(m-1);
		
		dev[a]=data[i][colindx]-mean;
		//System.out.println("data="+data[i][colindx]+
		//	   " mean="+mean+" dev="+dev[a]+" a="+a);
		a++;
	    }
	}


	double correction=0;
	double estimate=rowmean;
		
	//compute correction
	for(int i=0;i<n-1;i++){
	    double tmp=0;
	    for(int j=0;j<n-1;j++){
		//System.out.println(sigma12[j]*sigma22inv[j][i]);
		tmp+=sigma12[j]*sigma22inv[j][i];
	    }
	    //System.out.println("tmp="+tmp+" *dev="+dev[i]);
	    correction+=tmp*dev[i];
	}
	double S2=0;
	for(int i=0;i<n-1;i++){
	    double tmp=0;
	    for(int j=0;j<n-1;j++){
		tmp+=sigma12[j]*sigma22inv[j][i];
	    }
	    S2+=tmp*sigma12[i];
	}

	//System.out.println("estimate="+estimate+" "+"correction="+correction);
	double unexplvar=(sigma11-S2)/sigma11;
	//return unexplvar;
	estimate+=correction;
	return estimate;

    }


    


    public static double missingEstimate2(double [][]data, int rowindx, 
					 int colindx){
	//computes bivariate expectations for missing value, and weights 
	//the results after similarity to vector to predict
	//missing value indicated by indexes

	int n=data.length;
	int m=data[0].length;
	double []estimate=new double[n-1];
	double []weight=new double[n-1];

	int a=0,b=0;
	double []x=new double[m-1];
	double rowmean=0;
	int c=0;
	for(int i=0;i<m;i++)
	    if(i!=colindx){
		x[c]=data[rowindx][i];
		rowmean+=data[rowindx][i];
		c++;
	    }
	rowmean/=(double)(m-1);
       
	for(int i=0;i<n;i++){
	    if(i!=rowindx){
		double []y=new double[m-1];
		
		double ymean=0;
		c=0;
		for(int j=0;j<m;j++)
		    if(j!=colindx){
			
			y[c]=data[i][j];
			ymean+=data[i][j];
			c++;
		    }
		ymean/=(double)(m-1);
		
		double r=corr(x,y);
		
		weight[a]=(r*r)/(1-r*r+1e-6);
		weight[a]*=weight[a];
		
		double cxy=covariance(x,y);
		double vary=variance(y);
		estimate[a]=rowmean+cxy*(data[i][colindx]-ymean)/vary;
		
		/*
		double corrsum=0;
		for(int j=0;j<n;j++)
		    if(j!=rowindx && j!=i){
			double r2=corr(data[i],data[j]);
			corrsum+=r2*r2;
		    }
		weight[a]/=corrsum;
		*/
		/*
		System.out.print((float)estimate[a]+" ("+
				 (float)data[i][colindx]+") ");
		*/
		a++;
	    }
	}
	
	/*
	System.out.print("Estimates: ");
	for(int i=0;i<n-1;i++)
	    System.out.print((float)estimate[i]+" ");
	System.out.println();
	*/
	/*
	System.out.print("Weights: ");
	for(int i=0;i<n-1;i++)
	    System.out.print((int)(weight[i]*100)+" ");
	System.out.println();
	*/
	double weightsum=0;
	double theest=0;
	for(int i=0;i<n-1;i++)
	    weightsum+=weight[i];
	
	for(int i=0;i<n-1;i++){
	    weight[i]/=weightsum;
	    theest+=estimate[i]*weight[i];
	}
	
	return theest;
    }

    
    public static double[][] estimateMissingRowMean(double [][]data, 
					     boolean[][] missing){
	int n=data.length;
	int m=data[0].length;
	double estimates[][]=new double[n][m];
	
	for(int i=0;i<n;i++){
	    double mean=0,num=0;
	    for(int j=0;j<m;j++)
		if(!missing[i][j]){
		    estimates[i][j]=data[i][j];
		    num++;
		    mean+=data[i][j];
		}
	    mean/=num;
	    for(int j=0;j<m;j++)
		if(missing[i][j]){
		    estimates[i][j]=mean;
		}
	}

	return estimates;
    }

    
    public static double binomialProb(int x, int n, double p){
	if(x>n)
	    return 0;
	double result=0;
	double xd=(double)x;
	double nd=(double)n;
	
	for(double i=xd+1;i<nd+1;i++)
	    result*=i*(1-p)/(i-xd);
	for(int i=0;i<x;i++)
	    result*=p;
	return result;
    }
    
    public static double binomialProbLarger(int x,int n,double p){
	double result=0;
	for(int i=x;i<=n;i++)
	    result+=binomialProb(i,n,p);
	return result;
    }

    public static double zapproxWilcoxon(double []vector1, double []vector2){
	double na=(double)vector1.length;
	double nb=(double)vector2.length;
	double wa=0;
	int n=(int)(na+nb);
	double []allobs=new double[n];
	int []source=new int[n];
	for(int i=0;i<(int)na;i++){
	    allobs[i]=vector1[i];
	    source[i]=1;
	}
	for(int i=0;i<(int)nb;i++){
	    allobs[i+(int)na]=vector2[i];
	    source[i+(int)na]=2;
	}
	/*
	System.out.print("All observations:");
	for(int i=0;i<n;i++)
	    System.out.print(" "+allobs[i]);
	System.out.println();
	*/
	parallelSort(allobs,source);
	/*
	System.out.print("Sorted:");
	for(int i=0;i<n;i++)
	    System.out.print(" "+allobs[i]);
	System.out.println();
	*/
	boolean ties=false;
	double []ranks=new double[(int)(na+nb)];
	for(int i=0;i<n;i++)
	    ranks[i]=i+1;
	double tiecorrectionfactor=1;
	for(int i=0;i<n-1;i++){
	    int j=i+1;
	    while(j<n && allobs[i]>=allobs[j])
		j++;
	    j--;
	    if(j>i){
		double newrank=(ranks[i]+ranks[j])/2;
		for(int k=i;k<=j;k++){
		    ranks[k]=newrank;
		}
		double q=j-i+1,N=n;
		tiecorrectionfactor-=(q*(q*q-1))/(n*(n*n-1));
		ties=true;
	    }
	    i=j;
	}
	//System.out.println("Tie correction factor: "+tiecorrectionfactor);
	if(na<=nb){
	    for(int i=0;i<n;i++)
		if(source[i]==1) wa+=ranks[i];
	}
	else{
	    for(int i=0;i<n;i++)
		if(source[i]==2) wa+=ranks[i];
	    double tmp=na;
	    na=nb;
	    nb=tmp;
	}
	if(!ties)
	    return (wa-na*(na+nb+1)/2)/Math.sqrt(na*nb*(na+nb+1)/12);
	else if(tiecorrectionfactor!=0)
	    return (wa-na*(na+nb+1)/2)/Math.sqrt(tiecorrectionfactor*na*nb*(na+nb+1)/12);
	else return 0;
    }

    public static double ratioBSSWSS(double []vector1, double []vector2){
	double am=0,bm=0,m=0;
	double n1=(double)vector1.length;
	double n2=(double)vector2.length;
	for(int i=0;i<vector1.length;i++){
	    am+=vector1[i];
	    m+=vector1[i];
	}
	for(int i=0;i<vector2.length;i++){
	    bm+=vector2[i];
	    m+=vector2[i];
	}
	am/=n1;
	bm/=n2;
	m/=(n1+n2);
	double bss=0,wss=0;
	bss=n1*(am-m)*(am-m)+n2*(bm-m)*(bm-m);
	for(int i=0;i<vector1.length;i++){
	    double diff=vector1[i]-am;
	    wss+=diff*diff;
	}
	for(int i=0;i<vector2.length;i++){
	    double diff=vector2[i]-bm;
	    wss+=diff*diff;
	}
	if(bss==0 || wss==0)
	    return 0;
	else
	    return bss/wss;
    }

    public static double sepScore(double []vector1, double []vector2){
	return tStatistic(vector1,vector2);
	//return zapproxWilcoxon(vector1,vector2);
	//return ratioBSSWSS(vector1,vector2);
	/*
	double obs [][]=new double [2][];
	obs[0]=vector1;
	obs[1]=vector2;
	return KWtest.KWstatistic(obs);
	*/
    }

    public static double pairedtStatistic(double []vector, 
					  int []pairdata){
	int count=0;
	int m=vector.length;
	for(int i=0;i<m;i++)
	    if(pairdata[i]>=0) count++;
	double []d=new double[count];
	count=0;
	for(int i=0;i<m;i++)
	    if(pairdata[i]>=0){
		d[count]=vector[i]-vector[pairdata[i]];
		count++;
	    }
	double dm=0,sd=0,n=d.length;
	for(int i=0;i<d.length;i++)
	    dm+=d[i];
	dm/=n;
	for(int i=0;i<d.length;i++){
	    double diff=(d[i]-dm);
	    sd+=diff*diff;
	}
	sd=Math.sqrt(sd/(n-1));
	return dm/(sd/Math.sqrt(n));
    }

    public static double[][] pairedRankProductStatistic(double [][]data, 
					  int []pairdata){
	int n=data.length;
        int m=data[0].length;
        int pairs=0;
        for(int i=0;i<m;i++)
            if(pairdata[i]>=0) pairs++;
        double [][]diffs=new double[n][pairs];
        for(int i=0;i<n;i++){
            int a=0;
            for(int j=0;j<m;j++)
                if(pairdata[j]>=0){
                    diffs[i][a]=data[i][j]-data[i][pairdata[j]];
                    a++;
                }
        }
        double[][] ranks=new double[n][pairs];
        double[] diff_i=new double[n];
        int []indx=new int[n];
        for(int i=0;i<pairs;i++){
            for(int j=0;j<n;j++){
                diff_i[j]=diffs[j][i];
                indx[j]=j;
            }
            parallelSort(diff_i,indx);
            for(int j=0;j<n;j++)
                ranks[indx[j]][i]=(j+1);
        }
        double [][]rp=new double[n][2];
        for(int i=0;i<n;i++){
            rp[i][0]=1;
            rp[i][1]=1;
            for(int j=0;j<pairs;j++){
                rp[i][0]*=ranks[i][j]/n;
                rp[i][1]*=(n+1-ranks[i][j])/n;
            }
                
        }
        return rp;
    }
    
    public static double pvalueProductProb(double prod, int nexp){
        double sum=prod;
        double c=-Math.log(prod);
        double fak=1;
        double lp=1;
        for(int i=1;i<=nexp-1;i++){
            fak*=i;
            lp*=c;
            sum+=prod*lp/fak;
        }
        return sum;
    }
    
    public static double entropy(int a, int b){
	if(a<=0 && b<=0) return 1;
	if(a<=0 || b<=0) return 0;
	double x=a,y=b;
	double c=Math.log(2);
	return -(x/(x+y))*(Math.log(x/(x+y))/c)
	    -(y/(x+y))*(Math.log(y/(x+y))/c);
    }


    public static void parallelSort(double []x, int[] y){
	//shellsort x and move along corresponding elements in y
	int n=x.length;
	int []increment=new int[30];//
	int c=2,count=-1;
        
	
	while(c<n/2 && count<29){
	    c*=2;
	    count++;
	    increment[count]=c-3;
	}
        if(count<0){
            count++;
            increment[count]=1;
        }
        
	//System.out.print("Increments: ");
	//for(int i=0;i<=count;i++) System.out.print(increment[i]+" ");
	//System.out.println();
	for(int a=count;a>=0;a--){
	    int incr=increment[a];
	    for(int i=incr;i<n;i++){
		if(x[i]<x[i-incr]){
		    int j=i;
		    double tmp=x[j];
		    int tmpy=y[j];
		    while(j>=incr && tmp<x[j-incr]){
			x[j]=x[j-incr];
			y[j]=y[j-incr];
			j-=incr;
		    }
		    x[j]=tmp;
		    y[j]=tmpy;
		}
	    }
	}
    }

    public static void parallelQuickSort(double []x, int []y, int l, int r){
	if(l<r){
	    int m=l;

	    //first some code to ensure we avoid a worst case split
	    int ind=r;
	    if(r-l+1>5){
		if(x[l]<Math.max(x[r],x[(l+r)/2]) && 
		   x[l]>Math.min(x[r],x[(l+r)/2]))
		    ind=l;
		if(x[(l+r)/2]<Math.max(x[l],x[r]) && 
		   x[(l+r)/2]>Math.min(x[l],x[r]))
		    ind=(l+r)/2;
	    }
	    double tmp1;
	    int tmp2;
	    if(ind<r){
		tmp1=x[ind];
		x[ind]=x[r];
		x[r]=tmp1;
		tmp2=y[ind];
		y[ind]=y[r];
		y[r]=tmp2;
	    }
	    
	    //then sort

	    for(int i=l;i<r;i++)
		if(x[i]<x[r]){
		    tmp1=x[m];
		    x[m]=x[i];
		    x[i]=tmp1;
		    tmp2=y[m];
		    y[m]=y[i];
		    y[i]=tmp2;
		    m++;
		}
	    
	    tmp1=x[m];
	    x[m]=x[r];
	    x[r]=tmp1;
	    tmp2=y[m];
	    y[m]=y[r];
	    y[r]=tmp2;
	    
	    parallelQuickSort(x,y,l,m-1);
	    parallelQuickSort(x,y,m+1,r);
	}
    }

}




