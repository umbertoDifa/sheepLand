package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.util.concurrent.Callable;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class TimerCallable implements Callable<Void>{
    private final int timer;

    public TimerCallable(int timer) {
        this.timer = timer;
    }
    
    public Void call() throws Exception {
        try{
        Thread.sleep(timer);
        } catch (InterruptedException e){
            //TODO junk: gestire sta eccezione
        }          
        throw new TimerException("Timeout connessione al server per la partita"); 
    }
    
}
