package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

public class SocketTrasmission extends TrasmissionController {

    public void refreshRegion(String nickName, int regionIndex, int numbOfSheep, int numbOfRam, int numbOfLamb) {
        ServerSockets.NickSocketMap.get(nickName).send("regione " + regionIndex
                + numbOfSheep + " pecore, " + numbOfLamb + " agnelli, " + numbOfRam + " montoni");
    }

    public void refreshStreet(String nickName, int streetIndex, boolean fence, String nickNameOfShepherdPlayer) {
        ServerSockets.NickSocketMap.get(nickName).send(streetIndex + "," + fence + "," + nickNameOfShepherdPlayer);
    }

    public void refreshGameParameters(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshCurrentPlayer(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshCard(String nickName, String card, int value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshBlackSheep(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshWolf(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshMoveOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * informa tutti tranne il nickname che il nickname ha mosso lo shepherd
     * nella newStreet
     *
     * @param nickNameMover
     * @param newStreet
     * @return
     */
    public void refreshMoveShepherd(String nickNameMover, String newStreet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshKillOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean askSetUpShepherd(String nickName, int shepherdIndex) {
        ServerSockets.NickSocketMap.get(nickName).send("SetUpShepherd");
        ServerSockets.NickSocketMap.get(nickName).send(""+shepherdIndex);
        String chosenStringedStreet = ServerSockets.NickSocketMap.get(nickName).receive();
        String result = super.getNick2PlayerMap().get(nickName).setShepherd(shepherdIndex, chosenStringedStreet);
        ServerSockets.NickSocketMap.get(nickName).send(result);
        if (result.contains("Patore posizionato corretamente!")) {
            return true;
        }
        return false;
    }

    public boolean askChooseAction(String nickName, String possibleActions) {
        ServerSockets.NickSocketMap.get(nickName).send("ChooseActions");
        ServerSockets.NickSocketMap.get(nickName).send(possibleActions);
        String result = ServerSockets.NickSocketMap.get(nickName).receive();
        int action = Integer.parseInt(result);
        switch (action) {
            case 1:
                return askMoveOvine(nickName);
            case 2:
                return askMoveSheperd(nickName);
            case 3:
                return buyLand(nickName);
            case 4:
                return askMateSheepWith(nickName);
            case 5:
                return askKillOvine(nickName);
        }
    }    
    

    public boolean askMoveOvine(String nickName) {
        ServerSockets.NickSocketMap.get(nickName).send("MoveOvine");
        String result = ServerSockets.NickSocketMap.get(nickName).receive();
        String token[] = result.split(",");
        result = super.getNick2PlayerMap().get(nickName).moveOvine(token[0], token[1], token[2]);
        if( result.contains("Ovino mosso!"));
            ServerSockets.NickSocketMap.get(nickName).send();
    }

    public boolean askMoveSheperd(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String buyLand(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String askKillOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String askMateSheepWith(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void askThrowDice(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshInfo(String nickName, String info) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
