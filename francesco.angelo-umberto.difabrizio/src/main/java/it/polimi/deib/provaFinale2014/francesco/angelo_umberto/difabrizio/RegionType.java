/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio;

/**
 *
 * @author Francesco
 */
public enum RegionType {
    MOUNTAIN, HILL, COUNTRYSIDE, PLAIN, LAKE, DESERT, SHEEPSBURG;
    
    static RegionType getDefaultRegionType(){
        return MOUNTAIN;
    }
}
