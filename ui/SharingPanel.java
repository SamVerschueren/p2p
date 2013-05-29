package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import domain.DomainController;
import domain.commands.AddDirectoryCommand;
import domain.commands.RemoveDirectoryCommand;

public class SharingPanel extends JPanel implements ActionListener {

    private DomainController controller;
    
    private Window parent;
    private JList<Object> rootFolderList;
    private JButton btnAdd, btnDelete;
    private JButton btnOk, btnCancel;
    
    public SharingPanel(Window parent, DomainController controller) {
        this.parent = parent;
        this.controller = controller;
        
        this.setLayout(new BorderLayout());
        
        this.init();
    }
    
    private void init() {
        rootFolderList = new JList<Object>(controller.getRootFolderListModel());
        
        btnAdd = new JButton("Add");
        btnAdd.addActionListener(this);
        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(this);
        
        btnOk = new JButton("Ok");
        btnOk.addActionListener(this);
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        Box southPanel = Box.createHorizontalBox();
        southPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        southPanel.add(btnOk);
        southPanel.add(Box.createHorizontalGlue());
        southPanel.add(btnCancel);
        
        JPanel btnPanel = new JPanel(new GridLayout(2,1,5,5));
        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        
        JPanel eastPanel = new JPanel(new FlowLayout());
        eastPanel.add(btnPanel);
        
        JScrollPane listScrollPane = new JScrollPane(rootFolderList);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
        centerPanel.add(listScrollPane, BorderLayout.CENTER);
        
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnCancel) {
            controller.undoCommands();
            parent.dispose();
        }
        else if(e.getSource() == btnAdd) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            if(fileChooser.showDialog(parent, "Select folder") == JFileChooser.APPROVE_OPTION){
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                controller.addCommand(new AddDirectoryCommand(path));
            }
        }
        else if(e.getSource() == btnDelete) {
            int selectedIndex = rootFolderList.getSelectedIndex();
            
            if(selectedIndex >= 0) {                
                controller.addCommand(new RemoveDirectoryCommand(rootFolderList.getSelectedValue().toString()));
            }
        }
        else if(e.getSource() == btnOk){
            controller.executeCommands();
            parent.dispose();
        }
    }
}
