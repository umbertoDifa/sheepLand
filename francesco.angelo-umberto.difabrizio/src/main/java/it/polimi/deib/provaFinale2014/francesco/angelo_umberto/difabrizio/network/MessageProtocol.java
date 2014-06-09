package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public enum MessageProtocol {

    WELCOME, REGION, STREET, GAME_PARAMETERS, MONEY, CURRENT_PLAYER,CARD,
    BLACK_SHEEP_REFRESH, MOVE_SHEPHERD_REFRESH, BUY_LAND_REFRESH, MOVE_OVINE_REFRESH,
    MATE_SHEEP_WITH_REFRESH, KILL_OVINE_REFRESH, WOLF_REFRESH,FENCE_REFRESH,
    PLAYER_DISCONNECTED, SPECIAL_ANIMAL_POSITION, SET_UP_SHEPHERD,
    CHOOSE_ACTION, MOVE_OVINE, MOVE_SHEPHERD, BUY_LAND, MATE_SHEEP_WITH,
    KILL_OVINE, UNEXPECTED_END_OF_GAME, SHOW_MY_RANK, CLASSIFICATION;
}
