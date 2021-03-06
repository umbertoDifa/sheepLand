package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Umberto
 */
public class StreetTest {

    /**
     * Test of hasFence method, of class Street.
     */
    @Test
    public void testHasFence() {
        System.out.println("hasFence");
        //creo una strada
        Street instance = new Street(3);

        //che non ha recinto
        assertFalse(instance.hasFence());

        //glielo metto
        instance.setFence(new Fence(true));

        //e ora ce l'ha
        assertTrue(instance.hasFence());
    }

    /**
     * Test per chiarire le modalità e proprietà del casting
     */
    @Test
    public void testCast() {
        //creo un nodo con tipo dinamico strada
        Node nodoStrada = new Street(1);

        //lo casto a strada
        Street street = (Street) nodoStrada;

        //aggiungo una fence alla strada
        street.setFence(new Fence(false));

        //ricasto il nodo su un'altra strada
        Street secondStreet = (Street) nodoStrada;
        //la seconda strada ha un recinto?
        assertTrue(secondStreet.hasFence());
        //la prima strada è uguale alla secnoda?
        assertEquals(street, secondStreet);
        //la prima strada ha lo stesso indirizzo della secnoda?
        assertSame(street, secondStreet);
        //l'indirizzo del nodo è quello della strada?
        assertSame(nodoStrada, street);
    }

    /**
     * Test of hasShepherd method, of class Street.
     */
    @Test
    public void testHasShepherd() {
        System.out.println("hasShepherd");
        //creo una stada
        Street instance = new Street(0);

        //testo che non abbia un pastore
        assertFalse(instance.hasShepherd());

        //aggiungo un pastore
        instance.setShepherd(new Shepherd(GameConstants.STANDARD_WALLET_AMMOUNT.getValue()));

        //testo che abbia un pastore
        assertTrue(instance.hasShepherd());

        //elimino il pastore nella strada
        instance.setShepherd(null);
        //testo

        assertFalse(instance.hasShepherd());
        //lo riaggiungo

        instance.setShepherd(new Shepherd(GameConstants.STANDARD_WALLET_AMMOUNT.getValue()));

        //testo
        assertTrue(instance.hasShepherd());

    }

    /**
     * Test of setFence method, of class Street.
     */
    @Test
    public void testSetFence() {
        System.out.println("setFence");
        //aggiungo una strada
        Street street = new Street(0);

        //testo che non abbia il recinto
        assertFalse(street.hasFence());

        //aggiungo un recinto
        street.setFence(new Fence(false));

        //testo che lo abbia
        assertTrue(street.hasFence());
    }

    /**
     * Test of isFree method, of class Street.
     */
    @Test
    public void testIsFree() {
        System.out.println("isFree");

        //creo una strada
        Street instance = new Street(0);

        //testo is free
        assertTrue(instance.isFree());

        //aggiungo un recinto
        instance.setFence(new Fence(true));

        //testo is free
        assertFalse(instance.isFree());

        //creo un'altra strada
        Street instance2 = new Street(0);

        //testo is free
        assertTrue(instance2.isFree());

        //aggiungo un pastore
        instance2.setShepherd(new Shepherd(GameConstants.STANDARD_WALLET_AMMOUNT.getValue()));

        //testo is free
        assertFalse(instance2.isFree());

        //rimuovo il pastore
        instance2.setShepherd(null);

        //testo
        assertTrue(instance2.isFree());

        //riaggiungo un pastore
        instance2.setShepherd(new Shepherd(GameConstants.STANDARD_WALLET_AMMOUNT.getValue()));

        //aggiungo una fence
        instance2.setFence(new Fence(true));

        //testo is free
        assertFalse(instance2.isFree());

        //annullo il pastore
        instance2.setShepherd(null);

        //testo is free
        assertFalse(instance2.isFree());

        //riaggiungo un pastore
        instance2.setShepherd(new Shepherd(GameConstants.STANDARD_WALLET_AMMOUNT.getValue()));

        //testo is free
        assertFalse(instance2.isFree());

        //aggiungo un pastore alla prima
        instance.setShepherd(new Shepherd(GameConstants.STANDARD_WALLET_AMMOUNT.getValue()));

        //testo il free
        assertFalse(instance.isFree());

    }

    /**
     * Test of getValue method, of class Street.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Street instance = new Street(3);
        int expResult = 3;
        int result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of setShepherd method, of class Street.
     */
    @Test
    public void testSetShepherd() {
        System.out.println("setShepherd");
        Shepherd shepherd = new Shepherd(GameConstants.STANDARD_WALLET_AMMOUNT.getValue());

        Street instance = new Street(2);

        instance.setShepherd(shepherd);

        assertSame(shepherd, instance.getShepherd());

        Shepherd sph2 = new Shepherd(GameConstants.STANDARD_WALLET_AMMOUNT.getValue());

        instance.setShepherd(sph2);

        assertSame(sph2, instance.getShepherd());
    }

    /**
     * Test of getShepherd method, of class Street.
     */
    @Test
    public void testGetShepherd() {
        System.out.println("getShepherd");
        Street instance = new Street(4);
        Shepherd expResult = new Shepherd(GameConstants.STANDARD_WALLET_AMMOUNT.getValue());
        instance.setShepherd(expResult);
        Shepherd result = instance.getShepherd();
        assertEquals(expResult, result);

    }

}
