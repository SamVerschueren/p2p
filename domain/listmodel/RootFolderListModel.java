package domain.listmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import domain.filesystem.Directory;
import domain.filesystem.XmlFileRoot;

/**
 * Represents the ListModel of the rootfolder
 * 
 * @author Sam Verschueren <sam.verschueren@gmail.com>
 * @param <E>
 * @since 30/10/2012
 */
public class RootFolderListModel<E> extends AbstractListModel<E> implements
        Observer {
    private List<Directory> directories;

    public RootFolderListModel(XmlFileRoot root) {
        this.directories = new ArrayList<>(root.getDirectories());

        // this.chat.addObserver(this);
    }

    @Override
    public E getElementAt(int index) {
        return (E) directories.get(index);
    }

    @Override
    public int getSize() {
        return directories.size();
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        // this.fireContentsChanged(this, 0, this.getSize());
    }

    public void addItem(Directory dir) {
        directories.add(dir);
        fireContentsChanged(dir, 0, this.getSize());
    }

    public Directory removeItem(String dirname) {
        Directory dir = null, tempDir;
        int count = 0;

        while (dir == null && count < directories.size()) {
            tempDir = directories.get(count);
            if (tempDir.getName().equals(dirname))
                dir = tempDir;
            count++;
        }
        if (dir != null) {
            removeItem(dir);
        }
        return dir;
    }
    
    public void removeItem(Directory dir){
        directories.remove(dir);
        fireContentsChanged(dir, 0, this.getSize());
    }
}
