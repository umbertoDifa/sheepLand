package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;

public class Region extends Node {
    final private RegionType type;
    private ArrayList<Ovine> myOvines = new ArrayList<Ovine>();
   
    public Region(){
    	super();
        this.type = RegionType.getDefaultRegionType();
    }
    
    public Region(RegionType type){
    	super();
        this.type = type;
    }

    public RegionType getType() {
            return type;
    }

    public ArrayList<Ovine> getMyOvines() {
        return myOvines;
    }
    
    public void addOvine(Ovine ovine){
        this.myOvines.add(ovine);
    }
    
    /**
     * controlla che tutte le strade limitrofe abbiano un recinto
     * @return true se e solo se sono tutte recintate
     */
    public boolean isAllFenced(){
        ArrayList<Node> endStreets = this.getNeighbourNodes();
        for(Node s: endStreets){           //per ogni strada limitrofa
            if(s instanceof Street){       //se è una strada
                Street street = (Street) s; //casto a strada
                if(!street.hasFence()){    //ritorno false se non ha un recinto
                    return false;
                }   
            }
        }
        return true;                    //controllati tutti posso ritornare true
    }
}
