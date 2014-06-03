package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions;

/**
 * Eccezione sollevata se finiscono i recinti
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class FinishedFencesException extends Exception {

    /**
     * It is raised when there are no more fence to put on the streets
     */
    public FinishedFencesException() {
    }

    /**
     * The same as the one without the message, but this one also carry a
     * message
     *
     * @param message
     */
    public FinishedFencesException(String message) {
        super(message);
    }

}
