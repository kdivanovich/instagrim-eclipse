package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;

import java.awt.Color;			// libs needed fo the filters
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.io.ByteArrayInputStream;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.Bytes;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

import javax.imageio.ImageIO;

import static org.imgscalr.Scalr.*;

import org.imgscalr.Scalr.Method;

import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;




//import uk.ac.dundee.computing.aec.stores.TweetStore;
import java.util.UUID; // needed for the pic delete func
import java.util.LinkedList;

public class PicModel {

	Cluster cluster;

	public void PicModel() {

	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public void insertPic(byte[] b, String type, String name, String user, String caption, int likes, String filter) {
		try {
			Convertors convertor = new Convertors();
			
			likes = 0;
			String types[] = Convertors.SplitFiletype(type);
			ByteBuffer buffer = ByteBuffer.wrap(b);
			int length = b.length;
			java.util.UUID picid = convertor.getTimeUUID();

			// The following is a quick and dirty way of doing this, will fill
			// the disk quickly !
			Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
			FileOutputStream output = new FileOutputStream(new File(
					"/var/tmp/instagrim/" + picid));

			output.write(b);
			byte[] thumbb = picresize(picid.toString(), types[1], filter);	// create thumbnail
			int thumblength = thumbb.length;
			ByteBuffer thumbbuf = ByteBuffer.wrap(thumbb);
			byte[] processedb = picdecolour(picid.toString(), types[1], filter);	// apply filter
			ByteBuffer processedbuf = ByteBuffer.wrap(processedb);
			int processedlength = processedb.length;
			Session session = cluster.connect("instagrim");
			
				// now inserting a caption/name as well
			PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name) values(?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added, caption) values(?,?,?,?)");
				//insert 0 likes
			PreparedStatement psInsertLikes = session.prepare("insert into likes (likes,login,picid) values(?,?,?)");
			
			BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
			BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);
			BoundStatement bsInsertLikes = new BoundStatement(psInsertLikes);

			Date DateAdded = new Date();
			session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,
							processedbuf, user, DateAdded, length, thumblength,processedlength, type, name));
			session.execute(bsInsertPicToUser.bind(picid, user, DateAdded, caption));
			session.execute(bsInsertLikes.bind(likes, user, picid.toString()));
			session.close();

		} catch (IOException ex) {
			System.out.println("Error --> " + ex);
		}
	}

	// deleting a picture funtionality:
	public String deletePic(String user, UUID picID) {
		try {
			Session session = cluster.connect("instagrim");

			PreparedStatement psTimeStored = session.prepare("SELECT interaction_time, user FROM pics WHERE picid = ?");
			PreparedStatement psPicDelete = session.prepare("DELETE FROM pics WHERE picid = ?");
			PreparedStatement psPicDeleteFromUserPicList = session.prepare("DELETE FROM userpiclist WHERE user = ? AND pic_added = ?");

			BoundStatement bsTimeStored = new BoundStatement(psTimeStored);
			BoundStatement bsPicDelete = new BoundStatement(psPicDelete);
			BoundStatement bsPicDeleteFromUserPicList = new BoundStatement(psPicDeleteFromUserPicList);

			ResultSet rs = session.execute(bsTimeStored.bind(picID));
			Date dateAdded = new Date();
			String owner = "";
			
			if (rs.isExhausted()) {
				session.close();
				return "no rows";
			} else {
				for (Row row : rs) {
					dateAdded = row.getDate("interaction_time");
					owner = row.getString("user");
				}
			}
			if (owner.equals(user)) {
				session.execute(bsPicDelete.bind(picID));
				session.execute(bsPicDeleteFromUserPicList.bind(user, dateAdded));
				session.close();
				return "image deleted";
			}
			session.close();
			return "Error: the owner is not the current user. \nYou cannot edit other people's images.";
		} 
		catch (Exception exception) {
			return exception.getMessage();
		}
	}

	public byte[] picresize(String picid, String type, String filter) {
		try {
			BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/"
					+ picid));
			BufferedImage thumbnail = createThumbnail(BI, filter);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(thumbnail, type, baos);
			baos.flush();

			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (IOException et) {

		}
		return null;
	}

	public byte[] picdecolour(String picid, String type, String filter) {
		try {
			BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/"+ picid));
			BufferedImage processed = createProcessed(BI, filter);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(processed, type, baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (IOException et) {

		}
		return null;
	}

	public static BufferedImage createThumbnail(BufferedImage img, String filter) {
		
		if (filter.equals("green")) {
			int Width = img.getWidth();
			int Height = img.getHeight();

			for (int x = 0; x < Width; x++) {
				for (int y = 0; y < Height; y++) {
					int p = img.getRGB(x, y);

					int a = (p >> 24) & 0xff;
					int g = (p >> 8) & 0xff;

					// set new RGB
					p = (a << 24) | (0 << 16) | (g << 8) | 0;
					img.setRGB(x, y, p);
				}
			}
			img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);
		} 
		else if (filter.equals("red")) {
			int Width = img.getWidth();
			int Height = img.getHeight();

			for (int x = 0; x < Width; x++) {
				for (int y = 0; y < Height; y++) {
					int p = img.getRGB(x, y);

					int a = (p >> 24) & 0xff;
					int r = (p >> 16) & 0xff;

					// set new RGB
					p = (a << 24) | (r << 16) | (0 << 8) | 0;
					img.setRGB(x, y, p);
				}
			}
			img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);
		}
		else if (filter.equals("blue")) {
			int Width = img.getWidth();
			int Height = img.getHeight();

			for (int x = 0; x < Width; x++) {
				for (int y = 0; y < Height; y++) {
					int p = img.getRGB(x, y);

					int a = (p >> 24) & 0xff;
					int b = (p >> 0) & 0xff;

					// set new RGB
					p = (a << 24) | (0 << 16) | (0 << 8) | (b << 0);
					img.setRGB(x, y, p);
				}
			}
			img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);
		}
		else if (filter.equals("bw")){
			img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
		}
		else if (filter.equals("nofilter")){
			img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);
		}
		
		return pad(img, 2);
	}	
	

	public static BufferedImage createProcessed(BufferedImage img, String filter) { 
		if (filter.equals("green")) {
			int Width = img.getWidth();
			int Height = img.getHeight();

			for (int x = 0; x < Width; x++) {
				for (int y = 0; y < Height; y++) {
					int p = img.getRGB(x, y);

					int a = (p >> 24) & 0xff;
					int g = (p >> 8) & 0xff;

					// set new RGB
					p = (a << 24) | (0 << 16) | (g << 8) | 0;
					img.setRGB(x, y, p);
				}
			}
			img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);
		} 
		else if (filter.equals("red")) {
			int Width = img.getWidth();
			int Height = img.getHeight();

			for (int x = 0; x < Width; x++) {
				for (int y = 0; y < Height; y++) {
					int p = img.getRGB(x, y);

					int a = (p >> 24) & 0xff;
					int r = (p >> 16) & 0xff;

					// set new RGB
					p = (a << 24) | (r << 16) | (0 << 8) | 0;
					img.setRGB(x, y, p);
				}
			}
			img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);
		}
		else if (filter.equals("blue")) {
			int Width = img.getWidth();
			int Height = img.getHeight();

			for (int x = 0; x < Width; x++) {
				for (int y = 0; y < Height; y++) {
					int p = img.getRGB(x, y);

					int a = (p >> 24) & 0xff;
					int b = (p >> 0) & 0xff;

					// set new RGB
					p = (a << 24) | (0 << 16) | (0 << 8) | (b << 0);
					img.setRGB(x, y, p);
				}
			}
			img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);
		}
		else if (filter.equals("bw")){
			img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
		}
		else if (filter.equals("nofilter")){
			img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);
		}
		
		return pad(img, 4);
	}
	

	//========================================================================================================================
			// write likes to the table
		
		public void writeLikes(String login, String picid, int likes) {
			Session session = cluster.connect("instagrim");
					    	 
		    PreparedStatement ps = session.prepare("insert into likes (login,picid,likes) values(?,?,?)");
		    BoundStatement bs = new BoundStatement(ps);
		    session.execute(bs.bind(login,picid,likes));
		}	
	
	//========================================================================================================================
			// method to display the likes

	public int getLikesForPic(String picid) {
		 	int likes = 0;
	        Session session = cluster.connect("instagrim");
	        PreparedStatement ps = session.prepare("select login,likes from likes where picid=?  ALLOW FILTERING");
	        BoundStatement boundStatement = new BoundStatement(ps);
	        ResultSet rs = null;
	        rs = session.execute(boundStatement.bind(picid));
	        
	        if (rs.isExhausted()) {
	            System.out.println("No Likes Yet.");
	            return 0;
	        } else {
	            for (Row row : rs) {	                
	                likes = row.getInt("likes");
	            }
	        }
	        
	        return likes;
	    }
	
	//========================================================================================================================
		// the following method is slightly re-worked for the majed user = all pics
	
	public LinkedList<Pic> getPicsForUser(String User) {
		LinkedList<Pic> Pics = new LinkedList<>();
		Session session = cluster.connect("instagrim");
		// PreparedStatement ps = session.prepare("select picid from userpiclist where user =?");	// removing to specify if all pics(user Majed) are specified
		PreparedStatement psAllPics;
		
			// filter if the user is the dummy one
		if ("majed".equals(User)){
			psAllPics = session.prepare("select picid,caption,user from userpiclist");			
		}
		else {
			psAllPics = session.prepare("select picid,caption,user from userpiclist where user =?");	
		}
		
		ResultSet rs = null;
		BoundStatement boundStatement = new BoundStatement(psAllPics);
		
		if ("majed".equals(User)) {	// if that's the user w/ all the pics
			rs = session.execute(boundStatement.bind(User)); 
		}
		else {
			rs = session.execute(boundStatement.bind(User));	// else fetch the actual User
		}
		
		if (rs.isExhausted()) {
			System.out.println("No images returned");
			return null;
		} else {
			for (Row row : rs) {
				Pic pic = new Pic();
				java.util.UUID UUID = row.getUUID("picid");
				System.out.println("UUID" + UUID.toString());
				pic.setCaption(row.getString("caption"));
				pic.setUser(row.getString("user"));
				pic.setUUID(UUID);	
				Pics.add(pic);
			}
		}
		return Pics;
	}
		
	//========================================================================================================================
		// write comments to the table
	
	public void writeComment(String login, String picid, String comment) {
		Session session = cluster.connect("instagrim");
		
		Convertors convertor = new Convertors();
		java.util.UUID commentid = convertor.getTimeUUID();
	    	        
	    PreparedStatement ps = session.prepare("insert into comments (commentid,comment,picid,login) values(?,?,?,?)");
	    BoundStatement bs = new BoundStatement(ps);
	    session.execute(bs.bind(commentid,comment,picid,login));
	}
	

	//========================================================================================================================
			// method to display the comments
		
	 public LinkedList<String> getCommentsForPic(String picid) {
		 LinkedList<String> comments = new LinkedList<>();
		 	
	        Session session = cluster.connect("instagrim");
	        PreparedStatement ps = session.prepare("select login,comment from comments where picid=?  ALLOW FILTERING");
	        BoundStatement boundStatement = new BoundStatement(ps);
	        ResultSet rs = null;
	        rs = session.execute(boundStatement.bind(picid));
	        
	        if (rs.isExhausted()) {
	            System.out.println("No Comments Yet.");
	            return null;
	        } else {
	            for (Row row : rs) {	                
	                comments.add(row.getString("login")+": "+row.getString("comment"));
	            }
	        }
	        
	        return comments;
	    }
		
	
	//========================================================================================================================
		//this one is my work
	
		// method to make a query that returns all pics in the dB, almost a copy of the above method
	public LinkedList<Pic> getAllPics() {		
        LinkedList<Pic> Pics = new LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid,caption,user from userpiclist");
        ResultSet rs = null;
        
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute(boundStatement.bind( ));	// no need to bind as we don't have where xxxx=?
        
        if (rs.isExhausted()) {
            System.out.println("No pictures found");
            return null;
        } 
        else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                System.out.println("UUID" + UUID.toString());
                pic.setCaption(row.getString("caption"));
                pic.setUser(row.getString("user"));
                pic.setUUID(UUID);                
                Pics.add(pic);            
                }
        }
        return Pics;
    }
	
	
	//========================================================================================================================
	

	public Pic getPic(int image_type, java.util.UUID picid) throws IOException {
		Session session = cluster.connect("instagrim");
		ByteBuffer bImage = null;
		String type = null;
		int length = 0;
		try {
			Convertors convertor = new Convertors();
			ResultSet rs = null;
			PreparedStatement ps = null;

			if (image_type == Convertors.DISPLAY_IMAGE|| image_type == Convertors.MAKE_RED) {
				System.out.println("Getting image...");
				ps = session
						.prepare("select image,imagelength,type from pics where picid =?");
			} else if (image_type == Convertors.DISPLAY_THUMB) {
				ps = session
						.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
			} else if (image_type == Convertors.DISPLAY_PROCESSED) {
				ps = session
						.prepare("select processed,processedlength,type from pics where picid =?");
			}
			BoundStatement boundStatement = new BoundStatement(ps);
			rs = session.execute( // this is where the query is executed
					boundStatement.bind( // here you are binding the
											// 'boundStatement'
							picid));

			if (rs.isExhausted()) {
				System.out.println("No Images returned");
				return null;
			} else {
				for (Row row : rs) {
					if (image_type == Convertors.DISPLAY_IMAGE || image_type == Convertors.MAKE_RED) {
						bImage = row.getBytes("image");
						length = row.getInt("imagelength");
					} else if (image_type == Convertors.DISPLAY_THUMB) {
						bImage = row.getBytes("thumb");
						length = row.getInt("thumblength");

					} else if (image_type == Convertors.DISPLAY_PROCESSED) {
						bImage = row.getBytes("processed");
						length = row.getInt("processedlength");
					}

					type = row.getString("type");

				}
			}
		} catch (Exception et) {
			System.out.println("Can't get Pic" + et);
			return null;
		}
		session.close();
		Pic p = new Pic();
		
		
		if(image_type == Convertors.MAKE_RED){
            System.out.println("Applying make red...");
            byte[] tp = new byte[bImage.remaining()];
            bImage.get(tp);
            tp = redPic(tp, type);    
            bImage = ByteBuffer.wrap(tp);
        }
		
		
		p.setPic(bImage, length, type);		
		return p;
	}
	
		// credit:  the following function is "inspired" the user littlefury, this is here so I can exercise in putting filters
	public byte[] redPic(byte[] byteArray, String type){
        byte[] imageInByte = null;
        try{
            String types[]=Convertors.SplitFiletype(type);
            System.out.println("Filter... type: " + type);
            InputStream input = new ByteArrayInputStream(byteArray);
            BufferedImage original = ImageIO.read(input); // dimensions width x height, black on transparent

            for (int x = 0; x < original.getWidth(); x++) {
                for (int y = 0; y < original.getHeight(); y++) {
                    int rgba = original.getRGB(x,y);
                    Color col = new Color (rgba, true);
                    col = new Color (col.getRed(), col.getGreen() - (col.getGreen()/2), col.getBlue() - (col.getBlue()/2));
                    original.setRGB(x, y, col.getRGB());
                }
            }
            
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageIO.write(original, types[1], bs);
            bs.flush();
            imageInByte = bs.toByteArray();

            bs.close();
            input.close();
        }
        catch(IOException et){
            
        }
        return imageInByte;
    }
}
