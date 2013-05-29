package domain.network;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Observable;

import domain.FileTask;

/**
 * Creates a TCPConnection to a specified InetAddress and port number.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @author Matts Devriendt		<matts.devriendt@gmail.com>
 * @since 1/11/2012
 */
public class TCPConnection extends Observable implements Runnable {

    private Socket client;
    private ObjectOutputStream writer;
    private String downloadsFolder = System.getProperty("user.home") + "/Downloads";
    
    /**
     * Creates a new instance of the TCPConnection.
     * 
     * @param inetAddress       The InetAddress of the destination.
     * @param port              The port of the destination service.
     * @throws IOException      If an error occurs when creating the socket.
     */
    public TCPConnection(InetAddress inetAddress, int port) throws IOException {
        this.client = new Socket(inetAddress, port);
        this.writer = new ObjectOutputStream(client.getOutputStream());
    }
    
    /**
     * Closes the TCPConnection.
     * 
     * @throws IOException      If an error occurs when closing the socket.
     */
    public void close() throws IOException {
        if(client != null) {
            writer.close();
            client.close();
        }
    }
    
    /**
     * Sends a NetworkObject over the wire to the other side.
     * 
     * @param object            The object to be send.
     * @throws IOException      If an error occurs when sending the object.
     */
    public void send(NetworkObject object) throws IOException {
        writer.writeObject(object);
        writer.flush();
    }

	@Override
	public void run() {
		ObjectInputStream stream = null;
		try {
			stream = new ObjectInputStream(client.getInputStream());
			RandomAccessFile datafile = null;
			ByteBuffer buffer = ByteBuffer.allocate(102400);
			//FileChannel channel;
	
			FileTask task = null;
			
			while(client.isConnected() && !client.isClosed()) {
				// RESPONSE_XML en RESPONSE_FILE komen hier binnen
				NetworkObject object = (NetworkObject)stream.readObject();
				
				if(object.getAction() == NetworkAction.RESPONSE_FILE) {
					Map<String, Object> data = (Map<String, Object>)object.getObject();
					
					File file = new File(this.downloadsFolder + "/" + data.get("filename").toString());
					
					if(datafile == null)
						datafile = new RandomAccessFile(file,"rw");
					
					FileChannel channel = datafile.getChannel();
			        
					if(!file.exists()) {
						file.createNewFile();
					}
					
					if(task == null) {
						task = new FileTask(file.getName());
						task.setFileSize((long)data.get("filesize"));
						task.setStatus("Downloading...");
						task.setMd5((String)data.get("md5"));
					}
					
					Object objData = data.get("data");					
					
					if(objData != null) {
						int len = (int)data.get("bytelength");
						
					    buffer.clear();
					    buffer.put((byte[])objData, 0, len);
				        buffer.flip();
				        channel.position(channel.size());
				        channel.write(buffer);
	
						task.setDownloadedSize(task.getDownloadedSize() + len);
						
						setChanged();
						notifyObservers(task);
					}
					else {
						datafile.close();
						task.done();
						
						setChanged();
						notifyObservers(task);
						
						task = null;
						datafile = null;
					}
					
				}
				else {
					this.setChanged();
					this.notifyObservers(object);
				}
			}
		} catch (EOFException e) {
			// Do nothing, the other side closed the line
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// If the stream exists, close it no mather what happens.
			if(stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

