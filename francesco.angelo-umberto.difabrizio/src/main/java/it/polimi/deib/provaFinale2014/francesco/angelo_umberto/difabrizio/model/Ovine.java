/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 *
 * @author Francesco
 */
public class Ovine {
    private OvineType type;
    
    public Ovine(OvineType type){
        this.type = type;
    }
    /**
     * per default l'oggetto ovino viene creato con un tipo casuale
     */
    public Ovine(){
        this.type = OvineType.getRandomOvineType();
    }

	public OvineType getType() {
		return type;
	}

	public void setType(OvineType type) {
		this.type = type;
	}
}
