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
public class Fence {
    private boolean finalFence;
    
    public boolean isFinal(){
        return finalFence;
    }
    
    public void setFinal(){
        finalFence = true;
    }
}
