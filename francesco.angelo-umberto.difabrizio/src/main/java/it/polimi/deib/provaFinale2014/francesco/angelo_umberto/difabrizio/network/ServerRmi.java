
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author 
 */
public interface ServerRmi extends Remote {
    public boolean connect(ClientRmi client, String nickName)throws RemoteException;
}
