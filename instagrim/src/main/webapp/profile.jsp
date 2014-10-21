<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Your Profile</title>
</head>
<body>

	 <header>
	 	<h1><center>InstaGrim ! </center></h1>
        <h2><center>Your world in Black and White</center></h2>                 
     </header>
     
     <a href="/Instagrim"><b>Home</b></a></br></br>
     
     <%  LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");%>
     Username:   <% out.print(lg.getUsername()); %> </br>
     First name: <% out.print(lg.getFirstName()); %> </br>
     Last name:  <% out.print(lg.getLastName()); %>	</br>     
     Email:      <% out.print(lg.getEmail()); %> </br>     

</body>
</html>