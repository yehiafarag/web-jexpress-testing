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

/*

 * ImageSelection.java

 *

 * Created on 31. mai 2001, 13:50

 *  Testclass for transforming a Image to clipboard..

 */
package no.uib.jexpress_modularized.core.visualization;

import java.awt.*;

import java.awt.datatransfer.*;

import java.io.*;

import javax.swing.*;

import java.awt.datatransfer.DataFlavor;

/**
 *
 *
 *
 * @author bjarte
 *
 * @version  *
 */
public class ImageSelection extends TransferHandler implements java.awt.datatransfer.Transferable, Serializable {

    private static final DataFlavor flavors[] = {DataFlavor.imageFlavor};
    private Image image;

    public int getSourceActions(JComponent c) {

        return TransferHandler.COPY;

    }

    public boolean canImport(JComponent comp, DataFlavor flavor[]) {

        if (!(comp instanceof JLabel)
                || (comp instanceof AbstractButton)) {

            return false;

        }

        for (int i = 0, n = flavor.length; i < n; i++) {

            if (flavor[i].equals(flavors[0])) {

                return true;

            }

        }

        return false;

    }

    public Transferable createTransferable(JComponent comp) {

        // Clear

        image = null;

        Icon icon = null;



        if (comp instanceof JLabel) {

            JLabel label = (JLabel) comp;

            icon = label.getIcon();

        } else if (comp instanceof AbstractButton) {

            AbstractButton button = (AbstractButton) comp;

            icon = button.getIcon();

        }

        if (icon instanceof ImageIcon) {

            image = ((ImageIcon) icon).getImage();

            return this;

        }

        return null;

    }

    public boolean importData(JComponent comp,
            Transferable t) {

        ImageIcon icon = null;

        try {

            if (t.isDataFlavorSupported(flavors[0])) {

                image = (Image) t.getTransferData(flavors[0]);

                icon = new ImageIcon(image);

            }

            if (comp instanceof JLabel) {

                JLabel label = (JLabel) comp;

                label.setIcon(icon);

                return true;

            } else if (comp instanceof AbstractButton) {

                AbstractButton button = (AbstractButton) comp;

                button.setIcon(icon);

                return true;

            }

        } catch (UnsupportedFlavorException ignored) {
        } catch (IOException ignored) {
        }

        return false;

    }

    // Transferable
    public Object getTransferData(DataFlavor flavor) {

        if (isDataFlavorSupported(flavor)) {

            return image;

        }

        return null;

    }

    public DataFlavor[] getTransferDataFlavors() {

        return flavors;

    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {

        return flavor.equals(flavors[0]);

    }
}
/*static public DataFlavor ImageFlavor;

 private DataFlavor[] flavors = {ImageFlavor}; 

 private java.awt.Image image;

 static {

 try {

 ImageFlavor = new DataFlavor(

 Class.forName("java.awt.Image"), "AWT Image");

 }

 catch(ClassNotFoundException e) {

 e.printStackTrace();

 }

 }

 public ImageSelection(java.awt.Image image) {

 this.image = image;

 }

 public synchronized DataFlavor[] getTransferDataFlavors() {

 return flavors;	}

 public boolean isDataFlavorSupported(DataFlavor flavor) {

 return flavor.equals(ImageFlavor);

 }

 public synchronized Object getTransferData(DataFlavor flavor)  throws java.io.IOException {

 if(flavor.equals(ImageFlavor)) {

 return image;

 }

 else return null;

 }

 public void lostOwnership(java.awt.datatransfer.Clipboard c, java.awt.datatransfer.Transferable t) {

 }

 }

 */
