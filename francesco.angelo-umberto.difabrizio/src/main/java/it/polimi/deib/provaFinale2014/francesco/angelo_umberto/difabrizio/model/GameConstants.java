package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Costanti del gioco
 * @author Francesco
 */
public enum GameConstants {
    NUM_CARDS(36),NUM_INITIAL_CARDS(6), NUM_CARDS_FOR_REGION_TYPE(5), NUM_FENCES(32), 
    NUM_REGIONS_FOR_TYPE(3), NUM_STREETS(42), NUM_REGIONS(19), 
    NUM_FINAL_FENCES(12), NUM_ACTIONS(3), INITIAL_WALLET_AMMOUNT(20);
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
