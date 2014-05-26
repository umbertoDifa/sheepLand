package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.MissingCardException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Node;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.BusyStreetException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NoOvineException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NodeNotFoundException;
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
public class Player {

    protected final Shepherd[] shepherd;
    private final GameManager gameManager;
    private Region oldRegion;
    private Region newRegion;

    /**
     * Lista di azioni che un player può fare, si aggiorna ad ogni azione del
     * turno
     */
    private List<String> possibleAction;

    /**
     * Lista degli interi che designano le azioni fattibili in un turno,
     * corrispondenti alle possibleAction
     */
    private List<Integer> allowedActions;

    public Player(GameManager gameManager) {

        this.shepherd = new Shepherd[gameManager.shepherd4player];
        //creo i pastori necessari
        for (int i = 0; i < gameManager.shepherd4player; i++) {
            this.shepherd[i] = new Shepherd();
        }
        this.gameManager = gameManager;
        //condivido le risorse del primo pastore con tutti gli altri
        this.setUpSheperdSharing(this.shepherd[0]);

    }

    private void setUpSheperdSharing(Shepherd mainShepherd) {

        //per ogni pastore tranne il (for inizia da 1)
        for (int i = 1; i < gameManager.shepherd4player; i++) {

            //condividi il portafoglio
            this.shepherd[i].setWallet(mainShepherd.getWallet());

            //condividi le carte
            this.shepherd[i].setMyCards(mainShepherd.getMyCards());
        }
    }

    /**
     * Invita il player a fare una mossa tra quelle che gli sono permesse. Ne
     * può scegliere al massimo una.
     *
     * @throws ActionNotFoundException  Se l'azione chiesta non esiste
     * @throws ActionCancelledException Se il player ha deciso di non fare più
     *                                  l'azione
     * @throws FinishedFencesException  Se non ci sono più recinti da inserire
     *                                  quando si muovono i pastori
     */
    public void chooseAndMakeAction() throws ActionNotFoundException,
                                             ActionCancelledException,
                                             FinishedFencesException {

        try {
            createActionList();

            //raccogli la scelta
            String stringedChoice = (this.gameManager.server.talkTo(
                    this.hashCode(),
                    "Scegli l'azione da fare tra:" + possibleAction));

            isChoiceOk(stringedChoice);

            int actionChoice = Integer.parseInt(stringedChoice);

            this.gameManager.server.sendTo(this.hashCode(), "ok!");

            DebugLogger.println("Scelta: " + actionChoice);
            switch (actionChoice) {
                case 1:
                    this.moveOvine(OvineType.SHEEP);
                    gameManager.server.broadcastExcept(
                            "Il giocatore ha spostato una pecora da " + gameManager.map.getNodeIndex(
                                    oldRegion) + " a " + gameManager.map.getNodeIndex(
                                    newRegion), this.hashCode());
                    break;
                case 2:
                    this.moveOvine(OvineType.RAM);
                    gameManager.server.broadcastExcept(
                            "Il giocatore ha spostato un montone da " + gameManager.map.getNodeIndex(
                                    oldRegion) + " a " + gameManager.map.getNodeIndex(
                                    newRegion), this.hashCode());
                    break;
                case 3:
                    this.moveOvine(OvineType.LAMB);
                    gameManager.server.broadcastExcept(
                            "Il giocatore ha spostato un agnello da " + gameManager.map.getNodeIndex(
                                    oldRegion) + " a " + gameManager.map.getNodeIndex(
                                    newRegion), this.hashCode());
                    break;
                case 4:
                    this.moveShepherd();
                    break;
                case 5:
                    this.buyLand();
                    break;
                case 6:
                    this.mateSheepWith(OvineType.SHEEP);
                    break;
                case 7:
                    this.mateSheepWith(OvineType.RAM);
                    break;
                case 8:
                    this.killOvine();
                    break;
            }
        } catch (NodeNotFoundException ex) {
            //Non potrà mai succedere perchè se la mossa è stata compiuta le regioni
            //esistono
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE, null,
                    ex);
        }

    }

    /**
     * Checks that the received string matches the expected pattern.
     *
     * @param stringedChoice String to check
     *
     * @return Returns true if it does, an exception if it does not.
     *
     * @throws ActionNotFoundException If string does not match the expected
     *                                 pattern
     */
    private boolean isChoiceOk(String stringedChoice) throws
            ActionNotFoundException {

        int actionChoice;
        try {
            //se la stringa è un numero
            actionChoice = Integer.parseInt(stringedChoice);

            //se il numero è contenuto in quelli possibili
            if (!allowedActions.contains(actionChoice)) {
                throw new ActionNotFoundException(
                        "L'azione non è permessa in questa fase di gioco");
            }
            return true;
        } catch (NumberFormatException e) {
            throw new ActionNotFoundException(
                    "Azione non esistente prego riporvare.");
        }

    }

    private void createActionList() {
        //crea lista con le possibili scelte         
        possibleAction = new ArrayList<String>();

        //crea una lista con i numeri delle possibili scelte
        allowedActions = new ArrayList<Integer>();

        //aggiungi movimento ovini se possibile
        int i = 1;
        for (OvineType type : OvineType.values()) {
            if (canMoveOvine(type)) {
                possibleAction.add(i + "- Sposta " + type);
                allowedActions.add(i);
            }
            i++;
        }

        possibleAction.add("4- Sposta pastore");
        allowedActions.add(4);

        //aggiungi acquisto carta se possibile
        if (canBuyCard()) {
            possibleAction.add("5-Compra terreno");
            allowedActions.add(5);
        }

        possibleAction.add("6-Accoppia pecore");
        possibleAction.add("7-Accoppia montone e pecora");
        possibleAction.add("8-Abbatti pecora");
        allowedActions.add(6);
        allowedActions.add(7);
        allowedActions.add(8);
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
     *
     * @throws ActionCancelledException
     */
    private void moveOvine(OvineType type) throws ActionCancelledException {
        Region startRegion;
        Region endRegion;
        while (true) {
            while (true) {
                try {
                    DebugLogger.println("Chiedo prima regione");
                    //chiedi regione di partenza
                    startRegion = this.gameManager.askAboutRegion(
                            this.hashCode(), "Da dove vuoi spostare l'ovino?");
                    break;
                } catch (RegionNotFoundException ex) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);
                    this.gameManager.server.sendTo(this.hashCode(),
                            ex.getMessage());

                }
            }
            while (true) {
                try {
                    DebugLogger.println("Chiedo seconda regione");

                    //e regione arrivo
                    endRegion = this.gameManager.askAboutRegion(this.hashCode(),
                            "In quale regione vuoi spostarlo?");
                    break;
                } catch (RegionNotFoundException ex) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);
                    this.gameManager.server.sendTo(this.hashCode(),
                            ex.getMessage());

                }
            }
            //per ogni strada occupata dai patori del giocatore
            for (Street possibleStreet : this.getShepherdsStreets()) {
                //se le regioni confinano con la strada
                if (startRegion.isNeighbour(possibleStreet) && endRegion.isNeighbour(
                        possibleStreet) && startRegion != endRegion) {
                    //rimuovi ovino del tipo specificato
                    DebugLogger.println("Rimuovo ovino");
                    try {
                        startRegion.removeOvine(type);
                    } catch (NoOvineException ex) {
                        Logger.getLogger(DebugLogger.class.getName()).log(
                                Level.SEVERE,
                                ex.getMessage(), ex);
                        DebugLogger.println("Nessun ovino annullo azione");

                        throw new ActionCancelledException(ex.getMessage());
                    }
                    //e aggiungilo nella regione d'arrivo
                    endRegion.addOvine(new Ovine(type));
                    this.gameManager.server.sendTo(this.hashCode(),
                            "Mossa avvenuta con successo!");

                    //setto le variabili oldRegion e newRegion per informare 
                    //gli altri player
                    this.oldRegion = startRegion;
                    this.newRegion = endRegion;
                    return;
                }
            }
            //le regioni non confinano col pastore 
            throw new ActionCancelledException(
                    "Non è possibile effettuare la mossa! "
                    + "Le regioni indicate non confinano col tuo pastore");

        }
    }

    /**
     * Chiede al giocatore quale pastore spostare e in che strada Se la mossa è
     * possibile (se confinanti o non confinanti e puoi pagare) muovo il pastore
     * e metto il cancello Altrimenti richiedo o annullo azione
     *
     * @throws ActionCancelledException
     */
    private void moveShepherd() throws ActionCancelledException,
                                       FinishedFencesException {

        Street startStreet;
        Street endStreet;
        int idShepherd = 0;
        Shepherd shepherdToMove = null;
        //Controllo sul numero dei pastori

        //se c'è più di un pastore per giocatore
        if (gameManager.shepherd4player > 1) {

            //chiedi al giocatore l'id del pastore da muovere
            idShepherd = this.gameManager.askIdShepherd(this.hashCode(),
                    gameManager.shepherd4player, "Quale pastore vuoi muovere?");
            //prendi shepherd corrispondente
            shepherdToMove = this.shepherd[idShepherd];

            //lancia eccezione se non ci sono pastori
        } else if (gameManager.shepherd4player < 0) {
            throw new ActionCancelledException("Nessun pastore da muovere.");
        }

        //mossa vera e propria
        while (true) {
            try {
                //da strada partenza
                startStreet = shepherdToMove.getStreet();

                //chiedi strada arrivo (lancia StreetNotFoundException,BusyStreetException)
                endStreet = this.gameManager.askStreet(this.hashCode(),
                        idShepherd);
                // se le strade sono confinanti
                if (startStreet.isNeighbour(endStreet)) {
                    //muovilo
                    shepherdToMove.moveTo(endStreet);

                    //metti recinto nella vecchia strada (lancia FinishedFencesException)
                    startStreet.setFence(this.gameManager.bank.getFence());

                    //se le strade non confinano e puoi pagare
                } else if (shepherdToMove.getWallet().getAmount() > 1) {
                    shepherdToMove.getWallet().pay(1); //paga
                    shepherdToMove.moveTo(endStreet); //muovilo

                    //se non puoi pagare, chiedi se cancellare o riprovare mossa
                } else {
                    this.gameManager.askCancelOrRetry(idShepherd,
                            "Strada irraggiungibile.");
                }
                //se la strada è occupata avvisa e chiedi se cancellare o riprovare mossa
            } catch (BusyStreetException e) {
                this.gameManager.askCancelOrRetry(idShepherd, "Strada occupata.");
                Logger
                        .getLogger(Player.class
                                .getName()).log(
                                Level.SEVERE, e.getMessage(), e);

                //se la strada di arrivo non esiste informa e riprova o cancella mossa
            } catch (StreetNotFoundException e) {
                this.gameManager.askCancelOrRetry(this.hashCode(),
                        "Strada di arrivo non esistente ");
                Logger
                        .getLogger(Player.class
                                .getName()).log(
                                Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    private void buyLand() throws ActionCancelledException {

        //creo lista delle possibili regioni da comprare di un pastore
        List<RegionType> possibleRegionsType = new ArrayList<RegionType>();

        String stringedTypeOfCard;
        RegionType chosenTypeOfCard;
        Region endRegion;
        int cardPrice;

        //Raccolgo le monete del primo giocatore usando quelle del suo primo pastore
        //che sicuramente esiste e in quanto il wallet è condiviso da tutti
        //i pastori di un giocatore
        int shepherdMoney = this.shepherd[0].getWallet().getAmount();

        //TODO questo è un metodo
        //per ogni pastore del giocatore
        for (Shepherd shepherd : this.shepherd) {
            //per ogni nodo confinante alla strada di quel pastore
            for (Node region : shepherd.getStreet().getNeighbourNodes()) {
                // se è una regione
                if (region instanceof Region) {
                    // castala a tipo di regione
                    endRegion = (Region) region;
                    //aggiungila ai tipi di regione possibili
                    possibleRegionsType.add(endRegion.getType());
                }

            }
        }
        while (true) {
            try {
                //chiedi il tipo di carta desiderato            
                stringedTypeOfCard = this.gameManager.server.talkTo(
                        this.hashCode(), "Quale tipo di carta vuoi comprare?");

                //convertilo in RegionType
                chosenTypeOfCard = RegionType.valueOf(stringedTypeOfCard);
                //se il tipo chiesto è contenuto nei tipi comprabili dal pastore
                if (possibleRegionsType.contains(chosenTypeOfCard)) {
                    //richiedi prezzo alla banca                    
                    cardPrice = this.gameManager.bank.getPriceOfCard(
                            chosenTypeOfCard);
                    //se il pastore ha abbastanza soldi
                    if (shepherdMoney >= cardPrice) {
                        //recupero la carta dal banco
                        Card card = this.gameManager.bank.getCard(
                                chosenTypeOfCard);
                        //aggiorno i suoi soldi
                        this.shepherd[0].getWallet().pay(cardPrice);

                        //la do al pastore
                        this.shepherd[0].addCard(card);
                        return;
                        //se non ha abbastanza soldi  //TODO: accorpare creando stringa errorMessage da passare a askCancel or Retry alla fine
                    } else {
                        this.gameManager.askCancelOrRetry(this.hashCode(),
                                "Non puoi comprare il territorio " + stringedTypeOfCard + "non hai abbastanza soldi");
                    }
                    //se il tipo non è tra quelli accessibili
                } else {
                    this.gameManager.askCancelOrRetry(this.hashCode(),
                            "Non puoi comprare il territorio, nessun tuo pastore confina con " + stringedTypeOfCard);
                }

            } catch (MissingCardException e) {
                this.gameManager.askCancelOrRetry(this.hashCode(),
                        "Il territorio richiesto non è disponibile");
                Logger
                        .getLogger(Player.class
                                .getName()).log(
                                Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    private void mateSheepWith(OvineType otherOvineType) throws
            ActionCancelledException {
        //TODO: ricontrollare e scomporre
        List<Region> nearRegions = null;
        Region chosenRegion;
        int randomStreetValue;
        String errorMessage = "";

        //per ogni pastore del giocatore
        for (Shepherd shepherdPlayer : this.shepherd) {
            //per ogni regione confinante alla strada di quel pastore
            for (Region region : shepherdPlayer.getStreet().getNeighbourRegions()) {
                //se non contenuta nelle regioni vicine aggiungila
                if ((nearRegions != null) && !nearRegions.contains(region)) {
                    nearRegions.add(region);
                }
            }
        }
        try {
            //chiedi conferma per lanciare dado e lancialo
            randomStreetValue = this.gameManager.askAndThrowDice(this.hashCode());
            //chiedi regione (può lanciare RegionNotFoundException)
            chosenRegion = this.gameManager.askAboutRegion(this.hashCode(),
                    "In quale regione?");
            //se regione è fra le regioni vicine
            if ((nearRegions != null) && nearRegions.contains(chosenRegion)) {
                //controlla che nella regione sia possibile accoppiare Sheep con otherOvine
                if (chosenRegion.isPossibleToMeetSheepWith(otherOvineType)) {
                    //per ogni strada confinante alla regione scelta
                    for (Street street : chosenRegion.getNeighbourStreets()) {
                        //se ha valore uguale a quello del dado e sopra c'è un suo pastore
                        if (street.getValue() == randomStreetValue && this.hasShepherdIn(
                                street)) {
                            //aggiungi ovino e esci dal ciclo
                            if (otherOvineType == otherOvineType.SHEEP) {
                                chosenRegion.addOvine(new Ovine(OvineType.SHEEP));
                            } else if (otherOvineType == otherOvineType.RAM) {
                                chosenRegion.addOvine(new Ovine(OvineType.LAMB));
                            }
                            return;
                        } else {
                            errorMessage = "Accoppiamento non permesso";
                            break;
                        }
                    }
                    if (errorMessage.compareTo("") == 0) {
                        errorMessage = "Nessuna strada con quel valore e col tuo pastore";
                    }
                } else {
                    errorMessage = "Nessun possibile accoppiamento per questa regione.";
                }
            } else {
                errorMessage = "Regione lontana dai tuoi pastori.";
            }
        } catch (RegionNotFoundException ex) {
            errorMessage = ex.getMessage();
            Logger
                    .getLogger(Player.class
                            .getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);
        } finally {
            this.gameManager.askCancelOrRetry(this.hashCode(), errorMessage);
        }
    }

    private void killOvine() {
        Region chosenRegion;
        int randomValue;
        List<Shepherd> neighbourShepherds = new ArrayList<Shepherd>();
        int sumToPay = 0;
        String errorMessage = "";
        OvineType chosenOvineType = null;

        //chiedi se lanciare e lancia il dado
        randomValue = this.gameManager.askAndThrowDice(this.hashCode());

        //per ogni strada del giocatore
        for (Street street : this.getShepherdsStreets()) {
            //se ha valore uguale al risultato del dado
            if ((street.getValue() == randomValue)) {
                try {
                    //scegliere regione da attaccare
                    chosenRegion = this.gameManager.askAboutRegion(
                            this.hashCode(),
                            "scegliere la regione da attaccare");

                    //se ci sono animali
                    if (!chosenRegion.getMyOvines().isEmpty()) {
                        //per ogni strada vicina alla strada del pastore
                        for (Street nearStreet : street.getNeighbourStreets()) {
                            //se ha un pastore e non è dell'attaccante
                            if ((nearStreet.getShepherd() != null) && !(this.ownsShepherd(
                                    nearStreet.getShepherd()))) {
                                //aggiungilo ai pastori vicini
                                neighbourShepherds.add(nearStreet.getShepherd());
                            }
                        }
                        //per ogni pastore vicino
                        for (Shepherd neighbourShepherd : neighbourShepherds) {
                            //chiedi se lanciare al player corrispondente e lancia il dado
                            randomValue = this.gameManager.askAndThrowDice(
                                    this.gameManager.getPlayerByShepherd(
                                            neighbourShepherd).hashCode());
                            //se ha fatto più di 5, 5 compreso
                            if (randomValue >= 5) {
                                //aggiorna valore da pagare
                                sumToPay += 2;
                            }
                        }
                        //se può pagare
                        if (this.shepherd[0].getWallet().getAmount() >= sumToPay) {
                            this.shepherd[0].getWallet().pay(sumToPay);
                            while (true) {
                                //chiedi tipo d ovino
                                this.gameManager.server.talkTo(
                                        this.hashCode(), "che tipo di ovino?");
                                //se presente nella regione scelta
                                try {
                                    //rimuovilo
                                    chosenRegion.removeOvine(chosenOvineType);
                                } catch (NoOvineException ex) {
                                    //riprovare o annullare azione?
                                    this.gameManager.askCancelOrRetry(
                                            this.hashCode(),
                                            chosenOvineType + "non presente");
                                    Logger
                                            .getLogger(Player.class
                                                    .getName()).log(
                                                    Level.SEVERE,
                                                    ex.getMessage(), ex);
                                }
                            }
                        } else {
                            errorMessage = "Non puoi pagare il silezio dei tuoi vicini.";
                        }
                    } else {
                        errorMessage = "Non ci sono animali da abbattere in questa regione.";
                    }
                } catch (RegionNotFoundException e) {
                    errorMessage = e.getMessage();
                    Logger
                            .getLogger(Player.class
                                    .getName()).log(
                                    Level.SEVERE, e.getMessage(), e);
                } catch (ActionCancelledException e) {
                    errorMessage = e.getMessage();
                    Logger
                            .getLogger(Player.class
                                    .getName()).log(
                                    Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        errorMessage = "Non hai strade di quel valore";
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

}
