package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 * A street is not found in the map
 *
 * @author Umberto
 */
public class StreetNotFoundException extends NodeNotFoundException {

    /**
     * Throws the exception
     */
    public StreetNotFoundException() {
    }

    /**
     * Throws the exception with a message
     *
     * @param message
     */
    public StreetNotFoundException(String message) {
        super(message);
    }

}
