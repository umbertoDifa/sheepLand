package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class RmiClientProxy {

    private int gameId;
    private boolean online;
    private final ClientInterfaceRemote client;

    public RmiClientProxy( ClientInterfaceRemote client) {
        this.online = true;
        this.client = client;
    }
    
    public void setGameId(int gameId){
        this.gameId = gameId;
    }
    
    public ClientInterfaceRemote getClientRmi(){
        return client;
    }

}
