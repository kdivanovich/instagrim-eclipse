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
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "UpdateAvatar", urlPatterns = { "/UpdateAvatar" })
public class UpdateAvatar extends HttpServlet {
	Cluster cluster = null;

	public void init(ServletConfig config) throws ServletException {
		cluster = CassandraHosts.getCluster();
	}

	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String picid = request.getParameter("picid");
		
		User us = new User();		
		us.setCluster(cluster);
		
		us.UpdateAvatar(username,picid );
		response.sendRedirect("/Instagrim/Images/" + username);		
	}


	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

	

}
