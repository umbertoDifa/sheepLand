package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public enum NetworkConstants {
    
    PORT_RMI(6000),
    PORT_SOCKET(5050),    
    OFFLINE(0),
    ONLINE(1);

    private final int value;

    public int getValue() {
        return value;
    }

    private NetworkConstants(int value) {
        this.value = value;
    }
    

}
