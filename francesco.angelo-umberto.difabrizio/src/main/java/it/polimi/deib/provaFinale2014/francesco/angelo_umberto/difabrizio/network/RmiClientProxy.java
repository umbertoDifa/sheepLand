package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class RmiClientProxy extends ClientProxy{

    private final ClientInterfaceRemote client;

    public RmiClientProxy(ClientInterfaceRemote client) {
        this.client = client;        
    }

    public ClientInterfaceRemote getClientRmi() {
        return client;
    }

}
