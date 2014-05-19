package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import java.util.ArrayList;
/**
 * Rappresenta la plancia, quindi conosce Strade e Regioni e come esse sono connesse.
 * Crea un grafo che connette Strade e Regioni. Conosce la posizione del lupo 
 * e della pecora nera.
 * E' fonadamentale considerare che l'array delle regioni è ordinato in base al tipo
 * di regione per permettere una ricerca attraverso indici. Ogni tipo di regione 
 * ha il suo spazio nell'array in cui inserire le proprie regioni.
 * E' fondamentale che l'indice dell'array delle strade corrisponde all'id di quella 
 * precisa strada nella mappa, secondo la convenzione documentata.
 * @author Umberto
 */
public class Map {

    private Node[] streets;
    private Node[] regions;
    private BlackSheep blackSheep;
    private Wolf wolf;

    /**
     * Crea mappa e istanzia la grandezza dei due array di nodi, uno per le strade, uno per
     * le regioni, secondo le dimensioni ufficiali del gioco. Instanzia anche
     * una pecora nera e un lupo
     */
    public Map() {
        this.streets = new Street[GameConstants.NUM_STREETS.getValue()];
        this.regions = new Region[GameConstants.NUM_REGIONS.getValue()];
        this.blackSheep = new BlackSheep();
        this.wolf = new Wolf();
    }

    //TODO: compattare convertStringToStreet e convertStringTo Region in un metodo di Node?
    public Street convertStringToStreet(String streetId) throws StreetNotFoundException {
        int id = Integer.parseInt(streetId);
        if (id >= 0 && id < GameConstants.NUM_STREETS.getValue() &&
                this.streets[Integer.parseInt(streetId)] != null) {//se l'id è nel range dell'array e quello che c'è dentro non è null
            return (Street) this.streets[id];
        } else {
            throw new StreetNotFoundException("Strada non esistente");
        }
    }

    public Region convertStringToRegion(String regionId) throws RegionNotFoundException {
        int id = Integer.parseInt(regionId);
        if (id >= 0 && id < GameConstants.NUM_REGIONS.getValue()) {
            return (Region) this.regions[id];
        } else {
            throw new RegionNotFoundException("Regione non esistente");
        }
    }

    /**
     * Crea nodi delle regioni e delle strade, li assegna a map,  e li
     * collega fra di loro 
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
     * Data una region e un value ritorna la strada limitrofa alla regione
     * con quel preciso valore
     * @param region La regione i cui confini devono essere controllati
     * @param value Il valore della strada limitrofa che si cerca
     * @return La strada trovata, null altrimenti
     */
    //TODO invece di ritornare null forse StreetNotFoundException? controlla gli usage
    public Street getStreetByValue(Region region, int value) {
        //salvo i nodi adiacenti alla regione
        ArrayList<Node> adjacentStreet = region.getNeighbourNodes();
        for (int i = 0; i < adjacentStreet.size(); i++) { //per ogni nodo            
            if (adjacentStreet.get(i) instanceof Street) { //se il nodo è una strada
                Street tmpStreet = (Street) adjacentStreet.get(i);  //castalo a street
                if (tmpStreet.getValue() == value)//se il valore è uguale a value
                {
                    return tmpStreet;//ritornala            
                }
            }
        }
        //se non hai trovato nessuna strada limitrofa con quel valore
        return null;
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
     * regione in cui arrivo
     *
     * @param startRegion Regione di partenza
     * @param street Strada attraverso cui passare
     * @return Regione di arrivo
     */
    public Region getEndRegion(Region startRegion, Street street) {
        ArrayList<Node> neighbourRegions;

        neighbourRegions = street.getNeighbourNodes();
        for (Node node : neighbourRegions) {           //per ogni nodo confinante alla strada
            if (!node.equals(startRegion) && (node instanceof Region)) {   //
                return (Region) node;
            }
        }
        //se non hai trovato nessun altra regione
        return null; //TODO: non dovrebbe succedere mai giusto? Ammenocchè la mappa non si costruita male...
    }
}
