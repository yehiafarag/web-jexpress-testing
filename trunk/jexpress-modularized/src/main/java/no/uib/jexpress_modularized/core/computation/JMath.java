
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
package no.uib.jexpress_modularized.core.computation;

import java.io.Serializable;

public class JMath extends java.lang.Object implements Serializable{

    public boolean[][] nulls;
    int method = 0;
    // double[] buffer; //Used in median calculation..
    //Set to false if 1.0 is not to be added to the correlation
    public boolean correlationToDistance = true;
    private int indexa, indexb;

    /**
     * Creates new JMath
     */
    public JMath() {
    }

    public JMath(String metric) {
        if (metric.equals("sqeuclid")) {
            method = 0;
        } else if (metric.equals("euclid")) {
            method = 1;
        } else if (metric.equals("bray_curtis")) {
            method = 2;
        } else if (metric.equals("manhattan")) {
            method = 3;
        } else if (metric.equals("Cosine Correlation")) {
            method = 4;
        } else if (metric.equals("Pearson Correlation")) {
            method = 5;
        } else if (metric.equals("neuclid")) {
            method = 7;
        } else if (metric.equals("spearman")) {
            method = 10;
        }

        //else if(metric.equals( "man Rank Correlation" )){method=6;}     
        /*
         * else if(metric.equals( "canberra" )){method=2;} else
         * if(metric.equals( "sqchord" )){method=3;} else if(metric.equals(
         * "sqchi" )){method=4;} else if(metric.equals( "stcorr" )){method=5;}
         */

    }

    public void setMetric(int metric) {
        method = metric;
    }

    public int getMetric() {
        return (method);
    }

    public void setNulls(boolean[][] nulls) {
        this.nulls = nulls;
    }

    public static void fillCombo(javax.swing.JComboBox combo) {
        combo.removeAllItems();
        combo.addItem("Squared Euclidean");
        combo.addItem("Euclidean");
        //combo.addItem("Euclidean (Nullweighted)");
        //combo.addItem("Canberra");
        //combo.addItem("Squared Chord");
        //combo.addItem("Squared Chi-Squared");
        combo.addItem("Bray Curtis");
        combo.addItem("Manhattan");
        combo.addItem("Cosine Correlation");
        combo.addItem("Pearson Correlation");
        combo.addItem("Uncentered Pearson Correlation");
        combo.addItem("Euclidean (Nullweighted)");
        combo.addItem("Camberra");
        combo.addItem("Chebychev");
        combo.addItem("Spearman Rank Correlation");

    }

    public String getMetricsString() {
        if (method == 0) {
            return "Squared Euclidean";
        } else if (method == 1) {
            return "Euclidean";
        } else if (method == 2) {
            return "Bray Curtis";
        } else if (method == 3) {
            return "Manhattan";
        } else if (method == 4) {
            return "Cosine Correlation";
        } else if (method == 5) {
            return "Pearson Correlation";
        } else if (method == 6) {
            return "Uncentered Pearson Correlation";
        } else if (method == 7) {
            return "Euclidean (Nullweighted)";
        } else if (method == 8) {
            return "Camberra";
        } else if (method == 9) {
            return "Chebychev";
        } else if (method == 10) {
            return "Spearman Rank Correlation";
        } else {
            return "Unknown (error)";
        }

    }

    public double dist(double[] s, double[] t) {
        if (method == 0) {
            return sqeuclid(s, t);
        } else if (method == 1) {
            return euclid(s, t);
        } /*
         * else if(method==2) return canberra(s,t); else if(method==3) return
         * sqchord(s,t); else if(method==4) return sqchi(s,t);
         */ else if (method == 2) {
            return bray_curtis(s, t);
        } else if (method == 3) {
            return manhattan(s, t);
        } else if (method == 4) {
            return stcorr(s, t);
        } else if (method == 5) {
            return pcorr(s, t);
        } else if (method == 6) {
            return uncpcorr(s, t);
        } else if (method == 7) {
            return nullEuclid(s, t);
        } else if (method == 8) {
            return Camberra(s, t);
        } else if (method == 9) {
            return Chebychev(s, t);
        } else if (method == 10) {
            return spearman(s, t);
        } else {
            return 0.0;
        }
    }

    public double dist(double[] s, double[] t, boolean[] nulls, boolean[] nullt) {
        if (method == 0) {
            return sqeuclid(s, t);
        } else if (method == 1) {
            return euclid(s, t);
        } /*
         * else if(method==2) return canberra(s,t); else if(method==3) return
         * sqchord(s,t); else if(method==4) return sqchi(s,t);
         */ else if (method == 2) {
            return bray_curtis(s, t);
        } else if (method == 3) {
            return manhattan(s, t);
        } else if (method == 4) {
            return stcorr(s, t);
        } else if (method == 5) {
            return pcorr(s, t);
        } else if (method == 6) {
            return uncpcorr(s, t);
        } else if (method == 7) {
            return nullEuclid(s, t, nulls, nullt);
        } else if (method == 8) {
            return Camberra(s, t);
        } else if (method == 9) {
            return Chebychev(s, t);
        } else if (method == 10) {
            return spearman(s, t);
        } else {
            return 0.0;
        }
    }

    public boolean isCorrelation() {
        if (method == 4) {
            return true;
        } else if (method == 5) {
            return true;
        } else if (method == 6) {
            return true;
        } else if (method == 10) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * public double dist(double[] s,double[] t,int indexa,int indexb){
     * this.indexa=indexa; this.indexb=indexb; if(method==0) return
     * sqeuclid(s,t); else if(method==1) return euclid(s,t);
     *
     * else if(method==2) return bray_curtis(s,t); else if(method==3) return
     * manhattan(s,t); else if(method==4) return stcorr(s,t); else if(method==5)
     * return pcorr(s,t); else if(method==6) return uncpcorr(s,t); else
     * if(method==7) return nullEuclid(s,t); else if(method==8) return
     * Camberra(s,t); else if(method==9) return Chebychev(s,t); //else
     * if(method==6) return spearman(s,t);
     *
     * else return 0.0; }
     */
    public double sqeuclid(double[] s, double[] t) {
        double ret = 0.0;
        double[] temp = new double[s.length];

        for (int i = 0; i < s.length; i++) {

            ret = ret + Math.pow(s[i] - t[i], 2.0);
        }
        return ret;
    }

    //Sqrt(Sum( (x[i]-y[i])^2 )).
    public double euclid(double[] s, double[] t) {
        double ret = 0.0;
        double[] temp = new double[s.length];

        for (int i = 0; i < s.length; i++) {

            ret = ret + Math.pow(s[i] - t[i], 2.0);
        }
        return Math.sqrt(ret);
    }

    //Not working.. Sum( abs(x[i]-y[i]) / abs(x[i]+y[i]) )

    /*
     * public double canberra(double[] s,double[]t){ double ret=0.0; tools
     * tl=new tools(); Double test=null;
     *
     * for(int i=0;i<s.length;i++){
     *
     * if(s[i]==0.0 && t[i]==0.0) test=new Double(100000.0);
     *
     * else test=new Double( Math.abs(s[i]-t[i]) / Math.abs((s[i]+t[i])) );
     * if(!test.isNaN()) ret=ret + test.doubleValue();
     *
     *
     *
     * }
     * //if(ret>1000000)ret=1000000;
     *
     * ret=tl.log(10,ret);
     *
     * //System.out.print("\ncanberra : "+ret); return ret; }
     */
    public double sqchord(double[] s, double[] t) {


        double ret = 0.0;
        Double test = null;

        for (int i = 0; i < s.length; i++) {
            test = new Double(Math.sqrt(s[i]) - Math.sqrt(t[i]));

            if (!test.isNaN() && test.doubleValue() < 1e10) {
                ret = ret + test.doubleValue();
            }
        }

        return ret;
    }

    public double sqchi(double[] s, double[] t) {
        double ret = 0.0;
        Double test = null;

        for (int i = 0; i < s.length; i++) {

            test = new Double((Math.sqrt(s[i]) - t[i]) / (s[i] + t[i]));
            if (!test.isNaN() && test.doubleValue() < 1e10) {
                ret = ret + test.doubleValue();
            }
        }




        return ret;
    }

    //Standard korrelasjon.....
    public double stcorr(double[] s, double[] t) {
        double ret = 0.0;
        //Double test=null;

        for (int i = 0; i < s.length; i++) {
            //test=new Double( ( s[i] * t[i] ) );
            ret = ret + (s[i] * t[i]);
        }

        double ss = 0.0;
        double tt = 0.0;

        for (int i = 0; i < s.length; i++) {
            ss += s[i] * s[i];//Math.pow(s[i],2.0);
            tt += t[i] * t[i];//Math.pow(t[i],2.0);
        }

        tt = Math.sqrt(tt);
        ss = Math.sqrt(ss);

        double nevner = ss * tt;

        if (nevner < 0.01) {
            nevner = 0.01;
        }

        ret = ret / nevner;

        //test=new Double(ret);
        if (correlationToDistance) {
            return 1 - ret;
        } else {
            return ret;
        }
    }

    /*
     * //Rank korrelasjon..... public double stcorr(double[] s,double[] t){
     * double ret=0.0; Double test=null;
     *
     * for(int i=0;i<s.length;i++){ test=new Double( ( s[i] * t[i] ) ); ret=ret
     * +test.doubleValue(); }
     *
     * double ss=0.0; double tt=0.0;
     *
     * for(int i=0;i<s.length;i++){ ss+=s[i]*s[i];//Math.pow(s[i],2.0);
     * tt+=t[i]*t[i];//Math.pow(t[i],2.0); }
     *
     * tt=Math.sqrt(tt); ss=Math.sqrt(ss);
     *
     * double nevner=ss*tt;
     *
     * if(nevner < 0.01) nevner=0.01;
     *
     * ret=ret/nevner;
     *
     * test=new Double(ret);
     *
     * return 1-ret; }
     *
     */
    //Pearson korrelasjon.....
    public double pcorr(double[] s, double[] t) {
        double ret = 0.0;
        Float test = null;

        double[] sc = new double[s.length];
        double[] tc = new double[s.length];

        double means = 0;
        double meant = 0;

        for (int i = 0; i < s.length; i++) {
            means += s[i];
            meant += t[i];
        }

        means = means / s.length;
        meant = meant / s.length;

        for (int i = 0; i < s.length; i++) {

            sc[i] = s[i] - means;
            tc[i] = t[i] - meant;

            //test=new Float( ( sc[i] * tc[i] ) );
            ret = ret + (sc[i] * tc[i]);//test.doubleValue();
        }

        ret = ret / (s.length - 1);

        double ss = 0.0;
        double tt = 0.0;

        for (int i = 0; i < s.length; i++) {
            ss += Math.pow(sc[i], 2);//sc[i]*sc[i];
            tt += Math.pow(tc[i], 2);//tc[i]*tc[i];
        }

        ss = ss / (s.length - 1);
        tt = tt / (s.length - 1);

        //ss=Math.sqrt(ss);
        //tt=Math.sqrt(tt);


        double nevner = Math.sqrt(ss * tt);
        //nevner = Math.sqrt(nevner);

        if (nevner < 0.01) {
            nevner = 0.01;
        }

        ret = ret / nevner;

        //      test=new Float(ret);

        //return 1- Math.abs(ret); 
        if (correlationToDistance) {
            return 1 - ret;
        } else {
            return ret;
        }
    }

    public double uncpcorr(double[] s, double[] t) {
        double ret = 0.0;
        Float test = null;

        double[] sc = new double[s.length];
        double[] tc = new double[s.length];

        double means = 0;
        double meant = 0;

        for (int i = 0; i < s.length; i++) {
            means += s[i];
            meant += t[i];
        }

        means = means / s.length;
        meant = meant / s.length;

        for (int i = 0; i < s.length; i++) {

            sc[i] = s[i] - means;
            tc[i] = t[i] - meant;

            //test=new Float( ( sc[i] * tc[i] ) );
            ret = ret + (s[i] * t[i]);//test.doubleValue();
        }

        ret = ret / (s.length - 1);

        double ss = 0.0;
        double tt = 0.0;

        for (int i = 0; i < s.length; i++) {
            ss += Math.pow(sc[i], 2);
            tt += Math.pow(tc[i], 2);
        }

        // ss=ss/(s.length-1);
        // tt=tt/(s.length-1);

        ss = Math.sqrt(ss);
        tt = Math.sqrt(tt);



        double nevner = ss * tt;
        //nevner = Math.sqrt(nevner);

        //  if(nevner < 1) nevner=1;

        ret = ret / nevner;

        //      test=new Float(ret);

        //return 1- Math.abs(ret); 
        if (correlationToDistance) {
            return 1 - ret;
        } //else return Math.abs(ret);
        else {
            return ret;
        }
    }

    //Sum( abs(x[i]-y[i]) ) / Sum( x[i]+y[i] ).
    public double bray_curtis(double[] s, double[] t) {
        double sum1 = 0.0;
        double sum2 = 0.0;

        double[] temp = new double[s.length];

        for (int i = 0; i < s.length; i++) {

            sum1 = sum1 + Math.abs(s[i] - t[i]);
            sum2 = sum2 + Math.abs(s[i] + t[i]);
        }



        return sum1 / sum2;
    }

    //Also called city block..  Sum( abs(x[i]-y[i]) ).
    public double manhattan(double[] s, double[] t) {
        double ret = 0.0;
        double[] temp = new double[s.length];

        for (int i = 0; i < s.length; i++) {

            ret = ret + Math.abs(s[i] - t[i]);
        }

        return ret;
    }

    public double nullEuclid(double[] s, double[] t, boolean[] nullss, boolean[] nullst) {
        double ret = 0.0;

        //boolean cols[] = new boolean[s.length];
        int counter = 0;

        //if(nulls==null) return euclid(s,t);
        double sum = 0;

        if (nulls != null) {
            for (int i = 0; i < s.length; i++) {
                if ((!nullss[i] && !nullst[i]) && !Double.isNaN(s[i]) && !Double.isNaN(t[i])) {
                    sum += Math.pow(s[i] - t[i], 2.0);
                    counter++;
                }
            }

            sum = sum / counter;
        } else {
            for (int i = 0; i < s.length; i++) {
                if ((!nullss[i] && !nullst[i]) && !Double.isNaN(s[i]) && !Double.isNaN(t[i])) {

                    sum += Math.pow(s[i] - t[i], 2.0);
                    counter++;
                }
            }

            sum = sum / counter;



        }


        return Math.sqrt(sum);

    }

    public double nullEuclid(double[] s, double[] t) {
        double ret = 0.0;

        //boolean cols[] = new boolean[s.length];
        int counter = 0;

        //if(nulls==null) return euclid(s,t);
        double sum = 0;

        if (nulls != null) {
            for (int i = 0; i < s.length; i++) {
                if (!Double.isNaN(s[i]) && !Double.isNaN(t[i])) {
                    sum += Math.pow(s[i] - t[i], 2.0);
                    counter++;
                }
            }

            sum = sum / counter;
        } else {
            for (int i = 0; i < s.length; i++) {
                if (!Double.isNaN(s[i]) && !Double.isNaN(t[i])) {

                    sum += Math.pow(s[i] - t[i], 2.0);
                    counter++;
                }
            }

            sum = sum / counter;



        }


        return Math.sqrt(sum);

    }

    /*
     * public double spearman(double[] s,double[]t){ double ret=0.0; double[]
     * temp=new double[s.length];
     *
     * double[] rnksx = new double[s.length]; double[] rnksy = new
     * double[t.length];
     *
     * for(int i=0;i<s.length;i++){ rnksx[i]=s[i]; rnksy[i]=t[i]; }
     *
     *
     * java.util.Arrays.sort(rnksx); java.util.Arrays.sort(rnksy);      *
     * double[] ss = new double[s.length]; double[] tt = new double[t.length];
     *
     * double[] chs =null; double[] cht =null;
     *
     * try{ chs = (double[]) rnksx.clone(); cht = (double[]) rnksy.clone(); }
     * catch(Exception e){}
     *
     * for(int i=0;i<s.length;i++){ for(int j=0;j<s.length;j++){
     *
     * if(s[i]== rnksx[j]){ ss[i]=j+1; rnksx[j]=Double.POSITIVE_INFINITY;
     * break;} //if(s[i]== rnksx[j]){ ss[i]=j+1; break;} } }
     *
     * for(int i=0;i<t.length;i++){ for(int j=0;j<t.length;j++){
     *
     * if(t[i]== rnksy[j]){ tt[i]=j+1; rnksy[j]=Double.POSITIVE_INFINITY;
     * break;} //if(t[i]== rnksy[j]){ tt[i]=j+1; break;} } }
     *
     * int eqstart=-1; int eqend=-1; int rnk=0;
     *
     * for(int i=0;i<rnksx.length-1;i++){ if(chs[i]==chs[i+1] && eqstart==-1){
     * eqstart=i; rnk+=ss[i]; System.out.print("\nS");}
     *
     * if(chs[i]!=chs[i+1] && eqstart!=-1){ eqend=i ; rnk+=ss[i];
     * System.out.print("\neeE");}
     *
     * //if(i==rnksx.length-2 && rnksx[i]==rnksx[i+1]) eqend=i+1;
     *
     * if(eqstart!=-1 && eqend!=-1){ for(int j=eqstart;j<eqend;j++){ ss[j] =
     * rnk/(eqend-eqstart+1); System.out.print("\nstart: "+eqstart);
     * System.out.print("\nend: "+eqend); } eqstart=-1; eqend=-1; rnk=0; } }
     *
     *
     * double cnst = (s.length+1)/2;
     *
     * for(int i=0;i<s.length;i++){
     *
     * // System.out.print("\nvals:"+s[i]+" rank:"+ss[i]); //
     * System.out.print("\nvalt:"+t[i]+" rank:"+tt[i]);
     *
     * // ret=ret + Math.pow(ss[i]-tt[i],2); ret+= (ss[i] - cnst) * (tt[i] -
     * cnst); }
     *
     *
     *
     * double nevner =s.length * ((s.length*s.length)-1); nevner = nevner/12;
     * ret = ret/nevner; // ret=1-ret; return ret; }
     */
    public double spearman(double[] x, double[] y) {

        double result;


        int n = x.length;
        double nevner = (n * (n * n - 1)) / 12.0;
        double median = (n + 1.0) / 2.0;

        //Rank arrays
        double[] rx = new double[n];
        double[] ry = new double[n];


        //Read data into two sequences.
        double[] modx = new double[x.length];
        double[] mody = new double[y.length];

        //Sorter
        int[] arrx = JDoubleSorter.quickSort(x);
        int[] arry = JDoubleSorter.quickSort(y);

        for (int i = 0; i < arrx.length; i++) {
            modx[i] = x[arrx[i]];

            mody[i] = y[arry[i]];
        }

        calcRanks(rx, modx, arrx);

        calcRanks(ry, mody, arry);

        double teller = 0.0;

        for (int a = 0; a < n; a++) {
            //System.out.println(rx[a]+ ","+ ry[a]);
            teller += ((rx[a] - median) * (ry[a] - median));

        }

        result = teller / nevner;
        //System.out.print("\n>"+result);    
        //return result;

        if (correlationToDistance) {
            return 1 - result;
        } else {
            return result;
        }


    }

    public void calcRanks(double[] ranks, double[] sorted, int[] indx) {
        int i = 0;
        int wrank = 1;
        int numtie = 0;
        int k;
        double commonRank;

        while (i < ranks.length) {
            k = i;
            numtie = 1;

            while (k < ranks.length - 1) {
                if (sorted[k] == sorted[k + 1]) {

                    numtie++;
                    k++;
                } else {
                    break;
                }
            }

            if (numtie == 1) {
                ranks[indx[i]] = wrank;
                wrank++;
                i++;
            } else {
                commonRank = wrank + (numtie - 1) * 0.5;
                for (int a = i; a <= k; a++) {
                    ranks[indx[a]] = commonRank;
                }
                wrank = wrank + numtie;
                i = i + numtie;

            }
        }

    }

    //rs = 1 - 6*(d12 + d22 + ... + dn2)/(n(n2-1)), 
    public double Camberra(double[] s, double[] t) {
        double ret = 0;

        for (int i = 0; i < s.length; i++) {
            ret += Math.abs(((s[i] - t[i]) / (s[i] + t[i])));
        }
        return ret;
    }

    public double Chebychev(double[] s, double[] t) {
        double ret = Double.MIN_VALUE;

        for (int i = 0; i < s.length; i++) {

            if (Math.abs(s[i] - t[i]) > ret) {
                ret = Math.abs(s[i] - t[i]);
            }

        }
        return ret;
    }

    public static void main(String[] args) {
        java.text.DecimalFormat dfrm = new java.text.DecimalFormat("0.#E0");
        dfrm.setMaximumFractionDigits(2);//decFormat.setMaximumFractionDigits( 7 );
        dfrm.setMinimumFractionDigits(2);
        //double d = 0.000032;
        double d = 0.32;
        System.out.print("\n" + dfrm.format(d));
        if (true) {
            return;
        }

        //JMath m = new JMath("Spearman Rank Correlation");   
        //double d = m.dist(new double[]{23, 43, 63, 83, 103, 123, 143, 163, 183},new double[]{0,4,63,64,73,0,79,21,99});
        //double d = m.dist(new double[]{89,90,75,30,51,75,62,45,90,20},new double[]{2,3,4,45,55,7,9,13,15,14});

        //double d = m.dist(new double[]{5,2,10,7,1,8,3,9,4,6},new double[]{6.5,9,2.5,2.5,9,2.5,9,2.5,6.5,5});
        //double d= m.spearman(new double[]{5,2,10,7,1,8,3,9,4,6},new double[]{6.5,9,2.5,2.5,9,2.5,9,2.5,6.5,5});

        // double[] d = new double[]{10,10,10,9};


        //System.out.print("\nANS: "+JMath.mean(d));



    }

    public static double median(double[] array) {

        if (array.length == 2) {
            return (array[0] + array[1]) / 2;
        } else {
            java.util.Arrays.sort(array);

            // System.out.print("\nmmm:"+(double)Math.round(array.length/2.0)+" = "+(double)(array.length/2.0));   

            if ((double) (Math.round(array.length / 2.0)) != (double) (array.length / 2.0)) {


                return array[array.length / 2];

            } else {

                // System.out.print("\nHE:"+array.length/2.0);

                return (array[array.length / 2] + array[(array.length / 2) - 1]) / 2;
            }

        }
    }

    public static double mean(double[] array) {

        if (array.length == 1) {
            return array[0];
        } else if (array.length == 2) {
            return (array[0] + array[1]) / 2;
        } else {
            double ret = 0;
            for (int i = 0; i < array.length; i++) {
                ret += array[i];
            }

            return ret / array.length;
        }

    }

    public static double median(double[] array, int length) {

        if (length == 2) {
            return (array[0] + array[1]) / 2;
        } else {
            java.util.Arrays.sort(array, 0, length);

            // System.out.print("\nmmm:"+(double)Math.round(array.length/2.0)+" = "+(double)(array.length/2.0));   

            if ((double) (Math.round(length / 2.0)) != (double) (length / 2.0)) {


                return array[length / 2];

            } else {

                // System.out.print("\nHE:"+array.length/2.0);

                return (array[length / 2] + array[(length / 2) - 1]) / 2;
            }

        }
    }

    /*
     * public static double median(double[] array,int[] indexes,double[]
     * buffer){
     *
     * if(indexes.length==2) return (array[indexes[0]]+array[indexes[1]])/2;
     * else{
     *
     * if(buffer==null || buffer.length<indexes.length){ buffer=new
     * double[indexes.length+50]; System.out.print("*"); } for(int
     * i=0;i<indexes.length;i++) buffer[i]=array[indexes[i]];
     *
     *
     * java.util.Arrays.sort(buffer,0,indexes.length);      *
     * // System.out.print("\nmmm:"+(double)Math.round(array.length/2.0)+" =
     * "+(double)(array.length/2.0));      *
     * if( (double)( Math.round(indexes.length/2.0)) !=
     * (double)(indexes.length/2.0)){
     *
     *
     * return buffer[indexes.length/2];
     *
     * }
     * else{
     *
     * // System.out.print("\nHE:"+array.length/2.0);
     *
     * return (buffer[indexes.length/2]+buffer[(indexes.length/2)-1])/2; }
     *
     * }
     * }
     */
    public static double mean(double[] array, int length) {

        if (length == 1) {
            return array[0];
        } else if (length == 2) {
            return (array[0] + array[1]) / 2;
        } else {
            double ret = 0;
            for (int i = 0; i < length; i++) {
                ret += array[i];
            }

            return ret / length;
        }

    }

    public static double[] meanAndStDev(double[] array, int length) {
        double[] ret = new double[2];

        ret[0] = mean(array, length);

        //var=0.0;
        for (int j = 0; j < length; j++) {
            ret[1] += (Math.pow(array[j] - ret[0], 2));
        }
        ret[1] = ret[1] / (length - 1);

        ret[1] = Math.sqrt(ret[1]);


        return ret;
    }

    public static double[] medianAndStDev(double[] array, int length) {
        double[] ret = new double[2];

        ret[0] = median(array, length);
        double mean = mean(array, length);

        //var=0.0;
        for (int j = 0; j < length; j++) {
            ret[1] += (Math.pow(array[j] - mean, 2));
        }
        ret[1] = ret[1] / (length - 1);

        ret[1] = Math.sqrt(ret[1]);


        return ret;
    }
    /*
     * public static double mean(double[] array,int[] indexes){
     *
     * if(indexes.length==1) return array[0]; else if(indexes.length==2) return
     * (array[indexes[0]]+array[indexes[1]])/2; else{ double ret = 0; for(int
     * i=0;i<indexes.length;i++) ret+=array[indexes[i]];
     *
     * return ret/indexes.length; }
     *
     * }
     */
    /*
     * public static double[] meanAndStDev(double[] array,int[] indexes){
     * double[] ret = new double[2];
     *
     * ret[0]=mean(array,indexes);
     *
     * //var=0.0; for(int j=0;j<indexes.length;j++){
     * ret[1]+=(Math.pow(array[indexes[j]]-ret[0],2)); }
     * ret[1]=ret[1]/(indexes.length-1);
     *
     * ret[1]=Math.sqrt(ret[1]);
     *
     *
     * return ret; }
     */
    /*
     * public static double[] medianAndStDev(double[] array,int[]
     * indexes,double[] buffer){ double[] ret = new double[2];
     *
     * double mean = mean(array,indexes);
     *
     * ret[0]=median(array,indexes,buffer);
     *
     * //var=0.0; for(int j=0;j<indexes.length;j++){
     * ret[1]+=(Math.pow(array[indexes[j]]-mean,2)); }
     * ret[1]=ret[1]/(indexes.length-1);
     *
     * ret[1]=Math.sqrt(ret[1]);
     *
     *
     * return ret; }
     *
     */
}
