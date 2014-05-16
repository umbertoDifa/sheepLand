/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 *
 * @author Francesco
 */
public enum GameConstants {
    TOT_CARDS(36), TOT_FENCES(32), NUM_REGIONS_FOR_TYPE(3), NUM_STREETS(42), NUM_REGIONS(19);
    private final int value;
    

    GameConstants(int value){
        this.value = value;
    }
    
    /**
     * @return valore corrispondente alla costante di inizializzazione del gioco
     */
    public int getValue(){
        return this.value;
    }
}
