
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

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
public class FenceTest {
    
    public FenceTest() {
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
     * Test of isFinal method, of class Fence.
     */
    @Test
    public void testIsFinal() {
        System.out.println("isFinal");
        
        //creo un recinto finale e uno no
        Fence instance = new Fence(true);
        Fence instance2 = new Fence(false);
        
       
        assertTrue(instance.isFinal());
        assertFalse(instance2.isFinal());
        
    }
    
}
