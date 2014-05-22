package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;

/**
 * Rappresenta il pastore di un giocatore. Gestisce il portafoglio e conosce in
 * quale strada si trova. Conserva le carte acquistate.
 *
 * @author Francesco
 */
public class Shepherd {

    private Street street;
    private Wallet wallet;
    private ArrayList<Card> myCards = new ArrayList<Card>();

    public Shepherd() {
        wallet = new Wallet();
    }

    /**
     * Modifica la strada che occupa il pastore. privato in quanto è invocato da
     * moveTo
     *
     * @param street Strada in cui spostarsi
     */
    private void setStreet(Street street) {
        this.street = street;
    }
    /**
     * Sposta il pastore nella strada passata come parametro
     * @param street Strada su cui spostare il pastore
     */
    public void moveTo(Street street) {
        //setto il pastore su street
        this.setStreet(street);
        
        //occupo la street
        street.setShepherd(this);
    }
    /**
     * 
     * @return Il portafoglio del pastore
     */
    public Wallet getWallet() {
        return wallet;
    }
    
    /**
     * 
     * @return La strada in cui si trova il pastore
     */
    public Street getStreet() {
        return street;
    }

    public ArrayList<Card> getMyCards() {
        return myCards;
    }

    public void setMyCards(ArrayList<Card> myCards) {
        this.myCards = myCards;
    }
    
    /**
     * Aggiunge una carta terreno a quelle del pastore
     * @param card Carta da aggiungere
     */
    public void addCard(Card card) {
        this.myCards.add(card);
    }
    /**
     * Rimuove una carta precisa da quelle appartenenti al pastore
     * @param card Carta da rimuovere
     */
    public void removeCard(Card card) {
        this.myCards.remove(card);
    }
    
    public void setWallet(Wallet wallet){
        this.wallet = wallet;
    }

}
