package domain.commands;

import domain.filesystem.Directory;
import domain.filesystem.XmlFileRoot;
import domain.listmodel.RootFolderListModel;

public class AddDirectoryCommand implements ICommand {
    private Directory dir;
    
    public AddDirectoryCommand(String path){
        dir = new Directory(path);
    }
    
    @Override
    public void executeCommand(Object arg) {
        XmlFileRoot root = (XmlFileRoot)arg;
        root.addDirectory(dir);
    }
    
    @Override
    public void executeListCommand(Object arg) {
        if (arg instanceof RootFolderListModel<?>) {
            RootFolderListModel<Object> model = (RootFolderListModel<Object>) arg;
            model.addItem(dir);
        }
    }
    
    @Override
    public void undoListCommand(Object arg){
        if(arg instanceof RootFolderListModel<?>){
            RootFolderListModel<Object> model = (RootFolderListModel<Object>) arg;
            model.removeItem(dir);
        }
    }
}
