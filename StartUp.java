import javax.swing.JFrame;
import javax.swing.UIManager;

import ui.Application;
import domain.DomainController;

public class StartUp {
    /**
     * Let the magic begin
     * 
     * @author Bart
     * @author Sam
     * @author Matts
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Could not set the look and feel
            e.printStackTrace();
        } 
        
        DomainController controller = new DomainController();
        
        Application application = new Application(controller);
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
