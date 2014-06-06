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
    private final List<Ovine> myOvines;

    /**
     * Costruisce una regione di tipo a caso fra i tipi di regione
     */
    public Region() {
        super();
        this.myOvines = new ArrayList<Ovine>();
        this.type = RegionType.getDefaultRegionType();
    }

    /**
     * Costruisce una regione del tipo specificato
     *
     * @param type Tipo di regione da costruire
     */
    public Region(RegionType type) {
        super();
        this.myOvines = new ArrayList<Ovine>();
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
        List<Street> endStreets = this.getNeighbourStreets();
        //per ogni strada limitrofa

        for (Street s : endStreets) {
            //ritorno false se non ha un recinto
            if (!s.hasFence()) {
                return false;
            }
        }

        //controllati tutti posso ritornare true
        return true;
    }

    /**
     * It removes an ovine of the specified type from a region and returns it
     *
     * @param type Tipo di ovino da cercare
     *
     * @return Removed Ovine
     *
     * @throws NoOvineException If that ovine type does not exist
     */
    public Ovine removeOvine(OvineType type) throws NoOvineException {
        //scorri la lista degli ovini fino a quando non trovi uno di quel tipo
        //scorri la lista degli ovini
        for (int i = 0; i < this.myOvines.size(); i++) {
            //il primo ovino che trovi del tipo che ti interessa 
            if (this.myOvines.get(i).getType() == type) {
                //salva il riferimento all'ovino che stai per uccidere
                Ovine ovineToRemove = this.myOvines.get(i);
                //rimuovilo 
                this.myOvines.remove(i);

                //ritorna l'ovino rimosso
                return ovineToRemove;
            }
        }
        //se non lo trovi
        throw new NoOvineException("Non c'è un ovino del tipo cercato.");
    }

    /**
     * It says if it is possible to meet a sheep with an other type of animal
     *
     * @param mateType Type of animal to meet the sheep with
     *
     * @return true if they can mate, false if not
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
            if (ovine.getType() == thisOvineType) {
                return true;
            }
        }
        //falso se li hai controllati tutti
        return false;
    }
}
