package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

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
     * @return vero se e solo se la strada è occupata da un recinto
     */
    public boolean hasFence() {
        return (this.fence != null);
    }

    /**
     *
     * @return vero se e solo se la strada è occupata da un pastore
     */
    public boolean hasSheeherd() {
        return (this.shepherd != null);
    }

    /**
     *
     * @return vero se e solo non è occupata da un recinto o da un pastore
     */
    public boolean isFree() {
        return (!this.hasFence() && !this.hasSheeherd());
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
