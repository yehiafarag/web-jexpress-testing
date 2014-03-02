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

import java.awt.image.BufferedImage;
import javax.swing.filechooser.FileFilter;
//import jexpress.cluster;
import javax.swing.*;
//import expresscomponents.ProjectNodes.Node;
import java.awt.Image;
import java.awt.FileDialog;
import java.io.*;


public final class filehandler extends java.awt.Component implements Serializable {

    public static final String PROP_LAST_MODIFIED = "lastModified";
    public static final String PROP_DATABASE_ID = "DatabaseId";
    public static final String PROP_NAME = "name";
    DataOutputStream dataout;
    FileOutputStream ostream, ostream2;
    FileInputStream istream, istream2;
    java.io.DataInputStream datain;
    boolean project;
    private boolean test = false;
    private boolean Preparetest = false;
    java.awt.Label Status;
    int[][] adata, values;
    int[] agenvalues;
    gen[] gendata;
    String version;
    public String lastpath;
    int minval, maxval;
    boolean[] stepovergen;
    //cluster cl;
    int minvalue, maxvalue, clusters, lengde, bredde;
    String clusvector[];

    public static void main(String[] args) {
        filehandler f = new filehandler(args);
    }

    public filehandler(String lastpath) {
        this.lastpath = lastpath;
    }
    /*
     * public filehandler(String lastpath,cluster cl){ this.lastpath=lastpath;
     * this.cl=cl; }
     *
     * public filehandler(cluster cl){ this.cl=cl; }
     */

    public filehandler() {
    }

    public filehandler(String[] args) {
    }

    public double intdubval(String s) {
        double ret = 0;
        if (s == null) {
            s = "0";
        }
        Double temp = new Double(s);
        ret = temp.doubleValue();
        return ret * 1000;
    }

    public void saveimage(Image image) {
        JFileChooser fd;
        File file;

        no.uib.jexpress_modularized.core.visualization.extern.PNG.PngEncoder png = new no.uib.jexpress_modularized.core.visualization.extern.PNG.PngEncoder();
        if (image != null) {

            fd = new JFileChooser();
            fd.setFileFilter(new FileFilter() {

                public boolean accept(File f) {
                    if (f.getName().endsWith(".png")) {
                        return true;
                    } else if (f.isDirectory()) {
                        return true;
                    } else {
                        return false;
                    }
                }

                public String getDescription() {
                    return "PNG image files (*.png)";
                }
            });

            if (lastpath != null && lastpath != "") {
                fd.setCurrentDirectory(new File(lastpath));
            }
            //fd.showSaveDialog(new JFrame());
            int returnVal = fd.showSaveDialog(new JFrame());
            file = fd.getSelectedFile();
            File dir = fd.getCurrentDirectory();


            if (file != null && !file.isDirectory() && returnVal == JFileChooser.APPROVE_OPTION) {
                if (!file.getAbsolutePath().toLowerCase().endsWith(".png")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }

                this.lastpath = dir.getAbsolutePath();
                try {
                    png.encode((BufferedImage) image, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void saveimage3(Image image) {
        FileDialog fd;
        //File file;
        no.uib.jexpress_modularized.core.visualization.extern.BMPFile bmp = new no.uib.jexpress_modularized.core.visualization.extern.BMPFile();
        if (image != null) {
            fd = new FileDialog(new java.awt.Frame("Enter filename"));
            fd.setFile(".png");
            fd.setMode(FileDialog.SAVE);
            fd.setFilenameFilter(new java.io.FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.endsWith(".bmp")) {
                        return true;
                    } else {
                        return false;
                    }

                }
            });



            if (lastpath != null && lastpath != "") {
                fd.setDirectory(lastpath);
            }

            fd.setVisible(true);
            //file=new File(fd.getDirectory()+fd.getFile());
            if (fd.getFile() != null) {
                this.lastpath = fd.getDirectory();
                bmp.saveBitmap(fd.getDirectory() + fd.getFile(), ((BufferedImage) image).getRGB(0, 0, ((BufferedImage) image).getWidth(), ((BufferedImage) image).getHeight(), null, ((BufferedImage) image).getWidth(), ((BufferedImage) image).getWidth()), ((BufferedImage) image).getWidth(), ((BufferedImage) image).getHeight());



                //saveimage(image,file.getAbsolutePath());
            }
        }
    }

    public void saveimage2(Image image) {
        FileDialog fd;
        File file;

        if (image != null) {
            fd = new FileDialog(new java.awt.Frame("Enter filename"));
            fd.setFile(".gif");
            fd.setMode(FileDialog.SAVE);

            if (lastpath != null && lastpath != "") {
                fd.setDirectory(lastpath);
            }

            fd.setVisible(true);
            file = new File(fd.getDirectory() + fd.getFile());
            if (fd.getFile() != null) {
                this.lastpath = fd.getDirectory();
                saveimage(image, file.getAbsolutePath());
            }
        }
    }

    public void saveimage(Image image, String filename) {
        File file = new File(filename);
        no.uib.jexpress_modularized.core.visualization.extern.PNG.PngEncoder png = new no.uib.jexpress_modularized.core.visualization.extern.PNG.PngEncoder();
        try {
            png.encode((BufferedImage) image, file);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void saveimageOLD(Image image, String filename) {
        File file = new File(filename);

        try {

            FileOutputStream output = new FileOutputStream(file);
            BufferedOutputStream buffer = new BufferedOutputStream(output);
            //ge=new GifEncoder(image,output,true);
            //new ImageToGif(image, output, false);
            //ge.encode();

            int pixels[][] = getPixels(image);

            int palette[] = no.uib.jexpress_modularized.core.visualization.extern.Quantize.quantizeImage(pixels, 256);

            Image ret = setImage(palette, pixels);
            /*
             * JFrame fr = new JFrame(); JLabel l = new JLabel(new
             * ImageIcon(image)); fr.getContentPane().add("Center",l);
             * fr.pack(); fr.setVisible(true);
             */


            no.uib.jexpress_modularized.core.visualization.extern.GIFEncoder encoder = new no.uib.jexpress_modularized.core.visualization.extern.GIFEncoder(ret);
            encoder.Write(buffer);

            output.close();

        } catch (IOException e) {
        } catch (java.awt.AWTException ae) {
        }


    }

    public Image setImage(int palette[], int pixels[][]) {
        int w = pixels.length;
        int h = pixels[0].length;
        int pix[] = new int[w * h];

        // convert to RGB
        for (int x = w; x-- > 0;) {
            for (int y = h; y-- > 0;) {
                pix[y * w + x] = palette[pixels[x][y]];
            }
        }
        return createImage(new java.awt.image.MemoryImageSource(w, h, pix, 0, w));

    }

    static int[][] getPixels(Image image) throws IOException {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        int pix[] = new int[w * h];
        java.awt.image.PixelGrabber grabber = new java.awt.image.PixelGrabber(image, 0, 0, w, h, pix, 0, w);

        try {
            if (grabber.grabPixels() != true) {
                throw new IOException("Grabber returned false: "
                        + grabber.status());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int pixels[][] = new int[w][h];
        for (int x = w; x-- > 0;) {
            for (int y = h; y-- > 0;) {
                pixels[x][y] = pix[y * w + x];
            }
        }

        return pixels;
    }

    /**
     * Export a dataset to tab delimited text file.
     *
     * @param data The dataset that contains the data.
     *
     */
    /*
     * public void savegenes(DataSet data){//String[] names,double[][] matrix){
     * String br= System.getProperty("line.separator");
     *
     * JFileChooser fd; File f;
     *
     * String[] colnames= data.getColumnIds(); double[][] matrix=data.getData();
     *
     * DataOutputStream dataout; int temp=0;
     *
     * fd=new JFileChooser(); fd.setFileFilter(new FileFilter(){ public boolean
     * accept(File f){ if(f.getName().endsWith(".txt")) return true; else
     * if(f.isDirectory()) return true; else return false; }
     *
     * public String getDescription(){ return "TAB delimited text file (*.txt)";
     * } });
     *
     * if(lastpath!= null && lastpath !=""){fd.setCurrentDirectory(new
     * File(lastpath));}
     *
     * //fd.showSaveDialog(new JFrame()); int returnVal = fd.showSaveDialog(new
     * JFrame()); f=fd.getSelectedFile(); File dir = fd.getCurrentDirectory();
     *
     * try{
     *
     * if(f!=null && !f.isDirectory() && returnVal ==
     * JFileChooser.APPROVE_OPTION){
     *
     * this.lastpath=dir.getAbsolutePath(); ostream = new FileOutputStream(f);
     * BufferedOutputStream buffer=new BufferedOutputStream(ostream); dataout =
     * new DataOutputStream(buffer);
     *
     * for(int i=0;i<data.getInfoHeaders().length;i++){
     *
     * //System.out.print("\n>"+data.getInfoHeaders()[i]+"<");
     *
     * dataout.writeBytes(data.getInfoHeaders()[i]); dataout.writeBytes("\t"); }
     *
     *
     * for(int i=0;i<colnames.length;i++){
     *
     * dataout.writeBytes(String.valueOf(colnames[i])); if(i!=
     * colnames.length-1)dataout.writeBytes("\t"); }
     *
     * dataout.writeBytes(br);
     *
     * for(int i=0;i<data.getDataLength();i++){
     *
     * for(int j=0;j<data.getInfoHeaders().length;j++){
     * dataout.writeBytes(data.getInfos()[i][j]);
     * //if(j!=data.getInfoHeaders().length-1) dataout.writeBytes("\t"); }
     *
     * for(int j=0;j<matrix[0].length;j++){ //dataout.writeBytes("\t");
     * dataout.writeBytes(String.valueOf(matrix[i][j]));
     * if(j!=matrix[0].length-1)dataout.writeBytes("\t"); }
     * dataout.writeBytes(br); }
     *
     * buffer.flush(); ostream.close();
     *
     * }} catch (IOException ex) { errorMsg err = new errorMsg(8); }
     *
     *
     *
     * }
     *
     */
    /**
     * Save the x and y coordinates of a PCA plot.
     *
     * @param names The indexed identifiers.
     *
     * @param xy The indexed coordinates.
     */
    /*
     * public void savexy(DataSet data,double[][] xy,double[]
     * eigenvalues){//String[] names,double[][] matrix){ JFileChooser fd; File
     * f; String br= System.getProperty("line.separator"); DataOutputStream
     * dataout; int temp=0;
     *
     * fd=new JFileChooser(); fd.setFileFilter(new FileFilter(){ public boolean
     * accept(File f){ if(f.getName().endsWith(".txt")) return true; else
     * if(f.isDirectory()) return true; else return false; }
     *
     * public String getDescription(){ return "TAB delimited text files
     * (*.txt)"; } });
     *
     * if(lastpath!= null && lastpath !=""){fd.setCurrentDirectory(new
     * File(lastpath));} //fd.showSaveDialog(new JFrame()); int returnVal =
     * fd.showSaveDialog(new JFrame()); f=fd.getSelectedFile(); File dir =
     * fd.getCurrentDirectory();
     *
     * try{
     *
     * // f=new File(fd.getDirectory()+fd.getFile()); if(f!=null &&
     * !f.isDirectory() && returnVal == JFileChooser.APPROVE_OPTION){
     * this.lastpath=dir.getAbsolutePath();
     *
     * ostream = new FileOutputStream(f); BufferedOutputStream buffer=new
     * BufferedOutputStream(ostream); dataout = new DataOutputStream(buffer);
     *
     * dataout.writeBytes("EigenValues"+"\t"); for(int
     * j=1;j<data.getInfoHeaders().length;j++){ dataout.writeBytes(" "+"\t"); }
     *
     * for(int j=0;j<eigenvalues.length;j++){
     * dataout.writeBytes(eigenvalues[j]+"\t"); }
     *
     * dataout.writeBytes(br);
     *
     * //dataout.writeBytes("Identifiers"); //
     * System.out.print(data.usedInfoCount()); for(int
     * j=0;j<data.getInfoHeaders().length;j++){
     * dataout.writeBytes(data.getInfoHeaders()[j]+"\t"); }
     * //dataout.writeBytes("X\tY");
     *
     * dataout.writeBytes(br);
     *
     * for(int i=0;i<data.getDataLength();i++){
     *
     * for(int j=0;j<data.getInfoHeaders().length;j++){
     * dataout.writeBytes(data.getInfos()[i][j]+"\t"); }
     *
     * for(int j=0;j<xy[i].length;j++){ if(j<xy[i].length-1)
     * dataout.writeBytes(xy[i][j]+"\t"); else dataout.writeBytes(xy[i][j]+"");
     * }
     *
     * dataout.writeBytes(br); }
     *
     * buffer.flush(); ostream.close();
     *
     * }}
     *
     * catch (java.io.FileNotFoundException ex) {new errorMsg(4); } catch
     * (Exception ex) {ex.printStackTrace(); } }
     */
    /*
     * //Used in the MDS component
     *
     * public void savexy(DataSet data,double[] x,double[] y){//String[]
     * names,double[][] matrix){ JFileChooser fd; File f; String br=
     * System.getProperty("line.separator"); DataOutputStream dataout; int
     * temp=0;
     *
     * fd=new JFileChooser(); fd.setFileFilter(new FileFilter(){ public boolean
     * accept(File f){ if(f.getName().endsWith(".txt")) return true; else
     * if(f.isDirectory()) return true; else return false; }
     *
     * public String getDescription(){ return "TAB delimited text files
     * (*.txt)"; } });
     *
     * if(lastpath!= null && lastpath !=""){fd.setCurrentDirectory(new
     * File(lastpath));} //fd.showSaveDialog(new JFrame()); int returnVal =
     * fd.showSaveDialog(new JFrame()); f=fd.getSelectedFile(); File dir =
     * fd.getCurrentDirectory();
     *
     * try{
     *
     * // f=new File(fd.getDirectory()+fd.getFile()); if(f!=null &&
     * !f.isDirectory() && returnVal == JFileChooser.APPROVE_OPTION){
     * this.lastpath=dir.getAbsolutePath();
     *
     * ostream = new FileOutputStream(f); BufferedOutputStream buffer=new
     * BufferedOutputStream(ostream); dataout = new DataOutputStream(buffer);
     *
     * // dataout.writeBytes(br);
     *
     * //dataout.writeBytes("Identifiers"); //
     * System.out.print(data.usedInfoCount()); for(int
     * j=0;j<data.getInfoHeaders().length;j++){
     * dataout.writeBytes(data.getInfoHeaders()[j]+"\t"); }
     *
     * dataout.writeBytes("Dim 1\tDim2");
     *
     *
     * //dataout.writeBytes("X\tY");
     *
     * dataout.writeBytes(br);
     *
     * for(int i=0;i<data.getDataLength();i++){
     *
     * for(int j=0;j<data.getInfoHeaders().length;j++){
     * dataout.writeBytes(data.getInfos()[i][j]+"\t"); }
     *
     * dataout.writeBytes(x[i]+"\t"+y[i]);
     *
     *
     * dataout.writeBytes(br); }
     *
     * buffer.flush(); ostream.close();
     *
     * }}
     *
     * catch (java.io.FileNotFoundException ex) {new errorMsg(4); } catch
     * (Exception ex) {ex.printStackTrace(); } }
     *
     */
    /*
     * public String saveProject(TreeNode node,String projectName,boolean pro){
     * this.project=pro; JFileChooser fd; File f=null; File dir=null; int
     * returnVal = JFileChooser.APPROVE_OPTION; try{ if(projectName==null ||
     * projectName==""){ fd=new JFileChooser();
     *
     * if(lastpath!= null && lastpath !=""){fd.setCurrentDirectory(new
     * File(lastpath));}
     *
     * fd.setFileFilter(new FileFilter(){ public boolean accept(File f){
     * if(project && f.getName().endsWith(".pro")) return true; else if(!project
     * && f.getName().endsWith(".mod")) return true; else if(f.isDirectory())
     * return true; else return false; }
     *
     * public String getDescription(){ if(project)return "J-Express project
     * files (*.pro)"; else return "J-Express module files (*.mod)"; } });
     *
     * returnVal = fd.showSaveDialog(new JFrame()); //.showSaveDialog(new
     * JFrame());
     *
     * if(returnVal!=fd.APPROVE_OPTION) return "";
     *
     * f=fd.getSelectedFile();
     *
     * if(pro && !f.getAbsolutePath().toLowerCase().endsWith(".pro")) f=new
     * File(f.getAbsolutePath()+".pro"); if(!pro &&
     * !f.getAbsolutePath().toLowerCase().endsWith(".mod")) f=new
     * File(f.getAbsolutePath()+".mod");
     *
     * if (f!=null && f.exists()){ if
     * (JOptionPane.showConfirmDialog(null,"Overwrite"+f.getName(), "Confirm
     * overwrite", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION ) return
     * ""; }
     *
     *
     * dir = fd.getCurrentDirectory();
     *
     * lastpath=dir.getAbsolutePath(); }
     *
     * else{ f=new File(projectName); // dir = f.getAbsolutePath(); }
     *
     * if(f!=null && !f.isDirectory() && returnVal ==
     * JFileChooser.APPROVE_OPTION){ if(dir!=null)
     * lastpath=dir.getAbsolutePath();//fd.getDirectory()+fd.getFile(); ostream
     * = new FileOutputStream(f);
     *
     * //BufferedOutputStream buffer=new BufferedOutputStream(ostream);
     *
     * //GZIPOutputStream zipout = new GZIPOutputStream(buffer);
     * java.util.zip.GZIPOutputStream zipout = new
     * java.util.zip.GZIPOutputStream(ostream); ObjectOutputStream p = new
     * ObjectOutputStream(zipout); saveproject(node,p);
     *
     * zipout.flush(); zipout.close();
     *
     * ostream.close(); }}
     *
     * catch(IOException e){ JOptionPane.showMessageDialog(null,"Could not write
     * to file\n"+f.getAbsolutePath()+"\nMake sure the file is not write
     * protected..", "Error",JOptionPane.ERROR_MESSAGE); return ""; } return
     * f.getAbsolutePath(); }
     *
     */
    /*
     * public String saveProjectNEW(ExpressNode node,String projectName,boolean
     * pro,JFrame parent){ this.project=pro; JFileChooser fd; File f=null; File
     * dir=null; int returnVal = JFileChooser.APPROVE_OPTION; try{
     * if(projectName==null || projectName==""){ fd=new JFileChooser();
     *
     * proFileView view = new proFileView(); fd.setFileView(view);
     *
     * if(lastpath!= null && lastpath !=""){fd.setCurrentDirectory(new
     * File(lastpath));}
     *
     * fd.setFileFilter(new FileFilter(){ public boolean accept(File f){
     * if(project && f.getName().endsWith(".pro")) return true; else if(!project
     * && f.getName().endsWith(".mod")) return true; else if(f.isDirectory())
     * return true; else return false; }
     *
     * public String getDescription(){ if(project)return "J-Express project
     * files (*.pro)"; else return "J-Express module files (*.mod)"; } });
     *
     * if(parent!=null) returnVal = fd.showSaveDialog(parent); else returnVal =
     * fd.showSaveDialog(new JFrame()); //.showSaveDialog(new JFrame());
     *
     * if(returnVal!=fd.APPROVE_OPTION) return "";
     *
     * f=fd.getSelectedFile();
     *
     * if(pro && !f.getAbsolutePath().toLowerCase().endsWith(".pro")) f=new
     * File(f.getAbsolutePath()+".pro"); if(!pro &&
     * !f.getAbsolutePath().toLowerCase().endsWith(".mod")) f=new
     * File(f.getAbsolutePath()+".mod");
     *
     * if (f!=null && f.exists()){ if
     * (JOptionPane.showConfirmDialog(null,"Overwrite"+f.getName(), "Confirm
     * overwrite", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION ) return
     * ""; }
     *
     *
     * dir = fd.getCurrentDirectory();
     *
     * lastpath=dir.getAbsolutePath(); }
     *
     * else{ f=new File(projectName);
     *
     * // dir = f.getAbsolutePath(); }
     *
     * if(f!=null && !f.isDirectory() && returnVal ==
     * JFileChooser.APPROVE_OPTION){ if(dir!=null)
     * lastpath=dir.getAbsolutePath();//fd.getDirectory()+fd.getFile();
     *
     * File temp = null;
     *
     * File stemp = null;//new
     * File(temp.getParent()+File.separator+"saving.tmp");
     *
     * if(f.exists()){ stemp = new
     * File(f.getParent()+File.separator+"saving.tmp"); if(stemp.exists())
     * stemp.delete(); temp = stemp; } else temp=f;
     *
     * //JOptionPane.showMessageDialog(null,"saving as: "+temp.toString());
     *
     * //if(cl==null) JOptionPane.showMessageDialog(null,"NO CL!");
     *
     * ostream = new FileOutputStream(temp);
     *
     * node.setCl(cl); ObjectOutputStream p = new ObjectOutputStream(ostream);
     *
     * p.writeObject(node); p.flush(); ostream.close();
     *
     * //double[] d = new double[2]; //double s = d[8];
     *
     *
     * if(temp != f){ f.delete(); boolean ren = temp.renameTo(f); if(!ren){
     * JFrame frame = null; if(cl!=null) frame=cl.MW;
     *
     * JOptionPane.showMessageDialog(frame,"Could not overwrite temporary file:
     * "+temp.getAbsolutePath()+" to "+f.getAbsolutePath(),"File
     * Error",JOptionPane.ERROR_MESSAGE);
     *
     * }
     * }
     *
     * }
     * if(cl!=null) cl.addRecentFile(f); }
     *
     * catch(IOException e){ JOptionPane.showMessageDialog(null,"Could not write
     * to file\n"+f.getAbsolutePath()+"\nMake sure the file is not write
     * protected.."+"\n"+e.toString(), "Error",JOptionPane.ERROR_MESSAGE);
     * e.printStackTrace(); return ""; }
     *
     * finally{ if (ostream !=null){ try{ ostream.close(); } catch (Exception
     * ignored){ } } }
     *
     *
     *
     * return f.getAbsolutePath(); }
     *
     *
     *
     *
     */
    /*
     *
     *
     *
     *
     * public void saveproject(DefaultMutableTreeNode node,ObjectOutputStream p)
     * throws IOException{
     *
     *
     * IconData tmp=(IconData)node.getUserObject(); if(tmp.getObject()
     * instanceof DataSet){ DataSet d =(DataSet) tmp.getObject();
     *
     * saveHashDataSet(null,d,p); if(node.getChildCount()>0){ for(int
     * i=0;i<node.getChildCount();i++){
     * saveproject((DefaultMutableTreeNode)node.getChildAt(i),p);
     *
     * }
     * }
     * }
     * else{ Node pnode = (Node) tmp.getObject(); pnode.write(p); }
     *
     * }
     *
     * public void saveproject(Object source,ObjectOutputStream p) throws
     * IOException{ if(source instanceof DataSet){ DataSet d =(DataSet) source;
     * saveHashDataSet(null,d,p); } else{
     * //System.out.print("\n--------------->Writing node");
     *
     * Node pnode = (Node)source; pnode.write(p); }
     *
     * }
     *
     */
    /*
     * public String loadProject(DefaultMutableTreeNode root,JTree
     * tree,DefaultTreeModel model,boolean pro,File file,cluster cl,boolean
     * test,JProgressBar bar){ this.test=test; return
     * loadProject(root,tree,model,pro,file,cl); }
     */
    /*
     *
     * public TreeNode loadProjectOLD(InputStream istream){ TreeNode
     * returnRoot=null; JFileChooser fd; DataSet dat = null;//new DataSet();
     * try{
     *
     * returnRoot = loadProjectOLD(dat,istream); istream.close(); }
     * catch(IOException e){e.printStackTrace();}
     *
     * //if(returnRoot instanceof DefaultMutableTreeNode){
     *
     *
     * //}
     *
     *
     * return returnRoot;
     *
     * }
     *
     *
     *
     *
     *
     *
     * public String loadProjectNEW(final ExpressNode root,final JTree
     * tree,final DefaultTreeModel model,final boolean pro,final File file,final
     * cluster cl){
     *
     * final expresscomponents.SwingWorker worker = new
     * expresscomponents.SwingWorker() { //final JInternalFrame fr = new
     * JInternalFrame("Loading"); //final JProgressBar bar = new JProgressBar();
     *
     * TreeNode returnRoot=null;
     *
     * DataSet dat = null;
     *
     * File f=file;      *
     * public Object construct() {
     *
     *
     * filehandler.this.project=pro;
     *
     * JFileChooser fd;
     *
     * Vector objects=null;
     *
     * ImageIcon iicon= new ImageIcon( getClass().getResource( "im/cfolder.gif"
     * ) ); DataSet nulldata = new DataSet(); nulldata.setFile("Loaded
     * Dataset");
     *
     * int returnVal = JFileChooser.APPROVE_OPTION; File dir = null; try{
     * if(f==null){ fd=new JFileChooser();
     *
     * proFileView view = new proFileView(); fd.setFileView(view);
     *
     * // fd.setFileSelectionMode(fd.FILES_AND_DIRECTORIES);
     *
     * //if(lastpath!=null && new File(lastpath).exists())
     * fd.setCurrentDirectory(new File(lastpath));
     *
     * fd.setFileFilter(new FileFilter(){ public boolean accept(File f){
     * if(project && f.getName().endsWith(".pro")) return true; else if(!project
     * && f.getName().endsWith(".mod")) return true; else if(f.isDirectory())
     * return true; else return false; }
     *
     * public String getDescription(){ if(project)return "J-Express project
     * files (*.pro)"; else return "J-Express module files (*.mod)"; } });
     *
     * if(lastpath!= null && lastpath !=""){fd.setCurrentDirectory(new
     * File(lastpath));} //fd.showOpenDialog(new JFrame()); returnVal =
     * fd.showOpenDialog(cl.MW);
     *
     *
     * f=fd.getSelectedFile(); dir = fd.getCurrentDirectory(); }
     *
     * if(f==null) return null;
     *
     * if(f!=null && !f.exists()){ JOptionPane.showMessageDialog(cl.MW, "Can not
     * find the file \n"+f.getAbsolutePath(), "File Problem",
     * JOptionPane.ERROR_MESSAGE); return null; }
     *
     * if(f!=null && !f.isDirectory() && returnVal ==
     * JFileChooser.APPROVE_OPTION){ if(dir!=null){
     * filehandler.this.lastpath=dir.getAbsolutePath(); if(cl!=null)
     * cl.addRecentFile(f); }
     *
     * istream = new FileInputStream(f); javax.swing.ProgressMonitorInputStream
     * pi = new javax.swing.ProgressMonitorInputStream(cl.MW,"Reading",istream);
     *
     * java.io.BufferedInputStream buffer=new
     * java.io.BufferedInputStream(pi,300000);
     *
     * ObjectInputStream p = null;
     *
     *
     *
     * try{ p = new ObjectInputStream(buffer); } catch(Exception e){
     *
     * //System.out.print("Header not OK..\nTrying zipped");
     *
     * try{ // The stream is probably in the old zipped format...
     * istream.close();
     *
     * istream = new FileInputStream(f); buffer=new
     * java.io.BufferedInputStream(istream); java.util.zip.GZIPInputStream zipin
     * = new java.util.zip.GZIPInputStream(buffer);
     *
     *
     * //javax.swing.ProgressMonitorInputStream pi = new
     * javax.swing.ProgressMonitorInputStream(cl.MW,"Reading",buffer);
     *
     * p = new ObjectInputStream(zipin);
     *
     * dat = (DataSet)loadProjectOLD(p); } catch(Exception
     * ee){ee.printStackTrace();} finally{ if (istream !=null){ try{
     * istream.close(); } catch (Exception ignored){ } } } }
     *
     * if(dat==null){ //System.out.print("dat null:<");
     *
     * try{
     *
     * //ProgressBarUpdater ud = new ProgressBarUpdater(bar, istream);
     * //ud.start();
     *
     * dat = (DataSet) p.readObject(); //p.writeObject(node); //if(dat==null)
     * System.out.print("\ndat STILL null:<");
     *
     * }
     * // catch(Exception e){ //fr.setVisible(false); //fr.dispose();
     *
     * JOptionPane.showMessageDialog(cl.MW, "Unable to parse File\nMake sure
     * this is a project file", "File Error", JOptionPane.ERROR_MESSAGE);
     *
     * e.printStackTrace(); }
     *
     * finally{ if (istream !=null){ try{ istream.close(); } catch (Exception
     * ignored){ } } } } else{ //System.out.print("dat not null:)");
     * model.setRoot(dat); }
     *
     *
     * istream.close(); buffer.close(); //zipin.close(); p.close();
     *
     * if(cl!=null) cl.projectFile=f.getAbsolutePath();
     *
     * }
     * else{ f=null; } }
     *
     *
     *
     * catch(IOException e){
     *
     * //return loadProject(root,final JTree tree,final DefaultTreeModel
     * model,final boolean pro,final File file,final cluster cl){
     * //e.printStackTrace();loadunzippedProject(root,model,f,cl);f=null;
     *
     *
     * }
     *
     * finally{ if (istream !=null){ try{ istream.close(); } catch (Exception
     * ignored){ } } }
     *
     *
     * // Note: // Earlier versions of J-Express did only save uncompressed
     * files. // Therefore: If the format is not a gzip format, try to load the
     * data // in the old way..
     *
     * return null; }
     *
     * public void finished(){ //fr.dispose();
     *
     * if(f==null) return;
     *
     * //Check if this is an old type of dataset.. boolean oldData = false;
     * if(returnRoot !=null && returnRoot.getChildCount()>0){ if(returnRoot
     * instanceof DefaultMutableTreeNode) oldData = true;
     *
     * // DefaultMutableTreeNode ch1 =
     * (DefaultMutableTreeNode)returnRoot.getChildAt(0); //
     * if((ch1.getUserObject()!=null)) oldData=true; }
     *
     * if(oldData){ DataSet dat2 =
     * filehandler.convertOldTree((DefaultMutableTreeNode)returnRoot, tree,
     * model, cl); if(cl!=null)cl.treeModel.setRoot(dat2);
     * if(cl!=null)cl.treeRoot=dat2; if(cl!=null)tree.treeDidChange(); } else{
     * if(cl!=null)cl.treeModel.setRoot(dat); if(cl!=null)cl.treeRoot=dat;
     * if(cl!=null)tree.treeDidChange();      *
     * }
     * }
     * };
     *
     *
     * worker.start();
     *
     * // if(f!=null)return f.getAbsolutePath(); //else if(cl!=null)
     * cl.addRecentFile(file); return null;
     *
     * }
     *
     *
     *
     *
     * public String loadProjectNEWNoThread(final ExpressNode root,final JTree
     * tree,final DefaultTreeModel model,final boolean pro,final File file,final
     * cluster cl){
     *
     * final JInternalFrame fr = new JInternalFrame("Loading"); final
     * JProgressBar bar = new JProgressBar();
     *
     * TreeNode returnRoot=null;
     *
     * DataSet dat = null;
     *
     * File f=file;      *
     *
     * filehandler.this.project=pro; JFileChooser fd;
     *
     * Vector objects=null;
     *
     * ImageIcon iicon= new ImageIcon( getClass().getResource( "im/cfolder.gif"
     * ) ); DataSet nulldata = new DataSet(); nulldata.setFile("Loaded
     * Dataset");
     *
     * int returnVal = JFileChooser.APPROVE_OPTION; File dir = null; try{
     * if(f==null){ fd=new JFileChooser(); //
     * fd.setFileSelectionMode(fd.FILES_AND_DIRECTORIES);
     *
     * //if(lastpath!=null && new File(lastpath).exists())
     * fd.setCurrentDirectory(new File(lastpath));
     *
     * fd.setFileFilter(new FileFilter(){ public boolean accept(File f){
     * if(project && f.getName().endsWith(".pro")) return true; else if(!project
     * && f.getName().endsWith(".mod")) return true; else if(f.isDirectory())
     * return true; else return false; }
     *
     * public String getDescription(){ if(project)return "J-Express project
     * files (*.pro)"; else return "J-Express module files (*.mod)"; } });
     *
     * if(lastpath!= null && lastpath !=""){fd.setCurrentDirectory(new
     * File(lastpath));} //fd.showOpenDialog(new JFrame()); returnVal =
     * fd.showOpenDialog(cl.MW);
     *
     *
     * f=fd.getSelectedFile(); dir = fd.getCurrentDirectory(); }
     *
     * if(f==null) return null;
     *
     * if(f!=null && !f.exists()){ JOptionPane.showMessageDialog(null, "Can not
     * find the file \n"+f.getAbsolutePath(), "File Problem",
     * JOptionPane.ERROR_MESSAGE); return null; }
     *
     * if(f!=null && !f.isDirectory() && returnVal ==
     * JFileChooser.APPROVE_OPTION){ if(dir!=null){
     * filehandler.this.lastpath=dir.getAbsolutePath(); if(cl!=null)
     * cl.addRecentFile(f); }
     *
     * fr.getContentPane().add("North",new JLabel("Progress"));
     * fr.getContentPane().add("Center",bar);
     *
     * if(cl!=null)cl.MW.JDesktopPane1.add(fr); if(cl!=null)fr.setLocation(
     * cl.MW.projectPanel.getLocation().x+50,
     * cl.MW.projectPanel.getLocation().y+50 );
     *
     * fr.pack(); fr.show();
     *
     *
     *
     *
     * istream = new FileInputStream(f);
     *
     * //System.out.print("AVAILABLE:"+istream.available());      *
     * java.io.BufferedInputStream buffer=new
     * java.io.BufferedInputStream(istream); //java.util.zip.GZIPInputStream
     * zipin = new java.util.zip.GZIPInputStream(buffer);
     *
     * ObjectInputStream p = null;
     *
     *
     *
     * try{ p = new ObjectInputStream(buffer); } catch(Exception e){
     *
     * //System.out.print("Header not OK..\nTrying zipped");
     *
     * try{ // The stream is probably in the old zipped format...
     * istream.close();
     *
     * istream = new FileInputStream(f); buffer=new
     * java.io.BufferedInputStream(istream); java.util.zip.GZIPInputStream zipin
     * = new java.util.zip.GZIPInputStream(buffer); p = new
     * ObjectInputStream(zipin);
     *
     * dat = (DataSet)loadProjectOLD(p); } catch(Exception
     * ee){ee.printStackTrace();} finally{ if (istream !=null){ try{
     * istream.close(); } catch (Exception ignored){ } } } }
     *
     * if(dat==null){ //System.out.print("dat null:<");
     *
     * try{
     *
     * ProgressBarUpdater ud = new ProgressBarUpdater(bar, istream); ud.start();
     *
     * dat = (DataSet) p.readObject(); //p.writeObject(node); //if(dat==null)
     * System.out.print("\ndat STILL null:<");
     *
     * }
     * catch(Exception e){e.printStackTrace();}
     *
     * finally{ if (istream !=null){ try{ istream.close(); } catch (Exception
     * ignored){ } } } } else{ //System.out.print("dat not null:)");
     * model.setRoot(dat); }
     *
     *
     * istream.close(); buffer.close(); //zipin.close(); p.close(); if(cl!=null)
     * cl.addRecentFile(f); if(cl!=null) cl.projectFile=f.getAbsolutePath();
     *
     * }
     * else{ f=null; } }
     *
     *
     *
     * catch(IOException e){
     *
     * //return loadProject(root,final JTree tree,final DefaultTreeModel
     * model,final boolean pro,final File file,final cluster cl){
     * //e.printStackTrace();loadunzippedProject(root,model,f,cl);f=null;
     *
     *
     * }
     * finally{ if (istream !=null){ try{ istream.close(); } catch (Exception
     * ignored){ } } }
     *
     *
     * // Note: // Earlier versions of J-Express did only save uncompressed
     * files. // Therefore: If the format is not a gzip format, try to load the
     * data // in the old way..
     *
     * fr.dispose();
     *
     * if(f==null) return null;
     *
     * //Check if this is an old type of dataset.. boolean oldData = false;
     * if(returnRoot !=null && returnRoot.getChildCount()>0){ if(returnRoot
     * instanceof DefaultMutableTreeNode) oldData = true;
     *
     * // DefaultMutableTreeNode ch1 =
     * (DefaultMutableTreeNode)returnRoot.getChildAt(0); //
     * if((ch1.getUserObject()!=null)) oldData=true; }
     *
     * if(oldData){ DataSet dat2 =
     * filehandler.convertOldTree((DefaultMutableTreeNode)returnRoot, tree,
     * model, cl); if(cl!=null)cl.treeModel.setRoot(dat2);
     * if(cl!=null)cl.treeRoot=dat2; if(cl!=null)tree.treeDidChange(); } else{
     * if(cl!=null)cl.treeModel.setRoot(dat); if(cl!=null)cl.treeRoot=dat;
     * if(cl!=null)tree.treeDidChange();      *
     * }
     *
     * model.setRoot(dat);
     *
     *
     * return f.getAbsolutePath(); }      *
     */
    /*
     * public void loadunzippedProject(TreeNode root,DefaultTreeModel model,File
     * f,cluster cl){ JTree tree=null; Vector objects=null;
     *
     * ImageIcon iicon= new ImageIcon( getClass().getResource( "im/cfolder.gif"
     * ) ); DataSet nulldata = new DataSet(); nulldata.setFile("Loaded
     * Dataset");
     *
     * try{
     *
     * if(f!=null){
     *
     * FileInputStream istream = new FileInputStream(f);
     * java.io.BufferedInputStream buffer=new
     * java.io.BufferedInputStream(istream); ObjectInputStream p = new
     * ObjectInputStream(buffer);
     *
     * loadProject((DefaultMutableTreeNode)root,p,tree,model,cl,null);
     * istream.close(); } } catch(IOException e){errorMsg err = new
     * errorMsg(3);}
     *
     * }
     */
    /*
     *
     * public static DataSet convertOldTree(DefaultMutableTreeNode trunk, JTree
     * tree, DefaultTreeModel model, cluster cl){
     *
     * DefaultMutableTreeNode point=(DefaultMutableTreeNode)trunk;
     *
     * // System.out.print("\n>"+point.getChildCount());
     *
     * // System.out.print("\n"+trunk.getUserObject().getClass().getName());
     *
     * IconData ic = (IconData)trunk.getUserObject(); DataSet dpoint =
     * (DataSet)ic.getObject();
     *
     * DataSet data; java.util.Stack s=new java.util.Stack(); java.util.Stack
     * s2=new java.util.Stack();
     *
     * DataSet root = dpoint; DataSet tmp2 = null; IconData id,id2 = null;
     *
     * DefaultMutableTreeNode dm=null; s.push(point); s2.push(dpoint);
     *
     * while(!s.isEmpty()){
     *
     * // id=(IconData)point.getUserObject(); // tmp=(DataSet)id.getObject();
     * point=(DefaultMutableTreeNode) s.pop(); dpoint=(DataSet) s2.pop();
     * //dpoint=(DataSet)((IconData)trunk.getUserObject()).getObject();
     *
     *
     *
     *
     * for(int i=0;i<point.getChildCount();i++){
     *
     * dm= (DefaultMutableTreeNode) point.getChildAt(i);
     * id2=(IconData)dm.getUserObject(); tmp2=(DataSet)id2.getObject();
     *
     * //if(dpoint==null) System.out.print("\n:A:>"+point.toString()+"\n");
     * //if(tmp2==null) System.out.print("\n:B:>"+id2.toString()+"\n");
     *
     * tmp2.removeAllChildren(); tmp2.removeFromParent();
     *
     * Object o = tmp2.getParent(); //if(o!=null)
     * System.out.print("\n>>>"+o.getClass().getName());
     *
     *
     * if(tmp2!=null && dpoint!=null) dpoint.add(tmp2);
     *
     * s.push(dm); s2.push(tmp2);
     *
     * }
     *
     * }
     * return root; }
     *
     *
     *
     *
     *
     * //node must contain a DataSet.. public TreeNode loadProjectOLD(TreeNode
     * trunk, InputStream is){ String type =""; DataSet ds=null; DataSet
     * temp=null; Node pnode=null; TreeNode root = null; ObjectInputStream p =
     * (ObjectInputStream)is; cluster cl = null;
     *
     * try{ Object o = p.readObject(); while(!(o instanceof String)) o =
     * p.readObject();
     *
     * //System.out.print("\n>>Now Available: "+p.available());
     *
     * String version = (String)o;
     *
     * if(version.equals("v1.0")) ds=loadDataSetV10(p); else
     * if(version.equals("v1.1")) ds=loadDataSetV11(p); else
     * if(version.equals("v1.2")){ ds=loadDataSetV12(p);} else
     * if(version.equals("v1.3")){ ds=loadDataSetV13(p);} else
     * if(version.equals("v1.4")){ ds=loadDataSetV14(p);} else
     * if(version.equals("v1.5")){ ds=loadDataSetV15(p);} else
     * if(version.equals("v1.6")){ ds=loadDataSetV16(p);} else
     * if(version.equals("v1.7")){ ds=loadHashDataSet(p);} else
     * if(version.equals("v1.8")){ try{ //type = (String)p.readObject(); Object
     * t = p.readObject();
     *
     * while(!(t instanceof String)) t = p.readObject(); type= (String)t;
     *
     *
     * //type=(String)t; // System.out.print("\nTYPE:>"+type+"\n");
     *
     *
     * if(type.equals("DataSet")) ds=loadHashDataSet(p); else
     * if(type.equals("ThumbNode")) pnode= new
     * expresscomponents.ProjectNodes.ThumbNode(cl,p); else
     * if(type.equals("PCANode")) pnode= new
     * expresscomponents.ProjectNodes.PCANode(cl,p); else
     * if(type.equals("GraphNode")) pnode= new
     * expresscomponents.ProjectNodes.GraphNode(cl,p); else
     * if(type.equals("HclustNode")) pnode= new
     * expresscomponents.ProjectNodes.HclustNode(cl,p); else
     * if(type.equals("SpotViewNode")) pnode= new
     * expresscomponents.ProjectNodes.SpotViewNode(cl,p); else
     * if(type.equals("MDSNode")) pnode= new
     * expresscomponents.ProjectNodes.MDSNode(cl,p);
     *
     * else if(type.equals("GSEANode")) pnode= new
     * expresscomponents.ProjectNodes.GSEANode(cl,p);
     *
     * else if(type.equals("RankNode")) pnode= new
     * expresscomponents.ProjectNodes.RankNode(cl,p); else
     * if(type.equals("SAMNode")) pnode= new
     * expresscomponents.ProjectNodes.SAMNode(cl,p); else
     * if(type.equals("ClusterListNode")) pnode= new
     * expresscomponents.ProjectNodes.ClusterListNode(cl,p); } catch(Exception
     * ce){ce.printStackTrace();} }
     *
     * }
     * catch(Exception ce){ce.printStackTrace();}
     *
     *
     * // if(ds==null)
     * ds=(DataSet)((IconData)trunk.getUserObject()).getObject();
     *
     * if(ds==null){ //System.out.print("\nNo dataset!>"+type+"\n"); }
     *
     * if(ds!=null){
     *
     * int childs=ds.getDataSetChildCount();
     *
     * TreeNode isRoot = addNode(ds,trunk);
     *
     * if(isRoot!=null) root=isRoot;
     *
     * if(ds.getDataSetChildCount()>0){ for(int
     * i=0;i<ds.getDataSetChildCount();i++){
     *
     * loadProjectOLD(ds,p);
     *
     * }
     * }
     * }
     *
     * else if (pnode!=null){ ((ExpressNode)trunk).add(pnode);//.getNode());
     *
     * //DataSet rt = (DataSet)((IconData)trunk.getUserObject()).getObject();
     * //rt.addChild();
     *
     *
     * // ((DefaultTreeModel)tree.getModel()).reload(trunk);
     * //source.getTree().makeVisible(new TreePath(th.getNode().getPath())); }
     * return root; }
     *
     */
    /*
     * public void loadHashProject(DefaultMutableTreeNode trunk,
     * ObjectInputStream p,JTree tree,DefaultTreeModel model,cluster cl) throws
     * IOException{ DataSet ds=null; DataSet temp=null; Node pnode=null; try{
     * String version = (String)p.readObject(); String type =
     * (String)p.readObject(); if(type.equals("DataSet")) ds=loadHashDataSet(p);
     * else if(type.equals("ThumbNode")) pnode= new ThumbNode(cl,p); else
     * if(type.equals("PCANode")) pnode= new PCANode(cl,p); else
     * if(type.equals("GraphNode")) pnode= new GraphNode(cl,p); else
     * if(type.equals("HclustNode")) pnode= new HclustNode(cl,p); }
     * catch(Exception ce){ce.printStackTrace();}
     *
     * if(ds!=null){ int childs=ds.getDataSetChildCount();
     *
     * addNode(ds.getNode(),trunk,tree,model); if(ds.getDataSetChildCount()>0){
     * for(int i=0;i<ds.getDataSetChildCount();i++){
     * loadHashProject(ds.getNode(),p,tree,model,cl); } } }
     *
     * else if (pnode!=null){ trunk.add(pnode.getNode());
     * ((DefaultTreeModel)tree.getModel()).reload(trunk);
     * //source.getTree().makeVisible(new TreePath(th.getNode().getPath())); }
     *
     * }
     *
     */
    /*
     * public TreeNode addNode(TreeNode child,TreeNode parent){ //Do not add the
     * child if it's already in the tree.. TreeNode root=null; if(parent==null){
     * // model.setRoot(child); root=child; //cl.treeRoot=child; }
     *
     * else{
     *
     * ((ExpressNode)parent).add((ExpressNode)child); } return root; }
     */
    /*
    public void saveDataSet(DataSet data, ObjectOutputStream p) throws IOException {

        boolean[] e = new boolean[8];
        if (data.getData() != null) {
            e[0] = true;
        }
        if (data.getFile() != null) {
            e[1] = true;
        }
        if (data.getInfo() != null) {
            e[2] = true;
        }
        //        if(data.getNames()!=null) e[3]=true;
        if (data.getColNames() != null) {
            e[4] = true;
        }
        if (data.getnulls() != null) {
            e[5] = true;
        }
        if (data.getInfos() != null) {
            e[6] = true;
        }
        if (data.getparentIndexes() != null) {
            e[7] = true;
        }


        p.writeObject("v1.6");
        p.writeObject(e);
        if (e[0]) {
            p.writeObject(data.getData());
        }
        if (e[1]) {
            p.writeObject(data.getFile());
        }
        if (e[2]) {
            p.writeObject(data.getInfo());
        }
        //        if(e[3]) p.writeObject(data.getNames());
        if (e[4]) {
            p.writeObject(data.getColNames());
        }
        if (e[5]) {
            p.writeObject(data.getnulls());
        }
        if (e[6]) {
            p.writeObject(data.getInfos());
        }
        if (e[6]) {
            p.writeObject(data.getInfoHeaders());
        }
        if (e[7]) {
            p.writeObject(data.getparentIndexes());
        }

        p.writeObject("No old meta");
        p.writeObject(new Integer(data.getDataSetChildCount()));
        p.writeObject(data.getIcon());
        p.writeObject(data.getGroups());

        p.writeObject(new Boolean(data.getLogTransformed()));
        p.writeObject(new Boolean(data.getRatios()));
        p.writeObject(new Boolean(data.getmaxminFromParent()));
        p.writeObject(new Boolean(data.externalIconSet()));
        if (data.externalIconSet()) {
            p.writeObject(data.getExternalIcon());
        }

    }
*/
    /*
    //Saves the hashtable source
    //If source = null, it generates a new hashtable from the dataset.
    public void saveHashDataSet(Hashtable source, DataSet data, ObjectOutputStream p) {
        try {
            //Hashtable h = DataSetToHashtable(data);
            if (source == null) {
                source = DataSetToHashtable(data, cl);
            }
            p.writeObject("v1.8");
            p.writeObject("DataSet");
            p.writeObject(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showHashComponents(Hashtable source) {
        java.util.Enumeration e = source.keys();
        java.util.Enumeration e2 = source.elements();
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            Object o2 = e2.nextElement();
            if (o instanceof Hashtable) {
                //System.out.print("\nHASH::>");
                showHashComponents((Hashtable) o);
                //System.out.print("\n<::+HASH");
            } else {
                //System.out.print("\n"+o.toString()+"  :  "+o2.toString());
            }


        }



    }

    public static double[][] fromFloat(float[][] data) {
        if (data.length == 0) {
            return new double[0][0];
        }
        double[][] ret = new double[data.length][data[0].length];
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                ret[i][j] = (double) data[i][j];
            }
        }
        return ret;
    }

    public static float[][] fromDouble(double[][] data) {
        if (data.length < 1) {
            return new float[0][0];
        }

        float[][] ret = new float[data.length][data[0].length];
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                ret[i][j] = (float) data[i][j];
            }
        }
        return ret;
    }

    public static Hashtable DataSetToHashtable(DataSet data, cluster cl) {
        //System.out.print("-");
        boolean test = false;

        Hashtable h = new Hashtable();
        h.put("Version", new Double(2.0));
        if (data == null) {
            return h;
        }

        h.put("linked", new Boolean(data.linked()));

        boolean writeAsFloat = false;

        if (cl != null) {
            Hashtable props = cl.props;
            if (props.containsKey("wfloat")) {
                writeAsFloat = ((Boolean) props.get("wfloat")).booleanValue();
            }
        } else {
            System.out.print("\nno CL");
        }

        // Hashtable p = cl.props;



        if (!data.linked() && data.getData() != null) {

            if (writeAsFloat) {
                //System.out.print("\nWriting as float");
                h.put("data", fromDouble(data.getData()));
            } else {
                //System.out.print("\nWriting as double");
                h.put("data", data.getData());
            }
            //h.put("data",data.getData());
        }


        if (data.getFile() != null) {
            h.put(PROP_NAME, data.getFile());
        }
        if (data.getInfo() != null) {
            h.put("info", data.getInfo());
        }
        if (data.getColNames() != null) {
            h.put("colnames", data.getColNames());
        }
        if (data.getColInfos() != null) {
            h.put("colinfos", data.getColInfos());
        }
        if (data.getColInfoHeaders() != null) {
            h.put("colinfoheaders", data.getColInfoHeaders());
        }
        if (data.getnulls() != null) {
            h.put("nulls", data.getnulls());
        }
        if (data.getInfos() != null) {
            h.put("infos", data.getInfos());
        }
        if (data.getInfoHeaders() != null) {
            h.put("infoheaders", data.getInfoHeaders());
        }
        if (data.getparentIndexes() != null) {
            h.put("parentIndexes", data.getparentIndexes());
        }
        if (data.getparentColumnIndexes() != null) {
            h.put("parentColumnIndexes", data.getparentColumnIndexes());
        }

        if (data.getLastModified() != null) {
            h.put(PROP_LAST_MODIFIED, data.getLastModified());
        }

        if (data.getDatabaseId() != null) {
            h.put(PROP_DATABASE_ID, data.getDatabaseId());
        }



        h.put("MetaList", data.getMetaList());
        h.put("Demodata", data.isDemo());

        //h.put("childcount",new Integer(data.getDataSetChildCount()));
        if (data.getIcon() != null) {
            h.put("icon", data.getIcon());
        }

        //This one is new..
        //expresscomponents.LineStyles.LineMark.remapMarksToIntegers(data.getGroups());
        if (data.getGroups() != null) {
            h.put("groups", data.getGroups());
        }

        h.put("logtransformed", new Boolean(data.getLogTransformed()));
        h.put("ratios", new Boolean(data.getRatios()));
        h.put("maxminfromparent", new Boolean(data.getmaxminFromParent()));
        h.put("externaliconset", new Boolean(data.externalIconSet()));
        h.put("isTransposed", new Boolean(data.isTransposed()));
        h.put("infoIDColumn", new Integer(data.getInfoIDColumn()));

        h.put("structures", data.structures);
        if (data.fileRowMapping != null) {
            h.put("fileRowMapping", data.fileRowMapping);
        }
        if (data.fileColMapping != null) {
            h.put("fileColMapping", data.fileColMapping);
        }

        if (data.externalIconSet()) {
            h.put("externalIconClip", new ImageAnalysis.ChipView.ClipImage(data.getExternalIcon().getImage()));
        }
        //if( ((Boolean)h.get("externaliconset")).booleanValue() && (h.containsKey("externalIconClip"))){ ICON=new ImageIcon( ((ImageAnalysis.ChipView.ClipImage)h.get("externalIcon")).getImage()); ret.setIcon(ICON);}
        //if(h.containsKey("columngroups") ) ret.setColumnGroups( (Vector)h.get("columngroups") );
        if (data.getColumnGroups() != null) {
            h.put("columngroups", data.getColumnGroups());
        }


        if (data.getColumnArrangement() != null) {
            h.put("columnarrangement", data.getColumnArrangement());
        }
        if (data.getRowArrangement() != null) {
            h.put("rowarrangement", data.getRowArrangement());
        }

        if (data.getusedInfos() != null) {
            h.put("userow", data.getusedInfos());
        }
        if (data.getusedColInfos() != null) {
            h.put("usecol", data.getusedColInfos());
        }


        if (test) {
            h.put("Test", "ohYes");
        }
        //showHashComponents(h);

        Vector ch = new Vector();
        for (int i = 0; i < data.getChildCount(); i++) {
            Object o = data.getChildAt(i);

            if (o instanceof DataSet) {
                ch.addElement(DataSetToHashtable((DataSet) o, cl));
            } else if (o instanceof Node) {
                ch.addElement(((Node) o).getHash());
            }
        }

        h.put("children", ch);


        return h;
    }

    public DataSet loadHashDataSet(ObjectInputStream p) {// throws IOException{
        DataSet ret = new DataSet();
        ImageIcon ICON = null;
        try {
            Hashtable h = (Hashtable) p.readObject();
            return HashtableToDataSet(h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public DataSet HashtableToDataSet(Hashtable h) {// throws IOException{
        DataSet ret = new DataSet();
        HashtableToDataSet(h, ret);
        return ret;
    }

    public static void convertOldGroups(DataSet data) {

        Vector oldrowGroups = data.getGroups();
        Vector oldcolGroups = data.getColumnGroups();

        if (oldrowGroups.elementAt(0) instanceof Group) {
            return;
        }

        Vector rowGroups = new Vector();
        Vector colGroups = new Vector();

        for (int i = 0; i < oldrowGroups.size(); i++) {
            rowGroups.add(new Group((Vector) oldrowGroups.elementAt(i)));
        }

        for (int i = 0; i < oldcolGroups.size(); i++) {
            colGroups.add(new Group((Vector) oldcolGroups.elementAt(i)));
        }
        data.setGroups(rowGroups);
        data.setColumnGroups(colGroups);

    }

    public void HashtableToDataSet(Hashtable h, DataSet ret) {// throws IOException{
        //System.out.print("+");
        ImageIcon ICON = null;
        if (!Preparetest && test && !h.containsKey("Test")) {
            return;
        }

        if (h.containsKey("linked")) {
            ret.setLinked(((Boolean) h.get("linked")).booleanValue());
        }
        if (h.containsKey("data")) {

            Object dat = h.get("data");
            if (dat instanceof double[][]) {
                ret.setData((double[][]) dat);
            } else if (dat instanceof float[][]) {
                ret.setData(fromFloat((float[][]) dat));
            }

        }
        if (h.containsKey(PROP_DATABASE_ID)) {
            ret.setDatabaseId((String) h.get(PROP_DATABASE_ID));
        }
        if (h.containsKey("name")) {
            ret.setFile((String) h.get("name"));
        }
        if (h.containsKey("info")) {
            ret.setInfo((String) h.get("info"));
        }
        if (h.containsKey("colnames")) {
            ret.setColNames((String[]) h.get("colnames"));
        }
        if (h.containsKey("nulls")) {
            ret.setnulls((boolean[][]) h.get("nulls"));
        }
        if (h.containsKey("infos")) {
            ret.setInfos((String[][]) h.get("infos"));
        } else {
            System.out.print("\nNo infos");
        }
        if (h.containsKey("infoheaders")) {
            ret.setInfoHeaders((String[]) h.get("infoheaders"));
        }
        if (h.containsKey("colinfos")) {

            String[][] clinf = (String[][]) h.get("colinfos");
//            
//            for(int i=0;i<clinf.length;i++){
//                System.out.print("\n>"+clinf[i][0]);
//            }
//            
            ret.setColInfos(clinf);

        } else {
            System.out.print("\nNo Colinfos");
        }
        if (h.containsKey("colinfoheaders")) {
            ret.setColInfoHeaders((String[]) h.get("colinfoheaders"));
        }

        if (h.containsKey("Demodata")) {
            ret.setDemoData((Boolean) h.get("Demodata"));
        }


        if (h.containsKey("parentIndexes")) {
            ret.setparentIndexes((boolean[]) h.get("parentIndexes"));
        }
        if (h.containsKey("parentColumnIndexes")) {
            ret.setparentColumnIndexes((boolean[]) h.get("parentColumnIndexes"));
        }

        if (h.containsKey("meta")) {
            ret.setMetaList(expresscomponents.Documentation.MetaInfoList.convertFromOld((String) h.get("meta")));
        } else if (h.containsKey("MetaList")) {
            ret.setMetaList((expresscomponents.Documentation.MetaInfoList) h.get("MetaList"));
        }
        //h.put("MetaList", data.getMetaList());

        if (h.containsKey("childcount")) {
            ret.setChildCount(((Integer) h.get("childcount")).intValue());
        }
        if (h.containsKey("icon")) {
            ret.setIcon((String) h.get("icon"));
        }

        //This on is new (july 03)..
        if (h.containsKey("groups")) {
            //expresscomponents.LineStyles.LineMark.remapIntegersToMarks((Vector)h.get("groups") );
            ret.setGroups((Vector) h.get("groups"));
        }
        if (h.containsKey("logtransformed")) {
            ret.setLogTransformed(((Boolean) h.get("logtransformed")).booleanValue());
        }
        if (h.containsKey("ratios")) {
            ret.setRatios(((Boolean) h.get("ratios")).booleanValue());
        }
        if (h.containsKey("maxminfromparent")) {
            ret.setmaxminFromParent(((Boolean) h.get("maxminfromparent")).booleanValue());
        }
        if (h.containsKey("structures")) {
            ret.structures = (Hashtable) h.get("structures");
        }
        if (h.containsKey("externaliconset")) {
            ret.setExternalIconSet(((Boolean) h.get("externaliconset")).booleanValue());
            if (((Boolean) h.get("externaliconset")).booleanValue() && (h.containsKey("externalIcon"))) {
                ICON = (ImageIcon) h.get("externalIcon");
                ret.setIcon(ICON);
            }

            if (((Boolean) h.get("externaliconset")).booleanValue() && (h.containsKey("externalIconClip"))) {
                ICON = new ImageIcon(((ImageAnalysis.ChipView.ClipImage) h.get("externalIconClip")).getImage());
                ret.setIcon(ICON);
            }
        }
        if (h.containsKey("columngroups")) {
            ret.setColumnGroups((Vector) h.get("columngroups"));
        }
        if (h.containsKey("isTransposed")) {
            ret.setTransposed(((Boolean) h.get("isTransposed")).booleanValue());
        }

        if (h.containsKey("columnarrangement")) {
            ret.setColumnArrangement((int[]) h.get("columnarrangement"));
        }
        if (h.containsKey("rowarrangement")) {
            ret.setRowArrangement((int[]) h.get("rowarrangement"));
        }

        if (h.containsKey("fileRowMapping")) {
            ret.fileRowMapping = ((int[]) h.get("fileRowMapping"));
        }
        if (h.containsKey("fileColMapping")) {
            ret.fileColMapping = ((int[]) h.get("fileColMapping"));
        }
        // h.put("fileRowMapping",data.fileRowMapping);

        if (h.containsKey("infoIDColumn")) {
            ret.setInfoIDColumn(((Integer) h.get("infoIDColumn")).intValue());  //.setIn.fileColMapping= ( (int[])h.get("fileColMapping") );
        }        //h.put("infoIDColumn",new Integer(data.getInfoIDColumn()));



        if (h.containsKey("userow")) {
            ret.setUsedInfos((boolean[]) h.get("userow"));
        }
        if (h.containsKey("usecol")) {
            ret.setUsedColInfos((boolean[]) h.get("usecol"));
        }

        if (h.containsKey("children")) {
            Vector v = (Vector) h.get("children");

            for (int i = 0; i < v.size(); i++) {
                Object o = v.elementAt(i);
                Hashtable hs = (Hashtable) o;
                //if(o instanceof DataSet) ch.addElement(DataSetToHashtable((DataSet)o));
                //else if(o instanceof Node) ch.addElement(((Node)o).getHash());

                if (hs.containsKey("type")) {
                    ret.add(Node.fromHash(hs, null));
                } else {
                    ret.add(HashtableToDataSet((Hashtable) v.elementAt(i)));
                }
            }

        }
        convertOldGroups(ret);


        if (h.containsKey(PROP_LAST_MODIFIED)) {
            try {
                ret.setLastModified((Date) h.get(PROP_LAST_MODIFIED));
            } catch (Exception e) {
                e.printStackTrace();
                ret.setLastModified(new Date());
            }
        }
        ret.clearModifiedState();
    }
*/
    /*
     * public DataSet loadDataSet(){ FileDialog fd; File f; DataSet ret=null;
     *
     * try{ fd=new FileDialog(new Frame()); fd.setMode(FileDialog.LOAD);
     * fd.show();
     *
     * f=new File(fd.getDirectory()+fd.getFile());
     *
     *
     * if(fd.getFile()!=null){ lastpath=fd.getDirectory()+fd.getFile();
     * FileInputStream istream = new FileInputStream(f); BufferedInputStream
     * buffer=new BufferedInputStream(istream); ObjectInputStream p = new
     * ObjectInputStream(buffer);
     *
     * String version = (String)p.readObject(); if(version.equals("v1.0"))
     * ret=loadDataSetV10(p); else if(version.equals("v1.1"))
     * ret=loadDataSetV11(p); istream.close(); }
     *
     *
     * }catch (Exception e){e.printStackTrace();} return ret; }
     *
     */
    /*
    public DataSet loadDataSetV10(ObjectInputStream p) throws IOException {

        DataSet ret = new DataSet();
        try {

            //   System.out.print("\nREADING");
            //   String version= p.readUTF();
            boolean[] e = (boolean[]) p.readObject(); //This array tells if the objects exists and are not null.

            double[][] data = null;
            String file = null;
            String info = null;
            String[] names = null;
            String[] colnames = null;
            boolean[][] nulls = null;
            java.awt.Color[] colors = null;
            String icon = null;
            Integer childCount = null;
            Vector classes = null;

            if (e[0]) {
                data = (double[][]) p.readObject();
            }
            if (e[1]) {
                file = (String) p.readObject();
            }
            if (e[2]) {
                info = (String) p.readObject();
            }
            if (e[3]) {
                names = (String[]) p.readObject();
            }
            if (e[4]) {
                colnames = (String[]) p.readObject();
            }
            if (e[5]) {
                nulls = (boolean[][]) p.readObject();
            }
            if (e[6]) {
                colors = (java.awt.Color[]) p.readObject();
            }
            childCount = (Integer) p.readObject();
            icon = (String) p.readObject();

            String[][] newInfos = new String[names.length][1];
            for (int i = 0; i < newInfos.length; i++) {
                newInfos[i][0] = names[i];
            }


            if (e[0]) {
                ret.setData(data);
            }
            if (e[1]) {
                ret.setFile(file);
            }
            if (e[2]) {
                ret.setInfo(info);
            }
            if (e[3]) {
                ret.setInfos(newInfos);
            }
            if (e[4]) {
                ret.setColNames(colnames);
            }
            if (e[5]) {
                ret.setnulls(nulls);
            }


            // if(e[6]) ret.setColors(colors);
            ret.setChildCount(childCount.intValue());
            //if(e[7]) ret.setIcon(icon);
            ret.setIcon(icon);

        } //catch(IOException e){e.printStackTrace();}
        catch (ClassNotFoundException e) {
            errorMsg err = new errorMsg(3);
        }

        return ret;
    }

    public DataSet loadDataSetV11(ObjectInputStream p) throws IOException {

        DataSet ret = new DataSet();
        try {

            //   System.out.print("\nREADING");
            //   String version= p.readUTF();
            boolean[] e = (boolean[]) p.readObject(); //This array tells if the objects exists and are not null.

            double[][] data = null;
            String file = null;
            String info = null;
            String[] names = null;
            String[] colnames = null;
            String[] infos = null;
            String[][] newInfos = null;
            boolean[] parentIndexes = null;
            boolean[][] nulls = null;
            String icon = null;
            Integer childCount = null;
            Vector classes = null;
            String meta = "";

            if (e[0]) {
                data = (double[][]) p.readObject();
            }
            if (e[1]) {
                file = (String) p.readObject();
            }
            if (e[2]) {
                info = (String) p.readObject();
            }
            if (e[3]) {
                names = (String[]) p.readObject();
            }
            if (e[4]) {
                colnames = (String[]) p.readObject();
            }
            if (e[5]) {
                nulls = (boolean[][]) p.readObject();
            }
            if (e[6]) {
                infos = (String[]) p.readObject();
            }
            if (e.length > 7 && e[7]) {
                parentIndexes = (boolean[]) p.readObject();
            }

            if (e[3] && e[6]) {
                newInfos = new String[names.length][2];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];
                    newInfos[i][1] = infos[i];

                }
            }

            if (e[3] && !e[6]) {
                newInfos = new String[names.length][1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];
                }
            }


            meta = (String) p.readObject();
            childCount = (Integer) p.readObject();
            icon = (String) p.readObject();
            classes = (Vector) p.readObject();
            //if(e[6]){
            //newinfos=new String[infos.length][1];
            //for(int i=0;i<infos.length;i++) newinfos[i][0]=infos[i];
            //}


            if (e[0]) {
                ret.setData(data);
            }
            if (e[1]) {
                ret.setFile(file);
            }
            if (e[2]) {
                ret.setInfo(info);
            }
            //if(e[3]) ret.setNames(names);
            if (e[4]) {
                ret.setColNames(colnames);
            }
            if (e[5]) {
                ret.setnulls(nulls);
            }
            ret.setInfos(newInfos);
            if (e.length > 7 && e[7]) {
                ret.setparentIndexes(parentIndexes);
            }

//            ret.setMeta(meta);
            ret.setChildCount(childCount.intValue());
            ret.setIcon(icon);
            ret.setGroups(classes);
        } //catch(IOException e){e.printStackTrace();}
        //catch(ClassNotFoundException e){}
        catch (Exception e) {
            errorMsg err = new errorMsg(3);
        }
        return ret;
    }

    public DataSet loadDataSetV12(ObjectInputStream p) throws IOException {

        DataSet ret = new DataSet();
        try {

            //   System.out.print("\nREADING");
            //   String version= p.readUTF();
            boolean[] e = (boolean[]) p.readObject(); //This array tells if the objects exists and are not null.

            double[][] data = null;
            String file = null;
            String info = null;
            String[] names = null;
            String[] colnames = null;
            String[][] infos = null;
            String[][] newInfos = null;
            boolean[] parentIndexes = null;
            boolean[][] nulls = null;
            String icon = null;
            Integer childCount = null;
            Vector classes = null;
            String meta = "";
            Boolean externalIcon = null;
            ImageIcon ICON = null;

            if (e[0]) {
                data = (double[][]) p.readObject();
            }
            if (e[1]) {
                file = (String) p.readObject();
            }
            if (e[2]) {
                info = (String) p.readObject();
            }
            if (e[3]) {
                names = (String[]) p.readObject();
            }
            if (e[4]) {
                colnames = (String[]) p.readObject();
            }
            if (e[5]) {
                nulls = (boolean[][]) p.readObject();
            }
            if (e[6]) {
                infos = (String[][]) p.readObject();
            }
            if (e.length > 7 && e[7]) {
                parentIndexes = (boolean[]) p.readObject();
            }

            meta = (String) p.readObject();
            childCount = (Integer) p.readObject();
            icon = (String) p.readObject();
            classes = (Vector) p.readObject();

            if (e[3] && e[6]) {
                newInfos = new String[names.length][infos[0].length + 1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];

                    for (int j = 1; j < newInfos[0].length; j++) {
                        newInfos[i][j] = infos[i][j - 1];
                    }
                }
            } else if (e[3] && !e[6]) {
                newInfos = new String[names.length][1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];
                }
            } else if (!e[3] && e[6]) {
                newInfos = infos;
            }

            if (e[0]) {
                ret.setData(data);
            }
            if (e[1]) {
                ret.setFile(file);
            }
            if (e[2]) {
                ret.setInfo(info);
            }
            //            if(e[3]) ret.setNames(names);
            if (e[4]) {
                ret.setColNames(colnames);
            }
            if (e[5]) {
                ret.setnulls(nulls);
            }
            //if(e[6])
            ret.setInfos(newInfos);
            if (e.length > 7 && e[7]) {
                ret.setparentIndexes(parentIndexes);
            }

//            ret.setMeta(meta);
            ret.setChildCount(childCount.intValue());
            ret.setIcon(icon);
            ret.setGroups(classes);
        } //catch(IOException e){e.printStackTrace();}
        //catch(ClassNotFoundException e){}
        catch (Exception e) {
            errorMsg err = new errorMsg(3);
        }
        return ret;
    }

    public DataSet loadDataSetV13(ObjectInputStream p) throws IOException {

        DataSet ret = new DataSet();
        try {

            //   System.out.print("\nREADING");
            //   String version= p.readUTF();
            boolean[] e = (boolean[]) p.readObject(); //This array tells if the objects exists and are not null.

            double[][] data = null;
            String file = null;
            String info = null;
            String[] names = null;
            String[] colnames = null;
            String[][] infos = null;
            String[][] newInfos = null;
            boolean[] parentIndexes = null;
            boolean[][] nulls = null;
            String icon = null;
            Integer childCount = null;
            Vector classes = null;
            String meta = "";
            Boolean externalIcon = null;
            ImageIcon ICON = null;

            if (e[0]) {
                data = (double[][]) p.readObject();
            }
            if (e[1]) {
                file = (String) p.readObject();
            }
            if (e[2]) {
                info = (String) p.readObject();
            }
            if (e[3]) {
                names = (String[]) p.readObject();
            }
            if (e[4]) {
                colnames = (String[]) p.readObject();
            }
            if (e[5]) {
                nulls = (boolean[][]) p.readObject();
            }
            if (e[6]) {
                infos = (String[][]) p.readObject();
            }
            if (e.length > 7 && e[7]) {
                parentIndexes = (boolean[]) p.readObject();
            }

            meta = (String) p.readObject();
            childCount = (Integer) p.readObject();
            icon = (String) p.readObject();
            classes = (Vector) p.readObject();

            externalIcon = (Boolean) p.readObject();
            //p.writeObject(new Boolean(data.externalIconSet()));
            if (externalIcon.booleanValue()) {
                ICON = (ImageIcon) p.readObject();
                ret.setIcon(ICON);
            }

            if (e[3] && e[6]) {
                newInfos = new String[names.length][infos[0].length + 1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];

                    for (int j = 1; j < newInfos[0].length; j++) {
                        newInfos[i][j] = infos[i][j - 1];
                    }
                }
            } else if (e[3] && !e[6]) {
                newInfos = new String[names.length][1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];
                }
            } else if (!e[3] && e[6]) {
                newInfos = infos;
            }

            if (e[0]) {
                ret.setData(data);
            }
            if (e[1]) {
                ret.setFile(file);
            }
            if (e[2]) {
                ret.setInfo(info);
            }
            //            if(e[3]) ret.setNames(names);
            if (e[4]) {
                ret.setColNames(colnames);
            }
            if (e[5]) {
                ret.setnulls(nulls);
            }
            //if(e[6])
            ret.setInfos(newInfos);
            if (e.length > 7 && e[7]) {
                ret.setparentIndexes(parentIndexes);
            }

//            ret.setMeta(meta);
            ret.setChildCount(childCount.intValue());
            ret.setIcon(icon);
            ret.setGroups(classes);
        } //catch(IOException e){e.printStackTrace();}
        //catch(ClassNotFoundException e){}
        catch (Exception e) {
            errorMsg err = new errorMsg(3);
        }
        return ret;
    }

    public DataSet loadDataSetV14(ObjectInputStream p) throws IOException {

        DataSet ret = new DataSet();
        try {

            //   System.out.print("\nREADING");
            //   String version= p.readUTF();
            boolean[] e = (boolean[]) p.readObject(); //This array tells if the objects exists and are not null.

            double[][] data = null;
            String file = null;
            String info = null;
            String[] names = null;
            String[] colnames = null;
            String[][] infos = null;
            String[][] newInfos = null;
            boolean[] parentIndexes = null;
            boolean[][] nulls = null;
            String icon = null;
            Integer childCount = null;
            Vector classes = null;
            String meta = "";
            Boolean externalIcon = null;
            ImageIcon ICON = null;
            boolean logtrans = false;
            boolean ratios = false;

            if (e[0]) {
                data = (double[][]) p.readObject();
            }
            if (e[1]) {
                file = (String) p.readObject();
            }
            if (e[2]) {
                info = (String) p.readObject();
            }
            if (e[3]) {
                names = (String[]) p.readObject();
            }
            if (e[4]) {
                colnames = (String[]) p.readObject();
            }
            if (e[5]) {
                nulls = (boolean[][]) p.readObject();
            }
            if (e[6]) {
                infos = (String[][]) p.readObject();
            }
            if (e.length > 7 && e[7]) {
                parentIndexes = (boolean[]) p.readObject();
            }

            meta = (String) p.readObject();
            childCount = (Integer) p.readObject();
            icon = (String) p.readObject();
            classes = (Vector) p.readObject();
            logtrans = ((Boolean) p.readObject()).booleanValue();
            ratios = ((Boolean) p.readObject()).booleanValue();
            externalIcon = (Boolean) p.readObject();
            //p.writeObject(new Boolean(data.externalIconSet()));
            if (externalIcon.booleanValue()) {
                ICON = (ImageIcon) p.readObject();
                ret.setIcon(ICON);
            }

            if (e[3] && e[6]) {
                newInfos = new String[names.length][infos[0].length + 1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];

                    for (int j = 1; j < newInfos[0].length; j++) {
                        newInfos[i][j] = infos[i][j - 1];
                    }
                }
            } else if (e[3] && !e[6]) {
                newInfos = new String[names.length][1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];
                }
            } else if (!e[3] && e[6]) {
                newInfos = infos;
            }

            if (e[0]) {
                ret.setData(data);
            }
            if (e[1]) {
                ret.setFile(file);
            }
            if (e[2]) {
                ret.setInfo(info);
            }
            //            if(e[3]) ret.setNames(names);
            if (e[4]) {
                ret.setColNames(colnames);
            }
            if (e[5]) {
                ret.setnulls(nulls);
            }
            //if(e[6])
            ret.setInfos(newInfos);
            if (e.length > 7 && e[7]) {
                ret.setparentIndexes(parentIndexes);
            }

//            ret.setMeta(meta);
            ret.setChildCount(childCount.intValue());
            ret.setIcon(icon);
            ret.setGroups(classes);
            ret.setLogTransformed(logtrans);
            ret.setRatios(ratios);
        } //catch(IOException e){e.printStackTrace();}
        //catch(ClassNotFoundException e){}
        catch (Exception e) {
            errorMsg err = new errorMsg(3);
        }
        return ret;
    }

    public DataSet loadDataSetV15(ObjectInputStream p) throws IOException {

        DataSet ret = new DataSet();
        try {

            boolean[] e = (boolean[]) p.readObject(); //This array tells if the objects exists and are not null.

            double[][] data = null;
            String file = null;
            String info = null;
            String[] names = null;
            String[] colnames = null;
            String[][] infos = null;
            String[][] newInfos = null;
            boolean[] parentIndexes = null;
            boolean[][] nulls = null;
            String icon = null;
            Integer childCount = null;
            Vector classes = null;
            String meta = "";
            Boolean externalIcon = null;
            ImageIcon ICON = null;
            boolean logtrans = false;
            boolean ratios = false;
            boolean maxminFromParent = false;

            if (e[0]) {
                data = (double[][]) p.readObject();
            }
            if (e[1]) {
                file = (String) p.readObject();
            }
            if (e[2]) {
                info = (String) p.readObject();
            }
            if (e[3]) {
                names = (String[]) p.readObject();
            }
            if (e[4]) {
                colnames = (String[]) p.readObject();
            }
            if (e[5]) {
                nulls = (boolean[][]) p.readObject();
            }
            if (e[6]) {
                infos = (String[][]) p.readObject();
            }
            if (e.length > 7 && e[7]) {
                parentIndexes = (boolean[]) p.readObject();
            }

            meta = (String) p.readObject();
            childCount = (Integer) p.readObject();
            icon = (String) p.readObject();
            classes = (Vector) p.readObject();
            logtrans = ((Boolean) p.readObject()).booleanValue();
            ratios = ((Boolean) p.readObject()).booleanValue();
            maxminFromParent = ((Boolean) p.readObject()).booleanValue();
            externalIcon = (Boolean) p.readObject();
            //p.writeObject(new Boolean(data.externalIconSet()));


            if (externalIcon.booleanValue()) {
                ICON = (ImageIcon) p.readObject();
                ret.setIcon(ICON);
            }

            if (e[3] && e[6]) {
                newInfos = new String[names.length][infos[0].length + 1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];

                    for (int j = 1; j < newInfos[0].length; j++) {
                        newInfos[i][j] = infos[i][j - 1];
                    }
                }
            } else if (e[3] && !e[6]) {
                newInfos = new String[names.length][1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];
                }
            } else if (!e[3] && e[6]) {
                newInfos = infos;
            }

            if (e[0]) {
                ret.setData(data);
            }
            if (e[1]) {
                ret.setFile(file);
            }
            if (e[2]) {
                ret.setInfo(info);
            }
            //            if(e[3]) ret.setNames(names);
            if (e[4]) {
                ret.setColNames(colnames);
            }
            if (e[5]) {
                ret.setnulls(nulls);
            }
            //if(e[6])
            ret.setInfos(newInfos);
            if (e.length > 7 && e[7]) {
                ret.setparentIndexes(parentIndexes);
            }

//            ret.setMeta(meta);
            ret.setChildCount(childCount.intValue());
            ret.setIcon(icon);
            ret.setGroups(classes);
            ret.setLogTransformed(logtrans);
            ret.setRatios(ratios);
            ret.setmaxminFromParent(maxminFromParent);
        } //catch(IOException e){e.printStackTrace();}
        //catch(ClassNotFoundException e){}
        catch (Exception e) {
            errorMsg err = new errorMsg(3);
        }
        return ret;
    }

    public DataSet loadDataSetV16(ObjectInputStream p) throws IOException {

        DataSet ret = new DataSet();
        try {

            boolean[] e = (boolean[]) p.readObject(); //This array tells if the objects exists and are not null.

            double[][] data = null;
            String file = null;
            String info = null;
            String[] names = null;
            String[] colnames = null;
            String[][] infos = null;
            String[][] newInfos = null;
            String[] infoHeaders = null;
            boolean[] parentIndexes = null;
            boolean[][] nulls = null;
            String icon = null;
            Integer childCount = null;
            Vector classes = null;
            String meta = "";
            Boolean externalIcon = null;
            ImageIcon ICON = null;
            boolean logtrans = false;
            boolean ratios = false;
            boolean maxminFromParent = false;

            if (e[0]) {
                data = (double[][]) p.readObject();
            }
            if (e[1]) {
                file = (String) p.readObject();
            }
            if (e[2]) {
                info = (String) p.readObject();
            }
            if (e[3]) {
                names = (String[]) p.readObject();
            }
            if (e[4]) {
                colnames = (String[]) p.readObject();
            }
            if (e[5]) {
                nulls = (boolean[][]) p.readObject();
            }
            if (e[6]) {
                infos = (String[][]) p.readObject();
            }
            if (e[6]) {
                infoHeaders = (String[]) p.readObject();
            }
            if (e.length > 7 && e[7]) {
                parentIndexes = (boolean[]) p.readObject();
            }

            meta = (String) p.readObject();
            childCount = (Integer) p.readObject();
            icon = (String) p.readObject();
            classes = (Vector) p.readObject();
            logtrans = ((Boolean) p.readObject()).booleanValue();
            ratios = ((Boolean) p.readObject()).booleanValue();
            maxminFromParent = ((Boolean) p.readObject()).booleanValue();
            externalIcon = (Boolean) p.readObject();
            //p.writeObject(new Boolean(data.externalIconSet()));


            if (externalIcon.booleanValue()) {
                ICON = (ImageIcon) p.readObject();
                ret.setIcon(ICON);
            }

            if (e[3] && e[6]) {
                newInfos = new String[names.length][infos[0].length + 1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];

                    for (int j = 1; j < newInfos[0].length; j++) {
                        newInfos[i][j] = infos[i][j - 1];
                    }
                }
            } else if (e[3] && !e[6]) {
                newInfos = new String[names.length][1];
                for (int i = 0; i < newInfos.length; i++) {
                    newInfos[i][0] = names[i];
                }
            } else if (!e[3] && e[6]) {
                newInfos = infos;
            }

            if (e[0]) {
                ret.setData(data);
            }
            if (e[1]) {
                ret.setFile(file);
            }
            if (e[2]) {
                ret.setInfo(info);
            }
            //            if(e[3]) ret.setNames(names);
            if (e[4]) {
                ret.setColNames(colnames);
            }
            if (e[5]) {
                ret.setnulls(nulls);
            }
            //if(e[6])
            ret.setInfos(newInfos);
            ret.setInfoHeaders(infoHeaders);
            if (e.length > 7 && e[7]) {
                ret.setparentIndexes(parentIndexes);
            }

//            ret.setMeta(meta);
            ret.setChildCount(childCount.intValue());
            ret.setIcon(icon);
            ret.setGroups(classes);
            ret.setLogTransformed(logtrans);
            ret.setRatios(ratios);
            ret.setmaxminFromParent(maxminFromParent);
        } //catch(IOException e){e.printStackTrace();}
        //catch(ClassNotFoundException e){}
        catch (Exception e) {
            errorMsg err = new errorMsg(3);
        }
        return ret;
    }

    public void saveTree(gen trunk, DataSet data) {
        JFileChooser fd;
        File f;

        try {

            fd = new JFileChooser();
            fd.setFileFilter(new FileFilter() {

                public boolean accept(File f) {
                    if (f.getName().endsWith(".txt")) {
                        return true;
                    } else if (f.isDirectory()) {
                        return true;
                    } else {
                        return false;
                    }
                }

                public String getDescription() {
                    return "Text Files (*.txt)";
                }
            });

            if (lastpath != null && lastpath != "") {
                fd.setCurrentDirectory(new File(lastpath));
            }
            int returnVal = fd.showSaveDialog(new JFrame());
            //fd.showSaveDialog(new JFrame());
            f = fd.getSelectedFile();
            File dir = fd.getCurrentDirectory();

            if (f != null && !f.isDirectory() && returnVal == JFileChooser.APPROVE_OPTION) {
                this.lastpath = dir.getAbsolutePath();
                ostream = new FileOutputStream(f);
                BufferedOutputStream buffer = new BufferedOutputStream(ostream);
                DataOutputStream o = new DataOutputStream(buffer);

                writegene(trunk, data, o);
                buffer.flush();
                o.flush();
                buffer.close();
                o.close();
                ostream.close();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void writegene(gen root, DataSet data, DataOutputStream dataout) throws Exception {
        String ret = "";

        if (root.merged) {
            dataout.writeBytes("(");
            writegene(root.right, data, dataout);
            dataout.writeBytes(",");
            writegene(root.left, data, dataout);
            dataout.writeBytes(")");
        } else {
            int count = 0;
            for (int j = 0; j < data.getInfoHeaders().length; j++) {
                if (data.getusedInfos()[j]) {
                    ret += data.getInfos()[root.nme][j];
                    if (count != data.usedInfoCount() - 1) {
                        ret = ret + "\t";
                    }
                    count++;
                }
            }


            dataout.writeBytes(ret);

            //if(a.names[intvalue(trunk.name)].indexOf(name)!=-1){

        }



    }

    public int intval(String s) {
        Integer i = new Integer(s);
        return i.intValue();
    }
}
*/
    /*
class proFileView extends FileView {

    Icon proIcon = new ImageIcon(getClass().getResource("/expresscomponents/Visuals/im/fdat.gif"));
    //Icon classIcon = new ImageIcon( getClass().getResource( "im/cfolder.gif" ) );

   
    public String getName(File file) {
        String filename = file.getName();
        if (filename.endsWith(".java")) {
            filename += " : " + file.length();
            return filename;
        }
        return null;
    }
*/
    /**
     * Return special icons for .java and .class files
     */
    /*
    public Icon getIcon(File file) {
        // default icons for all directories
        if (file.isDirectory()) {
            return null;
        }
        String filename = file.getName().toLowerCase();
        if (filename.endsWith(".pro")) {
            return proIcon;
        }
        //else if (filename.endsWith(".class")) {
        //  return classIcon;
        //}
        return null;
    }
    */
    
    /*
     * public static void main(String args[]) { SwingUtilities.invokeLater(new
     * Runnable() { public void run() { JFileChooser fileChooser = new
     * JFileChooser("."); FileView view = new JavaFileView();
     * fileChooser.setFileView(view); int status =
     * fileChooser.showOpenDialog(null); System.exit(0); } }); }
     */
}
