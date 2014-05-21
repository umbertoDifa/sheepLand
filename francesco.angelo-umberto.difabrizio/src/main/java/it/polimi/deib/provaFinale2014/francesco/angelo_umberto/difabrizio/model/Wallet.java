package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Portafoglio del pastore
 *
 * @author Francesco
 */
public class Wallet {

    private int amount;

    public Wallet() {
        amount = GameConstants.INITIAL_WALLET_AMMOUNT.getValue();
    }

    /**
     *
     * @return Denaro nel portafoglio
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Setta il denaro nel portafoglio
     *
     * @param amount Valore da settare
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public void pay(int prize){
        this.amount -= prize; 
    }

}
