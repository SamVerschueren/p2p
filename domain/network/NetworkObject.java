package domain.network;

import java.io.Serializable;

/**
 * Represents an object that can be send over the wire. This makes it easier to talk to other applications. You can
 * set the action or the purpose of this networkobject, and you can set the actual data itself.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 31/10/2012
 */
public class NetworkObject implements Serializable {

    private NetworkAction action;
    private Object object;
    
    /**
     * Creates a new instance of the networkobject.
     */
    public NetworkObject() {        
    }
    
    /**
     * Creates a new instance of the networkobject.
     * 
     * @param action        The action of the networkobject.
     */
    public NetworkObject(NetworkAction action) {
        this(action, null);
    }
    
    /**
     * Creates a new instance of the networkobject.
     * 
     * @param action        The action of the networkobject.
     * @param object        The real object that you wanted to send in the first place.
     */
    public NetworkObject(NetworkAction action, Object object) {
        this.setAction(action);
        this.setObject(object);
    }
    
    /**
     * Sets the action of the networkobject.
     * 
     * @param action        The action of the networkobject.
     */
    public void setAction(NetworkAction action) {
        this.action = action;
    }
    
    /**
     * Returns the action of the networkobject.
     * 
     * @return The action of the networkobject.
     */
    public NetworkAction getAction() {
        return this.action;
    }
    
    /**
     * Sets the actual data of the networkobject.
     * 
     * @param object        The actual data.
     */
    public void setObject(Object object) {
        this.object = object;
    }
    
    /**
     * Returns the actual data of the networkobject.
     * 
     * @return The actual data of the object.
     */
    public Object getObject() {
        return this.object;
    }
}
