package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Font;

/**
 *
 * @author Francesco
 */
public class CardBoard extends BackgroundAndTextJPanel {

    public CardBoard(Font font, String text) {
        super(font, text);
    }

    public void decrease(int amountToRemove) {


        //calcolo nuovo ammount
        int newAmmount = Integer.parseInt(this.getText()) - amountToRemove;
        
        //
        if (newAmmount < 0) {
            this.setUp(".\\images\\numFencesRed.png", 67, 77, 78, 94);
        }
        
        //setto il nuovo ammount
        this.setText(Integer.toString(newAmmount));
    }
}
