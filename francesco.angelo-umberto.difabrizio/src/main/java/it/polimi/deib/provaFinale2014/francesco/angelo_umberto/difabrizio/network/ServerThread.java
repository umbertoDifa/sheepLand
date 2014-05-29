package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.GameManager;
import java.util.List;
import java.util.Map;

/**
 * thread che gestisce la connessione client server
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerThread implements Runnable {

    private final TrasmissionController trasmissionController;
    private final GameManager gameManager;

    public ServerThread(List<String> clientNickNames, TrasmissionController trasmissionController) {
        DebugLogger.println("ServerThread creato");

        this.gameManager = new GameManager(clientNickNames, this);
        DebugLogger.println("GameManger creato");
        
        this.trasmissionController = trasmissionController;
        
    }

    public void run() {
        DebugLogger.println("Inizo partita run ServerThread");
        this.broadcastMessage("Partita avviata!");
       
        DebugLogger.println("Broadcast di benvenuto effettuato");
        this.gameManager.startGame();

        this.broadcastMessage("Disconnessione.");
        
        //un thread è appena terminato e con lui la partita
        //TODO eliminare tutti i clients di quella tabella dalla partita
        ServerManager.activatedGames--;
    }

    public TrasmissionController getTrasmissionController() {
        return trasmissionController;
    }
    
    
    public void broadcastRegion(){
        trasmissionController.broadcastRegion();
    }
    
    public void broadcastInitialCondition(){
        trasmissionController.broadcastInitialCondition();
    }
    /**
     * Invia message e ottiene una stringa di risposta dal client corrispondente
     * all'hashCode
     *
     * @param playerNickName
     * @param message
     *
     * @return String
     */
    public String talkTo(String playerNickName, String message) {
        this.sendTo(playerNickName, message);
        return receiveFrom(playerNickName);
    }

    /**
     * Invia una stringa message al client corrispondente all'hashCode
     *
     * @param playerNickName
     * @param message
     */
    public void sendTo(String playerNickName, String message) {
        ServerManager.NickSocketMap.get(playerNickName).send(message);
    }

    /**
     * Riceve una stringa dal client corrispondente all'hashcode
     *
     *
     * @param playerNickName
     *
     * @return String
     */
    public String receiveFrom(String playerNickName) {
        return ServerManager.NickSocketMap.get(playerNickName).receive();
    }

    //TODO correggi gli usi di questa in broadcastExcept...o forse no
    public void broadcastMessage(String message) {
        broadcastExcept(message, null);
    }

    /**
     * Broadcast un messaggio a tutti i player tranne quello indicato Se il
     * player indicato vale -1 allora il messaggio viene inviato comunque a
     * tutti i player
     *
     * @param message
     * @param differentPlayer
     */
    public void broadcastExcept(String message, String differentPlayer) {

        if (differentPlayer == null) {
            //per ogni coppia di key,value
            for (Map.Entry pairs : ServerManager.NickSocketMap.entrySet()) {
                sendTo((String) pairs.getKey(), message);
            }
        } else {
            //a tutti tranne il differentPlayer
            String name;
            for (Map.Entry pairs : ServerManager.NickSocketMap.entrySet()) {
                name = (String) pairs.getKey();
                if (!name.equals(differentPlayer)) {
                    sendTo((String) pairs.getKey(), message);
                }
            }
        }
    }
    
    
}
