package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta il pastore di un giocatore. Gestisce il portafoglio e conosce in
 * quale strada si trova. Conserva le carte acquistate.
 *
 * @author Francesco
 */
public class Shepherd {

    private Street street;
    private Wallet wallet;
    private List<Card> myCards = new ArrayList<Card>();
    
    public Shepherd(int walletAmount){
        wallet = new Wallet(walletAmount);
    }
    
    public Shepherd(Wallet wallet){
        this.wallet = wallet;
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

    public List<Card> getMyCards() {
        return myCards;
    }

    public void setMyCards(List<Card> myCards) {
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
    
    public boolean ifPossiblePay(int price) {
        //se puoi pagare
        if (wallet.getAmount() >= price) {
            wallet.setAmount(wallet.getAmount() - price);
            return true;
        }
        return false;
    }

}
