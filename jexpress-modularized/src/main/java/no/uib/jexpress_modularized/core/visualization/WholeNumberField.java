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
package no.uib.jexpress_modularized.core.visualization;

import java.io.Serializable;

public class WholeNumberField extends javax.swing.JTextField implements Serializable{

    private java.awt.Toolkit toolkit;
    private java.text.NumberFormat integerFormatter;

    public WholeNumberField(int value, int columns) {





        super(columns);



        setHorizontalAlignment((int) RIGHT);

        toolkit = java.awt.Toolkit.getDefaultToolkit();

        integerFormatter = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);

        integerFormatter.setParseIntegerOnly(true);

        setValue(value);

        addfocus();

    }

    public WholeNumberField() {

        super();

        setHorizontalAlignment((int) RIGHT);

        toolkit = java.awt.Toolkit.getDefaultToolkit();

        integerFormatter = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);

        integerFormatter.setParseIntegerOnly(true);

        setValue(0);

        addfocus();

    }

    public void addfocus() {

        addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent e) {

                selectAll();

            }
        });



    }

    public int getValue() {

        int retVal = 0;

        try {

            retVal = integerFormatter.parse(getText()).intValue();

        } catch (java.text.ParseException e) {

            // This should never happen because insertString allows

            // only properly formatted data to get in the field.

            toolkit.beep();

        }

        return retVal;

    }

    public void setValue(int value) {

        setText(integerFormatter.format(value));

    }

    protected javax.swing.text.Document createDefaultModel() {

        return new WholeNumberField.WholeNumberDocument();

    }

    protected class WholeNumberDocument extends javax.swing.text.PlainDocument {

        public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {

            char[] source = str.toCharArray();

            char[] result = new char[source.length];

            int j = 0;



            for (int i = 0; i < result.length; i++) {

                if (Character.isDigit(source[i]) || source[i] == '-') {
                    result[j++] = source[i];
                }

                // else {

                //     toolkit.beep();

                //     System.err.println("insertString: " + source[i]);

                // }

            }

            super.insertString(offs, new String(result, 0, j), a);

        }
    }
}
