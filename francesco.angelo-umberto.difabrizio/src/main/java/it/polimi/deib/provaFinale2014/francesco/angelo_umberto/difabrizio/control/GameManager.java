package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.UnexpectedEndOfGameException;
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
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerManager;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.TrasmissionController;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.PlayerDisconnectedException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.SocketClientProxy;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.IOException;
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

    private final Thread myThread;
    /**
     * The map of a certain game. It holds the charateristics of the region and
     * of the streets as well as the position of the blackSheep and the wolf
     */
    private final Map map;
    private List<Player> players = new ArrayList<Player>();
    private String[] clientNickNames;
    private final int playersNumber;
    /**
     * It's the controller of the trasmission, it has the duty of use the right
     * trasmission between server and client (rmi or socket)
     */
    private final TrasmissionController controller;
    /**
     * rappresenterà il segnalino indicante il primo giocatore del giro
     */
    private int firstPlayer;
    /**
     * It's the player currently playing
     */
    private int currentPlayer;
    /**
     * It's the number of shepherd that each player has
     */
    protected final int shepherd4player;
    /**
     * It's the bank which stores fences and cards so that the game manager can
     * take them during the game
     */
    private final Bank bank;

    /**
     * Creates a game manager connecting it to a given list of clientNickNames
     * and with a specified type of connection
     *
     * @param clientNickNames NickNames of the clients connected to the game
     * @param controller      Type of connection between cllient and server
     */
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

    /**
     * It's the method to start the gameManager as a thread
     */
    public void start() {
        myThread.start();
    }

    public void run() {

        this.startGame();

    }

    public TrasmissionController getController() {
        return controller;
    }

    public Map getMap() {
        return map;
    }

    public Bank getBank() {
        return bank;
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
    private void setUpGame() {
        DebugLogger.println("Avvio partita");

        brodcastStartGame();

        setUpMap();

        setUpAnimals();

        setUpCards();

        setUpFences();

        setUpShift();

        DebugLogger.println(
                "SetUpShift Terminato: il primo giocatore e'" + this.firstPlayer);

        setUpInitialCards();

        brodcastInitialCondition();

        brodcastCards();

        this.setUpShepherds();

        DebugLogger.println("SetUpshpherds terminato");

    }

    private void brodcastStartGame() {
        for (String client : clientNickNames) {
            controller.refreshStartGame(client);
        }
    }

    private void brodcastInitialCondition() {
        for (String client : clientNickNames) {
            this.refreshInitialConditions(client);
        }
    }

    private void brodcastCards() {
        for (int i = 0; i < playersNumber; i++) {
            refreshCards(i);
        }
    }

    private void refreshCards(int indexOfPlayer) {
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
        int sheepsburgIndex = 18;
        //recupera l'array delle regioni
        Region[] region = this.map.getRegions();

        //per ogni regione tranne shepsburg
        for (int i = 0; i < region.length - 1; i++) {
            //aggiungi un ovino (a caso)          
            region[i].addOvine(new Ovine());
        }

        //posiziono lupo e pecora nera a sheepsburg
        map.getBlackSheep().setAt(map.getRegions()[sheepsburgIndex]);
        map.getWolf().setAt(map.getRegions()[sheepsburgIndex]);
    }

    /**
     * Chiede ad ogni giocatore dove posizionare il proprio pastore
     */
    private void setUpShepherds() {
        int i;
        int j;

        //per ogni player 
        for (i = 0; i < this.playersNumber; i++) {
            //setto il player corrente
            currentPlayer = (firstPlayer + i) % playersNumber;

            //per ogni suo pastore
            try {
                for (j = 0; j < this.shepherd4player; j++) {
                    players.get(currentPlayer).setMyshepherd(j);
                }
            } catch (PlayerDisconnectedException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);
                //player disconnesso salto i suoi pastori

                controller.brodcastPlayerDisconnected(
                        clientNickNames[currentPlayer]);
            }
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

    private void playTheGame() throws UnexpectedEndOfGameException {
        int[][] classification;
        int numOfWinners = 1;

        try {
            DebugLogger.println("Avvio esecuzione giri");
            this.executeRounds();
        } catch (FinishedFencesException ex) {

            Logger.getLogger(DebugLogger.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        }
        //se il gioco va come deve o se finisco i recinti quando non devono cmq calcolo i punteggi
        //stilo la classifica in ordine decrescente
        classification = this.calculatePoints();

        //calcolo quanti sono al primo posto a parimerito
        for (int i = 0; i < classification[1].length - 1; i++) {
            if (classification[1][i] == classification[1][i + 1]) {
                numOfWinners++;
            }
        }

        int i;
        //per tutti i vincitori
        for (i = 0; i < numOfWinners; i++) {
            controller.sendRank(true, clientNickNames[classification[0][i]],
                    classification[1][i]);
        }

        //per tutti gli altri
        for (; i < playersNumber; i++) {
            controller.sendRank(false, clientNickNames[classification[0][i]],
                    classification[1][i]);

        }

        DebugLogger.println("invio classifica");

        controller.sendClassification(classificationToString(classification));

    }

    private void startGame() {
        DebugLogger.println("SetUpGameAvviato");
        this.setUpGame();

        DebugLogger.println("SetUpGame Effettuato");
        try {
            this.playTheGame();
        } catch (UnexpectedEndOfGameException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            //avvioso tutti i player della fine del gioco improvvisa
            //questo perchè se c'è solo un giocatore in partita la chiudo
            //però lo devo avvisare
            controller.unexpectedEndOfGame();
        }

        //gameFinished
        DebugLogger.println("Gioco terminato");

        //elimo i nickName dalla mappa e se sono socket chiudo il socket
        for (String client : clientNickNames) {
            try {
                if (ServerManager.Nick2ClientProxyMap.get(
                        client) instanceof SocketClientProxy) {
                    ((SocketClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            client)).getSocket().close();
                }

                ServerManager.Nick2ClientProxyMap.remove(client);
            } catch (IOException ex) {
                //il client che stavo eliminando ha già chiuso il socket
                //poco male
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
            }
        }

        //diminuisco il numero di partite attive
        ServerManager.activatedGames--;
    }

    private void refreshRegions(String client) {
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
            controller.refreshRegion(client, i, numbOfSheep,
                    numbOfRam, numbOfLamb);
        }

    }

    private void refreshStreets(String client) {
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
                shepherdName = getPlayerNickNameByShepherd(
                        street.getShepherd());
            }
            controller.refreshStreet(client, i, fence, shepherdName);
        }
    }

    private void refreshSpecialAnimals(String client) {
        try {
            //refresh wolf position
            controller.refreshSpecialAnimalInitialPosition(client,
                    map.getWolf(), "" + map.getNodeIndex(
                            map.getWolf().getMyRegion()));
            //refresh blacksheep position
            controller.refreshSpecialAnimalInitialPosition(client,
                    map.getBlackSheep(), "" + map.getNodeIndex(
                            map.getBlackSheep().getMyRegion()));

        } catch (NodeNotFoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(),
                    ex);
        }

    }

    private void refreshInitialConditions(String client) {

        refreshRegions(client);

        refreshStreets(client);

        controller.refreshMoney(client);

        refreshSpecialAnimals(client);

        //broadcast nickNames
        //broadcast shepherd
    }

    private void executeRounds() throws FinishedFencesException,
                                        UnexpectedEndOfGameException {
        currentPlayer = this.firstPlayer;
        boolean lastRound = false;
        int playerOffline = 0;

        while (!(lastRound && roundComplete())) {
            //prova a fare un turno
            DebugLogger.println("Avvio esecuzione turno");

            //se il player è Online
            if (ServerManager.Nick2ClientProxyMap.get(
                    clientNickNames[currentPlayer]).isOnline()) {
                try {
                    handleReconnection();

                    //there's a player online at least
                    playerOffline = 1;

                    //before starting anyone shift the last action is setted 
                    //to none of the possibles
                    players.get(currentPlayer).lastAction = ActionConstants.NO_ACTION.getValue();

                    //the shepherd used is set to none too
                    players.get(currentPlayer).lastShepherd = null;

                    lastRound = this.executeShift(currentPlayer);
                } catch (PlayerDisconnectedException ex) {
                    //il giocatore si disconnette durante il suo turno
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);

                    controller.brodcastPlayerDisconnected(
                            clientNickNames[currentPlayer]);
                }
                nextPlayer();

                evolveLambs();

                //controllo se ho finito il giro
                //se il prossimo a giocare è il primo del giro
                if (currentPlayer == this.firstPlayer) {
                    //avvio il market  
                    //FIXME this.startMarket();
                    //muovo il lupo
                    DebugLogger.println("muovo lupo");
                    this.moveSpecialAnimal(this.map.getWolf());
                    DebugLogger.println("lupo mosso");
                }

            } else {
                DebugLogger.println(
                        "Player offline:" + clientNickNames[currentPlayer]);
                //player offline
                playerOffline++;

                //skip player               
                controller.brodcastPlayerDisconnected(
                        clientNickNames[currentPlayer]);

                if (playerOffline == this.playersNumber) {
                    //tutti i player sono offline termino la partita
                    throw new UnexpectedEndOfGameException(
                            "Tutti i player si sono disconnesi, la partita termina");
                }

                nextPlayer();
            }
        }
    }

    private boolean roundComplete() {
        if (ServerManager.Nick2ClientProxyMap.get(
                clientNickNames[firstPlayer]).isOnline()) {
            return currentPlayer == this.firstPlayer;
        }
        int temporaryFirstPlayer;

        for (int i = 0; i < this.playersNumber; i++) {
            //aggiorno il firstPlayer temporaneo
            temporaryFirstPlayer = (this.firstPlayer + 1) % this.playersNumber;

            if (ServerManager.Nick2ClientProxyMap.get(
                    clientNickNames[temporaryFirstPlayer]).isOnline()) {
                return currentPlayer == temporaryFirstPlayer;
            }
        }

        //se ho girato tutti i player e nessuno è online
        // ritorno false e sarà l'execute rounds ad accorgersi di dover terminare
        //la partita senza client
        return false;
    }

    private void nextPlayer() {
        //aggiorno il player che gioca 
        currentPlayer++;
        //conto in modulo playersNumber
        currentPlayer %= this.playersNumber;
    }

    private void handleReconnection() throws PlayerDisconnectedException {
        //controllo se il player ha bisogno di un refresh
        if (ServerManager.Nick2ClientProxyMap.get(
                clientNickNames[currentPlayer]).needRefresh()) {

            //setto il needRefresh a false
            ServerManager.Nick2ClientProxyMap.get(
                    clientNickNames[currentPlayer]).setRefreshNeeded(false);

            //avvio il player che è tornato in partita
            controller.refreshStartGame(clientNickNames[currentPlayer]);

            //lo aggiorno
            this.refreshInitialConditions(clientNickNames[currentPlayer]);

            //gli chiedo di settare tutti i pastori che non aveva settato
            int shepherdToSet = ServerManager.Nick2ClientProxyMap.get(
                    clientNickNames[currentPlayer]).getNumberOfShepherdStillToSet();

            for (int i = 0; i < shepherdToSet; i++) {
                players.get(currentPlayer).setMyshepherd(
                        shepherd4player - shepherdToSet + i);
            }

        }

    }

    private boolean executeShift(int player) throws FinishedFencesException,
                                                    PlayerDisconnectedException {
        DebugLogger.println("Broadcast giocatore di turno");

        controller.brodcastCurrentPlayer(clientNickNames[player]);

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

    private void moveSpecialAnimal(SpecialAnimal animal) {
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
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            "nok" + ex.getMessage(), ex);
            controller.refreshSpecialAnimal(animal,
                    "nok" + "," + streetValue + "," + startRegionIndex);
        } catch (RegionNotFoundException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            "nok" + ex.getMessage(), ex);
            controller.refreshSpecialAnimal(animal,
                    "nok" + "," + streetValue + "," + startRegionIndex);
        } catch (NodeNotFoundException ex) {
            //non può accadere
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
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
    private String getPlayerNickNameByShepherd(Shepherd shepherd) {
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
        DebugLogger.println("Calculate points");

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
                        type) * map.numberOfOvineIn(type);
            }
            //aggiungo i soldi avanzati
            classification[1][i] += player.shepherd[0].getWallet().getAmount();

            DebugLogger.println(
                    "player " + clientNickNames[i] + " punti " + classification[1][i]);
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

    private String classificationToString(int[][] classification) {
        String result = "";
        for (int i = 0; i < playersNumber; i++) {
            result += clientNickNames[classification[0][i]] + "," + classification[1][i] + ",";
        }
        DebugLogger.println("creta classifica stringhizzata " + result);
        return result;
    }

    private void evolveLambs() {
        //per tutti gli ovini sulla mappa
        for (Ovine ovine : map.getAllOvines()) {
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
