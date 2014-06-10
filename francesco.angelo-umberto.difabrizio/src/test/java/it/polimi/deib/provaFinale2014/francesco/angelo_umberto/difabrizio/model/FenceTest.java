
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Umberto
 */
public class FenceTest {

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
