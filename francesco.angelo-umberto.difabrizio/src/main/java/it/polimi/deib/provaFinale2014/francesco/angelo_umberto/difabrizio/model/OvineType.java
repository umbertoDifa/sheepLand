package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.Random;

/**
 * Tutti i tipi di ovini possibili.
 * @author Francesco
 */
public enum OvineType {

    SHEEP, RAM, LAMB;

    private static final int size = OvineType.values().length; //size dell'enum cached così non la ricalco ogni volta
    private static final Random random = new Random(); //oggetto random cached
    
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
        int choice = random.nextInt(size); //prendi un numero a caso appartenente al totale dei valori dell'enum
        return OvineType.values()[choice]; //ritorno l'ovino corrispondente
    }
}
