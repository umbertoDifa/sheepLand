
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
     * Test dei costruttori di Bank e Card.
     * Test dei metodi loadCard e getCard di Bank
     * @throws java.lang.Exception
     */
    @Test
    public void testGetCard() throws Exception {
        System.out.println("getCard");
        RegionType type = RegionType.COUNTRYSIDE; //creo una carta country
        Bank instance = new Bank(GameConstants.NUM_CARDS.getValue(), GameConstants.NUM_FENCES.getValue()); //inizializzo una bank con 3 carte e 3 recinti
        Card expResult = new Card(2, type); //creo una carta di valore 2 e tipo country
        instance.loadCard(expResult, RegionType.COUNTRYSIDE.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue());//la carico in corrispondenza di dove inizia il suo indice(2)
        try{
            Card result = instance.getCard(type);
            assertSame("Card aggiunta == carta ricevuta",expResult, result);
        }catch(MissingCardException e){
            fail("get Card solleva eccezione quando provo a prelevare "+e.getMessage()
                    + "una carte di un tipo presente fra le cards");
        }
        
    }
    //TODO commenta e finisci
    @Test (expected = MissingCardException.class)
    public void testGetCardException() throws MissingCardException{
        System.out.println("getCard");
        RegionType type = RegionType.COUNTRYSIDE; //creo una carta country
        Bank instance = new Bank(GameConstants.NUM_CARDS.getValue(), GameConstants.NUM_FENCES.getValue()); //inizializzo una bank con 3 carte e 3 recinti
        Card expResult = new Card(2, type); //creo una carta di valore 2 e tipo country
        //instance.loadCard(expResult, RegionType.COUNTRYSIDE.getIndex() * GameConstants.NUM_CARDS_FOR_REGION_TYPE.getValue());//la carico in corrispondenza di dove inizia il suo indice(2)
        
            Card result = instance.getCard(type);
            assertSame("Card aggiunta == carta ricevuta",expResult, result);
       
        
    }
    
}
