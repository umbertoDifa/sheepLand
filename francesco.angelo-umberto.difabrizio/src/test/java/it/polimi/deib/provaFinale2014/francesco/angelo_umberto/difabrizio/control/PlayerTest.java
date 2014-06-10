package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.SocketTrasmission;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.TrasmissionController;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Umberto
 */
public class PlayerTest {

    Player instance;
    List<String> players;
    TrasmissionController controller;
    GameManager gameManager;

    /**
     * Set up players names, controller and an instance of Player
     *
     * @throws RemoteException
     */
    @Before
    public void setUp() throws RemoteException {
        controller = new SocketTrasmission();

        players = new ArrayList<String>();
        players.add("fra");
        players.add("umbo");
        gameManager = new GameManager(players, controller);
        instance = new Player(gameManager, "aldo");
    }

    /**
     * Test of getPlayerNickName method, of class Player.
     */
    @Test
    public void testGetPlayerNickName() {
        System.out.println("getPlayerNickName");
        String expResult = "aldo";
        String result = instance.getPlayerNickName();
        assertEquals(expResult, result);

    }

    /**
     * Test of getMainShepherd method, of class Player.
     */
    @Test
    public void testGetMainShepherd() {
        System.out.println("getMainShepherd");
        Shepherd result = instance.getMainShepherd();
        Shepherd expResult = instance.shepherd[0];
        assertEquals(expResult, result);
    }

    /**
     * Test of moveOvine method, of class Player.
     */
    @Test
    public void testMoveOvine() {
        System.out.println("moveOvine");
        String beginRegion = "8";
        String finishRegion = "10";
        String type = "sheep";

        gameManager.getMap().setUp();
        //aggiungo pecora nella region 8
        gameManager.getMap().getRegions()[8].addOvine(new Ovine(OvineType.SHEEP));
        String expResult = "Ovino mosso";
        instance.setShepherd(0, "14");
        String result = instance.moveOvine(beginRegion, finishRegion, type);
        assertEquals(expResult, result);

    }

    /**
     * Test of setShepherd method, of class Player.
     */
    @Test
    public void testSetShepherd() {
        System.out.println("setShepherd");
        gameManager.getMap().setUp();
        instance.setShepherd(0, "14");
    }

    /**
     * Test of setShepherdRemote method, of class Player.
     */
    @Test
    public void testSetShepherdRemote() {
        //se funziona la funzione non remota funziona anche questo
    }

    /**
     * Test of moveShepherd method, of class Player.
     */
    @Test
    public void testMoveShepherd() {
        System.out.println("moveShepherd");
        gameManager.getMap().setUp();

        instance.setShepherd(0, "14");

        gameManager.getBank().loadFence(0);

        String result = instance.moveShepherd("0", "33");

        assertEquals(result, "Pastore spostato,1");
    }

    /**
     * Test of buyLand method, of class Player.
     */
    @Ignore
    @Test
    public void testBuyLand() {
        System.out.println("buyLand");
        String landToBuy = "";
        Player instance = null;
        String expResult = "";
        String result = instance.buyLand(landToBuy);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mateSheepWith method, of class Player.
     */
    @Ignore
    @Test
    public void testMateSheepWith() {
        System.out.println("mateSheepWith");
        String shepherdNumber = "";
        String regionToMate = "";
        String otherOvineType = "";
        Player instance = null;
        String expResult = "";
        String result = instance.mateSheepWith(shepherdNumber, regionToMate,
                otherOvineType);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of killOvine method, of class Player.
     */
    @Ignore
    @Test
    public void testKillOvine() {
        System.out.println("killOvine");
        String shepherdNumber = "";
        String region = "";
        String typeToKill = "";
        Player instance = null;
        String expResult = "";
        String result = instance.killOvine(shepherdNumber, region, typeToKill);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of moveShepherdRemote method, of class Player.
     */
    @Test
    public void testMoveShepherdRemote() {
        //se funziona la funzione non remota funziona anche questo
    }

    /**
     * Test of moveOvineRemote method, of class Player.
     */
    @Test
    public void testMoveOvineRemote() {
        //se funziona la funzione non remota funziona anche questo
    }

    /**
     * Test of buyLandRemote method, of class Player.
     */
    @Test
    public void testBuyLandRemote() {
        //se funziona la funzione non remota funziona anche questo
    }

    /**
     * Test of mateSheepWithRemote method, of class Player.
     */
    @Test
    public void testMateSheepWithRemote() {
        //se funziona la funzione non remota funziona anche questo
    }

    /**
     * Test of killOvineRemote method, of class Player.
     */
    @Test
    public void testKillOvineRemote() {
        //se funziona la funzione non remota funziona anche questo
    }

}
