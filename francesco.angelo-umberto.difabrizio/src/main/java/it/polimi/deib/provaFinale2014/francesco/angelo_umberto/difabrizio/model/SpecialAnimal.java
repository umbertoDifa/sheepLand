package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.CannotMoveAnimalException;

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
     * Sposta l'animale attraverso una strada verso una regione
     * @param street Strada da attraversare
     * @param endRegion Regione da raggiungere
     * @throws CannotMoveAnimalException Se non è possibile muovere quell'animale
     */
    public void moveThrough(Street street, Region endRegion) throws
            CannotMoveAnimalException {
    }

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
    /**
     * 
     * @return Stringa del nome dell'animale
     */
    @Override
    public abstract String toString();

}
