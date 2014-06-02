package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Bank;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Dice;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NodeNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.TrasmissionController;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * E' il controllo della partita. Si occupa di crearne una a seconda del numero
 * dei giocatori.
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class GameManager implements Runnable {

    Thread myThread;

    protected final Map map;
    private List<Player> players = new ArrayList<Player>();
    private String[] clientNickNames;
    private final int playersNumber;
    protected TrasmissionController controller;
    /**
     * rappresenterà il segnalino indicante il primo giocatore del giro
     */
    private int firstPlayer;
    protected int currentPlayer;
    protected final int shepherd4player;
    protected final Bank bank;  //per permettere a player di usarlo

    public GameManager(List<String> clientNickNames,
                       TrasmissionController controller) {

        this.controller = controller;
        //salvo il numero di player
        this.playersNumber = clientNickNames.size();

        //salvo i loro nomi in un array
        this.clientNickNames = clientNickNames.toArray(new String[playersNumber]);

        //creo la mappa univoca del gioco
        this.map = new Map();

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
        try {
            //setto arraylist dei giocatori
            this.setUpPlayers();
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            //TODO stesso discorso della run()
        }

        controller.setNick2PlayerMap(this.clientNickNames, players);

        myThread = new Thread(this);
    }

    public void start() {
        myThread.start();
    }

    public void run() {
        try {
            this.startGame();
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            //TODO chiamare una funzione per avviasare il server
            //oppure lo faccio io ovvero setto il nickName offline
        }
    }

    /**
     * Riempie l'arraylist dei player assegnando ad ognuno il nickName
     * corrispondente nell'array dei nickName rispettando l'ordine
     */
    private void setUpPlayers() throws RemoteException {
        //per ogni giocatore
        for (int i = 0; i < playersNumber; i++) {
            try {
                //lo aggiungo alla lista dei giocatori
                players.add(new Player(this, clientNickNames[i]));
            } catch (RemoteException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                throw new RemoteException(
                        "Il player:" + clientNickNames[i] + " si è disconnesso");
            }
        }
    }

    /**
     * Metodo principale che viene invocato dal server thread per creare tutti
     * gli oggetti di una partita e avviarla
     */
    private void SetUpGame() throws RemoteException {
        DebugLogger.println("Avvio partita");

        controller.broadcastStartGame();

        DebugLogger.println("SetUpMap Avviato");
        this.setUpMap();

        DebugLogger.println("SetUpAnimals Avviato");
        this.setUpAnimals();

        DebugLogger.println("SetUpcards Avviato");
        this.setUpCards();

        DebugLogger.println("SetUpFences Avviato");
        this.setUpFences();

        DebugLogger.println("SetUpShift Avviato");
        this.setUpShift();
        DebugLogger.println(
                "SetUpShift Terminato: il primo giocatore e'" + this.firstPlayer);

        DebugLogger.println("SetUpinitial Avviato");
        this.setUpInitialCards();

        DebugLogger.println("broadcastinitial conditions");
        this.broadcastInitialConditions();

        DebugLogger.println("brodcast cards");
        this.brodcastCards();

        this.setUpShepherds();
        DebugLogger.println("SetUpshpherds terminato");

    }

    private void brodcastCards() throws RemoteException {
        for (int i = 0; i < playersNumber; i++) {
            refreshCards(i);
        }
    }

    private void refreshCards(int indexOfPlayer) throws RemoteException {
        int numberOfCards = players.get(indexOfPlayer).shepherd[0].getMyCards().size();

        for (int j = 0; j < numberOfCards; j++) {
            Card card = players.get(indexOfPlayer).shepherd[0].getMyCards().get(
                    j);
            controller.refreshCard(clientNickNames[indexOfPlayer],
                    card.getType().toString(), card.getValue());
        }
    }

    private void setUpInitialCards() {

        for (int i = 0; i < clientNickNames.length; i++) {
            //aggiungi la carta prendendola dalle carte iniziali della banca
            Card initialCard = this.bank.getInitialCard();

            this.players.get(i).shepherd[0].addCard(
                    initialCard);
        }

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
    private void setUpShepherds() throws RemoteException {
        int i;//indice giocatori
        int j;//indice pastori
        boolean outcomeOk;

        //per ogni playerint 
        for (i = 0; i < this.playersNumber; i++) {
            //setto il player corrente
            currentPlayer = (firstPlayer + i) % playersNumber;

            //per ogni suo pastore
            for (j = 0; j < this.shepherd4player; j++) {
                outcomeOk = false;
                while (!outcomeOk) {
                    //prova a chiedere la strada per il j-esimo pastore                    
                    outcomeOk = controller.askSetUpShepherd(
                            clientNickNames[currentPlayer], j);
                }//while               
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

    private void playTheGame() throws RemoteException {
        int[][] classification = new int[2][playersNumber];
        int numOfWinners = 1;

        try {
            DebugLogger.println("Avvio esecuzione giri");
            this.executeRounds();
        } catch (FinishedFencesException ex) {

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
//                this.server.sendTo(clientNickNames[classification[0][i]],
//                        "hai vinto! con" + classification[1][i]);
            }
            //per tutti gli altri
            for (; i < playersNumber; i++) {
//                this.server.sendTo(clientNickNames[classification[0][i]],
//                        "hai perso! con" + classification[1][i]);
            }

//            this.server.broadcastMessage(printResults(classification));
        }
    }

    public void startGame() throws RemoteException {
        DebugLogger.println("SetUpGameAvviato");
        this.SetUpGame();

        DebugLogger.println("SetUpGame Effettuato");
        this.playTheGame();

        //gameFinished
    }

    private void broadcastInitialConditions() throws RemoteException {

        //broadcast regions
        int numbOfSheep, numbOfLamb, numbOfRam;

        for (int i = 0; i < this.map.getRegions().length; i++) {
            numbOfLamb = 0;
            numbOfRam = 0;
            numbOfSheep = 0;
            Region region = this.map.getRegions()[i];
            for (Ovine ovine : region.getMyOvines()) {
                if (ovine.getType() == OvineType.SHEEP) {
                    numbOfSheep++;
                } else if (ovine.getType() == OvineType.LAMB) {
                    numbOfLamb++;
                } else if (ovine.getType() == OvineType.RAM) {
                    numbOfRam++;
                }
            }
            //refersh la regione a tutti i client
            for (String client : clientNickNames) {
                controller.refreshRegion(client, i, numbOfSheep,
                        numbOfRam, numbOfLamb);
            }
        }

        //broadcast streets
        boolean fence;
        String shepherdName;

        int streetsNumber = this.map.getStreets().length;
        for (int i = 0; i < streetsNumber; i++) {
            Street street = this.map.getStreets()[i];
            fence = false;
            shepherdName = null;
            if (street.hasFence()) {
                fence = true;
            } else if (street.hasShepherd()) {
                shepherdName = getPlayerNickNameByShepherd(street.getShepherd());
            }
            for (String client : clientNickNames) {
                controller.refreshStreet(client, i, fence, shepherdName);
            }
        }

    }

    private void executeRounds() throws FinishedFencesException, RemoteException {
        currentPlayer = this.firstPlayer;
        boolean lastRound = false;
        int numberOfShiftsMade = 0;

        while (!(lastRound && currentPlayer == this.firstPlayer)) {
            //prova a fare un turno
            DebugLogger.println("Avvio esecuzione turno");
            lastRound = this.executeShift(currentPlayer);

            //aggiorno il numero di giri
            numberOfShiftsMade++;

            //aggiorno il player che gioca 
            currentPlayer++;
            //conto in modulo playersNumber
            currentPlayer %= this.playersNumber;

            evolveLambs();
            broadcastInitialConditions();

            //controllo se ho finito il giro
            //se il prossimo a giocare è il primo del giro
            if (currentPlayer == this.firstPlayer) {
                //1)avvio il market  
                //FIXME this.startMarket();
                //2)muovo il lupo
                DebugLogger.println("muovo lupo");
                this.moveSpecialAnimal(this.map.getWolf());
                DebugLogger.println("lupo mosso");

            }
        }//while
    }

    private boolean executeShift(int player) throws FinishedFencesException,
                                                    RemoteException {
        DebugLogger.println("Broadcast giocatore di turno");

        controller.refreshCurrentPlayer(clientNickNames[player]);

        DebugLogger.println("Muovo pecora nera");
        //muovo la pecora nera
        this.moveSpecialAnimal(this.map.getBlackSheep());

        //faccio fare le azioni al giocatore
        for (int i = 0; i < GameConstants.NUM_ACTIONS.getValue(); i++) {

            DebugLogger.println(
                    "Avvio choose and make action per il player " + player);
            //scegli l'azione e falla
            this.players.get(player).chooseAndMakeAction();
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
//    protected Street askStreet(String playerName, int idShepherd) throws
//            StreetNotFoundException,
//            BusyStreetException {
//        String errorString = "Strada già occupata, prego riprovare:";
//
//        DebugLogger.println("Chiedo una strada in askStreet");
//
//        //raccogli decisione
//        String stringedStreet = this.server.talkTo(playerName,
//                "In quale strada vuoi posizionare il pastore " + Integer.toString(
//                        idShepherd + 1) + " ?");
//        DebugLogger.println("Risposta sulla strada ottenuta");
//        //traducila in oggetto steet 
//        Street chosenStreet = map.convertStringToStreet(stringedStreet);
//        DebugLogger.println("Conversione strada effettuata");
//        //se la strada è occuapata
//        if (!chosenStreet.isFree()) {
//            throw new BusyStreetException(errorString);
//            //solleva eccezione
//        }
//        //altrimenti ritorna la strada
//        return chosenStreet;
//    }
    /**
     * Chiede, mandandogli la stringa message, al giocatore corrispondente all
     * hashCode, qual'è l'id del pastore da muovere tra i suoi numShepherd
     * pastori. Il metodo dev'essere invocato con numShepherd maggiore di zero.
     *
     * @param playerNickName
     *
     * @return id pastore scelto
     */
//    protected int askIdShepherd(String playerNickName) {
//        int idShepherd;
//        String errorMessage;
//
//        DebugLogger.println("chiedo id pastore da muovere");
//        while (true) {
//            try {
//                //chiedi quale pastore muovere
//                idShepherd = Integer.parseInt(this.server.talkTo(
//                        playerNickName, "Quale pastore vuoi muovere?"));
//
//                //se l'id è valido
//                if (idShepherd > 0 && idShepherd <= shepherd4player) {
//                    this.server.sendTo(playerNickName, "pastore selezionato ok");
//                    break;
//                }
//                errorMessage = "Non esiste il pastore chiesto, prego riprovare.";
//
//            } catch (NumberFormatException e) {
//                Logger.getLogger(DebugLogger.class
//                        .getName()).log(
//                                Level.SEVERE, e.getMessage(), e);
//                errorMessage = "La stringa inserita non identifica un pastore, prego riprovare.";
//            }
//
//            this.server.sendTo(playerNickName, errorMessage);
//        }
//
//        //la risposta sarà 1 o 2 quindi lo ricalibro sulla lunghezza dell'array                   
//        return --idShepherd;
//    }
//    /**
//     * Chiede al player inviandogli la stringa message un id regione
//     *
//     * @param playerNickName
//     * @param message
//     *
//     * @return Regione corrispondente
//     *
//     * @throws RegionNotFoundException
//     */
//    protected Region askAboutRegion(String playerNickName, String message)
//            throws
//            RegionNotFoundException {
//        Region chosenRegion;
//        String stringedRegion = this.server.talkTo(playerNickName, message);
//        chosenRegion = map.convertStringToRegion(stringedRegion);
//        DebugLogger.println("regione ok");
//        this.server.sendTo(playerNickName, "regione ok");
//
//        return chosenRegion;
//    }
   
    private void moveSpecialAnimal(SpecialAnimal animal) throws RemoteException {
        //salvo la regione in cui si trova l'animale
        Region actualAnimalRegion = animal.getMyRegion();
        int startRegionIndex = 0;
        Region endRegion;
        //cerco la strada che dovrebbe attraversare
        Street potentialWalkthroughStreet;
        int streetValue = Dice.roll();
        
        try {
            startRegionIndex = map.getNodeIndex(
                    actualAnimalRegion);

            potentialWalkthroughStreet = this.map.getStreetByValue(
                    actualAnimalRegion, streetValue);

            //calcola regione d'arrivo
            endRegion = this.map.getEndRegion(actualAnimalRegion,
                    potentialWalkthroughStreet);

            //cerco di farlo passare (nel caso del lupo si occupa pure di farlo mangiare)
            String result = animal.moveThrough(potentialWalkthroughStreet,
                    endRegion);

            //tutto ok      
            controller.refreshSpecialAnimal(animal,
                    result + "," + streetValue + "," + startRegionIndex + "," + map.getNodeIndex(
                            endRegion));
        } catch (StreetNotFoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "nok" + ex.getMessage(), ex);
            controller.refreshSpecialAnimal(animal,
                    "nok" + "," + streetValue + "," + startRegionIndex);
        } catch (RegionNotFoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "nok" + ex.getMessage(), ex);
            controller.refreshSpecialAnimal(animal,
                    "nok" + "," + streetValue + "," + startRegionIndex);
        } catch (NodeNotFoundException ex) {
            //non può accadere
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "nok" + ex.getMessage(), ex);
           controller.refreshSpecialAnimal(animal,
                    "nok" + "," + streetValue + "," + startRegionIndex);
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

    /**
     * dato un pastore risale al giocatore
     *
     * @param shepherd
     *
     * @return player corrispondente al pastore
     */
    protected String getPlayerNickNameByShepherd(Shepherd shepherd) {
        for (Player player : players) {
            for (int i = 0; i < shepherd4player; i++) {
                if (player.shepherd[i] == shepherd) {
                    return player.getPlayerNickName();
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
        for (Player player : players) {
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

    private void evolveLambs() {
        //per tutti gli ovini che sono agnelli
        for (Region region : this.map.getRegions()) {
            for (Ovine ovine : region.getMyOvines()) {
                if (ovine.getType() == OvineType.LAMB) {
                    //se l'età è quella della trasformazione
                    if (ovine.getAge() == GameConstants.LAMB_EVOLUTION_AGE.getValue()) {
                        //trasformalo
                        ovine.setType(OvineType.getRandomLambEvolution());
                    } else {
                        //altrimenti
                        //aumenta l'età
                        ovine.setAge(ovine.getAge() + 1);
                    }
                }

            }
        }
    }
}
