package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * It's the Remote interface of the client to let the server call methods on the
 * client directly
 *
 * @author Umberto
 */
public interface ClientInterfaceRemote extends Remote {

    /**
     * Disconnects the client showing the reason in a message
     *
     * @param message The reason of the disconnection
     *
     * @throws RemoteException If there's a connection problem
     */
    public void disconnect(String message) throws RemoteException;

    /**
     * It allerts the client that the game is gonna start
     *
     * @throws RemoteException If there's a connection problem
     */
    public void welcome() throws RemoteException;

    /**
     * It links the user with is own player so that the client can call the
     * methods of his plaer directly
     *
     * @param player The remote interface of the player that has to be linked
     *               with the user
     *
     * @throws RemoteException If there's a connection problem
     */
    public void connectPlayer(PlayerRemote player) throws RemoteException;

    /**
     * It refresh the status of a given region, carrying information about the
     * amount of animals which are in that region
     *
     * @param regionIndex The region index on the map
     * @param numbOfSheep The number of sheeps in that region
     * @param numbOfRam   The number of rams in that region
     * @param numbOfLamb  The number of lambs in that region
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
                              int numbOfLamb) throws RemoteException;

    /**
     * It refresh the status of a given street , carrying information abount the
     * status of the region which is: occupied by a fence, occupied by a
     * shepherd or free
     *
     * @param streetIndex  The street index on the map
     * @param hasFence     If there's a fence
     * @param nickShepherd The shepherd who's there, null if there's no shepherd
     * @param shepherdIndex
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshStreet(int streetIndex, boolean hasFence,
                              String nickShepherd, int shepherdIndex) throws RemoteException;

    /**
     * It refresh that a given player moved one of his shepherd from a street to
     * an other
     *
     * @param nickNameMover The player who moved the shepherd
     * @param shepherdIndex The shepherd who was moved
     * @param streetIndex   The street where the shepherd is now
     * @param price price to move the shepherd
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshMoveShepherd(String nickNameMover, int shepherdIndex,
                                    String streetIndex, int price) throws RemoteException;

    /**
     * It refresh that a given player moved an ovine from one region to an other
     *
     * @param nickNameMover The player who moved the ovine
     * @param startRegion   The region where the ovine was
     * @param endRegion     The region whre the ovien is now
     * @param ovineType     The type of ovine which was moved
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshMoveOvine(String nickNameMover, String startRegion,
                                 String endRegion, String ovineType) throws
            RemoteException;

    /**
     * Refreshes the number of available fences
     *
     * @param availableFences Fence available
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshAvailableFences(int availableFences) throws
            RemoteException;

    /**
     * Refresh that two ovine mated and a new one is born, or that a player
     * tried to mate two ovines but failed
     *
     * @param nickNameMater The player who tried to mate ovines
     * @param region        The region where the ovine mate
     * @param otherType     The type of ovine which mated with the sheep
     * @param newType       The type which was born
     * @param outcome       If the new ovine was succesfully born or not
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshMateSheepWith(String nickNameMater, String region,
                                     String otherType, String newType,
                                     String outcome) throws
            RemoteException;

    /**
     * Refreshes that an ovine was killed, or that a player tried to kill an
     * ovine but failed
     *
     * @param nickNameKiller The player who tried to kill an ovine
     * @param region         The region where he tried to kill
     * @param type           The type that was killed
     * @param outcome        If the ovine was successfully killed
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshKillOvine(String nickNameKiller, String region,
                                 String type,
                                 String outcome) throws RemoteException;

    /**
     * Refreshes the player that a player bought a land
     *
     * @param nickNameBuyer The player who bought the land
     * @param boughtLand    The land who was bought
     * @param price         The price at which it was bought
     *
     * @throws RemoteException If there is a connection priblem
     */
    public void refreshBuyLand(String nickNameBuyer, String boughtLand,
                               int price) throws RemoteException;

    /**
     * Refresh the game paramters
     *
     * @param nickNames       All the nickNames
     * @param wallets
     * @param shepherd4player the number of shepherd that ech player has
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshGameParameters(String[] nickNames, int[] wallets,
                                      int shepherd4player) throws
            RemoteException;

    /**
     * Refresh who is the player playing now
     *
     * @param currenPlayer The playing player
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refereshCurrentPlayer(String currenPlayer) throws
            RemoteException;

    /**
     * Refreshes the initail position of the blacksheep and the wolf
     *
     * @param position The region in the map
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshSpecialAnimalInitialPosition(String position) throws
            RemoteException;

    /**
     * refershes that a player has gone offline
     *
     * @param player The player taht has gone offlien
     *
     * @throws RemoteException if there's a connection problem
     */
    public void refreshPlayerDisconnected(String player) throws RemoteException;

    /**
     * Refreseh to the player the amount of money he has
     *
     * @param money The amount of money in the wallet
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshMoney(String money) throws RemoteException;

    /**
     * refresh which card was bought and who much it was payed
     *
     * @param type  the type of land bought
     * @param value The price of the card
     *
     * @throws RemoteException If there's a connection problem
     */
    public void refreshCard(String type, int value) throws RemoteException;

    /**
     * Refreshes teh blacksheep movement
     *
     * @param movement The old region, the ew region
     *
     * @throws RemoteException If there's a conneciton problem
     */
    public void refreshBlackSheep(String movement) throws RemoteException;

    /**
     * Refreshes the wolf movements
     *
     * @param regionIndex The old region, the new region, the ovine eaten
     *
     * @throws RemoteException If tehre 's a connection problem
     */
    public void refreshWolf(String regionIndex) throws RemoteException;

    /**
     * Asks teh user to put his shepherd on the map
     *
     * @param idShepherd The shepherd to put on the map
     *
     * @return The street in which it was put
     *
     * @throws RemoteException If there's a connection problem
     */
    public String setUpShepherd(int idShepherd) throws RemoteException;

    /**
     * Asks to the player to choose an action
     *
     * @param actions Tha availale actions
     *
     * @return The action chosen
     *
     * @throws RemoteException
     */
    public String chooseAction(String actions) throws RemoteException;

    /**
     * The rank of the player at the end of teh game
     *
     * @param winner If it was a winner
     * @param rank   The rank of the player
     *
     * @throws RemoteException If There's a conneciton problem
     */
    public void showMyRank(String winner, String rank) throws RemoteException;

    /**
     * The rank of all the players
     *
     * @param classification All teh rankings
     *
     * @throws RemoteException If there's a connection problem
     */
    public void showClassification(String classification) throws RemoteException;

}
