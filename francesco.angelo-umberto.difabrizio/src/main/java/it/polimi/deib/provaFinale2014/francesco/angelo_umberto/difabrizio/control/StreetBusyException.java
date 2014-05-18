
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Francesco
 */
public class StreetBusyException extends InvocationTargetException{

    public StreetBusyException() {
    }

    public StreetBusyException(Throwable target, String s) {
        super(target, s);
    }
    
    
}
