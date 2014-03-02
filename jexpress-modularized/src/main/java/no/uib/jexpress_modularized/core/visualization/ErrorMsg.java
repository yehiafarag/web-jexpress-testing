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
import javax.swing.JOptionPane;

public class ErrorMsg extends Object implements Serializable {

//ErrorDialog err;
    String[] errors = {
        "There are elements in the selected area that can not be traslated to numeric values, use the missing value converter.",
        "J-Express can not connect to the internet. Please use a web-browser and point it to http://molmine.com to connect manually.\n(If you clicked a link this link may not be compatible with this browser.)",
        "Error. Data could not be loaded",
        "Dataloading was not successful.\nThe file may be in a wrong format.",
        "Can not write to file. File might be in use by other process.",
        "J-express has run out of memory!\nIt might help to create a smaller dendrogram.",
        "Cannot connect to the site ",
        "Some data has been removed, The branch can not be executed.",
        "Cannot save data, make sure the file is not in use",
        "The axis used for the PCA plot had a greater index than the dimension of the eigenvalues and has been reset. You may change this setting in the PCA properties dialog.",
        "Input to this method should be an all-positive dataset.",
        /*11*/ "One or more columns were not found in the data file",
        /*12*/ "One or more columns can not be converted to a numberic value",
        /*13*/ "The Raw data files are not of the same length",
        /*14*/ "The Info file is not of the same length as the data file",
        /*15*/ "The File is not recognized as an GenePix file",
        /*16*/ "Something went wrong in the data import.\nCheck that the files have the right format and contains the columns you want to load.",
        /*17*/ "J-express has run out of memory"
    };

    public ErrorMsg(int errorcode) {

//err = new ErrorDialog();

//err.msgpane.setText(errors[errorcode]);

//err.ok.addActionListener(this);

//err.setVisible(true);

        JOptionPane.showMessageDialog(null, errors[errorcode], "Error", JOptionPane.ERROR_MESSAGE);

    }

    public ErrorMsg(int errorcode, String message) {

//err = new ErrorDialog();

//err.msgpane.setText(errors[errorcode]+"<br>"+message+"</HTML>");

//err.ok.addActionListener(this);

//err.setVisible(true);

        JOptionPane.showMessageDialog(null, errors[errorcode] + "\n" + message, "Error", JOptionPane.ERROR_MESSAGE);

    }

//for missing values in the dataloader.
    public ErrorMsg(int x, int y) {

//err = new ErrorDialog();

//err.msgpane.setText("There are elements in the selected area (["+String.valueOf(x) +"]["+String.valueOf(y)+"]) that can not be traslated to numeric values, use the missing value converter");

//err.msgpane.setText(errors[0]);

//err.ok.addActionListener(this);

//err.setVisible(true);

        JOptionPane.showMessageDialog(null, errors[0], "Error", JOptionPane.ERROR_MESSAGE);



    }
}
