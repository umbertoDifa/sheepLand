package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.Player;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author
 */
public abstract class TrasmissionController {

    private final HashMap<String, Player> nick2PlayerMap = new HashMap<String, Player>();

    public void setNick2PlayerMap(String[] nickNames, List<Player> players) {
        for (int i = 0; i < nickNames.length; i++) {
            nick2PlayerMap.put(nickNames[i], players.get(i));
        }
    }

    public abstract void broadcastStartGame() throws RemoteException;

    public abstract void refreshRegion(String nickName, int regionIndex,
                                       int numbOfSheep, int numbOfRam,
                                       int numbOfLamb) throws RemoteException;

    public abstract void refreshStreet(String nickName, int streetIndex,
                                       boolean fence,
                                       String nickNameOfShepherdPlayer) throws
            RemoteException;

    public abstract void refreshGameParameters(String nickName,
                                               int numbOfPlayers,
                                               int shepherd4player) throws
            RemoteException;

    public abstract void refreshCurrentPlayer(String nickName) throws
            RemoteException;

    public abstract void refreshCard(String nickName, String card, int value)
            throws RemoteException;

    public abstract void refreshSpecialAnimal(SpecialAnimal animal,String movementResult) throws RemoteException;
    
    public abstract void refreshMoney(String nickName) throws RemoteException;

    /**
     * It refreshes to all the player except the nickName player, the action
     * which was made by moving an ovine
     * @param nickName Player not to refresh
     * @param startRegion Region where the ovine was before moving
     * @param endRegion Region of the ovine after the move
     * @param ovineType Type of ovine which was moved
     * @throws RemoteException 
     */
    public abstract void refreshMoveOvine(String nickName, String startRegion,
                                          String endRegion, String ovineType)
            throws RemoteException;

    public abstract void refreshMoveShepherd(String nickNameMover,
                                             String shepherdIndex,
                                             String newStreet) throws
            RemoteException;
    public abstract void refreshBuyLand(String nickNameBuyer, String boughtLand,String price) throws RemoteException;
    
    public abstract void refreshMateSheepWith(String nickName, String region, String otherType, String newType,String outcome) throws RemoteException;

    public abstract void refreshKillOvine(String nickName,String region,String type,String outcome) throws
            RemoteException;

    //ritorna una stringa corripondente o a una strada o il risultato della chiamata RMI
    public abstract boolean askSetUpShepherd(String nickName, int shepherdIndex)
            throws RemoteException;

    //ritorna una stringa corrispondente all'azione scelta, sia per socket che rmi
    public abstract boolean askChooseAction(String nickName,
                                            String possibleActions) throws
            RemoteException;

    //ritorna una stringa con regione di partenza e di arrivo o risultato RMI !!!!TORNA DUE
    public abstract boolean askMoveOvine(String nickName) throws RemoteException;

    //ritorna una stringa corrispondente a una strada o risultato RMI
    public abstract boolean askMoveSheperd(String nickName) throws
            RemoteException;

    //ritorna una stringa corrispondente al tipo di regione
    public abstract boolean askBuyLand(String nickName) throws RemoteException;

    //ritorna stringa corrispondente a Regione e Tipo di ovino
    public abstract boolean askKillOvine(String nickName) throws RemoteException;

    //ritorna una stringa corrispondente alla regione e Tipo di ovino
    public abstract boolean askMateSheepWith(String nickName,String type) throws
            RemoteException;

    public abstract void askThrowDice(String nickName) throws RemoteException;

    public abstract void refreshInfo(String nickName, String info) throws
            RemoteException;

    public HashMap<String, Player> getNick2PlayerMap() {
        return nick2PlayerMap;
    }

    public abstract void sendRank(boolean winner, String nickName, int score) throws RemoteException;
    
    public abstract void sendClassification(String classification) throws RemoteException;
}
