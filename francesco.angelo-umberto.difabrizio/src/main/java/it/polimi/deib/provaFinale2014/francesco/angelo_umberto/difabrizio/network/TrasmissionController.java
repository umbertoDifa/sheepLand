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

    public int askStreet();
}
