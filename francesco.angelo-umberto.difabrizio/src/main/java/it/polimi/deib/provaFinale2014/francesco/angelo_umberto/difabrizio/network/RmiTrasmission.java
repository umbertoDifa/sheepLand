package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.Player;
import java.util.List;

public class RmiTrasmission extends TrasmissionController {

    @Override
    public String refreshRegion(String nickName, int regionIndex, int numbOfSheep, int numbOfRam, int numbOfLamb) {
        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshRegion(regionIndex, numbOfSheep, numbOfRam, numbOfLamb);
    }

    @Override
    public String refreshStreet(String nickName, int streetIndex, boolean fence, String nickNameOfShepherdPlayer) {
        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshStreet(streetIndex, fence, nickNameOfShepherdPlayer);
    }

    @Override
    public String refreshGameParameters(String nickName, ) {
        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refereshGameParameters(numbOfPlayers, nickName, shepherd4player);
    }

    @Override
    public String refreshCurrentPlayer(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String refreshCard(String nickName, String card, int value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String refreshBlackSheep(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String refreshWolf(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String refreshMoveOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String refreshMoveShepherd(String nickNameMover, String newStreet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String refreshKillOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean askSetUpShepherd(String nickName, int shepherdIndex) {
        return ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().setUpShepherd(shepherdIndex);
    }

    @Override
    public boolean askChooseAction(String nickName, String[] possibleActions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String askMoveOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String askMoveSheperd(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
