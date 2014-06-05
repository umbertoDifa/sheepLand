package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public enum ControlConstants {

    /**
     * Indica quanti pastori hanno i giocatori nel caso di un gioco con pochi
     * giocatori
     */
    SHEPHERD_FOR_FEW_PLAYERS(2),
    /**
     * Indica da quanti giocatori è composto una partita piccola, nel quale ogni
     * player ha diritto a SHEPHERD_FOR_FEW_PLAYERS pastori
     */
    NUM_FEW_PLAYERS(2),
    /**
     * Indica quanti pastori ha ogni giocatore durante un gioco normale
     */
    STANDARD_SHEPHERD_FOR_PLAYER(1),
    /**
     * Player has 10 seconds to reconnect during is own turn
     */
    TIMEOUT_PLAYER_RECONNECTION(15000),
    /**
     * How many times can the player disconnect in his own turn without losing
     * his shift
     */
    MAX_NUMBER_OF_DISCONNETIONS(2);
    private final int value;

    ControlConstants(int value) {
        this.value = value;
    }

    /**
     * @return Valore corrispondente alla costante in questione
     */
    public int getValue() {
        return this.value;
    }
}
