package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import domain.network.NetworkAction;
import domain.network.NetworkObject;

/**
 * The repository with all the users.
 * 
 * @author Matts Devriendt      <matts.devriendt@gmail.com>
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 20/10/2012
 */
public class UserRepository extends Observable implements Observer {

    private User user;
    
    private List<User> users;
    private DomainController controller;
    
    public UserRepository(DomainController controller){
        users = new ArrayList<User>();
        this.controller = controller;
    }
    
    /**
     * Add a user to the repository and notify the observers
     * 
     * @param User    the user you want to add
     */
    public void add(User element) {
        if(!users.contains(element)) {
            users.add(element);
            
            Collections.sort(this.users);
            
            this.setChanged();
            this.notifyObservers();
        }
    }
    
    /**
     * Removes a user from the repository and notifies the observers.
     * 
     * @param element   the user you want to delete.
     */
    public void remove(User element) {
        this.users.remove(element);
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Returns all the users
     * 
     * @return List<User>     the list of users
     */
    public List<User> findAll(){
        return users;
    }
    
    /**
     * Sets the current user-object.
     * 
     * @param user      the user you want to set
     */
    public void setUser(User user) {
        this.user = user;
    }
    
    /**
     * Returns the current user-object.
     * 
     * @return User     returns the current user
     */
    public User getUser() {
        return this.user;
    }
    
    public User getUserByName(String name) {
        for(User u:this.users) {
            if(u.getName().equals(name)) {
                return u;
            }
        }
        
        return null;
    }

    @Override
    public void update(Observable observable, Object object) {
    	if(object instanceof NetworkObject) {
            NetworkObject no = (NetworkObject)object;
            
            if(no.getObject() instanceof User) {
                NetworkAction action = no.getAction();
                
                if(action == NetworkAction.DELETE_USER) {
                    this.remove((User)no.getObject());
                }
                else {
                	this.add((User)no.getObject());
                    
                    if(no.getAction() == NetworkAction.NEW_USER) {                    
                        controller.sendObject(new NetworkObject(NetworkAction.RESPONSE, user));
                    }
                }
            }
        }  
    }
}
