package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 * Movement doesn go alright
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class MovementException extends Exception {

    /**
     * Throws movemetn exception
     */
    public MovementException() {
    }

    /**
     * Throws movement exceptino with message
     *
     * @param message
     */
    public MovementException(String message) {
        super(message);
    }

}
