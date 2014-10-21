/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.stores;

/**
 *
 * @author Administrator
 */
public class LoggedIn {
    boolean logedin=false;
    String Username=null;
    
    // to get the first name, getter/setter below called on an object lg in the .jsp files
    String first_name = null;
    String last_name = null;
    String email = null;
    
    public void LogedIn(){        
    }
    
    public void setLastName(String lastName){
        this.last_name=lastName;
    }
    public String getLastName(){
        return last_name;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return email;
    }
    public void setFirstName(String firstName){
        this.first_name=firstName;
    }
    public String getFirstName(){
        return first_name;
    }
    
    public void setUsername(String name){
        this.Username=name;
    }
    public String getUsername(){
        return Username;
    }
    public void setLogedin(){
        logedin=true;
    }
    public void setLogedout(){
        logedin=false;
    }
    
    public void setLoginState(boolean logedin){
        this.logedin=logedin;
    }
    public boolean getlogedin(){
        return logedin;
    }

}
