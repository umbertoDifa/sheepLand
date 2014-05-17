package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

public class Region extends Node {
    final private RegionType type;
   
    public Region(){
    	super();
        this.type = RegionType.getDefaultRegionType();
    }
    
    public Region(RegionType type){
    	super();
        this.type = type;
    }

	public RegionType getType() {
		return type;
	}
}
