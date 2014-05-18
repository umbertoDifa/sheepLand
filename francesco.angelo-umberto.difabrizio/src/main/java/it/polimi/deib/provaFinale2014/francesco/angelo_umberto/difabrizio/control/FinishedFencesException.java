package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

/**
 * Eccezione sollevata se finiscono i recinti
 * @author francesco.angelo-umberto.difabrizio
 */
public class FinishedFencesException extends Exception{

    public FinishedFencesException() {
    }

    public FinishedFencesException(String message) {
        super(message);
    }
    
}
