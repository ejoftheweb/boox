/*reated on 02 October 2007, 10:06
 *
 * This code is copyright PLATOSYS,
 * to the extent that copyright subsists.
 *
 *
 */

package uk.co.platosys.xservlets;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.mail.SimpleEmail;
import org.apache.ecs.xhtml.h1;
import org.apache.ecs.xhtml.li;
import org.apache.ecs.xhtml.p;
import org.apache.ecs.xhtml.ul;
//import uk.co.platosys.peerSite.PeerSiteConstants;
import uk.co.platosys.webplates.Webplate;

/**
 *
 * @author edward
 */
public class ServletErrorPages {

    public static Webplate serverError(HttpServletRequest request, Exception e){
        
        try{
            SimpleEmail errorMail=new SimpleEmail();
            errorMail.addTo(XservletProperties.DEVELOPER_EMAIL);
            errorMail.setHostName(XservletProperties.MAIL_HOST);
            errorMail.setSubject("Xservlet Error in Servlet: "+e.getClass().getName());
            errorMail.setFrom(XservletProperties.INSTALLATION_EMAIL);
            String errorString = "The Xservlets application server at "+ XservletProperties.APPLICATION_NAME+" threw an exception: \n\n";
            errorString=errorString+"The error was: "+e.getClass().getName()+"\n\n";
            errorString=errorString+"The error message was: "+e.getMessage()+"\n\n";
            errorString=errorString+"The stack trace follows: \n\n";
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (int i=0; i<stackTrace.length; i++){
                errorString=errorString+stackTrace[i]+"\n";
            }
            errorMail.setMsg(errorString);
            errorMail.send();
        }catch(Exception ex){}

      Webplate responseDocument=new Webplate(request, "PLATOSYS SERVER ERROR");
      responseDocument.appendBody(new h1("Sorry - a server error occurred"));
      responseDocument.appendBody(new p().addElement("While processing your request, a "+e.getClass().getSimpleName()+" error happened on the server. This is probably our fault, because if we had built our software properly no errors could happen."));
      responseDocument.appendBody(new p().addElement("The server reported:"+ e.getMessage()));
      responseDocument.appendBody(new p().addElement("Unfortunately, even our dedicated developers can make mistakes. An email containing the stack  trace of the error has been sent to the responsible developer who will be made to sit in the naughty corner until the bug is fixed.")); 
      responseDocument.appendBody(new p().addElement("For security reasons, your session has now been terminated and you have been logged out. Sorry"));
      return responseDocument;
    }
    /*
    protected static Webplate serverError(){
        
    }
    protected static Webplate serverError(String message){
        
    }
    protected static Webplate serverError(Exception e, String message){
        
    }
     */
    public static Webplate badRequestError(HttpServletRequest request){
      Webplate responseDocument=new Webplate(request, "PLATOSYS SERVER ERROR");
      responseDocument.appendBody(new h1("Sorry - a server error occurred"));
      responseDocument.appendBody(new p().addElement("Your browser seems to have submitted a badly-formed request - do you have an up-to-date browser?"));
      String browserString = request.getHeader("User-Agent");
      if (browserString!=null){
        responseDocument.appendBody(new p().addElement("You seem to be using the following browser:" +browserString));
      }else{
          responseDocument.appendBody(new p().addElement("We could not determine " +
                  "what browser you are using. This may well indicate that your browser" +
                  " is old or no longer supported, which can expose your system to" +
                  " unnecessary security risks. Please consider updating your " +
                  "browser to a modern, standards-compliant one such as Firefox"));
          
      }
      return responseDocument;
        
    }
    public static Webplate serverFail(HttpServletRequest request){
       Webplate responseDocument=new Webplate(request,"PLATOSYS SERVER ERROR");
      responseDocument.appendBody(new h1("Sorry - something went wrong"));
      responseDocument.appendBody(new p().addElement("We don't know what. This error should not arise. We have tested and tested and tried to make the error go away"));
      responseDocument.appendBody(new p().addElement("Obviously, this bug escaped. Sorry 'bout that. Can happen.")); 
      return responseDocument;
  
    }
    public static Webplate serverFail(HttpServletRequest request, String message){
       Webplate responseDocument=new Webplate(request,"PLATOSYS SERVER ERROR");
      responseDocument.appendBody(new h1("Sorry - something went wrong"));
      responseDocument.appendBody(new p().addElement("The server said: "+message));
      responseDocument.appendBody(new p().addElement("This error should not arise. We have tested and tested and tried to make the error go away"));
      responseDocument.appendBody(new p().addElement("Obviously, this bug escaped. Sorry 'bout that. Can happen.")); 
      return responseDocument;
  
    }
    public static Webplate incompleteFormError(HttpServletRequest request, List errorList){
        Webplate responseDocument=new Webplate(request, "PLATOSYS SERVER ERROR");
        responseDocument.appendBody(new h1("Sorry - your form was incomplete, we cannot process it"));
        responseDocument.appendBody(new p().addElement("It looks like you forgot to fill in one or more  of the required boxes"));
        responseDocument.appendBody(new p().addElement("The missing items were:"));
        ul lst = new ul();
        responseDocument.appendBody(lst);
        Iterator it = errorList.iterator();
        while (it.hasNext()){
            String error =(String) it.next();
            lst.addElement(new li().addElement(error));
        }
        responseDocument.appendBody(new p().addElement("Please submit your form again"));
        return responseDocument;
    }
        public static Webplate formError(HttpServletRequest request, Exception e){
             Webplate responseDocument=new Webplate(request, "PLATOSYS SERVER ERROR");
            responseDocument.appendBody(new h1("Sorry - There was an error on your form"));
            responseDocument.appendBody(new p().addElement("The error was "+e.getClass().getName()));
            responseDocument.appendBody(new p().addElement("The error message was "+e.getMessage()));
            return responseDocument;
        }
         public static Webplate formRefused(HttpServletRequest request, String msg){
            Webplate responseDocument=new Webplate(request, "PLATOSYS SERVER ERROR");
            responseDocument.appendBody(new h1("Sorry - We cannot process your request"));
            responseDocument.appendBody(new p().addElement(msg));
            responseDocument.appendBody(new p().addElement("Press the Back button on your " +
                    "browser to return to the form and correct it if possible, or give up and go home"));
        return responseDocument;
        }
          public static Webplate badEmailError(HttpServletRequest request){
            Webplate responseDocument=new Webplate(request, "LOGIN ERROR");
            responseDocument.appendBody(new h1("Unknown Email"));
            responseDocument.appendBody(new p().addElement("Sorry, we do not recognise that email address"));
            responseDocument.appendBody(new p().addElement("If you think you made a mistake, please try again, or " +
                    "go to the signup page to register your account with that address"));


            return responseDocument;
        }
        public static Webplate badPasswordError(HttpServletRequest request){
            Webplate responseDocument=new Webplate(request, "LOGIN ERROR");
            responseDocument.appendBody(new h1("Incorrect Password"));
            responseDocument.appendBody(new p().addElement("Sorry, the password you entered was wrong"));
            responseDocument.appendBody(new p().addElement("Please try again using the correct password"));
            p pl = (new p());
            pl.addElement("If you have forgotten your password, please follow a href=\""+XservletProperties.RESET_PASSWORD_LINK+"\"> this link </a> to reset it");
        
            return responseDocument;
        }
        public static Webplate mismatchedPasswordsError(HttpServletRequest request){
            Webplate responseDocument=new Webplate(request, "PLATOSYS SERVER ERROR");
            responseDocument.appendBody(new h1("Passwords Don't Match"));
            responseDocument.appendBody(new p().addElement("Sorry, the passwords you entered don't match"));
            responseDocument.appendBody(new p().addElement("Please try again !"));
            return responseDocument;
        }
        public static Webplate statusError(HttpServletRequest request, String msg){
        Webplate responseDocument=new Webplate(request, "STATUS PROBLEM");
        responseDocument.appendBody(new h1("Sorry - There is a problem with your status"));
        responseDocument.appendBody(new p().addElement(msg));
        responseDocument.appendBody(new p().addElement("please wait or correct your status"));
        return responseDocument;
        }
}
