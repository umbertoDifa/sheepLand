package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class RmiClientProxy {

    private final int gameNumber;
    private int status;
    private final ClientRmi client;

    public RmiClientProxy(int gameNumber, ClientRmi client, int status) {
        this.gameNumber = gameNumber;
        this.status = status;
        this.client = client;
    }

}
