package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class is a manager that collects connections from clients and starts
 * games when there are enough cilents. It starts a thread any time it needs to
 * start a new game but it allows only a maximum of games.
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerManager implements ServerRmi {

    /**
     * It contains the seconds that the timeout waits before interrupting the
     * process that waits for client's connections, it's set up by the
     * constructor.
     */
    private int secondsBeforeAcceptTimeout;

    /**
     * Timeout in milliseconds for the client's connections
     */
    private int timeoutAccept;

    //constanti generiche
    private final int MILLISECONDS_IN_SECONDS = 1000;

    //costanti di default per i costruttori
    /**
     * Default seconds to wait before timeout the clients connection to a game
     */
    private static final int DEFAULT_TIMEOUT_ACCEPT = 10;
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

    //variabili
    /**
     * Connection port of the server (rmi or socket)
     */
    private final int port;
    /**
     * Server name of the rmi server
     */
    private String serverName;
    private static boolean rmi;

    private final int maxNumberOfGames;
    private final int maxClientsForGame;

    private final int minClientsForGame;
    /**
     * It represents the number of active games. Since it's static it can be
     * modified by any thread which decrements it before dying
     */
    protected static int activatedGames = 0;
    /**
     * The number of players waiting for a mach to start during an rmi
     * connection
     */
    private int numberOfPlayers;
    /**
     * Thread timer
     */
    private Timer timer = new Timer();
    /**
     * The socket of the server
     */
    private ServerSocket serverSocket;
    /**
     * Lista dei nickNames dei client che sono in coda per iniziare una partita
     */
    private List<String> clientNickNames = new ArrayList<String>();
    /**
     * The client trying to connect
     */
    private Socket clientSocket;
    /**
     * Executes the threads which manage the games
     */
    ExecutorService executor = Executors.newCachedThreadPool();

    protected static HashMap<String, RmiClientProxy> NickClientRmiMap = new HashMap<String, RmiClientProxy>();
    protected static HashMap<String, SocketClientProxy> NickSocketMap = new HashMap<String, SocketClientProxy>();
    //TODO usare la riga sopra

    /**
     * It construct a server manager at a certain port, with a maximum and
     * minimum number of clients for a game. It also sets the maximum number of
     * games that can be active at the same time and the seconds to wait before
     * the timeout of the connections for a game.
     *
     * @param port              Port to bind the server
     * @param maxGames          Max number of simultanious game
     * @param maxClientsForGame Max number of clients for a game
     * @param minClientsForGame Min number of clients for a game
     * @param acceptTimeout     Seconds for the timeout
     */
    public ServerManager(int port, int maxGames, int maxClientsForGame,
                         int minClientsForGame, int acceptTimeout) {
        this.maxNumberOfGames = maxGames;
        this.maxClientsForGame = maxClientsForGame;
        this.minClientsForGame = minClientsForGame;
        this.secondsBeforeAcceptTimeout = acceptTimeout;
        this.timeoutAccept = secondsBeforeAcceptTimeout * MILLISECONDS_IN_SECONDS;
        //setta la porta del server 
        this.port = port;

        //decidi cosa fare dei log delle exception
        DebugLogger.turnOffExceptionLog();
    }

    public ServerManager(int port, int maxGames, int maxClientsForGame,
                         int minClientsForGame) {
        this(port, maxGames, maxClientsForGame, minClientsForGame,
                DEFAULT_TIMEOUT_ACCEPT);
    }

    public ServerManager(int port, int maxGames, int maxClientsForGame) {
        this(port, maxGames, maxClientsForGame, DEFAULT_MIN_CLIENTS_FOR_GAME);
    }

    public ServerManager(int port) { //default constructor
        this(port, DEFAULT_MAX_GAMES, DEFAULT_MAX_CLIENTS_FOR_GAME);
    }

    public ServerManager(String serverName, int port) {
        this(serverName, port, DEFAULT_MIN_CLIENTS_FOR_GAME,
                DEFAULT_MAX_CLIENTS_FOR_GAME, DEFAULT_MAX_GAMES);
    }

    public ServerManager(String serverName, int port, int minClientsForGame,
                         int maxClientsForGame, int maxGames) {
        this.port = port;
        this.serverName = serverName;
        this.maxClientsForGame = maxClientsForGame;
        this.minClientsForGame = minClientsForGame;
        this.maxNumberOfGames = maxGames;
    }

    /**
     * Creates a serverSocket for the server and starts the clientHandling by
     * calling handleClientRequest
     */
    public void startServerSocket() {

        //cerco di tirare su il server
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            // porta non disponibile
            Logger.getLogger(DebugLogger.class.getName()).log(
                    Level.SEVERE,
                    "Impossibile creare il serverSocket " + e.getMessage(), e);
            return;
        }
        DebugLogger.println("Server pronto");
        //System.out.println("Server pronto");
        this.handleClientRequest();
    }

    public void startRMI() {
        try {
            //Creo una versione remota
            ServerRmi stub = (ServerRmi) UnicastRemoteObject.exportObject(this,
                    0);

            //Setto i player che aspettano di iniziare una partita a 0
            numberOfPlayers = 0;
            //Creo registro rmi nel quale caricare la mia istanza del server
            Registry registry = LocateRegistry.createRegistry(port);

            //Faccio il bind della mia istanza remota con un nome specifico
            registry.rebind(serverName, stub);

            System.out.println("ServerRmi caricato.");
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
        }

    }

    /**
     * Checks that there are enough clients for a game and starts it
     */
    private void startSocketGame() {
        //se ci sono abbastanza  giocatori
        if (this.clientNickNames.size() >= minClientsForGame) {
            DebugLogger.println(
                    "Avvio il gioco con " + clientNickNames.size() + " giocatori");

            //avvio il thread per gestire la partita
            executor.submit(new ServerThread(clientNickNames,
                    new SocketTrasmission()));

            //aumento i giochi attivi
            activatedGames++;

            System.out.println("Partita numero " + activatedGames + " avviata.");
        } else {
            //se non ci sono abbastanza giocatori
            handleClientRejection(
                    "Mi dispiace non ci sono abbastanza giocatori per una partita, riprovare più tardi.");
        }

        //comunque vada svuota la lista dei socket
        clientNickNames.clear();
    }

    private void startRmiGame() {
        if (numberOfPlayers >= minClientsForGame) {
            DebugLogger.println(
                    "Avvio il gioco con " + numberOfPlayers + " giocatori");
            executor.submit(new ServerThread(clientNickNames,
                    new RmiTrasmission()));
        }else{
        //TODO reject
        }
    }

    /**
     * It handles the connections to the serverSocket. It accepts a manimum of
     * clients for a game specified in the constants. It creates a thread for
     * every new game, when it reaches the maximum number of activated games it
     * rejects every client calling handleClientRejection
     */
    private void handleClientRequest() {

        while (true) {
            try {
                //accetto un client
                clientSocket = serverSocket.accept();

                //se non ho attivato tutte le partite
                if (ServerManager.activatedGames < maxNumberOfGames) {
                    DebugLogger.println("Chiedo nick");

                    if (isNewPlayer()) {
                        DebugLogger.println("Client accettato");

                        //se è il primo client
                        if (clientNickNames.size() == 1) {
                            timer = new Timer();

                            //avvio il timer
                            timer.startTimer();
                            DebugLogger.println("timer avviato");
                        } else if (clientNickNames.size() == maxClientsForGame) {
                            //se ho tutti i client per un game
                            timer.stopTimer();

                            //avvio il gioco
                            startSocketGame();
                        }
                    } else {
                        //TODO handle reconnection
                    }
                } else {
                    //se le partite attivate sono il massimo
                    DebugLogger.println("Client rifiutato");
                    handleClientRejection(
                            "Il server è pieno, riprova più tardi");
                }

            } catch (IOException ex) {
                //casini col server
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);
            }
        }

    }

    private boolean isNewPlayer() throws IOException {

        Scanner fromClient = new Scanner(clientSocket.getInputStream());
        PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream());

        toClient.println("Inserisci il tuo nickName: ");
        toClient.flush();

        String nickName = fromClient.nextLine();

        if (NickSocketMap.containsKey(nickName)) {
            DebugLogger.println("NickName alredy in use");
            return false;
        } else {
            //aggiungilo alla map
            NickSocketMap.put(nickName, new SocketClientProxy(clientSocket));
            //aggiungilo ai client in connessione
            clientNickNames.add(nickName);
            DebugLogger.println("nickName " + nickName + "added");
            return true;
        }

    }

    public int connect(ClientRmi client, String nickName) throws RemoteException {

        //se il client che tenta di connettersi non esiste
        if (!NickClientRmiMap.containsKey(nickName)) {

            //se ci sono partite da poter avviare
            if (activatedGames < maxNumberOfGames) {
                numberOfPlayers++;
                clientNickNames.add(nickName);
                NickClientRmiMap.put(nickName, new RmiClientProxy(client));
                //se è il primo player
                if (numberOfPlayers == 1) {
                    timer = new Timer();
                    timer.startTimer();
                    DebugLogger.println("timer avviato");

                } else if (numberOfPlayers == maxClientsForGame) {
                    //se non è il primo
                    timer.stopTimer();
                    startRmiGame();
                }

                //comunque vada lo mappo
            } else {
                //TODO disconnect client
            }
        }
        return 0;//FIXME!!
    }

    /**
     * Rejects clients when the number it isn't sufficient to start a game or
     * the server is full
     */
    private void handleClientRejection(String message) {
        DebugLogger.println("Rifiuto Client.");

        //per tutti i client
        for (Map.Entry pairs : NickSocketMap.entrySet()) {

            //provo ad avvisarli
            SocketClientProxy client = (SocketClientProxy) pairs.getValue();
            client.send(message);

        }
        //svuoto array socket
        clientNickNames.clear();
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
                if (rmi) {
                    startRmiGame();
                } else {
                    startSocketGame();
                }
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

    protected boolean removeNickName(String nickToRemove) {
        if (NickSocketMap.containsKey(nickToRemove)) {
            NickSocketMap.remove(nickToRemove);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Main method, creates a serverMangager and starts it
     *
     * @param args
     */
    public static void main(String[] args) {
        //creo un server su una certa porta
        ServerManager server;
        String answer;
        int choice;
        int port = 5050;
        String serverName = "sheepland";

        Scanner stdIn = new Scanner(System.in);
        boolean stringValid = false;

        while (!stringValid) {
            try {
                System.out.println(
                        "Scegli connessione:\n1- Socket\n2- RMI");
                answer = stdIn.nextLine();
                choice = Integer.parseInt(answer);

                if (choice == 1) {
                    stringValid = true;
                    rmi = false;
                    server = new ServerManager(port);
                    server.startServerSocket();
                } else if (choice == 2) {
                    stringValid = true;
                    rmi = true;
                    server = new ServerManager(serverName, port);
                    server.startRMI();
                } else {
                    System.out.println("La scelta inserita non è valida\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Scelta non valida\n");

            }
        }
        System.out.println("Server spento.");
    }

}
