package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import javax.swing.JTextArea;

/**
 * The class extends JTextArea adding graphic proprieties
 * and the possibility to add text in the head.
 * @author Francesco
 */
class HistoryPanel extends JTextArea {

    /**
     * create HistoryPanel, set background color, text.
     * Disable editability, autoscroll. set the possibility
     * to start new line if necessary
     */
    public HistoryPanel() {
        this.setBackground(Color.white);
        setText("Gioco in caricamento:\n");
        setEditable(false);
        setAutoscrolls(false);
        setLineWrap(true);
        setWrapStyleWord(true);

    }

    /**
     * add in head to the existing text the message.
     * @param message 
     */
    protected void show(String message) {
        String oldText = this.getText();
        this.setText("-" + message + "\n" + oldText);
    }

}
