package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

public class SocketTrasmission extends TrasmissionController {
    
    public String refreshRegion(String nickName, int regionIndex, int numbOfSheep, int numbOfRam, int numbOfLamb) {
        ServerSockets.NickSocketMap.get(nickName).send("regione " + regionIndex 
                + numbOfSheep+" pecore, "+numbOfLamb + " agnelli, " + numbOfRam + " montoni");
    }

    public String refreshStreet(String nickName, int streetIndex, boolean fence, String nickNameOfShepherdPlayer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String refreshGameParameters(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String refreshCurrentPlayer(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String refreshCard(String nickName, String card, int value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String refreshBlackSheep(String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String refreshWolf(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String refreshMoveOvine(String nickName) {
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
    public String refreshMoveShepherd(String nickNameMover, String newStreet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String refreshKillOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean askSetUpShepherd(String nickName, int shepherdIndex) {
        ServerSockets.NickSocketMap.get(nickName).send("Inserire una strada per il tuo pastore numero" + shepherdIndex);
        String chosenStringedStreet = ServerSockets.NickSocketMap.get(nickName).receive();
        String result = super.getNick2PlayerMap().get(nickName).setShepherd(shepherdIndex, chosenStringedStreet);
        ServerSockets.NickSocketMap.get(nickName).send(result);
        if (result.contains("Patore posizionato corretamente!")) {
            return true;
        }
        return false;
    }
    
    public boolean askChooseAction(String nickName, String[] possibleActions) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String askMoveOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String askMoveSheperd(String nickName) {
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
