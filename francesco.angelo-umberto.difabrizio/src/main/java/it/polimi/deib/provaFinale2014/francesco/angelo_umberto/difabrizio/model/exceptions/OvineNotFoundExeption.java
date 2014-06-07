package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 * No ovine is found in a region
 * @author francesco.angelo-umberto.difabrizio
 */
public class OvineNotFoundExeption extends Exception{

    /**
     * Throws teh exception
     */
    public OvineNotFoundExeption() {
    }
    /**
     * Throws the exceptino with a message
     * @param message 
     */
    public OvineNotFoundExeption(String message) {
        super(message);
    }

}
