package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.network;

/**
 *
 * @author
 */
public interface TrasmissionController {

    public void broadcastInitialCondition();

    public void broadcastRegion();

    public void refreshAll();

    public int askRegion();

    public String askStreet(String nickName, int idShepherd);
    
    public void sendTo(String nickName, String message);
}
