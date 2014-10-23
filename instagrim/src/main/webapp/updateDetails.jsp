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
	<header>
		<h1>
			<center>InstaGrim !</center>
		</h1>
		<h2>
			<center>Your world in Black and White</center>
		</h2>
	</header>
	<nav>
		<a href="/Instagrim"><b>Home</b></a></br> </br>
	</nav>
	<%
		LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
	%>
	

	<form method="POST" action="UpdateInfo">
		First name: <input type="text" name="firstName" value=<%=lg.getFirstName()%> 
																placeholder="First Name">
		<input type="submit"	value="Submit change">
	</form>
	
	


</body>
</html>
