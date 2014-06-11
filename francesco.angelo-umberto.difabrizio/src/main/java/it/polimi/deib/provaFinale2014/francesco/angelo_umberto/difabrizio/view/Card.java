package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.utility.DebugLogger;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

/**
 *
 * @author Francesco
 */
public class Card extends CardBoard implements MouseListener {

    JLabel bankNum;

    public Card(Font font, String bankNum, String myCardNum) {
        super(font, myCardNum);
        this.bankNum = new JLabel(bankNum);
        this.bankNum.setFont(FontFactory.getFont());
        this.bankNum.setForeground(Color.white);
        this.setBackground(new Color(0, 0, 0, 0));
        this.add(this.bankNum);
        this.bankNum.setBounds(10, 6, 28, 28);
        this.addMouseListener(this);
    }

    public void increase(int amountToAdd) {
        //calcolo nuovo ammount
        int newAmmount = Integer.parseInt(this.getText()) + amountToAdd;

        //setto il nuovo ammount
        this.setText(Integer.toString(newAmmount));
    }

    public void mouseClicked(MouseEvent e) {
        DebugLogger.println("carta terreno clickata, dentro la catch dell evento");
        repaint();
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void mouseExited(MouseEvent e) {
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
