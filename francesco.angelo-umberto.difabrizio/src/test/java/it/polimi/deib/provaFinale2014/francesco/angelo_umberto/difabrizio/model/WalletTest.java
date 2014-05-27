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
public class WalletTest {

    public WalletTest() {
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
     * Test of getAmount method, of class Wallet.
     */
    @Test
    public void testGetAmount() {
        System.out.println("getAmount");
        Wallet instance = new Wallet(GameConstants.STANDARD_WALLET_AMMOUNT.getValue());
        int expResult = GameConstants.STANDARD_WALLET_AMMOUNT.getValue();
        int result = instance.getAmount();
        assertEquals(expResult, result);

    }

    /**
     * Test of setAmount method, of class Wallet.
     */
    @Test
    public void testSetAmount() {
        System.out.println("setAmount");
        int amount = 0;
        Wallet instance = new Wallet(GameConstants.STANDARD_WALLET_AMMOUNT.getValue());
        instance.setAmount(amount);
        assertTrue(instance.getAmount() == 0);
    }

}
