package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
    private final int NUMBER_OF_TIMER = 1;
    private final int MILLISECONDS_IN_SECONDS = 1000;

    //costanti di default per i costruttori
    private static final int DEFAULT_TIMEOUT_ACCEPT = 10;
    private static final int DEFAULT_TIMEOUT_REFRESH = 10;
    private static final int DEFAULT_MIN_CLIENTS = 2;
    private static final int DEFAULT_MAX_GAMES = 3;
    private static final int DEFAULT_MAX_CLIENTS_FOR_GAME = 6;

    //variabili
    private final int port;
    private final int maxNumberOfGames;
    private final int maxClientsForGame;
    private final int minClientsForGame;
    static int activatedGames = 0; //variabile che tiene conto delle partite avviate, modificabile da ogni thread

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
        System.out.println("Server pronto");
        this.handleClientRequest(serverSocket);
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
    private void handleClientRequest(ServerSocket serverSocket) {
        ArrayList<Socket> clientSockets;
        ExecutorService executor = Executors.newCachedThreadPool();
        while (true) {
            try {//TODO implementare il timeOut anche come processo!
                clientSockets = timedOutAccept(serverSocket, timeoutAccept,
                        maxClientsForGame);
                //a questo punto ho la lista dei client per una partita
                if (clientSockets.size() >= minClientsForGame) {
                    executor.submit(new ServerThread(clientSockets));
                    System.out.println("Partita avviata.");
                    activatedGames++; //essendo static questa variabile potra essere decrementata
                    //dai thread appena prima di terminare

                    //serverExecutorPool.submit( new ServerThread(clientSockets) );
                    //sta riga serve se uso la parte commentata sopra come executor             
                } else {
                    System.out.print(
                            "Non ci sono abbastanza client per una partita.");
                }
                while (activatedGames >= maxNumberOfGames) { //finchè un thread non mi cambia il valore di activatedGames
                    //messaggio di rifiuto delle connessioni
                    this.handleClientRejection(serverSocket);

                }
            } catch (IOException e) {
                break; //TODO: che faccio ?entro qui quando il serverSocket è chiuso
            }
        }
        executor.shutdown();
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

    /**
     * puramete un metodo di prova per testare la connessione con il client
     */
    public void provaServer() {
        ServerSocket serverSocket;
        //cerco di tirare su il server
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // porta non disponibile
            return;
        }
        System.out.println("Server ready");
        Socket socketProva = new Socket();

        try {
            socketProva = serverSocket.accept();

            System.out.println("ClientConnected");
            Scanner socketIn = new Scanner(socketProva.getInputStream());//input stream
            PrintWriter socketOut = new PrintWriter(socketProva.
                    getOutputStream());//outputstream
            String clientLine = socketIn.nextLine();
            System.out.println("Mesaggio ricevuto " + clientLine);
            socketOut.println(clientLine);
            socketOut.flush();
            System.out.println("Messsaggio inviato indietro");
        } catch (IOException ex) {
            Logger.getLogger(ServerManager.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        ServerManager server = new ServerManager(5050);
        server.startServer();
    }
}
