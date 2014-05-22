package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.MissingCardException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Node;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.BusyStreetException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.MovementException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NoOvineException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.ServerManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Classe giocatore
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Player {

    private final Shepherd[] shepherd;
    private final GameManager gameManager;
    private final int numShepherd;
    
     /**
     * Creo un logger per il sistema
     */
    private final static Logger logger = Logger.getLogger(
            ServerManager.class.getName());

    public Player(GameManager gameManager, int numShepherd) {
        this.numShepherd = numShepherd;
        this.shepherd = new Shepherd[numShepherd];
        
        //creo i pastori necessari
        for(int i=0; i<numShepherd; i++){
            this.shepherd[i] = new Shepherd();
        }
            
        //condivido le risorse del primo pastore con tutti gli altri
        this.setUpSheperdSharing(this.shepherd[0]);
        this.gameManager = gameManager;
    }
    
    private void setUpSheperdSharing(Shepherd mainShepherd){
        
        //per ogni pastore tranne il (for inizia da 1)
        for(int i = 1; i < this.numShepherd; i++){
            
            //condividi il portafoglio
            this.shepherd[i].setWallet(mainShepherd.getWallet());
            
            //condividi le carte
            this.shepherd[i].setMyCards(mainShepherd.getMyCards());
        }
    }
    /**
     * Dato un indice i, ritorna il pastore corrispondente a quell'indice
     *
     * @param i Indice del pastore
     *
     * @return Un pastore del giocatore, null se non esiste l'iesmo pastore
     */
    //TODO warn: quando si fanno le chiamate a questa funzione assicurarsi di inserire un idice valido
    public Shepherd getShepherd(int i) {
        if (i >= 0 && i < this.numShepherd) {
            logger.info("Pastore trovato");
            return shepherd[i];
        }
        return null; //pastore non esistente!
    }

    public void chooseAndMakeAction() throws ActionNotFoundException,
                                             ActionCancelledException,
                                             FinishedFencesException {

        //crea array con le possibili scelte 
        String[] possibleActions = {"1- Sposta una pecora", "2-Sposta Montone", "3-Sposta agnello", "4- Sposta pastore",
                                    "5-Compra terreno", "6-Accoppia pecore", "7-Accoppia montone e pecora",
                                    "8-Abbatti pecora"};

        //raccogli la scelta trasformando la string in int
        int actionChoice = Integer.parseInt(this.gameManager.getServer().talkTo(
                this.hashCode(),
                "Scegli l'azione da fare tra:" + Arrays.toString(possibleActions)));
        //TODO: chissa se funziona sta roba sopra 
        switch (actionChoice) {
            case 1:
                this.moveOvine(OvineType.SHEEP);
                break;
            case 2:
                this.moveOvine(OvineType.RAM);
                break;
            case 3:
                this.moveOvine(OvineType.LAMB);
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
            default:
                throw new ActionNotFoundException(
                        "Azione non esistente.Prego inserire una scelta valida.");
        }
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
            try {
                startRegion = this.gameManager.askAboutRegion(
                        this.hashCode(), "Da dove vuoi spostare l'ovino");  //chiedi regione di partenza
                endRegion = this.gameManager.askAboutRegion(this.hashCode(),
                        "in quale regione vuoi spostarlo?"); //e regione arrivo
                for (Street possibleStreet : this.getShepherdsStreets()) {   //per ogni strada occupata dai patori del giocatore
                    if (startRegion.isNeighbour(possibleStreet) && endRegion.isNeighbour(
                            possibleStreet)) { //se le regioni confinano con la strada
                        startRegion.removeOvine(type);     //rimuovi ovino del tipo specificato
                        endRegion.addOvine(new Ovine(type));     //e aggiungilo nella regione d'arrivo 
                        return;
                    }
                }
                throw new MovementException(
                        "Mossa non valida, nessun ovino presente nella regione di partenza.");
            } catch (MovementException ex) {
                this.gameManager.askCancelOrRetry(this.hashCode(),
                        ex.getMessage());
            }//catch
        }//while
    }

    /**
     * Chiede al giocatore quale pastore spostare e in che strada Se la mossa è
     * possibile (se confinanti o non confinanti e puoi pagare)
     * muovo il pastore e metto il cancello Altrimenti richiedo o
     * annullo azione
     *
     * @throws ActionCancelledException
     */
    private void moveShepherd() throws ActionCancelledException,
                                       FinishedFencesException {
        String stringedStreet;
        Street startStreet;
        Street endStreet;
        int idShepherd = 0;
        Shepherd shepherdToMove = null;
        //Controllo sul numero dei pastori
        
        //se c'è più di un pastore per giocatore
        if (numShepherd > 1) {
            
            //chiedi al giocatore l'id del pastore da muovere
            idShepherd = this.gameManager.askIdShepherd(this.hashCode(), 
                    numShepherd, "Quale pastore vuoi muovere?");
            
            shepherdToMove = this.shepherd[idShepherd]; //prendi shepherd corrispondente

        //lancia eccezione se non ci sono pastori
        } else if(numShepherd < 0)
            throw new ActionCancelledException("Nessun pastore da muovere.");

        //mossa vera e propria
        while (true) {
            try {
                startStreet = shepherdToMove.getStreet();  //da strada partenza
                
                //chiedi strada arrivo (lancia StreetNotFoundException,BusyStreetException)
                endStreet = this.gameManager.askStreet(this.hashCode(), idShepherd);
                if (startStreet.isNeighbour(endStreet)) {// se le strade sono confinanti
                    shepherdToMove.moveTo(endStreet);  //muovilo

                    //metti recinto nella vecchia strada (lancia FinishedFencesException)
                    startStreet.setFence(this.gameManager.bank.getFence());
                    
                //se le strade non confinano e puoi pagare
                } else if(shepherdToMove.getWallet().getAmount() > 1){
                    shepherdToMove.getWallet().pay(1); //paga
                    shepherdToMove.moveTo(endStreet); //muovilo
                    
                //se non puoi pagare, chiedi se cancellare o riprovare mossa
                } else {
                    this.gameManager.askCancelOrRetry(idShepherd, "Strada irraggiungibile.");
                }
            //se la strada è occupata avvisa e chiedi se cancellare o riprovare mossa
            } catch(BusyStreetException e){
                    this.gameManager.askCancelOrRetry(idShepherd, "Strada occupata.");
            
            //se la strada di arrivo non esiste informa e riprova o cancella mossa
            } catch (StreetNotFoundException e) {
                this.gameManager.askCancelOrRetry(this.hashCode(),
                        "Strada di arrivo non esistente ");
            }
        }
    }

    private void buyLand() throws ActionCancelledException {

        //creo lista delle possibili regioni da comprare di un pastore
        ArrayList<RegionType> possibleRegionsType = new ArrayList<RegionType>();

        String stringedTypeOfCard;
        RegionType chosenTypeOfCard;
        Region endRegion;
        int cardPrice;

        //Raccolgo le monete del primo giocatore usando quelle del suo primo pastore
        //che sicuramente esiste e in quanto il wallet è condiviso da tutti
        //i pastori di un giocatore
        int shepherdMoney = this.shepherd[0].getWallet().getAmount();

        //TODO questo è un metodo
        for (Shepherd shepherd : this.shepherd) { //per ogni pastore del giocatore
            for (Node region : shepherd.getStreet().getNeighbourNodes()) {  //per ogni nodo confinante alla strada di quel pastore
                if (region instanceof Region) {  // se è una regione
                    endRegion = (Region) region;   // castala a tipo di regione
                    possibleRegionsType.add(endRegion.getType());  //aggiungila ai tipi di regione possibili
                }

            }
        }
        while (true) {
            try {
                //chiedi il tipo di carta desiderato            
                stringedTypeOfCard = this.gameManager.getServer().talkTo(
                        this.hashCode(), "Quale tipo di carta vuoi comprare?");

                //convertilo in RegionType
                chosenTypeOfCard = RegionType.valueOf(stringedTypeOfCard);

                if (possibleRegionsType.contains(chosenTypeOfCard)) {   //se il tipo chiesto è contenuto nei tipi comprabili dal pastore
                    //richiedi prezzo alla banca                    
                    cardPrice = this.gameManager.bank.priceOfCard(
                            chosenTypeOfCard);

                    if (shepherdMoney >= cardPrice) {//se il pastore ha abbastanza soldi
                        //recupero la carta dal banco
                        Card card = this.gameManager.bank.getCard(chosenTypeOfCard);
                        //aggiorno i suoi soldi
                        this.shepherd[0].getWallet().pay(cardPrice);

                        //la do al pastore
                        this.shepherd[0].addCard(card);
                        return;
                    } else {//se non ha abbastanza soldi  //TODO: accorpare creando stringa errorMessage da passare a askCancel or Retry alla fine
                        this.gameManager.askCancelOrRetry(this.hashCode(),
                                "Non puoi comprare il territorio " + stringedTypeOfCard + "non hai abbastanza soldi");
                    }
                } else {//se il tipo non è tra quelli accessibili                    
                    this.gameManager.askCancelOrRetry(this.hashCode(),
                            "Non puoi comprare il territorio, nessun tuo pastore confina con " + stringedTypeOfCard);
                }

            } catch (MissingCardException e) {
                this.gameManager.askCancelOrRetry(this.hashCode(),
                        "Il territorio richiesto non è disponibile");
            }
        }
    }

    private void mateSheepWith(OvineType otherOvineType) throws ActionCancelledException {
        //TODO: ricontrollare e scomporre
        ArrayList<Region> nearRegions = null;
        Region chosenRegion;
        int randomStreetValue;
        String errorMessage = "";
        boolean sheepFounded = false, otherOvineFounded = false;
        
        //per ogni pastore del giocatore
        for(Shepherd shepherdPlayer: this.shepherd){
            //per ogni regione confinante alla strada di quel pastore
            for(Region region: shepherdPlayer.getStreet().getNeighbourRegions()){
                //se non contenuta nelle regioni vicine aggiungila
                if((nearRegions != null) && !nearRegions.contains(region))
                    nearRegions.add(region);
            }
        }
        try{
            //chiedi conferma per lanciare dado e lancialo
            randomStreetValue = this.gameManager.askAndThrowDice(this.hashCode());
            //chiedi regione (può lanciare RegionNotFoundException)
            chosenRegion = this.gameManager.askAboutRegion(this.hashCode(),
                    "In quale regione?");
            //se regione è fra le regioni vicine
            if((nearRegions != null) && nearRegions.contains(chosenRegion)){
                //controlla che nella regione sia possibile accoppiare Sheep con otherOvine
                if(chosenRegion.isPossibleMeetSheepWith(otherOvineType)){
                    //per ogni strada confinante alla regione scelta
                    for(Street street: chosenRegion.getNeighbourStreets()){
                        //se ha valore uguale a quello del dado e sopra c'è un suo pastore
                        if(street.getValue() == randomStreetValue && this.hasShepherdIn(street)){
                            //aggiungi ovino e esci dal ciclo
                            if(otherOvineType == otherOvineType.SHEEP)
                                chosenRegion.addOvine(new Ovine(OvineType.SHEEP));
                            else if(otherOvineType == otherOvineType.RAM)
                                chosenRegion.addOvine(new Ovine(OvineType.LAMB));
                            return;
                        }else{
                            errorMessage = "Accoppiamento non permesso";
                            break;
                        }
                    } if(errorMessage.compareTo("")==0)
                        errorMessage = "Nessuna strada con quel valore e col tuo pastore";
                }else{
                    errorMessage = "Nessun possibile accoppiamento per questa regione.";
                }
            }else{
                errorMessage = "Regione lontana dai tuoi pastori.";
            }
        } catch(RegionNotFoundException ex){
            errorMessage = ex.getMessage();
        }finally{
            this.gameManager.askCancelOrRetry(this.hashCode(), errorMessage);
        }
    }

    private void killOvine() {
        Region chosenRegion;
        int randomValue;
        ArrayList<Shepherd> neighbourShepherds = new ArrayList<Shepherd>();
        int sumToPay = 0;
        String errorMessage = "";
        OvineType chosenOvineType = null;
        
        randomValue = this.gameManager.askAndThrowDice(this.hashCode()); //lancia il dado
        
        //per ogni strada del giocatore
        for(Street street: this.getShepherdsStreets()){
            //se ha valore uguale al risultato del dado
            if((street.getValue() == randomValue)){
                try{
                    //scegliere regione da attaccare
                    chosenRegion = this.gameManager.askAboutRegion(this.hashCode(),
                            "scegliere la regione da attaccare");
                    
                    //se ci sono animali
                    if(!chosenRegion.getMyOvines().isEmpty()){
                        //per ogni strada vicina alla strada del pastore
                        for(Street nearStreet: street.getNeighbourStreets()){
                            //se ha un pastore e non è dell'attaccante
                            if((nearStreet.getShepherd() != null)&&!(this.ownsShepherd(nearStreet.getShepherd()))){
                                //aggiungilo ai pastori vicini
                                neighbourShepherds.add(nearStreet.getShepherd());
                            }
                        }
                        //per ogni pastore vicino
                        for(Shepherd neighbourShepherd: neighbourShepherds){
                            //chiedi se lanciare al player corrispondente e lancia il dado
                            randomValue = this.gameManager.askAndThrowDice(
                                    this.gameManager.getPlayerByShepherd(neighbourShepherd).hashCode());
                            //se ha fatto più di 5, 5 compreso
                            if(randomValue>=5)
                                //aggiorna valore da pagare
                                sumToPay += 2;
                        }
                        //se può pagare
                        if(this.getShepherd(0).getWallet().getAmount() >= sumToPay){
                            this.getShepherd(0).getWallet().pay(sumToPay);
                            while(true){
                                //chiedi tipo d ovino
                                this.gameManager.getServer().talkTo(this.hashCode(), "che tipo di ovino?");
                                //se presente nella regione scelta
                                try{
                                    //rimuovilo
                                    chosenRegion.removeOvine(chosenOvineType);
                                }catch(NoOvineException ex){
                                    //riprovare o annullare azione?
                                    this.gameManager.askCancelOrRetry(this.hashCode(), 
                                            chosenOvineType+"non presente");
                                }
                            }
                        }else{
                            errorMessage = "Non puoi pagare il silezio dei tuoi vicini.";
                        }
                    }else{
                        errorMessage = "Non ci sono animali da abbattere in questa regione.";  
                    }
                }catch (RegionNotFoundException e){
                    errorMessage = e.getMessage();
                }catch (ActionCancelledException e){
                    errorMessage = e.getMessage();
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
    private Street[] getShepherdsStreets() {
        //creo array grande come il numero dei pastori
        Street[] streets = new Street[numShepherd];

        for (int i = 0; i < numShepherd; i++) {
            streets[i] = this.shepherd[i].getStreet();
        }
        return streets;
    }

    private boolean hasShepherdIn(Street street) {
        for(Shepherd possibleShepherd: this.shepherd){ //per ogni suo pastore
            if(possibleShepherd.getStreet().equals(street))
                return true;
        }
        return false;
    }
    
    private boolean ownsShepherd(Shepherd sheepherd){
        for(Shepherd myShepherd: this.shepherd){
            if(myShepherd == sheepherd)
                return true;
        }
        return false;
    }

}
