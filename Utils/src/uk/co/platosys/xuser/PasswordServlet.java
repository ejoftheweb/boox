/**
 * This servlet handles password management for the Xuser subsystem. It isn't written properly yet.
 * 
 * Process: Forgotten password > sends email with time-limited link > click on link in email> 
 * form to reset password > enter new password > confirmation email sent with link to login page.
 * 
 * 
 */

package uk.co.platosys.xuser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.co.platosys.util.Logger;
import uk.co.platosys.webplates.Webplate;
import uk.co.platosys.xservlets.AbsentParameterException;
import uk.co.platosys.xservlets.Xservlet;
import uk.co.platosys.xservlets.XservletProperties;


public class PasswordServlet extends Xservlet {
	
		   Map<String,String[]> parameterMap;
		   public Logger logger = XservletProperties.XSERVLET_LOGGER;
		   boolean done;
		   private PrintWriter out;
		   public ServletContext application;
		   String applicationName;
		   static String APPLICATION_NAME=XservletProperties.APPLICATION_NAME;
		   
		   //gives access to the same "application" variable as available in jsps
		   public void initi(){
		       application=getServletConfig().getServletContext();
		   }
		  
		   /** 
		    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
		    * @param request servlet request
		    * @param response servlet response
		    */
		   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
			  try {
				   String email = getStringParameter(request, "email");
				   String newPass= getStringParameter(request, "newpass");

				   out=response.getWriter();
		            Webplate responseDocument=XuserResponsePages.success(request);
		            response.setContentType("application/xhtml+xml;charset=UTF-8");
		            responseDocument.output(out);
		            out.close();
		            request.getSession().invalidate();
			   }catch (AbsentParameterException e) {
				 out=response.getWriter();
		            Webplate responseDocument=XuserResponsePages.fail(request, "Sorry");
		            response.setContentType("application/xhtml+xml;charset=UTF-8");
		            responseDocument.output(out);
		            out.close();
		            request.getSession().invalidate();
			}
		   }
		   
		   
		
}
