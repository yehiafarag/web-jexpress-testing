/*
 * Curve.java
 *
 * Created on 7. august 2002, 10:06
 */
package no.uib.jexpress_modularized.core.visualization.charts;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;
import no.uib.jexpress_modularized.core.visualization.StripedPanel;

/**
 *
 * @author bjarte dysvik
 */
public class Curve extends JPanel implements MouseListener, MouseMotionListener, Serializable{

    public static final int HERMITE = 0;
    public static final int BEZIER = 1;
    public static final int BSPLINE = 2;
    private int mode = HERMITE;
    public static final int ADD = 0;
    public static final int MOVE = 1;
    public static final int DELETE = 2;
    private int action = ADD;
    double[] YVALS;
    int width = 200;
    int height = 200;
    private Vector points = new Vector(16, 4);
    // If a control point is being moved, this is the index into the list
    // of the moving point.  Otherwise it contains -1
    private int moving_point;
    private int precision;
    private float eMatrix[][] = new float[4][4];
    // Initialize the curve-type matrices
    private static float bezierMatrix[][] = {
        {-1, 3, -3, 1},
        {3, -6, 3, 0},
        {-3, 3, 0, 0},
        {1, 0, 0, 0},};
    private static float hermiteMatrix[][] = {
        {2, -2, 1, 1},
        {-3, 3, -2, -1},
        {0, 0, 1, 0},
        {1, 0, 0, 0}
    };
    private static float mult = (float) (1.0 / 6.0);
    private static float bsplineMatrix[][] = {
        {-mult, 3 * mult, -3 * mult, mult},
        {3 * mult, -6 * mult, 3 * mult, 0},
        {-3 * mult, 0, 3 * mult, 0},
        {mult, 4 * mult, mult, 0}
    };

    public static void main(String args[]) {
        JFrame f = new JFrame("Curve");

        StripedPanel pan = new StripedPanel();
        pan.setColor1(Color.green);
        pan.setColor1(new Color(204, 204, 204));

        final Curve curve = new Curve();

        pan.setLayout(new BorderLayout());
        pan.add("Center", curve);
        curve.setOpaque(false);

        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        f.getContentPane().add("Center", pan);
        f.setVisible(true);

        JButton b = new JButton("test");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                curve.getCurveValue();

            }
        });


        f.getContentPane().add("South", b);
        f.pack();

    }

    public Point getPoint1() {
        ControlPoint pnt = (ControlPoint) points.elementAt(1);
        return new Point(pnt.x, pnt.y);
    }

    public Point getPoint2() {
        ControlPoint pnt = (ControlPoint) points.elementAt(2);
        return new Point(pnt.x, pnt.y);
    }

    public void setPointLocation(Point p1, Point p2) {
        ControlPoint pnt = (ControlPoint) points.elementAt(1);
        pnt.x = p1.x;
        pnt.y = p1.y;

        pnt = (ControlPoint) points.elementAt(2);
        pnt.x = p2.x;
        pnt.y = p2.y;

    }

    public Curve() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        setCurveType(BEZIER);
        //setBackground(Color.white);

        setPrecision(50);

        addMouseListener(this);
        addMouseMotionListener(this);

        points.addElement(new ControlPoint(0, height));
        points.addElement(new ControlPoint((int) (width * 0.25), (int) (height * 0.75)));
        points.addElement(new ControlPoint((int) (width * 0.75), (int) (height * 0.25)));
        points.addElement(new ControlPoint(width, 0));
        setAction(MOVE);

    }

    private void calcEMatrix(int prec) {
        // In order to use the "forward difference" method of curve plotting,
        // we must generate this matrix.  The parameter indicates the precision;
        // the number of line segments to use for each curve.

        float step = (float) (1.0 / (float) prec);

        eMatrix[0][0] = 0;
        eMatrix[0][1] = 0;
        eMatrix[0][2] = 0;
        eMatrix[0][3] = 1;

        eMatrix[1][2] = step;
        eMatrix[1][1] = eMatrix[1][2] * step;
        eMatrix[1][0] = eMatrix[1][1] * step;
        eMatrix[1][3] = 0;

        eMatrix[2][0] = 6 * eMatrix[1][0];
        eMatrix[2][1] = 2 * eMatrix[1][1];
        eMatrix[2][2] = 0;
        eMatrix[2][3] = 0;

        eMatrix[3][0] = eMatrix[2][0];
        eMatrix[3][1] = 0;
        eMatrix[3][2] = 0;
        eMatrix[3][3] = 0;
    }

    public void setAction(int action) {
        // Change the action type
        switch (action) {
            case ADD:
            case MOVE:
            case DELETE:
                this.action = action;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setCurveType(int mode) {
        // Change the curve display type
        switch (mode) {
            case HERMITE:
            case BEZIER:
            case BSPLINE:
                this.mode = mode;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setPrecision(int prec) {
        precision = prec;
        calcEMatrix(prec);
    }

    public void clearPoints() {
        points.removeAllElements();
    }

    private int findPoint(int a, int b) {
        // Scan the list of control points to find out which (if any) point
        // contains the coordinates: a,b.
        // If a point is found, return the point's index, otherwise return -1
        int max = points.size();

        for (int i = 0; i < max; i++) {
            ControlPoint pnt = (ControlPoint) points.elementAt(i);
            if (pnt.within(a, b)) {
                return i;
            }
        }
        return -1;
    }

    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseDragged(java.awt.event.MouseEvent e) {
        if (moving_point >= 0 && new Rectangle(0, 0, width, height).contains(e.getPoint())) {
            ControlPoint pnt = (ControlPoint) points.elementAt(moving_point);
            pnt.x = e.getX();
            pnt.y = e.getY();
            repaint();

            this.firePropertyChange("updated", 0, 1);
        }
    }

    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseMoved(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mousePressed(java.awt.event.MouseEvent e) {
        moving_point = findPoint(e.getX(), e.getY());
        repaint();
    }

    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
        moving_point = -1;
    }

    private void multMatrix(float m[][], float g[][], float mg[][]) {
        // This function performs the meat of the calculations for the
        // curve plotting.  Note that it is not a matrix multiplier in the
        // pure sense.  The first matrix is the curve matrix (each curve type
        // has its own matrix), and the second matrix is the geometry matrix
        // (defined by the control points).  The result is returned in the
        // third matrix.

        // First clear the return array
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                mg[i][j] = 0;
            }
        }

        // Perform the matrix math
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 4; k++) {
                    mg[i][j] = mg[i][j] + (m[i][k] * g[k][j]);
                }
            }
        }
    }

    public void paint(Graphics g) {

        //g.setColor(getBackground());
        //g.fillRect(0,0,width,height);

        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        int np = points.size();
        float geom[][] = new float[4][2];
        float mg[][] = new float[4][2];
        float plot[][] = new float[4][2];

        g.setColor(Color.blue);
        g.setPaintMode();

        // draw a border around the canvas
        //g.drawRect(0,0, size().width-1, size().height-1);

        // draw the control points
        for (int i = 1; i < np - 1; i++) {
            ControlPoint p = (ControlPoint) points.elementAt(i);
            g.drawRect(p.x - p.PT_SIZE, p.y - p.PT_SIZE, p.PT_SIZE * 2, p.PT_SIZE * 2);

            //g.drawString(String.valueOf(i),p.x+p.PT_SIZE,p.y-p.PT_SIZE);
        }

        g.setColor(Color.green);

        YVALS = new double[width];
        for (int i = 0; i < YVALS.length; i++) {
            YVALS[i] = -1;
        }

        for (int i = 0; i < np - 3;) {
            // Four control points are needed to create a curve.
            // If all the control points are used, the last series of four 
            // points begins with point np-4.
            switch (mode) {
                // The geometry matrix for a series of control points is
                // different for each curve type.
                case (HERMITE):
                    geom[0][0] = ((ControlPoint) points.elementAt(i)).x;
                    geom[0][1] = ((ControlPoint) points.elementAt(i)).y;
                    geom[1][0] = ((ControlPoint) points.elementAt(i + 3)).x;
                    geom[1][1] = ((ControlPoint) points.elementAt(i + 3)).y;
                    geom[2][0] = ((ControlPoint) points.elementAt(i + 1)).x - geom[0][0];
                    geom[2][1] = ((ControlPoint) points.elementAt(i + 1)).y - geom[0][1];
                    geom[3][0] = geom[1][0] - ((ControlPoint) points.elementAt(i + 2)).x;
                    geom[3][1] = geom[1][1] - ((ControlPoint) points.elementAt(i + 2)).y;
                    multMatrix(hermiteMatrix, geom, mg);
                    // The beginning of the next Hermite curve is the last
                    // point of the previous curve.
                    i += 3;
                    break;
                case (BEZIER):
                    for (int j = 0; j < 4; j++) {
                        geom[j][0] = ((ControlPoint) points.elementAt(i + j)).x;
                        geom[j][1] = ((ControlPoint) points.elementAt(i + j)).y;
                    }
                    multMatrix(bezierMatrix, geom, mg);
                    // The beginning of the next Bezier curve is the last
                    // point of the previous curve.
                    i += 3;
                    break;
                case (BSPLINE):
                    for (int j = 3; j >= 0; j--) {
                        geom[3 - j][0] = ((ControlPoint) points.elementAt(i + j)).x;
                        geom[3 - j][1] = ((ControlPoint) points.elementAt(i + j)).y;
                    }
                    multMatrix(bsplineMatrix, geom, mg);
                    // B-Spline is the slowest curve, since the beginning of
                    // the next series of four control points is the second
                    // control point of the previous series.
                    i++;
                    break;
            }

            // In order to plot the curve using forward differences
            // (a speedier way to plot the curve), another matrix
            // calculation is required, taking into account the precision
            // of the curve.
            multMatrix(eMatrix, mg, plot);
            float startX = plot[0][0];
            float x = startX;
            float startY = plot[0][1];
            float y = startY;
            // Plot the curve using the forward difference method
            for (int j = 0; j < precision; j++) {
                x += plot[1][0];
                plot[1][0] += plot[2][0];
                plot[2][0] += plot[3][0];
                y += plot[1][1];
                plot[1][1] += plot[2][1];
                plot[2][1] += plot[3][1];
                g.drawLine((int) startX, (int) startY, (int) x, (int) y);
                startX = x;
                startY = y;
                //System.out.print("\n"+(int)startX);
                try {
                    YVALS[(int) startX - 1] = (double) startY;
                } catch (Exception e) {
                }

            }
        }
    }

    public void createCurve() {

        int np = points.size();
        float geom[][] = new float[4][2];
        float mg[][] = new float[4][2];
        float plot[][] = new float[4][2];

        YVALS = new double[width];
        for (int i = 0; i < YVALS.length; i++) {
            YVALS[i] = -1;
        }

        for (int i = 0; i < np - 3;) {
            // Four control points are needed to create a curve.
            // If all the control points are used, the last series of four 
            // points begins with point np-4.
            switch (mode) {
                // The geometry matrix for a series of control points is
                // different for each curve type.
                case (HERMITE):
                    geom[0][0] = ((ControlPoint) points.elementAt(i)).x;
                    geom[0][1] = ((ControlPoint) points.elementAt(i)).y;
                    geom[1][0] = ((ControlPoint) points.elementAt(i + 3)).x;
                    geom[1][1] = ((ControlPoint) points.elementAt(i + 3)).y;
                    geom[2][0] = ((ControlPoint) points.elementAt(i + 1)).x - geom[0][0];
                    geom[2][1] = ((ControlPoint) points.elementAt(i + 1)).y - geom[0][1];
                    geom[3][0] = geom[1][0] - ((ControlPoint) points.elementAt(i + 2)).x;
                    geom[3][1] = geom[1][1] - ((ControlPoint) points.elementAt(i + 2)).y;
                    multMatrix(hermiteMatrix, geom, mg);
                    // The beginning of the next Hermite curve is the last
                    // point of the previous curve.
                    i += 3;
                    break;
                case (BEZIER):
                    for (int j = 0; j < 4; j++) {
                        geom[j][0] = ((ControlPoint) points.elementAt(i + j)).x;
                        geom[j][1] = ((ControlPoint) points.elementAt(i + j)).y;
                    }
                    multMatrix(bezierMatrix, geom, mg);
                    // The beginning of the next Bezier curve is the last
                    // point of the previous curve.
                    i += 3;
                    break;
                case (BSPLINE):
                    for (int j = 3; j >= 0; j--) {
                        geom[3 - j][0] = ((ControlPoint) points.elementAt(i + j)).x;
                        geom[3 - j][1] = ((ControlPoint) points.elementAt(i + j)).y;
                    }
                    multMatrix(bsplineMatrix, geom, mg);
                    // B-Spline is the slowest curve, since the beginning of
                    // the next series of four control points is the second
                    // control point of the previous series.
                    i++;
                    break;
            }

            // In order to plot the curve using forward differences
            // (a speedier way to plot the curve), another matrix
            // calculation is required, taking into account the precision
            // of the curve.
            multMatrix(eMatrix, mg, plot);
            float startX = plot[0][0];
            float x = startX;
            float startY = plot[0][1];
            float y = startY;
            // Plot the curve using the forward difference method
            for (int j = 0; j < precision; j++) {
                x += plot[1][0];
                plot[1][0] += plot[2][0];
                plot[2][0] += plot[3][0];
                y += plot[1][1];
                plot[1][1] += plot[2][1];
                plot[2][1] += plot[3][1];
                startX = x;
                startY = y;
                try {
                    YVALS[(int) startX - 1] = (double) startY;
                } catch (Exception e) {
                }

            }
        }
    }

    public double[] getYvals() {
        repaint();
        getCurveValue();
        return YVALS;
    }

    public void getCurveValue() {

        double startX = 0;
        double endX = 0;

        createCurve();

        YVALS[0] = width;
        YVALS[YVALS.length - 1] = 0;

        for (int i = 0; i < YVALS.length; i++) {

            if (YVALS[i] == -1) {

                //Case when there is no values to the left..
                if (startX == -1) {
                    int j = i;
                    while (YVALS[j] == -1) {
                        if (j == YVALS.length - 2) {
                            break;
                        }
                        j++;
                    }
                    startX = YVALS[j];
                    for (int k = i; k < j; k++) {
                        YVALS[k] = startX;
                    }
                } //case when there are missing values in between..
                else {
                    int j = i;
                    while (YVALS[j] == -1) {
                        if (j > YVALS.length - 2) {
                            break;
                        }
                        j++;
                    }
                    endX = YVALS[j];

                    if (j == YVALS.length - 2) {
                        for (int k = i; k < j; k++) {
                            YVALS[k] = startX;
                        }
                    } else {
                        for (int k = i; k < j; k++) {
                            YVALS[k] = startX + ((double) (endX - startX) * ((double) (k - i) / (double) (j - i)));
                        }
                    }
                }

            } else {
                startX = YVALS[i];
            }

            if (YVALS[i] < 0) {
                YVALS[i] = 0;
            }
            if (YVALS[i] > width) {
                YVALS[i] = width;
            }

            YVALS[i] = (width - YVALS[i]) / width;
        }
    }
}
