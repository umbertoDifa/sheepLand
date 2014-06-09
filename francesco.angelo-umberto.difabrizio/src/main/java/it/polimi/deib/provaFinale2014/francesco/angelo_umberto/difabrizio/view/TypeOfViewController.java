package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

/**
 *
 * @author Francesco
 */
public interface TypeOfViewController {

    public void showWelcome();

    public void showEndGame();

    public void showInfo(String info);

    public void showBoughtLand(String boughLand, String price);

    public void showSetShepherd(int shepherdIndex, String streetIndex);

    public void showMoveShepherd(String shepherdIndex, String priceToMove);

    public void showMoveOvine(String startRegion, String endRegion, String type);

    public void showMateSheepWith(String region, String otherType,
                                  String newType);

    public void showMyRank(Boolean winner, String rank);

    /**
     * Una stringa della classfica in cui si alternano nome del player,
     * punteggio
     *
     * @param classification classifica stringhizzata
     */
    public void showClassification(String classification);

    public void showUnexpectedEndOfGame();

    public void showKillOvine(String region, String type, String shepherdPayed);

    public String setUpShepherd(int idShepherd);

    public void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
                              int numbOfLamb);

    public void refreshStreet(int streetIndex, boolean fence,
                              String nickShepherd);

    public void refreshMoveShepherd(String nickNameMover, int shepherdIndex,
                                    String streetIndex);

    public void refreshBuyLand(String buyer, String land, int price);

    public void refreshGameParameters(String[] nickNames, int shepherd4player);

    public void refreshKillOvine(String killer, String region, String type,
                                 String outcome);

    public void refreshMoney(String money);

    public void refereshCurrentPlayer(String currenPlayer);

    public void refereshCard(String type, int value);

    public void refreshBlackSheep(String result);

    public void refreshWolf(String result);

    public void refreshPlayerDisconnected(String player);

    public void specialAnimalInitialCondition(String region);

    public String chooseAction(int[] availableActions,
                               String[] availableStringedActions);

    public void refreshMateSheepWith(String nickName, String region,
                                     String otherType, String newType,
                                     String outcome);

    public void refreshMoveOvine(String nickName, String type,
                                 String startRegion,
                                 String endRegion);

    /**
     * Chiede regione partenza, regione arrivo, tipo di ovino per spostare un
     * ovino e ritorna i tre parametri separati da una virgola I paraetri
     * possono essree chiesti dalla view in qualsiasi ordine a patto di
     * restituirli come da contratto
     *
     * @return
     */
    public String askMoveOvine();

    /**
     * Chiede il nickName
     *
     * @return nickName
     */
    public String askNickName();

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

    /**
     * Asks the player to choose a shepherd and a region. Then returns this
     * information
     *
     * @return shepherd,region
     */
    public String askMateSheepWith();

    /**
     * Asks the player to insert shepherdNear the region of the ovine to kill,
     * region where to kill the ovine and type to kill in case of success
     *
     * @return ShepherdIndex,region,type
     */
    public String askKillOvine();

    /**
     * Refresh the number of available fence
     *
     * @param fences fence available
     */
    public void refreshFences(int fences);

}
