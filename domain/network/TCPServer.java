package domain.network;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import domain.DomainController;
import domain.FileTask;

/**
 * This class manages an individual client.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 1/11/2012
 */
public class TCPServer extends Observable implements Runnable {

	private DomainController controller;
    private Socket client;
    
    /**
     * Creates a new instance of the TCPServer.
     * 
     * @param client        The socket of the clientconnection.
     */
    public TCPServer(DomainController controller, Socket client) {
    	this.controller = controller;
        this.client = client;
    }
    
    public byte[] read(File file) throws IOException {

        byte []buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if ( ios.read(buffer) == -1 ) {
                throw new IOException("EOF reached while trying to read the whole file");
            }        
        } finally { 
            try {
                 if ( ios != null ) 
                      ios.close();
            } catch ( IOException e) {
            }
        }

        return buffer;
    }
    
    @Override
    public void run() {
        try {
            ObjectInputStream stream = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            
            FileTask task = null;
            
            while(client.isConnected() && !client.isClosed()) {
                try {
                    NetworkObject object = (NetworkObject)stream.readObject();
                    
                    if(object.getAction() == NetworkAction.REQUEST_XML) {
                    	NetworkObject response = new NetworkObject(NetworkAction.RESPONSE_XML);
                    	response.setObject(this.controller.getXmlForCurrentUser());

                    	out.writeObject(response);
                    }
                    else if(object.getAction() == NetworkAction.REQUEST_FILE) {
                    	NetworkObject response = new NetworkObject(NetworkAction.RESPONSE_FILE);
                    	String md5 = object.getObject().toString();
                    	File file = controller.getXmlForCurrentUser().getFileByMd5(md5);
                    	
                    	if(task == null) {
                    		task = new FileTask(file.getName());
                    		task.setMd5(md5);
                    		task.setFileSize(file.length());
                    		task.setStatus("Uploading...");
                    	}
                    	
                    	if(!file.exists()) {
                    		System.err.println("Someone requested " + file.getName() + " but it does not exist");
                    		continue;
                    	}
                    	
                    	DataInputStream dis = new DataInputStream(new FileInputStream(file));
                    	
                    	byte[] bytes = new byte[102400]; // 100 kb per keer verzenden

                        int read = 0;
                        
                        int i=0;
                        
                        Map<String, Object> data = new HashMap<String, Object>();

                        data.put("filename", file.getName());
                        data.put("filesize", file.length());
                        data.put("md5", md5);
                        
                        while ((read = dis.read(bytes)) != -1) {
                            data.put("bytelength", read);
                            data.put("data", bytes);
                            data.put("chunk", i);
                            
                            task.setDownloadedSize(task.getDownloadedSize() + read);
                            
                            this.setChanged();
                            this.notifyObservers(task);
                            
                            response.setObject(data);
                            
                            i++;
                            
                            out.writeObject(response);
                            out.reset();
                        }

                        task.done();
                        
                        this.setChanged();
                        this.notifyObservers(task);
                        
                        task = null;
                        
                        dis.close();
                        
                        data.put("chunk", i);
                    	data.put("data", null);
                    	
                    	response.setObject(data);
                    	
                    	out.writeObject(response);
                    }
                } 
                catch (SocketException e) {
                    break;
                }
                catch (EOFException e) {
                    break;
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            
            stream.close();
            out.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
