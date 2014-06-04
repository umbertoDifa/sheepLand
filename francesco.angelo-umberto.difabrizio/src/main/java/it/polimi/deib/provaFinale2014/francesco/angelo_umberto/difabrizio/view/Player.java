package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Francesco
 */
class Player extends BackgroundAndTextJPanel {

    private JTextArea moneyText;

    public Player(String text, Font font) {
        super(font, text);
    }

    public void setUp(String imgPath1, String imgPath2, int xText1, int yText1, int xText2, int yText2, int width, int height) {
        super.setUp(imgPath1, xText1, yText1, width, height);
        MoneyPanel moneyPanel = new MoneyPanel();
        JLabel moneyText = new JLabel("20");
        moneyText.setFont(font);
        moneyText.setForeground(new Color(64, 64, 64));
        moneyPanel.add(moneyText);
        moneyPanel.setUp(imgPath2, 38, 40);
        moneyPanel.setBackground(new Color(0, 0, 0, 0));

        //    moneyLabel.setPreferredSize(new Dimension(38, 40));
        this.setLayout(null);
        MyGui.addComponentsToPane(this, moneyPanel, xText2, yText2);
    }

    public void setAmount(int amount){
        moneyText.setText(String.valueOf(amount));
    }
}
