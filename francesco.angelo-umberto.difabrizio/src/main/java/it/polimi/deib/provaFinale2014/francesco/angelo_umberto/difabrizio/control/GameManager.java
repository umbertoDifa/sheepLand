package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Bank;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerThread;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * E' il controllo della partita. Si occupa di crearne una a seconda del numero
 * dei giocatori.
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class GameManager {//TODO: pattern memento per ripristini?
//TODO: dividere la classe fra GameSetupper e GameManager? è grossa...

    private final ServerThread server;

    private final Map map;
    private ArrayList<Player> players = new ArrayList<Player>();
    private final int playersNumber;
    private int firstPlayer; //rappresenterà il segnalino indicante il primo giocatore del giro
    private final int[] playersHashCode; //valore cached degli hash dei giocatori

    private final Bank bank;

    /**
     * Crea un GameManager
     *
     * @param playersNumber Numero dei giocatori di una partita
     * @param server Thread che gestisce la partita
     */
    public GameManager(int playersNumber, ServerThread server) {
        this.playersNumber = playersNumber;
        this.playersHashCode = new int[playersNumber]; //creo un array della dimensione del numero dei player
        this.map = new Map(); //creo la mappa univoca del gioco
        this.server = server; //creo il collegamento all'univoco serverThread
        this.setUpPlayers(playersNumber); //setto arraylist giocatori e array hashcode giocatori
        this.bank = new Bank(GameConstants.NUM_CARDS.getValue(),
                GameConstants.NUM_FENCES.getValue());
    }

    /**
     * Dato il numero di player, riempie l'arraylist dei player e riempie
     * l'array dei rispettivi hashcode
     *
     * @param numbPlayer
     */
    private void setUpPlayers(int numbPlayer) {
        for (int i = 0; i < playersNumber; i++) { //per ogni giocatore
            players.add(new Player());       //lo aggiungo alla lista dei giocatori
            playersHashCode[i] = players.get(i).hashCode();//salvo il suo hashcode
        }
    }

    /**
     * Metodo principale che viene invocato dal server thread per creare tutti
     * gli oggetti di una partita e avviarla
     */
    public void SetUpGame() {
        this.setUpMap();
        this.setUpSocketPlayerMap();
        this.setUpAnimals();
        this.setUpShepherds();
        this.setUpCards();
        this.setUpFences();
        this.setUpShift();
        this.startGame();
    }

    /**
     * Per ogni terreno regione della mappa aggiungi un collegamento ad un
     * animale il cui tipo è scelto in maniera randomica
     */
    private void setUpAnimals() {
        Region[] region = this.map.getRegions();//recupera l'array delle regioni
        for (Region reg : region) { //per ogni regione
            reg.addOvine(new Ovine());//aggiungi un ovino (a caso)
        }
    }

    /**
     * Chiede ad ogni giocatore dove posizionare il proprio pastore
     */
    private void setUpShepherds() {
        String stringedStreet;
        String errorString = "Strada già occupata, prego riprovare:";
        String message = "In quale strada vuoi posizionare il pastore?";
        Street chosenStreet;
        RegionType randomRegionType;

        for (int i = 0; i < this.playersNumber; i++) {
            //ciclo che chiede la strada per un pastore ogni volta che questa risulta già occupata
            //TODO: la roba sotto è quella che funziona senza askUntilOk
//            while (true) {
//                try {
//                    chosenStreet = this.askStreet(playersHashCode[i]);
//                    break;
//                } catch (Exception e) {
//                    this.server.sendTo(this.playersHashCode[i], e.getMessage());//manda il messaggio di errore al client
//                }
//            }
            Class[] parameterTypes = new Class[1];
            parameterTypes[0] = Integer.class;
            Method askStreetMethod;
            try {
                askStreetMethod = GameManager.class.getMethod("askStreet", parameterTypes);
            } catch (NoSuchMethodException ex) {
                //TODO:gestisci ora come ora termina il metodo
                Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
                return;
            } catch (SecurityException ex) {
                //TODO:gestisci ora come ora termina il metodo
                Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            chosenStreet = (Street) askUntilOk(askStreetMethod, message, errorString, i);
            this.players.get(i).getShepherd().moveTo(chosenStreet); //sposta il pastore 
            //creo una carta con valore 0 e di tipo casuale e l'aggiungo a 
            //quelle del pastore corrispondente al mio player
            randomRegionType = RegionType.getRandomRegionType();
            this.players.get(i).getShepherd().addCard(new Card(0, randomRegionType)); //aggiungi la carta
            //invia conferma riepilogativa
            this.server.sendTo(this.playersHashCode[i],
                    "Pastore posizionato. Hai una carta terreno di tipo: " + randomRegionType.toString());
        }

    }

    private void setUpShift() {
        //creo oggetto random
        Random random = new Random();
        //imposto il primo giocatore a caso tra quelli presenti
        this.firstPlayer = random.nextInt(this.playersNumber);
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

    /**
     * Carica i recinti finali e non nel banco
     */
    private void setUpFences() {
        int i; // counter
        //salvo il numero di recinti non finali
        int numbOfNonFinalFences = GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.
                getValue();
        //carico il numero di recinti non finali in bank
        for (i = 0; i < numbOfNonFinalFences; i++) {
            bank.loadFence(i);
        }
        //carico i recinti finali in bank. 'i' parte da dove l'ha lasciato il 
        //ciclo sopra è arriva alla fine dell'array
        for (; i < GameConstants.NUM_FENCES.getValue(); i++) {
            bank.loadFinalFence(i);
        }
    }

    /**
     * Crea le carte di tutti i tipi necessari da caricare nel banco
     */
    private void setUpCards() {
        for (int i = 0; i < RegionType.values().length - 1; i++) //per ogni tipo di regione - sheepsburg  
        {
            for (int j = 0; j < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); j++) { //per tante quante sono le carte di ogni tipo
                //crea una carta col valore giusto( j crescente da 0 al max) e tipo giusto(dipendente da i) 
                Card cardToAdd = new Card(j, RegionType.values()[i]);
                //calcola la posizione a cui aggiungerla(fondamentale per la getCard che
                //usa una politica basata sugli indici)
                int position = (i * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue()) + j;
                //caricala
                bank.loadCard(cardToAdd, position);
            }
        }

    }

    private void startGame() {
        this.executeRound();
        //this.calculatePoints();
        //this.broadcastWinner();
    }

    private void executeRound() {
        int currentPlayer = this.firstPlayer;
        boolean lastRound = false;

        //TODO: spostare while in startGame
        while (!(lastRound && currentPlayer == this.firstPlayer)) {//condizione di fine gioco, se non è l'ultimo giro o il giocatore non è l'ultimo del giro
            try { //prova a fare un turno
                this.executeShift(currentPlayer);
            } catch (FinishedFencesException e) { //se finiscono i recinti
                //i recinti sono finiti chiama l'ultimo giro
                lastRound = true;
            } finally {//comunque vada
                //aggiorno il player che gioca in modulo playersNumber
                currentPlayer++;
                currentPlayer %= this.playersNumber;
            }
        }
    }

    private void executeShift(int player) throws FinishedFencesException {
        String noMoreFenceMessage = "Recinti Finiti!";
        for (int i = 0; i < GameConstants.NUM_ACTIONS.getValue(); i++) {

            //TODO: aggiungere controllo, while e try catch
            this.chooseAction(player).execute();
        }
        //TODO: cambiare condizione
        if (this.bank.numberOfUsedFences() >= GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.getValue()) {
            throw new FinishedFencesException(noMoreFenceMessage);
        }
        //this.server.talkTo(this.playersHashCode[player], "Selezione un'azione da fare:")
    }

    private Action chooseAction(int player) {
        String[] possibleActions = {"1- Sposta una pecora", "2- Sposta pastore",
            "3-Compra terreno", "4-Accoppia pecore", "5-Accoppia montone e pecora",
            "6-Abbatti pecora"};
        int actionChoice = Integer.parseInt(this.server.talkTo(
                this.playersHashCode[player],
                "Scegli l'azione da fare tra:" + Arrays.toString(possibleActions)));//TODO: chissa se funziona sta roba del to string
        return Action.make(actionChoice);
    }

    /**
     * Controlla se street è libera
     *
     * @param street
     *
     * @return true se libera, false altrimenti
     */
    private boolean isStreetFree(Street street) {
        return street.isFree();
    }

    /**
     * converte una stringa street che indica l'id della strada sulla mappa
     * nell'oggetto Street corrispondente
     *
     * @param street L'id stringa che indica la strada
     *
     * @return la Street corrispondente
     */
    private Street convertStringToStreet(String street) {
        return map.getStreets()[Integer.parseInt(street)];
    }

    /**
     * chiede al cliente corrispondente al palyerHashCode la strada desiderata.
     * se libera la ritorna, se occupata solleva un eccezione
     *
     * @param playerHashCode
     * @return
     * @throws Exception
     */
    private Street askStreet(int playerHashCode) throws Exception {
        String stringedStreet;
        Street chosenStreet;
        String errorString = "Strada già occupata, prego riprovare:";

        stringedStreet = this.server.talkTo(playerHashCode,
                "In quale strada vuoi posizionare il pastore?"); //raccogli decisione
        chosenStreet = convertStringToStreet(stringedStreet); //traducila in oggetto steet
        if (!isStreetFree(chosenStreet)) { //se la strada è occuapata
            throw new StreetBusyException(null, errorString); //solleva eccezione throwable è posto a null
        }
        //this.askUntilOk(new callableAskStreet(playerHashCode), errorString, errorString)
        return chosenStreet;
    }
    /**
     * Il method è una richiesta particolare che viene eseguita finchè il player non da una 
     * risposta adeguata
     * @param method Metodo da eseguire
     * @param message Messaggio da inivare al player
     * @param messageError Messaggio inviato al player in caso di risposta non 
     * adeguata
     * @param player Giocatore a cui mandare la richiesta
     * @return L'oggetto della richiesta cioè quello che ritorna il method
     */
    private Object askUntilOk(Method method, String message, String messageError, int player) {
        Object[] parameters = new Object[1];
        parameters[0] = message;
        while (true) {
            try {
                return method.invoke(this, parameters);                
            } catch (IllegalAccessException ex) {
                //TODO:gestisci
                Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                //TODO:gestisci
                Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                //TODO: se il metodo lancia un eccezione
                Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//TODO: metodo askTillRightAnswer   torna un object che castiamo, oppure torniamo un void 
//    private Object askTillRightAnswer() throws Exception{
//        Collable c = new Callable<Object>{ public Object call(){ return  }}
//    }
//    public Object askUntilOk(Callable<Object> callableMethod, String messageTosend, String messageError) {
//        while (true) {
//            try {
//                return callableMethod.call();
//            } catch (Exception e) {
//                this.server.sendTo(callableMethod.g, messageError);
//            }
//        }
//    }
//    private class callableAskStreet implements Callable<Street> {
//
//        private int playerHashCode;
//
//        public callableAskStreet(int playerHashCode) {
//            this.playerHashCode = playerHashCode;
//        }
//
//        public Street call() throws Exception {
//            return askStreet(playerHashCode);
//        }
//
//        public int getPlayerHashCode() {
//            return playerHashCode;
//        }
//
//    }
}
