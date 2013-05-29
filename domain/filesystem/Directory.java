package domain.filesystem;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * Represents a directory
 * 
 * @author Bart Beyers <bart.beyers@gmail.com>
 * @since 27/10/2012
 */

public class Directory implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -2467647584028251854L;
    @XmlElement
    private String name;
    @XmlElement(name = "file")
    private List<FileRepresentation> files;
    @XmlElement(name = "directory")
    private List<Directory> directories;
    @XmlElement
    private String path;

    public Directory() {
        files = new ArrayList<>();
        directories = new ArrayList<>();
    }

    /**
     * Creates a new instance of the directory representation
     * 
     * @param dirPath
     *            Path to the directory
     */
    public Directory(String dirPath) {
        this.path = dirPath;
        files = new ArrayList<>();
        directories = new ArrayList<>();
        readFiles();
    }

    /**
     * Reads the entire director and stores its children
     * 
     * @author Bart Beyers <bart.beyers@gmail.com>
     * @since 27/10/2012
     */
    private void readFiles() {
        File dir = new File(path);
        File[] children = dir.listFiles();
        name = dir.getName();

        for (File f : children) {
            if (f.isDirectory()) {
                Directory directory = new Directory(f.getAbsolutePath());
                directories.add(directory);
            } else {
                FileRepresentation file = new FileRepresentation(f.getName(),
                        f.length(), f.lastModified(), f.getAbsolutePath());
                files.add(file);
            }
        }
    }

    /**
     * Get the name of this directory
     * 
     * @return The name of this directory
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns all files in this directory
     * 
     * @return List of all files
     */
    public List<FileRepresentation> getFiles() {
        return files;
    }

    /**
     * Returns all subdirectories of this directory
     * 
     * @return List of all subdirectories
     */
    public List<Directory> getDirectories() {
        return directories;
    }

    /**
     * Generates an md5 hash of all files in this directory and all subdirectories
     * 
     * @param md5Generator The md5generator
     */
    public void generateMd5(Md5Generator md5Generator) {
        for (Directory dir : directories) {
            dir.generateMd5(md5Generator);
        }
        for (FileRepresentation f : files) {
            f.generateMd5(md5Generator);
        }
    }
    
    /**
     * Returns the total file size for all files in this directory and all subdirectories
     * 
     * @return Total file size
     */
    public long getFileSize() {
        long fileSize = 0;
        for (Directory dir : directories) {
            fileSize += dir.getFileSize();
        }
        for (FileRepresentation file : files) {
            fileSize += file.getMd5Size();
        }
        return fileSize;
    }
    
    public FileRepresentation getFileByName(String[] pathToFile, int index){
    	if(pathToFile != null && pathToFile.length > 0){
            if(index < pathToFile.length - 1){
                Directory dir = getDirectory(pathToFile[index]);
                index++;
                return dir.getFileByName(pathToFile, index);
            } else{
                return getFile(pathToFile[index]);
            }
        }
        return null;
    }
    
    public File getFileByMd5(String md5){
        File file = null;
        int count = 0;
        while(file == null && count<files.size()){
            FileRepresentation tempFile = files.get(count);
            if(tempFile.getMd5().equals(md5))
                file = tempFile.getFile();
            count++;
        }
        
        if(file!=null)
            return file;
        
        count = 0;
        while(file == null && count<directories.size()){
            file = directories.get(count).getFileByMd5(md5);
            count++;
        }
        
        return file;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
    private Directory getDirectory(String name){
        Directory dir = null;
        int count = 0;
        
        while(dir==null||count<directories.size()){
            Directory tempDir = directories.get(count);
            if(tempDir.getName().equals(name)){
                dir = tempDir;
            }
            count++;
        }        
        return dir;
    }
    
    private FileRepresentation getFile(String name){
        FileRepresentation file = null;
        int count = 0;
        
        while(file==null&&count<files.size()){
            FileRepresentation tempFile = files.get(count);
            if(tempFile.getName().equals(name)){
                file = tempFile;
            }
            count++;
        }
        
        return file;
    }
}
