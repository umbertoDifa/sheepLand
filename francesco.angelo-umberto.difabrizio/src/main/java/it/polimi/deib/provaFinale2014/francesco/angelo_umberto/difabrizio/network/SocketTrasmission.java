package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Street;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.exceptions.BusyStreetException;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;

public class SocketTrasmission implements TrasmissionController {

    public void refreshAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int askRegion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void broadcastInitialCondition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void broadcastRegion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setUpShepherd() {

    }

    public String askStreet(String nickName, int idShepherd) {
        DebugLogger.println("Chiedo una strada in askStreet");

        //raccogli decisione
        String stringedStreet = talkTo(nickName,
                "In quale strada vuoi posizionare il pastore " + Integer.toString(
                        idShepherd + 1) + " ?");
        DebugLogger.println("Risposta sulla strada ottenuta");

        return stringedStreet;
    }

    public String talkTo(String playerNickName, String message) {
        this.sendTo(playerNickName, message);
        return receiveFrom(playerNickName);
    }

    public void sendTo(String playerNickName, String message) {
        ServerManager.NickSocketMap.get(playerNickName).send(message);
    }

    public String receiveFrom(String playerNickName) {
        return ServerManager.NickSocketMap.get(playerNickName).receive();
    }

}
