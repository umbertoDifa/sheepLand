package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Umberto
 */
public interface PlayerRemote extends Remote {

    public void moveOvine(String type, int startRegionIndex, int endRegionIndex)
            throws RemoteException;

    public void moveShepherd(String nickName, int idShepherd, int streetIndex)
            throws RemoteException;

    public void setUpShepherd(String nickName, int idShepherd, int streetIndex);

    public void buyLand() throws RemoteException, ActionCancelledException;

    public void mateSheepWith(OvineType otherOvineType) throws RemoteException,
                                                               ActionCancelledException;

    public void killOvine() throws RemoteException;
}
