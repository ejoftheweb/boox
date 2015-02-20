/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.xservlets;

import java.util.Map;


import uk.co.platosys.util.Logger;

import java.io.*;

import java.util.List;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;


import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import uk.co.platosys.webplates.Webplate;

/**
 *  The Xservlet class is a much more useful base for servlets than HttpServlet,
 * when, as is frequently the case, the purpose of the servlet is to process 
 * form data.
 * 
 * Its main feature is its ability to collect parameters from requests and it provides
 * a number of methods, typically of the form get[Type]Parameter(request, name).
 * 
 * Application designers will usually also be designing the form used to submit the 
 * data so can keep an eye on the data submitted.
 * 
 * 
 * 
 * Xservlet has now been re-engineered to work in a GWT Ajax environment, and it 
 * extends RemoteServiceServlet. In GWT, there is less need to collect request parameters as 
 * servlets implement rpc methods.
 * 
 * However, it does give you access to a Session object, which RemoteServiceServlet doesn't.
 * This lets you access objects, such as - in particular - an authenticated Xuser object - which you 
 * keep in session scope. 
 * 
 * 
 * @author edward
 */
public abstract class Xservlet extends RemoteServiceServlet {
   /**
	 * 
	 */
	private static final long serialVersionUID = -4271664473998811268L;
Map<String,String[]> parameterMap;
   public static Logger logger = XservletProperties.XSERVLET_LOGGER;
   boolean done;
   private PrintWriter out;
   public ServletContext application;
   String applicationName;
   static String APPLICATION_NAME=XservletProperties.APPLICATION_NAME;
   
   //gives access to the same "application" variable as available in jsps
   public void initi(){
       application=getServletConfig().getServletContext();
   }
   public HttpSession getSession(){
	   return this.getThreadLocalRequest().getSession();
   }
  
   /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
   
    /**
     * Returns a String parameter from the request, typically a string field in a form. 
     * http treats string parameters containing whitespace as separate tokens, and this 
     * concatenates string parameters with spaces (such as a real name) into a single string.
     * @param request
     * @param parameterName
     * @return
     * @throws uk.co.platosys.xservlets.XservletException
     */
    public String getStringParameter(HttpServletRequest request, String parameterName)throws AbsentParameterException{
        parameterMap=request.getParameterMap();
        if(!parameterMap.containsKey(parameterName)){
            
            throw new AbsentParameterException("parameter "+parameterName+" not found in request");
        }
        String[] parameters = parameterMap.get(parameterName);
        return (buildItem(parameters));
    }
        /**
     *Returns multiple parameters having a single name as a list. 
         * The main use for this is for <select multiple> controls. Note that because
         * of http's whitespace handling, separate words are treated as separate tokens - the 
         * getStringParameter(request, name) method concatenates them. The form should
         * use the value attribute to the <option> elements safely to avoid multi-word 
         * values.
     * @param request
     * @param parameterName
     * @return
     * @throws uk.co.platosys.xservlets.XservletException
     */
    public List<String> getStringParameters(HttpServletRequest request, String parameterName)throws XservletException, AbsentParameterException{
        parameterMap=request.getParameterMap();
        if(!parameterMap.containsKey(parameterName)){
            throw new AbsentParameterException("parameter "+parameterName+" not found in request");
        }
        List<String> list = new Vector<String>();
        String[] parameters = parameterMap.get(parameterName);
        for (int i=0; i<parameters.length; i++){
            list.add(parameters[i]);
        }
        return list;
    }
    /**
     * Returns a boolean parameter, such as a checkbox.
     * @param request
     * @param parameterName
     * @return
     * @throws uk.co.platosys.xservlets.XservletException
     */
    public boolean getBooleanParameter(HttpServletRequest request, String parameterName)throws XservletException{
            parameterMap=request.getParameterMap();
       
        if(!parameterMap.containsKey(parameterName)){
            return false;
        }
        String[] parameters = parameterMap.get(parameterName);
        String parameter=(buildItem(parameters));
        if(parameter.equalsIgnoreCase("on")){return true;}
        else{return false;}
    
        
    }
    /**
     * This method is used to process a form where one or other (but not both) of two fields must be present and it determines
     * which of them has been completed. For example, an amount can be either including or excluding tax.
     * 
     * @param request
     * @param trueParameterName
     * @param falseParameterName
     * @return true if trueParameterName is present and not-null; false if falseParameterName is present and not-null; otherwise throws an XservletException
     * 
     * @throws uk.co.platosys.xservlets.XservletException
     */
    public boolean getBooleanAlternative(HttpServletRequest request, String trueParameterName, String falseParameterName) throws XservletException{
            parameterMap=request.getParameterMap();
       
        
        if((!parameterMap.containsKey(trueParameterName))&&(!parameterMap.containsKey(falseParameterName))){
            throw new XservletException("neither parameter "+trueParameterName+" nor parameter "+falseParameterName+" found in request");
        }
        String trueParameter=buildItem(parameterMap.get(trueParameterName));
        String falseParameter=buildItem(parameterMap.get(falseParameterName));
        if (trueParameter.equals("")){return false;}
        if (falseParameter.equals("")){return true;}
        throw new XservletException("both parameters in request");
        
       
        //not sure that this deals with all the possible permutations of parameter not present/parameter null
    }
  
     public double getDoubleParameter(HttpServletRequest request, String parameterName)throws AbsentParameterException{
        parameterMap=request.getParameterMap();
        
        if(!parameterMap.containsKey(parameterName)){
            throw new AbsentParameterException("parameter "+parameterName+" not found in request");
        }
        String[] parameters = parameterMap.get(parameterName);
        try{
            return Double.parseDouble(buildItem(parameters));
        }catch(NumberFormatException nfe){
            return 0;
        }
     }
        
        private String buildItem(String[] item){
            String built="";
            for (int i=0; i<item.length; i++){
                built=built+item[i];
                if (i<(item.length-1)){
                    built=built+" ";
                }
            }
            return built;
        } 
    public void badRequestResponse(HttpServletRequest request,HttpServletResponse response, String message)throws IOException{
    	logger.log(4, "method is "+ request.getMethod());
        
    	logger.log(4, "PUS: bad request response "+message);  
      logger.log(4, "method is "+ request.getMethod());
      //logger.log(4, request.getMethod());
      if(!done){
             out=response.getWriter();
            response.setContentType("application/xhtml+xml;charset=UTF-8");
            Webplate responseDocument=ServletErrorPages.badRequestError(request);
            responseDocument.output(out);
            out.close();
        }
        done=true;
    }
  
    public void errorResponse(HttpServletRequest request, HttpServletResponse response, Exception e)throws IOException{
        logger.log("PUS: error Response ", e);
        if(!done){
            out=response.getWriter();
            Webplate responseDocument=ServletErrorPages.serverError(request, e);  
            response.setContentType("application/xhtml+xml;charset=UTF-8");
            responseDocument.output(out);
            out.close();
            request.getSession().invalidate();
        }
        done=true;
    }
    public void problemResponse(HttpServletRequest request, HttpServletResponse response, List<String> problems)throws IOException{
        logger.log(2, "PUS: error Response ");
        if(!done){
            out=response.getWriter();
            Webplate responseDocument=ServletErrorPages.incompleteFormError(request, problems);
            response.setContentType("application/xhtml+xml;charset=UTF-8");
            responseDocument.output(out);
            out.close();
            request.getSession().invalidate();
        }
        done=true;
    }
        public void serveResponse(HttpServletResponse response, Webplate responseDocument){
           //serve up the response
        try{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        responseDocument.output(out);
        out.close();
        }catch(Exception e){}
        
    } 
        
    public void formRefused(HttpServletRequest request, HttpServletResponse response, String message)throws IOException{
      logger.log(4, "Xservlet: Form Refused response "+message);  
      if(!done){
            response.setContentType("application/xhtml+xml;charset=UTF-8");
            Webplate responseDocument=ServletErrorPages.formRefused(request, message);
            responseDocument.output(out);
            out.close();
        }
        done=true;
    }
    /**
     * Returns the user to the referring page, reloaded. - with the content updated as
     * a result of the Xservlet's actions. A session notice is posted.
     * @param request
     * @param response
     * @param message
     * @throws java.io.IOException
     */
    public void returnResponse(HttpServletRequest request, HttpServletResponse response, String message) throws IOException{
        addSessionNotice(request, message);
        response.sendRedirect(request.getHeader("referer"));
    }
    
    public void addApplicationNotice(String notice){
        if ((application.getAttribute("applicationNotices"))==null){
            if ((application.getAttribute("name"))==null){
                applicationName=APPLICATION_NAME;
            }else{
                applicationName=(String) application.getAttribute("name");
            }
            application.setAttribute("applicationNotices", new NoticeBoard(applicationName));
        }
        NoticeBoard applicationNotices = (NoticeBoard) application.getAttribute("applicationNotices");
        applicationNotices.addNotice(notice);
        
    }
    public void addSessionNotice(HttpServletRequest request, String notice){
        HttpSession session = request.getSession();
        String sessionName;
         if ((session.getAttribute("sessionNotices"))==null){
            if ((session.getAttribute("name"))==null){
                sessionName=session.getId();
            }else{
                sessionName=(String) session.getAttribute("name");
            }
            session.setAttribute("sessionNotices", new NoticeBoard(sessionName));
        }
        NoticeBoard sessionNotices = (NoticeBoard) session.getAttribute("sessionNotices");
        sessionNotices.addNotice(notice);
    }
    public void addSessionAdminNotice(HttpServletRequest request, String notice){
        HttpSession session = request.getSession();
        String sessionName;
         if ((session.getAttribute("sessionAdminNotices"))==null){
            if ((session.getAttribute("name"))==null){
                sessionName=session.getId();
            }else{
                sessionName=(String) session.getAttribute("name");
            }
            session.setAttribute("sessionAdminNotices", new NoticeBoard(sessionName));
        }
        NoticeBoard sessionAdminNotices = (NoticeBoard) session.getAttribute("sessionAdminNotices");
        sessionAdminNotices.addNotice(notice);
    }
    public String getRegID(HttpServletRequest request){
        HttpSession session=request.getSession();
        if (session.getAttribute("regID")==null){
            return null;
        }else{
            return (String) session.getAttribute("regID");
        }
    }
    /**
     * This method is used for debugging and writes the serialised HttpResponse to the logger.
     */
    @Override
	protected 	void onAfterResponseSerialized(String serializedResponse){
    	//logger.log("Xservlet: responseSerialised");
		//logger.log(serializedResponse);
	}
    /**
     * catches the SerializationException for forensic examination.
     */
    @Override
    public String processCall(String payload) throws SerializationException{
    	try{
    		return super.processCall(payload);
    	}catch(SerializationException se){
    		logger.log("Xservlet serialisation excption", se);
    		throw se;
    	}
    }
}   
