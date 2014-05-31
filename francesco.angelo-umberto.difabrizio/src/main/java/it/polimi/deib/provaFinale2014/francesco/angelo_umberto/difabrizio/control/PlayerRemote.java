package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Umberto
 */
public interface PlayerRemote extends Remote {

    public String setShepherdRemote(int idShepherd, String stringedStreet) throws RemoteException;
    public String moveShepherdRemote(int shepherdIndex, String newStreet) throws RemoteException;

}
