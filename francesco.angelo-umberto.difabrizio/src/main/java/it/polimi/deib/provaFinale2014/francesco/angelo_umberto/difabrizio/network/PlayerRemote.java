package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Umberto
 */
public interface PlayerRemote extends Remote {

    /**
     * Interface to call the setShepherd method
     *
     * @param idShepherd
     * @param stringedStreet
     *
     * @return
     *
     * @throws RemoteException
     */
    String setShepherdRemote(int idShepherd, String stringedStreet)
            throws RemoteException;

    /**
     * Interface to call the moveShepherd method
     *
     * @param shepherdIndex
     * @param newStreet
     *
     * @return
     *
     * @throws RemoteException
     */
    String moveShepherdRemote(String shepherdIndex, String newStreet)
            throws
            RemoteException;

    /**
     * Interface to calle the moveOvine method
     *
     * @param startRegion
     * @param endRegion
     * @param type
     *
     * @return
     *
     * @throws RemoteException
     */
    String moveOvineRemote(String startRegion, String endRegion,
            String type) throws RemoteException;

    /**
     * Interface to call the buyLand method
     *
     * @param regionType
     *
     * @return
     *
     * @throws RemoteException
     */
    String buyLandRemote(String regionType) throws RemoteException;

    /**
     * Interface to call mateSheepWith method of the player
     *
     * @param shepherdNumber
     * @param regionToMate
     * @param otherOvineType
     * @return
     * @throws RemoteException
     */
    String mateSheepWithRemote(String shepherdNumber, String regionToMate,
            String otherOvineType) throws RemoteException;

    /**
     * Interface to call the method killOvineRemote of player
     *
     * @param shepherdNumber
     * @param region
     * @param typeToKill
     * @return
     * @throws RemoteException
     */
    String killOvineRemote(String shepherdNumber, String region,
            String typeToKill) throws RemoteException;

    /**
     * Interface to calle the putCardInMarket of the player
     *
     * @param card
     * @param price
     * @throws RemoteException
     */
    void putCardInMarketRemote(String card, int price) throws RemoteException;

    /**
     * Interface to call teh sasme method in player
     *
     * @param card card to buy
     * @throws RemoteException
     */
    void payCardFromMarketRemote(String card) throws RemoteException;
}
