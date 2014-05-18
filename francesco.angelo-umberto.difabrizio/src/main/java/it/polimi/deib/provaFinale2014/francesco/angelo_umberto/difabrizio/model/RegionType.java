
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.Random;


public enum RegionType {
	MOUNTAIN(0), HILL(1), COUNTRYSIDE(2), PLAIN(3), LAKE(4), DESERT(5), SHEEPSBURG(6);
        
    private int index;
    private static final int size = RegionType.values().length; //size dell'enum cached così non la ricalco ogni volta
    private static final Random random = new Random(); //oggetto random cached

    
    RegionType(int index){
    	this.index = index;
    }
    
    public static RegionType getDefaultRegionType(){
        return MOUNTAIN;
    }
    
    public int getIndex(){
    	return index;
    }
    
    public static RegionType getRandomRegionType() {
        int choice = random.nextInt(size); //prendi un numero a caso appartenente al totale dei valori dell'enum
        return RegionType.values()[choice]; //ritorno la region corrispondente
    }

}
