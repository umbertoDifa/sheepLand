package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Classe ovino. Può rappresentare una pecora, un montone o un agnello
 *
 * @author Francesco
 */
public class Ovine {

    private OvineType type;

    /**
     * Costruisce un ovino del tipo specificato
     *
     * @param type Tipo di ovino che si vuole
     */
    public Ovine(OvineType type) {
        this.type = type;
    }

    /**
     * Crea un ovino di un tipo a caso
     */
    public Ovine() {
        this.type = OvineType.getRandomOvineType();
    }
    
    /**
     * 
     * @return Il tipo di ovino
     */
    public OvineType getType() {
        return type;
    }
    
    /**
     * Imposta il tipo di ovino
     * @param type Tipo da impostare
     */
    public void setType(OvineType type) {
        this.type = type;
    }
}
