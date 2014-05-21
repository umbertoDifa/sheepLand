package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NoOvineException;
import java.util.ArrayList;

/**
 * Rappresenta una Regione. E' un nodo della mappa.
 *
 * @author Umberto
 */
public class Region extends Node {

    final private RegionType type;
    private ArrayList<Ovine> myOvines = new ArrayList<Ovine>();

    /**
     * Costruisce una regione di tipo a caso fra i tipi di regione
     */
    public Region() {
        super();
        this.type = RegionType.getDefaultRegionType();
    }

    /**
     * Costruisce una regione del tipo specificato
     *
     * @param type Tipo di regione da costruire
     */
    public Region(RegionType type) {
        super();
        this.type = type;
    }

    /**
     *
     * @return Il tipo di regione
     */
    public RegionType getType() {
        return type;
    }

    /**
     *
     * @return La lista degli ovini in una regione
     */
    public ArrayList<Ovine> getMyOvines() {
        return myOvines;
    }

    /**
     * Aggiunge un ovino a quelli presenti in una regione
     *
     * @param ovine Ovino da aggiungere
     */
    public void addOvine(Ovine ovine) {
        this.myOvines.add(ovine);
    }

    /**
     * Controlla che tutte le strade limitrofe abbiano un recinto
     *
     * @return True se e solo se tutte le strade sono recintate
     */
    public boolean isAllFenced() {
        ArrayList<Node> endStreets = this.getNeighbourNodes();
        for (Node s : endStreets) {           //per ogni strada limitrofa
            if (s instanceof Street) {       //se è una strada
                Street street = (Street) s; //casto a strada
                if (!street.hasFence()) {    //ritorno false se non ha un recinto
                    return false;
                }
            }
        }
        return true;                    //controllati tutti posso ritornare true
    }

    /**
     * Rimuove dagli ovini di una regione quello che gli passo
     *
     * @param type Tipo di ovino da cercare
     * @throws NoOvineException Se l'ovino di quel tipo non c'è
     */
    public void removeOvine(OvineType type) throws NoOvineException {
        //scorri la lista degli ovini fino a quando non trovi uno di quel tipo
        for (int i = 0; i < this.myOvines.size(); i++) {//scorri la lista degli ovini
            if (this.myOvines.get(i).getType() == type) //il primo ovino che trovi del tipo che ti interessa 
            {
                this.myOvines.remove(i);//rimuovilo
                return;
            }
        }
        //se non lo trovi
        throw new NoOvineException("Non c'è un ovino del tipo cercato.");
    }
    
    public boolean isPossibleMeetSheepWith(OvineType otherOvineType){
        boolean sheepFounded = false, otherOvineFounded = false;
        
        for(int i=0; (i< this.getMyOvines().size())||
                (sheepFounded&&otherOvineFounded); i++){ //per tutti gli ovini nella regione o finchè non li trovi
            if(this.getMyOvines().get(i).getType() == OvineType.SHEEP){ //se Sheep metti alza flag pecora
                sheepFounded = true;
            }else if(this.getMyOvines().get(i).getType() == otherOvineType){ //se di otherType alza relativo flag
                otherOvineFounded = true;
            }
        }
        return (sheepFounded && otherOvineFounded);
    }
    
    public boolean hasOvine(OvineType thisOvineType){
        for(Ovine ovine: this.getMyOvines()){
            if(ovine.getType() == thisOvineType)
                return true;
        }
        return false;
    }
}
