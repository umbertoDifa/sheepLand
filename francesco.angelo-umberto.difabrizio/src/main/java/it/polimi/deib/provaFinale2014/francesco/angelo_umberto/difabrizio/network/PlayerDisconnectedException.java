package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 * When a player disconnect during is own shift
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class PlayerDisconnectedException extends Exception {

    /**
     * Throws the exception
     */
    public PlayerDisconnectedException() {
    }

    /**
     * Throws the exception with a message
     *
     * @param message Message
     */
    public PlayerDisconnectedException(String message) {
        super(message);
    }

}
