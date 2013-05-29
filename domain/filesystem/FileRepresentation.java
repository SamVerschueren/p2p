package domain.filesystem;

import java.io.File;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

/**
 * Represents a directory
 * 
 * @author Bart Beyers <bart.beyers@gmail.com>
 * @since 27/10/2012
 */
public class FileRepresentation implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -4193505532055058940L;
    @XmlElement
    private String name;
    @XmlElement
    private long size;
    @XmlElement
    private long modified;    
    private String md5;
    @XmlElement
    private String path;

    public FileRepresentation() {

    }

    /**
     * Creates a new instance of a file representation
     * 
     * @param name Name of the file
     * @param size Size of the file
     * @param modified When the file was last modified
     * @param md5  Md5 hash of the file
     */
    public FileRepresentation(String name, long size, long modified, String path) {
        this.name = name;
        this.size = size;
        this.modified = modified;
        this.path = path;
    }

    /**
     * Gets the name of the current file
     * 
     * @return Name of the current file
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the size of the current file
     * 
     * @return Size of the current file
     */
    public long getSize() {
    	return size;
    }
    
    public long getMd5Size(){
    	if(md5 == null)
    		return size;
    	
    	return 0;
    }

    /**
     * Sets the md5 hash of this file
     * 
     * @param md5
     *            Md5 hash of the file
     */
    @XmlElement
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    
    /**
     * Gets the md5 hash of this file
     * @return Md5 hash of the file
     */
    public String getMd5(){
        return md5;
    }

    /**
     * Calculates the md5 hash of this file
     * 
     * @param md5Generator Md5 Generator
     */
    public void generateMd5(Md5Generator md5Generator) {
        if (md5 == null) {
            this.md5 = md5Generator.getMD5(getFile());
        }
    }
    
    public File getFile(){
        return new File(path);
    }

}
