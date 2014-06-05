package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author Umberto
 */
public class ClientProxy {

    private int status;
    private boolean refreshNeeded;
    private int numberOfShepherdStillToSet;

    public ClientProxy() {
        this.status = NetworkConstants.ONLINE.getValue();
        this.refreshNeeded = false;
        this.numberOfShepherdStillToSet = 0;
    }

    public boolean isOnline() {
        return status == NetworkConstants.ONLINE.getValue();
    }

    public boolean needRefresh() {
        if (refreshNeeded) {
            refreshNeeded = false;
            return true;
        }
        return false;
    }

    public int getNumberOfShepherdStillToSet() {
        return numberOfShepherdStillToSet;
    }

    protected void setStatus(int status) {
        this.status = status;
    }

    protected void setRefreshNeeded(boolean refresh) {
        this.refreshNeeded = refresh;
    }

    public void setNumberOfShepherdStillToSet(int shepherdStillToSet) {
        this.numberOfShepherdStillToSet = shepherdStillToSet;
    }

}
