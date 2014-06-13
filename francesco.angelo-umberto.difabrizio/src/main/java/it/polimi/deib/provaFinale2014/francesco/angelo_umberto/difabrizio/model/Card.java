package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Rappresenta una carta
 *
 * @author Francesco
 */
public class Card {

    private int value;
    private int marketValue;
    private boolean forSale;
    private boolean initial;

    private final RegionType type;

    /**
     *
     * @return Il tipo di regione della carta
     */
    public RegionType getType() {
        return type;
    }

    /**
     *
     * @return Il valore della carta
     */
    public int getValue() {
        return value;
    }

    public boolean isInitial() {
        return initial;
    }
    
    

    /**
     *
     * @param value Valore da assegnare alla carta
     * @param type  Tipo di carta
     */
    public Card(int value, RegionType type) {
        this(value, type, false);
    }
    /**
     * Create a card
     * @param value value of the card
     * @param type type
     * @param initial if the card is initial
     */
    public Card(int value, RegionType type, boolean initial) {
        this.value = value;
        this.type = type;
        this.initial = initial;

        //inizialmente il valore di mercato è settato al valore della carta
        this.marketValue = this.value;
    }

    /**
     * Setta il valore della carta a value
     *
     * @param value Valore con cui settare il valore della carta
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     *
     * @return Il valore di mercato della carta
     */
    public int getMarketValue() {
        return marketValue;
    }

    /**
     * Imposta il valore di mercato della carta a marketValue
     *
     * @param marketValue Valore da impostare
     */
    public void setMarketValue(int marketValue) {
        this.marketValue = marketValue;
    }

    /**
     * Returns if a card is for sale
     *
     * @return true if it's for sale, false if not
     */
    public boolean isForSale() {
        return forSale;
    }

    /**
     * Setes the for sale variable
     *
     * @param forSale true if the card has to be sold
     */
    public void setForSale(boolean forSale) {
        this.forSale = forSale;
    }

}
