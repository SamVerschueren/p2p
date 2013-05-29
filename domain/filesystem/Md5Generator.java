package domain.filesystem;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;

import domain.FileTask;

/**
 * Class that generates an md5 hash 
 * 
 * @author Sam Verschueren 
 *
 */
public class Md5Generator extends Observable {
    private String currentFileName;
    private FileTask fileTask;
    
    public Md5Generator(long totalFileSize) {
        fileTask = new FileTask("Hashing");
        fileTask.setFileSize(totalFileSize);
        fileTask.setStatus("Hashing...");
        fileTask.setMd5("hashingItems");
    }

    public String getMD5(File file) {
        try {
            currentFileName = file.getName();
            
            fileTask.setFileName(currentFileName);
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();

            DataInputStream dis = new DataInputStream(new FileInputStream(file));

            byte[] bytes = new byte[1048576]; // Reading 1 mb of data at a time

            int read = 0;

            while ((read = dis.read(bytes)) != -1) {
                md.update(bytes, 0, read);
                fileTask.setDownloadedSize(fileTask.getDownloadedSize() + read);
                setChanged();
                notifyObservers(fileTask);
            }

            byte[] md5 = md.digest();

            BigInteger bigInt = new BigInteger(1, md5);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            dis.close();
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public String getCurrentFileName() {
        return currentFileName;
    }
    
    public FileTask getFileTask(){
        return this.fileTask;
    }
    
    public void taskSavingFile(){
        fileTask.setStatus("Saving xml file");
        setChanged();
        notifyObservers(fileTask);
    }
    
    public void taskCompleted(){
        fileTask.done();
        setChanged();
        notifyObservers(fileTask);
    }
}
