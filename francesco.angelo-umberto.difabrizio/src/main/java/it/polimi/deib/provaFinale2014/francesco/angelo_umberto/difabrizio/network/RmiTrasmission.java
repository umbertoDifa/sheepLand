package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.BlackSheep;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Wolf;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.rmi.RemoteException;
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

    @Override
    public void refreshStreet(String nickName, int streetIndex, boolean fence,
                              String nickNameOfShepherdPlayer) {
        if (canPlayerReceive(nickName)) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(nickName)).getClientRmi().refreshStreet(
                        streetIndex, fence, nickNameOfShepherdPlayer);
            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);

            }
        }

    }

    @Override
    public void refreshGameParameters(String nickName, int numbOfPlayers,
                                      int shepherd4player) {
        if (canPlayerReceive(nickName)) {
            try {
                ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(nickName)).getClientRmi().refreshGameParameters(
                        numbOfPlayers, nickName, shepherd4player);
            } catch (RemoteException ex) {
                setPlayerOffline(nickName, ex);

            }
        }

    }

    @Override
    public void refreshCurrentPlayer(String nickNamePlayer) {
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

    @Override
    public void refreshMoveShepherd(String nickNameMover, String shepherdInedx,
                                    String newStreet) {
        String nickName = null;

        try {
            //per tutti i nick tranne quello dato refresha
            for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
                nickName = (String) pairs.getKey();
                if (!nickName.equals(nickNameMover) && canPlayerReceive(nickName)) {

                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refreshMoveShepherd(
                                    nickNameMover, shepherdInedx, newStreet);
                }
            }
        } catch (RemoteException ex) {
            setPlayerOffline(nickName, ex);
        }
    }

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
                    ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                            nickName)).getClientRmi().refreshMoney(type);
                } catch (RemoteException ex) {
                    setPlayerOffline(nickName, ex);
                }
            }

        }
    }

    @Override
    public boolean askSetUpShepherd(String nickName, int shepherdIndex) throws
            PlayerDisconnectedException {
        try {
            String chosenStreet = ((RmiClientProxy) ServerManager.Nick2ClientProxyMap.get(
                    nickName)).getClientRmi().setUpShepherd(
                            shepherdIndex);

            //invia conferma riepilogativa agli utenti
            if (chosenStreet != null) {
                refreshMoveShepherd(nickName, "" + shepherdIndex, chosenStreet);
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
                        refreshMoveShepherd(nickName, token[1], token[2]);
                        return true;
                    case '3':
                        refreshBuyLand(nickName, token[2], token[3]);
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
                        //refresho i soldi a tutti
                        for (Map.Entry pairs : getNick2PlayerMap().entrySet()) {
                            String nick = (String) pairs.getKey();
                            refreshMoney(nick);
                        }
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

    @Override
    public void broadcastStartGame(String nickName) {

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

    @Override
    public void refreshBuyLand(String nickNameBuyer, String boughtLand,
                               String price) throws RemoteException {

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

    @Override
    public void refreshSpecialAnimal(SpecialAnimal animal, String movementResult) {
        if (animal instanceof BlackSheep) {
            this.refreshBlackSheep(movementResult);
        } else if (animal instanceof Wolf) {
            this.refreshWolf(movementResult);
        }
    }

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

    @Override
    public void refreshMoney(String nickName) {
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
                                    "Il gioco è terminato\nArriverderci!");  //TODO to test

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
    public void refreshSpecialAnimalInitialPosition(String nickName,
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
    public void refreshPlayerDisconnected(String nickNameDisconnected) {
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
                NetworkConstants.OFFLINE.getValue());

        DebugLogger.println(
                "Il player:" + nickName + " si è disconnesso, lo setto su offline");
    }

}
