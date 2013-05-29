package domain.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Observable;

/**
 * This class listens to incomming messages constantly and notifies his observers when something
 * changed.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 11/10/2012
 */
public class NetworkListener extends Observable implements Runnable {
    
    private InetAddress multiCastAddress;
    private MulticastSocket mcSocket;
    
    /**
     * Creates a new instance of our oh so loosely coupled networklistener.
     */
    public NetworkListener() {
        try {
            this.multiCastAddress = InetAddress.getByName("230.0.0.1");
            
            this.mcSocket = new MulticastSocket(4444);
            this.mcSocket.joinGroup(this.multiCastAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Constantly running and listening to the networkmessages.
     */
    @Override
    public void run() {
        DatagramPacket packet;
        
        while(true) {
            byte[] buffer = new byte[1024];
            packet = new DatagramPacket(buffer, buffer.length);
            
            try {
                this.mcSocket.receive(packet);
                
                ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
                ObjectInput in = new ObjectInputStream(bis);
                
                Object object = in.readObject();
               
                this.setChanged();
                this.notifyObservers(object);
                
                bis.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}