package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

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
 * It's the RMI version of the client. It's used to comunicate with the user and
 * to send and receive commands to/from the server
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
    private String parameters;
    private String result;
    private boolean connectionResult;

    /**
     * Creates an rmi client setting it's variables.
     *
     * @param ip         The ip to which it will connect to find the server
     * @param port       The port to search the server
     * @param nameServer The name to which the server is binded
     * @param view       The kind of view wanted by the user
     */
    public ClientRmi(String ip, int port, String nameServer,
                     TypeOfViewController view) {
        this.nameServer = nameServer;
        this.port = port;
        this.ip = ip;
        this.view = view;

    }

    /**
     * Starts the client by connecting to the server after asking to the user
     * the nickname. It keeps asking the nickName to the user till it's a valid
     * one. Creates a skeleton of its self so that the server can call remote
     * methods on it
     */
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

                do {
                    this.nickName = view.askNickName();
                } while ("".equals(nickName) || nickName.contains(",") || nickName.contains(
                        ":"));
                //da evitare come la peste la stringa vuota come nickname
                //esplodono i satelliti della nasa

                DebugLogger.println("Invio nickName");

                //connettiti
                connectionResult = serverRmi.connect(this, nickName);

            } while (connectionResult == false);

            //crea uno skeleton affinche il server possa chiamare dei metodi su di te
            registry.rebind(nickName, this);

        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "StartRmiClient" + ex.getMessage(), ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "Il server non è ancora bounded " + ex.getMessage(), ex);
        }

    }

    /**
     * {@inheritDoc }
     *
     * @param regionIndex
     * @param numbOfSheep
     * @param numbOfRam
     * @param numbOfLamb
     */
    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
                              int numbOfLamb) {
        view.refreshRegion(regionIndex, numbOfSheep, numbOfRam, numbOfLamb);
    }

    /**
     * {@inheritDoc }
     *
     * @param streetIndex
     * @param fence
     * @param nickShepherd
     */
    public void refreshStreet(int streetIndex, boolean fence,
                              String nickShepherd, int shepherdIndex) {
        view.refreshStreet(streetIndex, fence, nickShepherd, shepherdIndex);
    }

    /**
     * {@inheritDoc }
     *
     * @param currenPlayer
     */
    public void refereshCurrentPlayer(String currenPlayer) {
        view.refereshCurrentPlayer(currenPlayer);
    }

    /**
     * {@inheritDoc }
     *
     * @param type
     * @param value
     */
    public void refreshCard(String type, int value) {
        view.refereshCard(type, value);
    }

    /**
     * {@inheritDoc }
     *
     * @param regionIndex
     */
    public void refreshBlackSheep(String regionIndex) {
        view.refreshBlackSheep(regionIndex);
    }

    /**
     * {@inheritDoc }
     *
     * @param regionIndex
     */
    public void refreshWolf(String regionIndex) {
        view.refreshWolf(regionIndex);
    }

    /**
     * {@inheritDoc }
     *
     * @param idShepherd
     *
     * @return
     *
     * @throws RemoteException
     */
    public String setUpShepherd(int idShepherd) throws RemoteException {
        String chosenStreet = view.setUpShepherd(idShepherd);

        result = playerRmi.setShepherdRemote(idShepherd, chosenStreet);

        token = result.split(",", -1);
        if (result.contains("Pastore posizionato correttamente!")) {
            view.showSetShepherd(idShepherd, chosenStreet);
            return chosenStreet;
        }
        view.showInfo(token[0]);
        return null;
    }

    /**
     * {@inheritDoc }
     */
    public String chooseAction(String actions) {
        DebugLogger.println("azioni possibili ricevute nella choose action");
        
        //receive possible actions      
        String[] possibleActions = actions.split(",");

        int[] availableAcions = new int[possibleActions.length];
        String[] actionsName = new String[possibleActions.length];

        for (int i = 0; i < possibleActions.length; i++) {
            token = possibleActions[i].split("-");

            availableAcions[i] = Integer.parseInt(token[0]);
            actionsName[i] = token[1];
        }

        // ottengo il risultato e lo controllo
        boolean rightFormat;
        String choice;
        int action = -1;

        do {
            choice = view.chooseAction(availableAcions, actionsName);
            try {
                action = Integer.parseInt(choice);
                rightFormat = true;
            } catch (NumberFormatException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                view.showInfo("Azione non valida.\nPrego riprovare.");
                rightFormat = false;
            }
        } while (!rightFormat || !actionExists(availableAcions, action));

        try {
            switch (Integer.parseInt(choice)) {
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
                default:
                    return "0," + "null";

            }
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            this.disconnect("Il server è offline, la partita termina");
        }
        return null;
    }

    private boolean actionExists(int[] availableActions, int action) {
        for (int i = 0; i < availableActions.length; i++) {
            if (availableActions[i] == action) {
                return true;
            }
        }
        view.showInfo("Azione non esistente.\nPrego riporvare:");
        return false;
    }

    private String moveOvine() throws RemoteException {
        parameters = view.askMoveOvine();

        token = parameters.split(",", -1);

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

    /**
     * {@inheritDoc }
     *
     * @param nickNameMover
     * @param startRegion
     * @param endRegion
     * @param ovineType
     */
    public void refreshMoveOvine(String nickNameMover, String startRegion,
                                 String endRegion, String ovineType) {
        view.refreshMoveOvine(nickNameMover, ovineType, startRegion, endRegion);
    }

    private String moveShepherd() {

        parameters = view.askMoveShepherd();
        token = parameters.split(",", -1);
        String shepherdIndex = token[0];
        String endStreet = token[1];
        try {
            result = playerRmi.moveShepherdRemote(shepherdIndex, endStreet);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return null;
        }
        if (result.contains("Pastore spostato")) {
            token = result.split(",", -1);
            view.showMoveShepherd(shepherdIndex, token[1]);
            return parameters + "," + token[1];
        }
        view.showInfo(result);
        return null;
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param indexShepherd
     * @param newStreet
     * @param price
     */
    public void refreshMoveShepherd(String nickName, int indexShepherd,
                                    String newStreet, int price) {
        view.refreshMoveShepherd(nickName, indexShepherd, newStreet, price);
    }

    /**
     * {@inheritDoc }
     *
     * @throws java.rmi.RemoteException
     */
    public void refreshBuyLand(String nickNameBuyer, String boughtLand,
                               int price) throws RemoteException {
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
        token = result.split(",", -1);
        if (result.contains("Carta acquistata")) {
            view.showBoughtLand(token[1], token[2]);
            return result;
        }
        view.showInfo(token[0]);
        return null;
    }

    private String mateSheepWith(String ovineType) {
        parameters = view.askMateSheepWith();

        token = parameters.split(",", -1);
        try {
            result = playerRmi.mateSheepWithRemote(token[0], token[1],
                    ovineType);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return null;
        }
        String[] resultTokens = result.split(",", -1);
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

    /**
     * {@inheritDoc }
     *
     * @param nickNameMater
     * @param region
     * @param otherType
     * @param newType
     * @param outcome
     */
    public void refreshMateSheepWith(String nickNameMater, String region,
                                     String otherType, String newType,
                                     String outcome) {
        view.refreshMateSheepWith(nickName, region, otherType, newType, outcome);
    }

    private String killOvine() {
        parameters = view.askKillOvine();

        DebugLogger.println("parametri" + parameters);

        token = parameters.split(",", -1);

        try {
            result = playerRmi.killOvineRemote(token[0], token[1], token[2]);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return null;
        }

        if (result.contains("Ovino ucciso")) {
            String[] tokenResult = result.split(",", -1);

            view.showKillOvine(token[1], token[2], tokenResult[1]);

            return token[1] + "," + token[2] + "," + "ok";
        } else if ("Non puoi pagare il silenzio degli altri pastori".equals(
                result) || "Il valore del dado è diverso dalla strada del pastore".equals(
                        result)) {
            view.showInfo(result);

            return token[1] + "," + token[2] + "," + "nok";
        }
        view.showInfo(result);
        return null;
    }

    /**
     * {@inheritDoc }
     *
     * @param message
     */
    public void disconnect(String message) {
        view.showInfo(message);
        if (connectionResult == true) {
            try {
                UnicastRemoteObject.unexportObject(this, true);
                registry.unbind(nickName);
            } catch (RemoteException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
            }
            synchronized (Client.LOCK) {
                Client.LOCK.notify();
            }
        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws RemoteException
     */
    public void welcome() throws RemoteException {
        view.showWelcome();
    }

    /**
     * {@inheritDoc }
     *
     * @param player
     *
     * @throws RemoteException
     */
    public void connectPlayer(PlayerRemote player) throws RemoteException {
        this.playerRmi = player;
    }

    /**
     * {@inheritDoc }
     *
     * @param money
     *
     * @throws RemoteException
     */
    public void refreshMoney(String money) throws RemoteException {
        view.refreshMoney(money);
    }

    /**
     * {@inheritDoc }
     *
     * @param winner
     * @param rank
     *
     * @throws RemoteException
     */
    public void showMyRank(String winner, String rank) throws RemoteException {
        view.showMyRank(Boolean.parseBoolean(winner), rank);
    }

    /**
     * {@inheritDoc }
     *
     * @param classification
     *
     * @throws RemoteException
     */
    public void showClassification(String classification) throws RemoteException {
        view.showClassification(classification);
    }

    /**
     * {@inheritDoc }
     *
     * @param nickNameKiller
     * @param region
     * @param type
     * @param outcome
     *
     * @throws RemoteException
     */
    public void refreshKillOvine(String nickNameKiller, String region,
                                 String type, String outcome) throws
            RemoteException {
        view.refreshKillOvine(nickNameKiller, region, type, outcome);
    }

    /**
     * {@inheritDoc }
     *
     * @param position
     *
     * @throws RemoteException
     */
    public void refreshSpecialAnimalInitialPosition(String position) throws
            RemoteException {
        view.specialAnimalInitialCondition(position);
    }

    /**
     * {@inheritDoc }
     *
     * @param player
     *
     * @throws RemoteException
     */
    public void refreshPlayerDisconnected(String player) throws RemoteException {
        view.refreshPlayerDisconnected(player);
    }

    public void refreshGameParameters(String[] nickNames, int[] wallets,
                                      int shepherd4player)
            throws RemoteException {
        view.refreshGameParameters(nickNames, wallets, shepherd4player);
    }

    public void refreshAvailableFences(int availableFences) throws
            RemoteException {
        view.refreshFences(availableFences);
    }

    public void refreshOtherPlayerWallet(String otherPlayer, int otherMoney)
            throws RemoteException {
        view.refreshOtherPlayerMoney(otherPlayer, otherMoney);
    }

}
