package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Bank;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Map;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.RmiTrasmission;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.TrasmissionController;
import java.util.ArrayList;
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
public class GameManagerTest {

    GameManager instance;
    TrasmissionController controller;

    public GameManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        controller = new RmiTrasmission();

        instance = new GameManager(new ArrayList<String>(),
                controller);

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getController method, of class GameManager.
     */
    @Test
    public void testGetController() {
        System.out.println("getController");
        TrasmissionController expResult = controller;
        TrasmissionController result = instance.getController();
        assertEquals(expResult, result);

    }

    /**
     * Test of getMap method, of class GameManager.
     */
    @Test
    public void testGetMap() {
        System.out.println("getMap");

        instance = new GameManager(new ArrayList<String>(),
                controller);

        Map result = instance.getMap();
        
        //l'unico pericolo è il null pointer excetpion
        assertNotNull(result);
    }

    /**
     * Test of getBank method, of class GameManager.
     */
    @Test
    public void testGetBank() {
        System.out.println("getBank");
        Bank result = instance.getBank();
        //l'unico pericolo è il null pointer exception
        assertNotNull(result);
    }
}
