package ui.components;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import ui.TransferInfoPanel;
import java.util.Observable;
import java.util.Observer;

public class ProgressComponent extends JPanel implements Observer {
	
    private JLabel lblInfo;
    private JProgressBar progressBar;
    private TransferInfoPanel transferInfoPanel;
    private long totalFileSize;
    
    public ProgressComponent(Observable observable, String info,
            TransferInfoPanel transferInfoPanel) {
        observable.addObserver(this);
        this.transferInfoPanel = transferInfoPanel;
        lblInfo = new JLabel(info);

        progressBar = new JProgressBar();
        progressBar.setString("0 %");
        progressBar.setStringPainted(true);
        
        setLayout(new BorderLayout());

        add(lblInfo, BorderLayout.WEST);
        add(progressBar, BorderLayout.CENTER);
        setVisible(true);
        repaint();
    }

    public void setTotalFileSize(long fileSize){
    	this.totalFileSize = fileSize;
    }
    
    @Override
    public void update(Observable o, Object arg) {
    	int progress = Math.min(100, (int) arg);
    	if (progress == 100) {
            transferInfoPanel.remove(this);
            transferInfoPanel.repaint();
        }
        progressBar.setValue(progress);
        progressBar.setString(progress + " %");
        repaint();
    }
    
    
}