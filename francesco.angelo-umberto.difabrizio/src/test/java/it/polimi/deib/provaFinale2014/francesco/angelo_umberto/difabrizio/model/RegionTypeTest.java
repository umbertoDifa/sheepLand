
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class RegionTypeTest {
    
    public RegionTypeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of values method, of class RegionType.
     */
    @Ignore
    @Test
    public void testValues() {
        System.out.println("values");
        RegionType[] expResult = null;
        RegionType[] result = RegionType.values();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of valueOf method, of class RegionType.
     */
    @Ignore
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "";
        RegionType expResult = null;
        RegionType result = RegionType.valueOf(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDefaultRegionType method, of class RegionType.
     */
    @Ignore
    @Test
    public void testGetDefaultRegionType() {
        System.out.println("getDefaultRegionType");
        RegionType expResult = null;
        RegionType result = RegionType.getDefaultRegionType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIndex method, of class RegionType.
     */
    @Ignore
    @Test
    public void testGetIndex() {
        System.out.println("getIndex");
        RegionType instance = null;
        int expResult = 0;
        int result = instance.getIndex();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRandomRegionType method, of class RegionType.
     */
    @Ignore
    @Test
    public void testGetRandomRegionType() {
        System.out.println("getRandomRegionType");
        RegionType expResult = null;
        RegionType result = RegionType.getRandomRegionType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Testa la conversione dei parametri dell'enum in stringa
     */
    @Test
    public void testToString(){
        
        List<RegionType> types = new ArrayList<RegionType>();
        
        types.addAll(Arrays.asList(RegionType.values()));
        
        System.err.println(types);
    }
}
