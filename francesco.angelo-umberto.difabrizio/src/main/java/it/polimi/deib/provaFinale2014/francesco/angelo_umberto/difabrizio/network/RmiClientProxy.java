package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class RmiClientProxy {

    private int gameId;
    private boolean online;
    private final ClientRmi client;

    public RmiClientProxy( ClientRmi client) {
        this.online = true;
        this.client = client;
    }
    
    public void setGameId(int gameId){
        this.gameId = gameId;
    }
    
    public ClientRmi getClientRmi(){
        return client;
    }

}
