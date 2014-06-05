package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NoOvineException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lupo
 *
 * @author Umberto
 */
public class Wolf extends SpecialAnimal {

    /**
     * Given a street and an end region the wolf tries to move to that region
     * following the rules of the game. If in the end region there exists at
     * least one sheep the wolf eats it.
     *
     * @param street    Strada da attraversare
     * @param endRegion Regione da raggiungere
     *
     * @return If the wolf can easly move "Il lupo si muove,[type of ovine
     *         eaten]"; if the wolf jumps the fence"Il lupo ha saltato la
     *         recinzione ,[type of ovine eaten]"; if the wolf cannot move "Il
     *         lupo tenta di muoversi ma la strada è sbarrata!"; type of ovine
     *         eaten can be null
     */
    @Override
    public String moveThrough(Street street, Region endRegion) {
        String result;
        String fenceJump;
        //cerco di far muover il lupo
        //se la strada non è sbarrata o tutta la regione è sbarrata
        if (!street.hasFence()) {
            this.setAt(endRegion);
            result = "ok";
            fenceJump = "nok";
        } else if (super.getMyRegion().isAllFenced()) {
            DebugLogger.println("Il lupo ha saltato la recinzione!");
            this.setAt(endRegion);
            result = "ok";
            fenceJump = "ok";
        } else {
            //la strada è sbarrata ma altre sono libere
            return "nok";

        }

        try {
            //se la regione d'arrivo ha pecore mangiane una
            return result + "," + fenceJump + "," + this.eatOvine(endRegion);
        } catch (NoOvineException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return result + "," + fenceJump + "," + "nok";
        }

    }

    /**
     * If there is at least one sheep, the wolf eats it
     *
     * @param region Region where the wolf has to eat
     */
    private String eatOvine(Region region) throws NoOvineException {

        DebugLogger.println(
                "Tentativo di mangiare un tipo: " + OvineType.SHEEP);
        region.removeOvine(OvineType.SHEEP);
        //se tutto va bene ritorna l'ovino mangiato
        //in questo caso è sempre sheep ma può essere facilmente cambiato 
        //considerando che removeOvine ritorna l'ovino rimosso
        return "sheep";

    }

    @Override
    public String toString() {
        return "Wolf";
    }

}
