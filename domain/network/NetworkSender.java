package domain.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * Class that provides functionality of sending objects over a network.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 12/10/2012
 */
public class NetworkSender {
	
    private InetAddress multiCastAddress;
    private MulticastSocket mcSocket;
    
    /**
     * Creates a new instance of our uber cool network sender, w00t, w00t!
     */
    public NetworkSender() {
        try {
            this.multiCastAddress = InetAddress.getByName("230.0.0.1");
            
            this.mcSocket = new MulticastSocket(4444);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Sends a serializable object over a network.
     * 
     * @param object           The object that has to be send over the network.
     * @throws IOException     If something went wrong with the input/output.
     */
    public void send(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(object);
        
        byte[] buffer = bos.toByteArray();
        
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.multiCastAddress, 4444);
        
        this.mcSocket.send(packet);
        
        bos.close();
        out.close();
    }
}