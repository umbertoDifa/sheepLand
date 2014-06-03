package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.ShepherdNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Dice;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
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
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe giocatore
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Player extends UnicastRemoteObject implements PlayerRemote {

    protected final Shepherd[] shepherd;

    private final GameManager gameManager;
    private final String playerNickName;

    /**
     * Lista di azioni che un player può fare, si aggiorna ad ogni azione del
     * turno
     */
    private String possibleAction;

    public Player(GameManager gameManager, String playerNickName) throws
            RemoteException {
        this.playerNickName = playerNickName;
        this.gameManager = gameManager;

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

    public String getPlayerNickName() {
        return playerNickName;
    }

    /**
     * Invita il player a fare una mossa tra quelle che gli sono permesse. Ne
     * può scegliere al massimo una. Il player deve scegliere e fare un'azione
     * finchè il risultato non è valido
     *
     * @throws java.rmi.RemoteException
     */
    public void chooseAndMakeAction() throws RemoteException {

        boolean outcomeOk;
        createActionList();
        do {
            //raccogli la scelta
            outcomeOk = gameManager.controller.askChooseAction(playerNickName,
                    possibleAction);
        } while (!outcomeOk);
    }

    /**
     * Crea una lista di azioni possibili secondo il formato:
     * [numero_azione]-[descrizione] separando le azioni da una virgola
     */
    private void createActionList() {
        //nessuna azione disponibile inizialmente                
        possibleAction = "";

        for (OvineType type : OvineType.values()) {
            if (canMoveOvine(type)) {
                possibleAction += "1-Sposta ovino,";
                break;
            }
        }

        possibleAction += "2-Sposta pastore,";

        //aggiungi acquisto carta se possibile
        if (canBuyCard()) {
            possibleAction += "3-Compra terreno,";
        }
        if (canMateSheep()) {
            possibleAction += "4-Accoppia pecore,";
        }
        if (canMateSheepWithRam()) {
            possibleAction += "5-Accoppia montone e pecora,";
        }
        if (canKillOvine()) {
            possibleAction += "6-Abbatti ovino";
        }

    }

    private boolean canMoveOvine(OvineType ovine) {

        for (Region region : getShepherdsRegion()) {
            //se in almeno una c'è almeno un ovino di quel tipo
            if (region.hasOvine(ovine)) {
                return true;
            }
        }

        //nessun ovino di quel tipo trovato nelle regioni adiacenti al pastore
        return false;
    }

    private boolean canBuyCard() {
        int price;
        int shepherdMoney = this.shepherd[0].getWallet().getAmount();

        //per tutte le regioni confinanti
        for (Region region : getShepherdsRegion()) {
            try {
                //prendi il prezzo della carta per ogni regione
                price = this.gameManager.bank.getPriceOfCard(region.getType());
                if (price < shepherdMoney) {
                    return true;
                }
            } catch (MissingCardException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
            }

        }
        return false;
    }

    private boolean canMateSheep() {
        int numbOfSheep;

        for (Shepherd shphd : shepherd) {
            for (Region region : shphd.getStreet().getNeighbourRegions()) {
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
        }
        return false;
    }

    private boolean canMateSheepWithRam() {
        int numbOfSheep;
        int numbOfRam;

        for (Shepherd shphd : shepherd) {
            for (Region region : shphd.getStreet().getNeighbourRegions()) {
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
        }

        return false;
    }

    private boolean canKillOvine() {
        for (Shepherd shphd : shepherd) {
            for (Region region : shphd.getStreet().getNeighbourRegions()) {
                if (!region.getMyOvines().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * chiede le regioni di partenza e di arrivo dell'ovino del tipo
     * specificato, lo rimuove dalla regione, lo aggiunge nella regione da dove
     * può arrivare passando per la strada occupata dal pastore del giocatore.
     * Se mossa non valida chiede e annullare o ripetere azione
     *
     * @param type
     * @param finishRegion
     * @param beginRegion
     *
     * @return
     */
    public String moveOvine(String beginRegion, String finishRegion, String type) {

        Region startRegion;
        Region endRegion;
        String typeToMove;

        try {
            startRegion = gameManager.map.convertStringToRegion(beginRegion);
            endRegion = gameManager.map.convertStringToRegion(finishRegion);
        } catch (RegionNotFoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return "La regione inserita non esiste";
        }
        try {
            typeToMove = convertAndCheckOvineType(type);
        } catch (OvineNotFoundExeption ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
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
                //rimuovi ovino del tipo specificato
                Ovine movedOvine;
                DebugLogger.println("Rimuovo ovino");
                try {
                    movedOvine = startRegion.removeOvine(
                            OvineType.valueOf(typeToMove));
                } catch (NoOvineException ex) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);
                    return "Nessun ovino nella regione di partenza!";
                }
                DebugLogger.println("ovino rimosso");
                //e aggiungi, proprio quello rimosso nella regione d'arrivo
                endRegion.addOvine(movedOvine);
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
     * @param indexShepherd  Index of the Shepherd in the player's array
     * @param stringedStreet Street that the shepherd has to move to
     *
     * @return "Pastore posizionato" if everything goes right, an error string
     *         if an exeption is caught.
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

        return "Pastore posizionato correttamente!";
    }

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
    //aggiustare. convertire il parametro sringato della strada
    public String moveShepherd(String shepherdNumber, String newStreet) {
        DebugLogger.println(
                "Inizio move shepherd col pastore: " + shepherdNumber);

        //se il pastore selezionato è nell'array dei pastori
        int shepherdIndex;
        try {
            shepherdIndex = convertAndCheckShepherd(shepherdNumber);
        } catch (ShepherdNotFoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return ex.getMessage();
        }

        Shepherd currentShepherd = shepherd[shepherdIndex];
        Street startStreet = currentShepherd.getStreet();

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
                startStreet.setFence(this.gameManager.bank.getFence());
            } catch (FinishedFencesException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE,
                        ex.getMessage(), ex);
                return "Recinti terminati";
            }
            DebugLogger.println("Pastore posizionato");

            return "Pastore spostato,0";
        } else if (currentShepherd.ifPossiblePay(
                GameConstants.PRICE_FOR_SHEPHERD_JUMP.getValue())) {
            DebugLogger.println("Pagamento effettuato");
            currentShepherd.moveTo(endStreet);
            try {
                startStreet.setFence(this.gameManager.bank.getFence());
            } catch (FinishedFencesException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(
                        Level.SEVERE,
                        ex.getMessage(), ex);
                return "Recinti terminati";
            }
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

        //creo lista delle possibili regioni da comprare di un pastore
        List<String> possibleRegionsType = new ArrayList<String>();

        //per ogni regione confinante con i pastori del giocatore
        for (Region region : getShepherdsRegion()) {
            //aggiungila ai tipi di regione possibili
            possibleRegionsType.add(region.getType().toString());
        }

        //se la stringa coincide con uno dei tipi di regione possibili
        try {
            for (String type : possibleRegionsType) {
                if (type.equalsIgnoreCase(landToBuy)) {
                    //richiedi prezzo alla banca                    
                    int cardPrice = this.gameManager.bank.getPriceOfCard(
                            RegionType.valueOf(type));
                    //paga se puo
                    if (shepherd[0].ifPossiblePay(cardPrice)) {
                        //carta scquistabile
                        //recupero la carta dal banco
                        Card card = this.gameManager.bank.getCard(
                                RegionType.valueOf(type));

                        //la do al pastore
                        this.shepherd[0].addCard(card);
                        return "Carta acquistata," + type + "," + cardPrice;
                    } else {
                        return "Non hai abbastanza soldi per pagare la carta";
                    }
                }
            }
            return "Non è possibile acquistare il territorio richiesto in quanto non confina con il tuo pastore";

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
     *                       sheep and other ovine
     * @param regionToMate   The region with the sheep and the other animal
     * @param otherOvineType The other ovine to mate with the sheep
     *
     * @return "Accoppiamento eseguito",[ovineCreated] if the mate goes
     *         allright, an error string if not
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
            matingRegion = gameManager.map.convertStringToRegion(regionToMate);
        } catch (ShepherdNotFoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
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

        boolean canMate = false;
        //se il tipo per l'accoppiamento non è una pecora
        if (OvineType.valueOf(type) != OvineType.SHEEP) {

            //controllo che la regione contienga una pecora e l'altro tipo di ovino
            if (matingRegion.hasOvine(OvineType.SHEEP) && matingRegion.hasOvine(
                    OvineType.valueOf(type))) {
                canMate = true;
            }
        } else {
            //conto le pecore
            int numbOfSheep = 0;

            for (Shepherd shphd : shepherd) {
                for (Region region : shphd.getStreet().getNeighbourRegions()) {
                    for (Ovine ovine : region.getMyOvines()) {
                        if (ovine.getType() == OvineType.SHEEP) {
                            numbOfSheep++;
                        }
                    }
                    if (numbOfSheep >= 2) {
                        canMate = true;
                    }
                }
            }

        }

        if (canMate) {
            //lancio il dado
            int diceValue = Dice.roll();
            if (diceValue == shepherd[shepherdIndex].getStreet().getValue()) {

                //aggiungo un ovino alla regione in base al tipo dell'altro ovino
                if (type.equalsIgnoreCase(OvineType.SHEEP.toString())) {

                    matingRegion.addOvine(new Ovine(OvineType.SHEEP));
                    return "Accoppiamento eseguito," + OvineType.SHEEP.toString();

                } else if (type.equalsIgnoreCase(OvineType.RAM.toString())) {

                    matingRegion.addOvine(new Ovine(OvineType.LAMB));
                    return "Accoppiamento eseguito," + OvineType.LAMB.toString();
                }
            } else {

                return "Il valore del dado è diverso dalla strada del pastore";
            }
        }
        return "Non è possibile l'accoppiamento nella regione indicata";
    }

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
            regionOfTheMurder = gameManager.map.convertStringToRegion(region);
        } catch (ShepherdNotFoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
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
        
        DebugLogger.println("controlli fatti");
        
        //se il pastore confina con la regione indicata
        boolean canKill = false;
        for (Region reg : shepherd[shepherdIndex].getStreet().getNeighbourRegions()) {
            if (reg == regionOfTheMurder) {
                canKill = true;
            }
        }
        
        DebugLogger.println("controllo regione confinante");
        
        if (!canKill) {
            return "La regione inserita non confina col tuo pastore";
        }
        //se c'è un ovino di quel tipo nella regione chiesta
        if (!regionOfTheMurder.hasOvine(OvineType.valueOf(type))) {
            return "Non esiste nessun " + type + " nella regione " + region;
        }
        
        DebugLogger.println("controllo ovino esistente");
        
        //lancio il dado del pastore se è come la sua strada
        int diceValue = Dice.roll();
        if (diceValue != shepherd[shepherdIndex].getStreet().getValue()) {
            return "Il valore del dado è diverso dalla strada del pastore";
        }
        
        DebugLogger.println("lancio dado pastore effettuato");
        
        //lancio il dado degli altri giocatori
        numbOfShepherdToPay = 0;        
        for (Street street : shepherd[shepherdIndex].getStreet().getNeighbourStreets()) {
            if (street.hasShepherd() && Dice.roll() >= GameConstants.MIN_DICE_VALUE_TO_PAY_SILENCE.getValue()) {
                //aumento il numero di pastori da pagare e lo inserisco nella lista
                numbOfShepherdToPay++;
                shepherdToPay.add(street.getShepherd());
            }
        }
        
        DebugLogger.println("lancio dado degli altri giocatori");

        //calcolo se il pastore può pagare
        if (shepherd[0].ifPossiblePay(
                numbOfShepherdToPay * GameConstants.PRICE_OF_SILENCE.getValue())) {
            //paga gli altri player
            
            DebugLogger.println("pastore ha pagato il silenzio");
            DebugLogger.println("pastore paga "+shepherdToPay.size()+" pastori");
            
            for(Shepherd shp : shepherdToPay){
                shp.getWallet().setAmount(shp.getWallet().getAmount() + GameConstants.PRICE_OF_SILENCE.getValue());
            }
            
            try {
                
                DebugLogger.println("uccido l'ovino");
                
                //ammazza l'ovino
                regionOfTheMurder.removeOvine(OvineType.valueOf(type));
                return "Ovino ucciso";
            } catch (NoOvineException ex) {
                //non può succedere perchè ho verificato prima che esista
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                return ex.getMessage();
            }

        } else {
            return "Non puoi pagare il silenzio degli altri pastori";
        }

    }

    public void sellCards() {
        //TODO
    }

    public void buyCards() {
        //TODO
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
        List<Region> regions = new ArrayList<Region>();

        for (Street street : getShepherdsStreets()) {
            regions.addAll(street.getNeighbourRegions());
        }
        return regions;
    }

    /**
     *
     * @param street
     *
     * @return vero se il giocatore ha un pastore nella strada passata
     */
    private boolean hasShepherdIn(Street street) {
        //per ogni suo pastore
        for (Shepherd possibleShepherd : this.shepherd) {
            if (possibleShepherd.getStreet().equals(street)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param sheepherd
     *
     * @return ritorna vero se il pastore è del giocatore
     */
    private boolean ownsShepherd(Shepherd sheepherd) {
        //per ogni patore del giocatore
        for (Shepherd myShepherd : this.shepherd) {
            //se corrisponde al pastore passato
            if (myShepherd == sheepherd) {
                return true;
            }
        }
        return false;
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
    public Street convertAndCheckStreet(String stringedStreet) throws
            StreetNotFoundException, BusyStreetException {
        Street chosenStreet = gameManager.map.convertStringToStreet(
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

    public String moveShepherdRemote(String shepherdIndex, String newStreet)
            throws
            RemoteException {
        return this.moveShepherd(shepherdIndex, newStreet);
    }

    public String moveOvineRemote(String startRegion, String endRegion,
                                  String type) {
        return this.moveOvine(startRegion, endRegion, type);
    }

    public String buyLandRemote(String regionType) throws RemoteException {
        return buyLand(regionType);
    }

}
