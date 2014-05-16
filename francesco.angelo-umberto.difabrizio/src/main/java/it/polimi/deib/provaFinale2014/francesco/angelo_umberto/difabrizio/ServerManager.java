package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * manager che tiene in vita il server e di volta in volta avvia un thread per
 * gestire una nuova partita
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerManager {

    //constanti
    private final int MAX_NUMBER_OF_GAMES = 3;
    private final int MAX_CLIENTS_FOR_GAME = 6;
    private final int MIN_CLIENTS_FOR_GAME = 2;
    private final int MAX_NUMBER_OF_CLIENT_SERVER_CONNECTIONS_AVAILABLE;
    private final int SECONDS_BEFORE_IDLE_THREAD_SHUTDOWN = 10; //TODO: boh, puramente indicativo, tanto ho messo threadcore = al massimo dei thread attivi

    private final int MAX_QUEUED_GAMES = 2;

    private final int SECONDS_BEFORE_ACCEPT_TIMEOUT = 20;
    private final int MILLISECONDS_IN_SECONDS = 1000;
    private final int TIMEOUT_ACCEPT = SECONDS_BEFORE_ACCEPT_TIMEOUT * MILLISECONDS_IN_SECONDS; //TODO: controlla sto dato
    private final int NUMBER_OF_TIMER = 1;
    //variabili
    private final int port;
    static int activatedGames = 0;

    //TODO: overlodare il server manager per avere come parametri MAX_NUMBER_OF_GAMES e MAX_NUMBER_OF_CLIENTS_FOR_GAME
    public ServerManager(int port) {
        this.MAX_NUMBER_OF_CLIENT_SERVER_CONNECTIONS_AVAILABLE = MAX_NUMBER_OF_GAMES * MAX_CLIENTS_FOR_GAME;
        //il costruttore setta la porta del server 
        this.port = port;
    }

    public void startServer() {
        ServerSocket serverSocket;
        //cerco di tirare su il server
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            //TODO: vedere come esce sto errore
            System.err.println(e.getMessage()); // porta non disponibile
            return;
        }
        System.out.println("Server ready");
        this.handleClientRequest(serverSocket);
    }

    private void handleClientRequest(ServerSocket serverSocket) {
        ArrayList<Socket> clientSockets;
        ExecutorService executor = Executors.newCachedThreadPool();
//        //********* Tutta questa parte può essere usata per gestire il numero di serverThread creata ma è un bazooca
//        //TODO: forse tutta sta parte di executor è barocca, magari posso facilmente usare solo le slide
//        //creo il mio handler per il rifiuto dei client
//        ClientRejectionHandler rejectionHandler = new ClientRejectionHandler();
//        //questa factory crea un thread quando necessario e se può li riusa
//        ThreadFactory threadFactory = Executors.defaultThreadFactory();
//        // esecutore che si occupa ti avviare il thread di gestione di un gioco
//        ThreadPoolExecutor serverExecutorPool
//                = new ThreadPoolExecutor(MAX_NUMBER_OF_GAMES,
//                        MAX_NUMBER_OF_GAMES,
//                        SECONDS_BEFORE_IDLE_THREAD_SHUTDOWN,
//                        TimeUnit.SECONDS,
//                        new ArrayBlockingQueue<Runnable>(MAX_QUEUED_GAMES),
//                        threadFactory,
//                        rejectionHandler);
        while (true) {
            try {
                clientSockets = timedOutAccept(serverSocket, TIMEOUT_ACCEPT,
                        MAX_CLIENTS_FOR_GAME);
                //a questo punto ho la lista dei client per una partita
                if (clientSockets.size() >= MIN_CLIENTS_FOR_GAME) {
                    executor.submit(new ServerThread(clientSockets));
                    activatedGames++; //essendo static questa variabile potra essere decrementata
                    //dai thread appena prima di terminare

                    //TODO: serverExecutorPool.submit( new ServerThread(clientSockets) );
                    //sta riga serve se uso la parte commentata sopra come executor             
                }
                while (activatedGames >= MAX_NUMBER_OF_GAMES) {
                    //TODO: messaggio di rifiuto delle connessioni
                    //finchè un thread non mi cambia il valore di activatedGames
                }
            } catch (IOException e) {
                break; //entro qui quando il serverSocket è chiuso
            }
        }
        executor.shutdown();
    }

    private void handleClientRequestSecondVersion(ServerSocket serverSocket) {//TODO:completamente non funzionante
        ArrayList<Socket> clientSockets = new ArrayList<Socket>();
        ExecutorService serverExecutor = Executors.newCachedThreadPool();
        Future future; // variabile che servirà a collezionare l'exception lanciata dal timer
        ExecutorService timerExecutor = Executors.newFixedThreadPool(NUMBER_OF_TIMER);
        
        while (true) {
            try {
                clientSockets.add( serverSocket.accept() );//aspetto il primo client
                //avvio il thread del timer
                future = timerExecutor.submit(new TimerCallable(TIMEOUT_ACCEPT));   
                //inizio la lettura di max 6 client
                for(int i = 0; i < MAX_CLIENTS_FOR_GAME - 1; i++){
                clientSockets.add( serverSocket.accept() ); //aggiungo man mano i client che si connetton
                }
                //se arrivo qui tutti i client si sono connessi prima del timeout quindi uccido il thread del timer
                timerExecutor.shutdownNow();
                 try{
                future.get();
                }catch (InterruptedException e){
                    //TODO: gestire eccezione dovuta al kill del timer sopra
                }
                //controllo quanti client ho rimendiato
                //eventualmente ripeto il tutto
            } catch (IOException e) {
                break; //entro qui se il serverSocket viene chiuso
            } catch (ExecutionException e){
                //TODO: gestire eccezione che mi arriva dal timerCallable
                //causata dal timer stesso
            }
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
    //TODO: leggendo la riga sopra forse va messo un timeout "grande" anceh sulla prima accept
    private ArrayList<Socket> timedOutAccept(ServerSocket server, int timeout,
                                             int maxConnections)
            throws IOException {
        ArrayList<Socket> socketList = new ArrayList<Socket>();
        long startTime, endTime;

        try {
            socketList.add(server.accept()); // aspetto il primo client
            server.setSoTimeout(timeout); // imposto il primo timeout intero
            startTime = System.currentTimeMillis(); //prendo il timestamp del primo client
            for (int i = 0; i < maxConnections - 1; i++) {
                socketList.add(server.accept()); //aspetto l'isemo o una exception del timer
                endTime = System.currentTimeMillis();
                if (timeout - (endTime - startTime) > 0) //se ho ancora tempo
                {
                    server.setSoTimeout((int) (timeout - (endTime - startTime))); //aggiorno il timeout sottraendo il tempo aspettato per l'isemo clientù
                } //TODO: forse alla riga sopra ci potrebbe essere un errore per la sottrazione fra int e long anche se la differenza dei long dovrebbe essere molto piccola
                else {
                    break; //altrimenti finisco
                }
            }
        } catch (SocketTimeoutException e) {
            return socketList;
        }
        return socketList;
    }
}
