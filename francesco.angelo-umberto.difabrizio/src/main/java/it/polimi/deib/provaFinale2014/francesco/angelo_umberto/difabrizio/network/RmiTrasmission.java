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
                              int numbOfRam, int numbOfLamb) throws
            RemoteException {
        try {
            ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshRegion(
                    regionIndex, numbOfSheep, numbOfRam, numbOfLamb);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            throw new RemoteException(
                    "Il player:" + nickName + " si è disconnesso");
        }

    }

    @Override
    public void refreshStreet(String nickName, int streetIndex, boolean fence,
                              String nickNameOfShepherdPlayer) throws
            RemoteException {
        try {
            ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshStreet(
                    streetIndex, fence, nickNameOfShepherdPlayer);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            throw new RemoteException(
                    "Il player:" + nickName + " si è disconnesso");
        }

    }

    @Override
    public void refreshGameParameters(String nickName, int numbOfPlayers,
                                      int shepherd4player) throws
            RemoteException {
        try {
            ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshGameParameters(
                    numbOfPlayers, nickName, shepherd4player);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            throw new RemoteException(
                    "Il player:" + nickName + " si è disconnesso");
        }

    }

    @Override
    public void refreshCurrentPlayer(String nickNamePlayer) {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            try {
                ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refereshCurrentPlayer(
                        nickNamePlayer);
            } catch (RemoteException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);

            }

        }
    }

    @Override
    public void refreshCard(String nickName, String card, int value) throws
            RemoteException {
        try {
            ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshCard(
                    card, value);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            throw new RemoteException(
                    "Il player:" + nickName + " si è disconnesso");
        }

    }

    @Override
    public void refreshMoveShepherd(String nickNameMover, String shepherdInedx,
                                    String newStreet)
            throws RemoteException {
        String nickName = null;

        try {
            //per tutti i nick tranne quello dato refresha
            for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
                nickName = (String) pairs.getKey();
                if (!nickName.equals(nickNameMover)) {

                    ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshMoveShepherd(
                            nickNameMover, shepherdInedx, newStreet);
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            throw new RemoteException(
                    "Il player:" + nickName + " si è disconnesso");
        }
    }

    @Override
    public void refreshKillOvine(String nickNameKiller, String region,
                                 String type,
                                 String outcome) {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameKiller)) {
                try {
                    ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshKillOvine(
                            nickNameKiller, region, type, outcome);
                } catch (RemoteException ex) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);

                }
            }

        }
    }

    @Override
    public boolean askSetUpShepherd(String nickName, int shepherdIndex) throws
            RemoteException {
        try {
            String chosenStreet = ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().setUpShepherd(
                    shepherdIndex);

            //invia conferma riepilogativa agli utenti
            if (chosenStreet != null) {
                refreshMoveShepherd(nickName, "" + shepherdIndex, chosenStreet);
                return true;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            throw new RemoteException(
                    "Il player:" + nickName + " si è disconnesso");
        }
        return false;
    }

    @Override
    public boolean askChooseAction(String nickName, String possibleActions)
            throws RemoteException {
        try {
            String action = ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().chooseAction(
                    possibleActions);
            DebugLogger.println("ricevuto: " + action);
            if (!action.contains("null")) {
                //tokenizza 
                String token[] = action.split(",");

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
                        return true;
                }
            }
            return false;

        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
            throw new RemoteException(
                    "Il player:" + nickName + " si è disconnesso");
        }
    }

    @Override
    public boolean askMoveOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean askMoveSheperd(String nickName) throws RemoteException {
        return false;
    }

    @Override
    public boolean askBuyLand(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean askKillOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean askMateSheepWith(String nickName, String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void askThrowDice(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshInfo(String nickName, String info) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshMoveOvine(String nickNameMover, String startRegion,
                                 String endRegion, String ovineType) {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameMover)) {
                try {
                    ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshMoveOvine(
                            nickNameMover, startRegion, endRegion, ovineType);
                } catch (RemoteException ex) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);

                }
            }

        }

    }

    @Override
    public void broadcastStartGame() throws RemoteException {

        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            try {
                ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().welcome();
                ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().connectPlayer(
                        super.getNick2PlayerMap().get(nickName));
            } catch (RemoteException ex) {
                Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                        ex.getMessage(), ex);
                throw new RemoteException(
                        "Il player:" + nickName + " si è disconnesso");
            }

        }
    }

    @Override
    public void refreshBuyLand(String nickNameBuyer, String boughtLand,
                               String price) throws RemoteException {

        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameBuyer)) {
                try {
                    ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshBuyLand(
                            nickNameBuyer, boughtLand, price);
                } catch (RemoteException ex) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE,
                            ex.getMessage(), ex);

                }
            }

        }

    }

    @Override
    public void refreshSpecialAnimal(SpecialAnimal animal, String movementResult)
            throws
            RemoteException {
        if (animal instanceof BlackSheep) {
            this.refreshBlackSheep(movementResult);
        } else if (animal instanceof Wolf) {
            this.refreshWolf(movementResult);
        }
    }

    @Override
    public void refreshMateSheepWith(String nickNameMater, String region,
                                     String otherType, String newType,
                                     String outcome) throws
            RemoteException {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameMater)) {
                try {
                    ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshMateSheepWith(
                            nickNameMater, region, otherType, newType, outcome);
                } catch (RemoteException ex) {
                    Logger.getLogger(DebugLogger.class.getName()).log(
                            Level.SEVERE, ex.getMessage(), ex);

                }
            }

        }

    }

    @Override
    public void refreshMoney(String nickName) throws RemoteException {
        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshMoney(
                ""
                + super.getNick2PlayerMap().get(nickName).getMainShepherd().getWallet().getAmount());
    }

    @Override
    public void sendRank(boolean winner, String nickName, int score) throws
            RemoteException {
        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().showMyRank(
                "" + winner, "" + score);
    }

    @Override
    public void sendClassification(String classification) throws RemoteException {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            try {
                ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().showClassification(
                        classification);
            } catch (RemoteException ex) {
                Logger.getLogger(DebugLogger.class
                        .getName()).log(Level.SEVERE,
                                ex.getMessage(), ex);

            }

        }
    }

    private void refreshBlackSheep(String movementResult) {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            try {
                ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshBlackSheep(
                        movementResult);

            } catch (RemoteException ex) {
                Logger.getLogger(DebugLogger.class
                        .getName()).log(Level.SEVERE,
                                ex.getMessage(), ex);

            }

        }
    }

    private void refreshWolf(String movementResult) {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            try {
                ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshWolf(
                        movementResult);

            } catch (RemoteException ex) {
                Logger.getLogger(DebugLogger.class
                        .getName()).log(Level.SEVERE,
                                ex.getMessage(), ex);

            }

        }
    }

}
