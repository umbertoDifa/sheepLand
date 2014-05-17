package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;

public class Region extends Node {
    final private RegionType type;
    private ArrayList<Ovine> myOvines = new ArrayList<Ovine>();
   
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

    public ArrayList<Ovine> getMyOvines() {
        return myOvines;
    }
    
    public void addOvine(Ovine ovine){
        this.myOvines.add(ovine);
    }
}
