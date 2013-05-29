package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import domain.network.NetworkObject;

/**
 * This is the Chatbox where our user can chat in with eachother. Chatting is done with
 * multicast so no TCP/IP connections are necessary for this.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 11/10/2012
 */
public class Chat extends Observable implements Observer {

    private List<ChatMessage> messages;
    
    /**
     * Creates a new instance of a chat.
     */
    public Chat() {
        this.messages = new ArrayList<ChatMessage>();
    }
    
    /**
     * Adds a new message to the chat.
     * 
     * @param message       The message that needs to be added.
     */
    public void addMessage(ChatMessage message) {
        this.messages.add(message);
        
        this.setChanged();
        this.notifyObservers();
    }
    
    /**
     * Returns all the chatmessages.
     * 
     * @return messages     All the chatmessages.
     */
    public List<ChatMessage> getMessages() {
        return this.messages;
    }

    @Override
    public void update(Observable observable, Object object) {
        if(object instanceof NetworkObject) {
            NetworkObject no = (NetworkObject)object;
            
            if(no.getObject() instanceof ChatMessage) {
                ChatMessage message = (ChatMessage)no.getObject();
                
                this.addMessage(message);
            }
        }
    }
}
