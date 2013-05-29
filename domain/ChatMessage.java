package domain;

import java.io.Serializable;
import java.util.Date;

/**
 * This is a chatmessage that can be send by a <em>User</em>.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 11/08/2012
 */
public class ChatMessage implements Serializable, Comparable<ChatMessage> {

    private String message;
    private Date date;
    private User user;
    
    /**
     * Creates a new instance of the chatmessage.
     * 
     * @param message       The message.
     * @param user          The user who sended the message.
     */
    public ChatMessage(String message, User user) {
        this(message, user, new Date());
    }
    
    /**
     * Creates a new instance of the chatmessage.
     *  
     * @param message       The message.
     * @param user          The user who sended the message.
     * @param date          The time on which the user has sended the message.
     */
    public ChatMessage(String message, User user, Date date) {
        this.setMessage(message);
        this.setUser(user);
        this.setDate(date);
    }
    
    /**
     * Sets the message attribute.
     * 
     * @param message       The message.
     */
    private void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Returns the value message attribute.
     * 
     * @return message      The message.
     */
    public String getMessage() {
        return this.message;
    }
    
    /**
     * Sets the user who sended the message.
     * 
     * @param user          The user who sended the message.
     */
    private void setUser(User user) {
        this.user = user;
    }
    
    /**
     * Returns the user who send the message.
     * 
     * @return user         The user who send the message.
     */
    public User getUser() {
        return this.user;    
    }
    
    /**
     * Sets the time on which the message has been posted.
     * 
     * @param date          The time
     */
    private void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * Returns the time on which the message has been posted.
     * 
     * @return date         The time
     */
    public Date getDate() {
        return this.date;
    }
    
    /**
     * Returns the formatted message.
     * 
     * @return String       formatted message
     */
    @Override
    public String toString() {
        return String.format("%s: %s", this.user, this.message);
    }

    /**
     * Compares the messages on the date.
     * 
     * @param o             The ChatMessages that needs to be compared.
     * @return int          Look at the javadocs of Object for the explanation.
     */
    @Override
    public int compareTo(ChatMessage o) {
        return o.date.compareTo(this.date);
    }
}
