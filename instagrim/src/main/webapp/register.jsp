<%-- 
    Document   : register.jsp
    Created on : Sep 28, 2014, 6:29:51 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<body bgcolor="#FFFFCC">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
    </head>
    
    <body>
        <header>
        <h1><center>InstaGrim ! </center></h1>
        <h2><center>Your world in Coloured Filters</center></h2>      
        </header>
        
        <nav>
            <ul>
            	<li class="footer"><a href="/Instagrim">Home</a></li>
                <li><a href="login.jsp">Login</a></li>
            </ul>
        </nav>
       
        <article>
            <h3>Register as user</h3>
            <form method="POST"  action="Register">
                <ul>
                    Fields marked with a star (*) are compulsory. </br></br>
                    <li>User Name * <input type="text" name="username"></li>
                    <li>Password * <input type="password" name="password"></li>
                    <li>Repeat your password * <input type="password" name="repeatPassword"></li></br>
                    
                    <li>First name <input type="text" name="firstName"></li>
                    <li>Last name <input type="text" name="lastName"></li>
                    <li>Email <input type="text" name="email"></li>                    
                </ul>
                <br/>
                <input type="submit" value="Register"> 
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
