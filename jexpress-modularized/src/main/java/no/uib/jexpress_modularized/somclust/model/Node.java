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
package no.uib.jexpress_modularized.somclust.model;

import java.util.Stack;
import java.util.Vector;

public class Node implements Cloneable, java.io.Serializable {

    //static final long serialVersionUID = -163549931968437L;
    static final long serialVersionUID = -7186374095564129430L;
    public Node right, left, parent;
    public float[][] M;
    public double value;
    public int nme;
    public double xcor, ycor;
    public int members = 1;
    public boolean merged = false;
    public double longestspanval;
    private java.awt.Color color;
    public boolean branched = false;
    public boolean mark = false;

    public java.awt.Color getColor() {
        return color;
    }

    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    public int getx() {
        return (int) xcor;
    }

    public int gety() {
        return (int) ycor;
    }

    public double getval() {

        return value;
    }

    public Node(int name) {

        this.nme = name;

    }

    public Node(float value) {

        this.value = value;

        merged = true;
    }

    public Node(Node gen1, Node gen2, float value) {

        members = gen1.members + gen2.members;

        this.value = (double) value;

        merged = true;

        right = gen1;
        left = gen2;

    }

    public double calcvectordistance(double[] vector1, double[] vector2) {

        double ret = 0;


        for (int i = 0; i < vector1.length; i++) {

            if (vector1[i] > vector2[i]) {
                ret = ret + vector1[i] - vector2[i];
            } else {
                ret = ret + Math.pow((vector2[i] - vector1[i]), 2);
            }

        }

        ret = ret * ret;

        return Math.sqrt(ret);
    }

    public Node(String name) {
        merged = false;
    }

    @Override
    public Object clone() {

        Node ret;

        if (merged) {

            ret = new Node((float) value);

            ret.right = (Node) right.clone();

            ret.left = (Node) left.clone();

        } else {
            ret = new Node(nme);
        }

        return ret;

    }

    public void fillMembers(Vector v, Stack st) {
        if (!merged) {
            v.addElement(new Integer(nme));
            return;
        }
        v.clear();
        st.clear();
        Node tmp = null;
        st.push(this);

        while (!st.empty()) {
            tmp = (Node) st.pop();

            if (tmp.merged) {
                st.push(tmp.right);
                st.push(tmp.left);
                //System.out.print( tmp.value );
            } else {
                v.addElement(new Integer(tmp.nme));
            }
        }
    }

    public void normalizeMergedValues() {

        Stack st = new Stack();
        st.push(this);
        Node node = null;

        double max = -1;

        while (!st.isEmpty()) {
            node = (Node) st.pop();

            if (node.value > max) {
                max = node.value;
            }

            if (node.merged) {
                st.push(node.left);
                st.push(node.right);
            }
        }

        st.push(this);
        while (!st.isEmpty()) {
            node = (Node) st.pop();

            node.value = node.value / max;

            if (node.merged) {
                st.push(node.left);
                st.push(node.right);
            }
        }
    }
}
