package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.tree.DefaultMutableTreeNode;

import domain.DomainController;
import domain.MenuAction;

/**
 * This is THE JFrame. Every panel a user saw, is added to this!
 * 
 * @author Matts Devriendt      <matts.devriendt@gmail.com>
 * @since 20/10/2012
 */
public class Overview extends JFrame {

	private DomainController controller;
	private MenuHandler menuHandler;
	
    /**
     * Creates a new instance of the frame.
     */
	public Overview(DomainController controller) {
	    super("sheeP2Peehs");
	        
        this.controller = controller;
        this.menuHandler = new MenuHandler(this, controller);
        
        this.init();
        this.initMenu();
        
        this.setSize(900, 700);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);  
        this.setLocationRelativeTo(null);
        this.setVisible(true);
	}
	
    /**
     * Initializes the gui components.
     */
	private void init(){
	    BorderLayout borderLayout = new BorderLayout();
	    borderLayout.setHgap(10);
	    borderLayout.setVgap(10);
	    setLayout(borderLayout);
	    
	    TabPanel tabPanel = new TabPanel(controller);
	    
	    UserPanel userPanel = new UserPanel(controller);
	    userPanel.addObserver(tabPanel);
	    
	    JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        centerSplitPane.setLeftComponent(tabPanel);
        centerSplitPane.setRightComponent(userPanel);
        centerSplitPane.setResizeWeight(0.8);
        centerSplitPane.setOneTouchExpandable(true);
        centerSplitPane.setContinuousLayout(true);
	    
        JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        bottomSplitPane.setTopComponent(centerSplitPane);
        bottomSplitPane.setBottomComponent(new TransferInfoPanel(controller));
        bottomSplitPane.setResizeWeight(0.8);
        bottomSplitPane.setOneTouchExpandable(true);
        bottomSplitPane.setContinuousLayout(true);
	    
	    add(makeHeader(),BorderLayout.NORTH);  
	    add(bottomSplitPane, BorderLayout.CENTER);
	}
	
	/**
	 * Initializes the JMenuBar of the application.
	 */
	private void initMenu() {
	    JMenuBar menuBar = new JMenuBar();
	    
	    Enumeration<DefaultMutableTreeNode> children = controller.loadMenu().children();
        while(children.hasMoreElements()) {
            DefaultMutableTreeNode element = children.nextElement();
            
            MenuAction menuAction = (MenuAction)element.getUserObject();
            
            JMenu menuHeader = new JMenu(menuAction.getDescription());      
            menuBar.add(menuHeader);
            
            this.createSubmenu(element.children(), menuHeader);
        }
        
        this.setJMenuBar(menuBar);
	}
	
	/**
	 * This method recursively creates the submenus.
	 * 
	 * @param children         The children of the root.
	 * @param menuComponent    The component on which the children has to be attached.
	 */
	private void createSubmenu(Enumeration<DefaultMutableTreeNode> children, JMenu menuComponent) {
	    while(children.hasMoreElements()) {
            DefaultMutableTreeNode child = children.nextElement();
            
            MenuAction menuAction = (MenuAction)child.getUserObject();
            
            if(child.isLeaf()) {
                JMenuItem menuItem = new JMenuItem(menuAction.getDescription());
                menuItem.setName(menuAction.getAction());
                menuItem.addActionListener(menuHandler);
                
                menuComponent.add(menuItem);
            }
            else {
                JMenu menu = new JMenu(menuAction.getDescription());
                
                menuComponent.add(menu);
                
                // Recursive call
                this.createSubmenu(child.children(), menu);
            }
        }
	}
	
    /**
     * Initializes the gui components for the header.
     * 
     * @return JPanel       panel with the name of the program
     */
	private JPanel makeHeader(){
	    JPanel header = new JPanel();
	    JLabel name = new JLabel("sheeP2Peehs");
	    name.setFont(new Font("Arial",Font.PLAIN,23));
	    name.setForeground(Color.DARK_GRAY);
	    
	    header.setLayout(new BorderLayout());
	    header.add(name,BorderLayout.EAST);
	    
	    return header;
	}
}
