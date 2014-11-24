/*
 * Copyright Edward Barrow and Platosys.
 * Most Platosys code is licensed under the GPL.
 */

package uk.co.platosys.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author edward
 */
public abstract class URLReader {
    public static boolean getFileFromURL(URL source, File destination) throws java.io.IOException{
        InputStream inputStream=null;
        OutputStream outputStream=null;
        try{
            URLConnection connection =  source.openConnection();
            connection.connect();
            inputStream = new BufferedInputStream(connection.getInputStream());
            outputStream = new BufferedOutputStream(new FileOutputStream(destination));
            int c;
            while ((c=inputStream.read())!=-1){
                outputStream.write(c);
            }
            outputStream.flush();
        }catch(Exception e){
            throw new java.io.IOException("URL reader problem accessing URL", e);

        }finally{
            inputStream.close();
            outputStream.close();
        }
        return true;
    }
    

}
