/*

 * MetaInfoList.java

 *

 * Created on 30. juli 2003, 15:32

 */
package no.uib.jexpress_modularized.core.visualization.documentation;

/**
 *
 *
 *
 * @author bjarte dysvik
 *
 */
public class MetaInfoList extends java.util.Vector implements java.io.Serializable {

    /**
     * Creates a new instance of MetaInfoList
     */
    public MetaInfoList() {
    }

    public MetaInfoNode getMetaNodeAt(int i) {

        return (MetaInfoNode) this.elementAt(i);

    }

    public MetaInfoList copyList() {

        MetaInfoList copy = new MetaInfoList();

        for (int i = 0; i < size(); i++) {
            if (getMetaNodeAt(i) != null) {
                copy.addElement(getMetaNodeAt(i).copyNode());
            }
        }

        return copy;

    }

    public static MetaInfoList convertFromOld(String oldList) {

        String[] strings = convertFromRTF(oldList).split("\n");

        MetaInfoNode metainfonode = new MetaInfoNode(MetaInfoNode.old);

        int space = 0;

        int count = 1;

        //metainfonode.put("Old Meta Tag", oldList);



        for (int i = 0; i < strings.length; i++) {

            if (strings[i].trim() != "") {

                metainfonode.put("Old Meta Tag " + String.valueOf(count), strings[i]);

                count++;

                //System.out.print(strings[i]);

                space = 0;

            } else {
                space++;
            }



            if (space == 1) {

                metainfonode.put("Old Meta Tag " + String.valueOf(count), " ");

                count++;

            }

        }

        MetaInfoList ret = new MetaInfoList();

        ret.addElement(metainfonode);



        return ret;

    }

    public static void main(String[] args) {





        try {

            java.lang.StringBuffer buffer = new java.lang.StringBuffer();



            java.io.File f = new java.io.File("c:/java/tmp.txt");



            java.io.FileReader reader = new java.io.FileReader(f);

            java.io.BufferedReader read = new java.io.BufferedReader(reader);



            String line = read.readLine();





            while (line != null) {

                buffer.append(line);

                line = read.readLine();

            }





            reader.close();



            String s = buffer.toString();



            // System.out.print("\n"+s);



            convertFromOld(s);



        } catch (Exception e) {
        }

        //convertFromRTF(s);

    }

    public static String convertFromRTF(String text) {

        String ret = "";



        javax.swing.text.DefaultStyledDocument doc = new javax.swing.text.DefaultStyledDocument();



        javax.swing.text.rtf.RTFEditorKit rtfkit = new javax.swing.text.rtf.RTFEditorKit();

        //java.io.StringWriter writer = new java.io.StringWriter();



        //java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();

        java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(text.getBytes());



        java.io.CharArrayWriter writer = new java.io.CharArrayWriter();





        //java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(text.getBytes());

        //java.io.StringReader reader = new java.io.StringReader(text);

        try {

            // doc.insertString(0,text, null);

            rtfkit.read(in, doc, 0);

            ret = doc.getText(0, doc.getLength());

            //rtfkit.write(writer, doc, 0, doc.getLength());

        } catch (Exception e) {
            e.printStackTrace();
        }





        //System.out.print("\n"+writer.toString());



        return ret;





    }
}
