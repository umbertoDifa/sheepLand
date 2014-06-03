package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.control;

/**
 * All the constants in this class are used to give a number value to the
 * possible actions that can be performed by the players. In so doing it's easy
 * to disabilitate some of them in the right moment
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public enum ActionConstants {

    /**
     * Code for the action move ovine
     */
    MOVE_OVINE(1),
    /**
     * Code for the action move shepherd
     */
    MOVE_SHEPHERD(2),
    /**
     * code for the action buy land
     */
    BUY_LAND(3),
    /**
     * code for the action mate sheep with sheep
     */
    MATE_SHEEP_WITH_SHEEP(4),
    /**
     * Code for the action mate sheep with ram
     */
    MATE_SHEEP_WITH_RAM(4),
    /**
     * Code for the action kill ovine
     */
    KILL_OVINE(6),
    /**
     * Code for no action
     */
    NO_ACTION(-1);

    private final int value;

    private ActionConstants(int value) {
        this.value = value;
    }

    /**
     * Returns the code of the action
     *
     * @return Code of the action
     */
    public int getValue() {
        return value;
    }
}
