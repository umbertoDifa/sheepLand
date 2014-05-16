package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio;


public class Street extends Node {
    private int value;
    private Fence fence;
    private Shepherd shepherd;
    
    /**
     * 
     * @return vero se e solo se la strada è occupata da un recinto
     */
    public boolean hasFence(){
        return (this.fence != null);
    }
    /**
     * 
     * @return vero se e solo se la strada è occupata da un pastore
     */
    public boolean hasSheeherd(){
        return (this.shepherd != null);
    }
    /**
     * 
     * @return vero se e solo è occupata, da un recinto o da un pastore
     */
    public boolean isFree(){
        return !(this.hasFence()||this.hasSheeherd());
    }

}
