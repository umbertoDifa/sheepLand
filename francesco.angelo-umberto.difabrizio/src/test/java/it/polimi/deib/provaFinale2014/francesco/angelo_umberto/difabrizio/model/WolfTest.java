
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

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
public class WolfTest {//TODO: tutti test da fare
    
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of moveThrough method, of class Wolf.
     */
    @Ignore
    @Test
    public void testMoveThrough() throws Exception {
        System.out.println("moveThrough");
        Street street = null;
        Region endRegion = null;
        Wolf instance = new Wolf();
        instance.moveThrough(street, endRegion);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Wolf.
     */
    @Ignore
    @Test
    public void testToString() {
        System.out.println("toString");
        Wolf instance = new Wolf();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
