<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
    Author     : Administrator
--%>
<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.models.*" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts"%>
<%@ page import="com.datastax.driver.core.Cluster"%>

<!DOCTYPE html>
<html>
	<body bgcolor="#FFFFCC">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
    </head>
    <body>
   		
        <h1><center>InstaGrim ! </center></h1>
        <h2><center>Your world in Coloured Filters</center></h2>      
        <nav>
            <ul>  
            	<a href="/Instagrim"><b>Home</b></a></br></br>
            	<%  LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");%>

				<% 
				 String user = lg.getUsername();
			     User us = new User();	 
			     Cluster cluster = null;           
			     cluster = CassandraHosts.getCluster();
			     us.setCluster(cluster);
			     				     
			     lg.setFirstName(us.getFirstName(user));
			     lg.setPicid(us.getPicid(user));				
				%>
				<IMG HEIGHT=50 WIDTH=50 SRC="/Instagrim/Image/<%=lg.getPicid()%>" >

				<h3><% out.println(lg.getUsername()); %></h3>
				
                <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
                <li class="nav"><a href="/Instagrim/Images/majed">Sample Images</a></li>
            </ul>
        </nav>
        
        
 
        <article>
            <h3>File Upload</h3>
            <form method="POST" enctype="multipart/form-data" action="Image">
                File to upload: <input type="file" name="upfile" accept="image"><br/>
                <br/>
                <input type="text" name="caption" value="" placeholder="picture name/caption"> <br/><br/>
                <input type="text" name="likes" value="0" hidden>
                
                Filter: <select type="text" name="filter">
  				<option value="bw">Black and white</option>
  				<option value="red">Red</option>
    			<option value="green">Green</option>
    			<option value="blue">Blue</option>
    			<option value="nofilter">No filter</option>
  				</select> <br><br>
				
			<input type="submit" value="Press"> to upload the file!
            </form>
        </article>
        
        
        
        </br></br></br>
         <footer>
            <ul>                
                <li>&COPY; Konstantin I.</li>
                <li>Dundee, 2014</li>
            </ul>
        </footer>
    </body>
</html>
