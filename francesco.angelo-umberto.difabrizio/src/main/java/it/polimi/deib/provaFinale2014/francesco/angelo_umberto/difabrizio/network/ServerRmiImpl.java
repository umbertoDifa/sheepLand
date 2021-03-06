package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.GameManager;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * It's the rmi server which implements the remote interface. It deals with the
 * connections of the clients and manage the starting of a game the rejection of
 * a player and the reconnection of a plyaer
 *
 * @author Umberto
 */
public class ServerRmiImpl extends UnicastRemoteObject implements ServerRmi,
        Runnable {

    private final int maxNumberOfGames;
    private final int maxClientsForGame;
    private final int minClientsForGame;

    /**
     * Timeout in milliseconds for the client's connections
     */
    private final int timeoutAccept;
    /**
     * Thread timer
     */
    private Timer timer = new Timer();

    /**
     * Thread del server
     */
    private final Thread myThread;

    /**
     * Executes the threads which manage the games
     */
    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * The number of players waiting for a mach to start during an rmi
     * connection
     */
    private int numberOfPlayers;

    private final int port;
    private final String serverName;

    private final PrintWriter stdOut = new PrintWriter(System.out);
    /**
     * Lista dei nickNames dei client che sono in coda per iniziare una partita
     */
    private List<String> clientNickNames = new ArrayList<String>();

    /**
     * Creates a serverRmi with a given serverName and the port to do the remote
     * binding
     *
     * @param serverName The name which will be used by the server to bind
     * @param port Port to bind to
     *
     * @throws RemoteException
     */
    public ServerRmiImpl(String serverName, int port) throws
            RemoteException {
        myThread = new Thread(this);

        this.port = port;
        this.serverName = serverName;

        this.maxNumberOfGames = NetworkConstants.DEFAULT_MAX_GAMES.getValue();
        this.maxClientsForGame = NetworkConstants.DEFAULT_MAX_CLIENTS_FOR_GAME.getValue();
        this.minClientsForGame = NetworkConstants.DEFAULT_MIN_CLIENTS_FOR_GAME.getValue();

        this.timeoutAccept = NetworkConstants.DEFAULT_TIMEOUT_ACCEPT.getValue() * NetworkConstants.MILLISECONDS_IN_SECONDS.getValue();
    }

    /**
     * Starts the server
     */
    protected void start() {
        myThread.start();
    }

    /**
     * {@inheritDoc }
     */
    public void run() {
        this.startServer();
    }

    private void startServer() {
        try {

            //Setto i player che aspettano di iniziare una partita a 0
            numberOfPlayers = 0;

            //Creo registro rmi nel quale caricare la mia istanza del server
            Registry registry = LocateRegistry.createRegistry(port);

            //Faccio il bind della mia istanza remota con un nome specifico
            registry.rebind(serverName, this);

            stdOut.println("ServerRmi caricato.");
            stdOut.flush();
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
        }

    }

    /**
     * {@inheritDoc }
     *
     * @param client
     * @param nickName
     *
     * @return
     *
     * @throws RemoteException
     */
    public boolean connect(ClientInterfaceRemote client, String nickName) throws
            RemoteException {
        //se il client che tenta di connettersi non esiste
        if (!ServerManager.NICK_2_CLIENT_PROXY_MAP.containsKey(nickName)) {

            //se ci sono partite da poter avviare
            if (ServerManager.activatedGames < maxNumberOfGames) {

                numberOfPlayers++;
                clientNickNames.add(nickName);
                DebugLogger.println(nickName + ": added");

                ServerManager.NICK_2_CLIENT_PROXY_MAP.put(nickName,
                        new RmiClientProxy(client));
                //se è il primo player
                if (numberOfPlayers == 1) {
                    timer = new Timer();
                    timer.startTimer();
                    DebugLogger.println("timer avviato");

                } else if (numberOfPlayers == maxClientsForGame) {
                    //se non è il primo
                    timer.stopTimer();
                    startGame();
                }
                return true;
                //comunque vada lo mappo
            } else {
                DebugLogger.println("Client rifiutato");
                RmiClientProxy clientToReject = (RmiClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName);
                clientToReject.getClientRmi().disconnect(
                        "Il server è pieno riporavare più tardi");
                clientNickNames.clear();
                return false;
            }
        } else {
            if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName).isOnline()) {
                DebugLogger.println("Client già in gioco");
                //rigettalo
                client.disconnect(
                        "Il giocatore con il nickName inserito è già online");
                return false;
            } else {
                //il client deve riconnettersi
                //rimuovo la vecchia instanza
                DebugLogger.println("Client tenta di riconnettersi");

                //salvo se aveva pastori da mettere
                int shepherdToSet = ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName).getNumberOfShepherdStillToSet();

                ServerManager.NICK_2_CLIENT_PROXY_MAP.remove(nickName);
                DebugLogger.println(nickName + " rimosso");
                //metto il nuovo
                ServerManager.NICK_2_CLIENT_PROXY_MAP.put(nickName,
                        new RmiClientProxy(client));
                DebugLogger.println(nickName + " aggiunto");
                //setto il refresh
                ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName).setRefreshNeeded(
                        true);
                DebugLogger.println(nickName + " settato refresh");

                //setto i pastori mancanti
                ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName).setNumberOfShepherdStillToSet(
                        shepherdToSet);
                DebugLogger.println(
                        nickName + " settato pastori da mettere a " + shepherdToSet);
                return true;
            }
        }
    }

    private void startGame() {

        if (numberOfPlayers >= minClientsForGame) {
            DebugLogger.println(
                    "Avvio il gioco con " + numberOfPlayers + " giocatori");

            executor.submit(new GameManager(clientNickNames,
                    new RmiTrasmission()));

            ServerManager.activatedGames++;

            stdOut.println(
                    "Numero di partite attive " + ServerManager.activatedGames);
            stdOut.flush();

        } else {
            handleClientRejection(
                    "Ci scusiamo, non ci sono abbastanza giocatori per una partita");
            //elimina il loro record dai giocatori attivi
            for (String client : clientNickNames) {
                ServerManager.NICK_2_CLIENT_PROXY_MAP.remove(client);
            }
        }
        //comunque vada svuota la lista dei socket
        clientNickNames.clear();
        numberOfPlayers = 0;

        DebugLogger.println("Lista client:" + clientNickNames.toString());
    }

    private void handleClientRejection(String message) {
        DebugLogger.println("Rifiuto Client.");

        //per tutti i client
        for (Map.Entry pairs : ServerManager.NICK_2_CLIENT_PROXY_MAP.entrySet()) {
            //se il loro nick è tra quelli in lista di attesa
            String nick = (String) pairs.getKey();
            if (clientNickNames.contains(nick)) {
                RmiClientProxy clientProxy = (RmiClientProxy) pairs.getValue();
                try {
                    clientProxy.getClientRmi().disconnect(message);
                } catch (RemoteException e) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE, e.getMessage(), e);
                    //poco male il client che dovevamo rifiutare si è gia disconnesso
                }
            }

        }
    }

    /**
     * The timer that starts when the first client of a game connects to the
     * server
     */
    private class Timer implements Runnable {

        private final Thread myThread;

        /**
         * Creates a timer
         */
        public Timer() {
            this.myThread = new Thread(this);
        }

        /**
         * Starts the timer
         */
        public void startTimer() {
            this.myThread.start();
        }

        /**
         * {@inheritDoc }
         */
        public void run() {
            try {
                //avvio il timer

                Thread.sleep(timeoutAccept);

                DebugLogger.println("Timer finito");

                //se finisce avvio game
                startGame();

            } catch (InterruptedException ex) {
                //se blocco il timer io non succede niente, muori e basta.
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);
            }
        }

        /**
         * Stops the timer
         */
        public void stopTimer() {
            this.myThread.interrupt();
            DebugLogger.println("Timer fermato");

        }

    }

}
