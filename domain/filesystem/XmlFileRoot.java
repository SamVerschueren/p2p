package domain.filesystem;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Root class for the xml file, contains only directories
 * 
 * @author Bart Beyers <bart.beyers@gmail.com>
 * @since 29/10/2012
 * 
 */
@XmlRootElement(name = "p2p")
public class XmlFileRoot implements Serializable{

    private static final long serialVersionUID = -2582496433935304247L;

    private String userName;
    @XmlElement(name = "directory")
    private List<Directory> directories;

    /**
     * Creates a new instance of the rootfile
     */
    public XmlFileRoot() {
        directories = new ArrayList<>();
    }

    /**
     * Creates a new instance of the rootfile with a username.
     * 
     * @param username
     *            The username
     */
    public XmlFileRoot(String username) {
        this();

        this.userName = username;
    }

    /**
     * Adds a directory to the root
     * 
     * @param dir
     *            Directory to be added
     */
    public void addDirectory(Directory dir) {
        directories.add(dir);
    }

    /**
     * Removes a directory from the root
     * 
     * @param dirname
     *            Name of the directory to be removed
     */
    public void removeDirectory(String dirname) {
        int index = -1, count = 0;
        ;

        while (index == -1 && count < directories.size()) {
            if (directories.get(count).getName() == dirname) {
                index = count;
            } else {
                count++;
            }
        }

        if (index != -1)
            directories.remove(index);
    }

    /**
     * Sets the username
     * 
     * @param name
     *            The new username
     */
    public void setUserName(String name) {
        this.userName = name;
    }

    /**
     * Gets the username
     * 
     * @return Username
     */
    @XmlElement
    public String getUserName() {
        return this.userName;
    }

    /**
     * Gets all the directories of the root file
     * 
     * @return All directories
     */
    public List<Directory> getDirectories() {
        return directories;
    }

    /**
     * Generates an md5 hash of all files nested in these directories
     * 
     * @param md5Generator
     *            The generator to create the md5 hash
     */
    public void generateMd5(Md5Generator md5Generator) {
        try {
            for (Directory dir : directories) {
                dir.generateMd5(md5Generator);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Calculates the total file size of all files nested in these directories
     * 
     * @return Total file size
     */
    public long getTotalFileSize() {
        long fileSize = 0;
        for (Directory dir : directories) {
            fileSize += dir.getFileSize();
        }
        return fileSize;
    }
    
    public FileRepresentation getFileByName(String[] pathToFile){
    	if(pathToFile!= null && pathToFile.length > 0){
            Directory dir = getDirectory(pathToFile[0]);
            if(dir != null){
                return dir.getFileByName(pathToFile, 1);
            } 
        }
        return null;
    }
    
    public File getFileByMd5(String md5){
        int count = 0;
        File file = null;
        while(file == null && count<directories.size()){
            file = directories.get(count).getFileByMd5(md5);
            count++;
        }        
        return file;        
    }
    
    private Directory getDirectory(String name){
        Directory dir = null;
        int count = 0;
        
        while(dir==null&&count<directories.size()){
            Directory tempDir = directories.get(count);
            if(tempDir.getName().equals(name)){
                dir = tempDir;
            }
            count++;
        }
        
        return dir;
    }
}
