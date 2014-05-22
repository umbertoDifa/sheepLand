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
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.BusyStreetException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerManager;
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
     * Creo un logger per il sistema
     */
    private final static Logger logger = Logger.getLogger(
            ServerManager.class.getName());

    /**
     * Crea un GameManager
     *
     * @param playersNumber Numero dei giocatori di una partita
     * @param server Thread che gestisce la partita
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
            players.add(new Player(this, this.shepherd4player));
            //salvo il suo hashcode
            playersHashCode[i] = players.get(i).hashCode();
        }
    }

    /**
     * Metodo principale che viene invocato dal server thread per creare tutti
     * gli oggetti di una partita e avviarla
     */
    private void SetUpGame() {
        logger.info("SetUpMap Avviato");
        this.setUpMap();
        logger.info("SetUpAnimals Avviato");

        this.setUpAnimals();
        logger.info("SetUpSheperds Avviato");

        this.setUpShepherds();
        logger.info("SetUpCards Avviato");

        this.setUpCards();
        logger.info("SetUpFences Avviato");

        this.setUpFences();
        logger.info("SetUpShift Avviato");

        this.setUpShift();
        logger.log(Level.INFO, "SetUpShift Terminato: il primo giocatore \u00e8 {0}", this.firstPlayer);

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
        //per ogni regione
        for (Region reg : region) {
            //aggiungi un ovino (a caso)          
            reg.addOvine(new Ovine());
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

        //per ogni playerint 
        for (i = 0; i < this.playersNumber; i++) {
            //indice pastori
            int j;
            //per ogni suo pastore
            for (j = 0; j < this.shepherd4player; j++) {
                while (true) {
                    //prova a chiedere la strada per il j-esimo pastore
                    try {
                        //se ho un valore di ritorno
                        chosenStreet = askStreet(this.playersHashCode[i], j);
                        break;
                        //se strada non trovata 
                    } catch (StreetNotFoundException ex) {
                        //invio msg strada non trovata e ricomincia loop
                        this.server.sendTo(this.playersHashCode[i],
                                ex.getMessage());
                        //se la strada è occupata
                    } catch (BusyStreetException e) {
                        //manda il messaggio di errore al client e ricomincia il loop
                        this.server.sendTo(this.playersHashCode[i],
                                e.getMessage());
                    }
                }//while
                logger.log(Level.INFO,
                        "Setto il pastore: {0} del giocatore: {1}",
                        new Object[]{j, i});
                //sposta il pastore 
                this.players.get(i).getShepherd(j).moveTo(chosenStreet);
                logger.info("Pastore settato");

                //creo una carta con valore 0 e di tipo casuale e l'aggiungo a 
                //quelle del pastore corrispondente al mio player
                //aggiungi la carta prendendola dalle carte iniziali della banca
                logger.info("Prendo una carta dalla banca");
                Card initialCard = this.bank.getInitialCard();

                logger.info("Aggiungo la carta al pastore");
                this.players.get(i).getShepherd(j).addCard(initialCard);

                logger.info("invio conferma");
                //invia conferma riepilogativa
                this.server.sendTo(this.playersHashCode[i],
                        "Pastore posizionato. Hai una carta terreno di tipo: " + initialCard.getType().toString());
            }//for pastori
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
        //per ogni tipo di regione - sheepsburg  
        for (int i = 0; i < RegionType.values().length - 1; i++) {
            //per tante quante sono le carte di ogni tipo
            for (int j = 0; j < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); j++) {
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
            logger.info("Avvio esecuzione giri");
            this.executeRounds();
        } catch (FinishedFencesException ex) {
            this.getServer().broadcastMessage(
                    "I recinti totali sono finiti, fine gioco e calcolo dei punteggi");
        } finally {
            //se il gioco va come deve o se finisco i recinti quando non devono cmq calcolo i punteggi
            //TODO:decidere scelta fatta sopra
            //this.calculatePoints();
            //this.broadcastWinner();
        }
    }

    public void startGame() {
        logger.info("SetUpGameAvviato");
        this.SetUpGame();
        logger.info("SetUpGame Effettuato");
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
            logger.info("Avvio esecuzione turno");
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
                this.moveSpecialAnimal(this.map.getWolf());
            }
        }//while
    }

    private boolean executeShift(int player) throws FinishedFencesException {
        String noMoreFenceMessage = "Recinti Finiti!";

        logger.info("Muovo pecora nera");
        //muovo la pecora nera
        this.moveSpecialAnimal(this.map.getBlackSheep());
        logger.info("pecora nera mossa");
        //faccio fare le azioni al giocatore
        //per il numero di azioni possibili per un turno
        for (int i = 0; i < GameConstants.NUM_ACTIONS.getValue(); i++) {
            while (true) {
                try {
                    logger.info("Avvio choose and make action");
                    //scegli l'azione e falla
                    this.players.get(player).chooseAndMakeAction();
                    //se non arriva un l'eccezione passo alla prossima azione
                    break;
                } catch (ActionException ex) {
                    //avvisa e riavvia la procedura di scelta dell'i-esima azione
                    this.server.sendTo(playersHashCode[player], ex.getMessage());
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
     * @throws BusyStreetException se la strada è occupata
     */
    protected Street askStreet(int playerHashCode, int idShepherd) throws
            StreetNotFoundException,
            BusyStreetException {
        String errorString = "Strada già occupata, prego riprovare:";

        logger.info("Chiedo una strada in askStreet");
        
        //raccogli decisione
        String stringedStreet = this.server.talkTo(playerHashCode,
                "In quale strada vuoi posizionare il pastore " + Integer.toString(
                        idShepherd + 1) + " ?");
        logger.info("Risposta sulla strada ottenuta");
        //traducila in oggetto steet 
        Street chosenStreet = map.convertStringToStreet(stringedStreet);
        logger.info("Conversione strada effettuata");
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
        //iteratore sui player
        int i;
        //per ogni player 
        for (i = 0; i < this.playersNumber; i++) {
            //raccogli cosa vuole vendere
            this.players.get(i).sellCards();
        }
        //lancia il dado per sapere il primo a comprare
        //il modulo serve ad essere sicuro che venga selezionato un player esistente
        int playerThatBuys = Dice.getRandomValue() % this.playersNumber;
        //per ogni player 
        for (i = 0; i < this.playersNumber; i++) {
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
            //se vuole riprovare
            case 'R':
                break;
            //se vuole annullare o se mette una roba a caso
            default:
                throw new ActionCancelledException("Azione annullata");
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
            idShepherd = Integer.parseInt(this.getServer().talkTo(
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
        this.getServer().talkTo(playerHashCode, "vuoi lanciare dado?");
        return Dice.getRandomValue();
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
                if (player.getShepherd(i) == shepherd) {
                    return player;
                }
            }
        }
        return null;
    }
}
