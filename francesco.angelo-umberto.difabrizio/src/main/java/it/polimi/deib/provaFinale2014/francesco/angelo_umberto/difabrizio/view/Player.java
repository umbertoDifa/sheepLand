package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

/**
 *
 * @author Francesco
 */
class Player extends BackgroundAndTextJPanel implements MouseListener{

    private JLabel moneyText;

    public Player(String text, Font font) {
        super(font, text);
        super.setOpacity(true);
    }

    public void setUp(String imgPath1, String imgPath2, int xText1, int yText1, int xText2, int yText2, int width, int height) {
        super.setUp(imgPath1, xText1, yText1, width, height);
        MoneyPanel moneyPanel = new MoneyPanel();
        moneyText = new JLabel("0");
        moneyText.setFont(font);
        moneyText.setForeground(new Color(64, 64, 64));
        moneyPanel.add(moneyText);
        moneyPanel.setUp(imgPath2, 38, 40);
        moneyPanel.setBackground(new Color(0, 0, 0, 0));
        this.setLayout(null);
        GuiView.addComponentsToPane(this, moneyPanel, xText2, yText2);
    }
    
    public void setAmount(int amount){
        moneyText.setText(String.valueOf(amount));
    }
    
    public void pay(int price){
        moneyText.setText(String.valueOf(Integer.parseInt(moneyText.getText())-price));
    }
    
    protected void isYourShift(){
        super.setOpacity(false);
    }
    
    protected void isNotYourShift() {
        super.setOpacity(true);
    }
    
    public void mouseClicked(MouseEvent e) {
        isYourShift();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }


}
