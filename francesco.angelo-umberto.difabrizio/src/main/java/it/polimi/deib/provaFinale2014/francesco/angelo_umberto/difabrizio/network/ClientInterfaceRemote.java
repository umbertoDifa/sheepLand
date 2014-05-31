package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.PlayerRemote;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Umberto
 */
public interface ClientInterfaceRemote extends Remote {

    public void disconnect(String message) throws RemoteException;

    public void welcome() throws RemoteException;
    
    public void connectPlayer(PlayerRemote player) throws RemoteException;

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
                              int numbOfLamb) throws RemoteException;

    public void refreshStreet(int streetIndex, boolean Fence,
                              String nickShepherd) throws RemoteException;
    
    public void refreshMoveShepherd(String nickNameMover,String shepherdIndex, String streetIndex) throws RemoteException;

    public void refreshGameParameters(int numbOfPlayers, String firstPlayer,
                                      int shepherd4player) throws
            RemoteException;

    public void refereshCurrentPlayer(String currenPlayer) throws
            RemoteException;

    public void refereshCard(String type, int value) throws RemoteException;

    public void refreshBlackSheep(int regionIndex) throws RemoteException;

    public void refreshWolf(int regionIndex) throws RemoteException;

    public String setUpShepherd(int idShepherd) throws RemoteException;

    public void chooseAction(String actions) throws RemoteException;

    public boolean moveShepherd() throws RemoteException;
}
