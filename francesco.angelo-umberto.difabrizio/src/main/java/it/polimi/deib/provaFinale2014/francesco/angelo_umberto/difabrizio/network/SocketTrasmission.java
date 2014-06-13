package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.BlackSheep;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.OvineType;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Wolf;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.util.List;
import java.util.Map;

/**
 * It deals with the trasmission between sockets, checkink everytime if a player
 * is online an refreshed before sending information to him.
 *
 * @author Umberto
 */
public class SocketTrasmission extends TrasmissionController {

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param regionIndex
     * @param numbOfSheep
     * @param numbOfRam
     * @param numbOfLamb
     */
    public void refreshRegion(String nickName, int regionIndex, int numbOfSheep,
                              int numbOfRam, int numbOfLamb) {
        if (canPlayerReceive(nickName)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    MessageProtocol.REGION.toString());

            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    regionIndex);
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    numbOfSheep);
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    numbOfRam);
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    numbOfLamb);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param streetIndex
     * @param fence
     * @param nickNameOfShepherdPlayer
     * @param shepherdIndex
     */
    public void refreshStreet(String nickName, int streetIndex, boolean fence,
                              String nickNameOfShepherdPlayer, int shepherdIndex) {
        if (canPlayerReceive(nickName)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    MessageProtocol.STREET.toString());
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    streetIndex);
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    fence);
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    nickNameOfShepherdPlayer);
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    shepherdIndex);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickNamePlayer
     */
    public void brodcastCurrentPlayer(String nickNamePlayer) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNamePlayer) && canPlayerReceive(nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.CURRENT_PLAYER.toString());
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(nickNamePlayer);
            }

        }

    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param type
     * @param value
     */
    public void refreshCard(String nickName, String type, int value) {
        if (canPlayerReceive(nickName)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    MessageProtocol.CARD.toString());
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    type);
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    value);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickNamePlayer
     * @param region
     * @param otherType
     * @param newType
     * @param outcome
     */
    @Override
    public void refreshMateSheepWith(String nickNamePlayer, String region,
                                     String otherType, String newType,
                                     String outcome) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNamePlayer) && canPlayerReceive(nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.MATE_SHEEP_WITH_REFRESH.toString());
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                nickNamePlayer);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                region);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                otherType);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                newType);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                outcome);
            }

        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickNameBuyer
     * @param boughtLand
     * @param price
     */
    public void refreshBuyLand(String nickNameBuyer, String boughtLand,
                               int price) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameBuyer) && canPlayerReceive(nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.BUY_LAND_REFRESH.toString());
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                nickNameBuyer);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                boughtLand);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                price);
            }

        }
    }

    /**
     * Invia una stringa del tipo [Cosa è successo alla pecora nera],[regione
     * della pecora nera]
     *
     * @param movementResult
     */
    private void refreshBlackSheep(String movementResult) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (canPlayerReceive(nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.BLACK_SHEEP_REFRESH.toString());
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                movementResult);
            }

        }
    }

    private void refreshWolf(String movementResult) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (canPlayerReceive(nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.WOLF_REFRESH.toString());
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                movementResult);
            }

        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickNameMover
     * @param startRegion
     * @param endRegion
     * @param ovineType
     */
    public void refreshMoveOvine(String nickNameMover, String startRegion,
                                 String endRegion, String ovineType) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameMover) && canPlayerReceive(nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.MOVE_OVINE_REFRESH.toString());
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                nickNameMover);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                startRegion);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                endRegion);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                ovineType);
            }

        }
    }

    /**
     * informa tutti tranne il nickname che il nickname ha mosso lo shepherd
     * nella newStreet
     *
     * @param nickNameMover
     * @param shepherdIndex
     * @param newStreet
     * @param price
     */
    public void refreshMoveShepherd(String nickNameMover, int shepherdIndex,
                                    String newStreet, int price) {
        //per tutti i nick tranne quello dato refresha
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameMover) && canPlayerReceive(nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.MOVE_SHEPHERD_REFRESH.toString());
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                nickNameMover);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                shepherdIndex);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                newStreet);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(price);
            }

        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickNameKiller
     * @param region
     * @param type
     * @param outcome
     */
    public void refreshKillOvine(String nickNameKiller, String region,
                                 String type, String outcome) {
        //per tutti i nick tranne quello dato refresha
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameKiller) && canPlayerReceive(nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.KILL_OVINE_REFRESH.toString());
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                nickNameKiller);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                region);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                type);
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                outcome);
            }

        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param shepherdIndex
     *
     * @return
     *
     * @throws PlayerDisconnectedException
     */
    public boolean askSetUpShepherd(String nickName, int shepherdIndex) throws
            PlayerDisconnectedException {
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                MessageProtocol.SET_UP_SHEPHERD.toString());
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                shepherdIndex);
        //ricevo la stringa della strada
        String chosenStringedStreet;

        chosenStringedStreet = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                nickName)).receiveString();
        //tento di eseguire la setShepherd
        String result = getNick2PlayerMap().get(nickName).setShepherd(
                shepherdIndex, chosenStringedStreet);

        //invio il risultato qualsiasi sia
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                result);
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                chosenStringedStreet);

        //ritorno il successo o meno dell'operazione
        if (result.contains("Pastore posizionato correttamente!")) {
            //refresho
            String[] token = result.split(",");
            refreshMoveShepherd(nickName, shepherdIndex,
                    chosenStringedStreet, Integer.parseInt(token[1]));
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param possibleActions
     *
     * @return
     *
     * @throws PlayerDisconnectedException
     */
    public boolean askChooseAction(String nickName, String possibleActions)
            throws PlayerDisconnectedException {
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                MessageProtocol.CHOOSE_ACTION.toString());
        DebugLogger.println("choose action inviata a " + nickName);

        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                possibleActions);
        String result = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                nickName)).receiveString();

        // so che il risultato è buono perchè passo al client solo quelli possibili
        //e lui fa anche il controllo sulla correttezza sintattica della stringa
        int action = Integer.parseInt(result);
        switch (action) {
            case 1:
                return askMoveOvine(nickName);
            case 2:
                return askMoveSheperd(nickName);
            case 3:
                return askBuyLand(nickName);
            case 4:
                return askMateSheepWith(nickName, OvineType.SHEEP.toString());
            case 5:
                return askMateSheepWith(nickName, OvineType.RAM.toString());
            case 6:
                return askKillOvine(nickName);
            default:
                return false;
        }
    }

    private boolean askMoveOvine(String nickName) throws
            PlayerDisconnectedException {
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                MessageProtocol.MOVE_OVINE.toString());

        //ricevo i parametri
        String result = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                nickName)).receiveString();
        String[] token = result.split(",", -1);

        result = getNick2PlayerMap().get(nickName).moveOvine(token[0],
                token[1], token[2]);
        DebugLogger.println(result);
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                result);
        if (result.contains("Ovino mosso")) {
            //refreshio
            refreshMoveOvine(nickName, token[0], token[1], token[2]);
            return true;
        }
        return false;
    }

    private boolean askMoveSheperd(String nickName) throws
            PlayerDisconnectedException {

        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                MessageProtocol.MOVE_SHEPHERD.toString());

        String result = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                nickName)).receiveString();

        String[] token = result.split(",", -1);

        //eseguo
        result = getNick2PlayerMap().get(nickName).moveShepherd(token[0],
                token[1]);

        //invio il risultato
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                result);

        if (result.contains("Pastore spostato")) {
            //invia conferma riepilogativa agli utenti
            int price = Integer.parseInt(result.split(",")[1]);
            refreshMoveShepherd(nickName, Integer.parseInt(token[0]), token[1],
                    price);

            //invia refresh portafoglio
            refreshMoney(nickName);
            return true;
        }
        return false;

    }

    private boolean askBuyLand(String nickName) throws
            PlayerDisconnectedException {
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                MessageProtocol.BUY_LAND.toString());
        String landToBuy = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                nickName)).receiveString();

        String result = getNick2PlayerMap().get(nickName).buyLand(
                landToBuy);

        //invio il risultato al client
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                result);

        String[] token = result.split(",", -1);

        if (result.contains("Carta acquistata")) {
            refreshBuyLand(nickName, token[1], Integer.parseInt(token[2]));
            //aggiorno i soldi del compratore
            refreshMoney(nickName);
            return true;
        }
        return false;
    }

    private boolean askKillOvine(String nickName) throws
            PlayerDisconnectedException {
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                MessageProtocol.KILL_OVINE.toString());
        String parameters = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                nickName)).receiveString();
        DebugLogger.println(parameters);
        String[] token = parameters.split(",", -1);
        String result = getNick2PlayerMap().get(nickName).killOvine(
                token[0], token[1], token[2]);

        DebugLogger.println(result);
        //invio risultato
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                result);
        if (result.contains("Ovino ucciso")) {

            refreshKillOvine(nickName, token[1], token[2], "ok");

            //se l'azione ha successo aggiorna i portafogli remoti
            for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
                String nick = (String) pairs.getKey();
                refreshMoney(nick);
            }

            //aggiorna la consapevolezza di ogni altro giocatore rispetto agli altri in quanto a money
            refreshWallets();

            return true;
        } else if ("Non puoi pagare il silenzio degli altri pastori".equals(
                result) || "Il valore del dado è diverso dalla strada del pastore".equals(
                        result)) {
            refreshKillOvine(nickName, token[1], token[2], "nok:" + result);
            return true;

        }
        return false;
    }

    private boolean askMateSheepWith(String nickName, String type) throws
            PlayerDisconnectedException {
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                MessageProtocol.MATE_SHEEP_WITH.toString());
        String parameters = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                nickName)).receiveString();

        DebugLogger.println(parameters);

        //splitto i parametri
        String[] token = parameters.split(",", -1);
        String shepherd = token[0];
        String region = token[1];

        String result = getNick2PlayerMap().get(nickName).mateSheepWith(
                shepherd, region, type);

        DebugLogger.println(result);

        if (result.contains("Accoppiamento eseguito")) {
            token = result.split(",", -1);
            //token1 ha il tipo creato

            DebugLogger.println(
                    "invio risultato mateSheepWith " + result + "," + type);
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    result + "," + type);

            refreshMateSheepWith(nickName, region, type, token[1], "ok");
            return true;
        } else if ("Il valore del dado è diverso dalla strada del pastore".equals(
                result)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    result);
            refreshMateSheepWith(nickName, region, type, token[1], "nok");
            return true;
        }
        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                result);
        return false;

    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     */
    @Override
    public void refreshStartGame(String nickName) {
        if (ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName).isOnline()) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    "Avvio gioco");
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    MessageProtocol.WELCOME.toString());
        }

    }

    /**
     * {@inheritDoc }
     *
     * @param animal
     * @param movementResult
     */
    @Override
    public void refreshSpecialAnimal(SpecialAnimal animal, String movementResult) {
        if (animal instanceof BlackSheep) {
            this.refreshBlackSheep(movementResult);
        } else if (animal instanceof Wolf) {
            this.refreshWolf(movementResult);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     */
    @Override
    public void refreshMoney(String nickName) {
        if (canPlayerReceive(nickName)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    MessageProtocol.MONEY.toString());
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    ""
                    + getNick2PlayerMap().get(nickName).getMainShepherd().getWallet().getAmount());
        }

    }

    /**
     * {@inheritDoc }
     *
     * @param winner
     * @param nickName
     * @param score
     */
    @Override
    public void sendRank(boolean winner, String nickName, int score) {
        DebugLogger.println("Send result to " + nickName);
        if (canPlayerReceive(nickName)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    MessageProtocol.SHOW_MY_RANK.toString());
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    winner + "," + score);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param classification
     */
    @Override
    public void sendClassification(String classification) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (canPlayerReceive(nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.CLASSIFICATION.toString());
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                classification);
            }
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param animal
     * @param region
     */
    @Override
    public void refreshSpecialAnimalPosition(String nickName,
                                             SpecialAnimal animal,
                                             String region) {
        if (canPlayerReceive(nickName)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    MessageProtocol.SPECIAL_ANIMAL_POSITION.toString());
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(nickName)).send(
                    animal.toString() + ","
                    + region);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickNameDisconnected
     */
    @Override
    public void brodcastPlayerDisconnected(String nickNameDisconnected) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameDisconnected) && canPlayerReceive(
                    nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.PLAYER_DISCONNECTED.toString());
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(nickNameDisconnected);
            }

        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void unexpectedEndOfGame() {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (canPlayerReceive(nickName)) {
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        nickName)).send(
                                MessageProtocol.UNEXPECTED_END_OF_GAME.toString());
            }

        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param nickNames
     * @param wallet
     * @param shepherd4player
     */
    @Override
    public void refreshGameParameters(String nickName, String[] nickNames,
                                      int[] wallet,
                                      int shepherd4player) {
        if (canPlayerReceive(nickName)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    nickName)).send(MessageProtocol.GAME_PARAMETERS.toString());
            String tmp = "";
            for (int i = 0; i < nickNames.length; i++) {
                tmp += nickNames[i] + "," + wallet[i] + ",";
            }
            DebugLogger.println("invio " + tmp);
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    nickName)).send(tmp + shepherd4player);
            DebugLogger.println("invio " + tmp + shepherd4player);
        }
    }

    @Override
    public void refreshWallets() {
        //per tutti i player
        //se quel player può ricever
        //gli mando il portafoglio di tutti gli altri
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (canPlayerReceive(nickName)) {
                for (Map.Entry couple : getNick2PlayerMap().entrySet()) {
                    String other = (String) couple.getKey();
                    if (!nickName.equals(other)) {
                        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                                nickName)).send(
                                        MessageProtocol.REFRESH_OTHER_PLAYER_MONEY.toString());

                        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                                nickName)).send(other);

                        ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                                nickName)).send(
                                        getNick2PlayerMap().get(other).getMainShepherd().getWallet().getAmount());
                    }
                }

            }
        }

    }

    /**
     * {@inheritDoc }
     *
     * @param client
     * @param fenceAvailable
     */
    @Override
    public void refreshNumberOfAvailableFence(String client, int fenceAvailable) {
        if (canPlayerReceive(client)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    client)).send(MessageProtocol.FENCE_REFRESH.toString());

            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    client)).send(fenceAvailable);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param client
     * @param regionTypes
     * @param availableCards
     */
    @Override
    public void refreshBankCards(String client, String[] regionTypes,
                                 int[] availableCards) {
        if (canPlayerReceive(client)) {
            //per ogni regione
            for (int i = 0; i < regionTypes.length; i++) {
                //mando il message protocol
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        client)).send(MessageProtocol.BANK_CARD.toString());

                //mando il nome della regione
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        client)).send(regionTypes[i]);

                //mando la quantità di carte disponibile
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        client)).send(availableCards[i]);
            }

        }
    }

    /**
     * {@inheritDoc }
     *
     * @param client
     * @param sellableCards
     *
     * @return
     *
     * @throws PlayerDisconnectedException
     */
    @Override
    public boolean sellCard(String client, String[] sellableCards) throws
            PlayerDisconnectedException {
        if (canPlayerReceive(client)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    client)).send(MessageProtocol.MARKET_SELL.toString());

            //ricevo se vuole vendere
            boolean wantToSell = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    client)).receiveBoolean();

            if (wantToSell) {
                //inivio il numero di carte che il player ha
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        client)).send(sellableCards.length);

                for (String card : sellableCards) {
                    ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                            client)).send(card);
                }

                //ricevo nome della carta e prezzo a cui venderla
                String card = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        client)).receiveString();

                int price = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        client)).receiveInt();
                DebugLogger.println(
                        "Ricevuta carata da vendere: " + card + " al prezzo " + price);
                //imposto la carta da vendere
                getNick2PlayerMap().get(client).putCardInMarket(card, price);
            }
            return wantToSell;
        }
        return false;
    }

    /**
     * {@inheritDoc }
     *
     * @param playerNickName
     * @param buyableCards
     *
     * @return
     *
     * @throws PlayerDisconnectedException
     */
    @Override
    public boolean buyCard(String playerNickName, List<Card> buyableCards)
            throws PlayerDisconnectedException {
        if (canPlayerReceive(playerNickName)) {
            ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    playerNickName)).send(MessageProtocol.MARKET_BUY.toString());

            //ricevo se vuole comprare
            boolean wantToBuy = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                    playerNickName)).receiveBoolean();

            if (wantToBuy) {
                //inivio il numero di carte che il player ha
                ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        playerNickName)).send(buyableCards.size());

                for (Card card : buyableCards) {
                    //invio tipo di carta
                    ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                            playerNickName)).send(card.getType().toString());

                    //invio prezzo della carta
                    ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                            playerNickName)).send(card.getMarketValue());
                }

                //ricevo nome della carta da comprare
                String card = ((SocketClientProxy) ServerManager.NICK_2_CLIENT_PROXY_MAP.get(
                        playerNickName)).receiveString();
                DebugLogger.println("Carta da comprare ricevuta: " + card);
                //compro la carta
                getNick2PlayerMap().get(playerNickName).payCardFromMarket(card);
            }
            return wantToBuy;
        }
        return false;
    }

}
