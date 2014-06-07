package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 * If there's no node in an a map
 * @author francesco.angelo-umberto.difabrizio
 */
public class NodeNotFoundException extends MovementException{
    /**
     * Throws the exception
     */
    public NodeNotFoundException() {
    }
    /**
     * Throws the exception with a message
     * @param message 
     */
    public NodeNotFoundException(String message) {
        super(message);
    }

}
