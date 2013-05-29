package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import domain.DomainController;

/**
 * Screen where you have to enter a username.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @author Matts Devriendt      <matts.devriendt@gmail.com>
 */
public class Application extends JFrame implements ActionListener {

    private DomainController controller;
    
    private JLabel lblNameProgram;
    private JTextField txtUserName;
    private JButton btnStart;
    
    /**
     * Creates a new instance of the frame.
     */
    /**
     * @param controller
     */
    public Application(DomainController controller) {
        super("Sheep2peehS - Login");
        
        this.controller = controller;
        
        this.init();
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setSize(300, 300);
    }
    
    /**
     * Initializes the gui components.
     */
    private void init() {  
        lblNameProgram = new JLabel("Sheep2peehS");
        lblNameProgram.setFont(new Font("Arial",Font.PLAIN,23));
        lblNameProgram.setForeground(Color.DARK_GRAY);
        
        txtUserName = new JTextField(10);
        txtUserName.addActionListener(this);
        
        btnStart = new JButton("Start");
        btnStart.addActionListener(this);
        
        JPanel pnlNameProgram = new JPanel(new FlowLayout());
        pnlNameProgram.add(lblNameProgram);

        JPanel pnlLogin = new JPanel(new FlowLayout());
        pnlLogin.add(new JLabel("Username:"));
        pnlLogin.add(txtUserName);
        
        JPanel pnlButton = new JPanel(new FlowLayout());
        pnlButton.add(pnlLogin);
        pnlButton.add(btnStart);
        
        add(pnlNameProgram, BorderLayout.NORTH);
        add(new ImagePanel("/assets/sheep1.png"), BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);   
    }

    /**
     * the username is sent to the controller,
     * the login-screen disappears and the main-frame is started (you get the joke? main-frame => overview-frame)
     * 
     * @param e     the actionevent (automatic)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == txtUserName || e.getSource() == btnStart) {
            try {
                controller.login(txtUserName.getText());

                this.dispose();
                
                Overview overview = new Overview(controller);
                overview.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                overview.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        // When the window is closed, remove user from the network
                        controller.exit();
                        
                        System.exit(0);
                    }
                });
            }
            catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
