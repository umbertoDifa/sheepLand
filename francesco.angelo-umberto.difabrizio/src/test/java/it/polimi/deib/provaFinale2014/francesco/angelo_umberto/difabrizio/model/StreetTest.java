
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
public class StreetTest {
    
    public StreetTest() {
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
     * Test of hasFence method, of class Street.
     */
    @Ignore
    @Test
    public void testHasFence() {
        System.out.println("hasFence");
        Street instance = null;
        boolean expResult = false;
        boolean result = instance.hasFence();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test per chiarire le modalità e proprietà del casting
     */
    @Test
    public void testCast(){
        //creo un nodo con tipo dinamico strada
        Node nodoStrada = new Street(1);
        
        //lo casto a strada
        Street street = (Street) nodoStrada;
        
        //aggiungo una fence alla strada
        street.setFence(new Fence(false));
        
        //ricasto il nodo su un'altra strada
        Street secondStreet = (Street) nodoStrada;
        //la seconda strada ha un recinto?
        assertTrue(secondStreet.hasFence());
        //la prima strada è uguale alla secnoda?
        assertEquals(street, secondStreet);
        //la prima strada ha lo stesso indirizzo della secnoda?
        assertSame(street, secondStreet);
        //l'indirizzo del nodo è quello della strada?
        assertSame(nodoStrada,street);
    }
    /**
     * Test of hasShepherd method, of class Street.
     */
    @Ignore
    @Test
    public void testHasShepherd() {
        System.out.println("hasShepherd");
        Street instance = null;
        boolean expResult = false;
        boolean result = instance.hasShepherd();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFence method, of class Street.
     */
    @Ignore
    @Test
    public void testSetFence() {
        System.out.println("setFence");
        Fence fence = null;
        Street instance = null;
        instance.setFence(fence);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isFree method, of class Street.
     */
    @Ignore
    @Test
    public void testIsFree() {
        System.out.println("isFree");
        Street instance = null;
        boolean expResult = false;
        boolean result = instance.isFree();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValue method, of class Street.
     */
    @Ignore
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Street instance = null;
        int expResult = 0;
        int result = instance.getValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValue method, of class Street.
     */
    @Ignore
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        int value = 0;
        Street instance = null;
        instance.setValue(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isShepherdThere method, of class Street.
     */
    @Ignore
    @Test
    public void testIsShepherdThere() {
        System.out.println("isShepherdThere");
        Shepherd shepherd = null;
        Street instance = null;
        boolean expResult = false;
        boolean result = instance.isShepherdThere(shepherd);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
