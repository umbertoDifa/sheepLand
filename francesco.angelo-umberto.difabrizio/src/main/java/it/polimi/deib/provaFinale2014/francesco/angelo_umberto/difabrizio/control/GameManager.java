package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.CannotMoveAnimalException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Bank;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Dice;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.BusyStreetException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NodeNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerThread;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: importantissimo settare il parent handler di ogni logger!
/**
 * E' il controllo della partita. Si occupa di crearne una a seconda del numero
 * dei giocatori.
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class GameManager {//TODO: pattern memento per ripristini?

    protected final ServerThread server;

    protected final Map map;
    private List<Player> players = new ArrayList<Player>();
    private final int playersNumber;
    private int firstPlayer; //rappresenterà il segnalino indicante il primo giocatore del giro
    private final int[] playersHashCode; //valore cached degli hash dei giocatori
    protected final int shepherd4player;
    protected final Bank bank;  //per permettere a player di usarlo

    /**
     * Crea un GameManager
     *
     * @param playersNumber Numero dei giocatori di una partita
     * @param server        Thread che gestisce la partita
     */
    public GameManager(int playersNumber, ServerThread server) {
        this.playersNumber = playersNumber;
        //creo un array della dimensione del numero dei player
        this.playersHashCode = new int[playersNumber];
        //creo la mappa univoca del gioco
        this.map = new Map();
        //creo il collegamento all'univoco serverThread
        this.server = server;
        this.bank = new Bank(GameConstants.NUM_CARDS.getValue(),
                GameConstants.NUM_INITIAL_CARDS.getValue(),
                GameConstants.NUM_FENCES.getValue());
        if (this.playersNumber <= ControlConstants.NUM_FEW_PLAYERS.getValue()) {
            this.shepherd4player = ControlConstants.SHEPHERD_FOR_FEW_PLAYERS.getValue();
        } else {
            this.shepherd4player = ControlConstants.STANDARD_SHEPHERD_FOR_PLAYER.getValue();
        }
        //setto arraylist giocatori e array hashcode giocatori
        this.setUpPlayers();
        this.setUpSocketPlayerMap();
    }

    /**
     * Riempie l'arraylist dei player e riempie l'array dei rispettivi hashcode
     *
     */
    private void setUpPlayers() {
        //per ogni giocatore
        for (int i = 0; i < playersNumber; i++) {
            //lo aggiungo alla lista dei giocatori
            players.add(new Player(this));
            //salvo il suo hashcode
            playersHashCode[i] = players.get(i).hashCode();
        }
    }

    /**
     * Metodo principale che viene invocato dal server thread per creare tutti
     * gli oggetti di una partita e avviarla
     */
    private void SetUpGame() {
        DebugLogger.println("SetUpMap Avviato");
        this.setUpMap();
        DebugLogger.println("SetUpAnimals Avviato");

        this.setUpAnimals();
        DebugLogger.println("SetUpSheperds Avviato");

        this.setUpCards();
        DebugLogger.println("SetUpFences Avviato");

        this.setUpFences();
        DebugLogger.println("SetUpShift Avviato");

        this.setUpShift();
        DebugLogger.println(
                "SetUpShift Terminato: il primo giocatore e'" + this.firstPlayer);

        this.broadcastInitialConditions();

        this.setUpShepherds();
        DebugLogger.println("SetUpCards Avviato");

    }

    /**
     * Per ogni terreno regione della mappa aggiungi un collegamento ad un
     * animale il cui tipo è scelto in maniera randomica e posiziono pecora nera
     * e lupo a sheepsburg
     */
    private void setUpAnimals() {
        int SHEEPSBURG_ID = 18;
        //recupera l'array delle regioni
        Region[] region = this.map.getRegions();

        //per ogni regione tranne shepsburg
        for (int i = 0; i < region.length - 1; i++) {
            //aggiungi un ovino (a caso)          
            region[i].addOvine(new Ovine());
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

        int i;//indice giocatori
        int j;//indice pastori
        int currentPlayer;
        //per ogni playerint 
        for (i = 0; i < this.playersNumber; i++) {
            //setto il player corrente
            currentPlayer = (firstPlayer + i) % playersNumber;

            //per ogni suo pastore
            for (j = 0; j < this.shepherd4player; j++) {
                while (true) {
                    //prova a chiedere la strada per il j-esimo pastore
                    try {
                        //se ho un valore di ritorno
                        chosenStreet = askStreet(
                                this.playersHashCode[currentPlayer], j);
                        break;
                        //se strada non trovata 
                    } catch (StreetNotFoundException ex) {
                        //invio msg strada non trovata e ricomincia loop
                        this.server.sendTo(this.playersHashCode[currentPlayer],
                                ex.getMessage());
                        Logger.getLogger(DebugLogger.class.getName()).log(
                                Level.SEVERE, ex.getMessage(), ex);
                        //se la strada è occupata
                    } catch (BusyStreetException e) {
                        //manda il messaggio di errore al client e ricomincia il loop
                        this.server.sendTo(this.playersHashCode[currentPlayer],
                                e.getMessage());
                        Logger.getLogger(DebugLogger.class.getName()).log(
                                Level.SEVERE, e.getMessage(), e);
                    }
                }//while
                DebugLogger.println(
                        "Setto il pastore: " + j + " del giocatore: " + currentPlayer
                );
                //sposta il pastore 
                this.players.get(currentPlayer).shepherd[j].moveTo(
                        chosenStreet);
                DebugLogger.println("Pastore settato");

                //creo una carta con valore 0 e di tipo casuale e l'aggiungo a 
                //quelle del pastore corrispondente al mio player
                //aggiungi la carta prendendola dalle carte iniziali della banca
                DebugLogger.println("Prendo una carta dalla banca");
                Card initialCard = this.bank.getInitialCard();

                DebugLogger.println("Aggiungo la carta al pastore");
                this.players.get(currentPlayer).shepherd[j].addCard(
                        initialCard);

                DebugLogger.println("invio conferma");
                //invia conferma riepilogativa all'utente
                this.server.sendTo(this.playersHashCode[currentPlayer],
                        "Pastore posizionato. Hai una carta terreno di tipo: " + initialCard.getType().toString());

                try {
                    //aggiorna gli altri
                    this.server.broadcastExcept(
                            "Il giocatore " + currentPlayer + " ha posizionato il pastore " + j + " nella strada " + this.map.getNodeIndex(
                                    chosenStreet),
                            playersHashCode[currentPlayer]);
                } catch (NodeNotFoundException ex) {
                    //è impossibile che la strada non esista perchè è stata appena convertita...
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);
                }
            }
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
     * Inserisce le carte nella banca, lo stesso numero per ogni regione,
     * nell'ordine in cui sono le enum della RegionType così da poter usare una
     * ricerca indicizzata per trovarle in seguito
     */
    private void setUpCards() {
        //per ogni tipo di regione - sheepsburg 
        int i;
        int j;
        for (i = 0; i < RegionType.values().length - 1; i++) {
            //per tante quante sono le carte di ogni tipo
            for (j = 0; j < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); j++) {
                //crea una carta col valore giusto( j crescente da 0 al max) e tipo giusto(dipendente da i) 
                Card cardToAdd = new Card(j, RegionType.values()[i]);
                //caricala
                bank.loadCard(cardToAdd);
            }
        }
    }

    private void playTheGame() {
        this.server.broadcastMessage("Inizia il gioco!");
        try {
            DebugLogger.println("Avvio esecuzione giri");
            this.executeRounds();
        } catch (FinishedFencesException ex) {
            this.server.broadcastMessage(
                    "I recinti totali sono finiti, fine gioco e calcolo dei punteggi");
            Logger.getLogger(DebugLogger.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        } finally {
            //se il gioco va come deve o se finisco i recinti quando non devono cmq calcolo i punteggi
            //TODO:decidere scelta fatta sopra
            //this.calculatePoints();
            //this.broadcastWinner();
        }
    }

    public void startGame() {
        DebugLogger.println("SetUpGameAvviato");
        this.SetUpGame();

        DebugLogger.println("SetUpGame Effettuato");
        this.playTheGame();
        //gameFinished
    }

    private void broadcastInitialConditions() {
        for (int i = 0; i < playersNumber; i++) {
            this.server.sendTo(playersHashCode[i],
                    "Ci sono :" + playersNumber + " giocatori, tu sei il numero :" + i
                    + "; ogni giocatore ha :" + this.shepherd4player
                    + "pastori. Il primo del turno è :" + firstPlayer
                    + ". Ogni giocatore ha :" + GameConstants.NUM_ACTIONS.getValue() + " azioni.");
        }
    }

    private void executeRounds() throws FinishedFencesException {
        int currentPlayer = this.firstPlayer;
        boolean lastRound = false;
        //TODO dicutere il fine giro per la discordanza 11recinti 12recintni
        //se non è l'ultimo giro o il giocatore non è l'ultimo del giro
        while (!(lastRound && currentPlayer == this.firstPlayer)) {
            //prova a fare un turno
            DebugLogger.println("Avvio esecuzione turno");
            lastRound = this.executeShift(currentPlayer);

            //aggiorno il player che gioca 
            currentPlayer++;
            //conto in modulo playersNumber
            currentPlayer %= this.playersNumber;

            //controllo se ho finito il giro
            //se il prossimo a giocare è il primo del giro
            if (currentPlayer == this.firstPlayer) {
                //1)avvio il market  
                this.startMarket();
                //2)muovo il lupo
                try {
                    this.moveSpecialAnimal(this.map.getWolf());
                } catch (CannotMoveAnimalException e) {
                    this.server.broadcastMessage(
                            "Il lupo non si muove perchè " + e.getMessage());
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE, e.getMessage(), e);
                }
            }
        }//while
    }

    private boolean executeShift(int player) throws FinishedFencesException {
        //TODO: timer shift? cos' un giocatore non può metterci più di un toto
        //a fare le sue azioni
        DebugLogger.println("Muovo pecora nera");
        try {
            //muovo la pecora nera
            this.moveSpecialAnimal(this.map.getBlackSheep());
            DebugLogger.println("pecora nera mossa");
            this.server.broadcastMessage(
                    "La Pecora nera si è mossa in: " + this.map.getNodeIndex(
                            this.map.getBlackSheep().getMyRegion()));
        } catch (CannotMoveAnimalException e) {
            this.server.broadcastMessage(e.getMessage());
            Logger.getLogger(DebugLogger.class.getName()).log(
                    Level.SEVERE, e.getMessage(), e);
        } catch (NodeNotFoundException ex) {
            //non può verificarsi perchè se la pecora si muove allora il nodo esiste
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
        }
        //faccio fare le azioni al giocatore
        //per il numero di azioni possibili per un turno
        for (int i = 0; i < GameConstants.NUM_ACTIONS.getValue(); i++) {
            while (true) {
                try {
                    DebugLogger.println("Avvio choose and make action");
                    //scegli l'azione e falla
                    this.players.get(player).chooseAndMakeAction();
                    //se non arriva un l'eccezione passo alla prossima azione
                    break;
                } catch (ActionException ex) {
                    DebugLogger.println("Gestisco ActionCancelledException");
                    //avvisa e riavvia la procedura di scelta dell'i-esima azione
                    this.server.sendTo(playersHashCode[player], ex.getMessage());
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
        if (this.bank.numberOfUsedFence() >= GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.getValue()) {
            //se sono finiti i recinti normali chiamo l'ultimo giro
            return true;
        }
        //se ci sono ancora recinti non chiamo l'ultimo giro
        return false;
    }

    /**
     * Chiede al giocatore corrispondente all playerHashCode, in quale strada
     * posizionare il pastore il cui id è idShepherd
     *
     * @param playerHashCode
     * @param idShepherd
     *
     * @return
     *
     * @throws StreetNotFoundException se la strada non esiste
     * @throws BusyStreetException     se la strada è occupata
     */
    protected Street askStreet(int playerHashCode, int idShepherd) throws
            StreetNotFoundException,
            BusyStreetException {
        String errorString = "Strada già occupata, prego riprovare:";

        DebugLogger.println("Chiedo una strada in askStreet");

        //raccogli decisione
        String stringedStreet = this.server.talkTo(playerHashCode,
                "In quale strada vuoi posizionare il pastore " + Integer.toString(
                        idShepherd + 1) + " ?");
        DebugLogger.println("Risposta sulla strada ottenuta");
        //traducila in oggetto steet 
        Street chosenStreet = map.convertStringToStreet(stringedStreet);
        DebugLogger.println("Conversione strada effettuata");
        //se la strada è occuapata
        if (!chosenStreet.isFree()) {
            throw new BusyStreetException(errorString);
            //solleva eccezione
        }
        //altrimenti ritorna la strada
        return chosenStreet;
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
        String stringedRegion = this.server.talkTo(playerHashCode, message);
        chosenRegion = map.convertStringToRegion(stringedRegion);
        this.server.sendTo(playerHashCode, "regione ok");
        return chosenRegion;
    }  
  
    
    //TODO piuttosto che fare mappa e server private e fornire i getter
    //forse è meglio che siano private

    private void moveSpecialAnimal(SpecialAnimal animal) throws
            CannotMoveAnimalException {
        //salvo la regione in cui si trova l'animale
        Region actualAnimalRegion = animal.getMyRegion();

        //cerco la strada che dovrebbe attraversare
        Street potentialWalkthroughStreet;
        try {
            potentialWalkthroughStreet = this.map.getStreetByValue(
                    actualAnimalRegion,
                    Dice.roll());

            //calcola regione d'arrivo
            Region endRegion = this.map.getEndRegion(actualAnimalRegion,
                    potentialWalkthroughStreet);

            //cerco di farlo passare (nel caso del lupo si occupa pure di farlo mangiare)
            animal.moveThrough(potentialWalkthroughStreet,
                    endRegion);

            //tutto ok
        } catch (StreetNotFoundException ex) {
            throw new CannotMoveAnimalException(
                    "La strada designata dal dado non esiste");
        } catch (RegionNotFoundException ex) {
            throw new CannotMoveAnimalException(
                    "La regione di arrivo non esiste");
        }
    }

    private void startMarket() {
        //iteratore sui player
        int i;
        //per ogni player 
        for (i = 0; i < this.playersNumber; i++) {
            //raccogli cosa vuole vendere
            this.players.get(i).sellCards();
        }
        //lancia il dado per sapere il primo a comprare
        //il modulo serve ad essere sicuro che venga selezionato un player esistente
        int playerThatBuys = Dice.roll() % this.playersNumber;
        //per ogni player 
        for (i = 0; i < this.playersNumber; i++) {
            //chiedi se vuole comprare           
            this.players.get(playerThatBuys).buyCards();
            //aggiorno il prossimo player
            playerThatBuys = (playerThatBuys + 1) % this.playersNumber;
        }
    }

    protected void askCancelOrRetry(int playerHashCode, String message) throws
            ActionCancelledException {
        //chiedo cosa vuole fare traducendo la scelta in char e processandolo in una switch
        DebugLogger.println("AskOrRetry avviato");
        Character choice = server.talkTo(
                playerHashCode, message + " Riprovare(R) o Annullare(A)?").charAt(0);
        switch (choice) {
            //se vuole riprovare
            case 'R':
                this.server.sendTo(playerHashCode, "Riprova.");
                break;
            //se vuole annullare 
            default:                
                throw new ActionCancelledException("Abort.");
        }//switch
    }

    /**
     * Chiede, mandandogli la stringa message, al giocatore corrispondente all
     * hashCode, qual'è l'id del pastore da muovere tra i suoi numShepherd
     * pastori. Il metodo dev'essere invocato con numShepherd maggiore di zero.
     *
     * @param hashCode
     * @param numShepherd
     * @param message
     *
     * @return id pastore scelto
     */
    protected int askIdShepherd(int hashCode, int numShepherd, String message) {
        int idShepherd;
        do {
            //chiedi quale pastore muovere
            idShepherd = Integer.parseInt(this.server.talkTo(
                    this.hashCode(), "Quale pastore vuoi muovere?"));

            //la risposta sarà 1 o 2 quindi lo ricalibro sulla lunghezza dell'array
            idShepherd--;
        } while (idShepherd < 0 && idShepherd > numShepherd);  //fintanto che non va bene l'id
        return idShepherd;
    }

    /**
     * chiedo conferma per lanciare il dado al giocatore corrispondente al
     * playerHashCode. Ritorno sempre un valore random
     *
     * @param playerHashCode
     *
     * @return
     */
    protected int askAndThrowDice(int playerHashCode) {
        this.server.talkTo(playerHashCode,
                "Premi un tasto per lanciare dado?");
        //discard returned string e lancia il dado
        return Dice.roll();
    }

    /**
     * dato un pastore risale al giocatore
     *
     * @param shepherd
     *
     * @return player corrispondente al pastore
     */
    protected Player getPlayerByShepherd(Shepherd shepherd) {
        for (Player player : players) {
            for (int i = 0; i < shepherd4player; i++) {
                if (player.shepherd[i] == shepherd) {
                    return player;
                }
            }
        }
        return null;
    }
}
