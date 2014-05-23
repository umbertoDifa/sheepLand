
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;
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
public class ShepherdTest {
    
    public ShepherdTest() {
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
     * Test of moveTo method, of class Shepherd.
     */
    @Ignore
    @Test
    public void testMoveTo() {
        System.out.println("moveTo");
        Street street = null;
        Shepherd instance = new Shepherd();
        instance.moveTo(street);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWallet method, of class Shepherd.
     */
    @Ignore
    @Test
    public void testGetWallet() {
        System.out.println("getWallet");
        Shepherd instance = new Shepherd();
        Wallet expResult = null;
        Wallet result = instance.getWallet();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStreet method, of class Shepherd.
     */
    @Ignore
    @Test
    public void testGetStreet() {
        System.out.println("getStreet");
        Shepherd instance = new Shepherd();
        Street expResult = null;
        Street result = instance.getStreet();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMyCards method, of class Shepherd.
     */
    @Ignore
    @Test
    public void testGetMyCards() {
        System.out.println("getMyCards");
        Shepherd instance = new Shepherd();
        List<Card> expResult = null;
        List<Card> result = instance.getMyCards();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMyCards method, of class Shepherd.
     */
    @Ignore
    @Test
    public void testSetMyCards() {
        System.out.println("setMyCards");
        List<Card> myCards = null;
        Shepherd instance = new Shepherd();
        instance.setMyCards(myCards);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addCard method, of class Shepherd.
     */
    
    @Test
    public void testAddCard() {
        System.out.println("addCard");
        
        //creo una carta a caso
        Card card = new Card(0, RegionType.HILL);
        
        //creo un pastore      
        Shepherd instance = new Shepherd();
        
        //aggiungo una carta
        instance.addCard(card);
        
        //verifico che la carta ci sia
        assertSame(card, instance.getMyCards().get(0));
    }

    /**
     * Test of removeCard method, of class Shepherd.
     */
    @Ignore
    @Test
    public void testRemoveCard() {
        System.out.println("removeCard");
        Card card = null;
        Shepherd instance = new Shepherd();
        instance.removeCard(card);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setWallet method, of class Shepherd.
     */
    @Ignore
    @Test
    public void testSetWallet() {
        System.out.println("setWallet");
        Wallet wallet = null;
        Shepherd instance = new Shepherd();
        instance.setWallet(wallet);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
