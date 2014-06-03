package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Portafoglio del pastore
 *
 * @author Francesco
 */
public class Wallet {

    private int amount;

    /**
     * Creates a wallet with a given ammount of money
     *
     * @param amount money to put in the wallet
     */
    public Wallet(int amount) {
        this.amount = amount;
    }

    /**
     *
     * @return Denaro nel portafoglio
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the ammount of money in the waller
     *
     * @param amount Ammount of money to set in the wallet
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
