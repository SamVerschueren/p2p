package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
    
    public MyTreeCellRenderer(){
    }
    
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {

    	try {
	        super.getTreeCellRendererComponent(tree, value, selected, expanded,
	                leaf, row, hasFocus);
	
	        if (leaf) {            
	            if(((DefaultMutableTreeNode)value).getUserObject() instanceof String[]){
	                JPanel panel = new JPanel();
	                
	                String[] values = (String[])((DefaultMutableTreeNode)value).getUserObject();
	                JLabel label = (JLabel)this;
	                label.setText(values[0]);
	                panel.setLayout(new GridLayout(2, 1));
	                
	                String v = values[0].toString();
	                String[] splitted = v.split("\\.");
	                String extension = splitted[splitted.length-1];               
	                
	                InputStream stream = this.getClass().getResourceAsStream("/assets/image/" + extension + ".png");
	                
	                if (stream != null) {
	                	Image image = ImageIO.read(stream);
	                	
	                    label.setIcon(new ImageIcon(image));
	                } else if (extension.equals("avi") || extension.equals("mkv")
	                        || extension.equals("3gp") || extension.equals("mp4")) {
	                	Image image = ImageIO.read(this.getClass().getResourceAsStream("/assets/image/vlc.png"));
					
						label.setIcon(new ImageIcon(image));
	                }
	                
	                panel.setBackground(Color.WHITE);
	                panel.add(label);
	                JLabel size = new JLabel(values[1]);
	                size.setForeground(Color.GRAY);
	                Font f = label.getFont();
	                
	                f = new Font(f.getFontName(), f.getStyle(), f.getSize()-1);
	                
	                Image emptyImage = ImageIO.read(this.getClass().getResourceAsStream("/assets/image/empty.png"));
	                
	                size.setFont(f);
	                size.setIcon(new ImageIcon(emptyImage));
	                panel.add(size);
	                return panel;
	            }
	        }
    	}
    	catch(IOException e) { }

        return this;
    }
}
