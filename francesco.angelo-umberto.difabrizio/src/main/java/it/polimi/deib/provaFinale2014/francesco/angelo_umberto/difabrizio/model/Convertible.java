
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NodeNotFoundException;

/**
 *
 * @author Umberto
 */
public interface Convertible {
    public Object convert (String stringToConvert) throws NodeNotFoundException;
}
