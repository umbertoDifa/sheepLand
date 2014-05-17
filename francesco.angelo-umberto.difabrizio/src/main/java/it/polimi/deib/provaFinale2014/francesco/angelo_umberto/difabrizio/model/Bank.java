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
public class Bank {
    private Card[] unusedCards;
    private Fence[] unusedFences;

    public Bank(int numCards, int numeFences) {
        this.unusedCards = new Card[numCards];
        this.unusedFences = new Fence[numeFences];
    }

    public Card[] getUnusedCards() {
        return unusedCards;
    }
    
    public Card getCard(RegionType type) throws Exception{
    	String errorMessage = "non ci sono più carte per il tipo"+type.toString();
    	for( int i=type.getIndex()*GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue();
    			i<(type.getIndex()+1)*GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++){
    		if(this.unusedCards[i] != null){
    			Card foundedCard = this.unusedCards[i];
    			this.unusedCards[i] = null;
    			return foundedCard;
    		}
    	}
    	throw new Exception(errorMessage);
    }

    public void loadFence(){
    }
    
    public void loadFinalFence(){
    }
    
    public Fence[] getUnusedFences() {
        return unusedFences;
    }

}
