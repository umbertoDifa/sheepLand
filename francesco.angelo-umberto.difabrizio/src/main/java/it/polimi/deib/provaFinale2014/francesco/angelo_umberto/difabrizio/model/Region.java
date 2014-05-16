
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;

public class Region extends Node {
    final private RegionType type;
   
    public Region(){
        this.type = RegionType.getDefaultRegionType();
    }
    
    public Region(RegionType type){
        this.type = type;
    }
    
    
}
