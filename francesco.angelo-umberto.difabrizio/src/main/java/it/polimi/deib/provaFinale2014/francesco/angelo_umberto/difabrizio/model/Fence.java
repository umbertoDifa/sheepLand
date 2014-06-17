package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Il recinto
 *
 * @author Umberto
 */
public class Fence {

    private final boolean finalFence;

    /**
     * Crea un recinto che può essere finale o non finale
     *
     * @param isFinal True se deve essere finale, false altrimenti
     */
    public Fence(boolean isFinal) {
        this.finalFence = isFinal;
    }

    /**
     *
     * @return True se il recinto è finale, false altrimenti
     */
    public boolean isFinal() {
        return finalFence;
    }

}
