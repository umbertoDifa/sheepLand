package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
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
    private final int[] playersHashCode; //valore cached degli hash dei giocatori

    public GameManager(int playersNumber, ServerThread server) {
        this.playersNumber = playersNumber;
        this.playersHashCode = new int[playersNumber]; //creo un array della dimensione del numero dei player
        this.map = new Map(); //creo la mappa univoca del gioco
        this.server = server; //creo il collegamento all'univoco serverThread
        this.setUpPlayers(playersNumber); //setto arraylist giocatori e array hashcode giocatori

    }

    /**
     * dato il numero di player, riempie l'arraylist dei player e riempie
     * l'array dei rispettivi hashcode
     *
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
     * per ogni terreno regione della mappa aggiungi un collegamento ad un
     * animale il cui tipo è scelto in maniera randomica
     */
    private void setUpAnimals() {
        Region[] region = this.map.getRegions();//recupera l'array delle regioni
        for (Region reg : region) { //per ogni regione
            reg.addOvine(new Ovine());//aggiungi un ovino (a caso)
        }
    }

    /**
     * chiede ad ogni giocatore dove posizionare il proprio pastore
     */
    private void setUpShepherds() {
        String stringedStreet;
        String errorString = "Strada già occupata, prego riprovare:";
        Street chosenStreet;
        for (int i = 0; i < this.playersNumber; i++) {
            //ciclo che chiede la strada per un pastore ogni volta che questa risulta già occupata
            while (true) {
                try {
                    stringedStreet = this.server.talkTo(this.playersHashCode[i],
                            "In quale strada vuoi posizionare il pastore?"); //raccogli decisione
                    chosenStreet = convertStringToStreet(stringedStreet); //traducila in oggetto steet
                    if ( !isStreetFree(chosenStreet) ) { //se la strada è occuapata
                        throw new Exception(errorString); //solleva eccezione
                    }
                    break;
                } catch (Exception e) {
                    this.server.sendTo(this.playersHashCode[i], e.getMessage());//manda il messaggio di errore al client
                }
            }
            //prendi il pastore dell'iesimo giocatore e spostalo sulla strada 
            //corrispondente all'indice dell'array delle strade ottenuto
            //convertendo la risposta ottenuta in int
            this.players.get(i).getShepherd().moveTo(chosenStreet);
            this.server.sendTo(this.playersHashCode[i],
                    "Pastore posizionato");
        }

    }

    private void setUpShift() {
        //TODO
    }

    /**
     * chiama l'omonimo metodo della Map
     */
    private void setUpMap() {
        this.map.setUp();
    }

    /**
     * chiama l'omonimo metodo del serverThread
     */
    private void setUpSocketPlayerMap() {
        this.server.setUpSocketPlayerMap(playersHashCode);
    }

    private boolean isStreetFree(Street street) {
        return street.isFree();
    }

    private Street convertStringToStreet(String street) {
        return map.getStreets()[Integer.parseInt(street)];
    }
}
