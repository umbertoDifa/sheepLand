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

/**
 * Creates a socket version of the client to play the game
 *
 * @author Umberto
 */
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

    int infoInt;

    boolean infoBool;

    /**
     * Creates a client socket
     *
     * @param ip   The server ip
     * @param port The server port
     * @param view The kind of view chosen by the user
     */
    public ClientSocket(String ip, int port, TypeOfViewController view) {
        this.ip = ip;
        this.port = port;
        this.view = view;
    }

    /**
     * Starts a client that asks the nickName to the user and sends it to the
     * server. Depending on the server answer the client starts a game or
     * terminates
     */
    protected void startClient() {
        boolean nickNameAccepted = false;
        String connectionResult = "nok";

        while (!nickNameAccepted) {
            try {
                setUpSocketConnection();
            } catch (IOException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                view.showInfo("Il server è spento, impossibile connettersi");
            }

            getNickName();

            DebugLogger.println("Invio nickName: '" + nickName + "'");

            sendString(nickName);

            connectionResult = receiveString();
            DebugLogger.println(connectionResult);
            if ("Il tuo nickName non è valido, c'è un altro giocatore con lo stesso nick".equals(
                    connectionResult)) {
                view.showInfo(
                        "Il tuo nickName non è valido, c'è un altro giocatore con lo stesso nick");
            } else {
                nickNameAccepted = true;
            }
        }

        if (!("Mi dispiace non ci sono abbastanza giocatori per una partita, riprovare più tardi.".equals(
                connectionResult) || "Ci sono troppe partite attive riprovare più tardi".equals(
                        connectionResult))) {
            DebugLogger.println(connectionResult);
            waitCommand();
        } else {
            view.showInfo(connectionResult);
        }

    }

    private void getNickName() {
        boolean nickNameOk = false;
        while (!nickNameOk) {
            this.nickName = view.askNickName();
            if ("".equals(nickName) || nickName.contains(",") || nickName.contains(
                    ":") || nickName.contains(" ")) {
                nickNameOk = false;
                view.showInfo(
                        "Il nickName inserito non è valido, prego riprovare.");
            } else {
                nickNameOk = true;
            }
        }
        //da evitare come la peste la stringa vuota come nickname
        //esplodono i satelliti della nasaF
    }

    private void setUpSocketConnection() throws IOException {
        //creo socket server
        Socket socket = new Socket(ip, port);
        DebugLogger.println("Connessione stabilita");

        //creo scanner ingresso server
        serverIn = new Scanner(socket.getInputStream());

        //creo printwriter verso server
        serverOut = new PrintWriter(socket.getOutputStream());
        DebugLogger.println(
                "Canali di comunicazione impostati");
    }

    private String receiveString() {
        return serverIn.nextLine();
    }

    private int receiveInt() {
        infoInt = serverIn.nextInt();

        //skip new line
        receiveString();

        return infoInt;
    }

    private boolean receiveBoolean() {
        infoBool = serverIn.nextBoolean();

        //skip new line
        receiveString();

        return infoBool;
    }

    private void sendString(String message) {
        serverOut.println(message);
        serverOut.flush();
    }

    private void sendBoolean(boolean message) {
        serverOut.println(message);
        serverOut.flush();
    }

    private void sendInt(int message) {
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
                } else if (MessageProtocol.REFRESH_OTHER_PLAYER_MONEY.equals(
                        MessageProtocol.valueOf(received))) {
                    refreshOtherPlayerMoney();
                } else if (MessageProtocol.BANK_CARD.equals(
                        MessageProtocol.valueOf(received))) {
                    refreshBankCard();
                } else if (MessageProtocol.MARKET_BUY.equals(
                        MessageProtocol.valueOf(received))) {
                    buyCardFromMarket();
                } else if (MessageProtocol.MARKET_SELL.equals(
                        MessageProtocol.valueOf(
                                received))) {
                    sellCardToMarket();

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
                } else if (MessageProtocol.GAME_PARAMETERS.equals(
                        MessageProtocol.valueOf(received))) {
                    refreshGameParameters();
                } else if (MessageProtocol.FENCE_REFRESH.equals(
                        MessageProtocol.valueOf(received))) {
                    refreshAvailableFences();
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
                    endOfGame = true;

                }
            }
        } catch (NoSuchElementException ex) {
            Logger.getLogger(DebugLogger.class
                    .getName()).log(Level.SEVERE,
                            ex.getMessage(), ex);
            view.showEndGame();
        }

    }

    private void refreshRegion() {
        //ricevo regione index
        int regionIndex = receiveInt();

        //ricevo numero pecore
        int sheeps = receiveInt();

        //ricevo numero ram
        int rams = receiveInt();

        //ricevo numero lamb
        int lambs = receiveInt();

        view.refreshRegion(regionIndex, sheeps, rams, lambs);

    }

    private void refreshStreet() {
        int streetIndex = receiveInt();

        boolean fence = receiveBoolean();

        String nick = receiveString();

        int shepherdIndex = receiveInt();

        view.refreshStreet(streetIndex, fence, nick, shepherdIndex);
    }

    private void refreshGameParameters() {
        received = receiveString();

        DebugLogger.println(received);

        token = received.split(",", -1);
        String[] nickNames = new String[(token.length - 1) / 2];
        int[] wallets = new int[(token.length - 1) / 2];

        DebugLogger.println("lunghezza" + token.length);
        int i = 0;
        for (int j = 0; i < token.length - 1; j++) {
            nickNames[j] = token[i];
            wallets[j] = Integer.parseInt(token[i + 1]);

            DebugLogger.println(token[i]);
            DebugLogger.println(token[i + 1]);

            i += 2;
        }
        view.refreshGameParameters(nickNames, wallets, Integer.parseInt(
                token[token.length - 1]));

    }

    private void refreshCurrentPlayer() {
        //ricevo il nickName
        received = receiveString();
        view.refereshCurrentPlayer(received);
    }

    private void refreshCard() {
        String card = receiveString();

        int value = receiveInt();

        view.refereshCard(card, value);
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
        String player = receiveString();
        int shepherdIndex = receiveInt();
        String street = receiveString();
        int price = receiveInt();

        view.refreshMoveShepherd(player, shepherdIndex, street, price);
    }

    private void refreshMateSheepWith() {
        String player = receiveString();
        String region = receiveString();
        String otherType = receiveString();
        String newType = receiveString();
        String outcome = receiveString();

        view.refreshMateSheepWith(player, region, otherType, newType,
                outcome);
    }

    private void refreshKillOvine() {
        String player = receiveString();
        String region = receiveString();
        String type = receiveString();
        String outcome = receiveString();

        view.refreshKillOvine(player, region, type, outcome);
    }

    private void refreshBuyLand() {
        String player = receiveString();
        String land = receiveString();
        int price = receiveInt();

        view.refreshBuyLand(player, land, price);
    }

    private void refreshMoveOvine() {
        String player = receiveString();
        String startRegion = receiveString();
        String endRegion = receiveString();
        String type = receiveString();

        view.refreshMoveOvine(player, type, startRegion, endRegion);
    }

    private void refreshAvailableFences() {
        int fences = receiveInt();

        view.refreshFences(fences);
    }

    private void setUpShepherd() {
        int shepherdIndex = receiveInt();

        //chiedo strada dalla view
        String street = view.setUpShepherd(shepherdIndex);

        //e la mando al server
        sendString(street);
        String result = receiveString();
        String chosenStreet = receiveString();

        if (result.contains("Pastore posizionato correttamente")) {
            //che mi rimanda il risultato            
            view.showSetShepherd(shepherdIndex, chosenStreet);
        } else {
            view.showInfo(result);
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

        boolean rightFormat;
        String choice;
        int action = -1;

        do {
            choice = view.chooseAction(availableAcions, actionsName);
            try {
                action = Integer.parseInt(choice);
                rightFormat = true;

            } catch (NumberFormatException ex) {
                Logger.getLogger(DebugLogger.class
                        .getName()).log(Level.SEVERE,
                                ex.getMessage(), ex);
                view.showInfo(
                        "Azione non valida.\nPrego riprovare.");
                rightFormat = false;
            }

        } while (!rightFormat || !actionExists(availableAcions, action));

        //invio l'intero ritornato
        sendString(choice);
    }

    private boolean actionExists(int[] availableActions, int action) {
        for (int i = 0; i < availableActions.length; i++) {
            if (availableActions[i] == action) {
                return true;
            }
        }
        view.showInfo("Azione non esistente.\nPrego riporvare:");
        return false;
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
            token = parameters.split(",", -1);
            view.showMoveOvine(token[0], token[1], token[2]);
        } else {
            view.showInfo(result);
        }
    }

    private void moveShepherd() {
        String parameters = view.askMoveShepherd();

        sendString(parameters);

        token = parameters.split(",", -1);
        String shepherdIndex = token[0];

        //ottengo il risultato
        String result = receiveString();

        if (result.contains("Pastore spostato")) {
            token = result.split(",", -1);
            view.showMoveShepherd(shepherdIndex, token[1]);
        } else {
            view.showInfo(result);
        }
    }

    private void buyLand() {
        //inivio al server il territorio da acquistare
        sendString(view.askBuyLand());

        //ottengo i lrisultato dell'operazione
        String result = receiveString();
        token = result.split(",", -1);
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
        String[] resultTokens = result.split(",", -1);
        //resultTokens1 è il tipo creato, resultTokens2 è il tipo accoppiato con la pecora
        if (result.contains("Accoppiamento eseguito")) {
            token = parameters.split(",", -1);
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

        token = parameters.split(",", -1);

        if (result.contains("Ovino ucciso")) {
            String[] tokenResult = result.split(",", -1);
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
        token = received.split(",", -1);

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

    private void refreshOtherPlayerMoney() {
        String otherPlayer = receiveString();
        int otherMoney = receiveInt();

        view.refreshOtherPlayerMoney(otherPlayer, otherMoney);
    }

    private void refreshBankCard() {
        String regionType = receiveString();
        int availableCards = receiveInt();

        view.refreshBankCard(regionType, availableCards);
    }

    private void sellCardToMarket() {
        String[] cards;

        boolean wantToSell = (view.askWillingTo("vendere"));
        sendBoolean(wantToSell);

        if (wantToSell) {
            //receive number of cards
            int numberOfCards = receiveInt();

            cards = new String[numberOfCards];

            for (int i = 0; i < numberOfCards; i++) {
                cards[i] = receiveString();
            }
            String cardToSell = null;
            boolean cardValid = false;

            while (!cardValid) {

                cardToSell = view.askSellCard(cards);
                DebugLogger.println("Carta da vendere:" + cardToSell);

                //check card
                for (String card : cards) {
                    if (card.equalsIgnoreCase(cardToSell)) {
                        cardValid = true;
                        break;
                    }
                }
            }
            boolean priceValid = false;
            int price = 1;

            while (!priceValid) {
                price = view.askPriceCard();
                if (price > 0 && price <= 4) {
                    priceValid = true;
                }
            }
            DebugLogger.println(
                    "Invio carta da vendere " + cardToSell + " per un prezzo di " + price);
            sendString(cardToSell);
            sendInt(price);
        }
    }

    private void buyCardFromMarket() {
        boolean wantToBuy = (view.askWillingTo("comprare"));
        DebugLogger.println("Decisione di comparare: " + wantToBuy);

        sendBoolean(wantToBuy);
        if (wantToBuy) {
            //ricevo quante carte disponibili
            int numberOfCards = receiveInt();
            String[] cards = new String[numberOfCards];
            int[] prices = new int[numberOfCards];

            for (int i = 0; i < numberOfCards; i++) {
                cards[i] = receiveString();
                prices[i] = receiveInt();
                DebugLogger.println(
                        "Ricevuta carta " + i + " di tipo " + cards[i] + " per il prezzo di " + prices[i]);
            }
            String cardToBuy = null;
            boolean cardValid = false;

            while (!cardValid) {

                cardToBuy = view.askBuyMarketCard(cards, prices);
                DebugLogger.println("Carta da comprare:" + cardToBuy);

                //check card
                for (String card : cards) {
                    if (card.equalsIgnoreCase(cardToBuy)) {
                        cardValid = true;
                        break;
                    }
                }
            }
            DebugLogger.println(
                    "Invio carta da comprare " + cardToBuy);
            sendString(cardToBuy);
        }

    }

}
