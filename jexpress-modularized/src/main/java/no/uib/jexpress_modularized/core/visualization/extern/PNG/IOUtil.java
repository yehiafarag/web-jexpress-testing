package no.uib.jexpress_modularized.core.visualization.extern.PNG;



import java.io.*;

import java.util.Vector;



/**

 * General IO utilities and helper methods

 */

public class IOUtil implements Serializable

{

    /**

     * Attempts to completely read the contents of a specified

     * file. The file is closed whether or not the read was

     * successful.

     * 

     * @param filename the name of the file to be read

     * 

     * @returns an array of bytes read from the file on success

     * 

     * @throws IOException if an I/O error occurs

     */

    public static byte [] readFully (String filename)

        throws IOException

    {

        return readFully (new FileInputStream (filename));

    }

    

    /**

     * Attempts to completely read the contents of a specified

     * file. The file is closed whether or not the read was

     * successful.

     * 

     * @param file the file to be read

     * 

     * @returns an array of bytes read from the file on success

     * 

     * @throws IOException if an I/O error occurs

     */

    public static byte [] readFully (File file)

        throws IOException

    {

        return readFully (new FileInputStream (file));

    }

    

    /**

     * Attempts to completely read the contents of a specified

     * InputStream. The stream is closed whether or not the

     * read was successful.

     * 

     * @param is the InputStream to read from

     * 

     * @returns an array of bytes read from the stream on success

     * 

     * @throws IOException if an I/O error occurs

     */

    public static byte [] readFully (InputStream is)

        throws IOException

    {

        int size=10000;

        int off=0; /* Offset - how much we've read */

        int got;

        byte [] ret = new byte [size];

        try

        {

            while (true)

            {

                got = is.read (ret, off, size-off);

                if (got==-1) // End of stream

                    break;

                off+=got;

                if (off==size) /* If we've read to the end of our buffer,

                                * enlarge it. */

                {

                    size *= 2;

                    byte [] tmp = new byte [size];

                    System.arraycopy (ret, 0, tmp, 0, off);

                    ret = tmp;

                    

                }

            }

        }

        finally

        {

            close (is);

        }

        /* If we've got a bigger buffer than we need, resize it */

        if (off != size)

        {

            byte [] tmp = new byte [off];

            System.arraycopy (ret, 0, tmp, 0, off);

            ret = tmp;

        }

        return ret;

    }



    /**

     * Attempts to completely read the contents of a specified

     * text file, line by line. The file is closed whether 

     * or not the read was successful.

     * 

     * @param filename the name of the file to be read

     * 

     * @returns a Vector of Strings, one for each line of the file

     * 

     * @throws IOException if an I/O error occurs

     */

    public static Vector readText (String filename)

        throws IOException

    {

        return readText (new FileReader (filename));

    }

        

    /**

     * Attempts to completely read the contents of a specified

     * text file, line by line. The file is closed whether 

     * or not the read was successful.

     * 

     * @param file the file to be read

     * 

     * @returns a Vector of Strings, one for each line of the file

     * 

     * @throws IOException if an I/O error occurs

     */

    public static Vector readText (File file)

        throws IOException

    {

        return readText (new FileReader (file));

    }

    

    /**

     * Attempts to completely read data from the specified

     * Reader, line by line. The Reader is closed whether or

     * not the read was successful.

     * 

     * @param filename the name of the file to be read

     * 

     * @returns a Vector of Strings, one for each line of data

     * 

     * @throws IOException if an I/O error occurs

     */

    public static Vector readText (Reader r)

        throws IOException

    {

        BufferedReader br = new BufferedReader (r);

        try

        {

            Vector ret = new Vector();

            String line;

            while ((line=br.readLine())!=null)

                ret.addElement (line.intern()); // Avoid wasting space

            return ret;

        }

        finally

        {

            close (br);

        }

    }



    /**

     * Attempts to completely write the contents of a Vector

     * of Strings to a specified text file, line by line. 

     * The file is closed whether or not the write was successful.

     * 

     * @param lines a Vector of String objects to write to the file

     * @param filename the name of the file to be written

     * 

     * @throws IOException if an I/O error occurs

     */

    public static void writeText (Vector lines, String filename)

        throws IOException

    {

        writeText (lines, new FileWriter (filename));

    }

    

    /**

     * Attempts to completely write the contents of a Vector

     * of Strings to a specified text file, line by line. 

     * The file is closed whether or not the write was successful.

     * 

     * @param lines a Vector of String objects to write to the file

     * @param file the file to be written

     * 

     * @throws IOException if an I/O error occurs

     */

    public static void writeText (Vector lines, File file)

        throws IOException

    {

        writeText (lines, new FileWriter (file));

    }



    /**

     * Attempts to completely write the contents of a Vector

     * of Strings to a specified writer, line by line.

     * The writer is closed whether or not the write was successful.

     * 

     * @param lines a Vector of String objects to write to the file

     * @param out the Writer to output to

     * 

     * @throws IOException if an I/O error occurs

     */

    public static void writeText (Vector lines, Writer out)

        throws IOException

    {

        BufferedWriter bw = new BufferedWriter (out);

        try

        {

            int l=lines.size();

            for (int i=0; i < l; i++)

            {

                bw.write ((String) lines.elementAt (i));

                bw.newLine();

            }

        }

        finally

        {

            close (bw);

        }

    }

    

    /**

     * Closes the specified InputStream, swallowing any exceptions.

     * 

     * @param in the InputStream to close. May be null.

     */

    public static void close (InputStream in)

    {

        if (in != null)

        {

            try

            {

                in.close();

            }

            catch (IOException e)

            {

            }

        }

    }



    /**

     * Closes the specified OutputStream, swallowing any exceptions.

     * 

     * @param out the OutputStream to close. May be null.

     */

    public static void close (OutputStream out)

    {

        if (out != null)

        {

            try

            {

                out.close();

            }

            catch (IOException e)

            {

            }

        }

    }



    /**

     * Closes the specified Reader, swallowing any exceptions.

     * 

     * @param in the Reader to close. May be null.

     */

    public static void close (Reader in)

    {

        if (in != null)

        {

            try

            {

                in.close();

            }

            catch (IOException e)

            {

            }

        }

    }



    /**

     * Closes the specified Writer, swallowing any exceptions.

     * 

     * @param out the Writer to close. May be null.

     */

    public static void close (Writer out)

    {

        if (out != null)

        {

            try

            {

                out.close();

            }

            catch (IOException e)

            {

            }

        }

    }

}

