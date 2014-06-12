package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Market {
    List<List<Card>> group;

    public Market(int playersNumber) {
        group = new ArrayList<List<Card>>(playersNumber);
    }
    
}
