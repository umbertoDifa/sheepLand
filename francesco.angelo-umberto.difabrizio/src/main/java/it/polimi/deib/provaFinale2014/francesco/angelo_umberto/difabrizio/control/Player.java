package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Player {
    private final Shepherd shepherd;

    public Player() {
        this.shepherd = new Shepherd();
    }

    public Shepherd getShepherd() {
        return shepherd;
    }
}
