package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.Player;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * It's the controller of all the transmission between server and client. It's
 * used by the server to send and receive message.
 *
 * @author
 */
public abstract class TrasmissionController {

    private final Map<String, Player> nick2PlayerMap = new HashMap<String, Player>();

    /**
     * Sets the map that contains the couple nickName->player so that the
     * trasmissionController can loop around players when refreshing the new
     * conditions of the game. NickNames and players must be in the same ordered
     * so that they're linked right
     *
     * @param nickNames The array of nicknames of a given game
     * @param players   The list of players of that game
     */
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

    /**
     * Refreshes to the client that the game has started
     *
     * @param nickName The client to refresh
     */
    public abstract void refreshStartGame(String nickName);

    /**
     * Refresh the the client the status of a region
     *
     * @param nickName    The client to refresh
     * @param regionIndex The region of the map
     * @param numbOfSheep The number of sheep in the region
     * @param numbOfRam   the number of rams int the region
     * @param numbOfLamb  The number of lamb in the region
     */
    public abstract void refreshRegion(String nickName, int regionIndex,
                                       int numbOfSheep, int numbOfRam,
                                       int numbOfLamb);

    /**
     * Refresh to the client the status of a street
     *
     * @param nickName                 The client to refresh
     * @param streetIndex              The street to refresh
     * @param fence                    If the street has a fence on it
     * @param nickNameOfShepherdPlayer The nick of the player that has a
     *                                 shepherd on that street(might be null)
     */
    public abstract void refreshStreet(String nickName, int streetIndex,
                                       boolean fence,
                                       String nickNameOfShepherdPlayer);

    /**
     * Refreshes the game parameters
     *
     * @param nickName
     * @param nickNames
     * @param wallets
     * @param shepherd4player
     */
    public abstract void refreshGameParameters(String nickName,
                                               String[] nickNames, int[] wallets,                                                                                          
                                               int shepherd4player

    );

    /**
     * Broadcast to the online player the player who is currently playing
     *
     * @param nickName The playing player
     */
    public abstract void brodcastCurrentPlayer(String nickName);

    /**
     * Refreshes to the client the just bought card
     *
     * @param nickName The client who bought the card
     * @param card     The card who was bought
     * @param value    The price
     */
    public abstract void refreshCard(String nickName, String card, int value);

    /**
     * Refreshes the movement of a special animal such as a blacksheep or a wolf
     *
     * @param animal         The type of special animal
     * @param movementResult The result of the movemetn
     */
    public abstract void refreshSpecialAnimal(SpecialAnimal animal,
                                              String movementResult);

    /**
     * Refresh the initial positiovn of the special animal
     *
     * @param client Player to refersh
     * @param animal Animal to refresh
     * @param region Region where the animal is
     */
    public abstract void refreshSpecialAnimalPosition(String client,
                                                      SpecialAnimal animal,
                                                      String region);

    /**
     * Refreshes to the player his amount of money
     *
     * @param nickName The player to refresh
     */
    public abstract void refreshMoney(String nickName);

    /**
     * Brodcast that a player has disconnected
     *
     * @param nickNameDisconnected The name of the player disconnected
     */
    public abstract void brodcastPlayerDisconnected(String nickNameDisconnected);

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

    /**
     * Refreshes the movement of a shepherd in the map
     *
     * @param nickNameMover Who moved the shepherd
     * @param shepherdIndex Which shepherd was moved
     * @param endStreet     Street where the shepherd was moved
     */
    public abstract void refreshMoveShepherd(String nickNameMover,
                                             int shepherdIndex,
                                             String endStreet,int price);

    /**
     * Refreshes that a card was bought to the online players
     *
     * @param nickNameBuyer Who bought the card
     * @param boughtLand    Wich land he bought
     * @param price         The pirce of the card
     */
    public abstract void refreshBuyLand(String nickNameBuyer, String boughtLand,
                                        int price);

    /**
     * Refreshes that a sheep mated with some other kind of ovine. The result
     * can be positive of a failure depending on the outcome
     *
     * @param nickName  Who mated the ovines
     * @param region    In which region
     * @param otherType The type who mated with the sheep
     * @param newType   The type which was born
     * @param outcome   If the mating was succesfull or not
     */
    public abstract void refreshMateSheepWith(String nickName, String region,
                                              String otherType, String newType,
                                              String outcome);

    /**
     * refreshes that a ovine was killed or that someone tried to kill it
     *
     * @param nickName Who was the killer
     * @param region   In which region
     * @param type     Which animal was killed
     * @param outcome  If the animal was successfully killed or survived
     */
    public abstract void refreshKillOvine(String nickName, String region,
                                          String type, String outcome);

    /**
     * Ask to the client to put its shepherd on the map
     *
     * @param nickName      Client that has to put the shpherd
     * @param shepherdIndex Shepherd to put
     *
     * @return The street in which the shepherd has to be put
     *
     * @throws PlayerDisconnectedException If the player disconnects while
     *                                     putting the shepherd
     */
    public abstract boolean askSetUpShepherd(String nickName, int shepherdIndex)
            throws PlayerDisconnectedException;

    /**
     * Asks the player to choose an action between the availabe ones
     *
     * @param nickName        Who has to choose the action
     * @param possibleActions The action available to that player
     *
     * @return The action chosen
     *
     * @throws PlayerDisconnectedException If the player disconnects while
     *                                     chosing
     */
    public abstract boolean askChooseAction(String nickName,
                                            String possibleActions) throws
            PlayerDisconnectedException;

    /**
     * If the game end unexpectedly
     */
    public abstract void unexpectedEndOfGame();

    /**
     * Returns the map of nicks and associated players
     *
     * @return
     */
    protected Map<String, Player> getNick2PlayerMap() {
        return nick2PlayerMap;
    }

    /**
     * Send the rank to a player telling him the score and if he's the winner
     *
     * @param winner   If the player won
     * @param nickName Which player to send the rank to
     * @param score    The score of the player
     */
    public abstract void sendRank(boolean winner, String nickName, int score);

    /**
     * Send all the rankings to all the players
     *
     * @param classification the rankings
     */
    public abstract void sendClassification(String classification);

    /**
     * Refreshes to the client the number of available fences
     *
     * @param client         The player to refrehs
     * @param fenceAvailable The number of available fence
     */
    public abstract void refreshNumberOfAvailableFence(String client,
                                                       int fenceAvailable);

}
