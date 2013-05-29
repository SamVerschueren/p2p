package ui;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import domain.DomainController;
import domain.Observable;
import domain.Observer;

/**
 * The panel with a JList where you can see all the users.
 * 
 * @author Matts Devriendt      <matts.devriendt@gmail.com>
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 21/10/2012
 */
public class UserPanel extends JPanel implements MouseListener, Observable {
    
    private DomainController controller;
    private JList<Object> lstUsers;
    
    private List<Observer> observers = new ArrayList<Observer>();
    
    /**
     * Creates a new instance of the panel.
     * 
     * @param controller        The domain controller.
     */
    public UserPanel(DomainController controller) {
        this.controller = controller;
        
        this.init();
    }
    
    /**
     * Initializes the gui components.
     */
    private void init(){
        lstUsers = new JList<Object>(controller.getUserListModel());
        lstUsers.addMouseListener(this);
        
        setLayout(new BorderLayout());
        
        add(new JScrollPane(lstUsers),BorderLayout.CENTER);
    }

    
    @Override
    public void mouseClicked(MouseEvent e) {
        // If the user clicked twice
        if(e.getClickCount() == 2) {
            String user = lstUsers.getSelectedValue().toString();
            
            try {
                controller.tcpConnect(user);
                controller.downloadXml(user);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this.getParent(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            this.notifyObservers(user);
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

    @Override
    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObservers(Object arg) {
        for(Observer o:this.observers) {
            o.update(this, arg);
        }
    }
}
