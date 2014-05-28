package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionException;
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
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.CannotMoveAnimalException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NodeNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerThread;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
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
    private List<PlayerImp> players = new ArrayList<PlayerImp>();
    private String clientNickNames[];
    private final int playersNumber;
    /**
     * rappresenterà il segnalino indicante il primo giocatore del giro
     */
    private int firstPlayer;
    protected int currentPlayer;
    protected final int shepherd4player;
    protected final Bank bank;  //per permettere a player di usarlo

    public GameManager(List<String> clientNickNames, ServerThread server) {
        //salvo il numero di player
        this.playersNumber = clientNickNames.size();

        //salvo i loro nomi in un array
        this.clientNickNames = clientNickNames.toArray(new String[playersNumber]);

        //creo la mappa univoca del gioco
        this.map = new Map();

        //creo il collegamento all'univoco serverThread
        this.server = server;

        //creo la banca
        this.bank = new Bank(GameConstants.NUM_CARDS.getValue(),
                GameConstants.NUM_INITIAL_CARDS.getValue(),
                GameConstants.NUM_FENCES.getValue());

        //setto il pastore principale
        if (this.playersNumber <= ControlConstants.NUM_FEW_PLAYERS.getValue()) {
            this.shepherd4player = ControlConstants.SHEPHERD_FOR_FEW_PLAYERS.getValue();
        } else {
            this.shepherd4player = ControlConstants.STANDARD_SHEPHERD_FOR_PLAYER.getValue();
        }
        //setto arraylist dei giocatori 
        this.setUpPlayers();
    }

    /**
     * Riempie l'arraylist dei player e riempie l'array dei rispettivi hashcode
     *
     */
    private void setUpPlayers() {
        //per ogni giocatore
        for (int i = 0; i < playersNumber; i++) {
            //lo aggiungo alla lista dei giocatori
            players.add(new PlayerImp(this, clientNickNames[i]));
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

        //per ogni playerint 
        for (i = 0; i < this.playersNumber; i++) {
            //setto il player corrente
            currentPlayer = (firstPlayer + i) % playersNumber;

            //sveglia il currentPlayer 
            server.sendTo(clientNickNames[currentPlayer], "E' il tuo turno");
            DebugLogger.println("E' il turno inviato shepherds");

            //per ogni suo pastore
            for (j = 0; j < this.shepherd4player; j++) {
                while (true) {
                    //prova a chiedere la strada per il j-esimo pastore
                    try {
                        //se ho un valore di ritorno
                        chosenStreet = askStreet(
                                clientNickNames[currentPlayer], j);
                        break;
                        //se strada non trovata 
                    } catch (StreetNotFoundException ex) {
                        //invio msg strada non trovata e ricomincia loop
                        this.server.sendTo(clientNickNames[currentPlayer],
                                ex.getMessage());
                        Logger.getLogger(DebugLogger.class.getName()).log(
                                Level.SEVERE, ex.getMessage(), ex);
                        //se la strada è occupata
                    } catch (BusyStreetException e) {
                        //manda il messaggio di errore al client e ricomincia il loop
                        this.server.sendTo(clientNickNames[currentPlayer],
                                e.getMessage());
                        Logger.getLogger(DebugLogger.class.getName()).log(
                                Level.SEVERE, e.getMessage(), e);
                    }
                }//while
                this.server.sendTo(clientNickNames[currentPlayer],
                        "Pastore accettato");
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
                this.players.get(currentPlayer).shepherd[0].addCard(
                        initialCard);

                DebugLogger.println("invio conferma");
                //invia conferma riepilogativa all'utente
                this.server.sendTo(clientNickNames[currentPlayer],
                        "Pastore posizionato. Hai una carta terreno di tipo: " + initialCard.getType().toString());

                try {
                    //aggiorna gli altri
                    this.server.broadcastExcept(
                            "Il giocatore " + currentPlayer + " ha posizionato il pastore " + j + " nella strada " + this.map.getNodeIndex(
                                    chosenStreet),
                            clientNickNames[currentPlayer]);
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

    //TODO modularizzare
    private void playTheGame() {
        int[][] classification = new int[2][playersNumber];
        int numOfWinners = 1;
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
            //stilo la classifica in ordine decrescente
            classification = this.calculatePoints();

            //calcolo quanti sono al primo posto a parimerito
            while (classification[1][numOfWinners] == classification[1][numOfWinners + 1]) {
                numOfWinners++;
            }

            int i;
            //per tutti i vincitori
            for (i = 0; i < numOfWinners; i++) {
                this.server.sendTo(clientNickNames[classification[0][i]],
                        "hai vinto! con" + classification[1][i]);
            }
            //per tutti gli altri
            for (; i < playersNumber; i++) {
                this.server.sendTo(clientNickNames[classification[0][i]],
                        "hai perso! con" + classification[1][i]);
            }

            this.server.broadcastMessage(printResults(classification));
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
        //LandData landData = this.map.createLandData();
        //GameData gameData = this.createGameData();
        //add cards
        //CompleteDataTransfer data = createDataTransfer();
        //server.broadcastInitialConditon(data)
        for (int i = 0; i < playersNumber; i++) {
            this.server.sendTo(clientNickNames[i],
                    "Ci sono :" + playersNumber + " giocatori, tu sei il numero :" + i
                    + "; ogni giocatore ha :" + this.shepherd4player
                    + "pastori. Il primo del turno è :" + firstPlayer
                    + ". Ogni giocatore ha :" + GameConstants.NUM_ACTIONS.getValue() + " azioni.");
        }
    }
    
    

    private void executeRounds() throws FinishedFencesException {
        currentPlayer = this.firstPlayer;
        boolean lastRound = false;
        //TODO dicutere il fine giro per la discordanza 11recinti 12recintni
        //se non è l'ultimo giro o il giocatore non è l'ultimo del giro
        while (!(lastRound && currentPlayer == this.firstPlayer)) {
            //prova a fare un turno
            DebugLogger.println("Avvio esecuzione turno");
            //TODO: spostare mossa pecora nera e controllo recinti?
            lastRound = this.executeShift(currentPlayer);

            //aggiorno il player che gioca 
            currentPlayer++;
            //conto in modulo playersNumber
            currentPlayer %= this.playersNumber;

            //controllo se ho finito il giro
            //se il prossimo a giocare è il primo del giro
            if (currentPlayer == this.firstPlayer) {
                //1)avvio il market  
                //FIXME this.startMarket();
                //2)muovo il lupo
                try {
                    DebugLogger.println("muovo lupo");
                    this.moveSpecialAnimal(this.map.getWolf());
                    this.server.broadcastMessage(
                            "Il lupo si è mosso in: " + this.map.getNodeIndex(
                                    this.map.getWolf().getMyRegion()));
                } catch (CannotMoveAnimalException e) {
                    DebugLogger.println(
                            "il lupo non si muove perchè " + e.getMessage());

                    this.server.broadcastMessage(
                            "Il lupo non si muove perchè " + e.getMessage());
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE, e.getMessage(), e);
                } catch (NodeNotFoundException ex) {
                    //non può verificarsi in questa occasione
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);
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
                    Level.SEVERE,
                    "La pecora non si muove perchè: " + e.getMessage(), e);
        } catch (NodeNotFoundException ex) {
            //non può verificarsi perchè se la pecora si muove allora il nodo esiste
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "La pecora non si muove perchè: " + ex.getMessage(), ex);
        }

        //sveglia il client
        server.sendTo(clientNickNames[player], "E' il tuo turno");
        DebugLogger.println("E' il tuo turno inviato");

        //faccio fare le azioni al giocatore
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
                    this.server.sendTo(clientNickNames[player],
                            "err:" + ex.getMessage());
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
        //se sono finiti i recinti normali chiamo l'ultimo giro
        return this.bank.numberOfUsedFence() >= GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.getValue();
    }

    /**
     * Chiede al giocatore corrispondente all playerHashCode, in quale strada
     * posizionare il pastore il cui id è idShepherd
     *
     * @param playerName
     * @param idShepherd
     *
     * @return
     *
     * @throws StreetNotFoundException se la strada non esiste
     * @throws BusyStreetException     se la strada è occupata
     */
    protected Street askStreet(String playerName, int idShepherd) throws
            StreetNotFoundException,
            BusyStreetException {
        String errorString = "Strada già occupata, prego riprovare:";

        DebugLogger.println("Chiedo una strada in askStreet");

        //raccogli decisione
        String stringedStreet = this.server.talkTo(playerName,
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
     * Chiede, mandandogli la stringa message, al giocatore corrispondente all
     * hashCode, qual'è l'id del pastore da muovere tra i suoi numShepherd
     * pastori. Il metodo dev'essere invocato con numShepherd maggiore di zero.
     *
     * @param playerNickName
     *
     * @return id pastore scelto
     */
    protected int askIdShepherd(String playerNickName) {
        int idShepherd;
        String errorMessage;

        DebugLogger.println("chiedo id pastore da muovere");
        while (true) {
            try {
                //chiedi quale pastore muovere
                idShepherd = Integer.parseInt(this.server.talkTo(
                        playerNickName, "Quale pastore vuoi muovere?"));

                //se l'id è valido
                if (idShepherd > 0 && idShepherd <= shepherd4player) {
                    this.server.sendTo(playerNickName, "pastore selezionato ok");
                    break;
                }
                errorMessage = "Non esiste il pastore chiesto, prego riprovare.";

            } catch (NumberFormatException e) {
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE, e.getMessage(), e);
                errorMessage = "La stringa inserita non identifica un pastore, prego riprovare.";
            }

            this.server.sendTo(playerNickName, errorMessage);
        }

        //la risposta sarà 1 o 2 quindi lo ricalibro sulla lunghezza dell'array                   
        return --idShepherd;
    }

    /**
     * Chiede al player inviandogli la stringa message un id regione
     *
     * @param playerNickName
     * @param message
     *
     * @return Regione corrispondente
     *
     * @throws RegionNotFoundException
     */
    protected Region askAboutRegion(String playerNickName, String message)
            throws
            RegionNotFoundException {
        Region chosenRegion;
        String stringedRegion = this.server.talkTo(playerNickName, message);
        chosenRegion = map.convertStringToRegion(stringedRegion);
        DebugLogger.println("regione ok");
        this.server.sendTo(playerNickName, "regione ok");

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
                    "La strada indicata dal dado non esiste");
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

    protected void askCancelOrRetry(String playerNickName, String message)
            throws
            ActionCancelledException {
        //chiedo cosa vuole fare traducendo la scelta in char e processandolo in una switch
        DebugLogger.println("AskOrRetry avviato");
        Character choice = server.talkTo(
                playerNickName, message + " Riprovare(R) o Annullare(A)?").charAt(
                        0);
        switch (choice) {
            //se vuole riprovare
            case 'R':
                this.server.sendTo(playerNickName, "Riprova.");
                break;
            //se vuole annullare 
            default:
                throw new ActionCancelledException("Abort.");
        }//switch
    }

    /**
     * chiedo conferma per lanciare il dado al giocatore corrispondente al
     * playerHashCode. Ritorno sempre un valore random
     *
     * @param playerNickName
     *
     * @return
     */
    protected int askAndThrowDice(String playerNickName) {
        this.server.talkTo(playerNickName,
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
    protected PlayerImp getPlayerByShepherd(Shepherd shepherd) {
        for (PlayerImp player : players) {
            for (int i = 0; i < shepherd4player; i++) {
                if (player.shepherd[i] == shepherd) {
                    return player;
                }
            }
        }
        return null;
    }

    private int[][] calculatePoints() {
        int[][] classification = new int[2][playersNumber];
        int tmp1, tmp2;
        //per ogni giocatore
        int i = 0;
        for (PlayerImp player : players) {
            //per ogni tipo di regione
            classification[0][i] = i;
            for (RegionType type : RegionType.values()) {
                //aggiungo al suo punteggio num di pecore in quel tipo di regione per num di carte di quel tipo
                classification[1][i] += player.shepherd[0].numOfMyCardsOfType(
                        type) * map.numOfOvineIn(type);
            }
            i++;
        }

        //ordino la classifica
        for (int j = 0; j < playersNumber; j++) {
            for (int k = j; k < playersNumber; k++) {
                if (classification[1][j] < classification[1][k]) {
                    tmp1 = classification[0][j];
                    tmp2 = classification[1][j];
                    classification[0][j] = classification[0][k];
                    classification[1][j] = classification[1][k];
                    classification[0][k] = tmp1;
                    classification[1][k] = tmp2;
                }
            }
        }

        return classification;
    }

    private String printResults(int[][] classification) {
        String result = "";
        for (int i = 0; i < playersNumber; i++) {
            result += i + "posto: player" + classification[0][i] + "con" + classification[1][i];
        }
        return result;
    }
}
