/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;

/**
 *
 * @author Francesco
 */
public class Map {
    //private ArrayList<Node> map;
    private Node[] streets;
    private Node[] regions;
    
    public Map() {
        //this.map = new ArrayList<Node>();
        this.streets = new Node[GameConstants.NUM_STREETS.getValue()];
        this.regions = new Node[GameConstants.NUM_REGIONS.getValue()];
    }
    
    private void addNode(Node node){

    }
    
  //TODO: hardcode mapping create node+create links
    public void setUpMap(){
        
//        for(int i=0; i<GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++)
//            map.add(new Region(RegionType.MOUNTAIN));
//        for(int i=0; i<GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++)
//            map.add(new Region(RegionType.COUNTRYSIDE));
//        for(int i=0; i<GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++)
//            map.add(new Region(RegionType.DESERT));
//        for(int i=0; i<GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++)
//            map.add(new Region(RegionType.HILL));
//        for(int i=0; i<GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++)
//            map.add(new Region(RegionType.LAKE));
//        for(int i=0; i<GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++)
//            map.add(new Region(RegionType.PLAIN));
//        map.add(new Region(RegionType.SHEEPSBURG));
        for(int i=0; i<GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++)
            this.regions[i] = new Region(RegionType.MOUNTAIN);
        
        this.regions[0].connectTo(this.streets[0]);
    }
}
