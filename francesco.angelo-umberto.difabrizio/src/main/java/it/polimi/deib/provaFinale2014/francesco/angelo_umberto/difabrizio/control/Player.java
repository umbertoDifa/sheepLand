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
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe giocatore
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Player {

    //TODO: pensare modalità 2 giocatori, 2 shepherd ciascuno
    private final Shepherd[] shepherd;
    private final GameManager gameManager;
    private final int numShepherd;

    public Player(GameManager gameManager, int numShepherd) {
        this.numShepherd = numShepherd;
        this.shepherd = new Shepherd[numShepherd];
        this.gameManager = gameManager;
    }

    /**
     *
     * @return Il pastore del giocatore
     */
    //TODO quando si fanno le chiamate a questa funzione assicurarsi di inserire un idice valido
    public Shepherd getShepherd(int i) {
        if (i >= 0 && i < this.numShepherd) {
            return shepherd[i];
        }
        return null; //pastore non esistente!
    }

    public void chooseAndMakeAction() throws ActionNotFoundException,
                                             ActionCancelledException,
                                             FinishedFencesException {

        //crea array con le possibili scelte //TODO montone agnello
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
                this.mateSheeps();
                break;
            case 7:
                this.mateSheepAndRam();
                break;
            case 8:
                this.killSheep();
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
                    if (startRegion.isNeighbour(possibleStreet) && possibleStreet.isNeighbour(
                            endRegion)) { //se la strada confina con le due regioni
                        startRegion.removeOvine(type);     //rimuovi ovino del tipo specificato
                        endRegion.addOvine(new Ovine(type));     //e aggiungilo nella regione d'arrivo 
                        return;
                    }
                }
                throw new MovementException("Mossa non valida");
            } catch (MovementException ex) {
                this.gameManager.askCancelOrRetry(this.hashCode(),
                        ex.getMessage());
            }//catch
        }//while
    }

//        while (true) {
//            try {
//
//                //TODO: mossa da regione, attraverso strada              
//                //chiedi da quale regione
//                askRegion();
//                askStreet();
//
//                String stringedRegion = this.gameManager.getServer().talkTo(
//                        this.hashCode(), "Da dove vuoi spostare l'ovino?");
//                Region startRegion = this.gameManager.getMap().convertStringToRegion(
//                        stringedRegion); //TODO: controllare validità regione, eccezione
//                if(startRegion.isNeighbour(this.shepherd.getStreet())){  //se la regione selezionata confina con la strada del pastore
//                    Ovine ovineToMove = startRegion.hasOvine(type); //TODO: spostare NoOvineException in Ovine
//                    if (ovineToMove != null) {// se c'è quell'ovino nella regione
//                        //chiedi attraverso quale strada
//                        String stringedStreet = this.gameManager.getServer().talkTo(
//                                this.hashCode(), "Attraverso quale strada?");
//                        Street throughStreet = this.gameManager.getMap().convertStringToStreet(
//                                stringedRegion);
//                        if (!throughStreet.hasFence() && throughStreet.isShepherdThere(
//                                this.shepherd)) {// se la strada non ha recinti e il tuo pastore è li 
//                            //trova la regione in cui andrà
//                            Region endRegion = this.gameManager.getMap().getEndRegion(
//                                    startRegion, throughStreet); //questa non dovrebbe mai fallire!
//                            //spostala                        
//                            startRegion.removeOvine(ovineToMove.getType());  //non fallisce perchè sopra ho controllato se c'erano delle pecore
//                            endRegion.addOvine(new Ovine(type));
//                            //informa
//                            this.gameManager.getServer().sendTo(this.hashCode(),
//                                    "Movimento di" + type.toString() + "effettuato!");
//                        }
//                    } else {
//                        throw new NoOvineException(
//                                "Non ci sono" + type.toString() + "nella regione selezionata.");
//                    }
//                } else {
//                    throw new NoOvineException("La regione selezionata non è valida.");
//                }
//            } catch (MovementException ex) { //se non c'è l'ovino, o la strada o la regione
//                //chiedo cosa vuole fare traducendo la scelta in char e processandolo in una switch
//                Character choice = this.gameManager.getServer().talkTo(
//                        this.hashCode(),
//                        ex.getMessage() + " Riprovare(R) o Annullare(A)?").charAt(
//                                0); //TODO vedi che qui c'è una getMessage da riempire 
//                //TODO se vuole annullare non gli devo togliere l'azione
//                switch (choice) {
//                    case 'R':
//                        break;
//                    default: //se vuole annullare o se mette una roba a caso
//                        throw new ActionCancelledException("Azione annullata");                        
//                }//switch
//            }//catch
//        }//while
    /**
     * Chiede al giocatore quale pastore spostare e in che strada Se la mossa è
     * possibile muovo il pastore e metto il cancello Altrimenti richiedo o
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
        //Controllo sul numero dei pastori
        if (numShepherd > 0) {  //se c'è più di un pastore per giocatore
            do {
                idShepherd = Integer.parseInt(
                        this.gameManager.getServer().talkTo(
                                this.hashCode(), "Quale pastore vuoi muovere?"));  //chiedi quale pastore muovere
                //la risposta sarà 1 o 2 quindi lo ricalibro sulla lunghezza dell'array
                idShepherd--;
            } while (idShepherd < 0 && idShepherd > this.numShepherd);  //fintanto che non va bene l'id
        } else {
            throw new ActionCancelledException("Ci sono zero pastori!");
        }

        //mossa vera e propria
        while (true) {
            try {
                startStreet = this.shepherd[idShepherd].getStreet();  //da strada partenza
                stringedStreet = this.gameManager.getServer().talkTo(
                        this.hashCode(),
                        "Inserire strada di destinzaione");  //chiedi strada di arrivo
                endStreet = this.gameManager.getMap().convertStringToStreet(
                        stringedStreet);  //convertila
                if (endStreet.isFree()) {//se la strada di arrivo è libera
                    if (startStreet.isNeighbour(endStreet)) {// se le strade sono confinanti
                        this.shepherd[idShepherd].moveTo(endStreet);  //muovilo
                        
                        //metti recinto nella vecchia strada (lancia FinishedFencesException)
                        startStreet.setFence(this.gameManager.bank.getFence()); 
                    } else {//se le strade non confinano
                        //controlla che il pastore abbia i soldi per pagare il trasporto
                        //eventualmente muovi
                    }
                } else {//se la strada è occupata
                    //avvisa e richiedi cosa fare
                    this.gameManager.askCancelOrRetry(this.hashCode(),
                            "Strada già occupata");
                }
            } catch (StreetNotFoundException e) {//se la strada di arrivo non esiste
                //informa e riprova o cancella mossa
                this.gameManager.askCancelOrRetry(this.hashCode(),
                        "Strada di arrivo non esistente ");

            }
        }

    

    

    private void buyLand() throws ActionCancelledException {
        ArrayList<RegionType> possibleRegionsType = new ArrayList<RegionType>();
        String stringedTypeOfCard;
        RegionType chosenTypeOfCard;
        Region endRegion = null;
        int prize;
        int amount = this.shepherd.get(0).getWallet().getAmount();

        for (Shepherd shepherd : this.getShepherd()) {  //per ogni pastore del giocatore
            for (Node region : shepherd.getStreet().getNeighbourNodes()) {  //per ogni nodo confinante alla strada di quel pastore
                if (region instanceof Region) {  // se è una regione
                    endRegion = (Region) region;   // castala a tipo di regione
                    possibleRegionsType.add(endRegion.getType());  //aggiungila ai tipi di regione possibili
                }

            }
        }
        while (true) {
            try {
                stringedTypeOfCard = this.gameManager.getServer().talkTo(
                        this.hashCode(), "Quale tipo di carta vuoi comprare?");
                //chiedi il tipo di carta desiderato e convertilo in RegionType
                chosenTypeOfCard = RegionType.values()[Integer.parseInt(
                        stringedTypeOfCard)];
                prize = this.gameManager.bank.getPrizeOf(chosenTypeOfCard);  //prendi prezzo della carta da banca //TODO: bank.getPrizeOf da implementare
                if (possibleRegionsType.contains(chosenTypeOfCard)) {   //se il tipo è contenuto nei tipi comprabili dal pastore
                    if (amount >= prize) {
                        Card card = this.gameManager.bank.getCard(
                                chosenTypeOfCard);
                        this.shepherd.get(0).addCard(card);
                        this.shepherd.get(0).getWallet().setAmount(
                                amount - prize);
                        return;
                    }
                }
            } catch (MissingCardException e) { //TODO: gestire meglio eccezione
                this.gameManager.askCancelOrRetry(this.hashCode(),
                        "Tipo carta non valido");
            }
        }
    }

    private void mateSheeps() {
        //TODO
    }

    private void mateSheepAndRam() {
        //TODO
    }

    private void killSheep() {
        //TODO
    }

    public void sellCards() {
        //TODO
    }

    public void buyCards() {
        //TODO
    }

    /**
     * ritorna le strade occupate dai pastori del giocatore
     *
     * @return
     */
    private Street[] getShepherdsStreets() {
        Street[] streets = new Street[numShepherd];
        for (int i = 0; i < numShepherd; i++) {
            streets[i] = this.shepherd.get(i).getStreet();
        }
        return streets;
    }

}
