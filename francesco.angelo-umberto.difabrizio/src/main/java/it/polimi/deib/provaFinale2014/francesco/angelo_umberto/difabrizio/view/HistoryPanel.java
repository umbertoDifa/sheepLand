package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import javax.swing.JTextArea;

/**
 *
 * @author Francesco
 */
class HistoryPanel extends JTextArea {

    public HistoryPanel() {
        setText("prova prova prova prova prova prova prova prova prova prova prova prova prova prova prova prova prova prova prova prova");
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    protected void addInHead(String message) {
        this.setText(message + this.getText());
    }

}
