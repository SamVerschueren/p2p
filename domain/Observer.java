package domain;

/**
 * The interface of an observer object.
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 22/10/2012
 */
public interface Observer {
    
    /**
     * The observable object will call this method to update the observer.
     * @param o     The observable object.
     * @param arg   The argument passed by the observable object.
     */
    public void update(Observable o, Object arg);
}
