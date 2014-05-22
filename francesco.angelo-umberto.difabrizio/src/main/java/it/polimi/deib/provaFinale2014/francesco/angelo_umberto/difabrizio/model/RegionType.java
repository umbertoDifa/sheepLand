package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.Random;

/**
 * Enum che contiene i tipi di regione del gioco
 * @author Umberto
 */
public enum RegionType {
	MOUNTAIN(0), HILL(1), COUNTRYSIDE(2), PLAIN(3), LAKE(4), DESERT(5), SHEEPSBURG(6);
        
    private int index;
    //size dell'enum cached così non la ricalco ogni volta
    private static final int size = RegionType.values().length;
    //oggetto random cached
    private static final Random random = new Random();

    /**
     * Costruisce un tipo di regione corrispondente all'indice passato.
     * L'indice è fondamentale perchè usato nella ricerca delle regioni
     * nella mappa. Infatti viene cercato solo il range in cui ci aspettiamo 
     * ti trovare quei tipi di regione.
     * @param index Indice della regione da creare
     */
    RegionType(int index){
    	this.index = index;
    }
    
    /**
     * 
     * @return Il tipo default di regione(montagna)
     */
    public static RegionType getDefaultRegionType(){
        return MOUNTAIN;
    }
    /**
     * Ottiene l'indice corrispondente ad una regione. Usato per una ricerca più
     * efficente nell'array delle regioni in queanto quest'ultimo è ordinato per 
     * tipi di regione secondo gli indici di questa enum
     * @return L'indice corrispondente al territorio
     */
    public int getIndex(){
    	return index;
    }
    /**
     * Restitutisce una regione a caso. Tranne sheepsburg
     * @return Un tipo a caso di regione
     */
    public static RegionType getRandomRegionType() {
        //prendi un numero a caso appartenente al totale dei valori dell'enum
        int choice = random.nextInt(size-1);
        //ritorno la region corrispondente
        return RegionType.values()[choice];
    }

}
