package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.exceptions.ActionCancelledException;
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
    private int playersToWaitBefore;
    private int playersToWaitAfter;
    private int numberOfAction;

    private int i;
    private boolean actionCompleted;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startClient() {
        //TODO: importante! verficare cosa succede se un client si connette e si disconnette subito!
        //viene contato nell'accept del server ma poi a conti fatti non ci sarà!
        //TODO e se disconnette il server?
        //TODO: se un player inserisce delle cose metre sta giocando un'altro? flush stdin prima di iniziare?
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

            //raccolgo saluto TODO: potrei raccogliere un rifiuto, aggiustare
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
            this.numberOfAction = Character.getNumericValue(token[5].charAt(0));

            playersToWaitBefore = (((me - firstPlayer) + 2 * numberOfPlayers) % numberOfPlayers);
            playersToWaitAfter = (numberOfPlayers - playersToWaitBefore - 1);

            DebugLogger.println(
                    numberOfPlayers + " " + me + " " + shepherds4player + " "
                    + firstPlayer + " " + numberOfAction);

            //setUpPastori
            setUpSheperds();

            //ricevi Inizio gioco
            System.out.println(receiveString());

            //inizia i giri            
            this.executeRound();

        } catch (IOException ex) {
            //Si verifica se porta sbagliata
            //o se il server non risponde entro un timeout
            //o se il server è spento
            System.err.println(
                    "Ci scusiamo, il server è momentaneamente non disponibile.\n"
                    + "La preghiamo di verificare la porta e l'indirizzo ip.");
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(),
                    ex);
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

    private void putShepherds() {
        for (i = 0; i < shepherds4player; i++) {
            makeChoiceUntil("posizionato");
        }
    }

    /**
     * It takes a question from the server, grab the user answer and send it
     * back. If the server likes the answer, it sends a string that contains the
     * word acceptString so that the client knows that everything went ok. If
     * the answer does not contain the acceptString the client tries to send a
     * new answer and so on till the user string fits the server standards.
     *
     * @param acceptString Word to receive to understand that the comunication
     *                     went ok.
     */
    private void makeChoiceUntil(String acceptString) {
        while (true) {
            String result;
            DebugLogger.println("rispondo scelta:");

            //scelgo azione
            talkTo();
            //ricevo ok o err
            result = receiveString();
            DebugLogger.println("Ricevuta " + result);
            if (result.contains(acceptString)) {
                break;
            }
            //errore
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

        //getInfoOthersSheperds
        this.refreshInfo(playersToWaitBefore * shepherds4player);

        //putSheperds
        this.putShepherds();

        //getInfoOthersSheperds
        this.refreshInfo(playersToWaitAfter * shepherds4player);
    }

    private void executeShift() {
        String result;

        //getInfoOthersSheperds
        //ogni giocatore ha 3 azioni più la
        //mossa della pecora nera
        this.refreshInfo(playersToWaitBefore * (numberOfAction + 1));
        DebugLogger.println("refreshInfo terminata");

        //ricevi info mia pecora nera
        System.out.println(receiveString());

        for (i = 0; i < numberOfAction; i++) {
            while (true) {
                DebugLogger.println("Inizio azione");

                //scelta azione
                makeChoiceUntil("ok");

                //prima risposta
                makeChoiceUntil("ok");

                //seconda risposta
                makeChoiceUntil("ok");

                //ricevo verdetto
                DebugLogger.println("ricevo verdetto");

                result = receiveString();
                DebugLogger.println("Ricevuta " + result);

                if (result.contains("successo")) {
                    break;
                }
            }

        }

        //getInfoOthersSheperds
        this.refreshInfo(playersToWaitAfter * (numberOfAction + 1));
    }

    private void executeRound() {
        while (true) {
            executeShift();

            //controlla lupo
            System.out.println(receiveString());
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5050);
        client.startClient();
    }
}
