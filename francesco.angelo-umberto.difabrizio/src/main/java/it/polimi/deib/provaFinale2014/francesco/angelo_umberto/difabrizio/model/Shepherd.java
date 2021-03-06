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
    private final Wallet wallet;
    private List<Card> myCards;

    /**
     * create shepherd with a wallet with a specified amount and istanciate his
     * list of cards
     *
     * @param walletAmount
     */
    public Shepherd(int walletAmount) {
        wallet = new Wallet(walletAmount);
        myCards = new ArrayList<Card>();
    }

    /**
     * create shepherd and connect it with a precise wallet and a precise list
     * of cards. It's used to share cards and wallet of others shepherds
     *
     * @param wallet
     * @param cards
     */
    public Shepherd(Wallet wallet, List<Card> cards) {
        this.wallet = wallet;
        this.myCards = cards;
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
     *
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

    /**
     * Return the list of cards of a shepherd
     *
     * @return The list of card of teh shephderd
     */
    public List<Card> getMyCards() {
        return myCards;
    }

    /**
     * The number of cards of a certain region type
     *
     * @param chosenType Type of region
     *
     * @return Number of cards of that type
     */
    public int numOfMyCardsOfType(RegionType chosenType) {
        int sum = 0;
        for (Card card : this.myCards) {
            if (card.getType() == chosenType) {
                sum++;
            }
        }
        return sum;
    }

    /**
     * It sets the cards of the shepherd
     *
     * @param myCards List of card to give to the shepherd
     */
    public void setMyCards(List<Card> myCards) {
        this.myCards = myCards;
    }

    /**
     * Aggiunge una carta terreno a quelle del pastore
     *
     * @param card Carta da aggiungere
     */
    public void addCard(Card card) {
        this.myCards.add(card);
    }

    /**
     * Rimuove una carta precisa da quelle appartenenti al pastore
     *
     * @param card Carta da rimuovere
     */
    public void removeCard(Card card) {
        this.myCards.remove(card);
    }

    /**
     * The method pays the given price if the shepherd has enough money, it does
     * nothing if the shepherd has not enough money
     *
     * @param price Price to pay
     *
     * @return True if paid, false if not
     */
    public boolean ifPossiblePay(int price) {
        //se puoi pagare
        if (wallet.getAmount() >= price) {
            wallet.setAmount(wallet.getAmount() - price);
            return true;
        }
        return false;
    }

    /**
     * Add money to the shepherd wallet
     *
     * @param money
     */
    public void earnMoney(int money) {
        wallet.setAmount(money + wallet.getAmount());
    }

}
