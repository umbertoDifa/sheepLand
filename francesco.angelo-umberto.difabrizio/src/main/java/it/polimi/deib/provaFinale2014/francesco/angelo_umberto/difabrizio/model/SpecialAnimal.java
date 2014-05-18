package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public abstract class SpecialAnimal {
    private Region myRegion;
    public void setAt(Region region){};
    public void moveThroughTo(Street street,Region endRegion){};
    public Region getMyRegion() {
        return myRegion;
    }

    public void setMyRegion(Region myRegion) {
        this.myRegion = myRegion;
    }
    
}
