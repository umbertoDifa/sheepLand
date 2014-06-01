package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

/**
 *
 * @author Francesco
 */
public interface TypeOfView {

    public void showWelcome();

    public void showEndGame();

    public void showInfo(String info);

    public void showBoughtLand(String boughLand,String price);
    
    public void showSetShepherd(String shepherdIndex, String streetIndex);

    public String setUpShepherd(int idShepherd);

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
                              int numbOfLamb);

    public void refreshStreet(int streetIndex, boolean Fence,
                              String nickShepherd);

    public void refreshMoveShepherd(String nickNameMover, String shepherdIndex,
                                    String streetIndex);

    public void refreshBuyLand(String buyer, String land,String price);

    public void refereshGameParameters(int numbOfPlayers, String firstPlayer,
                                       int shepherd4player);

    public void refereshCurrentPlayer(String currenPlayer);

    public void refereshCard(String type, int value);

    public void refreshBlackSheep(String regionIndex);

    public void refreshWolf(String regionIndex);

    public int chooseAction(int[] availableActions,
                            String[] availableStringedActions);

    public void refreshMoveOvine(String type, String startRegion,
                                 String endRegion);

    /**
     * Chiede regione partenza, regione arrivo, tipo di ovino per spostare un
     * ovino e ritorna i tre parametri separati da una virgola I paraetri
     * possono essree chiesti dalla view in qualsiasi ordine a patto di
     * restituirli come da contratto
     *
     * @return
     */
    public String moveOvine();

    public void moveShepherd(String startRegion, String endRegion);

    /**
     * Asks to the player which shepherd to move and in which streeet
     *
     * @return
     */
    public String askMoveShepherd();

    /**
     * it asks the player which land to buy and returns it
     *
     * @return
     */
    public String askBuyLand();

    public void showMoveShepherd(String priceToMove);

}
