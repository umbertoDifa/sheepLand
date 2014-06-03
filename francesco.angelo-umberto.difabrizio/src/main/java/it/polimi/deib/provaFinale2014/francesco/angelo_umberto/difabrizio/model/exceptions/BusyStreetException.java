package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 * Eccezione sollevata se la strada è gia occupata
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class BusyStreetException extends Exception {

    /**
     * Raised when a street is occupied by a shepherd or a fence
     */
    public BusyStreetException() {
    }

    /**
     * Same as the other constructor but with a message
     *
     * @param message message
     */
    public BusyStreetException(String message) {
        super(message);
    }

}
