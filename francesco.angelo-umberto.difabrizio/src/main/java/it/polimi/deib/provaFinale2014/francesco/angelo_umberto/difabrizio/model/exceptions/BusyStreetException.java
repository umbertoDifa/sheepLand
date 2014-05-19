package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 *Eccezione sollevata se la strada è gia occupata
 * @author francesco.angelo-umberto.difabrizio
 */
public class BusyStreetException extends Exception{

    public BusyStreetException() {
    }

    public BusyStreetException(String message) {
        super(message);
    }

}
