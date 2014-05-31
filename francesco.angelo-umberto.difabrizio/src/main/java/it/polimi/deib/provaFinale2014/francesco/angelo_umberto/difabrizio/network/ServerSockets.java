package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.GameManager;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerSockets implements Runnable {

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

    private final int maxClientsForGame;
    private final int minClientsForGame;

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
    /**
     * Thread timer
     */
    private Timer timer = new Timer();

    private final int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    /**
     * Lista dei nickNames dei client che sono in coda per iniziare una partita
     */
    private List<String> clientNickNames = new ArrayList<String>();

    /**
     * It represents the number of active games. Since it's static it can be
     * modified by any thread which decrements it before dying
     */
    protected static int activatedGames = 0;

    private final int maxNumberOfGames;
    /**
     * Executes the threads which manage the games
     */
    ExecutorService executor = Executors.newCachedThreadPool();

    protected static HashMap<String, SocketClientProxy> NickSocketMap = new HashMap<String, SocketClientProxy>();

    /**
     * Thread del server
     */
    Thread myThread;

    public ServerSockets(int port) {
        myThread = new Thread(this);

        this.port = port;

        this.maxNumberOfGames = DEFAULT_MAX_GAMES;
        this.maxClientsForGame = DEFAULT_MAX_CLIENTS_FOR_GAME;
        this.minClientsForGame = DEFAULT_MIN_CLIENTS_FOR_GAME;
        this.secondsBeforeAcceptTimeout = DEFAULT_TIMEOUT_ACCEPT;
        this.timeoutAccept = secondsBeforeAcceptTimeout * MILLISECONDS_IN_SECONDS;

        //turn off debug
        DebugLogger.turnOffExceptionLog();

    }

    public void start() {
        myThread.start();
    }

    public void run() {
        this.startServer();
    }

    private void startServer() {

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
                //prendo il nick dal client
                DebugLogger.println("Chiedo nick");
                String nickName = getNickName();

                //se non ho attivato tutte le partite
                if (activatedGames < maxNumberOfGames) {

                    if (isNewPlayer(nickName)) {
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
                            startGame();
                        }
                    } else {
                        //TODO handle reconnection
                    }
                } else {
                    //se le partite attivate sono il massimo
                    DebugLogger.println("Client rifiutato");
                    handleClientRejection(
                            "Il server è pieno, riprova più tardi");
                    clientNickNames.clear();

                }

            } catch (IOException ex) {
                //casini col server
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);
            }
        }

    }

    /**
     * Checks that there are enough clients for a game and starts it
     */
    private void startGame() {
        //se ci sono abbastanza  giocatori
        if (this.clientNickNames.size() >= minClientsForGame) {
            DebugLogger.println(
                    "Avvio il gioco con " + clientNickNames.size() + " giocatori");

            //avvio il thread per gestire la partita
            executor.submit(new GameManager(clientNickNames,
                    new SocketTrasmission()));

            //aumento i giochi attivi
            activatedGames++;

            System.out.println("Partita numero " + activatedGames + " avviata.");
        } else {
            //se non ci sono abbastanza giocatori
            handleClientRejection(
                    "Mi dispiace non ci sono abbastanza giocatori per una partita, riprovare più tardi.");
            //elimina il loro record dai giocatori attivi
            for (String client : clientNickNames) {
                NickSocketMap.remove(client);
            }

        }
        //comunque vada svuota la lista dei socket
        clientNickNames.clear();

        DebugLogger.println("Lista client:" + clientNickNames.toString());
    }

    private String getNickName() throws IOException {
        Scanner fromClient = new Scanner(clientSocket.getInputStream());
        return fromClient.nextLine();
    }

    private boolean isNewPlayer(String nickName) {

        if (NickSocketMap.containsKey(nickName)) {
            DebugLogger.println("NickName alredy in use");
            return false;
        } else {
            //aggiungilo alla map
            NickSocketMap.put(nickName, new SocketClientProxy(clientSocket));
            //aggiungilo ai client in connessione
            clientNickNames.add(nickName);
            DebugLogger.println("nickName " + nickName + " added");
            return true;
        }

    }

    /**
     * Rejects clients when the number it isn't sufficient to start a game or
     * the server is full
     */
    private void handleClientRejection(String message) {
        DebugLogger.println("Rifiuto Client.");

        //per tutti i client
        for (Map.Entry pairs : NickSocketMap.entrySet()) {
            //se il loro nick è tra quelli in lista di attesa
            String nick = (String) pairs.getKey();
            if (clientNickNames.contains(nick)) {
                SocketClientProxy client = (SocketClientProxy) pairs.getValue();
                client.send(message);
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
