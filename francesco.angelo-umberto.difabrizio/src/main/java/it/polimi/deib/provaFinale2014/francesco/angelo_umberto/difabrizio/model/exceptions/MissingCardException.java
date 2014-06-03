package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 *Eccezione che viene alzata dalla banca se non c'è la carta chiesta
 * @author francesco.angelo-umberto.difabrizio
 */
public class MissingCardException extends  Exception{
    /**
     * throws the exception
     */
    public MissingCardException() {
    }
    /**
     * throws the exceptino with a message
     * @param message message
     */
    public MissingCardException(String message) {
        super(message);
    }

}
