package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.MissingCardException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.GameConstants;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.BusyStreetException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NoOvineException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe giocatore
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Player implements PlayerRemote {

    protected final Shepherd[] shepherd;

    private final GameManager gameManager;
    private final String playerNickName;

    /**
     * Lista di azioni che un player può fare, si aggiorna ad ogni azione del
     * turno
     */
    private String possibleAction;

    public Player(GameManager gameManager, String playerNickName) {
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
     * può scegliere al massimo una.
     *
     */
    public void chooseAndMakeAction() {

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
        int i = 1;
        for (OvineType type : OvineType.values()) {
            if (canMoveOvine(type)) {
                possibleAction += i + "-Sposta " + type + ",";
            }
            i++;
        }

        possibleAction += "4-Sposta pastore,";

        //aggiungi acquisto carta se possibile
        if (canBuyCard()) {
            possibleAction += "5-Compra terreno,";
        }

        possibleAction += "6-Accoppia pecore,";
        possibleAction += "7-Accoppia montone e pecora,";
        possibleAction += "8-Abbatti pecora";

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

        //trova le regioni confinanti ai pastori
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

    /**
     * chiede le regioni di partenza e di arrivo dell'ovino del tipo
     * specificato, lo rimuove dalla regione, lo aggiunge nella regione da dove
     * può arrivare passando per la strada occupata dal pastore del giocatore.
     * Se mossa non valida chiede e annullare o ripetere azione
     *
     * @param type
     * @param oldRegion
     * @param newRegion
     *
     * @return
     */
    public String moveOvine(String type, String oldRegion, String newRegion) {

        Region startRegion;
        Region endRegion;
        try {
            startRegion = gameManager.map.convertStringToRegion(newRegion);
            endRegion = gameManager.map.convertStringToRegion(oldRegion);
        } catch (RegionNotFoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            return "La regione inserita non esiste";
        }

        //per ogni strada occupata dai patori del giocatore
        //finchè non ne trovo una adatta e quindi o ritorno 
        //un fallimento o un successo
        for (Street possibleStreet : this.getShepherdsStreets()) {
            //se le regioni confinano con la strada e sono diverse tra loro
            if (startRegion.isNeighbour(possibleStreet) && endRegion.isNeighbour(
                    possibleStreet) && startRegion != endRegion) {
                //rimuovi ovino del tipo specificato
                DebugLogger.println("Rimuovo ovino");
                try {
                    startRegion.removeOvine(OvineType.valueOf(type));
                } catch (NoOvineException ex) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);
                    return "Nessun ovino nella regione di partenza!";
                }
                //e aggiungilo nella regione d'arrivo
                endRegion.addOvine(new Ovine(OvineType.valueOf(type)));

                //refreshio
                gameManager.controller.refreshMoveOvine(playerNickName,
                        oldRegion, newRegion, type);

                return "Ovino mosso!";
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
            chosenStreet = checkStreet(stringedStreet);

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

        //invia conferma riepilogativa agli utenti
        gameManager.controller.refreshMoveShepherd(playerNickName,
                stringedStreet);
        return "Patore posizionato corretamente!";
    }

    /**
     * Chiede al giocatore quale pastore spostare e in che strada Se la mossa è
     * possibile (se confinanti o non confinanti e puoi pagare) muovo il pastore
     * e metto il cancello Altrimenti richiedo o annullo azione
     *
     * @param shepherdIndex
     * @param newStreet
     *
     * @return
     */
    //aggiustare. convertire il parametro sringato della strada
    public String moveShepherd(int shepherdIndex, String newStreet) {

        Shepherd currentShepherd = shepherd[shepherdIndex];
        Street startStreet = currentShepherd.getStreet();

        Street endStreet;
        //controllo strada
        try {
            endStreet = checkStreet(newStreet);

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

        //se strada free ed esiste e non è quella di partenza ed è vicina o posso pagare
        if (startStreet != endStreet) {
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
                //invia conferma riepilogativa agli utenti
                gameManager.controller.refreshMoveShepherd(playerNickName,
                        newStreet);
                return "pastore posizionato";
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
                //invia conferma riepilogativa agli utenti
                gameManager.controller.refreshMoveShepherd(playerNickName,
                        newStreet);
                return "Passaggio pagato e pastore posizionato";

            }
            return "Non hai i soldi per spostarti";
        }
        return "Non puoi spostarti su una strada su cui sei già";
    }

    public void buyLand() throws ActionCancelledException {

//        //creo lista delle possibili regioni da comprare di un pastore
//        List<RegionType> possibleRegionsType = new ArrayList<RegionType>();
//
//        String stringedTypeOfCard;
//        RegionType chosenTypeOfCard;
//        int cardPrice;
//
//        //per ogni regione confinante con i pastori del giocatore
//        for (Region region : getShepherdsRegion()) {
//            //aggiungila ai tipi di regione possibili
//            possibleRegionsType.add(region.getType());
//        }
//
//        try {
//            while (true) {
//                //chiedi il tipo di carta desiderato            
//                stringedTypeOfCard = this.gameManager.server.talkTo(
//                        playerNickName,
//                        "Quale tipo di carta vuoi comprare?\n" + possibleRegionsType);
//
//                //se la stringa coincide con uno dei tipi di regione possibili
//                boolean typeFound = false;
//                for (RegionType type : possibleRegionsType) {
//                    if (type.name().equalsIgnoreCase(stringedTypeOfCard)) {
//                        typeFound = true;
//                        break;
//                    }
//                }
//                //TODO: boh sta roba
//                if (typeFound) {
//                    break;
//                }
//                gameManager.server.sendTo(playerNickName,
//                        "Non puoi comprare un terreno che non confina con il tuo pastore");
//            }
//            //convertilo in RegionType
//            chosenTypeOfCard = RegionType.valueOf(stringedTypeOfCard);
//
//            //richiedi prezzo alla banca                    
//            cardPrice = this.gameManager.bank.getPriceOfCard(
//                    chosenTypeOfCard);
//
//            //se il pastore ha abbastanza soldi paga
//            if (shepherd[0].ifPossiblePay(cardPrice)) {
//                //carta scquistabile
//                gameManager.server.sendTo(playerNickName, "Carta acquistabile");
//
//                //recupero la carta dal banco
//                Card card = this.gameManager.bank.getCard(
//                        chosenTypeOfCard);
//
//                //la do al pastore
//                this.shepherd[0].addCard(card);
//
//                //lo informo
//                //TODO
//                return;
//            } else {
//                //se non ha abbastanza soldi
//                gameManager.server.sendTo(playerNickName,
//                        "Non puoi comprare il territorio " + stringedTypeOfCard + "non hai abbastanza soldi");
//            }
//            //se il tipo non è tra quelli accessibili
//
//        } catch (MissingCardException e) {
//            gameManager.server.sendTo(playerNickName,
//                    "Il territorio richiesto non è disponibile");
//            Logger
//                    .getLogger(DebugLogger.class
//                            .getName()).log(Level.SEVERE, e.getMessage(), e);
//        }
    }

    public void mateSheepWith(OvineType otherOvineType) throws
            ActionCancelledException {
//        //TODO: ricontrollare e scomporre
//        List<Region> nearRegions = null;
//        Region chosenRegion;
//        int randomStreetValue;
//        String errorMessage = "";
//
//        //per ogni pastore del giocatore
//        for (Shepherd shepherdPlayer : this.shepherd) {
//            //per ogni regione confinante alla strada di quel pastore
//            for (Region region : shepherdPlayer.getStreet().getNeighbourRegions()) {
//                //se non contenuta nelle regioni vicine aggiungila
//                if ((nearRegions != null) && !nearRegions.contains(region)) {
//                    nearRegions.add(region);
//                }
//            }
//        }
//        try {
//            //chiedi conferma per lanciare dado e lancialo
//            randomStreetValue = this.gameManager.askAndThrowDice(playerNickName);
//            //chiedi regione (può lanciare RegionNotFoundException)
//            chosenRegion = this.gameManager.askAboutRegion(playerNickName,
//                    "In quale regione?");
//            //se regione è fra le regioni vicine
//            if ((nearRegions != null) && nearRegions.contains(chosenRegion)) {
//                //controlla che nella regione sia possibile accoppiare Sheep con otherOvine
//                if (chosenRegion.isPossibleToMeetSheepWith(otherOvineType)) {
//                    //per ogni strada confinante alla regione scelta
//                    for (Street street : chosenRegion.getNeighbourStreets()) {
//                        //se ha valore uguale a quello del dado e sopra c'è un suo pastore
//                        if (street.getValue() == randomStreetValue && this.hasShepherdIn(
//                                street)) {
//                            //aggiungi ovino e esci dal ciclo
//                            if (otherOvineType == otherOvineType.SHEEP) {
//                                chosenRegion.addOvine(new Ovine(OvineType.SHEEP));
//                            } else if (otherOvineType == otherOvineType.RAM) {
//                                chosenRegion.addOvine(new Ovine(OvineType.LAMB));
//                            }
//                            return;
//                        } else {
//                            errorMessage = "Accoppiamento non permesso";
//                            break;
//                        }
//                    }
//                    if (errorMessage.compareTo("") == 0) {
//                        errorMessage = "Nessuna strada con quel valore e col tuo pastore";
//                    }
//                } else {
//                    errorMessage = "Nessun possibile accoppiamento per questa regione.";
//                }
//            } else {
//                errorMessage = "Regione lontana dai tuoi pastori.";
//            }
//        } catch (RegionNotFoundException ex) {
//            errorMessage = ex.getMessage();
//            Logger
//                    .getLogger(Player.class
//                            .getName()).log(
//                            Level.SEVERE, ex.getMessage(), ex);
//        } finally {
//            this.gameManager.askCancelOrRetry(playerNickName, errorMessage);
//        }
    }

    public void killOvine() {
//        Region chosenRegion;
//        int randomValue;
//        List<Shepherd> neighbourShepherds = new ArrayList<Shepherd>();
//        int sumToPay = 0;
//        String errorMessage = "";
//        OvineType chosenOvineType = null;
//
//        //chiedi se lanciare e lancia il dado
//        randomValue = this.gameManager.askAndThrowDice(playerNickName);
//
//        //per ogni strada del giocatore
//        for (Street street : this.getShepherdsStreets()) {
//            //se ha valore uguale al risultato del dado
//            if ((street.getValue() == randomValue)) {
//                try {
//                    //scegliere regione da attaccare
//                    chosenRegion = this.gameManager.askAboutRegion(
//                            playerNickName,
//                            "scegliere la regione da attaccare");
//
//                    //se ci sono animali
//                    if (!chosenRegion.getMyOvines().isEmpty()) {
//                        //per ogni strada vicina alla strada del pastore
//                        for (Street nearStreet : street.getNeighbourStreets()) {
//                            //se ha un pastore e non è dell'attaccante
//                            if ((nearStreet.getShepherd() != null) && !(this.ownsShepherd(
//                                    nearStreet.getShepherd()))) {
//                                //aggiungilo ai pastori vicini
//                                neighbourShepherds.add(nearStreet.getShepherd());
//                            }
//                        }
//                        //per ogni pastore vicino
//                        for (Shepherd neighbourShepherd : neighbourShepherds) {
//                            //chiedi se lanciare al player corrispondente e lancia il dado
//                            randomValue = this.gameManager.askAndThrowDice(
//                                    this.gameManager.getPlayerByShepherd(
//                                            neighbourShepherd).playerNickName);
//                            //se ha fatto più di 5, 5 compreso
//                            if (randomValue >= 5) {
//                                //aggiorna valore da pagare
//                                sumToPay += 2;
//                            }
//                        }
//                        //se può pagare il silenzio
//                        if (this.shepherd[0].ifPossiblePay(sumToPay)) {
//                            while (true) {
//                                //chiedi tipo d ovino
//                                this.gameManager.server.talkTo(
//                                        playerNickName, "che tipo di ovino?");
//                                //se presente nella regione scelta
//                                try {
//                                    //rimuovilo
//                                    chosenRegion.removeOvine(chosenOvineType);
//                                } catch (NoOvineException ex) {
//                                    //riprovare o annullare azione?
//                                    this.gameManager.askCancelOrRetry(
//                                            playerNickName,
//                                            chosenOvineType + "non presente");
//                                    Logger
//                                            .getLogger(Player.class
//                                                    .getName()).log(
//                                                    Level.SEVERE,
//                                                    ex.getMessage(), ex);
//                                }
//                            }
//                        } else {
//                            errorMessage = "Non puoi pagare il silezio dei tuoi vicini.";
//                        }
//                    } else {
//                        errorMessage = "Non ci sono animali da abbattere in questa regione.";
//                    }
//                } catch (RegionNotFoundException e) {
//                    errorMessage = e.getMessage();
//                    Logger
//                            .getLogger(Player.class
//                                    .getName()).log(
//                                    Level.SEVERE, e.getMessage(), e);
//                } catch (ActionCancelledException e) {
//                    errorMessage = e.getMessage();
//                    Logger
//                            .getLogger(Player.class
//                                    .getName()).log(
//                                    Level.SEVERE, e.getMessage(), e);
//                }
//            }
//        }
//        errorMessage = "Non hai strade di quel valore";
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
    public Street checkStreet(String stringedStreet) throws
            StreetNotFoundException, BusyStreetException {
        Street chosenStreet = gameManager.map.convertStringToStreet(
                stringedStreet);
        DebugLogger.println("Conversione strada effettuata");
        //se la strada è occuapata
        if (!chosenStreet.isFree()) {
            throw new BusyStreetException("Strada occupata");
            //solleva eccezione
        }
        //altrimenti ritorna la strada
        return chosenStreet;
    }

}
