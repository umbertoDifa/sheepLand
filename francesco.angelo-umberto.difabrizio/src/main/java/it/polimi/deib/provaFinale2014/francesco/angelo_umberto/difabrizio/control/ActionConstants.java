package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

/**
 * All the constants in this class are used to give a number value to the
 * possible actions that can be performed by the players. In so doing it's easy
 * to disabilitate some of them in the right moment
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public enum ActionConstants {

    MOVE_OVINE(1),
    MOVE_SHEPHERD(2),
    BUY_LAND(3),
    MATE_SHEEP_WITH_SHEEP(4),
    MATE_SHEEP_WITH_RAM(4),
    KILL_OVINE(6),
    NO_ACTION(-1);

    private final int value;

    private ActionConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
