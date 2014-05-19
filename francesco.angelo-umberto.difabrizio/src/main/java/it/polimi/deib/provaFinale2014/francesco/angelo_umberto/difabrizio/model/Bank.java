/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.MissingCardException;

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

    /**
     * Cerca una carta del tipo specificato nell'array delle carte disponibili e
     * la ritorna se esiste, altrimenti solleva un eccezione se le carte sono
     * finite
     *
     * @param type Tipo di carta voluto
     *
     * @return una Card del tipo chiesto
     *
     * @throws Exception Se la carta non c'è
     */
    public Card getCard(RegionType type) throws MissingCardException {
        String missingCardMessage = "Non ci sono più carte per il tipo " + type.toString(); //preparo un messaggio di errore
        //l'algoritmo che segue a come idea di indicizzare l'array
        //le carte vengono caricate nell'array di quelle a disposizione della 
        //banca in maniera ordinata
        for (int i = type.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue();
                i < (type.getIndex() + 1) * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++) {
            if (this.unusedCards[i] != null) {
                Card foundedCard = this.unusedCards[i];
                this.unusedCards[i] = null;
                return foundedCard;
            }
        }
        throw new MissingCardException(missingCardMessage);
    }

    public Fence getFence() {
        //TODO eccezione qui o in numberOfUsedFence?
        //salvo il primo recinto che ho
        int position = this.numberOfUsedFence();
        if (position >= 0 && position < this.unusedFences.length) { //se l'indice ha senso
            Fence availableFence = unusedFences[this.numberOfUsedFence()]; //salva il recinto
            unusedFences[this.numberOfUsedFence()] = null; //annulla il riferimento nell'arrray
            return availableFence;//ritorna il recinto
        } else {
         throw new FenceFinishedException("Non ci sono più recinti");
        }
    }

    /**
     * Crea un recinto non finale e lo aggiunge all'array nella posizione
     * specificata
     *
     * @param position
     */
    public void loadFence(int position) {
        unusedFences[position] = new Fence(false);
    }

    /**
     * Crea un recinto finale e lo aggiunge all'array nella posizione
     * specificata
     *
     * @param position
     */
    public void loadFinalFence(int position) {
        unusedFences[position] = new Fence(true);
    }

    /**
     * Riceve una card e la posiziona dove richiesto nell'array delle carte non
     * usate(del banco)
     *
     * @param card     Carta da caricare
     * @param position Posizione in cui caricarla
     */
    public void loadCard(Card card, int position) {
        unusedCards[position] = card;
    }

    /**
     * Restitutisce il numero di recinti ceduti dalla banca
     *
     * @return Il numero di recinti usati
     */
    public int numberOfUsedFence() {
        int i; //serve a contare quanti recinti sono stati dati
        for (i = 0; i < unusedFences.length; i++) { //scorri l'array
            if (unusedFences[i] != null) //al primo recinto disponibile               
            {
                return i;         //restituisci quanti ne sono stati dati        }            
            }
        }
        return -1; //unico caso tutti i recinti dati...male male
    }

}
