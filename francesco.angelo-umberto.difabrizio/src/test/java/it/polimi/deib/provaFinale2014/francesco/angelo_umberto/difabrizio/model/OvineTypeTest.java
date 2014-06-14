package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Umberto
 */
public class OvineTypeTest {

    /**
     * Test of values method, of class OvineType.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        OvineType[] expResult = OvineType.values();
        OvineType[] result = OvineType.values();
        assertArrayEquals(expResult, result);

    }

    /**
     * Test of valueOf method, of class OvineType.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "LAMB";
        OvineType expResult = OvineType.LAMB;
        OvineType result = OvineType.valueOf(name);
        assertEquals(expResult, result);

    }

    /**
     * Test of getDefaultOvineType method, of class OvineType.
     */
    @Test
    public void testGetDefaultOvineType() {
        System.out.println("getDefaultOvineType");
        OvineType expResult = OvineType.SHEEP;
        OvineType result = OvineType.getDefaultOvineType();
        assertEquals(expResult, result);

    }

    /**
     * Test of getRandomOvineType method, of class OvineType.
     */
    @Test
    public void testGetRandomOvineType() {
        System.out.println("getRandomOvineType");

        OvineType result = OvineType.getRandomOvineType();
        assertNotNull(result);

    }

    /**
     * Test of getRandomLambEvolution method, of class OvineType.
     */
    @Test
    public void testGetRandomLambEvolution() {
        System.out.println("getRandomLambEvolution");

        OvineType result = OvineType.getRandomLambEvolution();
        assertTrue(result != OvineType.LAMB);

    }

}
