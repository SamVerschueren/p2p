package domain.filesystem;

import java.io.IOException;

import javax.swing.JOptionPane;

import persistence.PersistenceController;

public class HashingTask implements Runnable {
    private Md5Generator generator;
    private XmlFileRoot root;
    private PersistenceController persistenceController;

    public HashingTask(Md5Generator generator, XmlFileRoot root,
            PersistenceController persistenceController) {
        this.generator = generator;
        this.root = root;
        this.persistenceController = persistenceController;
    }

    @Override
    public void run() {
        root.generateMd5(generator);
        
        try {            
            generator.taskSavingFile();
			persistenceController.createXml(root);
			generator.taskCompleted();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error hashing files");
		}
    }
}
