package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author
 */
public interface ServerRmi extends Remote {
    /**
     * It's the remote method called by the client to connect itself to the server
     * @param client The client remote interface
     * @param nickName Nick name of the client
     * @return true if the client could connect, false if not
     * @throws RemoteException if there's a connection problem
     */
    public boolean connect(ClientInterfaceRemote client, String nickName) throws
            RemoteException;
}
