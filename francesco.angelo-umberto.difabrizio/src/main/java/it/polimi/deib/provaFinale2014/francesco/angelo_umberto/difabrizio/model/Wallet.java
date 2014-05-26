package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Portafoglio del pastore
 *
 * @author Francesco
 */
public class Wallet {

    private int amount;

    public Wallet(int amount){
        this.amount = amount;
    }
    
     public Wallet() {
        this(GameConstants.STANDARD_WALLET_AMMOUNT.getValue());
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
    
    /**
     * Il pastore paga un certo price che viene detratto dal suo portafoglio
     * NB. attenzione il metodo non controlla se il pastore può permettersi di
     * pagare
     * @param price 
     */
    
    //TODO si potrebbe includere il controllo sui soldi disponibili in questo metodo
    //che se non può pagare restituisce un exception piuttosto che gestire ogni volta
    //le if-else sul pagamento
    public void pay(int price){
        this.amount -= price; 
    }

}
