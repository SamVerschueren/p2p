package domain.network;

/**
 * These are all the actions that can be done over the network.
 * 
 * @author Sam Verschueren		<sam.verschueren@gmail.com>
 * @since ...
 */
public enum NetworkAction {
    DELETE_USER,
    NEW_USER,
    
    CHAT_MESSAGE,
    
    REQUEST_XML,
    RESPONSE_XML,
    
    REQUEST_FILE,
    RESPONSE_FILE,
    
    RESPONSE
}
