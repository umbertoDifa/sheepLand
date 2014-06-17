package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.GameManager;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server that deals with sockets connections
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerSockets implements Runnable {

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

    private final int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    /**
     * Lista dei nickNames dei client che sono in coda per iniziare una partita
     */
    private List<String> clientNickNames = new ArrayList<String>();

    private final PrintWriter stdOut = new PrintWriter(System.out);

    private final int maxNumberOfGames;
    /**
     * Executes the threads which manage the games
     */
    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Thread del server
     */
    private final Thread myThread;

    /**
     * create a server binded on a certain port
     *
     * @param port Port to bind the server
     */
    public ServerSockets(int port) {
        myThread = new Thread(this);

        this.port = port;

        this.maxNumberOfGames = NetworkConstants.DEFAULT_MAX_GAMES.getValue();
        this.maxClientsForGame = NetworkConstants.DEFAULT_MAX_CLIENTS_FOR_GAME.getValue();
        this.minClientsForGame = NetworkConstants.DEFAULT_MIN_CLIENTS_FOR_GAME.getValue();
        this.timeoutAccept = NetworkConstants.DEFAULT_TIMEOUT_ACCEPT.getValue() * NetworkConstants.MILLISECONDS_IN_SECONDS.getValue();

        //turn off debug
        DebugLogger.turnOffExceptionLog();

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
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            // porta non disponibile
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "Impossibile creare il serverSocket " + e.getMessage(), e);
            return;
        }

        stdOut.println("Server pronto");
        stdOut.flush();

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

                if (nickName != null && isNewPlayer(nickName)) {

                    //se non ho attivato tutte le partite
                    if (ServerManager.activatedGames < maxNumberOfGames) {
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
                        //se le partite attivate sono il massimo
                        DebugLogger.println("Client rifiutato");

                        // rifiuto client
                        handleClientRejection(
                                "Ci sono troppe partite attive riprovare più tardi");
                        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                                nickName)).getSocket().close();
                        ServerManager.NICK_2_CLIENT_PROXY_MAP.remove(nickName);
                        clientNickNames.clear();

                    }
                } else if (nickName != null) {
                    if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName).isOnline()) {
                        DebugLogger.println("Client già in gioco");
                        //rigettalo
                        rejectNameSake(clientSocket);
                    } else {
                        //il client deve riconnettersi
                        //rimuovo la vecchia instanza
                        DebugLogger.println("Client tenta di riconnettersi");
                        //salvo se aveva pastori da mettere
                        int shepherdToSet = ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                                nickName).getNumberOfShepherdStillToSet();

                        ServerManager.NICK_2_CLIENT_PROXY_MAP.remove(nickName);

                        //metto il nuovo
                        ServerManager.NICK_2_CLIENT_PROXY_MAP.put(nickName,
                                new SocketClientProxy(clientSocket));

                        //setto il refresh
                        ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName).setRefreshNeeded(
                                true);

                        ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName).setNumberOfShepherdStillToSet(
                                shepherdToSet);
                        DebugLogger.println(
                                nickName + " settato pastori da mettere a " + shepherdToSet);
                    }
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
            ServerManager.activatedGames++;

            stdOut.println(
                    "numero di partite attive " + ServerManager.activatedGames);
            stdOut.flush();
        } else {
            //se non ci sono abbastanza giocatori
            handleClientRejection(
                    "Mi dispiace non ci sono abbastanza giocatori per una partita, riprovare più tardi.");
            //elimina il loro record dai giocatori attivi
            for (String client : clientNickNames) {
                ServerManager.NICK_2_CLIENT_PROXY_MAP.remove(client);
            }

        }
        //comunque vada svuota la lista dei socket
        clientNickNames.clear();

        DebugLogger.println("Lista client:" + clientNickNames.toString());
    }

    private String getNickName() throws IOException {
        Scanner fromLastClient = new Scanner(clientSocket.getInputStream());
        try {
            return fromLastClient.nextLine();
        } catch (NoSuchElementException ex) {
            //il client che doveva darmi il nick si disconnette
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return null;
        }

    }

    /**
     * Controlla se il player è gia in gioco, se è nuovo lo aggiunge ai player
     * in lista
     *
     * @param nickName
     *
     * @return true se nuovo giocatore, false altrimenti
     */
    private boolean isNewPlayer(String nickName) {

        if (ServerManager.NICK_2_CLIENT_PROXY_MAP.containsKey(nickName)) {
            return false;
        } else {
            //aggiungilo alla map
            ServerManager.NICK_2_CLIENT_PROXY_MAP.put(nickName,
                    new SocketClientProxy(clientSocket));
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
        for (Map.Entry pairs : ServerManager.NICK_2_CLIENT_PROXY_MAP.entrySet()) {
            //se il loro nick è tra quelli in lista di attesa
            String nick = (String) pairs.getKey();
            if (clientNickNames.contains(nick)) {
                SocketClientProxy client = (SocketClientProxy) pairs.getValue();
                client.send(message);
                try {
                    client.getSocket().close();
                } catch (IOException ex) {
                    //se non riesco a chiudere un socket con un client che dovevo
                    //rifiutare poco male
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);
                }
            }

        }
    }

    private void rejectNameSake(Socket client) {
        PrintWriter toClient;
        try {
            toClient = new PrintWriter(client.getOutputStream());
            toClient.println(
                    "Il tuo nickName non è valido, c'è un altro giocatore con lo stesso nick");
            toClient.flush();
            client.close();
        } catch (IOException ex) {
            //poco male il client si è disconnesso prima di ricevere il rifiuto
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
        }

    }

    /**
     * The timer that starts when the first client of a game connects to the
     * server
     */
    private class Timer implements Runnable {

        private final Thread thread;

        /**
         * Creates a timer
         */
        public Timer() {
            this.thread = new Thread(this);
        }

        /**
         * Starts the timer
         */
        public void startTimer() {
            this.thread.start();
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
            this.thread.interrupt();
            DebugLogger.println("Timer fermato");

        }

    }

}
