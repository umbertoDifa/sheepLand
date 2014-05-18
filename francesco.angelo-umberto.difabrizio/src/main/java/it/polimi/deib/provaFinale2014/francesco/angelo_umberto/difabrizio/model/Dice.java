package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

import java.util.Random;

/**
 *
 * @author francesco.angelo-umberto.difabrizio
 */
public class Dice {

    private static final int DICE_FACES = 6;
    private static final Random dice = new Random();
    
    /**
     * Lancia il dado e torna un valore random tra 1 e 6
     * @return Il valore che esce dal lancio del dado
     */
    public static int getRandomValue() {
        //nextInt mi ritorna un numero tra 0 e 5, aggiungo 1 cosi diventa tra 1 e 6                                             
        return dice.nextInt(DICE_FACES) + 1;
    }
}
