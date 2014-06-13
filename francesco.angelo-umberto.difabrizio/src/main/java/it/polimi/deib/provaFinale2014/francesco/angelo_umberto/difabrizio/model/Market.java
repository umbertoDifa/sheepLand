package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Market {

    private final int playersNumber;
    private List<List<Card>> playersThatSellCards;

    public Market(int playersNumber) {
        this.playersNumber = playersNumber;

        playersThatSellCards = new ArrayList<List<Card>>();

        for (int i = 0; i < playersNumber; i++) {
            List<Card> cardsToBeSold = new ArrayList<Card>();
            playersThatSellCards.add(cardsToBeSold);
        }
    }

    /**
     * Add a card in the sellable cards of an owner
     *
     * @param cardToAdd  Card to add
     * @param indexOwner owner of teh card
     */
    public void addCard(Card cardToAdd, int indexOwner) {
        playersThatSellCards.get(indexOwner).add(cardToAdd);
    }

    /**
     * Remove a card from teh sellable card of the given owner
     *
     * @param cardToRemove Card to remove
     * @param indexOwner   owner of the card
     *
     * @return true if the element was removed, false if not
     */
    public boolean removeCard(Card cardToRemove, int indexOwner) {
        return playersThatSellCards.get(indexOwner).remove(cardToRemove);
    }

    /**
     * Removes all the forSale flags on the cards and removes all those cards
     * from the market
     */
    public void clear() {
        //tolgo is flag for sale dalle carte rimaste
        for (int i = 0; i < playersThatSellCards.size(); i++) {
            for (int j = 0; j < playersThatSellCards.get(i).size(); j++) {
                playersThatSellCards.get(i).get(j).setForSale(false);
            }
        }

        playersThatSellCards.clear();

        for (int i = 0; i < playersNumber; i++) {
            List<Card> cardsToBeSold = new ArrayList<Card>();
            playersThatSellCards.add(cardsToBeSold);
        }
    }

    /**
     * given a type of card returns the card of that type with the lowest price
     *
     * @param type type of region of the card
     *
     * @return the card if there's a card of that type, null if no card exists
     */
    public Card getCard(RegionType type) {
        Card cardToReturn = null;
        for (int i = 0; i < playersThatSellCards.size(); i++) {
            for (int j = 0; j < playersThatSellCards.get(i).size(); j++) {
                if (playersThatSellCards.get(i).get(j).getType() == type) {
                    DebugLogger.println("Carta trovata nel player " + i);

                    if (cardToReturn != null) {
                        if (cardToReturn.getMarketValue() > playersThatSellCards.get(
                                i).get(j).getMarketValue()) {
                            DebugLogger.println("Carta migliore trovata");

                            cardToReturn = playersThatSellCards.get(i).get(j);
                        }
                    } else {
                        cardToReturn = playersThatSellCards.get(i).get(j);
                    }
                }
            }
        }

        return cardToReturn;
    }

    /**
     * Given a card in the market, the method returns its owner index, or -1 if
     * teh owner of the card is not in the market
     *
     * @param card Card whose owner you want to find
     *
     * @return index of the owner
     */
    public int getOwnerByCard(Card card) {
        for (int i = 0; i < playersThatSellCards.size(); i++) {
            for (int j = 0; j < playersThatSellCards.get(i).size(); j++) {
                if (playersThatSellCards.get(i).get(j) == card) {
                    return i;
                }
            }
        }
        return -1;
    }
}
