package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *Classe del client visto dal server, contiene il socket e gli stream per parlarci
 * @author francesco.angelo-umberto.difabrizio
 */
public class Sclient {

    private final Socket socket;
    private Scanner fromClient;
    private PrintWriter toClient;

    public Sclient(Socket socket) {
        this.socket = socket;
        try {
            this.toClient = new PrintWriter(this.socket.getOutputStream()); //inizializzo stream out
            this.fromClient = new Scanner(this.socket.getInputStream());    //inizializzo streamo in
        } catch (IOException ex) {//se fallisce la creazione di un canale di scambio dati
            System.err.println(ex.getMessage()); //TODO: provalo
        }
    }
    /**
     * Manda un messaggio al client attraverso il proprio socket
     * @param message 
     */
    public void send(String message){
        toClient.println(message);
        toClient.flush();
    }
    /**
     * Riceve un messaggio dal client
    */
    public String receive(){
       return fromClient.nextLine();
    }

}
