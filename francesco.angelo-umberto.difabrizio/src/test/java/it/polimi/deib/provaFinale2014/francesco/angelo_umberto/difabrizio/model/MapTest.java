package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NodeNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco
 */
public class MapTest {

    private Map map;

    /**
     * Creates a map and sets it up
     */
    @Before
    public void setUp() {
        map = new Map();
        map.setUp();
    }

    /**
     * Test of setUp method, of class Map.
     */
    @Test
    public void testSetUp() {
        System.out.println("setUp");
        //testo connessione tra regioni e strade
        assertTrue(map.getRegions()[1].getNeighbourNodes().contains(
                map.getStreets()[10]));
        assertTrue(map.getRegions()[7].getNeighbourNodes().contains(
                map.getStreets()[7]));
        assertFalse(map.getRegions()[10].getNeighbourNodes().contains(
                map.getStreets()[27]));
        assertFalse(map.getRegions()[18].getNeighbourNodes().contains(
                map.getStreets()[41]));
        assertTrue(map.getRegions()[14].getNeighbourNodes().contains(
                map.getStreets()[39]));
        assertFalse(map.getRegions()[8].getNeighbourNodes().contains(
                map.getStreets()[12]));

        //testo connessione tra strade
        assertTrue(map.getStreets()[0].getNeighbourNodes().contains(
                map.getRegions()[3]));
        assertTrue(map.getStreets()[7].getNeighbourNodes().contains(
                map.getStreets()[6]));
        assertFalse(map.getStreets()[34].getNeighbourNodes().contains(
                map.getStreets()[6]));

    }

    /**
     * Test of convertStringToStreet method, of class Map.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testConvertStringToStreet() throws Exception {
        System.out.println("convertStringToStreet");

        int exCount;

        assertTrue(map.convertStringToStreet("0").getValue() == 2);
        assertTrue(map.convertStringToStreet("3").getValue() == 3);
        assertTrue(map.convertStringToStreet("18").getValue() == 2);
        assertTrue(map.convertStringToStreet("15").getValue() == 2);
        assertTrue(map.convertStringToStreet("22").getValue() == 3);
        assertTrue(map.convertStringToStreet("16").getValue() == 6);
        assertTrue(map.convertStringToStreet("39").getValue() == 1);
        assertTrue(map.convertStringToStreet("41").getValue() == 5);

        exCount = 0;
        try {
            map.convertStringToStreet("42");
        } catch (StreetNotFoundException e) {
            exCount++;
            DebugLogger.println(e.getMessage());
        }

        assertTrue(exCount == 1);

        exCount = 0;
        try {
            map.convertStringToStreet("s42");
        } catch (StreetNotFoundException e) {
            exCount++;
            DebugLogger.println(e.getMessage());

        }

        assertTrue(exCount == 1);

        exCount = 0;
        try {
            map.convertStringToStreet("432");
        } catch (StreetNotFoundException e) {
            exCount++;
            DebugLogger.println(e.getMessage());

        }

        assertTrue(exCount == 1);

        exCount = 0;
        try {
            map.convertStringToStreet("-42");
        } catch (StreetNotFoundException e) {
            exCount++;
            DebugLogger.println(e.getMessage());
        }

        assertTrue(exCount == 1);

        exCount = 0;
        try {
            map.convertStringToStreet("2e");
        } catch (StreetNotFoundException e) {
            exCount++;
            DebugLogger.println(e.getMessage());
        }

        assertTrue(exCount == 1);

    }

    /**
     * Test of convertStringToRegion method, of class Map.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testConvertStringToRegion() throws Exception {
        System.out.println("convertStringToRegion");
        int exCount;
        try {
            map.convertStringToRegion("0");
            map.convertStringToRegion("18");
            map.convertStringToRegion("3");
            map.convertStringToRegion("4");
            map.convertStringToRegion("9");
            map.convertStringToRegion("11");
            map.convertStringToRegion("0");
            map.convertStringToRegion("15");
        } catch (RegionNotFoundException e) {
            fail("Queste strade esistono, eccezione sbagliata");
        }

        exCount = 0;
        try {
            map.convertStringToRegion("19");
        } catch (RegionNotFoundException e) {
            exCount++;
            DebugLogger.println(e.getMessage());
        }

        assertTrue(exCount == 1);

        exCount = 0;
        try {
            map.convertStringToRegion("s42");
        } catch (RegionNotFoundException e) {
            exCount++;
            DebugLogger.println(e.getMessage());

        }

        assertTrue(exCount == 1);

        exCount = 0;
        try {
            map.convertStringToRegion("432");
        } catch (RegionNotFoundException e) {
            exCount++;
            DebugLogger.println(e.getMessage());

        }

        assertTrue(exCount == 1);

        exCount = 0;
        try {
            map.convertStringToRegion("-42");
        } catch (RegionNotFoundException e) {
            exCount++;
            DebugLogger.println(e.getMessage());
        }

        assertTrue(exCount == 1);

        exCount = 0;
        try {
            map.convertStringToRegion("2e");
        } catch (RegionNotFoundException e) {
            exCount++;
            DebugLogger.println(e.getMessage());
        }

        assertTrue(exCount == 1);

    }

    /**
     * Test of getBlackSheep method, of class Map.
     */
    @Test
    public void testGetBlackSheep() {
        System.out.println("getBlackSheep");
        map.getBlackSheep();
    }

    /**
     * Test of getWolf method, of class Map. Metodo testato indirettamnente
     * nelle altre classi(WolfTest)
     */
    @Test
    public void testGetWolf() {
        System.out.println("getWolf");
    }

    /**
     * Test of getStreetByValue method, of class Map.
     *
     * @throws java.lang.Exception
     */
    @Test(expected = StreetNotFoundException.class)
    public void testGetStreetByValue() throws Exception {
        System.out.println("getStreetByValue");

        assertSame(map.getStreetByValue(map.convertStringToRegion("0"), 1),
                map.convertStringToStreet("10"));
        assertSame(map.getStreetByValue(map.convertStringToRegion("0"), 2),
                map.convertStringToStreet("0"));
        assertSame(map.getStreetByValue(map.convertStringToRegion("0"), 3),
                map.convertStringToStreet("3"));

        map.getStreetByValue(map.convertStringToRegion("0"), 4);
    }

    /**
     * Test of getStreets method, of class Map.
     *
     * @throws StreetNotFoundException
     */
    @Test
    public void testGetStreets() throws StreetNotFoundException {
        System.out.println("getStreets");
        assertSame(map.getStreets()[3], map.convertStringToStreet("3"));
    }

    /**
     * Test of getRegions method, of class Map.
     *
     * @throws RegionNotFoundException
     */
    @Test
    public void testGetRegions() throws RegionNotFoundException {
        System.out.println("getRegions");

        assertSame(map.getRegions()[11], map.convertStringToRegion("11"));
    }

    /**
     * Test of getEndRegion method, of class Map.
     *
     * @throws RegionNotFoundException
     * @throws StreetNotFoundException
     */
    @Test
    public void testGetEndRegion() throws RegionNotFoundException,
                                          StreetNotFoundException {
        int exCount;

        System.out.println("getEndRegion");
        assertSame(map.getEndRegion(map.convertStringToRegion("5"),
                map.convertStringToStreet("15")), map.convertStringToRegion("1"));

        assertSame(map.getEndRegion(map.convertStringToRegion("9"),
                map.convertStringToStreet("19")),
                map.convertStringToRegion("10"));

        assertSame(map.getEndRegion(map.convertStringToRegion("16"),
                map.convertStringToStreet("30")),
                map.convertStringToRegion("12"));

        assertSame(map.getEndRegion(map.convertStringToRegion("4"),
                map.convertStringToStreet("2")), map.convertStringToRegion("7"));

        //cerco di andare in regioni impossibili
        exCount = 0;
        try {
            assertSame(map.getEndRegion(map.convertStringToRegion("17"),
                    map.convertStringToStreet("5")), null);
        } catch (RegionNotFoundException e) {
            exCount++;
        }
        try {
            assertSame(map.getEndRegion(map.convertStringToRegion("10"),
                    map.convertStringToStreet("11")), null);
        } catch (RegionNotFoundException e) {
            exCount++;
        }
        try {
            assertSame(map.getEndRegion(map.convertStringToRegion("3"),
                    map.convertStringToStreet("33")), null);
        } catch (RegionNotFoundException e) {
            exCount++;
        }

        assertTrue(exCount == 3);

        assertSame(map.getEndRegion(map.convertStringToRegion("3"),
                map.convertStringToStreet("5")), map.convertStringToRegion("6"));

    }

    /**
     * Test of getNodeIndex method, of class Map.
     *
     * @throws NodeNotFoundException
     */
    @Test
    public void testGetNodeIndex() throws NodeNotFoundException {
        System.out.println("getNodeIndex");

        assertTrue(map.getNodeIndex(map.getRegions()[12]) == 12);
        assertTrue(map.getNodeIndex(map.getRegions()[1]) == 1);
        assertTrue(map.getNodeIndex(map.getRegions()[18]) == 18);

        assertTrue(map.getNodeIndex(map.getStreets()[12]) == 12);
        assertTrue(map.getNodeIndex(map.getStreets()[1]) == 1);
        assertTrue(map.getNodeIndex(map.getStreets()[0]) == 0);
    }

    /**
     * Test of numberOfOvineIn method, of class Map.
     */
    @Test
    public void testNumberOfOvineIn() throws RegionNotFoundException {
        System.out.println("numberOfOvineIn");
        RegionType type = RegionType.COUNTRYSIDE;
        Map instance = new Map();
        instance.setUp();
        instance.getBlackSheep().setAt(instance.convertStringToRegion("10"));
        int expResult = 0;
        int result = instance.numberOfOvineIn(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllOvines method, of class Map.
     */
    @Test
    public void testGetAllOvines() {
        System.out.println("getAllOvines");
        Map instance = new Map();
        instance.setUp();
        List<Ovine> result = instance.getAllOvines();
        assertEquals(0, result.size());

    }

}
