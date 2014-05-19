
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.MissingCardException;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco
 */
public class BankTest {
    
    public BankTest() {
    }

    /**
     * Test of getCard method, of class Bank.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetCard() throws Exception {
        System.out.println("getCard");
        RegionType type = RegionType.COUNTRYSIDE;
        Bank instance = new Bank(3, 3);
        Card expResult = new Card(2, type);
        instance.loadCard(expResult, 0);
        try{
            Card result = instance.getCard(type);
            assertSame("Card aggiunta =! carta messa",expResult, result);
        }catch(MissingCardException e){
            fail("get Card solleva eccezione quando provo a prelevare "
                    + "una carte di un tipo presente fra le cards");
        }
        
    }
    
}
