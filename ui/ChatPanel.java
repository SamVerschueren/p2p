package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import ui.components.JPlaceHolderTextField;

import domain.DomainController;

public class ChatPanel extends JPanel implements ActionListener {

    private DomainController controller;
    
    private JList<Object> lstChatMessages;
    private JPlaceHolderTextField txtMessage;
    private JButton btnSend;
    
    private JScrollPane scroll;
    
    /**
     * Creates a new instance of the panel.
     */
    public ChatPanel(DomainController controller) {
        this.controller = controller;
        
        this.init();
        
        this.setVisible(true);
    }
    
    /**
     * Initializes the gui components.
     */
    private void init() {
        lstChatMessages = new JList<Object>(controller.getChatListModel());
        lstChatMessages.setCellRenderer(new MessageCellRendering());
        
        txtMessage = new JPlaceHolderTextField();
        txtMessage.setPlaceHolder("Send your message...");
        txtMessage.addActionListener(this);
        
        btnSend = new JButton("Send");
        btnSend.addActionListener(this);
        
        JPanel pnlSend = new JPanel(new BorderLayout());
        pnlSend.add(txtMessage, BorderLayout.CENTER);
        pnlSend.add(btnSend, BorderLayout.EAST);
        
        scroll = new JScrollPane(lstChatMessages);
        scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

			/**
			 * Makes sure the scrollbar sticky's to the bottom of the page. Very nice!
			 */
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				lstChatMessages.ensureIndexIsVisible(lstChatMessages.getModel().getSize()-1);
			}   
        });
        
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(pnlSend, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == txtMessage || e.getSource() == btnSend) {
            String message = txtMessage.getText();
            
            if (message.length()>0)
                controller.sendMessage(message);
  
            txtMessage.setText("");
        }
    }
}
