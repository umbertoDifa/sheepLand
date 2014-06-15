package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Francesco
 */
class Market extends JPanel {

    //bottoni si no
    private final JButton buttonOk = new JButton("si");
    private final JButton buttonKo = new JButton("no");

    //bottoni valori
    private final List<JButton> buttonsPrice;
    
    //carte
    private List<Card> cards;

    //scritta
    private final JLabel label;
    
    //img sfondo
    private final Image image;
    
    //card container
    private final JPanel cardContainer;
    
    private JScrollPane scroll;

/**
 * create market with specified dimension, with a label for text messages,
 * buttons for y/n answer and buttons for price answer.
 * @param width
 * @param height 
 */
    public Market(int width, int height) {
        this.buttonsPrice = new ArrayList<JButton>();
        this.cards = new ArrayList<Card>();
        this.label = new JLabel();
        this.cardContainer = new JPanel();
        this.scroll = new JScrollPane(cardContainer);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.setPreferredSize(new Dimension(Dim.MARKET.getW(), Dim.MARKET.getH()));
        image = ImagePool.getByName("market");
        this.setLayout(null);
        this.add(label);
        this.add(scroll);
        label.setFont(FontFactory.getFont());
        label.setBounds(50, 30, 430, 30);
        scroll.setBounds(100, 100, 70*5, 70*2);
        cardContainer.setPreferredSize(new Dimension(70*5, 70*5));
        cardContainer.setLayout(new FlowLayout());

        this.buttonOk.setFont(FontFactory.getFont());
        this.buttonKo.setFont(FontFactory.getFont());

        for (int i = 0; i < 4; i++) {
            JButton b = new JButton(String.valueOf(i + 1));
            b.setFont(FontFactory.getFont());
            b.setBounds(100 + 70 * i, 160, 60, 60);
            b.setVisible(false);
            b.setEnabled(false);
            buttonsPrice.add(b);
            this.add(b);
        }

        this.buttonOk.setBounds(200, 80, 70, 60);
        this.buttonKo.setBounds(280, 80, 70, 60);

        this.buttonKo.setVisible(false);
        this.buttonOk.setVisible(false);

        this.buttonKo.setEnabled(false);
        this.buttonOk.setEnabled(false);

        this.add(buttonOk);
        this.add(buttonKo);

        this.setVisible(false);
    }

    /**
     * set up the market to get the will to do it or not the action
     * @param action 
     */
    protected void askWillingToView(String action) {
        this.setVisible(true);
        removeOldCards();
        scroll.setVisible(false);

        for (JButton button : buttonsPrice) {
            button.setVisible(false);
            button.setEnabled(false);
        }

        this.label.setText("MARKET: vuoi " + action + "?");
        this.buttonOk.setEnabled(true);
        this.buttonOk.setVisible(true);
        this.buttonKo.setEnabled(true);
        this.buttonKo.setVisible(true);
    }

    /**
     * set up the market to get what cards he wants to sell form the availableSellCards
     * @param availableSellCards 
     */
    protected void askWhatSellView(String[] availableSellCards) {
        this.setVisible(true);
        removeOldCards();
        scroll.setVisible(true);

        this.buttonOk.setVisible(false);
        this.buttonKo.setVisible(false);

        this.label.setText("MARKET: quali carte vuoi vendere?");

        for (int i = 0, j=0; i < availableSellCards.length; i++) {
            Card card = new Card(FontFactory.getFont(), "", "", availableSellCards[i]);
            cards.add(card);
            card.setUp(".\\images\\" + availableSellCards[i].toLowerCase() + "2.png", 30, 30, 60, 60);
            card.setEnabled(true);
            card.setPreferredSize(new Dimension(60, 60));
            cardContainer.add(card);
        }
        scroll.revalidate();
        scroll.repaint();
    }

    /**
     * set up the market to get what cards he wants to buy from the availableCards
     * at the specified price
     * @param availableCards
     * @param prices 
     */
    public void askBuyView(String[] availableCards, int[] prices) {
        this.setVisible(true);
        scroll.setVisible(true);
        
        removeOldCards();

        this.buttonOk.setVisible(false);
        this.buttonKo.setVisible(false);

        this.label.setText("MARKET: quali carte vuoi comprare?");

        for (int i = 0; i < availableCards.length; i++) {
            Card card = new Card(FontFactory.getFont(), "", String.valueOf(prices[i]), availableCards[i]);
            cards.add(card);
            card.setUp(".\\images\\" + availableCards[i].toLowerCase() + "2.png", 30, 30, 60, 60);
            card.setEnabled(true);
            card.setPreferredSize(new Dimension(60, 60));
            cardContainer.add(card);
        }
        scroll.revalidate();
        scroll.repaint();
    }

    /**
     * set up the market to get the price he wants for sale
     */
    public void askPriceView() {
        this.setVisible(true);
        scroll.setVisible(false);
        removeOldCards();
        this.label.setText("MARKET: a quanto la vuoi vendere?");
        for (JButton b : buttonsPrice) {
            b.setVisible(true);
            b.setEnabled(true);
        }

    }

    /**
     * return the button for ok answer
     * @return 
     */
    protected JButton getButtonOk() {
        return buttonOk;
    }

    /**
     * return the button for ko answer
     * @return 
     */
    protected JButton getButtonKo() {
        return buttonKo;
    }

    /**
     * return the list of all cards, to sell or buy
     * @return 
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * get all the prices buttons
     * @return 
     */
    public List<JButton> getPriceButtons() {
        return buttonsPrice;
    }

    /**
     * remove all card to buy or sell
     */
    private void removeOldCards() {
        cards.clear();
        Component[] components = this.cardContainer.getComponents();
        for (Component comp : components) {
            if (comp instanceof Card) {
                cardContainer.remove(comp);
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        if (image != null) {
            g.drawImage(image, 0, 0, Dim.MARKET.getW(), Dim.MARKET.getH(), this);
        }
        super.paintComponent(g);
    }


}
