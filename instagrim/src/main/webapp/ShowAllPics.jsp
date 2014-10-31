<%-- 
    Document   : UsersPics
    Created on : Sep 24, 2014, 2:52:48 PM
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
        <link rel="stylesheet" type="text/css" href="/Instagrim/Styles.css" />
    </head>
    <body>
        <header>
        <%  LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");%>
        <h1><center>InstaGrim ! </center></h1>
        <h2><center>Your world in Coloured Filters</center></h2>   
        </header>
        
        
        <nav>
            <ul>
            	<a href="/Instagrim"><b>Home</b></a></br></br>
				
				<% 
				 String user = lg.getUsername();
			     User us = new User();	 
			     Cluster cluster = null;           
			     cluster = CassandraHosts.getCluster();
			     us.setCluster(cluster);			     

			     PicModel picMod = new PicModel();
			   	 picMod.setCluster(cluster);
			     				     
			     lg.setFirstName(us.getFirstName(user));
			     lg.setPicid(us.getPicid(user));				
				%>
				<IMG HEIGHT=50 WIDTH=50 SRC="/Instagrim/Image/<%=lg.getPicid()%>" >
				

				<h3><% out.println(lg.getUsername()); %></h3>				
                <li><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
                <li class="nav"><a href="/Instagrim/upload.jsp">Upload</a></li>
                <li><a href="/Instagrim/Logout">Logout</a></li> </br>
            </ul>
        </nav>
 
        <article>
            <h1>The pics of all Instagrim users</h1>
            
            <form method="POST" action="/Instagrim/Search">
                	<input type="text" name="searchText" placeholder="search by name/caption" >	
					<input type="submit"	value="Search"> 
				</form><br><br>
        <%
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("allPics");
   			LinkedList<String> lsComments = new LinkedList<>();
   			int lsLikes = 0;
            if (lsPics == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
        	
            for (int i =0; i<lsPics.size(); i++ ){	// my code, replicates Andy's code above
             	Pic p = lsPics.get(i);
             	lsComments = picMod.getCommentsForPic(p.getSUUID());
             	lsLikes = picMod.getLikesForPic(p.getSUUID());
             	
        %>        
        
        <a href="/Instagrim/Image/<%=p.getSUUID()%>" ><img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a><br>       
		
		<i> <% out.println(p.getCaption());	%> </i> </br>		 
        Original Poster: <a href="/Instagrim/Images/<%=p.getUser()%>" >  <%out.println(p.getUser()); %> </a><br>
		       
        <%

     // loop through the comments to display them one under another
		if (lsComments != null) {		
			for (int j=0; j<lsComments.size(); j++) { %>        
	 			<a> <% //out.println(lsComments.get(j));	// print user and comment as 1 string       	 			
	 			String userAndComment = lsComments.get(j);
	 			String[] parts = userAndComment.split(":");
	 			%>       	 			
	 			<a href="/Instagrim/Images/<%=parts[0]%>"><%=parts[0] %>:</a>       	 		
	 			<% 
	 			out.println(parts[1]);       	 			
	 			%>   </br>	<% 
			}
		}  %> <br>
        
        
        
        <form method="POST" action="/Instagrim/Like">
    	 	 <% out.println( lsLikes + " likes.");%> 
        		<input type="text" name="likes" value=<%=picMod.getLikesForPic(p.getSUUID())%> hidden>
        		<input type="text" name="login" value="<%=lg.getUsername() %>" hidden>  
				<input type="text" name="picid" value="<%=p.getSUUID() %>" hidden > 
				<input type="text" name="page" value="DisplayAllImages" hidden >  			
        	<input type="submit"	value="Like"> <br><br>	
        </form>
        
                
        <form method="POST" action="/Instagrim/Comment">
        		<input type="text" name="comment" placeholder="your comment">
        		<input type="text" name="login" value="<%=lg.getUsername() %>" hidden>  
				<input type="text" name="picid" value="<%=p.getSUUID() %>" hidden > 				
				<input type="text" name="page" value="DisplayAllImages" hidden > 					
        	<input type="submit"	value="Comment"> <br><br>	
        </form>         
						
		<form method="POST" action="/Instagrim/UpdateAvatar">
			<input type="text" name="username" value="<%=lg.getUsername() %>" hidden>  
			<input type="text" name="picid" value="<%=p.getSUUID() %>" hidden >   	
		</form>
		<br>
        
        <%
            }
            }
        %>
        </article>
         <footer>
            <ul>                
                <li>&COPY; Konstantin I.</li>
                <li>Dundee, 2014</li>
            </ul>
        </footer>
    </body>
</html>
