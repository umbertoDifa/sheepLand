package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.PlayerRemote;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view.TypeOfViewController;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Umberto
 */
public class ClientRmi extends UnicastRemoteObject implements
        ClientInterfaceRemote {

    private final String nickName;
    private final String ip;
    private final int port;
    private final String nameServer;

    private final TypeOfViewController view;

    private ServerRmi serverRmi;
    private PlayerRemote playerRmi;
    private Registry registry;

    public ClientRmi(String ip, int port, String nameServer, TypeOfViewController view,
                     String nickName) throws RemoteException {
        this.nickName = nickName;
        this.nameServer = nameServer;
        this.port = port;
        this.ip = ip;
        this.view = view;

    }

    protected void startClient() {
        try {
            registry = LocateRegistry.getRegistry(ip, port);

            //crea uno skeleton affinche il server possa chiamare dei metodi su di te
            registry.rebind(nickName, this);

            //cerco l'oggetto nel registry
            serverRmi = (ServerRmi) registry.lookup(
                    nameServer);

            serverRmi.connect(this, nickName);

        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "StartRmiClient" + ex.getMessage(), ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "Il server non è ancora bounded " + ex.getMessage(), ex);
        }
        try {
            registry.rebind(nickName, this);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "Il client non è riuscito a fare il bind" + ex.getMessage(),
                    ex);
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

    public void refreshGameParameters(int numbOfPlayers, String firstPlayer,
                                      int shepherd4player) {
        view.refereshGameParameters(numbOfPlayers, firstPlayer, shepherd4player);
    }

    public void refereshCurrentPlayer(String currenPlayer) {
        view.refereshCurrentPlayer(currenPlayer);
    }

    public void refereshCard(String type, int value) {
        view.refereshCard(type, value);
    }

    public void refreshBlackSheep(String regionIndex) {
        view.refreshBlackSheep(regionIndex);
    }

    public void refreshWolf(String regionIndex) {
        view.refreshWolf(regionIndex);
    }

    public String setUpShepherd(int idShepherd) throws RemoteException {
        String chosenStreet = view.setUpShepherd(idShepherd);
        String result;

        result = playerRmi.setShepherdRemote(idShepherd, chosenStreet);

        view.showInfo(result);
        if (result.contains("Patore posizionato corretamente!")) {
            return chosenStreet;
        }
        return null;
    }

    /**
     * It ask the player to choose an action and makes it by calling the right
     * method.
     *
     * @param actions Actions that ca be chosen
     *
     * @return A string with the number of the action and the result or a string
     *         with the number of the action concatenated with null
     */
    public String chooseAction(String actions) {
        String[] possibleActions = actions.split(",");

        int[] availableAcions = new int[possibleActions.length];
        String[] actionsName = new String[possibleActions.length];

        for (int i = 0; i < possibleActions.length; i++) {
            String[] token = possibleActions[i].split("-");
            availableAcions[i] = Integer.parseInt(token[0]);
            actionsName[i] = token[1];
        }

        int choice = view.chooseAction(availableAcions, actionsName);
        try {
            switch (choice) {
                case 1:
                    return "1," + this.moveOvine();

                //TODO completare
            }
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);         
            //TODO gestire quando il client tenta di eseguire un metodo sul server
            //ma fallisce
        }
        return null; //FIXME
    }

    private String moveOvine() throws RemoteException {
        String parameters = view.moveOvine();
        String[] token = parameters.split(",");

        String result = playerRmi.moveOvineRemote(token[0], token[1], token[2]);
        view.showInfo(result);
        //se l'azione è andata a buon fine
        if ("Ovino mosso!".equals(result)) {
            //ritorna la stringa dei parametri tipo,arrivo,partenza
            return parameters;
        }
        //altrimenti null
        return null;
    }

    public void refreshMoveOvine(int startRegionIndex, int endRegionIndex,
                                 String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean moveShepherd() {
        String result;
        try {
            result = view.askMoveShepherd();
            String[] token = result.split(",");
            result = playerRmi.moveShepherdRemote(token[0], token[1]);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return false;
        }
        if (result.contains("pastore posizionato")) {
            return true;
        }
        return false;
    }

    public void refreshMoveShepherd(String nickName, String indexShepherd,
                                    String newStreet) {
        view.refreshMoveShepherd(nickName, indexShepherd, newStreet);
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

    public void disconnect(String message) {
        view.showInfo(message);
        try {
            UnicastRemoteObject.unexportObject(this, true);
            registry.unbind(nickName);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(),
                    ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(),
                    ex);
        }
    }

    public void welcome() throws RemoteException {
        view.showWelcome();
    }

    public void connectPlayer(PlayerRemote player) throws RemoteException {
        this.playerRmi = player;
    }

}
