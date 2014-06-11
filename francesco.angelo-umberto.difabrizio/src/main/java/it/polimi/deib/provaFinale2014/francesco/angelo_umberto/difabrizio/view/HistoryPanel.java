package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.JTextArea;

/**
 *
 * @author Francesco
 */
class HistoryPanel extends JTextArea {

    public HistoryPanel() {
        this.setBackground(Color.white);
        setText("Gioco in caricamento:\n");
        setEditable(false);
        setAutoscrolls(false);
        setLineWrap(true);
        setWrapStyleWord(true);

    }

    protected void showToHistoryPanel(String message) {
        append("-");
        append(message);
        append("\n");
    }
}
