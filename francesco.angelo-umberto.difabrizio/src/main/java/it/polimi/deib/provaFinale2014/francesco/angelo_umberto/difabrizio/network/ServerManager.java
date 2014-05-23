package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.DebugLogger;
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
 * manager che tiene in vita il server e di volta in volta avvia un thread per
 * gestire una nuova partita. implementa un timeout(secondi) di attesa per le
 * connessioni ad ogni partita e un timeout(secondi) per il refresh delle numero
 * delle partite garantendo un massimo di partite avviate.
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerManager {

    //modifica qui per cambiare il timeout della accept per la connessione ad una partita
    private final int secondsBeforeAcceptTimeout;

    //modifica qui per cambiare la frequenza con cui viene controllata quante partite sono attive
    private final int secondsBeforeRefreshNumberOfGamesActive;
    private final int timeoutAccept;

    //constanti generiche
    private final int MILLISECONDS_IN_SECONDS = 1000;

    //costanti di default per i costruttori
    private static final int DEFAULT_TIMEOUT_ACCEPT = 10;
    private static final int DEFAULT_TIMEOUT_REFRESH = 10;
    private static final int DEFAULT_MIN_CLIENTS = 2;
    private static final int DEFAULT_MAX_GAMES = 3;
    private static final int DEFAULT_MAX_CLIENTS_FOR_GAME = 4;

    //variabili
    private final int port;
    private final int maxNumberOfGames;
    private final int maxClientsForGame;
    private final int minClientsForGame;
    /**
     * Variabile che tiene conto delle partite avviate. Essendo static questa
     * variabile potra essere decrementata dai thread appena prima di terminare.
     */
    static int activatedGames = 0;
    /**
     * Indica se è scattato il timeout nel thread Timer
     */
    boolean timeout;

    List<Socket> clientSockets = new ArrayList<Socket>();
    ExecutorService executor = Executors.newCachedThreadPool();

    public ServerManager(int port, int maxGames, int maxClientsForGame,
                         int minClientsForGame, int acceptTimeout,
                         int refreshTimeout) {
        this.maxNumberOfGames = maxGames;
        this.maxClientsForGame = maxClientsForGame;
        this.minClientsForGame = minClientsForGame;
        this.secondsBeforeRefreshNumberOfGamesActive = refreshTimeout;
        this.secondsBeforeAcceptTimeout = acceptTimeout;
        this.timeoutAccept = secondsBeforeAcceptTimeout * MILLISECONDS_IN_SECONDS;
        //setta la porta del server 
        this.port = port;
    }

    public ServerManager(int port, int maxGames, int maxClientsForGame,
                         int minClientsForGame) {
        this(port, maxGames, maxClientsForGame, minClientsForGame,
                DEFAULT_TIMEOUT_ACCEPT, DEFAULT_TIMEOUT_REFRESH);
    }

    public ServerManager(int port, int maxGames, int maxClientsForGame) {
        this(port, maxGames, maxClientsForGame, DEFAULT_MIN_CLIENTS);
    }

    public ServerManager(int port) { //default constructor
        this(port, DEFAULT_MAX_GAMES, DEFAULT_MAX_CLIENTS_FOR_GAME);
    }

    /**
     * crea un socket server e avvia handleClientRequest che gestirà le
     * connessioni
     */
    public void startServer() {
        ServerSocket serverSocket;

        //cerco di tirare su il server
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            // porta non disponibile
            Logger.getLogger(ServerManager.class.getName()).log(
                    Level.SEVERE, e.getMessage(), e);
            return;
        }
        DebugLogger.println("Server pronto");
        //System.out.println("Server pronto");
        this.handleClientRequest(serverSocket);
    }

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
            DebugLogger.println("Rifiuto Client, pochi giocatori.");

            //per tutti i client
            for (Socket client : clientSockets) {
                PrintWriter toClient;

                try {
                    //provo ad avvisarli

                    toClient = new PrintWriter(client.getOutputStream());
                    toClient.println(
                            "Mi dispiace non ci sono abbastanza giocatori per una partita, riprovare più tardi.");
                    toClient.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ServerManager.class.getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);
                    //Il client a cui stavo per dire che non può giocare si è già disconnesso, stica.
                    //TODO giusto?
                }

            }
        }
        //comunque vada svuota la lista dei socket

        clientSockets.removeAll(clientSockets);
    }

    /**
     * si occupa di gestire le connessioni al serverSocket secondo la politica
     * del caso. Accetta un massimo di client per partita specificato nelle
     * costanti della classe accetta un massimo di partite specificato nelle
     * costanti della classe per ogni partita accettata avvia un thread che si
     * occupa di gestirla quando è pieno imposta il rifiuto delle connessioni
     * gestite col metodo handleClientRejection
     *
     * @param serverSocket
     */
    //TODO chiedere il doppio processo con il problema del kill simultaneo
    private void handleClientRequest(ServerSocket serverSocket) {
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
                    handleClientRejection(clientSockets.get(0));
                }
            } catch (IOException ex) {
                //casini col server
                Logger.getLogger(ServerManager.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);
            }
        }

    }

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
                Logger.getLogger(ServerManager.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);
            }
        }

        public void stopTimer() {
            this.myThread.interrupt();
            DebugLogger.println("Timer fermato");

        }

    }

    /**
     * se arriva una connessione al server entro un timeout tale connessione
     * viene rifiutata avvisando il client con un messaggio
     *
     * @param server
     */
    private void handleClientRejection(Socket client) {
        try {
            //svuoto array socket
            clientSockets.removeAll(clientSockets);

            //creo l'output stream verso quel client
            PrintWriter socketOut = new PrintWriter(client.
                    getOutputStream());

            //invio messaggio di informazione
            socketOut.println("Il server è pieno, riprova più tardi");
            socketOut.flush();
        } catch (IOException ex) {
            //il client si è disconnesso prima di sapere che tanto non poteva 
            //giocare stica
            Logger.getLogger(ServerManager.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public static void main(String[] args) {
        ServerManager server = new ServerManager(5050);
        server.startServer();
    }
}
