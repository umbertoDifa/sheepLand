package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Classe pecora nera.
 *
 * @author Umberto
 */
public class BlackSheep extends SpecialAnimal {

    /**
     * Se la street è libera la attraversa per raggiungere la endRegion
     *
     * @param street    Strada da attraversare
     * @param endRegion Regione in cui arrivare
     *
     * @return Returns "ok" if the blacksheep could move, "nok" if it could not
     *         move
     */
    @Override
    public String moveThrough(Street street, Region endRegion) {
        //se la strada è libera 
        if (street.isFree()) {
            //muovi la pecora nera
            this.setAt(endRegion);
            return "ok";
        } else {
            return "nok";

        }

    }
    
    @Override
    public String toString(){
        return "BlackSheep";
    }
}
