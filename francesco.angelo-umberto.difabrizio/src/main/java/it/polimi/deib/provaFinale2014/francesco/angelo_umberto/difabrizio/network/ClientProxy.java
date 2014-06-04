package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author Umberto
 */
public class ClientProxy {

    private int status;
    private boolean refreshNeeded;

    public ClientProxy() {
        this.status = NetworkConstants.ONLINE.getValue();
        this.refreshNeeded = false;
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

    protected void setStatus(int status) {
        this.status = status;
    }

    protected void setRefreshNeeded(boolean refresh) {
        this.refreshNeeded = refresh;
    }

}
