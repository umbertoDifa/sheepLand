package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class CannotMoveWolfException extends CannotMoveAnimalException{
    /**
     * Throws cannot move wolf exception
     */
    public CannotMoveWolfException() {
    }
    /**
     * Throws the exception with a message
     * @param message message
     */
    public CannotMoveWolfException(String message) {
        super(message);
    }

}
