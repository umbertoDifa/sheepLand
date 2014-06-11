package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Costanti del gioco
 *
 * @author Francesco
 */
public enum GameConstants {

    /**
     * Carte comprabili dai giocatori al banco
     */
    /**
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
     * Numero di recinti finali
     */
    NUM_FINAL_FENCES(12),
    NUM_NOT_FINAL_FENCES(5),//FIXME was 20
    /**
     * Numero di recinti, finali + non finali
     */
    NUM_FENCES(NUM_FINAL_FENCES.value + NUM_NOT_FINAL_FENCES.value),
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
     * Numero di azioni che possono essere fatte in un turno da un giocatore
     */
    NUM_ACTIONS(3),
    /**
     * Numero di azioni totali tra cui scegliere
     */
    NUM_TOT_ACTIONS(6),
    /**
     * Valore iniziale del portafoglio quando i giocatori sono più del minimo
     */
    STANDARD_WALLET_AMMOUNT(20),
    /**
     * Il valore del portafoglio quando ci sono pochi giocatori
     */
    LOW_PLAYER_WALLET_AMMOUNT(30),
    /**
     * Il prezzo da pagare per far muovere un pastore su una strada non
     * adiacente a quella di partenza
     */
    PRICE_FOR_SHEPHERD_JUMP(1),
    /**
     * Prezzo per comprare sheepland, è volutamente settato a più del massimo di
     * soldi che un pastore può avere per evitare che la carta possa essere
     * comprata, e allo stesso tempo in futuro potrebbe adattarsi a dei
     * cambiamenti delle regole del gioco
     */
    PRICE_FOR_SHEEPSBURG(
            GameConstants.STANDARD_WALLET_AMMOUNT.getValue() + GameConstants.LOW_PLAYER_WALLET_AMMOUNT.getValue()),
    /**
     * The age when the lamb evolves to ram or sheep
     */
    LAMB_EVOLUTION_AGE(2),
    /**
     * The value of the blacksheep when making the rank and counting the sheeps
     * in the regions
     */
    BLACKSHEEP_WEIGHT(2),
    /**
     * It's the minimum value of the dice so that a shepherd has to pay the
     * other players to be silent in case of killing an ovine
     */
    MIN_DICE_VALUE_TO_PAY_SILENCE(5),
    /**
     * Price that a shepherd has to pay in order to get the silence of an other
     * player
     */
    PRICE_OF_SILENCE(2);

    private final int value;

    GameConstants(int value) {
        this.value = value;
    }

    /**
     * @return Valore corrispondente alla costante in questione
     */
    public int getValue() {
        return this.value;
    }
}
