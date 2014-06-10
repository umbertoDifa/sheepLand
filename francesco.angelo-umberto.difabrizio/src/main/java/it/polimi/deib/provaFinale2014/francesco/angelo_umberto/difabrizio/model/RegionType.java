package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.Random;

/**
 * Enum che contiene i tipi di regione del gioco
 *
 * @author Umberto
 */
public enum RegionType {

    /**
     * Type mountain of the region
     */
    MOUNTAIN(0),
    /**
     * Type hill of the region
     */
    HILL(1),
    /**
     * Type countryside of a region
     */
    COUNTRYSIDE(2),
    /**
     * Type plain of a region
     */
    PLAIN(3),
    /**
     * Type lake of a region
     */
    LAKE(4),
    /**
     * Type desert of a region
     */
    DESERT(5),
    /**
     * Type shepsburg of a region
     */
    SHEEPSBURG(6);

    private final int index;
    //size dell'enum cached così non la ricalco ogni volta
    private static final int SIZE = RegionType.values().length;
    //oggetto random cached
    private static final Random RANDOM = new Random();

    /**
     * Costruisce un tipo di regione corrispondente all'indice passato. L'indice
     * è fondamentale perchè usato nella ricerca delle regioni nella mappa.
     * Infatti viene cercato solo il range in cui ci aspettiamo ti trovare quei
     * tipi di regione.
     *
     * @param index Indice della regione da creare
     */
    RegionType(int index) {
        this.index = index;
    }

    /**
     *
     * @return Il tipo default di regione(montagna)
     */
    protected static RegionType getDefaultRegionType() {
        return MOUNTAIN;
    }

    /**
     * Ottiene l'indice corrispondente ad una regione. Usato per una ricerca più
     * efficente nell'array delle regioni in queanto quest'ultimo è ordinato per
     * tipi di regione secondo gli indici di questa enum
     *
     * @return L'indice corrispondente al territorio
     */
    public int getIndex() {
        return index;
    }
    /**
     * returns a region type given its index
     * @param index the index of the region
     * @return the name of the region if it exist, null if not
     */
    public static String getRegionByIndex(int index){
        for(RegionType type: RegionType.values()){
            if(type.getIndex() == index){
                return type.toString();
            }
        }
        return null;
    }

    /**
     * Restitutisce una regione a caso. Tranne sheepsburg
     *
     * @return Un tipo a caso di regione
     */
    protected static RegionType getRandomRegionType() {
        //prendi un numero a caso appartenente al totale dei valori dell'enum
        int choice = RANDOM.nextInt(SIZE - 1);
        //ritorno la region corrispondente
        return RegionType.values()[choice];
    }

}
