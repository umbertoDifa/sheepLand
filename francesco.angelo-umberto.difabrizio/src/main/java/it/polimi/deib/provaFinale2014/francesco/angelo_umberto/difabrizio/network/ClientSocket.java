package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSocket {

    //server info
    private final String ip;
    private final int port;
    private final typeOfView view;

    //canali di comunicazione
    private Scanner serverIn;
    private PrintWriter serverOut;
    private Scanner stdIn;

    //variabili dei metodi
    private String token[];
    private String received;

    public ClientSocket(String ip, int port, typeOfView view) {
        this.ip = ip;
        this.port = port;
        this.view = view;
    }

    public void startClient() {

        try {
            //creo socket server
            Socket socket = new Socket(ip, port);
            DebugLogger.println("Connessione stabilita");

            //creo scanner ingresso server
            serverIn = new Scanner(socket.getInputStream());

            //creo printwriter verso server
            serverOut = new PrintWriter(socket.getOutputStream());

            //creo scanner da tastiera
            stdIn = new Scanner(System.in);
            DebugLogger.println("Canali di comunicazione impostati");

            waitCommand();

        } catch (IOException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
        }
    }

    private String receiveString() {
        return serverIn.nextLine();
    }

    private void sendString(String message) {
        serverOut.println(message);
        serverOut.flush();
    }

    private void waitCommand() {
        while (true) {
            received = receiveString();

            if (received.equals("RefreshRegion")) {
                refreshRegion();
            } else if (received.equals("RefreshStreet")) {
                refreshStreet();
            } else if (received.equals("RefereshGameParameters")) {
                refreshGameParameters();
            } else if (received.equals("RefereshCurrentPlayer")) {
                refreshCurrentPlayer();
            } else if (received.equals("RefereshCards")) {
                refreshCards();
            } else if (received.equals("RefreshBlackSheep")) {
                refreshBlackSheep();
            } else if (received.equals("RefreshWolf")) {
                refreshWolf();
            } else if (received.equals("SetUpShepherds")) {
                setUpShepherds();
            } else if (received.equals("ChooseAction")) {
                chooseAction();
            } else if (received.equals("MoveOvine")) {
                moveOvine();
            } else if (received.equals("MoveShepherd")) {
                moveShepherd();
            } else if (received.equals("BuyLand")) {
                buyLand();
            } else if (received.equals("MateSheepWith")) {
                mateSheepWith();
            } else if (received.equals("KillOvine")) {
                killOvine();
            }
        }
    }

    public void refreshRegion() {
        //ricevo i nuovi parametri
        received = receiveString();
        token = received.split(",");
        //TODO la stringa con i quattro parametri verrà 
        //mandata separando i parametri da virgole
        view.refreshRegion(Integer.parseInt(token[0]),
                Integer.parseInt(token[1]), Integer.parseInt(token[2]),
                Integer.parseInt(token[3]));
    }

    public void refreshStreet() {
        received = receiveString();
        token = received.split(",");

        view.refreshStreet(Integer.parseInt(token[0]), Boolean.parseBoolean(
                token[1]), token[2]);
    }

    public void refreshGameParameters() {
        received = receiveString();
        token = received.split(",");

        view.refereshGameParameters(Integer.parseInt(token[0]), token[1],
                Integer.parseInt(token[2]));
    }

    public void refreshCurrentPlayer() {
        received = receiveString();

        view.refereshCurrentPlayer(received);
    }

    public void refreshCards() {
        received = receiveString();
        token = received.split(",");
        view.refereshCards(token);
    }

    public void refreshBlackSheep() {
        received = receiveString();
        view.refreshBlackSheep(Integer.parseInt(received));
    }

    public void refreshWolf() {
        received = receiveString();
        view.refreshWolf(Integer.parseInt(received));
    }

    public void setUpShepherds() {
        //Scegli pastore        
        int chosenShepherd = view.askIdShepherd();

        //scegli strada
        int street = view.askStreet();
        sendString(chosenShepherd + "," + street);
    }

    public void chooseAction() {
        //receive possible actions
        String numberOfActions = receiveString();        
        token = received.split(",");
        String nameOfActions = receiveString();
        
        view.chooseAction(avaibleActions, token);
    }

    public void moveOvine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void moveShepherd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void buyLand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mateSheepWith() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void killOvine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}