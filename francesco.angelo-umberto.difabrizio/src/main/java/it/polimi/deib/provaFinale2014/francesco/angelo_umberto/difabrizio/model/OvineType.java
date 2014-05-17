/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.Random;

/**
 *
 * @author Francesco
 */
public enum OvineType {

    SHEEP, RAM, LAMB;

    private static final int size = OvineType.values().length; //size dell'enum cached così non la ricalco ogni volta
    private static final Random random = new Random(); //oggetto random cached

    static OvineType getDefaultOvineType() {
        return SHEEP;
    }

    /**
     * ritorna un tipo di ovino a caso tra quelli disponibili
     *
     * @return OvineType
     */
    static OvineType getRandomOvineType() {
        int choice = random.nextInt(size); //prendi un numero a caso appartenente al totale dei valori dell'enum
        return OvineType.values()[choice]; //ritorno l'ovino corrispondente
    }
}
