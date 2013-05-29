package persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Scanner;

import javax.swing.tree.DefaultMutableTreeNode;

import domain.MenuAction;

/**
 * This class maps the menu to the appropriate Java objects.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 30/10/2012
 */
public class MenuMapper {

    private final String FILE = "/assets/menu/menu.txt";
    
    /**
     * This method creates the tree representation of the menu.
     * 
     * @return defaultMutableTreeNode   The root of the menu.
     */
	public DefaultMutableTreeNode loadMenu() {
	    MenuAction menuRootAction = new MenuAction("root", "root", 0, 0);
	    DefaultMutableTreeNode root = new DefaultMutableTreeNode(menuRootAction);
	    
	    Scanner input = new Scanner(this.getClass().getResourceAsStream(FILE));
	    
	    while(input.hasNext()) {
	        int parent = input.nextInt();
	        int menuActionId = input.nextInt();
	        String action = input.next();
	        String description = input.nextLine().trim();
	        
	        MenuAction menuAction = new MenuAction(action, description, parent, menuActionId);
	        DefaultMutableTreeNode node = new DefaultMutableTreeNode(menuAction);
	        
	        this.addToParent(root, node);
	    }
	    
	    input.close();
	    
	    return root;
	}
	
	/**
	 * This method adds the child to the correct parent.
	 * 
	 * @param root      The root of the menu.
	 * @param child     The child that needs to be attached to another node.
	 */
	private void addToParent(DefaultMutableTreeNode root, DefaultMutableTreeNode child) {
	    boolean found = false;
	    
	    MenuAction menuAction = (MenuAction)child.getUserObject();
	    
	    Enumeration<DefaultMutableTreeNode> it = root.depthFirstEnumeration();
	    
	    while(it.hasMoreElements() && !found) {
	        DefaultMutableTreeNode element = it.nextElement();
	        
	        if(((MenuAction)element.getUserObject()).getMenuActionId() == menuAction.getParent()) {
	            element.add(child);
	            
	            found = true;
	        }
	    }
	}
}
