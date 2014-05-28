package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control.GameData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class CompleteTransferData {

    private final GameData gameData;
    private final LandData landData;

    private List<CardData> cards;

    public CompleteTransferData(GameData gameData, LandData landData) {
        this.gameData = gameData;
        this.landData = landData;
    }

    public void addCard(CardData cardData) {
        cards.add(cardData);
    }

    public void cleanCards() {
        cards.clear();
    }
}
