package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 * If cannot move black sheep
 *
 * @author Umberto
 */
public class CannotMoveBlackSheepException extends CannotMoveAnimalException {

    /**
     * Constructor to throw the exception
     */
    public CannotMoveBlackSheepException() {
    }

    /**
     * Throws the exception with a message
     *
     * @param message
     */
    public CannotMoveBlackSheepException(String message) {
        super(message);
    }

}
