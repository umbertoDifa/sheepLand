package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.UnexpectedEndOfGameException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Bank;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Dice;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Market;
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
    /**
     * List of players in the game
     */
    protected final List<Player> players = new ArrayList<Player>();
    private final String[] clientNickNames;
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

    private final Market market;

    /**
     * Creates a game manager connecting it to a given list of clientNickNames
     * and with a specified type of connection
     *
     * @param clientNickNames NickNames of the clients connected to the game
     * @param controller Type of connection between cllient and server
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

        this.market = new Market(playersNumber);

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

    /**
     * {@inheritDoc }
     */
    public void run() {

        this.startGame();

    }

    /**
     * Returns the trasmission controller of a certain gameManager
     *
     * @return the Trasmission controller
     */
    public TrasmissionController getController() {
        return controller;
    }

    /**
     * Returns the map of a given gam
     *
     * @return The map of the game
     */
    public Map getMap() {
        return map;
    }

    /**
     * Returns the bank of the game
     *
     * @return the bank
     */
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
                players.add(new Player(this, clientNickNames[i], market));
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

        this.firstPlayer = setUpShift();

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
        int clearPastCards = - 2;

        int numberOfCards = players.get(indexOfPlayer).shepherd[0].getMyCards().size();
        controller.refreshCard(clientNickNames[indexOfPlayer],
                RegionType.COUNTRYSIDE.toString(), clearPastCards);

        for (int j = 0; j < numberOfCards; j++) {
            Card card = players.get(indexOfPlayer).shepherd[0].getMyCards().get(
                    j);
            DebugLogger.println("Invio carta: " + card.getType().toString() + "al giocatore :" + indexOfPlayer);
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

            //brodcast il player corrente
            controller.brodcastCurrentPlayer(clientNickNames[currentPlayer]);

            //per ogni suo pastore
            try {
                for (j = 0; j < this.shepherd4player; j++) {
                    players.get(currentPlayer).chooseShepherdStreet(j);
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

    private int setUpShift() {
        //creo oggetto random
        Random random = new Random();
        //imposto il primo giocatore a caso tra quelli presenti
        return random.nextInt(this.playersNumber);
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
                if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        client) instanceof SocketClientProxy) {
                    ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                            client)).getSocket().close();
                }

                ServerManager.NICK_2_CLIENT_PROXY_MAP.remove(client);
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
        String playerOnTheStreet;
        int streetsNumber = this.map.getStreets().length;
        for (int i = 0; i < streetsNumber; i++) {
            Street street = this.map.getStreets()[i];
            fence = false;
            playerOnTheStreet = null;
            int shepherdIndex = 0;
            if (street.hasFence()) {
                fence = true;
            } else if (street.hasShepherd()) {
                playerOnTheStreet = getPlayerNickNameByShepherd(
                        street.getShepherd());

                //cerco l'indice del player
                int playerIndex = getPlayerIndexByNickName(playerOnTheStreet);

                //cerco l'index dello shepherd
                shepherdIndex = getShepherdIndex(playerIndex,
                        street.getShepherd());

            }
            controller.refreshStreet(client, i, fence, playerOnTheStreet,
                    shepherdIndex);
        }
    }

    /**
     * Given a nickName returns the index in the list of players
     *
     * @param nickName nickNmae of the player
     *
     * @return index
     */
    protected int getPlayerIndexByNickName(String nickName) {
        for (int i = 0; i < playersNumber; i++) {
            if (players.get(i).getPlayerNickName().equals(
                    nickName)) {
                return i;
            }
        }
        return -1;
    }

    private int getShepherdIndex(int playerIndex, Shepherd shepherd) {
        for (int i = 0; i < players.get(playerIndex).shepherd.length; i++) {
            if (shepherd == players.get(playerIndex).shepherd[i]) {
                return i;
            }
        }
        return -1;
    }

    private void refreshSpecialAnimals(String client) {
        try {
            //refresh wolf position
            controller.refreshSpecialAnimalPosition(client,
                    map.getWolf(), "" + map.getNodeIndex(
                            map.getWolf().getMyRegion()));
            //refresh blacksheep position
            controller.refreshSpecialAnimalPosition(client,
                    map.getBlackSheep(), "" + map.getNodeIndex(
                            map.getBlackSheep().getMyRegion()));

        } catch (NodeNotFoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(),
                    ex);
        }

    }

    /**
     * Refreshes all the initial condition to a player. In order: nicknames of
     * the players and their money, region status, street status, specialAnimals
     * and fence number
     *
     * @param client Client to refresh
     */
    protected void refreshInitialConditions(String client) {
        int[] wallets = getWalletsAmmount();

        controller.refreshGameParameters(client, clientNickNames, wallets,
                shepherd4player);

        refreshRegions(client);

        refreshStreets(client);

        controller.refreshMoney(client);

        refreshSpecialAnimals(client);

        refreshBankCards(client);

        try {
            controller.refreshNumberOfAvailableFence(client,
                    GameConstants.NUM_FENCES.getValue() - GameConstants.NUM_FINAL_FENCES.getValue() - bank.numberOfUsedFence());
        } catch (FinishedFencesException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(),
                    ex);
        }

    }

    private void refreshBankCards(String client) {
        //salvo il numero rimanente di ogni carta
        int[] availableCards = new int[RegionType.values().length - 1];
        String[] regionTypes = new String[RegionType.values().length - 1];

        int i = 0;

        for (RegionType type : RegionType.values()) {
            if (type != RegionType.SHEEPSBURG) {
                regionTypes[i] = type.toString();
                availableCards[i] = bank.getNumberOfAvailableCards(type);
                DebugLogger.println(
                        "Aggiunta in " + i + " carta " + type + "con numero dispo: " + availableCards[i]);
                i++;
            }
        }

        controller.refreshBankCards(client, regionTypes, availableCards);

    }

    private int[] getWalletsAmmount() {
        int[] wallets = new int[clientNickNames.length];

        for (int i = 0; i < clientNickNames.length; i++) {
            wallets[i] = players.get(i).getMainShepherd().getWallet().getAmount();
        }

        return wallets;
    }

    private void executeRounds() throws FinishedFencesException,
            UnexpectedEndOfGameException {
        currentPlayer = this.firstPlayer;
        boolean lastRound = false;
        int playerOffline = 0;

        while (!(lastRound && this.firstPlayer == this.currentPlayer)) {
            //prova a fare un turno
            DebugLogger.println("Avvio esecuzione turno");

            //se il player è Online
            if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
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
                    
                    //the movement of at least a shepherd is set to false
                    players.get(currentPlayer).hasMovedShepherd = false;
                    
                    //the number of actions made is set to 0
                    players.get(currentPlayer).numberOfActionsMade = 0;

                    lastRound = this.executeShift(currentPlayer);
                } catch (PlayerDisconnectedException ex) {
                    //il giocatore si disconnette durante il suo turno
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);

                    controller.brodcastPlayerDisconnected(
                            clientNickNames[currentPlayer]);
                }

                evolveLambs();

                //informa i player dell'evoluzione dei lamb, dello stato delle regioni,
                //del numero di carte disponibili di ogni terreno
                for (String client : clientNickNames) {
                    refreshRegions(client);
                    refreshSpecialAnimals(client);
                    refreshBankCards(client);
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

            }
            nextPlayer();

            //controllo se ho finito il giro
            //se il prossimo a giocare è il primo del giro
            if (firstPlayer == currentPlayer) {
                //avvio il market  
                this.startMarket();

                //muovo il lupo
                DebugLogger.println("muovo lupo");
                this.moveSpecialAnimal(this.map.getWolf());
                DebugLogger.println("lupo mosso");
            }
        }
    }

    private void startMarket() throws UnexpectedEndOfGameException {
        //giro di sell
        DebugLogger.println("inizio giro sell");
        sellCardsRound();

        //creo random player
        int firstBuyer = setUpShift();
        DebugLogger.println("primo giocatore ad acquistare: " + firstBuyer);

        //giro di buy
        buyCardsRound(firstBuyer);
        DebugLogger.println("giro buy finito");

        //tolgo il check forSale da ogni carta rimasta nel market e tolgo le 
        //carte rimaste dal market
        market.clear();
        DebugLogger.println("Market pulito");
    }

    private void sellCardsRound() throws UnexpectedEndOfGameException {
        int playerOffline = 0;
        boolean wantToSell;
        do {
            DebugLogger.println("Inizio sell giocatore: " + currentPlayer);
            if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    clientNickNames[currentPlayer]).isOnline()) {

                controller.brodcastCurrentPlayer(clientNickNames[currentPlayer]);

                try {
                    handleReconnection();

                    playerOffline = 1;

                    do {

                        wantToSell = players.get(currentPlayer).sellCard();

                    } while (wantToSell);
                } catch (PlayerDisconnectedException ex) {

                    //il giocatore si disconnette durante il suo turno di market
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);

                    controller.brodcastPlayerDisconnected(
                            clientNickNames[currentPlayer]);
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
            }

            DebugLogger.println("Cambio giocatore");
            nextPlayer();
        } while (this.firstPlayer != this.currentPlayer);
        DebugLogger.println("fuori dal while del sell");

    }

    private void buyCardsRound(int firstBuyer) throws
            UnexpectedEndOfGameException {
        int playerOffline = 0;
        boolean wantToBuy;
        int currentBuyer = firstBuyer;

        do {
            DebugLogger.println("inizio buy giocatore " + currentBuyer);
            if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    clientNickNames[currentBuyer]).isOnline()) {

                controller.brodcastCurrentPlayer(clientNickNames[currentBuyer]);

                try {
                    handleReconnection();

                    playerOffline = 1;

                    do {
                        wantToBuy = players.get(currentBuyer).buyCard();
                    } while (wantToBuy);

                } catch (PlayerDisconnectedException ex) {

                    //il giocatore si disconnette durante il suo turno di market
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);

                    controller.brodcastPlayerDisconnected(
                            clientNickNames[currentBuyer]);
                }
            } else {
                DebugLogger.println(
                        "Player offline:" + clientNickNames[currentBuyer]);
                //player offline
                playerOffline++;

                //skip player               
                controller.brodcastPlayerDisconnected(
                        clientNickNames[currentBuyer]);

                if (playerOffline == this.playersNumber) {
                    //tutti i player sono offline termino la partita
                    throw new UnexpectedEndOfGameException(
                            "Tutti i player si sono disconnesi, la partita termina");
                }
            }

            //refresh delle carte possedute da tutti
            brodcastCards();
            
            //refresh dei soldi
            controller.refreshWallets();
            
            controller.refreshMoney(clientNickNames[currentBuyer]);
            
            currentBuyer = nextBuyer(currentBuyer);
        } while (firstBuyer != currentBuyer);

    }

    private int nextBuyer(int current) {
        int currentBuyer = current;
        //aggiorno il player che gioca 
        currentBuyer++;
        //conto in modulo playersNumber
        currentBuyer %= this.playersNumber;

        return currentBuyer;
    }

    private boolean roundComplete(int first, int current) {
        if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                clientNickNames[first]).isOnline()) {
            return current == first;
        }
        int temporaryFirstPlayer;

        for (int i = 0; i < this.playersNumber; i++) {
            //aggiorno il firstPlayer temporaneo
            temporaryFirstPlayer = (first + 1) % this.playersNumber;

            if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    clientNickNames[temporaryFirstPlayer]).isOnline()) {
                return current == temporaryFirstPlayer;
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
        if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                clientNickNames[currentPlayer]).needRefresh()) {

            //setto il needRefresh a false
            ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    clientNickNames[currentPlayer]).setRefreshNeeded(false);

            //avvio il player che è tornato in partita
            controller.refreshStartGame(clientNickNames[currentPlayer]);

            //lo aggiorno
            this.refreshInitialConditions(clientNickNames[currentPlayer]);

            //refresh cards
            refreshCards(currentPlayer);

            //gli chiedo di settare tutti i pastori che non aveva settato
            int shepherdToSet = ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    clientNickNames[currentPlayer]).getNumberOfShepherdStillToSet();

            for (int i = 0; i < shepherdToSet; i++) {
                players.get(currentPlayer).chooseShepherdStreet(
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
            this.players.get(player).numberOfActionsMade = i;
            
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
