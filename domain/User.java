package domain;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Represents a user in our program. The user can chat, download, upload and all that kinds of
 * stuff.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 11/10/2012
 */
public class User implements Serializable, Comparable<User> {

    private String name;
    private InetAddress inetAddress;
    
    /**
     * Creates a new instance of the user.
     * 
     * @param name      The name of the user.
     */
    public User(String name) {
        this.setName(name);
        try {

            //MATTS:
        	//String host = "169.254.19.144";
            //this.setInetAddress(InetAddress.getByName(host));


        	//DE REST:
            this.setInetAddress(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            System.out.println("fout");
        }
        
    }
    
    /**
     * Sets the name of the user.
     * 
     * @param name      The name, duh!
     */
    private void setName(String name) {
        if(name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("Please provide a username to login.");
        }
        
        this.name = name;
    }
    
    /**
     * Returns the name of the user.
     * 
     * @return name     The name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Sets the InetAddress of the user.
     * 
     * @param inetAddres    The InetAddress of the user.
     */
    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }
    
    /**
     * Returns the InetAddress of the user.
     * 
     * @return inetAddress  The InetAddress of the user.
     */
    public InetAddress getInetAddress() {
        return this.inetAddress;
    }
    
    @Override
    public boolean equals(Object object) {
        if(object == null || object.getClass() != this.getClass())
            return false;
        if(object == this)
            return true;
        
        User user = (User)object;
        
        return user.name.equals(this.name) && user.inetAddress.equals(this.inetAddress);        
    }
    
    /**
     * checks the order of a user to this user
     * 
     * @return int     
     */
    @Override
    public int compareTo(User user) {
        return user.name.compareTo(this.name);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
