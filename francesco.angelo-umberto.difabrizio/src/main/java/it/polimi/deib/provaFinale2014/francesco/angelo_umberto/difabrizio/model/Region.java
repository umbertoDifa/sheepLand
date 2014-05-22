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
        //per ogni strada limitrofa
        for (Node s : endStreets) {
            //se è una strada
            if (s instanceof Street) {
                //casto a strada
                Street street = (Street) s;
                //ritorno false se non ha un recinto
                if (!street.hasFence()) {
                    return false;
                }
            }
        }
        //controllati tutti posso ritornare true
        return true;
    }

    /**
     * Rimuove dagli ovini di una regione quello che gli passo
     *
     * @param type Tipo di ovino da cercare
     * @throws NoOvineException Se l'ovino di quel tipo non c'è
     */
    public void removeOvine(OvineType type) throws NoOvineException {
        //scorri la lista degli ovini fino a quando non trovi uno di quel tipo
        //scorri la lista degli ovini
        for (int i = 0; i < this.myOvines.size(); i++) {
            //il primo ovino che trovi del tipo che ti interessa 
            if (this.myOvines.get(i).getType() == type){
                //rimuovilo e ritorna
                this.myOvines.remove(i);
                return;
            }
        }
        //se non lo trovi
        throw new NoOvineException("Non c'è un ovino del tipo cercato.");
    }
    
    /**
     * 
     * @param otherOvineType
     * @return ritorna vero sse trova sia una pecora sia
     * un ovino di tipo otherOvineType
     */
    public boolean isPossibleMeetSheepWith(OvineType otherOvineType){
        boolean sheepFounded = false, otherOvineFounded = false;
        
        //per tutti gli ovini nella regione o finchè non li trovi
        for(int i=0; (i< this.getMyOvines().size())||
                (sheepFounded&&otherOvineFounded); i++){
            //se Sheep metti alza flag pecora
            if(this.getMyOvines().get(i).getType() == OvineType.SHEEP){
                sheepFounded = true;
            //se di otherType alza relativo flag
            }else if(this.getMyOvines().get(i).getType() == otherOvineType){
                otherOvineFounded = true;
            }
        }
        //ritorna vero sse hai trovato sia una sheep che un ovine di tipo richiesto
        return (sheepFounded && otherOvineFounded);
    }
    
    /**
     * 
     * @param thisOvineType
     * @return vero sse la reigone ha un ovino di tipo thisOvineType
     */
    public boolean hasOvine(OvineType thisOvineType){
        //per ogni ovino nella regione
        for(Ovine ovine: this.getMyOvines()){
            //se del tipo thisOvineType
            if(ovine.getType() == thisOvineType)
                //ritorna vero
                return true;
        }
        //falso se li hai controllati tutti
        return false;
    }
}
