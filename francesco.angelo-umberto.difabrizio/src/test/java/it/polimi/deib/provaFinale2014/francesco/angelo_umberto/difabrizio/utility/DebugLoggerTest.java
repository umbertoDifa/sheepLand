package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Umberto
 */
public class DebugLoggerTest {

    /**
     * Test of println method, of class DebugLogger.
     */
    @Test
    public void testPrintln() {
        System.out.println("println");
        String debugPrint = "funge";
        DebugLogger.println(debugPrint);
    }

    /**
     * Test of turnOffExceptionLog method, of class DebugLogger.
     */
    @Test
    public void testTurnOffExceptionLog() {
        System.out.println("turnOffExceptionLog");
        DebugLogger.turnOffExceptionLog();

    }

}
