package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

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
public class OvineTest {

    public OvineTest() {
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
     * Test of getType method, of class Ovine.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        //creo un ovino di tipo Lamb
        Ovine lambInstance = new Ovine(OvineType.LAMB);

        //creo un ovino di tipo Ram
        Ovine ramInstance = new Ovine(OvineType.RAM);

        //creo un ovino di tipo Sheep
        Ovine sheepInstance = new Ovine(OvineType.SHEEP);

        //salvo il tipo di ovino che mi aspetto
        OvineType expSheep = OvineType.SHEEP;
        OvineType expLamb = OvineType.LAMB;
        OvineType expRam = OvineType.RAM;

        OvineType sheepResult = sheepInstance.getType();
        OvineType ramResult = ramInstance.getType();
        OvineType lambResult = lambInstance.getType();

        //controllo se il tipo che mi aspetto è giusto
        assertEquals(expSheep, sheepResult);
        assertEquals(expRam, ramResult);
        assertEquals(expLamb, lambResult);

        //controllo i tipi che non mi aspetto
        assertNotEquals(expSheep, lambResult);
        assertNotEquals(expRam, sheepResult);
        assertNotEquals(expLamb, ramResult);
    }

    /**
     * Test of setType method, of class Ovine.
     */
    @Test
    public void testSetType() {
        System.out.println("setType");
        //creo un ovino lamb
        Ovine instance = new Ovine(OvineType.LAMB);
        
        //lo trasformo in ram
        instance.setType(OvineType.RAM);
        
        assertSame(OvineType.RAM, instance.getType()); //conversione di tipo a buon fine
        
        //poi lo trasformo in sheep
         instance.setType(OvineType.SHEEP);
        
        assertSame(OvineType.SHEEP, instance.getType()); //conversione di tipo a buon fine
        
        //poi lo ritrasformo in lamb
        instance.setType(OvineType.LAMB);
        
        assertSame(OvineType.LAMB, instance.getType()); //conversione di tipo a buon fine
       
    }

}
