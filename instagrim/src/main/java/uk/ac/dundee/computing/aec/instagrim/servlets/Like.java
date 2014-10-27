/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "Like", urlPatterns = { "/Like" })
public class Like extends HttpServlet {
	Cluster cluster = null;

	public void init(ServletConfig config) throws ServletException {
		cluster = CassandraHosts.getCluster();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PicModel pm = new PicModel();
		pm.setCluster(cluster);
		
		String login = request.getParameter("login");
		String picid = request.getParameter("picid");
		String likes = request.getParameter("likes");
		String currentPage = request.getParameter("page");
						
        pm.writeLikes(login, picid, likes);
        
        if (currentPage.equals("DisplayAllImages")) {
			response.sendRedirect("/Instagrim/" + currentPage);
		}
		else 
			response.sendRedirect("/Instagrim/Images/" + login);
	}

	
	@Override
	public String getServletInfo() {
		return "Short description";
	}

}