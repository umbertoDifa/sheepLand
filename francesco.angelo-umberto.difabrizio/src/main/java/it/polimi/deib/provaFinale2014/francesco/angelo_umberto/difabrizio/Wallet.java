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
public class Wallet {
    private int amount;
    private final int INITIAL_AMOUNT = 20;
            
    public Wallet(){
        amount = INITIAL_AMOUNT;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
}
