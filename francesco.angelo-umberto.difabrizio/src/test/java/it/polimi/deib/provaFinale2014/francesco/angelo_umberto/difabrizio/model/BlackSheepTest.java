package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.CannotMoveBlackSheepException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Umberto
 */
public class BlackSheepTest {

    Map map;

    public BlackSheepTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        map = new Map();
        map.setUp();
    }

    @After
    public void tearDown() {
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
        try {
            map.getBlackSheep().moveThrough(map.convertStringToStreet("14"),
                    map.convertStringToRegion("10"));
            fail("La pecora nera non può attraversare cancelli");
        } catch (CannotMoveBlackSheepException e) {
            //tutto ok
        }

        map.getBlackSheep().moveThrough(map.convertStringToStreet("9"),
                map.convertStringToRegion("7"));

        //setto un pastore
        map.getStreets()[2].setShepherd(new Shepherd());

        try {
            map.getBlackSheep().moveThrough(map.convertStringToStreet("2"),
                    map.convertStringToRegion("4"));
            fail("La pecora nera non può attraversare pastori");
        } catch (CannotMoveBlackSheepException e) {
            //tutto ok
        }

        assertSame(map.getBlackSheep().getMyRegion(), map.convertStringToRegion(
                "7"));

    }

}
