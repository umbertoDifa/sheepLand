package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Umberto
 */
public class SpecialAnimalTest {

    /**
     * Test of setAt method, of class SpecialAnimal.
     *
     * @throws
     * it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException
     */
    @Test
    public void testSetAt() throws RegionNotFoundException {
        Map map = new Map();
        map.setUp();
        System.out.println("setAt");
        Region region = map.convertStringToRegion("10");
        SpecialAnimal instance = new BlackSheep();
        instance.setAt(region);

    }

    /**
     * Test of moveThrough method, of class SpecialAnimal.
     *
     * @throws
     * it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException
     * @throws
     * it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException
     */
    @Test
    public void testMoveThrough() throws StreetNotFoundException,
                                         RegionNotFoundException {
        Map map = new Map();
        map.setUp();
        System.out.println("moveThrough");
        map.getBlackSheep().setAt(map.convertStringToRegion("3"));
        Street street = map.convertStringToStreet("1");
        Region endRegion = map.convertStringToRegion("4");

        SpecialAnimal instance = new BlackSheep();

        String result = instance.moveThrough(street, endRegion);
        assertNotNull(result);

    }

    /**
     * Test of getMyRegion method, of class SpecialAnimal.
     *
     * @throws
     * it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException
     */
    @Test
    public void testGetMyRegion() throws RegionNotFoundException {
        Map map = new Map();
        map.setUp();
        map.getBlackSheep().setAt(map.convertStringToRegion("1"));
        System.out.println("getMyRegion");
        SpecialAnimal instance = map.getBlackSheep();
        Region expResult = map.convertStringToRegion("1");
        Region result = instance.getMyRegion();
        
        assertEquals(expResult, result);

    }

    /**
     * Test of setMyRegion method, of class SpecialAnimal.
     * @throws it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException
     */
    @Test
    public void testSetMyRegion() throws RegionNotFoundException {
        Map map = new Map();
        map.setUp();
        System.out.println("setMyRegion");
        Region myRegion = map.convertStringToRegion("10");
        SpecialAnimal instance = map.getBlackSheep();
        instance.setMyRegion(myRegion);

    }   

}
