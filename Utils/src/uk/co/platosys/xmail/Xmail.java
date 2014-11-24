/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.xmail;

import org.apache.commons.mail.SimpleEmail;
import uk.co.platosys.util.Logger;

/**
 *
 * @author edward
 */
public class Xmail extends SimpleEmail {
Logger logger = XmailProperties.XMAIL_LOGGER;
    public Xmail(){
        super();
        setHostName(XmailProperties.MAIL_HOST);
        logger.log("Xmail created");

    }
    public Xmail(Exception x){

     super();
     setHostName(XmailProperties.MAIL_HOST);
     setSubject(x.getClass().getCanonicalName()+" at "+XmailProperties.INSTALLATION_HOST);
     String msg = "This is the Xservlets subsystem at "+XmailProperties.INSTALLATION_HOST+"\n\n";
     msg=msg+" A "+x.getClass().getCanonicalName()+" was thrown, with the message \n"+
             x.getMessage()+"\n";
     msg=msg+"The cause chain was: \n";
     Throwable cause = x.getCause();
     while(cause!=null){
         msg=msg+cause.getClass().getCanonicalName()+ " : "+cause.getMessage()+" \n";
         if (cause.getCause()==null){
             msg=msg+"\n Stack trace of original cause: \n\n";
             StackTraceElement[] causeStack= cause.getStackTrace();
             for(int i=0; i<causeStack.length; i++){
                 msg=msg+causeStack[i].toString()+"\n";
             }
         }
         cause=cause.getCause();
     }
     msg=msg+"\n The stack trace: \n\n ";
     StackTraceElement[] causeStack= x.getStackTrace();
             for(int i=0; i<causeStack.length; i++){
                 msg=msg+causeStack[i].toString()+"\n";
             }
        try{
             this.setMsg(msg);
              this.addTo(XmailProperties.ADMINSISTRATOR_EMAIL);
              if (XmailProperties.CC_SUPPORT){
                     this.addCc("xserlvet.support@platosys.co.uk");
              }
              
         }catch(Exception e){
             logger.log("XMAIL problems", e);
         }
     }



}
