package it.polimi.deib.provaFinale2014.francesco.angelo_umberto.difabrizio.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Francesco
 */
public class InfoPanel extends JPanel {

    private final JLabel label;
    private List<Icon> imagesDice = new ArrayList<Icon>();
    private final JLabel dice;
    private int width;
    private int height;
    private Image imageBackground;

    /**
     * create the InfoPanel, setting font, width, height, set imageBackground, save
     * the list of possible background images.
     * @param font
     * @param imagesDice
     * @param imageBackground
     * @param width
     * @param height 
     */
    public InfoPanel(Font font, List<Icon> imagesDice, Image imageBackground, int width, int height) {
        label = new JLabel("é uscito:");
        label.setFont(font);
        this.imagesDice = imagesDice;
        dice = new JLabel();
        this.setLayout(null);
      //  this.add(label, BorderLayout.NORTH);
      //  this.add(dice, BorderLayout.CENTER);
        setDice(1);
        this.width = width;
        this.height = height;
        this.imageBackground = imageBackground;
        MyGui.addComponentsToPane(this, label, 64, 150);
        MyGui.addComponentsToPane(this, dice, 77, 300);
        repaint();
    }

    /**
     * set the Label's text
     * @param text 
     */
    public void setText(String text) {
        label.setText(text);
    }

    /**
     * set the dice's image corrisponding to
     * value "result"
     * @param result 
     */
    public void setDice(int result) {
        dice.setIcon(imagesDice.get(result - 1));
    }

    /**
     * override the paintComponent to set the
     * background image of the panel
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        setOpaque(false);
        if (imageBackground != null) {
            g.drawImage(imageBackground, 0, 0, new Color(0, 0, 0, 0), this);
        }
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(width, height);
    }

}
