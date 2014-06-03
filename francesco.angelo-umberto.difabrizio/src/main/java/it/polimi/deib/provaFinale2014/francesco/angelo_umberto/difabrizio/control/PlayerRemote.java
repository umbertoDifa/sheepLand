package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

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
    public String setShepherdRemote(int idShepherd, String stringedStreet)
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
    public String moveShepherdRemote(String shepherdIndex, String newStreet)
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
    public String moveOvineRemote(String startRegion, String endRegion,
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
    public String buyLandRemote(String regionType) throws RemoteException;
}
