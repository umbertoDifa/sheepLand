package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Umberto
 */
public interface ClientInterfaceRemote extends Remote {

    public void disconnect(String message) throws RemoteException;
}
