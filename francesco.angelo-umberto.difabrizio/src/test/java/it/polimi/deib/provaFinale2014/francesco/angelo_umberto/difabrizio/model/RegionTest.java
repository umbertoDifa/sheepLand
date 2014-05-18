
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco
 */
public class RegionTest {
    
    public RegionTest() {
    }

    /**
     * Test of getMyOvines method, of class Region.
     */
    @Test
    public void testGetMyOvines() {
        System.out.println("getMyOvines");
        Region instance = new Region();
        Ovine ovine = new Ovine();
        instance.addOvine(ovine);
        ArrayList<Ovine> result = instance.getMyOvines();
        assertSame(ovine, result.get(0));
    }
    
}
