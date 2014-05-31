package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

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

        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshRegion(
                regionIndex, numbOfSheep, numbOfRam, numbOfLamb);

    }

    @Override
    public void refreshStreet(String nickName, int streetIndex, boolean fence,
                              String nickNameOfShepherdPlayer) throws
            RemoteException {

        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshStreet(
                streetIndex, fence, nickNameOfShepherdPlayer);

    }

    @Override
    public void refreshGameParameters(String nickName, int numbOfPlayers,
                                      int shepherd4player) throws
            RemoteException {

        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshGameParameters(
                numbOfPlayers, nickName, shepherd4player);

    }

    @Override
    public void refreshCurrentPlayer(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshCard(String nickName, String card, int value) throws
            RemoteException {

        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refereshCard(
                card, value);

    }

    @Override
    public void refreshBlackSheep(int regionIndex) {
        //ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshBlackSheep(regionIndex);
    }

    @Override
    public void refreshWolf(int regionIndex) {
        //ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshWolf(regionIndex);
    }

    @Override
    public void refreshMoveShepherd(String nickNameMover, String newStreet)
            throws RemoteException {
        //per tutti i nick tranne quello dato refresha
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameMover)) {

                ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshMoveShepherd(
                        nickNameMover, newStreet);

            }

        }
    }

    @Override
    public void refreshKillOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean askSetUpShepherd(String nickName, int shepherdIndex) throws
            RemoteException {

        return ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().setUpShepherd(
                shepherdIndex);

    }

    @Override
    public boolean askChooseAction(String nickName, String possibleActions) {
        try {
            ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().chooseAction(
                    possibleActions);
        } catch (RemoteException ex) {
            Logger.getLogger(DebugLogger.class.getName()).log(Level.SEVERE,
                    ex.getMessage(), ex);
        }
        return false; //TODO
    }

    @Override
    public boolean askMoveOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean askMoveSheperd(String nickName) throws RemoteException {

        return ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().moveShepherd();

    }

    @Override
    public String buyLand(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String askKillOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String askMateSheepWith(String nickName) {
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
    public void refreshMoveOvine(String nickName, String startRegion,
                                 String endRegion, String ovineType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void broadcastStartGame() throws RemoteException {

        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();

            ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().welcome();
            ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().connectPlayer(
                    super.getNick2PlayerMap().get(nickName));

        }
    }

}
