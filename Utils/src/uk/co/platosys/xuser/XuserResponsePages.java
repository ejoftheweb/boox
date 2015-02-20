/*
 * XuserResponsePages.java
 *
 * Created on 02 October 2007, 10:06
 *
 * This code is copyright PLATOSYS,
 * to the extent that copyright subsists.
 *
 *
 */

package uk.co.platosys.xuser;

import javax.servlet.http.HttpServletRequest;

import org.apache.ecs.xhtml.h1;
import org.apache.ecs.xhtml.p;

import uk.co.platosys.webplates.Webplate;

/**
 *
 * @author edward
 */
public class XuserResponsePages extends uk.co.platosys.xservlets.ServletErrorPages {

    
   
    
        public static Webplate success(HttpServletRequest request){
             Webplate responseDocument=new Webplate(request,"XUSER System at"+XuserConstants.INSTALLATION_NAME);
            responseDocument.appendBody(new h1("Success!"));
            responseDocument.appendBody(new p().addElement("You have now successfully signed up to "+XuserConstants.INSTALLATION_NAME));
            responseDocument.appendBody(new p().addElement("Have Fun!"));
            return responseDocument;
        }
         public static Webplate fail(HttpServletRequest request, String msg){
            Webplate responseDocument=new Webplate(request, "XUSER System at"+XuserConstants.INSTALLATION_NAME);
            responseDocument.appendBody(new h1("Sorry - your signup confirmation failed"));
            responseDocument.appendBody(new p().addElement(msg));
            responseDocument.appendBody(new p().addElement("Press the Back button on your " +
                    "browser to return to the form and correct it if possible, or give up and go home"));
        return responseDocument;
        }
      
}
