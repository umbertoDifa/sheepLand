package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 * When a player disconnect during is own shift
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class TmpPlayerDisconnectedException extends Exception {

    /**
     * Throws the exception
     */
    public TmpPlayerDisconnectedException() {
    }

    /**
     * Throws the exception with a message
     *
     * @param message Message
     */
    public TmpPlayerDisconnectedException(String message) {
        super(message);
    }

}
