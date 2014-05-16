/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio;

import java.util.ArrayList;

/**
 *
 * @author Francesco
 */
public class Region extends Node {
    final private RegionType TYPE;
   
    public Region(){
        this.TYPE = RegionType.getDefaultRegionType();
    }
    
    public Region(RegionType type){
        this.TYPE = type;
    }

}
