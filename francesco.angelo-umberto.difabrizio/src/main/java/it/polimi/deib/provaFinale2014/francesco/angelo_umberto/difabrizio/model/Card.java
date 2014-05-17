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
public class Card {
    private int value;
    private int marketValue;
    private boolean initial; //TODO: serve? forse no perchè le carte iniziali le
                            //crea il GameManager...poi le tratto normalmente...
    private RegionType type;

    public RegionType getType() {
        return type;
    }
    
    public int getValue() {
        return value;
    }

    public Card(int value, RegionType type) {
        this.value = value;
        this.type = type;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(int marketValue) {
        this.marketValue = marketValue;
    }

    public boolean isInitial() { //TODO: come sopra per la var initial
        return initial;
    }

    public void setInitial() {//TODO: tambem come sopra
        this.initial = true;
    }
   
}
