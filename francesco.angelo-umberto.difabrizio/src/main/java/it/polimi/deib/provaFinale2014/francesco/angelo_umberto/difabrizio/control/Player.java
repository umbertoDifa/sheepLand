package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import java.util.Arrays;

/**
 *Classe giocatore
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
        int actionChoice = Integer.parseInt (this.gameManager.getServer().talkTo(this.hashCode(),
                "Scegli l'azione da fare tra:" + Arrays.toString(possibleActions)));
//TODO: chissa se funziona sta roba sopra 
         switch(actionChoice){
             case 1: this.moveSheep(); break;
             case 2: this.moveShepherd(); break;
             case 3: this.buyLand(); break;
             case 4: this.mateSheeps();break;
             case 5: this.mateSheepAndRam(); break;
             case 6: this.killSheep(); break;
             default: throw new ActionNotFoundException("Azione non esistente"); 
         }
    }
     
     private void moveSheep(){
         //TODO
     }
     
     private void moveShepherd(){
         //TODO
     }
     private void buyLand(){
         //TODO
     }
     
     private void mateSheeps(){
         //TODO
     }
     
     private void mateSheepAndRam(){
         //TODO
     }
     
     private void killSheep(){
         //TODO
     }
}
