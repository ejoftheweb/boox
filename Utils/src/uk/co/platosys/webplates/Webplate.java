/*
 * Webplate.java
 *
 * Created on 23 September 2007, 09:35
 *
 * This code is copyright PLATOSYS,
 * to the extent that copyright subsists.
 *
 *
 */

package uk.co.platosys.webplates;

import javax.servlet.http.HttpServletRequest;
import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;

/**
 *
 * @author edward
 */
public class Webplate extends XhtmlDocument {
   public static final String DEFAULT_STYLESHEET_NAME="default";
   public static final String DEFAULT_STYLE_FOLDER="styles";
   private String styleSheetName=DEFAULT_STYLESHEET_NAME;
   private String styleFolder=DEFAULT_STYLE_FOLDER;
   private link stylink=new link();
   private HttpServletRequest request;
   /** Creates a new instance of Webplate *
    * It needs a "request" parameter in order to get the context path from which the stylesheet can be located
    * 
    */
    public Webplate(HttpServletRequest request) {
        super();
        this.request=request;
        //doctype declaration
        this.setDoctype(new Doctype.XHtml10Transitional());
        
        //stylesheet declaration
        setStyleSheetName(DEFAULT_STYLESHEET_NAME);
        this.appendHead(stylink);
    }
     /** Creates a new instance of Webplate with the given title */
    public Webplate(HttpServletRequest request, String title) {
        super();
        this.request=request;
        //doctype declaration
        this.setDoctype(new Doctype.XHtml10Transitional());
        
        //stylesheet declaration
        setStyleSheetName(DEFAULT_STYLESHEET_NAME);
        this.appendHead(stylink);
        this.appendTitle(title);
    }
    public void setStyleSheetName(String styleSheetName){
        this.styleSheetName=styleSheetName;
        stylink.setRel("stylesheet");
        stylink.setType("text/css");
        stylink.setHref(request.getContextPath()+"/"+styleFolder+"/"+styleSheetName+".css");
    }
    public void setStyleSheetFolder(String styleSheetFolderName){
        this.styleFolder=styleSheetFolderName;
        setStyleSheetName(styleSheetName);
    }
        
    
}
