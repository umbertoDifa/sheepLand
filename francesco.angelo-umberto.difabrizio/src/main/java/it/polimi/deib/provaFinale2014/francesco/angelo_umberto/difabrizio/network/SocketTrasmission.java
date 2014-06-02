package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.BlackSheep;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.SpecialAnimal;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model.Wolf;
import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.util.Map;

public class SocketTrasmission extends TrasmissionController {

    public void refreshRegion(String nickName, int regionIndex, int numbOfSheep,
                              int numbOfRam, int numbOfLamb) {
        ServerSockets.NickSocketMap.get(nickName).send("RefreshRegion");
        ServerSockets.NickSocketMap.get(nickName).send(
                regionIndex + "," + numbOfSheep + "," + numbOfRam + "," + numbOfLamb);
    }

    public void refreshStreet(String nickName, int streetIndex, boolean fence,
                              String nickNameOfShepherdPlayer) {
        ServerSockets.NickSocketMap.get(nickName).send("RefreshStreet");
        ServerSockets.NickSocketMap.get(nickName).send(
                streetIndex + "," + fence + "," + nickNameOfShepherdPlayer);
    }

    public void refreshGameParameters(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void refreshCurrentPlayer(String nickNamePlayer) {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNamePlayer)) {
                ServerSockets.NickSocketMap.get(nickName).send(
                        "RefreshCurrentPlayer");
                ServerSockets.NickSocketMap.get(nickName).send(nickNamePlayer);
            }

        }

    }

    public void refreshCard(String nickName, String type, int value) {
        ServerSockets.NickSocketMap.get(nickName).send("RefreshCard");
        ServerSockets.NickSocketMap.get(nickName).send(type + "," + value);
    }

    public void refreshBuyLand(String nickNameBuyer, String boughtLand,
                               String price) {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameBuyer)) {
                ServerSockets.NickSocketMap.get(nickName).send("RefreshBuyLand");
                ServerSockets.NickSocketMap.get(nickName).send(
                        nickNameBuyer + "," + boughtLand + "," + price);
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
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();

            ServerSockets.NickSocketMap.get(nickName).send("RefreshBlackSheep");
            ServerSockets.NickSocketMap.get(nickName).send(movementResult);

        }
    }

    private void refreshWolf(String movementResult) {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();

            ServerSockets.NickSocketMap.get(nickName).send("RefreshWolf");
            ServerSockets.NickSocketMap.get(nickName).send(movementResult);

        }
    }

    public void refreshMoveOvine(String nickNameMover, String startRegion,
                                 String endRegion, String ovineType) {
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameMover)) {
                ServerSockets.NickSocketMap.get(nickName).send(
                        "RefreshMoveOvine");
                ServerSockets.NickSocketMap.get(nickName).send(
                        nickNameMover + "," + startRegion + "," + endRegion + "," + ovineType);
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
     */
    public void refreshMoveShepherd(String nickNameMover, String shepherdIndex,
                                    String newStreet) {
        //per tutti i nick tranne quello dato refresha
        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            if (!nickName.equals(nickNameMover)) {
                ServerSockets.NickSocketMap.get(nickName).send(
                        "RefreshMoveShepherd");
                ServerSockets.NickSocketMap.get(nickName).send(
                        nickNameMover + "," + shepherdIndex + "," + newStreet);
            }

        }
    }

    public void refreshKillOvine(String nickName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean askSetUpShepherd(String nickName, int shepherdIndex) {
        ServerSockets.NickSocketMap.get(nickName).send("SetUpShepherd");
        ServerSockets.NickSocketMap.get(nickName).send("" + shepherdIndex);
        //ricevo la stringa della strada
        String chosenStringedStreet = ServerSockets.NickSocketMap.get(nickName).receive();

        //tento di eseguire la setShepherd
        String result = super.getNick2PlayerMap().get(nickName).setShepherd(
                shepherdIndex, chosenStringedStreet);

        //invio il risultato qualsiasi sia
        ServerSockets.NickSocketMap.get(nickName).send(result + ","
                + shepherdIndex + "," + chosenStringedStreet);

        //ritorno il successo o meno dell'operazione
        if (result.contains("Pastore posizionato correttamente!")) {
            //refresho
            refreshMoveShepherd(nickName, "" + shepherdIndex,
                    chosenStringedStreet);
            return true;
        }
        return false;
    }

    public boolean askChooseAction(String nickName, String possibleActions) {
        ServerSockets.NickSocketMap.get(nickName).send("ChooseAction");
        DebugLogger.println("choose action inviata a " + nickName);

        ServerSockets.NickSocketMap.get(nickName).send(possibleActions);
        String result = ServerSockets.NickSocketMap.get(nickName).receive();

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
            //return askMateSheepWith(nickName);
            case 5:
            // return askKillOvine(nickName);
        }
        return false;
    }

    public boolean askMoveOvine(String nickName) {
        ServerSockets.NickSocketMap.get(nickName).send("MoveOvine");

        //ricevo i parametri
        String result = ServerSockets.NickSocketMap.get(nickName).receive();
        String[] token = result.split(",");

        result = super.getNick2PlayerMap().get(nickName).moveOvine(token[0],
                token[1], token[2]);
        DebugLogger.println(result);
        ServerSockets.NickSocketMap.get(nickName).send(result);
        if (result.contains("Ovino mosso")) {
            //refreshio
            refreshMoveOvine(nickName, token[0], token[1], token[2]);
            return true;
        }
        return false;
    }

    public boolean askMoveSheperd(String nickName) {

        ServerSockets.NickSocketMap.get(nickName).send("MoveShepherd");

        String result = ServerSockets.NickSocketMap.get(nickName).receive();

        String[] token = result.split(",");

        //eseguo
        result = super.getNick2PlayerMap().get(nickName).moveShepherd(token[0],
                token[1]);

        //invio il risultato
        ServerSockets.NickSocketMap.get(nickName).send(result);

        if (result.contains("Pastore spostato")) {
            //invia conferma riepilogativa agli utenti
            refreshMoveShepherd(nickName, token[0], token[1]);
            return true;
        }
        return false;

    }

    public boolean askBuyLand(String nickName) {
        ServerSockets.NickSocketMap.get(nickName).send("BuyLand");
        String landToBuy = ServerSockets.NickSocketMap.get(nickName).receive();

        String result = super.getNick2PlayerMap().get(nickName).buyLand(
                landToBuy);

        //invio il risultato al client
        ServerSockets.NickSocketMap.get(nickName).send(result);

        String[] token = result.split(",");

        if (result.contains("Carta acquistata")) {
            refreshBuyLand(nickName, token[1], token[2]);
            return true;
        }
        return false;
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

    @Override
    public void refreshGameParameters(String nickName, int numbOfPlayers,
                                      int shepherd4player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void broadcastStartGame() {

        for (Map.Entry pairs : super.getNick2PlayerMap().entrySet()) {
            String nickName = (String) pairs.getKey();
            ServerSockets.NickSocketMap.get(nickName).send("Avvio gioco");
            ServerSockets.NickSocketMap.get(nickName).send("Welcome");
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

}
