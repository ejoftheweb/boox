/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.platosys.xservlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import uk.co.platosys.xservlets.Xservlet;

/**
 * This servlet clears a session attribute and redirects to the calling page.
 * @author edward
 */
public class ClearSessionObjectServlet extends Xservlet {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = -1979723803631795936L;



	/** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try{
        String objectID = getStringParameter(request, "objectID");
        request.getSession().setAttribute(objectID, null);
        response.sendRedirect(request.getHeader("referer"));
        }catch(Exception e){}
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
    * Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 



    /** 
    * Returns a short description of the servlet.
    */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
