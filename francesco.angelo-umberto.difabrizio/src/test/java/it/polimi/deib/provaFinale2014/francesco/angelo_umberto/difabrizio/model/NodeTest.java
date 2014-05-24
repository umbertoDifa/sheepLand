package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco
 */
public class NodeTest {

    public NodeTest() {
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
     * Test of isNeighbour method, of class Node.
     */
    @Test
    public void testIsNeighbour() {
        System.out.println("isNeighbour");
        Node node1 = new Street(0);
        Node node2 = new Street(1);
        Node node3 = new Region();
        Node node4 = new Region(RegionType.HILL);
        Node node5 = new Street(0);

        node1.connectTo(node3);
        node2.connectTo(node4);
        node2.connectTo(node5);

        boolean expResult = false;
        boolean result = node1.isNeighbour(node2);
        assertEquals(expResult, result);
        assertEquals(true, node2.isNeighbour(node4));
        assertEquals(false, node4.isNeighbour(node1));
        assertEquals(true, node3.isNeighbour(node1));
        assertEquals(true, node2.isNeighbour(node5));
    }

    /**
     * Test of connectTo method, of class Node.
     */
    @Test
    public void testConnectTo() {
        System.out.println("connectTo");
        Node node1 = new Street(10);
        Node node2 = new Street(16);

        assertFalse(node1.isNeighbour(node2));
        assertFalse(node2.isNeighbour(node1));

        node1.connectTo(node2);

        assertTrue(node1.isNeighbour(node2));
        assertTrue(node2.isNeighbour(node1));
    }

    /**
     * Test of getNeighbourNodes method, of class Node.
     */
    @Test
    public void testGetNeighbourNodes() {
        System.out.println("getNeighbourNodes");

        //creo una regione
        Node reg1 = new Region(RegionType.HILL);

        //creo un'altra regione e 3 strade 
        Node reg2 = new Region(RegionType.LAKE);
        Node str1 = new Street(3);
        Node str2 = new Street(2);
        Node str3 = new Street(6);

        //connetto (regione e strade) e )regione e regione)
        reg1.connectTo(reg2);
        reg1.connectTo(str3);
        reg1.connectTo(str2);
        reg1.connectTo(str1);

        List<Node> expectedNeighbours = new ArrayList<Node>();
        expectedNeighbours.add(reg2);
        expectedNeighbours.add(str1);
        expectedNeighbours.add(str2);
        expectedNeighbours.add(str3);

        assertFalse(expectedNeighbours.isEmpty());

        //prendo i vicini       
        List<Node> neighbours = reg1.getNeighbourNodes();

        //tolgo dai nodi che dovrebbero essere vicini quelli 
        //effettivamente ottenuti
        expectedNeighbours.removeAll(neighbours);

        assertTrue(expectedNeighbours.isEmpty());

    }

    /**
     * Test of getNeighbourStreets method, of class Node.
     */
    @Test
    public void testGetNeighbourStreets() {
        System.out.println("getNeighbourStreets");

        //creo una regione
        Node reg1 = new Region(RegionType.HILL);

        //creo un'altra regione e 3 strade 
        Node reg2 = new Region(RegionType.LAKE);
        Node str1 = new Street(3);
        Node str2 = new Street(2);
        Node str3 = new Street(6);

        //connetto (regione e strade) e )regione e regione)
        reg1.connectTo(reg2);
        reg1.connectTo(str3);
        reg1.connectTo(str2);
        reg1.connectTo(str1);

        List<Street> expectedStreet = new ArrayList<Street>();
        expectedStreet.add((Street) str1);
        expectedStreet.add((Street) str2);
        expectedStreet.add((Street) str3);

        assertFalse(expectedStreet.isEmpty());

        //prendo le strade vicine 
        List<Street> neighbourStreets = reg1.getNeighbourStreets();
        
        //prendo tutti i nodi vicini        
        List<Node> neighbours = reg1.getNeighbourNodes();

        //tolgo dai nodi che dovrebbero essere vicini quelli 
        //effettivamente ottenuti
        expectedStreet.removeAll(neighbourStreets);

        assertTrue(expectedStreet.isEmpty());
        
        //tolgo da i nodi vicini le strade e controllo che ci sia la regione
        
        neighbours.removeAll(neighbourStreets);
        
        assertSame(neighbours.get(0), reg2);
        
    }

    /**
     * Test of getNeighbourRegions method, of class Node.
     */
    @Test
    public void testGetNeighbourRegions() {
        System.out.println("getNeighbourRegions");
        Node reg1 = new Region(RegionType.HILL);

        //creo un'altra regione e 3 strade 
        Node reg2 = new Region(RegionType.LAKE);
        Node str1 = new Street(3);
        Node str2 = new Street(2);
        Node str3 = new Street(6);
        
        //connetto (regione e strade) e (regione e regione)
        reg1.connectTo(reg2);
        reg1.connectTo(str3);
        reg1.connectTo(str2);
        reg1.connectTo(str1);
        
        assertSame(reg1.getNeighbourRegions().get(0), reg2);
    }
   

}
