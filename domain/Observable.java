package domain;

/**
 * The interface of an Observable object
 * 
 * @author Sam Verschueren      <sam.verschueren@gmail.com>
 * @since 22/10/2012
 */
public interface Observable {
    
    /**
     * Adds an observer to a collection.
     * 
     * @param o     The observer that has to be added.
     */
    public void addObserver(Observer o);
    
    /**
     * Notify all the observers in the collection with an argument.
     * 
     * @param arg   The argument with which the observers had to be updated.
     */
    public void notifyObservers(Object arg);
}
