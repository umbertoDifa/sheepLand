package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public interface RemoteClientRmi extends Remote {

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
                              int numbOfLamb) throws RemoteException;

    public void refreshStreet(int streetIndex, boolean Fence,
                              String nickShepherd) throws RemoteException;

    public void refereshGameParameters(int numbOfPlayers, String firstPlayer,
                                       int shepherd4player) throws
            RemoteException;

    public void refereshCurrentPlayer(String currenPlayer) throws
            RemoteException;

    public void refereshCards(List<String> myCards) throws RemoteException;

    public void refreshBlackSheep(int regionIndex) throws RemoteException;

    public void refreshWolf(int regionIndex) throws RemoteException;

    public void setUpShepherds() throws RemoteException;

    public void chooseAction() throws RemoteException;

    public void moveOvine() throws RemoteException;

    public void refreshMoveOvine(int startRegionIndex, int endRegionIndex,
                                 String type) throws RemoteException;

    public void moveShepherd() throws RemoteException;

    public void refreshMoveShepherd() throws RemoteException;

    public void buyLand() throws RemoteException;

    public void mateSheepWith() throws RemoteException;

    public void refreshMateSheepWith(int regionIndex, String ovineType) throws
            RemoteException;

    public void killOvine() throws RemoteException;

    public void refreshKillOvine(int regionIndex) throws RemoteException;
    
    public void connectRmiTrasmission(RmiTrasmission rmiTrasmission);

}
