package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Umberto
 */
public class DiceTest {

    /**
     * Test of roll method, of class Dice.
     */
    @Test
    public void testRoll() {
        System.out.println("roll");

        for (int i = 0; i < 100; i++) {
            int result = Dice.roll();
            assertTrue(result > 0 && result <= 6);
        }

    }

}
