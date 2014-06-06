package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.Player;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author
 */
public abstract class TrasmissionController {

    private final Map<String, Player> nick2PlayerMap = new HashMap<String, Player>();

    public void setNick2PlayerMap(String[] nickNames, List<Player> players) {
        for (int i = 0; i < nickNames.length; i++) {
            nick2PlayerMap.put(nickNames[i], players.get(i));
        }
    }

    /**
     * Verifico che il giocatore sia online e non abbia bisogno di refresh
     *
     * @param nickName giocatore di cui controllare lo stato
     *
     * @return true se il giocatore è online e aggiornato, false altrimenti
     */
    protected boolean canPlayerReceive(String nickName) {
        return ServerManager.Nick2ClientProxyMap.get(nickName).isOnline()
                && !ServerManager.Nick2ClientProxyMap.get(nickName).needRefresh();
    }

    public abstract void broadcastStartGame(String nickName);

    public abstract void refreshRegion(String nickName, int regionIndex,
                                       int numbOfSheep, int numbOfRam,
                                       int numbOfLamb);

    public abstract void refreshStreet(String nickName, int streetIndex,
                                       boolean fence,
                                       String nickNameOfShepherdPlayer);

    public abstract void refreshGameParameters(String nickName,
                                               int numbOfPlayers,
                                               int shepherd4player);

    public abstract void refreshCurrentPlayer(String nickName);

    public abstract void refreshCard(String nickName, String card, int value);

    public abstract void refreshSpecialAnimal(SpecialAnimal animal,
                                              String movementResult);

    public abstract void refreshSpecialAnimalInitialPosition(String client,
                                                             SpecialAnimal animal,
                                                             String region);

    public abstract void refreshMoney(String nickName);

    public abstract void refreshPlayerDisconnected(String nickNameDisconnected);

    /**
     * It refreshes to all the player except the nickName player, the action
     * which was made by moving an ovine
     *
     * @param nickName    Player not to refresh
     * @param startRegion Region where the ovine was before moving
     * @param endRegion   Region of the ovine after the move
     * @param ovineType   Type of ovine which was moved
     */
    public abstract void refreshMoveOvine(String nickName, String startRegion,
                                          String endRegion, String ovineType);

    public abstract void refreshMoveShepherd(String nickNameMover,
                                             String shepherdIndex,
                                             String newStreet);

    public abstract void refreshBuyLand(String nickNameBuyer, String boughtLand,
                                        String price) throws RemoteException;

    public abstract void refreshMateSheepWith(String nickName, String region,
                                              String otherType, String newType,
                                              String outcome);

    public abstract void refreshKillOvine(String nickName, String region,
                                          String type, String outcome);

    //ritorna una stringa corripondente o a una strada o il risultato della chiamata RMI
    public abstract boolean askSetUpShepherd(String nickName, int shepherdIndex)
            throws PlayerDisconnectedException;

    //ritorna una stringa corrispondente all'azione scelta, sia per socket che rmi
    public abstract boolean askChooseAction(String nickName,
                                            String possibleActions) throws
            PlayerDisconnectedException;
    
    public abstract void unexpectedEndOfGame();

    protected Map<String, Player> getNick2PlayerMap() {
        return nick2PlayerMap;
    }

    public abstract void sendRank(boolean winner, String nickName, int score);

    public abstract void sendClassification(String classification);
}
