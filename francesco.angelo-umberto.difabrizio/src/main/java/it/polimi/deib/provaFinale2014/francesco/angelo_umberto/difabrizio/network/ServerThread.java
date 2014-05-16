package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class ServerThread implements Runnable{
    private final ArrayList<Socket> clientSockets;
    
    public ServerThread(ArrayList<Socket> clientSockets){
        this.clientSockets = clientSockets;
    }

    public void run() {
        //TODO: chiaramente da implementare
    }
}
