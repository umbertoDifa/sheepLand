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
    private Card[] UnusedCards;
    private Fence[] UnusedFences;

    public Bank() {
        this.UnusedCards = new Card[GameConstants.TOT_CARDS.getValue()];
        this.UnusedFences = new Fence[GameConstants.TOT_FENCES.getValue()];
    }

    public Card[] getUnusedCards() {
        return UnusedCards;
    }

    public Fence[] getUnusedFences() {
        return UnusedFences;
    }

}
