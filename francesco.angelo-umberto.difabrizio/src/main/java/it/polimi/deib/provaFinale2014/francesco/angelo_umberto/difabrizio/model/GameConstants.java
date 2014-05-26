package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Costanti del gioco
 * @author Francesco
 */
public enum GameConstants {
    /**
     * Carte comprabili dai giocatori al banco
     *//**
     * Carte comprabili dai giocatori al banco
     */
    NUM_CARDS(30),
    /**
     * Carte iniziali da poter distribuire ai giocatori
     */
    NUM_INITIAL_CARDS(6),
    /**
     * Carte per ogni tipo di regione
     */
    NUM_CARDS_FOR_REGION_TYPE(5), 
    /**
     * Numero di recinti, finali + non finali
     */
    NUM_FENCES(32), 
    /**
     * Numero di regioni per ogni tipo di terreno
     */
    NUM_REGIONS_FOR_TYPE(3), 
    /**
     * Numero totale di strade
     */
    NUM_STREETS(42), 
    /**
     * Numero di regioni, compresa shepsburg
     */
    NUM_REGIONS(19), 
    /**
     * Numero di recinti finali
     */
    NUM_FINAL_FENCES(12), 
    /**
     * Numero di azioni che possono essere fatte in un turno 
     * da un giocatore
     */
    NUM_ACTIONS(3), 
    /**
     * Valore iniziale del portafoglio quando i giocatori sono più
     * del minimo
     */
    STANDARD_WALLET_AMMOUNT(20),
    /**
     * Il valore del portafoglio quando ci sono pochi giocatori
     */
    LOW_PLAYER_WALLET_AMMOUNT(30);
    
    private final int value;
    

    GameConstants(int value){
        this.value = value;
    }
    
    /**
     * @return Valore corrispondente alla costante in questione
     */
    public int getValue(){
        return this.value;
    }
}
