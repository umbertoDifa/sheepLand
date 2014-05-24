package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.NoOvineException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.RegionNotFoundException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.StreetNotFoundException;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

public class RegionTest {

    public RegionTest() {
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
     * Test of getMyOvines method, of class Region.
     */
    @Test
    public void testGetMyOvines() {
        System.out.println("getMyOvines");
        Region instance = new Region();
        Ovine ovine = new Ovine();
        instance.addOvine(ovine);
        List<Ovine> result = instance.getMyOvines();
        assertSame(ovine, result.get(0));
    }

    /**
     * Test of getType method, of class Region.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        //creo una regione montagna
        Region instance = new Region(RegionType.MOUNTAIN);
        RegionType expResult = RegionType.MOUNTAIN;
        RegionType result = instance.getType();
        assertSame(expResult, result);
        //che chiaramente non è deserto
        assertNotSame(RegionType.DESERT, result);
    }

    /**
     * Test of addOvine method, e removeOvine of class Region.
     */
    @Test
    public void testAddAndRemoveOvine() {
        System.out.println("addOvine");

        int numberOfOvine = 5;
        int i;
        int j;
        int exCounter = 0;
        int exCounterExpected = 10;
        //creo una regione a caso
        Region instance = new Region();

        //creo dei lamb e li aggiungo; 
        for (i = 0; i < numberOfOvine; i++) {
            instance.addOvine(new Ovine(OvineType.LAMB));
        }
        //creo dei sheep e li aggiungo
        for (i = 0; i < numberOfOvine; i++) {
            instance.addOvine(new Ovine(OvineType.SHEEP));
        }
        //creo dei ram e li aggiungo
        for (i = 0; i < numberOfOvine; i++) {
            instance.addOvine(new Ovine(OvineType.RAM));
        }

        //uso la getMyOvines per testare che siano stati inserirti nella regione
        List<Ovine> myOvines = instance.getMyOvines();

        //controllo che ci siano gli ovini cosi come li ho inseriti
        //controllo i lamb; 
        for (i = 0; i < numberOfOvine; i++) {
            assertSame(myOvines.get(i).getType(), OvineType.LAMB);
        }
        //controllo sheep
        for (j = 0; j < numberOfOvine; j++, i++) {
            assertSame(myOvines.get(i).getType(), OvineType.SHEEP);
        }
        //controllo i ram
        for (j = 0; j < numberOfOvine; j++, i++) {
            assertSame(myOvines.get(i).getType(), OvineType.RAM);
        }
        try {
            //elimino tutte le pecore
            for (i = 0; i < numberOfOvine; i++) {
                instance.removeOvine(OvineType.SHEEP);
            }
        } catch (NoOvineException ex) {
            fail("Non è possibile rimuovere le pecore");
        }

        //elimino anche gli agnelli
        try {
            for (i = 0; i < numberOfOvine; i++) {
                instance.removeOvine(OvineType.LAMB);
            }
        } catch (NoOvineException ex) {
            fail("Non è possibile rimuovere gli agnelli");
        }

        //elimino i montoni
        try {
            for (i = 0; i < numberOfOvine; i++) {
                instance.removeOvine(OvineType.RAM);
            }
        } catch (NoOvineException ex) {
            fail("Non è possibile rimuovere i montoni");
        }
        //controllo non ci sia nulla rimuovendo animali a caso 
        for (i = 0; i < exCounterExpected; i++) {
            try {
                instance.removeOvine(OvineType.getRandomOvineType());
            } catch (NoOvineException ex) {
                exCounter++;
            }
        }
        assertEquals(exCounterExpected, exCounter);

        //aggiungo un agnello,una pecora,un montone nell'ordine per 5 volte
        for (i = 0; i < numberOfOvine; i++) {
            instance.addOvine(new Ovine(OvineType.LAMB));
            instance.addOvine(new Ovine(OvineType.SHEEP));
            instance.addOvine(new Ovine(OvineType.RAM));
        }
        //elimino tutte le pecore meno una
        try {
            for (i = 0; i < numberOfOvine - 1; i++) {
                instance.removeOvine(OvineType.SHEEP);
            }
        } catch (NoOvineException ex) {
            fail("L'unica pecora rimasta non è stata trovata");
        }
        //elimino lal 5 pecora
        try {
            instance.removeOvine(OvineType.SHEEP);
        } catch (NoOvineException ex) {
            fail("Non ho trovato l'ultima pecora");
        }
        //elimno la 6 pecora e fallisco
        try {
            instance.removeOvine(OvineType.SHEEP);
            fail("Ho ottenuto una pecora che non c'era");
        } catch (NoOvineException ex) {
            assertTrue(true);
        }
    }

    /**
     * Test of isAllFenced method, of class Region.
     */
    @Test
    public void testIsAllFenced() {
        //Il test è fortemente dipendente dalla struttura della mappa nonchè  
        //dalla struttura dati usata per implementarla
        System.out.println("isAllFenced");
        //faccio il setu up della mappa
        Map map = new Map();
        map.setUp();
        //scelgo una regione e aggiungo recinti a tutte le sue strade
        String stringedRegion1 = "3"; //questa avrà tutti recinti
        Region region = new Region(); //la istanzio solo perchè altrimenti mi dice che potrebbe non essere stata instanziata
        try {
            region = map.convertStringToRegion(stringedRegion1); //converto l'id della stringa in regione
            List<Node> neighbourNodes = region.getNeighbourNodes(); //prendo i suoi nodi vicini
            for (Node nodo : neighbourNodes) {//per ogni nodo
                if (nodo instanceof Street) {//se è una strada
                    Street street = (Street) nodo;
                    street.setFence(new Fence(true)); //gli aggiungo un recinto
                }

            }
        } catch (RegionNotFoundException ex) {
            fail("Regione1 non trovata, ma ti pare??");
        }
        //testo che sia allFences
        assertTrue(region.isAllFenced());

        //scelgo un'altra regione
        String stringedRegion2 = "0"; //questa avrà 2 recinti su 3
        //ci metto recinti su tutte le strade tranne una
        //considerando la mappa e che già ci sono recinti nella regione 3
        Region region2 = new Region();
        try {
            region2 = (Region) map.convertStringToRegion(stringedRegion2);//converto l'id in regione
            map.getStreetByValue(region2, 3).setFence(new Fence(false)); //prendo la strada con valore 3 e ci metto un recinto
        } catch (RegionNotFoundException ex) {
            fail("Regione2 non trovata, ma ti pare??");
        } catch (StreetNotFoundException ex) {
            fail(ex.getMessage());
        }
        //testo
        assertFalse(region2.isAllFenced());
        //scelgo un'altra regione
        String stringedRegion3 = "1"; //questa ha zero recinti
        Region region3 = new Region();
        try {
            region3 = map.convertStringToRegion(stringedRegion3);
        } catch (RegionNotFoundException ex) {
            fail("Regione3 non trovata, ma ti pare??");
        }
        assertFalse(region3.isAllFenced());

        try {
            //test super compatto
            assertFalse(map.convertStringToRegion("15").isAllFenced());
            assertFalse(map.convertStringToRegion("9").isAllFenced());
            assertFalse(map.convertStringToRegion("11").isAllFenced());

            map.getStreetByValue(region2, 1).setFence(new Fence(false));
            assertTrue(region2.isAllFenced());
        } catch (RegionNotFoundException ex) {
            fail("Regione non trovata, ma ti pare??");
        } catch (StreetNotFoundException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of addOvine method, of class Region.
     */
    @Test
    public void testAddOvine() {
        System.out.println("addOvine");
        Ovine ovine = new Ovine(OvineType.RAM);
        Region instance = new Region();
        instance.addOvine(ovine);

        assertSame(ovine, instance.getMyOvines().get(0));
    }

    /**
     * Test of removeOvine method, of class Region.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testRemoveOvine() throws Exception {
        System.out.println("removeOvine");
        Ovine ovine = new Ovine(OvineType.RAM);
        Region instance = new Region();
        instance.addOvine(ovine);

        assertSame(ovine, instance.getMyOvines().get(0));

        //tolgo l'unico ram
        instance.removeOvine(OvineType.RAM);

        for (int i = 0; i < 100; i++) {
            try {
                instance.removeOvine(OvineType.getRandomOvineType());
                fail("Non è possibile recuperare degli ovini che non ci sono");
            } catch (NoOvineException e) {
                // ho tolto l'unico ram dalla regione, qualsiasi ovino chiedo fallirà
            }
        }

    }

    /**
     * Test of isPossibleToMeetSheepWith method, of class Region.
     * @throws NoOvineException
     */
    @Test
    public void testIsPossibleToMeetSheepWith() throws NoOvineException {
        System.out.println("isPossibleToMeetSheepWith");

        OvineType otherOvineType = OvineType.SHEEP;
        Region instance = new Region();

        //metto nella regione una sola pecora
        instance.addOvine(new Ovine(OvineType.SHEEP));

        boolean expResult = false;
        boolean result = instance.isPossibleToMeetSheepWith(otherOvineType);
        assertEquals(expResult, result);

        //aggiungo un altra pecora
        instance.addOvine(new Ovine(OvineType.SHEEP));
        result = instance.isPossibleToMeetSheepWith(otherOvineType);
        assertTrue(result);

        assertFalse(instance.isPossibleToMeetSheepWith(OvineType.RAM));

        //aggiungo un ram
        instance.addOvine(new Ovine(OvineType.RAM));
        assertTrue(instance.isPossibleToMeetSheepWith(OvineType.RAM));

        //tolgo una pecora
        instance.removeOvine(OvineType.SHEEP);
        assertTrue(instance.isPossibleToMeetSheepWith(OvineType.RAM));

        //tolgo l'ultima pecora
        instance.removeOvine(OvineType.SHEEP);
        assertFalse(instance.isPossibleToMeetSheepWith(OvineType.RAM));

        //aggiungo una pecora
        instance.addOvine(new Ovine(OvineType.SHEEP));
        assertTrue(instance.isPossibleToMeetSheepWith(OvineType.RAM));

    }

    /**
     * Test of hasOvine method, of class Region.
     */

    @Test
    public void testHasOvine() {
        System.out.println("hasOvine");
        OvineType thisOvineType = OvineType.LAMB;
        Region instance = new Region();
        boolean expResult = false;
        boolean result = instance.hasOvine(thisOvineType);
        assertEquals(expResult, result);
        
        //aggiungo un lamb e vedo che dice
        instance.addOvine(new Ovine(OvineType.LAMB));
        
        assertTrue(instance.hasOvine(thisOvineType));
    }
}
