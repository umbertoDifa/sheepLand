package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

/**
 *
 * @author Francesco
 */
public interface TypeOfView {

    public void showInfo(String info);

    public String setUpShepherd(int idShepherd);

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

    public int chooseAction(int[] availableActions,
                            String[] availableStringedActions);
    
    public void moveOvine(String type, String startRegion, String endRegion);
}
