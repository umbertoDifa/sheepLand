package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.PlayerRemote;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.ShepherdNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Dice;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Market;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.BusyStreetException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.MissingCardException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NoOvineException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.OvineNotFoundExeption;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.NetworkConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerManager;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.PlayerDisconnectedException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe giocatore
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Player extends UnicastRemoteObject implements PlayerRemote {

    /**
     * It's the list of shepherd of a given player
     */
    protected final Shepherd[] shepherd;

    private final GameManager gameManager;
    private final String playerNickName;
    /**
     * It's the code of the last action performed by the player int the current
     * shift It can be NO_ACTION if the player didn't make any action, so this
     * is the case of the first action of each shift
     */
    protected int lastAction;
    /**
     * Number of action made by the plyaer
     */
    protected int numberOfActions;
    /**
     * If the player has moved the shepherd in the current shift
     */
    protected boolean hasMovedShepherd;
    /**
     * It's the last shepherd who was used by the player to make an action. It's
     * null if no shepherd was used, which means the player didn't make any
     * action in the current shift
     */
    protected Shepherd lastShepherd;
    private static final String NO_SAME_SHEPHERD_STRING = "Non è possibile muovere due pastori diversi nello stesso turno";

    /**
     * Lista di azioni che un player può fare, si aggiorna ad ogni azione del
     * turno
     */
    private String possibleAction;

    private final Market market;

    /**
     * Creates a player connected to a given gameManager and one and only one
     * player = client. It sets up the array of shepherd as well as their wallet
     * ammount. It sets up the sharing of wallet and cards between shepherds of
     * the same player
     *
     * @param gameManager The game manager controlling the game
     * @param playerNickName The nickName to whom the player is associated
     * @param market il market in cui il player vendrà le carte
     *
     * @throws RemoteException When the remote rmi call fails
     */
    public Player(GameManager gameManager, String playerNickName, Market market)
            throws
            RemoteException {
        this.playerNickName = playerNickName;
        this.gameManager = gameManager;
        this.market = market;
        this.shepherd = new Shepherd[gameManager.shepherd4player];

        if (gameManager.shepherd4player >= ControlConstants.SHEPHERD_FOR_FEW_PLAYERS.getValue()) {
            this.shepherd[0] = new Shepherd(
                    GameConstants.LOW_PLAYER_WALLET_AMMOUNT.getValue());
        } else {
            this.shepherd[0] = new Shepherd(
                    GameConstants.STANDARD_WALLET_AMMOUNT.getValue());
        }

        //setUp shepherds sharing cards and wallet    
        for (int i = 1; i < gameManager.shepherd4player; i++) {
            this.shepherd[i] = new Shepherd(this.shepherd[0].getWallet(),
                    this.shepherd[0].getMyCards());
        }

    }

    /**
     * Returns the player nickName
     *
     * @return Player nickName
     */
    public String getPlayerNickName() {
        return playerNickName;
    }

    /**
     * It returns the first shepherd of the array, which is the main one since
     * it always exists and it's also the one which creates the wallet and teh
     * cards that the others shepherd share with
     *
     * @return The first shepherd
     */
    public Shepherd getMainShepherd() {
        return shepherd[0];
    }

    /**
     * Invita il player a fare una mossa tra quelle che gli sono permesse. Ne
     * può scegliere al massimo una. Il player deve scegliere e fare un'azione
     * finchè il risultato non è valido
     *
     * @throws PlayerDisconnectedException if the player disconnects in his
     * shift more than a maximum number of chances
     */
    protected void chooseAndMakeAction() throws PlayerDisconnectedException {

        boolean outcomeOk;
        int numberOfDisconnections = 0;

        createActionList();

        do {
            try {
                //se il player si era disconnesso gli rimando il benvenuto
                handleReconnectionInTheSameTurn();

                DebugLogger.println(
                        "invio le azionio possibiil: " + possibleAction);
                //raccogli la scelta
                outcomeOk = gameManager.getController().askChooseAction(
                        playerNickName,
                        possibleAction);
            } catch (PlayerDisconnectedException ex) {
                DebugLogger.println(
                        "giocatore" + this.playerNickName + " disconnesso");
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                //se il player si disconnette
                outcomeOk = false;
                numberOfDisconnections++;

                //controllo il numero di volte che si è disconnesso nello stesso turno
                if (numberOfDisconnections >= NetworkConstants.MAX_NUMBER_OF_DISCONNETIONS.getValue()) {
                    gameManager.getController().brodcastPlayerDisconnected(
                            playerNickName);

                    throw new PlayerDisconnectedException(
                            "Il giocatore si è disconnesso");
                }

                //se ha ancora chances lo metto in pausa
                try {
                    Thread.sleep(
                            NetworkConstants.TIMEOUT_PLAYER_RECONNECTION.getValue());
                } catch (InterruptedException ex1) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex1.getMessage(), ex1);
                }
            }
        } while (!outcomeOk);

    }

    private void handleReconnectionInTheSameTurn() {
        //se il player si era disconnesso gli rimando il benvenuto
        if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                playerNickName).needRefresh()) {

            //setto il needRefresh a false
            ServerManager.NICK_2_CLIENT_PROXY_MAP.get(playerNickName).setRefreshNeeded(
                    false);

            DebugLogger.println(
                    "il giocatore " + playerNickName + "ha bisogno di un start game");

            gameManager.getController().refreshStartGame(
                    playerNickName);
            gameManager.refreshInitialConditions(playerNickName);
        }

    }

    /**
     * Crea una lista di azioni possibili secondo il formato:
     * [numero_azione]-[descrizione] separando le azioni da una virgola
     */
    private void createActionList() {
        //nessuna azione disponibile inizialmente                
        possibleAction = "";
        if (numberOfActions < GameConstants.NUM_ACTIONS.getValue() - 1 || hasMovedShepherd) {
            //non ultima azione o pastore mosso
            for (OvineType type : OvineType.values()) {
                if (canMoveOvine(type) && lastAction != ActionConstants.MOVE_OVINE.getValue()) {
                    possibleAction += "1-Sposta ovino,";
                    break;
                }
            }

            possibleAction += "2-Sposta pastore,";

            //aggiungi acquisto carta se possibile
            if (canBuyCard() && lastAction != ActionConstants.BUY_LAND.getValue()) {
                possibleAction += "3-Compra terreno,";
            }
            if (canMateSheep() && lastAction != ActionConstants.MATE_SHEEP_WITH_SHEEP.getValue()) {
                possibleAction += "4-Accoppia pecore,";
            }
            if (canMateSheepWithRam() && lastAction != ActionConstants.MATE_SHEEP_WITH_RAM.getValue()) {
                possibleAction += "5-Accoppia montone e pecora,";
            }
            if (canKillOvine() && lastAction != ActionConstants.KILL_OVINE.getValue()) {
                possibleAction += "6-Abbatti ovino";
            }

        } else {
            //ultima azione e pastore non mosso
            possibleAction += "2-Sposta pastore,";
        }

    }

    private boolean canMoveOvine(OvineType ovine) {
        //se non c'è un ultimo pastore
        if (lastShepherd == null) {
            for (Region region : getShepherdsRegion()) {
                //se in almeno una c'è almeno un ovino di quel tipo
                if (region.hasOvine(ovine)) {
                    return true;
                }
            }
            //nessun ovino di quel tipo trovato nelle regioni adiacenti al pastore
            return false;
        } else {
            //cerca fra le regioni confinanti dell'ultimo pastore
            for (Region region : lastShepherd.getStreet().getNeighbourRegions()) {
                //se in almeno una c'è almeno un ovino di quel tipo
                if (region.hasOvine(ovine)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean canBuyCard() {
        int price;
        int shepherdMoney = this.shepherd[0].getWallet().getAmount();
        List<Region> regions;

        if (lastShepherd == null) {
            regions = getShepherdsRegion();
        } else {
            regions = lastShepherd.getStreet().getNeighbourRegions();
        }

        //per tutte le regioni confinanti
        for (Region region : regions) {
            try {
                //prendi il prezzo della carta per ogni regione
                price = this.gameManager.getBank().getPriceOfCard(
                        region.getType());
                if (price < shepherdMoney) {
                    return true;

                }
            } catch (MissingCardException ex) {
                Logger.getLogger(DebugLogger.class
                        .getName()).log(Level.SEVERE,
                                ex.getMessage(), ex);
            }

        }
        return false;

    }

    private boolean canMateSheep() {
        int numbOfSheep;
        Shepherd[] shphArray;

        if (lastShepherd == null) {
            shphArray = shepherd;
        } else {
            shphArray = new Shepherd[1];
            shphArray[0] = lastShepherd;
        }

        for (Region region : getShepherdsRegion(shphArray)) {
            numbOfSheep = 0;
            for (Ovine ovine : region.getMyOvines()) {
                if (ovine.getType() == OvineType.SHEEP) {
                    numbOfSheep++;
                }
            }
            if (numbOfSheep >= 2) {
                return true;
            }
        }

        return false;
    }

    private boolean canMateSheepWithRam() {
        int numbOfSheep;
        int numbOfRam;
        Shepherd[] shphArray;

        if (lastShepherd == null) {
            shphArray = shepherd;
        } else {
            shphArray = new Shepherd[1];
            shphArray[0] = lastShepherd;
        }

        for (Region region : getShepherdsRegion(shphArray)) {
            numbOfRam = 0;
            numbOfSheep = 0;
            for (Ovine ovine : region.getMyOvines()) {
                if (ovine.getType() == OvineType.SHEEP) {
                    numbOfSheep++;
                } else if (ovine.getType() == OvineType.RAM) {
                    numbOfRam++;
                }
            }
            if (numbOfRam >= 1 && numbOfSheep >= 1) {
                return true;
            }

        }

        return false;

    }

    private boolean canKillOvine() {
        Shepherd[] shphArray;

        if (lastShepherd == null) {
            shphArray = shepherd;
        } else {
            shphArray = new Shepherd[1];
            shphArray[0] = lastShepherd;
        }

        for (Region region : getShepherdsRegion(shphArray)) {
            if (!region.getMyOvines().isEmpty()) {
                return true;
            }
        }

        return false;

    }

    /**
     * chiede le regioni di partenza e di arrivo dell'ovino del tipo
     * specificato, lo rimuove dalla regione, lo aggiunge nella regione da dove
     * può arrivare passando per la strada occupata dal pastore del giocatore.
     *
     *
     * @param type type of ovine to move
     * @param finishRegion Region where to move the ovine
     * @param beginRegion Regione where the ovine is
     *
     * @return "ovino mosso" se tutto ok, una stringa di errore altrimenti
     */
    public String moveOvine(String beginRegion, String finishRegion, String type) {

        Region startRegion;
        Region endRegion;
        String typeToMove;

        try {
            startRegion = gameManager.getMap().convertStringToRegion(beginRegion);
            endRegion = gameManager.getMap().convertStringToRegion(finishRegion);

        } catch (RegionNotFoundException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);

            return "La regione inserita non esiste";
        }
        try {
            typeToMove = convertAndCheckOvineType(type);

        } catch (OvineNotFoundExeption ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        }

        //per ogni strada occupata dai patori del giocatore
        //finchè non ne trovo una adatta e quindi o ritorno 
        //un fallimento o un successo
        for (Street possibleStreet : this.getShepherdsStreets()) {
            //se le regioni confinano con la strada e sono diverse tra loro
            if (startRegion.isNeighbour(possibleStreet) && endRegion.isNeighbour(
                    possibleStreet) && startRegion != endRegion) {

                if (lastShepherd != null && possibleStreet.getShepherd() != lastShepherd) {
                    return "Non puoi muovere due pastori diversi nello stesso turno";
                }

                //rimuovi ovino del tipo specificato
                Ovine movedOvine;
                DebugLogger.println("Rimuovo ovino");
                try {
                    movedOvine = startRegion.removeOvine(
                            OvineType.valueOf(typeToMove));

                } catch (NoOvineException ex) {
                    Logger.getLogger(DebugLogger.class
                            .getName()).log(
                                    Level.SEVERE,
                                    ex.getMessage(), ex);

                    return "Nessun ovino nella regione di partenza!";
                }
                DebugLogger.println("ovino rimosso");
                //e aggiungi, proprio quello rimosso nella regione d'arrivo
                endRegion.addOvine(movedOvine);
                lastAction = ActionConstants.MOVE_OVINE.getValue();
                lastShepherd = possibleStreet.getShepherd();
                return "Ovino mosso";
            }
        }
        return "Non è possibile spostare l'ovino tra le regioni inidicate";

    }

    /**
     * Cerca di piazzare il pastore passato nella strada, se ci riesce ritorna
     * una stringa di successo altrimenti una stringa che spiega l'errore
     * accaduto
     *
     * @param indexShepherd Index of the Shepherd in the player's array
     * @param stringedStreet Street that the shepherd has to move to
     *
     * @return "Pastore posizionato" if everything goes right, an error string
     * if an exeption is caught.
     */
    public String setShepherd(int indexShepherd, String stringedStreet) {

        Street chosenStreet;
        try {
            chosenStreet = convertAndCheckStreet(stringedStreet);

        } catch (StreetNotFoundException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        } catch (BusyStreetException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        }

        //sposta il pastore 
        shepherd[indexShepherd].moveTo(chosenStreet);

        return "Pastore posizionato correttamente!,0";
    }

    /**
     * Asks the player to set his shepherd whose index is given in the arguments
     *
     * @param shepherdIndex The shepherd to set
     *
     * @throws PlayerDisconnectedException If the player disconnects while
     * putting his shepherd on the map
     */
    protected void chooseShepherdStreet(int shepherdIndex) throws
            PlayerDisconnectedException {
        boolean outcomeOk;
        int numberOfDisconnections;
        outcomeOk = false;

        //do la possibilità al giocatore corrente di disconnettersi resettando
        //il numero  di disconnessioni
        numberOfDisconnections = 0;

        while (!outcomeOk) {
            try {
                //se il player si era disconnesso gli rimando il benvenuto
                handleReconnectionInTheSameTurn();

                DebugLogger.println(
                        "chiedo posizionamento del pastore " + shepherdIndex + " al giocatore " + playerNickName);

                //prova a chiedere la strada per il j-esimo pastore                    
                outcomeOk = gameManager.getController().askSetUpShepherd(
                        playerNickName, shepherdIndex);

                //resetto il numero di shepherd che il player dovrebbe settare dopo la riconnessione
                ServerManager.NICK_2_CLIENT_PROXY_MAP.get(playerNickName).setNumberOfShepherdStillToSet(
                        0);
            } catch (PlayerDisconnectedException ex) {
                DebugLogger.println(
                        "giocatore" + playerNickName + " disconnesso");
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);

                //se il player si disconnette
                outcomeOk = false;
                numberOfDisconnections++;

                //controllo il numero di volte che si è disconnesso nello stesso turno
                if (numberOfDisconnections >= NetworkConstants.MAX_NUMBER_OF_DISCONNETIONS.getValue()) {
                    //salvo quanti pastori deve ancora settare
                    ServerManager.NICK_2_CLIENT_PROXY_MAP.get(playerNickName).setNumberOfShepherdStillToSet(
                            gameManager.shepherd4player - shepherdIndex);

                    throw new PlayerDisconnectedException(
                            "giocatore disconnesso durante set up pastore");
                }

                //se ha ancora chances lo metto in pausa
                try {
                    Thread.sleep(
                            NetworkConstants.TIMEOUT_PLAYER_RECONNECTION.getValue());
                } catch (InterruptedException ex1) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex1.getMessage(), ex1);
                }
            }
        }//while
    }

    /**
     * It's the method called by the client to set up the shepherd which in turn
     * call the setShepherd
     *
     * @param idShepherd id of the shepherd to set
     * @param stringedStreet Street where to set the shepherd
     *
     * @return The result of the action
     *
     * @throws RemoteException When the rmi connection fails
     */
    public String setShepherdRemote(int idShepherd, String stringedStreet)
            throws RemoteException {
        return this.setShepherd(idShepherd, stringedStreet);
    }

    /**
     * Chiede al giocatore quale pastore spostare e in che strada Se la mossa è
     * possibile (se confinanti o non confinanti e puoi pagare) muovo il pastore
     * e metto il cancello
     *
     * @param shepherdNumber
     * @param newStreet
     *
     * @return
     */
    public String moveShepherd(String shepherdNumber, String newStreet) {
        DebugLogger.println(
                "Inizio move shepherd col pastore: " + shepherdNumber);

        //se il pastore selezionato è nell'array dei pastori
        int shepherdIndex;
        try {
            shepherdIndex = convertAndCheckShepherd(shepherdNumber);

        } catch (ShepherdNotFoundException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        }

        Shepherd currentShepherd = shepherd[shepherdIndex];
        Street startStreet = currentShepherd.getStreet();

        if (lastShepherd != null && currentShepherd != lastShepherd) {
            return NO_SAME_SHEPHERD_STRING;
        }

        Street endStreet;
        //controllo strada
        try {
            endStreet = convertAndCheckStreet(newStreet);

        } catch (StreetNotFoundException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        } catch (BusyStreetException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        }

        //se strada free ed esiste ed è vicina o posso pagare
        if (startStreet.isNeighbour(endStreet)) {
            //muovilo
            currentShepherd.moveTo(endStreet);
            try {
                startStreet.setFence(this.gameManager.getBank().getFence());

            } catch (FinishedFencesException ex) {
                Logger.getLogger(DebugLogger.class
                        .getName()).log(
                                Level.SEVERE,
                                ex.getMessage(), ex);

                return "Recinti terminati";
            }
            DebugLogger.println("Pastore posizionato");
            lastShepherd = currentShepherd;
            lastAction = ActionConstants.MOVE_SHEPHERD.getValue();
            hasMovedShepherd = true;
            return "Pastore spostato,0";
        } else if (currentShepherd.ifPossiblePay(
                GameConstants.PRICE_FOR_SHEPHERD_JUMP.getValue())) {
            DebugLogger.println("Pagamento effettuato");
            currentShepherd.moveTo(endStreet);
            try {
                startStreet.setFence(this.gameManager.getBank().getFence());

            } catch (FinishedFencesException ex) {
                Logger.getLogger(DebugLogger.class
                        .getName()).log(
                                Level.SEVERE,
                                ex.getMessage(), ex);

                return "Recinti terminati";
            }
            lastShepherd = currentShepherd;
            lastAction = ActionConstants.MOVE_SHEPHERD.getValue();
            hasMovedShepherd = true;
            return "Pastore spostato," + GameConstants.PRICE_FOR_SHEPHERD_JUMP.getValue();

        }
        return "Non hai soldi per spostarti";

    }

    /**
     * Tries to buy a land. If the player has enough money it buys it and
     * returns a string "Carta acquistata,[type],[price]". Otherwise it returns
     * a string mentioning the error occurred
     *
     * @param landToBuy
     *
     * @return "Carta acquistata,[type],[price]" or an error string
     */
    public String buyLand(String landToBuy) {

        if (landToBuy.equalsIgnoreCase(RegionType.SHEEPSBURG.toString())) {
            return "Non puoi comprare sheepsburg!";
        }

        //creo lista delle possibili regioni da comprare di un pastore
        List<String> possibleRegionsType = new ArrayList<String>();

        //creo la lista dei pastori corrispondenti a certi tipi di regione
        List<Shepherd> possibleShepherd = new ArrayList<Shepherd>();

        List<Street> possibleStreets;

        if (lastShepherd == null) {
            possibleStreets = getShepherdsStreets();
        } else {
            possibleStreets = new ArrayList<Street>();
            possibleStreets.add(lastShepherd.getStreet());
        }

        //salvo una corrispondenza regione->pastore che ci è vicino
        for (Street street : possibleStreets) {
            for (Region region : street.getNeighbourRegions()) {
                possibleRegionsType.add(region.getType().toString());
                possibleShepherd.add(street.getShepherd());
            }
        }

        //se la stringa coincide con uno dei tipi di regione possibili
        try {
            for (String type : possibleRegionsType) {
                if (type.equalsIgnoreCase(landToBuy)) {
                    //richiedi prezzo alla banca                    
                    int cardPrice = this.gameManager.getBank().getPriceOfCard(
                            RegionType.valueOf(type));
                    //paga se puo
                    if (shepherd[0].ifPossiblePay(cardPrice)) {
                        //carta scquistabile
                        //recupero la carta dal banco
                        Card card = this.gameManager.getBank().getCard(
                                RegionType.valueOf(type));

                        //la do al pastore
                        this.shepherd[0].addCard(card);
                        //l'ultimo pastore è quello che è vicino alla regione
                        //comprata, per come è stata costruita la lista in precedenza
                        //tale pastore si trova nella lista dei possibleShepherds
                        //allo stesso indice del tipo di regione comprata
                        lastShepherd = possibleShepherd.get(
                                possibleRegionsType.indexOf(type));
                        lastAction = ActionConstants.BUY_LAND.getValue();
                        return "Carta acquistata," + type + "," + cardPrice;
                    } else {
                        return "Non hai abbastanza soldi per pagare la carta";
                    }
                }
            }
            return "Non è possibile acquistare il territorio richiesto in quanto"
                    + " non confina con il tuo pastore in uso";

        } catch (MissingCardException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);

            return "Non ci sono più carte del territorio richiesto";
        }

    }

    /**
     * Tries to mate a sheep in the given region with an other type of ovine.
     * The region must be next to one of the shepherds.
     *
     * @param shepherdNumber Which shepherd is next to the region where to mate
     * sheep and other ovine
     * @param regionToMate The region with the sheep and the other animal
     * @param otherOvineType The other ovine to mate with the sheep
     *
     * @return "Accoppiamento eseguito",[ovineCreated] if the mate goes
     * allright, an error string if not
     */
    public String mateSheepWith(String shepherdNumber, String regionToMate,
            String otherOvineType) {

        int shepherdIndex;
        String type;
        Region matingRegion;

        try {
            //controllo i dati sul pastore,sul tipo di ovino e sulla regione
            shepherdIndex = convertAndCheckShepherd(shepherdNumber);
            type = convertAndCheckOvineType(otherOvineType);
            matingRegion = gameManager.getMap().convertStringToRegion(
                    regionToMate);

        } catch (ShepherdNotFoundException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        } catch (OvineNotFoundExeption ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        } catch (RegionNotFoundException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        }

        //confermo di usare lo stesso pastore
        if (lastShepherd != null && lastShepherd != shepherd[shepherdIndex]) {
            return NO_SAME_SHEPHERD_STRING;
        }

        //controllo se la regione chiesta confina con la strada del pastore indicato 
        boolean regionOk = false;

        for (Region region : shepherd[shepherdIndex].getStreet().getNeighbourRegions()) {
            if (region == matingRegion) {
                regionOk = true;
                break;
            }
        }
        if (!regionOk) {
            return "La regione non confina con il pastore indicato";
        }

        if (matingRegion.isPossibleToMeetSheepWith(OvineType.valueOf(type))) {
            //lancio il dado
            int diceValue = Dice.roll();
            if (diceValue == shepherd[shepherdIndex].getStreet().getValue()) {

                //aggiungo un ovino alla regione in base al tipo dell'altro ovino
                if (type.equalsIgnoreCase(OvineType.SHEEP.toString())) {

                    matingRegion.addOvine(new Ovine(OvineType.SHEEP));
                    lastShepherd = shepherd[shepherdIndex];
                    lastAction = ActionConstants.MATE_SHEEP_WITH_SHEEP.getValue();
                    return "Accoppiamento eseguito," + OvineType.SHEEP.toString();

                } else if (type.equalsIgnoreCase(OvineType.RAM.toString())) {

                    matingRegion.addOvine(new Ovine(OvineType.LAMB));
                    lastShepherd = shepherd[shepherdIndex];
                    lastAction = ActionConstants.MATE_SHEEP_WITH_RAM.getValue();
                    return "Accoppiamento eseguito," + OvineType.LAMB.toString();
                }
            } else {
                lastShepherd = shepherd[shepherdIndex];
                lastAction = ActionConstants.MATE_SHEEP_WITH_SHEEP.getValue();
                return "Il valore del dado è diverso dalla strada del pastore";
            }
        }
        return "Non è possibile l'accoppiamento nella regione indicata";
    }

    /**
     * The method checks the arguments to verify that they correspond to real
     * parameters. Then it roll the dice to see if the player can kill an ovine,
     * in case of success it calculates the amount of money needed to pay the
     * silence of the other player. If all these conditions are respected than
     * the given type of ovine is killed, and the killer pays the silence
     *
     * @param shepherdNumber The shepherd who kills the ovine
     * @param region The region of the ovine
     * @param typeToKill The typo of ovine to kill
     *
     * @return The result of the action
     */
    public String killOvine(String shepherdNumber, String region,
            String typeToKill) {
        int shepherdIndex;
        int numbOfShepherdToPay;
        List<Shepherd> shepherdToPay = new ArrayList<Shepherd>();
        String type;
        Region regionOfTheMurder;

        try {
            //controllo i dati sul pastore,sul tipo di ovino e sulla regione
            shepherdIndex = convertAndCheckShepherd(shepherdNumber);
            type = convertAndCheckOvineType(typeToKill);
            regionOfTheMurder = gameManager.getMap().convertStringToRegion(
                    region);

        } catch (ShepherdNotFoundException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        } catch (RegionNotFoundException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        } catch (OvineNotFoundExeption ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            return ex.getMessage();
        }

        //confermo di usare lo stesso pastore
        if (lastShepherd != null && lastShepherd != shepherd[shepherdIndex]) {
            return NO_SAME_SHEPHERD_STRING;
        }

        //se il pastore confina con la regione indicata
        boolean canKill = false;
        for (Region reg : shepherd[shepherdIndex].getStreet().getNeighbourRegions()) {
            if (reg == regionOfTheMurder) {
                canKill = true;
            }
        }

        if (!canKill) {
            return "La regione inserita non confina col tuo pastore";
        }
        //se c'è un ovino di quel tipo nella regione chiesta
        if (!regionOfTheMurder.hasOvine(OvineType.valueOf(type))) {
            return "Non esiste nessun " + type + " nella regione " + region;
        }

        //lancio il dado del pastore se è come la sua strada
        int diceValue = Dice.roll();
        if (diceValue != shepherd[shepherdIndex].getStreet().getValue()) {
            lastShepherd = shepherd[shepherdIndex];
            lastAction = ActionConstants.KILL_OVINE.getValue();
            return "Il valore del dado è diverso dalla strada del pastore";
        }

        //lancio il dado degli altri giocatori
        numbOfShepherdToPay = 0;
        for (Street street : shepherd[shepherdIndex].getStreet().getNeighbourStreets()) {
            if (street.hasShepherd()) {
                //controlle che il pastore vicino non sia dello stesso pastore
                boolean wrongShepherd = false;

                for (Shepherd shepher : shepherd) {
                    if (shepher == street.getShepherd()) {
                        wrongShepherd = true;
                    }
                }
                if (!wrongShepherd && Dice.roll() >= GameConstants.MIN_DICE_VALUE_TO_PAY_SILENCE.getValue()) {
                    //aumento il numero di pastori da pagare e lo inserisco nella lista
                    numbOfShepherdToPay++;
                    shepherdToPay.add(street.getShepherd());
                }
            }
        }

        //calcolo se il pastore può pagare
        if (shepherd[0].ifPossiblePay(
                numbOfShepherdToPay * GameConstants.PRICE_OF_SILENCE.getValue())) {
            //paga gli altri player

            DebugLogger.println(
                    "pastore paga " + shepherdToPay.size() + " pastori");

            for (Shepherd shp : shepherdToPay) {
                shp.getWallet().setAmount(
                        shp.getWallet().getAmount() + GameConstants.PRICE_OF_SILENCE.getValue());
            }

            try {
                //ammazza l'ovino
                regionOfTheMurder.removeOvine(OvineType.valueOf(type));
                lastShepherd = shepherd[shepherdIndex];
                lastAction = ActionConstants.KILL_OVINE.getValue();
                return "Ovino ucciso," + numbOfShepherdToPay;

            } catch (NoOvineException ex) {
                //non può succedere perchè ho verificato prima che esista
                Logger.getLogger(DebugLogger.class
                        .getName()).log(Level.SEVERE,
                                ex.getMessage(), ex);
                return ex.getMessage();
            }

        } else {
            lastShepherd = shepherd[shepherdIndex];
            lastAction = ActionConstants.KILL_OVINE.getValue();
            return "Non puoi pagare il silenzio degli altri pastori";
        }

    }

    /**
     * Ritorna le strade occupate dai pastori del giocatore
     *
     * @return
     */
    private List<Street> getShepherdsStreets() {
        //creo lista che accoglierà le strada
        List<Street> streets = new ArrayList<Street>();

        for (int i = 0; i < gameManager.shepherd4player; i++) {
            streets.add(this.shepherd[i].getStreet());
        }
        return streets;
    }

    private List<Region> getShepherdsRegion() {
        return getShepherdsRegion(this.shepherd);
    }

    /**
     * Given an array of shepherd return all the regions neighbour with those
     * shepherds
     *
     * @param shphdArray Array of shepherds
     *
     * @return Regions neighbour with the shepherds streets
     */
    private List<Region> getShepherdsRegion(Shepherd[] shphdArray) {
        List<Region> neighbourRegions = new ArrayList<Region>();

        for (Shepherd shphd : shphdArray) {
            for (Region region : shphd.getStreet().getNeighbourRegions()) {
                neighbourRegions.add(region);
            }
        }

        return neighbourRegions;
    }

    /**
     * Data una strada in stringa ritorna l'oggetto strada corrispondente o un
     * eccezione se la strada è occupata o non esistente
     *
     * @param stringedStreet
     *
     * @return
     *
     * @throws StreetNotFoundException
     * @throws BusyStreetException
     */
    private Street convertAndCheckStreet(String stringedStreet) throws
            StreetNotFoundException, BusyStreetException {
        Street chosenStreet = gameManager.getMap().convertStringToStreet(
                stringedStreet);
        DebugLogger.println("Conversione strada effettuata con successo");
        //se la strada è occuapata
        if (!chosenStreet.isFree()) {
            throw new BusyStreetException("Strada occupata");
            //solleva eccezione
        }
        //altrimenti ritorna la strada
        return chosenStreet;
    }

    /**
     * Checks if the shepherd is one of the player and returns its index in the
     * shepherd array
     *
     * @param shepherdNumber The sphepherd index
     *
     * @return The shepherdIndex in the array of shpherds
     */
    private int convertAndCheckShepherd(String shepherdNumber) throws
            ShepherdNotFoundException {
        try {
            int shepherdIndex = Integer.parseInt(shepherdNumber);
            if (shepherdIndex >= 0 && shepherdIndex < shepherd.length) {
                return shepherdIndex;
            } else {
                throw new ShepherdNotFoundException("Il pastore non esiste");

            }

        } catch (NumberFormatException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            throw new ShepherdNotFoundException(
                    "La stringa non rappresenta un pastore");
        }
    }

    /**
     * Checks if the string is an ovine type and returns the type as a string
     *
     * @param type Ovine type to check
     *
     * @return
     *
     * @throws OvineNotFoundExeption If the string is not an ovine
     */
    private String convertAndCheckOvineType(String type) throws
            OvineNotFoundExeption {
        for (OvineType ovine : OvineType.values()) {
            if (ovine.toString().equalsIgnoreCase(type)) {
                return ovine.toString();
            }
        }
        throw new OvineNotFoundExeption("La stringa non è un tipo di ovino.");
    }

    /**
     * It's the remote interface to call the method moveShepherd
     *
     * @param shepherdIndex shepherd to move
     * @param newStreet end street of the shepherd
     *
     * @return the result of the action
     *
     * @throws RemoteException When the rmi connection fails
     */
    public String moveShepherdRemote(String shepherdIndex, String newStreet)
            throws
            RemoteException {
        return this.moveShepherd(shepherdIndex, newStreet);
    }

    /**
     * Calls the method move ovine.
     *
     * @param startRegion Region of the ovine
     * @param endRegion Region where to move the ovine
     * @param type Type of ovine to move
     *
     * @return The result of the action
     */
    public String moveOvineRemote(String startRegion, String endRegion,
            String type) {
        return this.moveOvine(startRegion, endRegion, type);
    }

    /**
     * Calls the method buyLand. It' tryes to buy a land given the type
     *
     * @param regionType Type of land to buy
     *
     * @return the result of the action
     *
     * @throws RemoteException When the rmi connection fails
     */
    public String buyLandRemote(String regionType) throws RemoteException {
        return buyLand(regionType);
    }

    /**
     * {@inheritDoc }
     *
     * @param shepherdNumber
     * @param regionToMate
     * @param otherOvineType
     *
     * @return
     *
     * @throws RemoteException
     */
    public String mateSheepWithRemote(String shepherdNumber, String regionToMate,
            String otherOvineType) throws
            RemoteException {
        return mateSheepWith(shepherdNumber, regionToMate, otherOvineType);
    }

    /**
     * {@inheritDoc }
     *
     * @param shepherdNumber
     * @param region
     * @param typeToKill
     *
     * @return
     *
     * @throws RemoteException
     */
    public String killOvineRemote(String shepherdNumber, String region,
            String typeToKill) throws RemoteException {
        return killOvine(shepherdNumber, region, typeToKill);
    }

    private String[] getSellableCards() {
        List<String> cardsAvailble = new ArrayList<String>();
        DebugLogger.println("Carte vendibili del player " + playerNickName);

        for (Card card : shepherd[0].getMyCards()) {
            if (!card.isForSale() && !card.isInitial()) {
                cardsAvailble.add(card.getType().toString());
                DebugLogger.println(
                        card.getType().toString() + " di valore " + card.getValue());
            }
        }

        return cardsAvailble.toArray(new String[cardsAvailble.size()]);
    }

    /**
     * Puts a card in the market with a given price to be sold
     *
     * @param card
     * @param price
     */
    public void putCardInMarket(String card, int price) {
        for (Card cardToSell : shepherd[0].getMyCards()) {
            if (cardToSell.getType().toString().equalsIgnoreCase(
                    card) && !cardToSell.isForSale() && !cardToSell.isInitial()) {

                //ho torvato la carta da vendere
                cardToSell.setForSale(true);
                cardToSell.setMarketValue(price);

                market.addCard(cardToSell, gameManager.getPlayerIndexByNickName(
                        playerNickName));
                DebugLogger.println(
                        "messa carta " + cardToSell.getType().toString()
                        + " del giocatore " + playerNickName + " nel market per " + price);
                break;
            }
        }

    }

    /**
     * Sells a card of the player among tha available ones
     *
     * @return
     *
     * @throws PlayerDisconnectedException
     */
    public boolean sellCard() throws PlayerDisconnectedException {
        String[] sellableCards;
        int numberOfDisconnections = 0;
        //cerco le carte che il player può vendere     
        sellableCards = getSellableCards();
        if (sellableCards.length > 0) {
            DebugLogger.println("invio carte vendibili dal player");
            while (true) {
                try {
                    handleReconnectionInTheSameTurn();
                    //se ce ne sono
                    //chiedo quali vendere
                    return gameManager.getController().sellCard(playerNickName,
                            sellableCards);
                } catch (PlayerDisconnectedException ex) {
                    //se il player si disconnette                   
                    numberOfDisconnections++;

                    handleTemporaryDisoconnection(ex, numberOfDisconnections);
                }
            }
        } else {
            DebugLogger.println("non ci sono carte da vendere");
            return false;
        }
    }

    private void handleTemporaryDisoconnection(Exception ex,
            int numberOfDisconnections)
            throws PlayerDisconnectedException {
        DebugLogger.println(
                "giocatore" + playerNickName + " disconnesso");
        Logger.getLogger(DebugLogger.class.getName()).log(
                Level.SEVERE, ex.getMessage(), ex);

        //controllo il numero di volte che si è disconnesso nello stesso turno
        if (numberOfDisconnections >= NetworkConstants.MAX_NUMBER_OF_DISCONNETIONS.getValue()) {
            throw new PlayerDisconnectedException(
                    "giocatore disconnesso durante market");
        }

        //se ha ancora chances lo metto in pausa
        try {
            Thread.sleep(
                    NetworkConstants.TIMEOUT_PLAYER_RECONNECTION.getValue());
        } catch (InterruptedException ex1) {
            Logger.getLogger(DebugLogger.class.getName()).log(
                    Level.SEVERE,
                    ex1.getMessage(), ex1);
        }
    }

    /**
     * Ask to the player which card to buy among the available ones
     *
     * @return
     *
     * @throws PlayerDisconnectedException
     */
    protected boolean buyCard() throws PlayerDisconnectedException {
        List<Card> buyableCards;
        int numberOfDisconnections = 0;

        //cerco le carte che il player può comprare     
        buyableCards = getBuyableCards();

        if (buyableCards.size() > 0) {
            while (true) {
                try {
                    handleReconnectionInTheSameTurn();
                    //se ce ne sono
                    //chiedo quali vendere
                    return gameManager.getController().buyCard(playerNickName,
                            buyableCards);
                } catch (PlayerDisconnectedException ex) {
                    numberOfDisconnections++;

                    handleTemporaryDisoconnection(ex, numberOfDisconnections);
                }
            }
        } else {
            DebugLogger.println("non ci sono carte da comprare");
            return false;
        }
    }

    private List<Card> getBuyableCards() {
        //per ogni tipo di carta consulto il market per saper se c'è quel
        //tipo e quanto costa        
        //confronto il costo con quello che posso permettermi
        //se posso permettermelo lo aggiungo alle carte comprabili
        //tirotno le carte comprabili

        List<Card> cards = new ArrayList<Card>();
        Card tmpCard;

        for (RegionType type : RegionType.values()) {
            if (type != RegionType.SHEEPSBURG) {
                tmpCard = market.getCard(type);
                if (tmpCard != null && tmpCard.getMarketValue() <= shepherd[0].getWallet().getAmount()) {
                    cards.add(tmpCard);
                }
            }
        }
        return cards;

    }

    /**
     * Payes a card from the market, and changes the owner of that card
     *
     * @param card
     */
    public void payCardFromMarket(String card) {

        //devo trovare la carta col costo minore
        Card cardToExchange = market.getCard(RegionType.valueOf(
                card.toUpperCase()));
        DebugLogger.println("carta da comprare trovata");

        //tolgo il forSale alla carta
        cardToExchange.setForSale(false);
        DebugLogger.println("for sale cambiata");

        //trovo il player che l'ha venduta
        int owner = market.getOwnerByCard(cardToExchange);
        DebugLogger.println("owner trovato :" + owner);

        //diminuisco i soldi del player
        shepherd[0].ifPossiblePay(cardToExchange.getMarketValue());

        //aggiungo i soldi al venditore
        gameManager.players.get(owner).getMainShepherd().earnMoney(
                cardToExchange.getMarketValue());

        //do la carta al player
        shepherd[0].addCard(cardToExchange);

        //la tolgo da chi la venduta
        gameManager.players.get(owner).getMainShepherd().removeCard(
                cardToExchange);

        //la tolgo al market        
        if (market.removeCard(cardToExchange, owner)) {
            DebugLogger.println("Carta rimossa dal market");
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param card
     * @param price
     *
     * @throws RemoteException
     */
    public void putCardInMarketRemote(String card, int price) throws
            RemoteException {
        this.putCardInMarket(card, price);
    }

    /**
     * {@inheritDoc }
     *
     * @param card
     *
     * @throws RemoteException
     */
    public void payCardFromMarketRemote(String card) throws RemoteException {
        this.payCardFromMarket(card);
    }

}
