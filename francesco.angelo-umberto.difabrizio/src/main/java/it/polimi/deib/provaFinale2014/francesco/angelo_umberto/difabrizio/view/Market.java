package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Francesco
 */
class Market extends BackgroundAndTextJPanel implements MouseListener {

    private JButton button;
    private List<Card> sellCards = new ArrayList<Card>();
    private JLabel label;

    public Market(int width, int height) {
        setUp(".\\images\\market.png", 20, 20, width, height);
        this.setLayout(null);

    }

    public void askIfSellView() {
        this.label = new JLabel("MARKET: vuoi vendere?");
        this.button = new JButton("si");
        this.button.setFont(FontFactory.getFont());
        this.button.setLocation(200, 200);
        this.add(button);
    }

    public void askWhatSellView(String[] availableSellCards) {
        for (String type : availableSellCards) {
            Card card = new Card(FontFactory.getFont(), "", "");
            card.addMouseListener(this);
            card.setUp(".\\images\\" + type.toLowerCase() + "2.png", 30, 30, 60, 60);
            this.add(card); //FIXME posizione
        }
        
        this.button = new JButton("submit");
        this.button.setFont(FontFactory.getFont());
        this.button.setLocation(200, 200);
        this.add(button);
    }

    public void askBuy(List<Card> sellCards) {

    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof Card) {
            Card c = (Card) e.getSource();
            c.increase(1);
        }
    }

    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
