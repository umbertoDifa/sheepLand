package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Font;

/**
 * the class extends backgroundAndTextJpanel, adding methods
 * for decrement the jlabel text
 * @author Francesco
 */
public class CardBoard extends BackgroundAndTextJPanel {

    /**
     * create the Cardboard and the Jlabel with the indicated
     * font and text
     * @param font
     * @param text 
     */
    public CardBoard(Font font, String text) {
        super(font, text);
    }

    /**
     * Decrese the jlabel text, changing the image if necessary
     * @param amountToRemove 
     */
    public void decrease(int amountToRemove) {

        //calcolo nuovo ammount
        int newAmmount = Integer.parseInt(this.getText()) - amountToRemove;
        
        //se minore di zero setto l img all'img dei recinti finali
        if (newAmmount < 0) {
            this.setUp(".\\images\\numFencesRed.png", Dim.TEXT_FENCE.getW(), Dim.TEXT_FENCE.getH(), Dim.FENCE.getW(), Dim.FENCE.getH());
        }
        
        //setto il nuovo ammount
        this.setText(Integer.toString(newAmmount));
    }
}
