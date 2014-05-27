package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Portafoglio del pastore
 *
 * @author Francesco
 */
public class Wallet {

    private int amount;

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

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
