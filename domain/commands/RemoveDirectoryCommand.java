package domain.commands;

import domain.filesystem.Directory;
import domain.filesystem.XmlFileRoot;
import domain.listmodel.RootFolderListModel;

public class RemoveDirectoryCommand implements ICommand {
    private String dirname;
    private Directory dir;

    public RemoveDirectoryCommand(String dirname) {
        this.dirname = dirname;
    }

    @Override
    public void executeCommand(Object arg) {
        XmlFileRoot root = (XmlFileRoot) arg;
        root.removeDirectory(dirname);
    }

    @Override
    public void executeListCommand(Object arg) {
        if (arg instanceof RootFolderListModel<?>) {
            RootFolderListModel<Object> model = (RootFolderListModel<Object>) arg;
            dir = model.removeItem(dirname);
        }
    }
    
    @Override
    public void undoListCommand(Object arg){
        if(arg instanceof RootFolderListModel<?>){
            RootFolderListModel<Object> model = (RootFolderListModel<Object>) arg;
            model.addItem(dir);
        }
    }

}
