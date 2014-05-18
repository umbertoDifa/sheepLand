package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;

public class Map {

    private Node[] streets;
    private Node[] regions;
    private BlackSheep blackSheep;
    private Wolf wolf;

    /**
     * crea mappa e istanzia i due array di nodi, uno per strade, uno per
     * regioni, secondo le dimensioni ufficiali del gioco
     */
    public Map() {
        this.streets = new Street[GameConstants.NUM_STREETS.getValue()];
        this.regions = new Region[GameConstants.NUM_REGIONS.getValue()];
        this.blackSheep = new BlackSheep();
        this.wolf = new Wolf();
    }

    public Street convertStringToStreet(String streetId) {
        return (Street) this.streets[Integer.parseInt(streetId)];
    }

    /**
     * crea nodi, li assegna a map, collega fra di loro i node
     */
    public void setUp() {
        createRegions();
        createStreets();
        createLinks();
    }

    public BlackSheep getBlackSheep() {
        return blackSheep;
    }

    public Wolf getWolf() {
        return wolf;
    }
    /**
     * Ritorna la strada col valore corrispondente confinante con la Region
     * @param region La regione i cui confini devono essere controllati
     * @param value Il valore della strada limitrofa che si cerca
     * @return La strada trovata, null altrimenti
     */
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
     * collega i nodi tra di loro secondo la configurazione della mappa
     * ufficiale
     */
    private void createLinks() {

        //collego regioni a strade con un link bidirezionale
        this.regions[0].connectTo(this.streets[0]);
        this.regions[0].connectTo(this.streets[3]);
        this.regions[0].connectTo(this.streets[10]);
        this.regions[1].connectTo(this.streets[0]);
        this.regions[1].connectTo(this.streets[1]);
        this.regions[1].connectTo(this.streets[4]);
        this.regions[1].connectTo(this.streets[5]);
        this.regions[2].connectTo(this.streets[1]);
        this.regions[2].connectTo(this.streets[2]);
        this.regions[2].connectTo(this.streets[6]);
        this.regions[3].connectTo(this.streets[2]);
        this.regions[3].connectTo(this.streets[7]);
        this.regions[3].connectTo(this.streets[8]);
        this.regions[3].connectTo(this.streets[9]);
        this.regions[4].connectTo(this.streets[9]);
        this.regions[4].connectTo(this.streets[13]);
        this.regions[4].connectTo(this.streets[14]);
        this.regions[5].connectTo(this.streets[10]);
        this.regions[5].connectTo(this.streets[15]);
        this.regions[5].connectTo(this.streets[22]);
        this.regions[5].connectTo(this.streets[26]);
        this.regions[6].connectTo(this.streets[3]);
        this.regions[6].connectTo(this.streets[4]);
        this.regions[6].connectTo(this.streets[11]);
        this.regions[6].connectTo(this.streets[15]);
        this.regions[6].connectTo(this.streets[16]);
        this.regions[6].connectTo(this.streets[20]);
        this.regions[7].connectTo(this.streets[5]);
        this.regions[7].connectTo(this.streets[6]);
        this.regions[7].connectTo(this.streets[7]);
        this.regions[7].connectTo(this.streets[11]);
        this.regions[7].connectTo(this.streets[12]);
        this.regions[7].connectTo(this.streets[17]);
        this.regions[8].connectTo(this.streets[8]);
        this.regions[8].connectTo(this.streets[12]);
        this.regions[8].connectTo(this.streets[13]);
        this.regions[8].connectTo(this.streets[18]);
        this.regions[8].connectTo(this.streets[19]);
        this.regions[8].connectTo(this.streets[21]);
        this.regions[9].connectTo(this.streets[14]);
        this.regions[9].connectTo(this.streets[19]);
        this.regions[9].connectTo(this.streets[25]);
        this.regions[9].connectTo(this.streets[32]);
        this.regions[10].connectTo(this.streets[26]);
        this.regions[10].connectTo(this.streets[27]);
        this.regions[10].connectTo(this.streets[38]);
        this.regions[11].connectTo(this.streets[20]);
        this.regions[11].connectTo(this.streets[22]);
        this.regions[11].connectTo(this.streets[23]);
        this.regions[11].connectTo(this.streets[27]);
        this.regions[11].connectTo(this.streets[28]);
        this.regions[11].connectTo(this.streets[33]);
        this.regions[12].connectTo(this.streets[16]);
        this.regions[12].connectTo(this.streets[17]);
        this.regions[12].connectTo(this.streets[18]);
        this.regions[12].connectTo(this.streets[23]);
        this.regions[12].connectTo(this.streets[24]);
        this.regions[12].connectTo(this.streets[29]);
        this.regions[13].connectTo(this.streets[21]);
        this.regions[13].connectTo(this.streets[24]);
        this.regions[13].connectTo(this.streets[25]);
        this.regions[13].connectTo(this.streets[30]);
        this.regions[13].connectTo(this.streets[31]);
        this.regions[13].connectTo(this.streets[36]);
        this.regions[14].connectTo(this.streets[31]);
        this.regions[14].connectTo(this.streets[32]);
        this.regions[14].connectTo(this.streets[37]);
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
        this.regions[17].connectTo(this.streets[35]);
        this.regions[17].connectTo(this.streets[36]);
        this.regions[17].connectTo(this.streets[37]);
        this.regions[17].connectTo(this.streets[41]);
        this.regions[18].connectTo(this.streets[39]);
        this.regions[18].connectTo(this.streets[40]);
        this.regions[18].connectTo(this.streets[41]);

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
     * creazione strade e assegnazione strade alla mappa
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
     * creazione regioni e assegnazione delle regioni alla mappa
     */
    private void createRegions() {

        int j = 0;
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++)
            this.regions[j] = new Region(RegionType.MOUNTAIN);
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++)
            this.regions[j] = new Region(RegionType.COUNTRYSIDE);
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++)
            this.regions[j] = new Region(RegionType.DESERT);
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++)
            this.regions[j] = new Region(RegionType.HILL);
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++)
            this.regions[j] = new Region(RegionType.LAKE);
        for (int i = 0; i < GameConstants.NUM_REGIONS_FOR_TYPE.getValue(); i++, j++)
            this.regions[j] = new Region(RegionType.PLAIN);
        this.regions[j] = new Region(RegionType.SHEEPSBURG);
    }

    public Street[] getStreets() {
        return (Street[]) streets;
    }

    public Region[] getRegions() {
        return (Region[]) regions;
    }
}
