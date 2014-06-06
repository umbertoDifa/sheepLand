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
    private String nickName;

    //canali di comunicazione
    private Scanner serverIn;
    private PrintWriter serverOut;

    //variabili dei metodi
    private String[] token;
    private String received;

    public ClientSocket(String ip, int port, TypeOfViewController view) {
        this.ip = ip;
        this.port = port;
        this.view = view;
    }

    protected void startClient() {

        try {
            Scanner stdIn = new Scanner(System.in);
            PrintWriter stdOut = new PrintWriter(System.out);

            DebugLogger.println(
                    "Canali di comunicazione impostati");

            do {
                stdOut.println("Inserisci il tuo nickName:");
                stdOut.flush();

                nickName = stdIn.nextLine();
            } while ("".equals(nickName) || nickName.contains(",") || nickName.contains(
                    ":"));
            //da evitare come la peste la stringa vuota come nickname
            //esplodono i satelliti della nasa

            DebugLogger.println("Invio nickName");

            //creo socket server
            Socket socket = new Socket(ip, port);
            DebugLogger.println("Connessione stabilita");

            //creo scanner ingresso server
            serverIn = new Scanner(socket.getInputStream());

            //creo printwriter verso server
            serverOut = new PrintWriter(socket.getOutputStream());

            serverOut.println(nickName);
            serverOut.flush();

            String connectionResult = receiveString();
            DebugLogger.println(connectionResult);

            if (!("Mi dispiace non ci sono abbastanza giocatori per una partita, riprovare più tardi.".equals(
                    connectionResult) || "Il tuo nickName non è valido, c'è un altro giocatore con lo stesso nick".equals(
                            connectionResult) || "Ci sono troppe partite attive riprovare più tardi".equals(
                            connectionResult))) {
                DebugLogger.println(connectionResult);
                waitCommand();
            } else {
                view.showInfo(connectionResult);
            }

        } catch (IOException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            view.showInfo("Il server è spento, impossibile connettersi");
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
        boolean endOfGame = false;
        try {
            while (!endOfGame) {
                received = receiveString();
                DebugLogger.println(received);

                if (MessageProtocol.WELCOME.equals(MessageProtocol.valueOf(
                        received))) {
                    welcome();
                } else if (MessageProtocol.REGION.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshRegion();
                } else if (MessageProtocol.STREET.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshStreet();
                } else if (MessageProtocol.GAME_PARAMETERS.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshGameParameters();
                } else if (MessageProtocol.MONEY.equals(MessageProtocol.valueOf(
                        received))) {
                    refreshMoney();
                } else if (MessageProtocol.CURRENT_PLAYER.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshCurrentPlayer();
                } else if (MessageProtocol.CARD.equals(MessageProtocol.valueOf(
                        received))) {
                    refreshCard();
                } else if (MessageProtocol.BLACK_SHEEP_REFRESH.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshBlackSheep();
                } else if (MessageProtocol.MOVE_SHEPHERD_REFRESH.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshMoveShepherd();
                } else if (MessageProtocol.BUY_LAND_REFRESH.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshBuyLand();
                } else if (MessageProtocol.MOVE_OVINE_REFRESH.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshMoveOvine();
                } else if (MessageProtocol.MATE_SHEEP_WITH_REFRESH.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshMateSheepWith();
                } else if (MessageProtocol.KILL_OVINE_REFRESH.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshKillOvine();
                } else if (MessageProtocol.WOLF_REFRESH.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshWolf();
                } else if (MessageProtocol.PLAYER_DISCONNECTED.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    refreshPlayerDisconnected();
                } else if (MessageProtocol.SPECIAL_ANIMAL_POSITION.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    specialAnimalInitialPosition();
                } else if (MessageProtocol.SET_UP_SHEPHERD.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    setUpShepherd();
                } else if (MessageProtocol.CHOOSE_ACTION.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    chooseAction();
                } else if (MessageProtocol.MOVE_OVINE.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    moveOvine();
                } else if (MessageProtocol.MOVE_SHEPHERD.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    moveShepherd();
                } else if (MessageProtocol.BUY_LAND.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    buyLand();
                } else if (MessageProtocol.MATE_SHEEP_WITH.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    mateSheepWith();
                } else if (MessageProtocol.KILL_OVINE.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    killOvine();
                } else if (MessageProtocol.UNEXPECTED_END_OF_GAME.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    unexpectedEndOfGame();
                    endOfGame = true;
                } else if (MessageProtocol.SHOW_MY_RANK.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    showMyRank();
                } else if (MessageProtocol.CLASSIFICATION.equals(
                        MessageProtocol.valueOf(
                                received))) {
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
        String parameters = view.askMoveOvine();

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
            view.showKillOvine(token[1], token[2], tokenResult[1]);
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

    private void specialAnimalInitialPosition() {
        view.specialAnimalInitialCondition(receiveString());
    }

    private void refreshPlayerDisconnected() {
        view.refreshPlayerDisconnected(receiveString());
    }

    private void unexpectedEndOfGame() {
        view.showUnexpectedEndOfGame();
    }

}
