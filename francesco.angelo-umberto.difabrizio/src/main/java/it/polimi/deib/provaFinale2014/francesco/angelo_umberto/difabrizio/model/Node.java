package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.ArrayList;

/**
 * Rappresenta un nodo del grafo della mappa. Contiene la lista dei nodi vicini.
 * @author Francesco
 */
public abstract class Node { 
    private ArrayList<Node> neighbourNodes;

    public Node() {
        this.neighbourNodes = new ArrayList<Node>();
    }
    /**
     * Aggiunge un link bidirezionale tra due nodi: il chiamante e il parametro
     * @param node Nodo con cui collegarsi
     */
    public void connectTo(Node node){
        this.addNeighbour(node);
        node.addNeighbour(this);
    }
    /**
     * Aggiunge un link monodirezionale dal chiamante verso node
     * @param node Nodo verso cui stabilire un link
     */
    private void addNeighbour(Node node){
        this.neighbourNodes.add(node);
    }
    
    /**
     * 
     * @return La lista dei nodi adiacenti che siano Regioni o Strade
     */
    public ArrayList<Node> getNeighbourNodes() {
        return neighbourNodes;
    }
    
    /**
     * 
     * @return la lista delle strade confinanti a quel nodo, null se vuota
     */
    public ArrayList<Street> getNeighbourStreets() {
        ArrayList<Node> possibleStreets = this.getNeighbourNodes();
        ArrayList<Street> neighbourStreets = new ArrayList<Street>();
        
        for(Node node: possibleStreets){
            if((node instanceof Street))
                neighbourStreets.add( (Street) node);
        }
        return neighbourStreets;
    }
    
    /**
     * 
     * @return la lista delle regioni confinanti a quel nodo, null se vuota
     */
    public ArrayList<Region> getNeighbourRegions() {
        ArrayList<Node> possibleRegions = this.getNeighbourNodes();
        ArrayList<Region> neighbourRegions = new ArrayList<Region>();
        
        for(Node node: possibleRegions){
            if((node instanceof Region))
                neighbourRegions.add( (Region) node);
        }
        return neighbourRegions;
    }
    
    /**
     * Verifica se il chiamante è un nodo connesso col nodo passato nel parametro
     * @param node Nodo con cui verificare la connessione
     * @return True se sono connessi, false altrimenti
     */
    public boolean isNeighbour(Node node){
        return this.getNeighbourNodes().contains(node);
    }
    
}
