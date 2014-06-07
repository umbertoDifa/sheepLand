package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions;

/**
 * A region is not found in the map
 *
 * @author Umberto
 */
public class RegionNotFoundException extends NodeNotFoundException {

    /**
     * Throws the exception
     */
    public RegionNotFoundException() {
    }

    /**
     * Throws the excepiton with a message
     *
     * @param message
     */
    public RegionNotFoundException(String message) {
        super(message);
    }

}
