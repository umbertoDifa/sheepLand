package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.DebugLogger;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.GameManager;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * thread che gestisce la connessione client server
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerThread implements Runnable {

    private final List<Sclient> client = new ArrayList<Sclient>();
    private HashMap<Integer, Sclient> clientPlayerMap = new HashMap<Integer, Sclient>(); //mappa per tenere le coppie playerHash - playerSclient

    private final GameManager gameManager;
    
    public ServerThread(List<Socket> clientSockets) {
        DebugLogger.println("ServerThread creato");
        
        //per ogni socket nella lista
        for (Socket clientSocket : clientSockets) { 
            
            //aggiungi ai client un nuovo Sclient legato al rispettivo socket
            client.add(new Sclient(clientSocket)); 
        }
        DebugLogger.println("Creati " + client.size() + "client Sclient");
        DebugLogger.println("Sclient creati");
        this.gameManager = new GameManager(clientSockets.size(), this);
        DebugLogger.println("GameManger creato");

    }

    public void run() {
        DebugLogger.println("Inizo partita run ServerThread");
        this.broadcastMessage("Partita avviata!");
        DebugLogger.println("Broadcast di benvenuto effettuato");
        this.startGame();
        
        //un thread è appena terminato e con lui la partita
        ServerManager.activatedGames--; 
    }

    /**
     * il serverThread avvia il gioco cedendo il controllo al GameManager
     */
    private void startGame() {
        gameManager.startGame();
    }

    /**
     * preso un array di hashcode dei player li mappa sui rispettivi sClient
     *
     * @param playersHashCode
     */
    public void setUpSocketPlayerMap(int[] playersHashCode) {
        DebugLogger.println("ci sono " + playersHashCode.length + " hashcode:");
        
        //per ogni hashcode, quindi per ogni player
        for (int i = 0; i < playersHashCode.length; i++) {    
            
            //inserisci la coppia hashcode del player - Sclient corrispondente
            clientPlayerMap.put(playersHashCode[i], client.get(i)); 
        }
    }

    /**
     * Invia message e ottiene una stringa di risposta dal client corrispondente
     * all'hashCode
     *
     * @param hashCode
     * @param message
     *
     * @return String
     */
    public String talkTo(int hashCode, String message) {
        this.sendTo(hashCode, message);
        return receiveFrom(hashCode);
    }

    /**
     * Invia una stringa message al client corrispondente all'hashCode
     *
     * @param hashCode
     * @param message
     */
    public void sendTo(int hashCode, String message) {
        clientPlayerMap.get(hashCode).send(message);
    }

    /**
     * Riceve una stringa dal client corrispondente all'hashcode
     *
     * @param hashCode
     *
     * @return String
     */
    public String receiveFrom(int hashCode) {
        return clientPlayerMap.get(hashCode).receive();
    }

    public void broadcastMessage(String message) {
        
        //per ogni coppia di key,value
        for (Map.Entry pairs : clientPlayerMap.entrySet()) {
            
            //TODO: vedi se funziona questo cast
            sendTo((Integer) pairs.getKey(), message);
        }
    }
}
