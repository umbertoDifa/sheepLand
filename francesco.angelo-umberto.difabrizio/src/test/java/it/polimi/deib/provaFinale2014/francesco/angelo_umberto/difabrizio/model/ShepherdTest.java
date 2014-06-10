package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Umberto
 */
public class ShepherdTest {

    Map map;

    /**
     * Sets up the map
     */
    @Before
    public void setUp() {
        map = new Map();
        map.setUp();
    }

    /**
     * Test of moveTo method, of class Shepherd.
     *
     * @throws StreetNotFoundException
     */
    @Test
    public void testMoveTo() throws StreetNotFoundException {
        System.out.println("moveTo");
        Street street = map.convertStringToStreet("25");
        Shepherd instance = new Shepherd(
                GameConstants.STANDARD_WALLET_AMMOUNT.getValue());
        instance.moveTo(street);
        assertSame(instance.getStreet(), street);
    }

    /**
     * Test of getWallet method, of class Shepherd.
     */
    @Test
    public void testGetWallet() {
        System.out.println("getWallet");
        Shepherd instance = new Shepherd(
                GameConstants.STANDARD_WALLET_AMMOUNT.getValue());

        Wallet result = instance.getWallet();
        assertTrue(
                result.getAmount() == GameConstants.STANDARD_WALLET_AMMOUNT.getValue());

    }

    /**
     * Test of getStreet method, of class Shepherd.
     *
     * @throws StreetNotFoundException
     */
    @Test
    public void testGetStreet() throws StreetNotFoundException {
        System.out.println("getStreet");
        Shepherd instance = new Shepherd(
                GameConstants.STANDARD_WALLET_AMMOUNT.getValue());
        instance.moveTo(map.convertStringToStreet("13"));
        Street result = instance.getStreet();
        assertEquals(map.getStreets()[13], result);

    }

    /**
     * Test of getMyCards method, of class Shepherd.
     */
    @Test
    public void testGetMyCards() {
        System.out.println("getMyCards");
        Shepherd instance = new Shepherd(
                GameConstants.STANDARD_WALLET_AMMOUNT.getValue());

        Card card1 = new Card(1, RegionType.HILL);
        Card card2 = new Card(3, RegionType.COUNTRYSIDE);
        Card card3 = new Card(0, RegionType.HILL);
        List<Card> expectedCards = new ArrayList<Card>();
        expectedCards.add(card3);
        expectedCards.add(card2);
        expectedCards.add(card1);

        instance.addCard(card1);
        instance.addCard(card2);
        instance.addCard(card3);

        List<Card> result = instance.getMyCards();
        expectedCards.removeAll(result);

        assertTrue(expectedCards.isEmpty());

    }

    /**
     * Test of setMyCards method, of class Shepherd.
     */
    @Test
    public void testSetMyCards() {
        System.out.println("setMyCards");
        List<Card> myCards = new ArrayList<Card>();
        Shepherd instance = new Shepherd(
                GameConstants.STANDARD_WALLET_AMMOUNT.getValue());

        Card card1 = new Card(1, RegionType.HILL);
        Card card2 = new Card(3, RegionType.COUNTRYSIDE);
        Card card3 = new Card(0, RegionType.HILL);

        myCards.add(card3);
        myCards.add(card2);
        myCards.add(card1);

        instance.setMyCards(myCards);

        assertSame(instance.getMyCards(), myCards);
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
        Shepherd instance = new Shepherd(
                GameConstants.STANDARD_WALLET_AMMOUNT.getValue());

        //aggiungo una carta
        instance.addCard(card);

        //verifico che la carta ci sia
        assertSame(card, instance.getMyCards().get(0));
    }

    /**
     * Test of removeCard method, of class Shepherd.
     */
    @Test
    public void testRemoveCard() {
        System.out.println("removeCard");

        Shepherd instance = new Shepherd(
                GameConstants.STANDARD_WALLET_AMMOUNT.getValue());

        Card card1 = new Card(1, RegionType.HILL);
        Card card2 = new Card(3, RegionType.COUNTRYSIDE);
        Card card3 = new Card(0, RegionType.HILL);

        instance.addCard(card1);
        instance.addCard(card2);
        instance.addCard(card3);

        assertTrue(instance.getMyCards().size() == 3);

        instance.removeCard(card3);
        instance.removeCard(card2);
        instance.removeCard(card1);

        assertTrue(instance.getMyCards().isEmpty());
    }

}
