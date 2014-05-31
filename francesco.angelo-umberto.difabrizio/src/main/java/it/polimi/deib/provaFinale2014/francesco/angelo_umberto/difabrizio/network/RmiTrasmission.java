package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

public class RmiTrasmission extends TrasmissionController {

    @Override
    public void refreshRegion(String nickName, int regionIndex, int numbOfSheep,
                              int numbOfRam, int numbOfLamb) {
        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshRegion(
                regionIndex, numbOfSheep, numbOfRam, numbOfLamb);
    }

    @Override
    public void refreshStreet(String nickName, int streetIndex, boolean fence,
                              String nickNameOfShepherdPlayer) {
        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refreshStreet(
                streetIndex, fence, nickNameOfShepherdPlayer);
    }

    @Override
    public void refreshGameParameters(String nickName, int numbOfPlayers,
                                      int shepherd4player) {
        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refereshGameParameters(
                numbOfPlayers, nickName, shepherd4player);
    }

    @Override
    public void refreshCurrentPlayer(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshCard(String nickName, String card, int value) {
        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().refereshCard(
                card, value);
    }

    @Override
    public void refreshBlackSheep(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshWolf(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshMoveShepherd(String nickNameMover, String newStreet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refreshKillOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean askSetUpShepherd(String nickName, int shepherdIndex) {
        return ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().setUpShepherd(
                shepherdIndex);
    }

    @Override
    public boolean askChooseAction(String nickName, String possibleActions) {
        ServerRmiImpl.NickClientRmiMap.get(nickName).getClientRmi().chooseAction(
                possibleActions);
        return false; //TODO
    }

    @Override
    public boolean askMoveOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean askMoveSheperd(String nickName) {
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

    @Override
    public void refreshMoveOvine(String nickName, String startRegion,
                                 String endRegion, String ovineType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void broadcastStartGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
