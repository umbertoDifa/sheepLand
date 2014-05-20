package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;
/**
 * Rappresenta un nodo Strada della mappa. Può essere recintata o essere occupata
 * da un pastore. Ha un valore da 1 a 6. Attenzione potenzialmente la classe accetta
 * un qualsiasi intero come valore.
 * @author Umberto
 */
public class Street extends Node {

    private int value;
    private Fence fence;
    private Shepherd shepherd;

    public Street(int value) {
        super();
        this.value = value;
    }

    /**
     *
     * @return Vero se e solo se la strada è occupata da un recinto
     */
    public boolean hasFence() {
        return (this.fence != null);
    }

    /**
     *
     * @return Vero se e solo se la strada è occupata da un pastore
     */
    public boolean hasShepherd() {
        return (this.shepherd != null);
    }

    public void setFence(Fence fence) {
        this.fence = fence;
    }
    
    public void setShepherd(Shepherd shepherd){
        this.shepherd = shepherd;
    }

    /**
     *
     * @return Vero se e solo non è occupata da un recinto o da un pastore
     */
    public boolean isFree() {
        return (!this.hasFence() && !this.hasShepherd());
    }
    
    /**
     * Ottieni il valore della strada
     * @return Il valore della strada
     */
    public int getValue() {
        return value;
    }
   
    /**
     * Dato un pastore shepherd dice se si trova o no su quella strada
     * @param shepherd Pastore di cui controllare la posizione sulla strada
     * @return True se il pastore è sulla strada, false altrimenti
     */
    
    //TODO: ridondante, usare la strada che fornisce il giocatore
    public boolean isShepherdThere(Shepherd shepherd){
        return shepherd == this.shepherd;
    }

}
