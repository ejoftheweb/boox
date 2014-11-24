package uk.co.platosys.xuser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.co.platosys.util.Logger;
import uk.co.platosys.webplates.Webplate;
import uk.co.platosys.xservlets.AbsentParameterException;
import uk.co.platosys.xservlets.NoticeBoard;
import uk.co.platosys.xservlets.ServletErrorPages;
import uk.co.platosys.xservlets.Xservlet;
import uk.co.platosys.xservlets.XservletException;
import uk.co.platosys.xservlets.XservletProperties;


/**
 * The ConfirmationServlet class is a servlet to confirm an email address.
 *  In the standard sign-up protocol, a user provides an email address, to which an email is sent containing
 *  a key. This is in the form of a link in the email, which the user is expected to click.
 *  This generates an HTTP request, which this servlet picks up. 
 *  
 *  
 *  
 * 
 * 
 */
public class ConfirmationServlet extends Xservlet {
	
		   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		Map<String,String[]> parameterMap;
		   public Logger logger = XservletProperties.XSERVLET_LOGGER;
		   boolean done;
		   private PrintWriter out;
		   public ServletContext application;
		   String applicationName;
		   static String APPLICATION_NAME=XservletProperties.APPLICATION_NAME;
		   
		   /** 
		    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
		    * @param request servlet request
		    * @param response servlet response
		    */
		   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
			  try {
				   String email = getStringParameter(request, "email");
				   String regKey = getStringParameter(request, "regkey");
				   logger.log(3, "confirmation servlet: email="+email+", regkey="+regKey);
				   Xuser.confirmXuser(email, regKey);
				   out=response.getWriter();
		            Webplate responseDocument=XuserResponsePages.success(request);
		            response.setContentType("application/xhtml+xml;charset=UTF-8");
		            responseDocument.output(out);
		            out.close();
		            request.getSession().invalidate();
			   }catch(XuserException ape){
				   out=response.getWriter();
		            Webplate responseDocument=XuserResponsePages.fail(request, "Sorry");
		            response.setContentType("application/xhtml+xml;charset=UTF-8");
		            responseDocument.output(out);
		            out.close();
		            request.getSession().invalidate();
			  
			} catch (AbsentParameterException e) {
				 out=response.getWriter();
		            Webplate responseDocument=XuserResponsePages.fail(request, "Sorry");
		            response.setContentType("application/xhtml+xml;charset=UTF-8");
		            responseDocument.output(out);
		            out.close();
		            request.getSession().invalidate();
			}
		   }
	
		   
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
		   
		  
		    public void badRequestResponse(HttpServletRequest request,HttpServletResponse response, String message)throws IOException{
		      logger.log(4, "PUS: bad request response "+message);  
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
		   

}
