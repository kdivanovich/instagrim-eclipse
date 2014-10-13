/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Register", urlPatterns = { "/Register" })
public class Register extends HttpServlet {
	Cluster cluster = null;

	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		cluster = CassandraHosts.getCluster();
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (registrationError(username, password) == false) {	//  if details are > 2 symbols each - OK

			User us = new User();
			us.setCluster(cluster);
			//us.RegisterUser(username, password);						
			//response.sendRedirect("/Instagrim");				
			
			boolean checkRegistration = us.RegisterUser(username, password);                                 
            
            if (checkRegistration == false){    // if the name is taken = fail        	
            	response.sendRedirect("errorUsernameTaken.jsp");            	
            }
            else{
                response.sendRedirect("/Instagrim");
            }
		} else {
			response.sendRedirect("errorUserPassLength.jsp");
		}
		
		
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

	
	// OBSOLETE UNLESS CHANGED ABOVE TO USE IT
	// username and password cannot be less than 2 symbols each
	public boolean registrationError(String user, String pass) {
		if (user.length() < 2 || pass.length() < 2) {
			return true;	// there is an error while registering 
		} else
			return false;	// registration details are fine
	}


	
}
