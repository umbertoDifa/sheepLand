package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

/**
 * It has all the methods to show info to the relative view
 *
 * @author Francesco
 */
public interface TypeOfViewController {

    /**
     * Welcomes the player
     */
    void showWelcome();

    /**
     * Show the last view of the game
     */
    void showEndGame();

    /**
     * Shows informations
     *
     * @param info Message to show
     */
    void showInfo(String info);

    /**
     * Show to the player the boughtLand
     *
     * @param boughLand Land that was bought
     * @param price Price of the land
     */
    void showBoughtLand(String boughLand, String price);

    /**
     * Shows the result of the setShepherd
     *
     * @param shepherdIndex Which shwpherd was setted
     * @param streetIndex In which street
     */
    void showSetShepherd(int shepherdIndex, String streetIndex);

    /**
     * Show the result of the move shepherd
     *
     * @param shepherdIndex Which shehperd was moved
     * @param priceToMove At which price
     */
    void showMoveShepherd(String shepherdIndex, String priceToMove);

    /**
     * Shows the result of the move ovine
     *
     * @param startRegion Region where teh ovine was
     * @param endRegion Region where the ovine is
     * @param type Type of ovine moved
     */
    void showMoveOvine(String startRegion, String endRegion, String type);

    /**
     * Shows the result of the mateSheepWith
     *
     * @param region region of the mating
     * @param otherType Type that mate with the sheep
     * @param newType Type created
     */
    void showMateSheepWith(String region, String otherType,
            String newType);

    /**
     * Shows the rank at the end of the game
     *
     * @param winner If the user is a winner
     * @param rank The rank of the user
     */
    void showMyRank(Boolean winner, String rank);

    /**
     * Una stringa della classfica in cui si alternano nome del player,
     * punteggio
     *
     * @param classification classifica stringhizzata
     */
    void showClassification(String classification);

    /**
     * Message shown if the game ends unexpectedly
     */
    void showUnexpectedEndOfGame();

    /**
     * Shows the result of teh kill ovien
     *
     * @param region Region where it was killed
     * @param type Type killed
     * @param shepherdPayed Who many shepherd were paied
     */
    void showKillOvine(String region, String type, String shepherdPayed);

    /**
     * Return where the user wants to setUp the shepherd
     *
     * @param idShepherd Shepherd to setUp
     *
     * @return The street
     */
    String setUpShepherd(int idShepherd);

    /**
     * Refreshes the status of teh region
     *
     * @param regionIndex Which region
     * @param numbOfSheep Number of sheeps in that region
     * @param numbOfRam Number of rams
     * @param numbOfLamb Numebr of lambs
     */
    void refreshRegion(int regionIndex, int numbOfSheep, int numbOfRam,
            int numbOfLamb);

    /**
     * Refresh the status of a street
     *
     * @param streetIndex Which street
     * @param fence If there is a fence
     * @param nickShepherd If there is a shepherd
     * @param shepherdIndex
     */
    void refreshStreet(int streetIndex, boolean fence,
            String nickShepherd, int shepherdIndex);

    /**
     * Refreshes to the player the outcome of a move shepherd
     *
     * @param nickNameMover Who moved the shepherd
     * @param shepherdIndex Which sheperd was moved
     * @param endStreet Where it was moved
     * @param price At which price
     */
    void refreshMoveShepherd(String nickNameMover, int shepherdIndex,
            String endStreet, int price);

    /**
     * Refreshes to the player the outcome of a buyland
     *
     * @param buyer Who bought the land
     * @param land Which land
     * @param price At which price
     */
    void refreshBuyLand(String buyer, String land, int price);

    /**
     * Refreshes the game parameters
     *
     * @param nickNames The nickNames of the player in the game
     * @param wallets The money of those players in the game
     * @param shepherd4player How many shepherd for player
     */
    void refreshGameParameters(String[] nickNames, int[] wallets,
            int shepherd4player);

    /**
     * Refresh the outcome of a kill ovine
     *
     * @param killer Which player killed the ovine
     * @param region IN which region
     * @param type Which ovine
     * @param outcome If the murder was successful
     */
    void refreshKillOvine(String killer, String region, String type,
            String outcome);

    /**
     * Refreshes the ammount of money of the player
     *
     * @param money money of the player
     */
    void refreshMoney(String money);

    /**
     * Refreshes the ammount of money of another player
     *
     * @param otherPlayer the other player
     * @param money his money
     */
    void refreshOtherPlayerMoney(String otherPlayer, int money);

    /**
     * Refershes the current player
     *
     * @param currenPlayer The player playing
     */
    void refereshCurrentPlayer(String currenPlayer);

    /**
     * refreshes which card the player has
     *
     * @param type Type of the card
     * @param value Number of card owned
     */
    void refereshCard(String type, int value);

    /**
     * refreshes the opsition of the blacksheep
     *
     * @param result
     */
    void refreshBlackSheep(String result);

    /**
     * Refreshses the position of the wolf
     *
     * @param result Position of the wolf
     */
    void refreshWolf(String result);

    /**
     * Refreshes the player that someone has gone offline
     *
     * @param player Player who went offline
     */
    void refreshPlayerDisconnected(String player);

    /**
     * Refresh where the specialAnimals are in the beginning
     *
     * @param region Region of the special animals
     */
    void specialAnimalInitialCondition(String region);

    /**
     * Ask the user to choose an action between the availabel ones
     *
     * @param availableActions Actions available
     * @param availableStringedActions Name of the available actions
     *
     * @return Action chosen
     */
    String chooseAction(int[] availableActions,
            String[] availableStringedActions);

    /**
     * Refreshes the outcome a mateSheepWith
     *
     * @param nickName Who mate the sheep
     * @param region In which region
     * @param otherType The type who mate with the sheep
     * @param newType The type that was born
     * @param outcome The outcome
     */
    void refreshMateSheepWith(String nickName, String region,
            String otherType, String newType,
            String outcome);

    /**
     * Refreshes the outcome of a move ovine
     *
     * @param nickName Who moved the ovine
     * @param type Which type of ovine was moved
     * @param startRegion From which region
     * @param endRegion To which region
     */
    void refreshMoveOvine(String nickName, String type,
            String startRegion,
            String endRegion);

    /**
     * Ask the ovine to move, the region where it was and the new region
     *
     * @return Start region,endRegion , type
     */
    String askMoveOvine();

    /**
     * Chiede il nickName
     *
     * @return nickName
     */
    String askNickName();

    /**
     * Asks to the player which shepherd to move and in which streeet
     *
     * @return Shepherd,street
     */
    String askMoveShepherd();

    /**
     * it asks the player which land to buy and returns it
     *
     * @return land to buy
     */
    String askBuyLand();

    /**
     * Asks the player to choose a shepherd and a region. Then returns this
     * information
     *
     * @return shepherd,region
     */
    String askMateSheepWith();

    /**
     * Asks the player to insert shepherdNear the region of the ovine to kill,
     * region where to kill the ovine and type to kill in case of success
     *
     * @return ShepherdIndex,region,type
     */
    String askKillOvine();

    /**
     * Refresh the number of available fence
     *
     * @param fences fence available
     */
    void refreshFences(int fences);

    /**
     * Refreshes the number of available cards of a region in the bank
     *
     * @param regionType type of region
     * @param availableCards number of available cards
     */
    void refreshBankCard(String regionType, int availableCards);

    /**
     * Asks to the player if he wants to sell cards
     *
     * @param action comprare o vendere
     *
     * @return true if he want, false if not
     */
    boolean askWillingTo(String action);

    /**
     * ask which card to sell between the available ones
     *
     *
     * @param availableCards cards that can be sold
     *
     * @return the chosen card
     */
    String askSellCard(String[] availableCards);

    /**
     * Asks the price at which a card must be sold
     *
     * @return
     */
    int askPriceCard();

    /**
     * Asks whihc card to buy in the market among tha availables ones
     *
     * @param cards available cards
     * @param prices prices of those cards
     *
     * @return chose card
     */
    String askBuyMarketCard(String[] cards, int[] prices);

}
