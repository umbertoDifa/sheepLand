package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Market;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.MarketTest;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Ovine;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Shepherd;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.SocketTrasmission;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network.TrasmissionController;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.rmi.RemoteException;
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
public class PlayerTest {

    Player instance;
    List<String> players;
    TrasmissionController controller;
    GameManager gameManager;
    Market market;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

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
        players.add("adnre");
        players.add("fefa");

        gameManager = new GameManager(players, controller);
        //creo un market e aggiungo le carte
        MarketTest marketTest = new MarketTest();
        marketTest.setUp();
        market = marketTest.getMarket();

        instance = new Player(gameManager, "fra", market);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test of getPlayerNickName method, of class Player.
     */
    @Test
    public void testGetPlayerNickName() {
        System.out.println("getPlayerNickName");
        String expResult = "fra";
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

    /**
     * Test of chooseAndMakeAction method, of class Player.
     */
    @Ignore
    @Test
    public void testChooseAndMakeAction() throws Exception {
        System.out.println("chooseAndMakeAction");
        Player instance = null;
        instance.chooseAndMakeAction();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of chooseShepherdStreet method, of class Player.
     */
    @Ignore
    @Test
    public void testChooseShepherdStreet() throws Exception {
        System.out.println("chooseShepherdStreet");
        int shepherdIndex = 0;
        Player instance = null;
        instance.chooseShepherdStreet(shepherdIndex);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSellableCards method, of class Player.
     */
    @Test
    public void testGetSellableCards() {
        System.out.println("getSellableCards");
        //aggiungo una carta iniziale al player
        instance.getMainShepherd().addCard(new Card(1, RegionType.HILL, true));

        //aggiugno altre carte
        Card carda = new Card(2, RegionType.HILL);
        carda.setForSale(true);
        instance.getMainShepherd().addCard(carda);
        instance.getMainShepherd().addCard(new Card(3, RegionType.HILL));
        instance.getMainShepherd().addCard(new Card(4, RegionType.MOUNTAIN));

        DebugLogger.println("carte possedute");
        for (Card card : instance.getMainShepherd().getMyCards()) {
            DebugLogger.println(
                    "card: " + card.getType().toString() + " value:" + card.getValue());
        }
        instance.getSellableCards();
    }

    /**
     * Test of putCardInMarket method, of class Player.
     */
    @Test
    public void testPutCardInMarket() {
        System.out.println("putCardInMarket");
        Card cardToAdd = new Card(3, RegionType.LAKE);

        instance.getMainShepherd().addCard(cardToAdd);

        instance.putCardInMarket(cardToAdd.getType().toString(), 4);

        assertSame(market.getCard(RegionType.LAKE), cardToAdd);
    }

    /**
     * Test of sellCard method, of class Player.
     */
    @Ignore
    @Test
    public void testSellCard() throws Exception {
        System.out.println("sellCard");
        Player instance = null;
        boolean expResult = false;
        boolean result = instance.sellCard();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of buyCard method, of class Player.
     */
    @Ignore
    @Test
    public void testBuyCard() throws Exception {
        System.out.println("buyCard");
        Player instance = null;
        boolean expResult = false;
        boolean result = instance.buyCard();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of payCardFromMarket method, of class Player.
     */
    @Test
    public void testPayCardFromMarket() {
        System.out.println("payCardFromMarket");

        //prendi i vecchi soldi
        int oldAmmount = instance.getMainShepherd().getWallet().getAmount();

        //scelgo il tipo di carta da comprare
        String cardType = RegionType.HILL.toString();

        //la prendo dal market
        Card cardToBuy = market.getCard(RegionType.HILL);

        DebugLogger.println(
                "trovata carda con owner: " + market.getOwnerByCard(
                        cardToBuy));

        //controllo che il giocatore ora non abbia quella carta
        for (Card card : instance.getMainShepherd().getMyCards()) {
            assertTrue(card != cardToBuy);
        }

        //pago e la conquisto
        instance.payCardFromMarket(cardType);

        int cardPrice = cardToBuy.getMarketValue();

        //controllo che siano scalati i soldi al player
        assertTrue(
                instance.getMainShepherd().getWallet().getAmount() + cardPrice == oldAmmount);

        boolean foundOk = false;
        //controllo di avere quella carta
        for (Card card : instance.getMainShepherd().getMyCards()) {
            if (card == cardToBuy) {
                foundOk = true;
            }
        }

        assertTrue(foundOk);

        //controllo che non ci sia più quella carta nel market
        assertTrue(market.getCard(RegionType.HILL) != cardToBuy);
        assertTrue(market.getCard(RegionType.HILL).getMarketValue() == 2);

    }

}
