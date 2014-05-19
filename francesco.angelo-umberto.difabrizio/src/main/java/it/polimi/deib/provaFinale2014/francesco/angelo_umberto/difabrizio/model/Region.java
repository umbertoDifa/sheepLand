package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;
/**
 * Rappresenta una Regione. E' un nodo della mappa.
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
     * Scandisce gli ovini in una regione per cercare un ovino di tipo ovineType
     *
     * @param ovineType Tipo di ovino da cercare
     *
     * @return L'ovino trovato, null altrimenti
     */
    public Ovine hasOvine(OvineType ovineType) {
        for (Ovine myOvine : this.myOvines) { //per ogni mio ovino
            if (myOvine.getType() == ovineType) { //se il tipo è quello che cercavo
                return myOvine;//ritorno vero
            }
        }
        return null;//se non ho trovato nessun ovino di quel tipo ritorno falso
    }
    /**
     * Rimuove dagli ovini di una regione quello che gli passo
     * @param ovine Ovino da eliminare
     */
    public void removeOvine(Ovine ovine) {
        this.myOvines.remove(ovine);
    }
}
