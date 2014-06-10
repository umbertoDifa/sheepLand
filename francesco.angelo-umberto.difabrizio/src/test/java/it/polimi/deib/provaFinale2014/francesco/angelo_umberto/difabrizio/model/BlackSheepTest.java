package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Umberto
 */
public class BlackSheepTest {
    
    Map map;
    /**
     * Creates a new map
     */
    @Before
    public void setUp() {
        map = new Map();
        map.setUp();
    } 

    /**
     * Test of moveThrough method, of class BlackSheep.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testMoveThrough() throws Exception {
        System.out.println("moveThrough");

        //metto la pecora a shepsburg
        map.getBlackSheep().setAt(map.convertStringToRegion("18"));

        //la sposto
        map.getBlackSheep().moveThrough(map.convertStringToStreet("18"),
                map.convertStringToRegion("9"));

        //testo
        assertSame(map.getBlackSheep().getMyRegion(), map.convertStringToRegion(
                "9"));

        //la sposto
        map.getBlackSheep().moveThrough(map.convertStringToStreet("8"),
                map.convertStringToRegion("7"));

        //testo
        assertSame(map.getBlackSheep().getMyRegion(), map.convertStringToRegion(
                "7"));

        //la sposto
        map.getBlackSheep().moveThrough(map.convertStringToStreet("9"),
                map.convertStringToRegion("8"));

        //testo
        assertSame(map.getBlackSheep().getMyRegion(), map.convertStringToRegion(
                "8"));

        //metto delle fence       
        map.getStreets()[14].setFence(new Fence(false));
        
       
        
        map.getBlackSheep().moveThrough(map.convertStringToStreet("9"),
                map.convertStringToRegion("7"));

        //setto un pastore
        map.getStreets()[2].setShepherd(new Shepherd(
                GameConstants.STANDARD_WALLET_AMMOUNT.getValue()));
        
        
        
        assertSame(map.getBlackSheep().getMyRegion(), map.convertStringToRegion(
                "7"));
        
    }
    
}
