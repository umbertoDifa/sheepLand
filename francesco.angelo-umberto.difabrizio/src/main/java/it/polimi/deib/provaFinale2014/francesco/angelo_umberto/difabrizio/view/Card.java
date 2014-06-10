package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Font;
import javax.swing.JLabel;

/**
 *
 * @author Francesco
 */
public class Card extends CardBoard {

    JLabel bankNum;

    public Card(Font font, String bankNum, String myCardNum) {
        super(font, myCardNum);
        this.bankNum = new JLabel(bankNum);

        this.add(this.bankNum);
        this.bankNum.setBounds(10, 6, 28, 28);
    }

    public void increase(int amountToAdd) {
        //calcolo nuovo ammount
        int newAmmount = Integer.parseInt(this.getText()) + amountToAdd;

        //setto il nuovo ammount
        this.setText(Integer.toString(newAmmount));
    }

}
