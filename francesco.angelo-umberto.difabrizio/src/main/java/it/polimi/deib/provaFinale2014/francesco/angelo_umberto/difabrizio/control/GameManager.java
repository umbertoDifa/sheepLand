package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.CannotMoveAnimalException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Bank;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Dice;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.BusyStreetException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerThread;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private int firstPlayer; //rappresenterà il segnalino indicante il primo giocatore del giro
    private final int[] playersHashCode; //valore cached degli hash dei giocatori
    private final int shepherd4player;
    protected final Bank bank;  //per permettere a player di usarlo

    /**
     * Crea un GameManager
     *
     * @param playersNumber Numero dei giocatori di una partita
     * @param server        Thread che gestisce la partita
     */
    public GameManager(int playersNumber, ServerThread server) {
        this.playersNumber = playersNumber;
        this.playersHashCode = new int[playersNumber]; //creo un array della dimensione del numero dei player
        this.map = new Map(); //creo la mappa univoca del gioco
        this.server = server; //creo il collegamento all'univoco serverThread
        this.setUpPlayers(); //setto arraylist giocatori e array hashcode giocatori
        this.bank = new Bank(GameConstants.NUM_CARDS.getValue(),
                GameConstants.NUM_INITIAL_CARDS.getValue(),
                GameConstants.NUM_FENCES.getValue());
        if (this.playersNumber <= ControlConstants.NUM_FEW_PLAYERS.getValue()) {
            this.shepherd4player = ControlConstants.SHEPHERD_FOR_FEW_PLAYERS.getValue();
        } else {
            this.shepherd4player = ControlConstants.STANDARD_SHEPHERD_FOR_PLAYER.getValue();
        }
    }

    /**
     * Riempie l'arraylist dei player e riempie l'array dei rispettivi hashcode
     *
     */
    private void setUpPlayers() {
        for (int i = 0; i < playersNumber; i++) { //per ogni giocatore
            players.add(new Player(this, this.shepherd4player));       //lo aggiungo alla lista dei giocatori
            playersHashCode[i] = players.get(i).hashCode();//salvo il suo hashcode
        }
    }

    /**
     * Metodo principale che viene invocato dal server thread per creare tutti
     * gli oggetti di una partita e avviarla
     */
    private void SetUpGame() {
        this.setUpMap();
        this.setUpSocketPlayerMap();
        this.setUpAnimals();
        this.setUpShepherds();
        this.setUpCards();
        this.setUpFences();
        this.setUpShift();
    }

    /**
     * Per ogni terreno regione della mappa aggiungi un collegamento ad un
     * animale il cui tipo è scelto in maniera randomica e posiziono pecora nera
     * e lupo a sheepsburg
     */
    private void setUpAnimals() {
        int SHEEPSBURG_ID = 18;
        Region[] region = this.map.getRegions();//recupera l'array delle regioni
        for (Region reg : region) { //per ogni regione
            reg.addOvine(new Ovine());//aggiungi un ovino (a caso)          
        }
        //posiziono lupo e pecora nera a sheepsburg
        map.getBlackSheep().setAt(map.getRegions()[SHEEPSBURG_ID]);
        map.getWolf().setAt(map.getRegions()[SHEEPSBURG_ID]);
    }

    /**
     * Chiede ad ogni giocatore dove posizionare il proprio pastore
     */
    private void setUpShepherds() {
        Street chosenStreet = new Street(0); //HACK: creo cmq una strada con valore 0 così non ho errori 
        //perchè la variabile potrebbe essere null fuori dal while infatti tutti i controlli necessari li faccio
        //in askStreet e nelle funzioni da lei chiamate che mi ridanno una delle eccezioni che gestisco

        //ciclo che chiede la strada per un pastore ogni volta che questa risulta già occupata
        int i;//indice giocatori

        for (i = 0; i < this.playersNumber; i++) {//per ogni playerint 
            int j;//indice pastori

            for (j = 0; j < this.shepherd4player; j++) {//per ogni suo pastore
                while (true) {
                    try {   //prova a chiedere la strada per il j-esimo pastore
                        chosenStreet = askStreet(i, this.playersHashCode[j]);    //se ho un valore di ritorno
                        break;                          //brekka

                    } catch (StreetNotFoundException ex) {//se strada non trovata 
                        this.server.sendTo(this.playersHashCode[i],
                                ex.getMessage()); //invio msg strada non trovata e ricomincia loop

                    } catch (BusyStreetException e) {    //se la strada è occupata
                        //manda il messaggio di errore al client e ricomincia il loop
                        this.server.sendTo(this.playersHashCode[i],
                                e.getMessage());
                    }
                }//while
            this.players.get(i).getShepherd(j).moveTo(chosenStreet); //sposta il pastore 
            }//for pastori

            //creo una carta con valore 0 e di tipo casuale e l'aggiungo a 
            //quelle del pastore corrispondente al mio player
            //aggiungi la carta prendendola dalle carte iniziali della banca
            Card initialCard = this.bank.getInitialCard();
            this.players.get(i).getShepherd(j).addCard(initialCard);

            //invia conferma riepilogativa
            this.server.sendTo(this.playersHashCode[i],
                    "Pastore posizionato. Hai una carta terreno di tipo: " + initialCard.getType().toString());
        }//for giocatori

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

    private void playTheGame() {
        try {
            this.executeRounds();
        } catch (FinishedFencesException ex) {
            this.getServer().broadcastMessage(
                    "I recinti totali sono finiti, fine gioco e calcolo dei punteggi");
        } finally {//se il gioco va come deve o se finisco i recinti quando non devono cmq calcolo i punteggi
            //TODO:decidere scelta fatta sopra
            //this.calculatePoints();
            //this.broadcastWinner();
        }
    }

    public void startGame() {
        this.SetUpGame();
        this.playTheGame();
        //gameFinished
    }

    private void executeRounds() throws FinishedFencesException {
        int currentPlayer = this.firstPlayer;
        boolean lastRound = false;
        //TODO dicutere il fine giro per la discordanza 11recinti 12recintni
        //se non è l'ultimo giro o il giocatore non è l'ultimo del giro
        while (!(lastRound && currentPlayer == this.firstPlayer)) {
            //prova a fare un turno
            lastRound = this.executeShift(currentPlayer);

            //aggiorno il player che gioca 
            currentPlayer++;
            currentPlayer %= this.playersNumber; //conto in modulo playersNumber

            //controllo se ho finito il giro                            
            if (currentPlayer == this.firstPlayer) {//se il prossimo a giocare è il primo del giro
                //1)avvio il market  
                this.startMarket();
                //2)muovo il lupo
                this.moveSpecialAnimal(this.map.getWolf());
            }
        }//while
    }

    private boolean executeShift(int player) throws FinishedFencesException {
        String noMoreFenceMessage = "Recinti Finiti!";

        //muovo la pecora nera
        this.moveSpecialAnimal(this.map.getBlackSheep());

        //faccio fare le azioni al giocatore
        for (int i = 0; i < GameConstants.NUM_ACTIONS.getValue(); i++) {//per il numero di azioni possibili per un turno
            while (true) {
                try {
                    this.players.get(player).chooseAndMakeAction(); //scegli l'azione e falla
                    break; //se non arriva un l'eccezione passo alla prossima azione
                } catch (ActionException ex) {
                    //avvisa e riavvia la procedura di scelta dell'i-esima azione
                    this.server.sendTo(playersHashCode[player], ex.getMessage());
                }
            }
        }
        if (this.bank.numberOfUsedFence() >= GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.getValue()) {
            return true; //se sono finiti i recinti normali chiamo l'ultimo giro
        }
        return false;   //se ci sono ancora recinti non chiamo l'ultimo giro
    }

    /**
     * Chiede al giocatore corrispondente all playerHashCode,
     * in quale strada posizionare il pastore il cui id è idShepherd
     * @param playerHashCode
     * @param idShepherd
     * @return
     * @throws StreetNotFoundException se la strada non esiste
     * @throws BusyStreetException se la strada è occupata
     */
    protected Street askStreet(int playerHashCode, int idShepherd) throws
            StreetNotFoundException,
            BusyStreetException {
        String errorString = "Strada già occupata, prego riprovare:";

        String stringedStreet = this.server.talkTo(playerHashCode,
                "In quale strada vuoi posizionare il pastore?" + Integer.toString(
                        idShepherd + 1)); //raccogli decisione
        //traducila in oggetto steet 
        Street chosenStreet = map.convertStringToStreet(stringedStreet);
        if (!chosenStreet.isFree()) { //se la strada è occuapata
            throw new BusyStreetException(errorString); //solleva eccezione
        }
        return chosenStreet; //altrimenti ritorna la strada
    }

    /**
     * Chiede al player inviandogli la stringa message un id regione
     *
     * @param playerHashCode
     * @param message
     *
     * @return Regione corrispondente
     *
     * @throws RegionNotFoundException
     */
    protected Region askAboutRegion(int playerHashCode, String message) throws
            RegionNotFoundException {
        Region chosenRegion;
        String stringedRegion = this.getServer().talkTo(playerHashCode, message);
        chosenRegion = getMap().convertStringToRegion(stringedRegion);
        return chosenRegion;
    }

    public ServerThread getServer() {
        return server;
    }

    public Map getMap() {
        return map;
    }

    private void moveSpecialAnimal(SpecialAnimal animal) { 
        //salvo la regione in cui si trova l'animale
        Region actualAnimalRegion = animal.getMyRegion();

        //cerco la strada che dovrebbe attraversare
        Street potentialWalkthroughStreet;
        try {
            potentialWalkthroughStreet = this.map.getStreetByValue(
                    actualAnimalRegion,
                    Dice.getRandomValue());
            
            //calcola regione d'arrivo
            Region endRegion = this.map.getEndRegion(actualAnimalRegion,
                    potentialWalkthroughStreet);
            
            //cerco di farlo passare (nel caso del lupo si occupa pure di farlo mangiare)
            animal.moveThrough(potentialWalkthroughStreet,
                    endRegion);
            
            //tutto ok
            this.getServer().broadcastMessage(animal.toString() + "mosso!");
        } catch (CannotMoveAnimalException ex) {
            this.getServer().broadcastMessage(ex.getMessage());
        } catch (StreetNotFoundException ex) {
            this.getServer().broadcastMessage(
                    ex.getMessage() + "Il lupo non si muove.");
        }
    }

    private void startMarket() {
        int i; //iteratore sui player
        for (i = 0; i < this.playersNumber; i++) {//per ogni player 
            //raccogli cosa vuole vendere
            this.players.get(i).sellCards();
        }
        //lancia il dado per sapere il primo a comprare
        int playerThatBuys = Dice.getRandomValue() % this.playersNumber; //il modulo serve ad essere sicuro che venga selezionato un player esistente
        for (i = 0; i < this.playersNumber; i++) {//per ogni player 
            //chiedi se vuole comprare           
            this.players.get(playerThatBuys).buyCards();
            //aggiorno il prossimo player
            playerThatBuys = (playerThatBuys + 1) % this.playersNumber;
        }
    }

    protected void askCancelOrRetry(int hashCode, String message) throws
            ActionCancelledException {
        //chiedo cosa vuole fare traducendo la scelta in char e processandolo in una switch
        Character choice = this.getServer().talkTo(
                hashCode(), message + " Riprovare(R) o Annullare(A)?").charAt(0);
        switch (choice) {
            case 'R': //se vuole riprovare
                break;
            default: //se vuole annullare o se mette una roba a caso
                throw new ActionCancelledException("Azione annullata");
        }//switch
    }
    
    
    /**
     * Chiede, mandandogli la stringa message, al giocatore corrispondente 
     * all hashCode, qual'è l'id del pastore da muovere tra i suoi
     * numShepherd pastori. Il metodo dev'essere invocato con numShepherd
     * maggiore di zero.
     * @param hashCode
     * @param numShepherd
     * @param message
     * @return id pastore scelto
     */
    protected int askIdShepherd(int hashCode, int numShepherd, String message){
        int idShepherd;
        do {
            //chiedi quale pastore muovere
            idShepherd = Integer.parseInt(this.getServer().talkTo(
                        this.hashCode(), "Quale pastore vuoi muovere?"));
            
            //la risposta sarà 1 o 2 quindi lo ricalibro sulla lunghezza dell'array
            idShepherd--;
        } while (idShepherd < 0 && idShepherd > numShepherd);  //fintanto che non va bene l'id
        return idShepherd;
    }
    
/**
 * chiedo conferma per lanciare il dado al giocatore
 * corrispondente al playerHashCode. Ritorno sempre un valore random
 * @param playerHashCode
 * @return 
 */
    protected int askThrowDice(int playerHashCode){
        this.getServer().talkTo(playerHashCode, "vuoi lanciare dado?");
        return Dice.getRandomValue();
    }
}
