package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 * Exception raised if a special animal cannot move
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class CannotMoveAnimalException extends Exception {

    /**
     * Raised when a blackSheep or awolf cannot move
     */
    public CannotMoveAnimalException() {
    }

    /**
     * Same as the other constructor but with a message
     *
     * @param message
     */
    public CannotMoveAnimalException(String message) {
        super(message);
    }

}
