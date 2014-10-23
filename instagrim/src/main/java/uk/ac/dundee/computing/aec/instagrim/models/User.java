/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
//import uk.ac.dundee.computing.aec.instagrim.stores.UsersStore;

/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
    public User(){
        
    }
    
    public boolean RegisterUser(String username, String Password, String firstName, String lastName, String email ){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
        
        String currentUser = username.toString();	// added so I can use it in the setting up the third PreparedStatement below
        
        	/*
        	 * Note:
        	 * A SQL statement is precompiled and stored in a >>>PreparedStatement object<<<. 
        	 * This object can then be used to efficiently execute this statement multiple times.
        	 */        
                
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password,first_name,last_name,email) Values(?,?,?,?,?)");
        PreparedStatement psChecker = session.prepare("select login from userprofiles where login =?"); 	// a checker to prepare for pulling usr/pass from database
        	// prepare the query to put the first name:
        //PreparedStatement psFirstName = session.prepare("insert into userprofiles (login,first_name) Values('currentUser', ?)");
       
        	/*
        	* Note:
        	*  To write changes to the database, such as for INSERT or UPDATE operations,
        	*   you will typically create a PreparedStatement object. 
         	*  This allows you to execute a statement with varying sets of input parameters
         	*/        
        BoundStatement boundStatement = new BoundStatement(ps);
        BoundStatement boundStatementChecker = new BoundStatement(psChecker);	// the bound statement for the prepared statement
        //BoundStatement boundStatementFirstName = new BoundStatement(psFirstName);	// the BS for the psFirstName
        
        	/* my addition:
        	 * 
        	 * Note:
        	 * A table of data representing a database result set, 
        	 * which is usually generated by executing a statement that queries the database. 
        	 */
        ResultSet usernameResults = session.execute(boundStatementChecker.bind(username));	// execute the checker (query), do necessary binding dynamically
        //ResultSet firstNameResults = session.execute(boundStatementFirstName.bind(username));
                
        if (!usernameResults.isExhausted()){	//If statement to go through the usernames in the database and check if yours is available
            return false;
        }
        else{        
        	// else execute Andy's original code: 
        session.execute( // this is where the query is executed
                boundStatement.bind(username,EncodedPassword,firstName,lastName,email)); // here you are binding the 'boundStatement'   
        }
        return true;
    }     
    
    
    //================================================================================================================
    // Update user's details -- a copy of the Register User 
    
    public void UpdateUserDetails(String username, String firstName, String lastName, String email ){
       
        Session session = cluster.connect("instagrim");    
        PreparedStatement psFirstNameDelete = session.prepare
        		("update userprofiles set first_name=?, last_name=?, email=? where login=? ");
        BoundStatement boundStatementFirstNameDelete = new BoundStatement(psFirstNameDelete);
        
        session.execute(boundStatementFirstNameDelete.bind(firstName, lastName, email, username ));  
        
    }
    
  //================================================================================================================
    
    
    public boolean IsValidUser(String username, String Password){    	
    		// encoding the password:
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        	// end of encoding
        
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind(username)); // here you are binding the 'boundStatement'
                        
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {               
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }   
    return false;  
    }
       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
       

       // the method that pulls the First Name from the database
       public String getFirstName(String username){
    	   String firstName = "no name found";
    	   Session session = cluster.connect("instagrim");
           PreparedStatement ps = session.prepare("select first_name from userprofiles where login =?");
           ResultSet rs = null;
           BoundStatement boundStatement = new BoundStatement(ps);
           rs = session.execute( // this is where the query is executed
                   boundStatement.bind(username)); // here you are binding the 'boundStatement'                           
           if (rs.isExhausted()) {
               System.out.println("No first name found");
               return "";
           } else {
               for (Row row : rs) {                  
                   firstName = row.getString("first_name");               
                   }
           }   
       return firstName ;  
       }    
       
    // the method that pulls the Last Name from the database
       public String getLastName(String username){
    	   String lastName = "no name found";
    	   Session session = cluster.connect("instagrim");
           PreparedStatement ps = session.prepare("select last_name from userprofiles where login =?");
           ResultSet rs = null;
           BoundStatement boundStatement = new BoundStatement(ps);
           rs = session.execute( // this is where the query is executed
                   boundStatement.bind(username)); // here you are binding the 'boundStatement'                           
           if (rs.isExhausted()) {
               System.out.println("No last name found");
               return "";
           } else {
               for (Row row : rs) {                  
                   lastName = row.getString("last_name");               
                   }
           }   
       return lastName ;  
       }  
     
       
       
     
    // the method that pulls the email from the database
       public String getEmail(String username){
    	   String email = "no email found";
    	   Session session = cluster.connect("instagrim");
           PreparedStatement ps = session.prepare("select email from userprofiles where login =?");
           ResultSet rs = null;
           BoundStatement boundStatement = new BoundStatement(ps);
           rs = session.execute( // this is where the query is executed
                   boundStatement.bind(username)); // here you are binding the 'boundStatement'                           
           if (rs.isExhausted()) {
               System.out.println("No last name found");
               return "";
           } else {
               for (Row row : rs) {                  
                   email = row.getString("email");               
                   }
           }   
       return email ;  
       } 
       
           
}
