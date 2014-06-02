package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Classe ovino. Può rappresentare una pecora, un montone o un agnello
 *
 * @author Francesco
 */
public class Ovine {

    private OvineType type;
    private int age;

    /**
     * Costruisce un ovino del tipo specificato
     *
     * @param type Tipo di ovino che si vuole
     */
    public Ovine(OvineType type) {
        this.type = type;
        this.age = 1;
    }

    /**
     * Crea un ovino di un tipo a caso
     */
    public Ovine() {
        this.type = OvineType.getRandomOvineType();
        this.age = 1;
    }

    /**
     *
     * @return Il tipo di ovino
     */
    public OvineType getType() {
        return type;
    }
    /**
     * The method returns the ovine age, it is expecially useful to
     * manage the evolution of lambs
     * @return The ovine age
     */
    public int getAge() {
        return age;
    }
    /**
     * The method sets the ovine age, it is expecially useful to
     * manage the evolution of lambs
     * @param age New age of the lamb
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Imposta il tipo di ovino
     *
     * @param type Tipo da impostare
     */
    public void setType(OvineType type) {
        this.type = type;
    }
}
