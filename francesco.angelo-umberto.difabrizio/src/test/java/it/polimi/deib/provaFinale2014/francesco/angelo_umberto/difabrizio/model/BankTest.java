package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.MissingCardException;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco
 */
public class BankTest {

    Bank instance;

    /**
     * Sets up a new bank with the game constants
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        //inizializzo una bank con le costanti del gioco
        instance = new Bank(GameConstants.NUM_CARDS.getValue(),
                GameConstants.NUM_INITIAL_CARDS.getValue(),
                GameConstants.NUM_FENCES.getValue());
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
        int i, j;
        //creo una carta country
        RegionType type = RegionType.COUNTRYSIDE;

        //aggiungo carte (riempio l'array)
        for (i = 0; i < RegionType.values().length - 1; i++) {
            for (j = 0; j < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); j++) {
                Card cardToAdd = new Card(j, RegionType.values()[i]);
                //caricala
                instance.loadCard(cardToAdd);
            }
        }
        //chiedo 5 carte di ogni tipo
        for (i = 0; i < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++) {
            instance.getCard(RegionType.COUNTRYSIDE);
            instance.getCard(RegionType.PLAIN);
            instance.getCard(RegionType.DESERT);
            instance.getCard(RegionType.MOUNTAIN);
            instance.getCard(RegionType.HILL);
            instance.getCard(RegionType.LAKE);
        }

    }

    /**
     * Tests the get card method
     *
     * @throws MissingCardException
     */
    @Test(expected = MissingCardException.class)
    public void testGetCardException() throws MissingCardException {
        System.out.println("getCard");
        int i, j;
        //creo una carta country
        RegionType type = RegionType.COUNTRYSIDE;

        //aggiungo carte (riempio l'array)
        for (i = 0; i < RegionType.values().length - 1; i++) {
            for (j = 0; j < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); j++) {
                Card cardToAdd = new Card(j, RegionType.values()[i]);
                //caricala
                instance.loadCard(cardToAdd);
            }
        }
        //chiedo 5 carte di ogni tipo
        for (i = 0; i < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++) {
            instance.getCard(RegionType.COUNTRYSIDE);
            instance.getCard(RegionType.PLAIN);
            instance.getCard(RegionType.DESERT);
            instance.getCard(RegionType.MOUNTAIN);
            instance.getCard(RegionType.HILL);
            instance.getCard(RegionType.LAKE);
        }

        //chiedo una carta a caso, qualsiasi genererà un ecception
        int count = 0;
        for (i = 0; i < 100; i++) {
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
    @Test
    public void testGetInitialCard() {
        int i;
        int j;
        int cont = 0;

        System.out.println("getInitialCard");

        //creo l'array che ospiterà i risultati
        Card result[] = new Card[GameConstants.NUM_INITIAL_CARDS.getValue()];

        //prendo le mie 6 carte iniziali 
        for (i = 0; i < GameConstants.NUM_INITIAL_CARDS.getValue(); i++) {
            result[i] = instance.getInitialCard();
        }
        //testo che siano tutte diverse tra di loro
        for (i = 0; i < GameConstants.NUM_INITIAL_CARDS.getValue() - 1; i++) {
            for (j = i + 1; j < GameConstants.NUM_INITIAL_CARDS.getValue(); j++) {
                //carte diverse
                assertNotSame(result[i], result[j]);
                assertNotEquals(result[i], result[j]);

                //tipo diverso
                assertNotSame(result[i].getType(), result[j].getType());
                assertNotEquals(result[i].getType(), result[j].getType());

                //e non shepsburg
                assertNotSame(result[i].getType(), RegionType.SHEEPSBURG);
                assertNotSame(result[j].getType(), RegionType.SHEEPSBURG);
                assertNotEquals(result[i].getType(), RegionType.SHEEPSBURG);
                assertNotSame(result[j].getType(), RegionType.SHEEPSBURG);
            }
        }
        //ne chiedo un'altra e mi becco l'exception arrayOutOfBounds
        for (i = 0; i < 20; i++) {
            try {
                Card card = instance.getInitialCard();
            } catch (IllegalArgumentException e) {
                //la random del dado inizializzata con size() delle carte fallisce
                cont++;
            }
        }

        assertTrue(cont == 20);
    }

    /**
     * Test of getFence method, of class Bank.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetFence() throws Exception {
        System.out.println("getFence");
        int i;

        //carico tutte le fence
        for (i = 0; i < GameConstants.NUM_FENCES.getValue(); i++) {
            instance.loadFence(i);
        }

        instance.getFence();
    }

    /**
     * Test of loadFence method, of class Bank.
     *
     * @throws
     * it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException
     */
    @Test
    public void testLoadFence() throws FinishedFencesException {
        System.out.println("loadFence");
        int i;

        //carico tutte le fence
        for (i = 0; i < GameConstants.NUM_FENCES.getValue(); i++) {
            instance.loadFence(i);
        }

        //le tolgo tutte
        for (i = 0; i < GameConstants.NUM_FENCES.getValue(); i++) {
            instance.getFence();
        }
    }

    /**
     * Test of loadFinalFence method, of class Bank.
     *
     * @throws
     * it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.FinishedFencesException
     */
    @Test(expected = FinishedFencesException.class)
    public void testLoadFinalFence() throws FinishedFencesException {
        System.out.println("loadFinalFence");
        int i;

        //carico metà fence non finali
        for (i = 0; i < GameConstants.NUM_FENCES.getValue() / 2; i++) {
            instance.loadFence(i);
        }

        //carico fence finali
        for (i = GameConstants.NUM_FENCES.getValue() / 2; i < GameConstants.NUM_FENCES.getValue(); i++) {
            instance.loadFinalFence(i);
        }

        //prendo la prima metà non finali
        for (i = 0; i < GameConstants.NUM_FENCES.getValue() / 2; i++) {
            assertFalse(instance.getFence().isFinal());
        }

        //prendo la seconda metà finale
        for (i = GameConstants.NUM_FENCES.getValue() / 2; i < GameConstants.NUM_FENCES.getValue(); i++) {
            assertTrue(instance.getFence().isFinal());
        }

        //chiedo un altra fence per avere l'eccezione
        instance.getFence();
    }

    /**
     * Test of loadCard method, of class Bank.
     */
    @Test
    public void testLoadCard() {
        System.out.println("loadCard");
        //creo una banca

        Card card1 = new Card(2, RegionType.HILL);

        instance.loadCard(card1);

    }

    /**
     * Test of numberOfUsedFence method, getFence e loadFence, of class Bank.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testNumberOfUsedFence() throws Exception {
        int i;
        int exceptionCounter = 0;

        //carico tutte le fence
        for (i = 0; i < GameConstants.NUM_FENCES.getValue(); i++) {
            instance.loadFence(i);
        }
        //testo che ci siano tutte le fence
        assertTrue(instance.numberOfUsedFence() == 0);

        //ne prendo la metà e le butto
        for (i = 0; i < GameConstants.NUM_FENCES.getValue() / 2; i++) {
            instance.getFence();
        }
        //testo che siano la metà
        assertTrue(
                instance.numberOfUsedFence() == GameConstants.NUM_FENCES.getValue() / 2);

        //le finisco
        for (i = GameConstants.NUM_FENCES.getValue() / 2; i < GameConstants.NUM_FENCES.getValue(); i++) {
            instance.getFence();
        }
        //testo l'eccezione
        try {
            instance.getFence();
        } catch (FinishedFencesException ex) {
            exceptionCounter++;
        }
        assertTrue(exceptionCounter == 1);

        //le carico tutte 
        for (i = 0; i < GameConstants.NUM_FENCES.getValue(); i++) {
            instance.loadFence(i);
        }
        assertTrue(instance.numberOfUsedFence() == 0);

        //ne uso 3
        for (i = 0; i < 3; i++) {
            instance.getFence();
        }
        //testo 3
        assertTrue(instance.numberOfUsedFence() == 3);

        //le toglo tutte
        for (i = 3; i < GameConstants.NUM_FENCES.getValue(); i++) {
            instance.getFence();
        }
        //testo l'eccezione
        try {
            instance.getFence();
        } catch (FinishedFencesException ex) {
            exceptionCounter++;
        }
        assertTrue(exceptionCounter == 2);
    }

    /**
     * Test of getPriceOfCard method, of class Bank.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testPriceOfCard() throws Exception {
        System.out.println("priceOfCard");
        int i, j, sum = 0;
        int exCounter = 0;

        //aggiungo carte (riempio l'array)
        for (i = 0; i < RegionType.values().length - 1; i++) {
            for (j = 0; j < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); j++) {
                Card cardToAdd = new Card(j, RegionType.values()[i]);
                //caricala
                instance.loadCard(cardToAdd);
            }
        }

        //chiedo tutte le carte di un tipo e per ognuna verifico che il prezzo
        //sia giusto
        for (i = 0; i < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); i++) {
            assertTrue(instance.getPriceOfCard(RegionType.LAKE) == i);
            instance.getCard(RegionType.LAKE);
        }
        //chiedo tutte le carte sommando i prezzi
        for (i = 0; i < RegionType.values().length - 1; i++) {
            for (j = 0; j < GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue(); j++) {
                if (RegionType.values()[i] != RegionType.LAKE) {
                    sum += instance.getPriceOfCard(RegionType.values()[i]);
                    instance.getCard(RegionType.values()[i]);
                }
            }
        }
        //verifico che sia giusto
        assertTrue(
                sum == 10 * (RegionType.values().length - 2));

        //verifico exception per ogni tipo
        for (i = 0, exCounter = 0; i < 100; i++) {
            try {
                instance.getPriceOfCard(RegionType.getRandomRegionType());
            } catch (MissingCardException ex) {
                exCounter++;
            }
        }
        //verifico exception
        assertTrue(exCounter == 100);
    }

    /**
     * Test of getPriceOfCard method, of class Bank.
     */
    @Test
    public void testGetPriceOfCard() throws MissingCardException {
        System.out.println("getPriceOfCard");
        RegionType type = RegionType.MOUNTAIN;
        instance.loadCard(new Card(2, RegionType.MOUNTAIN));

        int expResult = 2;

        int result = instance.getPriceOfCard(type);
        assertEquals(expResult, result);

    }

    /**
     * Test of getNumberOfAvailableCards method, of class Bank.
     */
    @Test
    public void testGetNumberOfAvailableCards() {
        System.out.println("getNumberOfAvailableCards");
        RegionType type = RegionType.MOUNTAIN;       

        int expResult = 0;
        
        int result = instance.getNumberOfAvailableCards(type);
        
        assertEquals(expResult, result);
     
    }

}
