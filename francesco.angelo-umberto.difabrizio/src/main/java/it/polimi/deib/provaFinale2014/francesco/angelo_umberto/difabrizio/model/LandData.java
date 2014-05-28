package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.Arrays;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class LandData {

    private RegionData regions[];
    private StreetData streets[];

    public LandData() {
        this.regions = new RegionData[GameConstants.NUM_REGIONS.getValue()];
        this.streets = new StreetData[GameConstants.NUM_REGIONS.getValue()];
    }

    public void addRegion(RegionData regionData) {
        for (int i = 0; i < regions.length; i++) {
            if (regions[i] == null) {
                regions[i] = regionData;
            }
        }
    }

    public void addStreet(StreetData streetData) {
        for (int i = 0; i < streets.length; i++) {
            if (streets[i] == null) {
                streets[i] = streetData;
            }
        }
    }

    public void clean() {
        Arrays.fill(regions, null);
        Arrays.fill(streets, null);
    }

}
