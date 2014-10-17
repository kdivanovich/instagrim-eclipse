<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
    Author     : Administrator
--%>

<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="Styles.css" />
    </head>
    <body>
   		
        <h1><center>InstaGrim ! </center></h1>
        <h2><center>Your world in Black and White</center></h2>        
        <nav>
        <a href="/Instagrim"><b>Home</b></a></br></br>
            <ul>  
            	<%  LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");%>
           		<IMG HEIGHT=50 WIDTH=50 SRC="/Instagrim/Image/554b7710-525e-11e4-a3b7-6894234415aa"></A>
				<h3><% out.println(lg.getUsername()); %></h3>
				
                <li class="nav"><a href="upload.jsp">Upload</a></li>
                <li class="nav"><a href="/Instagrim/Images/majed">Sample Images</a></li>
            </ul>
        </nav>
 
        <article>
            <h3>File Upload</h3>
            <form method="POST" enctype="multipart/form-data" action="Image">
                File to upload: <input type="file" name="upfile"><br/>

                <br/>
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
