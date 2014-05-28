package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class GameData {

    private final int numberOfPlayers;
    private final int numberOfShepherds;
    private final String firstPlayer;

    public GameData(int numberOfPlayers, int numberOfShepherds,
                    String firstPlayer) {
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfShepherds = numberOfShepherds;
        this.firstPlayer = firstPlayer;
    }

}
