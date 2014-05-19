package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.MovementException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NoOvineException;
import java.util.Arrays;

/**
 * Classe giocatore
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Player {

    private final Shepherd shepherd;
    private final GameManager gameManager;

    public Player(GameManager gameManager) {
        this.shepherd = new Shepherd();
        this.gameManager = gameManager;
    }

    /**
     *
     * @return Il pastore del giocatore
     */
    public Shepherd getShepherd() {
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

    private void moveOvine(OvineType type) throws ActionCancelledException {
        //TODO       
        boolean tryAction = true;
        while (tryAction) {
            try {
                //chiedi da quale regione
                String stringedRegion = this.gameManager.getServer().talkTo(
                        this.hashCode(), "Da dove vuoi spostare l'ovino?");
                Region startRegion = this.gameManager.getMap().convertStringToRegion(
                        stringedRegion);
                Ovine ovineToKill = startRegion.hasOvine(type);
                if (ovineToKill != null) {// se c'è quell'ovino nella regione
                    //chiedi attraverso quale strada
                    String stringedStreet = this.gameManager.getServer().talkTo(
                            this.hashCode(), "Attraverso quale strada?");
                    Street throughStreet = this.gameManager.getMap().convertStringToStreet(
                            stringedRegion);
                    if (!throughStreet.hasFence() && throughStreet.isShepherdThere(
                            this.shepherd)) {// se la strada non ha recinti e il tuo pastore è li 
                        //trova la regione in cui andrà
                        Region endRegion = this.gameManager.getMap().getEndRegion(
                                startRegion, throughStreet); //questa non dovrebbe mai fallire!
                        //spostala                        
                        startRegion.removeOvine(ovineToKill);  //non fallisce perchè sopra ho controllato se c'erano delle pecore
                        endRegion.addOvine(new Ovine(type));
                        //informa
                        this.gameManager.getServer().sendTo(this.hashCode(),
                                "Movimento di" + type.toString() + "effettuato!");
                    }
                } else {
                    throw new NoOvineException(
                            "Non ci sono" + type.toString() + "nella regione selezionata.");
                }
            } catch (MovementException ex) { //se non c'è l'ovino, o la strada o la regione
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
            }//catch
        }//while
    }

    private void moveShepherd() {
        //TODO
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

}
