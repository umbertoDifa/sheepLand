package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

import java.rmi.Remote;
import java.util.List;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public interface RemoteClientRmi extends Remote {

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
                              int numbOfLamb);

    public void refreshStreet(int streetIndex, boolean Fence,
                              String nickShepherd);

    public void refereshGameParameters(int numbOfPlayers, String firstPlayer,
                                       int shepherd4player);

    public void refereshCurrentPlayer(String currenPlayer);

    public void refereshCards(List<String> myCards);

    public void refreshBlackSheep(int regionIndex);

    public void refreshWolf(int regionIndex);

    public void setUpShepherds();

    public void chooseAction();

    public void moveOvine();

    public void refreshMoveOvine(int startRegionIndex, int endRegionIndex,
                                 String type);

    public void moveShepherd();

    public void refreshMoveShepherd();

    public void buyLand();

    public void mateSheepWith();

    public void refreshMateSheepWith(int regionIndex, String ovineType);

    public void killOvine();

    public void refreshKillOvine(int regionIndex);

}
