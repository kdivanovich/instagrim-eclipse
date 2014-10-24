<%-- 
    Document   : login.jsp
    Created on : Sep 28, 2014, 12:04:14 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />

    </head>
    <body>
        <header>
        <h1><center>InstaGrim ! </center></h1>
        <h2><center>Your world in Black and White</center></h2>        
        </header>
        <nav>
            <ul>
            	<li class="footer"><a href="/Instagrim">Home</a></li><br>
                <li><a href="register.jsp">Register</a></li>
                <li><a href="/Instagrim/Images/majed">Sample Images</a></li>
            </ul>
        
       
        <article>
            <h3>Login</h3>
            <form method="POST"  action="Login">
                <ul>
                    <li>User Name <input type="text" name="username"></li>
                    <li>Password <input type="password" name="password"></li>
                </ul>
                <br/>
                <input type="submit" value="Login"> 
            </form>

        </article>        
        </nav>
        
        <footer>
             <ul>                
                <li>&COPY; Konstantin I.</li>
                <li>Dundee, 2014</li>
            </ul>
        </footer>
    </body>
</html>
