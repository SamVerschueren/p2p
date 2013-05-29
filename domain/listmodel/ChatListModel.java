package domain.listmodel;

import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import domain.Chat;

/**
 * Represents the ListModel of the chat
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since ??/10/2012
 */
public class ChatListModel<E> extends AbstractListModel<E> implements Observer {

    private Chat chat;
    
    public ChatListModel(Chat chat) {
        this.chat = chat;

        this.chat.addObserver(this);
    }
    
    @Override
    public E getElementAt(int index) {
        return (E) chat.getMessages().get(index);
    }

    @Override
    public int getSize() {
        return chat.getMessages().size();
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        this.fireIntervalAdded(this, this.getSize()-1, this.getSize()-1);
    }
}
