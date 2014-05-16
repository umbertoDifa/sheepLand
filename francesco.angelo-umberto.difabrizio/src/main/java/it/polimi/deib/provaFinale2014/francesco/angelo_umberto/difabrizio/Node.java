/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio;

import java.util.ArrayList;

/**
 *
 * @author Francesco
 */
abstract class Node { 
    private ArrayList<Node> neighbourNodes;

    public Node() {
        this.neighbourNodes = new ArrayList<Node>();
    }
    /**
     * aggiunge un link bidirezionale tra due nodi
     * @param node 
     */
    public void connectTo(Node node){
        this.addNeighbour(node);
        node.addNeighbour(this);
    }
    /**
     * aggiunge un link monodirezionale verso node
     * @param node 
     */
    private void addNeighbour(Node node){
        this.neighbourNodes.add(node);
    }

    public ArrayList<Node> getNeighbourNodes() {
        return neighbourNodes;
    }
 
    
}
