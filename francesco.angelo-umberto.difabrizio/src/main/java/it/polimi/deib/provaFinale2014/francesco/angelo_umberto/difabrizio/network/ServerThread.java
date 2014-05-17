package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.GameManager;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *thread che gestisce la connessione client server
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerThread implements Runnable{
    private final ArrayList<Socket> clientSockets;
    private final int minClients;
    private final GameManager gameManager;
    private HashMap<Integer,Socket> socketPlayerMap;

    public ServerThread(ArrayList<Socket> clientSockets, int minClients){
        this.clientSockets = clientSockets;
        this.minClients = minClients;
        this.gameManager = new GameManager(clientSockets.size(), this);
    }
    
    public void run() {
            this.startGame();
        }    
    /**
     * il serverThread avvia il gioco cedendo il controllo al GameManager
     */
    private void startGame(){
       gameManager.SetUpGame();
    }
    /**
     * preso un array di hashcode dei player li mappa sui rispettivi socket
     * @param playersHashCode 
     */
    public void setUpSocketPlayerMap(int[] playersHashCode){
        for(int i = 0; i < playersHashCode.length; i++){    //per ogni hashcode, quindi per ogni player
            socketPlayerMap.put(playersHashCode[i], clientSockets.get(i)); //inserisci la coppia hashcode del player - clientSocket corrispondente
        }
    }
}
