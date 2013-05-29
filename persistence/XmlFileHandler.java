package persistence;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import domain.filesystem.XmlFileRoot;

/**
 * Class for handling all actions with xml files
 * 
 * @author Bart Beyers <bart.beyers@gmail.com>
 * @since 29/10/2012
 * 
 */
public class XmlFileHandler {
    private JAXBContext jc;

    public XmlFileHandler() {
        try {
            jc = JAXBContext.newInstance(XmlFileRoot.class);
        } catch (JAXBException e) {
            System.err.println("Error creating xml context");
            e.printStackTrace();
        }
    }

    /**
     * Generates an xml output of the selected directories and stores it in an
     * xml file
     * 
     * @param root
     *            Contains all the directories to be put in the xml file
     * @param xmlFilename
     *            Name of the xml file
     * @throws IOException 
     */
    public void createXml(XmlFileRoot root) throws IOException {
        try {
            String home = System.getProperty("user.home");
            String fileName = String.format("%s/p2p/%s.xml", home, root.getUserName());
            File xmlFile = new File(fileName);
            
            if(!xmlFile.exists())
            	xmlFile.createNewFile();
            
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(root, xmlFile);
        } catch (JAXBException e) {
            System.out.println("Error creating xml file: ");
            e.printStackTrace();
        }
    }

    /**
     * Reads an xml file and returns all files and directories specified in the
     * xml file
     * 
     * @param xmlFilename
     *            Name of the xml file
     * @return List of all files and directories in the xml file
     */
    public XmlFileRoot readXml(File xmlFile) {
        try {
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            return (XmlFileRoot) unmarshaller.unmarshal(xmlFile);
        } catch (JAXBException e) {
            System.out.println("Error creating xml file: ");
            e.printStackTrace();
            return null;
        }
    }
}
