package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view.TypeOfViewController;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSocket {

    //server info
    private final String ip;
    private final int port;
    private final TypeOfViewController view;
    private final String nickName;

    //canali di comunicazione
    private Scanner serverIn;
    private PrintWriter serverOut;

    //variabili dei metodi
    private String[] token;
    private String received;

    public ClientSocket(String ip, int port, TypeOfViewController view,
                        String nickName) {
        this.ip = ip;
        this.port = port;
        this.view = view;
        this.nickName = nickName;
    }

    protected void startClient() {

        try {
            //creo socket server
            Socket socket = new Socket(ip, port);
            DebugLogger.println("Connessione stabilita");

            //creo scanner ingresso server
            serverIn = new Scanner(socket.getInputStream());

            //creo printwriter verso server
            serverOut = new PrintWriter(socket.getOutputStream());

            DebugLogger.println(
                    "Canali di comunicazione impostati");

            DebugLogger.println("Invio nickName");
            serverOut.println(nickName);
            serverOut.flush();

            String connectionResult = receiveString();
            if ("Avvio gioco".equals(connectionResult)) {
                DebugLogger.println(connectionResult);
                waitCommand();
            } else {
                view.showInfo(connectionResult);
            }

        } catch (IOException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            System.out.println("Il server è spento, impossibile connettersi");
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
        try {
            while (true) {
                received = receiveString();
                DebugLogger.println(received);

                if ("Welcome".equals(received)) {
                    welcome();
                } else if ("RefreshRegion".equals(received)) {
                    refreshRegion();
                } else if ("RefreshStreet".equals(received)) {
                    refreshStreet();
                } else if ("RefreshGameParameters".equals(received)) {
                    refreshGameParameters();
                } else if ("RefreshMoney".equals(received)) {
                    refreshMoney();
                } else if ("RefreshCurrentPlayer".equals(received)) {
                    refreshCurrentPlayer();
                } else if ("RefreshCard".equals(received)) {
                    refreshCard();
                } else if ("RefreshBlackSheep".equals(received)) {
                    refreshBlackSheep();
                } else if ("RefreshMoveShepherd".equals(received)) {
                    refreshMoveShepherd();
                } else if ("RefreshBuyLand".equals(received)) {
                    refreshBuyLand();
                } else if ("RefreshMoveOvine".equals(received)) {
                    refreshMoveOvine();
                } else if ("RefreshMateSheepWith".equals(received)) {
                    refreshMateSheepWith();
                } else if ("RefreshKillOvine".equals(received)) {
                    refreshKillOvine();
                } else if ("RefreshWolf".equals(received)) {
                    refreshWolf();
                } else if ("SetUpShepherd".equals(received)) {
                    setUpShepherd();
                } else if ("ChooseAction".equals(received)) {
                    chooseAction();
                } else if ("MoveOvine".equals(received)) {
                    moveOvine();
                } else if ("MoveShepherd".equals(received)) {
                    moveShepherd();
                } else if ("BuyLand".equals(received)) {
                    buyLand();
                } else if ("MateSheepWith".equals(received)) {
                    mateSheepWith();
                } else if ("KillOvine".equals(received)) {
                    killOvine();
                } else if ("ShowMyRank".equals(received)){
                    showMyRank();
                } else if ("Classification".equals(received)){
                    showClassification();
                }
            }
        } catch (NoSuchElementException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            view.showEndGame();
        }
    }

    private void refreshRegion() {
        //ricevo i nuovi parametri
        received = receiveString();
        token = received.split(",");

        view.refreshRegion(Integer.parseInt(token[0]),
                Integer.parseInt(token[1]), Integer.parseInt(token[2]),
                Integer.parseInt(token[3]));
    }

    private void refreshStreet() {
        received = receiveString();
        token = received.split(",");

        view.refreshStreet(Integer.parseInt(token[0]), Boolean.parseBoolean(
                token[1]), token[2]);
    }

    private void refreshGameParameters() {
        received = receiveString();
        token = received.split(",");

        view.refereshGameParameters(Integer.parseInt(token[0]), token[1],
                Integer.parseInt(token[2]));
    }

    private void refreshCurrentPlayer() {
        //ricevo il nickName
        received = receiveString();
        view.refereshCurrentPlayer(received);
    }

    private void refreshCard() {
        received = receiveString();
        token = received.split(",");
        view.refereshCard(token[0], Integer.parseInt(token[1]));
    }

    private void refreshBlackSheep() {
        received = receiveString();

        view.refreshBlackSheep(received);
    }

    private void refreshWolf() {
        received = receiveString();

        view.refreshWolf(received);
    }

    private void refreshMoveShepherd() {
        received = receiveString();

        token = received.split(",");

        view.refreshMoveShepherd(token[0], token[1], token[2]);
    }

    private void refreshMateSheepWith() {
        received = receiveString();

        token = received.split(",");

        view.refreshMateSheepWith(token[0], token[1], token[2], token[3],
                token[4]);
    }

    private void refreshKillOvine() {
        received = receiveString();

        token = received.split(",");

        view.refreshKillOvine(token[0], token[1], token[2], token[3]);
    }

    private void refreshBuyLand() {
        received = receiveString();
        token = received.split(",");
        view.refreshBuyLand(token[0], token[1], token[2]);
    }

    private void refreshMoveOvine() {
        received = receiveString();
        token = received.split(",");
        view.refreshMoveOvine(token[0], token[1], token[2], token[3]);
    }

    private void setUpShepherd() {
        String shepherdIndex = receiveString();

        //chiedo strada dalla view
        String street = view.setUpShepherd(Integer.parseInt(shepherdIndex));

        //e la mando al server
        sendString(street);
        String result = receiveString();
        token = result.split(",");

        if (result.contains("Pastore posizionato correttamente")) {
            //che mi rimanda il risultato            
            view.showSetShepherd(token[1], token[2]);
        } else {
            view.showInfo(token[0]);
        }
    }

    private void chooseAction() {
        //receive possible actions
        String actions = receiveString();

        String[] possibleActions = actions.split(",");

        int[] availableAcions = new int[possibleActions.length];
        String[] actionsName = new String[possibleActions.length];

        for (int i = 0; i < possibleActions.length; i++) {
            token = possibleActions[i].split("-");

            availableAcions[i] = Integer.parseInt(token[0]);
            actionsName[i] = token[1];
        }
        //invio l'intero ritornato
        sendString("" + view.chooseAction(availableAcions, actionsName));
    }

    private void moveOvine() {
        //ottengo i parametri stratRegion, endRegion e Type dalla view
        String parameters = view.moveOvine();

        //li mando al server per far eseguire l'azione
        sendString(parameters);

        //ricevo il risultato dell'operazione
        String result = receiveString();
        DebugLogger.println(result);
        if (result.contains("Ovino mosso")) {
            token = parameters.split(",");
            view.showMoveOvine(token[0], token[1], token[2]);
        } else {
            view.showInfo(result);
        }
    }

    private void moveShepherd() {
        String result = view.askMoveShepherd();

        sendString(result);

        //ottengo il risultato
        result = receiveString();

        if (result.contains("Pastore spostato")) {
            token = result.split(",");
            view.showMoveShepherd(token[1]);
        } else {
            view.showInfo(result);
        }
    }

    private void buyLand() {
        //inivio al server il territorio da acquistare
        sendString(view.askBuyLand());

        //ottengo i lrisultato dell'operazione
        String result = receiveString();
        token = result.split(",");
        if (result.contains("Carta acquistata")) {
            //che mi rimanda il risultato            
            view.showBoughtLand(token[1], token[2]);
        } else {
            view.showInfo(token[0]);
        }

    }

    private void mateSheepWith() {
        String parameters = view.askMateSheepWith();
        sendString(parameters);

        //ottengo i lrisultato dell'operazione
        String result = receiveString();
        String[] resultTokens = result.split(",");
        //resultTokens1 è il tipo creato, resultTokens2 è il tipo accoppiato con la pecora
        if (result.contains("Accoppiamento eseguito")) {
            token = parameters.split(",");
            //token 1 è la regione
            //che mi rimanda il risultato              
            view.showMateSheepWith(token[1], resultTokens[2], resultTokens[1]);
        } else {
            view.showInfo(resultTokens[0]);
        }

    }

    private void killOvine() {
        String parameters = view.askKillOvine();
        sendString(parameters);

        //ottengo i lrisultato dell'operazione
        String result = receiveString();

        token = parameters.split(",");
        
        if (result.contains("Ovino ucciso")) {
            String[] tokenResult = result.split(",");
            view.showKillOvine(token[1], token[2],tokenResult[1]);
        } else {
            view.showInfo(result);
        }
    }

    private void welcome() {
        view.showWelcome();
    }

    private void refreshMoney() {
        view.refreshMoney(receiveString());
    }

    private void showMyRank() {
        received = receiveString();
        token = received.split(",");
        
        view.showMyRank(Boolean.parseBoolean(token[0]), token[1]);
    }

    private void showClassification() {
        view.showClassification(receiveString());
    }

}
