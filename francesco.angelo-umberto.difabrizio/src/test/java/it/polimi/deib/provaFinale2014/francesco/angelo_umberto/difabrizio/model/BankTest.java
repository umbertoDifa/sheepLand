package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.MissingCardException;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco
 */
public class BankTest {

    public BankTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test dei costruttori di Bank e Card. Test dei metodi loadCard e getCard
     * di Bank
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetCard() throws Exception {
        System.out.println("getCard");

        //creo una carta country
        RegionType type = RegionType.COUNTRYSIDE;

        //inizializzo una bank con le costanti del gioco
        Bank instance = new Bank(GameConstants.NUM_CARDS.getValue(),
                GameConstants.NUM_INITIAL_CARDS.getValue(),
                GameConstants.NUM_FENCES.getValue());

        //creo una carta di valore 2 e tipo country
        Card expResult = new Card(2, type);

        //la carico in corrispondenza di dove inizia il suo indice(2)
        instance.loadCard(expResult,
                RegionType.COUNTRYSIDE.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue());

        //controllo che la get ritorni la stessa carta
        try {
            Card result = instance.getCard(type);
            assertSame("Card aggiunta == carta ricevuta", expResult, result);
        } catch (MissingCardException e) {
            fail("get Card solleva eccezione quando provo a prelevare " + e.getMessage()
                    + "una carte di un tipo presente fra le cards");
        }

        //aggiungo carte (riempio l'array)
        for (int i = 0; i < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++) {
            instance.loadCard(new Card(i, RegionType.COUNTRYSIDE),
                    RegionType.COUNTRYSIDE.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);

            instance.loadCard(new Card(i, RegionType.DESERT),
                    RegionType.DESERT.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);

            instance.loadCard(new Card(i, RegionType.HILL),
                    RegionType.HILL.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);

            instance.loadCard(new Card(i, RegionType.LAKE),
                    RegionType.LAKE.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);

            instance.loadCard(new Card(i, RegionType.MOUNTAIN),
                    RegionType.MOUNTAIN.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);

            instance.loadCard(new Card(i, RegionType.PLAIN),
                    RegionType.PLAIN.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);
        }
        //chiedo 5 carte di ogni tipo
        for (int i = 0; i < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++) {
            instance.getCard(RegionType.COUNTRYSIDE);
            instance.getCard(RegionType.PLAIN);
            instance.getCard(RegionType.DESERT);
            instance.getCard(RegionType.MOUNTAIN);
            instance.getCard(RegionType.HILL);
            instance.getCard(RegionType.LAKE);
        }

    }

    @Test(expected = MissingCardException.class)
    public void testGetCardException() throws MissingCardException {
        System.out.println("getCard");

        Bank instance = new Bank(GameConstants.NUM_CARDS.getValue(),
                GameConstants.NUM_INITIAL_CARDS.getValue(),
                GameConstants.NUM_FENCES.getValue()); //inizializzo una bank 

//        RegionType type = RegionType.COUNTRYSIDE; //creo una carta country                
//        
//        Card expResult = new Card(2, type); //creo una carta di valore 2 e tipo country
//        //non la carico
//        //instance.loadCard(expResult, RegionType.COUNTRYSIDE.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue());//la carico in corrispondenza di dove inizia il suo indice(2)
//
//        //dovrebbe arrivare un eccezione
//        Card result = instance.getCard(type);
//        
        //aggiungo carte (riempio l'array)
        for (int i = 0; i < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++) {
            instance.loadCard(new Card(i, RegionType.COUNTRYSIDE),
                    RegionType.COUNTRYSIDE.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);

            instance.loadCard(new Card(i, RegionType.DESERT),
                    RegionType.DESERT.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);

            instance.loadCard(new Card(i, RegionType.HILL),
                    RegionType.HILL.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);

            instance.loadCard(new Card(i, RegionType.LAKE),
                    RegionType.LAKE.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);

            instance.loadCard(new Card(i, RegionType.MOUNTAIN),
                    RegionType.MOUNTAIN.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);

            instance.loadCard(new Card(i, RegionType.PLAIN),
                    RegionType.PLAIN.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue() + i);
        }

        //chiedo 5 carte di ogni tipo
        for (int i = 0; i < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++) {
            instance.getCard(RegionType.COUNTRYSIDE);
            instance.getCard(RegionType.PLAIN);
            instance.getCard(RegionType.DESERT);
            instance.getCard(RegionType.MOUNTAIN);
            instance.getCard(RegionType.HILL);
            instance.getCard(RegionType.LAKE);
        }

        //chiedo una carta a caso, qualsiasi genererà un ecception
        int count = 0;
        for (int i = 0; i < 100; i++) {
            try {
                instance.getCard(RegionType.getRandomRegionType());
            } catch (MissingCardException ex) {
                //carta non trovata
                count++;
            }
        }
        assertTrue(count == 100);
        instance.getCard(RegionType.getRandomRegionType());

    }

    /**
     * Test of getInitialCard method, of class Bank.
     */
    @Ignore
    @Test
    public void testGetInitialCard() {
        System.out.println("getInitialCard");
        Bank instance = null;
        Card expResult = null;
        Card result = instance.getInitialCard();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of priceOfCard method, of class Bank.
     */
    @Ignore
    @Test
    public void testPriceOfCard() throws Exception {
        System.out.println("priceOfCard");
        RegionType type = null;
        Bank instance = null;
        int expResult = 0;
        int result = instance.priceOfCard(type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFence method, of class Bank.
     */
    @Ignore
    @Test
    public void testGetFence() throws Exception {
        System.out.println("getFence");
        Bank instance = null;
        Fence expResult = null;
        Fence result = instance.getFence();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadFence method, of class Bank.
     */
    @Ignore
    @Test
    public void testLoadFence() {
        System.out.println("loadFence");
        int position = 0;
        Bank instance = null;
        instance.loadFence(position);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadFinalFence method, of class Bank.
     */
    @Ignore
    @Test
    public void testLoadFinalFence() {
        System.out.println("loadFinalFence");
        int position = 0;
        Bank instance = null;
        instance.loadFinalFence(position);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadCard method, of class Bank.
     */
    @Ignore
    @Test
    public void testLoadCard() {
        System.out.println("loadCard");
        Card card = null;
        int position = 0;
        Bank instance = null;
        instance.loadCard(card, position);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of numberOfUsedFence method, of class Bank.
     */
    @Ignore
    @Test
    public void testNumberOfUsedFence() throws Exception {
        System.out.println("numberOfUsedFence");
        Bank instance = null;
        int expResult = 0;
        int result = instance.numberOfUsedFence();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
