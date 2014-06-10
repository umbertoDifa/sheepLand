
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Umberto
 */
public class RegionTypeTest {

    /**
     * Test of values method, of class RegionType.
     */    
    @Test
    public void testValues() {        
        RegionType[] result = RegionType.values();
        assertEquals(7, result.length);
     
    }

    /**
     * Test of valueOf method, of class RegionType.
     */
    
    @Test
    public void testValueOf() {
        RegionType expResult = RegionType.COUNTRYSIDE;
        RegionType result = RegionType.valueOf("COUNTRYSIDE");
        assertEquals(expResult, result);
       
    }

    /**
     * Test of getDefaultRegionType method, of class RegionType.
     */    
    @Test
    public void testGetDefaultRegionType() {
        System.out.println("getDefaultRegionType");
        RegionType expResult = RegionType.MOUNTAIN;
        RegionType result = RegionType.getDefaultRegionType();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getIndex method, of class RegionType.
     */    
    @Test
    public void testGetIndex() {
        System.out.println("getIndex");
        RegionType instance = RegionType.DESERT;
        int expResult = 5;
        int result = instance.getIndex();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getRandomRegionType method, of class RegionType.
     */
  
    @Test
    public void testGetRandomRegionType() {
        System.out.println("getRandomRegionType");
       boolean outcome = false;
        RegionType result = RegionType.getRandomRegionType();
       
        for(RegionType type : RegionType.values()){
            if (type == result){
                outcome = true;
                break;
            }
        }              
        if(!outcome){
            fail("Il tipo di regione non esiste nell'enum");
        }
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
