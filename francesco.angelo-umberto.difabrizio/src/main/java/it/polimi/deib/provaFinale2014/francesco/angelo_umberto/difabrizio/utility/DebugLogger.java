package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class DebugLogger {

    private static final boolean DEBUG = true;

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

}
