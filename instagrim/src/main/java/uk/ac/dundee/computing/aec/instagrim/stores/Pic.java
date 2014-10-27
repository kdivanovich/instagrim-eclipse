/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

import com.datastax.driver.core.utils.Bytes;
import java.nio.ByteBuffer;

/**
 *
 * @author Administrator
 */
public class Pic {

    private ByteBuffer bImage = null;
    private int length;
    private String type;
    private String caption;
    private String user;
    //private String likes;
    private java.util.UUID UUID=null;
    
    public void Pic() {
    }
    
    /*
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
    */
    
    public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
    

	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
    
    public void setUUID(java.util.UUID UUID){
        this.UUID =UUID;
    }
    public String getSUUID(){
        return UUID.toString();
    }
    public void setPic(ByteBuffer bImage, int length,String type) {
        this.bImage = bImage;
        this.length = length;
        this.type=type;
    }

    public ByteBuffer getBuffer() {
        return bImage;
    }

    public int getLength() {
        return length;
    }
    
    public String getType(){
        return type;
    }

    public byte[] getBytes() {
         
        byte image[] = Bytes.getArray(bImage);
        return image;
    }


}
