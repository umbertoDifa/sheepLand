package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 * It represents a compact version on a client that the server uses to
 * comunicate with the real one
 *
 * @author Umberto
 */
public class ClientProxy {

    private NetworkConstants status;
    private boolean refreshNeeded;
    private int numberOfShepherdStillToSet;

    /**
     * Creates a client proxy setting it's status to ONLINE, the refresh to
     * NOT_NEEDED and the number of shepherd that he has to set to 0. This
     * numbers will be changed accordigly to the situation in case of a
     * disconnection
     */
    public ClientProxy() {
        this.status = NetworkConstants.ONLINE;
        this.refreshNeeded = false;
        this.numberOfShepherdStillToSet = 0;
    }

    /**
     * Checks if a player is online
     *
     * @return True if the player is online, false if not
     */
    public boolean isOnline() {
        return status == NetworkConstants.ONLINE;
    }

    /**
     * Checks the status of the player refresh in case of a disconnection, it's
     * true if the player reconnected after a disconnection but it is still not
     * updated
     *
     * @return True if the player needs a refresh of the game, false if not
     */
    public boolean needRefresh() {
        return refreshNeeded;
    }

    /**
     * It gets the number of the shepherds that a player has to set after a
     * disconnection
     *
     * @return The number of shepherd still to set
     */
    public int getNumberOfShepherdStillToSet() {
        return numberOfShepherdStillToSet;
    }

    /**
     * Can set the status of the player to online of offline
     *
     * @param status Online or offline
     */
    protected void setStatus(NetworkConstants status) {
        this.status = status;
    }

    /**
     * Sets the need of refresh after a disconnection
     *
     * @param refresh True if the player has to be refreshed, false if not
     */
    public void setRefreshNeeded(boolean refresh) {
        this.refreshNeeded = refresh;
    }

    /**
     * Sets the number of shepherds still to set
     *
     * @param shepherdStillToSet Shepherd still to set during a game
     */
    public void setNumberOfShepherdStillToSet(int shepherdStillToSet) {
        this.numberOfShepherdStillToSet = shepherdStillToSet;
    }

}
