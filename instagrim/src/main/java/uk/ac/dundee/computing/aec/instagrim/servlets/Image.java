package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import java.util.logging.*;	// needs utils for the pic deletion 

import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/Image",
    "/Image/*",
    "/Thumb/*",
    "/Images",
    "/Images/*",
    "/Delete",
    "/Delete/",
    "/Delete/*",
    "/DisplayAllImages",
    "/DisplayAllImages/",
    "/Filter/",
    "/Filter/*",
})
@MultipartConfig

public class Image extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();
    
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
        // TODO Auto-generated constructor stub
        CommandsMap.put("Image",   1);
        CommandsMap.put("Images",  2);
        CommandsMap.put("Thumb",   3);
        CommandsMap.put("Delete",  4);
        CommandsMap.put("DisplayAllImages",  5);   
        CommandsMap.put("Filter", 6);

    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String args[] = Convertors.SplitRequestPath(request);
        int command;
        try {
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", response);	// added a request
            return;
        }
        switch (command) {
            case 1:
                DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], response);
                break;
            case 2:
                DisplayImageList(args[2], request, response);
                break;
            case 3:
                DisplayImage(Convertors.DISPLAY_THUMB,args[2],  response);
                break;
            case 4: 
            	DeleteImage(args[2], request, response);
            	break;
            case 5:
            	DisplayAllImages(request, response);
            	break;    
            case 6:
            	System.out.println("Applying filter.. to " + args[2]);
            	DisplayImage(Convertors.MAKE_RED, args[2], response);
            	break;
            default:
                error("Bad Operator", response);
        }
    }   
    
    	// the method that uses the one in PicModel to showw all pics in a servlet
    private void DisplayAllImages( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PicModel tm = new PicModel();
    	tm.setCluster(cluster);
    	java.util.LinkedList<Pic> lsPics = tm.getAllPics();
    	RequestDispatcher rd = request.getRequestDispatcher("/ShowAllPics.jsp");
    	request.setAttribute("allPics", lsPics);
    	rd.forward(request,  response);    	
    }

    
    private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User);
        RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp");
        request.setAttribute("Pics", lsPics);
        rd.forward(request, response);
    }
    

    private void DisplayImage(int type,String Image, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);  
        
        Pic p = tm.getPic(type,java.util.UUID.fromString(Image));
        
        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());
        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for (Part part : request.getParts()) {
            System.out.println("Part Name " + part.getName());

            String caption = request.getParameter("caption");	// retrieve the name
            if (caption.isEmpty()) {
            	caption = "(no name)";
            }
            String type = part.getContentType();
            String filename = part.getSubmittedFileName();   
            String filter = request.getParameter("filter");
            int likes = 0;
            
            InputStream is = request.getPart(part.getName()).getInputStream();
            int i = is.available();
            HttpSession session=request.getSession();
            LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
            String username="majed";
            if (lg.getlogedin()){
                username=lg.getUsername();
            }
            if (i > 0) {
                byte[] b = new byte[i + 1];
                is.read(b);
                System.out.println("Length : " + b.length);
                PicModel tm = new PicModel();
                tm.setCluster(cluster);
                tm.insertPic(b, type, filename, username, caption, likes, filter);

                is.close();
                
                /*		-- experimental code to check if a new image's name is the same as one that's in the dB
                 boolean picNameAvailable = true;
                byte[] b = new byte[i + 1];
                is.read(b);
                System.out.println("Length : " + b.length);
                PicModel tm = new PicModel();
                
				LinkedList<String> picNames = new LinkedList<>();
                	picNames = tm.getPicNames(username);                 
                		for (int j=0; j<picNames.size(); j++) {
                			if (caption.equals(picNames.get(j))){
                				picNameAvailable = false;
                	} 
                }
                
				if (picNameAvailable == true) {
					tm.setCluster(cluster);
					tm.insertPic(b, type, filename, username, caption, likes,
							filter);

					is.close();
				}
 
                 */
            }
            RequestDispatcher rd = request.getRequestDispatcher("/upload.jsp");
             rd.forward(request, response);
        }
    }
    
    
    private void DeleteImage(String picID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PicModel tm = new PicModel();
        tm.setCluster(cluster);
                
        HttpSession session = request.getSession();
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        
        String currentUser = lg.getUsername();
        String image_deleted = tm.deletePic(currentUser, UUID.fromString(picID));
        
        if (!image_deleted.equals("image deleted")) {
        	error("No such picture exists. " + image_deleted, response);
        	return;
        } 
        else {
        	response.sendRedirect("/Instagrim/Images/" + currentUser);        	
        }
    }
    
    

    private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have an a error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
}
