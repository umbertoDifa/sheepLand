package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class RegionData {

    int sheep;
    int ram;
    int lamb;

    public void clear() {
        sheep = 0;
        ram = 0;
        lamb = 0;
    }

    public void addSheep() {
        sheep++;
    }

    public void addRam() {
        ram++;
    }

    public void addLamb() {
        lamb++;
    }

}
