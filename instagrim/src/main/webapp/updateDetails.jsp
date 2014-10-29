<%-- 
    Document   : login.jsp
    Created on : Sep 28, 2014, 12:04:14 PM
    Author     : Administrator
--%>
<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Instagrim</title>
<link rel="stylesheet" type="text/css" href="Styles.css" />

</head>
<body>
	<body bgcolor="#FFFFCC">
	<header>
		<h1>
			<center>InstaGrim !</center>
		</h1>
			<h2><center>Your world in Coloured Filters</center></h2>
	</header>
	<nav>
		<a href="/Instagrim"><b>Home</b></a></br> </br>
	</nav>
	<%
		LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
	%>
	

	<form method="POST" action="UpdateInfo">
		Username: <%=lg.getUsername() %>  </br>
		<input type="text" name="username" value=<%=lg.getUsername() %> hidden>  </br>
		First name: <input type="text" name="firstName" value=<%=lg.getFirstName() %>>  </br>
		Last name: <input type="text" name="lastName" value=<%=lg.getLastName() %> >  </br>
		Email: <input type="text" name="email" value=<%=lg.getEmail() %> > 		</br>													
		<input type="submit"	value="Submit change"> 	</br>
	</form>
	
	
	</br></br></br></br></br>	


</body>
</html>
