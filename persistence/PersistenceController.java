package persistence;

import java.io.File;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;

import domain.filesystem.XmlFileRoot;

/**
 * This class is the gate to persistent data.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 22/10/2012
 */
public class PersistenceController {
    
    private MenuMapper menuMapper;
    private XmlFileHandler xmlFileHandler;
    
    /**
     * Creates a new instance of the persistencecontroller.
     */
    public PersistenceController() {
        this.menuMapper = new MenuMapper();
        this.xmlFileHandler = new XmlFileHandler();
    }
    
    /**
     * This method creates the tree representation of the menu.
     * 
     * @return defaultMutableTreeNode   The root of the menu.
     */
    public DefaultMutableTreeNode loadMenu() {
        return menuMapper.loadMenu();
    }
    
    /**
     * Creates a xml file which contains the files the user wants to share
     * 
     * @param root Contains all directories and files that will be put in the xml file
     * @param xmlFilename Name of the file that will be created
     * @throws IOException 
     */
    public void createXml(XmlFileRoot root) throws IOException{
        xmlFileHandler.createXml(root);
    }
    
    /**
     * Reads all directories and files specified in an xml file
     * 
     * @param xmlFilename Name of the file that will be read
     * @return All directories and files
     */
    public XmlFileRoot readXml(File xmlFile){
        return xmlFileHandler.readXml(xmlFile);
    }
}
