package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NoOvineException;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una Regione. E' un nodo della mappa.
 *
 * @author Umberto
 */
public class Region extends Node {

    final private RegionType type;
    private List<Ovine> myOvines = new ArrayList<Ovine>();

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
    public List<Ovine> getMyOvines() {
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
        List<Node> endStreets = this.getNeighbourNodes();
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
     *
     * @throws NoOvineException Se l'ovino di quel tipo non c'è
     */
    public void removeOvine(OvineType type) throws NoOvineException {
        //scorri la lista degli ovini fino a quando non trovi uno di quel tipo
        //scorri la lista degli ovini
        for (int i = 0; i < this.myOvines.size(); i++) {
            //il primo ovino che trovi del tipo che ti interessa 
            if (this.myOvines.get(i).getType() == type) {
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
     * @param mateType
     *
     * @return ritorna vero sse trova sia una pecora sia un ovino di tipo
     *         mateType
     */
    public boolean isPossibleToMeetSheepWith(OvineType mateType) {
        //piouttosto che fare due for preferisco farne uno è 
        //fermarmi quando ho almeno due pecore è un altro tipo
        //poi controllo se ho trovato quello che mi serviva.
        //Anche se questo significa raggiungere la fini della lista
        //degli ovini anche quando in realtà avrei già trovato la coppia
        //sapendo che comunque gli ovini in una regione non saranno mai tantissimi
        //lo preferisco piuttosto che duplicare il for

        int foundedSheep = 0;
        int foundedMate = 0;

        int sheepToFind = 2;
        int mateToFind = 1;
        
        for (int i = 0; i < this.getMyOvines().size()
                && (foundedMate < mateToFind || foundedSheep < sheepToFind); i++) {
            if (this.getMyOvines().get(i).getType() == OvineType.SHEEP) {
                foundedSheep++;
            } else if (this.getMyOvines().get(i).getType() == mateType) {
                foundedMate++;
            }
        }

        if (mateType == OvineType.SHEEP) {
            return foundedSheep >= sheepToFind;
        }
        return foundedMate >= mateToFind && foundedSheep >= 1;

    }

    /**
     *
     * @param thisOvineType
     *
     * @return vero sse la reigone ha un ovino di tipo thisOvineType
     */
    public boolean hasOvine(OvineType thisOvineType) {
        //per ogni ovino nella regione
        for (Ovine ovine : this.getMyOvines()) {
            //se del tipo thisOvineType
            if (ovine.getType() == thisOvineType) //ritorna vero
            {
                return true;
            }
        }
        //falso se li hai controllati tutti
        return false;
    }
}
