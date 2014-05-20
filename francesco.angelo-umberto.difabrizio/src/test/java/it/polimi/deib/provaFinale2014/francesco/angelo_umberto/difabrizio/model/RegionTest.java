package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;
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
        ArrayList<Ovine> result = instance.getMyOvines();
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
     * Test of addOvine method, of class Region.
     */
    @Test
    public void testAllRegionMethods() {
        System.out.println("addOvine");
        
        int numberOfOvine = 5;
        int i;
        int j;
        //creo una regione a caso
        Region instance = new Region();

        //creo dei lamb e li aggiungo; 
        for ( i = 0; i < numberOfOvine; i++) {
            instance.addOvine( new Ovine(OvineType.LAMB));
        }
        //creo dei sheep e li aggiungo
        for ( i = 0; i < numberOfOvine; i++) {
            instance.addOvine(new Ovine(OvineType.SHEEP));
        }
        //creo dei ram e li aggiungo
        for ( i = 0; i < numberOfOvine; i++) {
            instance.addOvine(new Ovine(OvineType.RAM));
        }
        
        //uso la getMyOvines per testare che siano stati inserirti nella regione
        ArrayList<Ovine> myOvines = instance.getMyOvines();
        
        //controllo che ci siano gli ovini cosi come li ho inseriti
        //controllo i lamb; 
        for ( i = 0; i < numberOfOvine; i++) {
            assertSame(myOvines.get(i).getType(),OvineType.LAMB);
        }
        //controllo sheep
       for ( j = 0; j < numberOfOvine; j++) {
            assertSame(myOvines.get(i).getType(),OvineType.SHEEP);
        }
        //creo dei ram e li aggiungo
       for ( j = 0; j < numberOfOvine; j++) {
            assertSame(myOvines.get(i).getType(),OvineType.RAM);
        }
       
       //elimino tutte le pecore
       //controllo che non ci siano
       //controllo che ci siano ancora agnelli e montoni
       
       //elimino anche gli agnelli
       //controllo ci siano sono montoni
       
       //elimino i montoni
       //controllo non ci sia nulla
       
       //aggiungo un agnello,una pecora,un montone nell'ordine per 5 volte
    }

    /**
     * Test of isAllFenced method, of class Region.
     */
    @Test
    public void testIsAllFenced() {
        System.out.println("isAllFenced");
        Region instance = new Region();
        boolean expResult = false;
        boolean result = instance.isAllFenced();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasOvine method, of class Region.
     */
    @Test
    public void testHasOvine() {
        System.out.println("hasOvine");
        OvineType ovineType = null;
        Region instance = new Region();
        Ovine expResult = null;
        Ovine result = instance.hasOvine(ovineType);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeOvine method, of class Region.
     */
    @Test
    public void testRemoveOvine() {
        System.out.println("removeOvine");
        Ovine ovine = null;
        Region instance = new Region();
        //instance.removeOvine();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addOvine method, of class Region.
     */
    @Test
    public void testAddOvine() {
        System.out.println("addOvine");
        Ovine ovine = null;
        Region instance = new Region();
        instance.addOvine(ovine);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
