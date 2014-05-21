package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

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

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startClient() {
        //TODO: importante! verficare cosa succede se un client si connette e si disconnette subito!
        //viene contato nell'accept del server ma poi a conti fatti non ci sarà!
        //TODO e se disconnette il server?
        try {
            Socket socket = new Socket(ip, port);
            System.out.println("Connessione stabilita");
            Scanner stdIn = new Scanner(System.in);
            System.out.println(stdIn.nextLine());
            String stop = stdIn.nextLine();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void provaClient() {
        try {
            Socket socket = new Socket(ip, port);
            System.out.println("Connessione stabilita");
            Scanner socketIn = new Scanner(socket.getInputStream()); //creo input buffer del socket
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream());//creo output buffer socket
            Scanner stdIn = new Scanner(System.in); //creo input buffer da tastiera

            System.out.println("Inserisci messaggio:");
            String line = stdIn.nextLine();
            socketOut.println(line);
            socketOut.flush();
            String serverLine = socketIn.nextLine();
            System.out.println(serverLine);
        } catch (IOException ex) {
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5050);
        client.startClient();
    }
}
