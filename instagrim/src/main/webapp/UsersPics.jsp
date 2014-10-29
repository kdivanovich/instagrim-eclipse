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
<%@ page import="java.util.LinkedList"%>

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
				<li class="nav"><a href="/Instagrim/DisplayAllImages">Show All Images</a></li>
                <li class="nav"><a href="/Instagrim/upload.jsp">Upload</a></li>                
                <li><a href="/Instagrim/Logout">Logout</a></li> </br>
            </ul>
        </nav>
        
 
        <article>
            <h1>Your Pics</h1>
        <%
            LinkedList<Pic> lsPics = (LinkedList<Pic>) request.getAttribute("Pics");
       		LinkedList<String> lsComments = new LinkedList<>();
       		int lsLikes = 0;
            if (lsPics == null) {
        %>
        <p>You don't have any pictures yet. Click the "Upload" button to get started!</p>
        <%
        } else {
        	
            for (int i = 0; i<lsPics.size(); i++ ){	// my code, replicates Andy's code above
           		Pic p = lsPics.get(i);
            	lsComments = picMod.getCommentsForPic(p.getSUUID());
            	lsLikes = picMod.getLikesForPic(p.getSUUID());
            	//boolean liked = false; 
            	
        %>        
        
        <a href="/Instagrim/Image/<%=p.getSUUID()%>" ><img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a></br>
        
        <i> <% if (p.getCaption().isEmpty()){		// don't show the NULL value if the user didn't provide a name/caption
			} else {
				out.println(p.getCaption());	
			}%> </i> </br>
                
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
        
                
    	 <% 
    	 //if (liked == false){    	 
    	 %>
			        
        <form method="POST" action="/Instagrim/Like">	
       		 <%  // show number of likes
    	 	 out.println( lsLikes + " likes.");%> 	
        		<input type="text" name="likes" value="<%=picMod.getLikesForPic(p.getSUUID())%>" hidden>			
				<input type="text" name="picid" value="<%=p.getSUUID() %>" hidden > 
        		<input type="text" name="login" value="<%=lg.getUsername() %>" hidden>  
				<input type="text" name="page" value="login" hidden >  			
        	<input type="submit"	value="Like" > <br><br>	
        </form>
        
        <% //	}  %>	     
       
        
        <form method="POST" action="/Instagrim/Comment">
        		<input type="text" name="comment" placeholder="your comment">
        		<input type="text" name="login" value="<%=lg.getUsername() %>" hidden>  
				<input type="text" name="picid" value="<%=p.getSUUID() %>" hidden > 							
				<input type="text" name="page" value="user" hidden >		
        	<input type="submit"	value="Comment"> 	<br><br>
        </form>
        
        				
		<form method="POST" action="/Instagrim/UpdateAvatar">
			<input type="text" name="username" value="<%=lg.getUsername() %>" hidden>  
			<input type="text" name="picid" value="<%=p.getSUUID() %>" hidden > 											
		<input type="submit"	value="Select avatar"> 	<br><br>
		</form>
        
        
		<form action="/Instagrim/Delete/<%=p.getSUUID() %>">
    		<input type="submit" value="Delete"> 
		</form> 
		<br><br>        
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
