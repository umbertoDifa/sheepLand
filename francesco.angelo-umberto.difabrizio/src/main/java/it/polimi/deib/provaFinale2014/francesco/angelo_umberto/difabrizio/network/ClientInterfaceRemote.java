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

    public void refreshMoveShepherd(String nickNameMover, String shepherdIndex,
                                    String streetIndex) throws RemoteException;

    public void refreshMoveOvine(String nickNameMover, String startRegion,
                                 String endRegion, String ovineType) throws
            RemoteException;

    public void refreshMateSheepWith(String nickNameMater, String region,
                                     String otherType, String newType,
                                     String outcome) throws
            RemoteException;

    public void refreshKillOvine(String nickNameKiller, String region,
                                 String type,
                                 String outcome) throws RemoteException;

    public void refreshBuyLand(String nickNameBuyer, String boughtLand,
                               String price) throws RemoteException;

    public void refreshGameParameters(int numbOfPlayers, String firstPlayer,
                                      int shepherd4player) throws
            RemoteException;

    public void refereshCurrentPlayer(String currenPlayer) throws
            RemoteException;

    public void refreshMoney(String money) throws RemoteException;

    public void refreshCard(String type, int value) throws RemoteException;

    public void refreshBlackSheep(String regionIndex) throws RemoteException;

    public void refreshWolf(String regionIndex) throws RemoteException;

    public String setUpShepherd(int idShepherd) throws RemoteException;

    public String chooseAction(String actions) throws RemoteException;

    public void showMyRank(String winner, String rank) throws RemoteException;

    public void showClassification(String classification) throws RemoteException;

}
