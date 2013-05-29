package domain;

import java.util.Map;
import java.util.HashMap;

import domain.filesystem.XmlFileRoot;
import domain.network.NetworkAction;
import domain.network.NetworkObject;

/**
 * If you want to know more about this class, e-mail the author ;). He's responsible for this
 * class.
 * 
 * @author Bart Beyers		<bart.beyers@gmail.com>
 * @since the early days...
 */
public class XmlFileRepository extends java.util.Observable implements java.util.Observer {
	
    private Map<String, XmlFileRoot> xmlFiles;
    
    public XmlFileRepository(){
        xmlFiles = new HashMap<>();
    }
    
    public void add(String username, XmlFileRoot root){
        xmlFiles.put(username, root);
    }
    
    public XmlFileRoot get(String username){
        return xmlFiles.get(username);
    }

	@Override
	public void update(java.util.Observable observable, Object o) {
		if(o instanceof NetworkObject) {
			NetworkObject object = (NetworkObject)o;
			
			if(object.getAction() == NetworkAction.RESPONSE_XML) {
				if(object.getObject() instanceof XmlFileRoot) {
					XmlFileRoot response = (XmlFileRoot)object.getObject();
					
					xmlFiles.put(response.getUserName(), response);
					
					this.setChanged();
					this.notifyObservers(response);
				}
			}
		}
	}
}
