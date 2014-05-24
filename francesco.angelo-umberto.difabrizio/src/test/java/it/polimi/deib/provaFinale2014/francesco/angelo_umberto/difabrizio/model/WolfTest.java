package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.CannotMoveWolfException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Umberto
 */
public class WolfTest {

    Map map = new Map();
    Wolf wolf;

    public WolfTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        int SHEEPSBURG_ID = 18;
        //creo una mappa 
        map.setUp();

        //prendo il lupo della mappa
        wolf = map.getWolf();

        //recupera l'array delle regioni
        Region[] region = this.map.getRegions();

        //per ogni regione tranne shepsburg
        for (int i = 0; i < region.length - 1; i++) {
            //aggiungi un ovino (a caso)          
            region[i].addOvine(new Ovine());
        }
        //posiziono lupo e pecora nera a sheepsburg

        map.getBlackSheep().setAt(map.getRegions()[SHEEPSBURG_ID]);
        map.getWolf().setAt(map.getRegions()[SHEEPSBURG_ID]);

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of moveThrough method, of class Wolf.
     *
     * @throws java.lang.Exception
     */
   
    @Test
    public void testMoveThrough() throws Exception {
        DebugLogger.println("moveThrough");

        //primo spostamento del lupo
        DebugLogger.println("In 6 c'è " + map.getRegions()[6].getMyOvines().get(
                0).getType());
        //muovo il lupo
        wolf.moveThrough(map.convertStringToStreet("17"),
                map.convertStringToRegion("6"));

        //testo
        assertSame(wolf.getMyRegion(), map.convertStringToRegion("6"));
        assertSame(wolf.getMyRegion(), map.getRegions()[6]);
        try {
            DebugLogger.println(
                    "In 6 c'è " + map.getRegions()[6].getMyOvines().get(
                            0).getType());
        } catch (IndexOutOfBoundsException e) {
            DebugLogger.println("Il lupo ha mangiato l'unico animale che c'era");
        }

        //Secondo spostamento
        DebugLogger.println("In 5 c'è " + map.getRegions()[5].getMyOvines().get(
                0).getType());
        //muovo il lupo
        wolf.moveThrough(map.convertStringToStreet("11"),
                map.convertStringToRegion("5"));

        //testo
        assertSame(wolf.getMyRegion(), map.convertStringToRegion("5"));
        assertSame(wolf.getMyRegion(), map.getRegions()[5]);
        try {
            DebugLogger.println(
                    "In 5 c'è " + map.getRegions()[5].getMyOvines().get(
                            0).getType());
        } catch (IndexOutOfBoundsException e) {
            DebugLogger.println("Il lupo ha mangiato l'unico animale che c'era");
        }

        //Terzo spostamento
        try {
            DebugLogger.println(
                    "In 18 c'è " + map.getRegions()[18].getMyOvines().get(
                            0).getType());
        } catch (IndexOutOfBoundsException e) {
            DebugLogger.println("Non esistono ovini qui");

        }
        //muovo il lupo
        wolf.moveThrough(map.convertStringToStreet("16"),
                map.convertStringToRegion("18"));

        //testo
        assertSame(wolf.getMyRegion(), map.convertStringToRegion("18"));
        assertSame(wolf.getMyRegion(), map.getRegions()[18]);
        try {
            DebugLogger.println(
                    "In 18 c'è " + map.getRegions()[18].getMyOvines().get(
                            0).getType());
        } catch (IndexOutOfBoundsException e) {
            DebugLogger.println(
                    "Il lupo non può mangiare niente a shepsburg ora");
        }
    }

    @Test
    public void testMoveThrough2() throws Exception {
        //aggiungo piò ovini ad ogni regione
        Region[] region = this.map.getRegions();
        int i, j;
        int exCount = 0;
        
        int numberOfOvineForRegion = 1;
        //per ogni regione tranne shepsburg
        for (j = 0; j < numberOfOvineForRegion; j++) {
            for (i = 0; i < region.length - 1; i++) {
                //aggiungi un ovino (a caso)          
                region[i].addOvine(new Ovine());
            }
        }

        //faccio un percorso 18 -2 -15 -17         
        DebugLogger.println("Controllo iniziale");
        for (i = 0; i < numberOfOvineForRegion + 1; i++) {
            try {
                DebugLogger.println(
                        "In 18 c'è " + map.getRegions()[18].getMyOvines().get(
                                i).getType());
            } catch (IndexOutOfBoundsException e) {
                DebugLogger.println("Nessun animale in 18");
                break;
            }
        }
        for (i = 0; i < numberOfOvineForRegion + 1; i++) {
            try {
                DebugLogger.println(
                        "In 2 c'è " + map.getRegions()[2].getMyOvines().get(
                                i).getType());
            } catch (IndexOutOfBoundsException e) {
                DebugLogger.println("Nessun animale in 2");
                break;
            }
        }
        for (i = 0; i < numberOfOvineForRegion + 1; i++) {
            try {
                DebugLogger.println(
                        "In 15 c'è " + map.getRegions()[15].getMyOvines().get(
                                i).getType());
            } catch (IndexOutOfBoundsException e) {
                DebugLogger.println("Nessun animale in 15");
                break;
            }
        }
        for (i = 0; i < numberOfOvineForRegion + 1; i++) {
            try {
                DebugLogger.println(
                        "In 17 c'è " + map.getRegions()[17].getMyOvines().get(
                                i).getType());
            } catch (IndexOutOfBoundsException e) {
                DebugLogger.println("Nessun animale in 17");
                break;
            }
        }

        wolf.moveThrough(map.convertStringToStreet("23"),
                map.convertStringToRegion("2"));
        wolf.moveThrough(map.convertStringToStreet("33"),
                map.convertStringToRegion("15"));
        wolf.moveThrough(map.convertStringToStreet("38"),
                map.convertStringToRegion("17"));

        wolf.moveThrough(map.convertStringToStreet("38"),
                map.convertStringToRegion("15"));
        wolf.moveThrough(map.convertStringToStreet("33"),
                map.convertStringToRegion("2"));
        wolf.moveThrough(map.convertStringToStreet("23"),
                map.convertStringToRegion("18"));

        DebugLogger.println("Controllo finale");
        for (i = 0; i < numberOfOvineForRegion+1; i++) {
            try {
                DebugLogger.println(
                        "In 18 c'è " + map.getRegions()[18].getMyOvines().get(
                                i).getType());
            } catch (IndexOutOfBoundsException e) {
                DebugLogger.println("Nessun animale in 18");
                break;
            }
        }
        for (i = 0; i < numberOfOvineForRegion+1; i++) {
            try {
                DebugLogger.println(
                        "In 2 c'è " + map.getRegions()[2].getMyOvines().get(
                                i).getType());
            } catch (IndexOutOfBoundsException e) {
                DebugLogger.println("Nessun animale in 2");
                break;
            }
        }
        for (i = 0; i < numberOfOvineForRegion+1; i++) {
            try {
                DebugLogger.println(
                        "In 15 c'è " + map.getRegions()[15].getMyOvines().get(
                                i).getType());
            } catch (IndexOutOfBoundsException e) {
                DebugLogger.println("Nessun animale in 15");
                break;
            }
        }
        for (i = 0; i < numberOfOvineForRegion+1; i++) {
            try {
                DebugLogger.println(
                        "In 17 c'è " + map.getRegions()[17].getMyOvines().get(
                                i).getType());
            } catch (IndexOutOfBoundsException e) {
                DebugLogger.println("Nessun animale in 17");
                break;
            }
        }

        //il lupo è dinuovo in 18
        //aggiungo fence a tutte le strade pari
        for (i = 0; i < map.getStreets().length;) {
            map.getStreets()[i].setFence(new Fence(false));
            i += 2;
        }
        //test
        try {
            wolf.moveThrough(map.convertStringToStreet("16"),
                    map.convertStringToRegion("5"));
        } catch (CannotMoveWolfException e) {
            exCount++;
        }
        try {
            wolf.moveThrough(map.convertStringToStreet("18"),
                    map.convertStringToRegion("9"));
        } catch (CannotMoveWolfException e) {
            exCount++;
        }
        try {
            wolf.moveThrough(map.convertStringToStreet("24"),
                    map.convertStringToRegion("12"));
        } catch (CannotMoveWolfException e) {
            exCount++;
        }

        assertTrue(exCount == 3);

        //rinchiudo il lupo
        DebugLogger.println("Muovo su regione 6");
        wolf.moveThrough(map.convertStringToStreet("17"),
                map.convertStringToRegion("6"));

        map.getStreets()[17].setFence(new Fence(false));
        map.getStreets()[11].setFence(new Fence(false));
        map.getStreets()[5].setFence(new Fence(false));
        map.getStreets()[6].setFence(new Fence(false));
        map.getStreets()[7].setFence(new Fence(false));
        map.getStreets()[12].setFence(new Fence(false));
        //test

        exCount = 0;
        try {
            DebugLogger.println("Muovo su regione 18");

            wolf.moveThrough(map.convertStringToStreet("17"),
                    map.convertStringToRegion("18"));
        } catch (CannotMoveWolfException e) {
            exCount++;
        }

        assertTrue(exCount == 0);

        //cerco di tornare indietro ma fallisco
        exCount = 0;
        try {
            DebugLogger.println("Muovo su regione 6");
            wolf.moveThrough(map.convertStringToStreet("17"),
                    map.convertStringToRegion("6"));
        } catch (CannotMoveWolfException e) {
            exCount++;
        }

        assertTrue(exCount == 1);
    }

}
