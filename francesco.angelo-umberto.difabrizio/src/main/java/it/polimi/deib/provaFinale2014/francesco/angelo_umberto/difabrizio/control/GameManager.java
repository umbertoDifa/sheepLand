package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerThread;
import java.util.ArrayList;

/**
 * E' il controllo della partita. Si occupa di crearne una a seconda del numero
 * dei giocatori.
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class GameManager {//TODO: pattern memento per ripristini?

    private final ServerThread server;
    private final Map map;
    private ArrayList<Player> players = new ArrayList<Player>();
    private final int playersNumber;
    private final int[] playersHashCode;

  
    public GameManager(int playersNumber, ServerThread server) {
        this.playersNumber = playersNumber;
        this.playersHashCode = new int[playersNumber]; //creo un array della dimensione del numero dei player
        this.map = new Map(); //creo la mappa univoca del gioco
        this.server = server; //creo il collegamento all'univoco serverThread
        this.setUpPlayers(playersNumber); //setto arraylist giocatori e array hashcode giocatori

    }
    /**
     * dato il numero di player, riempie l'arraylist dei player
     * e riempie l'array dei rispettivi hashcode
     * @param numbPlayer 
     */
    private void setUpPlayers(int numbPlayer) {
        for (int i = 0; i < playersNumber; i++) { //per ogni giocatore
            players.add(new Player());   //TODO:occhio al costruttore id player       //lo aggiungo alla lista dei giocatori
            playersHashCode[i] = players.get(i).hashCode();//salvo il suo hashcode
        }
    }
    
    /**
     * metodo principale che viene invocato dal server thread per avviare la 
     * partita
     */
    public void SetUpGame() {
        this.setUpMap();
        this.setUpSocketPlayerMap();
        this.setUpAnimals();
        this.setUpShepherds();
        this.setUpShift();
    }
    
    /**
     * per ogni terreno regione della mappa aggiungi un collegamento ad un animale il cui tipo è scelto in
     *   maniera randomica
     */
    private void setUpAnimals(){
        
    }
    
    private void setUpShepherds(){
        //TODO
    }
   
    private void setUpShift(){
        //TODO
    }
    /**
     * chiama l'omonimo metodo della Map
     */
    private void setUpMap(){
        this.map.setUp();
    }
    /**
     * chiama l'omonimo metodo del serverThread
     */
    private void setUpSocketPlayerMap(){
        this.server.setUpSocketPlayerMap(playersHashCode);
    }
}
