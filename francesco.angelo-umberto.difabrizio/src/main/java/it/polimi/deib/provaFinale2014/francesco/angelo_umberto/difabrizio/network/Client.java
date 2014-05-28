package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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

    private int numberOfAction;

    private int i;
    private static boolean rmi;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startSocketClient() {
        //TODO: importante! verficare cosa succede se un client si connette e si disconnette subito!
        //viene contato nell'accept del server ma poi a conti fatti non ci sarà!
        //TODO e se disconnette il server?
        //TODO: se un player inserisce delle cose metre sta giocando un'altro? flush stdin prima di iniziare?
        try {
            //creo socket server
            Socket socket = new Socket(ip, port);
            System.out.println("Connessione stabilita");

            //creo scanner ingresso server
            serverIn = new Scanner(socket.getInputStream());

            //creo printwriter verso server
            serverOut = new PrintWriter(socket.getOutputStream());

            //creo scanner da tastiera
            stdIn = new Scanner(System.in);
            DebugLogger.println("Canali di comunicazione impostati");

            //ricevo richiesta nick
            System.out.println(receiveString());

            //rispondo nick
            sendString(stdIn.nextLine());

            while (true) {
                String received = receiveString();

                if (received.equals("RefreshRegion")) {

                } else if (received.equals("RefreshStreet")) {

                } else if (received.equals("RefereshGameParameters")) {

                } else if (received.equals("RefereshCurrentPlayer")) {

                } else if (received.equals("RefereshCards")) {

                } else if (received.equals("RefreshBlackSheep")) {

                } else if (received.equals("RefreshWolf")) {

                } else if (received.equals("SetUpShepherds")) {

                } else if (received.equals("ChooseAction")) {

                } else if (received.equals("MoveOvine")) {

                } else if (received.equals("MoveShepherd")) {

                } else if (received.equals("BuyLand")) {

                } else if (received.equals("MateSheepWith")) {

                } else if (received.equals("KillOvine")) {

                }
            }

//            //raccolgo saluto TODO: potrei raccogliere un rifiuto, aggiustare
//            System.out.println(receiveString());
//
//            //raccolgo inforamzioni di base sulla partita
//            String gameInfo = receiveString();
//            System.out.println(gameInfo);
//            String delimiter = ":";
//            String token[] = gameInfo.split(delimiter);
//
//            this.numberOfPlayers = Character.getNumericValue(token[1].charAt(0));
//            this.me = Character.getNumericValue(token[2].charAt(0));
//            this.shepherds4player = Character.getNumericValue(token[3].charAt(0));
//            this.firstPlayer = Character.getNumericValue(token[4].charAt(0));
//            this.numberOfAction = Character.getNumericValue(token[5].charAt(0));
//
//            DebugLogger.println(
//                    numberOfPlayers + " " + me + " " + shepherds4player + " "
//                    + firstPlayer + " " + numberOfAction);
//
//            //setUpPastori
//            setUpSheperds();
//
//            DebugLogger.println("Entro in execute Rounds");
//            //inizia i giri            
//            this.executeRound();
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

    private void startRmiClient() {

        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);

            //cerco l'oggetto nel registry
            ServerRmi serverRmi = (ServerRmi) registry.lookup(
                    "sheepland");

            Scanner stdIn = new Scanner(System.in);
            System.out.println("Inserisci il tuo nickName");
            String nickName = stdIn.nextLine();

            //serverRmi.connect(this, nickName);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "StartRmiClient" + ex.getMessage(), ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    "Il server non è ancora bounded " + ex.getMessage(), ex);
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
        String result;

        while (true) {
            result = receiveString();
            DebugLogger.println("Ricevuta: " + result);

            if (result.contains(acceptString)) {
                break;
            } else {
                DebugLogger.println("rispondo scelta:");
                sendString(stdIn.nextLine());
                result = receiveString();
                DebugLogger.println("Ricevuta: " + result);
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

    private void refreshInfoUntil(String acceptString) {
        String receivedString;

        while (true) {
            receivedString = receiveString();
            System.out.println(receivedString);
            if (receivedString.contains(acceptString)) {
                break;
            }
        }
    }

    private void setUpSheperds() {

        //getInfoOthersSheperds
        this.refreshInfoUntil("E' il tuo turno");
        //putSheperds
        this.putShepherds();
    }

    private void executeShift() {
        String result;

        //getInfoOthersSheperds
        refreshInfoUntil("E' il tuo turno");
        DebugLogger.println("refreshInfo terminata");

        for (i = 0; i < numberOfAction; i++) {

            DebugLogger.println("Inizio azione");

            //scelta azione
            makeChoiceUntil("successo");

            DebugLogger.println("Azione completata!");
        }
    }

    private void executeRound() {
        while (true) {
            executeShift();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5050);

        Scanner stdIn = new Scanner(System.in);
        boolean stringValid = false;
        String answer;
        int choice;

        while (!stringValid) {
            try {
                System.out.println(
                        "Scegli connessione:\n1- Socket\n2- RMI");
                answer = stdIn.nextLine();
                choice = Integer.parseInt(answer);

                if (choice == 1) {
                    stringValid = true;
                    rmi = false;
                    System.out.println(
                            "Scegli interfaccia:\n1- CLC\n2- GUI");
                    answer = stdIn.nextLine();
                    choice = Integer.parseInt(answer);
                    if (choice == 1) {
                        ClientSocket clientSocket = new ClientSocket("127.0.0.1",
                                5050, new CommandLineView());
                    } else if (choice == 2) {
                        ClientSocket clientSocket = new ClientSocket("127.0.0.1",
                                5050, new GuiView());
                    }
                } else if (choice == 2) {
                    stringValid = true;
                    rmi = true;
                    System.out.println(
                            "Scegli interfaccia:\n1- CLC\n2- GUI");
                    answer = stdIn.nextLine();
                    choice = Integer.parseInt(answer);
                    if (choice == 1) {
                        ClientRmi clientRmi = new ClientRmi("127.0.0.1",
                                5050, new CommandLineView());
                    } else if (choice == 2) {
                        ClientRmi clientRmi = new ClientRmi("127.0.0.1",
                                5050, new GuiView());
                    }

                } else {
                    System.out.println("La scelta inserita non è valida\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Scelta non valida\n");

            }
        }
        System.out.println("Server spento.");
    }

}
