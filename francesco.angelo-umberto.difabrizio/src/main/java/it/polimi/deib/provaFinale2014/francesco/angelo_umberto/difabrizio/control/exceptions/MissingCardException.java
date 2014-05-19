package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions;

/**
 *Eccezione che viene alzata dalla banca se non c'è la carta chiesta
 * @author francesco.angelo-umberto.difabrizio
 */
public class MissingCardException extends  Exception{

    public MissingCardException() {
    }

    public MissingCardException(String message) {
        super(message);
    }

}
