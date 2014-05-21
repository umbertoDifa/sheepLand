package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Node;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Region;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.RegionType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco
 */
public class NodeTest {
    
    public NodeTest() {
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
}