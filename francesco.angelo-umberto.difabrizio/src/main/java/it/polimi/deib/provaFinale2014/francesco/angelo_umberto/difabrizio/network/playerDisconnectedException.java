package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 * When a player disconnect during is own shift
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class playerDisconnectedException extends Exception {

    /**
     * Throws the exception
     */
    public playerDisconnectedException() {
    }

    /**
     * Throws the exception with a message
     *
     * @param message Message
     */
    public playerDisconnectedException(String message) {
        super(message);
    }

}
