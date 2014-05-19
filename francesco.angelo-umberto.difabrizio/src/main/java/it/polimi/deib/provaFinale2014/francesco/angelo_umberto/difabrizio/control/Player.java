package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
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

    public void chooseAndMakeAction() throws ActionNotFoundException {
        String[] possibleActions = {"1- Sposta una pecora", "2- Sposta pastore",
                                    "3-Compra terreno", "4-Accoppia pecore", "5-Accoppia montone e pecora",
                                    "6-Abbatti pecora"};
        //raccogli la scelta trasformando la string in int
        int actionChoice = Integer.parseInt(this.gameManager.getServer().talkTo(
                this.hashCode(),
                "Scegli l'azione da fare tra:" + Arrays.toString(possibleActions)));
//TODO: chissa se funziona sta roba sopra 
        switch (actionChoice) {
            case 1:
                this.moveSheep();
                break;
            case 2:
                this.moveShepherd();
                break;
            case 3:
                this.buyLand();
                break;
            case 4:
                this.mateSheeps();
                break;
            case 5:
                this.mateSheepAndRam();
                break;
            case 6:
                this.killSheep();
                break;
            default:
                throw new ActionNotFoundException("Azione non esistente");
        }
    }

    private void moveSheep() {
        //TODO       
        boolean tryAction = true;
        while (tryAction) {
            try {
                //chiedi da quale regione
                String stringedRegion = this.gameManager.getServer().talkTo(
                        this.hashCode(), "Da dove vuoi spostare la pecora?");
                Region startRegion = this.gameManager.getMap().convertStringToRegion(
                        stringedRegion);
                Ovine sheepToKill = startRegion.hasOvine(OvineType.SHEEP);
                if (sheepToKill != null ) {// se c'è una pecora nella regione
                    //chiedi attraverso quale strada
                    String stringedStreet = this.gameManager.getServer().talkTo(
                            this.hashCode(), "Attraverso quale strada?");
                    Street throughStreet = this.gameManager.getMap().convertStringToStreet(
                            stringedRegion);
                    if (throughStreet.isFree()) {// se la strada è free 
                        //trova la regione in cui andrà
                        Region endRegion = this.gameManager.getMap().getEndRegion(
                                startRegion, throughStreet); //questa non dovrebbe mai fallire!
                        //spostala                        
                        startRegion.removeOvine(sheepToKill);  //non fallisce perchè sopra ho controllato se c'erano delle pecore
                        endRegion.addOvine(new Ovine(OvineType.SHEEP));
                        //informa
                        this.gameManager.getServer().sendTo(this.hashCode(),
                                "Pecora spostata!");
                    }
                } else {
                    //TODO non ci sono ovini li
                }
            } catch (RegionNotFoundException ex) {
                //chiedo cosa vuole fare traducendo la scelta in char e processandolo in una switch
                Character choice = this.gameManager.getServer().talkTo(
                        this.hashCode(),
                        ex.getMessage() + " Riprovare(R) o Annullare(A)?").charAt(
                                0); //TODO vedi che qui c'è una getMessage da riempire 
                switch (choice) {
                    case 'R':
                        break;
                    default: //se vuole annullare o se mette una roba a caso
                        tryAction = false;
                        break;
                }

            } catch (StreetNotFoundException ex) {
                //TODO da compattare con quella sopra
                this.gameManager.getServer().talkTo(this.hashCode(),
                        "Strada non esistente.Riprovare(R) o Annullare(A)?");

            }
        }
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
}
