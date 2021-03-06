package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NodeNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta la plancia, quindi conosce Strade e Regioni e come esse sono
 * connesse. Crea un grafo che connette Strade e Regioni. Conosce la posizione
 * del lupo e della pecora nera. E' fonadamentale considerare che l'array delle
 * regioni è ordinato in base al tipo di regione per permettere una ricerca
 * attraverso indici. Ogni tipo di regione ha il suo spazio nell'array in cui
 * inserire le proprie regioni. E' fondamentale che l'indice dell'array delle
 * strade corrisponde all'id di quella precisa strada nella mappa, secondo la
 * convenzione documentata.
 *
 * @author Umberto
 */
public class Map {

    private final Node[] streets;
    private final Node[] regions;
    private final BlackSheep blackSheep;
    private final Wolf wolf;

    /**
     * Crea mappa e istanzia la grandezza dei due array di nodi, uno per le
     * strade, uno per le regioni, secondo le dimensioni ufficiali del gioco.
     * Instanzia anche una pecora nera e un lupo
     */
    public Map() {
        this.streets = new Street[GameConstants.NUM_STREETS.getValue()];
        this.regions = new Region[GameConstants.NUM_REGIONS.getValue()];
        this.blackSheep = new BlackSheep();
        this.wolf = new Wolf();
    }

    /**
     * Converts a string to its Street object taking advantage of the array
     * order of the streets.
     *
     * @param streetId Stringed id of the street
     *
     * @return Street object corresponding to that street
     *
     * @throws StreetNotFoundException If no street exists given that string
     */
    public Street convertStringToStreet(String streetId) throws
            StreetNotFoundException {
        if (streetId.matches("\\d{1,2}")) {
            int id = Integer.parseInt(streetId);
            //se l'id è nel range dell'array e quello che c'è dentro non è null
            if (id >= 0 && id < GameConstants.NUM_STREETS.getValue()
                    && this.streets[id] != null) {
                return (Street) this.streets[id];
            } else {
                throw new StreetNotFoundException("Strada non esistente");
            }
        } else {
            throw new StreetNotFoundException(
                    "La stringa inserita non identifica una strada, prego inserire l'id della strada.");
        }
    }

    /**
     * The same of convertStringToStreet for the regions
     *
     * @param regionId the region id to convert
     *
     * @return The region object in that index
     *
     * @throws RegionNotFoundException If no region with that id is found
     */
    public Region convertStringToRegion(String regionId) throws
            RegionNotFoundException {
        if (regionId.matches("\\d{1,2}")) {
            int id = Integer.parseInt(regionId);
            if (id >= 0 && id < GameConstants.NUM_REGIONS.getValue()
                    && this.regions[id] != null) {
                return (Region) this.regions[id];
            } else {
                throw new RegionNotFoundException("Regione non esistente");
            }
        } else {
            throw new RegionNotFoundException(
                    "La stringa inserita non identifica una regione, prego inserire l'id della regione.");
        }
    }

    /**
     * Crea nodi delle regioni e delle strade, li assegna a map, e li collega
     * fra di loro
     */
    public void setUp() {
        createRegions();
        createStreets();
        createLinks();
    }

    /**
     *
     * @return La pecora nera
     */
    public BlackSheep getBlackSheep() {
        return blackSheep;
    }

    /**
     *
     * @return Il lupo
     */
    public Wolf getWolf() {
        return wolf;
    }

    /**
     * Data una region e un value ritorna la strada limitrofa alla regione con
     * quel preciso valore
     *
     * @param region La regione i cui confini devono essere controllati
     * @param value  Il valore della strada limitrofa che si cerca
     *
     * @return La strada trovata
     *
     * @throws StreetNotFoundException
     */
    public Street getStreetByValue(Region region, int value) throws
            StreetNotFoundException {
        //per ogni strada adiacente alla regione
        for (Street street : region.getNeighbourStreets()) {
            //se il valore è uguale a value
            if (street.getValue() == value) {
                //ritornala
                return street;
            }
        }
        //se non hai trovato nessuna strada limitrofa con quel valore
        throw new StreetNotFoundException(
                "Non esiste una strada con valore " + Integer.toString(value) + " nella regione. ");
    }

    /**
     * Collega i nodi tra di loro secondo la configurazione della mappa
     * ufficiale
     */
    private void createLinks() {

        //collego regioni a strade con un link bidirezionale
        this.regions[0].connectTo(this.streets[0]);
        this.regions[0].connectTo(this.streets[3]);
        this.regions[0].connectTo(this.streets[10]);
        this.regions[3].connectTo(this.streets[0]);
        this.regions[3].connectTo(this.streets[1]);
        this.regions[3].connectTo(this.streets[4]);
        this.regions[3].connectTo(this.streets[5]);
        this.regions[4].connectTo(this.streets[1]);
        this.regions[4].connectTo(this.streets[2]);
        this.regions[4].connectTo(this.streets[6]);
        this.regions[7].connectTo(this.streets[2]);
        this.regions[7].connectTo(this.streets[7]);
        this.regions[7].connectTo(this.streets[8]);
        this.regions[7].connectTo(this.streets[9]);
        this.regions[8].connectTo(this.streets[9]);
        this.regions[8].connectTo(this.streets[13]);
        this.regions[8].connectTo(this.streets[14]);
        this.regions[1].connectTo(this.streets[10]);
        this.regions[1].connectTo(this.streets[15]);
        this.regions[1].connectTo(this.streets[22]);
        this.regions[1].connectTo(this.streets[26]);
        this.regions[5].connectTo(this.streets[3]);
        this.regions[5].connectTo(this.streets[4]);
        this.regions[5].connectTo(this.streets[11]);
        this.regions[5].connectTo(this.streets[15]);
        this.regions[5].connectTo(this.streets[16]);
        this.regions[5].connectTo(this.streets[20]);
        this.regions[6].connectTo(this.streets[5]);
        this.regions[6].connectTo(this.streets[6]);
        this.regions[6].connectTo(this.streets[7]);
        this.regions[6].connectTo(this.streets[11]);
        this.regions[6].connectTo(this.streets[12]);
        this.regions[6].connectTo(this.streets[17]);
        this.regions[9].connectTo(this.streets[8]);
        this.regions[9].connectTo(this.streets[12]);
        this.regions[9].connectTo(this.streets[13]);
        this.regions[9].connectTo(this.streets[18]);
        this.regions[9].connectTo(this.streets[19]);
        this.regions[9].connectTo(this.streets[21]);
        this.regions[10].connectTo(this.streets[14]);
        this.regions[10].connectTo(this.streets[19]);
        this.regions[10].connectTo(this.streets[25]);
        this.regions[10].connectTo(this.streets[32]);
        this.regions[17].connectTo(this.streets[26]);
        this.regions[17].connectTo(this.streets[27]);
        this.regions[17].connectTo(this.streets[38]);
        this.regions[2].connectTo(this.streets[20]);
        this.regions[2].connectTo(this.streets[22]);
        this.regions[2].connectTo(this.streets[23]);
        this.regions[2].connectTo(this.streets[27]);
        this.regions[2].connectTo(this.streets[28]);
        this.regions[2].connectTo(this.streets[33]);
        this.regions[18].connectTo(this.streets[16]);
        this.regions[18].connectTo(this.streets[17]);
        this.regions[18].connectTo(this.streets[18]);
        this.regions[18].connectTo(this.streets[23]);
        this.regions[18].connectTo(this.streets[24]);
        this.regions[18].connectTo(this.streets[29]);
        this.regions[12].connectTo(this.streets[21]);
        this.regions[12].connectTo(this.streets[24]);
        this.regions[12].connectTo(this.streets[25]);
        this.regions[12].connectTo(this.streets[30]);
        this.regions[12].connectTo(this.streets[31]);
        this.regions[12].connectTo(this.streets[36]);
        this.regions[11].connectTo(this.streets[31]);
        this.regions[11].connectTo(this.streets[32]);
        this.regions[11].connectTo(this.streets[37]);
        this.regions[15].connectTo(this.streets[33]);
        this.regions[15].connectTo(this.streets[34]);
        this.regions[15].connectTo(this.streets[38]);
        this.regions[15].connectTo(this.streets[40]);
        this.regions[16].connectTo(this.streets[28]);
        this.regions[16].connectTo(this.streets[29]);
        this.regions[16].connectTo(this.streets[30]);
        this.regions[16].connectTo(this.streets[34]);
        this.regions[16].connectTo(this.streets[35]);
        this.regions[16].connectTo(this.streets[39]);
        this.regions[13].connectTo(this.streets[35]);
        this.regions[13].connectTo(this.streets[36]);
        this.regions[13].connectTo(this.streets[37]);
        this.regions[13].connectTo(this.streets[41]);
        this.regions[14].connectTo(this.streets[39]);
        this.regions[14].connectTo(this.streets[40]);
        this.regions[14].connectTo(this.streets[41]);

        //collego strade a strade con un link bidirezionale
        this.streets[0].connectTo(this.streets[3]);
        this.streets[0].connectTo(this.streets[4]);
        this.streets[1].connectTo(this.streets[5]);
        this.streets[1].connectTo(this.streets[6]);
        this.streets[2].connectTo(this.streets[6]);
        this.streets[2].connectTo(this.streets[7]);
        this.streets[3].connectTo(this.streets[4]);
        this.streets[3].connectTo(this.streets[10]);
        this.streets[3].connectTo(this.streets[15]);
        this.streets[4].connectTo(this.streets[5]);
        this.streets[4].connectTo(this.streets[11]);
        this.streets[5].connectTo(this.streets[6]);
        this.streets[5].connectTo(this.streets[11]);
        this.streets[6].connectTo(this.streets[7]);
        this.streets[7].connectTo(this.streets[8]);
        this.streets[7].connectTo(this.streets[12]);
        this.streets[8].connectTo(this.streets[9]);
        this.streets[8].connectTo(this.streets[12]);
        this.streets[8].connectTo(this.streets[13]);
        this.streets[9].connectTo(this.streets[13]);
        this.streets[10].connectTo(this.streets[15]);
        this.streets[11].connectTo(this.streets[16]);
        this.streets[11].connectTo(this.streets[17]);
        this.streets[12].connectTo(this.streets[17]);
        this.streets[12].connectTo(this.streets[18]);
        this.streets[13].connectTo(this.streets[14]);
        this.streets[13].connectTo(this.streets[19]);
        this.streets[14].connectTo(this.streets[19]);
        this.streets[15].connectTo(this.streets[20]);
        this.streets[15].connectTo(this.streets[22]);
        this.streets[16].connectTo(this.streets[17]);
        this.streets[16].connectTo(this.streets[20]);
        this.streets[16].connectTo(this.streets[23]);
        this.streets[17].connectTo(this.streets[18]);
        this.streets[18].connectTo(this.streets[21]);
        this.streets[18].connectTo(this.streets[24]);
        this.streets[19].connectTo(this.streets[21]);
        this.streets[19].connectTo(this.streets[25]);
        this.streets[20].connectTo(this.streets[22]);
        this.streets[20].connectTo(this.streets[23]);
        this.streets[21].connectTo(this.streets[24]);
        this.streets[21].connectTo(this.streets[25]);
        this.streets[22].connectTo(this.streets[26]);
        this.streets[22].connectTo(this.streets[27]);
        this.streets[23].connectTo(this.streets[28]);
        this.streets[23].connectTo(this.streets[29]);
        this.streets[24].connectTo(this.streets[29]);
        this.streets[24].connectTo(this.streets[30]);
        this.streets[25].connectTo(this.streets[31]);
        this.streets[25].connectTo(this.streets[32]);
        this.streets[26].connectTo(this.streets[27]);
        this.streets[27].connectTo(this.streets[33]);
        this.streets[27].connectTo(this.streets[38]);
        this.streets[28].connectTo(this.streets[29]);
        this.streets[28].connectTo(this.streets[33]);
        this.streets[28].connectTo(this.streets[34]);
        this.streets[29].connectTo(this.streets[30]);
        this.streets[30].connectTo(this.streets[35]);
        this.streets[30].connectTo(this.streets[36]);
        this.streets[31].connectTo(this.streets[32]);
        this.streets[31].connectTo(this.streets[36]);
        this.streets[31].connectTo(this.streets[37]);
        this.streets[33].connectTo(this.streets[34]);
        this.streets[33].connectTo(this.streets[38]);
        this.streets[34].connectTo(this.streets[39]);
        this.streets[34].connectTo(this.streets[40]);
        this.streets[35].connectTo(this.streets[36]);
        this.streets[35].connectTo(this.streets[39]);
        this.streets[35].connectTo(this.streets[41]);
        this.streets[36].connectTo(this.streets[37]);
        this.streets[39].connectTo(this.streets[40]);
        this.streets[39].connectTo(this.streets[41]);
    }

    /**
     * Creazione strade e assegnazione strade alla mappa
     */
    private void createStreets() {

        this.streets[0] = new Street(2);
        this.streets[1] = new Street(3);
        this.streets[2] = new Street(1);
        this.streets[3] = new Street(3);
        this.streets[4] = new Street(4);
        this.streets[5] = new Street(6);
        this.streets[6] = new Street(2);
        this.streets[7] = new Street(3);
        this.streets[8] = new Street(5);
        this.streets[9] = new Street(2);
        this.streets[10] = new Street(1);
        this.streets[11] = new Street(5);
        this.streets[12] = new Street(4);
        this.streets[13] = new Street(3);
        this.streets[14] = new Street(1);
        this.streets[15] = new Street(2);
        this.streets[16] = new Street(6);
        this.streets[17] = new Street(1);
        this.streets[18] = new Street(2);
        this.streets[19] = new Street(6);
        this.streets[20] = new Street(1);
        this.streets[21] = new Street(1);
        this.streets[22] = new Street(3);
        this.streets[23] = new Street(5);
        this.streets[24] = new Street(3);
        this.streets[25] = new Street(2);
        this.streets[26] = new Street(4);
        this.streets[27] = new Street(2);
        this.streets[28] = new Street(6);
        this.streets[29] = new Street(4);
        this.streets[30] = new Street(5);
        this.streets[31] = new Street(4);
        this.streets[32] = new Street(5);
        this.streets[33] = new Street(4);
        this.streets[34] = new Street(3);
        this.streets[35] = new Street(2);
        this.streets[36] = new Street(6);
        this.streets[37] = new Street(1);
        this.streets[38] = new Street(1);
        this.streets[39] = new Street(1);
        this.streets[40] = new Street(2);
        this.streets[41] = new Street(5);
    }

    /**
     * Creazione regioni e assegnazione delle regioni alla mappa
     */
    private void createRegions() {

        int j = 0;
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++) {
            this.regions[j] = new Region(RegionType.HILL);
        }
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++) {
            this.regions[j] = new Region(RegionType.COUNTRYSIDE);
        }
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++) {
            this.regions[j] = new Region(RegionType.MOUNTAIN);
        }
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++) {
            this.regions[j] = new Region(RegionType.DESERT);
        }
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++) {
            this.regions[j] = new Region(RegionType.LAKE);
        }
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++) {
            this.regions[j] = new Region(RegionType.PLAIN);
        }
        this.regions[j] = new Region(RegionType.SHEEPSBURG);
    }

    /**
     *
     * @return L'array delle strade nella mappa
     */
    public Street[] getStreets() {
        return (Street[]) streets;
    }

    /**
     *
     * @return L'array delle regioni nella mappa
     */
    public Region[] getRegions() {
        return (Region[]) regions;
    }

    /**
     * Data una regione e una strada attraverso cui passare mi restituisce la
     * regione in cui arrivo.
     *
     * @param startRegion Regione di partenza
     * @param street      Strada attraverso cui passare
     *
     * @return Regione di arrivo
     *
     * @throws RegionNotFoundException Se la reigone di partenza e quella di
     *                                 arrivo non confinano o se non esiste una
     *                                 regione d'arrivo passando per la strada
     *                                 street
     */
    public Region getEndRegion(Region startRegion, Street street) throws
            RegionNotFoundException {
        List<Node> neighbourRegions;

        //se la regione di partenza e la strada confinano
        if (startRegion.isNeighbour(street)) {

            neighbourRegions = street.getNeighbourNodes();
            //per ogni nodo confinante alla strada
            for (Node node : neighbourRegions) {
                //se il nodo non è la regione di partenza e è una ragione
                if ((node instanceof Region) && !node.equals(startRegion)) {
                    //ritornala
                    return (Region) node;
                }
            }
            //se non hai trovato nessun altra regione
            //non dovrebbe succedere mai. Ammenocchè la mappa non si costruita male...
            throw new RegionNotFoundException(
                    "Non è possibile raggiungere nessuna regione con questo percorso");
        } else {
            throw new RegionNotFoundException(
                    "Impossibile procedere, la regione e la strada non confinano");
        }
    }

    /**
     * Dato un nodo della mappa ritorna il suo indice nel relativo array di
     * strade o regioni. Ritorna -1 se non lo trova
     *
     * @param node Regione o strada di cui conoscere l'indice
     *
     * @return L'indice cercato
     *
     * @throws NodeNotFoundException Se la regione o la strada cercate non
     *                               esistono
     */
    public int getNodeIndex(Node node) throws NodeNotFoundException {
        if (node instanceof Street) {
            for (int i = 0; i < streets.length; i++) {
                if (streets[i] == (Street) node) {
                    return i;
                }
            }
        } else {
            //è una regione
            for (int i = 0; i < regions.length; i++) {
                if (regions[i] == (Region) node) {
                    return i;
                }
            }

        }

        throw new NodeNotFoundException("Il nodo cercato non esiste");
    }

    /**
     * It calsulates the number of ovine in a certain region type. Every ovine
     * counts as 1, but the blackSheep count as 2
     *
     * @param type Region type
     *
     * @return number of ovines in that region including the blackSheep
     */
    public int numberOfOvineIn(RegionType type) {
        int numOvines = 0;
        //per ogni regione
        for (Node region : regions) {
            Region r = (Region) region;
            //se del tipo giusto
            if (r.getType() == type) {
                //aggiorna numOvines con num di ovini di quella regione
                numOvines += r.getMyOvines().size();
            }

        }

        //aggiunge +2 per la pecora nera se c'è
        if (blackSheep.getMyRegion().getType() == type) {
            numOvines += GameConstants.BLACKSHEEP_WEIGHT.getValue();
        }
        return numOvines;
    }
    
    /**
     * Checks every region to get all the ovines in the map
     * @return All the ovines int the map
     */
    public List<Ovine> getAllOvines(){
        List<Ovine> ovineList = new ArrayList<Ovine>();
        
        for(Region region : this.getRegions()){                       
            for(Ovine ovine : region.getMyOvines()){
                ovineList.add(ovine);
            }
        }
        
        return ovineList;
    }

}
