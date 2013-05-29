package domain.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import domain.DomainController;
import domain.User;

/**
 * The controller that handles all the networktrafic.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 1/11/2012
 *
 */
public class NetworkController {
	
	private DomainController domainController;
	
    private NetworkSender sender;
    private NetworkListener listener;
    private TCPServerManager manager;
    
    private Map<String, TCPConnection> connections;
    
    private List<Observer> tcpObservers = new ArrayList<Observer>();
    
    private ExecutorService pool = Executors.newCachedThreadPool();
    
    /**
     * Creates a new instance of the networkcontroller.
     */
    public NetworkController(DomainController controller) {
    	this.domainController = controller;
    	
    	this.init();
        
        this.connections = new HashMap<String, TCPConnection>();
    }
    
    /**
     * Initializes all the networkobjects.
     */
    private void init() {
        this.sender = new NetworkSender();
        
        this.listener = new NetworkListener();
        
        try {
            this.manager = new TCPServerManager(this.domainController, 4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(listener);
        pool.execute(manager);
    }
    
    public void addMulticastObserver(Observer o) {
    	this.listener.addObserver(o);
    }
    
    /**
     * Adds a new observer to the networklistener. When the listener receives a multicast
     * message, he will notifies all the observers.
     * 
     * @param o                 The observer for the listener.
     */
    public void addTCPObserver(Observer o) {
    	this.tcpObservers.add(o);
    	
        for(TCPConnection connection:this.connections.values()) {
        	connection.addObserver(o);
        }
    }
    
    public void addServerObserver(Observer o) {
    	this.manager.addServerObserver(o);
    }
    
    /**
     * Sends an object over the wire...
     * 
     * @param object            The object to be send.
     */
    public void sendObject(NetworkObject object) {
        try {
            sender.send(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void createTCPConnection(User user) throws IOException {
        if(user == null) {
            throw new IllegalArgumentException("The user does not exist.");
        }
        
        // If the ip address of the user is not in the connections map, create a connection.
        if(!this.connections.containsKey(user.getInetAddress().getHostAddress())) {
            TCPConnection connection = new TCPConnection(user.getInetAddress(), 4444);
            
            for(Observer o:this.tcpObservers) {
            	connection.addObserver(o);
            }
            
            pool.execute(connection);
            
            this.connections.put(user.getInetAddress().getHostAddress(), connection);
        }
    }

    public void downloadXml(User u) throws IOException {
        TCPConnection connection = this.connections.get(u.getInetAddress().getHostAddress());
        
        if(connection == null) {
            this.createTCPConnection(u);
        }
        
        connection.send(new NetworkObject(NetworkAction.REQUEST_XML));
    }
    
    public Observable downloadFile(User u, String md5) throws IOException{
        TCPConnection connection = connections.get(u.getInetAddress().getHostAddress());
        
        if(connection == null){
            createTCPConnection(u);
        }
        
        NetworkObject object = new NetworkObject(NetworkAction.REQUEST_FILE);
        object.setObject(md5);
        connection.send(object);
        
        return connection;
    }
}
