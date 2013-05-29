package ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import domain.DomainController;
import domain.Observable;
import domain.Observer;

/**
 * This panel holds the tabs where all the actions happens like chatting and showing a treeview of the files.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 20/10/2012
 */
public class TabPanel extends JPanel implements Observer {

    private DomainController controller;
    
    private JTabbedPane tabbedPane;
    
    /**
     * Creates a new instance of the panel.
     * 
     * @param controller        The controller that handles everything to the domain.
     */
    public TabPanel(DomainController controller) {
        this.controller = controller;
        
        this.setLayout(new BorderLayout());
        
        this.init();
    }
    
    /**
     * Initializes the gui components.
     */
    private void init() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Chat", new ChatPanel(controller));
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    @Override
    public void update(Observable o, Object arg) {
        String title = arg.toString();
        
        int index = tabbedPane.indexOfTab(title);
        
        if(tabbedPane.indexOfTab(title) == -1) {
        	tabbedPane.addTab(title, new FileTreePanel(controller, title));
            index = tabbedPane.getTabCount()-1;
        }

        tabbedPane.setSelectedIndex(index);
    }
}
