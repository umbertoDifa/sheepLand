package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 * A Shepherd is not found in a street
 * @author francesco.angelo-umberto.difabrizio
 */
public class ShepherdNotFoundException extends Exception{
    
    /**
     * throws the exception
     */
    public ShepherdNotFoundException() {
    }
    /**
     * Throws the exceptino with a message
     * @param message 
     */
    public ShepherdNotFoundException(String message) {
        super(message);
    }

}
