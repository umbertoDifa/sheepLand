package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions;

/**
 * It's thrown when al player disconnect from a game before finishing it
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class UnexpectedEndOfGameException extends Exception {

    /**
     * Throws the exception
     */
    public UnexpectedEndOfGameException() {
    }

    /**
     * Throws the exception with a message
     *
     * @param message
     */
    public UnexpectedEndOfGameException(String message) {
        super(message);
    }

}
