package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *implementa la nostra personale gestione per il rifiuto dei client
 * @author francesco.angelo-umberto.difabrizio
 */
public class ClientRejectionHandler implements RejectedExecutionHandler{
    
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
       //TODO: impostare cosa succede quando un thread viene rifiutato
    }
}
