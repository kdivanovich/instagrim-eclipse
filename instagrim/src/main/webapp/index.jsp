<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Administrator
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.models.*" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts"%>
<%@ page import="com.datastax.driver.core.Cluster"%>
<%@page import="java.util.*"%>
<%@ page import="com.datastax.driver.core.Cluster"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <header>
            <h1><center>InstaGrim ! </center></h1>
        	<h2><center>Your world in Black and White</center></h2>        
        </header>
        <nav>
            <ul>


					<%                        
                        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                        if (lg != null) {
                            String UserName = lg.getUsername();
                            if (lg.getlogedin()) {
                    %>
                    
                <a href="/Instagrim"><b>Home</b></a></br></br>
                              
				<IMG HEIGHT=50 WIDTH=50 SRC="/Instagrim/Image/<%=lg.getPicid()%>" >
				<h3><% out.println(lg.getUsername()); %></h3>
				
				<% String firstName = lg.getFirstName(); 	// Have a "Hello" message if the user provided their name
				%>
				
				
				 
				<% 
				 String user = lg.getUsername();
			     User us = new User();	 
			     Cluster cluster = null;           
			     cluster = CassandraHosts.getCluster();
			     us.setCluster(cluster);
			     				     
			     lg.setFirstName(us.getFirstName(user));
			     lg.setPicid(us.getPicid(user));
				if (firstName.length() > 0 ) { 
				
					 
				
				%>
				
				<div align="center"> Hello, <% out.println(lg.getFirstName()); %></div>
				
				<% } %>
               	               	
                <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
                <li><a href="upload.jsp">Upload</a></li>
                <li><a href="/Instagrim/profile.jsp">Profile</a></li>
                <li><a href="/Instagrim/Logout">Logout</a></li> </br>                
                    <%}
                            } else {
                                %>
                 <li><a href="register.jsp">Register</a></li>
                <li><a href="login.jsp">Login</a></li>
                <%
                                        
                            
                    }%>
            </ul>
        </nav>
         <footer>
            <ul>                
                <li>&COPY; Konstantin I.</li>
                <li>Dundee, 2014</li>
            </ul>
        </footer>
    </body>
</html>
