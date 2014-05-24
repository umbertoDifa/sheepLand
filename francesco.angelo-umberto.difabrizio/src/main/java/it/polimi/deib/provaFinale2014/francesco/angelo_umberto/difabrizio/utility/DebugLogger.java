package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility;

import java.util.logging.Logger;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class DebugLogger {

    private static final boolean DEBUG = true;
    public static final boolean PARENT_HANDLERS_ON = true;
    
    
            
    private DebugLogger() {        
    }

    public static void println(String debugPrint) {
        //se il debug è attivo
        if (DEBUG) {

            //stampa la stringa
            System.out.println(debugPrint);
        }       
        //altrimenti nulla
    }
    
    public static void turnOffExceptionLog(){
        Logger.getLogger(DebugLogger.class
                        .getName()).setUseParentHandlers(DebugLogger.PARENT_HANDLERS_ON);
    }

}
