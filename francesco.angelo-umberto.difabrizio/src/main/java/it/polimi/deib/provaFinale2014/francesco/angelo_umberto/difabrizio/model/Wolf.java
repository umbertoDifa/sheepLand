package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.CannotMoveWolfException;
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
     * Data una strada e una regione d'arrivo il lupo tenta di muoversi nella
     * regione secondo le regole del gioco. Inoltre se nella regione d'arrivo
     * esiste almeno una pecora il lupo la mangia.
     *
     * @param street    Strada da attraversare
     * @param endRegion Regione da raggiungere
     *
     * @throws CannotMoveWolfException Se la strada è sbarrata ma ce ne sono
     *                                 altre aperte nella regione di partenza
     *                                 del lupo
     */
    @Override
    public void moveThrough(Street street, Region endRegion) throws
            CannotMoveWolfException {

        //cerco di far muover il lupo
        //se la strada non è sbarrata o tutta la regione è sbarrata
        if (!street.hasFence()) {
            this.setAt(endRegion);
        } else if (super.getMyRegion().isAllFenced()) {
            DebugLogger.println("Il lupo ha saltato la recinzione!");
            this.setAt(endRegion);
        } else {
            //la strada è sbarrata ma altre sono libere
            throw new CannotMoveWolfException(
                    "La strada è sbarrata il lupo non può passare");
        }

    //se la regione d'arrivo ha pecore mangiane una
        this.eatOvine(endRegion);

    }

    /**
     * se c è almeno una pecora, ne mangia una
     *
     * @param region
     */
    private void eatOvine(Region region) {
        try {
            OvineType type = OvineType.getRandomOvineType();
            DebugLogger.println("Tentativo di mangiare un tipo: " + type);
            region.removeOvine(type);

        } catch (NoOvineException ex) {
            //non la mangiare perchè non c'è
            Logger.getLogger(Wolf.class
                    .getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
