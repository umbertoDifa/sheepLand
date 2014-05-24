package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
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

    //server info
    private final String ip;
    private final int port;

    //canali di comunicazione
    Scanner serverIn;
    PrintWriter serverOut;
    Scanner stdIn; //tastiera

    //game info
    private int numberOfPlayers;
    private int me;
    private int firstPlayer;
    private int shepherds4player;
    int i;

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
            DebugLogger.println("Canali di comunicazione impostati");

            //raccolgo saluto
            System.out.println(receiveString());

            //raccolgo inforamzioni di base sulla partita
            String gameInfo = receiveString();
            System.out.println(gameInfo);
            String delimiter = ":";
            String token[] = gameInfo.split(delimiter);

            this.numberOfPlayers = Character.getNumericValue(token[1].charAt(0));
            this.me = Character.getNumericValue(token[2].charAt(0));
            this.shepherds4player = Character.getNumericValue(token[3].charAt(0));
            this.firstPlayer = Character.getNumericValue(token[4].charAt(0));
            DebugLogger.println(
                    numberOfPlayers + " " + me + " " + shepherds4player + " " + firstPlayer);

            //setUpPastori
            setUpSheperds();

            //ricevi info pecora nera
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

    private void talkTo() {
        //stampo a video la stringa
        System.out.println(receiveString());

        //raccolgo la risposta dell'utente e la invio
        sendString(stdIn.nextLine());
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5050);
        client.startClient();
    }

    private void putShepherds() {
        for (i = 0; i < shepherds4player; i++) {
            while (true) {
                //posiziono il pastore
                talkTo();

                //ricevo la risposta
                String answer = receiveString();
                System.out.println(answer);
                if (answer.contains("posizionato")) {
                    //tutto ok
                    break;
                } else {
                    //occuapta o non esistente
                }
            }
        }
    }

    /**
     * Prende dallo stream n stringhe
     *
     * @param times Numero di stringhe da prendere
     */
    private void refreshInfo(int times) {
        for (i = 0; i < times; i++) {
            System.out.println(receiveString());
        }
    }

    private void setUpSheperds() {
        int playersToWaitBefore = ((me - firstPlayer) + 2 * numberOfPlayers) % numberOfPlayers;
        int playersToWaitAfter = numberOfPlayers - playersToWaitBefore - 1;
        
        if (me == firstPlayer) {
            //putShepherds
            this.putShepherds();

            //getInfoOtherSheperds
            this.refreshInfo(numberOfPlayers - 1);

        } else {
            //getInfoOthersSheperds
            this.refreshInfo(playersToWaitBefore);

            //putSheperds
            this.putShepherds();

            //getInfoOthersSheperds
            this.refreshInfo(playersToWaitAfter);
        }
    }
}
