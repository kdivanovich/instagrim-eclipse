<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.models.*" %>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts"%>
<%@ page import="com.datastax.driver.core.Cluster"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Your Profile</title>
</head>
<body>
	<body bgcolor="#FFFFCC">
	 <header>
	 	<h1><center>InstaGrim ! </center></h1>
        <h2><center>Your world in Coloured Filters</center></h2>                 
     </header>
     
     <a href="/Instagrim"><b>Home</b></a></br></br>            	
     
     <form method="POST" action="/Instagrim/Search">
                	<input type="text" name="searchText" placeholder="search by name/caption" >	
					<input type="submit"	value="Search"> 
				</form><br>
     
     <%  LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
     
     // Update the page right away:
     String user = lg.getUsername();
     User us = new User();	 
     Cluster cluster = null;           
     cluster = CassandraHosts.getCluster();
     us.setCluster(cluster);
     
     PicModel picMod = new PicModel();
   	 picMod.setCluster(cluster);
     
     lg.setFirstName(us.getFirstName(user));
     lg.setPicid(us.getPicid(user)); 
     String searchedFor = session.getAttribute("searchedFor").toString(); %> 
     
     <b>Search for: 
     <%
     out.println(session.getAttribute("searchedFor")); //print the passed searched text from UsersPics.jsp
     LinkedList<String> lsTags = new LinkedList<>();     
     lsTags = picMod.getSearchTags(searchedFor);
     
     
     String userAndUUID = lsTags.get(0);
	 String[] parts = userAndUUID.split(": ");
	 
	 
     %></b></b> <br>
     <a href="/Instagrim/Image/<%=parts[1]%>" ><img src="/Instagrim/Thumb/<%=parts[1]%>"></a></br><br>
     Original Poster: <a href="/Instagrim/Images/<%=parts[0]%>" >  <%out.println(parts[0]); %> </a><br>
		
<% session.removeAttribute("searchedFor"); %>
</body>
</html>