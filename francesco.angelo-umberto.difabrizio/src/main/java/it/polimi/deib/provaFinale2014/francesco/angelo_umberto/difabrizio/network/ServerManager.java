package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
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

//    private final int SECONDS_BEFORE_IDLE_THREAD_SHUTDOWN = 10; 
    //puramente indicativo, tanto ho messo threadcore = al massimo dei thread attivi
    //serve solo con ThreadPoolExecutor
//    private final int MAX_QUEUED_GAMES = 2; //serve solo per ThreadPoolExecutor
    private final int secondsBeforeAcceptTimeout; //modifica qui per cambiare il timeout della accept per la connessione ad una partita
    private final int secondsBeforeRefreshNumberOfGamesActive; //modifica qui per cambiare la frequenza con cui viene controllata quante partite sono attive
    private final int timeoutRefreshNumberOfGames;
    private final int timeoutAccept;

    //constanti generiche
    private final int MILLISECONDS_IN_SECONDS = 1000;

    //costanti di default per i costruttori
    private static final int DEFAULT_TIMEOUT_ACCEPT = 13;
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

    ArrayList<Socket> clientSockets = new ArrayList<Socket>();
    ExecutorService executor = Executors.newCachedThreadPool();
    /**
     * Creo un logger per il sistema
     */
    private final static Logger logger = Logger.getLogger(
            ServerManager.class.getName());
    /**
     * Creo una console per il logger
     */
    ConsoleHandler console = new ConsoleHandler();

    public ServerManager(int port, int maxGames, int maxClientsForGame,
                         int minClientsForGame, int acceptTimeout,
                         int refreshTimeout) {
        this.maxNumberOfGames = maxGames;
        this.maxClientsForGame = maxClientsForGame;
        this.minClientsForGame = minClientsForGame;
        this.secondsBeforeRefreshNumberOfGamesActive = refreshTimeout;
        this.secondsBeforeAcceptTimeout = acceptTimeout;
        this.timeoutAccept = secondsBeforeAcceptTimeout * MILLISECONDS_IN_SECONDS;
        this.timeoutRefreshNumberOfGames = secondsBeforeRefreshNumberOfGamesActive * MILLISECONDS_IN_SECONDS;
        //setta la porta del server 
        this.port = port;
        //setto il livello della console
//        console.setLevel(Level.OFF); //lui decide cosa dare in output del loggato
//        
//     
//        //aggiungo la console al logger
//        logger.addHandler(console);        
        logger.setLevel(Level.INFO); //lui decide cosa loggare

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
            System.err.println(e.getMessage()); // porta non disponibile
            return;
        }
        logger.info("Serever pronto");
        //System.out.println("Server pronto");
        this.handleClientRequest(serverSocket);
    }

    private void startGame() {
        //se ci sono abbastanza  giocatori
        if (this.clientSockets.size() >= minClientsForGame) {
            logger.info(
                    "Avvio il gioco con " + clientSockets.size() + " giocatori");
            //avvio il thread per gestire la partita
            executor.submit(new ServerThread(clientSockets));

            //aumento i giochi attivi
            activatedGames++;

            System.out.println("Partita numero " + activatedGames + " avviata.");

        } else {//se non ci sono abbastanza giocatori
            logger.info("Rifiuto Client, pochi giocatori.");
            //per tutti i client
            for (Socket client : clientSockets) {
                PrintWriter toClient;

                //provo ad avvisarli
                try {
                    toClient = new PrintWriter(client.getOutputStream());
                    toClient.println(
                            "Mi dispiace non ci sono abbastanza giocatori per una partita, riprovare più tardi.");
                    toClient.flush();
                } catch (IOException ex) {
                    //Il client a cui stavo per dire che non può giocare si è già disconnesso, stica.
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
                if (this.activatedGames < maxNumberOfGames) {
                    logger.info("Client accettato");
                    //se è il primo client
                    if (clientSockets.size() == 1) {

                        //ne creo un altro
                        timer = new Timer();

                        //avvio il timer
                        timer.startTimer();
                        logger.info("timer avviato");
                    }

                    //se ho tutti i client per un game
                    if (clientSockets.size() == maxClientsForGame) {
                        //fermo il timer
                        timer.stopTimer();

                        //avvio il gioco
                        startGame();
                    }
                } else {//se le partite attivate sono il massimo
                    logger.info("Client rifiutato");
                    handleClientRejection(clientSockets.get(0));
                }
            } catch (IOException ex) {
                //casini col server
            }
        }

    }

    private class Timer implements Runnable {

        private Thread myThread;

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

                logger.info("Timer finito");
                //se finisce avvio game
                startGame();

            } catch (InterruptedException ex) {
                //se blocco il timer io non succede niente, muori e basta.
            }
        }

        public void stopTimer() {
            this.myThread.interrupt();
            logger.info("Timer fermato");

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
        }

    }

    public static void main(String[] args) {
        ServerManager server = new ServerManager(5050);
        server.startServer();
    }
}
