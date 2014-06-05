package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.PlayerRemote;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view.TypeOfViewController;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Umberto
 */
public class ClientRmi implements ClientInterfaceRemote {

    private String nickName;
    private final String ip;
    private final int port;
    private final String nameServer;

    private final TypeOfViewController view;

    private ServerRmi serverRmi;
    private PlayerRemote playerRmi;
    private Registry registry;

    private String[] token;
    String parameters;
    String result;

    public ClientRmi(String ip, int port, String nameServer,
                     TypeOfViewController view) throws RemoteException {
        this.nickName = nickName;
        this.nameServer = nameServer;
        this.port = port;
        this.ip = ip;
        this.view = view;

    }

    protected void startClient() {
        try {
            UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.getRegistry(ip, port);

            //cerco l'oggetto nel registry
            serverRmi = (ServerRmi) registry.lookup(
                    nameServer);

            Scanner stdIn = new Scanner(System.in);
            PrintWriter stdOut = new PrintWriter(System.out);

            DebugLogger.println(
                    "Canali di comunicazione impostati");

            do {
                stdOut.println("Inserisci il tuo nickName:");
                stdOut.flush();

                nickName = stdIn.nextLine();
            } while ("".equals(nickName) || nickName.contains(",") || nickName.contains(
                    ":"));
            //da evitare come la peste la stringa vuota come nickname
            //esplodono i satelliti della nasa
            
            
            //crea uno skeleton affinche il server possa chiamare dei metodi su di te
            registry.rebind(nickName, this);
            DebugLogger.println("Invio nickName");
            
            //connettiti
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

    public void refreshCard(String type, int value) {
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

        result = playerRmi.setShepherdRemote(idShepherd, chosenStreet);

        token = result.split(",");
        if (result.contains("Pastore posizionato correttamente!")) {
            view.showSetShepherd("" + idShepherd, chosenStreet);
            return chosenStreet;
        }
        view.showInfo(token[0]);
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
        //receive possible actions      
        String[] possibleActions = actions.split(",");

        int[] availableAcions = new int[possibleActions.length];
        String[] actionsName = new String[possibleActions.length];

        for (int i = 0; i < possibleActions.length; i++) {
            token = possibleActions[i].split("-");

            availableAcions[i] = Integer.parseInt(token[0]);
            actionsName[i] = token[1];
        }

        //ottengo la risposta dallo user
        int choice = view.chooseAction(availableAcions, actionsName);
        try {
            switch (choice) {
                case 1:
                    return "1," + this.moveOvine();

                case 2:
                    return "2," + this.moveShepherd();
                case 3:
                    return "3," + this.buyLand();
                case 4:
                    return "4," + this.mateSheepWith("sheep");
                case 5:
                    return "5," + this.mateSheepWith("ram");
                case 6:
                    return "6," + this.killOvine();

            }
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            //TODO gestire quando il client tenta di eseguire un metodo sul server
            //ma fallisce
        }
        return null;
    }

    private String moveOvine() throws RemoteException {
        parameters = view.moveOvine();

        token = parameters.split(",");

        result = playerRmi.moveOvineRemote(token[0], token[1], token[2]);

        //se l'azione è andata a buon fine
        if ("Ovino mosso".equals(result)) {
            view.showMoveOvine(token[0], token[1], token[2]);
            //ritorna la stringa dei parametri partenza,arrivo,tipo
            return parameters;
        }
        view.showInfo(result);
        //altrimenti null
        return null;
    }

    public void refreshMoveOvine(String nickNameMover, String startRegion,
                                 String endRegion, String ovineType) {
        view.refreshMoveOvine(nickNameMover, ovineType, startRegion, endRegion);
    }

    private String moveShepherd() {
        try {
            parameters = view.askMoveShepherd();
            token = parameters.split(",");

            result = playerRmi.moveShepherdRemote(token[0], token[1]);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return null;
        }
        if (result.contains("Pastore spostato")) {
            token = result.split(",");
            view.showMoveShepherd(token[1]);
            return parameters;
        }
        view.showInfo(result);
        return null;
    }

    public void refreshMoveShepherd(String nickName, String indexShepherd,
                                    String newStreet) {
        view.refreshMoveShepherd(nickName, indexShepherd, newStreet);
    }

    public void refreshBuyLand(String nickNameBuyer, String boughtLand,
                               String price) throws RemoteException {
        view.refreshBuyLand(nickNameBuyer, boughtLand, price);
    }

    private String buyLand() {
        parameters = view.askBuyLand();

        try {
            result = playerRmi.buyLandRemote(parameters);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return null;
        }
        token = result.split(",");
        if (result.contains("Carta acquistata")) {
            view.showBoughtLand(token[1], token[2]);
            return result;
        }
        view.showInfo(token[0]);
        return null;
    }

    private String mateSheepWith(String ovineType) {
        parameters = view.askMateSheepWith();

        token = parameters.split(",");
        try {
            result = playerRmi.mateSheepWithRemote(token[0], token[1],
                    ovineType);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return null;
        }
        String[] resultTokens = result.split(",");
        if (result.contains("Accoppiamento eseguito")) {
            view.showMateSheepWith(token[1], ovineType, resultTokens[1]);
            return token[1] + "," + ovineType + "," + resultTokens[1] + ",ok";
        } else if ("Il valore del dado è diverso dalla strada del pastore".equals(
                result)) {
            view.showInfo(resultTokens[0]);
            return token[1] + "," + ovineType + "," + token[0] + "," + ",nok";
        }
        return null;
    }

    public void refreshMateSheepWith(String nickNameMater, String region,
                                     String otherType, String newType,
                                     String outcome) {
        view.refreshMateSheepWith(nickName, region, otherType, newType, outcome);
    }

    private String killOvine() {
        parameters = view.askKillOvine();

        DebugLogger.println("parametri" + parameters);

        token = parameters.split(",");

        try {
            result = playerRmi.killOvineRemote(token[0], token[1], token[2]);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return null;
        }

        if (result.contains("Ovino ucciso")) {
            String[] tokenResult = result.split(",");

            view.showKillOvine(token[1], token[2], tokenResult[1]);

            return token[1] + "," + token[2] + "," + ",ok";
        } else if ("Non puoi pagare il silenzio degli altri pastori".equals(
                result) || "Il valore del dado è diverso dalla strada del pastore".equals(
                        result)) {
            view.showInfo(result);

            return token[1] + "," + token[2] + "," + ",nok";
        }
        view.showInfo(result);
        return null;
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

    public void refreshMoney(String money) throws RemoteException {
        view.refreshMoney(money);
    }

    public void showMyRank(String winner, String rank) throws RemoteException {
        view.showMyRank(Boolean.parseBoolean(winner), rank);
    }

    public void showClassification(String classification) throws RemoteException {
        view.showClassification(classification);
    }

    public void refreshKillOvine(String nickNameKiller, String region,
                                 String type, String outcome) throws
            RemoteException {
        view.refreshKillOvine(type, region, type, outcome);
    }

    public void refreshSpecialAnimalInitialPosition(String position) throws
            RemoteException {
        view.specialAnimalInitialCondition(position);
    }

    public void refreshPlayerDisconnected(String player) throws RemoteException {
        view.refreshPlayerDisconnected(player);
    }

}
