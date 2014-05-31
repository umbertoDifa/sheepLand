package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.GameManager;
import static it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerSockets.activatedGames;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRmiImpl extends UnicastRemoteObject implements ServerRmi,
                                                                  Runnable {

    //constanti generiche
    private final int MILLISECONDS_IN_SECONDS = 1000;

    //costanti di default per i costruttori
    /**
     * Default seconds to wait before timeout the clients connection to a game
     */
    private static final int DEFAULT_TIMEOUT_ACCEPT = 15;
    /**
     * The default minimum number of clients for a game
     */
    private static final int DEFAULT_MIN_CLIENTS_FOR_GAME = 2;
    /**
     * The default maximum number of clients for a game
     */
    private static final int DEFAULT_MAX_CLIENTS_FOR_GAME = 4;
    /**
     * The maximum number of games that a server can activate simultaniusly
     */
    private static final int DEFAULT_MAX_GAMES = 3;

    private final int maxNumberOfGames;
    private final int maxClientsForGame;
    private final int minClientsForGame;

    /**
     * It contains the seconds that the timeout waits before interrupting the
     * process that waits for client's connections, it's set up by the
     * constructor.
     */
    private final int secondsBeforeAcceptTimeout;

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
    Thread myThread;

    /**
     * Executes the threads which manage the games
     */
    ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * The number of players waiting for a mach to start during an rmi
     * connection
     */
    private int numberOfPlayers;
    /**
     * It represents the number of active games. Since it's static it can be
     * modified by any thread which decrements it before dying
     */
    protected static int activatedGames = 0;

    private final int port;
    private final String serverName;
    private final String ip;

    /**
     * Lista dei nickNames dei client che sono in coda per iniziare una partita
     */
    private List<String> clientNickNames = new ArrayList<String>();
    protected static HashMap<String, RmiClientProxy> NickClientRmiMap = new HashMap<String, RmiClientProxy>();

    public ServerRmiImpl(String serverName, String ip, int port) throws
            RemoteException {
        myThread = new Thread(this);

        this.port = port;
        this.serverName = serverName;
        this.ip = ip;

        this.maxNumberOfGames = DEFAULT_MAX_GAMES;
        this.maxClientsForGame = DEFAULT_MAX_CLIENTS_FOR_GAME;
        this.minClientsForGame = DEFAULT_MIN_CLIENTS_FOR_GAME;
        this.secondsBeforeAcceptTimeout = DEFAULT_TIMEOUT_ACCEPT;
        this.timeoutAccept = secondsBeforeAcceptTimeout * MILLISECONDS_IN_SECONDS;
    }

    public void start() {
        myThread.start();
    }

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

            System.out.println("ServerRmi caricato.");
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
        }

    }

    public void connect(ClientInterfaceRemote client, String nickName) throws
            RemoteException {
        //se il client che tenta di connettersi non esiste
        if (!NickClientRmiMap.containsKey(nickName)) {

            //se ci sono partite da poter avviare
            if (activatedGames < maxNumberOfGames) {
                
                numberOfPlayers++;
                clientNickNames.add(nickName);
                DebugLogger.println(nickName+": added");
                
                NickClientRmiMap.put(nickName, new RmiClientProxy(client));
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

                //comunque vada lo mappo
            } else {
                DebugLogger.println("Client rifiutato");
                RmiClientProxy clientToReject = (RmiClientProxy) NickClientRmiMap.get(
                        nickName);
                clientToReject.getClientRmi().disconnect(
                        "Il server è pieno riporavare più tardi");
                clientNickNames.clear();
            }
        }//TODO handle reconnection
    }

    private void startGame() {

        if (numberOfPlayers >= minClientsForGame) {
            DebugLogger.println(
                    "Avvio il gioco con " + numberOfPlayers + " giocatori");

            executor.submit(new GameManager(clientNickNames,
                    new RmiTrasmission()));
            
            activatedGames++;
            
            System.out.println("Partita numero " + activatedGames + " avviata.");

        } else {
            handleClientRejection(
                    "Ci scusiamo, non ci sono abbastanza giocatori per una partita");
            //elimina il loro record dai giocatori attivi
            for (String client : clientNickNames) {
                NickClientRmiMap.remove(client);
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
        for (Map.Entry pairs : NickClientRmiMap.entrySet()) {
            //se il loro nick è tra quelli in lista di attesa
            String nick = (String) pairs.getKey();
            if (clientNickNames.contains(nick)) {
                RmiClientProxy clientProxy = (RmiClientProxy) pairs.getValue();
                try {
                    clientProxy.getClientRmi().disconnect(message);
                } catch (RemoteException ex) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);
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

        public Timer() {
            this.myThread = new Thread(this);
        }

        public void startTimer() {
            this.myThread.start();
        }

        public void run() {
            try {
                //avvio il timer

                this.myThread.sleep(timeoutAccept);

                DebugLogger.println("Timer finito");

                //se finisce avvio game
                startGame();

            } catch (InterruptedException ex) {
                //se blocco il timer io non succede niente, muori e basta.
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);
            }
        }

        public void stopTimer() {
            this.myThread.interrupt();
            DebugLogger.println("Timer fermato");

        }

    }

}
