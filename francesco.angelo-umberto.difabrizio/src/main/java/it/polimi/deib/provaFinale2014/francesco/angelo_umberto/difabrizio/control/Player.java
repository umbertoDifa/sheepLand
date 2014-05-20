package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Node;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.MovementException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NoOvineException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe giocatore
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Player {
    //TODO: pensare modalità 2 giocatori, 2 shepherd ciascuno
    private final ArrayList<Shepherd> shepherd = new ArrayList<Shepherd>();
    private final GameManager gameManager;
    private final int numShepherd;

    public Player(GameManager gameManager, int numShepherd) {
        this.numShepherd = numShepherd;
        for(int i=0; i<numShepherd; i++){ 
            this.shepherd.add( new Shepherd() );
        }
        this.gameManager = gameManager;
    }

    /**
     *
     * @return Il pastore del giocatore
     */
    public ArrayList<Shepherd> getShepherd() {
        return shepherd;
    }

    public void chooseAndMakeAction() throws ActionNotFoundException, ActionCancelledException {

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
     * chiede la regione da dove spostare l'ovino del tipo specificato, lo rimuove
     * dalla regione, lo aggiunge nella regione da dove può arrivare passando per la strada
     * occupata dal pastore del giocatore
     * @param type
     * @throws ActionCancelledException 
     */
    private void moveOvine(OvineType type) throws ActionCancelledException {
        Region startRegion;
        try {
            startRegion = this.gameManager.askAboutRegion(
                    this.hashCode(),"Da dove vuoi spostare l'ovino");  //chiedi regione di partenza
            for (Street possibleStreet: this.getShepherdsStreets()){   //per ogni strada occupata dai patori del giocatore
                if(startRegion.isNeighbour(possibleStreet)){         //se la strada confina con la regione di partenza
                    startRegion.removeOvine(type);                   //rimuovi ovino del tipo specificato
                    ArrayList<Region> possibleEndRegion;
                    
                    for(Node possibleEndRegion: possibleStreet.getNeighbourNodes()){   //per ogni node confinante alla strada 
                        if((possibleEndRegion instanceof Region) && (possibleEndRegion != startRegion)){
                            Region endRegion = (Region) possibleEndRegion;
                            endRegion.addOvine(new Ovine(type));
                        }
                    }
                }
            }
        }catch (MovementException ex) {
//chiedo cosa vuole fare traducendo la scelta in char e processandolo in una switch
                Character choice = this.gameManager.getServer().talkTo(
                        this.hashCode(),
                        ex.getMessage() + " Riprovare(R) o Annullare(A)?").charAt(
                                0); //TODO vedi che qui c'è una getMessage da riempire 
                //TODO se vuole annullare non gli devo togliere l'azione
                switch (choice) {
                    case 'R':
                        break;
                    default: //se vuole annullare o se mette una roba a caso
                        throw new ActionCancelledException("Azione annullata");                        
                }//switch
        }
        
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


    private void moveShepherd() {
        String stringedRegion;
        stringedRegion = this.gameManager.getServer().talkTo(this.hashCode(), "Inserire strada di destinzaione");
        
        
    }

    private void buyLand() {
        //TODO
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
 * @return 
 */
    private Street[] getShepherdsStreets(){
        Street[] streets = new Street[numShepherd];
        for(int i=0; i<numShepherd; i++)
            streets[i] = this.shepherd.get(i).getStreet();
        return streets;
    }

}
