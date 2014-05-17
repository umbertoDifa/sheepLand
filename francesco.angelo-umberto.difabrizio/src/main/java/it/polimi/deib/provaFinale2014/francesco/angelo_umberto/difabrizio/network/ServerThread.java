package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.GameManager;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * thread che gestisce la connessione client server
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerThread implements Runnable {

    private final ArrayList<Sclient> client = new ArrayList<Sclient>();
    private HashMap<Integer, Sclient> clientPlayerMap; //mappa per tenere le coppie playerHash - playerSclient

    private final GameManager gameManager;

    public ServerThread(ArrayList<Socket> clientSockets) {
        for (Socket clientSocket : clientSockets) { //per ogni socket nella lista
            client.add(new Sclient(clientSocket)); //aggiungi ai client un nuovo client legato al rispettivo socket
        }
        this.gameManager = new GameManager(clientSockets.size(), this);

    }

    public void run() {
        this.startGame();
    }

    /**
     * il serverThread avvia il gioco cedendo il controllo al GameManager
     */
    private void startGame() {
        gameManager.SetUpGame();
    }

    /**
     * preso un array di hashcode dei player li mappa sui rispettivi sClient
     *
     * @param playersHashCode
     */
    public void setUpSocketPlayerMap(int[] playersHashCode) {
        for (int i = 0; i < playersHashCode.length; i++) {    //per ogni hashcode, quindi per ogni player
            clientPlayerMap.put(playersHashCode[i], client.get(i)); //inserisci la coppia hashcode del player - Sclient corrispondente
        }
    }
    /**
     * Invia message e ottiene una stringa di risposta dal client 
     * corrispondente all'hashCode
     * @param hashCode
     * @param message
     * @return String
     */
    public String talkTo(int hashCode, String message) {
        this.sendTo(hashCode,message);
        return receiveFrom(hashCode);
    }
    /**
     * Invia una stringa message al client corrispondente all'hashCode
     * @param hashCode
     * @param message 
     */
    public void sendTo(int hashCode, String message) {
        clientPlayerMap.get(hashCode).send(message);
    }
    /**
     * Riceve una stringa dal client corrispondente all'hashcode
     * @param hashCode
     * @return String
     */
    public String receiveFrom(int hashCode) {
        return clientPlayerMap.get(hashCode).receive();
    }
}
