package domain.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import domain.DomainController;

/**
 * The manager class for all the incomming connection requests.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 1/11/2012
 */
public class TCPServerManager implements Runnable {

	private DomainController domainController;
	
    private ServerSocket serverSocket;
    private ExecutorService pool;
    
    private boolean isRunning = true;
    
    private List<Observer> serverObservers = new ArrayList<Observer>();
    
    /**
     * Creates a new instance of the TCPServerManager.
     * 
     * @param port              The portnumber on which the manager should listen for incomming calls.
     * @throws IOException      When the ServerSocket can not be created.
     */
    public TCPServerManager(DomainController controller, int port) throws IOException {
    	this.domainController = controller;
    	
        this.serverSocket = new ServerSocket(port);
        
        this.pool = Executors.newCachedThreadPool();
    }
    
    /**
     * Closes the threadpool and the serversocket.
     */
    public void stop() {
        isRunning = false;
        
        try {
            pool.shutdown();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addServerObserver(Observer observer) {
    	this.serverObservers.add(observer);
    }
    
	@Override
	public void run() {
	    while(isRunning) {
	        try {
	        	Socket clientSocket = serverSocket.accept();
	            
	        	TCPServer server = new TCPServer(this.domainController, clientSocket);
	        	
	        	for(Observer o:this.serverObservers) {
	        		server.addObserver(o);
	        	}
	        	
	            pool.execute(server);
	        } catch (SocketException e) {
	            break;
	        } catch (IOException e) {
	            System.out.println("Something went wrong while accepting a client. Error: " + e.getMessage());
	        }
	    }        
	}

}
