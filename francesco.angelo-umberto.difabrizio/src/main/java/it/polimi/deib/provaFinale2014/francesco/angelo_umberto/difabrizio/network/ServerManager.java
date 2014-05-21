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

            //avvio il thread per gestire la partita
            executor.submit(new ServerThread(clientSockets));
            System.out.println("Partita avviata.");

            //aumento i giochi attivi
            activatedGames++;

        } else {//se non ci sono abbastanza giocatori

            //per tutti i client
            for (Socket client : clientSockets) {
                PrintWriter toClient;

                //provo ad avvisarli
                try {
                    toClient = new PrintWriter(client.getOutputStream());
                    toClient.println(
                            "Mi dispiace non ci sono abbastanza giocatori per una partita, riprovare più tardi.");
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
        while (true) {
            try {
                //aspetto la prima accept
                clientSockets.add(serverSocket.accept());

                logger.info("Client accettato");
                //faccio partire il thread timer, ogni volta diverso perchè non
                //posso chiamare la run due volte sullo stesso thread
                Timer timer = new Timer();

                //setto a false il timeout
                timeout = false;

                //avvio il timer
                timer.startTimer();
                logger.info("timer avviato");
                //accetto le connessioni mentre il timer non è scaduto
                for (int i = 0; i < maxClientsForGame - 1 && !timeout; i++) {
                    clientSockets.add(serverSocket.accept()); //aspetto l'isemo client
                }

                //se raggiungo il numero massimo di player prima della fine del timeout
                if (!timeout) {
                    //uccido il processo di timeout
                    timer.stopTimer();
                }

                //avvio il gioco
                startGame();

                //finchè un thread non mi cambia il valore di activatedGames
                while (activatedGames >= maxNumberOfGames) {
                    //messaggio di rifiuto delle connessioni
                    this.handleClientRejection(serverSocket);
                }
                //ricomincio ad accettare connessioni
            } catch (IOException ex) {
                //cose andate male nella connessione
                System.err.println(ex.getMessage());
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
                synchronized (this) {
                    myThread.wait(timeoutAccept);
                }
                //se finisce
                //fermo l'accettazione dei client
                timeout = true;

            } catch (InterruptedException ex) {
                //se blocco il timer io non succede niente, muori e basta.
            }
        }

        public void stopTimer() {
            this.myThread.interrupt();
        }

    }

    /**
     * se arriva una connessione al server entro un timeout tale connessione
     * viene rifiutata avvisando il client con un messaggio
     *
     * @param server
     */
    private void handleClientRejection(ServerSocket server) {
        Socket rejectedSocket = new Socket(); //controllo se qualche client vuole connettersi
        try {
            server.setSoTimeout(timeoutRefreshNumberOfGames); //imposto timer per accept
            rejectedSocket = server.accept();// lo accetto
            PrintWriter socketOut = new PrintWriter(rejectedSocket.
                    getOutputStream());//creo l'output stream verso quel client
            socketOut.println("Il server è pieno, riprova più tardi");
        } catch (SocketTimeoutException e) {
            //refresh activatedGames
            return;
        } catch (IOException e) {
            System.err.println(e.getMessage()); // errore di connessione col client
            return;
        }
    }

    /**
     * colleziona tutte le connessioni possibili a un server in un certo timeout
     * ma non più di maxConnections
     *
     * @param server
     * @param timeout
     * @param maxConnections
     *
     * @return la lista di client accettati, ce n'è almeno uno perchè la prima
     *         accept non ha un timeout
     */
    private ArrayList<Socket> timedOutAccept(ServerSocket server, int timeout,
                                             int maxConnections)
            throws IOException {
        ArrayList<Socket> socketList = new ArrayList<Socket>();
        long startTime, endTime;

        try {
            server.setSoTimeout(0); //imposto timeout infinito
            socketList.add(server.accept()); // aspetto il primo client
            server.setSoTimeout(timeout); // imposto il primo timeout intero
            startTime = System.currentTimeMillis(); //prendo il timestamp del primo client
            for (int i = 0; i < maxConnections - 1; i++) {
                socketList.add(server.accept()); //aspetto l'isemo o una exception del timer
                endTime = System.currentTimeMillis();
                if (timeout - (endTime - startTime) > 0) //se ho ancora tempo
                {
                    server.setSoTimeout((int) (timeout - (endTime - startTime))); //aggiorno il timeout sottraendo il tempo aspettato per l'isemo clientù
                } //TODO warn: forse alla riga sopra ci potrebbe essere un errore per la sottrazione fra int e long anche se la differenza dei long dovrebbe essere molto piccola, test?
                else {
                    break; //altrimenti finisco
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout connessioni!");
            server.setSoTimeout(0); //Resetto il timeout ad un tempo infinito, così il prossimo gruppo di client non avrà il timer
            //per il primo così come succede la prima volta
            return socketList;
        }
        return socketList;
    }

    public static void main(String[] args) {
        ServerManager server = new ServerManager(5050);
        server.startServer();
    }
}
