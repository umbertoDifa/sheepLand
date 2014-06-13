
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Umberto
 */
public class GameConstantsTest {

    /**
     * Test of values method, of class GameConstants.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        GameConstants[] expResult = GameConstants.values();
        GameConstants[] result = GameConstants.values();
        assertArrayEquals(expResult, result);
       
    }

    /**
     * Test of valueOf method, of class GameConstants.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "NUM_CARDS";
        GameConstants expResult = GameConstants.NUM_CARDS;
        GameConstants result = GameConstants.valueOf(name);
        assertEquals(expResult, result);
      
    }

    /**
     * Test of getValue method, of class GameConstants.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        GameConstants instance = GameConstants.BLACKSHEEP_WEIGHT;
        int expResult = 2;
        int result = instance.getValue();
        
    }
    
}
