
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;


public enum RegionType {
	MOUNTAIN(0), HILL(1), COUNTRYSIDE(2), PLAIN(3), LAKE(4), DESERT(5), SHEEPSBURG(6);
    private int id;
	
    RegionType(int id){
    	this.id = id;
    }
    
    public static RegionType getDefaultRegionType(){
        return MOUNTAIN;
    }
    
    public int getIndex(){
    	return id;
    }

}
