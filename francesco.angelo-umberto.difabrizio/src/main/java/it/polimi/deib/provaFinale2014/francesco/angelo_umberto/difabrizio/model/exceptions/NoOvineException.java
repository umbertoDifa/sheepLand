package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 * No ovine found
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class NoOvineException extends MovementException {

    /**
     * Throws exception
     */
    public NoOvineException() {
    }

    /**
     * Throws exceptiono wtih message
     *
     * @param message message
     */
    public NoOvineException(String message) {
        super(message);
    }

}
