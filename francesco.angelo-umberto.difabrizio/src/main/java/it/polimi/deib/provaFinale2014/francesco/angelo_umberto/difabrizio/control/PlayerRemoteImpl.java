package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import java.rmi.RemoteException;


public class PlayerRemoteImpl implements PlayerRemote {
    
    private GameManager gameManager;
    
   public PlayerRemoteImpl(GameManager gameManager, String playerNickName) {
        this.playerNickName = playerNickName;
        this.gameManager = gameManager;
        gameManager.server.
        this.shepherd = new Shepherd[gameManager.shepherd4player];

        if (gameManager.shepherd4player >= ControlConstants.SHEPHERD_FOR_FEW_PLAYERS.getValue()) {
            this.shepherd[0] = new Shepherd(
                    GameConstants.LOW_PLAYER_WALLET_AMMOUNT.getValue());
        } else {
            this.shepherd[0] = new Shepherd(
                    GameConstants.STANDARD_WALLET_AMMOUNT.getValue());
        }

        //setUp shepherds sharing cards and wallet    
        for (int i = 1; i < gameManager.shepherd4player; i++) {
            this.shepherd[i] = new Shepherd(this.shepherd[0].getWallet(),
                    this.shepherd[0].getMyCards());
        }

    }
    
    
    public void moveOvine(String type, int startRegionIndex, int endRegionIndex)
            throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void moveShepherd(String nickName, int idShepherd, int streetIndex)
            throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setUpShepherd(String nickName, int idShepherd, int streetIndex) {
        
    }

    public void buyLand() throws RemoteException, ActionCancelledException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mateSheepWith(OvineType otherOvineType) throws RemoteException,
                                                               ActionCancelledException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void killOvine() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
