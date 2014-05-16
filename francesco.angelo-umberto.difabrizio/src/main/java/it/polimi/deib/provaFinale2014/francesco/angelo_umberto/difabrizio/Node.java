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
    public ArrayList<Node> neighbourNodes;
    
    void connectTo(Node node){
        neighbourNodes.add(node);
    }
}
