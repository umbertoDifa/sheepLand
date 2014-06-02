package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.CannotMoveAnimalException;

/**
 * Rappresenta un animale speciale quindi una pecora nera o un lupo.
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public abstract class SpecialAnimal {

    private Region myRegion;
    
    /**
     * Imposta la regione dell'animale speciale in region
     * @param region Regione in cui spostare l'animale
     */
    public void setAt(Region region) {
        setMyRegion(region);
    }

    /**
     * Moves the animal through a street to the endRegion
     * @param street Strada da attraversare
     * @param endRegion Regione da raggiungere
     * @return Depends on the implementations
     */
    public abstract String moveThrough(Street street, Region endRegion);

    /**
     * 
     * @return La regione dell'animale speciale
     */
    public Region getMyRegion() {
        return myRegion;
    }
    /**
     * Imposta la regione dell'animale speciale a myRegion
     * @param myRegion Regione su cui settare la posizione dell'animale
     */
    public void setMyRegion(Region myRegion) {
        this.myRegion = myRegion;
    }       

}
