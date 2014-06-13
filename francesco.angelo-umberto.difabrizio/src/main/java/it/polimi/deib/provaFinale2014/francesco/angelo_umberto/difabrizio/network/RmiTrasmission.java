package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.BlackSheep;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Card;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Wolf;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * It implements a trasmissionController. It is used to manage the connection as
 * rmi service. It implements all the method as remote invocation to the client
 *
 * @author Umberto
 */
public class RmiTrasmission extends TrasmissionController {

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param regionIndex
     * @param numbOfSheep
     * @param numbOfRam
     * @param numbOfLamb
     */
    @Override
    public void refreshRegion(String nickName, int regionIndex, int numbOfSheep,
                              int numbOfRam, int numbOfLamb) {
        if (canPlayerReceive(nickName)) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(nickName)).getClientRmi().refreshRegion(
                        regionIndex, numbOfSheep, numbOfRam, numbOfLamb);
            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);

            }
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
    @Override
    public void refreshStreet(String nickName, int streetIndex, boolean fence,
                              String nickNameOfShepherdPlayer, int shepherdIndex) {
        if (canPlayerReceive(nickName)) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(nickName)).getClientRmi().refreshStreet(
                        streetIndex, fence, nickNameOfShepherdPlayer,
                        shepherdIndex);
            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);

            }
        }

    }

    /**
     * {@inheritDoc }
     *
     * @param nickNamePlayer
     */
    @Override
    public void brodcastCurrentPlayer(String nickNamePlayer) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (canPlayerReceive(nickName) && !nickName.equals(nickNamePlayer)) {
                try {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refereshCurrentPlayer(
                                    nickNamePlayer);
                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);
                }
            }

        }
    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     * @param card
     * @param value
     */
    @Override
    public void refreshCard(String nickName, String card, int value) {
        if (canPlayerReceive(nickName)) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(nickName)).getClientRmi().refreshCard(
                        card, value);
            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);

            }
        }

    }

    /**
     * {@inheritDoc }
     *
     * @param nickNameMover
     * @param shepherdInedx
     * @param endStreet
     * @param price
     */
    @Override
    public void refreshMoveShepherd(String nickNameMover, int shepherdInedx,
                                    String endStreet, int price) {
        String nickName = null;

        try {
            //per tutti i nick tranne quello dato refresha
            for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
                nickName = (String) pairs.getKey();
                if (!nickName.equals(nickNameMover) && canPlayerReceive(nickName)) {

                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refreshMoveShepherd(
                                    nickNameMover, shepherdInedx, endStreet,
                                    price);
                }
            }
        } catch (RemoteException ex) {
            setPlayerOffline(nickName, ex);
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
    @Override
    public void refreshKillOvine(String nickNameKiller, String region,
                                 String type,
                                 String outcome) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameKiller) && canPlayerReceive(nickName)) {
                try {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refreshKillOvine(
                                    nickNameKiller, region, type, outcome);

                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);
                }
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
    @Override
    public boolean askSetUpShepherd(String nickName, int shepherdIndex) throws
            PlayerDisconnectedException {
        try {
            String chosenStreet = ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                    nickName)).getClientRmi().setUpShepherd(
                            shepherdIndex);

            //invia conferma riepilogativa agli utenti
            if (chosenStreet != null) {
                //il set up costa 0 denari
                refreshMoveShepherd(nickName, shepherdIndex, chosenStreet, 0);
                return true;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);

            setPlayerOffline(nickName, ex);

            throw new PlayerDisconnectedException(
                    "Il player:" + nickName + " si è disconnesso");
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
    @Override
    public boolean askChooseAction(String nickName, String possibleActions)
            throws PlayerDisconnectedException {
        DebugLogger.println("Chiedo azione a " + nickName);

        try {
            String action = ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                    nickName)).getClientRmi().chooseAction(possibleActions);

            DebugLogger.println("ricevuto: " + action);

            if (!action.contains("null")) {
                //tokenizza 
                String token[] = action.split(",", -1);

                //switcha l'azione e refreshia tutti in base  all'azinoe fatta dal player
                switch (token[0].charAt(0)) {
                    case '1':
                        refreshMoveOvine(nickName, token[1], token[2], token[3]);
                        return true;
                    case '2':
                        refreshMoveShepherd(nickName, Integer.parseInt(token[1]),
                                token[2], Integer.parseInt(token[3]));
                        return true;
                    case '3':
                        refreshBuyLand(nickName, token[2], Integer.parseInt(
                                token[3]));
                        return true;
                    case '4':
                        refreshMateSheepWith(nickName, token[1], token[2],
                                token[3], token[4]);
                        return true;
                    case '5':
                        refreshMateSheepWith(nickName, token[1], token[2],
                                token[3], token[4]);
                        return true;
                    case '6':
                        refreshKillOvine(nickName, token[1], token[2], token[3]);
                        refreshWallets();
                        return true;

                    default:
                        return false;
                }
            }
            return false;

        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);

            setPlayerOffline(nickName, ex);

            throw new PlayerDisconnectedException(
                    "Il player:" + nickName + " si è disconnesso");
        }
    }

    @Override
    public void refreshMoveOvine(String nickNameMover, String startRegion,
                                 String endRegion, String ovineType) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameMover) && canPlayerReceive(nickName)) {
                try {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refreshMoveOvine(
                                    nickNameMover, startRegion, endRegion,
                                    ovineType);
                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);

                }
            }

        }

    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     */
    @Override
    public void refreshStartGame(String nickName) {

        if (ServerManager.Nick2ClientProxyMap.get(nickName).isOnline()) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(nickName)).getClientRmi().welcome();
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(nickName)).getClientRmi().connectPlayer(
                        getNick2PlayerMap().get(nickName));
            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);
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
    @Override
    public void refreshBuyLand(String nickNameBuyer, String boughtLand,
                               int price) {

        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameBuyer) && canPlayerReceive(nickName)) {
                try {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refreshBuyLand(
                                    nickNameBuyer, boughtLand, price);
                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);

                }
            }

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
     * @param nickNameMater
     * @param region
     * @param otherType
     * @param newType
     * @param outcome
     */
    @Override
    public void refreshMateSheepWith(String nickNameMater, String region,
                                     String otherType, String newType,
                                     String outcome) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameMater) && canPlayerReceive(nickName)) {
                try {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refreshMateSheepWith(
                                    nickNameMater, region, otherType, newType,
                                    outcome);
                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);
                }
            }

        }

    }

    /**
     * {@inheritDoc }
     *
     * @param nickName
     */
    @Override
    public void refreshMoney(String nickName) {
        DebugLogger.println("refresh money a " + nickName);
        if (canPlayerReceive(nickName)) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(nickName)).getClientRmi().refreshMoney(
                        ""
                        + getNick2PlayerMap().get(nickName).getMainShepherd().getWallet().getAmount());
            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);

            }

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
    public void sendRank(boolean winner, String nickName, int score
    ) {
        if (canPlayerReceive(nickName)) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                        nickName)).getClientRmi().showMyRank(
                                "" + winner, "" + score);
            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);

            }
        }
    }

    /**
     * {@inheritDoc }
     *
     * @param classification
     */
    @Override
    public void sendClassification(String classification
    ) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (canPlayerReceive(nickName)) {
                try {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().showClassification(
                                    classification);
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().disconnect(
                                    "Il gioco è terminato\nArriverderci!");

                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);

                }
            }

        }
    }

    private void refreshBlackSheep(String movementResult) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (canPlayerReceive(nickName)) {
                try {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refreshBlackSheep(
                                    movementResult);

                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);

                }
            }

        }
    }

    private void refreshWolf(String movementResult) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (canPlayerReceive(nickName)) {
                try {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refreshWolf(
                                    movementResult);

                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);
                }
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
                try {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().disconnect(
                                    "La partita è terminata a causa di mancanza di giocatori, ci scusiamo");

                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);
                }
            }

        }
    }

    @Override
    public void refreshSpecialAnimalPosition(String nickName,
                                             SpecialAnimal animal,
                                             String region) {
        if (canPlayerReceive(nickName)) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(nickName)).getClientRmi().refreshSpecialAnimalInitialPosition(
                        animal + "," + region);

            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);

            }
        }

    }

    @Override
    public void brodcastPlayerDisconnected(String nickNameDisconnected) {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameDisconnected) && canPlayerReceive(
                    nickName)) {
                try {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refreshPlayerDisconnected(
                                    nickNameDisconnected);

                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);
                }
            }

        }
    }

    private void setPlayerOffline(String nickName, Exception ex) {
        Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                ex.getMessage(), ex);

        ServerManager.Nick2ClientProxyMap.get(nickName).setStatus(
                NetworkConstants.OFFLINE);

        DebugLogger.println(
                "Il player:" + nickName + " si è disconnesso, lo setto su offline");
    }

    @Override
    public void refreshGameParameters(String nickName, String[] nickNames,
                                      int[] wallets, int shepherd4player
    ) {
        if (canPlayerReceive(nickName)) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                        nickName)).getClientRmi().refreshGameParameters(
                                nickNames, wallets, shepherd4player);

            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);
            }
        }
    }

    @Override
    public void refreshNumberOfAvailableFence(String client, int fenceAvailable) {
        if (canPlayerReceive(client)) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                        client)).getClientRmi().refreshAvailableFences(
                                fenceAvailable);

            } catch (RemoteException ex) {
                setPlayerOffline(client, ex);
            }
        }
    }

    @Override
    public void refreshWallets() {
        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            try {
                if (canPlayerReceive(nickName)) {
                    for (Map.Entry couple : getNick2PlayerMap().entrySet()) {
                        String other = (String) couple.getKey();
                        if (!nickName.equals(other)) {

                            ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                                    nickName)).getClientRmi().refreshOtherPlayerWallet(
                                            other,
                                            getNick2PlayerMap().get(other).getMainShepherd().getWallet().getAmount());

                        }
                    }
                }
            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);
            }
        }
    }

    @Override
    public void refreshBankCards(String client, String[] regionTypes,
                                 int[] availableCards) {
        if (canPlayerReceive(client)) {
            try {
                for (int i = 0; i < regionTypes.length; i++) {
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            client)).getClientRmi().refreshBankCard(
                                    regionTypes[i],
                                    availableCards[i]);
                }

            } catch (RemoteException ex) {
                setPlayerOffline(client, ex);
            }
        }
    }

    @Override
    public boolean sellCard(String client, String[] sellableCards) throws
            PlayerDisconnectedException {
        if (canPlayerReceive(client)) {
            try {
                //ricevo se vuole vendere
                boolean wantToSell = ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                        client)).getClientRmi().askSellCard();

                if (wantToSell) {
                    //invio carte
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            client)).getClientRmi().sellCard(sellableCards);
                }
                return wantToSell;
            } catch (RemoteException ex) {
                setPlayerOffline(client, ex);

                throw new PlayerDisconnectedException(
                        "Il player:" + client + " si è disconnesso");
            }
        }
        return false;
    }

    @Override
    public boolean buyCard(String playerNickName, List<Card> buyableCards)
            throws PlayerDisconnectedException {
        if (canPlayerReceive(playerNickName)) {
            try {
                //ricevo se vuole comprare
                boolean wantToBuy = ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                        playerNickName)).getClientRmi().askBuyCard();

                if (wantToBuy) {
                    String[] names = new String[buyableCards.size()];
                    int[] prices = new int[buyableCards.size()];

                    for (int i = 0; i < buyableCards.size(); i++) {
                        names[i] = buyableCards.get(i).getType().toString();
                        prices[i] = buyableCards.get(i).getMarketValue();
                    }
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            playerNickName)).getClientRmi().buyCard(names,
                                    prices);

                }
                return wantToBuy;

            } catch (RemoteException ex) {
                setPlayerOffline(playerNickName, ex);

                throw new PlayerDisconnectedException(
                        "Il player:" + playerNickName + " si è disconnesso");

            }

        }
        return false;
    }
}
