/*
 * 
 */

package uk.co.platosys.xmail;

import java.io.File;
import java.util.List;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import uk.co.platosys.util.DocMan;
import uk.co.platosys.util.Logger;

/**
 *Xmail is a wrapper for the Apache Commons Mail package, itself a wrapper for the javax.mail
 *system.
 *
 *Xmail is intended for producing system-generated emails, such as those used to confirm user 
 *registration with a webapp.
 * Objects of the Xmail class are email messages.
 * @author edward
 */
public class Xmail extends SimpleEmail {
Logger logger = XmailProperties.XMAIL_LOGGER;
    String message="";
/**
 * The noargs constructor generates a simple email message with the host set from the properties
 * file. You then have to call its setFrom, setTo, setMsg etc methods before calling send(). 
 *  
 */
public Xmail(){
        super();
        setHostName(XmailProperties.MAIL_HOST);
        logger.log("Xmail created");

    }
    
    /**
     * Feed an exception to Xmail in the constructor and it creates a special type of mail containing the
     * cause chain and the stack trace of the exception.
     * 
     * Useful for test and beta systems it can be used to  channel error mails to development teams. Obviously,
     * you mostly put this in a catch block. 
     * 
     * 
     * @param x
     */
    public Xmail(Throwable x){

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
    /**
     * Builds an email from a jdom2 document. The document must contain content in the Xmail namespace http://www.platosys.co.uk/xmail, it will ignore
     * elements that not in that namespace. 
     * 
     * 
     * 
     * <mail>
	     * <para>
		     * <head></head>
		     * <content></content>
	     * </para>
	     * <para>
		     * <head></head>
		     * <content></content>
	     * </para>
     * </mail>
     *  
     * @param document
     * @param format
     * @throws XmailException
     */
    public Xmail(Document document, boolean format) throws XmailException{
    	super();
    	 setHostName(XmailProperties.MAIL_HOST);
         
    	Namespace ns = Namespace.getNamespace("http://www.platosys.co.uk/xmail");
    	Element rootElement=document.getRootElement();
    	int ll = 64;
    	String msg="";
    	String MAIL_ELEMENT_NAME="mail";
    	String PARA_ELEMENT_NAME="para";
    	String HEAD_ELEMENT_NAME="head";
    	String CONTENT_ELEMENT_NAME="content";
    	String HEAD_DELIM="**";
    	Element mailElement=rootElement.getChild(MAIL_ELEMENT_NAME, ns);
    	if(mailElement==null){throw new XmailException("no mail element in document");}
    	List<Element> paras = mailElement.getChildren(PARA_ELEMENT_NAME, ns);
    	for(Element para:paras){
    		String text = breakLines((para.getChildText(HEAD_ELEMENT_NAME, ns)),ll);
    		msg=msg+HEAD_DELIM+text+HEAD_DELIM+"\n\n";
    		text=para.getChildText(CONTENT_ELEMENT_NAME, ns);
    		msg=msg+text+"\n\n\n";
    	}
    	try{
    		setMsg(msg);
    	}catch(Exception x){
    		throw new XmailException(x);
    	}
    }
    public static Xmail build(File file, boolean format) throws XmailException{
    	try{
    		Document document = DocMan.build(file);
    		return new Xmail(document, format);
    		
    	}catch(Exception exception){
    		throw new XmailException(exception);
    	}
    }
    private String breakLines(String original, int linelength){
    	StringBuilder buf = new StringBuilder();
    	char[] chars= original.toCharArray();
    	int count=0;
    	for (char cr:chars){
    		buf.append(cr);
    		count++;
    		if (count==linelength){
    			buf.append("\n");
    			count=0;
    		}
    	}
    	return new String(buf);
    }
    
    @Override
    public Email setMsg(String message) throws EmailException{
    	super.setMsg(message);
    	this.message=message;
    	return this;
    }
    public String getMsg(){
    	return message;
    }
    /**
     * Appends a String to the end of the content of this email's content.
     * @param message
     * @throws EmailException 
     */
    public void append(String message) throws EmailException{
    	String msg=getMsg();
    	msg=msg+message;
    	setMsg(msg);
    }
    /**
     * Prepends a String and a line break to the start of this email's content.
     * @param message
     * @throws EmailException 
     */
    public void prepend(String message) throws EmailException{
    	String msg=getMsg();
    	msg=message+"\n\n"+msg;
    	setMsg(msg);
    }
   /**
    * Use to replace placeholders such as name in text. Placeholders are delimited by {}. 
    * The string holder param is submitted without the delimiters.
    * @param holder
    * @param replacement
    * @throws EmailException
    */
    public void replaceHolders(String holder, String replacement) throws EmailException{
    	String msg=getMsg();
    	String xholder="{"+holder+"}";
    	String message = msg.replace(xholder, replacement);
    	setMsg(message);
    }
}
