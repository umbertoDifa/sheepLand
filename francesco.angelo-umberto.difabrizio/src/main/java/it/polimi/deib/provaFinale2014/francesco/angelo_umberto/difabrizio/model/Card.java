package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.model;

/**
 * Rappresenta una carta
 * @author Francesco
 */
public class Card {
    private int value;
    private int marketValue;
    
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
    
    /**
     * 
     * @param value Valore da assegnare alla carta
     * @param type Tipo di carta
     */
    public Card(int value, RegionType type) {
        this.value = value;
        this.type = type;
        
        //inizialmente il valore di mercato è settato al valore della carta
        this.marketValue = this.value;
    }
    /**
     * Setta il valore della carta a value
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
     * @param marketValue Valore da impostare
     */
    public void setMarketValue(int marketValue) {
        this.marketValue = marketValue;
    }


}
