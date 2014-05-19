package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.CannotMoveAnimalException;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public abstract class SpecialAnimal {
    private Region myRegion;
    public void setAt(Region region){};
    public void moveThrough(Street street, Region endRegion) throws CannotMoveAnimalException{};
    public Region getMyRegion() {
        return myRegion;
    }

    public void setMyRegion(Region myRegion) {
        this.myRegion = myRegion;
    }
    
}
