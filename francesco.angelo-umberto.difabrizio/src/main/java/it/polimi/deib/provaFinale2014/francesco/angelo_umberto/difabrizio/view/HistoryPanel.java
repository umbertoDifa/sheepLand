package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import javax.swing.JTextArea;

/**
 *
 * @author Francesco
 */
class HistoryPanel extends JTextArea {

    public HistoryPanel() {
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
