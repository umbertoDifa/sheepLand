package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
public class ServerManager {

    /**
     * It contains the seconds that the timeout waits before interrupting the
     * process that waits for client's connections
     */
    private final int secondsBeforeAcceptTimeout;

    /**
     * Timeout in milliseconds for the client's connections
     */
    private final int timeoutAccept;

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
    private static final int DEFAULT_MIN_CLIENTS = 2;
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
     * Connection port of the server
     */
    private final int port;
    private final int maxNumberOfGames;
    private final int maxClientsForGame;
    private final int minClientsForGame;
    /**
     * It represents the number of active games. Since it's static it can be
     * modified by any thread which decrements it before dying
     */
    static int activatedGames = 0;

    /**
     * The socket of the server
     */
    ServerSocket serverSocket;

    /**
     * The list of clients connecting to a certain game
     */
    List<Socket> clientSockets = new ArrayList<Socket>();
    /**
     * Executes the threads which manage the games
     */
    ExecutorService executor = Executors.newCachedThreadPool();

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
        this(port, maxGames, maxClientsForGame, DEFAULT_MIN_CLIENTS);
    }

    public ServerManager(int port) { //default constructor
        this(port, DEFAULT_MAX_GAMES, DEFAULT_MAX_CLIENTS_FOR_GAME);
    }

    /**
     * Creates a serverSocket for the server and starts the clientHandling by
     * calling handleClientRequest
     */
    public void startServer() {

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

    /**
     * Checks that there are enough clients for a game and starts it
     */
    private void startGame() {
        //se ci sono abbastanza  giocatori
        if (this.clientSockets.size() >= minClientsForGame) {
            DebugLogger.println(
                    "Avvio il gioco con " + clientSockets.size() + " giocatori");

            //avvio il thread per gestire la partita
            executor.submit(new ServerThread(clientSockets));

            //aumento i giochi attivi
            activatedGames++;

            System.out.println("Partita numero " + activatedGames + " avviata.");
        } else {
            //se non ci sono abbastanza giocatori
            handleClientRejection(
                    "Mi dispiace non ci sono abbastanza giocatori per una partita, riprovare più tardi.");
        }

        //comunque vada svuota la lista dei socket
        clientSockets.clear();
    }

    /**
     * It handles the connections to the serverSocket. It accepts a manimum of
     * clients for a game specified in the constants. It creates a thread for
     * every new game, when it reaches the maximum number of activated games it
     * rejects every client calling handleClientRejection
     */
    private void handleClientRequest() {
        //creo un timer
        Timer timer = new Timer();

        while (true) {
            try {
                //accetto un client
                clientSockets.add(serverSocket.accept());

                //se non ho attivato tutte le partite
                if (ServerManager.activatedGames < maxNumberOfGames) {
                    DebugLogger.println("Client accettato");
                    //se è il primo client
                    if (clientSockets.size() == 1) {

                        //ne creo un altro
                        timer = new Timer();

                        //avvio il timer
                        timer.startTimer();
                        DebugLogger.println("timer avviato");
                    }

                    //se ho tutti i client per un game
                    if (clientSockets.size() == maxClientsForGame) {
                        //fermo il timer
                        timer.stopTimer();

                        //avvio il gioco
                        startGame();
                    }
                } else {
                    //se le partite attivate sono il massimo
                    DebugLogger.println("Client rifiutato");
                    handleClientRejection("Il server è pieno, riprova più tardi");
                }
            } catch (IOException ex) {
                //casini col server
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);
            }
        }

    }

    /**
     * Rejects clients when the number it isn't sufficient to start a game
     * or the server is full
     */
    private void handleClientRejection(String message) {
        DebugLogger.println("Rifiuto Client.");

        //per tutti i client
        for (Socket client : clientSockets) {
            PrintWriter toClient;

            try {
                //provo ad avvisarli
                toClient = new PrintWriter(client.getOutputStream());
                toClient.println(message);
                toClient.flush();
                //TODO: questa close fa crashare il client che la riceve...
                toClient.close();
            } catch (IOException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);
                //Il client a cui stavo per dire che non può giocare si è già disconnesso, stica.
                //TODO giusto?
            }
        }
        //svuoto array socket
        clientSockets.clear();
    }
    
    /**
     * The timer that starts when the first client of a game 
     * connects to the server
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
    
    /**
     * Main method, creates a serverMangager and starts it
     * @param args 
     */
    public static void main(String[] args) {
        ServerManager server = new ServerManager(5050);
        server.startServer();
    }
}
