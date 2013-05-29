package domain.listmodel;

import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import domain.UserRepository;

/**
 * Represents the listmodel of our userrepository.
 * 
 * @author Matts Devriendt      <matts.devriendt@gmail.com>
 * @since 20/10/2012
 */
public class UserListModel<E> extends AbstractListModel<E> implements Observer {

    private UserRepository userRepository;
    
    public UserListModel(UserRepository ur) {
        this.userRepository=ur;

        this.userRepository.addObserver(this);
    }
    
    @Override
    public E getElementAt(int index) {
        return (E) userRepository.findAll().get(index);
    }

    @Override
    public int getSize() {
        return this.userRepository.findAll().size();
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        this.fireContentsChanged(this, 0, this.getSize());
    }
}
