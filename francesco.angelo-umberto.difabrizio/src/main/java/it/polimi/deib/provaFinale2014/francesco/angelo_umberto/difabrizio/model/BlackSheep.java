package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.CannotMoveBlackSheepException;

/**
 * Classe pecora nera.
 * @author Umberto
 */
public class BlackSheep extends SpecialAnimal {
    /**
     * Se la street è libera la attraversa per raggiungere la endRegion
     * @param street Strada da attraversare
     * @param endRegion Regione in cui arrivare 
     * @throws CannotMoveBlackSheepException Se la strada è occupata da un pastore
     * o da un recinto
     */
    @Override
    public void moveThrough(Street street, Region endRegion) throws
            CannotMoveBlackSheepException {
        //se la strada è libera 
        if (street.isFree()) {
            //muovi la pecora nera
            this.setAt(endRegion);
        } else {
            throw new CannotMoveBlackSheepException(
                    "La pecora nera non può muoversi, strada occupata.");
    
        }
    }   
    
}
