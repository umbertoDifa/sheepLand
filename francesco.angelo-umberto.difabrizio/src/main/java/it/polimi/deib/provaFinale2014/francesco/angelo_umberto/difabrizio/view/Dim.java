package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

/**
 * Dimensions of panels
 * @author Francesco
 */
public enum Dim {

    MAP_BOARD(487, 900),
    NICK_PANEL(140, 100),
    INFO_PANEL(232, 444),
    ACTION(68, 72),
    CARD(105, 104),
    REGION_BOX(52, 78),
    MONEY_PANEL(38, 40),
    PLAYER(200, 99),
    FENCE(78, 94),
    MAIN(900, 800),
    //(68 + 10) * 3, 80
    HISTORY(234, 80), 
    //(68 + 10) * 3, 800
    DX_BAR(234, 800), 
    // 105, (116 + 10) * cardsJPanels.length
    CARD_CONTAINER(105, 756), 
     // 220, (99 + 4) * 4
    PLAYERS_CONTAINER(220, 412),
     // (68 + 10) * 3, (72 + 5) * 2
    ACTIONS_CONTAINER(234, 154),
    STREET(24, 24),
    ANIMAL_PREVIEW(42, 45),
    ANIMAL(68, 68),
    RANK_PANEL(326, 444),
    TEXT_CARD(139, 91),
    TEXT_FENCE(67, 77),
    TEXT_MONEY(20, 40),
    TEXT_NICKNAME(145, 81),
    FONT(28, 28),
    FENCE_POSITION(55, 0),
     //(mainJPanel.getPreferredSize().width / 2.5 - (444 / 2)), mainJPanel.getPreferredSize().height / 2 - (400)
    INFO_PANEL_POSITION(330, 0),
    // mainJPanel.getPreferredSize().width / 2 - (444 / 2)),   mainJPanel.getPreferredSize().height / 2 - (400)
    NICK_PANEL_POSITION(382, 70), 
    RANK_PANEL_POSITION(235, 160),
    MARKET(497,385), 
    MARKET_POSITION(193,65);

    private final int width;
    private final int height;

    Dim(int w, int h) {
        this.width = w;
        this.height = h;
    }

    /**
     * @return Valore corrispondente alla costante in questione
     */
    public int getW() {
        return this.width;
    }
    /**
     * get height
     * @return get height
     */
    public int getH() {
        return this.height;
    }
}
