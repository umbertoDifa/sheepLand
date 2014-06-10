package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Font;

/**
 *
 * @author Francesco
 */
public class Card extends BackgroundAndTextJPanel {

    public Card(Font font, String text) {
        super(font, text);
    }

    public void decrease(int amountToRemove) {
        //calcolo nuovo ammount
        int newAmmount = Integer.parseInt(this.getText()) - amountToRemove;
        
        //setto il nuovo ammount
        this.setText(Integer.toString(newAmmount));
    }
}
