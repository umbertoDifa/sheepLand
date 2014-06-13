package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Umberto
 */
public class MarketTest {

    int playersNumber = 3;

    //creo i market values
    int marketValue1 = 1;
    int marketValue2 = 2;
    int marketValue3 = 3;
    int marketValue4 = 4;

    //creo gli indice dei player
    int indexOwner0 = 0;
    int indexOwner1 = 1;
    int indexOwner2 = 2;

    //cards
    Card cardToAdd00;
    Card cardToAdd01;
    Card cardToAdd02;

    Card cardToAdd10;
    Card cardToAdd11;
    Card cardToAdd12;

    Card cardToAdd20;
    Card cardToAdd21;
    Card cardToAdd22;

    //creo un market
    Market instance;

    public MarketTest() {
        instance = new Market(playersNumber);
    }
    
    
    
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        
        //creo le carte che aggiungerò
        cardToAdd00 = new Card(1, RegionType.HILL);
        cardToAdd00.setMarketValue(marketValue1);
        cardToAdd01 = new Card(2, RegionType.DESERT);
        cardToAdd01.setMarketValue(marketValue1);
        cardToAdd02 = new Card(4, RegionType.MOUNTAIN);
        cardToAdd02.setMarketValue(marketValue3);

        cardToAdd10 = new Card(1, RegionType.HILL);
        cardToAdd10.setMarketValue(marketValue2);
        cardToAdd11 = new Card(2, RegionType.DESERT);
        cardToAdd11.setMarketValue(marketValue4);
        cardToAdd12 = new Card(4, RegionType.MOUNTAIN);
        cardToAdd12.setMarketValue(marketValue1);

        cardToAdd20 = new Card(1, RegionType.HILL);
        cardToAdd20.setMarketValue(marketValue3);
        cardToAdd21 = new Card(2, RegionType.DESERT);
        cardToAdd21.setMarketValue(marketValue2);
        cardToAdd22 = new Card(4, RegionType.MOUNTAIN);
        cardToAdd22.setMarketValue(marketValue1);

        //inserisco le carte
        this.testAddCard();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addCard method, of class Market.
     */
    public void testAddCard() {
        System.out.println("addCard");

        //aggiungo le carte
        instance.addCard(cardToAdd00, indexOwner0);
        instance.addCard(cardToAdd01, indexOwner0);
        instance.addCard(cardToAdd02, indexOwner0);

        instance.addCard(cardToAdd10, indexOwner1);
        instance.addCard(cardToAdd11, indexOwner1);
        instance.addCard(cardToAdd12, indexOwner1);

        instance.addCard(cardToAdd20, indexOwner2);
        instance.addCard(cardToAdd21, indexOwner2);
        instance.addCard(cardToAdd22, indexOwner2);

    }

    /**
     * Test of removeCard method, of class Market.
     */
    public void testRemoveCard() {
        System.out.println("removeCard");

        assertTrue(instance.removeCard(cardToAdd00, indexOwner0));

        assertTrue(instance.removeCard(cardToAdd11, indexOwner1));

        assertTrue(instance.removeCard(cardToAdd21, indexOwner2));
        assertTrue(instance.removeCard(cardToAdd22, indexOwner2));
    }

    /**
     * Test of clear method, of class Market.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        instance.clear();

        assertNull(instance.getCard(RegionType.HILL));
        assertNull(instance.getCard(RegionType.MOUNTAIN));
        assertNull(instance.getCard(RegionType.DESERT));
        
        assertEquals(-1, instance.getOwnerByCard(cardToAdd00));
        assertEquals(-1, instance.getOwnerByCard(cardToAdd01));
        assertEquals(-1, instance.getOwnerByCard(cardToAdd10));
        assertEquals(-1, instance.getOwnerByCard(cardToAdd22));

    }
    
    /**
     * The market
     * @return the market created
     */
    public Market getMarket(){
        return instance;
    }

    /**
     * Test of getCard method, of class Market.
     */
    @Test
    public void testGetCard() {
        System.out.println("getCard");

        Card result = instance.getCard(RegionType.HILL);
        assertSame(result, cardToAdd00);
        assertEquals(result, cardToAdd00);

        result = instance.getCard(RegionType.MOUNTAIN);
        assertSame(result, cardToAdd12);
        assertEquals(result, cardToAdd12);

        result = instance.getCard(RegionType.DESERT);
        assertSame(result, cardToAdd01);
        assertEquals(result, cardToAdd01);

        //rimuovo delle carte
        this.testRemoveCard();

        result = instance.getCard(RegionType.HILL);
        assertSame(result, cardToAdd10);
        assertEquals(result, cardToAdd10);

        result = instance.getCard(RegionType.MOUNTAIN);
        assertSame(result, cardToAdd12);
        assertEquals(result, cardToAdd12);

        result = instance.getCard(RegionType.DESERT);
        assertSame(result, cardToAdd01);
        assertEquals(result, cardToAdd01);
    }

    /**
     * Test of getOwnerByCard method, of class Market.
     */
    @Test
    public void testGetOwnerByCard() {
        System.out.println("getOwnerByCard");

        int result0 = instance.getOwnerByCard(cardToAdd00);
        int result1 = instance.getOwnerByCard(cardToAdd11);
        int result2 = instance.getOwnerByCard(cardToAdd21);

        assertEquals(0, result0);
        assertEquals(1, result1);
        assertEquals(2, result2);
    }

}
