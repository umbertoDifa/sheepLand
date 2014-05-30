package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.PlayerRemote;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.PlayerRemoteImpl;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view.TypeOfView;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Umberto
 */
public class ClientRmi implements ClientInterfaceRemote{

    private String nickName;
    private String ip;
    private int port;
    private String nameServer;
    private TypeOfView view;
    private ServerRmi serverRmi;
    private PlayerRemote playerRmi;

    public ClientRmi(String ip, int port, String nameServer, TypeOfView view, String nickName) {
        this.nickName = nickName;
        this.nameServer = nameServer;
        this.port = port;
        this.ip = ip;
        this.view = view;
    }

    public void start() {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);

            //cerco l'oggetto nel registry
            serverRmi = (ServerRmi) registry.lookup(
                    nameServer);
            
            boolean nickOk;
            do{
                String nick = view.askNickName();
                nickOk = serverRmi.connect(this, nickName);
            }while (nickOk == false);
            
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "StartRmiClient" + ex.getMessage(), ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "Il server non è ancora bounded " + ex.getMessage(), ex);
        }
    }

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
            int numbOfLamb) {
        view.refreshRegion(regionIndex, numbOfSheep, numbOfRam, numbOfLamb);
    }

    public void refreshStreet(int streetIndex, boolean Fence,
            String nickShepherd) {
        view.refreshStreet(streetIndex, Fence, nickShepherd);
    }

    public void refereshGameParameters(int numbOfPlayers, String firstPlayer,
            int shepherd4player) {
        view.refereshGameParameters(numbOfPlayers, firstPlayer, shepherd4player);
    }

    public void refereshCurrentPlayer(String currenPlayer) {
        view.refereshCurrentPlayer(currenPlayer);
    }

    public void refereshCards(String[] myCards) {
        view.refereshCards(myCards);
    }

    public void refreshBlackSheep(int regionIndex) {
        view.refreshBlackSheep(regionIndex);
    }

    public void refreshWolf(int regionIndex) {
        view.refreshWolf(regionIndex);
    }

    public boolean setUpShepherd(int idShepherd) {
        String chosenStreet = view.askStreet();
        String result;
        try {
            result = playerRmi.setShepherd(idShepherd, chosenStreet);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
        if(result.contains("Patore posizionato corretamente!")){
            return true;
        }
        return false;
    }

    public void chooseAction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void moveOvine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshMoveOvine(int startRegionIndex, int endRegionIndex,
            String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void moveShepherd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshMoveShepherd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void buyLand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mateSheepWith() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshMateSheepWith(int regionIndex, String ovineType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void killOvine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshKillOvine(int regionIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refereshCards(List<String> myCards) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void connectPlayerRemote(PlayerRemote playerRemote) {
        this.playerRemote = playerRemote;
    }

}
