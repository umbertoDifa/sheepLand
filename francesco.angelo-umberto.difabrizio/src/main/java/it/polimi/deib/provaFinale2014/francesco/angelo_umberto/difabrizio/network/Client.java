package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.DebugLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Client {

    private final String ip;
    private final int port;

    //canali di comunicazione
    Scanner serverIn;
    PrintWriter serverOut;
    Scanner stdIn; //tastiera

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startClient() {
        //TODO: importante! verficare cosa succede se un client si connette e si disconnette subito!
        //viene contato nell'accept del server ma poi a conti fatti non ci sarà!
        //TODO e se disconnette il server?
        try {
            //creo socket server
            Socket socket = new Socket(ip, port);
            DebugLogger.println("Connessione stabilita");
            //System.out.println("Connessione stabilita");

            //creo scanner ingresso server
            serverIn = new Scanner(socket.getInputStream());

            //creo printwriter verso server
            serverOut = new PrintWriter(socket.getOutputStream());

            //creo scanner da tastiera
            stdIn = new Scanner(System.in);
            DebugLogger.println("canali di comunicazione impostati");

            //raccolgo saluto
            DebugLogger.println(receiveString());
            
            
            //setUpPastori//TODO caso più pastori                  
            talkTo();
            
            //ricevi riepilogo pastore
            System.out.println(receiveString());
            DebugLogger.println("Sono qui e poi muoio");
            
            

        } catch (IOException ex) {
            //TODO gestire eccezione
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String receiveString() {
        return serverIn.nextLine();
    }

    private void sendString(String message) {
        serverOut.println(message);
        serverOut.flush();
    }
    
    private void talkTo(){
        //stampo a video la stringa
        System.out.println(receiveString());
        
        //raccolgo la risposta dell'utente e la invio
        sendString(stdIn.nextLine());
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5050);
        client.startClient();
    }
}
