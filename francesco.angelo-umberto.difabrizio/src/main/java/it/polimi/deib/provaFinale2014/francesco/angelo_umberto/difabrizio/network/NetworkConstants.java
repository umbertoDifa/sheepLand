package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public enum NetworkConstants {

    PORT_RMI(6000),
    PORT_SOCKET(5050),
    OFFLINE(0),
    ONLINE(1),
    /**
     * Player has 10 seconds to reconnect during is own turn
     */
    TIMEOUT_PLAYER_RECONNECTION(15000),
    /**
     * How many times can the player disconnect in his own turn without losing
     * his shift
     */
    MAX_NUMBER_OF_DISCONNETIONS(2),
    MILLISECONDS_IN_SECONDS(1000),
    /**
     * Default seconds to wait before timeout the clients connection to a game
     */
    DEFAULT_TIMEOUT_ACCEPT(15),
    /**
     * The default minimum number of clients for a game
     */
    DEFAULT_MIN_CLIENTS_FOR_GAME(2),
    /**
     * The default maximum number of clients for a game
     */
    DEFAULT_MAX_CLIENTS_FOR_GAME(4),
    /**
     * The maximum number of games that a server can activate simultaniusly
     */
    DEFAULT_MAX_GAMES(10);

    private final int value;

    private NetworkConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
