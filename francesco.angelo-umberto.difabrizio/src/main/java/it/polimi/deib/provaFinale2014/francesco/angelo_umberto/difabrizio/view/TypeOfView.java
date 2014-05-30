package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.util.List;

/**
 *
 * @author Umberto
 */
//TODO: refactor
public interface TypeOfView {

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
                              int numbOfLamb);

    public void refreshStreet(int streetIndex, boolean Fence,
                              String nickShepherd);

    public void refereshGameParameters(int numbOfPlayers, String firstPlayer,
                                       int shepherd4player);

    public void refereshCurrentPlayer(String currenPlayer);

    public void refereshCards(String[] myCards);

    public void refreshBlackSheep(int regionIndex);

    public void refreshWolf(int regionIndex);

    public boolean setUpShepherds();

    public int chooseAction(int[] avaibleActions, String[] avaibleStringedActions);

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
    
    public String askNickName();
    
    public int askIdShepherd();
    
    public String askStreet();
}
