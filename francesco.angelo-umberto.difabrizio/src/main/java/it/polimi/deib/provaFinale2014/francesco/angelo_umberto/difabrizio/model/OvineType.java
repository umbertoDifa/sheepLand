package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.Random;

/**
 * Tutti i tipi di ovini possibili.
 *
 * @author Francesco
 */
public enum OvineType {

    SHEEP, RAM, LAMB;
    //size dell'enum cached così non la ricalco ogni volta
    private static final int size = OvineType.values().length;
    //oggetto random cached
    private static final Random random = new Random();

    /**
     *
     * @return Ritorna il tipo default di ovino(pecora)
     */
    static OvineType getDefaultOvineType() {
        return SHEEP;
    }

    /**
     *
     * @return OvineType Un tipo a caso di ovino nella enum
     */
    static OvineType getRandomOvineType() {
        //prendi un numero a caso appartenente al totale dei valori dell'enum
        int choice = random.nextInt(size);
        //ritorno l'ovino corrispondente
        return OvineType.values()[choice];
    }
}
