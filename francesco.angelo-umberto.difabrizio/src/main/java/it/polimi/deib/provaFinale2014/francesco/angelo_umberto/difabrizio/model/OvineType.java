package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.Random;

/**
 * Tutti i tipi di ovini possibili.
 *
 * @author Francesco
 */
public enum OvineType {

    /**
     * Type of ovine sheep
     */
    SHEEP,
    /**
     * Type of ovine ram
     */
    RAM,
    /**
     * Type of ovine lamb
     */
    LAMB;
    //size dell'enum cached così non la ricalco ogni volta
    private static final int size = OvineType.values().length;
    //oggetto random cached
    private static final Random random = new Random();

    /**
     *
     * @return Ritorna il tipo default di ovino(pecora)
     */
    protected static OvineType getDefaultOvineType() {
        return SHEEP;
    }

    /**
     *
     * @return OvineType Un tipo a caso di ovino nella enum
     */
    protected static OvineType getRandomOvineType() {
        //prendi un numero a caso appartenente al totale dei valori dell'enum
        int choice = random.nextInt(size);
        //ritorno l'ovino corrispondente
        return OvineType.values()[choice];
    }

    /**
     * It return a ramndom type between the ovineTypes which will be the future
     * one of the lamb. Of course it won't be a lamb type
     *
     * @return A ramndom type for the lamb evolution
     */
    public static OvineType getRandomLambEvolution() {
        int choice;
        do {
            //prendi un numero a caso appartenente al totale dei valori dell'enum
            choice = random.nextInt(size);
            //finchè è uguale a lamb
        } while (OvineType.values()[choice] == OvineType.LAMB);

        return OvineType.values()[choice];
    }
}
