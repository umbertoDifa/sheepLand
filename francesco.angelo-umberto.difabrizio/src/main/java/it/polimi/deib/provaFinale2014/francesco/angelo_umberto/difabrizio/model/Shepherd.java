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
public class Shepherd {
    private Street street;
    private Wallet wallet;
    private ArrayList<Card> myCards = new ArrayList<Card>();
    
    public Shepherd(){
        wallet = new Wallet();
    }
/**
 * Modifica la strada che occupa il pastore. privato in quanto è invocato da moveTo
 * @param street 
 */
    private void setStreet(Street street) {
        this.street = street;
    }
    
    public void moveTo(Street street){
        this.setStreet(street);
    }
    
	public Wallet getWallet() {
		return wallet;
	}

	public Street getStreet() {
		return street;
	}
	
	public void addCard(Card card){
		this.myCards.add(card);
	}
    
}
