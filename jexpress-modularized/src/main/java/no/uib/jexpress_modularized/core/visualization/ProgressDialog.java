package no.uib.jexpress_modularized.core.visualization;

import java.io.Serializable;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * A dialog for displaying information about progress.
 *
 * @author  Harald Barsnes
 */
public class ProgressDialog extends javax.swing.JDialog implements Serializable{

    /**
     * The progress dialog parent.
     */
    private ProgressDialogParent progressDialogFrame;
    /**
     * If set to true, trying to close the progess bar will be ignored.
     * Use this option if the process being monitored can not be stopped.
     */
    private boolean doNothingOnClose = false;

    /**
     * Opens a new ProgressDialog with a Frame as a parent
     * 
     * @param parent
     * @param progressDialogFrame 
     * @param modal
     */
    public ProgressDialog(java.awt.Frame parent, ProgressDialogParent progressDialogFrame, boolean modal) {
        super(parent, modal);
        initComponents();

        this.progressDialogFrame = progressDialogFrame;

        // only works for Java 1.6 and newer
//        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().
//                getResource("...GIF")));

        setLocationRelativeTo(parent);
    }

    /**
     * Opens a new ProgressDialog with a JDialog as a parent
     * 
     * @param parent
     * @param progressDialogFrame 
     * @param modal
     */
    public ProgressDialog(javax.swing.JDialog parent, ProgressDialogParent progressDialogFrame, boolean modal) {
        super(parent, modal);

        initComponents();

        this.progressDialogFrame = progressDialogFrame;

        // only works for Java 1.6 and newer
//        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().
//                getResource("/no/uib/...GIF")));

        setLocationRelativeTo(parent);
    }

    /**
     * Opens a new ProgressDialog with a ProgressDialogParent as a parent
     *
     * @param parent
     * @param modal
     */
    public ProgressDialog(ProgressDialogParent parent, boolean modal) {

        this.setModal(true);

        initComponents();

        this.progressDialogFrame = parent;

        // only works for Java 1.6 and newer
//        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().
//                getResource("/no/uib/...GIF")));

        setLocationRelativeTo(null);
    }

    /**
     * Sets the progress bar value.
     * 
     * @param value the progress bar value
     */
    public void setValue(final int value) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setValue(value);
            }
        });
    }

    /**
     * Increases the progress value by 1
     */
    public void incrementValue() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setValue(progressBar.getValue()+1);
            }
        });
    }

    /**
     * Increases the progress value by n
     */
    public void incrementValue(int n) {
        final int increment = n;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setValue(progressBar.getValue()+increment);
            }
        });
    }

    /**
     * Sets the maximum value of the progress bar
     * 
     * @param value the maximum value
     */
    public void setMax(final int value) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setMaximum(value);
            }
        });
    }

    /**
     * Makes the dialog indeterminate or not indeterminate. Also 
     * turns the paint progress string on or off.
     * 
     * @param intermidiate
     * 
     * @deprecated Replaced by setIndeterminate, from utilities 3.1.17.
     * @see #setIndeterminate(boolean) 
     */
    public void setIntermidiate(final boolean intermidiate) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setStringPainted(!intermidiate);
                progressBar.setIndeterminate(intermidiate);
            }
        });
    }
    
    /**
     * Makes the dialog indeterminate or not indeterminate. Also 
     * turns the paint progress string on or off.
     * 
     * @param indeterminate
     */
    public void setIndeterminate(final boolean indeterminate) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setStringPainted(!indeterminate);
                progressBar.setIndeterminate(indeterminate);
            }
        });
    }

    /**
     * Sets the string to display in the progrss bar. For example to show 
     * the name of the file currently being converted.
     * 
     * @param currentFileName
     */
    public void setString(final String currentFileName) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setStringPainted(currentFileName != null);
                progressBar.setString(currentFileName);
            }
        });
    }

    /**
     * This method makes it impossible to close the dialog. Used when 
     * the method monitored by the progres bar can not be stopped.
     */
    public void doNothingOnClose() {
        doNothingOnClose = true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Please Wait");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        progressBar.setFont(progressBar.getFont().deriveFont(progressBar.getFont().getSize()-1f));
        progressBar.setStringPainted(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Closes the dialog (if it can be closed).
     * 
     * @param evt
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (!doNothingOnClose) {

            progressDialogFrame.cancelProgress();
            
            this.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_formWindowClosing
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables

    /**
     * Returns the progress bar for updates from external processes.
     * 
     * @return the progress bar
     */
    public JProgressBar getProgressBar() {
        return progressBar;
    }
}
