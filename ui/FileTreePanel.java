package ui;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import ui.components.MyTreeCellRenderer;
import domain.DomainController;
import domain.filesystem.Directory;
import domain.filesystem.FileRepresentation;
import domain.filesystem.XmlFileRoot;

public class FileTreePanel extends JPanel implements Observer, MouseListener {
	
    private DomainController controller;
    private JTree tree;
    private String userName;
    
    private JScrollPane centerPane;
    
    public FileTreePanel(DomainController controller, String userName){
        super();
        controller.registerXmlFileRepository(this);
        this.controller = controller;
        this.userName = userName;
        init();
    }
    
    private void init(){
        setLayout(new BorderLayout());
        
        this.centerPane = new JScrollPane();
        
        XmlFileRoot root = controller.getXmlFileForUser(userName);
        
        if(root != null)
        	this.showTree(root);
        
        add(centerPane, BorderLayout.CENTER);
    }
    
    private void showTreeHelper(Directory dir, DefaultMutableTreeNode parent){
        parent.setUserObject(dir.getName());
        for(Directory d: dir.getDirectories()){
            DefaultMutableTreeNode child = new DefaultMutableTreeNode();
            parent.add(child);
            showTreeHelper(d, child);
        }
        for(FileRepresentation f: dir.getFiles()){
            DefaultMutableTreeNode child = new DefaultMutableTreeNode();
            parent.add(child);
            String sizeIndicator = "bytes";
            long size = f.getSize();
            if (size > 1024){
                sizeIndicator = "kB";
                size = size/1024;
            }
            if(size > 1024){
                sizeIndicator = "MB";
                size = size/1024;
            }
            if(size > 1024){
                sizeIndicator = "GB";
                size = size/1024;
            }
            String[] values = new String[2];
            values[0] = f.getName();
            values[1] = String.format("%d %s",size, sizeIndicator);
            child.setUserObject(values);
        }
    }
    
    private void showTree(XmlFileRoot fileRoot) {
    	DefaultMutableTreeNode top = new DefaultMutableTreeNode(fileRoot.getUserName());
    	
    	tree = new JTree(top);
        tree.setCellRenderer(new MyTreeCellRenderer());
        tree.setRowHeight(0);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setExpandsSelectedPaths(true);
        tree.addMouseListener(this);
    	
    	for(Directory dir: fileRoot.getDirectories()){
            DefaultMutableTreeNode directoryNode = new DefaultMutableTreeNode();
            top.add(directoryNode);
            showTreeHelper(dir, directoryNode);
        }
    	
    	tree.expandRow(0);
        tree.setRootVisible(false);
    	
        centerPane.getViewport().add(tree);
    }
    
	@Override
	public void update(Observable o, Object object) {
		if(object instanceof XmlFileRoot) {
			XmlFileRoot fileRoot = (XmlFileRoot)object;
			
			System.out.println(fileRoot.getUserName());
			
			if(fileRoot.getUserName().equals(this.userName)) {
				this.showTree(fileRoot);
			}
		}
	}
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // If the user clicked twice
        if(e.getClickCount() == 2) {
            
            try {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                if(node==null || !node.isLeaf())
                    return;
                TreeNode[] path= node.getPath();
                
                String[] pathToFile = new String[path.length-1];
                for(int i=1;i<path.length;i++){
                    DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode)path[i];
                    if(tempNode.getUserObject() instanceof String[]){
                        String[] values = (String[])tempNode.getUserObject();
                        pathToFile[i-1] = values[0];
                    } else{
                        pathToFile[i-1] = path[i].toString();
                    }
                }
                System.out.println(Arrays.toString(pathToFile));
                
                controller.downloadFile(userName, pathToFile);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this.getParent(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) { }

    @Override
    public void mouseExited(MouseEvent arg0) { }

    @Override
    public void mousePressed(MouseEvent arg0) {}

    @Override
    public void mouseReleased(MouseEvent arg0) { }
}
