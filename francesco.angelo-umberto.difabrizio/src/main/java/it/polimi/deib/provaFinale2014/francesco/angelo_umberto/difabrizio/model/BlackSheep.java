package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.CannotMoveBlackSheepException;

public class BlackSheep extends SpecialAnimal {
    
    @Override
    public void setAt(Region region) {
        super.setMyRegion(region);
    }
    
    @Override
    public void moveThrough(Street street, Region endRegion) throws
            CannotMoveBlackSheepException {
        //se la strada è libera 
        if (street.isFree()) {
            this.setAt(endRegion);//muovi la pecora nera
        } else {
            throw new CannotMoveBlackSheepException(
                    "La pecora nera non può muoversi, strada occupata.");
    
        }
    }
    
}
