package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility;

import java.util.logging.Logger;

/**
 *Utility debug to print to the console a given String if the debug is on
 * @author francesco.angelo-umberto.difabrizio
 */
public class DebugLogger {

    private static final boolean DEBUG = false;
    private static final boolean PARENT_HANDLERS_ON = false;

    private DebugLogger() {
    }
    /**
     * Prints a sentece in debug mode
     * @param debugPrint 
     */
    public static void println(String debugPrint) {
        //se il debug è attivo
        if (DEBUG) {
            //stampa la stringa
            System.out.println("debug: " + debugPrint);
        }
        //altrimenti nulla
    }
    /**
     * It turns off the debug exception if the DEBUG variable is off
     */
    public static void turnOffExceptionLog() {
        Logger.getLogger(DebugLogger.class
                .getName()).setUseParentHandlers(DebugLogger.PARENT_HANDLERS_ON);
    }

}
