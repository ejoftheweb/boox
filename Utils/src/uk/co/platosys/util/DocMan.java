/**he editor.
 */

package uk.co.platosys.util;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * A class providing convenient static xml/jdom file read and write utilities.
 * 
 * To read an xml file do DocMan.build(filename);
 * To write an xml file do DocMan.write(filename, document-to-write);
 * 
 * @author edward
 */
public class DocMan {
   private static Logger logger=Logger.getLogger("platosys");
   /**
    * Reads an XML document from a file.
    * @param documentFile
    * @return 
    */
  public static Document build(File documentFile){
      try{
          SAXBuilder builder= new SAXBuilder();
          Document document = builder.build(documentFile);
          return document;
      }catch(Exception e){
          logger.log("DocMan Exception", e);
          return null;
      }
  }
  /**
   * Writes the given Document to the given File
   * @param file
   * @param document
   * @return true if it succeeds
   */
  public static boolean write(File file, Document document){
      try{
          Writer writer = new FileWriter(file);
          Format format = Format.getPrettyFormat();
          format.setLineSeparator(System.getProperty("line.separator"));
          format.setTextMode(Format.TextMode.TRIM_FULL_WHITE);
          XMLOutputter outputter = new XMLOutputter(format);
          outputter.output(document, writer);
          return true;
      }catch(Exception e){
          logger.log("DocMan problem writing xml file", e);
          return false;
      }
  }
  /**
   * writes a Document to a log file; useful for debugging
   * @param document
   * @return 
   */
  public static boolean log(Document document){
      try{
          Writer writer = logger.getWriter() ;
          Format format = Format.getPrettyFormat();
          format.setLineSeparator(System.getProperty("line.separator"));
          format.setTextMode(Format.TextMode.TRIM_FULL_WHITE);
          XMLOutputter outputter = new XMLOutputter(format);
          outputter.output(document, writer);
          return true;
      }catch(Exception e){
          logger.log("DocMan problem logging xml file", e);
          return false;
      }
  }
}
