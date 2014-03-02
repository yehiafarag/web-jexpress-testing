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

public class Scrollpic extends javax.swing.JLabel implements javax.swing.Scrollable , Serializable{

    private int imheight, imwidth;
    private int maxUnitIncrement = 1;
    javax.swing.ImageIcon icon;

    public Scrollpic(javax.swing.ImageIcon i, int m) {



        super(i);

        icon = i;

        imheight = i.getIconHeight();

        imwidth = i.getIconWidth();



        //super.setBackground(Color.white);

        //setBorder(new EtchedBorder());

        maxUnitIncrement = m;

    }

    public int getimageheight() {

        return imheight;
    }

    public int getimagewidth() {

        return imwidth;
    }

    public java.awt.Image getImage() {

        return icon.getImage();
    }

    public void setImage(java.awt.Image i) {

        icon.setImage(i);



    }

    public java.awt.Dimension getPreferredScrollableViewportSize() {

        return getPreferredSize();

    }

    public int getScrollableUnitIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {

        //Get the current position.

        int currentPosition = 0;

        if (orientation == javax.swing.SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }



        //Return the number of pixels between currentPosition

        //and the nearest tick mark in the indicated direction.

        if (direction < 0) {

            int newPosition = currentPosition
                    - (currentPosition / maxUnitIncrement)
                    * maxUnitIncrement;

            return (newPosition == 0) ? maxUnitIncrement : newPosition;

        } else {

            return ((currentPosition / maxUnitIncrement) + 1)
                    * maxUnitIncrement - currentPosition;

        }

    }

    public int getScrollableBlockIncrement(java.awt.Rectangle visibleRect, int orientation, int direction) {

        if (orientation == javax.swing.SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }

    }

    public boolean getScrollableTracksViewportWidth() {

        return false;

    }

    public boolean getScrollableTracksViewportHeight() {

        return false;

    }

    public void setMaxUnitIncrement(int pixels) {

        maxUnitIncrement = pixels;

    }
}
