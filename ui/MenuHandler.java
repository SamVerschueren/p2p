package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

import domain.DomainController;

/**
 * This class handles all the menu actions.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 22/10/2012
 */
public class MenuHandler implements ActionListener {

    private JFrame parent;
    private DomainController controller;
    
    /**
     * Creates a new instance of the menuhandler.
     * 
     * @param controller        The gate to the domainlogic.
     */
    public MenuHandler(JFrame parent, DomainController controller) {
        this.parent = parent;
        this.controller = controller;
    }
    
	/**
	 * Exits the application
	 */
	public void exit() {
		controller.exit();
		
	    System.exit(0);
	}
	
	/**
	 * Shows the sharing frame
	 */
	public void sharing() {  
	    JDialog dialog = new JDialog(parent, "Sharing", true);
	    dialog.setLayout(new BorderLayout());
	    dialog.getContentPane().add(new SharingPanel(dialog, controller), BorderLayout.CENTER);
	
	    dialog.setSize(350, 250);
	    dialog.setResizable(false);
	    dialog.setLocationRelativeTo(null);
	    dialog.setVisible(true);
	}

    /**
     * This method is called when an action on a menuitem is performed.
     * 
     * @param e                 The actionevent.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String methodName = ((JMenuItem) e.getSource()).getName();
        
        Method method;
        try {
            method = getClass().getMethod(methodName, new Class[0]);
            method.invoke(this, new Object[0]);
        } catch (Exception ex) {
            throw new RuntimeException("Could not execute " + methodName + ".");
        }
    }
}
