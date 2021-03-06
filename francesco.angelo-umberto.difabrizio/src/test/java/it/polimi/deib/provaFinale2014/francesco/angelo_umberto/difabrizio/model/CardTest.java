package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Umberto
 */
public class CardTest {

    /**
     * Test of getType method, of class Card.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        Card instance = new Card(4, RegionType.HILL);
        RegionType expResult = RegionType.HILL;
        RegionType result = instance.getType();
        assertEquals(expResult, result);

    }

    /**
     * Test of getValue method, of class Card.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Card instance = new Card(2, RegionType.COUNTRYSIDE);
        int expResult = 2;
        int result = instance.getValue();
        assertEquals(expResult, result);

    }

    /**
     * Test of setValue method, of class Card.
     */
    @Test
    public void testSetValue() {
        System.out.println("setValue");
        int value = 3;
        Card instance = new Card(1, RegionType.DESERT);
        instance.setValue(value);
        assertTrue(instance.getValue() == value);
    }

    /**
     * Test of getMarketValue method, of class Card.
     */
    @Test
    public void testGetMarketValue() {
        System.out.println("getMarketValue");
        Card instance = new Card(4, RegionType.HILL);
        int expResult = 4;
        int result = instance.getMarketValue();
        assertEquals(expResult, result);

        expResult = 2;
        instance.setMarketValue(2);
        assertEquals(expResult, instance.getMarketValue());
    }

    /**
     * Test of setMarketValue method, of class Card.
     */
    @Test
    public void testSetMarketValue() {
        System.out.println("setMarketValue");
        int marketValue = 4;
        Card instance = new Card(3, RegionType.MOUNTAIN);
        instance.setMarketValue(marketValue);
        assertEquals(marketValue, instance.getMarketValue());
    }

    /**
     * Test of isInitial method, of class Card.
     */
    @Test
    public void testIsInitial() {
        System.out.println("isInitial");
        Card instance = new Card(2, RegionType.HILL, true);
        boolean expResult = true;
        boolean result = instance.isInitial();
        assertEquals(expResult, result);

    }

    /**
     * Test of isForSale method, of class Card.
     */
    @Test
    public void testIsForSale() {
        System.out.println("isForSale");
        Card instance = new Card(2, RegionType.HILL);

        boolean expResult = false;
        boolean result = instance.isForSale();
        assertEquals(expResult, result);

    }

    /**
     * Test of setForSale method, of class Card.
     */
    @Test
    public void testSetForSale() {
        System.out.println("setForSale");
        boolean forSale = false;
        Card instance = new Card(2, RegionType.HILL);
        instance.setForSale(forSale);

    }

}
