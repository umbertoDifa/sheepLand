package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class RmiClientProxy extends ClientProxy {

    private final ClientInterfaceRemote client;

    /**
     * Creates a proxy for the rmi client
     *
     * @param client The remote interface of the client that will be used to
     *               call the rmi methods on the client
     */
    public RmiClientProxy(ClientInterfaceRemote client) {
        this.client = client;
    }

    /**
     * Returns the remote interface to call the remote methods on the client
     *
     * @return The client remote interface
     */
    public ClientInterfaceRemote getClientRmi() {
        return client;
    }

}
